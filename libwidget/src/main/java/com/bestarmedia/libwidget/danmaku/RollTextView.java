package com.bestarmedia.libwidget.danmaku;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.bestarmedia.libcommon.model.view.RollText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class RollTextView extends DanmakuView {

    /**
     * 弹幕背景颜色
     */
    public static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;
    /**
     * 默认文字大小
     */
    public static final float DEFAULT_TEXT_SIZE = 42f;
    /**
     * 默认文字颜色
     */
    public static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    /**
     * 滚动方向，右边向左边
     */
    public static final int DEFAULT_DIRECTION = 1;
    /**
     * 播放时长
     */
    public static final int DEFAULT_DURATION = 30;
    /**
     * 播放间隔
     */
    public static final float DEFAULT_SCROLL_SPEED = 1.0f;//越小越快，

    private DanmakuContext danmakuContext;

    private BaseDanmakuParser mParser;
    /**
     * 播放
     */
    private boolean isPlaying;
    /**
     * 等待播放队列
     */
    private List<RollText> queue = new ArrayList<>();
    private RollText playingRollText;
    /**
     * 播放间隔
     */
    private int playInterval = 15;
    private OnRollListener onRollListener;
    private long timeShown = 0;

    public RollTextView(Context context) {
        super(context);
        init();
    }

    public RollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public int getPlayInterval() {
        return playInterval;
    }

    /**
     * 播放间隔，单位秒
     */
    public void setPlayInterval(int interval) {
        this.playInterval = interval;
    }

    public void setOnRollListener(OnRollListener listener) {
        this.onRollListener = listener;
    }

    /**
     * @param isPriority 是否优先
     * @param text
     */
    public void addRollText(boolean isPriority, String id, final String text) {
        addRollText(isPriority, id, text, DEFAULT_TEXT_COLOR);
    }

    /**
     * @param isPriority 是否优先
     * @param text
     * @param color
     */
    public void addRollText(boolean isPriority, String id, final String text, final int color) {
        createDanmaku(isPriority, id, DEFAULT_DIRECTION, text, color, DEFAULT_TEXT_SIZE, DEFAULT_BACKGROUND_COLOR, DEFAULT_DURATION);
    }

    /**
     * @param isPriority 是否优先
     * @param text
     * @param color
     * @param duration   播放时长
     */
    public void addRollText(boolean isPriority, String id, final String text, final int color, int duration) {
        createDanmaku(isPriority, id, DEFAULT_DIRECTION, text, color, DEFAULT_TEXT_SIZE, DEFAULT_BACKGROUND_COLOR, duration);
    }


    /**
     * @param isPriority 是否优先
     * @param text       弹幕
     * @param color      颜色
     * @param direction  1右向左（默认），2左向右；3上到下；4下到上
     * @param textSize   文字大小
     * @param bgColor    背景颜色
     * @param duration   播放时长
     */
    public void addRollText(boolean isPriority, String id, final int direction, final String text, final int color, final float textSize, final int bgColor, int duration) {
        createDanmaku(isPriority, id, direction, text, color, textSize, bgColor, duration);
    }

    private void init() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 1); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        danmakuContext = DanmakuContext.create();
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 4)//设置描边样式
                .setDuplicateMergingEnabled(false)////是否启用合并重复弹幕
                .setScrollSpeedFactor(DEFAULT_SCROLL_SPEED)//设置弹幕滚动速度系数,只对滚动弹幕有效
                .setScaleTextSize(1.6f)
                .setCacheStuffer(new SpannedCacheStuffer(), null) // 图文混排使用SpannedCacheStuffer
                .setMaximumLines(maxLinesPair)
                .setMaximumVisibleSizeInScreen(1)
                .preventOverlapping(overlappingEnablePair);
        mParser = createParser();
        setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void drawingFinished() {
                Log.d("RollTextView", "drawingFinished 用时:" + (System.currentTimeMillis() - timeShown) + " 速度：" + DEFAULT_SCROLL_SPEED);
                queue.remove(0);
                isPlaying = false;
                getHandler().postDelayed(() -> {
                    tryToPlayFromQueue();
                }, playInterval * 1000);
            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {
                timeShown = System.currentTimeMillis();
                Log.d("RollTextView", "danmakuShown >>>>>>>>>>>>>>>>>>" + danmaku.getDuration());
                isPlaying = true;
                if (onRollListener != null) {
                    onRollListener.onRollStart(playingRollText);
                }
            }

            @Override
            public void prepared() {
                start();
            }
        });
        prepare(mParser, danmakuContext);
        showFPS(false);
        enableDanmakuDrawingCache(false);
    }


    private BaseDanmakuParser createParser() {
        return new BaseDanmakuParser() {
            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        };
    }


    /**
     * @param direction 1右向左（默认），2左向右；3上到下；4下到上
     * @param text
     * @param color
     */
    private void createDanmaku(boolean isPriority, String id, final int direction, String text, final int color, final float textSize, final int bgColor, final int duration) {
        RollText rollText = new RollText(id, direction, text, color, textSize, bgColor, duration);
        if (isPriority) {
            queue.add(0, rollText);
        } else {
            queue.add(rollText);
        }
        tryToPlayFromQueue();
    }

    public void playDanmaku(String danmakuText, final int duration, final int direction, final float textSize, final int textColor) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "显示时长：" + duration);
                    float speed = (float) duration * 1000 / 7500;
                    if (speed < 1.0f) {
                        speed = 1.0f;
                    }
                    Log.d(TAG, "修正后显示时长：" + duration + " 速度系数：" + speed);
                    danmakuContext.setScrollSpeedFactor(speed);
//                        danmakuContext.setScrollSpeedFactor(DEFAULT_SCROLL_SPEED);
                    int scrollType;
                    switch (direction) {
                        case 2:
                            scrollType = BaseDanmaku.TYPE_SCROLL_LR;
                            break;
                        case 3:
                            scrollType = BaseDanmaku.TYPE_FIX_TOP;
                            break;
                        case 4:
                            scrollType = BaseDanmaku.TYPE_FIX_BOTTOM;
                            break;
                        default:
                            scrollType = BaseDanmaku.TYPE_SCROLL_RL;
                            break;
                    }
                    BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(scrollType);
                    danmaku.text = danmakuText;
                    danmaku.padding = 5;
                    danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
                    danmaku.isLive = false;
                    danmaku.setTime(getCurrentTime() + 1000);
                    danmaku.textSize = textSize * (mParser.getDisplayer().getDensity() - 0.6f);
                    danmaku.textColor = textColor;
//                        danmaku.setDuration(new Duration(rollText.duration * 1000));//显示时长（只对不滚动生效）
                    danmaku.textShadowColor = Color.BLACK;
//                        danmaku.borderColor = Color.GREEN;//背景边框
                    // danmaku.underlineColor = Color.GREEN;//下划线颜色
                    addDanmaku(danmaku);
                    isPlaying = true;
                } catch (Exception e) {
                    Log.e(TAG, "play Danmaku :" + e.toString());
                }
                super.run();
            }
        };
        thread.start();
    }

    public void playDanmaku(RollText rollText) {
        final String danmakuText = rollText.text.replaceAll("\r", " ").replaceAll("\n", " ");
        int duration = rollText.duration;
        if (duration < 10) {
            duration = 10;
        }
        playDanmaku(danmakuText, duration, rollText.direction, rollText.textSize, rollText.color);
    }

    private void tryToPlayFromQueue() {
        if (!isPlaying && queue.size() > 0) {
            final RollText rollText = queue.get(0);
            isPlaying = true;
            if (onRollListener != null) {
                onRollListener.onRollStart(playingRollText);
            }
            playDanmaku(rollText);
            playingRollText = rollText;
        } else {
            if (onRollListener != null && queue.isEmpty()) {
                onRollListener.onRollQueueEmpty(playingRollText);
            }
        }
    }

    public interface OnRollListener {
        void onRollStart(RollText text);

        void onRollQueueEmpty(RollText last);
    }
}
