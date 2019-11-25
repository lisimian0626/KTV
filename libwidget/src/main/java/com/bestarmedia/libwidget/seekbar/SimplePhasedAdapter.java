package com.bestarmedia.libwidget.seekbar;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class SimplePhasedAdapter implements PhasedAdapter {

    protected StateListDrawable[] mItems;

    public SimplePhasedAdapter(Drawable[] items) {
        mItems = new StateListDrawable[items.length];
        Drawable drawable;
        for (int i = 0; i < items.length; i++) {
            drawable = items[i];
            if (drawable instanceof StateListDrawable) {
                mItems[i] = (StateListDrawable) drawable;
            } else {
                mItems[i] = new StateListDrawable();
                mItems[i].addState(new int[]{}, drawable);
            }
        }
    }

    public void setDrawables(Drawable[] items) {
        mItems = new StateListDrawable[items.length];
        Drawable drawable;
        for (int i = 0; i < items.length; i++) {
            drawable = items[i];
            if (drawable instanceof StateListDrawable) {
                mItems[i] = (StateListDrawable) drawable;
            } else {
                mItems[i] = new StateListDrawable();
                mItems[i].addState(new int[]{}, drawable);
            }
        }

    }

    public SimplePhasedAdapter(Resources resources, int[] items) {
        int size = items.length;
        mItems = new StateListDrawable[size];
        Drawable drawable;
        for (int i = 0; i < size; i++) {
            drawable = resources.getDrawable(items[i]);
            if (drawable instanceof StateListDrawable) {
                mItems[i] = (StateListDrawable) drawable;
            } else {
                mItems[i] = new StateListDrawable();
                mItems[i].addState(new int[]{}, drawable);
            }
        }
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public StateListDrawable getItem(int position) {
        return mItems[position];
    }

}
