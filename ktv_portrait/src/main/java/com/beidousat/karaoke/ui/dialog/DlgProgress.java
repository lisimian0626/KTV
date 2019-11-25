package com.beidousat.karaoke.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beidousat.karaoke.R;

/**
 * Created by J Wong on 2016/12/9.
 */

public class DlgProgress extends BaseDialog {

    private TextView tvTitle;
    private TextView tvTip;
    private TextView tvProgress;
    private ProgressBar mProgressBar;

    public DlgProgress(Activity context) {
        super(context, R.style.MyDialog);
        init();
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTip(String tip) {
        tvTip.setText(tip);
    }

    public void setProgress(long progress, long total) {
        tvProgress.setText(progress + "/" + total);
        mProgressBar.setProgress((int) ((100 * progress) / total));
    }

    public void setProgressText(String text) {
        tvProgress.setText(text);
    }

    public void setProgress(float progress) {
        mProgressBar.setProgress((int) (100 * progress));
    }

    void init() {
        this.setContentView(R.layout.dlg_progress);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = 500;
        lp.height = 300;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        tvTitle = findViewById(R.id.tv_title);
        tvTip = findViewById(R.id.tv_tip);
        tvProgress = findViewById(R.id.tv_progress);
        mProgressBar = findViewById(R.id.pgb_progress);
        setCanceledOnTouchOutside(false);
    }

}