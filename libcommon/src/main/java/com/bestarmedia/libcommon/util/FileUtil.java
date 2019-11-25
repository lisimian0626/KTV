package com.bestarmedia.libcommon.util;

import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;

import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

/**
 * Created by J Wong on 2016/11/21.
 */

public class FileUtil {

    public static String readFileContent(String strFilePath) {
        StringBuilder builder = new StringBuilder();
        Log.w("FileUtil", "readFileContent file :" + strFilePath);
        //打开文件
        File file = new File(strFilePath);
        if (file.exists() && file.isFile()) {
            InputStream instream = null;
            InputStreamReader inputreader = null;
            BufferedReader buffreader = null;
            try {
                instream = new FileInputStream(file);
                if (instream != null) {
                    inputreader = new InputStreamReader(instream);
                    buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        builder.append(line);
                    }
                }
            } catch (Exception e) {
                Log.w("FileUtil", "readFileContent ex:" + e.toString());
            } finally {
                if (buffreader != null) {
                    try {
                        buffreader.close();
                    } catch (Exception e) {
                        Log.w("FileUtil", "readFileContent close buffreader ex:" + e.toString());
                    }
                }
                if (inputreader != null) {
                    try {
                        inputreader.close();
                    } catch (Exception e) {
                        Log.w("FileUtil", "readFileContent close inputreader ex:" + e.toString());
                    }
                }
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (Exception e) {
                        Log.w("FileUtil", "readFileContent close instream ex:" + e.toString());
                    }
                }
            }
        }
        return builder.toString();
    }

    /**
     * 获取一个文件的md5值
     *
     * @return md5 value
     */
    public static String getFileMD5String(File file) {
        if (!file.isFile()) {
            return null;
        }
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * public static String getFileMD5String(File file) {
     * if (!file.isFile()) {
     * return null;
     * }
     * <p>
     * MessageDigest digest = null;
     * FileInputStream in = null;
     * byte buffer[] = new byte[1024];
     * int len;
     * try {
     * digest = MessageDigest.getInstance("MD5");
     * in = new FileInputStream(file);
     * while ((len = in.read(buffer, 0, 1024)) != -1) {
     * digest.update(buffer, 0, len);
     * }
     * in.close();
     * } catch (Exception e) {
     * Logger.w("FileUtil", "getFileMD5String ex:" + e.toString());
     * return null;
     * }
     * BigInteger bigInt = new BigInteger(1, digest.digest());
     * String md5 = bigInt.toString(16);
     * Logger.d("FileUtil", "getFileMD5String file md5==" + md5);
     * return md5;
     * }
     */
    public static boolean chmod777File(File file) {
        if (OkConfig.boxManufacturer() == 1) {
            return chmod777FileSu(file);
        }
        try {
            file.setExecutable(true);//设置可执行权限
            file.setReadable(true);//设置可读权限
            file.setWritable(true);//设置可写权限
            String cmd = "chmod 777 " + file.getAbsolutePath();
            Logger.d("FileUtil", "exec  cmd :" + cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("FileUtil", "su -c chmod 777 SongSecurityKey Exception");
        }
        return false;
    }

    private static boolean chmod777FileSu(File file) {
        try {
            file.setExecutable(true);//设置可执行权限
            file.setReadable(true);//设置可读权限
            file.setWritable(true);//设置可写权限
//            Process p = Runtime.getRuntime().exec("su");
//            p = Runtime.getRuntime().exec("su -c chmod 777 " + file.getAbsolutePath());
//            Runtime.getRuntime().exec(new String[]{"/system/bin/su","-c", cmd});
            String cmd = "chmod 777 " + file.getAbsolutePath();
            Logger.d("FileUtil", "chmod777FileSu cmd :" + cmd);
            Runtime.getRuntime().exec(new String[]{"/system/xbin/su", "-c", cmd});
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("FileUtil", "su -c chmod 777 SongSecurityKey Exception");
        }
        return false;
    }
}
