package com.beidousat.karaoke.ui.dialog.setting;

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
import android.widget.TextView;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libcommon.model.v4.DeviceType;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import java.util.List;

public class DlgDeviceType extends BaseDialog implements View.OnClickListener {
    private TextView tv_title, tv_title2;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private DeviceType mDeviceType;
    private OnDeviceTypeListener mOnDeviceTypeListener;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dlg_device_type_ok:
                onDeviceType(mDeviceType);
                dismiss();
                break;
            case R.id.dlg_device_type_iv_close:
                dismiss();
                break;
        }
    }

    public interface OnDeviceTypeListener {
        void onDeviceTypeSelect(DeviceType deviceType);
    }

    public DlgDeviceType(Activity activity) {
        super(activity, R.style.MyDialog);
        init();
    }

    private void init() {
        this.setContentView(R.layout.dlg_device_type);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        tv_title = findViewById(R.id.dlg_device_type_title);
        tv_title2 = findViewById(R.id.dlg_device_type_title2);
        tv_title.setText(getContext().getString(R.string.setting_room_devicetype_title));
        tv_title2.setText(getContext().getString(R.string.setting_room_devicetype_title2));
        findViewById(R.id.dlg_device_type_ok).setOnClickListener(this);
        findViewById(R.id.dlg_device_type_iv_close).setOnClickListener(this);
        recyclerView = findViewById(R.id.dlg_device_type_rv);
        adapter = new Adapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    public void setData(List<DeviceType> list) {
        adapter.setData(list);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext().getApplicationContext())
                .color(Color.TRANSPARENT).size(40).build();
        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext().getApplicationContext())
                .color(Color.TRANSPARENT).size(40).build();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), list.size() > 4 ? 3 : 2));
        recyclerView.addItemDecoration(verDivider);
        recyclerView.addItemDecoration(horDivider);
        adapter.notifyDataSetChanged();
    }

    public void setSelectItem(DeviceType deviceType) {
        this.mDeviceType = deviceType;
    }

    public void setmOnDeviceTypeListener(OnDeviceTypeListener mOnDeviceTypeListener) {
        this.mOnDeviceTypeListener = mOnDeviceTypeListener;
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<DeviceType> deviceTypeList;

        public Adapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public void setData(List<DeviceType> list_deviceType) {
            this.deviceTypeList = list_deviceType;
        }

        @Override
        public int getItemCount() {
            return deviceTypeList == null ? 0 : deviceTypeList.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View view = mInflater.inflate(R.layout.setting_room_devicetype_rvitem, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tv_device_type = view.findViewById(R.id.setting_room_regist_item_devicetype);
            viewHolder.tv_device_type.setOnClickListener(v -> {
                if (deviceTypeList.get(viewHolder.getAdapterPosition()).name.equalsIgnoreCase("点歌屏") || deviceTypeList.get(viewHolder.getAdapterPosition()).name.equalsIgnoreCase("副屏")) {
                    mDeviceType = deviceTypeList.get(viewHolder.getAdapterPosition());
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), R.string.setting_room_device_type_error, Toast.LENGTH_SHORT).show();
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
            DeviceType deviceType = deviceTypeList.get(i);
            if (deviceType.equals(mDeviceType)) {
                viewHolder.tv_device_type.setSelected(true);
                viewHolder.tv_device_type.setTextColor(mContext.getResources().getColor(R.color.dlg_device_type_bg_p));
                viewHolder.tv_device_type.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_selection), null, null, null);
            } else {
                viewHolder.tv_device_type.setSelected(false);
                viewHolder.tv_device_type.setTextColor(mContext.getResources().getColor(R.color.dlg_device_type_bg_n));
                viewHolder.tv_device_type.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            viewHolder.tv_device_type.setText(deviceType.name);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_device_type;

        public ViewHolder(View view) {
            super(view);
        }
    }

    private void onDeviceType(DeviceType deviceType) {
        if (mOnDeviceTypeListener != null) {
            mOnDeviceTypeListener.onDeviceTypeSelect(deviceType);
        }
    }
}
