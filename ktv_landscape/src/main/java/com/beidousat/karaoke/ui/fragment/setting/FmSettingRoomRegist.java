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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.KeyboardListener;
import com.beidousat.karaoke.ui.dialog.PromptDialogBig;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.dialog.setting.DlgDeviceReplace;
import com.beidousat.karaoke.ui.dialog.setting.DlgDeviceType;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.util.UIUtils;
import com.beidousat.karaoke.widget.WidgetKeyboard;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.helper.LoginHelper;
import com.bestarmedia.libcommon.http.BaseHttpRequest;
import com.bestarmedia.libcommon.http.JWTUtil;
import com.bestarmedia.libcommon.interf.CheckDeviceListener;
import com.bestarmedia.libcommon.model.node.Author;
import com.bestarmedia.libcommon.model.v4.DeviceType;
import com.bestarmedia.libcommon.model.v4.NodeRoom;
import com.bestarmedia.libcommon.model.v4.RoomDevice;
import com.bestarmedia.libcommon.model.vod.RegisterDevice;
import com.bestarmedia.libcommon.model.vod.login.DeviceInfo;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libcommon.util.NetWorkUtils;
import com.bestarmedia.libcommon.util.PackageUtil;
import com.bestarmedia.libwidget.edit.EditTextEx;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FmSettingRoomRegist extends BaseFragment implements View.OnClickListener, CheckDeviceListener, View.OnTouchListener, KeyboardListener, EditTextEx.OnEditTextIconClickListener {

    private View mRootView;
    private TextView tv_ktv_id, tv_device_type, tv_tips, tv_device_type_choose;
    private EditTextEx et_room_code, et_ktv_room_code;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private LoginHelper loginHelper;
    private WidgetKeyboard widgetKeyboard;
    private EditTextEx mEtFocus;
    private LinearLayout lin_roomName, lin_deviceInfo, lin_bindDevice;
    private List<DeviceType> deviceTypeList;
    private DeviceType selectDeviceType;
    private NodeRoom nodeRoom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_setting_room_regist, null);
        mRootView.findViewById(R.id.iv_back).setOnClickListener(this);
        mRootView.findViewById(R.id.setting_room_regist_tv_regist).setOnClickListener(this);
        mRootView.findViewById(R.id.setting_room_regist_tv_check).setOnClickListener(this);
        mRootView.findViewById(R.id.setting_room_regist_tv_choosetype).setOnClickListener(this);
        widgetKeyboard = mRootView.findViewById(R.id.keyboard);
        widgetKeyboard.setEditTextVisible(false);
        widgetKeyboard.showLackSongButton(false);
        et_room_code = mRootView.findViewById(R.id.setting_room_regist_et_roomcode);
        et_room_code.setOnTouchListener(this);
        et_room_code.setOnEditTextIconClickListener(this);
        et_ktv_room_code = mRootView.findViewById(R.id.setting_room_regist_et_roomname);
        et_ktv_room_code.setOnTouchListener(this);
        et_ktv_room_code.setOnEditTextIconClickListener(this);
        widgetKeyboard.setInputTextChangedListener(this);
        tv_device_type = mRootView.findViewById(R.id.setting_room_regist_tv_devicetype);
        tv_tips = mRootView.findViewById(R.id.setting_room_regist_tv_tips);
        tv_device_type_choose = mRootView.findViewById(R.id.setting_room_regist_tv_choosetype);
        tv_ktv_id = mRootView.findViewById(R.id.setting_room_regist_tv_ktvid);
        tv_ktv_id.setText("KTVID: " + VodConfigData.getInstance().getKtvNetCode());
        lin_roomName = mRootView.findViewById(R.id.setting_room_regist_lin_ktvname);
        lin_deviceInfo = mRootView.findViewById(R.id.setting_room_regist_lin_devicetype);
        lin_bindDevice = mRootView.findViewById(R.id.setting_room_regist_lin_bingdevice);
        recyclerView = mRootView.findViewById(R.id.room_setting_regist_rv);
        adapter = new Adapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        if (getContext() != null && getContext().getApplicationContext() != null) {
            VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext().getApplicationContext())
                    .color(Color.TRANSPARENT).size(5).build();
            recyclerView.addItemDecoration(verDivider);
        }
        loginHelper = LoginHelper.getInstance(getContext());
        loginHelper.setCheckDeviceListener(this);
        loginHelper.getDeviceType();
//        EventBus.getDefault().register(this);
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
//        EventBus.getDefault().unregister(this);
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
            case R.id.setting_room_regist_tv_check:
                if (!TextUtils.isEmpty(et_room_code.getText().toString().trim())) {
                    if (et_room_code.getText().toString().trim().startsWith("B")) {
                        loginHelper.getRoomInfo(et_room_code.getText().toString().trim());
                    } else {
                        Toast.makeText(getContext(), "房间号格式不对,请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "请输入房间号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.setting_room_regist_tv_choosetype:
                if (deviceTypeList != null && deviceTypeList.size() > 0) {
                    DlgDeviceType dlgDeviceType = new DlgDeviceType(getActivity());
                    dlgDeviceType.setData(deviceTypeList);
                    dlgDeviceType.setSelectItem(selectDeviceType == null ? deviceTypeList.get(0) : selectDeviceType);
                    dlgDeviceType.setmOnDeviceTypeListener(deviceType -> {
                        selectDeviceType = deviceType;
                        tv_device_type.setText(deviceType.name);
                    });
                    dlgDeviceType.show();
                }
//                FragmentUtil.addFragment(new FmSettingRoomName(), false,true,true,true);
                break;
            case R.id.setting_room_regist_tv_regist:
                if (nodeRoom == null) {
                    Toast.makeText(getContext(), "请先检测房间", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_room_code.getText().toString().trim())) {
                    Toast.makeText(getContext(), "房间编号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(et_ktv_room_code.getText().toString().trim())) {
                    Toast.makeText(getContext(), "房间名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (selectDeviceType == null) {
                    Toast.makeText(getContext(), "请先选择要注册的设备类型", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //检测是否重复设备
                    if (isReplace()) {
                        PromptDialogBig promptDialogBig = new PromptDialogBig(getActivity());
                        //主屏重复
                        if (selectDeviceType.type == 1) {
                            promptDialogBig.setTitle(R.string.setting_room_updata_device_main_title);
                            promptDialogBig.setMessage(R.string.setting_room_updata_device_main_content);
                            promptDialogBig.setOkButton(true, "替换", view1 -> regist(getMainID()));
                            promptDialogBig.show();
                        } else {
                            promptDialogBig.setTitle(getString(R.string.setting_room_updata_device_other_title, selectDeviceType.name));
                            promptDialogBig.setMessage(getString(R.string.setting_room_updata_device_other_content, selectDeviceType.name));
                            promptDialogBig.setCancleButton(true, getString(R.string.setting_room_dlg_add_title, selectDeviceType.name), view12 -> regist(""));
                            promptDialogBig.setOkButton(true, getString(R.string.setting_room_dlg_replace_title, selectDeviceType.name), view13 -> {
                                DlgDeviceReplace dlgDeviceReplace = new DlgDeviceReplace(getActivity());
                                dlgDeviceReplace.setData(getReplaceList());
                                dlgDeviceReplace.setmOnRoomDeviceListener(roomDevice -> regist(roomDevice.id));
                                dlgDeviceReplace.show();
                            });
                            promptDialogBig.show();
                        }
                    } else {
                        regist("");
                    }
//                    regist();
                }
                break;

        }
    }

    private void regist(String id) {
        RegisterDevice registerDevice = new RegisterDevice();
        if (!TextUtils.isEmpty(id)) {
            registerDevice.id = id;
        }
        registerDevice.ktvNetCode = VodConfigData.getInstance().getKtvNetCode();
        registerDevice.type = selectDeviceType.type;
        registerDevice.name = selectDeviceType.name;
        registerDevice.roomCode = et_room_code.getText().toString().trim();
        registerDevice.ktvRoomCode = et_ktv_room_code.getText().toString().trim();
        registerDevice.ip = NetWorkUtils.getLocalHostIp();
        registerDevice.serialNo = DeviceUtil.getCupSerial();
        registerDevice.macSerial = DeviceUtil.getMacAddress();
        registerDevice.anufacturer = OkConfig.boxManufacturerName();
        registerDevice.osVersion = String.valueOf(PackageUtil.getSystemVersionCode());
        registerDevice.apkVersionCode = String.valueOf(PackageUtil.getVersionCode(getContext()));
        registerDevice.apkVersionName = PackageUtil.getVersionName(getContext());
        registerDevice.apkDescribe = PackageUtil.getApplicationName(getContext());
        registerDevice.hdmiEnable = 0;
        loginHelper.register(registerDevice);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(BusEvent busEvent) {
//        switch (busEvent.id){
//            case EventBusId.Id.KTV_ROOM_CODE_CHANGED:
//                tv_roomName.setText(NodeRoomInfo.getInstance().getRoomName());
//                break;
//        }
//
//    }

    @Override
    public void onDeviceInfoSucced(DeviceInfo deviceInfo) {
        loginHelper.getToken(VodConfigData.getInstance().getKtvNetCode(), DeviceUtil.getCupSerial());
    }

    @Override
    public void onDeviceInfoFail(String msg) {
        PromptDialogSmall dialogSmall = new PromptDialogSmall(getContext());
        dialogSmall.setMessage(msg);
        if (getContext() != null && getActivity() != null) {
            dialogSmall.setOkButton(true, getContext().getString(R.string.reset_hdmi), view -> {
                EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
                UIUtils.hideNavibar(getContext(), false);
                getActivity().finish();
                android.os.Process.killProcess(android.os.Process.myPid());//获取PID
                System.exit(0);//直接结束程序
            });
        }
        dialogSmall.show();
    }

    @Override
    public void onRegister(boolean isSucced, String msg) {
        if (isSucced) {
            if (loginHelper != null) {
                loginHelper.getDeviceInfo(DeviceUtil.getCupSerial());
            }
        }
    }

    @Override
    public void onGetJwt(String jwt) {
        PrefData.setJWT(getContext(), jwt);
        VodConfigData.getInstance().setJwtMessage(JWTUtil.jwt(jwt));
        BaseHttpRequest.setToken(jwt);
        EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
        EventBusUtil.postSticky(EventBusId.Id.LOGIN_SUCCEED, "");
    }

    @Override
    public void onNodeRoom(NodeRoom nodeRoom) {
        this.nodeRoom = nodeRoom;
        lin_roomName.setVisibility(View.VISIBLE);
        lin_deviceInfo.setVisibility(View.VISIBLE);
        et_ktv_room_code.setText(TextUtils.isEmpty(nodeRoom.ktvRoomCode) ? nodeRoom.roomCode : nodeRoom.ktvRoomCode);
        if (deviceTypeList != null && deviceTypeList.size() > 0) {
            selectDeviceType = deviceTypeList.get(0);
            tv_device_type.setText(deviceTypeList.get(0).name);
        }
        if (nodeRoom.roomDeviceList != null && nodeRoom.roomDeviceList.size() > 0) {
            lin_bindDevice.setVisibility(View.VISIBLE);
            tv_tips.setText(R.string.setting_room_device_regist_tips2);
            if (getContext() != null) {
                tv_tips.setTextColor(getContext().getResources().getColor(R.color.room_regist_text2));
            }
            tv_device_type_choose.setVisibility(View.VISIBLE);
            adapter.setData(nodeRoom.roomDeviceList);
            adapter.notifyDataSetChanged();
        } else {
            tv_tips.setText(R.string.setting_room_device_regist_tips1);
            if (getContext() != null) {
                tv_tips.setTextColor(getContext().getResources().getColor(R.color.room_regist_text1));
            }
            tv_device_type_choose.setVisibility(View.GONE);
            lin_bindDevice.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLogout(boolean succed, String msg) {

    }

    @Override
    public void onDeviceType(List<DeviceType> deviceTypeList) {
        this.deviceTypeList = deviceTypeList;
    }

    @Override
    public void onAuthor(Author author) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        et_room_code.setSelected(false);
        et_ktv_room_code.setSelected(false);
        mEtFocus = (EditTextEx) view;
        mEtFocus.setSelected(true);
        widgetKeyboard.setInputText(mEtFocus.getText().toString().trim());
        return false;
    }

    @Override
    public void onInputTextChanged(String text) {
        if (mEtFocus == null) {
            et_room_code.setText(text);
        } else {
            mEtFocus.setText(text);
        }
    }

    @Override
    public void onWordCountChanged(int count) {

    }

    @Override
    public void onIconClick(View view, MotionEvent event) {
        widgetKeyboard.setInputText("");
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<RoomDevice> roomDeviceList;

        private Adapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        public void setData(List<RoomDevice> list_roomDevice) {
            this.roomDeviceList = list_roomDevice;
        }

        @Override
        public int getItemCount() {
            return roomDeviceList == null ? 0 : roomDeviceList.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View view = mInflater.inflate(R.layout.setting_room_regist_rvitem, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tv_device_name = view.findViewById(R.id.setting_room_regist_item_tv);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
            RoomDevice roomDevice = roomDeviceList.get(i);
            viewHolder.tv_device_name.setText(roomDevice.name);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_device_name;

        public ViewHolder(View view) {
            super(view);
        }
    }

    private boolean isReplace() {
        if (nodeRoom.roomDeviceList != null && nodeRoom.roomDeviceList.size() > 0) {
            for (RoomDevice roomDevice : nodeRoom.roomDeviceList) {
                if (roomDevice.type == selectDeviceType.type) {
                    //设备重复
                    return true;
                }
            }
        }
        return false;
    }

    private String getMainID() {
        String ID = "";
        if (nodeRoom.roomDeviceList != null && nodeRoom.roomDeviceList.size() > 0) {
            for (RoomDevice roomDevice : nodeRoom.roomDeviceList) {
                if (roomDevice.type == 1) {
                    //设备重复
                    ID = roomDevice.id;
                    break;
                }
            }
        }
        return ID;
    }

    private List<RoomDevice> getReplaceList() {
        List<RoomDevice> roomDeviceList = new ArrayList<>();
        if (nodeRoom.roomDeviceList != null && nodeRoom.roomDeviceList.size() > 0) {
            for (RoomDevice roomDevice : nodeRoom.roomDeviceList) {
                if (roomDevice.type == selectDeviceType.type) {
                    //设备重复
                    roomDeviceList.add(roomDevice);
                }
            }
        }
        return roomDeviceList;
    }
}
