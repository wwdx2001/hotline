package com.sh3h.hotline.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.util.AndroidComponentUtil;
import com.sh3h.mobileutil.util.LogUtil;

import java.io.IOException;

/**
 * 提醒临期工单
 * Created by zhangjing on 2017/8/8.
 */

public class AlertTasksService extends Service {

    private static final String TAG = "AlertTasksService";
    private MediaPlayer mMediaPlayer;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AlertTasksService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, AlertTasksService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "onCreate");
        MainApplication.get(this).getComponent().inject(this);
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i(TAG, "onStartCommand");
        AssetFileDescriptor fileDescriptor;
        try {
            fileDescriptor = this.getAssets().openFd("ring/alertDeadline.mp3");
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            LogUtil.i(TAG, e.getMessage());
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.getMessage());
            } finally {
                mMediaPlayer = null;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
