//package com.beidousat.karaoke.ui.dialog.vod;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.View;
//import android.view.WindowManager.LayoutParams;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.beidousat.karaoke.R;
//import com.beidousat.karaoke.VodApplication;
//import com.beidousat.karaoke.ui.dialog.BaseDialog;
//import com.beidousat.karaoke.ui.dialog.PopAppDownload;
//import com.beidousat.karaoke.ui.dialog.PromptDialogBig;
//import com.bestarmedia.libcommon.data.NodeRoomInfo;
//import com.bestarmedia.libcommon.helper.QrCodeRequest;
//import com.bestarmedia.libcommon.model.vod.RoomQrCodeSimple;
//import com.bestarmedia.libcommon.util.DeviceHelper;
//import com.bestarmedia.libcommon.util.QrCodeUtil;
//
//public class RoomQrCodeDialog extends BaseDialog implements View.OnClickListener {
//
//    private Context mContext;
//    private View mRootView;
//    private ImageView mIvQrCode;
//    private ProgressBar mPgbQrCode;
//    private TextView tvQrMsg;
//
//    public RoomQrCodeDialog(Context context) {
//        super(context, R.style.MyDialog);
//        mContext = context;
//        init();
//    }
//
//    void init() {
//        this.setContentView(R.layout.dlg_room_qrcode);
//        LayoutParams lp = getWindow().getAttributes();
//        lp.width = 804;
//        lp.height = 430;
//        lp.dimAmount = 0.7f;
//        lp.verticalMargin = 0.19f;
//        lp.horizontalMargin = 0.02f;
//        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//
//        getWindow().setAttributes(lp);
//        setCanceledOnTouchOutside(true);
//        mRootView = findViewById(R.id.rootView);
//        mIvQrCode = findViewById(R.id.iv_qrcode);
//        mIvQrCode.setOnClickListener(this);
//        mPgbQrCode = findViewById(R.id.pgb_qrcode);
//        tvQrMsg = findViewById(R.id.tv_qr_msg);
//
//
//        findViewById(R.id.btn_disconnect).setOnClickListener(this);
//        findViewById(R.id.riv_close).setOnClickListener(this);
//        findViewById(R.id.riv_down_app).setOnClickListener(this);
//
//        requestQrCode();
//    }
//
//    private void requestQrCode() {
//        mPgbQrCode.setVisibility(View.VISIBLE);
//        mIvQrCode.setClickable(false);
//        mIvQrCode.setImageResource(0);
//        QrCodeRequest request = new QrCodeRequest(getContext(), new QrCodeRequest.QrCodeRequestListener() {
//            @Override
//            public void onQrCode(RoomQrCodeSimple code) {
//                mPgbQrCode.setVisibility(View.GONE);
//                tvQrMsg.setText("");
//                tvQrMsg.setVisibility(View.GONE);
//                if (code != null) {
//                    Bitmap bitmap = QrCodeUtil.createQRCode(code.toString());
//                    if (bitmap != null) {
//                        mIvQrCode.setImageBitmap(bitmap);
//                    }
//                }
//            }
//
//            @Override
//            public void onQrCodeFail(String error) {
//                mPgbQrCode.setVisibility(View.GONE);
//                if (TextUtils.isEmpty(NodeRoomInfo.getInstance().getRoomSession())) {
//                    tvQrMsg.setText(error);
//                    tvQrMsg.setVisibility(View.VISIBLE);
//                    mIvQrCode.setVisibility(View.GONE);
//                    mIvQrCode.setClickable(false);
//                } else {
//                    tvQrMsg.setVisibility(View.GONE);
//                    mIvQrCode.setVisibility(View.VISIBLE);
//                    mIvQrCode.setImageResource(R.drawable.ic_qrcode_disable);
//                    mIvQrCode.setClickable(true);
//                }
//            }
//        });
//        request.requestCode();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_disconnect:
//                PromptDialogBig promptDialog = new PromptDialogBig(mContext);
//                promptDialog.setMessage(R.string.disconnect_phone_tip);
//                promptDialog.setOkButton(true, mContext.getString(R.string.disconnect), v1 -> {
//                    VodApplication.getVodApplication().getKaraokeController().resetBoxUUID(true);
//                    mIvQrCode.setImageBitmap(null);
//                    if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
//                        requestQrCode();
//                    } else {
//                        mIvQrCode.postDelayed(() -> requestQrCode(), 5000);
//                    }
//                });
//                promptDialog.setCancleButton(true, mContext.getString(R.string.cancel), null);
//                promptDialog.show();
//                break;
//            case R.id.iv_qrcode:
//                requestQrCode();
//                break;
//            case R.id.riv_down_app:
//                PopAppDownload download = new PopAppDownload(mContext);
//                download.showAnchorTop(v);
//                break;
//            case R.id.riv_close:
//                dismiss();
//                break;
//        }
//    }
//}
