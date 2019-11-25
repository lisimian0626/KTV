package com.bestarmedia.libskin.attr;

import android.view.View;

/**
 * Created by J Wong on 2017/1/17.
 */

public class SkinAttr {
    public String resName;
    public SkinAttrType attrType;


    public SkinAttr(SkinAttrType attrType, String resName) {
        this.resName = resName;
        this.attrType = attrType;
    }

    public void apply(View view) {
        attrType.apply(view, resName);
    }
}
