package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import com.beidousat.karaoke.ui.fragment.song.FmSingerDetail;
import com.beidousat.karaoke.util.FragmentUtil;
import com.beidousat.karaoke.widget.adapter.AdtSinger;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.OnSingerClickListener;
import com.bestarmedia.libcommon.model.v4.Start;
import com.bestarmedia.libcommon.model.v4.StartSimplePage;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.recycler.GridRecyclerView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetSingerPage extends GridRecyclerView implements HttpRequestListener, OnSingerClickListener {

    private AdtSinger mAdapter;

    public WidgetSingerPage(Context context) {
        super(context);
        init();
    }

    public WidgetSingerPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetSingerPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(DensityUtil.dip2px(getContext(), 20)).build();

        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(DensityUtil.dip2px(getContext(), 12)).build();

        setLayoutManager(new GridLayoutManager(getContext(), 5));

        addItemDecoration(horDivider);
        addItemDecoration(verDivider);

        mAdapter = new AdtSinger(getContext());
        mAdapter.setOnSingerClickListener(this);
        setVerticalScrollBarEnabled(false);
        setAdapter(mAdapter);
    }

    public void runLayoutAnimation() {
        if (getAdapter() != null) {
//            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.grid_layout_animation_from_bottom);
//            setLayoutAnimation(controller);
            getAdapter().notifyDataSetChanged();
//            scheduleLayoutAnimation();
        }
    }

//    private void init() {
//        mAdapter = new AdtSinger(getContext());
//        setNumColumns(4);
//        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
//        setHorizontalSpacing(spacing);
//        setVerticalSpacing(spacing);
//        setSelector(R.drawable.selector_list_item_song);
//        setAdapter(mAdapter);
//    }

    @Override
    public void onSingerClick(Start starInfo) {
        FmSingerDetail fmSingerDetail = FmSingerDetail.newInstance(starInfo);
        FragmentUtil.addFragment(fmSingerDetail, false, false, true, false);
    }

    public void setSinger(List<Start> starInfos) {
        mAdapter.setData(starInfos);
    }

    public void loadSinger(int page, Map<String, String> map) {
        requestSingers(page, map);
    }

    private void requestSingers(int page, Map<String, String> map) {
        HttpRequestV4 r = initRequest(RequestMethod.V4.V4_API_MUSICIAN);
        if (map != null) {
            Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                r.addParam(entry.getKey(), entry.getValue());
            }
        }
        r.addParam("current_page", String.valueOf(page));
        r.setConvert2Class(StartSimplePage.class);
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
        if (RequestMethod.V4.V4_API_MUSICIAN.equalsIgnoreCase(method)) {
            try {
                StartSimplePage result = (StartSimplePage) object;
                if (result != null) {
                    if (result.musician != null && result.musician.data != null && result.musician.data.size() > 0) {
                        mAdapter.setData(result.musician.data);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                Logger.e(getClass().getSimpleName(), e.toString());
            }
        }

//        if (RequestMethod.GET_SINGER.equalsIgnoreCase(method)) {
//            try {
//                if (object != null) {
//                    Singers singers = (Singers) object;
//                    if (singers != null && singers.list != null && singers.list.size() > 0) {
//                        mAdapter.setData(singers.list);
//                    }
//                }
//            } catch (Exception e) {
//                Log.e(getClass().getSimpleName(), e.toString());
//            }
//            mAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onFailed(String method, Object object) {
    }

    @Override
    public void onError(String method, String error) {

    }
}
