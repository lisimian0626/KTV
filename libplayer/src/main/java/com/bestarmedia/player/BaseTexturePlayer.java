package com.bestarmedia.player;

import android.view.Surface;

public class BaseTexturePlayer extends BaseMediaPlayer {

    private final static String TAG = "BaseTexturePlayer";

    public BaseTexturePlayer(Surface surface) {
        super(surface, null, null);
    }

}
