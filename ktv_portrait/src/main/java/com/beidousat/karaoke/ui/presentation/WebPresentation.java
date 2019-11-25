package com.beidousat.karaoke.ui.presentation;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.web.PassWord;
import com.bestarmedia.libcommon.util.Logger;
import com.tamic.jswebview.browse.JsWeb.CustomWebViewClient;
import com.tamic.jswebview.view.ProgressBarWebView;

import java.util.Map;

public class WebPresentation extends Presentation {
    private final static String TAG = "WebPresentation";
    private Display mHdmiDisplay = null;
    private ProgressBarWebView bridgeWebView;
    public WebPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        Logger.d(TAG,display.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_webview);
        bridgeWebView = findViewById(R.id.webview);
        findViewById(R.id.riv_close).setVisibility(View.GONE);
        bridgeWebView.setWebViewClient(new CustomWebViewClient(bridgeWebView.getWebView()) {


            @Override
            public String onPageError(String url) {
                //指定网络加载失败时的错误页面
                return "http://192.168.1.245:8086/lottery/";
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.loadUrl("http://192.168.1.245:8086/lottery/");
            }

            @Override
            public Map<String, String> onPageHeaders(String url) {

                // 可以加入header

                return null;
            }

        });
//        bridgeWebView.setWebChromeClient(new CustomWebChromeClient(bridgeWebView.getProgressBar()){});
//        mMethods.add(QUIT);
//        mMethods.add(SCREEN);
//        mMethods.add(PASSWORD);
//        //回调js的方法
//        bridgeWebView.registerHandlers(mMethods, (handlerName, responseData, function) -> {
//            switch (handlerName) {
//                case QUIT:
//                    Logger.d(TAG, "quit");
//                    dismiss();
//                    break;
//                case SCREEN:
//                    Logger.d(TAG, "screen:"+responseData);
//                    if(responseData.equalsIgnoreCase("1")){
//                        EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
//                    }else if(responseData.equalsIgnoreCase("0")){
//                        EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_RESUME, null);
//                    }
//                    break;
//                case PASSWORD:
//                    PassWord passWord = new PassWord();
//                    passWord.password = PrefData.getMngPassword(mContext);
//                    BaseModelV4 baseModelV4 = new BaseModelV4();
//                    baseModelV4.data = passWord.toJson();
//                    Logger.d(TAG, "password:" + baseModelV4.toString());
//                    function.onCallBack(baseModelV4.toString());
//                    break;
//            }
//        });
        bridgeWebView.loadUrl("http://192.168.1.245:8086/lottery/");
    }
}
