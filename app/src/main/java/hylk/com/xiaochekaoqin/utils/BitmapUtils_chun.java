package hylk.com.xiaochekaoqin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.lidroid.xutils.util.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jpm on 2016/7/12.
 */
public class BitmapUtils_chun {
    public static Bitmap getBitmapByPath(String path, int widthLimit) {
        if (TextUtils.isEmpty(path)) return null;
        Bitmap bmp = null;

        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            opts.inSampleSize = 1;
            BitmapFactory.decodeFile(path, opts);

            if (opts.outWidth <= 0 || opts.outHeight <= 0) {
                return null;
            }
            if (widthLimit > 0) {
                while (opts.outWidth > widthLimit) {
                    opts.inSampleSize *= 2;
                    BitmapFactory.decodeFile(path, opts);
                }
                if (opts.outWidth < widthLimit) {
                    opts.inSampleSize /= 2;
                }
            }
            opts.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(path, opts);
            if (widthLimit <= 0) {
                return bmp;
            }
            bmp = getBitmapWithLimit(bmp, widthLimit);
        } catch (Exception e) {
            LogUtils.d("YIBI----getBitmapByPath : " + e.getLocalizedMessage());
        }
        return bmp;
    }

    public static Bitmap getBitmapWithLimit(Bitmap bitmap, int widthLimit) {
        if (widthLimit <= 0) return bitmap;
        if (bitmap == null || bitmap.isRecycled()) return null;
        Bitmap bmp = null;

        try {
            int width = bitmap.getWidth();

//            if (width > widthLimit) {
                float ratio = widthLimit / (float) width;
                Matrix matrix = new Matrix();
                matrix.setScale(ratio, ratio);

                bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
//				if (bmp != null && (!bmp.isRecycled())) {
//					bitmap.recycle();
//				}
//            } else {
//                bmp = bitmap;
//            }
        } catch (Exception e) {
            LogUtils.d("YIBI----getBitmapWithLimit : " + e.getLocalizedMessage());
        }
        return bmp;
    }

    /**
     * 图片长宽压缩
     *
     * @param bgimage
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * Bitmap转换为文件
     *
     * @param bitmap
     * @param filename
     */
    public static void Bitmap2File(Bitmap bitmap, String filename) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
            fos.write(baos.toByteArray());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Bitmap转换为文件
     *
     * @param bitmap
     * @param filename
     */
    public static void Bitmap2File(Bitmap bitmap, File filename) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
            fos.write(baos.toByteArray());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 路径转bitmap
     * @param srcPath
     * @return
     */
    public static Bitmap path2Bitmap(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

    /**
     * bitmap压缩
     * @param image
     * @param  size 压缩到的大小
     * @return
     */
    public static Bitmap compressImage(Bitmap image,int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options=100;
        while ( baos.toByteArray().length / 1024>size && options > 0) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
