package com.bestarmedia.libcommon.model.v4;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class Areas implements Serializable {
    @Expose
    public List<Part> part;
}
