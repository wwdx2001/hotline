package com.sh3h.hotline.ui.multimedia;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.sh3h.hotline.R;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 自定义录音控件
 * Created by limeng on 2016/8/26.
 */
public class VoiceView extends RadioButton implements View.OnTouchListener {
    /**
     * 录音回调接口
     */
    public interface VoiceListener {
        void startRecord(boolean isFull);

        /**
         * 录音结束的回调
         *
         * @param fileName 保存的文件名
         * @param time     录音时长
         */
        void endRecord(String fileName, long time);
    }

    private static final String TAG = "VoiceView";
    //最快每隔0.2秒刷新一下界面
    private static final int MIN_REFRESH_TIME = 100;
    private static final int REPEAT_NUMBER = 300;
    //控制录音最长时间
    private static final int MAX_RECORD_TIME = 60 * 1000;
    //把声音分为六个等级的跨度值
    private static final int STEP_RECORD_VALUE = 1600;
    //录音的保存格式
    private static final String RECORD_FORMAT = ".3gp";
    //录音类
    private MediaRecorder mMediaRecorder;
    //录音文件名
    private String mMultiMediaName;
    //录音时长
    private long mRecordLength;
    //保存路径
    private String mPath;
    //回调
    private VoiceListener mVoiceListener;
    //上下文环境
    private Context mContext;

    private Subscription mSubscription;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    //显示声音大小的图片
    private ImageView mImageView;
    private boolean isImageViewShow;
    //刷新时间
    private boolean isRecording;
    private boolean isFull;

    public VoiceView(Context context) {
        super(context);
        this.isImageViewShow = false;
        this.isRecording = false;
        this.isFull = false;
    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.isImageViewShow = false;
        this.isRecording = false;
        this.isFull = false;
        TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.VoiceView);
        int defaultSize = typed.getLayoutDimension(R.styleable.VoiceView_voiceMax, Integer.MAX_VALUE);
        typed.recycle();
        setOnTouchListener(this);
        initParams();
    }

    /**
     * onInterceptTouchEvent()基本的规则是：
     * down事件首先会传递到onInterceptTouchEvent()方法
     * 如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return false，
     * 那么后续的move, up等事件将继续会先传递给该ViewGroup，之后才和down事件一样传递给最终的目标view的onTouchEvent()处理。
     * 如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return true，
     * 那么后续的move, up等事件将不再传递给onInterceptTouchEvent()，而是和down事件一样传递给该ViewGroup的onTouchEvent()处理，
     * 注意，目标view将接收不到任何事件。
     * 如果最终需要处理事件的view的onTouchEvent()返回了false，那么该事件将被传递至其上一层次的view的onTouchEvent()处理。
     * 如果最终需要处理事件的view 的onTouchEvent()返回了true，那么后续事件将可以继续传递给该view的onTouchEvent()处理。
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown();
                break;
            case MotionEvent.ACTION_UP:
                actionUp();
                break;
            case MotionEvent.ACTION_CANCEL:
                actionCancel();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * @param path 设置录音的保存路径
     */
    public void setOutputPath(String path) {
        this.mPath = path;
    }

    /**
     * @param voiceListener 设置监听
     */
    public void setOnVoiceListener(VoiceListener voiceListener) {
        this.mVoiceListener = voiceListener;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public void release() {
        LogUtil.i(TAG, "release");

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        hideImageView();
        releaseRecorder();
    }

    /**
     * 按下录音
     */
    private void actionDown() {
        LogUtil.i(TAG, "actionDown");

        release();
        if (isFull) {
            if (mVoiceListener != null) {
                mVoiceListener.startRecord(true);
            }
            return;
        } else {
            if (mVoiceListener != null) {
                mVoiceListener.startRecord(false);
            }
        }

        mRecordLength = System.currentTimeMillis();
        showImageView();
        mSubscription = Observable.create(
                new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        if (subscriber.isUnsubscribed()) {
                            LogUtil.i(TAG, "actionDown isUnsubscribed");
                            return;
                        }

                        if (prepareRecorder()) {
                            LogUtil.i(TAG, "actionDown: prepareRecorder is success");
                            isRecording = true;
                            subscriber.onNext(true);
                        } else {
                            LogUtil.i(TAG, "actionDown: prepareRecorder is failure");
                            releaseRecorder();
                            subscriber.onNext(false);
                        }

                        subscriber.onCompleted();
                    }
                })
                .concatMap(new Func1<Boolean, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Boolean aBoolean) {
                        return Observable.timer(MIN_REFRESH_TIME, TimeUnit.MILLISECONDS).repeat(REPEAT_NUMBER);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "actionDown onCompleted");
                        hideImageView();
                        releaseRecorder();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "actionDown onError " + e.getMessage());
                        hideImageView();
                        releaseRecorder();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtil.i(TAG, "actionDown onNext");
                        if (mMediaRecorder != null) {
                            refreshImageView(mMediaRecorder.getMaxAmplitude() / STEP_RECORD_VALUE);
                        }
                    }
                });
    }

    /**
     * 取消录音
     */
    private void actionCancel() {
        LogUtil.i(TAG, "actionCancel");
        if (isFull) {
            LogUtil.i(TAG, "actionCancel full");
            return;
        }

        release();
    }

    /**
     * 松开录音
     */
    private void actionUp() {
        LogUtil.i(TAG, "actionUp");
        if (isFull) {
            LogUtil.i(TAG, "actionUp full");
            return;
        }

        release();

        File file = new File(mPath, mMultiMediaName);
        mRecordLength = System.currentTimeMillis() - mRecordLength;
//        if (mRecordLength < 500) {
//            ApplicationsUtil.showMessage(mContext, "时间太短，请长按");
//            if (file.exists()) {
//                file.delete();
//            }
//            return;
//        }

        if (mVoiceListener != null) {
            mVoiceListener.endRecord(mMultiMediaName, mRecordLength);
        }

//        if (getFileSizes(file) > 0) {
//            if (mVoiceListener != null) {
//                String path = mPath + File.separator + mMultiMediaName;
//                mVoiceListener.endRecord(path, mMultiMediaName, mRecordLong);
//            }
//        } else {
//            ApplicationsUtil.showMessage(mContext, "请检查是不是已经禁止录音了");
//        }
    }

    /**
     * @param f 文件
     * @return 文件大小
     */
    private long getFileSizes(File f) {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                s = fis.available();
                if (s == 0) {
                    f.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    /**
     * 获取windowManager，并设置相关参数
     */
    private void initParams() {
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 设置LayoutParams(全局变量）相关参数
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        //透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.gravity = Gravity.CENTER;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 显示图片
     */
    private void showImageView() {
        if (mImageView == null) {
            mImageView = new ImageView(mContext);
            mImageView.setImageResource(R.mipmap.bg_recording1);
        }

        mWindowManager.addView(mImageView, mParams);
        isImageViewShow = true;
    }

    /**
     * 隐藏图片
     */
    private void hideImageView() {
        if ((mImageView != null) && isImageViewShow) {
            mWindowManager.removeView(mImageView);
        }

        isImageViewShow = false;
    }

    /**
     * 刷新声音大小
     *
     * @param value 声音大小
     */
    private void refreshImageView(int value) {
        if ((mImageView == null) || (!isImageViewShow)) {
            return;
        }

        int id;
        switch (value) {
            case 0:
                id = R.mipmap.bg_recording1;
                break;
            case 1:
                id = R.mipmap.bg_recording2;
                break;
            case 2:
                id = R.mipmap.bg_recording3;
                break;
            case 3:
                id = R.mipmap.bg_recording4;
                break;
            case 4:
                id = R.mipmap.bg_recording5;
                break;
            default:
                id = R.mipmap.bg_recording6;
                break;
        }

        mImageView.setImageResource(id);
    }

    private boolean prepareRecorder() {
        try {
            if (TextUtil.isNullOrEmpty(mPath)) {
                LogUtil.e(TAG, "path is null");
                return false;
            }

            File folder = new File(mPath);
            if (!folder.exists() && !folder.mkdirs()) {
                LogUtil.e(TAG, "failure to create the folder");
                return false;
            }

            mMultiMediaName = mRecordLength + RECORD_FORMAT;
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 设置输出文件格式
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// 设置编码格式
            mMediaRecorder.setOutputFile(mPath + File.separator + mMultiMediaName);// 使用绝对路径进行保存文件
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isRecording = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.getMessage());
            isRecording = false;
            return false;
        }
    }

    private void releaseRecorder() {
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.stop();  // stop the recording
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.getMessage());
            }

            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

        isRecording = false;
    }

}
