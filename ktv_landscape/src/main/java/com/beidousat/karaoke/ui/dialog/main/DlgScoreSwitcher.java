package com.beidousat.karaoke.ui.dialog.main;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libwidget.text.GradientTextView;
import com.bestarmedia.libwidget.util.DensityUtil;

/**
 * Created by J Wong on 2015/10/12 15:52.
 */
public class DlgScoreSwitcher extends BaseDialog implements View.OnClickListener {

    private GradientTextView tvEntertainment, tvProfessional;
    private ImageView ivClose;

    public DlgScoreSwitcher(Activity context) {
        super(context, R.style.MyDialog);
        init(context);
    }

    public void init(Activity context) {
        this.setContentView(R.layout.dlg_score_switcher);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        lp.width = DensityUtil.dip2px(getContext(), 400);
        lp.height = DensityUtil.dip2px(getContext(), 300);
        getWindow().setAttributes(lp);
        tvEntertainment = findViewById(R.id.score_tv_entertainment);
        tvProfessional = findViewById(R.id.score_tv_professional);
        ivClose = findViewById(R.id.score_iv_close);
        tvEntertainment.setOnClickListener(this);
        tvProfessional.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score_iv_close:
                dismiss();
                break;
            case R.id.score_tv_entertainment:
                VodApplication.getKaraokeController().setScoreMode(1);
                dismiss();
                break;
            case R.id.score_tv_professional:
                VodApplication.getKaraokeController().setScoreMode(2);
                dismiss();
                break;
        }
    }

}
