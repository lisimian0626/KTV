package com.beidousat.karaoke.ui.presentation;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import com.beidousat.karaoke.R;

public class MainPresentationTest extends Presentation {

    private final static String TAG = "MainPresentationTest";

    public MainPresentationTest(Context outerContext, Display display) {
        super(outerContext, display);
        Point realSize = new Point();
        display.getRealSize(realSize);
        Log.d(TAG, "MainPresentationTest realSize.x:" + realSize.x + " realSize.y:" + realSize.y);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_presentation_test);
    }

}
