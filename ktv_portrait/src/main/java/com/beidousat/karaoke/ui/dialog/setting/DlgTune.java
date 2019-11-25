package com.beidousat.karaoke.ui.dialog.setting;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.serialport.SerialController;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.helper.UsbHubReseter;
import com.bestarmedia.libcommon.model.vod.SerialPortCode;
import com.bestarmedia.libwidget.text.GradientTextView;

/**
 * Created by J Wong on 2015/10/12 15:52.
 */
public class DlgTune extends BaseDialog implements View.OnClickListener {

    private GradientTextView iv_mic_up, iv_mic_down, iv_music_up, iv_music_down, iv_effect_up, iv_effect_down;
    private GradientTextView tv_mic_tone_up, tv_mic_tone_default, tv_mic_tone_down;
    private GradientTextView tv_music_tone_up, tv_music_tone_default, tv_music_tone_down;
    private GradientTextView tv_effect_sing, tv_effect_blame, tv_effect_harmony, tv_effect_strick;
    private View padding_left, padding_right;

    public DlgTune(Activity context) {
        super(context, R.style.MyDialog);
        init();
    }

    public void init() {
        this.setContentView(R.layout.dlg_tune);
        initView();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.tone_iv_close).setOnClickListener(this);
        iv_mic_up.setOnClickListener(this);
        iv_mic_down.setOnClickListener(this);
        tv_mic_tone_up.setOnClickListener(this);
        tv_mic_tone_default.setOnClickListener(this);
        tv_mic_tone_down.setOnClickListener(this);
        tv_effect_sing.setOnClickListener(this);
        tv_effect_blame.setOnClickListener(this);
        tv_effect_harmony.setOnClickListener(this);
        tv_effect_strick.setOnClickListener(this);
        iv_music_up.setOnClickListener(this);
        iv_music_down.setOnClickListener(this);
        tv_music_tone_up.setOnClickListener(this);
        tv_music_tone_default.setOnClickListener(this);
        tv_music_tone_down.setOnClickListener(this);
        iv_effect_up.setOnClickListener(this);
        iv_effect_down.setOnClickListener(this);
    }

    private void initView() {
        padding_left = findViewById(R.id.tone_left_padding);
        padding_right = findViewById(R.id.tone_right_padding);
        //麦克风
        iv_mic_up = findViewById(R.id.tone_iv_mic_up);
        iv_mic_down = findViewById(R.id.tone_iv_mic_down);

        tv_mic_tone_up = findViewById(R.id.tone_tv_mic_up);
        tv_mic_tone_default = findViewById(R.id.tone_tv_mic_default);
        tv_mic_tone_down = findViewById(R.id.tone_tv_mic_down);
        //效果
        tv_effect_sing = findViewById(R.id.tone_tv_effect_sing);
        tv_effect_blame = findViewById(R.id.tone_tv_effect_blame);
        tv_effect_harmony = findViewById(R.id.tone_tv_effect_harmony);
        tv_effect_strick = findViewById(R.id.tone_tv_effect_trick);
        //音乐
        iv_music_up = findViewById(R.id.tone_iv_music_up);
        iv_music_down = findViewById(R.id.tone_iv_music_down);

        tv_music_tone_up = findViewById(R.id.tone_tv_music_tone_up);
        tv_music_tone_default = findViewById(R.id.tone_tv_music_tone_default);
        tv_music_tone_down = findViewById(R.id.tone_tv_music_tone_down);
        tv_music_tone_down = findViewById(R.id.tone_tv_music_tone_down);

        //混音
        iv_effect_up = findViewById(R.id.tone_iv_effect_up);
        iv_effect_down = findViewById(R.id.tone_iv_effect_down);

        SerialPortCode serialPortCode = SerialController.getInstance().getSerialPortCode();
        if (serialPortCode != null) {
            //是否显示麦克风模式模块
            boolean isShowMicUpDown = !TextUtils.isEmpty(serialPortCode.MicVolUpD) || !TextUtils.isEmpty(serialPortCode.MicVolDownD);
            boolean isShowMicTone = !TextUtils.isEmpty(serialPortCode.MicPitchUpD) || !TextUtils.isEmpty(serialPortCode.MicPitchOriD)
                    || !TextUtils.isEmpty(serialPortCode.MicPitchDownD);
            //是否显示麦克风+-
            findViewById(R.id.tone_lin_mic_voice).setVisibility(isShowMicUpDown || isShowMicTone ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_iv_mic_up).setVisibility(isShowMicUpDown ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_iv_mic_down).setVisibility(!TextUtils.isEmpty(serialPortCode.MicVolDownD) ? View.VISIBLE : View.GONE);

            //是否显示麦克风升、降、原调
            findViewById(R.id.tone_lin_mic_tone).setVisibility(isShowMicTone ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_tv_mic_up).setVisibility(!TextUtils.isEmpty(serialPortCode.MicPitchUpD) ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_tv_mic_default).setVisibility(!TextUtils.isEmpty(serialPortCode.MicPitchOriD) ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_tv_mic_down).setVisibility(!TextUtils.isEmpty(serialPortCode.MicPitchDownD) ? View.VISIBLE : View.GONE);

            //是否显示音效模式模块
            boolean isShowEffectMode = !TextUtils.isEmpty(serialPortCode.EffectSingD) || !TextUtils.isEmpty(serialPortCode.EffectBlameD)
                    || !TextUtils.isEmpty(serialPortCode.EffectTrickD) || !TextUtils.isEmpty(serialPortCode.EffectHarmonyD);
            findViewById(R.id.tone_lin_effect).setVisibility(isShowEffectMode ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_tv_effect_sing).setVisibility(!TextUtils.isEmpty(serialPortCode.EffectSingD) ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_tv_effect_blame).setVisibility(!TextUtils.isEmpty(serialPortCode.EffectBlameD) ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_tv_effect_trick).setVisibility(!TextUtils.isEmpty(serialPortCode.EffectTrickD) ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_tv_effect_harmony).setVisibility(!TextUtils.isEmpty(serialPortCode.EffectHarmonyD) ? View.VISIBLE : View.GONE);

            //是否混响加减模块
            boolean isShowReverb = !TextUtils.isEmpty(serialPortCode.EffectUpD) || !TextUtils.isEmpty(serialPortCode.EffectDownD);
            findViewById(R.id.tone_lin_mix).setVisibility(isShowReverb ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_iv_effect_up).setVisibility(!TextUtils.isEmpty(serialPortCode.EffectUpD) ? View.VISIBLE : View.GONE);
            findViewById(R.id.tone_iv_effect_down).setVisibility(!TextUtils.isEmpty(serialPortCode.EffectDownD) ? View.VISIBLE : View.GONE);
            if (getWindow() != null) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.gravity = Gravity.CENTER;
                //如果调音有效果模块,dialog宽度不够显示,要加大
                if (isShowEffectMode && isShowMicUpDown && isShowMicTone && isShowReverb) {
                    lp.width = 720;
                } else if (isShowEffectMode && (isShowMicUpDown || isShowMicTone || isShowReverb)) {
                    lp.width = 650;
                } else if (!isShowMicUpDown && !isShowMicTone && !isShowReverb) {
                    padding_left.setVisibility(View.VISIBLE);
                    padding_right.setVisibility(View.VISIBLE);
                }
                lp.dimAmount = 0.7f;
                getWindow().setAttributes(lp);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
    }

    @Override
    public void show() {
        super.show();
        if (OkConfig.boxManufacturer() == 0) {
            UsbHubReseter.getInstance().resetUsb();
        }
    }

    @Override
    public void dismiss() {
        if (OkConfig.boxManufacturer() == 0) {//
            UsbHubReseter.getInstance().callback();
        }
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tone_iv_close:
                dismiss();
                break;
            case R.id.tone_iv_music_up:
                VodApplication.getKaraokeController().volumeUp();
                break;
            case R.id.tone_iv_music_down:
                VodApplication.getKaraokeController().volumeDown();
                break;
            case R.id.tone_iv_mic_up:
                if (OkConfig.boxManufacturer() == 0)
                    UsbHubReseter.getInstance().resetUsb();
                VodApplication.getKaraokeController().micUp(true);
                break;
            case R.id.tone_iv_mic_down:
                if (OkConfig.boxManufacturer() == 0)
                    UsbHubReseter.getInstance().resetUsb();
                VodApplication.getKaraokeController().micDown(true);
                break;
            case R.id.tone_tv_mic_down:
                VodApplication.getKaraokeController().micToneDown(true);
                break;
            case R.id.tone_tv_mic_default:
                VodApplication.getKaraokeController().micToneDefault(true);
                break;
            case R.id.tone_tv_mic_up:
                VodApplication.getKaraokeController().micToneUp(true);
                break;
            case R.id.tone_tv_music_tone_down:
                if (OkConfig.boxManufacturer() == 0)
                    UsbHubReseter.getInstance().resetUsb();
                VodApplication.getKaraokeController().toneDown();
                break;
            case R.id.tone_tv_music_tone_default:
                if (OkConfig.boxManufacturer() == 0)
                    UsbHubReseter.getInstance().resetUsb();
                VodApplication.getKaraokeController().toneDefault();
                break;
            case R.id.tone_tv_music_tone_up:
                if (OkConfig.boxManufacturer() == 0)
                    UsbHubReseter.getInstance().resetUsb();
                VodApplication.getKaraokeController().toneUp();
                break;
            case R.id.tone_tv_effect_sing://唱将
                VodApplication.getKaraokeController().effect(0, true);
                break;
            case R.id.tone_tv_effect_blame://整蛊
                VodApplication.getKaraokeController().effect(1, true);
                break;
            case R.id.tone_tv_effect_trick://搞怪
                VodApplication.getKaraokeController().effect(2, true);
                break;
            case R.id.tone_tv_effect_harmony://和声
                VodApplication.getKaraokeController().effect(3, true);
                break;
            case R.id.tone_iv_effect_down://混响-
                VodApplication.getKaraokeController().reverberation(0, true);
                break;
            case R.id.tone_iv_effect_up://混响+
                VodApplication.getKaraokeController().reverberation(1, true);
                break;
        }
    }
}
