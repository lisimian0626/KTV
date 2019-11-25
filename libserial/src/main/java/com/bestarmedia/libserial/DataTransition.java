package com.bestarmedia.libserial;

import java.util.Locale;


/**
 * 数据转换工具
 *
 * @author LUOYE
 * @data 2015-07-05 02:13:26
 */
public class DataTransition {
    /**
     * 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
     *
     * @param num 用于判奇偶的数字
     * @return 位运算结果，最后一位是1则为奇数，为0是偶数
     */
    public static int isOdd(int num) {
        return num & 0x1;
    }

    /**
     * Hex字符串转int
     *
     * @param hex 十六进制字符
     * @return 转换后的整数
     */
    public static int hexToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * Hex字符串转byte
     *
     * @param hex 十六进制字符
     * @return 转换后的整数
     */
    public static byte hexToByte(String hex) {
        return (byte) Integer.parseInt(hex, 16);
    }

    /**
     * 1字节转2个Hex字符
     *
     * @param bt 一个字节
     * @return 字节的十六进制字符串表示，如bt = 255，转换后十六进制字符串表示为FF
     */
    public static String byte2Hex(Byte bt) {
        return String.format("%02x", bt).toUpperCase(Locale.getDefault());
    }

    /**
     * 字节数组转转hex字符串
     *
     * @param btArr 字节数组转十六进制字符串
     * @return 所有字节的十六进制字符串
     */
    public static String byteArrToHex(byte[] btArr) {
        StringBuilder strBuilder = new StringBuilder();
        int j = btArr.length;
        for (int i = 0; i < j; i++) {
            strBuilder.append(byte2Hex(btArr[i]));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    /**
     * 字节数组转转hex字符串，可选长度
     *
     * @param btArr  用于转换的字节数组
     * @param offset 开始转换的字节位置，起始位置以0开始计数
     * @param count  从开始位置起转换的字节个数，若offset + count > btArr.length，则只处理到btArr末尾
     * @return 转换后的十六进制字符串
     */
    public static String byteArrToHex(byte[] btArr, int offset, int count) {
        StringBuilder strBuilder = new StringBuilder();
        int j = offset + count > btArr.length ? btArr.length : offset + count;
        for (int i = offset; i < j; i++) {
            strBuilder.append(byte2Hex(btArr[i]));
        }
        return strBuilder.toString();
    }

    /**
     * hex字符串转字节数组
     *
     * @param hex 十六进制字符串
     * @return 转换后的字节数组
     */
    public static byte[] hexToByteArr(String hex) {
        int nLen = hex.length();
        byte[] result;
        if (isOdd(nLen) == 1) {// 奇数
            nLen++;
            result = new byte[(nLen / 2)];
            hex = "0" + hex;
        } else {// 偶数
            result = new byte[(nLen / 2)];
        }
        int j = 0;
        for (int i = 0; i < nLen; i += 2) {
            result[j] = hexToByte(hex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * long 转byte 小端
     *
     * @param num
     * @return
     */
    public static byte[] long2BytesLittle(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 7; ix >= 0; ix--) {
            int offset = (ix) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /**
     * long转byte 大端
     *
     * @param num
     * @return
     */
    public static byte[] long2BytesBig(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[7-ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /**
     * 前面4字节wifi信号量，后4字节名称
     * @param num
     * @return
     */
    public static byte[] long2BytesBig(int num, String name) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[3-ix] = (byte) ((num >> offset) & 0xff);
        }
        byte[] namebyte=name.getBytes();
        for(int i=0;i<4&&i<namebyte.length;i++)
        {
            byteNum[i+4]=namebyte[i];
        }
        return byteNum;
    }

    /**
     * ip地址转成long
     *
     * @param strIp ip地址
     * @return
     */
    public static long ipToLong(String strIp) {
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static long macToLong(String strMac) {
        long[] mac = new long[6];
        int position1 = strMac.indexOf(":");
        int position2 = strMac.indexOf(":", position1 + 1);
        int position3 = strMac.indexOf(":", position2 + 1);
        int position4 = strMac.indexOf(":", position3 + 1);
        int position5 = strMac.indexOf(":", position4 + 1);
        mac[0] = Long.parseLong(strMac.substring(0, position1), 16);
        mac[1] = Long.parseLong(strMac.substring(position1 + 1, position2), 16);
        mac[2] = Long.parseLong(strMac.substring(position2 + 1, position3), 16);
        mac[3] = Long.parseLong(strMac.substring(position3 + 1, position4), 16);
        mac[4] = Long.parseLong(strMac.substring(position4 + 1, position5), 16);
        mac[5] = Long.parseLong(strMac.substring(position5 + 1), 16);
        return (mac[0] << 40) + (mac[1] << 32) + (mac[2] << 24) + (mac[3] << 16) + (mac[4] << 8) + mac[5];
    }
}