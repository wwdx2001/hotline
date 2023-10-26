package com.sh3h.hotline.ui.collection;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.newentity.CollectionHandleEntity;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CollectionTaskListBean;
import com.sh3h.hotline.event.NotifRefrashDataEvent;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.nonresident.media.MultimediaFileFragment;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.util.StringRegexUtils;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CollectionTaskOrderBackfillingActivity extends ParentActivity implements ViewPager.OnPageChangeListener,
        RadioGroup.OnCheckedChangeListener, CollectionHandleMvpView {
    private final static String TAG = "HandleOrderActivity";

    private static final String STATE_FRAGMENT_SHOW = "CurrentFragment";
    private int mState = TaskState.HANDLE;

    @Inject
    EventPosterHelper mEventPosterHelper;

    @Inject
    Bus mEventBus;

    @Inject
    CollectionHandlePresenter mHandleOrderPresenter;

    @Inject
    DataManager mDataManager;

    @BindView(R.id.delay_order_rg)
    public RadioGroup mRadioGroup;

    @BindView(R.id.delay_order_details_rb)
    RadioButton mDetailsRadioBtn;

    @BindView(R.id.delay_order_process_rb)
    RadioButton mProcessRadioBtn;

    @BindView(R.id.handle_order_ll)
    LinearLayout mLayout;

    @BindView(R.id.handle_order_bottom_ll)
    LinearLayout mBottomLayout;

    private Toolbar mToolBar;

    private Unbinder mUnbinder;

    //详细
    private CollectionInfromationFragment mCollectionInfromationFragment;

    //回填
    private CollectionBackfillingFragment mCollectionBackfillingFragment;

    //多媒体
    private MultimediaFileFragment mMultimediaFragment;


    //当前加载的fragment
    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;

    private CollectionTaskListBean taskBean;
    private CollectionHandleEntity handleEntity;

    private String cjjg;
    private String cjbz;

    private String xxbg;
    private String lxr;
    private String lxfs;

    private int mOrigin;
    private String mTaskId;//工单编号

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_order);
        getActivityComponent().inject(this);
        mHandleOrderPresenter.attachView(this);
        mEventBus.register(this);
        mUnbinder = ButterKnife.bind(this);
        initToolBar(R.string.collection_task_order_handle);

        init(savedInstanceState);

        initView(savedInstanceState);

        checkPermissions();
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mOrigin = intent.getIntExtra(Constant.ORIGIN, 0);
        mTaskId = intent.getStringExtra(Constant.TASK_ID);
        taskBean = intent.getParcelableExtra("collection");

        if (mOrigin == 99) {
            if (savedInstanceState != null) {
                initParams(savedInstanceState);
            } else if (intent != null) {
                initParams(intent.getExtras());
            } else {
                initParams(null);
            }
        } else {
            initFragment();
        }
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        initMutimediaFragment();
        initCollectionBackfillingFragment();
        initCollectionInfromationFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {//保存信息
            submitInfo();
        } else if (item.getItemId() == android.R.id.home) {
            saveData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveData();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandleOrderPresenter.detachView();
        mEventBus.unregister(this);
        mUnbinder.unbind();
    }

    private void saveData() {
        if (mCollectionBackfillingFragment == null) {
            return;
        }

        cjjg = mCollectionBackfillingFragment.mInformationChange.getSelectedItem().toString().trim();
        cjbz = mCollectionBackfillingFragment.mCollectionRemark.getText().toString().trim();

        xxbg = mCollectionBackfillingFragment.mCollectionResult.getSelectedItem().toString().trim();
        lxr = mCollectionBackfillingFragment.etLianxir.getText().toString().trim();
        lxfs = mCollectionBackfillingFragment.etLianxifs.getText().toString().trim();

        handleEntity = new CollectionHandleEntity();
        handleEntity.setID((long) (taskBean.getAlbh() + taskBean.getPch()).hashCode());
        handleEntity.setAlbh(taskBean.getAlbh());
        handleEntity.setPch(taskBean.getPch());
        handleEntity.setXh(taskBean.getXh());
        handleEntity.setYhh(taskBean.getYhh());
        handleEntity.setZhbh(taskBean.getZhbh());

        handleEntity.setCjjg(cjjg);
        handleEntity.setCjbz(cjbz);
        handleEntity.setXxbg(xxbg);

        handleEntity.setLxr(lxr);
        handleEntity.setLxfs(lxfs);

        GreenDaoUtils.getDaoSession(this).getCollectionHandleEntityDao().insertOrReplace(handleEntity);
        mEventPosterHelper.postEventSafely(new NotifRefrashDataEvent(true));
    }

    private void submitInfo() {
        if (mCollectionBackfillingFragment == null) {
            ApplicationsUtil.showMessage(this, R.string.text_write_info);
            return;
        }
        saveData();

        if (TextUtils.isEmpty(cjjg)) {
//            ToastUtils.showShort("派送方式不能为空！");
            ToastParams params = new ToastParams();
            params.text = "催缴结果不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }

        if ("其他".equals(cjjg) && TextUtils.isEmpty(cjbz)) {
            if (TextUtils.isEmpty(cjbz)) {
                ToastParams params = new ToastParams();
                params.text = "催缴备注不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
        }

        if ("是".equals(xxbg)) {

            if (TextUtils.isEmpty(lxr)) {
//                    ToastUtils.showShort("联系人不能为空！");
                ToastParams params = new ToastParams();
                params.text = "联系人不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
//            if (!TextUtils.isEmpty(lxr) && StringRegexUtils.isSpecialChar(lxr)) {
////                    ToastUtils.showShort("统一社会信用代码不能为空！");
//                ToastParams params = new ToastParams();
//                params.text = "联系人包含特殊字符，请重新填写！";
//                params.style = new CustomToastStyle(R.layout.toast_error);
//                Toaster.show(params);
//                return;
//            }
            if (TextUtils.isEmpty(lxfs)) {
//                    ToastUtils.showShort("联系方式不能为空！");
                ToastParams params = new ToastParams();
                params.text = "联系方式不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
            if (!TextUtils.isEmpty(lxfs) && (lxfs.length() != 8 && lxfs.length() != 11)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
                ToastParams params = new ToastParams();
                params.text = "请输入正确的联系方式！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
            if (lxr.equals(taskBean.getLxr()) || lxr.equals(taskBean.getDqlxr())) {
//                ToastUtils.showShort("请变更信息后再进行提交！");
                ToastParams params = new ToastParams();
                params.text = "请变更联系人后再进行提交！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
            if (lxfs.equals(taskBean.getDqlxdh()) || lxfs.equals(taskBean.getZylxdh())) {
//                ToastUtils.showShort("请变更信息后再进行提交！");
                ToastParams params = new ToastParams();
                params.text = "请变更联系方式后再进行提交！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
        }

        if (mMultimediaFragment instanceof MultimediaFileFragment) {
            int fileSize = mMultimediaFragment.getFileSize();
            if (fileSize == 0) {
//                ToastUtils.showShort(getResources().getString(R.string.add_file));
                ToastParams params = new ToastParams();
                params.text = getResources().getString(R.string.add_file);
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }

            int pictureSize = mMultimediaFragment.getPictureSize();
            if (pictureSize == 0) {
//                ToastUtils.showShort(getResources().getString(R.string.add_picture));
                ToastParams params = new ToastParams();
                params.text = getResources().getString(R.string.add_picture);
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
        }

        AlertDialog.Builder buildDialog = new AlertDialog.Builder(this);
        buildDialog.setTitle("提示");
        buildDialog.setMessage("您确定要上传数据吗？");
        buildDialog.setCancelable(false);
        buildDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        buildDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ApplicationsUtil.showMessage(HandleOrderActivity.this, "确定");
                mHandleOrderPresenter.commitMyTaskInfo(taskBean, handleEntity);
//                uploadData(psfs, sfsd, wqsyy, wqsyy, ghxdh, qsr, sjrsf, xxbg, tyshxydm, khmc, lxr, lxfs, yjdz, bz);
                dialog.dismiss();
            }
        });
        buildDialog.create().show();
    }

    private void initCollectionInfromationFragment() {
        if (mCollectionInfromationFragment == null) {
            mCollectionInfromationFragment = new CollectionInfromationFragment();
            Bundle bundle = new Bundle();

            bundle.putInt(Constant.COLLECTION_NUM, 1);
            bundle.putParcelable("TaskBean", taskBean);

            mCollectionInfromationFragment.setArguments(bundle);
        }
        changeFragment(mCollectionInfromationFragment);
    }

    private void initMutimediaFragment() {
        if (mMultimediaFragment == null) {
            mMultimediaFragment = new MultimediaFileFragment();
            Bundle bundle = new Bundle();
//            String mTaskId = UUID.randomUUID().toString();
            bundle.putString("param1", taskBean.getAlbh());
            bundle.putInt("param2", Constant.TASK_TYPE_DOWNLOAD);
            bundle.putInt("param3", TaskState.HANDLE);
            bundle.putInt(Constant.COLLECTION_NUM, 1);
            bundle.putInt(Constant.ORIGIN, 11);
            mMultimediaFragment.setArguments(bundle);
        }
        changeFragment(mMultimediaFragment);
    }

    private void initCollectionBackfillingFragment() {
        if (mCollectionBackfillingFragment == null) {
            mCollectionBackfillingFragment = new CollectionBackfillingFragment();
            Bundle bundle = new Bundle();

            bundle.putInt(Constant.COLLECTION_NUM, 1);
            bundle.putParcelable("TaskBean", taskBean);

            mCollectionBackfillingFragment.setArguments(bundle);
        }
        changeFragment(mCollectionBackfillingFragment);

    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    private void changeFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        if (mCurrentFragment != fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);
            }
            if (!fragment.isAdded()) { // 先判断是否被add过
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.delay_order_container, fragment, fragment.getClass().getName()).commit();
            } else {
                transaction.hide(mCurrentFragment).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
            }
            mCurrentFragment = fragment;
        }
    }

    /**
     * 初始化控件
     */
    private void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            mRadioGroup.setOnCheckedChangeListener(this);
        mDetailsRadioBtn.setChecked(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.delay_order_details_rb://详细
                setToolbarTitle("催缴任务-工单详情");
                initCollectionInfromationFragment();
                break;

            case R.id.delay_order_handle_rb://处理
                setToolbarTitle("催缴任务-工单处理");
                initCollectionBackfillingFragment();
                break;

            case R.id.delay_order_multimedia_rb://多媒体
                setToolbarTitle("催缴任务-工单多媒体");
                initMutimediaFragment();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "onActivityResult");
        if (requestCode == MultimediaFileFragment.SNAP_VIDEO) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE, 0);
                if (type == AliyunVideoRecorder.RESULT_TYPE_RECORD) {
                    if (TextUtil.isNullOrEmpty(data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH))) {
                        return;
                    }
                    mEventPosterHelper.postEventSafely(new UIBusEvent.RecordVideo(data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH)));
                }
            }
        }
    }

    @Override
    public void onSignComplete(List<File> uploadFiles) {
        if (handleEntity != null) {
            if ("已催有人".equals(handleEntity.getCjjg())) {
                if (uploadFiles.size() > 0) {
                    mHandleOrderPresenter.loadImages(handleEntity.getAlbh(), Constant.TASK_TYPE_DOWNLOAD_ORDER, TaskState.HANDLE);
                } else {
                    ToastParams params = new ToastParams();
                    params.text = "还没有签名，请签名后再进行操作";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                }
            } else {
                mHandleOrderPresenter.loadImages(handleEntity.getAlbh(), Constant.TASK_TYPE_DOWNLOAD_ORDER, TaskState.HANDLE);
            }
        }
    }

    @Override
    public void onCommitTaskSuccess(String msg) {
        finish();
    }

    @Override
    public void onCommitError(String error) {
//        ToastUtils.showShort(error);
        ToastParams params = new ToastParams();
        params.text = error;
        params.style = new CustomToastStyle(R.layout.toast_error);
        Toaster.show(params);
    }

    @Override
    public void showMessage(String message) {

    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        initLocationOption();
        if (mOrigin == 99) {
            getData();
        }
    }

    private double lat;
    private double lon;
    private String address;
    private LocationClient mLocationClient;

    /**
     * 初始化定位
     */
    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        mLocationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(3000);
        //可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(locationOption);
        //开始定位
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }

            //获取定位结果
            location.getTime();    //获取定位时间
            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.getLocType();    //获取定位类型
//            location.getLatitude();    //获取纬度信息
//            location.getLongitude();    //获取经度信息
            location.getRadius();    //获取定位精准度
            location.getAddrStr();    //获取地址信息
            location.getCountry();    //获取国家信息
            location.getCountryCode();    //获取国家码
            location.getCity();    //获取城市信息
            location.getCityCode();    //获取城市码
            location.getDistrict();    //获取区县信息
            location.getStreet();    //获取街道信息
            location.getStreetNumber();    //获取街道码
            location.getLocationDescribe();    //获取当前位置描述信息
            location.getPoiList();    //获取当前位置周边POI信息

            location.getBuildingID();    //室内精准定位下，获取楼宇ID
            location.getBuildingName();    //室内精准定位下，获取楼宇名称
            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息
            //经纬度
            lat = location.getLatitude();
            lon = location.getLongitude();
            address = location.getCity() + location.getStreet()
                    + location.getStreetNumber() + location.getLocationDescribe();
            Log.e("MapResult", "result=" + location.getAddrStr() + "----" +
                    location.getCity() + "----" + location.getStreet() + "-----" +
                    "----" + location.getLocationDescribe()
                    + location.getStreetNumber());
        }
    }

    public String getAddress() {
        return address;
    }

    private void getData() {
        EasyHttp
                .post(URL.CuJIaoGDSelByALBH)
                .params("cbyid", mDataManager.getAccount())
//                .params("cbyid", "111032")
                .params("albh", mTaskId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new CallBackProxy<CustomApiResult<List<CollectionTaskListBean>>,
                        List<CollectionTaskListBean>>(new CustomCallBack<List<CollectionTaskListBean>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
//                        hideProgress();
                        onCompleted();
//                        llTab.setVisibility(View.GONE);
                        if (e.getCode() == 1010) {
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        } else {
//                            ToastUtils.showShort(e.getMessage());
                            ToastParams params = new ToastParams();
                            params.text = e.getMessage();
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }

                    }

                    @Override
                    public void onSuccess(final List<CollectionTaskListBean> mNetWorkDatas) {
                        LogUtils.e(mNetWorkDatas.toString());
                        if (mNetWorkDatas != null && mNetWorkDatas.size() > 0) {
                            taskBean = mNetWorkDatas.get(0);
                            initFragment();
                        } else {
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }

                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }
                }) {
                });
    }
}
