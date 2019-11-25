package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.beidousat.karaoke.widget.adapter.AdtConsume;
import com.bestarmedia.libcommon.model.erp.Consume;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.List;


/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetConsumePage extends RecyclerView {

    private AdtConsume mAdapter;

    public WidgetConsumePage(Context context) {
        super(context);
        init();
    }

    public WidgetConsumePage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetConsumePage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void notifyAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(8).margin(8).build();

        setLayoutManager(new LinearLayoutManager(getContext()));

        addItemDecoration(horDivider);

        mAdapter = new AdtConsume(getContext());
        setAdapter(mAdapter);
    }

    public void setConsumes(List<Consume> consumes) {
        mAdapter.setData(consumes);
    }


}

