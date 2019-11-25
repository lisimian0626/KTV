package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2016/6/21.
 */
public class ComboAttrs extends ErpResultBase implements Serializable {

//    @Expose
//    public int ComboAttr;

    @Expose
    public List<ComboAttr> data;

}
