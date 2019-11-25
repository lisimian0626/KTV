package com.bestarmedia.libcommon.ad;


import com.bestarmedia.libcommon.model.ad.RecommendInfo;

import java.util.List;

public interface HomeBannerRequestListenerV4 {

    void onRequestSuccess(List<RecommendInfo> recommendInfos);

    void onRequestFail();

}
