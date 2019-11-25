package com.bestarmedia.libcommon.security;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by J Wong on 2015/12/8 19:43.
 */

/**
 * 对外提供getMD5(String)方法
 *
 * @author randyjia
 */
public class Md5 {

    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
//            System.out.println("MD5(" + sourceStr + ",32) = " + result);
//            System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }


//    public static String getMD5(String val) throws NoSuchAlgorithmException {
//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        md5.update(val.getBytes());
//        byte[] m = md5.digest();//加密
//        return getString(m);
//    }
//
//    private static String getString(byte[] b) {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < b.length; i++) {
//            sb.append(b[i]);
//        }
//        return sb.toString();
//    }
}
