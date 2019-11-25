package com.beidousat.karaoke.ui.dialog.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;
import com.bestarmedia.libwidget.text.GradientTextView;

import java.util.List;

/**
 * Created by J Wong on 2015/10/12 15:52.
 */
public class DlgCallService extends BaseDialog implements View.OnClickListener {

    private RecyclerView mLvItems;
    private Activity mContext;

    public DlgCallService(Activity context) {
        super(context, R.style.MyDialog);
        init(context);
    }

    public void init(Activity context) {
        mContext = context;
        this.setContentView(R.layout.dlg_call_servcie);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = 610;
        lp.height = 400;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mLvItems = findViewById(R.id.rv_services);
        findViewById(R.id.riv_close).setOnClickListener(this);

        init();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.riv_close)
            dismiss();
    }

    private void init() {
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(mContext.getApplicationContext())
                .color(Color.TRANSPARENT).size(10).margin(50, 30).build();
        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(mContext.getApplicationContext())
                .color(Color.TRANSPARENT).size(10).margin(0, 30).build();
        mLvItems.setLayoutManager(new GridLayoutManager(mContext.getApplicationContext(), 4));
        mLvItems.addItemDecoration(verDivider);
        mLvItems.addItemDecoration(horDivider);
        AdapterService adapter = new AdapterService(getContext());
        adapter.setData(getContext().getResources().getStringArray(R.array.service_icon_texts));
        mLvItems.setAdapter(adapter);
    }


    private class AdapterService extends RecyclerView.Adapter<ViewHolder> {

        private LayoutInflater mInflater;
        private String[] icon_texts = new String[0];

        private AdapterService(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public void setData(String[] icons) {
            this.icon_texts = icons;
        }

        @Override
        public int getItemCount() {
            return icon_texts.length;
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View view = mInflater.inflate(R.layout.list_item_service, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.textView = view.findViewById(R.id.dlg_service_text);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i, @NonNull List<Object> payloads) {
            viewHolder.textView.setText(icon_texts[i]);
            viewHolder.textView.setOnClickListener(v -> {
                VodApplication.getKaraokeController().setServiceMode(i, true);
                dismiss();
            });
            viewHolder.itemView.setOnClickListener(v -> {
                VodApplication.getKaraokeController().setServiceMode(i, true);
                dismiss();
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
}
