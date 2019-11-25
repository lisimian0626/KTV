package com.bestarmedia.libcommon.model.erp;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/29.
 */

public class Combo implements Serializable {

    public String combo_id;

    public int count;

    public Combo(String combo_id, int count) {
        this.combo_id = combo_id;
        this.count = count;
    }
}