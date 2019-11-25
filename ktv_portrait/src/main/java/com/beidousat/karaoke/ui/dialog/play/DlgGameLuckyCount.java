package com.beidousat.karaoke.ui.dialog.play;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;

/**
 * Created by J Wong on 2018/4/26.
 */

public class DlgGameLuckyCount extends BaseDialog implements View.OnClickListener {

    private OnGameLuckNumListener mOnGameLuckNumListener;
    private String mNum = "0";

    public DlgGameLuckyCount(Context context) {
        super(context, R.style.MyDialog);
        init();
    }


    private void init() {
        this.setContentView(R.layout.dlg_game_lucky_count);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = 680;
        lp.height = 480;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        findViewById(R.id.riv_num0).setOnClickListener(this);
        findViewById(R.id.riv_num1).setOnClickListener(this);
        findViewById(R.id.riv_num2).setOnClickListener(this);
        findViewById(R.id.riv_num3).setOnClickListener(this);
        findViewById(R.id.riv_num4).setOnClickListener(this);
        findViewById(R.id.riv_num5).setOnClickListener(this);
        findViewById(R.id.riv_num6).setOnClickListener(this);
        findViewById(R.id.riv_num7).setOnClickListener(this);
        findViewById(R.id.riv_num8).setOnClickListener(this);
        findViewById(R.id.riv_num9).setOnClickListener(this);
        findViewById(R.id.riv_del).setOnClickListener(this);
        findViewById(R.id.riv_dlg_yes).setOnClickListener(this);
        findViewById(R.id.riv_dlg_no).setOnClickListener(this);
    }

    public void setOnGameLuckNumListener(OnGameLuckNumListener listener) {
        this.mOnGameLuckNumListener = listener;
    }

    private void callbackNum() {
        if (mOnGameLuckNumListener != null) {
            mOnGameLuckNumListener.onGameLuckNum(Integer.valueOf(mNum));
        }
    }

    private boolean canPlus() {
        return mNum != null && mNum.length() < 6;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_num0:
                if (canPlus()) {
                    mNum = mNum + "0";
                    callbackNum();
                }
                break;
            case R.id.riv_num1:
                if (canPlus()) {
                    mNum = mNum + "1";
                    callbackNum();
                }
                break;
            case R.id.riv_num2:
                if (canPlus()) {
                    mNum = mNum + "2";
                    callbackNum();
                }
                break;
            case R.id.riv_num3:
                if (canPlus()) {
                    mNum = mNum + "3";
                    callbackNum();
                }
                break;
            case R.id.riv_num4:
                if (canPlus()) {
                    mNum = mNum + "4";
                    callbackNum();
                }
                break;
            case R.id.riv_num5:
                if (canPlus()) {
                    mNum = mNum + "5";
                    callbackNum();
                }
                break;
            case R.id.riv_num6:
                if (canPlus()) {
                    mNum = mNum + "6";
                    callbackNum();
                }
                break;
            case R.id.riv_num7:
                if (canPlus()) {
                    mNum = mNum + "7";
                    callbackNum();
                }
                break;
            case R.id.riv_num8:
                if (canPlus()) {
                    mNum = mNum + "8";
                    callbackNum();
                }
                break;
            case R.id.riv_num9:
                if (canPlus()) {
                    mNum = mNum + "9";
                    callbackNum();
                }
                break;
            case R.id.riv_del:
                if (!TextUtils.isEmpty(mNum)) {
                    if (mNum.length() > 1) {
                        mNum = mNum.substring(0, mNum.length() - 1);
                    } else {
                        mNum = "0";
                    }
                    callbackNum();
                }
                break;
            case R.id.riv_dlg_no:
                dismiss();
                break;
            case R.id.riv_dlg_yes:
                dismiss();
                break;
        }
    }

    public interface OnGameLuckNumListener {
        void onGameLuckNum(int count);
    }
}
