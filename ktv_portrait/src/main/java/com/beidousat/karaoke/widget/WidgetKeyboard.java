package com.beidousat.karaoke.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.KeyboardListener;
import com.beidousat.karaoke.ui.dialog.PromptDialogBig;
import com.beidousat.widget.handwritingjar.IHandwriting;
import com.bestarmedia.libcommon.util.ListUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libskin.SkinManager;
import com.bestarmedia.libwidget.edit.EditTextEx;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.PathClassLoader;

/**
 * Created by J Wong on 2015/10/9 08:56.
 */
public class WidgetKeyboard extends LinearLayout implements View.OnClickListener, View.OnTouchListener, EditTextEx.OnEditTextIconClickListener {

    private View mRootView;
    private EditTextEx mEditText;
    private SelfAbsoluteLayout tablet;
    private FrameLayout mFlHandwriting;
    private Context mContext;

    private RadioGroup mRgInputTypes;

    private RadioButton mRbTypePinyin, mRbTypeNumber;

    private View mTypeKeyboard, mTypeHandWriting;
    private RecyclerView mRvKeyboardLine1, mRvKeyboardLine2, mRvKeyboardLine3, mRvNumber, mRvWords, mRvWordCount;

    private AdapterWord mAdtWord;
    private AdapterKeyboard AdtKeyboardLine1, AdtKeyboardLine2, AdtKeyboardLine3, AdtNumber;

    private TextView mTvFeedback;

    private KeyboardListener mKeyboardListener;

    private String[] texts = new String[]{"", "", "", "", "", ""};

    private String mEnableText;

    private String mTextWord;

    private boolean tabletHaveWord = false;

    private VerticalDividerItemDecoration verDivider;

    private final static String TAG = WidgetKeyboard.class.getSimpleName();

    public WidgetKeyboard(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public WidgetKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
        readAttr(attrs);
    }

    public WidgetKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
        readAttr(attrs);
    }


    public void setWords(final String texts) {
        mTextWord = texts;
        tablet.postDelayed(runnableTexts, 1000);
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

    private void setWords(String[] words) {
        if (words != null && words.length > 0) {
            String[] texts = new String[6];
            for (int i = 0; i < texts.length; i++) {
                texts[i] = words.length > i ? words[i] : "";
            }
            mAdtWord.setData(ListUtil.array2List(texts));
            mAdtWord.notifyDataSetChanged();
        } else {
            resetWordsList();
        }
    }

    private boolean mNeedDisableKey = true;

    public void needDisableKey(boolean needDisableKey) {
        mNeedDisableKey = needDisableKey;
    }

    public void setKeyboardKeyEnableText(String enableText) {
        mEnableText = enableText;
        Logger.i(getClass().getSimpleName(), "setKeyboardKeyEnableText :" + mEnableText);
        AdtKeyboardLine1.notifyDataSetChanged();
        AdtKeyboardLine2.notifyDataSetChanged();
        AdtKeyboardLine3.notifyDataSetChanged();
        AdtNumber.notifyDataSetChanged();
    }


    private void initView() {

        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_keyboard, this);
        View root = mRootView.findViewById(R.id.root_keyboard);
        Drawable bgKeyboard = SkinManager.getInstance().getResourceManager().getDrawableByName("keyboard_bg");
        if (bgKeyboard != null) {
            root.setBackground(bgKeyboard);
        } else {
            root.setBackgroundResource(R.drawable.keyboard_bg);
        }

        mEditText = mRootView.findViewById(android.R.id.edit);
        mEditText.setOnEditTextIconClickListener(this);

        mRgInputTypes = findViewById(R.id.rg_input_types);
        mRbTypePinyin = findViewById(R.id.rb_pinyin);
        mRbTypeNumber = findViewById(R.id.rb_handwriting);
        mRgInputTypes.setOnClickListener(v -> {
            int id = mRgInputTypes.getCheckedRadioButtonId();
            if (id == R.id.rb_pinyin) {
                setInputType(1);
            } else {
                setInputType(0);
            }
        });

        mTvFeedback = findViewById(R.id.tv_feedback);
        mTvFeedback.setOnClickListener(this);

        mTypeKeyboard = this.findViewById(R.id.type_keyboard);
        mTypeHandWriting = this.findViewById(R.id.type_handwriting);

        mFlHandwriting = this.findViewById(R.id.tablet);

        loadSoFromLandscape();

        findViewById(R.id.iv_del).setOnClickListener(this);

        mRvWordCount = this.findViewById(R.id.rv_words_count);

        mRvKeyboardLine1 = this.findViewById(R.id.rv_keyboard_line1);
        mRvKeyboardLine2 = this.findViewById(R.id.rv_keyboard_line2);
        mRvKeyboardLine3 = this.findViewById(R.id.rv_keyboard_line3);
        mRvNumber = this.findViewById(R.id.rv_number);
        mRvWords = this.findViewById(R.id.rv_words);
        int dividerWidth = DensityUtil.dip2px(getContext(), 2);
        verDivider = new VerticalDividerItemDecoration.Builder(getContext()).color(Color.TRANSPARENT)
                .size(dividerWidth).margin(dividerWidth, dividerWidth).build();
        initKeyboard();
        initWordCount();
        initHandWritingWord();
        resetWordsList();
        tablet.setOnWriteListener(texts -> {
            tabletHaveWord = true;
            if (texts != null && texts.length > 0) {
                setWords(texts);
            } else {
                resetWordsList();
            }
        });
        tablet.setOnTouchListener((v, event) -> {
            Logger.i(TAG, "onTouch : " + event.getAction());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        });


        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mEditText.setSelection(s.length());
                if (mKeyboardListener != null) {
                    if (!(mIsCleanText && TextUtils.isEmpty(s)))
                        mKeyboardListener.onInputTextChanged(s.toString());
                }
            }
        });

        mEditText.setOnClickListener(this);
        mEditText.setOnTouchListener(this);
        mEditText.setInputType(mEditText.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    private IHandwriting mItest;

    private void loadSoFromLandscape() {
        Context context;
        try {
            context = getContext().createPackageContext("com.beidousat.karaoke",
                    Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
            String path = context.getApplicationInfo().sourceDir;
            // error
            PathClassLoader loader = new PathClassLoader(path, getContext().getClassLoader());
            // success
            // PathClassLoader loader = new PathClassLoader(path,
            // "/data/data/com.test.appb/lib", mContext.getClassLoader());
            @SuppressWarnings("unchecked")
            Class<IHandwriting> reflect = (Class<IHandwriting>) loader.loadClass("com.wwengine.hw.WWHandWrite");
            mItest = reflect.newInstance();
            mItest.init(getContext());
            tablet = new SelfAbsoluteLayout(context, null);
//            tablet.setBackgroundResource(R.drawable.bg_keyboard_hand_writing);
            mFlHandwriting.addView(tablet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initHandWritingWord() {
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(2).margin(2, 2).build();
        mRvWords.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvWords.addItemDecoration(horDivider);
        mAdtWord = new AdapterWord();
        mRvWords.setAdapter(mAdtWord);
    }

    private void initKeyboard() {
        LinearLayoutManager layoutManagerNum = new LinearLayoutManager(getContext());
        layoutManagerNum.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvNumber.setLayoutManager(layoutManagerNum);
        mRvNumber.addItemDecoration(verDivider);
        AdtNumber = new AdapterKeyboard();
        mRvNumber.setAdapter(AdtNumber);
        AdtNumber.setData(ListUtil.array2List(getResources().getStringArray(R.array.keyboard_number)));

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvKeyboardLine3.setLayoutManager(layoutManager3);
        mRvKeyboardLine3.addItemDecoration(verDivider);
        AdtKeyboardLine3 = new AdapterKeyboard();
        mRvKeyboardLine3.setAdapter(AdtKeyboardLine3);
        AdtKeyboardLine3.setData(ListUtil.array2List(getResources().getStringArray(R.array.keyboard_keys_line3x)));


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvKeyboardLine2.setLayoutManager(layoutManager2);
        mRvKeyboardLine2.addItemDecoration(verDivider);
        AdtKeyboardLine2 = new AdapterKeyboard();
        mRvKeyboardLine2.setAdapter(AdtKeyboardLine2);
        AdtKeyboardLine2.setData(ListUtil.array2List(getResources().getStringArray(R.array.keyboard_keys_line2)));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvKeyboardLine1.setLayoutManager(layoutManager);
        mRvKeyboardLine1.addItemDecoration(verDivider);
        AdtKeyboardLine1 = new AdapterKeyboard();
        mRvKeyboardLine1.setAdapter(AdtKeyboardLine1);
        AdtKeyboardLine1.setData(ListUtil.array2List(getResources().getStringArray(R.array.keyboard_keys_line1)));
    }

    private void initWordCount() {
//        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
//                .color(Color.TRANSPARENT).size(16).margin(8, 8).build();
        mRvWordCount.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        mRvWordCount.addItemDecoration(horDivider);
        AdapterWordCount adapter = new AdapterWordCount();
        mRvWordCount.setAdapter(adapter);
        adapter.setData(ListUtil.array2List(getResources().getStringArray(R.array.keyboard_word_count)));
    }

    public void showLackSongButton(boolean show) {
        mTvFeedback.setVisibility(show ? VISIBLE : GONE);
    }

    private void readAttr(AttributeSet attrs) {

        boolean showInputType = true;
        boolean showWordCount = true;
        int focusType = 0;

        String mStrHint = null;

        if (getContext() != null && attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WidgetKeyboard);
            mStrHint = a.getString(R.styleable.WidgetKeyboard_keyboard_input_hint);
            showInputType = a.getBoolean(R.styleable.WidgetKeyboard_show_input_type, true);
            focusType = a.getInt(R.styleable.WidgetKeyboard_keyboard_focus_input_type, 0);
            showWordCount = a.getBoolean(R.styleable.WidgetKeyboard_show_word_count, true);
            a.recycle();
        }

        if (mStrHint != null) {
            setHintText(mStrHint);
        }

        showInputType(showInputType);

        setInputType(focusType);

        showWordCount(showWordCount);

    }

    public void showWordCount(boolean show) {
        mRvWordCount.setVisibility(show ? VISIBLE : INVISIBLE);
    }

    private void showInputType(boolean show) {
        mRgInputTypes.setVisibility(show ? VISIBLE : INVISIBLE);
    }

    public void setHintText(String hint) {
        mEditText.setHint(hint);
    }

    public void setInputText(String text) {
        mEditText.setText(text);
    }


    private void resetWordsList() {
        mAdtWord.setData(ListUtil.array2List(texts));
        mAdtWord.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.edit:
                break;
            case R.id.iv_del:
                deleteChar();
                tablet.removeCallbacks(runnableTexts);
                break;
            case R.id.tv_feedback:
                PromptDialogBig promptDialogBig = new PromptDialogBig(mContext);
                promptDialogBig.setTitle(R.string.lack_song);
                promptDialogBig.setTips(R.string.lack_song_tips);
                promptDialogBig.setQrcode(R.drawable.qrcode);
                promptDialogBig.setOkButton(false, "", null);
                promptDialogBig.setCancleButton(false, "", null);
                promptDialogBig.show();
                break;
        }
    }


    public void setInputTextChangedListener(KeyboardListener listener) {
        this.mKeyboardListener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == mEditText) {
            EditText edittext = (EditText) view;
            int inType = edittext.getInputType();       // Backup the input type
            edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
            edittext.onTouchEvent(motionEvent);         // Call native handler
            edittext.setInputType(inType);              // Restore input type
            return true; // Consume touch event
        }
        return false;
    }

    private Handler handler = new Handler(msg -> {
        deleteChar();
        startDelChar();
        return false;
    });


    private Runnable runnable = () ->
            handler.sendEmptyMessage(0);

    private void startDelChar() {
        if (!TextUtils.isEmpty(mEditText.getText())) {
            handler.postDelayed(runnable, 200);
        }
    }

    private void stopDelChar() {
        handler.removeCallbacks(runnable);
    }

    public void deleteChar() {
        if (tabletHaveWord) {
            tablet.reset_recognize();
            tabletHaveWord = false;
        } else {
            try {
                String text = mEditText.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    String txt = text.substring(0, text.length() - 1);
                    mEditText.setText(txt);
                }
            } catch (Exception e) {
                Logger.e("WidgetKeyBoard", e.toString());
            }
        }
    }


    public void setEditTextVisible(boolean show) {
        mEditText.setVisibility(show ? VISIBLE : GONE);
    }


    public class BaseAdapter extends RecyclerView.Adapter<ViewHolder> {

        LayoutInflater mInflater;
        List<String> mData = new ArrayList<>();

        private BaseAdapter() {
            mInflater = LayoutInflater.from(getContext());
        }

        public void setData(List<String> data) {
            this.mData = data;
        }

        public String getItem(int position) {
            if (mData == null) {
                return null;
            } else {
                return mData.get(position);
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvKey;

        public ViewHolder(View view) {
            super(view);
        }
    }

    public class AdapterWord extends BaseAdapter {

        private AdapterWord() {
            super();
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.list_item_handwriting, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvKey = view.findViewById(android.R.id.text1);
            /**
             * 皮肤
             */
            Drawable dItemBackground = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_keyboard_handwriting_word_background");
            if (dItemBackground != null) {
                viewHolder.tvKey.setBackground(dItemBackground);
            }
            ColorStateList dTextColor = SkinManager.getInstance().getResourceManager().getColorStateList2("selector_keyboard_key_text");
            if (dTextColor != null) {
                viewHolder.tvKey.setTextColor(dTextColor);
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final String keyText = mData.get(position);
            holder.tvKey.setText(keyText);
            holder.tvKey.setOnTouchListener((v, event) -> {
                tablet.removeCallbacks(runnableTexts);
                return false;
            });
            holder.tvKey.setOnClickListener(view -> {
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


    public class AdapterKeyboard extends BaseAdapter {


        private AdapterKeyboard() {
            super();
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.list_item_keyboard_key, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvKey = view.findViewById(android.R.id.button1);
            /**
             * 皮肤
             */
            Drawable dItemBackground = SkinManager.getInstance().getResourceManager().getDrawableByName("selector_keyboard_key");
            if (dItemBackground != null) {
                viewHolder.tvKey.setBackground(dItemBackground);
            }
            ColorStateList dTextColor = SkinManager.getInstance().getResourceManager().getColorStateList2("selector_keyboard_key_text");
            if (dTextColor != null) {
                viewHolder.tvKey.setTextColor(dTextColor);
            }
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final String keyText = mData.get(position);
            holder.tvKey.setText(keyText);
            int width = DensityUtil.dip2px(getContext(), 46);
            if (keyText.equals(getContext().getString(R.string.delete))) {
                width = DensityUtil.dip2px(getContext(), 118);
            } else if (keyText.equals("A") || keyText.equals("L") || keyText.equals("Z")) {//
                width = DensityUtil.dip2px(getContext(), 70);
            }
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.tvKey.getLayoutParams();
            layoutParams.width = width;
            holder.tvKey.setLayoutParams(layoutParams);

            final boolean isEnable = mIsCleanText || TextUtils.isEmpty(mEditText.getText())
                    || keyText.equals(getContext().getString(R.string.delete))
                    || (!TextUtils.isEmpty(mEnableText) && (mEnableText.contains(keyText)
                    || mEnableText.contains(keyText.toLowerCase())));
            holder.tvKey.setEnabled(!mNeedDisableKey || isEnable);
            holder.tvKey.setOnClickListener(view -> {
                try {
                    if (!TextUtils.isEmpty(keyText)) {
                        if (keyText.equals(getContext().getString(R.string.delete))) {
                            deleteChar();
                        } else {
                            addText(keyText);
                        }
                    }
                } catch (Exception e) {
                    Logger.e("WidgetKeyBoard", e.toString());
                }
            });
        }
    }

    public class AdapterWordCount extends BaseAdapter {

        private int mFocusItemPs = 0;

        private AdapterWordCount() {
            super();
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.list_item_keyboard_word_count, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvKey = view.findViewById(android.R.id.button1);
            ColorStateList dTextColor = SkinManager.getInstance().getResourceManager().getColorStateList2("selector_keyboard_tab_text");
            if (dTextColor != null) {
                viewHolder.tvKey.setTextColor(dTextColor);
            }
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final String keyText = mData.get(position);
            holder.tvKey.setText(keyText);
            holder.tvKey.setSelected(mFocusItemPs == position);
            holder.tvKey.setOnClickListener(view -> {
                mFocusItemPs = position;
                if (mKeyboardListener != null) {
                    mKeyboardListener.onWordCountChanged(mFocusItemPs);
                }
                notifyDataSetChanged();
            });
        }
    }

    /**
     * @param inputType 0:keyboard 1:write 2:number
     */
    public void setInputType(int inputType) {
        switch (inputType) {
            case 0:
                mTypeKeyboard.setVisibility(VISIBLE);
                mTypeHandWriting.setVisibility(GONE);
                mFlHandwriting.setVisibility(GONE);
                mRgInputTypes.check(R.id.rb_pinyin);
                mRbTypeNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                mRbTypePinyin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                break;
            case 1:
                mTypeKeyboard.setVisibility(GONE);
                mTypeHandWriting.setVisibility(VISIBLE);
                mFlHandwriting.setVisibility(VISIBLE);
                mRgInputTypes.check(R.id.rb_handwriting);
                mRbTypeNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                mRbTypePinyin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                break;
            default:
                mTypeKeyboard.setVisibility(VISIBLE);
                mTypeHandWriting.setVisibility(GONE);
                mFlHandwriting.setVisibility(GONE);
                mRgInputTypes.check(R.id.rb_pinyin);
                mRbTypeNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                mRbTypePinyin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                break;
        }
    }

    private boolean mIsCleanText = false;

    public void addText(String text) {
        mEditText.setText(mEditText.getText() + text);
    }

    public void setCleanText(boolean cleanText) {
        mIsCleanText = cleanText;
        if (mIsCleanText) {
            setWords("");
            setKeyboardKeyEnableText("");
            setText("");
        }
    }

    public void setText(CharSequence text) {
        if (!TextUtils.equals(text, mEditText.getText())) {
            mEditText.setText(text);
        }
    }

    @Override
    public void onIconClick(View view, MotionEvent event) {
        mIsCleanText = false;
        if (mKeyboardListener != null) {
            mKeyboardListener.onInputTextChanged("");
        }
    }
}
