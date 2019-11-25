package com.bestarmedia.libcommon.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/***
 * @author zhouya
 * @version V1.0
 * @Description: des加密/解密类
 */
public class Des {
    /**
     * 加密（使用DES算法）
     *
     * @param txt 需要加密的文本
     * @param key 密钥
     * @return 成功加密的文本
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String enCrypto(String txt, String key) throws InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        StringBuffer sb = new StringBuffer();
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory skeyFactory = null;
        Cipher cipher = null;
        try {
            skeyFactory = SecretKeyFactory.getInstance("DES");
            cipher = Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecretKey deskey = skeyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] cipherText = cipher.doFinal(txt.getBytes());
        for (int n = 0; n < cipherText.length; n++) {
            String stmp = (Integer.toHexString(cipherText[n] & 0XFF));

            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 解密（使用DES算法）
     *
     * @param txt 需要解密的文本
     * @param key 密钥
     * @return 成功解密的文本
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String deCrypto(String txt, String key)
            throws InvalidKeyException, InvalidKeySpecException,
            NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory skeyFactory = null;
        Cipher cipher = null;
        try {
            skeyFactory = SecretKeyFactory.getInstance("DES");
            cipher = Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecretKey deskey = skeyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] btxts = new byte[txt.length() / 2];
        for (int i = 0, count = txt.length(); i < count; i += 2) {
            btxts[i / 2] = (byte) Integer.parseInt(txt.substring(i, i + 2), 16);
        }
        return (new String(cipher.doFinal(btxts)));
    }

    public static void main(String[] args) throws InvalidKeyException,
            NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException {
        String soureTxt = "这里是要加密的文本内容";
        String key = "这里是自定义的密钥";
        String str = null;
        System.out.println("明文：" + soureTxt);
        str = enCrypto(soureTxt, key);
        System.out.println("加密：" + str);
        System.out.println("解密：" + deCrypto(str, key));
        System.out.println(str + "====" + key);

    }
}