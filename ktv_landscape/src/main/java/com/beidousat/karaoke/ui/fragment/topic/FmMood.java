package com.beidousat.karaoke.ui.fragment.topic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.adapter.AdtMood;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.TopicsV4;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.recycler.SpacesItemDecoration;

/**
 * Created by J Wong on 2015/12/16 16:43.
 */
public class FmMood extends BaseFragment {

    private View mRootView;
    private RecyclerView mRecyclerView;
    private AdtMood mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_mood, null);
        initView();
        requestTopics();
        return mRootView;
    }

    private void requestTopics() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.SONG_LIST);
        r.addParam("per_page", String.valueOf(100));
        r.addParam("current_page", String.valueOf(1));
        r.setConvert2Class(TopicsV4.class);
        r.get();
    }


    private void initView() {
        mRecyclerView = mRootView.findViewById(R.id.rv_mood);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        //RecyclerView的横向布局
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//防止滑动时跳动
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        layoutManager.invalidateSpanAssignments();
                        mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
                    }
                }
        );
        mAdapter = new AdtMood(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.SONG_LIST.equalsIgnoreCase(method)) {
                try {
                    if (object != null) {
                        TopicsV4 topics = (TopicsV4) object;
                        if (topics.topic != null && topics.topic.data != null && topics.topic.data.size() > 0) {
                            mAdapter.addData(topics.topic.data);
                        }
                    }
                } catch (Exception e) {
                    Logger.e(getClass().getSimpleName(), e.toString());
                }
            }
        }
        super.onSuccess(method, object);
    }
}
