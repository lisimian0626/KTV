package com.bestarmedia.libcommon.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/10/20 10:10.
 */
public class BaseListV4 implements Serializable {

    @Expose
    public int current_page;

//    @Expose
//    public int from;

//    @Expose
//    public int to;

    @Expose
    public int last_page;

//    @Expose
//    public String next_page_url;

//    @Expose
//    public String path;

    @Expose
    public int per_page;

//    @Expose
//    public String prev_page_url;

    @Expose
    public int total;

}
