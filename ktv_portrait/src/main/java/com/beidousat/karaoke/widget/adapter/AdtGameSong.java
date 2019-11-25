package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.vod.SongItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 08:54.
 */
public class AdtGameSong extends RecyclerView.Adapter<AdtGameSong.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SongSimple> mData = new ArrayList<>();
    private int mFocusPosition = -1;
    private boolean mCheckable = true;

    public AdtGameSong(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setCheckable(boolean checkable) {
        mCheckable = checkable;
    }

    public void setSelectPosition(int position) {
        mFocusPosition = position;
        notifyDataSetChanged();
    }

    public int getFocusPosition() {
        return mFocusPosition;
    }

    public SongSimple getSelectedPosition() {
        if (mFocusPosition >= 0 && mFocusPosition < mData.size()) {
            return mData.get(mFocusPosition);
        }
        return null;
    }

    public void setData(List<SongSimple> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View contentView;
        public TextView tvName;
        public TextView tvNum;
        public TextView tvSinger;
        public ImageView ivSelected;

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
        View view = mInflater.inflate(R.layout.list_item_game_song, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.contentView = view.findViewById(R.id.contentView);
        viewHolder.tvName = view.findViewById(R.id.tv_name);
        viewHolder.tvNum = view.findViewById(R.id.tv_number);
        viewHolder.tvSinger = view.findViewById(R.id.tv_singer);
        viewHolder.ivSelected = view.findViewById(R.id.iv_selected);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SongSimple song = mData.get(position);
        holder.contentView.setSelected(position == mFocusPosition);
        holder.tvName.setText(song.songName);
        holder.ivSelected.setVisibility(position == mFocusPosition ? View.VISIBLE : View.INVISIBLE);
        String singer = TextUtils.isEmpty(song.getSingerName()) ? "" : song.getSingerName().replace("false", "");
        holder.tvSinger.setText(singer);
        holder.contentView.setEnabled(mCheckable);
        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFocusPosition = position;
                notifyDataSetChanged();
            }
        });
    }
}
