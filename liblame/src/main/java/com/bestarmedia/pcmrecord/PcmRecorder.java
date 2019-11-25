package com.bestarmedia.pcmrecord;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

public class PcmRecorder {

    private static PcmRecorder pcmRecorder = null;
    private Handler mHandler = new Handler();
    private PcmRecordMicService2 pcmRecordMicService;
    private boolean hadInit;

    public static PcmRecorder getInstance() {
        if (pcmRecorder == null) {
            synchronized (PcmRecorder.class) {
                if (pcmRecorder == null) {
                    pcmRecorder = new PcmRecorder();
                }
            }
        }
        return pcmRecorder;
    }

    private PcmRecorder() {
        pcmRecordMicService = new PcmRecordMicService2();
    }

    public void init() {
        final MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//从麦克风采集声音
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); //内容输出格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("/mnt/sdcard/test.mp3");
        try {
            recorder.prepare();
            recorder.start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        recorder.stop();
                    } catch (Exception e) {
                        Log.e("PcmRecorder", "MediaRecorder stop 出错了", e);
                    }
                    try {
                        recorder.release();
                    } catch (Exception e) {
                        Log.e("PcmRecorder", "MediaRecorder release 出错了", e);
                    }
                    hadInit = true;
                }
            }, 2 * 1000);
        } catch (Exception e) {
            Log.e("PcmRecorder", "初始化MediaRecorder出错了", e);
        }
    }

    public void startRecord(String path) {
        pcmRecordMicService.startRecord(path);
    }

    public void stopRecord() {
        pcmRecordMicService.stopRecord();
    }

    public boolean isHadInit() {
        return hadInit;
    }
}
