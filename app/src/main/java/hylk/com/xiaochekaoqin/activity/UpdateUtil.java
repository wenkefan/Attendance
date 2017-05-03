package hylk.com.xiaochekaoqin.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import hylk.com.xiaochekaoqin.bean.CardMessage;
import hylk.com.xiaochekaoqin.bean.ClassMessage;
import hylk.com.xiaochekaoqin.bean.HoliDayMessage;
import hylk.com.xiaochekaoqin.bean.ParentsMessage;
import hylk.com.xiaochekaoqin.bean.UserMessage;
import hylk.com.xiaochekaoqin.bean.YEYMessage;
import hylk.com.xiaochekaoqin.dao.CardDao;
import hylk.com.xiaochekaoqin.dao.ClassDao;
import hylk.com.xiaochekaoqin.dao.HoliDayDao;
import hylk.com.xiaochekaoqin.dao.ParentsDao;
import hylk.com.xiaochekaoqin.dao.UserDao;
import hylk.com.xiaochekaoqin.global.Constants;
import hylk.com.xiaochekaoqin.global.UrlConstants;
import hylk.com.xiaochekaoqin.utils.OkHttpUtil;
import hylk.com.xiaochekaoqin.utils.PrefUtils;
import hylk.com.xiaochekaoqin.utils.TimeUtil;
import hylk.com.xiaochekaoqin.utils.ToastUtil;
import hylk.com.xiaochekaoqin.utils.UIUtils;

/**
 * Created by Administrator on 2016/8/30.
 */
public class UpdateUtil {


    private static OkHttpUtil okHttpUtil;

    private final int REQUEST_YEY = 2;
    private final int REQUEST_CLASS = 3;
    private final int REQUEST_CARD = 4;
    private final int REQUEST_USER = 5;
    private final int REQUEST_HOLIDAYS = 6;
    private final int REQUEST_PARENTS = 7;





    private static final int LOGIN_SUCCESS = 0; // 登录成功
    private static final int LOGIN_ERROR = 1; // 登录失败
    private static final int LOGIN_NET_ERROR = 2; // 登录时网络出现问题
    private static final int INSERT_USER_COMPLETE = 3; // 插入用户表完成

    private static final int CLASS_TABLE = 4; // 插入用户表完成
    private static final int CLASSTYPE_TABLE = 5; // 插入用户表完成
    private static final int CARD_TABLE = 6; // 插入用户表完成
    private static final int USER_TABLE = 7; // 插入用户表完成
    private static final int HOLIDAY_TABLE = 8; // 插入节假日表完成
    private static final int PARENT_TABLE = 9; // 插入完成



    private static Gson gson;
    private static int kgId;  // 园所id
    private Activity activity;


    public static UpdateUtil instance;
    private ProgressDialog mMypDialog;
//    private int teacherId;
    private boolean mIsFirst;
    private String mLastTime;


    public static UpdateUtil getInstance(){

        if (instance == null) {
            instance = new UpdateUtil();
        }

        return instance;

    }



    public void init(final Activity activity,boolean isTrue) {

        this.activity = activity ;

        showProgress();  // 显示进度条

        gson = new Gson();
        mLastTime = PrefUtils.getString(activity, Constants.LAST_TIME,"1999-01-01 00:00:00");
        Log.d("wzz----上次接口时间--",mLastTime);

        /** 是否第一次登陆应用 */
        mIsFirst = PrefUtils.getBoolean(UIUtils.getContext(), Constants.FIRST_LOGIN, true);

        if (isTrue){
            mLastTime = "1999-01-01 00:00:00";
            mIsFirst = true ;
        }

        // 园所Id
        kgId = PrefUtils.getInt(UIUtils.getContext(), Constants.YEYID, 33);
        Log.d("wzz------","kgId ==== " + kgId) ;

//        teacherId = PrefUtils.getInt(UIUtils.getContext(), Constants.TEACHER_ID,0);


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

                    case REQUEST_YEY:

                        YEYMessage yeyMessage = gson.fromJson(json, YEYMessage.class);
                        PrefUtils.putString(activity,Constants.YEYNAME,yeyMessage.KgName);

                        initClassTable();

                        break;

                    case REQUEST_CLASS:
                        createClassTable(json);
                        break;
                    case REQUEST_CARD:
                        createCardTable(json);
                        break;
                    case REQUEST_USER:
                        createUserTable(json);
                        break;
                    case REQUEST_HOLIDAYS:
                        createHoliDaysTable(json);
                        break;

                    case REQUEST_PARENTS:
                        createParentsTable(json);
                        break;
                }

            }
        });


    }




    // 初始化园所信息
    /** 班级 -- 卡 -- 用户 */
    public void upDateInfo() {

        initYEY();

    }

    private void initYEY() {

        String url_YEY = String.format(UrlConstants.GetKindergarten, kgId);
        okHttpUtil.getRequestJson(url_YEY, REQUEST_YEY);

        Log.d("url_YEY-----",url_YEY);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case LOGIN_NET_ERROR:

                    mMypDialog.cancel(); //取消对话框

                    ToastUtil.show(activity, "网络出现问题,请稍候重试 !");
                    listener.fail();  // 失败

                    break;

                /** 考勤卡表 */
                case CARD_TABLE:
                    initCardTable();
                    break;

                /** 考勤用户表 */
                case USER_TABLE:
                    initUserTable();
                    break;

                case HOLIDAY_TABLE:
                    initHoloDayTable();
                    break;

                case PARENT_TABLE:
                    initParentsTable();
                    break;

                case INSERT_USER_COMPLETE:  // 插入用户表完成

                    mMypDialog.cancel(); //取消对话框
                    ToastUtil.show(activity, "初始化数据成功 !");
                    Log.d("wzz-----","对话框消失");

                    // 完成之后，表示不是第一次登陆了
                    PrefUtils.putBoolean(activity,Constants.FIRST_LOGIN,false);
                    PrefUtils.putString(activity,Constants.LAST_TIME, TimeUtil.getCurrentTime());


                    /** 设置监听，初始化完成后再在MainActivity中初始化17UC读卡器 */
                    listener.complete();


                    break;

            }

        }


    };



    private OnCompleteListener listener;
    public interface OnCompleteListener{
        void complete();
        void fail();
    }
    public void setOnCompleteListener(OnCompleteListener listener){
        this.listener = listener ;
    }




    /** 班级表接口 */
    private void initClassTable(){

        String url_class = String.format(UrlConstants.GetClassListInfo, kgId); // 班级表接口
        okHttpUtil.getRequestJson(url_class, REQUEST_CLASS);

        Log.d("url_class-----",url_class);
    }

    private void initCardTable() {

        String url_card = String.format(UrlConstants.GetAttendanceCard, kgId,mLastTime);
        okHttpUtil.getRequestJson(url_card, REQUEST_CARD);

        Log.d("url_card-----",url_card);
    }

    private void initUserTable(){

        String url_user = String.format(UrlConstants.GetAttendanceUser, kgId,mLastTime); // 考勤用户接口
        okHttpUtil.getRequestJson(url_user, REQUEST_USER);

        Log.d("url_user-----",url_user);
    }

    private void initHoloDayTable() {

        String url_holidays = String.format(UrlConstants.GetHolidays, kgId); // 考勤用户接口
        okHttpUtil.getRequestJson(url_holidays, REQUEST_HOLIDAYS);

        Log.d("url_holidays-----",url_holidays);
    }

    /** 监护人表 */
    private void initParentsTable() {

        String url_parents = String.format(UrlConstants.GetGuardianListInfo, kgId); // 考勤用户接口
        okHttpUtil.getRequestJson(url_parents, REQUEST_PARENTS);

        Log.d("url_parents-----",url_parents);
    }



    private void createClassTable(String json) {

        // 解析json并添加数据到数据库sqlite

        List<ClassMessage> list = gson.fromJson(json, new TypeToken<List<ClassMessage>>(){}.getType());

        if (list != null){

            System.out.println("班级接口json数据集合size大小-----"+list.size());
            ClassDao classDao = new ClassDao(activity);
            classDao.insert(list);
            classDao.close();

        }

        handler.sendEmptyMessage(CARD_TABLE);

    }


    private void createCardTable(String json) {

        final List<CardMessage> list = gson.fromJson(json, new TypeToken<List<CardMessage>>(){}.getType());

        if (list != null){

            System.out.println("考勤卡接口json数据集合size大小-----"+list.size());
            CardDao cardDao = new CardDao(activity);
            cardDao.insert(list,mIsFirst );
            cardDao.close();
        }

        handler.sendEmptyMessage(USER_TABLE);

    }

    private void createUserTable(String json) {

        final List<UserMessage> list = gson.fromJson(json, new TypeToken<List<UserMessage>>(){}.getType());

        if (list != null){

            System.out.println("考勤用户接口json数据集合size大小-----"+list.size());
            UserDao userDao = new UserDao(activity);
            userDao.insert(list,mIsFirst);
            userDao.close();
        }

        handler.sendEmptyMessage(HOLIDAY_TABLE);

    }

    private void createHoliDaysTable(String json) {

        List<HoliDayMessage> list = gson.fromJson(json, new TypeToken<List<HoliDayMessage>>(){}.getType());

        if (list != null){
            System.out.println("假期表接口json数据集合size大小-----"+list.size());
            HoliDayDao dao = new HoliDayDao(activity);
            dao.insert(list);
            dao.close();
        }

        handler.sendEmptyMessage(PARENT_TABLE);

    }

    private void createParentsTable(String json) {

        List<ParentsMessage> list = gson.fromJson(json, new TypeToken<List<ParentsMessage>>(){}.getType());

        if (list != null){
            System.out.println("监护人表接口json数据集合size大小-----"+list.size());
            ParentsDao dao = new ParentsDao(activity);
            dao.insert(list);
            dao.close();
        }

        handler.sendEmptyMessage(INSERT_USER_COMPLETE);
    }




    private void showProgress() {
        //实例化
        mMypDialog = new ProgressDialog(activity);
        //设置进度条风格，风格为圆形，旋转的
        mMypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //设置ProgressDialog 标题
        mMypDialog.setTitle("请稍候");
        //设置ProgressDialog 提示信息
        mMypDialog.setMessage("正在初始化园所数据...");
        //设置ProgressDialog 标题图标
//        mMypDialog.setIcon(R.drawable.logo);

        //设置ProgressDialog 的进度条是否不明确
        mMypDialog.setIndeterminate(false);
        //设置ProgressDialog 是否可以按退回按键取消
//        mMypDialog.setCancelable(false);

        //让ProgressDialog显示
        mMypDialog.show();
    }




}
