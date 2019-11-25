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

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.erp.ErpAccessResult;
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
public class DlgAccountPassword extends BaseDialog implements View.OnClickListener, View.OnTouchListener, HttpRequestListener {

    private Context mContext;
    private TextView mBtnCancel, mBtnOk;
    private RecyclerView mRvKeyboardLine1, mRvKeyboardLine2, mRvKeyboardLine3, mRvNumber;
    private EditTextEx mEtAccount, mEtPwd;
    private AdapterKeyboard AdtKeyboardLine1, AdtKeyboardLine2, AdtKeyboardLine3, AdtNumber;
    private VerticalDividerItemDecoration verDivider;
    private int mMode;
    private NoImeEditText mEtNum;
    private OnAccountPwdListener mOnAccountPwdListener;
    private String mAccount, mPassword;
    private boolean mVerifyPwd;

    public DlgAccountPassword(Context context, int mode) {
        super(context, R.style.MyDialog);
        mMode = mode;
        this.mContext = context;
        init();
    }

    public HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(getContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onStart(String method) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_ERP_ACCESS.equalsIgnoreCase(method)) {
            ErpAccessResult result = null;
            if (object instanceof ErpAccessResult && (result = (ErpAccessResult) object).result != null && result.result.status == 1) {
                mBtnOk.setEnabled(true);
                if (mOnAccountPwdListener != null) {
                    mOnAccountPwdListener.onAccountPwd(mAccount, mPassword, mVerifyPwd);
                }
                dismiss();
            } else {
                mBtnOk.setEnabled(true);
                String error = "";
                if (result != null && result.result != null) {
                    error = result.result.info;
                }
                Logger.d(getClass().getSimpleName(), "onSuccess :" + error);
                PromptDialogSmall promptDialog = new PromptDialogSmall(mContext);
                promptDialog.setTitle(R.string.prompt);
                promptDialog.setMessage(error);
                promptDialog.show();
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (RequestMethod.NODE_ERP_ACCESS.equalsIgnoreCase(method)) {
            mBtnOk.setEnabled(true);
            Logger.d(getClass().getSimpleName(), "onZmqRequestFail:" + R.string.account_verify_fail);
            PromptDialogSmall promptDialog = new PromptDialogSmall(mContext);
            promptDialog.setTitle(R.string.prompt);
            promptDialog.setMessage(R.string.account_verify_fail);
            promptDialog.show();
        }
    }

    @Override
    public void onError(String method, String error) {

    }

    private void verifyAccount(final String account, final String pwd, final boolean verifyPwd) {
        mAccount = account;
        mPassword = pwd;
        mVerifyPwd = verifyPwd;
        HttpRequestV4 request = initRequest(RequestMethod.NODE_ERP_ACCESS);
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.addParam("card", mAccount);
        request.addParam("password", mPassword);
        request.addParam("verify_mode", String.valueOf(mVerifyPwd ? 1 : 0));
        request.addParam("mode", String.valueOf(mMode));
        request.setConvert2Class(ErpAccessResult.class);
        request.get();

    }

    public void setOnAccountPwdListener(OnAccountPwdListener listener) {
        mOnAccountPwdListener = listener;
    }

    public interface OnAccountPwdListener {
        void onAccountPwd(String account, String pwd, boolean isVerifyPwd);
    }

    private void init() {
        this.setContentView(R.layout.dlg_order_submit);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = 750;
        lp.height = 360;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mEtNum = findViewById(R.id.et_num);

        mEtAccount = findViewById(android.R.id.edit);
        mEtAccount.setSelected(true);
        mEtAccount.setOnTouchListener(this);
        mEtPwd = findViewById(android.R.id.input);
        mEtPwd.setOnTouchListener(this);
        mEtPwd.setSelected(false);

        mBtnCancel = findViewById(android.R.id.button1);
        mBtnCancel.setOnClickListener(this);
        mBtnOk = findViewById(android.R.id.button2);
        mBtnOk.setOnClickListener(this);

        mEtNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Logger.d("DlgAccountPassword", "beforeTextChanged:" + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logger.d("DlgAccountPassword", "onTextChanged:" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.toString().endsWith("\n")) {
                    String card = s.toString().replace("\n", "");
                    Logger.d("DlgAccountPassword", "card no:" + card);
                    verifyAccount(card, "", false);
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


    private void addText(String text) {
        if (mEtAccount.isSelected()) {
            mEtAccount.setText(mEtAccount.getText() + text);
        } else if (mEtPwd.isSelected()) {
            mEtPwd.setText(mEtPwd.getText() + text);
        }
    }


    private void deleteChar() {
        try {
            if (mEtAccount.isSelected()) {
                String text = mEtAccount.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    String txt = text.substring(0, text.length() - 1);
                    mEtAccount.setText(txt);
                }
            } else if (mEtPwd.isSelected()) {
                String text = mEtPwd.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    String txt = text.substring(0, text.length() - 1);
                    mEtPwd.setText(txt);
                }
            }
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
            case android.R.id.input:
                mEtPwd.setSelected(true);
                mEtAccount.setSelected(false);
                break;
            case android.R.id.edit:
                mEtPwd.setSelected(false);
                mEtAccount.setSelected(true);
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
                final String account = mEtAccount.getText().toString();
                final String pwd = mEtPwd.getText().toString();
                verifyAccount(account, pwd, true);
                mBtnOk.setEnabled(false);
                break;
            case android.R.id.input:
                mEtPwd.setSelected(true);
                mEtAccount.setSelected(false);
                break;
            case android.R.id.edit:
                mEtPwd.setSelected(false);
                mEtAccount.setSelected(true);
                break;
        }
    }
}
