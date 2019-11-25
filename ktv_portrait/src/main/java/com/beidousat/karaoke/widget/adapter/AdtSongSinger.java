package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.fragment.song.FmSingerDetail;
import com.beidousat.karaoke.util.FragmentUtil;
import com.bestarmedia.libcommon.model.v4.Start;
import com.bestarmedia.libcommon.util.Logger;

/**
 * Created by J Wong on 2017/6/9.
 */

public class AdtSongSinger extends AdtBaseSongSinger {

    public AdtSongSinger(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolderText onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_song_singer, viewGroup, false);
        ViewHolderText viewHolder = new ViewHolderText(view);
        viewHolder.tvKey = view.findViewById(android.R.id.text1);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolderText holder, int position) {
        final Start starInfo = mData.get(position);
        holder.tvKey.setText(starInfo.musicianName);
        holder.tvKey.setEnabled(starInfo.id != 0);
        holder.tvKey.setOnClickListener(view -> {
            try {
                FmSingerDetail fmSingerDetail = FmSingerDetail.newInstance(starInfo);
                FragmentUtil.addFragment(fmSingerDetail, false, false, true, false);
            } catch (Exception e) {
                Logger.e("WidgetKeyBoard", e.toString());
            }
        });
    }
}