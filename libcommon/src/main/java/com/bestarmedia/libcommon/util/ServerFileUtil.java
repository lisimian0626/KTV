package com.bestarmedia.libcommon.util;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.bestarmedia.libcommon.config.OkConfig;

import java.io.File;

/**
 * Created by J Wong on 2015/10/20 09:57.
 */
public class ServerFileUtil {

    private final static String USB_PATH = "mnt/usbhost1/";

    private final static String TAG = ServerFileUtil.class.getSimpleName();

//    public static String getScoreNoteUrl(String songFilePath) {
//        if (TextUtils.isEmpty(songFilePath)) {
//            return null;
//        }
//
//        String url = (OkConfig.getFilePath() + "data/grade/" + getFileName(songFilePath) + ".txt").replace(" ", "%20");
//
//        return url;
//    }
//
//    public static String getScoreNote2Url(String songFilePath) {
//        if (TextUtils.isEmpty(songFilePath)) {
//            return null;
//        }
//        String url = (OkConfig.getFilePath() + "data/grade/" + getFileName(songFilePath) + ".sec.txt").replace(" ", "%20");
//        return url;
//    }


    public static Uri getSingerImageUrl(String file) {
        if (TextUtils.isEmpty(file)) {
            return null;
        }
        String url = file.startsWith("http://") || file.startsWith("https://") ? file : OkConfig.getServerFile() + file;
        return Uri.parse(url.replace(" ", "%20"));
    }


    public static Uri getSingerThumbnailUrl(String file) {
        if (TextUtils.isEmpty(file)) {
            return null;
        }
//        String imgName = getFileName(file);
//        File dir = KaraokeSdHelper.getSingerImgDir();
//        File fileImage = new File(dir, imgName);
//        if (fileImage.exists()) {
//            return Uri.fromFile(fileImage);
//        }
        String url = file.startsWith("http://") || file.startsWith("https://") ? file : OkConfig.getServerFile() + file;
        return Uri.parse(url.replace(" ", "%20"));
    }

    public static Uri getImageUrl(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        String url = filePath.startsWith("http://") || filePath.startsWith("https://") ? filePath : OkConfig.getServerFile() + filePath;
        return Uri.parse(url.replace(" ", "%20"));
    }

    public static String getFileUrl(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        String url = (filePath.startsWith("http://") || filePath.startsWith("https://") || MediaUtils.isLiveUrl(filePath)) ? filePath : (OkConfig.getServerFile() + filePath);
        return url.replace(" ", "%20");
    }


    private static Uri getUsbSingerImageThumbnail(String file) {
        if (TextUtils.isEmpty(file)) {
            return null;
        }
        String path = file.startsWith(USB_PATH) ? file : (USB_PATH + "mnt/data/Img/SingerImg150/" + getFileName(file));
        return Uri.fromFile(new File(path));
    }

    private static Uri getUsbSingerImageUri(String file) {
        if (TextUtils.isEmpty(file)) {
            return null;
        }
        String path = file.startsWith(USB_PATH) ? file : (USB_PATH + "mnt/data/Img/SingerImg/" + getFileName(file));
        return Uri.fromFile(new File(path));
    }

    private static String getSdFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        String fileName = getFileName(filePath);
        String path = "file://" + Environment.getExternalStorageDirectory() + "/BNS/";
        if (filePath.contains("ad/")) {
            path = path + "ad/" + fileName;
        } else if (filePath.contains("song/")) {
            path = path + "song/" + fileName;
        } else if (filePath.contains("movie/")) {
            path = path + "movie/" + fileName;
        }
        return path;
    }

    private static String getUsbFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        String path = filePath.startsWith(USB_PATH) ? filePath : (USB_PATH + filePath);
        Logger.i(TAG, "usb file is exist:" + path + " :" + new File(path).exists());
        return path;
    }

    private static Uri getSdImgFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        String fileName = getFileName(filePath);
        String path = Environment.getExternalStorageDirectory() + "/BNS/Img/";
        if (filePath.contains("AlbumImg/")) {
            path = path + "AlbumImg/" + fileName;
        } else if (filePath.contains("LiveProgram/")) {
            path = path + "LiveProgram/" + fileName;
        } else if (filePath.contains("MovieHomePage/")) {
            path = path + "MovieHomePage/" + fileName;
        } else if (filePath.contains("SingerImg/")) {
            path = path + "SingerImg/" + fileName;
        } else if (filePath.contains("Topics/")) {
            path = path + "Topics/" + fileName;
        }
        return Uri.fromFile(new File(path));
    }

    private static Uri getUsbImgFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return Uri.fromFile(new File(filePath.startsWith(USB_PATH) ? filePath : (USB_PATH + filePath)));
    }


    public static String getFileName(String url) {
        if (TextUtils.isEmpty(url))
            return "";
        if (url.contains("/")) {
            int lastIndex = url.lastIndexOf("/");
            return url.substring(lastIndex + 1);
        } else {
            return url;
        }
    }

    public static String getFileNameExceptSuffix(String url) {
        if (TextUtils.isEmpty(url))
            return "";
        if (url.contains("/")) {
            int lastIndex = url.lastIndexOf("/");
            int index = url.lastIndexOf(".");
            return url.substring(lastIndex + 1, index);
        } else if (url.contains(".")) {
            int index = url.lastIndexOf(".");
            return url.substring(0, index);
        } else {
            return url;
        }
    }

    public static String getImageName(String url) {
        if (TextUtils.isEmpty(url))
            return null;
        int pos = url.lastIndexOf("/");
        if (pos < 0)
            return null;
        return url.substring(pos + 1);
    }

    public static File getScoreNote(String gradeLibFileUrls) {
        if (TextUtils.isEmpty(gradeLibFileUrls) || !gradeLibFileUrls.contains("|") || !gradeLibFileUrls.startsWith("http://")) {
            return null;
        }
        String[] urls = gradeLibFileUrls.split("\\|");
        String fileName = getFileName(urls[0]);
        return new File(KaraokeSdHelper.getNote(), fileName);
    }

    public static File getScoreNote2(String gradeLibFileUrls) {
        if (TextUtils.isEmpty(gradeLibFileUrls) || !gradeLibFileUrls.contains("|") || !gradeLibFileUrls.startsWith("http://")) {
            return null;
        }
        String[] urls = gradeLibFileUrls.split("\\|");
        String fileName = getFileName(urls[1]);
        return new File(KaraokeSdHelper.getNote(), fileName);
    }


    private static File getScoreNoteSecFromUsb(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return new File(filePath.startsWith(USB_PATH) ? filePath : (USB_PATH + "mnt/data/grade/" + filePath));
    }

}
