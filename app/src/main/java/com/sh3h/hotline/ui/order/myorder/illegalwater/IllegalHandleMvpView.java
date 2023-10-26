package com.sh3h.hotline.ui.order.myorder.illegalwater;

import com.sh3h.hotline.ui.base.MvpView;

public interface IllegalHandleMvpView extends MvpView {

    void onCommitTaskSuccess(String msg);

    void onCommitError(String error);

    void showMessage(String message);

}
