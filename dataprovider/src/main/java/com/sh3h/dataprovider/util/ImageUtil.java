package com.sh3h.dataprovider.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.blankj.utilcode.util.DeviceUtils.getAndroidID;

public class ImageUtil {

    /**
     * 绘制文字到上方中间
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToTopCenter(Context context, Bitmap bitmap, String text, int size, int color, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                (bitmap.getWidth() - bounds.width()) / 2,
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 绘制文字到下方中间
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToBottomCenter(Context context, Bitmap bitmap, String text, int size, int color, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                (bitmap.getWidth() - bounds.width()) / 2,
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 给一张Bitmap添加水印文字。
     *
     * @param src     源图片
     *                //@param content  水印文本
     * @param recycle 是否回收
     * @return 已经添加水印后的Bitmap。
     */
    public static Bitmap addTextWatermarkBottomCenter(Context context,Bitmap src, String address, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        String str = TimeUtils.getNowString();

        android.graphics.Bitmap.Config bitmapConfig = src.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        Bitmap ret = src.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true); // 获取更清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        paint.setColor(Color.RED);
        paint.setTextSize(dp2px(context, 30));
        Rect bounds = new Rect();
        paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (ret.getWidth() - bounds.width()) / 2,
                ret.getHeight() - dp2px(context, 25), paint);

        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setDither(true); // 获取更清晰的图像采样
        paint1.setFilterBitmap(true);// 过滤一些
        paint1.setColor(Color.RED);
        paint1.setTextSize(dp2px(context, 20));
        Rect bounds1 = new Rect();
        paint1.getTextBounds(address, 0, address.length(), bounds1);
        canvas.drawText(address, (ret.getWidth() - bounds1.width()) / 2,
                ret.getHeight() - dp2px(context, 65), paint1);

        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return ret;
    }

    /**
     * 设置水印图片在左上角
     *
     * @param context     上下文
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskLeftTop(Context context, Bitmap src, Bitmap watermark, int paddingLeft, int paddingTop) {
        return createWaterMaskBitmap(src, watermark,
                dp2px(context, paddingLeft), dp2px(context, paddingTop));
    }

    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark, int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
        // 保存
        canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        canvas.restore();
        return newb;
    }

    /**
     * 设置水印图片在右下角
     *
     * @param context       上下文
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskRightBottom(Context context, Bitmap src, Bitmap watermark, int paddingRight, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark,
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight),
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 设置水印图片到右上角
     *
     * @param context
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskRightTop(Context context, Bitmap src, Bitmap watermark, int paddingRight, int paddingTop) {
        return createWaterMaskBitmap(src, watermark,
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight),
                dp2px(context, paddingTop));
    }

    /**
     * 设置水印图片到左下角
     *
     * @param context
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskLeftBottom(Context context, Bitmap src, Bitmap watermark, int paddingLeft, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark, dp2px(context, paddingLeft),
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 设置水印图片到中间
     *
     * @param src
     * @param watermark
     * @return
     */
    public static Bitmap createWaterMaskCenter(Bitmap src, Bitmap watermark) {
        return createWaterMaskBitmap(src, watermark,
                (src.getWidth() - watermark.getWidth()) / 2,
                (src.getHeight() - watermark.getHeight()) / 2);
    }

    /**
     * 给图片添加文字到左上方
     *
     * @param context
     * @param bitmap
     * @param text
     * @return
     */
    public static Bitmap drawTextToLeftTop(Context context, Bitmap bitmap, String text, int size, int color, int paddingLeft, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft),
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 绘制文字到右下方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToRightBottom(Context context, Bitmap bitmap, String text, int size, int color, int paddingRight, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight),
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 绘制文字到右上方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap drawTextToRightTop(Context context, Bitmap bitmap, String text, int size, int color, int paddingRight, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight),
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 绘制文字到左下方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text, int size, int color, int paddingLeft, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft),
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 绘制文字到中间
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToCenter(Context context, Bitmap bitmap, String text, int size, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                (bitmap.getWidth() - bounds.width()) / 2,
                (bitmap.getHeight() + bounds.height()) / 2);
    }

    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text, Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    /**
     * 缩放图片
     *
     * @param src
     * @param w
     * @param h
     * @return
     */
    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    /**
     * dip转pix
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 加载本地图片 http://bbs.3gstdy.com
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            Bitmap bmp = BitmapFactory.decodeFile(url);
            return bmp;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 照片旋转方法
     *
     * @param map
     * @param rotate
     * @return
     */
    public static Bitmap setRotate(Bitmap map, int rotate) {
        Matrix matrix = new Matrix();
        // 设置旋转角度
        matrix.setRotate(rotate);
        // 重新绘制Bitmap
        map = Bitmap.createBitmap(map, map.getWidth() / 10, 0, map.getWidth() * 8 / 10, map.getHeight(), matrix, true);
        return map;
    }

    /**
     * 给一张Bitmap添加水印文字。
     *
     * @param src     源图片
     *                //@param content  水印文本
     * @param recycle 是否回收
     * @return 已经添加水印后的Bitmap。
     */
    public static Bitmap addTextWatermark(Bitmap src, String strname, String newjyyxm, String clsbdh,
                                   String hphm, String hpzl, String jylsh, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        String str = TimeUtils.getNowString();
        String sjimei = "imei:"
                + getUniqueDeviceId();
        //将sjimei字符串转大写
        StringBuffer sb = new StringBuffer();
        if (sjimei != null) {
            for (int i = 0; i < sjimei.length(); i++) {
                char c = sjimei.charAt(i);
                sb.append(Character.toUpperCase(c));
            }
        }
        sjimei = sb.toString();
        Bitmap ret = src.copy(src.getConfig(), true);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Canvas canvas = new Canvas(ret);
        paint.setColor(Color.RED);
        paint.setTextSize(45.0f);
        Rect bounds = new Rect();
        paint.getTextBounds(strname, 0, strname.length(), bounds);

        canvas.drawText("照片名称：" + strname + "  时间：" + str, 15, 25, paint);// 绘制上去字，开始未知x,y采用那只笔绘制
        canvas.drawText(sjimei + "  检验员：" + newjyyxm, 15, 50, paint);
        canvas.drawText("案例编号：" + jylsh, 15, 75, paint);
//        canvas.drawText("车牌：" + hphm + "  种类：" + hpzl, 15, 100, paint);
//        canvas.drawText("型号：" + clsbdh, 15, 125, paint);

        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return ret;
    }

    /**
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * 保存图片到文件File。
     *
     * @param src      源图片
     * @param path     要保存到的文件
     * @param //format 保存格式（PNG、JPEG、webp）
     * @param recycle  是否回收
     * @return true 成功 false 失败
     */
    public boolean save(Bitmap src, File path, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return false;
        }
        OutputStream os;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(path));
            ret = src.compress(Bitmap.CompressFormat.JPEG, 100, os);
            if (recycle && !src.isRecycled())
                src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static final String KEY_UDID = "KEY_UDID";
    private volatile static String udid;

    public static String getUniqueDeviceId() {
        return getUniqueDeviceId("", true);
    }

    public static String getUniqueDeviceId(String prefix, boolean useCache) {
        if (!useCache) {
            return getUniqueDeviceIdReal(prefix);
        }
        if (udid == null) {
            synchronized (DeviceUtils.class) {
                if (udid == null) {
                    final String id = getSpUtils4Utils().getString(KEY_UDID, null);
                    if (id != null) {
                        udid = id;
                        return udid;
                    }
                    return getUniqueDeviceIdReal(prefix);
                }
            }
        }
        return udid;
    }

    private static String getUniqueDeviceIdReal(String prefix) {
        try {
            final String androidId = getAndroidID();
            if (!TextUtils.isEmpty(androidId)) {
                return saveUdid(prefix + 2, androidId);
            }

        } catch (Exception ignore) {/**/}
        return saveUdid(prefix + 9, "");
    }

    private static String saveUdid(String prefix, String id) {
        udid = getUdid(prefix, id);
        getSpUtils4Utils().put(KEY_UDID, udid);
        return udid;
    }

    private static String getUdid(String prefix, String id) {
        if (id.equals("")) {
            return prefix + UUID.randomUUID().toString().replace("-", "");
        }
        return prefix + UUID.nameUUIDFromBytes(id.getBytes()).toString().replace("-", "");
    }

    private static SPUtils getSpUtils4Utils() {
        return SPUtils.getInstance("Utils");
    }
}
