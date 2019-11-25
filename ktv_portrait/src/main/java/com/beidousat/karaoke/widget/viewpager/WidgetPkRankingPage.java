package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.beidousat.karaoke.widget.adapter.AdtPkRanking;
import com.bestarmedia.libcommon.model.vod.PkRanking;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetPkRankingPage extends RecyclerView {

    private String mMethod;

    private AdtPkRanking mAdapter;

    private int dividerH = DensityUtil.dip2px(getContext(), 8);

    public WidgetPkRankingPage(Context context) {
        super(context);
        init();
    }

    public WidgetPkRankingPage(Context context, int marginVertical, int horizontalMargin) {
        super(context);
        dividerH = horizontalMargin;
        init();
    }

    public WidgetPkRankingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetPkRankingPage(Context context, AttributeSet attrs, int defStyleAttr) {
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

//        VerticalDividerItemDecoration2 verDivider = new VerticalDividerItemDecoration2.Builder(getContext())
//                .color(Color.TRANSPARENT).size(dividerV).margin(dividerV).build();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(linearLayoutManager);

        addItemDecoration(horDivider);
//        addItemDecoration(verDivider);

        mAdapter = new AdtPkRanking(getContext());
        setVerticalScrollBarEnabled(false);
        setAdapter(mAdapter);
    }


//    public void loadSong(int page, Map<String, String> map, int pageNum) {
//        requestSongs(page, map, pageNum);
//    }

    public void setSong(List<PkRanking> songs, int pageNum, int pageSize) {
        mAdapter.setData(songs, pageNum, pageSize);
    }

//    private void requestSongs(int page, Map<String, String> map, int pageNum) {
//        mPageNum = pageNum;
//        mMethod = RequestMethod.GET_DUEL_LIST;
//        HttpRequest r = initRequest(RequestMethod.GET_DUEL_LIST);
//        if (map != null) {
//            Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
//            while (entries.hasNext()) {
//                Map.Entry<String, String> entry = entries.next();
//                r.addParam(entry.getKey(), entry.getValue());
//            }
//        }
//        r.setConvert2Class(PkRankings.class);
//        r.doPost(page);
//    }

//    public HttpRequest initRequest(String method) {
//        HttpRequest request = new HttpRequest(getContext().getApplicationContext(), method);
//        request.setHttpRequestListener(this);
//        return request;
//    }

//    @Override
//    public void onStart(String method) {
//    }
//
//    @Override
//    public void onSuccess(String method, Object object) {
//        if (method.equalsIgnoreCase(mMethod)) {
//            try {
//                if (object != null) {
//                    PkRankings songs = (PkRankings) object;
//                    if (songs != null && songs.list != null && songs.list.size() > 0) {
//                        mAdapter.setData(songs.list, mPageNum);
//                    }
//                }
//            } catch (Exception e) {
//                Logger.e(getClass().getSimpleName(), e.toString());
//            }
//            mAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onFailed(String method, String error) {
//    }
}