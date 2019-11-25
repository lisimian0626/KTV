package com.bestarmedia.libcommon.interf;

import com.bestarmedia.libcommon.model.ad.ADModel;

import java.util.List;

public interface AdsRequestListenerV4 {

    void onAdsRequestSuccess(List<ADModel> adModelList);

    void onAdsRequestFail();

}
