package com.sh3h.hotline.ui.multimedia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 幻灯片展示-activity
 * Created by zhangjing on 2016/9/28.
 */
public class PictureDetailsActivity extends ParentActivity implements View.OnClickListener {
    @BindView(R.id.picture_detais_layout)
    LinearLayout mLayout;

    @BindView(R.id.guidePages)
    ViewPager mViewPager;

    @BindView(R.id.viewGroup)
    ViewGroup mViewGroup;

    private final static String TAG = "PictureDetailsActivity";
    private static int mPosition;
    private ImageView[] mImageViews;
    private static List<String> mPictureUrls;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_details);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        init();
    }

    private void init() {
        mLayout.setOnClickListener(this);
        Intent intent = getIntent();
        if(intent == null){
            throw new NullPointerException("intent is null");
        }
        mPosition = intent.getIntExtra(Constant.POSITION, 0);
        mPictureUrls = intent.getStringArrayListExtra(Constant.URLS);
        ImageView imageView;
        final ImageView[] imageViews = new ImageView[mPictureUrls.size()];
        WindowManager windowManager= (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int height = windowManager.getDefaultDisplay().getHeight();
        int heig = (int) (height * 0.015);
        for (int i = 0; i < mPictureUrls.size(); i++) {
            imageView = new ImageView(this);
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
            mViewGroup.addView(imageViews[i]);
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
                final ImageView photoView = new ImageView(PictureDetailsActivity.this);
                mImageViews[position] = photoView;
                if(mPictureUrls.get(position).contains("http")){
                    Picasso.with(PictureDetailsActivity.this)
                            .load(mPictureUrls.get(position))
                            .placeholder(R.mipmap.bg_place_holder)
                            .error(R.mipmap.bg_error)
                            .fit()
                            .tag(PictureDetailsActivity.this)
                            .into(photoView);
                }else{
                    Picasso.with(PictureDetailsActivity.this)
                            .load(new File(mPictureUrls.get(position)))
                            .placeholder(R.mipmap.bg_place_holder)
                            .error(R.mipmap.bg_error)
                            .fit()
                            .tag(PictureDetailsActivity.this)
                            .into(photoView);
                }
                view.addView(mImageViews[position], ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return mImageViews[position];
            }
        };
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mPosition);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
