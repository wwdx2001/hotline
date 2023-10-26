package com.sh3h.hotline.adapter.baseadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * ViewPager+Fragment适配器
 *
 * @author xiaochao
 * @date 2018/11/16
 */

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> data;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<Fragment> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

}
