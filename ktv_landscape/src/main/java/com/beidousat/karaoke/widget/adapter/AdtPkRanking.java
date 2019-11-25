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
import com.bestarmedia.libcommon.model.vod.PkRanking;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 08:54.
 */
public class AdtPkRanking extends RecyclerView.Adapter<AdtPkRanking.ViewHolder> {


    private Context mContext;
    private LayoutInflater mInflater;
    private List<PkRanking> mData = new ArrayList<>();
    private int mPageNum;
    private int mPageSize;

    public AdtPkRanking(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<PkRanking> data, int pageNum, int pageSize) {
        this.mPageNum = pageNum;
        this.mData = data;
        this.mPageSize = pageSize;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;
        private TextView tvNum, tvUserName, tvRecord, tvWonCount;

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
        View view = mInflater.inflate(R.layout.list_item_pk_ranking, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvNum = view.findViewById(R.id.tv_number);
        viewHolder.tvUserName = view.findViewById(R.id.tv_name);
        viewHolder.tvRecord = view.findViewById(R.id.tv_record);
        viewHolder.tvWonCount = view.findViewById(R.id.tv_won_count);
        viewHolder.ivAvatar = view.findViewById(R.id.iv_avatar);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PkRanking pk = mData.get(position);
        Logger.d("AdtPkRanking", "onBindViewHolder mPageNum:" + mPageNum);
        holder.tvNum.setText(String.valueOf((mPageNum - 1) * mPageSize + position + 1));
        holder.tvUserName.setText(pk.name + "");
        holder.tvRecord.setText(mContext.getString(R.string.pk_record_x, String.valueOf(pk.wins), pk.draws + "", String.valueOf(pk.losses), String.valueOf(pk.give_up)));
        holder.tvWonCount.setText(mContext.getString(R.string.total_score, String.valueOf(pk.score)));
//        int resSex = 0;
//        try {
//            int sex = Integer.valueOf(pk.Sex);
//            if (sex == 1)
//                resSex = R.drawable.ic_male;
//            else if (sex == 2)
//                resSex = R.drawable.ic_female;
//        } catch (Exception e) {
//        }
//
//        holder.tvUserName.setCompoundDrawablesWithIntrinsicBounds(0, 0, resSex, 0);
        GlideUtils.LoadImage(mContext, ServerFileUtil.getImageUrl(pk.avatar).getPath(), R.drawable.star_default, R.drawable.star_default, false, holder.ivAvatar);
//        Glide.with(mContext).load(ServerFileUtil.getImageUrl(pk.avatar))
//                .placeholder(R.drawable.star_default).skipMemoryCache(true).into(holder.ivAvatar);
    }
}
