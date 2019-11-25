package com.beidousat.karaoke.ui.dialog.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.serialport.SerialController;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.vod.LightItem;
import com.bestarmedia.libcommon.model.vod.SerialPortCode;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.text.GradientTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by J Wong on 2015/10/12 15:52.
 */
public class DlgLightController extends BaseDialog implements View.OnClickListener {
    private OnLightControllerListener mOnLightControllerListener;
    public interface OnLightControllerListener {

        void onLightAuto(int isAuto);

        void onLightModeSelect(LightItem lightItem);

        void onHdmiBack(int isBack);

        void onLightUp(int isUp);

    }

    private RecyclerView mLvItems;
    private Activity mContext;
    private ToggleButton tgAutoLight, tgTelevision;
    private LinearLayout llLightAuto, llHdmiScreen;
    private GradientTextView tvLightUp, tvLightDown;
    private AdapterService adapter;

    public DlgLightController(Activity context) {
        super(context, R.style.MyDialog);
        initView(context);
    }

    public void initView(Activity context) {
        mContext = context;
        this.setContentView(R.layout.dlg_lights_conctroller);
        llLightAuto = findViewById(R.id.light_lin_auto);
        tgAutoLight = findViewById(R.id.light_tg_auto);
        llHdmiScreen = findViewById(R.id.light_lin_screen);
        tgTelevision = findViewById(R.id.light_tg_screen);
        tvLightUp = findViewById(R.id.light_tv_up);
        tvLightDown = findViewById(R.id.light_tv_down);
        mLvItems = findViewById(R.id.light_rv_effect);
        findViewById(R.id.riv_close).setOnClickListener(this);
        llLightAuto.setOnClickListener(this);
        llHdmiScreen.setOnClickListener(this);
        tvLightUp.setOnClickListener(this);
        tvLightDown.setOnClickListener(this);
        init();
    }

    @Override
    public void show() {
        EventBus.getDefault().register(this);
        super.show();
    }

    @Override
    public void dismiss() {
        EventBus.getDefault().unregister(this);
        super.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.Serial.LIGHT_MODE_CHANGED:
            case EventBusId.Id.BUTTON_STATUS_CHANGED:
                try {
                    Log.d(getClass().getSimpleName(), "当前灯光码值：" + VodApplication.getKaraokeController().getButtonStatus().currentLightCode);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "按钮状态处理异常：", e);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_close:
                dismiss();
                break;
            case R.id.light_lin_auto:
                int autoLight = VodApplication.getKaraokeController().getButtonStatus().isLightAuto;
                if (autoLight > 0) {
                    autoLight = 0;
                } else {
                    autoLight = 1;
                }
                findViewById(R.id.light_tv_auto).setSelected(autoLight == 1);
                tgAutoLight.setChecked(autoLight == 1);
                if (mOnLightControllerListener != null) {
                    mOnLightControllerListener.onLightAuto(autoLight);
                }
                break;
            case R.id.light_lin_screen:
                int isHdmiBlack = VodApplication.getKaraokeController().getButtonStatus().isHdmiBack;
                if (isHdmiBlack <= 0) {
                    isHdmiBlack = 1;
                } else {
                    isHdmiBlack = 0;
                }
                tgTelevision.setChecked(isHdmiBlack != 1);
                findViewById(R.id.light_tv_screen).setSelected(isHdmiBlack != 1);
                if (mOnLightControllerListener != null) {
                    mOnLightControllerListener.onHdmiBack(isHdmiBlack);
                }
                break;
            case R.id.light_tv_up:
                if (mOnLightControllerListener != null) {
                    mOnLightControllerListener.onLightUp(1);
                }
                break;
            case R.id.light_tv_down:
                if (mOnLightControllerListener != null) {
                    mOnLightControllerListener.onLightUp(0);
                }
                break;
        }

    }

    public void setOnLightControllerListener(OnLightControllerListener listener) {
        this.mOnLightControllerListener = listener;
    }

    private void init() {
        if (VodConfigData.getInstance().getServerConfig().autoLightEnable == 0) {
            llLightAuto.setVisibility(View.INVISIBLE);
        }
        SerialPortCode serialPortCode = SerialController.getInstance().getSerialPortCode();
        if(serialPortCode!=null){
            tvLightUp.setVisibility(TextUtils.isEmpty(serialPortCode.LightUpD)?View.GONE:View.VISIBLE);
            tvLightDown.setVisibility(TextUtils.isEmpty(serialPortCode.LightDownD)?View.GONE:View.VISIBLE);
        }
        tgTelevision.setChecked(VodApplication.getKaraokeController().getButtonStatus().isHdmiBack != 1);
        findViewById(R.id.light_tv_screen).setSelected(VodApplication.getKaraokeController().getButtonStatus().isHdmiBack != 1);
        tgAutoLight.setChecked(VodApplication.getKaraokeController().getButtonStatus().isLightAuto == 1);
        findViewById(R.id.light_tv_auto).setSelected(VodApplication.getKaraokeController().getButtonStatus().isLightAuto == 1);


        List<LightItem> lightItemList = SerialController.getInstance().getLightItems();
        if (lightItemList != null && lightItemList.size() > 0 && getWindow() != null) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            if (lightItemList.size() > 4) {
                lp.width = 720;
            }
            lp.gravity = Gravity.CENTER;
            lp.dimAmount = 0.7f;
            getWindow().setAttributes(lp);
            HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(mContext.getApplicationContext())
                    .color(Color.TRANSPARENT).size(10).build();
            VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(mContext.getApplicationContext())
                    .color(Color.TRANSPARENT).size(10).build();

            mLvItems.setLayoutManager(new GridLayoutManager(mContext.getApplicationContext(), (lightItemList.size() % 4) == 0 ? (lightItemList.size() / 4) : (lightItemList.size() / 4) + 1));
            mLvItems.addItemDecoration(verDivider);
            mLvItems.addItemDecoration(horDivider);
        }
        adapter = new AdapterService(getContext());
        adapter.setData(lightItemList);
        mLvItems.setAdapter(adapter);
    }


    private class AdapterService extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<LightItem> listEffect;

        private AdapterService(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public void setData(List<LightItem> listEffect) {
            this.listEffect = listEffect;
        }

        @Override
        public int getItemCount() {
            return listEffect.size();
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View view = mInflater.inflate(R.layout.list_item_light, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.textView = view.findViewById(R.id.dlg_light_effect);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i, @NonNull List<Object> payloads) {
            viewHolder.textView.setText(listEffect.get(i).name);
            //全关,背景图标改变
            if (listEffect.get(i).outCode.equals(SerialController.getInstance().getSerialPortCode().LightsOffD)) {
                viewHolder.textView.setBackgroundResource(R.drawable.selector_light_effect_bg_red);
                viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.selector_light_effect_red), null, null, null);
                viewHolder.textView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_text_color_red_item));
                viewHolder.textView.setHighlightColor(mContext.getResources().getColor(R.color.selector_text_colorhight_red_item));
            } else {
                viewHolder.textView.setBackgroundResource(R.drawable.selector_light_effect_bg_blue);
                viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.selector_light_effect_blue), null, null, null);
                viewHolder.textView.setTextColor(mContext.getResources().getColorStateList(R.color.selector_text_color_blue_item));
                viewHolder.textView.setHighlightColor(mContext.getResources().getColor(R.color.selector_text_colorhight_blue_item));
            }
            viewHolder.textView.setSelected(VodApplication.getKaraokeController().getButtonStatus().currentLightCode.equals(listEffect.get(i).outCode));
            viewHolder.textView.setOnClickListener(v -> {
                onLightMode(listEffect.get(i));
                notifyDataSetChanged();
            });
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private GradientTextView textView;

        public ViewHolder(View view) {
            super(view);
        }
    }

    private void onLightMode(LightItem lightItem) {
        if (mOnLightControllerListener != null) {
            mOnLightControllerListener.onLightModeSelect(lightItem);
        }
    }

}
