package com.beidousat.karaoke.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beidousat.karaoke.R;


public class DlgInitLoading extends BaseDialog implements OnClickListener {

    private final String TAG = DlgInitLoading.class.getSimpleName();
    private ProgressBar mProgress;
    private TextView mTvMsg;
    private Button button;
    private View.OnClickListener mPositiveListener;

    public DlgInitLoading(Context context) {
        super(context, R.style.MyDialog);
        init();
    }

    void init() {
        this.setContentView(R.layout.dlg_init_loading);
        if (getWindow() == null)
            return;
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 500;
        lp.height = 300;
        lp.dimAmount = 0.7f;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(false);
        mProgress = findViewById(R.id.pgb_progress);
        mTvMsg = findViewById(R.id.tv_text);
        button = findViewById(android.R.id.button1);
        button.setOnClickListener(this);
        button.setVisibility(View.GONE);
        button.postDelayed(new Thread(() -> button.setVisibility(View.VISIBLE)), 10 * 1000);
    }

    public void setMessage(CharSequence text) {
        mTvMsg.setText(text);
    }

    public void setMessage(int resId) {
        mTvMsg.setText(resId);
    }

    public void setPositiveButton(String text, View.OnClickListener listener) {
        this.button.setText(text);
        this.mPositiveListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == android.R.id.button1) {
            if (mPositiveListener != null) {
                mPositiveListener.onClick(v);
            }
            dismiss();
        }
    }
}
