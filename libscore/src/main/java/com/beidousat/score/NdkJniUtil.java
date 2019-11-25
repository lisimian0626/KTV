package com.beidousat.score;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by admin on 2016/6/6.
 */
public class NdkJniUtil {
    static {
        try {
            System.loadLibrary("BeidouScoreJniSo");
        } catch (Exception e) {
            Log.w("NdkJniUtil", "NdkJniUtil loadLibrary ex:" + e.toString());
        }
    }

    public native static int setNotes(ArrayList<NoteInfo> noteList // 评分基准数据列表
    ); // 返回：暂时无意义

    public native static KeyInfo[] getAnalyzeResult(double a[]  // 从麦克风得到的，经过预处理后的数据
            , long current_pos // 当前的帧号，从0开始，为4096的整数倍
            , int date_len); // 传入的数据长度，默认为4096

    public native static KeyInfo[] getAnalyzeResultEasy(double a[]  // 从麦克风得到的，经过预处理后的数据
            , long current_pos // 当前的帧号，从0开始，为4096的整数倍
            , int date_len); // 传入的数据长度，默认为4096

    // 返回：评分细节列表，供绘图使用，[[time1, key1], [time2, key2], [time3, key3],...]
    public native static float getScore(); // 返回：歌曲总分
}
