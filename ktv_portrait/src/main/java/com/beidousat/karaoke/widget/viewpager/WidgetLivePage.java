package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.widget.adapter.AdtLive;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.Live;
import com.bestarmedia.libcommon.model.vod.Lives;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.recycler.GridRecyclerView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetLivePage extends GridRecyclerView implements HttpRequestListener {

    private AdtLive mAdapter;


    public WidgetLivePage(Context context) {
        super(context);
        init();
    }

    public WidgetLivePage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetLivePage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void runLayoutAnimation(boolean fromRight) {
        if (getAdapter() != null) {
            Logger.d(getClass().getSimpleName(), "runLayoutAnimation fromRight>>>>" + fromRight);
            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),
                    fromRight ? R.anim.layout_animation_fall_down : R.anim.layout_animation_fall_down);
            setLayoutAnimation(controller);
            getAdapter().notifyDataSetChanged();
            scheduleLayoutAnimation();
        }
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(28).margin(0, 0).build();
        addItemDecoration(horDivider);
        mAdapter = new AdtLive(getContext());
        setVerticalScrollBarEnabled(false);
        setAdapter(mAdapter);
    }


    public void loadLive(int page, Map<String, String> map) {
        requestSongs(page, map);
    }

    public void setLives(List<Live> lives) {
        mAdapter.setData(lives);
    }

    private void requestSongs(int page, Map<String, String> map) {
        HttpRequestV4 requestV4 = initRequestV4(RequestMethod.NODE_LIVE);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                requestV4.addParam(entry.getKey(), entry.getValue());
            }
        }
        requestV4.addParam("current_page", String.valueOf(page));
        requestV4.setConvert2Class(Lives.class);
        requestV4.get();
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
        if (RequestMethod.NODE_LIVE.equalsIgnoreCase(method)) {
            try {
                if (object instanceof Lives) {
                    Lives lives = (Lives) object;
                    if (lives.live != null && lives.live.data != null && lives.live.data.size() > 0) {
                        mAdapter.setData(lives.live.data);
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