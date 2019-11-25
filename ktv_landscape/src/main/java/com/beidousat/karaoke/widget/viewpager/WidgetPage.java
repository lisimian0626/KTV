package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.util.Logger;

/**
 * Created by J Wong on 2017/12/18.
 */

public class WidgetPage extends RelativeLayout implements View.OnClickListener {

    private View mRootView;
    private TextView tvPrePage, tvNextPage;
    private TextView tvPagesNum, tvFirstPage;
    private OnPageChangedListener mOnPageChangedListener;
    private int mCurrent, mTotal;

    public WidgetPage(Context context) {
        super(context);
        init();
    }

    public WidgetPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_page, this);
        tvPrePage = (TextView) mRootView.findViewById(R.id.tv_pre);
        tvNextPage = (TextView) mRootView.findViewById(R.id.tv_next);
        tvPagesNum = (TextView) mRootView.findViewById(R.id.tv_pages);
        tvFirstPage = (TextView) mRootView.findViewById(R.id.tv_first);

        tvPrePage.setOnClickListener(this);
        tvNextPage.setOnClickListener(this);
        tvPagesNum.setOnClickListener(this);
        tvFirstPage.setOnClickListener(this);

    }

    public void setPrePageTextColor(int resId) {
        tvPrePage.setTextColor(resId);
    }

    public void setNextPageTextColor(int resId) {
        tvNextPage.setTextColor(resId);
    }

    public void setPageNumTextColor(int resId) {
        tvPagesNum.setTextColor(resId);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pre:
                Logger.d(getClass().getSimpleName(), "onClick tv_pre ");
                if (mOnPageChangedListener != null) {
                    if (mCurrent > 0) {
                        int current = mCurrent;
                        mCurrent--;
                        mOnPageChangedListener.onPrePageClick(current, mCurrent);
                    } else {
                        mCurrent = mTotal;
                        mOnPageChangedListener.onNextPageClick(0, mCurrent);
                    }
                }
                break;
            case R.id.tv_next:
                Logger.d(getClass().getSimpleName(), "onClick tv_next ");
                if (mOnPageChangedListener != null) {
                    if (mCurrent < mTotal - 1) {
                        int current = mCurrent;
                        mCurrent++;
                        mOnPageChangedListener.onNextPageClick(current, mCurrent);
                    } else {
                        mCurrent = 0;
                        mOnPageChangedListener.onNextPageClick(mTotal, mCurrent);
                    }
                }
                break;
            case R.id.tv_first:
                Logger.d(getClass().getSimpleName(), "onClick tv_first ");
                if (mOnPageChangedListener != null && mCurrent != 0) {
                    mOnPageChangedListener.onFirstPageClick(mCurrent, 0);
                    mCurrent = 0;
                }
                break;
        }
    }

    public void setPrePressed(boolean pressed) {
//        if (mCurrent > 0) {
        tvPrePage.setPressed(pressed);
//        }
    }

    public void setNextPressed(boolean pressed) {
//        if (mCurrent + 1 < mTotal)
        tvNextPage.setPressed(pressed);
    }

    public int getCurrentPage() {
        return mCurrent;
    }

    public void setPageCurrent(int current) {
        mCurrent = current;
        tvPagesNum.setText((mCurrent + 1) + "/" + mTotal);
        setNextPressed(false);
        setPrePressed(false);
        checkButtonStatus();
    }

    public void setPageTotal(int total) {
        mTotal = total;
        tvPagesNum.setText((mCurrent + 1) + "/" + mTotal);
        setNextPressed(false);
        setPrePressed(false);
        checkButtonStatus();
    }

    private void checkButtonStatus() {
        tvPrePage.setVisibility(VISIBLE);
        tvNextPage.setVisibility(VISIBLE);
        tvPagesNum.setVisibility(VISIBLE);

        Logger.d(getClass().getSimpleName(), "checkButtonStatus mTotal:" + mTotal + "  mCurrent:" + mCurrent);
        if (mTotal > 1) {
            if (mCurrent > 0) {
                tvFirstPage.setVisibility(VISIBLE);
//                tvPrePage.setEnabled(true);
            } else {
                tvFirstPage.setVisibility(INVISIBLE);
//                tvPrePage.setEnabled(false);
            }
//            tvNextPage.setEnabled((mCurrent + 1) < mTotal);
        } else {
            tvFirstPage.setVisibility(INVISIBLE);
//            tvPrePage.setEnabled(false);
//            tvNextPage.setEnabled(false);
        }
    }

    public void setOnPageChangedListener(OnPageChangedListener listener) {
        this.mOnPageChangedListener = listener;
    }

    public interface OnPageChangedListener {

        void onPrePageClick(int before, int current);

        void onNextPageClick(int before, int current);

        void onFirstPageClick(int before, int current);
    }

}
