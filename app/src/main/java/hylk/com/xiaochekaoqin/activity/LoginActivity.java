package hylk.com.xiaochekaoqin.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import hylk.com.xiaochekaoqin.R;
import hylk.com.xiaochekaoqin.activity.jiqi.*;
import hylk.com.xiaochekaoqin.bean.LoginBean;
import hylk.com.xiaochekaoqin.global.Constants;
import hylk.com.xiaochekaoqin.global.UrlConstants;
import hylk.com.xiaochekaoqin.utils.LogUtil;
import hylk.com.xiaochekaoqin.utils.OkHttpUtil;
import hylk.com.xiaochekaoqin.utils.PrefUtils;
import hylk.com.xiaochekaoqin.utils.ToastUtil;

/**
 * Created by _wzz on 2016/5/11.
 */
public class LoginActivity extends Activity implements View.OnClickListener {


    private static final int REQUEST_KGID = 1;

    private static final int LOGIN_NET_ERROR = 2;
    private static final int LOGIN_SUCCESS = 3;
    private static final int LOGIN_ERROR = 4;

    private EditText mEtUserName, mEtPwd;

    private Button mBtLogin;
    private UpdateUtil updateUtil;
    private Gson mGson;

    /**
     * 保持屏幕不休眠
     */
    private boolean iswakeLock = true;// 是否常亮
    private PowerManager.WakeLock wakeLock;

    //    在onRusume方法中将获得到的锁使用acquire()方法来保持唤醒，在onPause方法中使用release()方法来释放掉该锁，利用Activity的生命周期来巧妙的使这两种方法成对的出现。
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, "DPA");

        if (iswakeLock) {
            wakeLock.acquire();
        }
        super.onResume();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (wakeLock != null) {
            wakeLock.release();
        }
//        android.os.Process.killProcess(android.os.Process.myPid()); //立即执行
    }

    // ------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉标题栏

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示...
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//防止休眠

        setContentView(R.layout.activity_login_school);

        initView();
    }

    private static OkHttpUtil okHttpUtil;

    private void initView() {

        mGson = new Gson();


//		ImageView back = (ImageView) findViewById(R.id.iv_back);
//		back.setOnClickListener(this);

        mEtUserName = (EditText) findViewById(R.id.et_user_name);
        mEtPwd = (EditText) findViewById(R.id.et_password);
        mBtLogin = (Button) findViewById(R.id.btn_login);

        mBtLogin.setOnClickListener(this);

        /** 初始化数据 */
        updateUtil = UpdateUtil.getInstance();

        updateUtil.setOnCompleteListener(new UpdateUtil.OnCompleteListener() {
            @Override
            public void complete() {

                LogUtil.d("从login初始化数据完成----");

                String YeyName = PrefUtils.getString(LoginActivity.this, Constants.YEYNAME, ""); // 数据初始完成后获取幼儿园名称
                LogUtil.d("幼儿园名称--: " + YeyName);

                boolean device =  PrefUtils.getBoolean(LoginActivity.this,Constants.Device,true);

                Intent intent;
                if (!device) {
                    intent = new Intent(LoginActivity.this, hylk.com.xiaochekaoqin.activity.jiqi.MainActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                }
                startActivity(intent);


                finish();

            }

            @Override
            public void fail() {
                LogUtil.d("login界面初始化数据时网络出现问题");
            }
        });


        //  ----------------------------------

        // 初始化 OKHttp
        okHttpUtil = OkHttpUtil.getInstance();

        okHttpUtil.setGETListener(new OkHttpUtil.OnGETListener() {
            @Override
            public void onFail() {

                handler.sendEmptyMessage(LOGIN_NET_ERROR);

                System.out.println("网络出现问题----");

            }

            @Override
            public <T> void onSuccess(String json, T cla, int tag) {

            }

            @Override
            public void onSuccess(String json, int tag) {

                switch (tag) {

                    case REQUEST_KGID:

                        parseUserJson(json);

                        break;


                }

            }
        });


    }


    /**
     * 班级表接口
     *
     * @param userName
     * @param pwd
     */
    private void requestKgId(String userName, String pwd) {

        String url_Kgid = String.format(UrlConstants.LOGIN, userName, pwd); // 班级表接口
        okHttpUtil.getRequestJson(url_Kgid, REQUEST_KGID);

        Log.d("url_kgId-----", url_Kgid);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_login:

                String userName = mEtUserName.getText().toString().trim();
                String pwd = mEtPwd.getText().toString().trim();


                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
                    ToastUtil.show(this, "用户名或密码不能为空");
                    return;
                }

                showProgress();  // 进度条

                requestKgId(userName, pwd);

                break;

//			case R.id.iv_back:
//
//				finish();
//
//				break;
        }

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case LOGIN_NET_ERROR:

                    mypDialog.cancel(); //取消对话框

                    ToastUtil.show(LoginActivity.this, "网络出现问题,请稍候重试 !");

                    break;

                case LOGIN_ERROR:

                    mypDialog.cancel(); //取消对话框

                    ToastUtil.show(LoginActivity.this, (String) msg.obj + "");


                    break;

                /** 考勤卡表 */
                case LOGIN_SUCCESS:

                    mypDialog.cancel(); //取消对话框

                    updateUtil.init(LoginActivity.this, false);
                    updateUtil.upDateInfo();

                    break;


            }

        }


    };


    private void parseUserJson(String json) {

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(json);

            if (jsonObject.getInt("Success") == 10000) {

                LoginBean bean = mGson.fromJson(json, LoginBean.class);

                LoginBean.RerurnValue value = bean.RerurnValue;

                PrefUtils.putInt(this, Constants.YEYID, value.KgId);

                handler.sendEmptyMessage(LOGIN_SUCCESS);

            } else {

                // 用户名或密码不正确
                String message = jsonObject.getString("Message");
                Message msg = new Message();
                msg.obj = message;
                msg.what = LOGIN_ERROR;
                handler.sendMessage(msg);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private ProgressDialog mypDialog;  // 进度提示，请稍候

    private void showProgress() {
        //实例化
        mypDialog = new ProgressDialog(LoginActivity.this);
        //设置进度条风格，风格为圆形，旋转的
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //设置ProgressDialog 标题
//		mypDialog.setTitle("请稍候");
        //设置ProgressDialog 提示信息
        mypDialog.setMessage("获取园所数据...");
        //设置ProgressDialog 标题图标
//		mypDialog.setIcon(R.drawable.logo);

        //设置ProgressDialog 的进度条是否不明确
        mypDialog.setIndeterminate(false);
        //设置ProgressDialog 是否可以按退回按键取消
        mypDialog.setCancelable(false);

        //让ProgressDialog显示
        mypDialog.show();
    }


}
