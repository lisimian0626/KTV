package com.beidousat.karaoke.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;


public class FeedbackDialog extends BaseDialog implements OnClickListener {

    private final String TAG = FeedbackDialog.class.getSimpleName();
    private RelativeLayout relativeLayout;
    private TextView mTvMsg;
    private Button button;
    private ImageView close, qrcode;
    private ProgressBar progressBar;
//    private Activity outerActivity;

    public FeedbackDialog(Activity context) {
        super(context, R.style.MyDialog);
//        outerActivity = (Activity) context;
        init();
    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return outerActivity.dispatchTouchEvent(event);
//    }
    void init() {
        this.setContentView(R.layout.feedback_dialog);
        if (getWindow() == null)
            return;
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 500;
        lp.height = 360;
        lp.dimAmount = 0.7f;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        relativeLayout = findViewById(R.id.feeback_bg);
        qrcode = findViewById(R.id.feedback_iv_qrcode);
        mTvMsg = findViewById(android.R.id.text1);
        button = findViewById(android.R.id.button1);
        close = findViewById(android.R.id.icon1);
        progressBar = findViewById(R.id.pgb_progress);
        close.setOnClickListener(this);
        button.setOnClickListener(this);
    }

//    public void setTitle(int resId) {
//        mTvTitle.setText(resId);
//    }
//
//    public void setTitle(CharSequence title) {
//        mTvTitle.setText(title);
//    }

    private void setMessage(CharSequence text) {
        mTvMsg.setText(text);
    }

    private void setMessage(int resId) {
        mTvMsg.setText(resId);
    }

    public void setSucced(int resId) {
        setMessage(resId);
        relativeLayout.setBackgroundResource(R.drawable.feedback);
        qrcode.setImageResource(R.drawable.feed_back_succed);
        qrcode.setVisibility(View.VISIBLE);
        close.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }

    public void setLoading(int resId) {
        setMessage(resId);
        relativeLayout.setBackgroundResource(R.drawable.feedback);
        qrcode.setVisibility(View.GONE);
        close.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);
    }

    public void setFail(int resId) {
        setMessage(resId);
        relativeLayout.setBackgroundResource(R.drawable.feedback_fail);
        qrcode.setImageResource(R.drawable.beixing_qrcode);
        qrcode.setVisibility(View.VISIBLE);
        close.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                dismiss();
                break;
            case android.R.id.icon1:
                dismiss();
                break;
        }
    }
}
