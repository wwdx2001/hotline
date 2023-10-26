package com.sh3h.hotline.ui.multimedia;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.file.FileHelper;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.OtoO;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_PICTURE;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_SCREEN_SHOT;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_SIGNUP;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_VOICE;
import static com.sh3h.dataprovider.data.entity.DUMedia.UPLOAD_FLAG_LOCAL;
import static com.sh3h.dataprovider.util.Constant.VIDEO_PATH;

/**
 * Created by zhangjing on 2016/9/12.
 */
public class MultimediaPresenter extends ParentPresenter<MultimediaMvpView> {
    private static final String TAG = "MultimediaPresenter";

    private final ConfigHelper mConfigHelper;
    private int mUserId;
    private MediaPlayer mMediaPlayer;
    private FileHelper mFileHelper;

    @Inject
    public MultimediaPresenter(DataManager dataManager,
                               ConfigHelper configHelper,
                               FileHelper fileHelper) {
        super(dataManager);
        this.mConfigHelper = configHelper;
        this.mFileHelper = fileHelper;
        mUserId = dataManager.getUserId();
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.getMessage());
            } finally {
                mMediaPlayer = null;
            }
        }
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    /**
     * get the path of the image folder
     *
     * @return
     */
    public File getImagePath() {
        return mConfigHelper.getImageFolderPath();
    }

    /**
     * get the path of the sound folder
     *
     * @return
     */
    public File getSoundPath() {
        return mConfigHelper.getSoundFolderPath();
    }

    /**
     * 加载本地图片
     *
     * @param taskId
     * @param taskType
     * @param taskState
     */
    public void loadImages(String taskId, int taskType, int taskState) {
        if (TextUtil.isNullOrEmpty(taskId)) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadImages");
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, DUMedia.FILE_TYPE_PICTURE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "loadImages onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadImages onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadImages onNext");

                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getImagePath(), fileName);
                            LogUtils.e("fileName", fileName);
                            if (file.exists()) {
                                duMedia.setFilePath(file.getPath());
                            }
                        }
                        getMvpView().onLoadImages(duMediaList);
                    }
                }));
    }

    /**
     * 扫描本地录音
     *
     * @param taskId
     * @param taskType
     * @param taskState
     */
    public void loadVoices(String taskId, int taskType, int taskState) {
        if (TextUtil.isNullOrEmpty(taskId)) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadVoices");
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, FILE_TYPE_VOICE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "loadVoices onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadVoices onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadImages onNext");

                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getSoundPath(), fileName);
                            if (file.exists()) {
                                duMedia.setFilePath(file.getPath());
                            }
                        }

                        getMvpView().onLoadVoices(duMediaList);
                    }
                }));
    }

    /**
     * 保存新照片-(压缩照片)
     *
     * @param taskId
     * @param taskType
     * @param taskState
     * @param fileName
     * @param filePath
     */
    public void saveNewImage(final String taskId, final int taskType, final int taskState,
                             final String fileName, File filePath, boolean isFromAlbum, Bitmap bitmap) {
        if (/*TextUtil.isNullOrEmpty(taskId)
                || */TextUtil.isNullOrEmpty(fileName)
                || (filePath == null)) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        if (isFromAlbum && bitmap != null) {//来自本地相册
            if (!mFileHelper.saveJPG(bitmap, filePath.getPath())) {
                LogUtil.i(TAG, "save image failed!");
                getMvpView().onError("保存图片失败!");
                return;
            }
        }

        LogUtil.i(TAG, "saveNewImage");
        mSubscription.add(mDataManager.compressImageAndAddStamp(fileName, filePath)
                .concatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        DUMedia duMedia = new DUMedia(
                                -1,
                                mUserId,
                                taskId,
                                taskType,
                                taskState,
                                FILE_TYPE_PICTURE,
                                fileName,
                                null,
                                null,
                                UPLOAD_FLAG_LOCAL,
                                null,
                                0);

                        return mDataManager.saveMedia(duMedia);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "saveNewImage onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "saveNewImage onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "saveNewImage onNext " + aBoolean);
                        getMvpView().onSaveNewImage(aBoolean);
                    }
                }));
    }

    /**
     * 删除照片
     *
     * @param duMedia
     */
    public void deleteImage(final DUMedia duMedia) {
        if ((duMedia == null)
                || TextUtil.isNullOrEmpty(duMedia.getFilePath())
                || TextUtil.isNullOrEmpty(duMedia.getFileName())) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        if (duMedia.getUploadFlag() != Constant.UPLOAD_FLAG_DEFAULT) {
            getMvpView().onError("照片正在上传或已上传，无法删除");
            return;
        }

        LogUtil.i(TAG, "deleteImage");
        mSubscription.add(mDataManager.deleteMedia(duMedia.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "deleteImage onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "deleteImage onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "deleteImage onNext " + aBoolean);
                        getMvpView().onDeleteImage(aBoolean, duMedia);
                    }
                }));
    }

    /**
     * 保存语音
     *
     * @param taskId
     * @param taskType
     * @param taskState
     * @param fileName
     * @param time
     */
    public void saveNewVoice(final String taskId, final int taskType, final int taskState,
                             final String fileName, long time) {
        if (TextUtil.isNullOrEmpty(taskId)
                || TextUtil.isNullOrEmpty(fileName)) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "saveNewImage");
        DUMedia.Extend extend = new DUMedia.Extend(time);
        JSONObject jsonObject = extend.toJson();
        DUMedia duMedia = new DUMedia(
                -1,
                mUserId,
                taskId,
                taskType,
                taskState,
                FILE_TYPE_VOICE,
                fileName,
                null,
                null,
                UPLOAD_FLAG_LOCAL,
                jsonObject != null ? jsonObject.toString() : null,
                0);
        mSubscription.add(mDataManager.saveMedia(duMedia)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "saveNewVoice onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "saveNewVoice onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "saveNewVoice onNext " + aBoolean);
                        getMvpView().onSaveNewVoice(aBoolean);
                    }
                }));
    }

    /**
     * 播放语音
     *
     * @param filePath
     */
    public void playVoice(final String filePath) {
        if (TextUtil.isNullOrEmpty(filePath)) {
            LogUtil.i(TAG, "param is null");
            getMvpView().onError("param is null");
            return;
        }

        LogUtil.i(TAG, "playVoice");
        mSubscription.add(Observable.create(
                new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        try {
                            if (mMediaPlayer != null) {
                                mMediaPlayer.stop();
                                mMediaPlayer.release();
                                mMediaPlayer = null;
                            }

                            mMediaPlayer = new MediaPlayer();
//                            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                                @Override
//                                public void onCompletion(MediaPlayer mp) {
//                                    mp.release();
//                                    mMediaPlayer = null;
//                                }
//                            });

                            mMediaPlayer.setDataSource(filePath);
                            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mMediaPlayer.prepare();
                            mMediaPlayer.start();
                            subscriber.onNext(true);
                        } catch (Exception e) {
                            LogUtil.e(TAG, e.getMessage());
                            e.printStackTrace();
                            subscriber.onNext(false);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "playVoice onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "playVoice onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "playVoice onNext " + aBoolean);
                    }
                }));
    }

    /**
     * 删除语音
     *
     * @param duMedia
     */
    public void deleteVoice(final DUMedia duMedia) {
        if ((duMedia == null)
                || TextUtil.isNullOrEmpty(duMedia.getFilePath())
                || TextUtil.isNullOrEmpty(duMedia.getFileName())) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "deleteImage");
        mSubscription.add(mDataManager.deleteMedia(duMedia.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "deleteImage onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "deleteImage onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "deleteImage onNext " + aBoolean);
                        getMvpView().onDeleteVoice(aBoolean, duMedia);
                    }
                }));
    }

    public void loadSignup(String taskId, int taskType, int taskState) {
        if (TextUtil.isNullOrEmpty(taskId)) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadSignup");
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, DUMedia.FILE_TYPE_SIGNUP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "loadSignup onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadSignup onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadImages onNext");
                        if (duMediaList == null || duMediaList.size() != 1
                            /*&& duMediaList.size() > 0*/) {
                            return;
                        }
                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getImagePath(), fileName);
                            if (file.exists()) {
                                duMedia.setFilePath(file.getPath());
                            }
                        }
                        getMvpView().onLoadSignup(duMediaList.get(0));
                    }
                }));
    }

    public void saveSignupImage(Bitmap bitmap, String signupName, String taskId, int taskState, int taskType) {
        String filePath = mConfigHelper.getImageFolderPath() + "/" + signupName;
        if (!mFileHelper.savePNG(bitmap, filePath)) {
            LogUtil.i(TAG, "save signup image failed!");
            getMvpView().onError("保存电子签名失败!");
        }
        DUMedia duMedia = new DUMedia(
                -1,
                mUserId,
                taskId,
                taskType,
                taskState,
                FILE_TYPE_SIGNUP,
                signupName,
                null,
                null,
                UPLOAD_FLAG_LOCAL,
                null,
                0);
        mSubscription.add(mDataManager.saveMedia(duMedia)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "saveSignupImage onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "saveSignupImage onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "saveSignupImage onNext " + aBoolean);
                        getMvpView().onSaveSignup(aBoolean);
                    }
                }));
    }

    /**
     * 删除签名
     *
     * @param duMedia
     */
    public void deleteSignup(final DUMedia duMedia) {
        if ((duMedia == null)
                || TextUtil.isNullOrEmpty(duMedia.getFilePath())
                || TextUtil.isNullOrEmpty(duMedia.getFileName())) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        if (duMedia.getUploadFlag() != Constant.UPLOAD_FLAG_DEFAULT) {
            getMvpView().onError("电子签名正在上传或已上传，无法删除");
            return;
        }

        LogUtil.i(TAG, "deleteSignup");
        mSubscription.add(mDataManager.deleteMedia(duMedia.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "deleteImage onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "deleteImage onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "deleteImage onNext " + aBoolean);
                        getMvpView().onDeleteSignup(aBoolean);
                    }
                }));
    }

    /**
     * 保存视频截图和视频路径到数据库
     *
     * @param videoPath
     * @param frameJPGName
     * @param taskId
     * @param taskState
     */
    public void saveFrameJPGVideo(String videoPath, String frameJPGName, String taskId,
                                  int taskType, int taskState) {
        //创建MediaMetadataRetriever对象
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        //设置资源位置
        mmr.setDataSource(videoPath);
        //获取第一帧图像的bitmap对象
        Bitmap bitmap = mmr.getFrameAtTime();
        mmr.release();
        String frameJPGPath = mConfigHelper.getVideoFolderPath() + "/" + frameJPGName;
        if (!mFileHelper.saveJPG(bitmap, frameJPGPath)) {
            LogUtil.i(TAG, "saveFrameJPGVide fail");
            return;
        }
        DUMedia duMedia = new DUMedia(
                -1,
                mUserId,
                taskId,
                taskType,
                taskState,
                FILE_TYPE_SCREEN_SHOT,
                frameJPGName,
                null,
                null,
                UPLOAD_FLAG_LOCAL,
                null,
                0);
        duMedia.setExtend((OtoO.add2Extend(duMedia.getExtend(), VIDEO_PATH, videoPath)));
        mSubscription.add(mDataManager.saveMedia(duMedia)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "saveFrameJPGVide onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "saveNewImage onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "saveNewImage onNext " + aBoolean);
                        getMvpView().onSaveRecordVideo(aBoolean);
                    }
                }));
    }

    /**
     * 加载本地录像
     *
     * @param taskId
     * @param taskState
     */
    public void loadVideos(String taskId, int taskType, int taskState) {
        if (TextUtil.isNullOrEmpty(taskId)) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadImages");
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, DUMedia.FILE_TYPE_SCREEN_SHOT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "loadVideos onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadVideos onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadVideos onNext");
                        if (duMediaList == null || duMediaList.isEmpty()) {
                            return;
                        }
                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getVideoPath(), fileName);
                            Log.e("videoPath", "path=" + getVideoPath() + "fileName=" + fileName);
                            if (file.exists()) {
                                duMedia.setFilePath(file.getPath());
                            }
                        }
                        getMvpView().onLoadVideoImages(duMediaList);
                    }
                }));
    }

    /**
     * get the path of the video folder
     *
     * @return
     */
    public File getVideoPath() {
        return mConfigHelper.getVideoFolderPath();
    }

    /**
     * 删除视频
     *
     * @param duMedia
     */
    public void deleteVideo(final DUMedia duMedia) {
        if ((duMedia == null)
                || TextUtil.isNullOrEmpty(duMedia.getFilePath())
                || TextUtil.isNullOrEmpty(duMedia.getFileName())) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        if (duMedia.getUploadFlag() != Constant.NO_UPLOAD) {
            getMvpView().onError("视频正在上传或已上传，无法删除");
            return;
        }

        LogUtil.i(TAG, "deleteImage");
        mSubscription.add(mDataManager.deleteMedia(duMedia.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "deleteImage onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "deleteImage onError " + e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "deleteImage onNext " + aBoolean);
                        getMvpView().onDeleteVideo(aBoolean, duMedia);
                    }
                }));
    }


//    private void saveNewImageFromAlbum(Bitmap bitmap, String imageName, String taskId, int taskState, int taskType) {
//        String filePath = mConfigHelper.getImageFolderPath() + "/" + imageName;
//        if (!mFileHelper.savePNG(bitmap, filePath)) {
//            LogUtil.i(TAG, "save image failed!");
//            getMvpView().onError("保存图片失败!");
//        }
//    }
}
