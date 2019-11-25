package com.beidousat.karaoke.ui.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.util.QrCodeUtil;


public class PromptDialogBig extends BaseDialog implements OnClickListener {

    private final String TAG = PromptDialogBig.class.getSimpleName();
    public static PromptDialogBig mIntance;
    private TextView mTvMsg, mTvTitle, mTvTips;
    private TextView tv_cancle, tv_ok;
    private ImageView iv_back, iv_qrcode;

    public static PromptDialogBig getIntance(Context context) {
        if (context == null) {
            mIntance = new PromptDialogBig(context);
        }
        return mIntance;
    }

    public PromptDialogBig(Context context) {
        super(context, R.style.MyDialog);
        init();
    }

    private View.OnClickListener mCancleListener, mOkListener;

    void init() {
        this.setContentView(R.layout.dlg_prompt_big);
        if(getWindow()==null)
            return;
        LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mTvTitle = findViewById(R.id.dlg_prompt_big_title);
        mTvMsg = findViewById(R.id.dlg_prompt_big_conntent);
        mTvTips = findViewById(R.id.dlg_prompt_big_tips);
        iv_qrcode = findViewById(R.id.dlg_prompt_qrcode);
        tv_cancle = findViewById(R.id.dlg_prompt_big_cancle);
        tv_ok = findViewById(R.id.dlg_prompt_big_ok);
        tv_cancle.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        iv_back = findViewById(R.id.dlg_prompt_big_iv_close);
        iv_back.setOnClickListener(this);
    }

    public void setTitle(int resId) {
        mTvTitle.setText(resId);
    }

    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    public void setMessage(CharSequence text) {
        mTvMsg.setVisibility(View.VISIBLE);
        mTvMsg.setText(text);
    }

    public void setMessage(int resId) {
        mTvMsg.setVisibility(View.VISIBLE);
        mTvMsg.setText(resId);
    }

    public void setTips(CharSequence text) {
        mTvTips.setVisibility(View.VISIBLE);
        mTvTips.setText(text);
    }

    public void setTips(int resId) {
        mTvTips.setVisibility(View.VISIBLE);
        mTvTips.setText(resId);
    }

    public void setQrcode(String url) {
        Bitmap bitmap = QrCodeUtil.createQRCode(url);
        if (bitmap != null) {
            iv_qrcode.setImageBitmap(bitmap);
        }
        iv_qrcode.setVisibility(View.VISIBLE);
    }

    public void setQrcode(int ResId) {
        iv_qrcode.setImageResource(ResId);
        iv_qrcode.setVisibility(View.VISIBLE);
    }

    public void setCancleButton(boolean isshow, String text, View.OnClickListener listener) {
        if (isshow) {
            this.tv_cancle.setVisibility(View.VISIBLE);
        } else {
            this.tv_cancle.setVisibility(View.GONE);
        }
        this.mCancleListener = listener;
        this.tv_cancle.setText(text);
    }

    public void setOkButton(boolean isshow, String text, View.OnClickListener listener) {
        if (isshow) {
            this.tv_ok.setVisibility(View.VISIBLE);
        } else {
            this.tv_ok.setVisibility(View.GONE);
        }
        this.mOkListener = listener;
        this.tv_ok.setText(text);
    }

    public void showIvClose(boolean isshow) {
        if (isshow) {
            iv_back.setVisibility(View.VISIBLE);
        } else {
            iv_back.setVisibility(View.GONE);
            iv_back.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dlg_prompt_big_cancle:
                if (mCancleListener != null) {
                    mCancleListener.onClick(v);
                }
                dismiss();
                break;
            case R.id.dlg_prompt_big_ok:
                if (mOkListener != null) {
                    mOkListener.onClick(v);
                }
                dismiss();
                break;
            case R.id.dlg_prompt_big_iv_close:
                dismiss();
                break;
        }
    }
}
