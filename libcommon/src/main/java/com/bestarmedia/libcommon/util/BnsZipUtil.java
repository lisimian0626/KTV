package com.bestarmedia.libcommon.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by admin on 2016/7/27.
 */
public class BnsZipUtil {

    public static boolean unzip(String zipFile, File targetDir, Handler handler) {
        File zf = new File(zipFile);
        if (!zf.exists()) {
            return false;
        }
        long totalLen = zf.length();
        long upzipLen = 0;
        int BUFFER = 4096; //这里缓冲区我们使用4KB，
        String strEntry; //保存每个zip的条目名称
        try {
            BufferedOutputStream dest = null; //缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; //每个zip条目的实例
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();
                    File entryFile = new File(targetDir.getParent() + "/" + strEntry);
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                        upzipLen = upzipLen + count;
                    }
                    Logger.i("BnsZipUtil: ", "=" + entry + " progress:" + upzipLen + "/" + totalLen);
                    Message msg = new Message();
                    msg.what = 1;
                    Bundle bd = new Bundle();
                    bd.putLong("total", totalLen);
                    bd.putLong("progress", upzipLen);
                    msg.setData(bd);
                    handler.sendMessage(msg);
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    Logger.w("BnsZipUtil", "unzip ex: " + ex.toString());
                }
            }
            zis.close();
            return true;
        } catch (Exception cwj) {
            Logger.w("BnsZipUtil", "unzip cwj: " + cwj.toString());
            return false;
        }
    }


//    public static boolean unzip(String zipPath, File targetDir) throws ZipException, IOException {
//        File zipFile = new File(zipPath);
//        if (!targetDir.exists()) {
//            targetDir.mkdirs();
//        }
//        ZipFile zf = new ZipFile(zipFile);
//        long totalLen = zipFile.length();
//        long upzipLen = 0;
//        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
//            ZipEntry entry = ((ZipEntry) entries.nextElement());
//            InputStream in = zf.getInputStream(entry);
//            String str = targetDir + File.separator + entry.getName();
//            File desFile = new File(str);
//            if (!desFile.exists()) {
//                File fileParentDir = desFile.getParentFile();
//                if (!fileParentDir.exists()) {
//                    fileParentDir.mkdirs();
//                }
//                desFile.createNewFile();
//            }
//            OutputStream out = new FileOutputStream(desFile);
//            byte buffer[] = new byte[4096];
//            int realLength;
//            while ((realLength = in.read(buffer)) > 0) {
//                upzipLen = upzipLen + realLength;
//                Logger.d("BnsZipUtil","totalLen");
//                out.write(buffer, 0, realLength);
//            }
//
//            in.close();
//            out.close();
//            return true;
//        }
//        return false;
//    }
}
