package com.bestarmedia.libcommon.model.store;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2017/5/15.
 */

public class StoreBaseModel implements Serializable {

    @Expose
    public String error;

    @Expose
    public String message;

    @Expose
    public JsonElement data;

}
