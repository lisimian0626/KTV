package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.erp.Consume;
import com.bestarmedia.libcommon.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2016/5/3.
 */
public class AdtConsume extends RecyclerView.Adapter<AdtConsume.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Consume> mData = new ArrayList<>();

    public AdtConsume(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<Consume> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvCount, tvPrice, tvTotal;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_bill, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvName = view.findViewById(R.id.tv_name);
        viewHolder.tvCount = view.findViewById(R.id.tv_count);
        viewHolder.tvPrice = view.findViewById(R.id.tv_price);
        viewHolder.tvTotal = view.findViewById(R.id.tv_total);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Consume consume = mData.get(position);
        holder.tvName.setText(consume.Name);
        holder.tvCount.setText(String.valueOf(consume.getPointNum()));
        holder.tvPrice.setText(mContext.getString(R.string.rmb_x, String.valueOf(consume.getPrice())));
        try {
            int pointType = Integer.valueOf(consume.PointType);
            boolean isBill = (pointType == 0);
            float total = consume.getPrice() * consume.getPointNum();
            Logger.d(getClass().getSimpleName(), "onBindViewHolder isBill:" + isBill + " total:" + total);
            holder.tvTotal.setText(mContext.getString(R.string.rmb_x, String.valueOf(isBill ? total : 0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
