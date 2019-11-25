package com.bestarmedia.libcommon.data;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.util.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by J Wong on 2016/6/26.
 */
public class GameStatus {

    private final static String TAG = GameStatus.class.getSimpleName();

    private Map<Integer, Integer> mGamePass = new HashMap<Integer, Integer>();

    private static GameStatus mGameStatus;

    public static GameStatus getInstance() {
        if (mGameStatus == null) {
            mGameStatus = new GameStatus();
        }
        return mGameStatus;
    }

    public GameStatus() {
    }

    public void setPassLevel(int level, int tutor) {
        Logger.d(TAG, "setPassLevel level:" + level + "  tutor:" + tutor);
        mGamePass.put(level, tutor);
        EventBusUtil.postSticky(EventBusId.Game.GAME_LEVEL_PASS_CHANGED, level);
    }

    public int getPassTutor(int level) {
        if (mGamePass != null && mGamePass.size() > 0 && mGamePass.containsKey(level)) {
            Logger.d(TAG, "getPassTutor level:" + level + "  tutor:" + mGamePass.get(level));
            return mGamePass.get(level);
        }
        return -1;
    }

    public int getPassLevel() {
        int passLevel = 0;
        if (mGamePass != null && mGamePass.size() > 0) {
            for (Map.Entry<Integer, Integer> entry : mGamePass.entrySet()) {
                int key = entry.getKey();
                Logger.d(TAG, "getPassLevel key:" + key);
//                int value = entry.getValue();
                if (key > passLevel)
                    passLevel = key;
            }
        }
        Logger.d(TAG, "getPassLevel level:" + passLevel);
        return passLevel;
    }
}
