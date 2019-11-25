package com.bestarmedia.libopengl.filter.shape;

import com.bestarmedia.libopengl.ICanvasGL;

/**
 * Created by Chilling on 2016/11/11.
 */

public interface DrawShapeFilter {
    String getVertexShader();

    String getFragmentShader();

    void onPreDraw(int program, ICanvasGL canvas);

    void destroy();
}
