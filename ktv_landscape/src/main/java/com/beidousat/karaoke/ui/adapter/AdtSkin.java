package com.beidousat.karaoke.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.model.vod.play.Skin;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/21 08:59.
 */

public class AdtSkin extends RecyclerView.Adapter<AdtSkin.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Skin> mData = new ArrayList<>();

    public AdtSkin(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Skin> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View contentView;
        ImageView ivCover, ivSelected;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.list_item_skin, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.contentView = view.findViewById(R.id.contentView);
        viewHolder.ivCover = view.findViewById(R.id.iv_image);
        viewHolder.ivSelected = view.findViewById(R.id.iv_selected);
        viewHolder.textView = view.findViewById(R.id.tv_title);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final Skin skinInfo = mData.get(i);
        if (TextUtils.isEmpty(skinInfo.id)) {//默认皮肤
            Glide.with(mContext).load(R.drawable.skin_default).skipMemoryCache(false).into(viewHolder.ivCover);
        } else {
            Glide.with(mContext).load(TextUtils.isEmpty(skinInfo.acrossImgUrl) ? skinInfo.acrossImg : skinInfo.acrossImgUrl).override(360, 203).centerCrop().skipMemoryCache(false).into(viewHolder.ivCover);
        }
        viewHolder.ivSelected.setImageResource(skinInfo.id.equals(PrefData.getSkinID(mContext)) ? R.drawable.play_scenes_s : 0);
        viewHolder.textView.setText(TextUtils.isEmpty(skinInfo.name) ? "" : skinInfo.name);
        viewHolder.contentView.setOnClickListener(v -> {
            if (!skinInfo.id.equals(PrefData.getSkinID(mContext))) {
                notifyDataSetChanged();
                if (mOnItemSelectListener != null)
                    mOnItemSelectListener.onItemSelected(skinInfo);
            }
        });

    }

    private OnItemSelectListener mOnItemSelectListener;

    public void setOnItemSelectListener(OnItemSelectListener l) {
        mOnItemSelectListener = l;
    }

    public interface OnItemSelectListener {
        void onItemSelected(Skin skinInfo);
    }
}
