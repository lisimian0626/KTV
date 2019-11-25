package com.beidousat.karaoke.player;

import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;

import com.beidousat.score.NativeScoreRunner;
import com.beidousat.score.NoteInfo;
import com.beidousat.score.OnScoreListener;
import com.beidousat.score.ScoreFileUtil;
import com.beidousat.score.ScoreLineInfo;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.http.BaseHttpRequest;
import com.bestarmedia.libcommon.http.BaseHttpRequestListener;
import com.bestarmedia.liblame.BnsAudioRecorder;
import com.bestarmedia.liblame.IAudioRecordListener;

import java.util.ArrayList;
import java.util.List;

public class KaraokePlayer implements OnBasePlayerListener, IAudioRecordListener {

    private final static String TAG = "KaraokePlayer";
    private final int VOL_UP_COUNT = 10;
    private float currentVol = 0.6F;
    private int volUpCount = 6;
    private int scoreMode = 1;
    private boolean isRecord;
    private boolean isMute = false;
    private String mainFilePath, filePath2, preloadPath;
    private String scoreFilePath, scoreFilePath2;
    private String mRecordFileName;
    private SurfaceView mainSurface, minorSurface;
    private DecodePlayer decodePlayer;
    private DecodePlayer decodePlayer2;
    private List<ScoreLineInfo> scoreLineInfoList;
    private ArrayList<NoteInfo> notes = null;
    private int mScoreMode = 0, mCurScoreLine = -1;
    private float mCurTotalScore;
    private List<Float> mShowScores = new ArrayList<>();
    private OnScoreListener mOnScoreListener;
    private final static long INTERVAL_PROGRESS = 1000;
    private long mPreProgress, mPreProgressCallback, mPreScoreCallBack;
    private int mProgressExceptionTime = 0;
    private OnBasePlayerListener basePlayerListener;

    private Handler mainHandler;

    public KaraokePlayer() {
        mainHandler = new android.os.Handler();
    }

    /**
     * 混合播放，用于音频+视频背景 模式
     *
     * @param mainFilePath 主文件
     * @param filePath2    第二文件地址（存音频需要播放背景视频）
     * @param preloadPath  预加载
     */
    public void playMixed(String mainFilePath, String filePath2, String preloadPath, SurfaceView mainSurface, SurfaceView minorSurface) {
        initParameters();

        this.mainFilePath = mainFilePath;
        this.filePath2 = filePath2;
        this.preloadPath = preloadPath;
        this.mainSurface = mainSurface;
        this.minorSurface = minorSurface;
        if (!TextUtils.isEmpty(filePath2)) {
            decodePlayer = new DecodePlayer(null, null);
            decodePlayer2 = new DecodePlayer(mainSurface, minorSurface);
            decodePlayer2.setBasePlayerListener(onBasePlayerListener2);
        } else {
            decodePlayer = new DecodePlayer(mainSurface, minorSurface);
        }
        decodePlayer.setProgressCallbackInterval(50);
        decodePlayer.setBasePlayerListener(this);
        decodePlayer.playEncode(this.mainFilePath, this.preloadPath);
    }

    private void replay() {
        if (!TextUtils.isEmpty(mainFilePath)) {
            playMixed(mainFilePath, filePath2, preloadPath, mainSurface, minorSurface);
        }
    }

    private void initParameters() {
        mCurTotalScore = 0;
        mCurScoreLine = -1;
        if (mShowScores != null) {
            mShowScores.clear();
        }
        if (scoreLineInfoList != null) {
            scoreLineInfoList.clear();
        }
        if (mOnScoreListener != null) {
            mOnScoreListener.onScoreCallback(0);
        }
    }

    /**
     * 播放
     */
    public void start() {
        if (decodePlayer != null) {
            decodePlayer.start();
        }
        if (decodePlayer2 != null) {
            decodePlayer2.start();
        }
        startVolInit();
        BnsAudioRecorder.getInstance().pause(false);
    }

    public void pause() {
        volOff();
        if (decodePlayer != null) {
            decodePlayer.pause();
        }
        if (decodePlayer2 != null) {
            decodePlayer2.pause();
        }
        BnsAudioRecorder.getInstance().pause(true);
    }

    public void release() {
        BnsAudioRecorder.getInstance().release();
        if (decodePlayer != null) {
            decodePlayer.release();
            decodePlayer = null;
        }
        if (decodePlayer2 != null) {
            decodePlayer2.release();
            decodePlayer2 = null;
        }
    }

    public void volOn() {
        isMute = false;
        startVolInit();
    }

    public void volOff() {
        isMute = true;
        mainHandler.removeCallbacks(runVolInit);
        decodePlayer.setVolume(0.0F);
    }

    public void setVol(float vol) {
        currentVol = vol > 1.0F ? 1.0F : vol;
        Log.d(TAG, "setVol:" + currentVol);
    }

    public void setTone(int tone) {
        decodePlayer.setTone(tone);
    }

    public void setScore(String scoreFilePath, String scoreFilePath2) {
        this.scoreFilePath = scoreFilePath;
        this.scoreFilePath2 = scoreFilePath2;
    }

    public void setRecord(boolean isRecord) {
        this.isRecord = isRecord;
    }

    public void setScoreMode(int mode) {
        this.scoreMode = mode;
    }

    public void setBasePlayerListener(OnBasePlayerListener listener) {
        basePlayerListener = listener;
    }

    public void setOnScoreListener(OnScoreListener scoreListener) {
        this.mOnScoreListener = scoreListener;
    }

    /**
     * @return 当前播放进度，单位毫秒
     */
    public int getCurrentPosition() {
        return decodePlayer.getCurrentPosition();
    }

    /**
     * @return 视频总时长，单位毫秒
     */
    public int getDuration() {
        return decodePlayer.getDuration();
    }

    public void selectTrack(int track) {
        decodePlayer.selectTrack(track);
    }

    /***
     *  0:立体声 1：左声道 2：右声道
     * for sun chip（晨芯）
     **/
    public void setVolChannel(int channel) {
        decodePlayer.setVolChannel(channel);
    }

    public MediaPlayer getMediaPlayer() {
        return decodePlayer.getMediaPlayer();
    }

    @Override
    public void onPlayStart(String path) {
        Log.i(TAG, "onPlayStart >>>>>>>>>>>>>>>>>>> " + path);
        if (basePlayerListener != null) {
            basePlayerListener.onPlayStart(path);
        }
        if (decodePlayer2 != null) {
            decodePlayer2.playMedia(this.filePath2);
            decodePlayer2.setVolume(0.0F);
        }
        startVolInit();
        BnsAudioRecorder.getInstance().release();
        NativeScoreRunner.getInstance().stop();
        mainHandler.removeCallbacks(runnableRecord);
        mainHandler.postDelayed(runnableRecord, 1000);
    }

    @Override
    public void onPlayProgress(String path, long duration, long current) {
        checkAddScore(current);
        if (basePlayerListener != null) {
            basePlayerListener.onPlayProgress(path, duration, current);
        }
        long curMilTime;
        if (mPreProgress != current
                && ((curMilTime = System.currentTimeMillis()) - mPreProgressCallback) >= INTERVAL_PROGRESS) {
            if (mOnScoreListener != null && curMilTime - mPreScoreCallBack >= 5000) {//每5秒回调分数
                int showScore = getCurScore();
                Log.d(TAG, "当前得分：" + showScore);
                mOnScoreListener.onScoreCallback(showScore);
                mPreScoreCallBack = curMilTime;
            }
            if ((mPreProgress >= current || current - mPreProgress > 3 * INTERVAL_PROGRESS) && current <= 30000 && duration >= 20 * 60 * 1000) {
                Log.w(TAG, "scheduleAtFixedRate 播放器进度异常，上一秒:" + mPreProgress + "  当前:" + current);
                mProgressExceptionTime++;
                if (OkConfig.boxManufacturer() == 0 && mProgressExceptionTime >= 2) {
                    mProgressExceptionTime = 0;
                    Log.e(TAG, "播放器时间轴异常5次以上自动重唱 上一秒时间轴:" + mPreProgress + " 当前时间轴：" + current + " url:" + mainFilePath);
//                    addPlayErrLog("播放器时间轴异常5次以上自动重唱 上一秒时间轴:" + mPreProgress + " 当前时间轴：" + current + " url:" + mainFilePath);
                    replay();
                }
            } else {
                mProgressExceptionTime = 0;
            }
            //每秒回调一次播放进度
            mPreProgressCallback = curMilTime;
            mPreProgress = current;
        } else {
            mProgressExceptionTime = 0;
        }
    }

    @Override
    public void onPlayCompletion(String path) {
        Log.i(TAG, "播放完成 :" + path);
        BnsAudioRecorder.getInstance().release();
        if (basePlayerListener != null) {
            basePlayerListener.onPlayCompletion(path);
        }
    }

    @Override
    public void onPlayError(String path, String error) {
        Log.e(TAG, "播放出错 :" + path + " error:" + error);
        BnsAudioRecorder.getInstance().release();
        if (basePlayerListener != null) {
            basePlayerListener.onPlayError(path, error);
        }
    }

    @Override
    public void audioByte(double[] bytes) {
        try {
            if (scoreLineInfoList != null && scoreLineInfoList.size() > 0) {
                int currentPosition = decodePlayer.getCurrentPosition();
//                Log.d(TAG, "输入评分数据length：" + bytes.length + " 播放去时间轴：" + currentPosition);
                NativeScoreRunner.getInstance().addData(bytes, currentPosition);
            }
        } catch (Exception e) {
            Log.w(TAG, "audioByte ex:" + e.toString());
        }
    }

    @Override
    public void audioBytes(byte[] bytes, int bufSize) {

    }

    public void setRecordFileName(String recordFileName) {
        this.mRecordFileName = recordFileName;
    }

    private void initRecord() {
        if (!TextUtils.isEmpty(mRecordFileName)) {//开启录音才需要打开AudioRecord
            Log.d(TAG, "初始化录音>>>>>>>>>>>");
            BnsAudioRecorder bnsAudioRecorder = BnsAudioRecorder.getInstance();
            bnsAudioRecorder.setAudioRecordListener(this);
            bnsAudioRecorder.setRecordName(mRecordFileName);
            bnsAudioRecorder.start();
        }
    }

    private void startVolInit() {
        mainHandler.removeCallbacks(runVolInit);
        volUpCount = 1;
        mainHandler.postDelayed(runVolInit, 100);
    }

    private boolean initScore() {
        if (scoreLineInfoList != null) {
            scoreLineInfoList.clear();
        }
        if (!TextUtils.isEmpty(scoreFilePath) && !TextUtils.isEmpty(scoreFilePath2)) {
            BaseHttpRequest httpRequest = new BaseHttpRequest();
            httpRequest.setBaseHttpRequestListener(new BaseHttpRequestListener() {
                @Override
                public void onRequestCompletion(String url, String body) {
                    NativeScoreRunner.getInstance().setScoreMode(scoreMode);
                    NativeScoreRunner.getInstance().start();
                    notes = ScoreFileUtil.readNote(body);
                    Log.d(TAG, "评分音调基准size：" + notes.size());
                    NativeScoreRunner.getInstance().setNotes(notes);
                }

                @Override
                public void onRequestFail(String url, String err) {
                }

                @Override
                public void onRequestError(String url, String err) {

                }
            });
            httpRequest.get(scoreFilePath);


            BaseHttpRequest httpRequest2 = new BaseHttpRequest();
            httpRequest2.setBaseHttpRequestListener(new BaseHttpRequestListener() {
                @Override
                public void onRequestCompletion(String url, String body) {
                    scoreLineInfoList = ScoreFileUtil.readNote2(body);
                    Log.d(TAG, "评分得分基准size：" + scoreLineInfoList.size());
                }

                @Override
                public void onRequestFail(String url, String err) {

                }

                @Override
                public void onRequestError(String url, String err) {

                }
            });
            httpRequest2.get(scoreFilePath2);
            return true;
        }
        return false;
    }

    private void checkAddScore(long playPosition) {
        try {
            if (scoreLineInfoList != null && scoreLineInfoList.size() > mCurScoreLine + 1) {
                ScoreLineInfo info = scoreLineInfoList.get(mCurScoreLine + 1);
                if (playPosition >= info.time * 1000) {
                    mCurScoreLine = mCurScoreLine + 1;
                    float oScore = info.socre;
                    float curScore = NativeScoreRunner.getInstance().getScore();
                    Log.d(TAG, "Native总得分：" + curScore);
                    float deltaScore = getScoreX() * (curScore - mCurTotalScore);
                    mCurTotalScore = curScore;
                    if (oScore > 0 && deltaScore >= 0) {
                        float percentScore = (100 * deltaScore / oScore);
                        if (percentScore > 0 && percentScore < 60) {
                            percentScore = percentScore * getScoreX();
                            if (percentScore < 60) {
                                percentScore = 60;
                            }
                        }
                        float gotScore = deltaScore > oScore ? 100 : (percentScore > 100 ? 100 : percentScore);
                        mShowScores.add(gotScore);
                    }
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "NdkJniUtil.setNotes ex:" + e.toString());
        }
    }

    private float getScoreX() {
        return 5.0f;
    }


    public synchronized int getCurScore() {
        int last = mShowScores.size();
        if (last > 0) {
            float total = 0;
            for (float s : mShowScores) {
                total = total + s;
            }
            int size = mShowScores.size();
            float average = total / size;
            return (int) average;
        }
        return 0;
    }

    private Runnable runVolInit = new Runnable() {
        @Override
        public void run() {
            if (volUpCount <= VOL_UP_COUNT) {
                float vol = volUpCount == VOL_UP_COUNT ? currentVol : (currentVol * volUpCount / VOL_UP_COUNT);
                decodePlayer.setVolume(vol);
                mainHandler.postDelayed(this, 150);
                volUpCount++;
            }
        }
    };

    private Runnable runnableRecord = new Runnable() {
        @Override
        public void run() {
            boolean readScoreNode = initScore();
            Log.d(TAG, "runnableRecord 是否评分歌曲:" + readScoreNode);
            if (readScoreNode) {//评分歌曲
                initRecord();
            } else if (BnsAudioRecorder.getInstance().isEncodeMP3()) {//开启录音
                initRecord();
            }
        }
    };


    private OnBasePlayerListener onBasePlayerListener2 = new OnBasePlayerListener() {
        @Override
        public void onPlayStart(String path) {
            Log.d(TAG, "第二播放器 开始播放 >>>>>>>>>>>>");
        }

        @Override
        public void onPlayProgress(String path, long duration, long current) {
            Log.d(TAG, "第二播放器 播放进度 >>>>>>>>>>>>");
        }

        @Override
        public void onPlayCompletion(String path) {
            Log.d(TAG, "第二播放器 播放完成 >>>>>>>>>>>>");
        }

        @Override
        public void onPlayError(String path, String error) {
            Log.e(TAG, "第二播放器 播放出错：" + error);
        }
    };
}
