package com.beidousat.karaoke.interf;

/**
 * Created by J Wong on 2019/18/56.
 */

public interface OnKaraokePlayerListener {
    void onPlayInfo(String playing, String next);

    void onPlayProgress(long duration, long current);
}
