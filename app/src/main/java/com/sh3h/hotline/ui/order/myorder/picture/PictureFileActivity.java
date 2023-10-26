package com.sh3h.hotline.ui.order.myorder.picture;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.response.DUFileRemote;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.IllegalWaterEntity;
import com.sh3h.hotline.entity.QuestionWaterEntity;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.multimedia.PictureAdapter;
import com.sh3h.hotline.ui.multimedia.PictureDetailsActivity;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PictureFileActivity extends ParentActivity implements PictureFileMvpView,
        View.OnClickListener {

    @Inject
    PictureFilePresenter mMultimediaPresenter;

    @BindView(R.id.multimedia_scrollview)
    ScrollView mScrollView;

    @BindView(R.id.ll_show_picture)
    LinearLayout llShowPicture;

    @BindView(R.id.multimedia_take_photo_ll)
    LinearLayout mTakePhoto;

    @BindView(R.id.inter_gv_picture)
    GridView mInterGridImages;

    @BindView(R.id.multimedia_gv_picture)
    GridView mGridImages;

    @Inject
    Bus mEventBus;

    @Inject
    EventPosterHelper mEventPosterHelper;

    private final static String TAG = "PictureFileActivity";
    private final static int PHOTOS_FULL_SIZE = 3;//照片最大容量
    private static final int CAPTURE_IMAGE = 1000;
    private static final int CAPTURE_ALBUM = 1001;

    private Unbinder mUnbinder;
    private MenuItem menuItem;

    public PictureAdapter mPictureAdapter; //照片适配器
    public PictureAdapter mInterPictureAdapter; //照片适配器
    private List<DUMedia> mPictureList = new ArrayList<>(); // 照片集合
    private List<DUMedia> mInterPictureList = new ArrayList<>(); // 照片集合
    private String mImageName;

    private String taskId;
    private String type;
    private int taskType;
    private int taskState;
    private boolean mIsUploadFlag;

    private int mOrigin;
    private ArrayList<DUFileRemote> mDuFileRemotes;
    private PopupWindow mPopwindow;
    private DUMyTask mDuMyTask;
    private String mNewPath;
    private String currentTime = "";
    private boolean mIsDataUpload;

    private IllegalWaterEntity illegalWaterEntity;
    private QuestionWaterEntity questionWaterEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_file);
        getActivityComponent().inject(this);
        mEventBus.register(this);
        mUnbinder = ButterKnife.bind(this);
        mMultimediaPresenter.attachView(this);

        if (getIntent() != null) {
            taskId = getIntent().getStringExtra("taskId");
            type = getIntent().getStringExtra("type");
            taskType = getIntent().getIntExtra("taskType", -1);
            taskState = getIntent().getIntExtra("taskState", -1);
            mOrigin = getIntent().getIntExtra(Constant.ORIGIN, 0);
            illegalWaterEntity = getIntent().getParcelableExtra("illegalTaskBean");
            questionWaterEntity = getIntent().getParcelableExtra("questionTaskBean");
        }

        if ("xchj".equals(type)) {
            initToolBar(R.string.title_picture_xchj);
        } else if ("bp".equals(type)) {
            initToolBar(R.string.title_picture_bp);
        } else {
            initToolBar(R.string.radio_button_media);
        }

        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mMultimediaPresenter.detachView();
        mEventBus.unregister(this);
        LogUtil.i(TAG, "onDestroy");
    }

    public int getPictureSize() {
        return mPictureList.size();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        menuItem = menu.findItem(R.id.action_save);
        menuItem.setTitle("");
        menuItem.setIcon(R.drawable.ic_baseline_add_photo_alternate_24);
        menuItem.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (!isNeedFufwdBH())
                choosePictures();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.multimedia_take_photo_ll:
                if (!isNeedFufwdBH())
                    choosePictures();
                break;
            case R.id.button_take_photo:
                takePhoto(false);
                break;
            case R.id.button_choose_photo:
                takePhoto(true);
                break;
            case R.id.button_choose_cancel:
                dismissPop();
                break;
            default:
                break;
        }
    }

    private boolean isNeedFufwdBH() {
        return false;
    }

    private void dismissPop() {
        if (mPopwindow != null && mPopwindow.isShowing()) {
            mPopwindow.dismiss();
        }
    }

    private void choosePictures() {

        //判断照片是否已满
        if (mPictureList.size() >= PHOTOS_FULL_SIZE) {
            ToastUtils.showShort(
                    R.string.text_pictures_full);
            return;
        }
        View popupView = getLayoutInflater().inflate(R.layout.pop_choose_picture, null);
        Button takePhoto = (Button) popupView.findViewById(R.id.button_take_photo);
        Button pickPhoto = (Button) popupView.findViewById(R.id.button_choose_photo);
        Button cancel = (Button) popupView.findViewById(R.id.button_choose_cancel);
//        mPopwindow = new PopupWindow(popupView, mMainLayout.getWidth(),
//                600);
        mPopwindow = new PopupWindow(popupView,
                ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        mPopwindow.setAnimationStyle(R.style.txt_camera_pop_menu);
        mPopwindow.setFocusable(true);
//        mPopwindow.setBackgroundDrawable(new ColorDrawable());
        mPopwindow.showAtLocation(mScrollView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        takePhoto.setOnClickListener(this);
        pickPhoto.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dismissPop();
        if ((CAPTURE_IMAGE == requestCode)
//                && (data != null)
                && (mImageName != null)) {
            File folder = mMultimediaPresenter.getImagePath();
            File file = new File(folder, mImageName);
            if (!file.exists()) {
                LogUtil.e(TAG, String.format("%s isn't existing", mImageName));
                return;
            }
            //保存照片-压缩
//            mMultimediaPresenter.saveNewImage(taskId, taskType, taskState, mImageName, file, false, null);
            LogUtils.e("taskId", taskId + currentTime);
            if ("xchj".equals(type)) {
                mMultimediaPresenter.saveNewImage(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE_XCHJZP, mImageName, file, false, null);
            } else if ("bp".equals(type)) {
                mMultimediaPresenter.saveNewImage(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE_BPZP, mImageName, file, false, null);
            } else {
                mMultimediaPresenter.saveNewImage(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE, mImageName, file, false, null);
            }
        } else if (requestCode == CAPTURE_ALBUM
                && data != null) {
            Uri originalUri = data.getData();
            LogUtil.i(TAG, "save albums");
            ContentResolver resolver = getContentResolver();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                File file = new File(mMultimediaPresenter.getImagePath(), mImageName);
                //保存照片-压缩
                if ("xchj".equals(type)) {
                    mMultimediaPresenter.saveNewImage(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE_XCHJZP, mImageName, file, false, null);
                } else if ("bp".equals(type)) {
                    mMultimediaPresenter.saveNewImage(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE_BPZP, mImageName, file, false, null);
                } else {
                    mMultimediaPresenter.saveNewImage(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE, mImageName, file, false, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoadImages(List<DUMedia> duMediaList) {
        if ((duMediaList == null) || (duMediaList.size() <= 0)) {
            mPictureList.clear();
        } else {
            mPictureList = duMediaList;
        }

        mPictureAdapter.setData(mPictureList);
    }

    @Override
    public void onSaveNewImage(Boolean aBoolean) {
        if (aBoolean) {
            ToastUtils.showShort(R.string.text_save_success);
//            mMultimediaPresenter.loadImages(taskId, taskType, taskState);
            if ("xchj".equals(type)) {
                mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE_XCHJZP);
            } else if ("bp".equals(type)) {
                mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE_BPZP);
            } else {
                mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE);
            }

            mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
        } else {
            ToastUtils.showShort(R.string.text_save_fail);
        }
    }

    @Override
    public void onDeleteImage(Boolean aBoolean, DUMedia duMedia) {
        if (aBoolean && (duMedia != null)) {
            ToastUtils.showShort(R.string.text_delete_success);
            mPictureList.remove(duMedia);
            mPictureAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShort(R.string.text_delete_failure);
        }
    }

    @Override
    public void onError(String error) {
        if (!TextUtil.isNullOrEmpty(error)) {
            ToastUtils.showShort(error);
        }
    }

    public void setTaskId(String newTaskId) {
        if (!StringUtils.isEmpty(newTaskId))
            taskId = newTaskId;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        if (mDuMyTask != null) {
            taskId = mDuMyTask.getCaseId();
        }
        if (mIsDataUpload) {
            mTakePhoto.setOnClickListener(null);
        } else {
            mTakePhoto.setOnClickListener(this);
        }
        initGridViewPictures();
        initGridViewInterPictures();
    }


    /**
     * 初始化照片列表
     */
    private void initGridViewPictures() {
        mPictureAdapter = new PictureAdapter(this, new ArrayList<>());
        mGridImages.setAdapter(mPictureAdapter);

        //点击显示幻灯片
        mGridImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPictureDetails(position);
            }
        });

        //长按删除照片
        mGridImages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
                    return true;
                }
                if (!mIsDataUpload) {
                    showDialog(DUMedia.FILE_TYPE_PICTURE, position);
                }
                return true;
            }
        });
        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            if (mDuFileRemotes != null && mDuFileRemotes.size() > 0) {
                List<DUFileRemote> dufileRemotes = new ArrayList<>();
                for (DUFileRemote duFileRemote : mDuFileRemotes) {
                    if (DUMedia.PICTURE_SUFFIX.equals(duFileRemote.getFileType())) {
                        dufileRemotes.add(duFileRemote);
                    }
                }
                mPictureAdapter.setData(dufileRemotes);
            }
        } else {
            if (taskType != Constant.TASK_TYPE_CREATE_SELF_ORDER) {
                if ("xchj".equals(type)) {
                    mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE_XCHJZP);
                } else if ("bp".equals(type)) {
                    mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE_BPZP);
                } else {
                    mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE);
                }
            }
        }
    }

    /**
     * 初始化照片列表
     */
    private void initGridViewInterPictures() {
        mInterPictureAdapter = new PictureAdapter(this, new ArrayList<>());
        mInterGridImages.setAdapter(mInterPictureAdapter);

        //点击显示幻灯片
        mInterGridImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showInterPictureDetails(position);
            }
        });

        List<DUFileRemote> dufileRemotes = new ArrayList<>();
        if ("xchj".equals(type)) {
            if (mOrigin == 22) {
                if (!TextUtils.isEmpty(illegalWaterEntity.getXczp1())) {
                    DUFileRemote duFileRemote1 = new DUFileRemote();
                    duFileRemote1.setFileUrl(illegalWaterEntity.getXczp1());
                    duFileRemote1.setFileType(DUMedia.PICTURE_SUFFIX);
                    dufileRemotes.add(duFileRemote1);

                    DUMedia dUMedia1 = new DUMedia();
                    dUMedia1.setFilePath(illegalWaterEntity.getXczp1());
                    mInterPictureList.add(dUMedia1);
                }

                if (!TextUtils.isEmpty(illegalWaterEntity.getXczp2())) {
                    DUFileRemote duFileRemote2 = new DUFileRemote();
                    duFileRemote2.setFileUrl(illegalWaterEntity.getXczp2());
                    duFileRemote2.setFileType(DUMedia.PICTURE_SUFFIX);
                    dufileRemotes.add(duFileRemote2);

                    DUMedia dUMedia2 = new DUMedia();
                    dUMedia2.setFilePath(illegalWaterEntity.getXczp2());
                    mInterPictureList.add(dUMedia2);
                }

                if (!TextUtils.isEmpty(illegalWaterEntity.getXczp3())) {
                    DUFileRemote duFileRemote3 = new DUFileRemote();
                    duFileRemote3.setFileUrl(illegalWaterEntity.getXczp3());
                    duFileRemote3.setFileType(DUMedia.PICTURE_SUFFIX);
                    dufileRemotes.add(duFileRemote3);

                    DUMedia dUMedia3 = new DUMedia();
                    dUMedia3.setFilePath(illegalWaterEntity.getXczp3());
                    mInterPictureList.add(dUMedia3);
                }
            } else if (mOrigin == 21) {
                if (!TextUtils.isEmpty(questionWaterEntity.getXczp1())) {
                    DUFileRemote duFileRemote1 = new DUFileRemote();
                    duFileRemote1.setFileUrl(questionWaterEntity.getXczp1());
                    duFileRemote1.setFileType(DUMedia.PICTURE_SUFFIX);
                    dufileRemotes.add(duFileRemote1);

                    DUMedia dUMedia1 = new DUMedia();
                    dUMedia1.setFilePath(questionWaterEntity.getXczp1());
                    mInterPictureList.add(dUMedia1);
                }

                if (!TextUtils.isEmpty(questionWaterEntity.getXczp1())) {
                    DUFileRemote duFileRemote2 = new DUFileRemote();
                    duFileRemote2.setFileUrl(questionWaterEntity.getXczp2());
                    duFileRemote2.setFileType(DUMedia.PICTURE_SUFFIX);
                    dufileRemotes.add(duFileRemote2);

                    DUMedia dUMedia2 = new DUMedia();
                    dUMedia2.setFilePath(questionWaterEntity.getXczp2());
                    mInterPictureList.add(dUMedia2);
                }

                if (!TextUtils.isEmpty(questionWaterEntity.getXczp1())) {
                    DUFileRemote duFileRemote3 = new DUFileRemote();
                    duFileRemote3.setFileUrl(questionWaterEntity.getXczp3());
                    duFileRemote3.setFileType(DUMedia.PICTURE_SUFFIX);
                    dufileRemotes.add(duFileRemote3);

                    DUMedia dUMedia3 = new DUMedia();
                    dUMedia3.setFilePath(questionWaterEntity.getXczp3());
                    mInterPictureList.add(dUMedia3);
                }
            }
        } else if ("bp".equals(type)) {
            if (mOrigin == 21) {
                if (!TextUtils.isEmpty(questionWaterEntity.getBpzp())) {
                    DUFileRemote duFileRemote1 = new DUFileRemote();
                    duFileRemote1.setFileUrl(questionWaterEntity.getBpzp());
                    duFileRemote1.setFileType(DUMedia.PICTURE_SUFFIX);
                    dufileRemotes.add(duFileRemote1);

                    DUMedia dUMedia = new DUMedia();
                    dUMedia.setFilePath(questionWaterEntity.getBpzp());
                    mInterPictureList.add(dUMedia);
                }
            }
        }
        mInterPictureAdapter.setData(dufileRemotes);

        if (dufileRemotes.size() > 0) {
            llShowPicture.setVisibility(View.VISIBLE);
        }else{
            llShowPicture.setVisibility(View.GONE);
        }
    }

    /**
     * 拍照/选取相册
     */
    private void takePhoto(boolean isFromAlbums) {
        try {
            //判断照片是否已满
            if (mPictureList.size() >= PHOTOS_FULL_SIZE) {
                ToastUtils.showShort(
                        R.string.text_pictures_full);
                return;
            }

            File folder = mMultimediaPresenter.getImagePath();
            if (!folder.exists()) {
                folder.mkdirs();
            }

            mImageName = "PIC_" + taskId + "_" + System.currentTimeMillis() + ".jpg";

            if (!isFromAlbums) {
                File file = new File(folder, mImageName);
                Uri uri = null;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {//7.0以上
                    uri = Uri.fromFile(file);
                } else {
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        uri = FileProvider.getUriForFile(this, "com.sh3h.hotline.fileprovider", file);
                    }
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAPTURE_IMAGE);
            } else {
                //相册
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, CAPTURE_ALBUM);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.getMessage());
        }
    }

    /**
     * 这是兼容的 AlertDialog
     */
    private void showDialog(int fileType, final int position) {
        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT || mIsUploadFlag) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.text_request));
        builder.setNegativeButton(getString(R.string.text_negative), null);
        switch (fileType) {
            case DUMedia.FILE_TYPE_PICTURE:
                builder.setMessage(getString(R.string.text_is_delete_photo));
                builder.setPositiveButton(getString(R.string.text_positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if ((position >= 0) && (position < mPictureList.size())) {
                                    mMultimediaPresenter.deleteImage(mPictureList.get(position));
                                }
                            }
                        });
                break;
            default:
                break;
        }
        builder.show();
    }

    /**
     * 展示照片详情-幻灯片
     *
     * @param position
     */
    private void showPictureDetails(int position) {
        ArrayList<String> urlList = new ArrayList<>();
        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            if (mDuFileRemotes != null && mDuFileRemotes.size() > 0) {
                for (DUFileRemote duFileRemote : mDuFileRemotes) {
                    if (DUMedia.PICTURE_SUFFIX.equals(duFileRemote.getFileType())) {
                        if (!TextUtil.isNullOrEmpty(duFileRemote.getFileUrl())) {
                            urlList.add(duFileRemote.getFileUrl());
                        }
                    }
                }
            }
        } else {
            if ((mPictureList != null) && (mPictureList.size() > 0)) {
                for (DUMedia duMedia : mPictureList) {
                    if (!TextUtil.isNullOrEmpty(duMedia.getFilePath())) {
                        urlList.add(duMedia.getFilePath());
                    }
                }
            }
        }
        if (urlList.size() > 0) {
            Intent intent = new Intent(this, PictureDetailsActivity.class);
            intent.putExtra(Constant.URLS, urlList);
            intent.putExtra(Constant.POSITION, position);
            startActivity(intent);
        }
    }

    /**
     * 展示照片详情-幻灯片
     *
     * @param position
     */
    private void showInterPictureDetails(int position) {
        ArrayList<String> urlList = new ArrayList<>();
        if ((mInterPictureList != null) && (mInterPictureList.size() > 0)) {
            for (DUMedia duMedia : mInterPictureList) {
                if (!TextUtil.isNullOrEmpty(duMedia.getFilePath())) {
                    urlList.add(duMedia.getFilePath());
                }
            }
        }
        if (urlList.size() > 0) {
            Intent intent = new Intent(this, PictureDetailsActivity.class);
            intent.putExtra(Constant.URLS, urlList);
            intent.putExtra(Constant.POSITION, position);
            startActivity(intent);
        }
    }

    public void notifyData(String taskId, int taskType, int taskState) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.taskState = taskState;

        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            if (mDuFileRemotes != null && mDuFileRemotes.size() > 0) {
                List<DUFileRemote> dufileRemotes = new ArrayList<>();
                for (DUFileRemote duFileRemote : mDuFileRemotes) {
                    if (DUMedia.PICTURE_SUFFIX.equals(duFileRemote.getFileType())) {
                        dufileRemotes.add(duFileRemote);
                    }
                }
                mPictureAdapter.setData(dufileRemotes);
            }
        } else {
            if (taskType != Constant.TASK_TYPE_CREATE_SELF_ORDER) {
                if (mMultimediaPresenter != null) {
                    if ("xchj".equals(type)) {
                        mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE_XCHJZP);
                    } else if ("bp".equals(type)) {
                        mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE_BPZP);
                    } else {
                        mMultimediaPresenter.loadImages(taskId + currentTime, taskType, taskState, DUMedia.FILE_TYPE_PICTURE);
                    }
                }
            }
        }
    }
}