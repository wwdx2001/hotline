package com.sh3h.hotline.ui.collection;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.hotline.ui.base.ParentPresenter;

import javax.inject.Inject;

public class CollectionTaskPresenter extends ParentPresenter<CollectionTaskMvpView> {
  @Inject
  public CollectionTaskPresenter(DataManager dataManager) {
    super(dataManager);
  }

  @Override
  public void cancelEasyhttpRequest() {

  }
}
