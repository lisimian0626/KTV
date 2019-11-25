package com.bestarmedia.pcmrecord;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;

import com.ifang.lame.PCMFormat;
import com.ifang.lame.RingBuffer;
import com.ifang.lame.SimpleLame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PcmRecordMicService2 implements RecordInf {
    private String mSavePath;
    private final int mSampleRate = 48000;

    private boolean isStopGetMic = true;//是否应该停止录音
    /**
     * 获取mic线程是否在运行中
     */
    private boolean isMicThreadRunning = false;

    private boolean isStopEncode = true;
    private boolean isRecording = false;//用于标识录音文件保存完成

    private long mRecordDuration = 0;
    private boolean isInitMp3Lame = false;

    private boolean isDiscardRecord;

    private float mMicScale = 1.0f;
    private final int CHANNEL_COUNT = 1;

    /* Encoded bit rate. MP3 file will be encoded with bit rate 32kbps */
    private int BIT_RATE = 128;
    private final int LEN = 7680/* * 2*/;//8960;
    private RingBuffer mRecordRingBuffer;

    /**
     * 重新录音的消息
     */
    private static final int REREOCRD = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == REREOCRD) {
                if (isRecordFinish()) {//如果录音已经完成，开始录音
                    startRecord(mSavePath);
                } else {//如果录音没有完成，等待500毫秒后再进行录音
                    if (mHandler.hasMessages(REREOCRD)) {
                        mHandler.removeMessages(REREOCRD);
                    }
                    mHandler.sendEmptyMessageDelayed(REREOCRD, 200);
                }
            }
        }
    };

    public PcmRecordMicService2() {
    }

    @Override
    public synchronized boolean startRecord(String savePath) {
        UtilLog.d("call startRecord, savePath=" + savePath + "  isRecording=" + isRecording);
        stopRecord();
        mSavePath = savePath;
        if (isRecording || isMicThreadRunning) {
            UtilLog.e("isRecording, should delay startRecord!!");
            if (mHandler.hasMessages(REREOCRD)) {
                mHandler.removeMessages(REREOCRD);
            }
            mHandler.sendEmptyMessageDelayed(REREOCRD, 200);
            return true;
        }

        mRecordRingBuffer = new RingBuffer(LEN * 20);

        isRecording = true;
        if (!isInitMp3Lame) {
            isInitMp3Lame = true;
            SimpleLame.init(mSampleRate, CHANNEL_COUNT, mSampleRate, BIT_RATE);
        }

        isDiscardRecord = false;
        startGetMicData(mSampleRate);
        startEncodeThread();
        return true;
    }

    @Override
    public void stopRecord() {
        if (mHandler.hasMessages(REREOCRD)) {
            mHandler.removeMessages(REREOCRD);
        }
        isStopEncode = true;
        isStopGetMic = true;
    }

    @Override
    public void reRecord() {
        discardRecord();
        if (mHandler.hasMessages(REREOCRD)) {
            mHandler.removeMessages(REREOCRD);
        }
        mHandler.sendEmptyMessageDelayed(REREOCRD, 1000);
    }

    @Override
    public boolean isRecording() {
        return isRecording;
    }

    @Override
    public boolean isRecordFinish() {
        return !isRecording;
    }

    @Override
    public long getRecordDuration() {
        return mRecordDuration;
    }

    @Override
    public String getSavePath() {
        return mSavePath;
    }

    @Override
    public void setComposeScale(float videoScale, float micScale) {
        mMicScale = micScale;
    }

    @Override
    public void setCompressionRatio(int ratio) {
        BIT_RATE = ratio;
    }

    /**
     * 是否运行录音线程
     *
     * @return
     */
    boolean isRunningRecordThread() {
        return !isStopGetMic;
    }

    private void startGetMicData(final int sample_rate) {
        isMicThreadRunning = true;
        isStopGetMic = false;
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    PCMFormat audioFormat = PCMFormat.PCM_16BIT;
                    int samplingRate = sample_rate;
                    int channelConfig = AudioFormat.CHANNEL_IN_STEREO;//AudioFormat.CHANNEL_CONFIGURATION_MONO;
                    int bytesPerFrame = audioFormat.getBytesPerFrame();
					/* Get number of samples. Calculate the buffer size (round up to the
					   factor of given frame size) */
                    int frameSize = AudioRecord.getMinBufferSize(samplingRate,
                            channelConfig, audioFormat.getAudioFormat()) / bytesPerFrame;
                    if (frameSize % FRAME_COUNT != 0) {
                        frameSize = frameSize + (FRAME_COUNT - frameSize % FRAME_COUNT);
                        UtilLog.d("Frame size: " + frameSize);
                    }

                    int bufferSize = frameSize * bytesPerFrame;
                    PcmRecord1 mAudioRecord = new PcmRecord1(MediaRecorder.AudioSource.MIC,
                            samplingRate, channelConfig, audioFormat.getAudioFormat(),
                            bufferSize);
                    bufferSize = bufferSize * 2;
                    UtilLog.e("---------bufferSize=" + bufferSize);
                    mAudioRecord.startRecording();
                    byte[] buff = new byte[bufferSize / 2];
                    byte[] buff2 = new byte[bufferSize];
                    int count;
                    mRecordRingBuffer.reset();
                    int i, j;
//					int skip = 39;
                    while (!isStopGetMic) {
                        count = mAudioRecord.read(buff2, 0, bufferSize);
//						if(skip > 0) {
//							skip--;
//							continue;
//						}
                        if (count > 0) {
                            if (count % 4 != 0) {
                                UtilLog.e("count=" + count);
                            }
                            for (i = 0, j = 0; i < count; i++) {
                                if (i % 4 == 0 || i % 4 == 1) {
                                    buff[j++] = buff2[i];
                                }
                            }
                            count = count / 2;

                            if (isRecording && !isStopEncode) {
                                if (mRecordRingBuffer.write(buff, count) == 0) {
                                    resizeBuffSize();
                                    mRecordRingBuffer.write(buff, count);
                                }
                            }
                        }
                    }
                    if (isStopEncode) {
                        mRecordRingBuffer = null;
                    }
                    isStopGetMic = true;

                    mAudioRecord.release();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    UtilLog.e("end startGetMicData");
                    isMicThreadRunning = false;
                }
            }
        }).start();
    }

    private void startEncodeThread() {
        isRecording = true;
        isStopEncode = false;
        new Thread(new Runnable() {

            @Override
            public void run() {
                short[] innerBuf = new short[LEN / 2];
                byte[] buffer = new byte[LEN];
                byte[] mp3Buffer = new byte[(int) (7200 + (innerBuf.length * 1.25))];

                FileOutputStream outStream = null;
                File f = new File(mSavePath);
                if (f.exists()) {
                    f.delete();
                }
                try {
                    outStream = new FileOutputStream(mSavePath);
                    long time = System.currentTimeMillis();
                    int curPriority = Thread.MIN_PRIORITY;
                    int curProcessPrio = android.os.Process.THREAD_PRIORITY_DEFAULT;
                    while (!isStopEncode) {
                        //UtilLog.d("mRecordRingBuffer.getReadSize()="+mRecordRingBuffer.getReadSize());
                        while (!isStopEncode && mRecordRingBuffer.getReadSize() >= LEN && !isResizing) {
                            if (mRecordRingBuffer.getReadSize() * 2 > mRecordRingBuffer.getCapacity()) {
                                if (curPriority < Thread.MAX_PRIORITY) {
                                    curPriority = curPriority + 4;
                                    if (curPriority > Thread.MAX_PRIORITY) {
                                        curPriority = Thread.MAX_PRIORITY;
                                    }
                                    Thread.currentThread().setPriority(curPriority);
                                }

                                if (curPriority == Thread.MAX_PRIORITY && curProcessPrio > android.os.Process.THREAD_PRIORITY_URGENT_AUDIO) {
                                    curProcessPrio = curProcessPrio - 4;
                                    if (curProcessPrio < android.os.Process.THREAD_PRIORITY_URGENT_AUDIO) {
                                        curProcessPrio = android.os.Process.THREAD_PRIORITY_URGENT_AUDIO;
                                    }
                                    android.os.Process.setThreadPriority(curProcessPrio);
                                }
                                UtilLog.d("curPriority=" + curPriority + "  curProcessPrio=" + curProcessPrio);
                            }
//							UtilLog.d("mRecordRingBuffer.getReadSize()="+mRecordRingBuffer.getReadSize());
                            mRecordRingBuffer.read(buffer, LEN);
                            ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(innerBuf);

                            if (Math.abs(mMicScale - 1.0f) > 0.01f) {
                                float temp;
                                for (int i = 0; i < innerBuf.length; i++) {
                                    temp = innerBuf[i] * mMicScale;
                                    if (temp > Short.MAX_VALUE) {
                                        temp = Short.MAX_VALUE;
                                    } else if (temp < Short.MIN_VALUE) {
                                        temp = Short.MIN_VALUE;
                                    }
                                    innerBuf[i] = (short) temp;
                                }
                            }

                            int encodedSize = SimpleLame.encode(innerBuf, innerBuf, innerBuf.length, mp3Buffer);

                            if (encodedSize < 0) {
                                UtilLog.e("Lame encoded size: " + encodedSize);
                                continue;
                            }
//							UtilLog.e("end encode");
                            try {
                                outStream.write(mp3Buffer, 0, encodedSize);
                            } catch (IOException e) {
                                UtilLog.e("Unable to write to file");
                            }
                        }
                        if (!isStopEncode && !isResizing) {
                            if (curPriority != Thread.MIN_PRIORITY) {
                                curPriority = Thread.MIN_PRIORITY;
                                Thread.currentThread().setPriority(curPriority);
                            }

                            if (curProcessPrio != android.os.Process.THREAD_PRIORITY_DEFAULT) {
                                curProcessPrio = android.os.Process.THREAD_PRIORITY_DEFAULT;
                                android.os.Process.setThreadPriority(curProcessPrio);
                            }
                            try {
//								UtilLog.d("sleep");
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (isStopGetMic) {
                        mRecordRingBuffer = null;
                    }
                    isStopEncode = true;
                    mRecordDuration = System.currentTimeMillis() - time;
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    if (outStream != null) {
                        try {
                            outStream.flush();
                            outStream.close();
                            outStream = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    UtilLog.e("end startEncodeThread");
                    isRecording = false;
                }

                if (isDiscardRecord) {
                    if (f.exists()) {
                        f.delete();
                    }
                    isDiscardRecord = false;
                }
            }
        }).start();
    }

    @Override
    public void discardRecord() {
        isDiscardRecord = true;
        stopRecord();
    }

    private boolean isResizing = false;

    private void resizeBuffSize() {
        isResizing = true;
        int size = mRecordRingBuffer.getCapacity() + LEN * 10;
        UtilLog.d("resizeBuffSize size=" + size);
        RingBuffer mRingBuffer = new RingBuffer(size);
        byte[] buf = new byte[1024];
        int count;
        while ((count = mRecordRingBuffer.read(buf, buf.length)) > 0) {
            mRingBuffer.write(buf, count);
        }
        mRecordRingBuffer = mRingBuffer;
        isResizing = false;
    }
}
