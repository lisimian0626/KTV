package com.bestarmedia.libcommon.helper;

import android.content.Context;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.TopicRequestBody;

/**
 * Created by J Wong on 2015/12/27 09:24.
 */
public class StatisticsHelper implements HttpRequestListener {

    private Context mContext;

    private static StatisticsHelper mStatisticsHelper;

    public static StatisticsHelper getInstance(Context context) {
        if (mStatisticsHelper == null) {
            mStatisticsHelper = new StatisticsHelper(context);
        }
        return mStatisticsHelper;
    }

    private StatisticsHelper(Context context) {
        this.mContext = context.getApplicationContext();
    }


    public void recordTopicClick(TopicRequestBody body) {
        if (NodeRoomInfo.getInstance().getNodeRoom() != null) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_STATISTICS_TOPIC);
            r.postJson(body.toString());
        }
    }


    private HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext.getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {

    }

    @Override
    public void onFailed(String method, Object obj) {

    }

    @Override
    public void onError(String method, String error) {

    }
}
