package com.sh3h.hotline.ui.order.myorder.picture;

import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 *
 */
public interface PictureFileMvpView extends MvpView {

    void onLoadImages(List<DUMedia> duMediaList);

    void onSaveNewImage(Boolean aBoolean);

    void onDeleteImage(Boolean aBoolean, DUMedia duMedia);

    void onError(String error);
}
