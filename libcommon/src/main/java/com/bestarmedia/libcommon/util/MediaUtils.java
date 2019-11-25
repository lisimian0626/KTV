package com.bestarmedia.libcommon.util;

import java.util.Random;

/**
 * Created by J Wong on 2017/10/31.
 */

public class MediaUtils {

    public static boolean isAudioFormatter(String path) {
        String lc = path.toLowerCase();
        return lc.endsWith(".mp3") || lc.endsWith(".wav") || lc.endsWith(".mp2") || lc.endsWith(".ac3")
                || lc.endsWith(".flac") || lc.endsWith(".ape") || lc.endsWith(".ogg") || lc.endsWith(".aac") || lc.endsWith(".wma");
    }


    public static boolean isLiveUrl(String path) {
        String lc = path.toLowerCase();
        return lc.startsWith("udp://") || lc.startsWith("rtmp://") || lc.startsWith("rtsp://") || lc.startsWith("rtp://") || lc.startsWith("rtcp://") || lc.endsWith(".m3u8");
    }


    public static boolean isVideo(String path) {
        String lc = path.toLowerCase();
        return lc.endsWith(".mp4") || lc.endsWith(".avi") || lc.endsWith(".wmv") || lc.endsWith(".rm") || lc.endsWith(".3gp") || lc.endsWith(".mov")
                || lc.endsWith(".rmvb") || lc.endsWith(".mpg") || lc.endsWith(".mpe") || lc.endsWith(".mpa") || lc.endsWith(".flv");
    }

    public static String getTestMediaPath() {
        Random random = new Random();
        return files[random.nextInt(files.length)];
    }

    private static final String[] files = new String[]{
            "http://172.30.1.230:10230/data/song/fdisk12/535c14ff-6ca1-441d-a4c5-0fd18e860c1b.mp4",
            "http://172.30.1.230:10230/data/song/xin/db573b4793e934f6232a8f2165e99c69.mp3",
            "http://172.30.1.230:10230/data/song/disk11/105148.mp4",
            "http://172.30.1.230:10230/data/song/xin/353b3a799f76f67cc97950503a41abad.mp4",
            "http://172.30.1.230:10230/data/song/xin/ef24ab252f360f80bfc691ff67281878.mp3",
            "http://172.30.1.230:10230/data/ad/H256-1080p-1.mp4",
            "http://172.30.1.230:10230/data/song/xin/ef24ab252f360f80bfc691ff67281878.mp3",
            "http://172.30.1.230:10230/data/song/xin/f7eb3f7cab9d30f38dc6c9d6c1476f2d.mp4",
            "http://172.30.1.230:10230/data/necessary/score_result.mp4",
            "http://172.30.1.230:10230/data/song/xin/fd420a98-16cd-4dbd-9544-a402aeb8d03a.mp4",
            "http://172.30.1.230:10230/data/song/xin/ef24ab252f360f80bfc691ff67281878.mp3",
            "http://172.30.1.230:10230/data/song/xin/feca70391baba665838f625934a0eb1e.mp4",
            "http://172.30.1.230:10230/data/song/xin/dd112e4e-e891-4776-a6d3-409b9d6fe790.mp4",
            "http://172.30.1.230:10230/data/song/xin/fea429ee2df6a7bee7904b4b79683ce6.mp3",
            "http://172.30.1.230:10230/data/song/xin/b9803add25e26abfbe264cb62061f97d.mp4",
            "http://172.30.1.230:10230/data/necessary/score_result.mp4",
            "http://172.30.1.230:10230/data/song/xin/9c1fd1cdf33c94fe80e908a78bf38658.mp4",
            "http://172.30.1.230:10230/data/song/xin/f3712dfc9ac153c3b5215e6a1905ab8c.mp3",
            "http://172.30.1.230:10230/data/necessary/ad_default.mp4"
    };


}
