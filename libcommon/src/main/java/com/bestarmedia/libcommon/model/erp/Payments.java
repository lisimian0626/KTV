package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2016/6/21.
 */
public class Payments extends ErpResultBase implements Serializable {

    @Expose
    public Charge data;

}
