package com.beidousat.karaoke.ui.dialog;

import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.helper.KaraokeController;
import com.beidousat.karaoke.ui.BaseActivity;
import com.beidousat.karaoke.ui.presentation.FireAlarmPresentation;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.helper.FireAlarmGetter;
import com.bestarmedia.libcommon.model.vod.service.FireAlarmInfo;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bumptech.glide.Glide;

import java.io.IOException;

/**
 * Created by J Wong on 2019/7/19.
 */

public class DlgFireAlarm extends android.support.v4.app.DialogFragment implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private RecyclerImageView rivFireImage;
    private MediaPlayer mediaPlayer;
    private FireAlarmPresentation mPresentation;
    private final SparseArray<FireAlarmPresentation> mActivePresentations = new SparseArray<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_fire_alarm, container);
        rivFireImage = view.findViewById(android.R.id.background);
        return view;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        init();
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        init();
        return super.show(transaction, tag);
    }

    @Override
    public void showNow(android.support.v4.app.FragmentManager manager, String tag) {
        super.showNow(manager, tag);
        init();
    }

    private void requestFireAlarm() {
        FireAlarmGetter getter = new FireAlarmGetter(getContext(), new FireAlarmGetter.OnFireAlarmListener() {
            @Override
            public void onFireAlarmSuccess(FireAlarmInfo info) {
                if (!TextUtils.isEmpty(info.AlarmAudio)) {
                    playAlarm(ServerFileUtil.getFileUrl(info.AlarmAudio));
                } else {
                    playAlarm(R.raw.fire_alarm);
                }
                if (!TextUtils.isEmpty(info.AlarmImgA)) {
                    Glide.with(DlgFireAlarm.this).load(ServerFileUtil.getFileUrl(info.AlarmImgA)).error(R.drawable.bg_fire_alarm)
                            .skipMemoryCache(false).into(rivFireImage);
                    if (mPresentation != null) {
                        Glide.with(DlgFireAlarm.this).load(ServerFileUtil.getFileUrl(info.AlarmImgA)).error(R.drawable.bg_fire_alarm)
                                .skipMemoryCache(false).into(mPresentation.getFireImageView());
                    }
                } else {
                    Glide.with(DlgFireAlarm.this).load(R.drawable.bg_fire_alarm).skipMemoryCache(false).into(rivFireImage);
                    if (mPresentation != null) {
                        Glide.with(DlgFireAlarm.this).load(R.drawable.bg_fire_alarm).skipMemoryCache(false).into(mPresentation.getFireImageView());
                    }
                }
            }

            @Override
            public void onFireAlarmFail() {
                playAlarm(R.raw.fire_alarm);
                Glide.with(DlgFireAlarm.this).load(R.drawable.bg_fire_alarm).skipMemoryCache(false).into(rivFireImage);
                if (mPresentation != null) {
                    Glide.with(DlgFireAlarm.this).load(R.drawable.bg_fire_alarm).skipMemoryCache(false).into(mPresentation.getFireImageView());
                }
            }
        });
        getter.requestFireAlarm();
    }

    @Override
    public void dismiss() {
        BaseActivity.mCanShowAd = true;
        close();
        VodApplication.getKaraokeController().resetInitVolume();
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
            EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_RESUME, null);
            mActivePresentations.clear();
        }
        super.dismiss();
    }


    private void playAlarm(final String pathPath) {
        if (getContext() != null) {
            AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        new Thread(() -> {
            try {
                Logger.d("FireAlarmPlayer", "playAlarm mp3 path:" + pathPath);
                mediaPlayer.setDataSource(pathPath);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                Logger.w("FireAlarmPlayer", "playAlarm server mp3 IOException:" + e.toString());
                playAlarm(R.raw.fire_alarm);
            } catch (Exception e) {
                Logger.w("FireAlarmPlayer", "playAlarm server mp3 Exception:" + e.toString());
                close();
                playAlarm(R.raw.fire_alarm);
            }
        }).start();
    }

    public void playAlarm(int resId) {
        try {
            if (getContext() != null) {
                AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
            }
            mediaPlayer = MediaPlayer.create(getContext(), resId);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Logger.w("FireAlarmPlayer", "playAlarm raw mp3 ex:" + e.toString());
        }
    }

    public void close() {
        Logger.d("FireAlarmPlayer", "停止播放>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(0.0f, 0.0f);
                mediaPlayer.setOnPreparedListener(null);
                mediaPlayer.setOnErrorListener(null);
                mediaPlayer.setOnCompletionListener(null);
                try {
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaPlayer.release();
                Logger.w("FireAlarmPlayer", "playAlarm close");
                mediaPlayer = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (getContext() != null) {
            AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            VodApplication.getKaraokeController().setStreamVolume(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        }
    }

    private void init() {
        EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
        BaseActivity.mCanShowAd = false;
        if (getContext() != null) {
            DisplayManager mDisplayManager = (DisplayManager) getContext().getSystemService(Context.DISPLAY_SERVICE);
            Display[] displays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            if (displays != null) {
                for (Display display : displays) {
                    if (display != null && display.isValid() && display.getName().toLowerCase().contains("hdmi")) {
                        Point outSize = new Point();
                        display.getSize(outSize);
                        Point realSize = new Point();
                        display.getRealSize(realSize);
                        showPresentation(display);
                        break;
                    }
                }
            }
        }
        requestFireAlarm();
    }

    private void showPresentation(final Display display) {
        final int displayId = display.getDisplayId();
        if (mActivePresentations.get(displayId) != null) {
            return;
        }
        mPresentation = new FireAlarmPresentation(getContext(), display);
        if (mPresentation.getWindow() != null)
            mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mPresentation.show();
        mActivePresentations.put(displayId, mPresentation);
    }
}
