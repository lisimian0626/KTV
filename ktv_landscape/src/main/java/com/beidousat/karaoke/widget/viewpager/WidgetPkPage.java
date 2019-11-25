package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.beidousat.karaoke.widget.adapter.AdtPk;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.Pk;
import com.bestarmedia.libcommon.model.vod.PkDuelsV4;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration2;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetPkPage extends RecyclerView implements HttpRequestListener {

    private String mMethod;

    private AdtPk mAdapter;

    private int dividerV = DensityUtil.dip2px(getContext(), 10);

    private int dividerH = DensityUtil.dip2px(getContext(), 16);

    public WidgetPkPage(Context context) {
        super(context);
        init();
    }

    public WidgetPkPage(Context context, int marginVertical, int horizontalMargin) {
        super(context);
        dividerV = marginVertical;
        dividerH = horizontalMargin;
        init();
    }

    public WidgetPkPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetPkPage(Context context, AttributeSet attrs, int defStyleAttr) {
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
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(dividerH).margin(dividerH).build();
        VerticalDividerItemDecoration2 verDivider = new VerticalDividerItemDecoration2.Builder(getContext())
                .color(Color.TRANSPARENT).size(dividerV).margin(dividerV).build();
        setLayoutManager(new GridLayoutManager(getContext(), 2));
        addItemDecoration(horDivider);
        addItemDecoration(verDivider);
        mAdapter = new AdtPk(getContext());
        setVerticalScrollBarEnabled(false);
        setAdapter(mAdapter);
    }


    public void loadSong(int page, Map<String, String> map) {
        requestSongs(page, map);
    }

    public void loadSong(String method, int page, Map<String, String> map) {
        requestSongs(method, page, map);
    }

    public void setSong(List<Pk> songs) {
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
        r.setConvert2Class(PkDuelsV4.class);
        r.get();
    }

    private void requestSongs(int page, Map<String, String> map) {
        mMethod = RequestMethod.V4.CLOUD_SONG_DUEL;
        HttpRequestV4 r = initRequest(RequestMethod.V4.CLOUD_SONG_DUEL);
        if (map != null) {
            Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                r.addParam(entry.getKey(), entry.getValue());
            }
        }
        r.addParam("current_page", String.valueOf(page));
        r.setConvert2Class(PkDuelsV4.class);
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
                    PkDuelsV4 pkDuelsV4;
                    if (object != null && object instanceof PkDuelsV4 && (pkDuelsV4 = (PkDuelsV4) object) != null && pkDuelsV4.songDuel != null
                            && pkDuelsV4.songDuel.data != null && pkDuelsV4.songDuel.data.size() > 0) {
                        mAdapter.setData(pkDuelsV4.songDuel.data);
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