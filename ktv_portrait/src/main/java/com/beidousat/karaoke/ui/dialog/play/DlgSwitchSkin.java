package com.beidousat.karaoke.ui.dialog.play;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;


public class DlgSwitchSkin extends BaseDialog implements OnClickListener {

    private final String TAG = DlgSwitchSkin.class.getSimpleName();
    private ProgressBar mProgress;
    private TextView mTvMsg;
    private Button button;

    public DlgSwitchSkin(Context context) {
        super(context, R.style.MyDialog);
        init();
    }

    private void init() {
        this.setContentView(R.layout.dlg_init_loading);
        if (getWindow() == null)
            return;
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 500;
        lp.height = 300;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        mProgress = findViewById(R.id.pgb_progress);
        mTvMsg = findViewById(R.id.tv_text);
        button = findViewById(android.R.id.button1);
        button.setOnClickListener(this);
        button.setVisibility(View.GONE);
    }

    public void showProgress(boolean show) {
        mProgress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public void showOkButton(String text) {
        button.setText(text);
        button.setVisibility(View.VISIBLE);
    }

    public void setMessage(CharSequence text) {
        mTvMsg.setText(text);
    }

    public void setMessage(int resId) {
        mTvMsg.setText(resId);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == android.R.id.button1) {
            dismiss();
        }
    }
}
