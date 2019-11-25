package com.beidousat.karaoke.player.proxy;


import com.beidousat.karaoke.player.proxy.IDownState.DownState;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载模块，支持断点下载
 *
 * @author hellogv
 */
public class DownloadThread extends Thread {
    static private final String TAG = "DownloadThread";
    private String mUrl;
    private String mPath;
    private int mDownloadSize;
    private int mTargetSize;
    private boolean mStop;
    private boolean mDownloading;
    private boolean mStarted;
    private boolean mError;
    private final static int BUF_LEN = 1024 * 512;
    private IDownState state;

    public DownloadThread() {
    }

    public DownloadThread(String url, String savePath, IDownState state, int tsize) {
        mUrl = url;
        mPath = savePath;
        this.state = state;

        mDownloadSize = 0;

        mTargetSize = tsize;
        mStop = false;
        mDownloading = false;
        mStarted = false;
        mError = false;
    }

    @Override
    public void run() {
        mDownloading = true;
        download();
    }

    /**
     * 启动下载线程
     */
    public void startThread() {
        if (!mStarted) {
            this.start();

            // 只能启动一次
            mStarted = true;
        }
    }

    /**
     * 停止下载线程
     */
    public void stopThread() {
        mStop = true;
    }

    /**
     * 是否正在下载
     */
    public boolean isDownloading() {
        return mDownloading;
    }

    /**
     * 是否下载异常
     *
     * @return
     */
    public boolean isError() {
        return mError;
    }

    public long getDownloadedSize() {
        return mDownloadSize;
    }

    /**
     * 是否下载成功
     */
    public boolean isDownloadSuccessed() {
        return (mDownloadSize != 0 && mDownloadSize >= mTargetSize);
    }

    public boolean checkStop() {
        return mStop;
    }

    private void download() {
        //下载成功则关闭
//		if(isDownloadSuccessed()){
//			Log.i(TAG,"...DownloadSuccessed...");
//			return;
//		}
        InputStream is = null;
//		FileOutputStream os = null;
//		RandomAccessFile os = null;
        if (mStop) {
            return;
        }
        try {
            System.out.println("url " + mUrl);
            URL url = new URL(mUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(50000);
            urlConnection.setInstanceFollowRedirects(true);//允许重定向

            System.out.println("code " + urlConnection.getResponseCode() + " " + mUrl);

            if (urlConnection.getResponseCode() == 404) {
                state.onDown(mUrl, 0, DownState.ERROREXIT, null, 0);
                mDownloading = false;
                mStop = true;
                return;
            }
            is = urlConnection.getInputStream();
//			if(mDownloadSize==0){//全新文件
//				os = new FileOutputStream(mPath);
//			os =  new RandomAccessFile(mPath, "rw");
//				Log.i(TAG,"download file:"+mPath);
//			}
//			else{//追加数据
//				os = new FileOutputStream(mPath,true);
//				Log.i(TAG,"append exists file:"+mPath);
//			}


            if (mStop) {
                return;
            }
//			System.out.println("ContentLength "+urlConnection.getContentLength());
            int clen = urlConnection.getContentLength();
            int len = 0;
            byte[] bs = new byte[BUF_LEN];
            byte[] hbuf = new byte[Config.ENCODE_HEAD_LEN * 4];
            int hlen = 0;
            while (!mStop) {
                len = is.read(bs, 0, Config.ENCODE_HEAD_LEN * 2);
                if (len > 0) {
                    System.arraycopy(bs, 0, hbuf, hlen, len);
                    hlen += len;
                }
                if (hlen >= Config.ENCODE_HEAD_LEN)
                    break;
            }
            if (hlen != -1) {
                //if(mUrl.toLowerCase().endsWith(Config.ENCODESF))
                if (Decoder.getinstance().checkEncryt(bs))
                    state.onInit(mUrl, clen - 8, true);
                else
                    state.onInit(mUrl, clen, false);
                byte[] dec = Decoder.getinstance().decoder(bs, len);
//				os.write(dec, 0, dec.length);
                mDownloadSize += dec.length;
//				System.out.println(len+" =dec= "+dec.length);
                state.onDown(mUrl, mDownloadSize, DownState.DOWNING, dec, dec.length);
            }
//			while (!mStop //未强制停止	
//					&&(mDownloadSize < mTargetSize)
//					&& ((len = is.read(bs,0,BUF_LEN)) != -1)) {//未全部读取
//				if (mStop) {
//					break;
//				}
////				os.write(bs, 0, len);
//				mDownloadSize += len;
//				state.onDown(mUrl, mDownloadSize, DownState.DOWNING, bs, len);
////				os.flush();
//			}

            state.onDown(mUrl, (int) mDownloadSize, DownState.SUCCESS, null, 0);
//			System.out.println("down end ");
        } catch (Exception e) {
            System.out.println("down ========errot============+++= " + e.getMessage());
            state.onDown(mUrl, (int) mDownloadSize, DownState.ERROR, null, 0);
            mError = true;
            e.printStackTrace();
//			Log.i(TAG,Utils.getExceptionMessage(e));
        } finally {
            state = null;
//			if (os != null) {
//				try {
//					os.close();
//				} catch (IOException e) {}
//			}
            mDownloading = false;

            mStop = true;
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

//			Log.i(TAG,"mDownloadSize:"+mDownloadSize+",mTargetSize:"+mTargetSize);
        }
    }
}
