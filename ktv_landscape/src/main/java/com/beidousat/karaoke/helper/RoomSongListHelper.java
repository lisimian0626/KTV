package com.beidousat.karaoke.helper;

import android.text.TextUtils;
import android.util.Log;

import com.beidousat.karaoke.VodApplication;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.v4.Song;
import com.bestarmedia.libcommon.model.vod.AddSongErrorCloud;
import com.bestarmedia.libcommon.model.vod.AddSongErrorPay;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.model.vod.RoomSongList;
import com.bestarmedia.libcommon.model.vod.RoomSongRequestBody;
import com.bestarmedia.libcommon.util.GsonUtil;
import com.bestarmedia.liblame.AudioRecordFileUtil;

import java.io.File;
import java.util.List;

/**
 * Created by J Wong on 2018/8/24.
 */

public class RoomSongListHelper implements HttpRequestListener {

    private OnRoomSongListChangedListener mOnRoomSongListChangedListener;
    private final static String TAG = "RoomSongListHelper";
    private volatile static RoomSongListHelper mRoomSongListHelper;

    public static RoomSongListHelper getInstance() {
        if (mRoomSongListHelper == null) {
            synchronized (RoomSongListHelper.class) {
                if (mRoomSongListHelper == null)
                    mRoomSongListHelper = new RoomSongListHelper();
            }
        }
        return mRoomSongListHelper;
    }

    private RoomSongListHelper() {
    }

    /**
     * 添加歌曲到已点歌曲列表
     */
    public void addSong2Choose(String songId, int type, int mvId, String mvGalleries, UserBase userBase, boolean isPriority, String uuid) {
        if (NodeRoomInfo.getInstance().getNodeRoom() != null) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_ROOM_SONG_LIST);
            r.setConvert2Class(RoomSongList.class);
            RoomSongRequestBody body = new RoomSongRequestBody();
            body.session = NodeRoomInfo.getInstance().getRoomSession();
            body.ktv_net_code = VodConfigData.getInstance().getKtvNetCode();
            body.ktv_name = NodeRoomInfo.getInstance().getKtvName();
            body.room_code = VodConfigData.getInstance().getRoomCode();
            body.type = type;
            body.is_priority = isPriority ? 1 : 0;
            body.song_id = songId;
            body.mv_id = mvId;
            body.mv_galleries = mvGalleries;
            body.user_id = userBase != null ? userBase.id : 0;
            body.user_name = userBase != null ? userBase.name : "";
            body.user_avatar = userBase != null ? userBase.avatar : "";
            if (!TextUtils.isEmpty(uuid))
                body.uuid = uuid;
            r.postJson(body.toString());
        }
    }

    /**
     * 把歌曲从已点列表移动到已唱列表
     */
    public void moveChoose2Sung(String uuid, int score, int duration, int playTime, String recordFile) {
        if (NodeRoomInfo.getInstance().getNodeRoom() != null) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_ROOM_SONG_LIST);
            r.setConvert2Class(RoomSongList.class);
            RoomSongRequestBody body = new RoomSongRequestBody();
            body.uuid = uuid;
            body.status = 1;
            body.score = score;
            body.duration = duration;
            body.play_time = playTime;
            body.session = NodeRoomInfo.getInstance().getRoomSession();
            body.room_code = VodConfigData.getInstance().getRoomCode();
            File fileRecord = new File(AudioRecordFileUtil.getRecordFilePath(recordFile, false));
            Log.i(TAG, "把歌曲从已点移动到已唱 uuid：" + uuid + " 评分：" + score + " 时长：" + duration + " 播放时长：" + playTime
                    + " 录音文件：" + fileRecord.getAbsolutePath() + " 文件大小：" + fileRecord.length() / 1024 + "KB");
            boolean haveRecordFile = fileRecord.exists() && fileRecord.length() > 1024;
            body.record_file = haveRecordFile ? recordFile : "";
            r.putJson(body.toString());
        }
    }

    /**
     * 读取已点已唱歌单列表
     */
    public void getChooseSungSongList() {
        if (NodeRoomInfo.getInstance().getNodeRoom() != null) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_ROOM_SONG_LIST);
            r.setConvert2Class(RoomSongList.class);
            r.addParam("session", NodeRoomInfo.getInstance().getRoomSession());
            r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
            r.get();
        }
    }


    /**
     * 打乱已点歌单
     */
    public void shuffleChooseSongList() {
        if (NodeRoomInfo.getInstance().getNodeRoom() != null) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_ROOM_SONG_LIST_SHUFFLE);
            r.setConvert2Class(RoomSongList.class);
            r.addParam("session", NodeRoomInfo.getInstance().getRoomSession());
            r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
            r.addParam("status", String.valueOf(0));
            r.get();
        }
    }

    /**
     * 把歌曲从已点列表中删除
     */
    public void deleteChooseSong(RoomSongItem song) {
        if (NodeRoomInfo.getInstance().getNodeRoom() != null) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_ROOM_SONG_LIST + "/" + song.uuid);
            r.setConvert2Class(RoomSongList.class);
//            r.addParam("session", NodeRoomInfo.getInstance().getRoomSession());
//            r.addParam("room_code",  VodConfigData.getInstance().getRoomCode());
//            r.addParam("id", song.uuid);
            r.delete();
        }
    }

    /**
     * 记录开始播放时间
     */
    void recordPlayStart(String uuid) {
        HttpRequestV4 r = initRequest(RequestMethod.NODE_ROOM_SONG_RECORD_PLAY_START + "/" + uuid);
        r.setConvert2Class(RoomSongItem.class);
        r.get();
    }

    private HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(VodApplication.getVodApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onStart(String method) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        if (object != null) {
            if (object instanceof RoomSongList) {//返回的是歌曲列表
                RoomSongList roomSongList = (RoomSongList) object;
                if (mOnRoomSongListChangedListener != null && roomSongList.songs != null) {//roomSongList.songs==null时说明接口没有返回已点列表，列表为空时应该返回空数组
                    mOnRoomSongListChangedListener.onChooseListChanged(roomSongList.songs);
                }
                if (mOnRoomSongListChangedListener != null && roomSongList.sung != null) {//roomSongList.sung ==null 的情况说明接口并没有返回已唱列表，不需要更新；列表为空接口返回的是空数组
                    mOnRoomSongListChangedListener.onSungListChanged(roomSongList.sung);
                }
            } else {
                Log.d(TAG, "onSuccess method:" + method + " instanceof not RoomSongList !!");
                if (object instanceof BaseModelV4) {
                    BaseModelV4 baseModelV4 = (BaseModelV4) object;
                    if (22101012 == baseModelV4.code) {
                        //提示付费
                        if (mOnRoomSongListChangedListener != null) {
                            String songId = "";
                            try {
                                AddSongErrorPay addSongError = (AddSongErrorPay) GsonUtil.convert2Object(baseModelV4.data, AddSongErrorPay.class);
                                songId = addSongError.songId;
                            } catch (Exception e) {
                                Log.w(TAG, "付费歌曲json解析出错了", e);
                            }
                            Log.e(TAG, "付费歌曲点点歌失败！songId:" + songId);
                            mOnRoomSongListChangedListener.onSongNeedToPay(songId);
                        }
                    } else if (20131024 == baseModelV4.code) {
                        Log.d(TAG, "云歌曲 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        AddSongErrorCloud song = (AddSongErrorCloud) GsonUtil.convert2Object(baseModelV4.data, AddSongErrorCloud.class);
                        Log.d(TAG, song != null && song.song != null ? ("song_id:" + song.song.id + " simp_name:"
                                + song.song.songName + " file:" + song.song.mediaUrl) : "云歌曲 is null >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        if (song != null && song.song != null && mOnRoomSongListChangedListener != null) {
                            song.song.isCloud = 1;
                            mOnRoomSongListChangedListener.onCloudSong(song.song);
                        }
                    } else {
                        Log.e(TAG, "点歌失败 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        if (mOnRoomSongListChangedListener != null) {
                            mOnRoomSongListChangedListener.onChooseSongFailTip(baseModelV4.tips);
                        }
                    }
                }
            }
        } else {
            if (method.startsWith(RequestMethod.NODE_ROOM_SONG_LIST + "/")) {//delete删除时返回body为空
                getChooseSungSongList();
            } else {
                Log.e(TAG, "onSuccess method:" + method + " object is null !!");
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (obj instanceof BaseModelV4) {
            BaseModelV4 baseModelV4 = (BaseModelV4) obj;
            if (22101012 == baseModelV4.code) {
                //提示付费
                if (mOnRoomSongListChangedListener != null) {
                    String songId = "";
                    try {
                        AddSongErrorPay addSongError = (AddSongErrorPay) GsonUtil.convert2Object(baseModelV4.data, AddSongErrorPay.class);
                        songId = addSongError.songId;
                    } catch (Exception e) {
                        Log.w(TAG, "付费歌曲json解析出错了", e);
                    }
                    Log.d(TAG, "onFailed songId:" + songId);
                    mOnRoomSongListChangedListener.onSongNeedToPay(songId);
                }
            } else if (20131024 == baseModelV4.code) {
                Log.d(TAG, "云歌曲 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                AddSongErrorCloud song = (AddSongErrorCloud) GsonUtil.convert2Object(baseModelV4.data, AddSongErrorCloud.class);
                Log.d(TAG, song != null && song.song != null ? ("song_id:" + song.song.id + " simp_name:"
                        + song.song.songName + " file:" + song.song.mediaUrl) : "云歌曲 is null >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                if (song != null && song.song != null && mOnRoomSongListChangedListener != null) {
                    song.song.isCloud = 1;
                    mOnRoomSongListChangedListener.onCloudSong(song.song);
                }
            } else if (20151013 == baseModelV4.code) {
                if (mOnRoomSongListChangedListener != null) {
                    mOnRoomSongListChangedListener.onChooseSongFailTip(baseModelV4.tips);
                }
            } else {
                Log.e(TAG, "点歌失败 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                if (mOnRoomSongListChangedListener != null) {
                    mOnRoomSongListChangedListener.onChooseSongFailTip(baseModelV4.tips);
                }
            }
        } else if (obj instanceof String) {
            Log.e(TAG, "onFailed error:" + obj.toString());
            if (mOnRoomSongListChangedListener != null) {
                mOnRoomSongListChangedListener.onChooseSongFailTip(obj.toString());
            }
        }
    }

    @Override
    public void onError(String method, String error) {
        if (mOnRoomSongListChangedListener != null) {
            mOnRoomSongListChangedListener.onChooseSongFailTip(error);
        }
    }

    public void setOnRoomSongListChangedListener(OnRoomSongListChangedListener listener) {
        mOnRoomSongListChangedListener = listener;
    }

    public interface OnRoomSongListChangedListener {

        void onChooseListChanged(List<RoomSongItem> curChooseSongs);

        void onSungListChanged(List<RoomSongItem> curSungSongs);

        void onSongNeedToPay(String songId);

        void onCloudSong(Song songDetail);

        void onChooseSongFailTip(String tip);

    }
}
