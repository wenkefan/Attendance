package hylk.com.xiaochekaoqin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hylk.com.xiaochekaoqin.R;
import hylk.com.xiaochekaoqin.baiduyuyin.Yuyin;
import hylk.com.xiaochekaoqin.bean.AttendanceRecord;
import hylk.com.xiaochekaoqin.bean.Child;
import hylk.com.xiaochekaoqin.bean.ClassMessage;
import hylk.com.xiaochekaoqin.bean.JiLuBean;
import hylk.com.xiaochekaoqin.bean.UserBean;
import hylk.com.xiaochekaoqin.dao.AttendanceDao;
import hylk.com.xiaochekaoqin.dao.ClassDao;
import hylk.com.xiaochekaoqin.global.Constants;
import hylk.com.xiaochekaoqin.global.ThreadManager;
import hylk.com.xiaochekaoqin.global.UrlConstants;
import hylk.com.xiaochekaoqin.upload.JKHttp;
import hylk.com.xiaochekaoqin.utils.LogUtil;
import hylk.com.xiaochekaoqin.utils.OkHttpUtil;
import hylk.com.xiaochekaoqin.utils.PrefUtils;
import hylk.com.xiaochekaoqin.utils.TimeUtil;
import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * 相比较1，加入了摄像头角度的旋转  Vitamio只支持ARMv6+以上的CPU
 */

// CardType: 1  幼儿 ；  CardType: 2, 陈菲比 老师  ；  CardType: 3,  家长卡   家长： 刘雷朋    幼儿：罗慧玲

public class MainActivity extends NFCBaseActivity {

    private static final int TAG_POST_ATTENDANCE = 0;
    private static final int TAG_POST_WEIXIN = 2;

    private static final int VALUE_TIMER = 11;
    private static final int VALUE_FENBANBOBAO = 12;

    private static final String Key_JiLu = "Key_JiLu";

    private String mUrl_WEIXIN;

    private boolean ifWeiXin, ifYuYin;  // 是否微信提醒
    private String YeyName;

    private int Style = 0;  // 考勤方式

    // 打开设置界面请求码
    private static final int REQUESTCODE_SETTING = 1;

    private boolean safeToTakePicture = false;
    private ImageView mPhotoInfo, back;
    private TextView mTv_bumen, mytime, jilu, wucashang;

    private int mLeaveTime; // 离园时间
    private String department; // 部门还是岗位

    private Yuyin baiduyuyin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//取消状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示...
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//防止休眠

        setContentView(R.layout.activity_main_new);
        baiduyuyin = Yuyin.getInstanit(this);//初始化百度语音
        initView();
        new TimeThread().start();//表
        /** 初始化数据，sql数据 */
        initData();

    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String str = sdf.format(new Date());
                    mHandler.sendMessage(mHandler.obtainMessage(VALUE_TIMER, str));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case VALUE_FENBANBOBAO:

                    Child bean = (Child) msg.obj;

                    break;

                case VALUE_TIMER: // 时间器

                    mytime.setText((String) msg.obj);

                    // 入园状态

                    if (Style == 0) {  // 自动
                        if (TimeUtil.getCurrentHour() < mLeaveTime) {
//                            mTvState.setText("上车");
//                            AttendanceDirection = 1;
                        } else {
//                            mTvState.setText("下车");
//                            AttendanceDirection = 2;
                        }
                    }

                    break;

            }
        }
    };


    private TextView mDay, mYearMon, mWeek;


    private void initView() {

        mYearMon = (TextView) findViewById(R.id.new_yearmonth);//年月
        mDay = (TextView) findViewById(R.id.new_day);//日
        mWeek = (TextView) findViewById(R.id.new_week);//星期
        mytime = (TextView) findViewById(R.id.mytime);//时分秒
        mTv_bumen = (TextView) findViewById(R.id.class_bumen);//班级
        mTvYeyName = (TextView) findViewById(R.id.tv_yey);//幼儿园名称
        mTvCardNo = (TextView) findViewById(R.id.tv_cardno);//卡号
        mTvName = (TextView) findViewById(R.id.tv_name);//姓名
        mTvClassName = (TextView) findViewById(R.id.tv_classname);//班级
        mTvTime = (TextView) findViewById(R.id.tv_time);//时间
        mTvState = (TextView) findViewById(R.id.state); // 入园或离园状态
//        jilu = (TextView) findViewById(R.id.tv_look_record);
        back = (ImageView) findViewById(R.id.title_back_iv);
//        wucashang = (TextView) findViewById(R.id.tv_look_shang);
//        wucashang.setOnClickListener(this);
        back.setVisibility(View.GONE);
    }

    private static int kgId;  // 园所id

    private void initData() {

        int Day = PrefUtils.getInt(this, "mDay", -1);
        if (Day != -1) {
            if (Day != TimeUtil.getDays()) {
                PrefUtils.saveToShared(this, Key_JiLu, null);
                PrefUtils.putInt(this, "mDay", TimeUtil.getDays());
            }
        } else {
            PrefUtils.putInt(this, "mDay", TimeUtil.getDays());
        }

        /** 年月日 **/
        mDay.setText(TimeUtil.getDay());
        mYearMon.setText(TimeUtil.getYearMon());
        mWeek.setText(TimeUtil.getWeek());

        mUrl_WEIXIN = UrlConstants.WeiXinURL; // 微信接口

        // 园所Id
        kgId = PrefUtils.getInt(this, Constants.YEYID, 33);

//        YeyName // 园所名称
        YeyName = PrefUtils.getString(MainActivity.this, Constants.YEYNAME, " "); // 数据初始完成后获取幼儿园名称
        mTvYeyName.setText(YeyName);  // 幼儿园


        // 离园和入园
        // 0自动  1入园  2离园
        Style = PrefUtils.getInt(this, Constants.ATTENDANCE_STYLE, 0);

        // 是否发送短信和微信,语音提醒
        ifWeiXin = PrefUtils.getBoolean(this, Constants.IF_WEIXIN, true); // 开始默认给手机微信提醒
        ifYuYin = PrefUtils.getBoolean(this, Constants.IF_YUYIN, true); // 开始默认语音播报


        /** 初始化数据 */
        UpdateUtil updateUtil = UpdateUtil.getInstance();

        updateUtil.setOnCompleteListener(new UpdateUtil.OnCompleteListener() {
            @Override
            public void complete() {

                LogUtil.d("从main界面初始化数据完成----");

                LogUtil.d("kgId:" + kgId + " YeyName:" + YeyName + " Style:" + Style + " ifWeiXin:" + ifWeiXin + " ifYuYin:" + ifYuYin);

                YeyName = PrefUtils.getString(MainActivity.this, Constants.YEYNAME, " "); // 数据初始完成后获取幼儿园名称
                mTvYeyName.setText(YeyName);  // 幼儿园

            }

            @Override
            public void fail() {
                LogUtil.d("main界面初始化数据时网络出现问题");
            }
        });


        /** post请求*/
        OkHttpUtil.getInstance().setPostListener(new OkHttpUtil.OnPostListener() {
            @Override
            public void onFail(int tag, final AttendanceRecord attendanceRecord, Child child1) {
                LogUtil.d("网络出现问题了，上传失败");

                if (tag == TAG_POST_ATTENDANCE && attendanceRecord != null) {
                    saveToLocal(attendanceRecord);
                    //添加记录
                    List<JiLuBean> list = (List<JiLuBean>) PrefUtils.queryForSharedToObject(MainActivity.this, Key_JiLu);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    if (AttendanceDirection == 1) {
                        JiLuBean jiLuBean = new JiLuBean();
                        jiLuBean.setLeixing("上车");
                        jiLuBean.setUserid(child1.userid);
                        jiLuBean.setName(child1.name);
                        jiLuBean.setClassName(child1.className);
                        jiLuBean.setClassid(child1.classInfoID);
                        jiLuBean.setTime(TimeUtil.getHM());
                        list.add(0, jiLuBean);
                        LogUtil.d("保存没上传的数据");
                    } else if (AttendanceDirection == 2) {

                        LogUtil.d("shanc没上传的数据");
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getUserid() == child1.userid) {
                                list.remove(i);
                                break;
                            }
                        }
                    }
                    //添加记录
                    PrefUtils.saveToShared(MainActivity.this, Key_JiLu, list);
                }

            }

            @Override
            public void onSuccess(int tag, final Child bean, final String json) {

                switch (tag) {
                    case TAG_POST_ATTENDANCE: // 上传考勤记录
                        LogUtil.d("上传考勤记录返回-：" + json);
                        if (TextUtils.equals(json, "true")) {  // 考勤成功
                            if (bean != null) {
                                // 幼儿和家长卡才发短信和微信
                                if (bean.cardType == 2) {
                                    return;
                                }
                                // 微信提醒
                                if (ifWeiXin) {
                                    WeiXinNotify(bean, AttendanceDirection);  // 传对象和进出方向
                                }
                                List<JiLuBean> list = (List<JiLuBean>) PrefUtils.queryForSharedToObject(MainActivity.this, Key_JiLu);
                                if (list == null) {
                                    list = new ArrayList<>();
                                }
                                if (AttendanceDirection == 1) {
                                    JiLuBean jiLuBean = new JiLuBean();
                                    jiLuBean.setLeixing("上车");
                                    jiLuBean.setUserid(bean.userid);
                                    jiLuBean.setName(bean.name);
                                    jiLuBean.setClassName(bean.className);
                                    jiLuBean.setClassid(bean.classInfoID);
                                    jiLuBean.setTime(TimeUtil.getHM());
                                    list.add(0, jiLuBean);
                                } else if (AttendanceDirection == 2) {
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).getUserid() == bean.userid) {
                                            list.remove(i);
                                            break;
                                        }
                                    }
                                }
                                //添加记录
                                PrefUtils.saveToShared(MainActivity.this, Key_JiLu, list);
                            }
                        }
                        break;
                    case TAG_POST_WEIXIN:
                        LogUtil.d("请求微信接口返回：" + json);
                        break;
                }
            }

        });

    }


    // 判断是入园还是离园
    int AttendanceDirection;  // 入园

    //  1. 显示幼儿信息和家长信息
    private void showInfo(String cardNo) {
        /** 从本地查询卡号对应信息 */
        ClassDao classDao = new ClassDao(MainActivity.this);
        final Child bean = classDao.queryByCardNo(cardNo);
        List<JiLuBean> list = (List<JiLuBean>) PrefUtils.queryForSharedToObject(MainActivity.this, Key_JiLu);
        AttendanceDirection = 1;
        if (list == null){
            list = new ArrayList<>();
        }
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getUserid() == bean.userid){
                AttendanceDirection = 2;
                break;
            }
        }
        LogUtil.d("监护人的个数：-----" + bean.list.size());

        if (bean.list.size() > 0) {
            LogUtil.d("姓名:" + bean.name + "  cardType:" + bean.cardType + " childId:" + bean.userid + "  监护人GuardianInfoId:" + bean.parentId + "  监护人UserId:" + bean.list.get(0).receiverUserId);
        } else {
            LogUtil.d("姓名:" + bean.name + "  cardType:" + bean.cardType + " childId:" + bean.userid + "  监护人GuardianInfoId:" + bean.parentId + "  监护人UserId:" + "无");
        }


        if (bean.name == null) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    safeToTakePicture = true;

                    if (bean.lastTime) {  // 一分钟内刷过
                        Toast.makeText(MainActivity.this, "刷卡频繁", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(MainActivity.this, "无此卡信息，请重新刷卡", Toast.LENGTH_SHORT).show();
                    }

                    // 清空数据
                    mTvName.setText("- -");
                    mTvClassName.setText("- -");
                    mTvTime.setText("- -");
                    mTvState.setText("- -");

                }
            });

            return;
        }
        Log.d("wzz---", "childName:------" + bean.name);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mTvName.setText(bean.name);
                mTvClassName.setText(bean.classInfoID == 0 ? bean.note : bean.className);  // 为0说明是老师，显示 教学部

                mTvTime.setText(TimeUtil.saveTime());
                if (AttendanceDirection == 1) {
                    mTvState.setText("上车");
                } else {
                    mTvState.setText("下车");
                }
            }
        });

        // 3. 语音播报

        if (ifYuYin) {

            if (AttendanceDirection == 1) {
                baiduyuyin.speak("欢迎" + bean.name);
            } else {
                baiduyuyin.speak(bean.name + "，再见");
            }
        }

        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.UserId = bean.userid;
        attendanceRecord.UserName = bean.name;
        attendanceRecord.CheckType = 1; // 考勤方式   0、手工  1、一体机读卡器 2、网络读卡器 3、通道 4、门禁  5、手机
        attendanceRecord.RelateId = bean.relateId;  // 刷家长卡时，relateId不为0  家长刷卡时候保存家长Id，幼儿或员工刷卡时默认为0
        attendanceRecord.SACardNo = cardNo;
        attendanceRecord.AttendanceDirection = AttendanceDirection; // 考勤进出方向   全部All -1  未知Unknown 0  入园Enter 1 出园Leave 2
        attendanceRecord.SADate = TimeUtil.getCurrentTime(); // 考勤时间
        attendanceRecord.AttendanceTaget = bean.cardType;  // 考勤对象  幼儿1  员工2  家长3

        upLoadAttendanceRecord(attendanceRecord, null, bean);
    }


    private TextView mTvYeyName, mTvState;

    private TextView mTvCardNo, mTvName, mTvClassName, mTvTime;

    /**
     * 上传考勤数据
     *
     * @param attendanceRecord
     * @param filename
     * @param bean
     */

    public void upLoadAttendanceRecord(AttendanceRecord attendanceRecord, String filename, final Child bean) {

        final Bitmap bitmap = BitmapFactory.decodeFile(filename);

        ArrayList<AttendanceRecord> list = new ArrayList<>();
        list.add(attendanceRecord);
        JKHttp.getInstance().upload(list, bitmap, kgId, bean, attendanceRecord);

    }

    /**
     * 将考勤记录保存在本地
     *
     * @param attendanceRecord
     */
    private void saveToLocal(AttendanceRecord attendanceRecord) {

        AttendanceDao attendanceDao = new AttendanceDao(this);
        attendanceDao.insert(attendanceRecord);
    }

    /**
     * 微信提醒
     *
     * @param bean
     * @param attendanceDirection
     */
    private void WeiXinNotify(Child bean, int attendanceDirection) {

//        您好,{0}{1}已到校,请勿挂念！【{2}】	{0}姓名 {1}时间 {2}幼儿园名称
//        您好,{0}{1}已离校,感谢关注！【{2}】	{0}姓名 {1}时间 {2}幼儿园名称
        String content1 = "您好，" + bean.name + TimeUtil.getCurrentTime() + "已上车，请勿挂念! " + YeyName;
        String content2 = "您好，" + bean.name + TimeUtil.getCurrentTime() + "已下车，感谢关注! " + YeyName;

        String content = null;
        if (attendanceDirection == 1) {
            content = content1;
        } else {
            content = content2;
        }

//       =================为了防止没有监护人而报错============================
        if (bean.list == null || bean.list.size() == 0) {
            return;
        }


        int receiverUserId = bean.list.get(0).receiverUserId;

        RequestBody requestBodyPost = new FormBody.Builder()
                .add("receiverUserId", receiverUserId + "") // 363  监护人表中的UserId字段  [  ]
                .add("noticecontent", content)
                .add("msgType", "2") // 消息类型  普通消息 1  考勤消息 2
                .add("childId", bean.userid + "") // 410  幼儿id  王翰潇
                .add("date", TimeUtil.getCurrentTime())
                .build();

        LogUtil.d("微信key-value： " + "[receiverUserId=" + receiverUserId + "&noticecontent=" + content + "&msgType=2" + "&childId=" + bean.userid + "&date" +
                "=" + TimeUtil.getCurrentTime() + "]");

        OkHttpUtil.getInstance().PostRequest2(mUrl_WEIXIN, TAG_POST_WEIXIN, null, requestBodyPost, null);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // 部门
        department = PrefUtils.getString(this, Constants.Department, "部门");

        // 离园和入园
        // 0自动  1入园  2离园
        Style = PrefUtils.getInt(this, Constants.ATTENDANCE_STYLE, 0);
        // 默认离园时间为12点
        mLeaveTime = PrefUtils.getInt(this, Constants.LeaveTime, 12);

    }

    @Override
    protected void onCardNoGet(final String CardNo) {

        mTvCardNo.setText(CardNo); //

        // usb刷卡后  1. 显示幼儿信息和家长信息  2. usb摄像头拍照保存  3. 语音播报
        ThreadManager.getTheadPool().execute(new Runnable() {
            @Override
            public void run() {

                showInfo(CardNo);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.exit(0); // 强制关闭虚拟机，否则下次进入应用刷卡读卡号会有问题。
    }

    public void jilu(View view) {
        startActivityForResult(new Intent(this, LookRecordActivity.class), 2);
//        jilu.setTextColor(getResources().getColor(R.color.colorAccent));
    }
    public void Shangche(View view){

//        wucashang.setTextColor(getResources().getColor(R.color.colorAccent));
        //单园时选择班级
        ClassDao classDao = new ClassDao(this);
        final List<ClassMessage> classMessages = classDao.queryAllClassList();
        final String[] clasList = new String[classMessages.size()];
        for (int i = 0; i < classMessages.size(); i++) {
            String classname = classMessages.get(i).getClassName();
            clasList[i] = classname;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("请选择班级");
        dialog.setIcon(R.mipmap.sch_classicon);
        dialog.setItems(clasList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, SelectClasChildActivity.class);
                intent.putExtra("selectclassid", classMessages.get(which).getClassInfoID());
                intent.putExtra("selectclassname", clasList[which]);
                intent.putExtra("fangxiangFlag", AttendanceDirection);
//                intent.putExtra("stationid", stationBean.getStationId());
//                intent.putExtra("KgId", UserInfoUtils.getInstance().getUserKgId());
                startActivityForResult(intent, 1);
            }
        });
        dialog.create();
        dialog.show();
    }

    public void select(View view) {
        //单园时选择班级
        ClassDao classDao = new ClassDao(this);
        final List<ClassMessage> classMessages = classDao.queryAllClassList();
        final String[] clasList = new String[classMessages.size()];
        for (int i = 0; i < classMessages.size(); i++) {
            String classname = classMessages.get(i).getClassName();
            clasList[i] = classname;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("请选择班级");
        dialog.setIcon(R.mipmap.sch_classicon);
        dialog.setItems(clasList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, SelectClasChildActivity.class);
                intent.putExtra("selectclassid", classMessages.get(which).getClassInfoID());
                intent.putExtra("selectclassname", clasList[which]);
                intent.putExtra("fangxiangFlag", AttendanceDirection);
//                intent.putExtra("stationid", stationBean.getStationId());
//                intent.putExtra("KgId", UserInfoUtils.getInstance().getUserKgId());
                startActivityForResult(intent, 1);
            }
        });
        dialog.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            Child bean = (Child) data.getSerializableExtra("selectObject");
            LogUtil.d("bena.getSacardno--:" + bean);
            if (bean != null && !bean.equals("")) {
                AttendanceDirection = 1;
                showInfo(bean);
            }
        } else if (requestCode == 2 && resultCode == 2) {
            int userid = data.getIntExtra("userid", 0);
            ClassDao classDao = new ClassDao(MainActivity.this);
            Child bean = classDao.queryChild(userid);
            AttendanceDirection = 2;
            showInfo(bean);
        }
    }

    //  1. 显示幼儿信息和家长信息
    private void showInfo(final Child bean) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mTvName.setText(bean.name);
                mTvClassName.setText(bean.className);  // 为0说明是老师，显示 教学部

                mTvTime.setText(TimeUtil.saveTime());

                if (AttendanceDirection == 1) {
                    mTvState.setText("上车");
                } else {
                    mTvState.setText("下车");
                }

            }
        });

        // 3. 语音播报

        if (ifYuYin) {

            if (AttendanceDirection == 1) {
                baiduyuyin.speak("欢迎" + bean.name);
            } else {
                baiduyuyin.speak(bean.name + "，再见");
            }
        }
        LogUtil.d("bean.tostring--:" + bean.toString());
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.UserId = bean.userid;
        attendanceRecord.UserName = bean.name;
        attendanceRecord.CheckType = 1; // 考勤方式   0、手工  1、一体机读卡器 2、网络读卡器 3、通道 4、门禁  5、手机
        attendanceRecord.RelateId = bean.relateId;  // 刷家长卡时，relateId不为0  家长刷卡时候保存家长Id，幼儿或员工刷卡时默认为0
        attendanceRecord.SACardNo = bean.sacrd;
        attendanceRecord.AttendanceDirection = AttendanceDirection; // 考勤进出方向   全部All -1  未知Unknown 0  入园Enter 1 出园Leave 2
        attendanceRecord.SADate = TimeUtil.getCurrentTime(); // 考勤时间
        attendanceRecord.AttendanceTaget = 1;  // 考勤对象  幼儿1  员工2  家长3

        upLoadAttendanceRecord(attendanceRecord, null, bean);
    }


    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
