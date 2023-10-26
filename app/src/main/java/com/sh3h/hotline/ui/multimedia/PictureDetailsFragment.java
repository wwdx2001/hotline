package com.sh3h.hotline.ui.multimedia;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.order.myorder.delayorback.DelayOrBackOrderActivity;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderActivity;
import com.sh3h.hotline.ui.order.self.detail.CreateSelfOrderDetailActivity;

import java.util.List;

/**
 * 幻灯片-显示照片详情
 * Created by zhangjing on 2016/9/26.
 */

public class PictureDetailsFragment extends ParentFragment implements View.OnClickListener {

    private final static String TAG = "PictureDetailFragment";
    private static int mPosition;
    private ImageView[] mImageViews;
    private static List<String> mPictureUrls;
    private DelayOrBackOrderActivity mDelayBackActivity;
    private HandleOrderActivity mHandleActivity;
    private CreateSelfOrderActivity mCreateSelfOrderActivity;
    private CreateSelfOrderDetailActivity mSelfOrderDetialActivity;

    public static PictureDetailsFragment newInstance(int position, List<String> pictureUrls) {
        mPosition = position;
        mPictureUrls = pictureUrls;
        return new PictureDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof DelayOrBackOrderActivity) {
            mDelayBackActivity = (DelayOrBackOrderActivity) getActivity();
            return;
        }
        if (getActivity() instanceof HandleOrderActivity) {
            mHandleActivity = (HandleOrderActivity) getActivity();
            return;
        }
        if (getActivity() instanceof CreateSelfOrderActivity) {
            mCreateSelfOrderActivity = (CreateSelfOrderActivity) getActivity();
            return;
        }
        if (getActivity() instanceof CreateSelfOrderDetailActivity) {
            mSelfOrderDetialActivity = (CreateSelfOrderDetailActivity) getActivity();
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_photo_detail, container, false);
        view.setOnClickListener(this);
        ViewPagerFixed viewPager = (ViewPagerFixed) view.findViewById(R.id.guidePages);
        ViewGroup group = (ViewGroup) view.findViewById(R.id.viewGroup);
        ImageView imageView;
        final ImageView[] imageViews = new ImageView[mPictureUrls.size()];
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int heig = (int) (height * 0.015);
        for (int i = 0; i < mPictureUrls.size(); i++) {
            imageView = new ImageView(getContext());
            //android.view.ViewGroup.LayoutParams
            imageView.setLayoutParams(new ViewGroup.LayoutParams(heig, heig));
            imageView.setPadding(heig, 0, heig, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(heig, heig);
            params.setMargins(heig, 0, heig, heig * 2);
            imageView.setLayoutParams(params);
            imageViews[i] = imageView;
            if (i == 0) {
                //默认选中第一张图
                imageViews[i].setBackgroundResource(R.mipmap.ic_selectedpicture);
            } else {
                imageViews[i].setBackgroundResource(R.mipmap.ic_notselectedpicture);
            }
            group.addView(imageViews[i]);
        }

        mImageViews = new ImageView[mPictureUrls.size()];
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return mImageViews.length;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup view, int position, Object object) {
                view.removeView(mImageViews[position]);
                mImageViews[position] = null;
            }

            @Override
            public Object instantiateItem(ViewGroup view, int position) {
                final ImageView photoView = new ImageView(getActivity());
                mImageViews[position] = photoView;
                Bitmap bm = BitmapFactory.decodeFile(mPictureUrls.get(position));
                photoView.setImageBitmap(bm);
//                photoView.setScaleType(ImageView.ScaleType.FIT_XY);
                view.addView(mImageViews[position], ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return mImageViews[position];
            }
        };
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imageViews.length; i++) {
                    imageViews[position].setBackgroundResource(R.mipmap.ic_selectedpicture);

                    if (position != i) {
                        imageViews[i].setBackgroundResource(R.mipmap.ic_notselectedpicture);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(mPosition);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View v) {
        Activity activity = getActivity();
        activity.onBackPressed();
        if (activity instanceof DelayOrBackOrderActivity) {
            if (mDelayBackActivity != null) {
                mDelayBackActivity.setLayout(false);
            }
            return;
        }
        if (activity instanceof HandleOrderActivity) {
            if (mHandleActivity != null) {
                mHandleActivity.setLayout(false);
            }
            return;
        }
        if (activity instanceof CreateSelfOrderActivity) {
            if (mCreateSelfOrderActivity != null) {
                mCreateSelfOrderActivity.setLayout(false);
            }
            return;
        }
        if (activity instanceof CreateSelfOrderDetailActivity) {
            if (mSelfOrderDetialActivity != null) {
                mSelfOrderDetialActivity.setLayout(false);
            }
            return;
        }
    }
}
