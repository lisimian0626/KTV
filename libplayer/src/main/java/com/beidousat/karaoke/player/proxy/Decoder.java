package com.beidousat.karaoke.player.proxy;

public class Decoder {
    private static Decoder _this = new Decoder();
    private Object lock = new Object();

    private Decoder() {
    }

    public native byte[] decode(byte[] src, int len);

    static {
        System.loadLibrary("decoder");
    }

    public boolean checkEncryt(byte[] src) {
        for (int i = 0; i < Config.HEAD_LEN; i++) {
            if (Config.ENCODE_HEAD[i] != src[i])
                return false;
        }
        return true;
    }

    public byte[] decoder(byte[] src, int len) {
        synchronized (lock) {
            if (len > Config.ENCODE_HEAD_LEN) {
//			System.out.printf("src = %x,%x,%x,%x,%x,%x,%x,%x,\r\n", src[0],src[1],src[2],src[3],src[4],src[5],src[6],src[7]);
                if (!checkEncryt(src)) {
                    byte[] rebyte = new byte[len];
                    System.arraycopy(src, 0, rebyte, 0, len);
                    return rebyte;
                }
                byte[] debyte = new byte[Config.ENCODE_HEAD_LEN];
                System.arraycopy(src, 0, debyte, 0, Config.ENCODE_HEAD_LEN);
                debyte = decode(debyte, Config.ENCODE_HEAD_LEN);
//			System.out.printf("%x,%x,%x,%x,%x,%x,%x,%x,\r\n", debyte[0],debyte[1],debyte[2],debyte[3],debyte[4],debyte[5],debyte[6],debyte[7]);
                byte[] rebyte = new byte[len - Config.ENCODE_HEAD_LEN + debyte.length];
                System.arraycopy(debyte, 0, rebyte, 0, debyte.length);
                System.arraycopy(src, Config.ENCODE_HEAD_LEN, rebyte, debyte.length, len - Config.ENCODE_HEAD_LEN);
                return rebyte;
            }
        }
        return src;
    }

    public static Decoder getinstance() {
        return _this;
    }
}
