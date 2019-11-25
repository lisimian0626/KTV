package com.beidousat.karaoke.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.beidousat.karaoke.R;


public class PromptDialog extends BaseDialog implements OnClickListener {

    private final String TAG = PromptDialog.class.getSimpleName();

    private TextView mTvMsg, mTvTitle;
    private Button button, button2;
    private View.OnClickListener mPositiveListener, mPositiveListener2;
//    private Activity outerActivity;

    public PromptDialog(Context context) {
        super(context, R.style.MyDialog);
//        outerActivity = (Activity) context;
        init();
    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return outerActivity.dispatchTouchEvent(event);
//    }
    void init() {
        this.setContentView(R.layout.prompt_dialog);
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 500;
        lp.height = 300;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mTvTitle = findViewById(android.R.id.title);
        mTvMsg = findViewById(android.R.id.text1);
        button = findViewById(android.R.id.button1);
        button.setOnClickListener(this);
        button2 = findViewById(android.R.id.button2);
        button2.setOnClickListener(this);
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

    public void setPositiveButton(String text, View.OnClickListener listener) {
        this.button.setText(text);
        this.mPositiveListener = listener;
    }

    public void setPositiveButton2(String text, View.OnClickListener listener) {
        this.button2.setVisibility(View.VISIBLE);
        this.button2.setText(text);
        this.mPositiveListener2 = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                if (mPositiveListener != null) {
                    mPositiveListener.onClick(v);
                }
                dismiss();
                break;
            case android.R.id.button2:
                if (mPositiveListener2 != null) {
                    mPositiveListener2.onClick(v);
                }
                dismiss();
                break;
        }
    }
}
