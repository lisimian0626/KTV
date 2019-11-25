package com.bestarmedia.libcommon.model.vod;

import com.bestarmedia.libcommon.model.BaseListV4;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by J Wong on 2015/11/3 17:30.
 */
public class PkRankings extends BaseListV4 {
    @Expose
    public List<PkRanking> data;

}