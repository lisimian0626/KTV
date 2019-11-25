package com.beidousat.score;

/**
 * Created by admin on 2016/6/7.
 */

public class NoteInfo {
    public NoteInfo() {
    }

    public NoteInfo(float in_pos, float in_len, float in_key, int score) {
        super();
        this.startPos = in_pos;
        this.len = in_len;
        this.key = in_key;
        this.endPos = in_pos + in_len;
        this.score = score;
    }

    public float startPos; // note的起始位置
    public float len; // note的持续时间
    public float key; // note的音调
    public float endPos;

    public int score;

}
