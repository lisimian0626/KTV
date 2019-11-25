package com.bestarmedia.libcommon.model.ad;

import com.bestarmedia.libcommon.model.vod.Topic;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecommendInfo implements Serializable {

    private int type;
    private String id;
    private String name;
    @SerializedName("ad_position")
    private String adPosition;
    @SerializedName("ad_content")
    private String adContent;
    @SerializedName("brand_name")
    private String brandName;
    @SerializedName("ad_type_id")
    private int adTypeId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(String adPosition) {
        this.adPosition = adPosition;
    }

    public String getAdContent() {
        return adContent;
    }

    public void setAdContent(String adContent) {
        this.adContent = adContent;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getAdTypeId() {
        return adTypeId;
    }

    public void setAdTypeId(int adTypeId) {
        this.adTypeId = adTypeId;
    }

    public ADModel toADModel() {
        ADModel adModel = new ADModel();
        try {
            adModel.setId(Integer.valueOf(getId()));
            adModel.setAdPosition(getAdPosition());
            adModel.setAdTypeId(getAdTypeId());
            adModel.setName(getBrandName());
            adModel.setAdContent(getAdContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adModel;
    }

//    public Topic toTopic() {
//        Topic topic = new Topic();
//        topic.ID = getId();
//        topic.TopicsName = getName();
//        try {
//            topic.Img = getAdContent().split("\\|")[0];
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            topic.RecommendImg = getAdContent().split("\\|")[1];
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return topic;
//    }
}
