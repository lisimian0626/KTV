package com.bestarmedia.libcommon.security;

import android.util.Base64;
import android.util.Log;

import com.bestarmedia.libcommon.model.store.AuthKeystore;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by J Wong on 2017/5/23.
 */

public class DesKeystore {

    public enum DiscuzAuthcodeMode {
        Encode, Decode
    }

    public static String CutString(String str, int startIndex, int length) {
        if (startIndex >= 0) {
            if (length < 0) {
                length = length * -1;
                if (startIndex - length < 0) {
                    length = startIndex;
                    startIndex = 0;
                } else {
                    startIndex = startIndex - length;
                }
            }

            if (startIndex > str.length()) {
                return "";
            }

        } else {
            if (length < 0) {
                return "";
            } else {
                if (length + startIndex > 0) {
                    length = length + startIndex;
                    startIndex = 0;
                } else {
                    return "";
                }
            }
        }

        if (str.length() - startIndex < length) {
            length = str.length() - startIndex;
        }

        return str.substring(startIndex, startIndex + length);
    }


    public static String CutString(String str, int startIndex) {
        return CutString(str, startIndex, str.length());
    }

    /**
     * @param plainText 明文
     * @return 32位密文
     */
    public static String MD5(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
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
            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

//    public static String MD5(String str) {
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(str.getBytes());
//            byte[] b = md5.digest();//加密
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < b.length; i++) {
//                sb.append(b[i]);
//            }
//            return sb.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }


    public static boolean StrIsNullOrEmpty(String str) {
        if (str == null || str.trim().equals("")) {
            return true;
        }
        return false;
    }

    static private byte[] GetKey(byte[] pass, int kLen) {
        byte[] mBox = new byte[kLen];

        for (int i = 0; i < kLen; i++) {
            mBox[i] = (byte) i;
        }

        int j = 0;
        for (int i = 0; i < kLen; i++) {

            j = (j + (int) ((mBox[i] + 256) % 256) + pass[i % pass.length])
                    % kLen;

            byte temp = mBox[i];
            mBox[i] = mBox[j];
            mBox[j] = temp;
        }

        return mBox;
    }


    public static String RandomString(int lens) {
        char[] CharArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int clens = CharArray.length;
        String sCode = "";
        Random random = new Random();
        for (int i = 0; i < lens; i++) {
            sCode += CharArray[Math.abs(random.nextInt(clens))];
        }
        return sCode;
    }

    public static String authcodeEncode(String source, String key, int expiry) {
        return authcode(source, key, DiscuzAuthcodeMode.Encode, expiry);

    }


    public static String authcodeEncode(String source, String key) {
        return authcode(source, key, DiscuzAuthcodeMode.Encode, 0);

    }


    public static String authcodeDecode(String source, String key) {
        return authcode(source, key, DiscuzAuthcodeMode.Decode, 0);

    }

    private static String authcode(String source, String key, DiscuzAuthcodeMode operation, int expiry) {
        try {
            if (source == null || key == null) {
                return "";
            }
            int ckey_length = 16;
            String keya, keyb, keyc, cryptkey, result;
            key = MD5(key);
            keya = MD5(CutString(key, 0, 16));
            keyb = MD5(CutString(key, 16, 16));
            keyc = ckey_length > 0 ? (operation == DiscuzAuthcodeMode.Decode ? CutString(
                    source, 0, ckey_length)
                    : RandomString(ckey_length))
                    : "";
            cryptkey = keya + MD5(keya + keyc);

            if (operation == DiscuzAuthcodeMode.Decode) {
                byte[] temp;
                temp = Base64.decode(CutString(source, ckey_length), Base64.DEFAULT);
                result = new String(RC4(temp, cryptkey));
                if (CutString(result, 10, 16).equals(CutString(MD5(CutString(result, 26) + keyb), 0, 16))) {
                    return CutString(result, 26);
                } else {
                    temp = Base64.decode(CutString(source + "=", ckey_length), Base64.DEFAULT);
                    result = new String(RC4(temp, cryptkey));
                    if (CutString(result, 10, 16).equals(CutString(MD5(CutString(result, 26) + keyb), 0, 16))) {
                        return CutString(result, 26);
                    } else {
                        temp = Base64.decode(CutString(source + "==", ckey_length), Base64.DEFAULT);
                        result = new String(RC4(temp, cryptkey));
                        if (CutString(result, 10, 16).equals(CutString(MD5(CutString(result, 26) + keyb), 0, 16))) {
                            return CutString(result, 26);
                        } else {
                            return "2";
                        }
                    }
                }
            } else {
                source = "0000000000" + CutString(MD5(source + keyb), 0, 16) + source;
                byte[] temp = RC4(source.getBytes("GBK"), cryptkey);
                return keyc + Base64.encode(temp, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static byte[] RC4(byte[] input, String pass) {
        if (input == null || pass == null)
            return null;


        byte[] output = new byte[input.length];
        byte[] mBox = GetKey(pass.getBytes(), 256);

        // 加密
        int i = 0;
        int j = 0;

        for (int offset = 0; offset < input.length; offset++) {
            i = (i + 1) % mBox.length;
            j = (j + ((mBox[i] + 256) % 256)) % mBox.length;
            byte temp = mBox[i];
            mBox[i] = mBox[j];
            mBox[j] = temp;
            byte a = input[offset];
            //byte b = mBox[(mBox[i] + mBox[j] % mBox.Length) % mBox.Length];
            // mBox[j] 一定比 mBox.Length 小，不需要在取模
            byte b = mBox[(toInt(mBox[i]) + toInt(mBox[j])) % mBox.length];
            output[offset] = (byte) ((int) a ^ toInt(b));
        }

        return output;
    }

    public static int toInt(byte b) {
        return ((b + 256) % 256);
    }


    public static AuthKeystore covert(String keystore) {
        AuthKeystore authKeystore = null;
        try {
            Gson gson = new Gson();
            authKeystore = gson.fromJson(keystore, AuthKeystore.class);
        } catch (Exception e) {
            Log.e("DesKeystore", "AuthKeystore  covert ex:" + e.toString());
        }
        return authKeystore;
    }
}
