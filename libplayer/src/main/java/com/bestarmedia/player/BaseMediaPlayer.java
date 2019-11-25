package com.bestarmedia.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.util.Invoke;
import com.bestarmedia.libcommon.util.MediaUtils;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BaseMediaPlayer {

    private MediaPlayer mediaPlayer;
    private ScheduledExecutorService scheduledExecutorService;
    private long progressCallbackInterval = 0L;
    private OnBasePlayerListener basePlayerListener;
    private String path;
    private Vector<Integer> trackAudioIndex = null;
    private Surface surface;
    private SurfaceView surfaceView, minorSurfaceView;
    private ExecutorService executorService;

    private final static String TAG = "BaseMediaPlayer";

    public BaseMediaPlayer(Surface surface, SurfaceView surfaceView, SurfaceView minorSurfaceView) {
        this.surface = surface;
        this.surfaceView = surfaceView;
        this.minorSurfaceView = minorSurfaceView;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void setProgressCallbackInterval(long intervalMils) {
        progressCallbackInterval = intervalMils;
    }

    /**
     * 播放器监听
     *
     * @param listener 监听
     */
    public void setBasePlayerListener(OnBasePlayerListener listener) {
        this.basePlayerListener = listener;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (!MediaUtils.isAudioFormatter(path)) {
                if (surfaceView != null) {
                    mediaPlayer.setDisplay(surfaceView.getHolder());
                    if (minorSurfaceView != null) {
//                        this.mediaPlayer.setMinorDisplay(minorSurfaceView.getHolder());
                        Invoke.invokeMethod(mediaPlayer, "setMinorDisplay", new Object[]{minorSurfaceView.getHolder()}, new Class[]{SurfaceHolder.class});
                    }
                } else {
                    mediaPlayer.setSurface(surface);
                }
            }
//            mediaPlayer.setLooping(false);
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {
                    Log.i("onSeekComplete", "onSeekComplete----" + mediaPlayer.getCurrentPosition());
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    onPlayStart(path);
                    if (progressCallbackInterval > 0) {
                        startTimer();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer m) {
                    onPlayCompletion(path);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    onPlayError(path, "what:" + what + " extra:" + extra);
                    return false;
                }
            });
        }
    }

    private void play(String path) {
        this.trackAudioIndex = null;
        this.path = path;
        if (TextUtils.isEmpty(path)) {
            onPlayError(path, "播放路径为空！");
        }
        try {
            Log.i(TAG, "播放：" + path);
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            Log.e(TAG, "读取文件时IO异常", e);
            onPlayError(path, "读取文件时IO异常！");
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.e(TAG, "播放文件时IO异常，url：" + this.path, e);
            onPlayError(path, "播放文件IO异常！");
        }
    }

    public void playMedia(final String path) {
        this.path = path;
        initMediaPlayer();
        play(this.path);
    }


    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        try {
            if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
                this.mediaPlayer.pause();
            }
        } catch (Exception e) {
            Log.e(TAG, "执行“pause()”发生异常：", e);
        }
    }

    /**
     * 播放
     */
    public void start() {
        try {
            if (this.mediaPlayer != null && !this.mediaPlayer.isPlaying()) {
                this.mediaPlayer.start();
            }
        } catch (Exception e) {
            Log.e(TAG, "执行“start()” 发生异常：", e);
        }

    }

    /**
     * 是否正在播放
     *
     * @return 是否正在播放
     */
    public boolean isPlaying() {
        if (this.mediaPlayer != null)
            try {
                return mediaPlayer.isPlaying();
            } catch (Exception e) {
                Log.w(TAG, "isPlaying ex:", e);
            }
        return false;
    }

    /**
     * 当前播放进度，单位毫秒
     *
     * @return 当前播放进度，单位毫秒
     */
    public int getCurrentPosition() {
        try {
            if (isPlaying()) {
                return mediaPlayer.getCurrentPosition();
            }
        } catch (Exception e) {
            Log.e(TAG, "执行“getCurrentPosition()” 发生异常：", e);
        }
        return 0;
    }

    /**
     * 视频总时长，单位毫秒
     *
     * @return 视频总时长，单位毫秒
     */
    public int getDuration() {
        if (isPlaying()) {
            try {
                return mediaPlayer.getDuration();
            } catch (Exception e) {
                Log.e(TAG, "执行“getDuration()” 发生异常：", e);
            }
        }
        return 0;
    }

    /**
     * 进度拖动
     *
     * @param seek 百分百（0-1）
     */
    public void seekTo(final float seek) {
        try {
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mediaPlayer != null) {
                            mediaPlayer.seekTo((int) (seek * getDuration()));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "执行“seekTo()” 发生异常：", e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "执行“seekTo()” 线程发生异常：", e);
        }
    }

    private int initTracks() {
        trackAudioIndex = new Vector<>();
        if (mediaPlayer != null) {
            MediaPlayer.TrackInfo[] trackInfos = mediaPlayer.getTrackInfo();
            if (trackInfos != null && trackInfos.length > 0) {
                for (int j = 0; j < trackInfos.length; j++) {
                    MediaPlayer.TrackInfo info = trackInfos[j];
                    if (info.getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
                        trackAudioIndex.add(j);
                    }
                }
            } else {
                if (trackInfos == null) {
                    Log.e(TAG, "获取音轨信息为NULL >>>>>>>>>>>>>>> ");
                } else {
                    Log.i(TAG, "获取音轨信息length：" + trackInfos.length);
                }
            }
        }
        return trackAudioIndex.size();
    }

    /**
     * 音轨
     *
     * @param track 音轨
     */
    public void selectTrack(final int track) {
        try {
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (trackAudioIndex == null) {
                            initTracks();
                        }
                        if (trackAudioIndex != null && trackAudioIndex.size() > track) {
                            mediaPlayer.selectTrack(trackAudioIndex.get(track));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "执行“selectTrack()” 发生异常：", e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "执行“selectTrack()” 线程发生异常：", e);
        }
    }

    /***
     *  0:立体声 1：左声道 2：右声道
     **/
    public void setVolChannel(final int channel) {
        try {
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isPlaying()) {
                            if (OkConfig.boxManufacturer() == 1) {//音诺恒
                                Invoke.invokeMethod(mediaPlayer, "setAudioChannel", new Object[]{channel}, new Class[]{int.class});
                            } else if (OkConfig.boxManufacturer() == 2) {//音王
                                Invoke.invokeMethod(mediaPlayer, "switchChannel", new Object[]{channel}, new Class[]{int.class});
                            } else {//晨芯
                                Invoke.invokeMethod(mediaPlayer, "setParameter", new Object[]{1102, channel}, new Class[]{int.class, int.class});
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "执行“setVolChannel()” 发生异常：", e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "执行“setVolChannel()” 线程发生异常：", e);
        }
    }

    /***
     * 设置音调
     **/
    public void setTone(final int tone) {
        try {
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isPlaying()) {
                            if (OkConfig.boxManufacturer() == 1) {//音诺恒
                                Invoke.invokeMethod(mediaPlayer, "setAudioPitch", new Object[]{tone}, new Class[]{int.class});
                            } else if (OkConfig.boxManufacturer() == 2) {//音王
                                Invoke.invokeMethod(mediaPlayer, "setPitch", new Object[]{(float) tone}, new Class[]{float.class});
                            } else {//晨芯
                                Invoke.invokeMethod(mediaPlayer, "setPitch", new Object[]{tone}, new Class[]{int.class});
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "执行“setTone()” 发生异常：", e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "执行“setTone()” 发生异常：", e);
        }
    }

    /**
     * 释放
     */
    public void release() {
        try {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.reset();
                this.mediaPlayer.release();
                this.mediaPlayer = null;
            }
            this.trackAudioIndex = null;
            stopTimer();
        } catch (Exception e) {
            Log.e(TAG, "执行“release()” 发生异常：", e);
        }
    }

    /**
     * 设置播放器音量百分百
     *
     * @param volume 音量百分百
     */
    public void setVolume(final float volume) {
        try {
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mediaPlayer != null) {
                            mediaPlayer.setVolume(volume, volume);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "执行“setVolume()” 发生异常：", e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "执行“setVolume()” 线程发生异常：", e);
        }
    }


    /**
     * 启动播放器进度扫描
     */
    private void startTimer() {
        if (this.scheduledExecutorService != null && !this.scheduledExecutorService.isShutdown()) {
            return;
        }
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduleAtFixedRate(this.scheduledExecutorService, progressCallbackInterval);
    }

    /**
     * 停止播放器进度扫描
     */
    private void stopTimer() {
        if (this.scheduledExecutorService != null) {
            this.scheduledExecutorService.shutdownNow();
            this.scheduledExecutorService = null;
        }
    }

    /**
     * 启动播放器进度扫描线程
     *
     * @param service ScheduledExecutorService
     */
    private void scheduleAtFixedRate(ScheduledExecutorService service, long interval) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (isPlaying()) {
                    long cur = getCurrentPosition();
                    long dur = getDuration();
                    onPlayProgress(path, dur, cur);
                }
            }
        }, 0, interval, TimeUnit.MILLISECONDS);
    }


    public void onPlayStart(String path) {
        if (basePlayerListener != null) {
            basePlayerListener.onPlayStart(path);
        }
    }

    public void onPlayProgress(String path, long duration, long current) {
        if (basePlayerListener != null) {
            basePlayerListener.onPlayProgress(path, duration, current);
        }
    }

    public void onPlayCompletion(String path) {
        this.trackAudioIndex = null;
        if (basePlayerListener != null) {
            basePlayerListener.onPlayCompletion(path);
        }
    }

    public void onPlayError(String path, String error) {
        if (basePlayerListener != null) {
            basePlayerListener.onPlayError(path, error);
        }
    }
}