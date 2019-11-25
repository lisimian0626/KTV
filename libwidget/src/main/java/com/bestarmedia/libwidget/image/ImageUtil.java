package com.bestarmedia.libwidget.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by J Wong on 2015/12/21 17:44.
 */
public class ImageUtil {

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap doReflection(Bitmap originalImage) {
        // 原始图片和反射图片中间的间距
        final int reflectionGap = 8;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        // 反转
        Matrix matrix = new Matrix();
        // 第一个参数为1表示x方向上以原比例为准保持不变，正数表示方向不变。
        // 第二个参数为-1表示y方向上以原比例为准保持不变，负数表示方向取反。
        matrix.preScale(1, -1);
        // reflectionImage就是下面透明的那部分,可以设置它的高度为原始的3/4,这样效果会更好些
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, 0, width, height / 3, matrix, false);
        // 创建一个新的bitmap,高度为原来的两倍
        Bitmap bitmap4Reflection = Bitmap.createBitmap(width, (height + height / 3), Bitmap.Config.ARGB_8888);
        // 其宽*高 = width * (height + height * 3 / 4)
        Canvas canvasRef = new Canvas(bitmap4Reflection);
        // 先画原始的图片
        canvasRef.drawBitmap(originalImage, 0, 0, null);
        // 画间距
        Paint deafaultPaint = new Paint();
        // defaultPaint不能为null，否则会有空指针异常。
        canvasRef.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
        // 画被反转以后的图片
        canvasRef.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        // 创建一个渐变的蒙版放在下面被反转的图片上面
        Paint paint = new Paint();
//        LinearGradient shader = new LinearGradient(200, originalImage.getHeight(), 10, bitmap4Reflection.getHeight()
//                + reflectionGap, 0x80ffffff, 0x00ffffff, TileMode.CLAMP);
        LinearGradient shader = new LinearGradient(0,
                originalImage.getHeight(), 0, bitmap4Reflection.getHeight()
                + reflectionGap, 0x70ffffff, 0x00ffffff,
                Shader.TileMode.MIRROR);

        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // 将蒙板画上
        canvasRef.drawRect(0, height, width, bitmap4Reflection.getHeight() + reflectionGap, paint);
        // 调用ImageView中的setImageBitmap
//        this.setImageBitmap(bitmap4Reflection);

        return bitmap4Reflection;
    }

}
