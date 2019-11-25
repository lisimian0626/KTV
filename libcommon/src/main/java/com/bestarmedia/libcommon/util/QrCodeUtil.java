package com.bestarmedia.libcommon.util;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

/**
 * Created by J Wong on 2015/11/18 15:12.
 */
public class QrCodeUtil {

    // logo图片的一半宽度
//    private static final int IMAGE_HALF_WIDTH = 25;
    private static final int QRCODE_WIDTH_HEIGHT = 160;
    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xffffffff;
//    private static final int QRCODE_HEIGHT = 80;

//    public static Bitmap createQrCode(Context context, String qrcodeContentStr) {
//        Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
//
//        try {
//            if (qrcodeContentStr.length() != 0) {
//                // 二维码内容转码,不然扫描出来的结果是乱码
//                qrcodeContentStr = new String(qrcodeContentStr.getBytes(), "ISO-8859-1");
////                mLogoBitmap = ((BitmapDrawable) context.getResources().getDrawable(
////                        R.mipmap.applogo)).getBitmap();
//                // 缩放Logo图片
//                Matrix m = new Matrix();
//                float sx = (float) 2 * IMAGE_HALF_WIDTH / mLogoBitmap.getWidth();
//                float sy = (float) 2 * IMAGE_HALF_WIDTH / mLogoBitmap.getHeight();
//                m.setScale(sx, sy);
//                mLogoBitmap = Bitmap.createBitmap(mLogoBitmap, 0, 0, mLogoBitmap.getWidth(),
//                        mLogoBitmap.getHeight(), m, false);
//
//                //显示生成的二维码
//                Bitmap qrcodeBitmap = createQRCode(qrcodeContentStr);
//                return qrcodeBitmap;
//
//            }
//        } catch (Exception e) {
//            Log.e("QrCodeUtil", "Exception:" + e.toString());
//        }
//
//        return null;
//    }
//
//
//    public static Bitmap createQRCode(String content) throws WriterException {
//
//        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
//        BitMatrix matrix = new MultiFormatWriter().encode(content,
//                BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT);
//        int width = matrix.getWidth();
//        int height = matrix.getHeight();
//
//        // 二维矩阵转为一维像素数组
//        int halfW = width / 2;
//        int halfH = height / 2;
//        int[] pixels = new int[width * height];
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if (x > halfW - IMAGE_HALF_WIDTH && x < halfW + IMAGE_HALF_WIDTH && y > halfH - IMAGE_HALF_WIDTH
//                        && y < halfH + IMAGE_HALF_WIDTH) {
//                    pixels[y * width + x] = mLogoBitmap.getPixel(x - halfW + IMAGE_HALF_WIDTH, y
//                            - halfH + IMAGE_HALF_WIDTH);
//                } else {
//                    //此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
//                    int qrcodeColor = 0xff262f22;
//                    int qrcodeBgColor = 0xfff1f1f1;
//                    pixels[y * width + x] = matrix.get(x, y) ? qrcodeColor : qrcodeBgColor;
//                }
//
//            }
//        }
//        Bitmap bitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//
//        // 通过像素数组生成bitmap
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//
//        return bitmap;
//    }


    public static Bitmap createQRCode(String str) {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            str = new String(str.getBytes(), "ISO-8859-1");
            BitMatrix matrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, QRCODE_WIDTH_HEIGHT, QRCODE_WIDTH_HEIGHT);
            int margin = 5;  //自定义白边边框宽度
            matrix = updateBit(matrix, margin);  //生成新的bitMatrix
            int width = matrix.getWidth();
            int height = matrix.getHeight();


            int[] pixels = new int[width * height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixels[y * width + x] = matrix.get(x, y) ? BLACK : WHITE;
//                    if (matrix.get(x, y)) {
////                        pixels[y * width + x] = BLACK;
//                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) {   //循环，将二维码图案绘制到新的bitMatrix中
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }
}
