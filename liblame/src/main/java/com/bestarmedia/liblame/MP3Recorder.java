package com.bestarmedia.liblame;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import com.czt.mp3recorder.util.LameUtil;

import java.io.File;
import java.io.IOException;

public class MP3Recorder {

    //=======================AudioRecord Default Settings=======================
    // private int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    private int DEFAULT_AUDIO_SOURCE = "ktv".equalsIgnoreCase(Build.MODEL) ? MediaRecorder.AudioSource.DEFAULT : MediaRecorder.AudioSource.CAMCORDER;
    /**
     * 与DEFAULT_CHANNEL_CONFIG相关，因为是mono单声，所以是1
     */
    private final static int DEFAULT_LAME_IN_CHANNEL = 1;

    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
//    private int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;

    /**
     * 以下三项为默认配置参数。Google Android文档明确表明只有以下3个参数是可以在所有设备上保证支持的。
     */
    static final int DEFAULT_SAMPLING_RATE = 44100;//模拟器仅支持从麦克风输入8kHz采样率

    /**
     * 下面是对此的封装
     * private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
     */
    private static final PCMFormat DEFAULT_AUDIO_FORMAT = PCMFormat.PCM_16BIT;

    //======================Lame Default Settings=====================
    private static final int DEFAULT_LAME_MP3_QUALITY = 5;

    /**
     * Encoded bit rate. MP3 file will be encoded with bit rate 32kbps
     */
    private static final int DEFAULT_LAME_MP3_BIT_RATE = 128;

    //==================================================================

    /**
     * 自定义 每160帧作为一个周期，通知一下需要进行编码
     */
    private static final int FRAME_COUNT = 160;
    private AudioRecord mAudioRecord = null;
    private int mBufferSize;
    private DataEncodeThread mEncodeThread;
    private volatile boolean mIsRecording = false;
    private File mRecordFile;
    private byte[] mPCMBuffer2;
    private int bufsize;
    private boolean mNeedCallback = true;
    private byte[] mScoreData = new byte[8192];
    private volatile boolean mIsPause;
    private volatile boolean mBeEncodeMP3 = false;

    /**
     * Default constructor. Setup recorder with default sampling rate 1 channel,
     * 16 bits pcm
     *
     * @param recordFile target file
     */
    MP3Recorder(File recordFile) {
        mRecordFile = recordFile;
    }

    private void callback(byte[] audioData, int readSize) {
        if (mAudioRecordListener != null) {
            mAudioRecordListener.audioBytes(audioData, readSize);//for mic visualizer
            if (!mIsPause) {//for score
                try {
                    double[] double_buffer = new double[4096];
                    for (int i = 0, j = 0; i < audioData.length; i = i + 2, j = j + 1) {
                        //右边为大端
                        long data_low = audioData[i];
                        long data_high = audioData[i + 1];
                        long data_true = data_high * 256 + data_low;
                        long data_complement;
                        //取大端的最高位（符号位）
                        int my_sign = (int) (data_high / 128);
                        if (my_sign == 1) {
                            data_complement = data_true - 65536;
                        } else {
                            data_complement = data_true;
                        }
                        double float_data = (data_complement / (double) 32768);
                        double_buffer[j] = float_data;
                    }
                    mAudioRecordListener.audioByte(double_buffer);
                } catch (Exception e) {
                    Log.w("MP3Recorder", "ex:" + e.toString());
                }
            }
        }
    }

    private void leftRightChannel(int readSize, byte[] audioData) {
        if ("ktv".equalsIgnoreCase(Build.MODEL)) {//全志H6录音使用别的方式，所有不用拆分左右
            System.arraycopy(audioData, 0, mScoreData, 4096, readSize);
            callback(mScoreData, readSize);
        } else {
            byte[] leftChannelAudioData = new byte[readSize / 2];//左声道为上面的口mic in,用于打分
            byte[] rightChannelAudioData = new byte[readSize / 2];//右声道为下面的口line in用于录音
            for (int i = 0; i < readSize / 2; i = i + 2) {
                leftChannelAudioData[i] = audioData[2 * i];
                leftChannelAudioData[i + 1] = audioData[2 * i + 1];
                rightChannelAudioData[i] = audioData[2 * i + 2];
                rightChannelAudioData[i + 1] = audioData[2 * i + 3];
            }
            if (!mIsPause && mBeEncodeMP3) {//for  mp3 record，全志H6不需要此方式
                mEncodeThread.addTask(rightChannelAudioData, readSize / 2);
            }
            mNeedCallback = !mNeedCallback;//line in PCM是回调两次才
            if (mNeedCallback) {
                mScoreData = new byte[8192];
                System.arraycopy(leftChannelAudioData, 0, mScoreData, 0, leftChannelAudioData.length);
            } else {
                System.arraycopy(leftChannelAudioData, 0, mScoreData, 4096, leftChannelAudioData.length);
                callback(mScoreData, readSize);
            }
        }
    }

    /**
     * Start recording. Create an encoding thread. Start record from this
     * thread.
     *
     * @throws IOException initAudioRecorder throws
     */
    public void start() throws IOException {
        if (mIsRecording) {
            return;
        }
        mIsRecording = true; // 提早，防止init或startRecording被多次调用
        initAudioRecorder();
        mAudioRecord.startRecording();
        Log.d("MP3Recorder", "AudioRecord startRecording()>>>>>>>>>>>>>> mBeEncodeMP3:" + mBeEncodeMP3);
        new Thread() {
            @Override
            public void run() {
                //设置线程权限
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                while (mIsRecording) {
                    try {
                        int readSize = mAudioRecord.read(mPCMBuffer2, 0, bufsize);
//                        Log.d("MP3Recorder", "AudioRecord read readSize:" + readSize);
                        if (readSize > 0) {
                            leftRightChannel(readSize, mPCMBuffer2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // release and finalize audioRecord
                try {
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    mAudioRecord = null;
                    mEncodeThread.sendStopMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private int mVolume;

    /**
     * @param buffer   buffer
     * @param readSize readSize
     */
    private void calculateRealVolume(byte[] buffer, int readSize) {
        Log.d("MP3Recorder", "buffer len:" + buffer.length + "  readSize:" + readSize);
        double sum = 0;
        for (int i = 0; i < readSize; i++) {
            // 这里没有做运算的优化，为了更加清晰的展示代码
            sum += buffer[i] * buffer[i];
        }
        if (readSize > 0) {
            // 平方和除以数据总长度，得到音量大小。
            double mean = sum / (double) readSize;
            final double volume = 10 * Math.log10(mean);

            double amplitude = sum / readSize;
            mVolume = (int) Math.sqrt(amplitude);

            Log.d("MP3Recorder", "calculateRealVolume mVolume:" + mVolume + "  volume:" + volume);
        } else {
            Log.d("MP3Recorder", "calculateRealVolume readSize < 0:");
        }
    }

    /**
     * 获取真实的音量
     *
     * @return 真实音量
     */
    public int getRealVolume() {
        return mVolume;
    }

    /**
     * 获取相对音量。 超过最大值时取最大值。
     *
     * @return 音量
     */
    public int getVolume() {
        if (mVolume >= MAX_VOLUME) {
            return MAX_VOLUME;
        }
        return mVolume;
    }

    private static final int MAX_VOLUME = 2000;

    /**
     * 根据资料假定的最大值。 实测时有时超过此值。
     *
     * @return 最大音量值。
     */
    public int getMaxVolume() {
        return MAX_VOLUME;
    }

    public void stop() {
        mIsRecording = false;
    }

    boolean isRecording() {
        return mIsRecording;
    }

    /**
     * Initialize audio recorder
     */
    private void initAudioRecorder() throws IOException {
        mBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE,
                DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat());
        /* Setup audio recorder */
        mAudioRecord = new AudioRecord(DEFAULT_AUDIO_SOURCE, DEFAULT_SAMPLING_RATE,
                DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat(), mBufferSize);
//        bufsize = mBufferSize * 2;
        bufsize = mBufferSize;
        mPCMBuffer2 = new byte[bufsize];
        /*
         * Initialize lame buffer
         * mp3 sampling rate is the same as the recorded pcm sampling rate
         * The bit rate is 32kbps
         *
         */

//        if (needEncode) {
        if (!"ktv".equalsIgnoreCase(Build.MODEL)) {
            LameUtil.init(DEFAULT_SAMPLING_RATE, DEFAULT_LAME_IN_CHANNEL, DEFAULT_SAMPLING_RATE, DEFAULT_LAME_MP3_BIT_RATE, DEFAULT_LAME_MP3_QUALITY);
        }
        // Create and run thread used to encode data
        // The thread will
        mEncodeThread = new DataEncodeThread(mRecordFile, mBufferSize / 2, DEFAULT_LAME_IN_CHANNEL);
        mEncodeThread.start();
        mAudioRecord.setRecordPositionUpdateListener(mEncodeThread, mEncodeThread.getHandler());
        mAudioRecord.setPositionNotificationPeriod(FRAME_COUNT);
//        } else {
//            String path = mRecordFile.getAbsolutePath();
//            File file = new File(path.replace(".mp3", ".raw"));
//            mFileOutputStream = new FileOutputStream(file);
//        }
    }

//    private FileOutputStream mFileOutputStream;
//    private final static boolean needEncode = true;


    public void setPause(boolean isPause) {
        this.mIsPause = isPause;
    }

    /**
     * 是否需要将音频编码成MP3文件
     *
     * @param encodeMP3 是否编码成MP3文件
     */
    void setEncodeMP3(boolean encodeMP3) {
        this.mBeEncodeMP3 = !"ktv".equalsIgnoreCase(android.os.Build.MODEL) && encodeMP3;
        Log.d("MP3Recorder", "setEncodeMP3 >>>" + mBeEncodeMP3);
    }


    private IAudioRecordListener mAudioRecordListener;
//    private long mPreCallback;

    void setAudioRecordListener(IAudioRecordListener l) {
        this.mAudioRecordListener = l;
    }

}