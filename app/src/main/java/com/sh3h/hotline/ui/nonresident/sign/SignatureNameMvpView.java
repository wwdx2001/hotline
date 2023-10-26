package com.sh3h.hotline.ui.nonresident.sign;

import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 *
 */
public interface SignatureNameMvpView extends MvpView {

    void onSaveSignup(Boolean aBoolean);

    void onLoadSignup(DUMedia duMedia);

    void onDeleteSignup(Boolean aBoolean);

    void onError(String error);
}
