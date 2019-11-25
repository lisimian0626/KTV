package com.beidousat.karaoke.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.erp.Taste;
import com.bestarmedia.libcommon.model.erp.TastesV4;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class RemarksDialog extends BaseDialog implements HttpRequestListener {

    private final String TAG = RemarksDialog.class.getSimpleName();

    private TextView mTvTitle;
    private RecyclerView mRecyclerView;
    private List<Taste> mTastes;

    public RemarksDialog(Activity context) {
        super(context, R.style.MyDialog);
        init();
    }

    void init() {
        this.setContentView(R.layout.dlg_package_detail);
        if (getWindow() == null)
            return;
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 400;
        lp.height = 450;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mTvTitle = findViewById(android.R.id.title);
        mRecyclerView = findViewById(android.R.id.list);

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(8).margin(8, 8)
                .build();
        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(8).margin(8, 8)
                .build();

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.addItemDecoration(verDivider);
        mRecyclerView.addItemDecoration(horDivider);

        request();
    }


    @Override
    public void onFailed(String method, Object object) {

    }

    @Override
    public void onError(String method, String error) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_PURCHASE_SPECIAL_TASTE.equalsIgnoreCase(method)) {
            TastesV4 tastesV4;
            if (object instanceof TastesV4 && (tastesV4 = (TastesV4) object).special_taste != null
                    && tastesV4.special_taste.data != null && tastesV4.special_taste.data.size() > 0) {
                mTastes = tastesV4.special_taste.data;
                setRemark(mTastes);
            }
        }
    }

    @Override
    public void onStart(String method) {

    }

    public Dialog setTitle(String title) {
        mTvTitle.setText(Html.fromHtml(title));
        return this;
    }

    private void setRemark(List<Taste> tastes) {
        AdapterRemarks adapter = new AdapterRemarks();
        mRecyclerView.setAdapter(adapter);
        adapter.setData(tastes);
    }

    private void request() {
        if (mTastes == null) {
            HttpRequestV4 request = initRequest(RequestMethod.NODE_PURCHASE_SPECIAL_TASTE);
            request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
            request.setConvert2Class(TastesV4.class);
            request.get();
        } else {
            setRemark(mTastes);
        }
    }

    public HttpRequestV4 initRequest(String key) {
        HttpRequestV4 request = new HttpRequestV4(getContext(), key);
        request.setHttpRequestListener(this);
        return request;
    }

    public class AdapterRemarks extends RecyclerView.Adapter<AdapterRemarks.ViewHolder> {

        private LayoutInflater mInflater;
        private List<Taste> mData = new ArrayList<>();

        private AdapterRemarks() {
            mInflater = LayoutInflater.from(getContext());
        }

        public void setData(List<Taste> data) {
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

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.list_item_remarks, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvKey = view.findViewById(android.R.id.text1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final Taste taste = mData.get(position);
            holder.tvKey.setText(taste.Name);
            holder.tvKey.setOnClickListener(v -> {
                if (mOnRemarkChooseListener != null) {
                    mOnRemarkChooseListener.onRemarkChoose(taste);
                }
                dismiss();
            });
        }
    }

    private OnRemarkChooseListener mOnRemarkChooseListener;

    public void setOnRemarkChooseListener(OnRemarkChooseListener listener) {
        this.mOnRemarkChooseListener = listener;
    }

    public interface OnRemarkChooseListener {
        void onRemarkChoose(Taste taste);
    }
}
