package com.bestarmedia.libcommon.model.store;

import com.google.gson.annotations.Expose;

/**
 * Created by J Wong on 2017/5/23.
 */

public class AuthKeystore {

    @Expose
    public String expired;//该密钥过期时间

    @Expose
    public String store_code;//店家编号

    @Expose
    public long action_time;//密钥下发时间

}
