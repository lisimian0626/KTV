package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import com.beidousat.karaoke.interf.OnPageScrollListener;

/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetBasePager extends ViewPager {

    Context mContext;

    private static final int MOVE_LIMITATION = 100;// 触发移动的像素距离

    private float mLastMotionX; // 手指触碰屏幕的最后一次x坐标

    private Scroller mScroller; // 滑动控件

    private boolean isScrollable = true;

    private float mLastTouchX;

    private OnPageScrollListener mOnPageScrollListener;

    private int mCurrentPage = 0;

    public WidgetBasePager(Context context) {
        super(context);
        initView(context);
    }

    public WidgetBasePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mScroller = new Scroller(context);
        setPageMargin(20);
        ViewPagerScroller.setViewPagerScrollDuration(WidgetBasePager.this, 500);
        setOffscreenPageLimit(2);
        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mLastTouchX = 0;
                if (mOnPageScrollListener != null) {
                    boolean isLeft = position >= mCurrentPage;
                    if (getAdapter() != null) {
                        int realPage = getAdapter().getCount();
                        mOnPageScrollListener.onPagerSelected(realPage > 0 ? position % realPage : position, isLeft);
                    }
                    mCurrentPage = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    public void setScrollable(boolean scrollable) {
        this.isScrollable = scrollable;
    }

    @Override
    public void setCurrentItem(int item) {
//        Logger.e("WidgetBasePager", " setCurrentItem:" + item);
        super.setCurrentItem(item);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
//        Logger.e("WidgetBasePager", " setCurrentItem:" + item);
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isScrollable) {
            return false;
        }
        try {
            final int action = event.getAction();
            final float x = event.getX();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mLastTouchX > 0) {
                        if (mLastTouchX < x) {
                            if (mOnPageScrollListener != null)
                                mOnPageScrollListener.onPageScrollLeft();
                        } else if (mLastTouchX > x) {
                            if (mOnPageScrollListener != null)
                                mOnPageScrollListener.onPageScrollRight();
                        }
                    }
                    mLastTouchX = x;
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - mLastMotionX) >= MOVE_LIMITATION) {
                        final int curItem = getCurrentItem();
                        if (x - mLastMotionX > 0) {//
                            if (curItem > 0) {
                                postDelayed(() -> setCurrentItem(curItem - 1), 100);
                            } else
                                postDelayed(() -> {
                                    if (getAdapter() != null) {
                                        setCurrentItem(getAdapter().getCount() - 1);
                                    }
                                }, 100);
                        } else {
                            if (getAdapter() != null && curItem < getAdapter().getCount() - 1) {
                                postDelayed(() -> setCurrentItem(curItem + 1), 100);
                            } else {
                                postDelayed(() -> setCurrentItem(0), 100);
                            }
                        }
                        return super.onTouchEvent(event);
                    }
                default:
                    break;
            }
            return super.onTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    @Override
//    public void setCurrentItem(int item) {
//        super.setCurrentItem(getAdapter() != null && getAdapter().getCount() > 0 ? item % getAdapter().getCount() : item);
//    }
//
//    @Override
//    public void setCurrentItem(int item, boolean smoothScroll) {
//        super.setCurrentItem(getAdapter() != null && getAdapter().getCount() > 0 ? item % getAdapter().getCount() : item, smoothScroll);
//    }
//
//    @Override
//    public int getCurrentItem() {
//        int currentItem = getAdapter() != null && getAdapter().getCount() > 0 ? super.getCurrentItem() % getAdapter().getCount() : super.getCurrentItem();
//        return currentItem;
//    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!isScrollable) {
            return false;
        }
        try {
            final int action = arg0.getAction();
            final float x = arg0.getX();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return super.onInterceptTouchEvent(arg0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setOnPagerScrollListener(OnPageScrollListener listener) {
        this.mOnPageScrollListener = listener;
    }
}