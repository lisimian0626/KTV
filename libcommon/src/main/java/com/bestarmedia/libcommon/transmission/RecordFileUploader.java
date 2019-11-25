package com.bestarmedia.libcommon.transmission;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.FileUploadListener;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.vod.Material;
import com.bestarmedia.libcommon.model.vod.MediaRecordRequestBody;
import com.bestarmedia.libcommon.model.vod.NodeMediaRecord;
import com.bestarmedia.libcommon.model.vod.NodeMediaRecordV4;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.util.Logger;

import java.io.File;

public class RecordFileUploader implements FileUploadListener, HttpRequestListener {

    private Context mContext;

    private UserBase userBase;

    private RoomSongItem mRoomSongItem;

    private OnRecordFileUploadListener mOnRecordFileUploadListener;

    private int mRequestTimes;

    private String mMaterialId;

    private Handler handler = new Handler();

    public RecordFileUploader(Context context) {
        mContext = context;
    }

    public void uploadRecordFile(File file, UserBase userBase, RoomSongItem songItem) {
        this.mRoomSongItem = songItem;
        this.userBase = userBase;
        if (mRoomSongItem != null && userBase != null) {
            FileUploader fileUploader = new FileUploader();
            fileUploader.setFileUploadListener(this);
            fileUploader.upload(file, RequestMethod.NODE_MATERIAL);
        } else {
            Logger.d(getClass().getSimpleName(), "uploadRecordFile mRoomSongItem is null || userBase is null !!!");
        }
    }

    private void updateFile2Cloud() {
        if (!TextUtils.isEmpty(mMaterialId)) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_MEDIA_RECORD);
            r.setConvert2Class(NodeMediaRecordV4.class);
            NodeMediaRecord nodeMediaRecord = new NodeMediaRecord();
            nodeMediaRecord.material_id = mMaterialId;
            nodeMediaRecord.user_id = userBase.id;
            nodeMediaRecord.song_code = mRoomSongItem.uuid;
            nodeMediaRecord.song_simple_name = mRoomSongItem.simpName;
            nodeMediaRecord.singer_id = mRoomSongItem.singerId;
            nodeMediaRecord.score = mRoomSongItem.score;
            r.postJson(nodeMediaRecord.toString());
        }
    }

    private void checkUploadCloudStatus() {
        if (!TextUtils.isEmpty(mMaterialId)) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_MEDIA_RECORD_UPLOAD_STATUS);
            r.setConvert2Class(NodeMediaRecordV4.class);
            r.addParam("material_id", mMaterialId);
            r.get();
            mRequestTimes++;
        }
    }

    private void createRecordLog(String cloudFileUrl) {
        HttpRequestV4 r = initRequest(RequestMethod.NODE_MEDIA_RECORD_LOG);
        r.setConvert2Class(NodeMediaRecordV4.class);
        MediaRecordRequestBody body = new MediaRecordRequestBody();
        body.user_id = userBase.id;
        body.ktv_net_code = VodConfigData.getInstance().getRoomCode();
        body.room_code = VodConfigData.getInstance().getRoomCode();
        body.song_id = mRoomSongItem.songCode;
        body.simple_song_name = mRoomSongItem.simpName;
        body.singer_name = mRoomSongItem.singerName;
        body.song_version = mRoomSongItem.songVersion;
        body.record_file_path = cloudFileUrl;
        body.score = mRoomSongItem.score;
        r.postJson(body.toString());
    }

    private HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext.getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onUploadStart(File file) {
        Logger.d(getClass().getSimpleName(), "onUploadStart >>>>>>>>>>>>>>>>>>>> ");
        if (mOnRecordFileUploadListener != null)
            mOnRecordFileUploadListener.onRecordFileUploadStart();
    }

    @Override
    public void onUploading(File file, float progress) {
        Logger.d(getClass().getSimpleName(), "onUploading >>>>>>>>>>>>>>>>>>>> ");
    }

    @Override
    public void onUploadCompletion(File file, Material material) {
        Logger.d(getClass().getSimpleName(), "onUploadCompletion >>>>>>>>>>>>>>>>>>>> ");
        if (material != null && !TextUtils.isEmpty(material.id)) {
            mMaterialId = material.id;
            updateFile2Cloud();
        }
    }

    @Override
    public void onUploadFailure(File file, String errInfo) {
        Logger.d(getClass().getSimpleName(), "onUploadFailure >>>>>>>>>>>>>>>>>>>> errInfo:" + errInfo);
        if (mOnRecordFileUploadListener != null)
            mOnRecordFileUploadListener.onRecordFileUploadFail(mRoomSongItem, userBase, errInfo);
    }

    @Override
    public void onStart(String method) {
        Logger.d(getClass().getSimpleName(), "onStart >>>>>>>>>>>>>>>>>>>> ");
    }

    @Override
    public void onSuccess(String method, Object object) {
        Logger.d(getClass().getSimpleName(), "onSuccess >>>>>>>>>>>>>>>>>>>> method:" + method);
        if (RequestMethod.NODE_MEDIA_RECORD_UPLOAD_STATUS.equalsIgnoreCase(method)) {//上传到云端进度查询
            NodeMediaRecordV4 uploadStatus;
            if (object instanceof NodeMediaRecordV4 && (uploadStatus = (NodeMediaRecordV4) object).mediaRecord != null
                    && !TextUtils.isEmpty(uploadStatus.mediaRecord.cloud_file_path)) {
                Logger.d(getClass().getSimpleName(), "onSuccess create log by media-record/upload-status >>>>>>>>> ");
                createRecordLog(uploadStatus.mediaRecord.cloud_file_path);
            } else {
                checkUploadCloudStatusAfter5s();
            }
        } else if (RequestMethod.NODE_MEDIA_RECORD.equalsIgnoreCase(method)) {
            NodeMediaRecordV4 nodeMediaRecordV4;
            if (object instanceof NodeMediaRecordV4 && (nodeMediaRecordV4 = (NodeMediaRecordV4) object).mediaRecord != null) {
                Logger.d(getClass().getSimpleName(), "onSuccess create log by media-record >>>>>>>>> ");
                if (!TextUtils.isEmpty(nodeMediaRecordV4.mediaRecord.cloud_file_path))
                    createRecordLog(nodeMediaRecordV4.mediaRecord.cloud_file_path);
                else {
                    checkUploadCloudStatusAfter5s();
                }
            } else {
                Logger.d(getClass().getSimpleName(), "onSuccess 录音上传返回model无法解析  >>>>>>>>> ");
                if (mOnRecordFileUploadListener != null)
                    mOnRecordFileUploadListener.onRecordFileUploadFail(mRoomSongItem, userBase, "返回数据不正确！");
            }
        } else if (RequestMethod.NODE_MEDIA_RECORD_LOG.equalsIgnoreCase(method)) {
            Logger.d(getClass().getSimpleName(), "onSuccess media-record/record-log  >>>>>>>>> ");
            if (mOnRecordFileUploadListener != null)
                mOnRecordFileUploadListener.onRecordFileUploaded(mRoomSongItem, userBase, object.toString());
        }
    }

    @Override
    public void onError(String method, String error) {

    }

    private void checkUploadCloudStatusAfter5s() {
        if (mRequestTimes <= 20)
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkUploadCloudStatus();
                }
            }, 5000);
    }

    @Override
    public void onFailed(String method, Object object) {
        Logger.d(getClass().getSimpleName(), "onFailed >>>>>>>>>>>>>>>>>>>> method:" + method);
        if (RequestMethod.NODE_MEDIA_RECORD_UPLOAD_STATUS.equalsIgnoreCase(method)) {
            checkUploadCloudStatusAfter5s();
        } else if (RequestMethod.NODE_MEDIA_RECORD.equalsIgnoreCase(method)) {
            String error = "";
            if (object instanceof BaseModelV4) {
                BaseModelV4 baseModelV4 = (BaseModelV4) object;
                error = baseModelV4.tips;
            } else if (object instanceof String) {
                error = object.toString();
            }
            if (!TextUtils.isEmpty(error) && error.contains("网络超时")) {
                checkUploadCloudStatus();
            } else {
                if (mOnRecordFileUploadListener != null)
                    mOnRecordFileUploadListener.onRecordFileUploadFail(mRoomSongItem, userBase, error);
            }
        } else if (RequestMethod.NODE_MEDIA_RECORD_LOG.equalsIgnoreCase(method)) {
            String error = "";
            if (object instanceof BaseModelV4) {
                BaseModelV4 baseModelV4 = (BaseModelV4) object;
                error = baseModelV4.tips;
            } else if (object instanceof String) {
                error = object.toString();
            }
            if (mOnRecordFileUploadListener != null)
                mOnRecordFileUploadListener.onRecordFileUploadFail(mRoomSongItem, userBase, error);
        }
    }

    public void setOnRecordFileUploadListener(OnRecordFileUploadListener listener) {
        this.mOnRecordFileUploadListener = listener;
    }

    public interface OnRecordFileUploadListener {
        void onRecordFileUploadStart();

        void onRecordFileUploaded(RoomSongItem song, UserBase userBase, String fileUrl);

        void onRecordFileUploadFail(RoomSongItem song, UserBase userBase, String error);
    }
}
