package com.beidousat.karaoke.ui.dialog.setting;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libcommon.http.BaseHttpRequest;
import com.bestarmedia.libcommon.http.BaseHttpRequestListener;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.store.StoreBaseModel;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.google.gson.Gson;

/**
 * Created by J Wong on 2017/5/15.
 */

public class DlgDeviceStore extends BaseDialog {

    private TextView mTvMsg, mTvStatus;
    private ProgressBar mPgbLoading;
    private RecyclerImageView mRivStatus;
    private String mBoxCode;
    private final static String TAG = DlgDeviceStore.class.getSimpleName();

    public DlgDeviceStore(Activity activity, String boxCode) {
        super(activity, R.style.MyDialog);
        mBoxCode = boxCode;
        init();
    }

    public void init() {
        this.setContentView(R.layout.dlg_device_store);
        if (getWindow() != null) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = 450;
            lp.height = 450;
            lp.gravity = Gravity.CENTER;
            lp.dimAmount = 0.7f;
            getWindow().setAttributes(lp);
        }
        setCanceledOnTouchOutside(false);
        mTvMsg = findViewById(R.id.tv_msg);
        mTvStatus = findViewById(R.id.tv_status);
        mTvStatus.setGravity(Gravity.CENTER);
        mPgbLoading = findViewById(R.id.pgb_progress);
        mRivStatus = findViewById(R.id.iv_status);

    }

    @Override
    public void show() {
        super.show();
        post();
    }

    private void post() {
        mPgbLoading.setVisibility(View.VISIBLE);
        mTvMsg.setVisibility(View.VISIBLE);
        String url = "http://e.beidousat.com/index.php/" + RequestMethod.DEVICE_STORE;
        BaseHttpRequest baseHttpRequest = new BaseHttpRequest();
        baseHttpRequest.setBaseHttpRequestListener(new BaseHttpRequestListener() {
            @Override
            public void onRequestCompletion(String url, String body) {
                Logger.d(TAG, "onResponse body :" + body);
                StoreBaseModel baseModel = convert2BaseModel(body);
                if (baseModel != null) {
                    if ("0".equals(baseModel.error)) {
                        String data = "";
                        try {
                            data = baseModel.data.getAsString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showSuccess(data);
                    } else {
                        showFail(baseModel.message);
                    }
                } else {
                    showFail("入库接口出错了：" + body);
                }
            }

            @Override
            public void onRequestFail(String url, String err) {
                showFail(err);
            }

            @Override
            public void onRequestError(String url, String err) {

            }
        });
        baseHttpRequest.addParam("dev_sn", DeviceUtil.getCupSerial())
                .addParam("net_mac", DeviceUtil.getMacAddress()).addParam("dev_type", "box").addParam("nonce", mBoxCode);
        baseHttpRequest.post(url);
    }


    private void showFail(final String errMsg) {
        mPgbLoading.post(() -> {
            mTvStatus.setText(getContext().getString(R.string.enter_store_fail, errMsg));
            mRivStatus.setImageResource(R.drawable.dlg_fail);
            mPgbLoading.setVisibility(View.GONE);
            mTvMsg.setVisibility(View.GONE);
            mTvStatus.setVisibility(View.VISIBLE);
            mRivStatus.setVisibility(View.VISIBLE);
        });
        handler.postDelayed(this::dismiss, 3000);
    }

    private void showSuccess(final String message) {
        mPgbLoading.post(() -> {
            mPgbLoading.setVisibility(View.GONE);
            mTvMsg.setVisibility(View.GONE);
            mTvStatus.setText(getContext().getString(R.string.enter_store_success) + "\n" + message);
            mRivStatus.setImageResource(R.drawable.dlg_successful);
            mTvStatus.setVisibility(View.VISIBLE);
            mRivStatus.setVisibility(View.VISIBLE);
        });
        handler.postDelayed(this::dismiss, 3000);
    }

    private Handler handler = new Handler();

    private StoreBaseModel convert2BaseModel(String response) {
        StoreBaseModel baseModel = null;
        try {
            Gson gson = new Gson();
            baseModel = gson.fromJson(response, StoreBaseModel.class);
        } catch (Exception e) {
            Logger.e(TAG, "convert2BaseModel ex:" + e.toString());
        }
        return baseModel;
    }

}
