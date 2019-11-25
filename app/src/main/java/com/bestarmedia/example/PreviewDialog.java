package com.bestarmedia.example;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.beidousat.karaoke.player.OnBasePlayerListener;
import com.bestarmedia.player.DecodeTexturePlayer;
import com.bestarmedia.texture.VideoTextureView;

public class PreviewDialog extends Dialog implements OnBasePlayerListener {

    private VideoTextureView videoTextureView;
    private DecodeTexturePlayer decodeTexturePlayer;

    public PreviewDialog(Context context, String path) {
        super(context);
        init(path);
    }


    void init(final String path) {
        this.setContentView(R.layout.dlg_preview);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.width = getContext().getResources().getInteger(R.integer.preview_w);
//        lp.height = getContext().getResources().getInteger(R.integer.preview_h);
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        videoTextureView = findViewById(R.id.player_texture);
        decodeTexturePlayer = new DecodeTexturePlayer(videoTextureView, null);
        decodeTexturePlayer.setOnBasePlayerListener(this);
        videoTextureView.postDelayed(() -> {
            decodeTexturePlayer.playEncode(path, null);
        }, 1000);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        decodeTexturePlayer.release();
    }


    @Override
    public void onPlayStart(String path) {
        decodeTexturePlayer.setVolume(0.0F);
    }

    @Override
    public void onPlayProgress(String path, long duration, long current) {

    }

    @Override
    public void onPlayCompletion(String path) {
        dismiss();
    }

    @Override
    public void onPlayError(String path, String error) {

    }
}
