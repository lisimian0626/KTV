package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.ui.dialog.play.DlgSelectPk;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.model.vod.Pk;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libcommon.util.TimeUtils;
import com.bestarmedia.libwidget.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by J Wong on 2015/12/17 08:54.
 */
public class AdtPk extends RecyclerView.Adapter<AdtPk.ViewHolder> {


    private Context mContext;
    private LayoutInflater mInflater;
    private List<Pk> mData = new ArrayList<>();

    public AdtPk(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<Pk> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;
        private TextView tvSongName, tvKtvName, tvUserName, tvSlogan, tvPk, tvTime;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_pk, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvSongName = view.findViewById(R.id.tv_song_name);
        viewHolder.tvKtvName = view.findViewById(R.id.tv_ktv);
        viewHolder.tvUserName = view.findViewById(R.id.tv_user);
        viewHolder.tvSlogan = view.findViewById(R.id.tv_Slogan);
        viewHolder.tvPk = view.findViewById(R.id.tv_pk);
        viewHolder.tvTime = view.findViewById(R.id.tv_time);
        viewHolder.ivAvatar = view.findViewById(R.id.iv_avatar);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Pk pk = mData.get(position);
        holder.tvSongName.setText(pk.songName);
        holder.tvKtvName.setText(pk.launchKtvNetName == null ? "" : pk.launchKtvNetName);
//        holder.tvKtvName.setText(pk.launchKtvNetCode);
        holder.tvSlogan.setText(pk.launchSlogan);
        holder.tvUserName.setText(pk.launchUserName);
        if (pk.status == 1) {
            boolean isMyRoom = VodConfigData.getInstance().getKtvNetCode().equals(pk.launchKtvNetCode) && VodConfigData.getInstance().getRoomCode().equals(pk.launchRoomCode);
            holder.tvPk.setText(isMyRoom ? "本房间发出" : "应战");
            holder.tvPk.setEnabled(!isMyRoom);
            holder.tvPk.setVisibility(View.VISIBLE);
            holder.tvPk.setBackgroundResource(isMyRoom ? 0 : R.drawable.selector_gray_button);
        } else if (pk.status == 2) {
            holder.tvPk.setText("应战中");
            holder.tvPk.setEnabled(false);
            holder.tvPk.setVisibility(View.VISIBLE);
            holder.tvPk.setBackgroundResource(0);
        } else {
            holder.tvPk.setText("");
            holder.tvPk.setVisibility(View.GONE);
            holder.tvPk.setEnabled(false);
            holder.tvPk.setBackgroundResource(0);
        }

        holder.tvTime.setText(TimeUtils.convertLongSecString(pk.expireTime));
        GlideUtils.LoadImage(mContext, ServerFileUtil.getImageUrl(pk.launchUserAvatar).getPath(), R.drawable.star_default, R.drawable.star_default, false, holder.ivAvatar);
//        Glide.with(mContext).load(ServerFileUtil.getImageUrl(pk.launchUserAvatar)).placeholder(R.drawable.star_default)
//                .skipMemoryCache(true).into(holder.ivAvatar);
        holder.tvPk.setOnClickListener(v -> {
            DlgSelectPk dlgPk = new DlgSelectPk(pk, VodApplication.getVodApplication().getStackTopActivity());
            dlgPk.setOnPkListener(pk1 -> {
                pk1.status = 2;
                notifyItemChanged(position);
            });
            dlgPk.show();
        });
    }
}
