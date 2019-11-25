package com.beidousat.karaoke.widget.game;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;

/**
 * Created by J Wong on 2018/3/29.
 */

public class PanelItemView extends FrameLayout implements ItemView {

    private View overlay;
    private TextView textView;

    public PanelItemView(Context context) {
        this(context, null);
    }

    public PanelItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PanelItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.widget_game_panel_item, this);
        overlay = findViewById(R.id.overlay);
        overlay.setVisibility(INVISIBLE);
        textView = findViewById(R.id.tv_text);
    }

    public String getGameText() {
        return textView.getText().toString();
    }

    public void setGameText(String text) {
        textView.setText(text);
    }

    @Override
    public void setFocus(boolean isFocused) {
        if (overlay != null) {
            overlay.setVisibility(isFocused ? VISIBLE : INVISIBLE);
            textView.setTextColor(isFocused ? Color.parseColor("#FFFFFF") : Color.parseColor("#b8750c"));
        }
    }

}
