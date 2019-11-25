package com.bestarmedia.liblame;

/**
 * Created by J Wong on 2016/6/12.
 */
public interface IAudioRecordListener {

    void audioByte(double[] bytes);

    void audioBytes(byte[] bytes, int bufSize);
}
