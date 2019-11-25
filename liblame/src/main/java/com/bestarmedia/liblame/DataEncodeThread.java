package com.bestarmedia.liblame;

import android.media.AudioRecord;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.czt.mp3recorder.util.LameUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataEncodeThread extends HandlerThread implements AudioRecord.OnRecordPositionUpdateListener {
    private StopHandler mHandler;
    private static final int PROCESS_STOP = 1;
    private byte[] mMp3Buffer;
    private int channel;
    //	private FileOutputStream mFileOutputStream;
    private RandomAccessFile mFileOutputStream;

    private static class StopHandler extends Handler {

        private DataEncodeThread encodeThread;

        StopHandler(Looper looper, DataEncodeThread encodeThread) {
            super(looper);
            this.encodeThread = encodeThread;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == PROCESS_STOP) {
                //处理缓冲区中的数据
                while (encodeThread.processData() > 0) ;
                // Cancel any event left in the queue
                removeCallbacksAndMessages(null);
                encodeThread.flushAndRelease();
                getLooper().quit();
            }
        }
    }

    /**
     * Constructor
     *
     * @param file       file
     * @param bufferSize bufferSize
     * @throws FileNotFoundException file not found
     */
    DataEncodeThread(File file, int bufferSize, int channel) throws FileNotFoundException {
        super("DataEncodeThread");
        this.mFileOutputStream = new RandomAccessFile(file, "rw");//new FileOutputStream(file);
        this.mMp3Buffer = new byte[(int) (7200 + (bufferSize * 2 * 1.25))];
        this.channel = channel;
    }

    @Override
    public synchronized void start() {
        super.start();
        mHandler = new StopHandler(getLooper(), this);
    }

    private void check() {
        if (mHandler == null) {
            throw new IllegalStateException();
        }
    }

    void sendStopMessage() {
        check();
        mHandler.sendEmptyMessage(PROCESS_STOP);
    }

    public Handler getHandler() {
        check();
        return mHandler;
    }

    @Override
    public void onMarkerReached(AudioRecord recorder) {
        // Do nothing
    }

    @Override
    public void onPeriodicNotification(AudioRecord recorder) {
        processData();
    }

    /**
     * 从缓冲区中读取并处理数据，使用lame编码MP3
     *
     * @return 从缓冲区中读取的数据的长度
     * 缓冲区中没有数据时返回0
     */
    private int processData() {
        try {
            if (mTasks.size() > 0) {
                Task task = mTasks.remove(0);
                byte[] buffer = task.getData2();
                int readSize = task.getReadSize();
                if (!"ktv".equalsIgnoreCase(Build.MODEL)) {
                    int encodedSize = LameUtil.encode2(buffer, readSize, mMp3Buffer, channel);
                    if (encodedSize > 0) {
                        try {
                            mFileOutputStream.write(mMp3Buffer, 0, encodedSize);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return readSize;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Flush all data left in lame buffer to file
     */
    private void flushAndRelease() {
        //将MP3结尾信息写入buffer中
        if (!"ktv".equalsIgnoreCase(Build.MODEL)) {
            final int flushResult = LameUtil.flush(mMp3Buffer);
            if (flushResult > 0) {
                try {
                    mFileOutputStream.write(mMp3Buffer, 0, flushResult);
                    int xframe = LameUtil.writeXingFrame(mMp3Buffer);
                    if (xframe > 0) {
                        mFileOutputStream.seek(0);
                        mFileOutputStream.write(mMp3Buffer, 0, xframe);
                        //write header
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (mFileOutputStream != null) {
                        try {
                            mFileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    LameUtil.close();
                }
            }
        }
    }

    private List<Task> mTasks = Collections.synchronizedList(new ArrayList<Task>());

    public void addTask(short[] rawData, int readSize) {
        mTasks.add(new Task(rawData, readSize));
    }

    void addTask(byte[] rawData, int readSize) {
        mTasks.add(new Task(rawData, readSize));
    }

    private class Task {
        private short[] rawData;
        private byte[] rawData2;
        private int readSize;

        Task(byte[] rawData, int readSize) {
            this.rawData2 = rawData.clone();
            this.readSize = readSize;
        }

        Task(short[] rawData, int readSize) {
            this.rawData = rawData.clone();
            this.readSize = readSize;
        }

        byte[] getData2() {
            return rawData2;
        }

        public short[] getData() {
            return rawData;
        }

        int getReadSize() {
            return readSize;
        }
    }
}
