package com.beidousat.karaoke.player.proxy;

import android.os.StatFs;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 工具类
 *
 * @author hellogv
 */
public class Utils {
    private static final String TAG = "com.proxy.utils";

    /**
     * 获取重定向后的URL，即真正有效的链接
     *
     * @param urlString
     * @return
     */
    public static String getRedirectUrl(String urlString) {
        String result = urlString;
        // 取得取得默认的HttpClient实例
        DefaultHttpClient httpClient = new DefaultHttpClient();

        // 创建HttpGet实例
        HttpGet request = new HttpGet(urlString);
        try {
            // 重定向设置连接服务器
            httpClient.setRedirectHandler(new RedirectHandler() {
                public URI getLocationURI(HttpResponse response,
                                          HttpContext context) throws ProtocolException {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
                            || (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
                            || (statusCode == HttpStatus.SC_SEE_OTHER)
                            || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                        // 此处重定向处理
                        return null;
                    }
                    return null;
                }

                public boolean isRedirectRequested(HttpResponse response,
                                                   HttpContext context) {
                    return false;
                }

            });
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
                    || (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
                    || (statusCode == HttpStatus.SC_SEE_OTHER)
                    || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                // 从头中取出转向的地址
                Header locationHeader = response.getFirstHeader("Location");
                if (locationHeader != null) {
                    String locationUrl = locationHeader.getValue();//Location Url
                    httpClient.getConnectionManager().shutdown();// 释放连接
                    return getRedirectUrl(locationUrl);//防止多次重定向
                }
            }
        } catch (ClientProtocolException ex) {
            Log.e(TAG, getExceptionMessage(ex));
        } catch (IOException ex) {
            Log.e(TAG, getExceptionMessage(ex));
        }
        httpClient.getConnectionManager().shutdown();// 释放连接
        return result;
    }

    public static String getSubString(String source, String startStr, String endStr) {
        try {
            int startIndex = source.indexOf(startStr) + startStr.length();
            int endIndex = source.indexOf(endStr, startIndex);
            return source.substring(startIndex, endIndex);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取有效的文件名
     *
     * @param str
     * @return
     */
    public static String getValidFileName(String str) {
        str = str.replace("\\", "");
        str = str.replace("/", "");
        str = str.replace(":", "");
        str = str.replace("*", "");
        str = str.replace("?", "");
        str = str.replace("\"", "");
        str = str.replace("<", "");
        str = str.replace(">", "");
        str = str.replace("|", "");
        str = str.replace(" ", "_");    //前面的替换会产生空格,最后将其一并替换掉
        return str;
    }

    /**
     * 获取外部存储器可用的空间
     *
     * @return
     */
    public static long getAvailaleSize(String dir) {
        StatFs stat = new StatFs(dir);//path.getPath());
        long totalBlocks = stat.getBlockCount();// 获取block数量
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize; // 获取可用大小
    }

    /**
     * 获取文件夹内的文件，按日期排序，从旧到新
     *
     * @param dirPath
     * @return
     */
    static private List<File> getFilesSortByDate(String dirPath) {
        List<File> result = new ArrayList<File>();
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0)
            return result;

        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.valueOf(f1.lastModified()).compareTo(
                        f2.lastModified());
            }
        });

        for (int i = 0; i < files.length; i++) {
            result.add(files[i]);
//			Log.i(TAG, i+":"+files[i].lastModified() + "---" + files[i].getPath());
        }
        return result;
    }

    /**
     * 删除多余的缓存文件
     *
     * @param dirPath 缓存文件的文件夹路径
     * @param maximun 缓存文件的最大数量
     */
    static protected void asynRemoveBufferFile(final String dirPath, final int maximun) {
        new Thread() {
            public void run() {
                List<File> lstBufferFile = Utils.getFilesSortByDate(dirPath);
                while (lstBufferFile.size() > maximun) {
//					Log.i(TAG, "---delete " + lstBufferFile.get(0).getPath());
                    lstBufferFile.get(0).delete();
                    lstBufferFile.remove(0);
                }
            }
        }.start();
    }

    public static String getExceptionMessage(Exception ex) {
        String result = "";
        StackTraceElement[] stes = ex.getStackTrace();
        for (int i = 0; i < stes.length; i++) {
            result = result + stes[i].getClassName()
                    + "." + stes[i].getMethodName()
                    + "  " + stes[i].getLineNumber() + "line"
                    + "\r\n";
        }
        return result;
    }

    public static String stringMD5(String input) {

        try {
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 输入的字符串转换成字节数组
            byte[] inputByteArray = input.getBytes();
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);

        } catch (NoSuchAlgorithmException e) {
            return null;

        }

    }

    public static String byteArrayToHex(byte[] byteArray) {
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int index = 0;
        char[] resultCharArray = new char[byteArray.length * 2];
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];

        }
        return new String(resultCharArray);
    }
}
