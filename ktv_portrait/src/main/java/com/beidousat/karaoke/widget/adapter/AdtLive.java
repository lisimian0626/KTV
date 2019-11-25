package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.fragment.play.FmLiveDetail;
import com.beidousat.karaoke.util.FragmentUtil;
import com.bestarmedia.libcommon.model.vod.Live;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/10/15 14:56.
 * 专辑
 */
public class AdtLive extends RecyclerView.Adapter<AdtLive.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Live> mData = new ArrayList<>();


    public AdtLive(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<Live> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View item;
        private TextView tvName, tvStatus;
        private RecyclerImageView ivCover;

        public ViewHolder(View view) {
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
        View view = mInflater.inflate(R.layout.list_item_live, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.item = view.findViewById(R.id.contentView);
        viewHolder.ivCover = view.findViewById(R.id.iv_cover);
        viewHolder.tvName = view.findViewById(R.id.tv_name);
        viewHolder.tvStatus = view.findViewById(R.id.tv_status);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Live live = mData.get(position);
        holder.tvName.setText(live.live_name);
        holder.tvStatus.setText(live.live_status == 1 ? mContext.getString(R.string.living) : live.start_time);
        holder.tvStatus.setTextColor(mContext.getResources().getColor(live.live_status == 1 ? R.color.tab_text_selected : R.color.tab_text));
        if (TextUtils.isEmpty(live.image_thumbnail)) {
            holder.ivCover.setImageResource(R.drawable.live_default);
        } else {
            Glide.with(mContext).load(ServerFileUtil.getImageUrl(live.image_thumbnail)).override(80, 80).centerCrop()
                    .placeholder(R.drawable.live_default).skipMemoryCache(false).into(holder.ivCover);
        }
        holder.item.setOnClickListener(v ->
                FragmentUtil.addFragment(FmLiveDetail.newInstance(live), false, false, false, false));
    }

}
