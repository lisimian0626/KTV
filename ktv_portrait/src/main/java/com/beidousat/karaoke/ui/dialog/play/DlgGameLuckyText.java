package com.beidousat.karaoke.ui.dialog.play;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libwidget.image.RecyclerImageView;

/**
 * Created by J Wong on 2018/4/26.
 */

public class DlgGameLuckyText extends BaseDialog implements View.OnClickListener {

    private View.OnClickListener mOnCancelListener, mOnOkListener;
    private RecyclerImageView mRivText;

    public DlgGameLuckyText(Context context) {
        super(context, R.style.MyDialog);
        init();
    }


    private void init() {
        this.setContentView(R.layout.dlg_game_lucky_text);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = 680;
        lp.height = 480;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mRivText = findViewById(R.id.riv_text);
        mRivText.setOnClickListener(this);

        findViewById(R.id.riv_dlg_yes).setOnClickListener(this);
        findViewById(R.id.riv_dlg_no).setOnClickListener(this);
    }

    public void setTestResId(int resId) {
        mRivText.setImageResource(resId);
    }

    public void setOnNolListener(View.OnClickListener listener) {
        this.mOnCancelListener = listener;
        findViewById(R.id.riv_dlg_yes).setVisibility(mOnCancelListener != null ? View.VISIBLE : View.GONE);
    }

    public void setOnOkListener(View.OnClickListener listener) {
        this.mOnOkListener = listener;
        findViewById(R.id.riv_dlg_yes).setVisibility(mOnOkListener != null ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_dlg_no:
                if (mOnCancelListener != null)
                    mOnCancelListener.onClick(v);
                break;
            case R.id.riv_dlg_yes:
                if (mOnOkListener != null)
                    mOnOkListener.onClick(v);
                break;
        }
    }

}
