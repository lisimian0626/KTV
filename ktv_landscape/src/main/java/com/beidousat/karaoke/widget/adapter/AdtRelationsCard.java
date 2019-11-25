package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.erp.RelationsCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2018/6/26.
 */
public class AdtRelationsCard extends RecyclerView.Adapter<AdtRelationsCard.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<RelationsCard> mData = new ArrayList<>();

    public AdtRelationsCard(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    public void setData(List<RelationsCard> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View contentView;
        private TextView tvNo, tvNum, tvName, tvTime;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() >= 8 ? mData.size() : 8;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_relations_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.contentView = view.findViewById(R.id.contentView);
        viewHolder.tvNo = view.findViewById(R.id.tv_no);
        viewHolder.tvNum = view.findViewById(R.id.tv_num);
        viewHolder.tvName = view.findViewById(R.id.tv_name);
        viewHolder.tvTime = view.findViewById(R.id.tv_time);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.contentView.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#24ffffff") : Color.parseColor("#1affffff"));
        if (position < mData.size()) {
            final RelationsCard card = mData.get(position);
            int no = position + 1;
            holder.tvNo.setText(String.valueOf(no < 10 ? ("0" + no) : no));
            holder.tvNum.setText(card.id);
            holder.tvName.setText(card.name);
            holder.tvTime.setText(card.date);
        } else {
            holder.tvNo.setText("");
            holder.tvNum.setText("");
            holder.tvName.setText("");
            holder.tvTime.setText("");
        }
    }
}
