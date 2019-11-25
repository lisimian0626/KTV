package com.beidousat.karaoke.ui.fragment.setting;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.adapter.AdtApkVersion;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.ApkVersionInfo;
import com.bestarmedia.libcommon.model.vod.VersionListV4;
import com.bestarmedia.libcommon.util.PackageUtil;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.List;


/**
 * Created by J Wong on 2016/8/24.
 */
public class FmVersionInfo extends BaseFragment implements AdtApkVersion.OnApkVersionClickListener {

    private View mRootView;
    private RecyclerView mRvVersions;
    private TextView mTvTime, mTvUpdateLog, mTvCurVersion;
    private AdtApkVersion mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_version_info, null);
        mRootView.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
            }
        });
        mTvCurVersion = mRootView.findViewById(R.id.tv_cur_version);
        mRvVersions = mRootView.findViewById(R.id.rv_versions);
        mRvVersions.setHasFixedSize(true);

        mTvTime = mRootView.findViewById(R.id.tv_time);
        mTvUpdateLog = mRootView.findViewById(R.id.tv_log);

        mTvCurVersion.setText(getString(R.string.cur_version_x, PackageUtil.getVersionName(getContext().getApplicationContext())));
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(1).margin(1).build();

        mRvVersions.setLayoutManager(new LinearLayoutManager(getContext()));

        mRvVersions.addItemDecoration(horDivider);

        mAdapter = new AdtApkVersion(getContext());
        mAdapter.setOnApkVersionClickListener(this);
        mRvVersions.setAdapter(mAdapter);

        requestVersionList();

        return mRootView;
    }

    @Override
    public void onApkVersionClick(ApkVersionInfo versionInfo) {
        if (versionInfo != null) {
            setInfo(versionInfo);
        }
    }

    private void requestVersionList() {
        HttpRequestV4 r = initRequestV4(RequestMethod.VOD_VERSION_LIST);
        r.addParam("current_page", String.valueOf(1));
        r.addParam("per_page", String.valueOf(8));
        r.addParam("type", String.valueOf(16));
        r.setConvert2Class(VersionListV4.class);
        r.get();
    }

    private void setInfo(ApkVersionInfo versionInfo) {
        mTvTime.setText(versionInfo.lastUpdateTime != null ? versionInfo.lastUpdateTime : "");
        mTvUpdateLog.setText(versionInfo.updateContent != null ? versionInfo.updateContent : "");
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.VOD_VERSION_LIST.equals(method)) {
                VersionListV4 versionListV4 = (VersionListV4) object;
                if (versionListV4 != null && versionListV4.version != null) {
                    List<ApkVersionInfo> versionInfos = versionListV4.version.data;
                    if (versionInfos != null && versionInfos.size() > 0) {
                        mAdapter.setData(versionInfos);
                        mAdapter.notifyDataSetChanged();
                        ApkVersionInfo versionInfo = versionInfos.get(0);
                        setInfo(versionInfo);
                    }
                }
            }
        }
        super.onSuccess(method, object);
    }
}
