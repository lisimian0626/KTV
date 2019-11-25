package com.beidousat.karaoke.data;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.helper.RoomSongListHelper;
import com.beidousat.karaoke.im.DeviceCommunicateHelper;
import com.beidousat.karaoke.im.VodCommunicateHelper;
import com.beidousat.karaoke.ui.dialog.PromptDialogBig;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.bestarmedia.libcommon.ad.VideoAdManager;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.interf.CloudSongDownloadListener;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.model.dto.PlayItem;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.v4.Song;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.vod.Live;
import com.bestarmedia.libcommon.model.vod.Movie;
import com.bestarmedia.libcommon.model.vod.MvInfo;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.transmission.CloudSongDownloadHelper;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libcommon.util.MatcherUtil;
import com.bestarmedia.libcommon.util.UUIDUtil;
import com.bestarmedia.libnetty.netty.NotificationMessageData;
import com.bestarmedia.proto.node.VideoMessageRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/10/10 11:10.
 */
public class ChooseSongs implements RoomSongListHelper.OnRoomSongListChangedListener, CloudSongDownloadListener, VideoAdManager.OnAdVideoListener {

    private final static String TAG = "ChooseSongs";
    private volatile static ChooseSongs mSelectedSongs;
    private List<RoomSongItem> chooseItemList = new ArrayList<>();
    private List<RoomSongItem> sungItemList = new ArrayList<>();
    private PromptDialogSmall mPromptDialog;
    private PromptDialogBig mDlgTipRemove;
    private Map<String, String> songIndex = new HashMap<>();
    private SparseArray<String> mvIndex = new SparseArray<>();
    private final static int MAX_SIZE = 99;
    private PlayItem playingItem;

    public static ChooseSongs getInstance() {
        if (mSelectedSongs == null) {
            synchronized (ChooseSongs.class) {
                if (mSelectedSongs == null) {
                    mSelectedSongs = new ChooseSongs();
                }
            }
        }
        return mSelectedSongs;
    }

    private ChooseSongs() {
        RoomSongListHelper.getInstance().setOnRoomSongListChangedListener(this);
        CloudSongDownloadHelper.getInstance(VodApplication.getVodApplicationContext()).addCloudSongDownloadListener(this);
        VideoAdManager.getInstance(VodApplication.getVodApplicationContext()).addOnAdVideoListener(this);
    }


    public void init() {
        Log.d(TAG, "init >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        chooseItemList.clear();
        sungItemList.clear();
        playingItem = null;
        getChooseSungListFromServer();
    }

    public void getChooseSungListFromServer() {
        RoomSongListHelper.getInstance().getChooseSungSongList();
    }

    private void tipMessage(int resId) {
        tipMessage(VodApplication.getVodApplicationContext().getString(resId));
    }

    private void tipMessage(String tip) {
        try {
            if (mPromptDialog == null || !mPromptDialog.isShowing()) {
                mPromptDialog = new PromptDialogSmall(VodApplication.getVodApplication().getStackTopActivity());
                mPromptDialog.setTitle(R.string.prompt);
                mPromptDialog.setMessage(tip);
                mPromptDialog.setOkButton(true, VodApplication.getVodApplicationContext().getString(R.string.close), null);
                mPromptDialog.show();
            }
        } catch (Exception e) {
            Log.w(TAG, "tipMessage ex:", e);
        }
    }

    private void dealContainSong(SongSimple song, boolean isTop, boolean isTip, int mvId, String galleries) {
        if (chooseItemList != null && chooseItemList.size() > 0) {
            if (isTop) {//优先从列表头开始
                for (int i = 0; i < chooseItemList.size(); i++) {
                    RoomSongItem songContain = chooseItemList.get(i);
                    if (songContain.songCode.equals(song.id) && songContain.mvId == mvId) {
                        if (i > 0) {
                            if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {//主屏
                                RoomSongItem remove = chooseItemList.remove(i);
                                addSong2List(true, remove.toSongSimple(), remove.type, remove.mvId, remove.galleries, null, songContain.uuid);
                            } else {//副屏
                                DeviceCommunicateHelper.sendPlayerOperate2Main(19, songContain.uuid);
                            }
                        }
                        return;
                    }
                }
            } else {
                for (int i = chooseItemList.size() - 1; i > -1; i--) {
                    RoomSongItem songContain = chooseItemList.get(i);
                    if (songContain.songCode.equals(song.id) && songContain.mvId == mvId) {
                        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {//主屏
                            if (isTip) {
                                tipRemoveSong(i, songContain.toSongSimple(), false, mvId, galleries);
                            } else {
                                addSong2List(false, song, songContain.type, songContain.mvId, songContain.galleries, null, songContain.uuid);
                            }
                        } else {
                            if (isTip) {
                                tipRemoveSong(i, songContain.toSongSimple(), false, mvId, galleries);
                            } else {
                                DeviceCommunicateHelper.sendPlayerOperate2Main(17, songContain.songCode);
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    public void move2TopByUUIDFormChoose(String uuid) {
        Log.d(TAG, "move2TopByUUIDFormChoose uuid:" + uuid);
        try {
            if (chooseItemList != null && chooseItemList.size() > 0) {
                int position = -1;
                for (int i = 0; i < chooseItemList.size(); i++) {
                    if (uuid.equalsIgnoreCase(chooseItemList.get(i).uuid)) {
                        position = i;
                        break;
                    }
                }
                if (position > 1) {//第3首以上优先才有意义
                    if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                        RoomSongItem song = chooseItemList.remove(position);
                        addSong2List(true, song.toSongSimple(), song.type, song.mvId, song.galleries, null, song.uuid);
                    } else {
                        DeviceCommunicateHelper.sendPlayerOperate2Main(19, uuid);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void removeByPosition(int position) {
        try {
            if (chooseItemList != null && chooseItemList.size() > position) {
                if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                    RoomSongItem song = chooseItemList.remove(position);
                    RoomSongListHelper.getInstance().deleteChooseSong(song);
                } else {
                    if (chooseItemList.size() > position) {
                        DeviceCommunicateHelper.sendPlayerOperate2Main(20, chooseItemList.get(position).uuid);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeSongByUUID(String uuid) {
        Log.d(TAG, "removeSongByUUID uuid:" + uuid);
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            try {
                if (chooseItemList != null && chooseItemList.size() > 0) {
                    for (int i = chooseItemList.size() - 1; i > 0; i--) {
                        RoomSongItem song = chooseItemList.get(i);
                        if (song.uuid.equals(uuid)) {
                            removeByPosition(i);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "removeSongById ex:", e);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(20, uuid);
        }
    }


    public boolean addSong(final SongSimple songItem, final boolean isTop, boolean isTip, UserBase userBase, int mvId, String mvGalleries) {
        try {
            if (!NodeRoomInfo.getInstance().canChooseSong()) {//未开房,只能操作服务
                tipMessage(R.string.can_not_select_song);
                return false;
            }
            if ((mvId <= 0 && songIndex.containsKey(songItem.id)) || (mvId > 0 && mvIndex.get(mvId) != null)) {//已存在列表
                dealContainSong(songItem, isTop, isTip, mvId, mvGalleries);
                return true;
            } else {//未存在
                if (chooseItemList != null && chooseItemList.size() >= MAX_SIZE) {
                    tipMessage(R.string.choose_max);
                    return false;
                }
            }
            if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                addSong2List(isTop, songItem, 0, mvId, mvGalleries, userBase, "");
                return true;
            } else {
                if (mvId > 0) {//点MV
                    DeviceCommunicateHelper.sendPlayerOperate2Main(isTop ? 16 : 15, mvId);
                } else {//点歌
                    DeviceCommunicateHelper.sendPlayerOperate2Main(isTop ? 18 : 17, songItem.id);
                }
                return true;
            }
        } catch (Exception e) {
            Log.w(TAG, "addSong", e);
        }
        return true;
    }

    private void addSong2List(boolean isTop, SongSimple songInfo, int type, int mvId, String mvGalleries, UserBase userBase, String uuid) {
        if (chooseItemList.size() <= 0) {//
            RoomSongListHelper.getInstance().addSong2Choose(String.valueOf(songInfo.id), type, mvId, mvGalleries, userBase, false, uuid);
        } else {
            RoomSongListHelper.getInstance().addSong2Choose(String.valueOf(songInfo.id), type, mvId, mvGalleries, userBase, isTop, uuid);
        }
    }

    private void tipRemoveSong(final int position, final SongSimple song, final boolean isTop, final int mvId, final String galleries) {
        if (mDlgTipRemove == null || !mDlgTipRemove.isShowing()) {
            mDlgTipRemove = new PromptDialogBig(VodApplication.getVodApplication().getStackTopActivity());
            mDlgTipRemove.setTitle(R.string.prompt);
            mDlgTipRemove.setMessage(position > 0 ? VodApplication.getVodApplicationContext().getString(R.string.song_already_in_selected_list) : VodApplication.getVodApplicationContext().getString(R.string.this_song_is_playing));
            if (chooseItemList != null && chooseItemList.size() < MAX_SIZE) {
                mDlgTipRemove.setOkButton(true, VodApplication.getVodApplicationContext().getString(R.string.sing_again), v -> {
                    if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                        addSong2List(isTop, song, 0, mvId, galleries, null, "");
                    } else {
                        DeviceCommunicateHelper.sendPlayerOperate2Main(isTop ? 18 : 17, song.id);
                    }
                });
            }
            if (position > 0) {
                mDlgTipRemove.setCancleButton(true, VodApplication.getVodApplicationContext().getString(R.string.remove), v -> removeByPosition(position));
            } else {
                mDlgTipRemove.setCancleButton(true, VodApplication.getVodApplicationContext().getString(R.string.close), null);
            }
            mDlgTipRemove.show();
        }
    }

    public void cleanData() {
        chooseItemList.clear();
        Log.d(TAG, "cleanData cleanPlaying>>>>>>>>>>>>>>>>>>>>>>>>>");
        init();
    }

    private int getChooseSize() {
        try {
            return chooseItemList.size();
        } catch (Exception e) {
            Log.w(TAG, "getChooseSize ex:", e);
        }
        return 0;
    }

    public RoomSongItem getFirstSong() {
        return getChooseSize() > 0 ? chooseItemList.get(0) : null;
    }

    public RoomSongItem getSecSong() {
        return getChooseSize() > 1 ? chooseItemList.get(1) : null;
    }

    public List<RoomSongItem> getSongs() {
        return chooseItemList;
    }


    private void broadcastSungChanged() {
        EventBusUtil.postSticky(EventBusId.Id.SUNG_SONG_CHANGED, "");
        broadcastSung();
    }

    public List<RoomSongItem> getHasSungSons() {
        return sungItemList;
    }

    public void moveChoose2Sung(int duration, int playTime) {
        try {
            if (playingItem != null && playingItem.play != null && playingItem.play.type == -3) {
                if (!NotificationMessageData.getInstance().getVideoQueueAfter().isEmpty()) {//插播视频
                    VideoMessageRequest pushVideo = NotificationMessageData.getInstance().getVideoQueueAfter().remove(0);
                    playVideo(UUIDUtil.getRandomUUID(), pushVideo.getVideo().getUrl(), "插播视频", 80, -3);
                } else {//
                    getChooseSungListFromServer();
                }
            } else {
                RoomSongItem top = getFirstSong();
                if (top != null && !TextUtils.isEmpty(top.uuid) && top.type >= 0) {
                    RoomSongListHelper.getInstance().moveChoose2Sung(top.uuid, top.score, top.duration > 0 ? (int) top.duration : duration,
                            top.current > 0 ? (int) top.current : playTime, top.recordFile);
                } else {//
                    if (!NotificationMessageData.getInstance().getVideoQueueAfter().isEmpty()) {//插播视频
                        Log.d(TAG, "有插播视频 >>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
                        if (playingItem == null || playingItem.play == null || !playingItem.play.songUrl.equalsIgnoreCase(
                                NotificationMessageData.getInstance().getVideoQueueAfter().get(0).getVideo().getUrl())) {
                            VideoMessageRequest pushVideo = NotificationMessageData.getInstance().getVideoQueueAfter().remove(0);
                            playVideo(UUIDUtil.getRandomUUID(), pushVideo.getVideo().getUrl(), "插播视频", 80, -3);
                        } else {
                            Log.d(TAG, "此插播视频正在播放 >>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
                        }
                    } else {
                        playPublicVideo();
                    }
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "将已点歌曲变为已唱发生异常：", e);
        }
    }

    /**
     * 获取并播放公播
     */
    public void playPublicVideo() {
        VideoAdManager.getInstance(VodApplication.getVodApplicationContext()).getPublicVideo();
    }


    public void broadcastSung() {
        VodCommunicateHelper.broadcastSungList(sungItemList);
        DeviceCommunicateHelper.broadcastSungChanged(sungItemList);
    }


    public void broadcastChoose() {
        VodCommunicateHelper.broadcastChooseList(chooseItemList);
        DeviceCommunicateHelper.broadcastChooseChanged(chooseItemList);
    }

    private void syncChoose() {
        try {
            if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                broadcastChoose();
            }
        } catch (Exception e) {
            Log.w(TAG, "syncChoose ex :", e);
        }
    }

    private void sendChooseChangedMsg(boolean syncChoose) {
        try {
            updateSongIndex();
            EventBusUtil.postSticky(EventBusId.Id.CHOOSE_SONG_CHANGED, chooseItemList.size());
            if (syncChoose)
                syncChoose();
        } catch (Exception e) {
            Log.w(TAG, "sendChooseChangedMsg ex :", e);
        }
    }

    private void updateSongIndex() {
        try {
            songIndex = new HashMap<>();
            mvIndex = new SparseArray<>();
            int songPs = 0;
            for (RoomSongItem info : chooseItemList) {
                String text = "";
                String mvText = "";
                if (songPs == 0) {
                    if (info.mvId > 0) {
                        mvText = VodApplication.getVodApplicationContext().getString(R.string.playing);
                    } else {
                        text = VodApplication.getVodApplicationContext().getString(R.string.playing);
                    }
                } else if (songPs == 1) {
                    if (info.mvId > 0) {
                        mvText = VodApplication.getVodApplicationContext().getString(R.string.next_song);
                    } else {
                        text = VodApplication.getVodApplicationContext().getString(R.string.next_song);
                    }
                } else {
                    if (info.mvId > 0) {
                        mvText = VodApplication.getVodApplicationContext().getString(R.string.priorities_x, (songPs + 1) + "");
                    } else {
                        text = VodApplication.getVodApplicationContext().getString(R.string.priorities_x, (songPs + 1) + "");
                    }
                }

                String preMvText = mvIndex.get(info.mvId);
                if (!TextUtils.isEmpty(preMvText)) {
                    String sort = MatcherUtil.findString(preMvText, "第", "首");
                    if (!TextUtils.isEmpty(sort)) {
                        String strOld = VodApplication.getVodApplicationContext().getString(R.string.priorities_x, sort);
                        String strNew = VodApplication.getVodApplicationContext().getString(R.string.priorities_x, sort + "  " + (songPs + 1));
                        if (preMvText != null)
                            mvText = preMvText.replace(strOld, strNew);
                    } else {
                        mvText = preMvText + "  " + mvText;
                    }
                }

                String preText = songIndex.get(info.songCode);
                if (!TextUtils.isEmpty(preText)) {
                    String sort = MatcherUtil.findString(preText, "第", "首");
                    if (!TextUtils.isEmpty(sort)) {
                        String strOld = VodApplication.getVodApplicationContext().getString(R.string.priorities_x, sort);
                        String strNew = VodApplication.getVodApplicationContext().getString(R.string.priorities_x, sort + "  " + (songPs + 1));
                        text = preText.replace(strOld, strNew);
                    } else {
                        text = preText + "  " + text;
                    }
                }
                if (info.mvId > 0) {
                    mvIndex.put(info.mvId, mvText);
                } else {
                    songIndex.put(info.songCode, text);
                }
                songPs++;
            }
        } catch (Exception e) {
            Log.e(TAG, "歌曲排序发送异常", e);
        }
    }

    public String getDownloadingText(String songId) {
        try {
            SongSimple download = CloudSongDownloadHelper.getSongStatus(songId);
            if (download != null && download.isCloud == 1 && download.downloadStatus != 1) {
                if (download.downloadStatus == 0) {//排队
                    return VodApplication.getVodApplicationContext().getString(R.string.in_list) + (download.downloadSort > 0 ? ("(" + download.downloadSort + ")") : "");
                } else if (download.downloadStatus == 2) {//下载中
                    return VodApplication.getVodApplicationContext().getString(R.string.downloading) + "(" + download.downloadPercent + "%)";
                } else if (download.downloadStatus >= 3) {//失败
                    return VodApplication.getVodApplicationContext().getString(R.string.download_fail) + "：" + download.downloadStatus;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "getDownloadingText ex", e);
        }
        return "";
    }

    public String getSongPriorities(SongSimple song) {
        try {
            String downloadingText = getDownloadingText(song.id + "");
            if (!TextUtils.isEmpty(downloadingText)) {
                return downloadingText;
            }
            if (songIndex.containsKey(song.id)) {
                String text = songIndex.get(song.id);
                if (TextUtils.isEmpty(text)) {
                    return "";
                }
                return text;
            }
        } catch (Exception e) {
            Log.w(TAG, "getSongPriorities ex :" + e.toString());
        }
        return "";
    }

    public String getMvPriorities(MvInfo mv) {
        try {
            String downloadingText = getDownloadingText(mv.id);
            if (!TextUtils.isEmpty(downloadingText)) {
                return downloadingText;
            }
            if (mvIndex.get(mv.mvId) != null) {
                String text = mvIndex.get(mv.mvId);
                if (TextUtils.isEmpty(text)) {
                    return "";
                }
                return text;
            }
        } catch (Exception e) {
            Log.w(TAG, "getMvPriorities ex :" + e.toString());
        }
        return "";
    }

    public boolean addLive(Live detail) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            try {
                if (!NodeRoomInfo.getInstance().canChooseSong()) {
                    tipMessage(R.string.can_not_select_song);
                    return false;
                }
                RoomSongListHelper.getInstance().addSong2Choose(detail.id, 2, 0, "", null, true, "");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "addLive ex:", e);
                return false;
            }
        } else {
            if (!NodeRoomInfo.getInstance().canChooseSong()) {//未开房,只能操作服务
                tipMessage(R.string.can_not_select_song);
                return false;
            }
            //屏蔽
//            SocketOperationUtil.sendChooseLive2Main(detail);
            return true;
        }
    }

    /**
     * 已经无电影播放权，暂不做
     *
     * @param movie 电影
     * @return 插入成功
     */
    public boolean addMovie(Movie movie) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            try {
                if (!NodeRoomInfo.getInstance().canChooseSong()) {//未开房,只能操作服务
                    tipMessage(R.string.can_not_select_song);
                    return false;
                }
                RoomSongListHelper.getInstance().addSong2Choose(movie.ID, 1, 0, "", null, false, "");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "addMovie ex:", e);
                return false;
            }
        } else {
            if (!NodeRoomInfo.getInstance().canChooseSong()) {//未开房,只能操作服务
                tipMessage(R.string.can_not_select_song);
                return false;
            }
            //屏蔽
//            SocketOperationUtil.sendChooseMovie2Main(movie);
            return true;
        }
    }

    public void shuffle() {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            try {
                RoomSongListHelper.getInstance().shuffleChooseSongList();
            } catch (Exception e) {
                Log.e(TAG, "shuffle ex :", e);
            }
        } else {
            DeviceCommunicateHelper.sendPlayerOperate2Main(25, 0);
        }
    }

    public RoomSongItem getSungByRecordFile(String file) {
        if (sungItemList != null && sungItemList.size() > 0)
            for (RoomSongItem song : sungItemList) {
                if (song.recordFile != null && song.recordFile.equals(file)) {
                    return song;
                }
            }
        return null;
    }


    private void downloadCloudSong(Song song) {
        if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
            CloudSongDownloadHelper.getInstance(VodApplication.getVodApplicationContext()).downloadSong(song);
        }
    }


    @Override
    public void onChooseListChanged(List<RoomSongItem> curChooseSongs) {
        if (!NotificationMessageData.getInstance().getVideoQueueAfter().isEmpty()) {//插播视频
            Log.d(TAG, "有插播视频 >>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            if (playingItem == null || playingItem.play == null || !playingItem.play.songUrl.equalsIgnoreCase(
                    NotificationMessageData.getInstance().getVideoQueueAfter().get(0).getVideo().getUrl())) {
                VideoMessageRequest pushVideo = NotificationMessageData.getInstance().getVideoQueueAfter().remove(0);
                playVideo(UUIDUtil.getRandomUUID(), pushVideo.getVideo().getUrl(), "插播视频", 80, -3);
            } else {
                Log.d(TAG, "此插播视频正在播放 >>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            }
        } else {//
            Log.d(TAG, "无插播视频 >>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            if (curChooseSongs != null && curChooseSongs.size() > 0) {//有已点列表
                if (playingItem == null || playingItem.play == null || !playingItem.play.uuid.equalsIgnoreCase(curChooseSongs.get(0).uuid)) {
                    chooseItemList = curChooseSongs;
                    playSong(new PlayItem(chooseItemList.get(0), chooseItemList.size() > 1 ? chooseItemList.get(1) : null));
                } else {
                    Log.i(TAG, "点歌行为或初始化数据，不处理 >>>>>>>>>>>>>");
                }
//                if (chooseItemList != null && chooseItemList.size() > 0 && chooseItemList.get(0).uuid.equalsIgnoreCase(curChooseSongs.get(0).uuid)) {//点歌行为，不处理
//                    Log.i(TAG, "点歌行为或初始化数据，不处理 >>>>>>>>>>>>>");
//                } else {
//                    chooseItemList = curChooseSongs;
//                    playSong(new PlayItem(chooseItemList.get(0), chooseItemList.size() > 1 ? chooseItemList.get(1) : null));
//                }
            } else {//已点列表为空，播放公播或广告
                Log.i(TAG, "已点列表为空，播放公播或广告 >>>>>>>>>>>>>");
                VideoAdManager.getInstance(VodApplication.getVodApplicationContext()).getPublicVideo();
            }
        }
        chooseItemList = curChooseSongs;
        updateSongIndex();
        EventBusUtil.postSticky(EventBusId.Id.CHOOSE_SONG_CHANGED, chooseItemList.size());
        sendChooseChangedMsg(true);
    }

    @Override
    public void onSungListChanged(List<RoomSongItem> curSungSongs) {
        Log.d(TAG, "onSungListChanged >>>>>>>>>>>>>>>>");
        sungItemList = curSungSongs;
        broadcastSungChanged();
    }

    @Override
    public void onSongNeedToPay(String songId) {
        //屏蔽
//        CopyrightFeeDialogHelper.getInstance(Main.mMainActivity).show(songId);
    }

    @Override
    public void onCloudSong(Song song) {
        downloadCloudSong(song);
    }

    @Override
    public void onChooseSongFailTip(String tip) {
        tipMessage(tip);
    }

    @Override
    public void onSongDownloadStatusChanged(String songId, int status) {
        SongSimple item = CloudSongDownloadHelper.getSongStatus(songId);
        if (item != null) {
            if (status == 1) {
                addSong(item, item.isToTop, false, null, 0, "");
            }
            broadcastSongDownloadChanged();
        }
    }

    @Override
    public void onSongDownloadChanged(Map<String, SongSimple> currentDownloadStatus) {
        broadcastSongDownloadChanged();
    }

    private void broadcastSongDownloadChanged() {
        EventBusUtil.postSticky(EventBusId.Id.SONG_DOWNLOAD_CHANGED, null);
        DeviceCommunicateHelper.broadcastSongDownloadStatus(CloudSongDownloadHelper.getInstance(VodApplication.getVodApplicationContext()).getDownloadStatus());
    }

    /**
     * @param id      记录id
     * @param fileUrl 文件url
     * @param name    名称
     * @param volume  初始音量
     * @param type    类型：-3插播视频；-2公益广告；-1贴片广告；
     */
    public void playVideo(String id, String fileUrl, String name, int volume, int type) {
        RoomSongItem item = new RoomSongItem();
        item.songCode = id;
        item.uuid = "";
        item.songUrl = fileUrl;
        item.simpName = name;
        item.volume = volume;
        item.hasSongFile = 1;
        item.audioTrack = 5;
        item.isToTop = true;
        item.type = type;
        playSong(new PlayItem(item, getFirstSong()));
    }

    @Override
    public void onAdModel(ADModel model) {
        playVideo(String.valueOf(model.getId()), model.getAdContent(), VodApplication.getVodApplicationContext().getString(R.string.ad), VodConfigData.getInstance().getBroadcastVolume(), -1);
    }


    private void playSong(PlayItem item) {
        playingItem = item;
        EventBusUtil.postSticky(EventBusId.PlayerId.PLAYER_PLAY_SONG, playingItem);
    }

    public void resetPlayingItem() {
        playingItem = null;
    }
}
