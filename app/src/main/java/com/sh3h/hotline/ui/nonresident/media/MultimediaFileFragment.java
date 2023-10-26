package com.sh3h.hotline.ui.nonresident.media;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.response.DUFileRemote;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.collection.CollectionTaskOrderBackfillingActivity;
import com.sh3h.hotline.ui.multimedia.MultimediaMvpView;
import com.sh3h.hotline.ui.multimedia.MultimediaPresenter;
import com.sh3h.hotline.ui.multimedia.PictureAdapter;
import com.sh3h.hotline.ui.multimedia.PictureDetailsActivity;
import com.sh3h.hotline.ui.multimedia.PlayVideoActivity;
import com.sh3h.hotline.ui.multimedia.RecordAdapter;
import com.sh3h.hotline.ui.multimedia.VoiceView;
import com.sh3h.hotline.ui.multimedia.signup.SignView;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MultimediaFileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultimediaFileFragment extends ParentFragment implements MultimediaFileMvpView,
        View.OnClickListener, VoiceView.VoiceListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    @Inject
    MultimediaFilePresenter mMultimediaPresenter;

    @BindView(R.id.multimedia_scrollview)
    ScrollView mScrollView;

    @BindView(R.id.multimedia_take_photo_ll)
    LinearLayout mTakePhoto;

    @BindView(R.id.multimedia_gv_picture)
    GridView mGridImages;

    @BindView(R.id.multimedia_gv_video)
    GridView mGridVideos;

    @BindView(R.id.multimedia_take_voice_voiceview)
    VoiceView mVoiceView;

    @BindView(R.id.multimedia_take_video_ll)
    LinearLayout mTakeVideo;

    @BindView(R.id.multimedia_lv_record)
    GridView mListViewRecords;

    @BindView(R.id.multimedia_take_voice_ll)
    LinearLayout mTakeVoice;//录音

    @Inject
    Bus mEventBus;

    @Inject
    EventPosterHelper mEventPosterHelper;

    private final static String TAG = "MultimediaFileFragment";
    private final static int PHOTOS_FULL_SIZE = 3;//照片最大容量
    private final static int VOICES_FULL_SIZE = 1;//录音最大容量
    private final static int VIDEOS_FULL_SIZE = 1;//视频最大容量
    private final static int VIDEO_MAX = 30000;//视频最大时长
    private final static int VIDEO_MIN = 3000;//视频最短时长
    private static final int CAPTURE_IMAGE = 1000;
    private static final int CAPTURE_ALBUM = 1001;
    public static final int SNAP_VIDEO = 2001;

    private Unbinder mUnbinder;

    public PictureAdapter mPictureAdapter; //照片适配器
    public RecordAdapter mRecordAdapter;
    public PictureAdapter mVideosAdapter;//录像适配器
    private List<DUMedia> mPictureList = new ArrayList<>(); // 照片集合
    private List<DUMedia> mVoiceList = new ArrayList<>(); // 录音集合
    private List<DUMedia> mVideoList = new ArrayList<>();//录像集合
    private String mImageName;

    private String taskId;
    private int taskType;
    private int taskState;
    private boolean mIsUploadFlag;

    private int mOrigin;
    private ArrayList<DUFileRemote> mDuFileRemotes;
    Recorder mRecorder;

    private ImageButton mStartRecordIB;//开始录音
    private ImageButton mStopRecordIB;//结束录音
    private TextView mTextmRecordCount;//录音计时
    private String mVoiceFileName;//录音文件名称
    private long mRecordLength;//录音文件长度
    private boolean mIsVoiceFull;//录音是否Full
    AlertDialog mRecordDialog;//录音弹出框
    private int mRecordCount = 0;
    private static long MAX_RECORD_LENGTH = 2 * 60; //录音最大时长
    private Runnable mRunnable;
    Handler mHandler = new Handler();
    private static final int MESSAGE_STOP_COUNT = 1;
    private PopupWindow mPopwindow;
    private DUMyTask mDuMyTask;
    private String mNewPath;
    private String currentTime = "";
    private boolean mIsDataUpload;

    public MultimediaFileFragment() {
    }

    public static MultimediaFileFragment newInstance(String param1, int param2, int param3) {
        MultimediaFileFragment fragment = new MultimediaFileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        mEventBus.register(this);

        if (getArguments() != null) {
            taskId = getArguments().getString(ARG_PARAM1);
            taskType = getArguments().getInt(ARG_PARAM2);
            taskState = getArguments().getInt(ARG_PARAM3);
            mOrigin = getArguments().getInt(Constant.ORIGIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multimedia_file, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mMultimediaPresenter.attachView(this);
        initView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        mVoiceView.release();
        mUnbinder.unbind();
        mMultimediaPresenter.detachView();
        mEventBus.unregister(this);
        LogUtil.i(TAG, "onDestroy");
    }

    @Override
    protected void lazyLoad() {

    }

    public int getFileSize() {
        return mPictureList.size() + mVideoList.size() + mVoiceList.size();
    }

    public int getPictureSize() {
        return mPictureList.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.multimedia_take_photo_ll:
                if (!isNeedFufwdBH())
                    choosePictures();
                break;
            case R.id.multimedia_take_voice_ll:
                if (!isNeedFufwdBH())
                    takeVoice();
                break;
            case R.id.dialog_btn_start_record:
                startTakeRecord();
                break;
            case R.id.dialog_btn_stop_record:
                stopTakeRecord();
                break;
            case R.id.button_take_photo:
                takePhoto(false);
                break;
            case R.id.button_choose_photo:
                takePhoto(true);
                break;
            case R.id.button_choose_cancel:
                dismissPop();
                break;
            default:
                break;
        }
    }

    private boolean isNeedFufwdBH() {
        if (taskType == Constant.TASK_TYPE_CREATE_SELF_ORDER &&
                mActivity instanceof CreateSelfOrderActivity) {
            //自开单，先判断有没有填写服务点编号
            if (StringUtils.isEmpty(((CreateSelfOrderActivity) mActivity).mFuwudianBH)) {
                ToastUtils.showShort("请先填写服务点编号");
                return true;
            }
        }
        return false;
    }

    private void dismissPop() {
        if (mPopwindow != null && mPopwindow.isShowing()) {
            mPopwindow.dismiss();
        }
    }

    private void choosePictures() {

        //判断照片是否已满
        if (mPictureList.size() >= PHOTOS_FULL_SIZE) {
            ToastUtils.showShort(
                    R.string.text_pictures_full);
            return;
        }
        View popupView = getActivity().getLayoutInflater().inflate(R.layout.pop_choose_picture, null);
        Button takePhoto = (Button) popupView.findViewById(R.id.button_take_photo);
        Button pickPhoto = (Button) popupView.findViewById(R.id.button_choose_photo);
        Button cancel = (Button) popupView.findViewById(R.id.button_choose_cancel);
//        mPopwindow = new PopupWindow(popupView, mMainLayout.getWidth(),
//                600);
        mPopwindow = new PopupWindow(popupView,
                ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        mPopwindow.setAnimationStyle(R.style.txt_camera_pop_menu);
        mPopwindow.setFocusable(true);
//        mPopwindow.setBackgroundDrawable(new ColorDrawable());
        mPopwindow.showAtLocation(mScrollView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        takePhoto.setOnClickListener(this);
        pickPhoto.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @OnClick(R.id.multimedia_take_video_ll)
    public void takeVideo() {
        if (mIsDataUpload) {
            return;
        }
        if (!isNeedFufwdBH()) {
            //判断视频是否已满
            if (mVideoList.size() >= VIDEOS_FULL_SIZE) {
                ToastUtils.showShort(
                        R.string.text_videos_full);
                return;
            }
            AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder()
                    .setResolutionMode(AliyunSnapVideoParam.RESOLUTION_540P) //录制 设置录制分辨率，目前支持360p，480p，540p，720p
                    .setRatioMode(AliyunSnapVideoParam.RATIO_MODE_3_4)  //录制 设置视频比例，目前支持1:1,3:4,9:16
                    .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO) //录制 设置录制模式，目前支持按录，点录和混合模式
                    //  .setFilterList(eff_dirs)  //录制 设置滤镜地址列表,具体滤镜接口接收的是一个滤镜数组
                    // .setBeautyLevel(beautyLevel)   //录制 设置美颜度
                    .setBeautyStatus(false) //录制 设置美颜开关
                    .setCameraType(CameraType.BACK) //录制 设置前后置摄像头
                    //.setFlashType(flashType) //录制 设置闪光灯模式
                    .setNeedClip(false) //录制 设置是否需要支持片段录制
                    .setMaxDuration(VIDEO_MAX) //录制 设置最大录制时长 单位毫秒
                    .setMinDuration(VIDEO_MIN) //录制 设置最小录制时长 单位毫秒
                    .setVideoQuality(VideoQuality.HD) //录制设置视频质量
                    .setGop(5) //设置关键帧间隔
                    //   .setVideoBitrate(2000) //录制设置视频码率，如果不设置则使用视频质量videoQulity参数计算出码率
                    //.setMinVideoDuration(minVideoDuration) //裁剪 设置过滤的视频最小长度 单位毫秒
                    //.setMaxVideoDuration(maxVideoDuration) //裁剪 设置过滤的视频最大长度 单位毫秒
                    //.setMinCropDuration(minCropDuration) //裁剪 设置视频最小裁剪时间 单位毫秒
                    //.setFrameRate(frameRate) //裁剪 设置帧率
                    //.setNeedRecord(needRecord)//设置是否需要开放录制入口
                    //.setResulutionMode(resulutionMode) //设置分辨率，目前支持360p，480p，540p，720p
                    //.setCropMode(cropMode) //设置裁剪方式，是否使用gpu进行裁剪，不设置则默认使用cpu来裁剪
                    //.setSortMode(AliyunSnapVideoParam.SORT_MODE_VIDEO) //设置导入相册过滤选择视频
                    .build();
            AliyunVideoRecorder.startRecordForResult(getActivity(), SNAP_VIDEO, recordParam);
        }
    }

    /**
     * 开始录音
     */
    private void startTakeRecord() {
        LogUtil.i(TAG, "startTakeRecord");
        mStopRecordIB.setEnabled(true);
        mStartRecordIB.setEnabled(false);
//        mVoiceFileName = String.format("%s.wav",
//                TextUtil.format(MainApplication.get(mActivity).getCurrentDate(),
//                        TextUtil.FORMAT_DATE_SECOND));
        if (taskType == Constant.TASK_TYPE_CREATE_SELF_ORDER &&
                mActivity instanceof CreateSelfOrderActivity) {
            //自开单，先判断有没有填写服务点编号
            currentTime = ((CreateSelfOrderActivity) mActivity).currentTime;
            if (StringUtils.isEmpty(((CreateSelfOrderActivity) mActivity).mFuwudianBH)) {
                ToastUtils.showShort("请先填写服务点编号");
                return;
            } else {
                mVoiceFileName = "RED_" + ((CreateSelfOrderActivity) mActivity).mFuwudianBH + "_" + System.currentTimeMillis() + ".wav";
            }
        } else {
            mVoiceFileName = "RED_" + taskId + "_" + System.currentTimeMillis() + ".wav";
        }

        try {
            mRecorder = OmRecorder.wav(
                    new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                        @Override
                        public void onAudioChunkPulled(AudioChunk audioChunk) {

                        }
                    }), new File(mMultimediaPresenter.getSoundPath(), mVoiceFileName));
            mRecordLength = System.currentTimeMillis();
            mRecorder.startRecording();
            mRunnable = new Runnable() {
                @Override
                public void run() {

                    if (mRecordCount == MAX_RECORD_LENGTH) {//限时两分钟
                        stopTakeRecord();
                    }

                    mRecordCount++;
                    mTextmRecordCount.setText("录音中:" + mRecordCount + "s");
                    mHandler.postDelayed(this, 1000);//每一秒刷新一次
                }
            };
            mRunnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PullableSource mic() {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 44100
                )
        );
    }


    /**
     * 结束录音
     */
    private void stopTakeRecord() {
        LogUtil.i(TAG, "stopTakeRecord");
        mRecordLength = System.currentTimeMillis() - mRecordLength;
        try {
            mRecorder.stopRecording();
            mRecordCount = 0;
            if (mRunnable != null) {
                Message message = new Message();
                message.what = MESSAGE_STOP_COUNT;
                handlerStop.sendMessage(message);
            }
            mRecordDialog.dismiss();
            mMultimediaPresenter.saveNewVoice(taskId + currentTime, taskType, taskState, mVoiceFileName, mRecordLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 录音
     */
    private void takeVoice() {
        if (mIsVoiceFull) {
            ToastUtils.showShort(R.string.text_record_full);
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_take_voice, null);
        mStartRecordIB = (ImageButton) view.findViewById(R.id.dialog_btn_start_record);
        mStopRecordIB = (ImageButton) view.findViewById(R.id.dialog_btn_stop_record);
        mTextmRecordCount = (TextView) view.findViewById(R.id.dialog_tv_record_count);
        mStartRecordIB.setOnClickListener(this);
        mStopRecordIB.setOnClickListener(this);
        mStopRecordIB.setEnabled(false);
        builder.setView(view);
        builder.setTitle(R.string.dilaog_start_record_title);
        builder.setNegativeButton(R.string.text_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mStopRecordIB.isEnabled()) {
                    stopTakeRecord();
                }
            }
        });
        builder.setCancelable(false);
        mRecordDialog = builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dismissPop();
        if ((CAPTURE_IMAGE == requestCode)
//                && (data != null)
                && (mImageName != null)) {
            File folder = mMultimediaPresenter.getImagePath();
            File file = new File(folder, mImageName);
            if (!file.exists()) {
                LogUtil.e(TAG, String.format("%s isn't existing", mImageName));
                return;
            }
            //保存照片-压缩
//            mMultimediaPresenter.saveNewImage(taskId, taskType, taskState, mImageName, file, false, null);
            LogUtils.e("taskId", taskId + currentTime);
            boolean isHighQulity = false;
            String address = "";
            if (mOrigin == 11) {
                isHighQulity = true;
                address = ((CollectionTaskOrderBackfillingActivity) getActivity()).getAddress();
                mMultimediaPresenter.saveNewImage(taskId + currentTime, taskType, taskState, mImageName, file, false, null, isHighQulity, address);
            } else {
                mMultimediaPresenter.saveNewImage(taskId + currentTime, taskType, taskState, mImageName, file, false, null, isHighQulity);
            }

        } else if (requestCode == CAPTURE_ALBUM
                && data != null) {
            Uri originalUri = data.getData();
            LogUtil.i(TAG, "save albums");
            ContentResolver resolver = getActivity().getContentResolver();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                File file = new File(mMultimediaPresenter.getImagePath(), mImageName);
                //保存照片-压缩
                boolean isHighQulity = false;
//                String address = "";
                if (mOrigin == 11) {
                    isHighQulity = true;
//                    address = ((CollectionTaskOrderBackfillingActivity) getActivity()).getAddress();
//                    mMultimediaPresenter.saveNewImage(taskId + currentTime, taskType, taskState, mImageName, file, true, bm, isHighQulity, address);
                }
//                else {
                mMultimediaPresenter.saveNewImage(taskId + currentTime, taskType, taskState, mImageName, file, true, bm, isHighQulity);
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startRecord(boolean isFull) {
        LogUtil.i(TAG, "startRecord");
        if (isFull) {
            ToastUtils.showShort(R.string.text_record_full);
        }
    }

    @Override
    public void endRecord(String fileName, long time) {
        if (!TextUtil.isNullOrEmpty(fileName)) {
            LogUtil.i(TAG, String.format(Locale.CHINESE,
                    "endRecord [fileName: %s, time: %d]", fileName, time));
            mMultimediaPresenter.saveNewVoice(taskId + currentTime, taskType, taskState, fileName, time);
        } else {
            LogUtil.e(TAG, "the file name is null");
        }
    }

    @Override
    public void onLoadImages(List<DUMedia> duMediaList) {
        if ((duMediaList == null) || (duMediaList.size() <= 0)) {
            mPictureList.clear();
        } else {
            mPictureList = duMediaList;
        }

        mPictureAdapter.setData(mPictureList);
    }

    @Override
    public void onLoadVoices(List<DUMedia> duMediaList) {
        if ((duMediaList == null) || (duMediaList.size() <= 0)) {
            mVoiceList.clear();
        } else {
            mVoiceList = duMediaList;
            if (duMediaList.size() >= VOICES_FULL_SIZE) {
                mIsVoiceFull = true;
                mVoiceView.setFull(true);
            } else {
                mIsVoiceFull = false;
                mVoiceView.setFull(false);
            }
        }

        mRecordAdapter.setData(mVoiceList);
    }

    @Override
    public void onSaveNewImage(Boolean aBoolean) {
        if (aBoolean) {
            ToastUtils.showShort(R.string.text_save_success);
//            mMultimediaPresenter.loadImages(taskId, taskType, taskState);
            mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState);
            mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
        } else {
            ToastUtils.showShort(R.string.text_save_fail);
        }
    }

    @Override
    public void onDeleteImage(Boolean aBoolean, DUMedia duMedia) {
        if (aBoolean && (duMedia != null)) {
            ToastUtils.showShort(R.string.text_delete_success);
            mPictureList.remove(duMedia);
            mPictureAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShort(R.string.text_delete_failure);
        }
    }

    @Override
    public void onSaveNewVoice(Boolean aBoolean) {
        if (aBoolean) {
            ToastUtils.showShort(R.string.text_save_success);
            mMultimediaPresenter.loadVoices(taskId + currentTime, taskType, taskState);
            mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
        } else {
            ToastUtils.showShort(R.string.text_save_fail);
        }
    }

    @Override
    public void onDeleteVoice(Boolean aBoolean, DUMedia duMedia) {
        if (aBoolean && (duMedia != null)) {
            ToastUtils.showShort(R.string.text_delete_success);

            mVoiceList.remove(duMedia);
            if (mVoiceList.size() >= VOICES_FULL_SIZE) {
                mIsVoiceFull = true;
                mVoiceView.setFull(true);
            } else {
                mIsVoiceFull = false;
                mVoiceView.setFull(false);
            }
            mRecordAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShort(R.string.text_delete_failure);
        }
    }

    @Override
    public void onError(String error) {
        if (!TextUtil.isNullOrEmpty(error)) {
            ToastUtils.showShort(error);
        }
    }

    @Override
    public void onLoadVideoImages(List<DUMedia> duMediaList) {
        if ((duMediaList == null) || (duMediaList.size() <= 0)) {
            mVideoList.clear();
        } else {
            mVideoList = duMediaList;
        }

        mVideosAdapter.setData(mVideoList);
    }

    @Override
    public void onSaveRecordVideo(Boolean aBoolean) {
        if (!aBoolean) {
//            ApplicationsUtil.showMessage(getActivity(), R.string.text_save_video_fail);
            return;
        }
        if (taskType == Constant.TASK_TYPE_CREATE_SELF_ORDER &&
                mActivity instanceof CreateSelfOrderActivity) {
            currentTime = ((CreateSelfOrderActivity) mActivity).currentTime;
            //自开单，先判断有没有填写服务点编号
            if (StringUtils.isEmpty(((CreateSelfOrderActivity) mActivity).mFuwudianBH)) {
                ToastUtils.showShort("请先填写服务点编号");
                return;
            } else {
                mMultimediaPresenter.loadVideos(((CreateSelfOrderActivity) mActivity).mFuwudianBH + currentTime, taskType, taskState);
            }
        } else {
            mMultimediaPresenter.loadVideos(taskId + currentTime, taskType, taskState);
        }
    }

    @Override
    public void onDeleteVideo(Boolean aBoolean, DUMedia duMedia) {
        if (aBoolean && (duMedia != null)) {
            ToastUtils.showShort(R.string.text_delete_success);
            mVideoList.remove(duMedia);
            mVideosAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShort(R.string.text_delete_failure);
        }
    }

    public void setTaskId(String newTaskId) {
        if (!StringUtils.isEmpty(newTaskId))
            taskId = newTaskId;
    }


    /**
     * 初始化控件
     */
    private void initView() {
        mVoiceView.setVisibility(View.GONE);
//        Bundle bundle = getArguments();
//        if (bundle == null) {
//            LogUtil.e(TAG, "bundle is null");
//            return;
//        }
//        mDuMyTask = bundle.getParcelable(Constant.DUMyTask);
//        mIsDataUpload = getArguments().getBoolean(Constant.DATA_IS_UPLOAD, false);
//        mOrigin = bundle.getInt(Constant.ORIGIN);
//        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
//            mDuFileRemotes = bundle.getParcelableArrayList(Constant.INTENT_PARAM_ORDER_PROCES_FILE);
//        } else {
//            taskId = bundle.getString(Constant.TASK_ID);
//            taskType = bundle.getInt(Constant.TASK_TYPE, 0);
//            taskState = bundle.getInt(Constant.TASK_STATE, 0);
//            mIsUploadFlag = bundle.getBoolean(Constant.DATA_IS_UPLOAD);
////            if (TextUtil.isNullOrEmpty(taskId)) {
////                LogUtil.e(TAG, "taskId is null");
////                return;
////            }
//        }
        if (mDuMyTask != null) {
            taskId = mDuMyTask.getFaId();
        }
        if (mIsDataUpload) {
            mTakePhoto.setOnClickListener(null);
            mTakeVoice.setOnClickListener(null);
            mVoiceView.setOnVoiceListener(null);
        } else {
            mTakePhoto.setOnClickListener(this);
            mTakeVoice.setOnClickListener(this);
            mVoiceView.setOnVoiceListener(this);
        }
        initGridViewPictures();
        initListViewRecords();
        initGridViewVideos();
    }

    /**
     * 初始化录像视频列表
     */
    private void initGridViewVideos() {
        mVideosAdapter = new PictureAdapter(mActivity, new ArrayList<>());
        mGridVideos.setAdapter(mVideosAdapter);

        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            if (mDuFileRemotes != null && mDuFileRemotes.size() > 0) {
                List<DUFileRemote> dufileRemotes = new ArrayList<>();
                for (DUFileRemote duFileRemote : mDuFileRemotes) {
                    if (DUMedia.VIDEO_SUFFIX.equals(duFileRemote.getFileType())) {
                        dufileRemotes.add(duFileRemote);
                    }
                }
                mVideosAdapter.setData(dufileRemotes);
            }
        } else {
            if (taskType != Constant.TASK_TYPE_CREATE_SELF_ORDER) {
                mMultimediaPresenter.loadVideos(taskId, taskType, taskState);
            }
        }

        mGridVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
                    if (mVideosAdapter.getDuMediaList().get(position) instanceof DUFileRemote) {
                        LogUtil.i(TAG, "ORIGIN_QUERY");
                        Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
                        intent.putExtra(Constant.VIDEO_FILE_PATH,
                                ((DUFileRemote) mVideosAdapter.getDuMediaList().get(position)).getFileUrl());
                        startActivity(intent);
                    }
                } else {
                    if (TextUtil.isNullOrEmpty(mVideoList.get(position).getExtend())) {
                        ApplicationsUtil.showMessage(getActivity(), R.string.text_video_not_exist);
                        return;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String extend = mVideoList.get(position).getExtend();
                    String bpath = "";
                    try {
                        JSONObject jsonObject = new JSONObject(extend);
                        String videoPath = (String) jsonObject.get(Constant.VIDEO_PATH);
                        bpath = "file://" + videoPath;

                        Uri uri = null;
                        File file = new File(videoPath);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(getContext(), "com.sh3h.hotline.fileprovider", file);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(uri, "video/*");
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //长按删除照片
        mGridVideos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
                    return true;
                }
                if (!mIsDataUpload) {
                    showDialog(DUMedia.FILE_TYPE_SCREEN_SHOT, position);
                }
                return true;
            }
        });
    }

    private void init() {
//        if (getArguments().getInt(ORIGIN) == 2) {//自开单
//            mCreateSelfOrderActivity = (CreateSelfOrderActivity) getActivity();
//            return;
//        }
//        if (getArguments().getInt(ORIGIN) == 3) {//自开单历史跳转的详情
//            mSelfOrderDetailActivity = (CreateSelfOrderDetailActivity) getActivity();
//            return;
//        }
//        mState = getArguments().getInt(HANDLE_STATE);
//        mOrigin = getArguments().getInt(ORIGIN);
//        if (mState == DELAY_ORDER || mState == BACK_ORDER) {
//            mDelayOrBackActivity = (DelayOrBackOrderActivity) getActivity();
//            return;
//        }
//        if (mState == HANDLE_ORDER) {
//            mHandleOrderActivity = (HandleOrderActivity) getActivity();
//        }
    }

    /**
     * 初始化照片列表
     */
    private void initGridViewPictures() {
        mPictureAdapter = new PictureAdapter(mActivity, new ArrayList<>());
        mGridImages.setAdapter(mPictureAdapter);

        //点击显示幻灯片
        mGridImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPictureDetails(position);
            }
        });

        //长按删除照片
        mGridImages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
                    return true;
                }
                if (!mIsDataUpload) {
                    showDialog(DUMedia.FILE_TYPE_PICTURE, position);
                }
                return true;
            }
        });
        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            if (mDuFileRemotes != null && mDuFileRemotes.size() > 0) {
                List<DUFileRemote> dufileRemotes = new ArrayList<>();
                for (DUFileRemote duFileRemote : mDuFileRemotes) {
                    if (DUMedia.PICTURE_SUFFIX.equals(duFileRemote.getFileType())) {
                        dufileRemotes.add(duFileRemote);
                    }
                }
                mPictureAdapter.setData(dufileRemotes);
            }
        } else {
            if (taskType != Constant.TASK_TYPE_CREATE_SELF_ORDER) {
                mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState);
            }
        }
    }

    /**
     * 初始化音频列表
     */
    private void initListViewRecords() {
        // voice button
        mVoiceView.setOutputPath(mMultimediaPresenter.getSoundPath().getPath());

        // voice list
        mRecordAdapter = new RecordAdapter(mActivity, new ArrayList<>());
        mListViewRecords.setAdapter(mRecordAdapter);
        mListViewRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                if (o instanceof DUMedia) {
                    mMultimediaPresenter.playVoice(((DUMedia) o).getFilePath());
                } else if (o instanceof DUFileRemote) {
                    mMultimediaPresenter.playVoice(((DUFileRemote) o).getFileUrl());
                }
            }
        });

        mListViewRecords.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
                    return true;
                }
                if (!mIsDataUpload) {
                    showDialog(DUMedia.FILE_TYPE_VOICE, position);
                }
                return true;
            }
        });
        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            if (mDuFileRemotes != null && mDuFileRemotes.size() > 0) {
                List<DUFileRemote> dufileRemotes = new ArrayList<>();
                for (DUFileRemote duFileRemote : mDuFileRemotes) {
                    if (DUMedia.VOICE_SUFFIX.equals(duFileRemote.getFileType())) {
                        dufileRemotes.add(duFileRemote);
                    }
                }
                mRecordAdapter.setData(dufileRemotes);
            }
        } else {
            if (taskType != Constant.TASK_TYPE_CREATE_SELF_ORDER) {
                mMultimediaPresenter.loadVoices(taskId, taskType, taskState);
            }
        }
    }

    /**
     * 拍照/选取相册
     */
    private void takePhoto(boolean isFromAlbums) {
        try {
            //判断照片是否已满
            if (mPictureList.size() >= PHOTOS_FULL_SIZE) {
                ToastUtils.showShort(
                        R.string.text_pictures_full);
                return;
            }

            File folder = mMultimediaPresenter.getImagePath();
            if (!folder.exists()) {
                folder.mkdirs();
            }

//            mImageName = String.format("%s.jpg",
//                    TextUtil.format(MainApplication.get(mActivity).getCurrentDate(),
//                            TextUtil.FORMAT_DATE_SECOND));
            if (taskType == Constant.TASK_TYPE_CREATE_SELF_ORDER &&
                    mActivity instanceof CreateSelfOrderActivity) {
                //自开单，先判断有没有填写服务点编号
                currentTime = ((CreateSelfOrderActivity) mActivity).currentTime;
                if (StringUtils.isEmpty(((CreateSelfOrderActivity) mActivity).mFuwudianBH)) {
                    ToastUtils.showShort("请先填写服务点编号");
                    return;
                } else {
                    mImageName = "PIC_" + ((CreateSelfOrderActivity) mActivity).mFuwudianBH + "_" + System.currentTimeMillis() + ".jpg";
                }
            } else {
                mImageName = "PIC_" + taskId + "_" + System.currentTimeMillis() + ".jpg";
            }
            if (!isFromAlbums) {
                File file = new File(folder, mImageName);
                Uri uri = null;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {//7.0以上
                    uri = Uri.fromFile(file);
                } else {
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        uri = FileProvider.getUriForFile(getContext(), "com.sh3h.hotline.fileprovider", file);
                    }
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAPTURE_IMAGE);
            } else {
                //相册
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, CAPTURE_ALBUM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.getMessage());
        }
    }

    /**
     * 这是兼容的 AlertDialog
     */
    private void showDialog(int fileType, final int position) {
        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT || mIsUploadFlag) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(getString(R.string.text_request));
        builder.setNegativeButton(getString(R.string.text_negative), null);
        switch (fileType) {
            case DUMedia.FILE_TYPE_PICTURE:
                builder.setMessage(getString(R.string.text_is_delete_photo));
                builder.setPositiveButton(getString(R.string.text_positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if ((position >= 0) && (position < mPictureList.size())) {
                                    mMultimediaPresenter.deleteImage(mPictureList.get(position));
                                }
                            }
                        });
                break;
            case DUMedia.FILE_TYPE_VOICE:
                builder.setMessage(getString(R.string.text_is_delete_record));
                builder.setPositiveButton(getString(R.string.text_positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if ((position >= 0) && (position < mVoiceList.size())) {
                                    mMultimediaPresenter.deleteVoice(mVoiceList.get(position));
                                }
                            }
                        });
                break;
            case DUMedia.FILE_TYPE_SCREEN_SHOT:
                builder.setMessage(getString(R.string.text_is_delete_video));
                builder.setPositiveButton(getString(R.string.text_positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if ((position >= 0) && (position < mVideoList.size())) {
                                    mMultimediaPresenter.deleteVideo(mVideoList.get(position));
                                }
                            }
                        });
                break;
            default:
                break;
        }
        builder.show();
    }

    /**
     * 展示照片详情-幻灯片
     *
     * @param position
     */
    private void showPictureDetails(int position) {
        ArrayList<String> urlList = new ArrayList<>();
        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            if (mDuFileRemotes != null && mDuFileRemotes.size() > 0) {
                for (DUFileRemote duFileRemote : mDuFileRemotes) {
                    if (DUMedia.PICTURE_SUFFIX.equals(duFileRemote.getFileType())) {
                        if (!TextUtil.isNullOrEmpty(duFileRemote.getFileUrl())) {
                            urlList.add(duFileRemote.getFileUrl());
                        }
                    }
                }
            }
        } else {
            if ((mPictureList != null) && (mPictureList.size() > 0)) {
                for (DUMedia duMedia : mPictureList) {
                    if (!TextUtil.isNullOrEmpty(duMedia.getFilePath())) {
                        urlList.add(duMedia.getFilePath());
                    }
                }
            }
        }
        if (urlList.size() > 0) {
            Intent intent = new Intent(mActivity, PictureDetailsActivity.class);
            intent.putExtra(Constant.URLS, urlList);
            intent.putExtra(Constant.POSITION, position);
            startActivity(intent);
        }
    }

    private MediaScannerConnection msc;

    @Subscribe
    public void saveRecordVideo(UIBusEvent.RecordVideo recordVideo) {
        LogUtil.i(TAG, "saveRecordVideo:");
        if (TextUtil.isNullOrEmpty(recordVideo.getVideoPath())) {
            return;
        }

        // 视频文件重命名
        String path = recordVideo.getVideoPath();
        int end = path.lastIndexOf("/");
        if (taskType == Constant.TASK_TYPE_CREATE_SELF_ORDER &&
                mActivity instanceof CreateSelfOrderActivity) {
            currentTime = ((CreateSelfOrderActivity) mActivity).currentTime;
            //自开单，先判断有没有填写服务点编号
            if (StringUtils.isEmpty(((CreateSelfOrderActivity) mActivity).mFuwudianBH)) {
                ToastUtils.showShort("请先填写服务点编号");
                return;
            } else {
                mNewPath = path.substring(0, end + 1) + "VIO_" + ((CreateSelfOrderActivity) mActivity).mFuwudianBH + "_" + System.currentTimeMillis() + ".mp4";
            }
        } else {
            mNewPath = path.substring(0, end + 1) + "VIO_" + taskId + "_" + System.currentTimeMillis() + ".mp4";
        }
        renameFile(new File(recordVideo.getVideoPath()).getAbsolutePath(), new File(mNewPath).getAbsolutePath());

        // 刷新媒体库
        msc = new MediaScannerConnection(getActivity(), new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                msc.scanFile(new File(mNewPath).getAbsolutePath(), null);
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                msc.disconnect();
            }

        });
        msc.connect();

        String frameJPGName = String.format("%s.jpg",
                TextUtil.format(MainApplication.get(getActivity()).getCurrentDate(),
                        TextUtil.FORMAT_DATE_SECOND));
        if (taskType == Constant.TASK_TYPE_CREATE_SELF_ORDER &&
                mActivity instanceof CreateSelfOrderActivity) {
            //自开单，先判断有没有填写服务点编号
            mMultimediaPresenter.saveFrameJPGVideo(mNewPath, frameJPGName,
                    ((CreateSelfOrderActivity) mActivity).mFuwudianBH + currentTime, taskType, taskState);
        } else {
            mMultimediaPresenter.saveFrameJPGVideo(mNewPath, frameJPGName,
                    taskId, taskType, taskState);
        }
    }


    final Handler handlerStop = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STOP_COUNT:
                    mRecordCount = 0;
                    if (mRunnable != null) {
                        mHandler.removeCallbacks(mRunnable);
                    }
                    break;
            }
        }
    };

    /**
     * oldPath 和 newPath必须是新旧文件的绝对路径
     */
    private void renameFile(String oldPath, String newPath) {
        if (TextUtils.isEmpty(oldPath)) {
            return;
        }
        if (TextUtils.isEmpty(newPath)) {
            return;
        }
        File file = new File(oldPath);
        file.renameTo(new File(newPath));
    }

    public void notifyData(String taskId, int taskType, int taskState) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.taskState = taskState;

        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            if (mDuFileRemotes != null && mDuFileRemotes.size() > 0) {
                List<DUFileRemote> dufileRemotes = new ArrayList<>();
                for (DUFileRemote duFileRemote : mDuFileRemotes) {
                    if (DUMedia.PICTURE_SUFFIX.equals(duFileRemote.getFileType())) {
                        dufileRemotes.add(duFileRemote);
                    }
                }
                mPictureAdapter.setData(dufileRemotes);
            }
        } else {
            if (taskType != Constant.TASK_TYPE_CREATE_SELF_ORDER) {
                if (mMultimediaPresenter != null) {
                    mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState);
                }
            }
        }

        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            if (mDuFileRemotes != null && mDuFileRemotes.size() > 0) {
                List<DUFileRemote> dufileRemotes = new ArrayList<>();
                for (DUFileRemote duFileRemote : mDuFileRemotes) {
                    if (DUMedia.VOICE_SUFFIX.equals(duFileRemote.getFileType())) {
                        dufileRemotes.add(duFileRemote);
                    }
                }
                mRecordAdapter.setData(dufileRemotes);
            }
        } else {
            if (taskType != Constant.TASK_TYPE_CREATE_SELF_ORDER) {
                if (mMultimediaPresenter != null) {
                    mMultimediaPresenter.loadVoices(taskId, taskType, taskState);
                }
            }
        }

        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            if (mDuFileRemotes != null && mDuFileRemotes.size() > 0) {
                List<DUFileRemote> dufileRemotes = new ArrayList<>();
                for (DUFileRemote duFileRemote : mDuFileRemotes) {
                    if (DUMedia.VIDEO_SUFFIX.equals(duFileRemote.getFileType())) {
                        dufileRemotes.add(duFileRemote);
                    }
                }
                mVideosAdapter.setData(dufileRemotes);
            }
        } else {
            if (taskType != Constant.TASK_TYPE_CREATE_SELF_ORDER) {
                if (mMultimediaPresenter != null) {
                    mMultimediaPresenter.loadVideos(taskId, taskType, taskState);
                }
            }
        }

    }
}