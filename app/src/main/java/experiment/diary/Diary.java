package experiment.diary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

public class Diary {
    private int month;
    private int day;
    private String title;
    private String content;
    private byte[] picture;

    Diary(int month, int day, String title, String content, byte[] picture) {
        this.month   = month;
        this.day     = day;
        this.title   = title;
        this.content = content;
        this.picture = picture;
    }

    public byte[] getPicture() {
        return picture;
    }
    public int getDay() {
        return day;
    }
    public int getMonth() {
        return month;
    }
    public String getContent() {
        return content;
    }
    public String getTitle() {
        return title;
    }

    public void setPicture(byte[] pic) {picture = pic;}
    public void setDay(int d) {day = d;}
    public void setMonth(int m) {month = m;}
    public void setTitle(String s) {title = s;}
    public void setContent(String s) {
        content = s;
    }

    // TODO: the method of picture changing
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable bitmapToDrawable(Bitmap bm) {
        return new BitmapDrawable(bm);
    }
}
