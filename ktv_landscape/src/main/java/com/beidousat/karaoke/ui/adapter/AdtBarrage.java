package com.beidousat.karaoke.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.vod.emoji.Barrage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/21 08:59.
 */

public class AdtBarrage extends RecyclerView.Adapter<AdtBarrage.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Barrage> mData = new ArrayList<>();
    private onCliceBarrage onCliceBarrage;

    public AdtBarrage(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void addData(List<Barrage> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void setOnCliceBarrage(AdtBarrage.onCliceBarrage onCliceBarrage) {
        this.onCliceBarrage = onCliceBarrage;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tv_barrage);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_atmosphere_barrages, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Barrage barrage = mData.get(i);
        viewHolder.textView.setText(barrage.content);
        viewHolder.itemView.setOnClickListener(v -> {
            if (onCliceBarrage != null) {
                onCliceBarrage.onClickBarrage(barrage);
            }
        });
    }

    public interface onCliceBarrage {
        void onClickBarrage(Barrage barrage);
    }
}
