package com.beidousat.karaoke.ui.dialog.web;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.net.http.SslError;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.beidousat.karaoke.ui.presentation.WebPresentation;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.web.PassWord;
import com.bestarmedia.libcommon.util.Logger;
import com.tamic.jswebview.browse.JsWeb.CustomWebViewClient;
import com.tamic.jswebview.view.ProgressBarWebView;

import java.util.ArrayList;
import java.util.Map;


public class DlgWebView extends BaseDialog implements OnClickListener {

    private final String TAG = DlgWebView.class.getSimpleName();
    private final String QUIT = "quit";
    private final String SCREEN = "screen";
    private final String PASSWORD = "password";
    private ProgressBarWebView bridgeWebView;
    private String mUrl;
    private Context mContext;
    private ArrayList<String> mMethods = new ArrayList<>();
    private Display mHdmiDisplay = null;
    private WebPresentation mPresentation;

    public DlgWebView(Context context, String url) {
        super(context, R.style.MyDialog);
        mUrl = url;
        this.mContext = context;
        init();
    }

    void init() {
        this.setContentView(R.layout.dlg_webview);
        LayoutParams lp = getWindow().getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        lp.type = LayoutParams.FIRST_SUB_WINDOW;
        getWindow().setAttributes(lp);
        findViewById(R.id.riv_close).setOnClickListener(this);
        bridgeWebView = findViewById(R.id.webview);
        bridgeWebView.setWebViewClient(new CustomWebViewClient(bridgeWebView.getWebView()) {


            @Override
            public String onPageError(String url) {
                //指定网络加载失败时的错误页面
                return mUrl;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.loadUrl(mUrl);
            }

            @Override
            public Map<String, String> onPageHeaders(String url) {

                // 可以加入header

                return null;
            }

        });
//        bridgeWebView.setWebChromeClient(new CustomWebChromeClient(bridgeWebView.getProgressBar()){});
        mMethods.add(QUIT);
        mMethods.add(SCREEN);
        mMethods.add(PASSWORD);
        //回调js的方法
        bridgeWebView.registerHandlers(mMethods, (handlerName, responseData, function) -> {
            switch (handlerName) {
                case QUIT:
                    Logger.d(TAG, "quit");
                    dismiss();
                    break;
                case SCREEN:
                    Logger.d(TAG, "screen:" + responseData);
                    if(responseData.equalsIgnoreCase("1")){
                        startSreen();
                    }else if(responseData.equalsIgnoreCase("0")){
                        closeSreen();
                    }
                    break;
                case PASSWORD:
                    PassWord passWord = new PassWord();
                    passWord.password = PrefData.getMngPassword(mContext);
                    BaseModelV4 baseModelV4 = new BaseModelV4();
                    baseModelV4.data = passWord.toJson();
                    Logger.d(TAG, "password:" + baseModelV4.toString());
                    function.onCallBack(baseModelV4.toString());
                    break;
            }
        });
        bridgeWebView.loadUrl(mUrl);
    }

    @Override
    public void show() {
        super.show();

    }

    @Override
    public void dismiss() {
        Logger.d(TAG, "dismiss");
        bridgeWebView.getWebView().destroy();
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_close:
                dismiss();
                break;
        }
    }


    private void initHdmiDisplay() {
        if (mHdmiDisplay == null && getContext() != null) {
            DisplayManager mDisplayManager = (DisplayManager) getContext().getSystemService(Context.DISPLAY_SERVICE);
            Display[] displays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            if (displays != null) {
                for (int i = 0; i < displays.length; i++) {
                    final Display display = displays[i];
                    if (display != null && display.isValid() && display.getName().toLowerCase().contains("hdmi")) {
                        mHdmiDisplay = display;
                        break;
                    }
                }
            } else {
                Logger.d(TAG, " Display[] is null");
            }
        }
    }


    private void startSreen() {
        initHdmiDisplay();
        if (mHdmiDisplay != null) {
            EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_STOP, null);
            showPresentation(mHdmiDisplay);
        } else {
            Logger.d(TAG, " Display[] is null");
        }
    }

    private void showPresentation(final Display display) {
        final int displayId = display.getDisplayId();
        Logger.d(TAG, " Display display.getName():" + display.getName() + "  show");
        mPresentation = new WebPresentation(VodApplication.getVodApplicationContext(), display);
        if (mPresentation.getWindow() != null) {
            mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        mPresentation.show();
    }

    private void closeSreen() {
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
            EventBusUtil.postSticky(EventBusId.PresentationId.MAIN_PLAYER_RESUME, null);
        }
    }
}
