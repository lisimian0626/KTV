package com.bestarmedia.libcommon.ad;


import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.model.ad.ADModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2016/1/12 10:58.
 */
public class AdModelDefault {

    public static List<ADModel> getCornerDefaultAd() {
        List<ADModel> adModelList = new ArrayList<>();
        ADModel ad = new ADModel();
        adModelList.add(ad);
        return adModelList;
    }

    public static List<ADModel> getDoorDefaultAd() {
        List<ADModel> adModelList = new ArrayList<>();
        ADModel ad = new ADModel();
        ad.setAdPosition("M1");
        ad.setAdContent(OkConfig.NECESSARY_FILE_DIR + "ad_default.mp4|");
//        ad.setAdContent("http://172.30.1.230:10230/data/necessary/ad_default.mp4|");
        adModelList.add(ad);
        return adModelList;
    }

    public static List<ADModel> getPatchDefaultAd() {
        List<ADModel> adModelList = new ArrayList<>();
        ADModel ad = new ADModel();
        ad.setAdContent(OkConfig.NECESSARY_FILE_DIR + "ad_default.mp4");
        adModelList.add(ad);
        return adModelList;
    }

//    public static Ad getScreenDefaultAd() {
//        Ad ad = new Ad();
//        ad.ADContent = "data/ad/img/ad_default.webp";
//        return ad;
//    }


    public static List<ADModel> getPublicServiceAd() {
        List<ADModel> adModelList = new ArrayList<>();
        ADModel ad = new ADModel();
        ad.setAdContent(OkConfig.NECESSARY_FILE_DIR + "ad_starting.mp4");
        adModelList.add(ad);
        return adModelList;
    }


    public static String getScoreResultVideo() {
        return OkConfig.NECESSARY_FILE_DIR + "score_result.mp4";
    }
}
