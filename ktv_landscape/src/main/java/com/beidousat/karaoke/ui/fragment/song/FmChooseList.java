package com.beidousat.karaoke.ui.fragment.song;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.viewpager.WidgetChoosePagerList;
import com.beidousat.karaoke.widget.viewpager.WidgetDownloadPager;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetSungPagerList;
import com.beidousat.karaoke.widget.viewpager.WidgetTopTabs;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.model.vod.RoomSongsV4;
import com.bestarmedia.libcommon.transmission.CloudSongDownloadHelper;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.image.RecyclerImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmChooseList extends BaseFragment implements OnPageScrollListener, View.OnClickListener, WidgetPage.OnPageChangedListener {

    private View mRootView;
    private WidgetChoosePagerList mSongPager;
    private WidgetSungPagerList mSungPager;
    private WidgetDownloadPager mDownloadPager;

    private WidgetPage mWidgetPage;

    private WidgetTopTabs mWidgetTopTabs;
    private TextView mTvShuffle;
    private RecyclerImageView mRivEmpty;
    private TextView mTvEmpty;
    private int mCurTab = 0;
    private Map<String, String> mRequestParam;
    private Map<String, String> mRequestSungParam;

    private int mCurrent = 0;
    private int mCurrentSung = 0;
    private int mCurrentDownload = 0;


    public static FmChooseList newInstance(int focusTab) {
        FmChooseList fragment = new FmChooseList();
        Bundle args = new Bundle();
        args.putInt("focusTab", focusTab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurTab = getArguments().getInt("focusTab", 0);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_choose_list, null);
        mRivEmpty = mRootView.findViewById(R.id.riv_empty);
        mTvEmpty = mRootView.findViewById(R.id.tv_empty);
        mTvShuffle = mRootView.findViewById(R.id.tv_shuffle);
        mTvShuffle.setOnClickListener(this);
        mSongPager = mRootView.findViewById(R.id.choosePager);

        mDownloadPager = mRootView.findViewById(R.id.downloadPager);

        mSungPager = mRootView.findViewById(R.id.sungPager);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetTopTabs = mRootView.findViewById(R.id.topTab);
        mWidgetPage.setOnPageChangedListener(this);
        mWidgetTopTabs.setLeftTabClickListener(this::setTab);
        mWidgetTopTabs.setRightTabShow(false);
        mSongPager.setOnPagerScrollListener(this);
        mSungPager.setOnPagerScrollListener(this);

        mDownloadPager.setOnPagerScrollListener(this);

        mWidgetTopTabs.setLeftTabs(R.array.choose_tabs);

        EventBus.getDefault().register(this);

        setTab(mCurTab);

        return mRootView;
    }

    @Override
    public void onPrePageClick(int before, int current) {
        if (mCurTab == 0) {
            mSongPager.setCurrentItem(current);
        } else if (mCurTab == 1) {
            mSungPager.setCurrentItem(current);
        } else if (mCurTab == 2) {
            mDownloadPager.setCurrentItem(current);
        }
    }

    @Override
    public void onNextPageClick(int before, int current) {
        if (mCurTab == 0) {
            mSongPager.setCurrentItem(current);
        } else if (mCurTab == 1) {
            mSungPager.setCurrentItem(current);
        } else if (mCurTab == 2) {
            mDownloadPager.setCurrentItem(current);
        }
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        if (mCurTab == 0) {
            mSongPager.setCurrentItem(current);
        } else if (mCurTab == 1) {
            mSungPager.setCurrentItem(current);
        } else if (mCurTab == 2) {
            mDownloadPager.setCurrentItem(current);
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
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void setTab(int tab) {
        mCurTab = tab;
        mWidgetTopTabs.setLeftTabFocus(mCurTab);
        switch (mCurTab) {
            case 0:
                mSongPager.setVisibility(View.VISIBLE);
                mSungPager.setVisibility(View.GONE);
                mDownloadPager.setVisibility(View.GONE);
                requestChoose();
                break;
            case 1:
                mSongPager.setVisibility(View.GONE);
                mSungPager.setVisibility(View.VISIBLE);
                mDownloadPager.setVisibility(View.GONE);
                mTvShuffle.setVisibility(View.INVISIBLE);
                requestSung();
                break;
            case 2:
                mSongPager.setVisibility(View.GONE);
                mSungPager.setVisibility(View.GONE);
                mDownloadPager.setVisibility(View.VISIBLE);
                mTvShuffle.setVisibility(View.INVISIBLE);
                initDownloadPager();
                break;
        }
    }

    public void initSongPager(int totalPage, List<RoomSongItem> firstPageSong, Map<String, String> params) {
        Log.i(getClass().getSimpleName(), "initSongPager total page:" + totalPage);
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mSongPager.initPager(totalPage, firstPageSong);
        if (totalPage <= 0) {
            mTvEmpty.setText(R.string.choose_empty_tip2);
            mRivEmpty.setVisibility(View.VISIBLE);
            mTvEmpty.setVisibility(View.VISIBLE);
            mTvShuffle.setVisibility(View.INVISIBLE);
        } else {
            mRivEmpty.setVisibility(View.GONE);
            mTvEmpty.setVisibility(View.GONE);
            mTvShuffle.setVisibility(mCurTab == 0 && firstPageSong != null && firstPageSong.size() > 2 ? View.VISIBLE : View.INVISIBLE);
        }
        if (mCurrent != mSongPager.getCurrentItem()) {
            mSongPager.setCurrentItem(mCurrent < totalPage - 1 ? mCurrent : totalPage - 1);
        }
    }

    public void initSungPager(int totalPage, List<RoomSongItem> firstPageSong, Map<String, String> params) {
        Log.i(getClass().getSimpleName(), "initSungPager total page:" + totalPage);
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mSungPager.initPager(totalPage, firstPageSong, params);
        if (totalPage <= 0) {
            mTvEmpty.setText(R.string.sung_empty_tip);
            mRivEmpty.setVisibility(View.VISIBLE);
            mTvEmpty.setVisibility(View.VISIBLE);
            mTvShuffle.setVisibility(View.INVISIBLE);
        } else {
            mRivEmpty.setVisibility(View.GONE);
            mTvEmpty.setVisibility(View.GONE);
        }
        if (mCurrentSung != mSungPager.getCurrentItem()) {
            mSungPager.setCurrentItem(mCurrentSung < totalPage - 1 ? mCurrentSung : totalPage - 1);
        }
    }

    public void initDownloadPager() {
        List<SongSimple> all = new ArrayList<>();
        List<SongSimple> songDownloading = CloudSongDownloadHelper.getDownloadingSongs();
        if (songDownloading != null && songDownloading.size() > 0) {
            all.addAll(songDownloading);
        }
        List<SongSimple> songDownloadFail = CloudSongDownloadHelper.getDownloadFailSongs();
        if (songDownloadFail != null && songDownloadFail.size() > 0) {
            all.addAll(songDownloadFail);
        }
        int totalPage = mDownloadPager.initPager(all);
        Log.i(getClass().getSimpleName(), "initDownloadPager total page:" + totalPage);
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);

        if (totalPage <= 0) {
            mTvEmpty.setText(R.string.download_empty_tip);
            mRivEmpty.setVisibility(View.VISIBLE);
            mTvEmpty.setVisibility(View.VISIBLE);
            mTvShuffle.setVisibility(View.INVISIBLE);
        } else {
            mRivEmpty.setVisibility(View.GONE);
            mTvEmpty.setVisibility(View.GONE);
        }
        if (mCurrentDownload != mDownloadPager.getCurrentItem()) {
            mDownloadPager.setCurrentItem(mCurrentDownload < totalPage - 1 ? mCurrentDownload : totalPage - 1);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_shuffle) {
            mCurrent = mSongPager.getCurrentItem();
            if (getActivity() != null) {
                VodApplication.getKaraokeController().shuffle();
            }
        }
    }

    @Subscribe
    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.Id.CHOOSE_SONG_CHANGED:
                Logger.d(getClass().getSimpleName(), "onEventMainThread CHOOSE_SONG_CHANGED >>>>>>>>>>>");
                if (mCurTab == 0) {
                    mCurrent = mSongPager.getCurrentItem();
                    requestChoose();
                }
                break;
            case EventBusId.Id.SUNG_SONG_CHANGED:
                if (mCurTab == 1) {
                    mCurrentSung = mSungPager.getCurrentItem();
                    requestSung();
                }
                break;
            case EventBusId.Id.SONG_DOWNLOAD_CHANGED:
                if (mCurTab == 2) {
                    mCurrentDownload = mDownloadPager.getCurrentItem();
                    initDownloadPager();
                }
                break;
            case EventBusId.Id.ROOM_STATUS_CHANGED:
                if (mCurTab == 0) {
                    mCurrent = mSongPager.getCurrentItem();
                    requestChoose();
                } else if (mCurTab == 1) {
                    mCurrentSung = mSungPager.getCurrentItem();
                    requestSung();
                }
                break;
            default:
                break;
        }
    }


    public void requestChoose() {
        HttpRequestV4 r = initRequestV4(RequestMethod.NODE_ROOM_SONG_LIST_CHOOSE);
        r.addParam("current_page", String.valueOf(1));
        r.addParam("per_page", String.valueOf(8));
        r.addParam("session", NodeRoomInfo.getInstance().getRoomSession());
        r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        r.setConvert2Class(RoomSongsV4.class);
        mRequestParam = r.getParams();
        r.get();
    }

    public void requestSung() {
        HttpRequestV4 r = initRequestV4(RequestMethod.NODE_ROOM_SONG_LIST_SUNG);
        r.addParam("current_page", String.valueOf(1));
        r.addParam("per_page", String.valueOf(8));
        r.addParam("session", NodeRoomInfo.getInstance().getRoomSession());
        r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        r.setConvert2Class(RoomSongsV4.class);
        mRequestSungParam = r.getParams();
        r.get();
    }

    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            try {
                if (RequestMethod.NODE_ROOM_SONG_LIST_CHOOSE.equalsIgnoreCase(method)) {
                    if (object instanceof RoomSongsV4) {
                        RoomSongsV4 songs = (RoomSongsV4) object;
                        if (songs.song != null && songs.song.data != null && songs.song.data.size() > 0) {
                            int totalPage = songs.song.last_page;
                            initSongPager(totalPage, songs.song.data, mRequestParam);
                        } else {
                            initSongPager(0, null, mRequestParam);
                        }
                    }
                } else if (RequestMethod.NODE_ROOM_SONG_LIST_SUNG.equalsIgnoreCase(method)) {
                    if (object instanceof RoomSongsV4) {
                        RoomSongsV4 songs = (RoomSongsV4) object;
                        if (songs.song != null && songs.song.data != null && songs.song.data.size() > 0) {
                            int totalPage = songs.song.last_page;
                            initSungPager(totalPage, songs.song.data, mRequestSungParam);
                        } else {
                            initSungPager(0, null, mRequestSungParam);
                        }
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

}
