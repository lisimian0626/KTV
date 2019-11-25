package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2018/9/25.
 */

public class Purchases implements Serializable {

    @Expose
    public List<Good> purchase;

}