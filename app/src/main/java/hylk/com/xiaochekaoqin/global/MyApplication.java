package hylk.com.xiaochekaoqin.global;


import android.app.Application;
import android.content.Context;
import android.os.Handler;

//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import hylk.com.xiaochekaoqin.utils.YiChangUtlis;

public class MyApplication extends Application {

    public static Context context;
    private static Handler handler;

    public static Context getContext() {

        return context;
    }

    public static Handler getHandler() {

        return handler;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        // 程序一创建，就初始化如下变量
        context = getApplicationContext();
        handler = new Handler();

        // 初始化崩溃日志收集器
        YiChangUtlis mCustomCrash = YiChangUtlis.getInstance();
        mCustomCrash.setCustomCrashInfo(this);

        //创建默认的ImageLoader配置参数
//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
//                .createDefault(this);
//
//        ImageLoader.getInstance().init(configuration);


    }


    // - ---------------以下为完全退出程序的方法

    private static MyApplication instance;

    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return
     */
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }


}
