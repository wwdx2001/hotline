package com.sh3h.hotline.ui.nonresident.media;

import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 *
 */
public interface MultimediaFileMvpView extends MvpView {

    void onLoadImages(List<DUMedia> duMediaList);

    void onLoadVoices(List<DUMedia> duMediaList);

    void onSaveNewImage(Boolean aBoolean);

    void onDeleteImage(Boolean aBoolean, DUMedia duMedia);

    void onSaveNewVoice(Boolean aBoolean);

    void onDeleteVoice(Boolean aBoolean, DUMedia duMedia);

    void onError(String error);

    void onLoadVideoImages(List<DUMedia> duMediaList);

    void onSaveRecordVideo(Boolean aBoolean);

    void onDeleteVideo(Boolean aBoolean, DUMedia duMedia);
}
