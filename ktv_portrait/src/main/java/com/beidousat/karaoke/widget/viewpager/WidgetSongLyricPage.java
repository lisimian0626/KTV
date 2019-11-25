package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.widget.adapter.AdtSongLyric;
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
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetSongLyricPage extends GridRecyclerView implements HttpRequestListener {

    private String mMethod;
    private AdtSongLyric mAdapter;

    private int dividerV = DensityUtil.dip2px(getContext(), 6);

    private int dividerH = DensityUtil.dip2px(getContext(), 6);

    public WidgetSongLyricPage(Context context) {
        super(context);
        init();
    }

    public WidgetSongLyricPage(Context context, int marginVertical, int horizontalMargin) {
        super(context);
        dividerV = marginVertical;
        dividerH = horizontalMargin;
        init();
    }

    public WidgetSongLyricPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetSongLyricPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void notifyAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void runLayoutAnimation(boolean fromRight) {
        if (mAdapter != null) {
            Logger.d(getClass().getSimpleName(), "runLayoutAnimation fromRight>>>>" + fromRight);
            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),
                    fromRight ? R.anim.grid_layout_animation_from_right : R.anim.grid_layout_animation_from_left);
            setLayoutAnimation(controller);
            getAdapter().notifyDataSetChanged();
            scheduleLayoutAnimation();
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

        mAdapter = new AdtSongLyric(getContext());
        setVerticalScrollBarEnabled(false);
        setAdapter(mAdapter);
    }

    public void loadSong(int page, Map<String, String> map) {
        requestSongs(page, map);
    }

    public void loadSong(String method, int page, Map<String, String> map) {
        requestSongs(method, page, map);
    }

    private String mLyricWord;

    public void setLyricWord(String word) {
        this.mLyricWord = word;
    }

    public void setSong(List<SongSimple> songs) {
        mAdapter.setKeyword(mLyricWord);
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
        if (method.equalsIgnoreCase(mMethod)) {
            try {
                if (object != null) {
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