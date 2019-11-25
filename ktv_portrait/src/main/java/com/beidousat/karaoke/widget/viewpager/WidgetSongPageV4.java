package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.view.animation.AnimationUtils;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.OnPreviewSongListener;
import com.beidousat.karaoke.interf.OnSongSelectListener;
import com.beidousat.karaoke.widget.adapter.AdtSong;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.SongSimplePage;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.recycler.GridRecyclerView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration2;
import com.bestarmedia.libwidget.util.DensityUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2018/7/6 15:29.
 */
public class WidgetSongPageV4 extends GridRecyclerView implements HttpRequestListener, OnPreviewSongListener, OnSongSelectListener {

    private String mMethod;

    private AdtSong mAdapter;

    private OnPreviewSongListener mOnPreviewSongListener;

    private boolean mIsShowSingerButton;

    private int dividerV = DensityUtil.dip2px(getContext(), 6);

    private int dividerH = DensityUtil.dip2px(getContext(), 6);

    public WidgetSongPageV4(Context context) {
        super(context);
        init();
    }

    public WidgetSongPageV4(Context context, int marginVertical, int horizontalMargin, boolean showSingerButton) {
        super(context);
        dividerV = marginVertical;
        dividerH = horizontalMargin;
        mIsShowSingerButton = showSingerButton;
        init();
    }

    public void notifyAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void runLayoutAnimation(boolean fromRight) {
        if (mAdapter != null) {
            if (OkConfig.boxManufacturer() != 2) {
                setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(),
                        fromRight ? R.anim.grid_layout_animation_from_right : R.anim.grid_layout_animation_from_left));
            }
            mAdapter.notifyDataSetChanged();
            if (OkConfig.boxManufacturer() != 2) {
                scheduleLayoutAnimation();
            }
        }
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(dividerH).margin(0, 0).build();

        VerticalDividerItemDecoration2 verDivider = new VerticalDividerItemDecoration2.Builder(getContext())
                .color(Color.TRANSPARENT).size(dividerV).margin(dividerV).build();

        setLayoutManager(new GridLayoutManager(getContext(), 2));

        addItemDecoration(horDivider);
        addItemDecoration(verDivider);

        mAdapter = new AdtSong(getContext(), mIsShowSingerButton);
        setVerticalScrollBarEnabled(false);
        setAdapter(mAdapter);
    }

    @Override
    public void onSongSelectListener(SongSimple song) {
        if (mOnSongSelectListener != null)
            mOnSongSelectListener.onSongSelectListener(song);
    }

    private OnSongSelectListener mOnSongSelectListener;

    public void setOnSongSelectListener(OnSongSelectListener listener) {
        mOnSongSelectListener = listener;
    }

    public void setOnPreviewSongListener(OnPreviewSongListener listener) {
        this.mOnPreviewSongListener = listener;
    }

    @Override
    public void onPreviewSong(SongSimple song, int ps) {
        if (mOnPreviewSongListener != null) {
            mOnPreviewSongListener.onPreviewSong(song, ps);
        }
    }

    public void loadSong(int page, Map<String, String> map) {
        requestSongs(page, map);
    }

    public void loadSong(String method, int page, Map<String, String> map) {
        requestSongs(method, page, map);
    }

    public void setSong(List<SongSimple> songs) {
        mAdapter.setData(songs);
    }

    private void requestSongs(String method, int page, Map<String, String> map) {
        mMethod = method;
        HttpRequestV4 r = initRequest(mMethod);
        if (map != null) {
            Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                r.addParam(entry.getKey(), entry.getValue());
            }
        }
        r.addParam("current_page", String.valueOf(page));
        r.setConvert2Class(SongSimplePage.class);
        r.get();
    }

    private void requestSongs(int page, Map<String, String> map) {
        mMethod = RequestMethod.V4.V4_API_SONG;
        HttpRequestV4 r = initRequest(mMethod);
        if (map != null) {
            Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                r.addParam(entry.getKey(), entry.getValue());
            }
        }
        r.addParam("current_page", String.valueOf(page));
        r.setConvert2Class(SongSimplePage.class);
        r.get();
    }

    public HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(getContext().getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        try {
            SongSimplePage songsV4;
            if (object instanceof SongSimplePage && (songsV4 = (SongSimplePage) object).song != null
                    && songsV4.song.data != null && songsV4.song.data.size() > 0) {
                mAdapter.setData(songsV4.song.data);
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Logger.e(getClass().getSimpleName(), e.toString());
        }
    }

    @Override
    public void onFailed(String method, Object object) {
    }

    @Override
    public void onError(String method, String error) {

    }
}