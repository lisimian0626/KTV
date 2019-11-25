package com.beidousat.karaoke.player;

public interface OnBasePlayerListener {
    void onPlayStart(String path);

    void onPlayProgress(String path, long duration, long current);

    void onPlayCompletion(String path);

    void onPlayError(String path, String error);
}
