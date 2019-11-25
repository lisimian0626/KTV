package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.data.ChooseSongs;
import com.beidousat.karaoke.ui.fragment.song.FmSingerDetail;
import com.beidousat.karaoke.util.FragmentUtil;
import com.beidousat.karaoke.widget.adapter.AdtSongSinger;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.Start;
import com.bestarmedia.libcommon.transmission.CloudSongDownloadHelper;
import com.bestarmedia.libwidget.anim.MoveAnimation;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.recycler.AutoLineLayoutManager;
import com.bestarmedia.libwidget.recycler.GridRecyclerView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration2;

import java.util.List;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetDownloadPage extends GridRecyclerView {

    private AdtDownload mAdapter;

    public WidgetDownloadPage(Context context) {
        super(context);
        init();
    }

    public WidgetDownloadPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetDownloadPage(Context context, AttributeSet attrs, int defStyleAttr) {
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
        setLayoutManager(new GridLayoutManager(getContext(), 2));
        addItemDecoration(horDivider);
        addItemDecoration(verDivider);
        mAdapter = new AdtDownload();
        setAdapter(mAdapter);
    }


    public void notifyAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setSong(List<SongSimple> songs) {
        mAdapter.setData(songs);
        mAdapter.notifyDataSetChanged();
    }

    public void runLayoutAnimation(boolean fromRight) {
        if (mAdapter != null && getAdapter() != null) {
            Log.d(getClass().getSimpleName(), "runLayoutAnimation fromRight>>>>" + fromRight);
            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),
                    fromRight ? R.anim.grid_layout_animation_from_right : R.anim.grid_layout_animation_from_left);
            setLayoutAnimation(controller);
            getAdapter().notifyDataSetChanged();
            scheduleLayoutAnimation();
        }
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View viewSong, viewSinger;

        private TextView tvNumber, tvName, tvSort, tvSinger, tvVersion;
        private ImageView rivMore, rivTop;

        private RecyclerImageView rivScoreTag;
        private RecyclerView mRvSingers;
        private TextView mTvClose;

        private ViewHolder(View view) {
            super(view);
        }
    }


    public class AdtDownload extends RecyclerView.Adapter<ViewHolder> {

        private List<SongSimple> mData;


        public void setData(List<SongSimple> songs) {
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
            viewHolder.tvNumber = itemView.findViewById(R.id.tv_no);
            viewHolder.tvName = itemView.findViewById(R.id.tv_name);
            viewHolder.tvSort = itemView.findViewById(R.id.tv_sort);
            viewHolder.tvSinger = itemView.findViewById(R.id.tv_singer);
            viewHolder.rivMore = itemView.findViewById(R.id.riv_more);
            viewHolder.rivTop = itemView.findViewById(R.id.riv_top);
            viewHolder.tvVersion = itemView.findViewById(R.id.tv_version);
            viewHolder.rivScoreTag = itemView.findViewById(R.id.riv_score);
            viewHolder.rivTop.setVisibility(INVISIBLE);
            viewHolder.rivMore.setVisibility(INVISIBLE);
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
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final SongSimple song = mData.get(position);
            holder.tvVersion.setText(!TextUtils.isEmpty(song.videoType) ? song.videoType : "");
            holder.tvNumber.setVisibility(GONE);
            String songName = song.songName.trim();
            holder.tvName.setText(songName);
            int size = 24;
            try {
                int len = songName.getBytes("GB2312").length;
                if (len > 42) {
                    size = 18;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            SongSimple songStatus = CloudSongDownloadHelper.getSongStatus(song.id);
            holder.tvVersion.setCompoundDrawablesWithIntrinsicBounds(0, 0, (songStatus != null && songStatus.downloadStatus == 1) || song.isCloud == 0 ? 0 : R.drawable.ic_song_cloud_tag, 0);
            holder.tvSinger.setText(song.getSingerName());
            if (song.isCloud == 1 || song.isCloud == -1) {
                holder.rivTop.setVisibility(INVISIBLE);
                holder.tvSort.setVisibility(VISIBLE);
                String sortText = ChooseSongs.getInstance().getDownloadingText(song.id);
                holder.tvSort.setText(sortText);
            }
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
            boolean singerBtnEnable = false;//歌星按钮能否点击
            AdtSongSinger adapterSinger = new AdtSongSinger(getContext());
            List<Integer> singerIds = song.singerMid;
            List<String> singerNames = song.singer;
            if (singerIds != null && singerIds.size() > 1) {//多歌星
                singerBtnEnable = true;
                adapterSinger.setData(singerIds, singerNames);
            } else if (singerIds != null && singerIds.size() > 0) {//单个歌星
                singerBtnEnable = true;
            }
            holder.tvSinger.setEnabled(singerBtnEnable);
            if (singerBtnEnable) {
                holder.tvSinger.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            } else {
                holder.tvSinger.getPaint().setFlags(0); //下划线
            }
            holder.tvSinger.getPaint().setAntiAlias(true);//抗锯齿
            holder.tvSinger.setEnabled(singerBtnEnable);

            holder.mRvSingers.setAdapter(adapterSinger);

            holder.tvSinger.setOnClickListener(v -> {
                if (song.singerMid != null) {
                    if (song.singerMid.size() > 1) {
                        holder.viewSong.startAnimation(MoveAnimation.create(MoveAnimation.LEFT, false, 300));
                        holder.viewSinger.startAnimation(MoveAnimation.create(MoveAnimation.LEFT, true, 300));
                        holder.viewSong.setVisibility(View.GONE);
                        holder.viewSinger.setVisibility(View.VISIBLE);
                    } else {
                        Start starInfo = new Start();
                        starInfo.id = Integer.valueOf(song.getSingerId());
                        starInfo.musicianName = song.getSingerName();
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
    }
}

