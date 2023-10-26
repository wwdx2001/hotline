package com.sh3h.hotline.ui.multimedia;

import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by zhangjing on 2016/9/12.
 */
public interface MultimediaMvpView extends MvpView {

    void onLoadImages(List<DUMedia> duMediaList);

    void onLoadVoices(List<DUMedia> duMediaList);

    void onSaveNewImage(Boolean aBoolean);

    void onDeleteImage(Boolean aBoolean, DUMedia duMedia);

    void onSaveNewVoice(Boolean aBoolean);

    void onSaveSignup(Boolean aBoolean);

    void onLoadSignup(DUMedia duMedia);

    void onDeleteSignup(Boolean aBoolean);

    void onDeleteVoice(Boolean aBoolean, DUMedia duMedia);

    void onError(String error);

    void onLoadVideoImages(List<DUMedia> duMediaList);

    void onSaveRecordVideo(Boolean aBoolean);

    void onDeleteVideo(Boolean aBoolean, DUMedia duMedia);
}
