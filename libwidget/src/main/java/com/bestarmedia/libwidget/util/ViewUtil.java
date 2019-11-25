package com.bestarmedia.libwidget.util;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by J Wong on 2017/12/29.
 */

public class ViewUtil {

    public static void setTextMarquee(TextView textView) {
        if (textView != null) {
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSingleLine(true);
            textView.setSelected(true);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
        }
    }
}
