package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
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
import com.beidousat.karaoke.data.ChooseSongs;
import com.beidousat.karaoke.helper.VodCloudHelper;
import com.beidousat.karaoke.ui.dialog.PopListMore;
import com.beidousat.karaoke.ui.dialog.vod.SelectCollectDialog;
import com.bestarmedia.libcommon.model.vod.MvInfo;
import com.bestarmedia.libwidget.recycler.AutoLineLayoutManager;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetMvPage extends RecyclerView {

    private AdtMv mAdapter;


    public WidgetMvPage(Context context) {
        super(context);
        init();
    }

    public WidgetMvPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetMvPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void notifyAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
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

        mAdapter = new AdtMv();
        setAdapter(mAdapter);
    }

    public void setMv(List<MvInfo> songs) {
        mAdapter.setData(songs);
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {

        private View viewSong;
        private TextView tvName;
        private TextView tvSort;
        private TextView tvSinger;
        private ImageView rivMore, rivTop;
        private TextView tvVersion;
        private RecyclerView mRvSingers;

        private ViewHolder(View view) {
            super(view);
        }
    }

    class AdtMv extends RecyclerView.Adapter<ViewHolder> {

        private LayoutInflater mInflater;
        private List<MvInfo> mData = new ArrayList<>();

        private AdtMv() {
            mInflater = LayoutInflater.from(getContext());
        }

        public void setData(List<MvInfo> data) {
            this.mData = data;
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
            view.findViewById(R.id.rl_singers).setVisibility(GONE);
            viewHolder.mRvSingers = view.findViewById(R.id.rv_singer);
            view.findViewById(R.id.tv_close).setVisibility(GONE);
            viewHolder.tvName = view.findViewById(com.beidousat.karaoke.R.id.tv_name);
            viewHolder.tvSort = view.findViewById(com.beidousat.karaoke.R.id.tv_sort);
            viewHolder.tvSinger = view.findViewById(com.beidousat.karaoke.R.id.tv_singer);
            viewHolder.tvVersion = view.findViewById(com.beidousat.karaoke.R.id.tv_version);
            viewHolder.rivMore = view.findViewById(com.beidousat.karaoke.R.id.riv_more);
            viewHolder.rivTop = view.findViewById(com.beidousat.karaoke.R.id.riv_top);
            view.findViewById(R.id.riv_score).setVisibility(GONE);
            view.findViewById(R.id.riv_cloud).setVisibility(GONE);
            HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                    .color(Color.TRANSPARENT).size(4).margin(4, 4).build();
            VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                    .color(Color.TRANSPARENT).size(5).margin(5, 5).build();
            AutoLineLayoutManager layoutManager = new AutoLineLayoutManager();
            layoutManager.setAutoMeasureEnabled(true);
            viewHolder.mRvSingers.setLayoutManager(layoutManager);
            viewHolder.mRvSingers.addItemDecoration(verDivider);
            viewHolder.mRvSingers.addItemDecoration(horDivider);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final MvInfo song = mData.get(position);
            String songName = song.songName == null ? "null" : song.songName.trim();
            holder.tvName.setText(songName);
            int size = 24;
            try {
                int len = songName.getBytes("GB2312").length;
                if (len > 24) {
                    size = 18;
                }
            } catch (Exception e) {
                Log.w("歌曲名称长度计算异常", e);
            }
            holder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
            String singer = TextUtils.isEmpty(song.songName) ? "" : song.songName.replace("false", "");
            holder.tvSinger.setText(singer);
            holder.tvVersion.setText(!TextUtils.isEmpty(song.videoType) ? song.videoType : "");
            String sort = ChooseSongs.getInstance().getMvPriorities(song);
            holder.tvSort.setText(sort);
            holder.tvSort.setSelected(!TextUtils.isEmpty(sort));
            holder.tvName.setSelected(!TextUtils.isEmpty(sort));
            holder.viewSong.setOnClickListener(v ->
                    VodCloudHelper.selectMV(getContext(), null, song.mvId, false));
            holder.rivTop.setOnClickListener(v ->
                    VodCloudHelper.selectMV(getContext(), null, song.mvId, true));
            holder.rivMore.setOnClickListener(v -> {
                List<String> items = new ArrayList<>();
                items.add(getContext().getString(R.string.collect));
                PopListMore popupList = new PopListMore(VodApplication.getVodApplication().getStackTopActivity());
                popupList.setData(items);
                popupList.setBackground(R.drawable.list_more_menu_bg);
                popupList.setOnDismissListener(() -> v.setSelected(false));
                popupList.setOnMenuClickListener(ps -> {
                    if (ps == 0) {
                        onCollect(song);
                    }
                });
                v.setSelected(true);
                popupList.showAnchorLeft(v);
            });
        }
    }

    private void onCollect(MvInfo info) {
        SelectCollectDialog dialog = new SelectCollectDialog(VodApplication.getVodApplication().getStackTopActivity(), info);
        dialog.show();
    }
}

