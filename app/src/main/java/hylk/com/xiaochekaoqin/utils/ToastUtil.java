package hylk.com.xiaochekaoqin.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lenovo on 2016/5/12.
 */
public class ToastUtil {
    public static void show(Context ctx, String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context ctx, String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
    }

}