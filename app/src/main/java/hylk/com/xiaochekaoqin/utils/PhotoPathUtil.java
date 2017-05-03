package hylk.com.xiaochekaoqin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/6.
 */

public class PhotoPathUtil {

    /**
     * 从sd卡获取图片资源
     *
     * @return
     */
    public static ArrayList<String> getImagePathFromSD(Context context) {
        // 图片列表
        ArrayList<String> imagePathList = new ArrayList<String>();

        // 得到该路径文件夹下所有的文件
        File fileAll = new File(getPicturePath(context));
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
            }
        }
        // 返回得到的图片列表
        return imagePathList;
    }


    /**
     * 获取图片存储路径
     */
    public static String getPicturePath(Context context) {
        //下面获取sd卡的根目录，设置我们需要保存的路径...

        String filename = null;
        // 图片路径初始化
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {  // 有内存卡
            filename = Environment.getExternalStorageDirectory().getPath() + "/hylk_media/";
            Log.d("wzz------", "有内存卡");
        } else {  // 无内存卡

            filename = context.getFilesDir().getPath() + "/hylk_media/";
            Log.d("wzz------", "无内存卡");
        }


        File file = new File(filename);
        if (!file.exists()) {//如果父文件夹不存在则进行新建...
            file.mkdirs();
        }

        return filename;
    }



    /**
     * 检查扩展名，得到图片格式的文件
     *
     * @param fName 文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    private static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }


}
