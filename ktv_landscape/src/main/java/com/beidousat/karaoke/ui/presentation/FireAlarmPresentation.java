package com.beidousat.karaoke.ui.presentation;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import com.beidousat.karaoke.R;
import com.bestarmedia.libwidget.image.RecyclerImageView;

public class FireAlarmPresentation extends Presentation {

    public FireAlarmPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        Point realSize = new Point();
        display.getRealSize(realSize);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_fire_alarm);
    }

    public RecyclerImageView getFireImageView() {
        return findViewById(android.R.id.background);
    }
}
