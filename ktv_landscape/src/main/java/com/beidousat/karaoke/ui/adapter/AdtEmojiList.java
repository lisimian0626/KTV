package com.beidousat.karaoke.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.vod.emoji.Emoji;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/21 08:59.
 */

public class AdtEmojiList extends RecyclerView.Adapter<AdtEmojiList.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Emoji> emojiList = new ArrayList<>();
    private onClickEmojiListener onClickEmojiListener;
    private Emoji select_emoji;

    public Emoji getSelect_emoji() {
        return select_emoji;
    }

    public void setSelect_emoji(Emoji select_emoji) {
        this.select_emoji = select_emoji;
    }

    public AdtEmojiList(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public AdtEmojiList.onClickEmojiListener getOnClickEmojiListener() {
        return onClickEmojiListener;
    }

    public void setOnClickEmojiListener(AdtEmojiList.onClickEmojiListener onClickEmojiListener) {
        this.onClickEmojiListener = onClickEmojiListener;
    }

    public void addData(List<Emoji> data) {
        this.emojiList = data;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerImageView ivCover;

        public ViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.emoji_list_iv_cover);
        }
    }

    @Override
    public int getItemCount() {
        return emojiList == null ? 0 : emojiList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.emoji_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Emoji emoji = emojiList.get(i);
        if (emoji != null) {
            Glide.with(mContext).load(emoji.coverUrl).skipMemoryCache(false).into(viewHolder.ivCover);
            if (select_emoji != null && select_emoji.id == emoji.id) {
                viewHolder.ivCover.setSelected(true);
            } else {
                viewHolder.ivCover.setSelected(false);
            }
        }
        viewHolder.itemView.setOnClickListener(v -> {
            if (onClickEmojiListener != null) {
                onClickEmojiListener.onClick(emoji);
            }
            select_emoji = emoji;
            notifyDataSetChanged();
        });
    }


    public interface onClickEmojiListener {
        void onClick(Emoji emoji);
    }
}
