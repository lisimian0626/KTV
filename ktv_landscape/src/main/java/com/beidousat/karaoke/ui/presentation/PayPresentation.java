package com.beidousat.karaoke.ui.presentation;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.util.NetWorkUtils;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.layout.EnterAnimLayout;
import com.bestarmedia.texture.VideoTextureView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PayPresentation extends Presentation {

    private VideoTextureView videoTextureView;
    private RelativeLayout rlPay, rlTip;
    private RecyclerImageView ivBackground, ivQrCode, ivBestarAd;
    private EnterAnimLayout enterAnimLayout;
    private int mHdmiH, mHdmiW;
    private int payContentX, payContentY, payContentW, payContentH;


    public PayPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        Point realSize = new Point();
        display.getRealSize(realSize);
        mHdmiH = realSize.y;
        mHdmiW = realSize.x;
        payContentX = 0;
        payContentY = 0;
        payContentW = mHdmiW;
        payContentH = mHdmiH;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_pay);
        rlPay = findViewById(R.id.rl_pay);
        rlTip = findViewById(R.id.rl_tip);
        videoTextureView = findViewById(R.id.video_texture);
        enterAnimLayout = findViewById(R.id.enter_anim);
        ivBackground = findViewById(R.id.iv_background);
        ivQrCode = findViewById(R.id.iv_qr_code);
        ivBestarAd = findViewById(R.id.iv_ad);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    public VideoTextureView getVideoTextureView() {
        return videoTextureView;
    }

    public EnterAnimLayout getEnterAnimLayout() {
        return enterAnimLayout;
    }

    public RecyclerImageView getIvBackground() {
        return ivBackground;
    }

    public RecyclerImageView getIvQrCode() {
        return ivQrCode;
    }

    public RecyclerImageView getIvAd() {
        return ivBestarAd;
    }

    public void setPosition(int x, int y, int w, int h) {
        Log.d(getClass().getSimpleName(), "二维码坐标 x:" + x + " y:" + y + " w:" + w + " h:" + h);
        x = (int) ((float) getPayContentWidth() * x / getHdmiWidth());
        y = (int) ((float) getPayContentHeight() * y / getHdmiHeight());
        w = (int) ((float) getPayContentWidth() * w / getHdmiWidth());
        h = (int) ((float) getPayContentHeight() * h / getHdmiHeight());
        Log.d(getClass().getSimpleName(), "二维码坐标2 x:" + x + " y:" + y + " w:" + w + " h:" + h);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivQrCode.getLayoutParams();
        layoutParams.setMargins(x, y, 0, 0);
        layoutParams.width = w;
        layoutParams.height = h;
        ivQrCode.setLayoutParams(layoutParams);
        initScreenSize();
        setTipShow(!NetWorkUtils.isNetworkAvailable(getContext()));
    }

    /**
     * 以720P为标准
     */
    private float getScale() {
        return (float) getHdmiHeight() / 720;
    }

    private int getHdmiHeight() {
        return mHdmiH;
    }

    private int getHdmiWidth() {
        return mHdmiW;
    }


    public void setPayContentPositions(int payContentX, int payContentY, int payContentW, int payContentH) {
        this.payContentX = payContentX;
        this.payContentY = payContentY;
        this.payContentW = payContentW > 0 ? payContentW : getHdmiWidth();
        this.payContentH = payContentH > 0 ? payContentH : getHdmiHeight();
    }

    private int getPayContentX() {
        return this.payContentX;
    }

    private int getPayContentY() {
        return this.payContentY;
    }

    private int getPayContentWidth() {
        return this.payContentW;
    }

    private int getPayContentHeight() {
        return this.payContentH;
    }

    private void initScreenSize() {
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) rlPay.getLayoutParams();
        linearParams.width = (int) (getPayContentWidth() * getScale());
        linearParams.height = (int) (getPayContentHeight() * getScale());
        linearParams.setMargins((int) (getPayContentX() * getScale()), (int) (getPayContentY() * getScale()), 0, 0);
        rlPay.setLayoutParams(linearParams);
//        resizeQrCode();
    }

//    private void resizeQrCode() {
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivQrCode.getLayoutParams();
//        ivQrCode.setLayoutParams(layoutParams);
//        ivQrCode.postInvalidate();
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        if (event.id == EventBusId.Id.NETWORK_CHANGE) {
            boolean isEnable = Boolean.valueOf(event.data.toString());
            setTipShow(!isEnable);
        } else if (event.id == EventBusId.Id.PAY_INTERFACE_ERROR) {
            int isError = Integer.valueOf(event.data.toString());
            setTipShow(isError == 1);
        }
    }

    private void setTipShow(boolean isShow) {
        rlTip.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
