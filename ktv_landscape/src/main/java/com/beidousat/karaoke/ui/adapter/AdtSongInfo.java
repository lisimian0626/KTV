package com.beidousat.karaoke.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.vod.Statistic;

import java.util.List;


public class AdtSongInfo extends RecyclerView.Adapter<AdtSongInfo.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Statistic> mListStatistic;

    public AdtSongInfo(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Statistic> statisticBeanList) {
        this.mListStatistic = statisticBeanList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_content;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mListStatistic == null ? 0 : mListStatistic.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.list_songinfo_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tv_name = view.findViewById(R.id.tv_name);
        viewHolder.tv_content = view.findViewById(R.id.tv_content);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Statistic statisticBean = mListStatistic.get(i);
        viewHolder.tv_name.setText(statisticBean.getName());
        viewHolder.tv_content.setText(statisticBean.getCount());

    }

}
