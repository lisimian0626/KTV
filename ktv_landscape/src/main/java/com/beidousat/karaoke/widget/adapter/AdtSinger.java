package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.interf.OnSingerClickListener;
import com.bestarmedia.libcommon.model.v4.Start;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 08:54.
 */
public class AdtSinger extends RecyclerView.Adapter<AdtSinger.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Start> mData = new ArrayList<>();

    public AdtSinger(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<Start> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover;
        private TextView tvName;

        private ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_singer, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivCover = view.findViewById(R.id.iv_cover);
        viewHolder.tvName = view.findViewById(android.R.id.title);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Start starInfo = mData.get(position);
        holder.tvName.setText(starInfo.musicianName);
        Glide.with(mContext).load(!TextUtils.isEmpty(starInfo.imgFileUrl) ? starInfo.imgFileUrl : starInfo.imgFilePath).override(140, 140).centerCrop()
                .placeholder(R.drawable.star_default).error(R.drawable.star_default).into(holder.ivCover);
        holder.itemView.setOnClickListener(v -> {
            if (mOnSingerClickListener != null) {
                mOnSingerClickListener.onSingerClick(starInfo);
            }
        });
    }

    private OnSingerClickListener mOnSingerClickListener;

    public void setOnSingerClickListener(OnSingerClickListener listener) {
        this.mOnSingerClickListener = listener;
    }
}
