package com.beidousat.karaoke.ui.dialog.setting;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
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
import com.bestarmedia.libcommon.model.v4.RoomDevice;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.List;

public class DlgDeviceReplace extends BaseDialog implements View.OnClickListener {
    private TextView tv_title, tv_title2;
    private Context mContext;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private RoomDevice mRoomDevice;
    private OnRoomDeviceListener mOnRoomDeviceListener;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dlg_device_replace_ok:
                if (mRoomDevice != null) {
                    onRoomDevice(mRoomDevice);
                    dismiss();
                } else {
                    Toast.makeText(mContext, "请先选中要替换的设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dlg_device_replace_iv_close:
                dismiss();
                break;
        }
    }

    public interface OnRoomDeviceListener {
        void onRoomDeviceSelect(RoomDevice roomDevice);
    }

    public DlgDeviceReplace(Context context) {
        super(context, R.style.MyDialog);
        this.mContext = context;
        init();
    }

    void init() {
        this.setContentView(R.layout.dlg_device_replace);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        tv_title = findViewById(R.id.dlg_device_replace_title);
        tv_title2 = findViewById(R.id.dlg_device_replace_title2);
        findViewById(R.id.dlg_device_replace_ok).setOnClickListener(this::onClick);
        findViewById(R.id.dlg_device_replace_iv_close).setOnClickListener(this::onClick);
        recyclerView = findViewById(R.id.dlg_device_replace_rv);
        adapter = new Adapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    public void setData(List<RoomDevice> list) {
        tv_title.setText(getContext().getString(R.string.setting_room_dlg_replace_title, list.get(0).name));
        tv_title2.setText(getContext().getString(R.string.setting_room_dlg_replace_content, list.get(0).name));
        adapter.setData(list);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext().getApplicationContext())
                .color(Color.TRANSPARENT).size(40).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(horDivider);
        adapter.notifyDataSetChanged();
    }

    public void setSelectItem(RoomDevice roomDevice) {
        this.mRoomDevice = roomDevice;
    }

    public void setmOnRoomDeviceListener(OnRoomDeviceListener onRoomDeviceListener) {
        this.mOnRoomDeviceListener = onRoomDeviceListener;
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<RoomDevice> roomDeviceList;

        public Adapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public void setData(List<RoomDevice> list_roomdevice) {
            this.roomDeviceList = list_roomdevice;
        }

        @Override
        public int getItemCount() {
            return roomDeviceList == null ? 0 : roomDeviceList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            View view = mInflater.inflate(R.layout.setting_room_device_replace_rvitem, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tv_device_ip = view.findViewById(R.id.setting_room_regist_item_replace);
            viewHolder.tv_device_ip.setOnClickListener(v -> {
                //将点击的效果传出去
                mRoomDevice = roomDeviceList.get(viewHolder.getAdapterPosition());
                notifyDataSetChanged();

            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            RoomDevice roomDevice = roomDeviceList.get(i);
            if (roomDevice.equals(mRoomDevice)) {
                viewHolder.tv_device_ip.setSelected(true);
                viewHolder.tv_device_ip.setTextColor(mContext.getResources().getColor(R.color.dlg_device_type_bg_p));
                viewHolder.tv_device_ip.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_selection), null, null, null);
            } else {
                viewHolder.tv_device_ip.setSelected(false);
                viewHolder.tv_device_ip.setTextColor(mContext.getResources().getColor(R.color.dlg_device_type_bg_n));
                viewHolder.tv_device_ip.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            viewHolder.tv_device_ip.setText(roomDevice.ip);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_device_ip;

        public ViewHolder(View view) {
            super(view);
        }
    }

    private void onRoomDevice(RoomDevice roomDevice) {
        if (mOnRoomDeviceListener != null) {
            mOnRoomDeviceListener.onRoomDeviceSelect(roomDevice);
        }
    }
}
