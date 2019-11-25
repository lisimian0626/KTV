package com.beidousat.karaoke.ui.fragment.play;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.helper.BnsSkinManager;
import com.beidousat.karaoke.ui.adapter.AdtSkin;
import com.beidousat.karaoke.ui.dialog.play.DlgSwitchSkin;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.bestarmedia.libcommon.data.Constant;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.play.Skin;
import com.bestarmedia.libcommon.model.vod.play.SkinPage;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.PackageUtil;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/16 16:43.
 */
public class FmSkin extends BaseFragment {

    private View mRootView;
    private RecyclerView mRvSkin;
    private AdtSkin mAdtSkin;
    private List<Skin> mSkins;
    private DlgSwitchSkin dlgSwitchSkin;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_skin, null);

        initView();

        requestSkinList();

        init();

        return mRootView;
    }


    private void requestSkinList() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.SKIN_LIST);
        r.addParam("skin_version", Constant.SKIN_VERSION);
        r.setConvert2Class(SkinPage.class);
        r.get();
    }


    private void initView() {
        mRvSkin = mRootView.findViewById(R.id.rv_skin);
    }


    private void init() {

        int dividerWidth = DensityUtil.dip2px(getActivity().getApplicationContext(), 20);
        int dividerWidth2 = DensityUtil.dip2px(getActivity().getApplicationContext(), 30);

        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getActivity().getApplicationContext())
                .color(Color.TRANSPARENT).size(dividerWidth2).margin(dividerWidth2, dividerWidth2).build();

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(dividerWidth).margin(dividerWidth, dividerWidth).build();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //RecyclerView的横向布局
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvSkin.setLayoutManager(layoutManager);
        mRvSkin.addItemDecoration(horDivider);
        mRvSkin.addItemDecoration(verDivider);
        mAdtSkin = new AdtSkin(getContext());
        mRvSkin.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);
                                            mAdtSkin.notifyDataSetChanged();
                                        }
                                    }
        );
        dlgSwitchSkin=new DlgSwitchSkin(getContext());
        mAdtSkin.setOnItemSelectListener(skinInfo -> {
            Logger.d("FmSkin", "select skin >>" + skinInfo.name);
            if (TextUtils.isEmpty(skinInfo.id)) {//还原默认皮肤
                new BnsSkinManager(getContext()).restoreDefaultSkin();
            } else {
                dlgSwitchSkin = new DlgSwitchSkin(getActivity());
                dlgSwitchSkin.setMessage("切换皮肤：" + skinInfo.name);
                dlgSwitchSkin.showProgress(true);
                dlgSwitchSkin.show();

                new BnsSkinManager(getContext()).setSwitchSkinListener((status, message) -> {
                    Logger.d("FmSkin", "onSwitchSkin status:" + status + "  message:" + message);
                    switch (status) {
                        case -1:
                            if (dlgSwitchSkin != null && dlgSwitchSkin.isShowing()) {
                                dlgSwitchSkin.setMessage(message);
                                dlgSwitchSkin.showProgress(false);
                                dlgSwitchSkin.showOkButton(getString(R.string.close));
                                mAdtSkin.notifyDataSetChanged();
                            }
                            break;
                        case 1:
                            if (dlgSwitchSkin != null && dlgSwitchSkin.isShowing()) {
                                dlgSwitchSkin.setMessage(message);
                                dlgSwitchSkin.showProgress(false);
                                dlgSwitchSkin.showOkButton(getString(R.string.ok));
                            }
                            mAdtSkin.notifyDataSetChanged();
                            break;
                        case 0:
                            if (dlgSwitchSkin != null && dlgSwitchSkin.isShowing()) {
                                dlgSwitchSkin.setMessage(message);
                                dlgSwitchSkin.showProgress(true);
                            }
                            break;
                    }
                }).downloadAndChangSkin(skinInfo.id, TextUtils.isEmpty(skinInfo.fileUrl) ? skinInfo.filePath : skinInfo.fileUrl);
            }
        });
        mRvSkin.setAdapter(mAdtSkin);
    }



    @Override
    public void onFailed(String method, Object error) {
        if (isAdded()) {
            mSkins = new ArrayList<>();
            mSkins.add(createDefaultSkin());
            mAdtSkin.setData(mSkins);
            mAdtSkin.notifyDataSetChanged();
        }
        super.onFailed(method, error);
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.SKIN_LIST.equals(method)) {
                if (object instanceof SkinPage) {
                    SkinPage skinPage = (SkinPage) object;
                    if (skinPage.skinList != null && skinPage.skinList.data != null ) {
                        mSkins = skinPage.skinList.data;
                        if (mSkins != null && mSkins.size() > 0) {
                            mSkins.add(0, createDefaultSkin());
                        } else {
                            mSkins = new ArrayList<>();
                            mSkins.add(createDefaultSkin());
                        }
                        mAdtSkin.setData(mSkins);
                        mAdtSkin.notifyDataSetChanged();
                    }
                }
            }
        }
        super.onSuccess(method, object);
    }

    private Skin createDefaultSkin() {
        Skin skinInfo = new Skin();
        skinInfo.id = "";
        skinInfo.name = getString(R.string.skin_default);
        return skinInfo;
    }
}
