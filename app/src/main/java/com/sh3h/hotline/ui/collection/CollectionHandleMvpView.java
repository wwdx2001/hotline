package com.sh3h.hotline.ui.collection;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.hotline.ui.base.MvpView;

import java.io.File;
import java.util.List;

public interface CollectionHandleMvpView extends MvpView {

    void onSignComplete(List<File> uploadFiles);

    void onCommitTaskSuccess(String msg);

    void onCommitError(String error);

    void showMessage(String message);

}
