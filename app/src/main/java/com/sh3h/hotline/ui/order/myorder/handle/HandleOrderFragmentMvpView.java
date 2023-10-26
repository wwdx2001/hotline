package com.sh3h.hotline.ui.order.myorder.handle;

import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangjing on 2016/10/19.
 */

public interface HandleOrderFragmentMvpView extends MvpView {

    void onGetHandleTypeWords(List<DUWord> duWords);

    void onGetHandleResultWords(List<DUWord> duWords);

    void onGetWord(Map<String, List<DUWord>> map);
}
