package com.sh3h.hotline.ui.query;

import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by zhangjing on 2016/9/18.
 */
public interface QueryOrderMvpView extends MvpView {

    void onInitIssueOriginSpinner(List<DUWord> words);

    void onInitFanYingLeiXingSpinner(List<DUWord> words);

    void onInitFanYingNeiRongSpinner(List<DUWord> words);

    void onInitFanYingQuMingSpinner(List<DUWord> words);

    void onInitStation(DUWord duWord,boolean isLoginStation);
}
