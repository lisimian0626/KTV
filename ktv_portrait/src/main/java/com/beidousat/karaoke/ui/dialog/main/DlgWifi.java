package com.beidousat.karaoke.ui.dialog.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libcommon.model.vod.Configuration;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import java.util.List;

public class DlgWifi extends BaseDialog implements View.OnClickListener {
    private RecyclerView recyclerView;
    private Adapter adapter;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dlg_wifi_iv_close) {
            dismiss();
        }
    }

    public DlgWifi(Activity activity) {
        super(activity, R.style.MyDialog);
        init();
    }

    private void init() {
        this.setContentView(R.layout.dlg_wifi);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        findViewById(R.id.dlg_wifi_iv_close).setOnClickListener(this);
        recyclerView = findViewById(R.id.dlg_wifi_rv);
        adapter = new Adapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    public void setData(List<Configuration.WIFI> list) {
        adapter.setData(list);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(40).build();
        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(40).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(verDivider);
        recyclerView.addItemDecoration(horDivider);
        adapter.notifyDataSetChanged();
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<Configuration.WIFI> wifiList;

        private Adapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        public void setData(List<Configuration.WIFI> list_wifi) {
            this.wifiList = list_wifi;
        }

        @Override
        public int getItemCount() {
            return wifiList == null ? 0 : wifiList.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View view = mInflater.inflate(R.layout.dlg_wifi_rvitem, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvWifiName = view.findViewById(R.id.dlg_item_wifi_name);
            viewHolder.tvWifiPassWord = view.findViewById(R.id.dlg_item_wifi_password);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
            Configuration.WIFI wifi = wifiList.get(i);
            viewHolder.tvWifiName.setText(wifi.wifiName);
            viewHolder.tvWifiPassWord.setText(wifi.wifiPassword);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvWifiName, tvWifiPassWord;

        public ViewHolder(View view) {
            super(view);
        }
    }

}
