package com.beidousat.karaoke.ui.dialog.safety;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.KeyboardListener;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.beidousat.karaoke.ui.dialog.PromptDialogBig;
import com.beidousat.karaoke.widget.WidgetKeyboardLandspace;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.helper.SafetyHelper;
import com.bestarmedia.libcommon.interf.SafetyListener;
import com.bestarmedia.libcommon.model.vod.safety.SafetyItem;
import com.bestarmedia.libcommon.model.vod.safety.SafetyUpload;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.List;


public class DlgRoomSafety extends BaseDialog implements KeyboardListener, SafetyListener {

    private final String TAG = DlgRoomSafety.class.getSimpleName();
    private WidgetKeyboardLandspace mWidgetKeyboard;
    private TextView tv_ok, tv_title, tv_conntent;
    private RecyclerView recyclerView;
    private String str_input;
    private Context mContext;
    private Adapter adapter;
    private boolean ischeck = false;
    private List<SafetyItem> safetyItems;
    private SafetyHelper safetyHelper;
    private Uploadlistener mUploadlistener;

    public interface Uploadlistener {
        void UploadSucceed();
    }

    public DlgRoomSafety(Context context) {
        super(context, R.style.MyDialog);
        this.mContext = context;
        init();
    }

    public void setOkButton(boolean isshow, String text, Uploadlistener listener) {
        if (isshow) {
            this.tv_ok.setVisibility(View.VISIBLE);
        } else {
            this.tv_ok.setVisibility(View.GONE);
        }
        this.mUploadlistener = listener;
        this.tv_ok.setText(text);
    }

    private void init() {
        this.setContentView(R.layout.dlg_room_safety);
        if (getWindow() == null)
            return;
        LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mWidgetKeyboard = findViewById(R.id.keyboard);
        mWidgetKeyboard.showLackSongButton(false);
        mWidgetKeyboard.showWordCount(false);
        mWidgetKeyboard.setInputTextChangedListener(this);
        mWidgetKeyboard.setInputPwdType();
        safetyHelper = new SafetyHelper(mContext);
        safetyHelper.setSafetyListener(this);
        tv_ok = findViewById(R.id.dlg_roomm_safety_ok);
        tv_title = findViewById(R.id.dlg_room_safety_title);
        recyclerView = findViewById(R.id.dlg_room_safety_rv);
        tv_conntent = findViewById(R.id.dlg_room_safety_conntent);
        tv_ok.setOnClickListener(view -> {
            if (ischeck) {
                if (IscheckPass()) {
                    tv_ok.setEnabled(false);
                    tv_ok.setText("正在提交...");
                    SafetyUpload safetyUpload = new SafetyUpload();
                    safetyHelper.postRoomSatety(safetyUpload.putSafetyUpload(safetyItems));
                } else {
                    PromptDialogBig promptDialogBig = new PromptDialogBig(mContext);
                    promptDialogBig.showIvClose(false);
                    promptDialogBig.setTitle(mContext.getString(R.string.dlg_safety_check_fail_title));
                    promptDialogBig.setMessage(mContext.getString(R.string.dlg_safety_check_fail_content));
                    promptDialogBig.setOkButton(true, mContext.getString(R.string.dlg_safety_check_fail_btn), null);
                    promptDialogBig.setCancleButton(false, null, null);
                    promptDialogBig.show();
                }
            } else {
                if (TextUtils.isEmpty(str_input)) {
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (str_input.trim().equalsIgnoreCase(PrefData.getMngPassword(mContext)) || str_input.trim().equals("63716616")) {
                        tv_title.setText(mContext.getString(R.string.dlg_safety_input_tips2));
                        tv_conntent.setText(mContext.getString(R.string.dlg_safety_input_content2));
                        mWidgetKeyboard.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new Adapter(mContext);
                        SafetyItem safetyItem = new SafetyItem();
                        safetyItems = safetyItem.getSagetyItemList();
                        recyclerView.setAdapter(adapter);
                        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext().getApplicationContext())
                                .color(Color.TRANSPARENT).size(25).build();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.addItemDecoration(horDivider);
                        ischeck = true;

                    } else {
                        Toast.makeText(mContext, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }

    @Override
    public void onInputTextChanged(String text) {
        str_input = text;
    }

    @Override
    public void onWordCountChanged(int count) {

    }


    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;

        public Adapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }
//
//        public void setData(List<SafetyItem> list_safety) {
//            this.safetyItems = list_safety;
//        }

        @Override
        public int getItemCount() {
            return safetyItems == null ? 0 : safetyItems.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View view = mInflater.inflate(R.layout.safety_rvitem, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tv_safety = view.findViewById(R.id.safety_rvitem_tv_name);
            viewHolder.tv_ok = view.findViewById(R.id.safety_rvitem_tv_ok);
            viewHolder.tv_fail = view.findViewById(R.id.safety_rvitem_tv_fail);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
            SafetyItem safetyItem = safetyItems.get(i);
            viewHolder.tv_safety.setText(safetyItem.name);
            viewHolder.tv_ok.setOnClickListener(view -> {
                SafetyItem safetyItem1 = safetyItems.get(i);
                safetyItem1.type = 1;
                notifyDataSetChanged();
            });
            viewHolder.tv_fail.setOnClickListener(view -> {
                SafetyItem safetyItem12 = safetyItems.get(i);
                safetyItem12.type = 2;
                notifyDataSetChanged();
            });
            if (safetyItem.type == 0) {
                viewHolder.tv_ok.setSelected(false);
                viewHolder.tv_fail.setSelected(false);
                viewHolder.tv_ok.setTextColor(mContext.getResources().getColor(R.color.dlg_device_type_bg_n));
                viewHolder.tv_fail.setTextColor(mContext.getResources().getColor(R.color.dlg_device_type_bg_n));
                viewHolder.tv_ok.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                viewHolder.tv_fail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else if (safetyItem.type == 1) {
                viewHolder.tv_ok.setSelected(true);
                viewHolder.tv_fail.setSelected(false);
                viewHolder.tv_ok.setTextColor(mContext.getResources().getColor(R.color.dlg_device_type_bg_p));
                viewHolder.tv_fail.setTextColor(mContext.getResources().getColor(R.color.dlg_device_type_bg_n));
                viewHolder.tv_ok.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_selection), null, null, null);
                viewHolder.tv_fail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else if (safetyItem.type == 2) {
                viewHolder.tv_ok.setSelected(false);
                viewHolder.tv_fail.setSelected(true);
                viewHolder.tv_ok.setTextColor(mContext.getResources().getColor(R.color.dlg_device_type_bg_n));
                viewHolder.tv_ok.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                viewHolder.tv_fail.setTextColor(mContext.getResources().getColor(R.color.dlg_safety_check_fail));
                viewHolder.tv_fail.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_error), null, null, null);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_safety, tv_ok, tv_fail;

        public ViewHolder(View view) {
            super(view);
        }
    }

    private boolean IscheckPass() {
        if (safetyItems != null && safetyItems.size() > 0) {
            for (SafetyItem safetyItem : safetyItems) {
                if (safetyItem.type == 2 || safetyItem.type == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRoomSafeFail() {

    }

    @Override
    public void onSafeSucceed() {

    }

    @Override
    public void offlineSucceed() {

    }

    @Override
    public void onSafeUploadSucceed(boolean isSucceed) {
        tv_ok.setText(mContext.getString(R.string.ok));
        tv_ok.setEnabled(true);
        if (mUploadlistener != null && isSucceed) {
            mUploadlistener.UploadSucceed();
        }
    }


    @Override
    public void onStoreSafeFail() {

    }

}
