package com.beidousat.karaoke.ui.dialog.safety;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.Configuration;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.QrCodeUtil;

public class DlgStoreSafety extends BaseDialog {
    private ImageView iv_qrcode;

    public DlgStoreSafety(Context context) {
        super(context, R.style.MyDialog);
        init();
    }

    private void init() {
        this.setContentView(R.layout.dlg_store_safety);
        if (getWindow() == null)
            return;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        iv_qrcode = findViewById(R.id.iv_store_safety_qrcode);
        Configuration.MonitorConfig monitorConfig = VodConfigData.getInstance().getProfileDetailV4().nodeProfileDetail.configuration.monitorConfig;
        Bitmap bitmap = QrCodeUtil.createQRCode(monitorConfig == null ? "" : monitorConfig.frontend + RequestMethod.V4.STORE_QRCODE + VodConfigData.getInstance().getKtvNetCode());
        Logger.d(DlgStoreSafety.class.getSimpleName(), "qrcode:" + monitorConfig == null ? "" : monitorConfig.frontend + RequestMethod.V4.STORE_QRCODE + VodConfigData.getInstance().getKtvNetCode());
        if (bitmap != null) {
            iv_qrcode.setImageBitmap(bitmap);
        }
    }


}
