package com.bestarmedia.player;

import android.graphics.SurfaceTexture;
import android.media.audiofx.Visualizer;
import android.util.Log;
import android.view.Surface;

import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.bestarmedia.libcommon.util.MediaUtils;
import com.bestarmedia.libopengl.canvas.gl.RawTexture;
import com.bestarmedia.libopengl.glview.texture.GLSurfaceTextureProducerView;
import com.bestarmedia.libopengl.glview.texture.GLTexture;
import com.bestarmedia.libopengl.glview.texture.gles.EglContextWrapper;
import com.bestarmedia.libopengl.glview.texture.gles.GLThread;
import com.bestarmedia.texture.MergeVideoTextureView;
import com.bestarmedia.texture.VideoTextureView;
import com.bestarmedia.texture.WindowStyle;

public class TexturePlayer implements GLThread.OnCreateGLContextListener, GLSurfaceTextureProducerView.OnSurfaceTextureSet, OnBasePlayerListener {

    private final VideoTextureView videoTextureView;
    private final MergeVideoTextureView mergeVideoTextureView;
    private SurfaceTexture surfaceTexture;
    private RawTexture surfaceTextureRelatedTexture;
    private BaseTexturePlayer basePlayer;
    private String path;
    private OnBasePlayerListener onBasePlayerListener;
    private Visualizer mVisualizer;
    private static final String TAG = "TexturePlayer";

    public TexturePlayer(VideoTextureView videoTextureView, MergeVideoTextureView mergeVideoTextureView) {
        this.videoTextureView = videoTextureView;
        this.mergeVideoTextureView = mergeVideoTextureView;
        this.videoTextureView.setOnCreateGLContextListener(this);
        this.videoTextureView.setOnSurfaceTextureSet(this);
    }


    public void setOnBasePlayerListener(OnBasePlayerListener listener) {
        this.onBasePlayerListener = listener;
    }

    @Override
    public void onCreate(EglContextWrapper eglContext) {
        if (mergeVideoTextureView != null) {
            mergeVideoTextureView.setSharedEglContext(eglContext);
        }
    }

    @Override
    public void onSet(SurfaceTexture surfaceTexture, RawTexture surfaceTextureRelatedTexture) {
        this.surfaceTexture = surfaceTexture;
        this.surfaceTextureRelatedTexture = surfaceTextureRelatedTexture;
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                videoTextureView.requestRenderAndWait();
            }
        });
        initSurfaceTexture();
    }

    private void initSurfaceTexture() {
        if (mergeVideoTextureView != null) {
            mergeVideoTextureView.addConsumeGLTexture(new GLTexture(surfaceTextureRelatedTexture, surfaceTexture));
//            mergeVideoTextureView.addConsumeGLTexture(new GLTexture(surfaceTextureRelatedTexture, surfaceTexture));
//            mergeVideoTextureView.addConsumeGLTexture(new GLTexture(surfaceTextureRelatedTexture, surfaceTexture));
//            mergeVideoTextureView.addConsumeGLTexture(new GLTexture(surfaceTextureRelatedTexture, surfaceTexture));
//            mergeVideoTextureView.addConsumeGLTexture(new GLTexture(surfaceTextureRelatedTexture, surfaceTexture));
        }
        Surface surface = new Surface(surfaceTexture);
        basePlayer = new BaseTexturePlayer(surface);
        basePlayer.setProgressCallbackInterval(50);
        basePlayer.setBasePlayerListener(this);
    }


    private void initVisualizer() {
        mVisualizer = new Visualizer(basePlayer.getMediaPlayer().getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                if (basePlayer == null || !basePlayer.isPlaying()) {
                    return;
                }
                if (videoTextureView != null) {
                    videoTextureView.updateVisualizer(bytes);
                }
                if (mergeVideoTextureView != null) {
                    mergeVideoTextureView.updateVisualizer(bytes);
                }
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                if (basePlayer == null || !basePlayer.isPlaying()) {
                    return;
                }
                if (videoTextureView != null) {
                    videoTextureView.updateVisualizerFFT(bytes);
                }
                if (mergeVideoTextureView != null) {
                    mergeVideoTextureView.updateVisualizerFFT(bytes);
                }
            }
        };
        mVisualizer.setDataCaptureListener(captureListener, Visualizer.getMaxCaptureRate() / 2, true, true);
        mVisualizer.setEnabled(true);
    }

    public void setScore(int score) {
        if (videoTextureView != null) {
            videoTextureView.setCurrentScore(score);
        }
    }

    public void release() {
        if (mVisualizer != null) {
            mVisualizer.release();
        }
        if (basePlayer != null) {
            basePlayer.release();
        }
    }

    public void play(String path) {
        this.path = path;
        if (basePlayer == null) {
            Log.e(TAG, "播放器未实例化！！！！！！！！！！！");
            return;
        }
        release();
        playMedia(basePlayer);
    }

    private void playMedia(BaseTexturePlayer mediaPlayer) {
        mediaPlayer.playMedia(path);
    }

    public void selectTrack(int track) {
        if (basePlayer != null) {
            basePlayer.selectTrack(track);
        }
    }

    public void setVolChannel(int channel) {
        if (basePlayer != null) {
            basePlayer.setVolChannel(channel);
        }
    }

    public void pause() {
        if (basePlayer != null) {
            basePlayer.pause();
        }
    }

    public void start() {
        if (basePlayer != null) {
            basePlayer.start();
        }
    }

    public void setTone(int tone) {
        if (basePlayer != null) {
            basePlayer.setTone(tone);
        }
    }

    public void setVolume(float volume) {
        if (basePlayer != null) {
            basePlayer.setVolume(volume);
        }
    }

    public void setWindowStyle(WindowStyle windowStyle) {
        if (videoTextureView != null) {
            videoTextureView.setWindowStyle(windowStyle);
        }
        if (mergeVideoTextureView != null) {
            mergeVideoTextureView.setWindowStyle(windowStyle);
        }
    }

    /**
     * @return 当前播放进度，单位毫秒
     */
    public int getCurrentPosition() {
        if (basePlayer != null) {
            return basePlayer.getCurrentPosition();
        }
        return 0;
    }


    /**
     * @return 视频总时长，单位毫秒
     */
    public int getDuration() {
        if (basePlayer != null) {
            return basePlayer.getDuration();
        }
        return 0;
    }

    public void seekTo(float seek) {
        if (basePlayer != null) {
            basePlayer.seekTo(seek);
        }
    }

    public boolean isPlaying() {
        if (basePlayer != null) {
            basePlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void onPlayStart(String path) {
        Log.d(TAG, "开始播放：" + path);
        if (MediaUtils.isAudioFormatter(path)) {
            initVisualizer();
        }
        if (onBasePlayerListener != null) {
            onBasePlayerListener.onPlayStart(path);
        }
    }

    @Override
    public void onPlayProgress(String path, long duration, long current) {
//        Log.d(TAG, "播放进度：" + (float) current / duration);
        if (onBasePlayerListener != null) {
            onBasePlayerListener.onPlayProgress(path, duration, current);
        }
    }

    @Override
    public void onPlayCompletion(String path) {
        Log.d(TAG, "开始结束：" + path);
        if (onBasePlayerListener != null) {
            onBasePlayerListener.onPlayCompletion(path);
        }
    }

    @Override
    public void onPlayError(String path, String error) {
        Log.e(TAG, "出错：" + error + " path:" + path);
        if (onBasePlayerListener != null) {
            onBasePlayerListener.onPlayError(path, error);
        }
    }
}
