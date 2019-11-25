package com.beidousat.karaoke.ui.fragment.setting;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.PromptDialogBig;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.util.FragmentUtil;
import com.beidousat.karaoke.util.UIUtils;
import com.bestarmedia.libcommon.data.DeviceInfoData;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.helper.LoginHelper;
import com.bestarmedia.libcommon.http.BaseHttpRequest;
import com.bestarmedia.libcommon.interf.CheckDeviceListener;
import com.bestarmedia.libcommon.model.node.Author;
import com.bestarmedia.libcommon.model.v4.DeviceType;
import com.bestarmedia.libcommon.model.v4.NodeRoom;
import com.bestarmedia.libcommon.model.v4.RoomDevice;
import com.bestarmedia.libcommon.model.vod.login.DeviceInfo;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

public class FmSettingRoomInfo extends BaseFragment implements View.OnClickListener, CheckDeviceListener {

    private View mRootView;
    private TextView tv_roomName, tv_roomCode, tv_ktvName, tv_ktvID;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private LoginHelper loginHelper;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.setting_room_info, null);
        mRootView.findViewById(R.id.iv_back).setOnClickListener(this);
        mRootView.findViewById(R.id.setting_room_tv_updata_roomName).setOnClickListener(this);
        mRootView.findViewById(R.id.setting_room_tv_logout).setOnClickListener(this);
        tv_roomName = mRootView.findViewById(R.id.setting_room_tv_roomName);
        tv_roomCode = mRootView.findViewById(R.id.setting_room_tv_roomCode);
        tv_ktvName = mRootView.findViewById(R.id.setting_room_tv_ktvName);
        tv_ktvID = mRootView.findViewById(R.id.setting_room_tv_ktvCode);
        tv_ktvName.setText(VodConfigData.getInstance().getKtvName());
        tv_ktvID.setText(VodConfigData.getInstance().getKtvNetCode());
        recyclerView = mRootView.findViewById(R.id.room_setting_rv);
        adapter = new Adapter(getContext());
        recyclerView.setAdapter(adapter);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(25).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(horDivider);
        loginHelper = LoginHelper.getInstance(getContext());
        loginHelper.setCheckDeviceListener(this);
        loginHelper.getRoomInfo(VodConfigData.getInstance().getRoomCode());
        EventBus.getDefault().register(this);
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
                break;
            case R.id.setting_room_tv_logout:
                if (DeviceInfoData.getInstance().getNodeRoom() != null && DeviceInfoData.getInstance().getNodeRoom().roomDeviceList != null &&
                        DeviceInfoData.getInstance().getNodeRoom().roomDeviceList.size() > 0) {
                    showLogoutDialog();
                } else {
                    Toast.makeText(getContext(), "没有绑定设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.setting_room_tv_updata_roomName:
                FragmentUtil.addFragment(new FmSettingRoomName(), false, true, true, true);
                break;

        }
    }

    private void showLogoutDialog() {
        PromptDialogBig promptDialogBig = new PromptDialogBig(getActivity());
        promptDialogBig.setTitle(R.string.setting_room_logout_title);
        promptDialogBig.setMessage(R.string.setting_room_logout_content);
        promptDialogBig.setOkButton(true, "确定", v -> {
            if (!TextUtils.isEmpty(DeviceInfoData.getInstance().getLocalId())) {
                loginHelper.logout(DeviceInfoData.getInstance().getLocalId());
            } else {
                Toast.makeText(getContext(), "房间信息不存在", Toast.LENGTH_SHORT).show();
            }
        });
        promptDialogBig.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BusEvent busEvent) {
        switch (busEvent.id) {
            case EventBusId.Id.KTV_ROOM_CODE_CHANGED:
                tv_roomName.setText(NodeRoomInfo.getInstance().getRoomName());
                break;
            case EventBusId.Id.KTV_ROOM_DEVCE_CHANGED:
                if (loginHelper != null) {
                    loginHelper.getRoomInfo(VodConfigData.getInstance().getRoomCode());
                }
                break;
        }

    }

    @Override
    public void onDeviceInfoSucced(DeviceInfo deviceInfo) {

    }

    @Override
    public void onDeviceInfoFail(String msg) {

    }

    @Override
    public void onRegister(boolean isSucced, String msg) {

    }

    @Override
    public void onGetJwt(String jwt) {

    }

    @Override
    public void onNodeRoom(NodeRoom nodeRoom) {
        DeviceInfoData.getInstance().setNodeRoom(nodeRoom);
        if (!TextUtils.isEmpty(nodeRoom.roomCode)) {
            tv_roomCode.setText(nodeRoom.roomCode);
        }
        if (!TextUtils.isEmpty(nodeRoom.ktvRoomCode)) {
            tv_roomName.setText(nodeRoom.ktvRoomCode);
        }
        if (nodeRoom.roomDeviceList != null && nodeRoom.roomDeviceList.size() > 0) {
            for (RoomDevice roomDevice : nodeRoom.roomDeviceList) {
                if (roomDevice.serialNo.equalsIgnoreCase(DeviceUtil.getCupSerial())) {
                    DeviceInfoData.getInstance().setLocalId(roomDevice.id);
                }
            }
            adapter.setData(nodeRoom.roomDeviceList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLogout(boolean succed, String msg) {
        if (succed) {
            showDlgTips();
        }
    }

    private void showDlgTips() {
        PrefData.setJWT(getContext(), "");
        VodConfigData.getInstance().setJwtMessage(null);
        BaseHttpRequest.setToken(null);
        PromptDialogSmall promptDialogSmall = new PromptDialogSmall(getActivity());
        promptDialogSmall.setTitle(R.string.setting_room_logout_succed_title);
        promptDialogSmall.setMessage(R.string.setting_room_logout_succed_content);
        promptDialogSmall.setOkButton(true, "重启", v -> {
            EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
            UIUtils.hideNavibar(getContext(), false);
            getActivity().finish();
            android.os.Process.killProcess(android.os.Process.myPid());//获取PID
            System.exit(0);//直接结束程序
        });
        promptDialogSmall.show();
    }

    @Override
    public void onDeviceType(List<DeviceType> deviceTypeList) {

    }

    @Override
    public void onAuthor(Author author) {

    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private LayoutInflater mInflater;
        private List<RoomDevice> roomDeviceList;

        private Adapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public void setData(List<RoomDevice> list_roomDevice) {
            this.roomDeviceList = list_roomDevice;
        }

        @Override
        public int getItemCount() {
            return roomDeviceList == null ? 0 : roomDeviceList.size();
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View view = mInflater.inflate(R.layout.setting_room_info_rvitem, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tv_device = view.findViewById(R.id.setting_room_item_tv_device);
            viewHolder.tv_device_info = view.findViewById(R.id.setting_room_item_tv_device_info);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
            RoomDevice roomDevice = roomDeviceList.get(i);
            if (roomDevice.serialNo.equalsIgnoreCase(DeviceUtil.getCupSerial())) {
                viewHolder.tv_device.setText("( 本机 ) " + roomDevice.name);
            } else {
                viewHolder.tv_device.setText(roomDevice.name);
            }
            viewHolder.tv_device_info.setText(roomDevice.ip);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_device, tv_device_info;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
