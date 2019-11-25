package com.beidousat.karaoke.ui.dialog;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.BaseActivity;
import com.bestarmedia.libcommon.ad.AdPlayRecorder;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.anim.Anim;
import com.bestarmedia.libwidget.anim.AnimBaiYeChuang;
import com.bestarmedia.libwidget.anim.EnterAnimUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.layout.EnterAnimLayout;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bumptech.glide.Glide;

/**
 * Created by J Wong on 2017/5/22.
 */

public class DlgAdScreen extends DialogFragment implements View.OnClickListener {

    private EnterAnimLayout enterAnimLayout;
    private RecyclerImageView mRivAd1;
    private ADModel mAd;

    private DialogInterface.OnDismissListener mOnClickListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        this.mOnClickListener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnClickListener != null) {
            mOnClickListener.onDismiss(dialog);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_ad_screen, container);
        enterAnimLayout = view.findViewById(R.id.root);
        mRivAd1 = view.findViewById(R.id.riv_ad_screen1);
        mRivAd1.setOnClickListener(this);
        if (mAd != null) {
            showAdScreen(mAd);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        BaseActivity.mLastTouchTime = System.currentTimeMillis();
        dismiss();
    }

    public void showAdScreen(ADModel ad) {
        mAd = ad;
        if (mRivAd1 == null || getContext() == null) {
            Logger.w("DlgAdScreen", "屏保广告对象为空！");
            return;
        }
        try {
            String[] urls = ad.getAdContent().split("\\|");
            Uri uri = ServerFileUtil.getImageUrl(urls[1]);
            int[] wh = DensityUtil.getScreenWidthHeight(getContext());
            Glide.with(this).load(uri).override(wh[0], wh[1]).into(mRivAd1);
            new AdPlayRecorder(getContext().getApplicationContext()).recordAdPlay(mAd);
            Anim anim = EnterAnimUtil.getRandomEnterAnim(enterAnimLayout);
            anim.startAnimation(800);
        } catch (Exception e) {
            Logger.e(getClass().getSimpleName(), "解析图片路径出错", e);
        }
    }
}
