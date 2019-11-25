package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/25.
 */

public class FreeCategories extends ErpResultBase implements Serializable {

    @Expose
    public java.util.List<GoodCategory> data;

}