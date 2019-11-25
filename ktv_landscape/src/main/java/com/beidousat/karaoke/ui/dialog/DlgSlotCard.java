package com.beidousat.karaoke.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.erp.SlotCardResult;
import com.bestarmedia.libcommon.util.ListUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.edit.EditTextEx;
import com.bestarmedia.libwidget.edit.NoImeEditText;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2016/6/23.
 */
public class DlgSlotCard extends BaseDialog implements View.OnClickListener, View.OnTouchListener, HttpRequestListener {

    private TextView mTvTitle;
    private TextView mBtnCancel, mBtnOk;
    private RecyclerView mRvKeyboardLine1, mRvKeyboardLine2, mRvKeyboardLine3, mRvNumber;
    private EditTextEx mEtAccount;
    private AdapterKeyboard AdtKeyboardLine1, AdtKeyboardLine2, AdtKeyboardLine3, AdtNumber;
    private VerticalDividerItemDecoration verDivider;
    private NoImeEditText mEtNum;
    private int mType;

    /**
     * @param context 上下文
     * @param type    1：上班；2：下班；3：取消
     */
    public DlgSlotCard(Context context, int type) {
        super(context, R.style.MyDialog);
        mType = type;
        init();
    }

    public HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(getContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    private OnClockListener mOnClockListener;

    public void setOnClockListener(OnClockListener listener) {
        this.mOnClockListener = listener;
    }

    public interface OnClockListener {

        void onClockSuccess(String title, String info);

        void onClockFail(String title, String err);
    }

    @Override
    public void onStart(String method) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_ERP_ROOM_CHECK.equalsIgnoreCase(method)) {
            mBtnOk.setEnabled(true);
            SlotCardResult slotCardResult;
            if (object instanceof SlotCardResult && (slotCardResult = (SlotCardResult) object).result != null) {
                if (slotCardResult.result.status == 1) {//成功
                    mEtAccount.setText("");
                    if (mOnClockListener != null) {
                        String title = getContext().getString(mType == 3 ? R.string.clock_cancel_success_desc :
                                (mType == 1 ? R.string.clock_on_success_desc : R.string.clock_off_success_desc));
                        mOnClockListener.onClockSuccess(title, slotCardResult.result.data != null && !TextUtils.isEmpty(slotCardResult.result.data.id) ?
                                (slotCardResult.result.data.id + "," + slotCardResult.result.data.name) : slotCardResult.result.info);
                    }
                } else {//失敗
                    if (mOnClockListener != null) {
                        String title = getContext().getString(mType == 3 ? R.string.clock_cancel_fail_desc :
                                (mType == 1 ? R.string.clock_on_fail_desc : R.string.clock_off_fail_desc));
                        mOnClockListener.onClockFail(title, slotCardResult.result.data != null && !TextUtils.isEmpty(slotCardResult.result.data.id) ?
                                (slotCardResult.result.data.id + "," + slotCardResult.result.data.name) : slotCardResult.result.info);
                    }
                }
            } else {
                if (mOnClockListener != null) {
                    String title = getContext().getString(mType == 3 ? R.string.clock_cancel_fail_desc :
                            (mType == 1 ? R.string.clock_on_fail_desc : R.string.clock_off_fail_desc));
                    mOnClockListener.onClockFail(title, "ERP未返回正确格式数据：无法解析");
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (RequestMethod.NODE_ERP_ROOM_CHECK.equalsIgnoreCase(method)) {
            String error = "";
            if (obj instanceof BaseModelV4) {
                BaseModelV4 baseModelV4 = (BaseModelV4) obj;
                error = baseModelV4.tips;
            } else if (obj instanceof String) {
                error = obj.toString();
            }
            mBtnOk.setEnabled(true);
            if (mOnClockListener != null) {
                String title = getContext().getString(mType == 3 ? R.string.clock_cancel_fail_desc :
                        (mType == 1 ? R.string.clock_on_fail_desc : R.string.clock_off_fail_desc));
                mOnClockListener.onClockFail(title, error);
            }
        }
    }

    @Override
    public void onError(String method, String error) {

    }

    private void postData(String card) {
        HttpRequestV4 r = initRequest(RequestMethod.NODE_ERP_ROOM_CHECK);
        r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        r.addParam("card", card);
        r.addParam("verify_mode", String.valueOf(mType - 1));
        r.setConvert2Class(SlotCardResult.class);
        r.get();
    }


    private void init() {
        this.setContentView(R.layout.dlg_slot_card);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = 750;
        lp.height = 460;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mEtNum = findViewById(R.id.et_num);

        mEtAccount = findViewById(android.R.id.edit);
        mEtAccount.setSelected(true);
        mEtAccount.setOnTouchListener(this);

//        mEtPwd = (EditTextEx) findViewById(android.R.id.input);
//        mEtPwd.setOnTouchListener(this);
//        mEtPwd.setSelected(false);

        mTvTitle = findViewById(android.R.id.title);
        mTvTitle.setText(mType == 3 ? R.string.clock_cancel : (mType == 1 ? R.string.clock_on : R.string.clock_off));

        mBtnCancel = findViewById(android.R.id.button1);
        mBtnCancel.setOnClickListener(this);
        mBtnOk = findViewById(android.R.id.button2);
        mBtnOk.setOnClickListener(this);

        mEtNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.toString().endsWith("\n")) {
                    String card = s.toString().replace("\n", "");
                    postData(card);
                    mEtNum.setText("");
                }
            }
        });

        mRvKeyboardLine1 = this.findViewById(R.id.rv_keyboard_line1);
        mRvKeyboardLine2 = this.findViewById(R.id.rv_keyboard_line2);
        mRvKeyboardLine3 = this.findViewById(R.id.rv_keyboard_line3);
        mRvNumber = this.findViewById(R.id.rv_number);

        int dividerWidth = DensityUtil.dip2px(getContext(), 2);

        verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(dividerWidth).margin(dividerWidth, dividerWidth).build();

        initKeyboard();
    }


    private void initKeyboard() {
        LinearLayoutManager layoutManagerNum = new LinearLayoutManager(getContext());
        layoutManagerNum.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvNumber.setLayoutManager(layoutManagerNum);
        mRvNumber.addItemDecoration(verDivider);
        AdtNumber = new AdapterKeyboard(true);
        mRvNumber.setAdapter(AdtNumber);
        AdtNumber.setData(ListUtil.array2List(getContext().getResources().getStringArray(R.array.keyboard_number)));

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvKeyboardLine3.setLayoutManager(layoutManager3);
        mRvKeyboardLine3.addItemDecoration(verDivider);
        AdtKeyboardLine3 = new AdapterKeyboard(false);
        mRvKeyboardLine3.setAdapter(AdtKeyboardLine3);
        AdtKeyboardLine3.setData(ListUtil.array2List(getContext().getResources().getStringArray(R.array.keyboard_keys_line3x)));


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvKeyboardLine2.setLayoutManager(layoutManager2);
        mRvKeyboardLine2.addItemDecoration(verDivider);
        AdtKeyboardLine2 = new AdapterKeyboard(false);
        mRvKeyboardLine2.setAdapter(AdtKeyboardLine2);
        AdtKeyboardLine2.setData(ListUtil.array2List(getContext().getResources().getStringArray(R.array.keyboard_keys_line2)));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvKeyboardLine1.setLayoutManager(layoutManager);
        mRvKeyboardLine1.addItemDecoration(verDivider);
        AdtKeyboardLine1 = new AdapterKeyboard(false);
        mRvKeyboardLine1.setAdapter(AdtKeyboardLine1);
        AdtKeyboardLine1.setData(ListUtil.array2List(getContext().getResources().getStringArray(R.array.keyboard_keys_line1)));
    }


    public class AdapterKeyboard extends BaseAdapter {

        private boolean mIsNumber;

        public AdapterKeyboard(boolean isNumber) {
            super();
            mIsNumber = isNumber;
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.list_item_keyboard_key, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvKey = view.findViewById(android.R.id.button1);
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
            holder.tvKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (!TextUtils.isEmpty(keyText)) {
                            if (keyText.equals(getContext().getString(R.string.delete))) {
                                deleteChar();
                            } else {
                                addText(keyText);
                            }
                        }
                    } catch (Exception e) {
                        Logger.e(getClass().getSimpleName(), e.toString());
                    }
                }
            });
        }
    }


    private void addText(String text) {
//        if (mEtAccount.isSelected()) {
        mEtAccount.setText(mEtAccount.getText() + text);
//        }
//        else if (mEtPwd.isSelected()) {
//            mEtPwd.setText(mEtPwd.getText() + text);
//        }
    }


    private void deleteChar() {
        try {
//            if (mEtAccount.isSelected()) {
            String text = mEtAccount.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                String txt = text.substring(0, text.length() - 1);
                mEtAccount.setText(txt);
            }
//            } else if (mEtPwd.isSelected()) {
//                String text = mEtPwd.getText().toString();
//                if (!TextUtils.isEmpty(text)) {
//                    String txt = text.substring(0, text.length() - 1);
//                    mEtPwd.setText(txt);
//                }
//            }
        } catch (Exception e) {
            Logger.e("WidgetKeyBoard", e.toString());
        }

    }


    public class BaseAdapter extends RecyclerView.Adapter<ViewHolder> {

        LayoutInflater mInflater;
        List<String> mData = new ArrayList<>();

        public BaseAdapter() {
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

        @NonNull
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
//            case android.R.id.input:
//                mEtPwd.setSelected(true);
//                mEtAccount.setSelected(false);
//                break;
            case android.R.id.edit:
//                mEtPwd.setSelected(false);
                mEtAccount.setSelected(true);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                dismiss();
                break;
            case android.R.id.button2:
                if (TextUtils.isEmpty(mEtAccount.getText())) {
                    Toast.makeText(getContext(), R.string.clock_card_or_input_card_number, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String account = mEtAccount.getText().toString();
                postData(account);
                mBtnOk.setEnabled(false);
                break;
            case android.R.id.edit:
                mEtAccount.setSelected(true);
                break;
            default:
                break;
        }
    }
}
