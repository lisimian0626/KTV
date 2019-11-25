package com.bestarmedia.libcommon.model.v4;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviceTypeV4 {

    @Expose
    @SerializedName("device_type")
    public List<DeviceType> deviceTypeList;
}
