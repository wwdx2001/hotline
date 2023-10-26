package com.sh3h.hotline.ui.nonresident.sign;

import android.graphics.Bitmap;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.file.FileHelper;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_SIGNUP;
import static com.sh3h.dataprovider.data.entity.DUMedia.UPLOAD_FLAG_LOCAL;

/**
 *
 */
public class SignatureNamePresenter extends ParentPresenter<SignatureNameMvpView> {
    private static final String TAG = "SignatureNamePresenter";

    private final ConfigHelper mConfigHelper;
    private int mUserId;
    private FileHelper mFileHelper;

    @Inject
    public SignatureNamePresenter(DataManager dataManager,
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
                        if (duMediaList == null || duMediaList.size() < 1
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

                        LogUtil.i(TAG, duMediaList.get(0).getFilePath());
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
}
