package com.sh3h.hotline.ui.query;

import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * 选择站点
 * Created by BJB147 on 2018/4/16.
 */
public interface ChooseSiteMvpView extends MvpView {

    void onShowMessage(String message);

    void onGetSites(List<DUWord> duWordList);
}
