package com.bestarmedia.liblame;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * Created by J Wong on 2016/9/12.
 */
public class AudioRecordFileUtil {

    private final static String ROOT = "/BNS/";

    private final static String RECORD = ROOT + "Record/";

    private final static String RECORD_FILE_SUFFIX = ".mp3";


    public static File getRecord() {
        if (!isSdcardExit())
            return null;
        String crash = Environment.getExternalStorageDirectory() + RECORD;
        File file = new File(crash);
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    private static boolean isSdcardExit() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getSdCardFreeSpace() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize;
//        long totalBlocks;
        long availableBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
//            totalBlocks = stat.getBlockCountLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
//            totalBlocks = stat.getBlockCount();
            availableBlocks = stat.getAvailableBlocks();
        }
        long freeSpace = blockSize * availableBlocks;
//        Logger.d("AudioFileFunc", "SdCardFreeSpace : " + freeSpace / 1024 / 1024);
        return freeSpace;
    }

    /**
     * @return 获取麦克风输入的原始音频流文件路径
     */
    public static String getRecordFilePath(String fileName, boolean deleteExit) {
        String mAudioRawPath = "";
        if (isSdcardExit()) {
            File recordDir = getRecord();
            File raw = new File(recordDir, fileName + RECORD_FILE_SUFFIX);
            if (deleteExit && raw.exists())
                raw.delete();
            mAudioRawPath = raw.getAbsolutePath();
        }
        return mAudioRawPath;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isRecordFileExist(String fileName) {
        return getRecord() != null && new File(getRecord(), fileName + RECORD_FILE_SUFFIX).exists();
    }

//    public static File getRecordFile(String recordFile) {
//        File file = new File(getRecord(), recordFile + RECORD_FILE_SUFFIX);
//        return file;
//    }

    public static String createUUIDFilePath(String uuid) {
        return "data/Media/" + uuid + RECORD_FILE_SUFFIX;
    }

    public static void deleteRecordFiles() {
        new Thread(new DeleteRecordFiles()).start();
    }

    static class DeleteRecordFiles implements Runnable {
        @Override
        public void run() {
            try {
                if (getRecord() != null)
                    deleteDir(getRecord());//删除录音文件
            } catch (Exception e) {
                Log.w("AudioFileFunc", "DeleteRecordFiles ex:" + e.toString());
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null && children.length > 0) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    /**
     * @param path 文件的绝对路径
     * @return 获取文件大小
     */
    public static long getFileSize(String path) {
        File mFile = new File(path);
        if (!mFile.exists())
            return -1;
        return mFile.length();
    }
}
