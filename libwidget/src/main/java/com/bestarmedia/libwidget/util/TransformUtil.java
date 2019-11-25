package com.bestarmedia.libwidget.util;

import android.support.v4.view.ViewPager;

public class TransformUtil {

    public static void reverse(ViewPager viewPager, ViewPager.PageTransformer transformer) {
        if (viewPager != null) {
            viewPager.setPageTransformer(true, transformer);
        }
    }

    public static void forward(ViewPager viewPager, ViewPager.PageTransformer transformer) {
        if (viewPager != null) {
            viewPager.setPageTransformer(false, transformer);
        }
    }

}
