package com.bestarmedia.libnetty.netty;

public class ProtoFrame {

    public int id;

    public int length;

    public byte[] byteBuf;

    public ProtoFrame(int id, int length, byte[] byteBuf) {
        this.id = id;
        this.length = length;
        this.byteBuf = byteBuf;
    }
}
