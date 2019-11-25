package com.bestarmedia.example;

import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.bestarmedia.libcommon.util.LogRecorder;
import com.bestarmedia.libcommon.util.MediaUtils;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bestarmedia.player.KaraokeTexturePlayer;
import com.bestarmedia.texture.MergeVideoTextureView;
import com.bestarmedia.texture.VideoTextureView;
import com.bestarmedia.texture.WindowStyle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnBasePlayerListener {

    private KaraokeTexturePlayer texturePlayer;
    private DisplayManager mDisplayManager;
    private MainPresentation mPresentation;
    private FloatView floatMinor;
    private RelativeLayout mWidgetMinor;
    private int tracks = 0;
    private int channel = 0;
    private int pause = 0;
    private int tone = 0;
    private int playIndex;

    private String[] files = new String[]{
            "http://172.30.1.230:10230/data/song/fdisk12/535c14ff-6ca1-441d-a4c5-0fd18e860c1b.mp4",
            "http://172.30.1.230:10230/data/song/xin/db573b4793e934f6232a8f2165e99c69.mp3",
            "http://172.30.1.230:10230/data/song/disk11/105148.mp4",
            "http://172.30.1.230:10230/data/song/xin/353b3a799f76f67cc97950503a41abad.mp4",
            "http://172.30.1.230:10230/data/song/xin/ef24ab252f360f80bfc691ff67281878.mp3",
            "http://172.30.1.230:10230/data/ad/H256-1080p-1.mp4",
            "http://172.30.1.230:10230/data/song/xin/ef24ab252f360f80bfc691ff67281878.mp3",
            "http://172.30.1.230:10230/data/song/xin/f7eb3f7cab9d30f38dc6c9d6c1476f2d.mp4",
            "http://172.30.1.230:10230/data/necessary/score_result.mp4",
            "http://172.30.1.230:10230/data/song/xin/fd420a98-16cd-4dbd-9544-a402aeb8d03a.mp4",
            "http://172.30.1.230:10230/data/song/xin/ef24ab252f360f80bfc691ff67281878.mp3",
            "http://172.30.1.230:10230/data/song/xin/feca70391baba665838f625934a0eb1e.mp4",
            "http://172.30.1.230:10230/data/song/xin/dd112e4e-e891-4776-a6d3-409b9d6fe790.mp4",
            "http://172.30.1.230:10230/data/song/xin/fea429ee2df6a7bee7904b4b79683ce6.mp3",
            "http://172.30.1.230:10230/data/song/xin/b9803add25e26abfbe264cb62061f97d.mp4",
            "http://172.30.1.230:10230/data/necessary/score_result.mp4",
            "http://172.30.1.230:10230/data/song/xin/9c1fd1cdf33c94fe80e908a78bf38658.mp4",
            "http://172.30.1.230:10230/data/song/xin/f3712dfc9ac153c3b5215e6a1905ab8c.mp3",
            "http://172.30.1.230:10230/data/necessary/ad_default.mp4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDisplayManager = (DisplayManager) getSystemService(DISPLAY_SERVICE);
        texturePlayer = new KaraokeTexturePlayer();
        startMainPlayer();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                if (texturePlayer != null) {
                    playIndex++;
                    texturePlayer.playMedia(files[playIndex % files.length], files[(playIndex + 1) % files.length]);
//                    texturePlayer.play("http://172.30.1.230:10230/data/necessary/ad_default.mp4");
//                    drawTextTextureView.start();
                }
                break;
            case R.id.btn_dannmaku:
//                Random random = new Random();
//                int nextInt = random.nextInt(255);
//                texturePlayer.sendDannmaku(bitmapLogo, "弹幕测试 弹幕测试 弹幕测试 弹幕测试 弹幕测试 ：" + nextInt,
//                        Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                break;
            case R.id.btn_pause:
                if (texturePlayer != null) {
                    if (pause % 2 == 0) {
                        texturePlayer.pause();
                    } else {
                        texturePlayer.start(true);
                    }
                    pause++;
                }
                break;
            case R.id.btn_track:
                if (texturePlayer != null) {
                    texturePlayer.selectTrack(tracks % 2);
                    tracks++;
                }
                break;
            case R.id.btn_channel:
                if (texturePlayer != null) {
                    texturePlayer.setVolChannel(channel % 3);
                    channel++;
                }
                break;
            case R.id.btn_tone_down:
                if (texturePlayer != null) {
                    tone = tone - 5;
                    texturePlayer.setTone(tone);
                }
                break;
            case R.id.btn_tone_default:
                if (texturePlayer != null) {
                    tone = 0;
                    texturePlayer.setTone(tone);
                }
                break;
            case R.id.btn_tone_up:
                if (texturePlayer != null) {
                    tone = tone + 5;
                    texturePlayer.setTone(tone);
                }
                break;
            case R.id.btn_preview:
                PreviewDialog previewDialog = new PreviewDialog(this, files[1]);
                previewDialog.show();
                break;

            case R.id.btn_window_style:
                if (texturePlayer != null) {
                    windowStyle++;
                    int style = windowStyle % 6;
                    switch (style) {
                        case 1:
                            texturePlayer.setWindowStyle(WindowStyle.ONE_BIG_FOUR_SMALL_OUTSIDE);
                            break;
                        case 2:
                            texturePlayer.setWindowStyle(WindowStyle.ONE_BIG_FOUR_SMALL_INSIDE);
                            break;
                        case 3:
                            texturePlayer.setWindowStyle(WindowStyle.THREE_X_THREE);
                            break;
                        case 4:
                            texturePlayer.setWindowStyle(WindowStyle.TWO_X_TWO);
                            break;
                        case 5:
                            texturePlayer.setWindowStyle(WindowStyle.ONE_BIG_ON_SMALL_RIGHT);
                            break;
                        default:
                            texturePlayer.setWindowStyle(WindowStyle.NORMAL);
                            break;
                    }
                }
                break;
            case R.id.btn_play_ad:
                if (mPresentation != null) {
                    mPresentation.playAd();
                }
                break;
            default:
                break;
        }
    }

    private int windowStyle = 0;

    @Override
    protected void onDestroy() {
        stopMainPlayer();
        super.onDestroy();
    }


    private void startMainPlayer() {
        Display[] displays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        if (displays != null) {
            for (Display display : displays) {
                if (display != null && display.isValid() && display.getName().toLowerCase().contains("hdmi")) {
                    Point outSize = new Point();
                    display.getSize(outSize);
                    Point realSize = new Point();
                    display.getRealSize(realSize);
                    showPresentation(display);
                    break;
                }
            }
            mDisplayManager.registerDisplayListener(mDisplayListener, null);
            runOnUiThread(() -> {
                mWidgetMinor = (RelativeLayout) View.inflate(this, R.layout.widget_minor, null);
                floatMinor = new FloatView(this, 0, 400, mWidgetMinor);
                floatMinor.setIsAllowTouch(true);
                floatMinor.setFloatViewClickListener(() ->
                        fullViewMinor(!mIsOpenMinor));
                floatMinor.addToWindow();
                fullViewMinor(false);
                MergeVideoTextureView mergeVideoTextureView = mWidgetMinor.findViewById(R.id.player_texture);
                VideoTextureView videoTextureView = mPresentation != null ? mPresentation.getMergeVideoTextureView() : null;
                texturePlayer.initPlayer(this, videoTextureView, mergeVideoTextureView);
                texturePlayer.setBasePlayerListener(MainActivity.this);
                floatMinor.postDelayed(() ->
                        texturePlayer.playMedia(files[playIndex % files.length], files[(playIndex + 1) % files.length]), 300);
            });
        }
    }

    private boolean mIsOpenMinor;

    private void fullViewMinor(boolean show) {
        if (show) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1280, 720);
            mWidgetMinor.setPadding(0, 0, 0, 0);
            mWidgetMinor.setLayoutParams(params);
            floatMinor.updateFloatViewPosition(0, 0, 1280, 720);
            mIsOpenMinor = true;
        } else {
            floatMinor.updateFloatViewPosition(300, 300, 160, 90);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(160, 90);
            int padding = DensityUtil.dip2px(getApplicationContext(), 1);
            mWidgetMinor.setPadding(padding, padding, padding, padding);
            mWidgetMinor.setLayoutParams(params);
            mIsOpenMinor = false;
        }

    }

    private void stopMainPlayer() {
        if (texturePlayer != null) {
            texturePlayer.release();
            texturePlayer = null;
        }
        floatMinor.removeFromWindow();
        floatMinor = null;
        mWidgetMinor = null;
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
        }
    }

    private void showPresentation(Display display) {
        mPresentation = new MainPresentation(getApplicationContext(), display);
        if (mPresentation.getWindow() != null)
            mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mPresentation.show();
    }

    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        public void onDisplayAdded(int displayId) {
            Toast.makeText(getApplicationContext(), "HDMI 已连接", Toast.LENGTH_SHORT).show();
            stopMainPlayer();
            startMainPlayer();
        }

        public void onDisplayChanged(int displayId) {
        }

        public void onDisplayRemoved(int displayId) {
            Toast.makeText(getApplicationContext(), "HDMI 已断开", Toast.LENGTH_SHORT).show();
            LogRecorder.addHdmiLog(getApplicationContext(), "HDMI 已断开");
            stopMainPlayer();
            startMainPlayer();
        }
    };

    @Override
    public void onPlayStart(String path) {
        //音频文件无画面，需要绘制自定义背景
        if (texturePlayer != null) {
            texturePlayer.setDrawCustomBackground(MediaUtils.isAudioFormatter(path));
        }
    }

    @Override
    public void onPlayProgress(String path, long duration, long current) {

    }

    @Override
    public void onPlayCompletion(String path) {
        playIndex++;
        texturePlayer.playMedia(files[playIndex % files.length], files[(playIndex + 1) % files.length]);
    }

    @Override
    public void onPlayError(String path, String error) {
        playIndex++;
        texturePlayer.playMedia(files[playIndex % files.length], files[(playIndex + 1) % files.length]);
    }
}
