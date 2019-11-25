package com.bestarmedia.libwidget.danmaku;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;

import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.util.DensityUtil;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.Duration;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class BarrageView extends DanmakuView {
    /**
     * 弹幕背景颜色
     */
    public static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;
    /**
     * 默认文字大小
     */
    public static final float DEFAULT_TEXT_SIZE = 26f;
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
    public static final int DEFAULT_DURATION = 15;

    private DanmakuContext danmakuContext;
    private BaseDanmakuParser mParser;
    private ExecutorService executorService;

    public BarrageView(Context context) {
        super(context);
    }

    public BarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarrageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param userName  用户名称
     * @param avatar    用户头像地址
     * @param text      弹幕
     * @param color     颜色
     * @param direction 1右向左（默认），2左向右；3上到下；4下到上
     * @param textSize  文字大小
     * @param bgColor   背景颜色
     * @param duration  播放时长
     */
    public void playDanmanku(final String userName, String avatar, final String text, final int color, int direction, final float textSize, final int bgColor, final int duration) {
        loadImage(avatar, userName, text, color, direction, textSize, bgColor, duration);
    }

    public void initDanmaku() {
        executorService = Executors.newFixedThreadPool(5);
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_FIX_TOP, 20); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        danmakuContext = DanmakuContext.create();
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_NONE, 0)//设置描边样式
                .setDuplicateMergingEnabled(false)////是否启用合并重复弹幕
                .setScrollSpeedFactor(60 / DEFAULT_DURATION)//设置弹幕滚动速度系数,只对滚动弹幕有效
                .setScaleTextSize(1.6f)
                .setCacheStuffer(new BackgroundCacheStuffer(Color.TRANSPARENT), null) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(10);
        mParser = createParser();
        setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void drawingFinished() {
            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {
            }

            @Override
            public void prepared() {
                start();
            }
        });
        prepare(mParser, danmakuContext);
        showFPS(false);
        enableDanmakuDrawingCache(true);
    }

    private BaseDanmakuParser createParser() {
        return new BaseDanmakuParser() {
            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        };
    }

    private SpannableStringBuilder createSpannable(Drawable drawable, String text) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("  " + text);
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }


    private void loadImage(String url, final String sender, String text, final int color, final int direction, final float textSize, final int bgColor, final int duration) {
        if (!TextUtils.isEmpty(url)) {
            Uri img = ServerFileUtil.getImageUrl(url);
            DanmakuImageLoader danmakuImageLoader = new DanmakuImageLoader(getContext(), text);
            danmakuImageLoader.setDanmakuImageLoaderListener((text2, drawable) ->
                    addDanmaku(direction, drawable, sender, text2, color, textSize, bgColor, duration));
            danmakuImageLoader.load(img);
        } else {
            addDanmaku(direction, null, sender, text, color, textSize, bgColor, duration);
        }
    }


    /**
     * @param direction 1右向左（默认），2左向右；3上到下；4下到上
     * @param drawable  图像
     * @param sender
     * @param text
     * @param color
     */
    private void addDanmaku(final int direction, final Drawable drawable, final String sender, String text,
                            final int color, final float textSize, final int bgColor, final int duration) {
        final String danmakuText = text.replaceAll("\r", " ").replaceAll("\n", " ");
        executorService.execute(() -> {
            try {
                danmakuContext.setCacheStuffer(new BackgroundCacheStuffer(bgColor), null).setScrollSpeedFactor((float) 60 / direction);
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
//                    BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_FIX_TOP);
                //seDanmaku createDanmaku(int type, float viewportWidth, float viewportHeight,float viewportSizeFactor, float scrollSpeedFactor)
//                    BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SPECIAL, 0.5f, 0.8f, 0.4f, 0.6f);
                if (drawable != null) {
                    int wh = DensityUtil.dip2px(getContext(), 32);
                    drawable.setBounds(0, 0, wh, wh);
                    danmaku.text = createSpannable(drawable, danmakuText);
                } else {
                    danmaku.text = !TextUtils.isEmpty(sender) ? (sender + "：" + danmakuText) : danmakuText;
                }
                danmaku.padding = 5;
                danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
                danmaku.isLive = true;
                danmaku.setTime(getCurrentTime() + 1200);
                danmaku.setDuration(new Duration(duration * 1000));//显示时长（只对不滚动生效）
                danmaku.textSize = textSize * (mParser.getDisplayer().getDensity() - 0.6f);
                danmaku.textColor = color;
                danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
                // danmaku.underlineColor = Color.GREEN;
                // danmaku.borderColor = Color.GREEN;
                addDanmaku(danmaku);
            } catch (Exception e) {
                Log.w(TAG, "addDanmaku :" + e.toString());
            }
        });
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//
//                super.run();
//            }
//        };
//        thread.start();
    }
}
