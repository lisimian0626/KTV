package com.bestarmedia.libcommon.model.vod.safety;

import java.util.ArrayList;
import java.util.List;

public class SafetyItem {
    public String name;
    public int type;


    public SafetyItem() {
    }

    public SafetyItem(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public List<SafetyItem> getSagetyItemList(){
        List<SafetyItem> safetyItemList=new ArrayList<>();
        safetyItemList.add(new SafetyItem("灭火器状况",0));
        safetyItemList.add(new SafetyItem("电路状况",0));
        safetyItemList.add(new SafetyItem("逃生通道状况",0));
        safetyItemList.add(new SafetyItem("防毒面具状况",0));
        return safetyItemList;
    }
}
