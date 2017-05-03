package hylk.com.xiaochekaoqin.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class YiChangUtlis implements Thread.UncaughtExceptionHandler {

    /**
     * 当前类注释:客户端运行 异常崩溃数据扑捉异常保存SD卡或者实时投递服务器工具类 项目名：FastDev4Android
     * 包名：com.chinaztt.fda.crash 作者：江清清 on 15/10/26 13:29 邮箱：jiangqqlmj@163.com
     * QQ： 781931404 公司：江苏中天科技软件技术有限公司
     */

    private static final String TAG = "CustomCrash";
    private static final int TYPE_SAVE_SDCARD = 1; // 崩溃日志保存本地SDCard --建议开发模式使用
    private static final int TYPE_SAVE_REMOTE = 2; // 崩溃日志保存远端服务器 --建议生产模式使用

    private int type_save = 1; // 崩溃保存日志模式 默认为1，采用保存Web服务器

    private static  String CRASH_SAVE_SDPATH = Environment
            .getExternalStorageDirectory() + "/hylk/LOG/"; // 崩溃日志SD卡保存路径

    private static final String CARSH_LOG_DELIVER = "http://img2.xxh.cc:8080/SalesWebTest/CrashDeliver";
    private static YiChangUtlis instance = new YiChangUtlis();
    private static Context mContext;

    private YiChangUtlis() {
    }

    /** 获取图片存储路径 */
    public static String getPicturePath(){
        //下面获取sd卡的根目录，设置我们需要保存的路径...
//        String filename = Environment.getExternalStorageState().toString() + File.separator + "CameraPhoto" + File.separator + "picture" + ".jpg";

        String filename = null ;
        // 图片路径初始化
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {  // 有内存卡
            filename = Environment.getExternalStorageDirectory().getPath() + "/hylk/attendance/LOG/";
            Log.d("wzz------", "有内存卡");
        } else {  // 无内存卡

            filename = mContext.getFilesDir().getPath() + "/hylk/attendance/LOG/";
            Log.d("wzz------", "无内存卡");
        }


        File file = new File(filename);
        if (!file.exists()) {//如果父文件夹不存在则进行新建...
            file.mkdirs();
        }

        return filename;
    }

    /**
     * @return
     */
    public static YiChangUtlis getInstance() {

        CRASH_SAVE_SDPATH = getPicturePath();

        return instance;
    }

    /*
     * (non-Javadoc) 进行重写捕捉异常
     * 
     * @see
     * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang
     * .Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {

        showToast(mContext, "很抱歉！程序发生异常即将退出");

        if (type_save == TYPE_SAVE_SDCARD) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 1,保存信息到sdcard中
                    saveToSdcard(mContext, ex);
                }
            }).start();

        } else if (type_save == TYPE_SAVE_REMOTE) {
            // 2,异常崩溃信息投递到服务器
            saveToServer(mContext, ex);
        }
        // saveToServer(mContext, ex);
        // 3,应用准备退出

//        try {
//            Thread.sleep(3500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // XiaocheMainActivity.getInstance().finishAffinity();
//        System.out.println(getExceptionInfo(ex));
        Log.d("ANXU", getExceptionInfo(ex));
        // android.os.Process.killProcess(android.os.Process.myPid());

    }

    /**
     * 设置自定异常处理类
     *
     * @param pContext
     */
    public void setCustomCrashInfo(Context pContext) {
        this.mContext = pContext;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 保存异常信息到sdcard中
     *
     * @param pContext
     * @param ex       异常信息对象
     */
    private void saveToSdcard(Context pContext, Throwable ex) {
        String fileName = null;
        StringBuffer sBuffer = new StringBuffer();
        // 添加异常信息
        sBuffer.append(getExceptionInfo(ex));

        File file1 = new File(CRASH_SAVE_SDPATH);
        if (!file1.exists()) {
            file1.mkdir();
        }
        fileName = file1.toString() + File.separator
                + paserTime(System.currentTimeMillis()) + ".log";
        File file2 = new File(fileName);
        FileOutputStream fos;
        try {
            BufferedOutputStream br = new BufferedOutputStream(new FileOutputStream(file2, true));
            br.write(sBuffer.toString().getBytes());
            br.flush();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);

    }

    /**
     * 进行把数据投递至服务器
     *
     * @param pContext
     * @param ex       崩溃异常
     */
    private void saveToServer(final Context pContext, Throwable ex) {
        final String carsh_log = getExceptionInfo(ex);
        Log.d("ANXU", carsh_log);
        // showToast(pContext, "很抱歉！程序出错！");
        // HttpUtils httpUtils = new HttpUtils();
        // RequestParams params = new RequestParams();
        // params.addBodyParameter("child", carsh_log);
        // httpUtils.configCurrentHttpCacheExpiry(0);
        // httpUtils.send(HttpMethod.POST, Connector.TEACHERMONTHATTENDANCES,
        // params, new RequestCallBack<String>() {
        //
        // @Override
        // public void onFailure(HttpException arg0, String arg1) {
        // showToast(pContext, arg1);
        // }
        //
        // @Override
        // public void onSuccess(ResponseInfo<String> arg0) {
        // System.out.println(arg0.result);
        //
        // }
        //
        // });
    }

    /**
     * 获取并且转化异常信息 同时可以进行投递相关的设备，用户信息
     *
     * @param ex
     * @return 异常信息的字符串形式
     */
    private String getExceptionInfo(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("---------Crash Log Begin " + TimeUtil.getCurrentTime() + "---------\n");
        // 在这边可以进行相关设备信息投递--这边就稍微设置几个吧
        // 其他设备和用户信息大家可以自己去扩展收集上传投递
        stringBuffer.append("SystemVersion:" + android.os.Build.VERSION.RELEASE
                + "\n");
        stringBuffer.append(sw.toString() + "\n");
        stringBuffer.append("---------Crash Log End---------\n");
        return stringBuffer.toString();
    }

    /**
     * 进行弹出框提示
     *
     * @param pContext
     * @param msg
     */
    private void showToast(final Context pContext, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(pContext, msg, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式
     *
     * @param milliseconds
     * @return
     */
    private String paserTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        // SimpleDateFormat format = new
        // SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String times = format.format(new Date(milliseconds));

        return times;
    }
}
