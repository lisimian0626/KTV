package com.bestarmedia.liblame;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioRecordHelper {

    private AudioRecord mAudioRecord;
    private int mRecordBufSize;
    private byte[] mPcmData;
    private final static int INIT = 1;
    private final static int RECORDING = 2;
    private final static int STOP = 3;

    private int mState = 0;

    private OnPcmCallback onPcmCallback;

    private ExecutorService executorService;


    public AudioRecordHelper() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    private boolean initRecorder() {
        mRecordBufSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, mRecordBufSize);
        if (mAudioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            mAudioRecord = null;
            mRecordBufSize = 0;
            return false;
        } else {
            //创建一个位置用于存放后续的PCM数据
            mPcmData = new byte[mRecordBufSize];
            mState = INIT;
            return true;
        }
    }

    public void start() {
        if (mState != INIT) {
            boolean success = initRecorder();
            Log.d(getClass().getSimpleName(), "录音初始化 >>>>>>>>>>>>>>>>>" + success);
            if (success) {
                mAudioRecord.startRecording();
                mState = RECORDING;
                Log.d(getClass().getSimpleName(), "录音开始1 >>>>>>>>>>>>>>>>>");
                executorService.execute(mReadDataThread);
                Log.d(getClass().getSimpleName(), "录音开始2 >>>>>>>>>>>>>>>>>");
            }
        }
    }

    public void release() {
        mState = STOP;
        mAudioRecord.release();
    }

    public boolean isRecording() {
        return mState == RECORDING;
    }

    public int getRecordState() {
        return mState;
    }

    public void setOnPcmCallback(OnPcmCallback onPcmCallback) {
        this.onPcmCallback = onPcmCallback;
    }

    private Thread mReadDataThread = new Thread() {
        @Override
        public void run() {
            int read;
            Log.d(getClass().getSimpleName(), "录音线程 >>>>>>>>>>>>>>>>> ");
            while (mState == RECORDING) {
                //读取mRecordBufSize长度的音频数据存入mPcmData中
                read = mAudioRecord.read(mPcmData, 0, mRecordBufSize);
                //如果读取音频数据没有出现错误 ===> read 大于0
                Log.d(getClass().getSimpleName(), "录音数据：" + read);
                if (read >= AudioRecord.SUCCESS) {
                    synchronized (AudioRecordHelper.class) {
                        if (onPcmCallback != null)
                            onPcmCallback.onPcmDataCallback(mPcmData, read);
                    }
                }
            }
        }
    };

    public interface OnPcmCallback {
        void onPcmDataCallback(byte[] pcmData, int read);
    }
}
