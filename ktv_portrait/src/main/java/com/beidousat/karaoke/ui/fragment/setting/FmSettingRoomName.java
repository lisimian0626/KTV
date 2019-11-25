package com.beidousat.karaoke.ui.fragment.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.KeyboardListener;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.WidgetKeyboard;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.SimpleRoom;

/**
 * Created by J Wong on 2017/5/12.
 */

public class FmSettingRoomName extends BaseFragment implements View.OnClickListener, KeyboardListener {
    private View mRootView;
    private WidgetKeyboard mWidgetKeyboard;
    String roomName;
    private PromptDialogSmall dialogSmall;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_setting_room, null);
        mWidgetKeyboard = mRootView.findViewById(R.id.keyboard);
        mWidgetKeyboard.showWordCount(false);
        mWidgetKeyboard.needDisableKey(false);
        mWidgetKeyboard.setInputTextChangedListener(this);
        mWidgetKeyboard.showLackSongButton(false);
        mWidgetKeyboard.setText(NodeRoomInfo.getInstance().getRoom().ktvRoomCode);
        mRootView.findViewById(R.id.iv_back).setOnClickListener(this);
        mRootView.findViewById(R.id.setting_room_tv_ok).setOnClickListener(this);
        return mRootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
                break;
            case R.id.setting_room_tv_ok:
                if (!TextUtils.isEmpty(roomName)) {
                    SimpleRoom simpleRoom = new SimpleRoom();
                    simpleRoom.setId(NodeRoomInfo.getInstance().getRoom().id);
                    simpleRoom.setKtvRoomCode(roomName);
                    requestRoom(simpleRoom);
                } else {
                    Toast.makeText(getActivity(), getText(R.string.room_name_setting_error), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void requestRoom(SimpleRoom simpleRoom) {
        HttpRequestV4 r = initRequestV4(RequestMethod.NODE_ROOM_NAME + "/" + NodeRoomInfo.getInstance().getRoom().id + "/" + "room-name");
        r.putJson(simpleRoom.toString());
    }

    @Override
    public void onSuccess(String method, Object object) {
        NodeRoomInfo.getInstance().getRoom().ktvRoomCode = roomName;
        dialogSmall = new PromptDialogSmall(getActivity());
        dialogSmall.setTitle(R.string.setting_room_updata_succed);
        dialogSmall.setMessage(R.string.setting_room_updata_name_succed);
        dialogSmall.setOkButton(true, getString(R.string.ok), view -> EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, ""));
        dialogSmall.show();
        EventBusUtil.postSticky(EventBusId.Id.KTV_ROOM_CODE_CHANGED, "");
        super.onSuccess(method, object);
    }

    @Override
    public void onFailed(String method, Object object) {
        Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
        super.onFailed(method, object);
    }

    @Override
    public void onInputTextChanged(String text) {
        roomName = text;
    }

    @Override
    public void onWordCountChanged(int count) {

    }

}
