package com.bestarmedia.example;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.bestarmedia.player.DecodeTexturePlayer;
import com.bestarmedia.player.TexturePlayer;
import com.bestarmedia.texture.VideoTextureView;

public class MainPresentation extends Presentation {

    private VideoTextureView mergeVideoTextureView;
    private VideoTextureView videoTextureView;
    private TexturePlayer texturePlayer;
    private final static String VIDEO = "http://172.30.1.230:10230/data/ad/ad_small_video_demo.mp4";

    public MainPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        Point realSize = new Point();
        display.getRealSize(realSize);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation);
        mergeVideoTextureView = findViewById(R.id.merge_player_texture);
        videoTextureView = findViewById(R.id.video_small);
        texturePlayer = new DecodeTexturePlayer(videoTextureView, null);
        texturePlayer.setOnBasePlayerListener(new OnBasePlayerListener() {
            @Override
            public void onPlayStart(String path) {
                videoTextureView.post(() -> videoTextureView.setVisibility(View.VISIBLE));
            }

            @Override
            public void onPlayProgress(String path, long duration, long current) {

            }

            @Override
            public void onPlayCompletion(String path) {
                texturePlayer.release();
                videoTextureView.post(() -> videoTextureView.setVisibility(View.GONE));
            }

            @Override
            public void onPlayError(String path, String error) {
                videoTextureView.post(() -> videoTextureView.setVisibility(View.GONE));
            }
        });
    }

    public VideoTextureView getMergeVideoTextureView() {
        return mergeVideoTextureView;
    }


    public void playAd() {
        videoTextureView.postDelayed(() -> {
            texturePlayer.play(VIDEO);
        }, 1000);
    }
}
