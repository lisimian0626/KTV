package com.beidousat.karaoke.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;


public class PromptDialogSmall extends BaseDialog implements OnClickListener {

    private final String TAG = PromptDialogSmall.class.getSimpleName();

    private TextView mTvMsg, mTvTitle;
    private TextView  tv_ok;
    private ImageView iv_close;
    public PromptDialogSmall(Context context) {
        super(context, R.style.MyDialog);
        init();
    }
    private View.OnClickListener mOkListener;

    void init() {
        this.setContentView(R.layout.dlg_prompt_small);
        LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mTvTitle = findViewById(R.id.dlg_prompt_small_tv_title);
        mTvMsg = findViewById(R.id.dlg_prompt_small_tv_conntont);
        tv_ok = findViewById(R.id.dlg_prompt_small_tv_ok);
        tv_ok.setOnClickListener(this);
        iv_close=findViewById(R.id.dlg_prompt_small_iv_close);
        iv_close.setOnClickListener(this);
    }

    public void setTitle(int resId) {
        mTvTitle.setText(resId);
    }

    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    public void setMessage(CharSequence text) {
        mTvMsg.setText(text);
    }

    public void setMessage(int resId) {
        mTvMsg.setText(resId);
    }


    public void setOkButton(boolean isshow,String text,View.OnClickListener listener) {
        if(isshow){
            this.tv_ok.setVisibility(View.VISIBLE);
        }else{
            this.tv_ok.setVisibility(View.GONE);
        }
        this.mOkListener = listener;
        this.tv_ok.setText(text);
    }

    public void showIvClose(boolean ishow){
        if(ishow){
            iv_close.setVisibility(View.VISIBLE);
        }else{
            iv_close.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dlg_prompt_small_iv_close:
                dismiss();
                break;
            case R.id.dlg_prompt_small_tv_ok:
                if (mOkListener != null) {
                    mOkListener.onClick(v);
                }
                dismiss();
                break;
        }
    }
}
