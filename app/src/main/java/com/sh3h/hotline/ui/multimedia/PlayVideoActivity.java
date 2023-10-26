package com.sh3h.hotline.ui.multimedia;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

/**
 * 播放視頻
 * Created by zhangjing on 2017/11/9.
 */
public class PlayVideoActivity extends ParentActivity implements MediaPlayer.OnErrorListener {

    private static final String TAG = "PlayVideoActivity";
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_play_video);
        VideoView videoView = (VideoView) this.findViewById(R.id.dialog_play_video_vv);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        // 播放在线视频
        if (TextUtil.isNullOrEmpty(getIntent().getStringExtra(Constant.VIDEO_FILE_PATH))) {
            return;
        } else {
            videoUrl = getIntent().getStringExtra(Constant.VIDEO_FILE_PATH);
        }
        Uri mVideoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(mVideoUri);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnErrorListener(this);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            LogUtil.i(TAG, "Media Error,Server Died" + extra);
        } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
            LogUtil.i(TAG, "Media Error,Error Unknown " + extra);
        }
        return true;
    }
}
