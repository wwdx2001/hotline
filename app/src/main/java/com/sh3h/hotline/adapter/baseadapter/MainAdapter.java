package com.sh3h.hotline.adapter.baseadapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.MainItemBean;
import com.sh3h.hotline.ui.main.MainActivity;

import java.util.List;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/1 09:26
 */
public class MainAdapter extends BaseQuickAdapter<MainItemBean, BaseViewHolder> {

    public MainAdapter(int layoutResId, @Nullable List<MainItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MainItemBean mainItemBean) {
        LinearLayout linearLayout = baseViewHolder.getView(R.id.ll_item);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.width = ScreenUtils.getScreenWidth() / 3;
        params.height = ScreenUtils.getScreenWidth() / 3;
        baseViewHolder.setText(R.id.tv_title, mainItemBean.getName());
        baseViewHolder.setImageResource(R.id.iv_icon, mainItemBean.getIcon());
    }


}
