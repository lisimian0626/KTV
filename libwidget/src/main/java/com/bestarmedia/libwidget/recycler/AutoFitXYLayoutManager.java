package com.bestarmedia.libwidget.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by J Wong on 2018/8/20.
 */

public class AutoFitXYLayoutManager extends LinearLayoutManager {

    public AutoFitXYLayoutManager(Context context) {
        super(context);
    }

    public AutoFitXYLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public AutoFitXYLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
//        try {
//            View view = recycler.getViewForPosition(0);
//            if (view != null) {
//                measureChild(view, widthSpec, heightSpec);
//                int measuredWidth = View.MeasureSpec.getSize(widthSpec);
//                int measuredHeight = view.getMeasuredHeight();
//                setMeasuredDimension(measuredWidth, measuredHeight);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
