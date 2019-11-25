package com.bestarmedia.libwidget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {
    protected List<T> mData;
    private View.OnClickListener mOnItemClickListener;

    public void setOnItemClickListener(View.OnClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public List<T> getData() {
        return mData;
    }

    public T getItem(int position) {
        if (mData == null) {
            return null;
        } else {
            return mData.get(position);
        }
    }

    public void addData(T data) {
        if (mData == null)
            mData = new ArrayList<T>();
        mData.add(data);
    }

    public void addDataTop(T data) {
        if (mData == null)
            mData = new ArrayList<T>();
        mData.add(0, data);

    }


    public void addDataToTail(List<T> data) {
        if (mData == null || data == null) {
            return;
        } else {
            mData.addAll(data);
        }
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    protected abstract int getItemViewLayoutId();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(getItemViewLayoutId(), parent, false);
        if (mOnItemClickListener != null) {
            v.setOnClickListener(mOnItemClickListener);
        }
        return new ViewHolder(v, context);
    }

    protected <T extends View> T $(View itemView, int id) {
        return (T) itemView.findViewById(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mItemView;
        public Context mContext;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mItemView = itemView;
            mContext = context;
        }

        public View getItemView() {
            return mItemView;
        }
    }

}
