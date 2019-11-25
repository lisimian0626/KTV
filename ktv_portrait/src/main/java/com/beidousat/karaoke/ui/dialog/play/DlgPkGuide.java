package com.beidousat.karaoke.ui.dialog.play;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.bestarmedia.libcommon.helper.QrCodeRequest;
import com.bestarmedia.libcommon.model.vod.RoomQrCodeSimple;


public class DlgPkGuide extends BaseDialog {


    private ImageView mIvQrCode;

    public DlgPkGuide(Activity context) {
        super(context, R.style.MyDialog);
        init();
    }

    private void init() {

        this.setContentView(R.layout.dlg_game_pk_guide);
        if (getWindow() == null)
            return;
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 900;
        lp.height = 400;
        lp.verticalMargin = 0.18f;
        lp.horizontalMargin = 0.02f;
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        View mRootView = findViewById(R.id.rootView);
        mIvQrCode = findViewById(R.id.riv_qrcode);

        QrCodeRequest request = new QrCodeRequest(getContext(), new QrCodeRequest.QrCodeRequestListener() {
            @Override
            public void onQrCode(RoomQrCodeSimple code) {
                if (code != null) {
//                    Bitmap bitmap = QrCodeUtil.createQRCode(code.toString());
//                    if (bitmap != null)
//                        mIvQrCode.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onQrCodeFail(String error) {
                mIvQrCode.setImageResource(R.drawable.ic_qrcode_disable);
            }
        });
        request.requestCode();

    }

}
