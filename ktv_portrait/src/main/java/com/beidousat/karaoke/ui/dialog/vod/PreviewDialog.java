package com.beidousat.karaoke.ui.dialog.vod;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libcommon.helper.SongDetailHelper;
import com.bestarmedia.libcommon.model.dto.SongOperation;
import com.bestarmedia.libcommon.model.v4.Song;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.player.DecodeTexturePlayer;
import com.bestarmedia.texture.VideoTextureView;


public class PreviewDialog extends BaseDialog implements OnClickListener, SeekBar.OnSeekBarChangeListener, OnBasePlayerListener {

    private final String TAG = PreviewDialog.class.getSimpleName();
    private VideoTextureView videoTextureView;
    private DecodeTexturePlayer decodeTexturePlayer;
    private ImageView mIvClose;
    private SeekBar mSeekBar;
    private TextView mTvName, mTvTime, mTvMsg;
    private SongSimple mSong;
    private String chooseId;
    private Song mSongDetail;

    public PreviewDialog(Context context, SongSimple song, String chooseId) {
        super(context, R.style.MyDialog);
        this.chooseId = chooseId;
        mSong = song;
        init();
    }

    private void init() {
        this.setContentView(R.layout.widget_preview);
        if (getWindow() != null) {
            LayoutParams lp = getWindow().getAttributes();
            lp.width = getContext().getResources().getInteger(R.integer.preview_w);
            lp.height = getContext().getResources().getInteger(R.integer.preview_h);
            lp.gravity = Gravity.CENTER;
            lp.dimAmount = 0.7f;
            getWindow().setAttributes(lp);
        }
        mTvTime = findViewById(R.id.tv_timer);
        mTvTime.setVisibility(View.INVISIBLE);
        videoTextureView = findViewById(R.id.surf_preview);
        mTvName = findViewById(R.id.tv_name);
        mTvMsg = findViewById(R.id.tv_msg);
        mIvClose = findViewById(R.id.iv_close);
        mIvClose.setOnClickListener(this);
        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(this);
        findViewById(R.id.iv_top).setOnClickListener(this);
        findViewById(R.id.iv_add).setOnClickListener(this);
        mTvName.setText(mSong.songName);
        mSeekBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onPlayStart(String path) {
        if (decodeTexturePlayer != null) {
            decodeTexturePlayer.setVolume(0);//要静音
        }
        findViewById(R.id.ll_loading).setVisibility(View.GONE);
    }

    @Override
    public void onPlayProgress(String path, long duration, long current) {

    }

    @Override
    public void onPlayCompletion(String path) {
        close();
    }

    @Override
    public void onPlayError(String path, String error) {
        mTvMsg.post(() -> {
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            mTvMsg.setText(error);
        });
    }

    private void play() {
        SongDetailHelper detailHelper = new SongDetailHelper(getContext(), mSong.id);
        detailHelper.setOnSongDetailListener(new SongDetailHelper.OnSongDetailListener() {
            @Override
            public void onSongDetail(final Song songDetail) {
                mSongDetail = songDetail;
                if (!TextUtils.isEmpty(mSongDetail.mediaUrl)) {
                    decodeTexturePlayer = new DecodeTexturePlayer(videoTextureView, null);
                    decodeTexturePlayer.setOnBasePlayerListener(PreviewDialog.this);
                    videoTextureView.postDelayed(runnable, 2000);
                } else {
                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                    mTvMsg.setText(R.string.song_file_not_exit);
                }
            }

            @Override
            public void onFail(String error) {
                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                mTvMsg.setText(error);
            }
        });
        detailHelper.getSongDetail();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mSongDetail != null && !TextUtils.isEmpty(mSongDetail.mediaUrl) && decodeTexturePlayer != null) {
                decodeTexturePlayer.playEncode(mSongDetail.mediaUrl, null);
                decodeTexturePlayer.setVolume(0);//要静音
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                close();
                break;
            case R.id.iv_add:
                if (mSong != null) {
                    VodApplication.getKaraokeController().selectSong(new SongOperation(0, mSong, v, chooseId, null, null), true);
                }
                break;
            case R.id.iv_top:
                if (mSong != null) {
                    VodApplication.getKaraokeController().selectSong(new SongOperation(1, mSong, v, chooseId, null, null), true);
                }
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        play();
    }

    @Override
    public void dismiss() {
        videoTextureView.removeCallbacks(runnable);
        if (decodeTexturePlayer != null) {
            Log.e(TAG, "MediaPlayer.release >>>>>>>>>>>>>>>>>>>>>> ");
            decodeTexturePlayer.release();
            decodeTexturePlayer = null;
        }
        super.dismiss();
    }

    private void close() {
        this.dismiss();
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (decodeTexturePlayer != null) {
            float seek = (float) seekBar.getProgress() / seekBar.getMax();
            decodeTexturePlayer.seekTo(seek);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
//        mIsTouching = true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }
}
