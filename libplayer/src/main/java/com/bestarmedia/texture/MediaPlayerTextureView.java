package com.bestarmedia.texture;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bestarmedia.libopengl.ICanvasGL;
import com.bestarmedia.libopengl.canvas.gl.BasicTexture;
import com.bestarmedia.libopengl.canvas.gl.RawTexture;
import com.bestarmedia.libopengl.filter.texture.BasicTextureFilter;
import com.bestarmedia.libopengl.filter.texture.TextureFilter;
import com.bestarmedia.libopengl.glview.texture.GLSurfaceTextureProducerView;


/**
 * Created by Chilling on 2017/12/16.
 */

public class MediaPlayerTextureView extends GLSurfaceTextureProducerView {

    private TextureFilter textureFilter = new BasicTextureFilter();

    public MediaPlayerTextureView(Context context) {
        super(context);
    }

    public MediaPlayerTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaPlayerTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();
    }

    public void setTextureFilter(TextureFilter textureFilter) {
        this.textureFilter = textureFilter;
    }

    @Override
    protected void onGLDraw(ICanvasGL canvas, SurfaceTexture producedSurfaceTexture, RawTexture producedRawTexture, @Nullable SurfaceTexture sharedSurfaceTexture, @Nullable BasicTexture sharedTexture) {
        producedRawTexture.setIsFlippedVertically(true);
        canvas.drawSurfaceTexture(producedRawTexture, producedSurfaceTexture, 0, 0, producedRawTexture.getWidth(), producedRawTexture.getHeight(), textureFilter);
    }


}
