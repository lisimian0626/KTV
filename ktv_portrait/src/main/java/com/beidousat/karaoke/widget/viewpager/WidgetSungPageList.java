package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.helper.BnsBeaconHelper;
import com.beidousat.karaoke.ui.dialog.PopListMore;
import com.beidousat.karaoke.ui.dialog.vod.DlgSelectDownload;
import com.beidousat.karaoke.ui.dialog.vod.SelectCollectDialog;
import com.beidousat.karaoke.ui.fragment.song.FmSingerDetail;
import com.beidousat.karaoke.util.FragmentUtil;
import com.beidousat.karaoke.widget.adapter.AdtSongSinger;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.dto.SongOperation;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.v4.Start;
import com.bestarmedia.libcommon.model.vod.MvInfo;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.model.vod.RoomSongsV4;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libcommon.util.ListUtil;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.liblame.AudioRecordFileUtil;
import com.bestarmedia.libskin.SkinManager;
import com.bestarmedia.libwidget.anim.MoveAnimation;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.recycler.AutoLineLayoutManager;
import com.bestarmedia.libwidget.recycler.GridRecyclerView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration2;
import com.bestarmedia.libwidget.util.GlideUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetSungPageList extends GridRecyclerView implements HttpRequestListener {

    private AdtSung mAdapter;
    private boolean mIsMainVod = true;
    private String mMethod;

    public WidgetSungPageList(Context context) {
        super(context);
        mIsMainVod = DeviceHelper.isMainVod(context);
        init();
    }

    public WidgetSungPageList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetSungPageList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(6).build();
        VerticalDividerItemDecoration2 verDivider = new VerticalDividerItemDecoration2.Builder(getContext())
                .color(Color.TRANSPARENT).size(6).build();
        setLayoutManager(new GridLayoutManager(getContext(), 1));
        addItemDecoration(horDivider);
        addItemDecoration(verDivider);

        mAdapter = new AdtSung();
        setAdapter(mAdapter);
    }

    public void setSong(List<RoomSongItem> songs) {
        mAdapter.setData(songs);
    }

    public void requestSongs(int page) {
        mMethod = RequestMethod.NODE_ROOM_SONG_LIST_SUNG;
        HttpRequestV4 r = initRequest(mMethod);
        r.addParam("current_page", String.valueOf(page));
        r.addParam("session", NodeRoomInfo.getInstance().getRoomSession());
        r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        r.setConvert2Class(RoomSongsV4.class);
        r.get();
    }

    public HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(getContext().getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    public void runLayoutAnimation() {
        if (mAdapter != null && getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (method.equalsIgnoreCase(mMethod)) {
            try {
                if (object != null) {
                    RoomSongsV4 songs = (RoomSongsV4) object;
                    if (songs.song != null && songs.song.data != null && songs.song.data.size() > 0) {
                        mAdapter.setData(songs.song.data);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "加载异常列表出错了：", e);
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {
    }

    @Override
    public void onError(String method, String error) {

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View viewSong, viewSinger;

        private TextView tvName, tvSort, tvSinger, tvVersion;
        private ImageView rivMore, rivTop, rivAvatar;
        private RecyclerImageView rivScoreTag;
        private RecyclerView mRvSingers;
        private TextView mTvClose;

        private ViewHolder(View view) {
            super(view);
        }
    }

    public class AdtSung extends RecyclerView.Adapter<ViewHolder> {

        private List<RoomSongItem> mData;

        public void setData(List<RoomSongItem> songs) {
            this.mData = songs;
        }

        @Override
        public int getItemCount() {
            try {
                return mData == null ? 0 : mData.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_choose_list, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            viewHolder.viewSong = itemView.findViewById(R.id.rl_song);
            viewHolder.viewSinger = itemView.findViewById(R.id.rl_singers);
            viewHolder.mRvSingers = itemView.findViewById(R.id.rv_singer);
            viewHolder.mTvClose = itemView.findViewById(R.id.tv_close);
            itemView.findViewById(R.id.tv_no).setVisibility(View.GONE);
            viewHolder.tvName = itemView.findViewById(R.id.tv_name);
            viewHolder.tvSort = itemView.findViewById(R.id.tv_sort);
            viewHolder.tvSinger = itemView.findViewById(R.id.tv_singer);
            viewHolder.tvVersion = itemView.findViewById(R.id.tv_version);
            viewHolder.rivAvatar = itemView.findViewById(R.id.riv_avatar);
            viewHolder.rivScoreTag = itemView.findViewById(R.id.riv_score);
            viewHolder.rivMore = itemView.findViewById(com.beidousat.karaoke.R.id.riv_more);
            viewHolder.rivTop = itemView.findViewById(com.beidousat.karaoke.R.id.riv_top);
            HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                    .color(Color.TRANSPARENT).size(4).margin(4, 4)
                    .build();
            VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                    .color(Color.TRANSPARENT).size(5).margin(5, 5)
                    .build();
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
            final RoomSongItem song = mData.get(position);
            holder.tvVersion.setText(song.mvId > 0 ? getContext().getString(R.string.self_made_mv) : (!TextUtils.isEmpty(song.songVersion) ? song.songVersion : ""));
            String songName = song.simpName.trim();
            holder.tvName.setText(songName);
            int size = 24;
            try {
                int len = songName.getBytes("GB2312").length;
                if (len > 42) {
                    size = 18;
                }
            } catch (Exception e) {
                Log.w(getClass().getSimpleName(), "onBindViewHolder根据歌名长度计算字体大小出错了", e);
            }
            holder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            final String singer = TextUtils.isEmpty(song.singerName) ? "" : song.singerName.replace("false", "");
            holder.tvSinger.setText(singer);
            holder.tvName.setText(song.simpName);
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, BnsBeaconHelper.haveBeacon() && song.isRedPacket == 1 ? R.drawable.ic_red_envelopes_tag : 0, 0);
            if (TextUtils.isEmpty(song.userAvatar)) {
                holder.rivAvatar.setImageResource(0);
            } else {
                GlideUtils.LoadCornersImage(getContext(), ServerFileUtil.getImageUrl(song.userAvatar),false, holder.rivAvatar,15);
            }
            boolean isShowBtn = false;
            if (song.type == 0) {
                File fileRecord = new File(AudioRecordFileUtil.getRecordFilePath(song.recordFile, false));
                isShowBtn = fileRecord.exists() && fileRecord.length() > 1024;
            }
            boolean isScore = !TextUtils.isEmpty(song.gradeLibFile) && song.gradeLibFile.split("\\|").length > 1;
            boolean isHd = song.isHd == 1;
            holder.rivScoreTag.setVisibility(isScore || isHd ? View.VISIBLE : View.GONE);
            int tagImg = 0;
            if (isScore && isHd) {
                tagImg = R.drawable.ic_song_list_hdscore_tag;
            } else if (isScore) {
                tagImg = R.drawable.ic_song_list_score_tag;
            } else if (isHd) {
                tagImg = R.drawable.ic_song_list_hd_tag;
            }
            holder.rivScoreTag.setImageResource(tagImg);
            boolean singerBtnEnable = false;//歌星按钮能否点击
            AdtSongSinger adapterSinger = new AdtSongSinger(getContext());
            if (!TextUtils.isEmpty(song.singerId) && song.singerId.contains("|")) {//多歌星
                try {
                    String[] ids = song.singerId.split("\\|");
                    String[] names = song.singerName.split("\\|");
                    List<Integer> singerIds = new ArrayList<>();
                    for (String id : ids) {
                        singerIds.add(Integer.valueOf(id));
                    }
                    if (ids.length > 0 && !ListUtil.isEmptyArray(ids)) {//存在歌星id
                        singerBtnEnable = true;
                        adapterSinger.setData(singerIds, Arrays.asList(names));
                    }
                } catch (Exception e) {
                    Log.w(getClass().getSimpleName(), "歌星转换出错了", e);
                }
            } else if (!TextUtils.isEmpty(song.singerId) && !song.singerId.contains("|")) {//单个歌星
                singerBtnEnable = true;
            }

            holder.tvSinger.setEnabled(singerBtnEnable);
            holder.mRvSingers.setAdapter(adapterSinger);

            holder.rivTop.setOnClickListener(v -> onToTop(song, v));

            final boolean isShowBtnSave = isShowBtn;

            holder.rivMore.setOnClickListener(v -> {
                final List<String> items = new ArrayList<>();
                items.add(getContext().getString(song.type == 0 ? R.string.replay : R.string.replay2));
                if (isShowBtnSave && mIsMainVod) {
                    items.add(getContext().getString(R.string.save));
                }
                if (song.type == 0) {
                    items.add(getContext().getString(R.string.collect));
                }
                PopListMore popupList = new PopListMore(VodApplication.getVodApplication().getStackTopActivity());
                popupList.setData(items);
                popupList.setBackground(R.drawable.list_more_menu_bg);
                popupList.setOnDismissListener(() -> v.setSelected(false));
                popupList.setOnMenuClickListener(position1 -> {
                    String text = items.get(position1);
                    if (getContext().getString(R.string.replay).equals(text) || getContext().getString(R.string.replay2).equals(text)) {//重唱
                        onRestore(song, v);
                    } else if (getContext().getString(R.string.save).equals(text)) {//保存录音
                        DlgSelectDownload dlgShare = new DlgSelectDownload(getContext(), song);
                        dlgShare.show();
                    } else if (getContext().getString(R.string.collect).equals(text)) {//收藏
                        SelectCollectDialog dialog = new SelectCollectDialog(VodApplication.getVodApplication().getStackTopActivity(), song.toSongSimple());
                        dialog.show();
                    }
                });
                v.setSelected(true);
                popupList.showAnchorLeft(v);

            });
            holder.tvSinger.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(song.singerId)) {
                    if (song.singerId.contains("|")) {//多歌星
                        holder.viewSong.startAnimation(MoveAnimation.create(MoveAnimation.LEFT, false, 300));
                        holder.viewSinger.startAnimation(MoveAnimation.create(MoveAnimation.LEFT, true, 300));
                        holder.viewSong.setVisibility(View.GONE);
                        holder.viewSinger.setVisibility(View.VISIBLE);
                    } else {
                        Start starInfo = new Start();
                        starInfo.id = Integer.valueOf(song.singerId);
                        starInfo.musicianName = song.singerName;
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
            });
            holder.viewSong.setOnClickListener(v -> onRestore(song, v));
        }
    }

    private void onToTop(RoomSongItem info, View v) {
        info.uuid = "";
        if (info.type == 1) {
            VodApplication.getKaraokeController().playMovie(info.toMovieDetail());
        } else if (info.type == 2) {
            VodApplication.getKaraokeController().playLive(info.toLiveDetail());
        } else {
            VodApplication.getKaraokeController().selectSong(new SongOperation(1, info.toSongSimple(), v, info.uuid,
                    new UserBase(info.userId, info.userName, info.userAvatar), new MvInfo(info.mvId, info.galleries, info.userId)), true);
        }
    }

    private void onRestore(RoomSongItem info, View v) {
        info.uuid = "";
        if (info.type == 1) {
            VodApplication.getKaraokeController().playMovie(info.toMovieDetail());
        } else if (info.type == 2) {
            VodApplication.getKaraokeController().playLive(info.toLiveDetail());
        } else {
            VodApplication.getKaraokeController().selectSong(new SongOperation(0, info.toSongSimple(), v, info.uuid,
                    new UserBase(info.userId, info.userName, info.userAvatar), new MvInfo(info.mvId, info.galleries, info.userId)), true);
        }
    }
}
