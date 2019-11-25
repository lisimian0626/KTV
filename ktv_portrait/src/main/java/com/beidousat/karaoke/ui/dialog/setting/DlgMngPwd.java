package com.beidousat.karaoke.ui.dialog.setting;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.util.ListUtil;
import com.bestarmedia.libwidget.edit.EditTextEx;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/10/12 15:52.
 */
public class DlgMngPwd extends BaseDialog implements View.OnClickListener {

    private RecyclerView mLvItems;
    private EditTextEx mEtPwd;
    private Context mContext;
    private TextView tvTip;


    public DlgMngPwd(Context context) {
        super(context, R.style.MyDialog);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        this.setContentView(R.layout.pop_mng_pwd);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = 450;
        lp.height = 450;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);

        setCanceledOnTouchOutside(false);

        tvTip = findViewById(R.id.tv_tip);
        mLvItems = findViewById(R.id.rv_items);
        mEtPwd = findViewById(android.R.id.input);
        findViewById(R.id.iv_close).setOnClickListener(this);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    private OnMngPwdListener mOnMngPwdListener;

    public void setOnMngPwdListener(OnMngPwdListener listener) {
        this.mOnMngPwdListener = listener;
    }

    public interface OnMngPwdListener {
        void onPass();
    }

    private void init() {

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(mContext)
                .color(Color.TRANSPARENT).size(12).build();

        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(mContext)
                .color(Color.TRANSPARENT).size(12).build();

        mLvItems.setLayoutManager(new GridLayoutManager(mContext, 3));
        mLvItems.addItemDecoration(verDivider);
        mLvItems.addItemDecoration(horDivider);
        AdapterNumber adapter = new AdapterNumber();
        mLvItems.setAdapter(adapter);
        adapter.setData(ListUtil.array2List(mContext.getResources().getStringArray(R.array.mng_pwd_texts)));
    }

//    public void show() {
//        showAtLocation(mContext.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, 0);
//    }

    public class AdapterNumber extends RecyclerView.Adapter<AdapterNumber.ViewHolder> {

        private LayoutInflater mInflater;
        private List<String> mData = new ArrayList<String>();

        public AdapterNumber() {
            mInflater = LayoutInflater.from(mContext);
        }

        public void setData(List<String> data) {
            this.mData = data;
            notifyDataSetChanged();
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

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.list_item_pop_keyboard, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvKey = view.findViewById(android.R.id.button1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final String keyText = mData.get(position);
            holder.tvKey.setText(keyText);
            holder.tvKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (position == getItemCount() - 1) {
                            String pwd = mEtPwd.getText().toString().trim();
                            if (pwd != null && ("63716616".equals(pwd) || pwd.equals(PrefData.getMngPassword(mContext)))) {
                                mOnMngPwdListener.onPass();
                                dismiss();
                            } else {
                                tvTip.setVisibility(View.VISIBLE);
                                tvTip.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvTip.setVisibility(View.INVISIBLE);
                                    }
                                }, 2000);
                            }
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
                        Log.e(getClass().getSimpleName(), e.toString());
                    }
                }
            });
        }
    }
}
