package com.beidousat.karaoke.data;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2016/6/19.
 */
public class RoomUsers {


    private static RoomUsers mRoomUsers;

    private Map<Integer, UserBase> mUsers = new HashMap<>();

    public static RoomUsers getInstance() {
        if (mRoomUsers == null) {
            mRoomUsers = new RoomUsers();
        }
        return mRoomUsers;
    }

    private RoomUsers() {
    }

    public void cleanRoomUser() {
        mUsers.clear();
        EventBusUtil.postSticky(EventBusId.Id.ROOM_USER_CHANGED, 0);
    }

    private boolean isRoomUser(int userId) {
        Logger.d(getClass().getSimpleName(), "RoomUser userid:" + userId);
        return mUsers != null && mUsers.containsKey(userId);
    }


    public List<UserBase> getUsers() {
        List<UserBase> userBases = new ArrayList<>();
        for (Map.Entry<Integer, UserBase> entry : mUsers.entrySet()) {
            UserBase key = entry.getValue();
            if (key.id > 0)
                userBases.add(key);
        }
        return userBases;
    }

//    public List<UserBase> getUsers2() {
//        List<UserBase> userBases = new ArrayList<UserBase>();
//        Iterator<Map.Entry<Integer, UserBase>> iter = mUsers.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry<Integer, UserBase> entry = iter.next();
//            UserBase key = entry.getValue();
//            userBases.add(key);
//        }
//        return userBases;
//    }


    public void removeUser(int userId) {
        if (mUsers != null && mUsers.size() > 0 && mUsers.containsKey(userId)) {
            mUsers.remove(userId);
            EventBusUtil.postSticky(EventBusId.Id.ROOM_USER_CHANGED, 0);
        }
    }

    public boolean addUserNotExist(UserBase userBase) {
        Logger.d("RoomUsers", "addUserNotExist " + (userBase == null ? " is null " : (" ID:" + userBase.id + " Name:" + userBase.name)));
        if (userBase != null && userBase.id > 0 && !isRoomUser(userBase.id)) {
            addUser(userBase);
            return true;
        }
        return false;
    }

    private void addUser(UserBase userBase) {
        mUsers.put(userBase.id, userBase);
        EventBusUtil.postSticky(EventBusId.Id.ROOM_USER_CHANGED, 0);
        //屏蔽通信
//        PhoneOperationUtil.broadcastRoomUsers(getUsers());
//        SocketOperationUtil.sendRoomUsers2Secondary(getUsers());
//        PhoneOperationUtil.broadcastRoomInfo(NodeRoomInfo.getInstance().getNodeRoom().roomSession);
//        if (!TextUtils.isEmpty(userBase.avatar)) {
//            VodDownloadFileHelper vodDownloadFileHelper = new VodDownloadFileHelper(mContext);
//            vodDownloadFileHelper.download(userBase.avatar);
//        }
    }


    public void setRoomUsers(List<UserBase> roomUsers) {
        this.mUsers.clear();
        if (roomUsers != null && roomUsers.size() > 0) {
            for (UserBase userBase : roomUsers) {
                mUsers.put(userBase.id, userBase);
            }
            EventBusUtil.postSticky(EventBusId.Id.ROOM_USER_CHANGED, 0);
        }
    }


}
