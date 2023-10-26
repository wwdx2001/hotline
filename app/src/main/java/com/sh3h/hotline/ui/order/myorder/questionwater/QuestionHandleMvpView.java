package com.sh3h.hotline.ui.order.myorder.questionwater;

import com.sh3h.hotline.ui.base.MvpView;

public interface QuestionHandleMvpView extends MvpView {

    void onCommitTaskSuccess(String msg);

    void onCommitError(String error);

    void showMessage(String message);

}
