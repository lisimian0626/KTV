package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
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
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.dto.SongOperation;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.transmission.CloudSongDownloadHelper;
import com.bestarmedia.libskin.SkinManager;
import com.bestarmedia.libwidget.image.RecyclerImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 08:54.
 */
public class AdtSongLyric extends RecyclerView.Adapter<AdtSongLyric.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SongSimple> mData = new ArrayList<>();

    public AdtSongLyric(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    private String mKeyword;

    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    public void setData(List<SongSimple> data) {
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View viewSong;
        private TextView tvName;
        private TextView tvSort;
        private TextView tvLyric;
        private ImageView rivMore, rivTop, rivScoreTag, rivCloud;

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
        View view = mInflater.inflate(R.layout.list_item_song, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.viewSong = view.findViewById(R.id.rl_song);
        viewHolder.tvName = view.findViewById(R.id.tv_name);
        viewHolder.tvSort = view.findViewById(R.id.tv_sort);
        viewHolder.tvLyric = view.findViewById(R.id.tv_singer);
        viewHolder.tvLyric.setMaxEms(14);
        view.findViewById(R.id.tv_version).setVisibility(View.GONE);
        viewHolder.rivScoreTag = view.findViewById(R.id.riv_score);
        viewHolder.rivCloud = view.findViewById(R.id.riv_cloud);
        viewHolder.rivMore = view.findViewById(com.beidousat.karaoke.R.id.riv_more);
        viewHolder.rivTop = view.findViewById(com.beidousat.karaoke.R.id.riv_top);
        Drawable dItemBackground = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_song_item");
        if (dItemBackground != null) {
            viewHolder.viewSong.setBackground(dItemBackground);
        }
        ColorStateList dSongNameColor = SkinManager.getInstance().getResourceManager().getColorStateList2("selector_list_item_text");
        if (dSongNameColor != null) {
            viewHolder.tvName.setTextColor(dSongNameColor);
        }
        ColorStateList dSubTextColor = SkinManager.getInstance().getResourceManager().getColorStateList2("selector_list_item_sub_text");
        if (dSubTextColor != null) {
            viewHolder.tvSort.setTextColor(dSubTextColor);
            viewHolder.tvSort.setTextColor(dSubTextColor);
            viewHolder.tvLyric.setTextColor(dSubTextColor);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        String sort = ChooseSongs.getInstance().getSongPriorities(song);
        holder.tvSort.setText(sort);
        holder.tvSort.setVisibility(TextUtils.isEmpty(sort) ? View.GONE : View.VISIBLE);
        holder.tvSort.setSelected(!TextUtils.isEmpty(sort));
        holder.tvName.setSelected(!TextUtils.isEmpty(sort));
        String lyric = "";
        if (!TextUtils.isEmpty(song.mainLyric)) {
            if (!TextUtils.isEmpty(mKeyword)) {
                int indexOf = song.mainLyric.indexOf(mKeyword);
                if (indexOf >= 0 && indexOf < song.mainLyric.length()) {
                    lyric = song.mainLyric.substring(indexOf);
                }
            } else {
                lyric = song.mainLyric;
            }
        }
        holder.tvLyric.setText(lyric);
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
