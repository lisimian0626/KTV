package com.beidousat.karaoke.ui.dialog.web;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;

public class DlgInputUrl extends BaseDialog implements View.OnClickListener {
    private EditText editTextEx;

    public DlgInputUrl(Context context) {
        super(context, R.style.MyDialog);
        init();
    }

    private void init() {
        this.setContentView(R.layout.dlg_input_url);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        findViewById(R.id.dlg_input_iv_close).setOnClickListener(this);
        findViewById(R.id.dlg_input_tv_ok).setOnClickListener(this);
        findViewById(R.id.dlg_input_tv_cancle).setOnClickListener(this);
        editTextEx = findViewById(R.id.dlg_input_et);
        editTextEx.setText("http://192.168.1.245:8086/lottery/");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dlg_input_iv_close:
                dismiss();
                break;
            case R.id.dlg_input_tv_ok:
                //打开网页
                DlgWebView dlgWebView = new DlgWebView(getContext(), editTextEx.getText().toString().trim());
                dlgWebView.show();
                dismiss();
                break;
            case R.id.dlg_input_tv_cancle:
                dismiss();
                break;
        }
    }

}
