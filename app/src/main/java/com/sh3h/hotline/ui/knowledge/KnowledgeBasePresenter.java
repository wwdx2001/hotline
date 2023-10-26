package com.sh3h.hotline.ui.knowledge;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.hotline.ui.base.ParentPresenter;

import javax.inject.Inject;

/**
 * Created by dengzhimin on 2016/9/14.
 */
public class KnowledgeBasePresenter extends ParentPresenter<KnowledgeBaseMvpView> {

    @Inject
    public KnowledgeBasePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }
}
