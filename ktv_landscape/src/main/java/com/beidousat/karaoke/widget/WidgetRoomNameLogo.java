package com.beidousat.karaoke.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.beidousat.karaoke.R;
import com.bestarmedia.libskin.SkinManager;
import com.bestarmedia.libwidget.anim.Anim;
import com.bestarmedia.libwidget.anim.EnterAnimUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.layout.EnterAnimLayout;
import com.bestarmedia.libwidget.text.GradientTextView;
import com.bestarmedia.libwidget.util.GlideUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WidgetRoomNameLogo extends EnterAnimLayout {

    private View mRootView;
    private EnterAnimLayout enterAnimLayout;
    private RecyclerImageView ivScreenLogo;
    private GradientTextView tvName;
    private boolean mIsPlaying;
    private ScheduledExecutorService mScheduledExecutorService;
    private String logoUrl, roomName;


    public WidgetRoomNameLogo(Context context) {
        super(context);
        initView();
    }

    public WidgetRoomNameLogo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WidgetRoomNameLogo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_room_name_logo, this);
        enterAnimLayout = mRootView.findViewById(R.id.root);
        ivScreenLogo = mRootView.findViewById(R.id.iv_screen_logo);
        tvName = mRootView.findViewById(R.id.tv_room_name);
    }

    public void loadData(String logoUrl, String roomName) {
        this.logoUrl = logoUrl;
        this.roomName = roomName;
        if (null != this.roomName) {
            tvName.setText(this.roomName);
        }
        loadLogo();
    }

    private void loadLogo() {
        if (!TextUtils.isEmpty(this.logoUrl)) {//加载店家logo
            Log.i(getClass().getSimpleName(), "loadData logoUrl:" + this.logoUrl);
            GlideUtils.LoadImage(getContext(), this.logoUrl, R.drawable.main_top_bar_logo, R.drawable.main_top_bar_logo, false, ivScreenLogo);
        } else {
            Drawable logo = SkinManager.getInstance().getResourceManager().getDrawableByName("main_top_bar_logo");
            if (logo != null) {//皮肤
                ivScreenLogo.setImageDrawable(logo);
            } else {
                GlideUtils.LoadImage(getContext(), R.drawable.main_top_bar_logo, false, ivScreenLogo);
            }
        }
    }

    public void startPlayer() {
        if (mIsPlaying || (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown()))
            return;
        mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduleAtFixedRate(mScheduledExecutorService);
        mIsPlaying = true;
    }

    public void stopPlayer() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdownNow();
            mScheduledExecutorService = null;
            mIsPlaying = false;
        }
    }

    private void scheduleAtFixedRate(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(() ->
                WidgetRoomNameLogo.this.post(() -> {
                    Anim anim = EnterAnimUtil.getRandomEnterAnim(enterAnimLayout);
                    anim.startAnimation(800);
                    if (tvName.getVisibility() == View.VISIBLE) {
                        tvName.setVisibility(GONE);
                        ivScreenLogo.setVisibility(VISIBLE);
                        loadLogo();
                    } else {
                        tvName.setVisibility(VISIBLE);
                        ivScreenLogo.setVisibility(GONE);
                    }
                }), 5, 5, TimeUnit.SECONDS);
    }
}
