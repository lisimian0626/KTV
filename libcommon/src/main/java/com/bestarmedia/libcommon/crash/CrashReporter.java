package com.bestarmedia.libcommon.crash;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.NodeCrash;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libcommon.util.FileUtil;
import com.bestarmedia.libcommon.util.KaraokeSdHelper;
import com.bestarmedia.libcommon.util.NetWorkUtils;
import com.bestarmedia.libcommon.util.PackageUtil;
import com.google.gson.Gson;

import java.io.File;

/**
 * Created by J Wong on 2016/11/21.
 */

public class CrashReporter implements HttpRequestListener {


    private File mReportingFile;
    private Context mContext;

    public CrashReporter(Context context) {
        this.mContext = context;
    }

    public void reportCrash() {
        File crashDir = KaraokeSdHelper.getCrash();
        File[] files;
        if (crashDir != null && crashDir.exists() && (files = crashDir.listFiles()) != null && files.length > 0) {
            for (File file : files) {
                if (file != null && file.exists() && file.length() > 0 && file.getName().endsWith(".log")) {
                    reportFile(file);
                    break;
                }
            }
        } else {
            Log.d("CrashReporter", "no log to report ");
        }
    }

    private void reportFile(File file) {
        mReportingFile = file;
        String fileContent = FileUtil.readFileContent(mReportingFile.getAbsolutePath());
        if (!TextUtils.isEmpty(fileContent)) {
            reportCrash(fileContent);
        } else {
            mReportingFile.delete();
            reportCrash();
        }
    }

    public void reportCrash(String log) {
        try {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_CRASH);
            NodeCrash nodeCrash = new NodeCrash(NetWorkUtils.getLocalHostIp(), VodConfigData.getInstance().getRoomCode(), NodeRoomInfo.getInstance().getRoomName(),
                    DeviceUtil.getCupSerial(), DeviceUtil.getMacAddress(), OkConfig.boxManufacturerName(), PackageUtil.getApplicationName(mContext), PackageUtil.getVersionCode(mContext),
                    PackageUtil.getVersionName(mContext), PackageUtil.getSystemVersionCode(), log);
            Gson gson = new Gson();
            String json = gson.toJson(nodeCrash);
            r.postJson(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext.getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onFailed(String method, Object obj) {
    }

    @Override
    public void onError(String method, String error) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_CRASH.equals(method)) {
            if (mReportingFile != null && mReportingFile.exists()) {
                boolean ret = mReportingFile.delete();
                Log.d("CrashReporter", "report log delete log file : " + ret);
            }
            reportCrash();
        }
    }

    @Override
    public void onStart(String method) {
    }
}
