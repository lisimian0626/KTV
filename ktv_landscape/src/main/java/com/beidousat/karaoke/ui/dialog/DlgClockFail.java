package com.beidousat.karaoke.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.beidousat.karaoke.R;


public class DlgClockFail extends BaseDialog {

    private final String TAG = DlgClockFail.class.getSimpleName();

    private TextView mTvTitle, mTvMsg;
    private boolean mAutoDismiss;

    public DlgClockFail(Context context) {
        super(context, R.style.MyDialog);
        init();
    }

    public DlgClockFail setTitle(String text) {
        mTvTitle.setText(text);
        return this;
    }

    public DlgClockFail setMessage(String text) {
        mTvMsg.setText(text);
        return this;
    }

    public DlgClockFail setTitleIcon(int resId) {
        mTvTitle.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return this;
    }

    public DlgClockFail setAutoDismiss(boolean autoDismiss) {
        this.mAutoDismiss = autoDismiss;
        return this;
    }

    void init() {
        this.setContentView(R.layout.dlg_clock_fail);
        if (getWindow() == null)
            return;
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 540;
        lp.height = 300;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mTvTitle = findViewById(R.id.tv_title);
        mTvMsg = findViewById(R.id.tv_msg);
    }

    @Override
    public void show() {
        super.show();
        if (mAutoDismiss) {
            mTvTitle.postDelayed(this::dismiss, 3000);
        }
    }
}
