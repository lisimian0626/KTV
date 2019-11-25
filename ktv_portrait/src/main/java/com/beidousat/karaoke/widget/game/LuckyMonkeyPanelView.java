package com.beidousat.karaoke.widget.game;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.util.Logger;

import java.util.HashMap;

/**
 * Created by J Wong on 2018/3/29.
 */


public class LuckyMonkeyPanelView extends FrameLayout {


    private ImageView bg_1;
    private ImageView bg_2;

    private PanelItemView itemView1, itemView2, itemView3,
            itemView4, itemView6,
            itemView7, itemView8, itemView9;

    private ItemView[] itemViewArr = new ItemView[8];
    private int currentIndex = 0;
    private int currentTotal = 0;
    private int stayIndex = 0;

    private boolean isMarqueeRunning = false;
    private boolean isGameRunning = false;
    private boolean isTryToStop = false;

    private static final int DEFAULT_SPEED = 300;
    private static final int MIN_SPEED = 100;
    private int currentSpeed = DEFAULT_SPEED;
    private String[] mTexts;


    public LuckyMonkeyPanelView(@NonNull Context context) {
        this(context, null);
    }

    public LuckyMonkeyPanelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyMonkeyPanelView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.widget_lucky_mokey_panel, this);
        setupView();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startMarquee();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopMarquee();
        super.onDetachedFromWindow();
    }

    private void setupView() {

        initSounds();

        bg_1 =  findViewById(R.id.bg_1);
        bg_2 = findViewById(R.id.bg_2);
        itemView1 =  findViewById(R.id.item1);
        itemView2 =  findViewById(R.id.item2);
        itemView3 =  findViewById(R.id.item3);
        itemView4 =  findViewById(R.id.item4);
        itemView6 =  findViewById(R.id.item6);
        itemView7 =  findViewById(R.id.item7);
        itemView8 =  findViewById(R.id.item8);
        itemView9 =  findViewById(R.id.item9);

        itemViewArr[0] = itemView4;
        itemViewArr[1] = itemView1;
        itemViewArr[2] = itemView2;
        itemViewArr[3] = itemView3;
        itemViewArr[4] = itemView6;
        itemViewArr[5] = itemView9;
        itemViewArr[6] = itemView8;
        itemViewArr[7] = itemView7;

    }


    public void setTexts(String[] texts) {
        mTexts = texts;
        itemView4.setGameText(mTexts[0]);
        itemView1.setGameText(mTexts[1]);
        itemView2.setGameText(mTexts[2]);
        itemView3.setGameText(mTexts[3]);
        itemView6.setGameText(mTexts[4]);
        itemView9.setGameText(mTexts[5]);
        itemView8.setGameText(mTexts[6]);
        itemView7.setGameText(mTexts[7]);
    }

    public void stopMarquee() {
        isMarqueeRunning = false;
        isGameRunning = false;
        isTryToStop = false;
    }

    private void startMarquee() {
        isMarqueeRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isMarqueeRunning) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (bg_1 != null && bg_2 != null) {
                                if (VISIBLE == bg_1.getVisibility()) {
                                    bg_1.setVisibility(GONE);
                                    bg_2.setVisibility(VISIBLE);
                                } else {
                                    bg_1.setVisibility(VISIBLE);
                                    bg_2.setVisibility(GONE);
                                }
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private long getInterruptTime() {
        currentTotal++;
        if (isTryToStop) {
            currentSpeed += 10;
            if (currentSpeed > DEFAULT_SPEED) {
                currentSpeed = DEFAULT_SPEED;
            }
        } else {
            if (currentTotal / itemViewArr.length > 0) {
                currentSpeed -= 30;
            }
            if (currentSpeed < MIN_SPEED) {
                currentSpeed = MIN_SPEED;
            }
        }
        return currentSpeed;
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public void startGame() {

        isGameRunning = true;
        isTryToStop = false;
        currentSpeed = DEFAULT_SPEED;

        new Thread(new Runnable() {
            @Override
            public void run() {
                playSound(2, 0);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (isGameRunning) {
                    try {
                        long sleep = getInterruptTime();
                        Logger.d(getClass().getSimpleName(), "getInterruptTime sleep>>>" + sleep);
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    post(new Runnable() {
                        @Override
                        public void run() {
                            int preIndex = currentIndex;
                            currentIndex++;
                            if (currentIndex >= itemViewArr.length) {
                                currentIndex = 0;
                            }
                            itemViewArr[preIndex].setFocus(false);
                            itemViewArr[currentIndex].setFocus(true);
                            playSound(1, 0);
                            mOnGameLuckyListener.onGameLuckyChanged(currentIndex);

                            if (!isGameRunning) {
                                try {
                                    if (mMediaPlayer.isPlaying())
                                        mMediaPlayer.pause();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (mOnGameLuckyListener != null) {
                                    String text = mTexts[(currentIndex) % mTexts.length];
                                    mOnGameLuckyListener.onGameLuckyFinish(text);
                                }
                                playSound(3, 0);
                                postDelayed(runStart, 4500);
                            }

                            if (isTryToStop && currentSpeed == DEFAULT_SPEED && stayIndex == currentIndex) {
                                isGameRunning = false;
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private Runnable runStart = new Runnable() {
        @Override
        public void run() {
            try {
                if (!mMediaPlayer.isPlaying())
                    mMediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void tryToStop(int position) {
        stayIndex = position;
        isTryToStop = true;
    }


    private MediaPlayer mMediaPlayer;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;

    /**
     * 初始化声音的方法
     */
    public void initSounds() {
        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.lucky_bg);
        mMediaPlayer.setLooping(true);

        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<>();
        soundPoolMap.put(1, soundPool.load(getContext(), R.raw.lucky_focus, 1));
        soundPoolMap.put(2, soundPool.load(getContext(), R.raw.lucky_ready_go, 1));
        soundPoolMap.put(3, soundPool.load(getContext(), R.raw.lucky_result, 1));

    }

    /**
     * 播放音效的方法
     */
    public void playSound(int sound, int loop) {
//        AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
//        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        float volume = streamVolumeCurrent / streamVolumeMax;
        soundPool.play(soundPoolMap.get(sound), 1, 1, 1, loop, 1f);
    }

    public void stopSound() {
        try {
            soundPool.autoPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBgMusic() {
        try {
            mMediaPlayer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startBgMusic() {
        try {
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnGameLuckyListener mOnGameLuckyListener;

    public void setOnGameLuckyListener(OnGameLuckyListener luckyListener) {
        mOnGameLuckyListener = luckyListener;
    }

    public interface OnGameLuckyListener {
        void onGameLuckyChanged(int position);

        void onGameLuckyFinish(String text);
    }
}
