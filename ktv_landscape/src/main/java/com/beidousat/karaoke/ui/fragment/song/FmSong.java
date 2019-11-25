package com.beidousat.karaoke.ui.fragment.song;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.util.FragmentUtil;
import com.beidousat.karaoke.widget.AdSupporterPlayer;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.NewSong;
import com.bestarmedia.libcommon.model.vod.NewSongInfo;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libskin.SkinManager;
import com.bestarmedia.libwidget.material.ImageMaterial;
import com.bestarmedia.libwidget.util.GlideUtils;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

/**
 * Created by J Wong on 2015/12/16 16:13.
 */
public class FmSong extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private AdSupporterPlayer mAdSupporterPlayer;
    private ImageMaterial im_new_song;
    private NewSongInfo newSongInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_song, null);
        ImageMaterial im_song = mRootView.findViewById(R.id.wm_pinyin);
        im_song.setOnClickListener(this);
        GlideUtils.loadLocalGif(getContext(), R.drawable.song_song, im_song);
        im_new_song = mRootView.findViewById(R.id.wm_song_new);
        im_new_song.setOnClickListener(this);
        mRootView.findViewById(R.id.wm_star).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_category).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_lyric).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_score).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_hd).setOnClickListener(this);
        mAdSupporterPlayer = mRootView.findViewById(R.id.ad_supporter);
        mAdSupporterPlayer.loadAds();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        SkinManager.getInstance().register(mRootView);
        requestRecommend();
        return mRootView;
    }


    private void requestRecommend() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.VOD_NEW_SONG);
        r.setConvert2Class(NewSong.class);
        r.get();
    }

    @Override
    public void onDestroyView() {
        SkinManager.getInstance().unregister(mRootView);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mAdSupporterPlayer != null)
            try {
                if (hidden) {
                    mAdSupporterPlayer.stopPlayer();
                } else {
                    mAdSupporterPlayer.startPlayer();
                }
            } catch (Exception e) {
                Logger.i(getClass().getName(), e.toString());
            }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdSupporterPlayer.startPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdSupporterPlayer.stopPlayer();
    }

    @Subscribe
    public void onEventMainThread(BusEvent event) {
        if (event.id == EventBusId.Id.AFTER_ROOM_INFO) {
            mAdSupporterPlayer.loadAds();
            requestRecommend();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wm_star:
                FragmentUtil.addFragment(new FmSinger(), false, false, true, false);
                break;
            case R.id.wm_pinyin:
                FmSongCommon fmSongCommon = FmSongCommon.newInstance(new String[]{}, new String[]{});
                FragmentUtil.addFragment(fmSongCommon, false, false, true, false);
                break;
            case R.id.wm_song_new:
                if (newSongInfo == null) {
                    Toast.makeText(getContext(), "模块正在准备，请稍后", Toast.LENGTH_SHORT).show();
                } else {
                    FragmentUtil.addFragment(FmNewSong.newInstance(newSongInfo), false, false, true, false);
                }
                break;
            case R.id.wm_category:
                FragmentUtil.addFragment(new FmCategory(), false, false, false, false);
                break;
            case R.id.wm_lyric:
                FragmentUtil.addFragment(FmSongLyric.newInstance(), false, false, true, false);
                break;
            case R.id.wm_score:
                fmSongCommon = FmSongCommon.newInstance(new String[]{"is_score"}, new String[]{"1"});
                FragmentUtil.addFragment(fmSongCommon, false, false, true, false);
                break;
            case R.id.wm_hd:
                fmSongCommon = FmSongCommon.newInstance(new String[]{"is_hd"}, new String[]{"1"});
                FragmentUtil.addFragment(fmSongCommon, false, false, true, false);
                break;
        }
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.VOD_NEW_SONG.equalsIgnoreCase(method)) {
                if (object instanceof NewSong) {
                    NewSong newSong = (NewSong) object;
                    newSongInfo = newSong.newSongInfo;
                    if (newSongInfo != null&&getContext()!=null) {
                        Glide.with(getContext()).load(TextUtils.isEmpty(newSong.newSongInfo.imageUrl) ?
                                newSong.newSongInfo.image : newSong.newSongInfo.imageUrl).error(R.drawable.song_new).into(im_new_song);
                    } else {
                        im_new_song.setImageResource(R.drawable.song_new);
                    }
                } else {
                    im_new_song.setImageResource(R.drawable.song_new);
                }
            }
        }
        super.onSuccess(method, object);
    }

    @Override
    public void onFailed(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.VOD_NEW_SONG.equalsIgnoreCase(method)) {
                im_new_song.setImageResource(R.drawable.song_new);
            }
        }
        super.onFailed(method, object);
    }

    @Override
    public void onError(String method, String error) {
        super.onError(method, error);
        im_new_song.setImageResource(R.drawable.song_new);
    }
}
