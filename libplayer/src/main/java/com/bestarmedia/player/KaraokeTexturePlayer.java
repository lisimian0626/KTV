package com.bestarmedia.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.beidousat.karaoke.player.R;
import com.beidousat.score.NativeScoreRunner;
import com.beidousat.score.NoteInfo;
import com.beidousat.score.OnScoreListener;
import com.beidousat.score.ScoreFileUtil;
import com.beidousat.score.ScoreLineInfo;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.http.BaseHttpRequest;
import com.bestarmedia.libcommon.http.BaseHttpRequestListener;
import com.bestarmedia.libcommon.util.MediaUtils;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.liblame.BnsAudioRecorder;
import com.bestarmedia.liblame.IAudioRecordListener;
import com.bestarmedia.libopengl.filter.texture.BasicTextureFilter;
import com.bestarmedia.libopengl.filter.texture.ContrastFilter;
import com.bestarmedia.libopengl.filter.texture.HueFilter;
import com.bestarmedia.libopengl.filter.texture.PixelationFilter;
import com.bestarmedia.libopengl.filter.texture.SaturationFilter;
import com.bestarmedia.libopengl.filter.texture.TextureFilter;
import com.bestarmedia.texture.MergeVideoTextureView;
import com.bestarmedia.texture.VideoTextureView;
import com.bestarmedia.texture.WindowStyle;

import java.util.ArrayList;
import java.util.List;

public class KaraokeTexturePlayer implements OnBasePlayerListener, IAudioRecordListener {

    private final static String TAG = "KaraokeTexturePlayer";
    private final int VOL_UP_COUNT = 10;
    private float currentVol = 0.6F;
    private int volUpCount = 6;
    private int scoreMode = 1;
    private String mainFilePath, preloadPath;
    private String scoreFilePath, scoreFilePath2;
    private String mRecordFileName;
    private VideoTextureView videoTextureView;
    private MergeVideoTextureView mergeVideoTextureView;
    private DecodeTexturePlayer decodePlayer;
    private List<ScoreLineInfo> scoreLineInfoList;
    private ArrayList<NoteInfo> notes = null;
    private int mCurScoreLine = -1;
    private float mCurTotalScore;
    private List<Float> mShowScores = new ArrayList<>();
    private OnScoreListener mOnScoreListener;
    //    private OnKeyInfoListener mOnKeyInfoListener;
    private final static long INTERVAL_PROGRESS = 1000;
    private long mPreProgress, mPreProgressCallback, mPreScoreCallBack;
    private int mProgressExceptionTime = 0;
    private OnBasePlayerListener basePlayerListener;
    private Handler mainHandler = new Handler();
    /**
     * 动态背景绘制相关
     */
//    private static final float VY_MULTIPLIER = 0.01f; // px/ms
//    private static final float VX_MULTIPLIER = 0.01f;
//    private static final int MIN_VY = 10;
//    private static final int MAX_VY = 30;
//    private static final int MIN_VX = 10;
//    private static final int MAX_VX = 30;
    private List<TextureFilter> upFilterList = new ArrayList<>();
//    private List<Bubble> bubbles = new ArrayList<>();//漂浮图标
//    private Bitmap bubbleBitmap;

    public KaraokeTexturePlayer() {
    }

    public void initPlayer(Context context, VideoTextureView videoTextureView, MergeVideoTextureView mergeVideoTextureView) {
        initFilterList(upFilterList);
//        bubbleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tv_score_music);
        Bitmap bitmapLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        Bitmap bitmapScoreBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.tv_bg_visualizer);
//        for (int i = 0; i < 50; i++) {
//            bubbles.add(createBubble(upFilterList));
//        }
        this.videoTextureView = videoTextureView;
        this.mergeVideoTextureView = mergeVideoTextureView;

        if (this.videoTextureView != null) {
//            this.videoTextureView.setBubbles(bubbles);
            this.videoTextureView.setLogoBitmap(bitmapLogo);
            this.videoTextureView.setScoreBitmap(bitmapScoreBg);
        }
        if (this.mergeVideoTextureView != null) {
//            this.mergeVideoTextureView.setBubbles(bubbles);
            this.mergeVideoTextureView.setLogoBitmap(bitmapLogo);
        }

        decodePlayer = new DecodeTexturePlayer(this.videoTextureView, this.mergeVideoTextureView);
        decodePlayer.setOnBasePlayerListener(this);
    }

    private void initFilterList(List<TextureFilter> filterList) {
        filterList.add(new BasicTextureFilter());
        filterList.add(new ContrastFilter(1.6f));
        filterList.add(new SaturationFilter(1.6f));
        filterList.add(new PixelationFilter(12));
        filterList.add(new HueFilter(100));
    }

//    private Bubble createBubble(List<TextureFilter> filterList) {
//        Random random = new Random();
//        TextureFilter textureFilter = filterList.get(random.nextInt(filterList.size()));
//        float vy = -(MIN_VY + random.nextInt(MAX_VY)) * VY_MULTIPLIER;
//        float vx = (MIN_VX + random.nextInt(MAX_VX)) * VX_MULTIPLIER;
//        vx = random.nextBoolean() ? vx : -vx;
//        float vRotate = 0.05f;
//        return new Bubble(new PointF(960, 540), vx, vy, vRotate, bubbleBitmap, textureFilter);
//    }


    /**
     * 播放媒体
     *
     * @param mainFilePath 主文件
     * @param preloadPath  预加载
     */
    public void playMedia(String mainFilePath, String preloadPath) {
        initParameters();
        this.mainFilePath = mainFilePath;
        this.preloadPath = preloadPath;
        if (decodePlayer != null) {
            this.decodePlayer.playEncode(this.mainFilePath, this.preloadPath);
        } else {
            Log.e(TAG, "播放器未实例化！！！！！！！！！！！！！");
        }
    }

    private void replay() {
        Log.d(TAG, "重唱 >>>>>>" + mainFilePath);
        if (!TextUtils.isEmpty(mainFilePath)) {
            playMedia(mainFilePath, preloadPath);
        }
    }


    private void initParameters() {
        mPreProgress = 0;
        mPreProgressCallback = 0;
        mPreScoreCallBack = 0;
        mCurTotalScore = 0;
        mCurScoreLine = -1;
        if (mShowScores != null) {
            mShowScores.clear();
        }
        if (scoreLineInfoList != null) {
            scoreLineInfoList.clear();
        }
        setScore(0);
        if (mOnScoreListener != null) {
            mOnScoreListener.onScoreCallback(0);
        }
    }

    /**
     * 播放
     */
    public void start(boolean volumeOn) {
        if (decodePlayer != null) {
            decodePlayer.start();
        }
        if (volumeOn)
            startVolInit();
        BnsAudioRecorder.getInstance().pause(false);
    }

    /**
     * 暂停
     */
    public void pause() {
        volOff();
        if (decodePlayer != null) {
            decodePlayer.pause();
        }
        BnsAudioRecorder.getInstance().pause(true);
    }

    public void release() {
        BnsAudioRecorder.getInstance().release();
        if (decodePlayer != null) {
            decodePlayer.release();
            decodePlayer = null;
        }
    }

    public void volOn() {
        startVolInit();
    }

    public void volOff() {
        mainHandler.removeCallbacks(runVolInit);
        setVolume(0.0F);
    }

    public void setVol(float vol) {
        currentVol = vol > 1.0F ? 1.0F : vol;
        Log.d(TAG, "setVol:" + currentVol);
    }

    public void setWindowStyle(WindowStyle style) {
        if (decodePlayer != null) {
            decodePlayer.setWindowStyle(style);
        }
    }


    public void setTone(int tone) {
        if (decodePlayer != null) {
            decodePlayer.setTone(tone);
        }
    }

    public void setScore(String scoreFilePath, String scoreFilePath2) {
        this.scoreFilePath = scoreFilePath;
        this.scoreFilePath2 = scoreFilePath2;
    }

    public void setScoreMode(int mode) {
        this.scoreMode = mode;
        if (mOnScoreListener != null) {
            mOnScoreListener.onScoreViewVisibility(scoreMode > 0);
        }
        if (videoTextureView != null) {
            videoTextureView.setDrawScoreView(scoreMode > 0);
        }
    }

    public void setScore(int score) {
        if (videoTextureView != null) {
            videoTextureView.setCurrentScore(score);
        }
    }

    public void setBackgroundFrame(Bitmap bitmap) {
        if (videoTextureView != null) {
            videoTextureView.setBackgroundFrame(bitmap);
        }
    }

    public void setBasePlayerListener(OnBasePlayerListener listener) {
        basePlayerListener = listener;
    }

    public void setOnScoreListener(OnScoreListener scoreListener) {
        this.mOnScoreListener = scoreListener;
    }

//    public void setOnKeyInfoListener(OnKeyInfoListener l) {
//        mOnKeyInfoListener = l;
//    }

    /**
     * 当前播放进度，单位毫秒
     *
     * @return 当前进度（单位：毫秒）
     */
    public int getCurrentPosition() {
        if (decodePlayer != null) {
            return decodePlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 视频总时长，单位毫秒
     *
     * @return 文件时长（单位毫秒）
     */
    public int getDuration() {
        if (decodePlayer != null) {
            return decodePlayer.getDuration();
        }
        return 0;
    }


    public void seekTo(float seek) {
        if (decodePlayer != null) {
            decodePlayer.seekTo(seek);
        }
    }


    public void selectTrack(int track) {
        if (decodePlayer != null) {
            decodePlayer.selectTrack(track);
        }
    }

    /**
     * 是否显示自定义动态背景
     *
     * @param isDraw 是否显示自定义动态背景(播放音频文件时使用)
     */
    public void setDrawCustomBackground(boolean isDraw) {
        if (videoTextureView != null) {
            videoTextureView.setDrawCustomBackground(isDraw);
        }
        if (mergeVideoTextureView != null) {
            mergeVideoTextureView.setDrawCustomBackground(isDraw);
        }
    }

    /***
     *  0:立体声 1：左声道 2：右声道
     * for sun chip（晨芯）
     **/
    public void setVolChannel(int channel) {
        if (decodePlayer != null) {
            decodePlayer.setVolChannel(channel);
        }
    }

    private void setVolume(float volume) {
        if (decodePlayer != null) {
            decodePlayer.setVolume(volume);
        }
    }

    @Override
    public void onPlayStart(String path) {
        Log.i(TAG, "onPlayStart >>>>>>>>>>>>>>>>>>> " + path);
        if (basePlayerListener != null) {
            basePlayerListener.onPlayStart(path);
        }
        //音频文件无画面，需要绘制自定义背景
        if (videoTextureView != null) {
            videoTextureView.setDrawCustomBackground(MediaUtils.isAudioFormatter(path));
        }
        if (mergeVideoTextureView != null) {
            mergeVideoTextureView.setDrawCustomBackground(MediaUtils.isAudioFormatter(path));
        }
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
        if (mPreProgress != current && ((curMilTime = System.currentTimeMillis()) - mPreProgressCallback) >= INTERVAL_PROGRESS) {
            if (curMilTime - mPreScoreCallBack >= 5000) {//每5秒回调分数
                int showScore = getCurScore();
                Log.d(TAG, "当前得分：" + showScore);
                mOnScoreListener.onScoreCallback(showScore);
                setScore(showScore);
                mPreScoreCallBack = curMilTime;
            }
            boolean progressException = (current - mPreProgress > 3 * INTERVAL_PROGRESS);
            Log.d(TAG, "当前进度：" + current / 1000 + " 上一秒进度：" + mPreProgress / 1000
                    + " 总时长：" + duration / 1000 + " 进度正常否：" + !progressException);
            if ((mPreProgress >= current || progressException) && current <= 60000 && duration >= 1200000) {//进度异常、当前进度小于1分、总长度超过20分钟
                mProgressExceptionTime++;
                Log.w(TAG, "scheduleAtFixedRate 播放器进度异常，上一秒:" + mPreProgress + "  当前:" + current + " 累计异常次数：" + mProgressExceptionTime);
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
        }
    }

    @Override
    public void onPlayCompletion(String path) {
        Log.i(TAG, "播放完成 :" + path);
        BnsAudioRecorder.getInstance().release();
        NativeScoreRunner.getInstance().stop();
        if (basePlayerListener != null) {
            basePlayerListener.onPlayCompletion(path);
        }
    }

    @Override
    public void onPlayError(String path, String error) {
        Log.e(TAG, "播放出错 :" + path + " error:" + error);
        BnsAudioRecorder.getInstance().release();
        NativeScoreRunner.getInstance().stop();
        if (basePlayerListener != null) {
            basePlayerListener.onPlayError(path, error);
        }
    }

    @Override
    public void audioByte(double[] bytes) {
        try {
            if (scoreLineInfoList != null && scoreLineInfoList.size() > 0) {
                int currentPosition = getCurrentPosition();
//                Log.d(TAG, "输入评分数据length：" + bytes.length + " 播放去时间轴：" + currentPosition);
                NativeScoreRunner.getInstance().addData(bytes, currentPosition);
            }
        } catch (Exception e) {
            Log.w(TAG, "audioByte ex:" + e.toString());
        }
    }

    private int calculateDecibel(byte[] buf, int mBufSize) {
        int sum = 0;
        for (int i = 0; i < mBufSize; i++) {
            sum += Math.abs(buf[i]);
        }
        // avg 10-50
        return sum / mBufSize;
    }

    @Override
    public void audioBytes(byte[] buffer, int bufSize) {
        byte[] bytes = new byte[bufSize];
        System.arraycopy(buffer, 0, bytes, 0, bufSize);
        int decibel = calculateDecibel(bytes, bufSize);
        if (videoTextureView != null) {
            videoTextureView.setMicVolume(decibel);
        }
//        if (mOnKeyInfoListener != null) {
////            Log.d(TAG, "录音音频波普数据length：" + bufSize);
//            mOnKeyInfoListener.onRecordData(buffer, bufSize);
//        }
    }

//    @Override
//    public void onOriginNotes(ArrayList<NoteInfo> noteInfos) {
//
//    }

//    @Override
//    public void onKeyInfoCallback(KeyInfo[] infos, int totalScore) {
//        if (mOnKeyInfoListener != null && (mScoreMode != 0)) {
//            mOnKeyInfoListener.onKeyInfoCallback(infos, totalScore);
//        }
//    }

//    @Override
//    public void onUpdateTime(long msTime) {
//
//    }
//
//    @Override
//    public void onRecordData(byte[] data, int bufSize) {
//        if (mOnKeyInfoListener != null) {
//            mOnKeyInfoListener.onRecordData(data, bufSize);
//        }
//    }

    public void setRecordFileName(String recordFileName) {
        this.mRecordFileName = recordFileName;
        BnsAudioRecorder.getInstance().setRecordName(mRecordFileName);
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
            try {
                BaseHttpRequest httpRequest = new BaseHttpRequest();
                httpRequest.setBaseHttpRequestListener(new BaseHttpRequestListener() {
                    @Override
                    public void onRequestCompletion(String url, String body) {
//                    NativeScoreRunner.getInstance().setOnKeyInfoListener(KaraokeTexturePlayer.this);
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
                httpRequest.get(ServerFileUtil.getFileUrl(scoreFilePath));

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
                httpRequest2.get(ServerFileUtil.getFileUrl(scoreFilePath2));
                return true;
            } catch (Exception e) {
                Log.e(TAG, "http 读取评分文件出错了", e);
            }
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
                setVolume(vol);
                mainHandler.postDelayed(this, 150);
                volUpCount++;
            }
        }
    };


    private Runnable runnableRecord = new Runnable() {
        @Override
        public void run() {
            boolean readScoreNode = initScore();
            Log.d(TAG, "是否评分歌曲:" + readScoreNode);
            if (videoTextureView != null) {
                videoTextureView.setDrawScoreView(readScoreNode && scoreMode > 0);
            }
            if (mOnScoreListener != null) {
                mOnScoreListener.onScoreViewVisibility(readScoreNode && scoreMode > 0);
            }
            if (readScoreNode || BnsAudioRecorder.getInstance().isEncodeMP3()) {//评分歌曲
                initRecord();
            }
        }
    };

}
