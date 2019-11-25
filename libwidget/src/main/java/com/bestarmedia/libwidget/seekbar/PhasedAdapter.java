package com.bestarmedia.libwidget.seekbar;

import android.graphics.drawable.StateListDrawable;

public interface PhasedAdapter {

    int getCount();

    StateListDrawable getItem(int position);

}
