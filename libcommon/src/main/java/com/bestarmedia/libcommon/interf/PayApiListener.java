package com.bestarmedia.libcommon.interf;

import com.bestarmedia.libcommon.model.vod.PayInfo;

/**
 * Created by J Wong on 2018/10/9.
 */

public interface PayApiListener {

    //房间付费信息
    void onPayInfo(PayInfo payInfo);

}
