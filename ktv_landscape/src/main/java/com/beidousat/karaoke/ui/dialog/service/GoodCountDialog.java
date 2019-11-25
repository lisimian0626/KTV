package com.beidousat.karaoke.ui.dialog.service;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libcommon.util.ListUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.edit.EditTextEx;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/10/12 15:52.
 */
public class GoodCountDialog extends BaseDialog implements View.OnClickListener {

    private RecyclerView mLvItems;
    private EditTextEx mEtPwd;
    private Context mContext;
    private TextView tvTip;

    public GoodCountDialog(Context context) {
        super(context, R.style.MyDialog);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        this.setContentView(R.layout.dlg_good_count);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        int[] wh = DensityUtil.getScreenWidthHeight(mContext);
        lp.width = 400;
        lp.height = 450;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
//        setCanceledOnTouchOutside(false);

        tvTip = findViewById(R.id.tv_tip);
        mLvItems = findViewById(R.id.rv_items);
        mEtPwd = findViewById(android.R.id.input);
        findViewById(R.id.iv_close).setOnClickListener(this);
        init();
    }


    private void init() {

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(mContext.getApplicationContext())
                .color(Color.TRANSPARENT).size(12).margin(12, 12)
                .build();

        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(mContext.getApplicationContext())
                .color(Color.TRANSPARENT).size(10).margin(10, 10)
                .build();

        mLvItems.setLayoutManager(new GridLayoutManager(mContext.getApplicationContext(), 3));
        mLvItems.addItemDecoration(verDivider);
        mLvItems.addItemDecoration(horDivider);
        AdapterNumber adapter = new AdapterNumber();
        mLvItems.setAdapter(adapter);
        adapter.setData(ListUtil.array2List(mContext.getResources().getStringArray(R.array.mng_pwd_texts)));
    }

    //    public void show() {
//        showAtLocation(mContext.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, 0);
//    }
    private OnGoodCountListener mOnGoodCountListener;

    public void setOnGoodCountListener(OnGoodCountListener l) {
        mOnGoodCountListener = l;
    }

    public interface OnGoodCountListener {
        void onGoodCount(int count);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_close) {
            dismiss();
        }
    }

    public class AdapterNumber extends RecyclerView.Adapter<AdapterNumber.ViewHolder> {

        private LayoutInflater mInflater;
        private List<String> mData = new ArrayList<>();

        public AdapterNumber() {
            mInflater = LayoutInflater.from(mContext);
        }

        public void setData(List<String> data) {
            this.mData = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvKey;

            public ViewHolder(View view) {
                super(view);
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.list_item_good_count_keyboard, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvKey = view.findViewById(android.R.id.button1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final String keyText = mData.get(position);
            holder.tvKey.setText(keyText);
            holder.tvKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (position == getItemCount() - 1) {
                            if (mOnGoodCountListener != null) {
                                mOnGoodCountListener.onGoodCount(Integer.valueOf(mEtPwd.getText().toString().trim()));
                            }
                            dismiss();
                        } else {
                            if (!TextUtils.isEmpty(keyText)) {
                                if (keyText.equals(mContext.getString(R.string.delete))) {
                                    String text = mEtPwd.getText().toString();
                                    if (!TextUtils.isEmpty(text)) {
                                        String txt = text.substring(0, text.length() - 1);
                                        mEtPwd.setText(txt);
                                    }
                                } else {
                                    mEtPwd.setText(mEtPwd.getText() + keyText);
                                }
                            }
                        }
                    } catch (Exception e) {
                        Logger.e(getClass().getSimpleName(), e.toString());
                    }
                }
            });
        }
    }
}
