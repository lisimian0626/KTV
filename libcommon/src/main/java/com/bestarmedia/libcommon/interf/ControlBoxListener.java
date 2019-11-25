package com.bestarmedia.libcommon.interf;

import com.bestarmedia.libcommon.model.vod.SerialPortCode;

/**
 * Created by J Wong on 2016/4/12.
 */
public interface ControlBoxListener {

    void onControlBox(SerialPortCode code);

}
