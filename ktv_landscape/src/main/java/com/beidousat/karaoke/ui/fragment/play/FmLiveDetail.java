package com.beidousat.karaoke.ui.fragment.play;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.data.ChooseSongs;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.bestarmedia.libcommon.data.ProjectorInfo;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.vod.Live;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by J Wong on 2015/10/9 12:06.
 */
public class FmLiveDetail extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private ImageView mIvCover;
    private TextView mTvName, mTvPlay, mTvReleaseTime, mTvDuration, mTvArea, mTvIntroducttion, mTvToScene;
    private Live mLive;
    private View llScenes;
    private ImageView mIvScenesStop;

    public static FmLiveDetail newInstance(Live live) {
        FmLiveDetail fragment = new FmLiveDetail();
        Bundle args = new Bundle();
        args.putSerializable("live", live);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLive = (Live) getArguments().getSerializable("live");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_movie_detail, null);
        initView();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        return mRootView;
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void initView() {

        llScenes = mRootView.findViewById(R.id.ll_scenes);
        mRootView.findViewById(R.id.iv_pause).setVisibility(View.GONE);
        mIvScenesStop = mRootView.findViewById(R.id.iv_stop);

        mIvCover = mRootView.findViewById(R.id.iv_cover);
        mTvName = mRootView.findViewById(R.id.tv_name);
        mTvPlay = mRootView.findViewById(R.id.tv_play_detail);
        mTvPlay.setOnClickListener(this);
        mTvReleaseTime = mRootView.findViewById(R.id.tv_release_time);
//        mTvDerector = mRootView.findViewById(R.id.tv_director);
        mTvDuration = mRootView.findViewById(R.id.tv_duration);
//        mTvTarring = mRootView.findViewById(R.id.tv_tarring);
        mTvArea = mRootView.findViewById(R.id.tv_area);
        mTvIntroducttion = mRootView.findViewById(R.id.tv_brief_introduction);
        mTvPlay.setBackgroundResource(R.drawable.selector_play_live);
        mTvToScene = mRootView.findViewById(R.id.tv_to_scenes);
        mTvToScene.setOnClickListener(this);

        if (mLive != null) {
            mTvPlay.setEnabled(mLive.live_status == 1);
            mTvPlay.setText(mLive.live_status == 1 ? R.string.play_begin : R.string.not_start);
            llScenes.setVisibility(mLive.live_status == 1 && ProjectorInfo.IS_HAD_PROJECTOR ? View.VISIBLE : View.GONE);
            mTvName.setText(mLive.live_name);
            setInfo(mLive);
        }

//        mIvScenesPause.setOnClickListener(this);
        mIvScenesStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_play_detail:
                if (mLive != null) {
                    VodApplication.getKaraokeController().playLive(mLive);
                    mTvPlay.setText(R.string.playing);
                }
                break;
            case R.id.tv_to_scenes:
                if (mLive != null && !TextUtils.isEmpty(mLive.live_address)) {
                    ProjectorInfo.setCurScenesType(getContext(), ProjectorInfo.ScenesType.LIVE);
                    //屏蔽手机通信
//                    SocketOperationUtil.sendVideoUrl2Projector(0, mLive.live_address);
                    refreshScenesViews();
                } else {
                    Toast.makeText(getContext(), "直播不存在...", Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.iv_pause:
//                boolean isPlaying = ProjectorInfo.getProjectorPlaying();
//                if (isPlaying) {
//                    SocketOperationUtil.sendPause2Projector();
//                } else {
//                    SocketOperationUtil.sendStart2Projector();
//                }
//                refreshScenesViews();
//                break;
            case R.id.iv_stop:
                ProjectorInfo.setCurScenesType(getContext(), ProjectorInfo.ScenesType.STAR);
//                mIvScenesPause.setVisibility(View.GONE);
                mIvScenesStop.setVisibility(View.GONE);
                mTvToScene.setEnabled(true);
                mTvToScene.setText(R.string.play_to_scenes);

                break;
        }
    }

    private boolean isIsProjectPlayingMovie() {
        String projectorPlayingUrl = ProjectorInfo.getProjectorPlayingUrl();
        return mLive != null && !TextUtils.isEmpty(projectorPlayingUrl) && projectorPlayingUrl.endsWith(mLive.live_address);
    }

    private void refreshScenesViews() {
        if (isIsProjectPlayingMovie()) {
//            mIvScenesPause.setVisibility(View.VISIBLE);
            mIvScenesStop.setVisibility(View.VISIBLE);
            mTvToScene.setEnabled(false);
            mTvToScene.setText(R.string.is_scenes_playing);
        } else {
//            mIvScenesPause.setVisibility(View.GONE);
            mIvScenesStop.setVisibility(View.GONE);
            mTvToScene.setEnabled(true);
            mTvToScene.setText(R.string.play_to_scenes);
        }
//        boolean isPlaying = ProjectorInfo.getProjectorPlaying();
//        mIvScenesPause.setImageResource(isPlaying ? R.drawable.selector_scenes_pause : R.drawable.selector_scenes_play);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        if (event.id == EventBusId.Id.CHOOSE_SONG_CHANGED) {
            RoomSongItem topSong = ChooseSongs.getInstance().getFirstSong();
            if (topSong != null && mLive != null && topSong.songCode.equals(mLive.id)) {
                mTvPlay.setEnabled(false);
                mTvPlay.setText(R.string.playing);
            } else {
                mTvPlay.setEnabled(true);
                mTvPlay.setText(R.string.play_begin);
            }
        }
    }

    private void setInfo(Live detail) {
        mRootView.findViewById(R.id.ll_movie_content).setVisibility(View.VISIBLE);
        mTvReleaseTime.setText(getString(R.string.live_begin_x, mLive.start_time));
        mTvDuration.setText(getString(R.string.live_end_x, detail.end_time));
        mTvArea.setText(getString(R.string.type_x, getLiveType(detail.live_type)));
        mTvIntroducttion.setText("          " + detail.description);

        if (!TextUtils.isEmpty(mLive.image)) {
//            Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.TRANSPARENT).borderWidthDp(0)
//                    .cornerRadiusDp(5).oval(false).build();
            RoundedCorners roundedCorners = new RoundedCorners(5);
            Glide.with(this).load(ServerFileUtil.getImageUrl(mLive.image)).override(270, 390)
                    .centerCrop().placeholder(R.drawable.live_default).apply(RequestOptions.bitmapTransform(roundedCorners))
                    .skipMemoryCache(false).into(mIvCover);
        } else {
            mIvCover.setImageResource(R.drawable.live_default);
        }

        RoomSongItem topSong = ChooseSongs.getInstance().getFirstSong();
        if (topSong != null && mLive != null && topSong.songCode.equals(mLive.id)) {
            mTvPlay.setEnabled(false);
            mTvPlay.setText(R.string.playing);
        } else if (mLive != null) {
            mTvPlay.setEnabled(mLive.live_status == 1);
            mTvPlay.setText(mLive.live_status == 1 ? R.string.play_begin : R.string.not_start);
        }
        if (ProjectorInfo.IS_HAD_PROJECTOR) {
            refreshScenesViews();
        }
    }

    private String getLiveType(String type) {
        //直播类型，0有线电视，1网络直播，2大厅转播
        String str = "";
        switch (type) {
            case "0":
                str = "有线电视";
                break;
            case "1":
                str = "网络直播";
                break;
            case "2":
                str = "大厅转播";
                break;
        }
        return str;
    }
}
