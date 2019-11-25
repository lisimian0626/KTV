package com.beidousat.karaoke.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beidousat.karaoke.R;


public class DlgExitInput extends BaseDialog implements OnClickListener {
    private TextView mTvTitle;
    private Button btnOK, btnCancel;
    private EditText mEditText;
    private OnOkListener mPositiveListener;

    public DlgExitInput(Context context) {
        super(context, R.style.MyDialog);
        init();
    }

    void init() {
        this.setContentView(R.layout.dlg_exit_input);
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 500;
        lp.height = 300;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mTvTitle = findViewById(android.R.id.title);
        btnOK =  findViewById(android.R.id.button2);
        btnCancel =  findViewById(android.R.id.button1);
        mEditText =  findViewById(android.R.id.edit);
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    public void setTitle(CharSequence text) {
        mTvTitle.setText(text);
    }

    public void setEditText(CharSequence text) {
        mEditText.setText(text);
    }

//    public void setCancleButton(String text, View.OnClickListener listener) {
//        this.btnCancel.setText(text);
//        this.mCancelListener = listener;
//    }

    public void setPositiveButton(String text, OnOkListener listener) {
        this.btnOK.setText(text);
        this.mPositiveListener = listener;
    }

    public interface OnOkListener {
        void onOk(String editText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button2:
                if (mPositiveListener != null) {
                    mPositiveListener.onOk(mEditText.getText().toString());
                }
                break;
            case android.R.id.button1:
//                if (mCancelListener != null) {
//                    mCancelListener.onClick(v);
//                }
                dismiss();
                break;
        }
    }
}
