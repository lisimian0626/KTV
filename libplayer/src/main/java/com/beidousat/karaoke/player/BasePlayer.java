package com.beidousat.karaoke.player;

import android.view.SurfaceView;

import com.bestarmedia.player.BaseMediaPlayer;

public class BasePlayer extends BaseMediaPlayer {

    public BasePlayer(SurfaceView mainSurfaceView, SurfaceView minorSurfaceView) {
        super(null, mainSurfaceView, minorSurfaceView);
    }
}
