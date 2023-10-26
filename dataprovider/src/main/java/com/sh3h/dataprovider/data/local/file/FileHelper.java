package com.sh3h.dataprovider.data.local.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.R;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.injection.annotation.ApplicationContext;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.ImageUtil;
import com.sh3h.localprovider.greendaoEntity.VideosPathEntity;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_PICTURE;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_SCREEN_SHOT;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_VOICE;

/**
 * Created by zhangjing on 2016/9/30.
 */
@Singleton
public class FileHelper {
    private static final String TAG = "FileHelper";
    private static final int PHOTO_LOW_QUALITY_WITH = 210;
    private static final int PHOTO_LOW_QUALITY_HEIGHT = 300;
    private static final int PHOTO_MIDDLE_QUALITY_WITH = 420;
    private static final int PHOTO_MIDDLE_QUALITY_HEIGHT = 600;
    private static final int PHOTO_HIGH_QUALITY_WITH = 630;
    private static final int PHOTO_HIGH_QUALITY_HEIGHT = 900;

    private final Context mContext;
    private final ConfigHelper mConfigHelper;
    public static final String FILENAME = "address";
    public static final String FILETIME = "time";
    public static final String UPLOAD = "upload";

    @Inject
    public FileHelper(@ApplicationContext Context context,
                      ConfigHelper configHelper) {
        mContext = context;
        mConfigHelper = configHelper;
    }

    public Observable<Boolean> compressImageAndAddStamp(final String fileName, final File file) {
        return compressImageAndAddStamp(fileName, file, false);
    }

    /**
     * compress a picture and add a stamp on it
     *
     * @param fileName
     * @param file
     * @return
     */
    public Observable<Boolean> compressImageAndAddStamp(final String fileName, final File file, final boolean isHighQulity) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (TextUtil.isNullOrEmpty(fileName)
                            || (file == null)) {
                        throw new NullPointerException("compressImageAndAddStamp contains null parameter");
                    }

                    if (!file.exists()) {
                        subscriber.onError(new Throwable("file isn't existing"));
                        return;
                    }

//                    // 使用鲁班压缩图片
                    Luban.with(BaseApplication.getInstance())
                            .load(file)
                            .ignoreBy(100)
                            .setTargetDir(file.getParentFile().getAbsolutePath())
                            .setRenameListener(new OnRenameListener() {
                                @Override
                                public String rename(String filePaths) {
                                    String mFilename = file.getName();
                                    return mFilename;
                                }
                            })
                            .filter(new CompressionPredicate() {
                                @Override
                                public boolean apply(String path) {
                                    return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                                }
                            })
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    if (file != null) {


                                        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                                        if (bmp == null) {
                                            return;
                                        }

//                                        bmp = addWatermark(bmp);

                                        //对图片添加水印 这里添加一张图片为示例：
                                        bmp = ImageUtil.drawTextToBottomCenter(BaseApplication.getInstance(),bmp, TimeUtils.getNowString(),30, Color.RED,30);
//                                        bmp = ImageUtil.addTextWatermark(bmp, fileName,"111032","","","","1368331100",true);

                                        try {
                                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                                            int quality;
                                            if (isHighQulity) {
                                                quality = 100;
                                            } else {
                                                quality = 50;
                                            }
                                            bmp.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                                            bos.flush();
                                            bos.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }


                                        // 压缩后的图片
                                        //发送广播通知图片增加了
//                                ImagePicker.galleryAddPic(ImageGridActivity.this, file);
//                                //删除系统缩略图
//                                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{image.getAbsolutePath()});
//                                //删除SD中图片
//                                image.delete();
                                        //删除成功后通知图库更新
//                                if (!image.exists()) {
//                                    String where = MediaStore.Audio.Media.DATA + " like \"" + image.getAbsolutePath() + "%" + "\"";
//                                    getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null);
//                                }
//                                finishPhoto(file.getAbsolutePath());
                                    } else {
//                                finishPhoto(path);
                                    }
//                            EventBus.getDefault().post(new YasuoSuccessEntity(imagePicker.getSelectedImages()));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过程出现问题时调用
//                                    finishPhoto(path);
//                            EventBus.getDefault().post(new YasuoSuccessEntity(imagePicker.getSelectedImages()));
                                }
                            }).launch();


                    //缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(file.getPath(), options);
//                    options.inSampleSize = calculateInSampleSize(options,
//                            PHOTO_MIDDLE_QUALITY_WITH, PHOTO_MIDDLE_QUALITY_HEIGHT);
//                    options.inJustDecodeBounds = false;
//                    Bitmap fBitmap = BitmapFactory.decodeFile(file.getPath(), options);
//                    if (fBitmap == null) {
//                        subscriber.onError(new Throwable("failure to decode the picture"));
//                        return;
//                    }

//                     rotate the bitmap
//                    int degree = getBitmapDegree(file.getPath());
//                    if (degree != 0) {
//                        fBitmap = rotateBitmapByDegree(fBitmap, degree);
//                    }
//
//                    // add the stamp
//                    fBitmap = addWatermark(fBitmap);
//
//                    // save image
//                    if (!saveImage(fBitmap, file.getPath())) {
//                        subscriber.onError(new Throwable("failure to save the picture"));
//                        return;
//                    }

                    subscriber.onNext(true);
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * compress a picture and add a stamp on it
     *
     * @param fileName
     * @param file
     * @return
     */
    public Observable<Boolean> compressImageAndAddStamp(final String fileName, final File file, final boolean isHighQulity, final String address) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (TextUtil.isNullOrEmpty(fileName)
                            || (file == null)) {
                        throw new NullPointerException("compressImageAndAddStamp contains null parameter");
                    }

                    if (!file.exists()) {
                        subscriber.onError(new Throwable("file isn't existing"));
                        return;
                    }

//                    // 使用鲁班压缩图片
                    Luban.with(BaseApplication.getInstance())
                            .load(file)
                            .ignoreBy(100)
                            .setTargetDir(file.getParentFile().getAbsolutePath())
                            .setRenameListener(new OnRenameListener() {
                                @Override
                                public String rename(String filePaths) {
                                    String mFilename = file.getName();
                                    return mFilename;
                                }
                            })
                            .filter(new CompressionPredicate() {
                                @Override
                                public boolean apply(String path) {
                                    return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                                }
                            })
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    if (file != null) {


                                        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                                        if (bmp == null) {
                                            return;
                                        }

//                                        bmp = addWatermark(bmp);

                                        //对图片添加水印 这里添加一张图片为示例：
                                        bmp = ImageUtil.addTextWatermarkBottomCenter(BaseApplication.getInstance(),bmp, address,true);
//                                        bmp = ImageUtil.addTextWatermark(bmp, fileName,"111032","","","","1368331100",true);

                                        try {
                                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                                            int quality;
                                            if (isHighQulity) {
                                                quality = 100;
                                            } else {
                                                quality = 50;
                                            }
                                            bmp.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                                            bos.flush();
                                            bos.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }


                                        // 压缩后的图片
                                        //发送广播通知图片增加了
//                                ImagePicker.galleryAddPic(ImageGridActivity.this, file);
//                                //删除系统缩略图
//                                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{image.getAbsolutePath()});
//                                //删除SD中图片
//                                image.delete();
                                        //删除成功后通知图库更新
//                                if (!image.exists()) {
//                                    String where = MediaStore.Audio.Media.DATA + " like \"" + image.getAbsolutePath() + "%" + "\"";
//                                    getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null);
//                                }
//                                finishPhoto(file.getAbsolutePath());
                                    } else {
//                                finishPhoto(path);
                                    }
//                            EventBus.getDefault().post(new YasuoSuccessEntity(imagePicker.getSelectedImages()));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过程出现问题时调用
//                                    finishPhoto(path);
//                            EventBus.getDefault().post(new YasuoSuccessEntity(imagePicker.getSelectedImages()));
                                }
                            }).launch();


                    //缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(file.getPath(), options);
//                    options.inSampleSize = calculateInSampleSize(options,
//                            PHOTO_MIDDLE_QUALITY_WITH, PHOTO_MIDDLE_QUALITY_HEIGHT);
//                    options.inJustDecodeBounds = false;
//                    Bitmap fBitmap = BitmapFactory.decodeFile(file.getPath(), options);
//                    if (fBitmap == null) {
//                        subscriber.onError(new Throwable("failure to decode the picture"));
//                        return;
//                    }

//                     rotate the bitmap
//                    int degree = getBitmapDegree(file.getPath());
//                    if (degree != 0) {
//                        fBitmap = rotateBitmapByDegree(fBitmap, degree);
//                    }
//
//                    // add the stamp
//                    fBitmap = addWatermark(fBitmap);
//
//                    // save image
//                    if (!saveImage(fBitmap, file.getPath())) {
//                        subscriber.onError(new Throwable("failure to save the picture"));
//                        return;
//                    }

                    subscriber.onNext(true);
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 删除文件
     *
     * @param duMedia
     * @return
     */
    public Observable<Boolean> deleteFile(final DUMedia duMedia) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (TextUtil.isNullOrEmpty(duMedia.getFilePath())) {
                        throw new NullPointerException("param is null");
                    }

                    File file = new File(duMedia.getFilePath());
                    if (duMedia.getFileType() == DUMedia.FILE_TYPE_SCREEN_SHOT
                            && (!TextUtil.isNullOrEmpty(duMedia.getExtend()))) {
                        Gson gson = new Gson();
                        VideosPathEntity pathEntity = gson.fromJson(duMedia.getExtend(), VideosPathEntity.class);
                        File extendFile = new File(pathEntity.getVideoPath());
                        if (/*file.exists() || */extendFile.exists()) {
                            subscriber.onNext(/*file.delete() &&*/ extendFile.delete());
                        } else {
                            subscriber.onNext(false);
                        }
                    } else {
                        if (file.exists()) {
                            subscriber.onNext(file.delete());
                        } else {
                            subscriber.onNext(false);
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 读取图片旋转的角度
     *
     * @param path
     * @return
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */

    private Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }

        if (returnBm == null) {
            returnBm = bm;
        }

        if (bm != returnBm) {
            bm.recycle();
        }

        return returnBm;
    }

    /**
     * 添加水印
     *
     * @param bitmap
     * @return
     */

    private Bitmap addWatermark(Bitmap bitmap) {
        Bitmap icon = null;
        try {
            int width = bitmap.getWidth();
            int hight = bitmap.getHeight();
            icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(icon);
            Paint photoPaint = new Paint();
            photoPaint.setDither(true);
            photoPaint.setFilterBitmap(true);
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect dst = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, dst, photoPaint);
            Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
            textPaint.setTextSize(15.0f);
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
            textPaint.setColor(Color.RED);
            textPaint.setAlpha(150);
            String t = TextUtil.format(System.currentTimeMillis(), TextUtil.FORMAT_FULL_DATETIME);
            //Date date = new Date(System.currentTimeMillis());
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //canvas.drawText(sdf.format(date), (float) (width * 0.19), (float) (hight * 0.97), textPaint);
            // canvas.drawText(t, (float) (width * 0.19), (float) (hight * 0.04), textPaint);
            canvas.drawText(t, (float) (width * 0.19), (float) (hight * 0.03), textPaint);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();


        } catch (Exception e) {
            e.printStackTrace();
            icon = bitmap;
        }

        return icon;
    }



    /**
     * save the image
     *
     * @param bitmap
     */
    private boolean saveImage(Bitmap bitmap, String filePath) {
        if ((bitmap == null) || (filePath == null)) {
            return false;
        }

        try {
            File file = new File(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            int quality;
            if (mConfigHelper.isPhotoQualityHigh()) {
                quality = 100;
            } else {
                quality = 50;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 压缩图片
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                      int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 保存图片
     *
     * @param bitmap
     */
    public boolean savePNG(Bitmap bitmap, String path) {
        if (bitmap == null || TextUtil.isNullOrEmpty(path)) {
            return false;
        }
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveJPG(Bitmap bitmap, String path) {
        return saveJPG(bitmap, path, false);
    }

    public boolean saveJPG(Bitmap bitmap, String path, boolean isHighQulity) {
        if (bitmap == null || TextUtil.isNullOrEmpty(path)) {
            return false;
        }
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (isHighQulity) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除文件(定期)
     *
     * @return
     */
    public Observable<Boolean> deleteFileRegular(final DUMedia duMedia) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    File filePath = null;
                    switch (duMedia.getFileType()) {
                        case FILE_TYPE_PICTURE:
                            filePath = new File(mConfigHelper.getImageFolderPath(),
                                    duMedia.getFileName());
                            break;
                        case FILE_TYPE_VOICE:
                            filePath = new File(mConfigHelper.getSoundFolderPath(),
                                    duMedia.getFileName());
                            break;
                        case FILE_TYPE_SCREEN_SHOT:
                            filePath = new File(mConfigHelper.getVideoFolderPath(),
                                    duMedia.getFileName());
                            break;
                    }
                    duMedia.setFilePath(filePath.getPath());
                    if (TextUtil.isNullOrEmpty(duMedia.getFilePath())) {
                        throw new NullPointerException("param is null");
                    }
                    File file = new File(duMedia.getFilePath());

                    if (duMedia.getFileType() == DUMedia.FILE_TYPE_SCREEN_SHOT
                            && (!TextUtil.isNullOrEmpty(duMedia.getExtend()))) {
                        String videoPath = "";
                        try {
                            JSONObject jsonObject = new JSONObject(duMedia.getExtend());
                            videoPath = (String) jsonObject.get(Constant.VIDEO_PATH);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        File extendFile = new File(videoPath);
                        if (file.exists() && extendFile.exists()) {
                            subscriber.onNext(file.delete() && extendFile.delete());
                        } else {
                            subscriber.onNext(false);
                        }
                    } else {
                        if (file.exists()) {
                            subscriber.onNext(file.delete());
                        } else {
                            subscriber.onNext(false);
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }
}
