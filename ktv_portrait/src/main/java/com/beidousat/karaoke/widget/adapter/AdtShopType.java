package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.erp.GoodCategory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by J Wong on 2016/5/3.
 */
public class AdtShopType extends RecyclerView.Adapter<AdtShopType.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<GoodCategory> mData = new ArrayList<GoodCategory>();

    public AdtShopType(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    private int mFocusTab;

    public void setFocusTab(int focusTab) {
        mFocusTab = focusTab;
    }

    public void setData(List<GoodCategory> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        public ViewHolder(View view) {
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
        View view = mInflater.inflate(R.layout.list_item_shop_type, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvName =  view.findViewById(R.id.tv_name);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final GoodCategory type = mData.get(position);
        holder.tvName.setText(type.Name);
        holder.tvName.setSelected(position == mFocusTab);
        holder.tvName.setOnClickListener(v -> {
            int preF = mFocusTab;
            mFocusTab = position;
            notifyItemChanged(position);
            notifyItemChanged(preF);
            if (mOnShopTypeClickListener != null)
                mOnShopTypeClickListener.onShopTypeClick(position, type);
        });
    }

    private OnShopTypeClickListener mOnShopTypeClickListener;

    public void setOnShopTypeClickListener(OnShopTypeClickListener l) {
        mOnShopTypeClickListener = l;
    }

    public interface OnShopTypeClickListener {
        void onShopTypeClick(int position, GoodCategory type);
    }
}
