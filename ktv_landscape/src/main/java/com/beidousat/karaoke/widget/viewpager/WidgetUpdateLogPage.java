package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.vod.SongUpdateLog;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetUpdateLogPage extends RecyclerView {

    private AdtLog mAdapter;


    public WidgetUpdateLogPage(Context context) {
        super(context);
        init();
    }

    public WidgetUpdateLogPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetUpdateLogPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        setLayoutManager(layoutManager);

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(2).build();

        addItemDecoration(horDivider);

        mAdapter = new AdtLog();
        setAdapter(mAdapter);
    }


    public void setLogs(List<SongUpdateLog.SongLog> logs) {
        mAdapter.setData(logs);
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvUpdateLog;
        private ViewHolder(View view) {
            super(view);
        }
    }


    public class AdtLog extends RecyclerView.Adapter<ViewHolder> {

        private List<SongUpdateLog.SongLog> mData;

        public void setData(List<SongUpdateLog.SongLog> songs) {
            this.mData = songs;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_update_log, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            viewHolder.tvDate =  itemView.findViewById(R.id.tv_date);
            viewHolder.tvUpdateLog = itemView.findViewById(R.id.tv_content);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SongUpdateLog.SongLog log = mData.get(position);
            holder.tvDate.setText(getContext().getString(R.string.update_week_of_year, log.weekOfYear + "", log.beginDate, log.endDate));
            holder.tvUpdateLog.setText(getContext().getString(R.string.song_update_log, log.newSong + "", log.update + "", log.down + ""));
        }
    }
}

