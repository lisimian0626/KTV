package com.bestarmedia.libcommon.transmission;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.interf.FileDownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by J Wong on 2015/12/7 18:41.
 */
public class FileDownloader {

    private long mTotalSize = 0L;
    private boolean IgnoreByFileLen = false;

    public void download(final File desFile, final String url, final FileDownloadListener listener) {
        if (desFile == null || TextUtils.isEmpty(url)) {
            return;
        }
        String fileUrl = url;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            fileUrl = OkConfig.getServerFile() + url;
        }
        Log.d(getClass().getSimpleName(), "download url:" + fileUrl);
        new AsyncDownloader(desFile, fileUrl, listener).execute();
    }


    public void setIgnoreByFileLen(boolean ignoreFileLen) {
        this.IgnoreByFileLen = ignoreFileLen;
    }

    private class AsyncDownloader extends AsyncTask<Void, Long, Boolean> {

        private String mUrl;
        private File mDesFile;
        private FileDownloadListener mFileDownloadListener;
        private File desFileTemp;

        private AsyncDownloader(File desFile, String url, FileDownloadListener listener) {
            mUrl = url;
            mDesFile = desFile;
            desFileTemp = new File(desFile.getAbsolutePath() + ".temp");
            mFileDownloadListener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            File parentFile = mDesFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            Log.d(getClass().getSimpleName(), "download url doInBackground:" + mUrl);
            OkHttpClient httpClient = new OkHttpClient();
            Call call = httpClient.newCall(new Request.Builder().url(mUrl).get().build());
            try {
                Response response = call.execute();
                if (response.code() == 200) {
                    InputStream inputStream = null;
                    FileOutputStream fileOutputStream = null;
                    try {
                        inputStream = response.body().byteStream();
                        fileOutputStream = new FileOutputStream(desFileTemp);
                        byte[] buff = new byte[1024 * 4];
                        long downloaded = 0;
                        long target = response.body().contentLength();
                        mTotalSize = target;
                        if (mDesFile.exists() && IgnoreByFileLen && mTotalSize == mDesFile.length()) {//通过文件大小判断是否需要下载
                            Log.d(getClass().getSimpleName(), "download not need to down:" + mUrl);
                            publishProgress(target, target);
                            return true;
                        } else {
                            Log.d(getClass().getSimpleName(), "download  need to down:" + mUrl);
                            publishProgress(0L, target);
                            while (true) {
                                int readed = inputStream.read(buff);
                                if (readed == -1) {
                                    break;
                                }
                                //write buff
                                fileOutputStream.write(buff, 0, readed);
                                downloaded += readed;
                                publishProgress(downloaded, target);
                                if (isCancelled()) {
                                    return false;
                                }
                            }
                            if (downloaded == target) {
                                if (mDesFile.exists()) {
                                    mDesFile.delete();
                                }
                                boolean rename = desFileTemp.renameTo(mDesFile);
                                Log.d(getClass().getSimpleName(), "renameTo ret:" + rename + " desfile:" + mDesFile.getAbsolutePath());
                                return rename;
                            } else {
                                Log.e(getClass().getSimpleName(), "downloaded != target");
                                return false;
                            }
                        }
//                        return downloaded == target;
                    } catch (IOException ignore) {
                        return false;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    }
                } else {
                    return false;
                }
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "IOException :" + e.toString());
                return false;
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Exception :" + e.toString());
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            if (mFileDownloadListener != null)
                mFileDownloadListener.onUpdateProgress(mDesFile, values[0], values[1]);
            Log.d(getClass().getSimpleName(), "download progress:" + String.format("%d / %d", values[0], values[1]));
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                Log.d(getClass().getSimpleName(), "File :" + mDesFile.getAbsolutePath() + " exist :" + mDesFile.exists() + "  result:" + result);
                if (result) {
                    mFileDownloadListener.onDownloadCompletion(mDesFile, mUrl, mTotalSize);
                } else {
                    mFileDownloadListener.onDownloadFail(mUrl);
                }
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "onPostExecute Exception :" + e.toString());
            }
        }
    }
}
