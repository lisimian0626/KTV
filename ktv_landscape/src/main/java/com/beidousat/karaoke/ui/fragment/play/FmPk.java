package com.beidousat.karaoke.ui.fragment.play;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.ui.dialog.play.DlgPkGuide;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetPkPager;
import com.beidousat.karaoke.widget.viewpager.WidgetPkRankingPager;
import com.beidousat.karaoke.widget.viewpager.WidgetTopTabs;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.helper.QrCodeRequest;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.Pk;
import com.bestarmedia.libcommon.model.vod.PkDuelsV4;
import com.bestarmedia.libcommon.model.vod.PkRanking;
import com.bestarmedia.libcommon.model.vod.PkRankingsV4;
import com.bestarmedia.libcommon.model.vod.RoomQrCodeSimple;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.QrCodeUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmPk extends BaseFragment implements OnPageScrollListener, View.OnClickListener, WidgetPage.OnPageChangedListener {

    private View mRootView;

    private WidgetPkPager mSongPager;
    private WidgetPkRankingPager mRankingPager;
    private WidgetTopTabs mWidgetTopTabs;
    private WidgetPage mWidgetPage;
    private ProgressBar mPgbQrCode;
    private View vShowGuide;
    private Map<String, String> mRequestParam;
    private View mEmptyView;
    private ImageView mIvQrCode;
    private TextView tv_tips;
    private boolean havePkSong;

    private int mFocusTab = 0;

    public static FmPk newInstance() {
        FmPk fragment = new FmPk();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_pk, null);
        mEmptyView = mRootView.findViewById(R.id.rl_empty);
        mSongPager =  mRootView.findViewById(R.id.pkPager);
        mRankingPager =  mRootView.findViewById(R.id.ranking);
        mWidgetTopTabs =  mRootView.findViewById(R.id.topTab);
        mWidgetTopTabs.setRightTabShow(false);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);

        mWidgetTopTabs.setLeftTabs(R.array.pk_tabs);
        mWidgetTopTabs.setLeftTabClickListener(leftOnTabClickListener);
        mIvQrCode = mRootView.findViewById(R.id.iv_qrcode);
        mRootView.findViewById(R.id.rl_room_qr_code).setOnClickListener(this);
        mPgbQrCode =  mRootView.findViewById(R.id.pgb_qrcode);
        tv_tips=mRootView.findViewById(R.id.tv_qr_msg);
        mSongPager.setOnPagerScrollListener(this);
        mRankingPager.setOnPagerScrollListener(this);

        vShowGuide = mRootView.findViewById(R.id.tv_guide);
        vShowGuide.setOnClickListener(this);

        requestDuel();

//        EventBus.getDefault().register(this);

        return mRootView;
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.Id.PK_DUEL_CHANGED:
                requestDuel();
                break;
            default:
                break;
        }
    }

    private WidgetTopTabs.OnTabClickListener leftOnTabClickListener = new WidgetTopTabs.OnTabClickListener() {
        @Override
        public void onTabClick(int position) {
            onLeftTabClick(position);
        }
    };

    private void requestRanking() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.CLOUD_SONG_DUEL_RANKING);
        mRequestParam = r.getParams();
        r.setConvert2Class(PkRankingsV4.class);
        r.get();
    }


    private void requestDuel() {
        HttpRequestV4 request = initRequestV4(RequestMethod.V4.CLOUD_SONG_DUEL);
        request.addParam("per_page", String.valueOf(6));
        request.addParam("current_page", String.valueOf(1));
        request.setConvert2Class(PkDuelsV4.class);
        mRequestParam = request.getParams();
        request.get();
    }

    private void updateViewVisible() {
        if (mFocusTab == 0) {
            mRankingPager.setVisibility(View.GONE);
            if (havePkSong) {
                mSongPager.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                vShowGuide.setVisibility(View.VISIBLE);
                mWidgetPage.setVisibility(View.VISIBLE);
            } else {
                mWidgetPage.setVisibility(View.INVISIBLE);
                mSongPager.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                vShowGuide.setVisibility(View.GONE);
                loadQrCode();
            }
        } else {
            mSongPager.setVisibility(View.GONE);
            mRankingPager.setVisibility(View.VISIBLE);
            mWidgetPage.setVisibility(View.VISIBLE);
            mSongPager.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
        if (RequestMethod.V4.CLOUD_SONG_DUEL.equalsIgnoreCase(method)) {
            havePkSong = false;
            updateViewVisible();
        }
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.CLOUD_SONG_DUEL.equalsIgnoreCase(method)) {
                PkDuelsV4 pkDuelsV4;
                if (object != null && object instanceof PkDuelsV4 && (pkDuelsV4 = (PkDuelsV4) object) != null && pkDuelsV4.songDuel != null
                        && pkDuelsV4.songDuel.data != null && pkDuelsV4.songDuel.data.size() > 0) {
                    havePkSong = true;
                    initSongPager(pkDuelsV4.songDuel.last_page, pkDuelsV4.songDuel.data, mRequestParam);
                } else {
                    havePkSong = false;
                }
                updateViewVisible();
            } else if (RequestMethod.V4.CLOUD_SONG_DUEL_RANKING.equalsIgnoreCase(method)) {
                PkRankingsV4 pkRankings;
                if (object != null && object instanceof PkRankingsV4 && (pkRankings = (PkRankingsV4) object) != null
                        && pkRankings.songDuel != null && pkRankings.songDuel.data != null && pkRankings.songDuel.data.size() > 0) {
                    initRankingPager(pkRankings.songDuel.data);
                }
            }
        }
        super.onSuccess(method, object);
    }

    private void onLeftTabClick(int position) {
        mFocusTab = position;
        if (mFocusTab == 0) {
            requestDuel();
        } else {
            requestRanking();
        }
        updateViewVisible();
    }

    private void initSongPager(int totalPage, List<Pk> firstPageSong, Map<String, String> params) {
        Logger.i(getClass().getSimpleName(), "Current total page:" + totalPage);
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mSongPager.initPager(totalPage, firstPageSong, params);
    }

    private void initRankingPager(List<PkRanking> pkRankings) {
        mWidgetPage.setPageCurrent(0);
        int totalPage = mRankingPager.initPager(pkRankings, 9);
        mWidgetPage.setPageTotal(totalPage);
    }


    @Override
    public void onPrePageClick(int before, int current) {
        if (mFocusTab == 0) {
            mSongPager.setCurrentItem(current);
        } else {
            mRankingPager.setCurrentItem(current);
        }
    }

    @Override
    public void onNextPageClick(int before, int current) {
        if (mFocusTab == 0) {
            mSongPager.setCurrentItem(current);
        } else {
            mRankingPager.setCurrentItem(current);
        }
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        if (mFocusTab == 0) {
            mSongPager.setCurrentItem(current);
        } else {
            mRankingPager.setCurrentItem(current);
        }
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPageScrollLeft() {
        mWidgetPage.setPrePressed(true);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPageScrollRight() {
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(true);
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        mWidgetPage.setPageCurrent(position);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
        if (mFocusTab == 0)
            mSongPager.notifyCurrentPage();
    }

    @Override
    public void onDestroyView() {
//        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_guide:
                DlgPkGuide dlgPkGuide = new DlgPkGuide(getActivity());
                dlgPkGuide.show();
                break;
            case R.id.rl_room_qr_code:
                loadQrCode();
                break;
        }
    }


    private void loadQrCode() {
        if (isAdded()) {
            mPgbQrCode.setVisibility(View.VISIBLE);
            mIvQrCode.setClickable(false);
            QrCodeRequest request = new QrCodeRequest(getContext(), new QrCodeRequest.QrCodeRequestListener() {
                @Override
                public void onQrCode(RoomQrCodeSimple code) {
                    if (isAdded()) {
                        mPgbQrCode.setVisibility(View.GONE);
                        if (code != null) {
                            Bitmap bitmap = QrCodeUtil.createQRCode(code.toString());
                            if (bitmap != null) {
                                mIvQrCode.setImageBitmap(bitmap);
                            }
                        }
                    }
                }

                @Override
                public void onQrCodeFail(String error) {
                    if (isAdded()) {
                        mPgbQrCode.setVisibility(View.GONE);
                        mIvQrCode.setImageResource(R.drawable.ic_qrcode_disable);
                        tv_tips.setText(error);
                        mIvQrCode.setClickable(true);
                    }
                }
            });
            request.requestCode();
        }
    }
}
