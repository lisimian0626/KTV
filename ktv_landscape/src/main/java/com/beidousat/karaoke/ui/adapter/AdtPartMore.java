package com.beidousat.karaoke.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.interf.OnItemClickListener;
import com.bestarmedia.libcommon.model.v4.Part;

import java.util.List;


public class AdtPartMore extends RecyclerView.Adapter<AdtPartMore.ViewHolder> {

    private List<Part> parts;
    private LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;

    public AdtPartMore(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Part> parts) {
        this.parts = parts;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return parts == null ? 0 : parts.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.spiner_item_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tv_name = view.findViewById(R.id.spinner_item_name);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Part part = parts.get(i);
        viewHolder.tv_name.setText(part.name);
        viewHolder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.OnItemClickListener(view, part);
            }
        });
    }
}
