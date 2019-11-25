package com.beidousat.karaoke.helper;

import android.content.Context;
import android.text.TextUtils;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.data.ChooseSongs;
import com.bestarmedia.libcommon.helper.MvSongHelper;
import com.bestarmedia.libcommon.helper.SongDetailHelper;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.v4.Song;
import com.bestarmedia.libcommon.model.vod.MvInfo;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.transmission.RecordFileUploader;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.liblame.AudioRecordFileUtil;
import com.bestarmedia.proto.node.Text;
import com.bestarmedia.proto.vod.MobileMessageBroadcast;
import com.bestarmedia.proto.vod.MobileUser;

import java.io.File;

/**
 * Created by J Wong on 2016/6/30.
 */
public class VodCloudHelper {

    private final static String TAG = "VodCloudHelper";

    public static void downloadRecord(final Context context, final UserBase userBase, final String recordFile, RecordFileUploader.OnRecordFileUploadListener listener) {
        final RoomSongItem song = ChooseSongs.getInstance().getSungByRecordFile(recordFile);
        if (song != null) {
            File record = new File(AudioRecordFileUtil.getRecordFilePath(recordFile, false));
            if (record.exists()) {
                RecordFileUploader recordFileUploader = new RecordFileUploader(context.getApplicationContext());
                recordFileUploader.setOnRecordFileUploadListener(listener);
                recordFileUploader.uploadRecordFile(record, userBase, song);
            }
        } else {
            Logger.w(TAG, "have not this sung song ");
        }
    }


    private static void selectMvSong(final Context ctx, final MvInfo mvInfo, final boolean is2top, final UserBase userBase) {
        if (mvInfo == null || TextUtils.isEmpty(String.valueOf(mvInfo.id)))
            return;
        SongDetailHelper detailHelper = new SongDetailHelper(ctx, String.valueOf(mvInfo.id));
        detailHelper.setOnSongDetailListener(new SongDetailHelper.OnSongDetailListener() {
            @Override
            public void onSongDetail(Song songDetail) {
                ChooseSongs.getInstance().addSong(songDetail, is2top, userBase == null, userBase, mvInfo.mvId, mvInfo.mvGalleries);
                if (userBase != null) {
                    String text = ctx.getString(R.string.upload_mv_x, songDetail.songName);
                    MobileMessageBroadcast mobileMessageBroadcast = MobileMessageBroadcast.getDefaultInstance().toBuilder()
                            .setUser(MobileUser.getDefaultInstance().toBuilder().setUserId(userBase.id).setAvatar(userBase.avatar).setName(userBase.name).build())
                            .setText(Text.getDefaultInstance().toBuilder().setContent(text)).build();
                    VodApplication.getKaraokeController().playBarrage(mobileMessageBroadcast);
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
        detailHelper.getSongDetail();
    }


    public static void selectMV(final Context ctx, final UserBase sender, int mvId, final boolean top) {
        MvSongHelper helper = new MvSongHelper(ctx, mvId);
        helper.setOnMvSongDetailListener(new MvSongHelper.OnMvSongDetailListener() {
            @Override
            public void onMvSongDetail(final MvInfo mvInfo) {
                selectMvSong(ctx, mvInfo, top, sender);
            }

            @Override
            public void onMvSongFail(String error) {

            }
        });
        helper.getMvSongDetail();
    }


    public static void topSong(final Context ctx, final UserBase sender, String songId) {
        SongDetailHelper detailHelper = new SongDetailHelper(ctx, songId);
        detailHelper.setOnSongDetailListener(new SongDetailHelper.OnSongDetailListener() {
            @Override
            public void onSongDetail(Song songDetail) {
                UserBase userBase = new UserBase(sender.id, sender.name, sender.avatar);
//                if (songDetail.hasSongFile != 1) {
//                    ChooseSongs.getInstance(ctx.getApplicationContext()).downloadCloudSong(songDetail);
//                } else {
                ChooseSongs.getInstance().addSong(songDetail, true, false, userBase, 0, "");
//                }
            }

            @Override
            public void onFail(String error) {

            }
        });
        detailHelper.getSongDetail();
    }

    public static void chooseSong(final Context ctx, final UserBase sender, String songId) {
        Logger.d(TAG, "chooseSong song_id:" + songId);
        SongDetailHelper detailHelper = new SongDetailHelper(ctx, songId);
        detailHelper.setOnSongDetailListener(new SongDetailHelper.OnSongDetailListener() {
            @Override
            public void onSongDetail(Song songDetail) {
                UserBase userBase = new UserBase(sender.id, sender.name, sender.avatar);
//                if (songDetail.hasSongFile != 1) {
//                    ChooseSongs.getInstance(ctx.getApplicationContext()).downloadCloudSong(songDetail);
//                } else {
                ChooseSongs.getInstance().addSong(songDetail, false, false, userBase, 0, "");
//                }
            }

            @Override
            public void onFail(String error) {

            }
        });
        detailHelper.getSongDetail();
    }
}
