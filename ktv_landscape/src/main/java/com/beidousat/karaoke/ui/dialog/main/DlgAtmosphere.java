package com.beidousat.karaoke.ui.dialog.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.serialport.SerialController;
import com.beidousat.karaoke.ui.adapter.AdtBarrage;
import com.beidousat.karaoke.ui.adapter.AdtEmojiDetail;
import com.beidousat.karaoke.ui.adapter.AdtEmojiList;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.beidousat.karaoke.widget.SelfAbsoluteLayout;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.helper.EmojiHelper;
import com.bestarmedia.libcommon.interf.EmojiListener;
import com.bestarmedia.libcommon.model.vod.SerialPortCode;
import com.bestarmedia.libcommon.model.vod.emoji.Barrage;
import com.bestarmedia.libcommon.model.vod.emoji.Emoji;
import com.bestarmedia.libcommon.model.vod.emoji.EmojiDetail;
import com.bestarmedia.libcommon.util.ListUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.text.GradientTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class DlgAtmosphere extends BaseDialog implements View.OnClickListener, EmojiListener, AdtEmojiList.onClickEmojiListener, AdtEmojiDetail.onClickEmojiDetailListener, AdtBarrage.onCliceBarrage {
    private GradientTextView mRivWindAuto, mRivWindHigh, mRivWindMid, mRivWindLow;
    private GradientTextView mRivModeAuto, mRivModeCold, mRivModeHot, mRivModeWind;
    private RelativeLayout rl_emoji, rl_barrage, rl_room;
    private RecyclerImageView mRvTabEmoji, mRvTabBarrage, mRvTabRoom;
    private EmojiHelper emojiHelper;
    private RecyclerView recyclerViewEmojiList, recyclerViewEmojiDetail;
    private AdtEmojiList adtEmojiList;
    private AdtEmojiDetail adtEmojiDetail;
    private RecyclerImageView emoji_default;
    private List<EmojiDetail> emoji_detail_default;
    //弹幕
    private RecyclerView mRvBarrages, mRvWords;
    private EditText mEtInput;
    private SelfAbsoluteLayout tablet;
    private boolean tabletHaveWord = false;
    private AdtBarrage mAdtBarrages;
    private AdapterWord mAdtWord;
    private String mTextWord;
    //房间
    private ToggleButton tg_air_switch, tg_wind_switch;
    private TextView tv_air_switch, tv_wind_switch;

    public DlgAtmosphere(Activity context) {
        super(context, R.style.MyDialog);
        init();
    }

    @Override
    public void show() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        super.show();

    }

    @Override
    public void dismiss() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
//        if (event.id == EventBusId.ImId.AIR_CON_STATUS) {//                initStatus();
//        }
    }

    private void init() {
        this.setContentView(R.layout.dlg_atmosphere);
        if (getWindow() != null) {
            LayoutParams lp = getWindow().getAttributes();
            lp.width = 800;
            lp.height = 620;
            lp.gravity = Gravity.CENTER;
            lp.dimAmount = 0.7f;
            getWindow().setAttributes(lp);
        }
        initView();
        initBarrages();
        initHandWritingWord();
        initData();
        resetWordsList();
    }

    private void initView() {
        findViewById(R.id.riv_close).setOnClickListener(this);
        //表情
        emoji_default = findViewById(R.id.emoji_default);
        emoji_default.setOnClickListener(this);
        mRvTabEmoji = findViewById(R.id.rv_emoji);
        mRvTabEmoji.setOnClickListener(this);
        mRvTabBarrage = findViewById(R.id.rv_barrage);
        mRvTabBarrage.setOnClickListener(this);
        mRvTabRoom = findViewById(R.id.rv_room);
        mRvTabRoom.setOnClickListener(this);
        rl_emoji = findViewById(R.id.rl_emoji);
        rl_barrage = findViewById(R.id.rl_barrage);
        rl_room = findViewById(R.id.rl_room);
        //弹幕
        mRvBarrages = findViewById(R.id.rv_barrages);
        mRvWords = findViewById(R.id.rv_words);
        mEtInput = findViewById(R.id.et_text);
        tablet = findViewById(R.id.tablet);
        tablet.setOnWriteListener(texts -> {
            tabletHaveWord = true;
            if (texts != null && texts.length > 0) {
                setWords(texts);
            } else {
                resetWordsList();
            }
        });
        tablet.setBackgroundResource(R.drawable.bg_mood_barrage_writing);
        findViewById(R.id.riv_del).setOnClickListener(this);
        findViewById(R.id.tv_send).setOnClickListener(this);
        //房间
        findViewById(R.id.ll_air_switcher).setOnClickListener(this);
        findViewById(R.id.ll_wind_switcher).setOnClickListener(this);
        findViewById(R.id.room_effect_bubble).setOnClickListener(this);
        findViewById(R.id.room_effect_smoke).setOnClickListener(this);
        findViewById(R.id.air_temp_up).setOnClickListener(this);
        findViewById(R.id.air_temp_down).setOnClickListener(this);
        tg_air_switch = findViewById(R.id.tg_air_switcher);
        tg_wind_switch = findViewById(R.id.tg_wind_switcher);
        tv_air_switch = findViewById(R.id.tv_air_switcher);
        tv_wind_switch = findViewById(R.id.tv_wind_switcher);
        mRivModeAuto = findViewById(R.id.air_mode_auto);
        mRivModeAuto.setOnClickListener(this);
        mRivModeCold = findViewById(R.id.air_mode_cool);
        mRivModeCold.setOnClickListener(this);
        mRivModeHot = findViewById(R.id.air_mode_hot);
        mRivModeHot.setOnClickListener(this);
        mRivModeWind = findViewById(R.id.air_mode_wind);
        mRivModeWind.setOnClickListener(this);

        mRivWindAuto = findViewById(R.id.wind_mode_auto);
        mRivWindAuto.setOnClickListener(this);
        mRivWindHigh = findViewById(R.id.wind_mode_high);
        mRivWindHigh.setOnClickListener(this);
        mRivWindMid = findViewById(R.id.wind_mode_mid);
        mRivWindMid.setOnClickListener(this);
        mRivWindLow = findViewById(R.id.wind_mode_low);
        mRivWindLow.setOnClickListener(this);
        SerialPortCode serialPortCode = SerialController.getInstance().getSerialPortCode();
        if (serialPortCode != null) {
            boolean isShowSwitcher = !TextUtils.isEmpty(serialPortCode.AirConOffD) || !TextUtils.isEmpty(serialPortCode.AirConOnD);
            findViewById(R.id.ll_air_switcher).setVisibility(isShowSwitcher ? View.VISIBLE : View.INVISIBLE);

            //是否显示空调
            boolean isShowMode = !TextUtils.isEmpty(serialPortCode.AirConModeAutoD) || !TextUtils.isEmpty(serialPortCode.AirConModeCoolD)
                    || !TextUtils.isEmpty(serialPortCode.AirConModeHotD) || !TextUtils.isEmpty(serialPortCode.AirConModeWindD);
            boolean isShowWindMode = !TextUtils.isEmpty(serialPortCode.AirConWindAutoD) || !TextUtils.isEmpty(serialPortCode.AirConWindHighD)
                    || !TextUtils.isEmpty(serialPortCode.AirConWindMidD) || !TextUtils.isEmpty(serialPortCode.AirConWindLowD);
            boolean isTemp = !TextUtils.isEmpty(serialPortCode.AirConTempUpD) || !TextUtils.isEmpty(serialPortCode.AirConTempDownD);

            findViewById(R.id.ll_air_mode).setVisibility(isShowMode ? View.VISIBLE : View.GONE);
            mRivModeAuto.setVisibility(!TextUtils.isEmpty(serialPortCode.AirConModeAutoD) ? View.VISIBLE : View.GONE);
            mRivModeCold.setVisibility(!TextUtils.isEmpty(serialPortCode.AirConModeCoolD) ? View.VISIBLE : View.GONE);
            mRivModeHot.setVisibility(!TextUtils.isEmpty(serialPortCode.AirConModeHotD) ? View.VISIBLE : View.GONE);
            mRivModeWind.setVisibility(!TextUtils.isEmpty(serialPortCode.AirConModeWindD) ? View.VISIBLE : View.GONE);

            findViewById(R.id.ll_wind_mode).setVisibility(isShowWindMode ? View.VISIBLE : View.GONE);
            mRivWindAuto.setVisibility(!TextUtils.isEmpty(serialPortCode.AirConWindAutoD) ? View.VISIBLE : View.GONE);
            mRivWindHigh.setVisibility(!TextUtils.isEmpty(serialPortCode.AirConWindHighD) ? View.VISIBLE : View.GONE);
            mRivWindMid.setVisibility(!TextUtils.isEmpty(serialPortCode.AirConWindMidD) ? View.VISIBLE : View.GONE);
            mRivWindLow.setVisibility(!TextUtils.isEmpty(serialPortCode.AirConWindLowD) ? View.VISIBLE : View.GONE);

            findViewById(R.id.ll_temp_mode).setVisibility(isTemp ? View.VISIBLE : View.GONE);
            findViewById(R.id.air_temp_up).setVisibility(!TextUtils.isEmpty(serialPortCode.AirConTempUpD) ? View.VISIBLE : View.GONE);
            findViewById(R.id.air_temp_down).setVisibility(!TextUtils.isEmpty(serialPortCode.AirConTempDownD) ? View.VISIBLE : View.GONE);
            //是否显示排风
            boolean isShowWind = !TextUtils.isEmpty(serialPortCode.AirConAirOnD) || !TextUtils.isEmpty(serialPortCode.AirConAirOffD);
            //是否显示泡泡机
            boolean isShowBubble = !TextUtils.isEmpty(serialPortCode.BubbleD);
            //是否显示烟雾机
            boolean isShowSmoke = !TextUtils.isEmpty(serialPortCode.SmokeD);

            findViewById(R.id.ll_air).setVisibility(isShowMode || isShowWindMode || isTemp ? View.VISIBLE : View.GONE);
            findViewById(R.id.rl_wind).setVisibility(isShowWind ? View.VISIBLE : View.GONE);

            findViewById(R.id.room_effect_bubble).setVisibility(isShowBubble ? View.VISIBLE : View.GONE);
            findViewById(R.id.room_effect_smoke).setVisibility(isShowSmoke ? View.VISIBLE : View.GONE);
            findViewById(R.id.rl_effect).setVisibility(!isShowBubble && !isShowSmoke ? View.GONE : View.VISIBLE);

            mRvTabRoom.setVisibility(isShowMode || isShowWindMode || isTemp || isShowWind || isShowBubble || isShowSmoke ? View.VISIBLE : View.INVISIBLE);
        }

    }

    private void initData() {
        //表情包列表
        recyclerViewEmojiList = findViewById(R.id.dlg_atmosphere_emoji_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewEmojiList.setLayoutManager(linearLayoutManager);
        adtEmojiList = new AdtEmojiList(getContext());
        adtEmojiList.setOnClickEmojiListener(this);
        recyclerViewEmojiList.setAdapter(adtEmojiList);
//        //表情包详细
        recyclerViewEmojiDetail = findViewById(R.id.dlg_atmosphere_emoji_detail);
        recyclerViewEmojiDetail.setLayoutManager(new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false));
        recyclerViewEmojiDetail.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        recyclerViewEmojiDetail.post(() -> adtEmojiDetail.notifyDataSetChanged());
                    }
                }
        );
        adtEmojiDetail = new AdtEmojiDetail(getContext());
        adtEmojiDetail.setOnClickEmojiDetailListener(this);
        recyclerViewEmojiDetail.setAdapter(adtEmojiDetail);
        emojiHelper = new EmojiHelper(getContext());
        emojiHelper.setEmojiListener(this);
        emojiHelper.getEmoji();
        setTab(0);
        emoji_default.setSelected(true);
        emoji_detail_default = new ArrayList<>();
        emoji_detail_default.add(new EmojiDetail(0, R.drawable.dlg_mood01));
        emoji_detail_default.add(new EmojiDetail(1, R.drawable.dlg_mood02));
        emoji_detail_default.add(new EmojiDetail(2, R.drawable.dlg_mood03));
        emoji_detail_default.add(new EmojiDetail(3, R.drawable.dlg_mood04));
        emoji_detail_default.add(new EmojiDetail(4, R.drawable.dlg_mood05));
        adtEmojiDetail.addData(emoji_detail_default);
    }

    private void setTab(int position) {
        switch (position) {
            case 0:
                mRvTabEmoji.setSelected(true);
                mRvTabBarrage.setSelected(false);
                mRvTabRoom.setSelected(false);
                rl_emoji.setVisibility(View.VISIBLE);
                rl_barrage.setVisibility(View.GONE);
                rl_room.setVisibility(View.GONE);
                break;
            case 1:
                mRvTabEmoji.setSelected(false);
                mRvTabBarrage.setSelected(true);
                mRvTabRoom.setSelected(false);
                rl_emoji.setVisibility(View.GONE);
                rl_barrage.setVisibility(View.VISIBLE);
                rl_room.setVisibility(View.GONE);
                break;
            case 2:
                mRvTabEmoji.setSelected(false);
                mRvTabBarrage.setSelected(false);
                mRvTabRoom.setSelected(true);
                rl_emoji.setVisibility(View.GONE);
                rl_barrage.setVisibility(View.GONE);
                rl_room.setVisibility(View.VISIBLE);
                initStatus();
                break;
        }
    }

    private void initStatus() {
        tg_air_switch.setChecked(VodApplication.getKaraokeController().getAirConStatus().airConOpen);
        tg_wind_switch.setChecked(VodApplication.getKaraokeController().getAirConStatus().windOpen);
        switch (VodApplication.getKaraokeController().getAirConStatus().mode) {
            case 0:
                mRivModeAuto.setSelected(true);
                mRivModeCold.setSelected(false);
                mRivModeHot.setSelected(false);
                mRivModeWind.setSelected(false);
                break;
            case 1:
                mRivModeAuto.setSelected(false);
                mRivModeCold.setSelected(true);
                mRivModeHot.setSelected(false);
                mRivModeWind.setSelected(false);
                break;
            case 2:
                mRivModeAuto.setSelected(false);
                mRivModeCold.setSelected(false);
                mRivModeHot.setSelected(true);
                mRivModeWind.setSelected(false);
                break;
            case 3:
                mRivModeAuto.setSelected(false);
                mRivModeCold.setSelected(false);
                mRivModeHot.setSelected(false);
                mRivModeWind.setSelected(true);
                break;
        }

        switch (VodApplication.getKaraokeController().getAirConStatus().windSpeed) {
            case 0:
                mRivWindAuto.setSelected(true);
                mRivWindHigh.setSelected(false);
                mRivWindMid.setSelected(false);
                mRivWindLow.setSelected(false);
                break;
            case 1:
                mRivWindAuto.setSelected(false);
                mRivWindHigh.setSelected(true);
                mRivWindMid.setSelected(false);
                mRivWindLow.setSelected(false);
                break;
            case 2:
                mRivWindAuto.setSelected(false);
                mRivWindHigh.setSelected(false);
                mRivWindMid.setSelected(true);
                mRivWindLow.setSelected(false);
                break;
            case 3:
                mRivWindAuto.setSelected(false);
                mRivWindHigh.setSelected(false);
                mRivWindMid.setSelected(false);
                mRivWindLow.setSelected(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_close:
                dismiss();
                break;
            case R.id.rv_emoji:
                setTab(0);
                break;
            case R.id.rv_barrage:
                setTab(1);
                emojiHelper.getBarrage();
                break;
            case R.id.rv_room:
                setTab(2);
                break;
            case R.id.tv_send:
                if (VodApplication.getKaraokeController() != null && !TextUtils.isEmpty(mEtInput.getText())) {
                    VodApplication.getKaraokeController().onBarrage(mEtInput.getText().toString());
                    mEtInput.setText("");
                }
                break;
            case R.id.riv_del:
                deleteChar();
                break;
            case R.id.emoji_default:
                emoji_default.setSelected(true);
                adtEmojiList.setSelect_emoji(null);
                adtEmojiList.notifyDataSetChanged();
                adtEmojiDetail.addData(emoji_detail_default);
                break;
            case R.id.ll_air_switcher:
                if (tg_air_switch.isChecked()) {
                    tv_air_switch.setSelected(false);
                    tg_air_switch.setChecked(false);
                } else {
                    tg_air_switch.setChecked(true);
                    tv_air_switch.setSelected(true);
                }
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().onAirConSwitch(tg_air_switch.isChecked());
                }
                break;
            case R.id.ll_wind_switcher:
                if (tg_wind_switch.isChecked()) {
                    tg_wind_switch.setChecked(false);
                    tv_wind_switch.setSelected(false);
                } else {
                    tg_wind_switch.setChecked(true);
                    tv_wind_switch.setSelected(true);
                }
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().onWindSwitch(tg_wind_switch.isChecked());
                }
                break;
            case R.id.room_effect_smoke:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().onSmoke();
                }
                break;
            case R.id.room_effect_bubble:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().onBubble();
                }
                break;
            case R.id.air_temp_up:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().onTempUpDown(true);
                }
                break;
            case R.id.air_temp_down:
                if (VodApplication.getKaraokeController() != null) {
                    VodApplication.getKaraokeController().onTempUpDown(false);
                }
                break;
            case R.id.air_mode_auto:
                VodApplication.getKaraokeController().onAirConMode(0);
                mRivModeAuto.setSelected(true);
                mRivModeCold.setSelected(false);
                mRivModeHot.setSelected(false);
                mRivModeWind.setSelected(false);
                break;
            case R.id.air_mode_cool:
                VodApplication.getKaraokeController().onAirConMode(1);
                mRivModeAuto.setSelected(false);
                mRivModeCold.setSelected(true);
                mRivModeHot.setSelected(false);
                mRivModeWind.setSelected(false);
                break;
            case R.id.air_mode_hot:
                VodApplication.getKaraokeController().onAirConMode(2);
                mRivModeAuto.setSelected(false);
                mRivModeCold.setSelected(false);
                mRivModeHot.setSelected(true);
                mRivModeWind.setSelected(false);
                break;
            case R.id.air_mode_wind:
                VodApplication.getKaraokeController().onAirConMode(3);
                mRivModeAuto.setSelected(false);
                mRivModeCold.setSelected(false);
                mRivModeHot.setSelected(false);
                mRivModeWind.setSelected(true);
                break;
            case R.id.wind_mode_auto:
                VodApplication.getKaraokeController().onAirWindSpeed(0);
                mRivWindAuto.setSelected(true);
                mRivWindHigh.setSelected(false);
                mRivWindMid.setSelected(false);
                mRivWindLow.setSelected(false);
                break;
            case R.id.wind_mode_high:
                VodApplication.getKaraokeController().onAirWindSpeed(1);
                mRivWindAuto.setSelected(false);
                mRivWindHigh.setSelected(true);
                mRivWindMid.setSelected(false);
                mRivWindLow.setSelected(false);
                break;
            case R.id.wind_mode_mid:
                VodApplication.getKaraokeController().onAirWindSpeed(2);
                mRivWindAuto.setSelected(false);
                mRivWindHigh.setSelected(false);
                mRivWindMid.setSelected(true);
                mRivWindLow.setSelected(false);
                break;
            case R.id.wind_mode_low:
                VodApplication.getKaraokeController().onAirWindSpeed(3);
                mRivWindAuto.setSelected(false);
                mRivWindHigh.setSelected(false);
                mRivWindMid.setSelected(false);
                mRivWindLow.setSelected(true);
                break;
        }
    }


    @Override
    public void onEmoji(List<Emoji> emojiList) {
        if (emojiList != null && emojiList.size() > 0) {
            adtEmojiList.addData(emojiList);
        }
    }

    @Override
    public void onEmojiDetial(List<EmojiDetail> emojiDetailList) {
        adtEmojiDetail.addData(emojiDetailList);
    }

    @Override
    public void onBarrage(List<Barrage> barrageList) {
        mAdtBarrages.addData(barrageList);
    }

    @Override
    public void onClick(Emoji emoji) {
        emoji_default.setSelected(false);
        emojiHelper.getEmojiDetail(emoji.id);
    }

    @Override
    public void onClick(EmojiDetail emojiDetail) {
        if (emojiDetail.drawableId != 0) {
            if (VodApplication.getKaraokeController() != null) {
                VodApplication.getKaraokeController().atmosphere(emojiDetail.mode, true);
            }
        } else {
            if (VodApplication.getKaraokeController() != null) {
                VodApplication.getKaraokeController().emojiDynamic(emojiDetail.fileUrl != null ? emojiDetail.fileUrl : emojiDetail.filePath);
            }
        }
    }

    private void initBarrages() {
        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(4).margin(4, 4).build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvBarrages.setLayoutManager(layoutManager);
        mRvBarrages.addItemDecoration(verDivider);
        mAdtBarrages = new AdtBarrage(getContext());
        mAdtBarrages.setOnCliceBarrage(this);
        mRvBarrages.setAdapter(mAdtBarrages);
    }

    private void initHandWritingWord() {
        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(4).margin(4, 4).build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvWords.setLayoutManager(layoutManager);
        mRvWords.addItemDecoration(verDivider);
        mAdtWord = new AdapterWord(getContext());
        mRvWords.setAdapter(mAdtWord);
    }

    private void addText(String text) {
        mEtInput.setText(mEtInput.getText() + text);
    }

    private void deleteChar() {
        if (tabletHaveWord) {
            tablet.reset_recognize();
            tabletHaveWord = false;
        } else {
            try {
                String text = mEtInput.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    String txt = text.substring(0, text.length() - 1);
                    mEtInput.setText(txt);
                }
            } catch (Exception e) {
                Logger.e(getClass().getSimpleName(), e.toString());
            }
        }
    }

    private void setWords(String[] words) {
        if (words != null && words.length > 0) {
            String[] texts = new String[5];
            for (int i = 0; i < texts.length; i++) {
                texts[i] = words.length > i ? words[i] : "";
            }
            mAdtWord.addData(ListUtil.array2List(texts));
            mAdtWord.notifyDataSetChanged();
        } else {
            resetWordsList();
        }
    }

    private String[] texts = new String[]{"", "", "", "", ""};

    private void resetWordsList() {
        mAdtWord.addData(ListUtil.array2List(texts));
        mAdtWord.notifyDataSetChanged();
    }

    private Runnable runnableTexts = new Runnable() {
        @Override
        public void run() {
            if (!TextUtils.isEmpty(mTextWord)) {
                if (!mTextWord.contains(",")) {
                    String[] text = new String[]{mTextWord};
                    setWords(text);
                } else {
                    String[] words = mTextWord.split(",");
                    setWords(words);
                }
            } else {
                resetWordsList();
            }
        }
    };

    @Override
    public void onClickBarrage(Barrage barrage) {
        if (VodApplication.getKaraokeController() != null) {
            VodApplication.getKaraokeController().onBarrage(barrage.content);
        }
    }


    public class AdapterWord extends RecyclerView.Adapter<AdapterWord.ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<String> mData = new ArrayList<>();

        private AdapterWord(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        private void addData(List<String> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            private ViewHolder(View view) {
                super(view);
                textView = view.findViewById(android.R.id.text1);
            }
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.list_item_handwriting_atmosphere, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
            final String keyText = mData.get(i);
            viewHolder.textView.setText(keyText);
            viewHolder.textView.setOnTouchListener((v, event) -> {
                tablet.removeCallbacks(runnableTexts);
                return false;
            });
            viewHolder.textView.setOnClickListener(v -> {
                try {
                    if (!TextUtils.isEmpty(keyText)) {
                        addText(keyText);
                        tablet.reset_recognize();
                        tabletHaveWord = false;
                    }
                } catch (Exception e) {
                    Logger.e("WidgetKeyBoard", e.toString());
                }
            });
        }

    }

}
