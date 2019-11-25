package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.data.ChooseSongs;
import com.beidousat.karaoke.helper.BnsBeaconHelper;
import com.beidousat.karaoke.ui.dialog.PopListMore;
import com.beidousat.karaoke.ui.fragment.song.FmSingerDetail;
import com.beidousat.karaoke.util.FragmentUtil;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.dto.SongOperation;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.Start;
import com.bestarmedia.libcommon.transmission.CloudSongDownloadHelper;
import com.bestarmedia.libskin.SkinManager;
import com.bestarmedia.libwidget.anim.MoveAnimation;
import com.bestarmedia.libwidget.recycler.AutoLineLayoutManager;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 08:54.
 */
public class AdtSong extends RecyclerView.Adapter<AdtSong.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SongSimple> mData = new ArrayList<>();
    private boolean mIsShowSingerButton;

    public AdtSong(Context context, boolean isShowSingerButton) {
        mContext = context;
        mIsShowSingerButton = isShowSingerButton;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<SongSimple> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View viewSong, viewSinger;
        private TextView tvName;
        private TextView tvSort;
        private TextView tvSinger;
        private ImageView rivMore, rivTop, rivScoreTag, rivCloud, rivPay;
        private TextView tvVersion;
        private RecyclerView mRvSingers;
        private TextView mTvClose;

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
        View view = mInflater.inflate(R.layout.list_item_song, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.viewSong = view.findViewById(R.id.rl_song);
        viewHolder.viewSinger = view.findViewById(R.id.rl_singers);
        viewHolder.mRvSingers = view.findViewById(R.id.rv_singer);
        viewHolder.mTvClose = view.findViewById(R.id.tv_close);
        viewHolder.tvName = view.findViewById(R.id.tv_name);
        viewHolder.tvSort = view.findViewById(R.id.tv_sort);
        viewHolder.tvSinger = view.findViewById(R.id.tv_singer);
        viewHolder.tvVersion = view.findViewById(R.id.tv_version);
        viewHolder.rivMore = view.findViewById(R.id.riv_more);
        viewHolder.rivTop = view.findViewById(R.id.riv_top);
        viewHolder.rivScoreTag = view.findViewById(R.id.riv_score);
        viewHolder.rivPay = view.findViewById(R.id.riv_pay);
        viewHolder.rivCloud = view.findViewById(R.id.riv_cloud);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(mContext)
                .color(Color.TRANSPARENT).size(4).margin(4, 4).build();
        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(mContext)
                .color(Color.TRANSPARENT).size(5).margin(5, 5).build();
        AutoLineLayoutManager layoutManager = new AutoLineLayoutManager();
        layoutManager.setAutoMeasureEnabled(true);
        viewHolder.mRvSingers.setLayoutManager(layoutManager);
        viewHolder.mRvSingers.addItemDecoration(verDivider);
        viewHolder.mRvSingers.addItemDecoration(horDivider);
        Drawable dItemBackground = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_song_item");
        if (dItemBackground != null) {
            viewHolder.viewSong.setBackground(dItemBackground);
            viewHolder.viewSinger.setBackground(dItemBackground);
        }
        ColorStateList dSongNameColor = SkinManager.getInstance().getResourceManager().getColorStateList2("selector_list_item_text");
        if (dSongNameColor != null) {
            viewHolder.tvName.setTextColor(dSongNameColor);
        }
        ColorStateList dSubTextColor = SkinManager.getInstance().getResourceManager().getColorStateList2("selector_list_item_sub_text");
        if (dSubTextColor != null) {
            viewHolder.tvSort.setTextColor(dSubTextColor);
            viewHolder.tvSinger.setTextColor(dSubTextColor);
            viewHolder.tvVersion.setTextColor(dSubTextColor);
            viewHolder.tvSort.setTextColor(dSubTextColor);
            viewHolder.mTvClose.setTextColor(dSubTextColor);
        }
        Drawable dTop = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_to_top");
        if (dTop != null) {
            viewHolder.rivTop.setImageDrawable(dTop);
        }
        Drawable dMore = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_song_list_more");
        if (dMore != null) {
            viewHolder.rivMore.setImageDrawable(dMore);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final SongSimple song = mData.get(position);
        int size = 24;
        String songName = song.songName;
        if (!TextUtils.isEmpty(song.songName)) {
            songName = song.songName.trim();
            try {
                int len = songName.getBytes("GB2312").length;
                if (len > 22) {
                    size = 18;
                }
            } catch (Exception e) {
                Log.w(getClass().getSimpleName(), "onBindViewHolder根据歌名长度计算字体大小出错了", e);
            }
        }
        holder.tvName.setText(TextUtils.isEmpty(songName) ? "未知歌名" : songName);
        holder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, BnsBeaconHelper.haveBeacon()
                && song.isRedPacket == 1 ? R.drawable.ic_red_envelopes_tag : 0, 0);
        final SongSimple songStatus;
        final boolean isShowPreview = song.isCloud == 0 || (((songStatus = CloudSongDownloadHelper.getSongStatus(song.id)) != null) && songStatus.downloadStatus == 1);
        holder.rivCloud.setVisibility(isShowPreview ? View.INVISIBLE : View.VISIBLE);
        holder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        holder.tvSinger.setText(song.getSingerName());
        holder.viewSinger.setVisibility(song.isShowSingers ? View.VISIBLE : View.GONE);
        holder.viewSong.setVisibility(song.isShowSingers ? View.GONE : View.VISIBLE);
        holder.rivScoreTag.setVisibility(song.isScore == 1 || song.isHd == 1 ? View.VISIBLE : View.GONE);
        int tagImg = 0;
        if (song.isScore == 1 && song.isHd == 1) {
            tagImg = R.drawable.ic_song_list_hdscore_tag;
        } else if (song.isScore == 1) {
            tagImg = R.drawable.ic_song_list_score_tag;
        } else if (song.isHd == 1) {
            tagImg = R.drawable.ic_song_list_hd_tag;
        }
        holder.rivScoreTag.setImageResource(tagImg);
        holder.rivPay.setVisibility(song.isPay == 1 ? View.VISIBLE : View.INVISIBLE);
        String sort = ChooseSongs.getInstance().getSongPriorities(song);
        holder.tvSort.setText(sort);
        holder.tvSort.setVisibility(TextUtils.isEmpty(sort) ? View.GONE : View.VISIBLE);
        holder.tvSort.setSelected(!TextUtils.isEmpty(sort));
        holder.tvVersion.setText(!TextUtils.isEmpty(song.videoType) ? song.videoType : "");
        holder.tvName.setSelected(!TextUtils.isEmpty(sort));
        boolean singerBtnEnable = false;//歌星按钮能否点击
        List<Integer> singerIds = song.singerMid;
        List<String> singerNames = song.singer;
        AdtSongSinger adapterSinger = new AdtSongSinger(mContext);
        if (singerIds != null && singerIds.size() > 1) {//多歌星
            singerBtnEnable = true;
            adapterSinger.setData(singerIds, singerNames);
        } else if (singerIds != null && singerIds.size() > 0) {//单个歌星
            singerBtnEnable = true;
        }
        if (mIsShowSingerButton && singerBtnEnable) {
            holder.tvSinger.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        } else {
            holder.tvSinger.getPaint().setFlags(0); //下划线
        }
        holder.tvSinger.getPaint().setAntiAlias(true);//抗锯齿

        holder.tvSinger.setEnabled(mIsShowSingerButton && singerBtnEnable);
        holder.mRvSingers.setAdapter(adapterSinger);

        holder.viewSong.setOnClickListener(v -> onSongClick(song, v));
        holder.rivTop.setOnClickListener(v -> onToTop(song, v));
        holder.rivMore.setOnClickListener(v -> {
            List<String> items = new ArrayList<>();
            items.add(mContext.getString(R.string.collect));
            if (isShowPreview) {
                items.add(mContext.getString(R.string.preview));
            }
            PopListMore popupList = new PopListMore(VodApplication.getVodApplication().getStackTopActivity());
            popupList.setData(items);
            popupList.setBackground(R.drawable.list_more_menu_bg);
            popupList.setOnDismissListener(() -> v.setSelected(false));
            popupList.setOnMenuClickListener(position1 -> {
                switch (position1) {
                    case 0:
                        onCollect(song, v);
                        break;
                    case 1:
                        onPreview(song, v);
                        break;
                }
            });
            v.setSelected(true);
            popupList.showAnchorLeft(v);
        });
        holder.tvSinger.setOnClickListener(v -> {
            if (song.singerMid != null && !song.singerMid.isEmpty()) {
                if (song.singerMid.size() > 1) {
                    holder.viewSong.startAnimation(MoveAnimation.create(MoveAnimation.LEFT, false, 300));
                    holder.viewSinger.startAnimation(MoveAnimation.create(MoveAnimation.LEFT, true, 300));
                    holder.viewSong.setVisibility(View.GONE);
                    holder.viewSinger.setVisibility(View.VISIBLE);
                } else {
                    Start starInfo = new Start();
                    starInfo.id = song.singerMid.get(0);
                    starInfo.musicianName = song.singer != null && !song.singer.isEmpty() ? song.singer.get(0) : "";
                    FmSingerDetail fmSingerDetail = FmSingerDetail.newInstance(starInfo);
                    FragmentUtil.addFragment(fmSingerDetail, false, false, true, false);
                }
            }
        });
        holder.mTvClose.setOnClickListener(v -> {
            holder.viewSong.startAnimation(MoveAnimation.create(MoveAnimation.RIGHT, true, 300));
            holder.viewSinger.startAnimation(MoveAnimation.create(MoveAnimation.RIGHT, false, 300));
            holder.viewSong.setVisibility(View.VISIBLE);
            holder.viewSinger.setVisibility(View.GONE);
            song.isShowSingers = false;
        });
    }

    private void onCollect(SongSimple info, View view) {
        EventBusUtil.postSticky(EventBusId.SongOperationId.SONG_COLLECT, new SongOperation(0, info, view, "", null, null));
    }

    private void onPreview(SongSimple info, View view) {
        EventBusUtil.postSticky(EventBusId.SongOperationId.SONG_PREVIEW, new SongOperation(0, info, view, "", null, null));
    }


    private void onToTop(SongSimple info, View view) {
        VodApplication.getKaraokeController().selectSong(new SongOperation(1, info, view, "", null, null), true);
    }

    private void onSongClick(SongSimple info, View view) {
        VodApplication.getKaraokeController().selectSong(new SongOperation(0, info, view, "", null, null), true);
    }

}
