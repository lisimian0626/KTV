package com.beidousat.karaoke.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.fragment.topic.FmMoodDetail;
import com.beidousat.karaoke.util.FragmentUtil;
import com.bestarmedia.libcommon.model.vod.Topic;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/21 08:59.
 */

public class AdtMood extends RecyclerView.Adapter<AdtMood.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Topic> mData = new ArrayList<>();

    public AdtMood(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void addData(List<Topic> data) {
        this.mData = data;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerImageView ivCover;

        public ViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.iv_cover);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_mood, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Topic topic = mData.get(i);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.ivCover.getLayoutParams();
        layoutParams.width = topic.coverImgWidth;
        viewHolder.ivCover.setLayoutParams(layoutParams);
//        Logger.d("test","width:"+topic.coverImgWidth+"    hight:"+topic.coverImgHidth );
        Glide.with(mContext).load(topic.coverImgUrl).skipMemoryCache(false).into(viewHolder.ivCover);
        viewHolder.itemView.setOnClickListener(v -> {
            FmMoodDetail detail = FmMoodDetail.newInstance(topic);
            FragmentUtil.addFragment(detail, false, false, true, false);
        });
    }

}
