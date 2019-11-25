package com.beidousat.karaoke.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.util.Logger;

/**
 * Created by J Wong on 2018/7/17.
 */

public class PopAppDownload extends PopupWindow {

    private Context mContext;

    private View contentView;

    private int popupWidth;

    private int popupHeight;

    public PopAppDownload(Context context) {
        super(context);
        mContext = context;
        contentView = View.inflate(context, R.layout.popup_app_download, null);
        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置点击隐藏的属性
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
        this.update();

        //获取自身的长宽高
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = contentView.getMeasuredHeight();
        popupWidth = contentView.getMeasuredWidth();
        Logger.d(getClass().getSimpleName(), "PopAppDownload popupWidth:" + popupWidth + " popupHeight:" + popupHeight);

        this.setAnimationStyle(R.style.dialogWindowAnim);

    }

    public void showAnchorTop(View anchor) {//获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        Logger.d(getClass().getSimpleName(), "showAnchorTop x:" + location[0] + "  y:" + location[1]);
//        showAtLocation(anchor, Gravity.NO_GRAVITY, (location[0] + anchor.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);

        showAtLocation(anchor, Gravity.NO_GRAVITY, 595, 100);
    }
}
