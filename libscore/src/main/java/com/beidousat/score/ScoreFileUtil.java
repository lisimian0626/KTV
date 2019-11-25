package com.beidousat.score;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2016/6/12.
 */
public class ScoreFileUtil {
    /**
     * 读取评分文件
     *
     * @param nodeInfo
     * @return
     */
    public static ArrayList<NoteInfo> readNote(String nodeInfo) {
        ArrayList<NoteInfo> noteList = new ArrayList<>();
        if (!TextUtils.isEmpty(nodeInfo)) {
            try {
                String[] lines = nodeInfo.split(";\r\n");
                if (lines != null && lines.length > 0) {
                    Log.i("ScoreFileUtil", "readNote lines:" + lines.length);
                    for (String line : lines) {
                        line.replace(";", "").replace("\r\n", "");
                        String[] segs = line.split(",");
                        float pos = Float.parseFloat(segs[0]);
                        float len = Float.parseFloat(segs[1]);
                        float key = Float.parseFloat(segs[2]);
                        int score = Integer.valueOf(segs[3]);
                        NoteInfo n = new NoteInfo(pos, len, key, score);
                        noteList.add(n);
                    }
                }
            } catch (Exception e) {
                Log.e("ScoreFileUtil", "readNote ex:" + e.toString());
            }
        }
        return noteList;
    }

    public static List<ScoreLineInfo> readNote2(String nodeInfo) {
        List<ScoreLineInfo> scoreLineInfos = new ArrayList<>();
        if (!TextUtils.isEmpty(nodeInfo)) {
            try {
                String[] lines = nodeInfo.split(";\r\n");
                if (lines != null && lines.length > 0) {
                    Log.i("ScoreFileUtil", "readNote2 lines:" + lines.length);
                    for (String line : lines) {
                        line.replace(";", "").replace("\r\n", "");
                        String[] segs = line.split(",");
                        float time = Float.parseFloat(segs[0]);
                        float score = Float.parseFloat(segs[1]);
                        ScoreLineInfo scoreLineInfo = new ScoreLineInfo(time, score);
                        scoreLineInfos.add(scoreLineInfo);
                    }
                }
            } catch (Exception e) {
                Log.e("ScoreFileUtil", "readNote2 ex:" + e.toString());
            }
        }
        return scoreLineInfos;
    }
}
