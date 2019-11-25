package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import com.beidousat.karaoke.widget.adapter.AdtHyun;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.play.CoolScreen;
import com.bestarmedia.libcommon.model.vod.play.Hyun;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.recycler.GridRecyclerView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.util.DensityUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetHyunPage extends GridRecyclerView implements HttpRequestListener {

    private AdtHyun mAdapter;

    public WidgetHyunPage(Context context) {
        super(context);
        init();
    }

    public WidgetHyunPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetHyunPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(DensityUtil.dip2px(getContext(), 20)).build();

        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(DensityUtil.dip2px(getContext(), 20)).build();

        setLayoutManager(new GridLayoutManager(getContext(), 3));

        addItemDecoration(horDivider);
        addItemDecoration(verDivider);

        mAdapter = new AdtHyun(getContext());
        setVerticalScrollBarEnabled(false);
        setAdapter(mAdapter);
    }

    public void notifyAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void runLayoutAnimation() {
        if (mAdapter != null) {
//            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.grid_layout_animation_from_bottom);
//            setLayoutAnimation(controller);
            getAdapter().notifyDataSetChanged();
//            scheduleLayoutAnimation();
        }
    }

    public void setHyuns(List<Hyun> hyuns) {
        mAdapter.setData(hyuns);
    }

    public void loadHyun(int page, Map<String, String> map) {
        requestHyun(page, map);
    }

    private void requestHyun(int page, Map<String, String> map) {
        HttpRequestV4 r = initRequest(RequestMethod.V4.COOL_SCREEN);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                r.addParam(entry.getKey(), entry.getValue());
            }
        }
        r.addParam("current_page", String.valueOf(page));
        r.setConvert2Class(CoolScreen.class);
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
        if (RequestMethod.V4.COOL_SCREEN.equalsIgnoreCase(method)) {
            try {
                CoolScreen result = (CoolScreen) object;
                if (result != null) {
                    if (result.hyunList != null && result.hyunList.data != null && result.hyunList.data.size() > 0) {
                        mAdapter.setData(result.hyunList.data);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                Logger.e(getClass().getSimpleName(), e.toString());
            }
        }

    }

    @Override
    public void onFailed(String method, Object object) {
    }

    @Override
    public void onError(String method, String error) {

    }
}
