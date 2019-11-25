package com.bestarmedia.liblame;

import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.pcmrecord.PcmRecorder;

import java.io.File;

/**
 * Created by J Wong on 2016/9/12.
 */
public class BnsAudioRecorder implements IAudioRecordListener {

    private static BnsAudioRecorder mBnsVoiceRecorder;
    private IAudioRecordListener mIAudioRecordListener;

    private MP3Recorder mRecorder;
    private boolean mIsEncodeMP3;
    private AudioRecordHelper audioRecordHelper;
    private String recordName;

    public static BnsAudioRecorder getInstance() {
        if (mBnsVoiceRecorder == null) {
            mBnsVoiceRecorder = new BnsAudioRecorder();
        }
        return mBnsVoiceRecorder;
    }


    public void setAudioRecordListener(IAudioRecordListener listener) {
        this.mIAudioRecordListener = listener;
    }

    public void start() {
        release();
        setRecordName(recordName);
        startRecord();
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    private void startRecord() {
        if (!TextUtils.isEmpty(this.recordName)) {
            String filePath = AudioRecordFileUtil.getRecordFilePath(this.recordName, true);
            try {
                if ("ktv".equalsIgnoreCase(android.os.Build.MODEL)) {
                    if (audioRecordHelper == null || !audioRecordHelper.isRecording()) {
                        PcmRecorder.getInstance().startRecord(filePath);
                        if (audioRecordHelper == null) {
                            audioRecordHelper = new AudioRecordHelper();
                        }
                        audioRecordHelper.start();
                    }
                } else {
                    if (mRecorder == null || !mRecorder.isRecording()) {
                        mRecorder = new MP3Recorder(new File(filePath));
                        mRecorder.setAudioRecordListener(this);
                        mRecorder.setEncodeMP3(isEncodeMP3());
                        mRecorder.start();
                    }
                }
                Log.d(getClass().getSimpleName(), "start >>>>>>>>>>>>>>>>>>>> filePath:" + filePath);
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "启动录音出错了", e);
            }
        }
    }

    public void pause(boolean isPause) {
        if (mRecorder != null)
            mRecorder.setPause(isPause);
    }

    public void release() {
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.stop();
            mRecorder = null;
        }
        if ("ktv".equalsIgnoreCase(android.os.Build.MODEL)) {
            PcmRecorder.getInstance().stopRecord();
            if (audioRecordHelper != null) {
                audioRecordHelper.release();
                audioRecordHelper = null;
            }
        }
    }

    public boolean isRecording() {
        return mRecorder != null && mRecorder.isRecording();
    }

    /**
     * 是否需要编码成MP3文件
     *
     * @param encodeMP3 是否编码成MP3文件
     */
    public void setEncodeMP3(boolean encodeMP3) {
        mIsEncodeMP3 = encodeMP3;
        Log.d("BnsAudioRecorder", "setEncodeMP3 >>>" + mIsEncodeMP3);
        if (mRecorder != null) {
            mRecorder.setEncodeMP3(isEncodeMP3());
        } else {
            if (!TextUtils.isEmpty(recordName)) {
                startRecord();
            }
        }
    }

    /**
     * @return 是否编码成mp3
     */
    public boolean isEncodeMP3() {
        return mIsEncodeMP3;
    }

    @Override
    public void audioByte(double[] bytes) {
        if (mIAudioRecordListener != null) {
            mIAudioRecordListener.audioByte(bytes);
        }
    }

    @Override
    public void audioBytes(byte[] bytes, int bufSize) {
        if (mIAudioRecordListener != null) {
            mIAudioRecordListener.audioBytes(bytes, bufSize);
        }
    }
}
