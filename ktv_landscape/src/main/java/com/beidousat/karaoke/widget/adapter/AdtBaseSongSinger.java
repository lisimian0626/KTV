package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bestarmedia.libcommon.model.v4.Start;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2017/6/9.
 */

public class AdtBaseSongSinger extends RecyclerView.Adapter<ViewHolderText> {


    LayoutInflater mInflater;
    List<Start> mData = new ArrayList<>();

    AdtBaseSongSinger(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Integer> singerIds, List<String> singers) {
        mData.clear();
        if (singerIds.size() >= singers.size()) {
            int i = 0;
            for (Integer id : singerIds) {
                Start info = new Start();
                info.id = id;
                info.musicianName = singers.size() > i ? singers.get(i) : "";
                mData.add(info);
                i++;
            }
        } else {
            int i = 0;
            for (String name : singers) {
                Start info = new Start();
                info.id = singerIds.size() > i ? singerIds.get(i) : 0;
                info.musicianName = name;
                mData.add(info);
                i++;
            }
        }
    }

    public void setData(List<Start> data) {
        this.mData = data;
    }

    public Start getItem(int position) {
        if (mData == null) {
            return null;
        } else {
            return mData.get(position);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolderText onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderText holder, int position) {
    }

}
