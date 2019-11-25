//package com.beidousat.karaoke.ui.dialog;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Paint;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.beidousat.karaoke.R;
//import com.bestarmedia.libcommon.util.QrCodeUtil;
//
//import java.text.DecimalFormat;
//
//
//public class PayDialog extends BaseDialog {
//
//    private TextView mTvTitle, tvFee;
//    private ImageView imageView;
//    private String payUrl;
//    private double fee;
//
//    public PayDialog(Context context) {
//        super(context, R.style.MyDialog);
//        init();
//    }
//
//    void init() {
//        this.setContentView(R.layout.dlg_pay);
//        if (getWindow() == null)
//            return;
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.width = 600;
//        lp.height = 500;
//        lp.gravity = Gravity.CENTER;
//        lp.dimAmount = 0.7f;
//        getWindow().setAttributes(lp);
//        mTvTitle = findViewById(R.id.dlg_pay_tv_title);
//        imageView = findViewById(R.id.dlg_pay_iv_qrcode);
//        tvFee = findViewById(R.id.tv_fee);
//        TextView textView = findViewById(R.id.tv_refund_process);
//        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//        textView.getPaint().setAntiAlias(true);//抗锯齿
//    }
//
//    public void setTitle(int resId) {
//        mTvTitle.setText(resId);
//    }
//
//    public void setTitle(CharSequence title) {
//        mTvTitle.setText(title);
//    }
//
//    public void setQrCode(String url, double fee) {
//        if (!TextUtils.isEmpty(url)) {
//            if (!url.equals(payUrl)) {
//                Bitmap bitmap = QrCodeUtil.createQRCode(url);
//                if (bitmap != null) {
//                    imageView.setImageBitmap(bitmap);
//                    payUrl = url;
//                }
//            }
//        }
//        if (fee != this.fee || TextUtils.isEmpty(tvFee.getText())) {
//            DecimalFormat df = new DecimalFormat("#0.00");
//            tvFee.setText(df.format(fee));
//            this.fee = fee;
//        }
//    }
//}
