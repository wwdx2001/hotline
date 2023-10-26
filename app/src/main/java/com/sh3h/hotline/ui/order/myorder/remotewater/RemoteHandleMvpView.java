package com.sh3h.hotline.ui.order.myorder.remotewater;

import com.sh3h.hotline.ui.base.MvpView;

import java.io.File;
import java.util.List;

public interface RemoteHandleMvpView extends MvpView {

    void onCommitTaskSuccess(String msg);

    void onCommitError(String error);

    void showMessage(String message);

}
