package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.widget.adapter.AdtGift;
import com.bestarmedia.libcommon.model.erp.Good;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.recycler.GridRecyclerView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration2;

import java.util.List;
import java.util.Objects;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetGiftPage extends GridRecyclerView {

    private AdtGift mAdapter;

    public WidgetGiftPage(Context context) {
        super(context);
        init();
    }

    public WidgetGiftPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetGiftPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void notifyAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void runLayoutAnimation(boolean fromRight) {
        if (mAdapter != null) {
            Logger.d(getClass().getSimpleName(), "runLayoutAnimation fromRight>>>>" + fromRight);
            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),
                    fromRight ? R.anim.grid_layout_animation_from_right : R.anim.grid_layout_animation_from_left);
            setLayoutAnimation(controller);
            getAdapter().notifyDataSetChanged();
            scheduleLayoutAnimation();
        }
    }

    private void init() {

        setOverScrollMode(OVER_SCROLL_NEVER);

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(6).margin(6).build();

        VerticalDividerItemDecoration2 verDivider = new VerticalDividerItemDecoration2.Builder(getContext())
                .color(Color.TRANSPARENT).size(6).margin(6).build();

        setLayoutManager(new GridLayoutManager(getContext(), 2));

        addItemDecoration(horDivider);

        addItemDecoration(verDivider);

        mAdapter = new AdtGift(getContext());
        setAdapter(mAdapter);
    }


    public void setGoods(String cateId, List<Good> goods) {
        mAdapter.setData(cateId, goods);
        runLayoutAnimation(true);
    }
}

