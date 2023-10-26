package com.sh3h.hotline.ui.order.myorder.picture;

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
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_VOICE;
import static com.sh3h.dataprovider.data.entity.DUMedia.UPLOAD_FLAG_LOCAL;
import static com.sh3h.dataprovider.util.Constant.VIDEO_PATH;

/**
 *
 */
public class PictureFilePresenter extends ParentPresenter<PictureFileMvpView> {
    private static final String TAG = "PictureFilePresenter";

    private final ConfigHelper mConfigHelper;
    private int mUserId;
    private FileHelper mFileHelper;

    @Inject
    public PictureFilePresenter(DataManager dataManager,
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
    public void loadImages(String taskId, int taskType, int taskState, int fileType) {
        if (TextUtil.isNullOrEmpty(taskId)) {
            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadImages");
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, fileType)
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
     * 保存新照片-(压缩照片)
     *
     * @param taskId
     * @param taskType
     * @param taskState
     * @param fileName
     * @param filePath
     */
    public void saveNewImage(final String taskId, final int taskType, final int taskState,
                             final int fileType, final String fileName, File filePath, boolean isFromAlbum, Bitmap bitmap) {
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
                                fileType,
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
}
