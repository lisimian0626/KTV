package com.beidousat.karaoke.interf;

/**
 * Created by J Wong on 2016/8/8.
 */
public interface OnPageScrollListener {
    
    void onPageScrollLeft();

    void onPageScrollRight();

    void onPagerSelected(int position, boolean isLeft);

}
