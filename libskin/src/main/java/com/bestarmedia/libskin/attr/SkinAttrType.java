package com.bestarmedia.libskin.attr;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bestarmedia.libskin.ResourceManager;
import com.bestarmedia.libskin.SkinManager;
import com.bestarmedia.libskin.utils.L;


/**
 * Created by J Wong on 2017/1/17.
 */

public enum SkinAttrType {
    BACKGROUND("background") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourceManager().getDrawableByName(resName);
            if (drawable != null) {
                view.setBackground(drawable);
            } else {
                try {
                    int color = getResourceManager().getColor(resName);
                    view.setBackgroundColor(color);
                } catch (Resources.NotFoundException ex) {
                    L.e("apply ex:" + ex.toString());
                }
            }
        }
    }, SRC("src") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof ImageView) {
                Drawable drawable = getResourceManager().getDrawableByName(resName);
                if (drawable == null) return;
                ((ImageView) view).setImageDrawable(drawable);
            }

        }
    }, COLOR("textColor") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof TextView) {
                ColorStateList colorList = getResourceManager().getColorStateList(resName);
                if (colorList == null) {
                    colorList = getResourceManager().getColorStateList2(resName);
                }
                if (colorList == null)
                    return;
                ((TextView) view).setTextColor(colorList);
            }
        }
    }, COLOR_HINT("textColorHint") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof TextView) {
                ColorStateList colorList = getResourceManager().getColorStateList(resName);
                if (colorList == null) {
                    colorList = getResourceManager().getColorStateList2(resName);
                }
                if (colorList == null)
                    return;
                ((TextView) view).setHintTextColor(colorList);
            }
        }
    }, COLOR_HIGHLIGHT("textColorHighlight") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof TextView) {
                ColorStateList colorList = getResourceManager().getColorStateList(resName);
                if (colorList == null) {
                    colorList = getResourceManager().getColorStateList2(resName);
                }
                if (colorList == null)
                    return;
                ((TextView) view).setHighlightColor(colorList.getDefaultColor());
            }
        }
    }, DRAWABLE_TOP("drawableTop") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof TextView) {
                Drawable drawableTop = getResourceManager().getDrawableByName(resName);
                if (drawableTop == null) return;
                TextView textView = (TextView) view;
                textView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            }
        }
    }, DRAWABLE_LEFT("drawableLeft") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof TextView) {
                Drawable drawableLeft = getResourceManager().getDrawableByName(resName);
                if (drawableLeft == null) return;
                TextView textView = (TextView) view;
                textView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            }
        }
    }, DRAWABLE_RIGHT("drawableRight") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof TextView) {
                Drawable drawableLeft = getResourceManager().getDrawableByName(resName);
                if (drawableLeft == null) return;
                TextView textView = (TextView) view;
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableLeft, null);
            }
        }
    }, DRAWABLE_BOTTOM("drawableBottom") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof TextView) {
                Drawable drawableLeft = getResourceManager().getDrawableByName(resName);
                if (drawableLeft == null) return;
                TextView textView = (TextView) view;
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawableLeft);
            }
        }
    }, PROGRESS_DRAWABLE("progressDrawable") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof ProgressBar) {
                Drawable drawable = getResourceManager().getDrawableByName(resName);
                if (drawable == null) return;
                ProgressBar progressBar = (ProgressBar) view;
                progressBar.setProgressDrawable(drawable);
            }
        }
    }, DIVIDER("divider") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof ListView) {
                Drawable divider = getResourceManager().getDrawableByName(resName);
                if (divider == null) return;
                ((ListView) view).setDivider(divider);
            }
        }
    };

    String attrType;

    SkinAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getAttrType() {
        return attrType;
    }


    public abstract void apply(View view, String resName);

    public ResourceManager getResourceManager() {
        return SkinManager.getInstance().getResourceManager();
    }

}
