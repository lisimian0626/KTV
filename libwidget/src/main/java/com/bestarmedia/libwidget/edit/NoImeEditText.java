package com.bestarmedia.libwidget.edit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by J Wong on 2017/4/6.
 */

public class NoImeEditText extends EditText {

    public NoImeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return false;
    }
}