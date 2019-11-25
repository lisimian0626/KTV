package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.bestarmedia.libcommon.model.vod.play.Hyun;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;

public class AdtHyun extends RecyclerView.Adapter<AdtHyun.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Hyun> mData = new ArrayList<>();

    public AdtHyun(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<Hyun> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerImageView rvImage, rvSelected;

        private ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_hyun, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.rvImage = view.findViewById(R.id.iv_image);
        viewHolder.rvSelected = view.findViewById(R.id.iv_image_selected);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Hyun hyun = mData.get(position);
        holder.rvSelected.setImageResource(VodApplication.getKaraokeController().getCoolScreenStatus().currentId == hyun.id ? R.drawable.play_scenes_s : 0);
        GlideUtils.LoadImage(mContext, !TextUtils.isEmpty(hyun.thumbnailUrl) ? hyun.thumbnailUrl : hyun.thumbnail, R.drawable.play_hyun_no, R.drawable.play_hyun_no, false, holder.rvImage);
        holder.itemView.setOnClickListener(v -> VodApplication.getKaraokeController().setCoolScreen(hyun.id, hyun.fileUrl));
    }

}
