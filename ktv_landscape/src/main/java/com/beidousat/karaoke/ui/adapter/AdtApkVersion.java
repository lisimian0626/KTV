package com.beidousat.karaoke.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.vod.ApkVersionInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by J Wong on 2016/5/3.
 */
public class AdtApkVersion extends RecyclerView.Adapter<AdtApkVersion.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ApkVersionInfo> mData = new ArrayList<ApkVersionInfo>();
    private int mSelectPs = 0;

    public AdtApkVersion(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<ApkVersionInfo> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvVersionName;

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
        View view = mInflater.inflate(R.layout.list_item_apk_version, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvVersionName = view.findViewById(R.id.tv_version);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ApkVersionInfo versionInfo = mData.get(position);
        holder.tvVersionName.setText(versionInfo.versionName);
        holder.tvVersionName.setSelected(mSelectPs == position);
        holder.tvVersionName.setOnClickListener(v -> {
            mSelectPs = position;
            notifyDataSetChanged();
            if (mOnApkVersionClickListener != null)
                mOnApkVersionClickListener.onApkVersionClick(versionInfo);
        });
    }

    private OnApkVersionClickListener mOnApkVersionClickListener;

    public void setOnApkVersionClickListener(OnApkVersionClickListener listener) {
        mOnApkVersionClickListener = listener;
    }

    public interface OnApkVersionClickListener {
        void onApkVersionClick(ApkVersionInfo versionInfo);
    }

}
