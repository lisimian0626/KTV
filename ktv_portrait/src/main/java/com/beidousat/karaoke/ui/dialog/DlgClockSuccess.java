package com.beidousat.karaoke.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.beidousat.karaoke.R;


public class DlgClockSuccess extends BaseDialog {

    private TextView mTvTitle, mTvName, mTvNo;
    private boolean mAutoDismiss;

    public DlgClockSuccess(Context context) {
        super(context, R.style.MyDialog);
        init();
    }

    public DlgClockSuccess setTitle(String text) {
        mTvTitle.setText(text);
        return this;
    }

    private DlgClockSuccess setMessage(String name, String no) {
        if (null != name)
            mTvName.setText(name);
        if (null != no)
            mTvNo.setText(no);
        return this;
    }

    public DlgClockSuccess setMessage(String info) {
        if (!TextUtils.isEmpty(info)) {
            if (info.contains(",")) {
                String[] noName = info.split(",");
                setMessage(getContext().getString(R.string.job_num, noName.length > 0 ? noName[0] : ""),
                        getContext().getString(R.string.stage_name, noName.length > 1 ? noName[1] : ""));
            } else {
                mTvName.setText(info);
                mTvNo.setVisibility(View.INVISIBLE);
            }
        }
        return this;
    }

    public DlgClockSuccess setTitleIcon(int resId) {
        mTvTitle.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return this;
    }

    public DlgClockSuccess setAutoDismiss(boolean autoDismiss) {
        this.mAutoDismiss = autoDismiss;
        return this;
    }

    void init() {
        this.setContentView(R.layout.dlg_clock_success);
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 540;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mTvTitle = findViewById(R.id.tv_title);
        mTvName = findViewById(R.id.tv_name);
        mTvNo = findViewById(R.id.tv_no);

    }

    @Override
    public void show() {
        super.show();
        if (mAutoDismiss) {
            mTvTitle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, 2000);
        }
    }
}
