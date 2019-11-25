package com.beidousat.karaoke.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;

public class WidgetScoreResult extends RelativeLayout {

    private View mRootView;
    private TextView mTvResultScore, mTvResultSong, mTvResultPercent;

    public WidgetScoreResult(Context context) {
        super(context);
        initView();
    }

    public WidgetScoreResult(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public WidgetScoreResult(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_score_result, this);
        mTvResultScore = findViewById(R.id.tv_result_score);
        mTvResultSong = findViewById(R.id.tv_result_song_name);
        mTvResultPercent = findViewById(R.id.tv_beat_percent);
    }


    public void setScoreResult(String songName, int score, int beatPercent) {
        mTvResultSong.setText(songName);
        mTvResultScore.setText(String.valueOf(score));
        mTvResultPercent.setText(beatPercent+"%");
    }
}
