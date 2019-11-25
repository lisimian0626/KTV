package com.beidousat.karaoke.widget;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.recycler.AutoFitXYLayoutManager;
import com.bestarmedia.libwidget.recycler.AutoPollRecyclerView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2018/8/20.
 */

public class WidgetMvImagePlayer extends LinearLayout {

    private List<String> mUrls;
    private AutoPollRecyclerView mRecyclerView;

    public WidgetMvImagePlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.wiget_mv_image_player, this);
        mRecyclerView = rootView.findViewById(R.id.recycler);
        AutoFitXYLayoutManager layoutManager = new AutoFitXYLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(DensityUtil.dip2px(getContext(), 10)).build();
        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(DensityUtil.dip2px(getContext(), 10)).build();
        mRecyclerView.addItemDecoration(horDivider);
        mRecyclerView.addItemDecoration(verDivider);
    }

    public void startPlayer() {
        mRecyclerView.start();
    }

    public void stopPlayer() {
        mRecyclerView.stop();
    }

    public void setImages(String images) {
        Logger.d(getClass().getSimpleName(), "images:" + images);
        if (!TextUtils.isEmpty(images)) {
            String[] imageArray = images.split("\\|");
            if (imageArray != null && imageArray.length > 0) {
                mUrls = new ArrayList<String>();
                for (int i = 0; i < imageArray.length; i++) {
                    mUrls.add(imageArray[i]);
                }
            }
            AutoPollAdapter adapter = new AutoPollAdapter(getContext(), mUrls);
            mRecyclerView.setAdapter(adapter);
        }
    }

    public class AutoPollAdapter extends RecyclerView.Adapter<BaseViewHolder> {

        private final Context mContext;

        private final List<String> mData;

        public AutoPollAdapter(Context context, List<String> list) {
            this.mContext = context;
            this.mData = list;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_mv_image, parent, false);
            BaseViewHolder holder = new BaseViewHolder(view);
            holder.ivCover = view.findViewById(R.id.cover_image);
            return holder;
        }

        @Override
        public void onBindViewHolder(final BaseViewHolder holder, int position) {
            String data = mData.get(position % mData.size());
            Uri mvImage = ServerFileUtil.getImageUrl(data);
            if (mvImage != null) {//获取图片真正的宽高
                Glide.with(getContext()).load(mvImage).into(holder.ivCover);
            }
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public RecyclerImageView ivCover;

        public BaseViewHolder(View view) {
            super(view);
        }
    }
}
