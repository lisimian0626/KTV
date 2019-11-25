package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.beidousat.karaoke.widget.adapter.AdtGameSong;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.SongSimplePage;
import com.bestarmedia.libcommon.util.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetGameSong extends RecyclerView implements HttpRequestListener {


    private String mMethod;
    private AdtGameSong mAdapter;


    public WidgetGameSong(Context context) {
        super(context);
        init();
    }

    public WidgetGameSong(Context context, int marginVertical, int horizontalMargin) {
        super(context);
        init();
    }

    public WidgetGameSong(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetGameSong(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setCheckable(boolean checkable) {
        if (mAdapter != null) {
            mAdapter.setCheckable(checkable);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void notifyAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }


    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);


//        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
//                .color(Color.TRANSPARENT).size(dividerH).margin(0, 0).build();
//
//        VerticalDividerItemDecoration2 verDivider = new VerticalDividerItemDecoration2.Builder(getContext())
//                .color(Color.TRANSPARENT).size(dividerV).margin(dividerV).build();
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        layoutManager3.setOrientation(LinearLayoutManager.VERTICAL);

        setLayoutManager(layoutManager3);

//        addItemDecoration(horDivider);
//        addItemDecoration(verDivider);

        mAdapter = new AdtGameSong(getContext());
        setVerticalScrollBarEnabled(false);
        setAdapter(mAdapter);
    }

    public int getSelectedPosition() {
        return mAdapter.getFocusPosition();
    }

    public SongSimple getSelectedSong() {
        return mAdapter.getSelectedPosition();
    }

    public void setSelectedSong(int ps) {
        mAdapter.setSelectPosition(ps);
        mAdapter.notifyDataSetChanged();
    }

    public void loadSong(int page, Map<String, String> map) {
        requestSongs(page, map);
    }


    public void setSong(List<SongSimple> songs) {
        mAdapter.setData(songs);
    }


    private void requestSongs(int page, Map<String, String> map) {
        mMethod = RequestMethod.V4.BREAKTHROUGH_SONG;
        HttpRequestV4 r = initRequestV4(mMethod);
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

    public HttpRequestV4 initRequestV4(String method) {
        HttpRequestV4 request = new HttpRequestV4(getContext().getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (method.equalsIgnoreCase(mMethod)) {
            try {
                if (object != null && object instanceof SongSimplePage) {
                    SongSimplePage songs = (SongSimplePage) object;
                    if (songs != null && songs.song != null && songs.song.data != null && songs.song.data.size() > 0) {
                        mAdapter.setData(songs.song.data);
                    }
                }
            } catch (Exception e) {
                Logger.e(getClass().getSimpleName(), e.toString());
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailed(String method, Object object) {
    }

    @Override
    public void onError(String method, String error) {

    }
}