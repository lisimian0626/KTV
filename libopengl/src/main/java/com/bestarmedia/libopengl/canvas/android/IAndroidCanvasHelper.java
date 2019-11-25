package com.bestarmedia.libopengl.canvas.android;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Chilling on 2018/4/14.
 * <p>
 * This is a helper class to use canvas for Android. This will use software rendering and generate at least one bitmap with specified width and height.
 * For async mode, it will generate 2 same bitmap. One for GLCanvas and one for itself.
 */
public interface IAndroidCanvasHelper {
    void init(int width, int height);

    void draw(final CanvasPainter canvasPainter);

    Bitmap getOutputBitmap();

    interface CanvasPainter {
        void draw(Canvas androidCanvas);
    }

    interface MODE {
        String MODE_SYNC = "SYNC";
        String MODE_ASYNC = "ASYNC";
    }

    class Factory {

        /**
         * @param mode {@link MODE}
         * @return AndroidCanvasHelper
         */
        public static IAndroidCanvasHelper createAndroidCanvasHelper(String mode) {
            if (mode.equals(MODE.MODE_SYNC)) {
                return new AndroidCanvasHelperSync();
            } else {
                return new AndroidCanvasHelperAsync();
            }
        }
    }
}
