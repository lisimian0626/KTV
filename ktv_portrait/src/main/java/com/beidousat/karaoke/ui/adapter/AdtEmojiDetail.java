package com.beidousat.karaoke.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.vod.emoji.EmojiDetail;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.GlideUtils;
import com.opensource.svgaplayer.SVGAImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/21 08:59.
 */

public class AdtEmojiDetail extends RecyclerView.Adapter<AdtEmojiDetail.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<EmojiDetail> emojiList = new ArrayList<>();
    private onClickEmojiDetailListener onClickEmojiDetailListener;

    public AdtEmojiDetail(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setOnClickEmojiDetailListener(AdtEmojiDetail.onClickEmojiDetailListener onClickEmojiDetailListener) {
        this.onClickEmojiDetailListener = onClickEmojiDetailListener;
    }

    public void addData(List<EmojiDetail> data) {
        this.emojiList = data;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerImageView ivCover;
        SVGAImageView svgaImageView;

        public ViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.emoji_detail_iv_cover);
            svgaImageView = view.findViewById(R.id.emoji_detail_svga);
        }
    }

    @Override
    public int getItemCount() {
        return emojiList == null ? 0 : emojiList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.emoji_detail_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        EmojiDetail emojiDetail = emojiList.get(i);
        if (emojiDetail != null) {
            if (emojiDetail.drawableId != 0) {
                GlideUtils.LoadImage(mContext, emojiDetail.drawableId, false, viewHolder.ivCover);
            } else {
                GlideUtils.LoadImage(mContext, emojiDetail.thumbnailUrl, R.drawable.emoji_default, R.drawable.emoji_default, false, viewHolder.ivCover);
            }
        }

        viewHolder.itemView.setOnClickListener(v -> {
            if (onClickEmojiDetailListener != null) {
                onClickEmojiDetailListener.onClick(emojiDetail);
            }
        });
    }


    public interface onClickEmojiDetailListener {
        void onClick(EmojiDetail emojiDetail);
    }


}
