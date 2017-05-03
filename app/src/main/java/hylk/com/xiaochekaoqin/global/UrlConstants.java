package hylk.com.xiaochekaoqin.global;

/**
 * 连接服务器接口
 *
 * @author Created by wzz on 2016/5/12.
 *
 */
public class UrlConstants {

    // 公共接口
    public static final String JK_API = "http://manage.youery.com/WebServices/MedicalRecordsService.ashx?Option=";


    // 登录幼儿园
    public static final String JK_API_YOUERYUAN = JK_API
            + "CheckKindergarten&kgId=%1$s&kgName=%2$s";


    // 考勤用户 ( 老接口 )
    public static final String JK_API_ATTENDANCEUSER_old = JK_API
            + "GetAttendanceUserInfo&KgId=%1$s&modifyTime=2000-01-01";

    // 考勤用户( 新接口 )
    public static final String JK_API_ATTENDANCEUSER = JK_API
            + "GetAttendanceUserInfo&kgId=%1$s&userId=%2$s&modifyTime=2000-01-01";

    // 考勤卡信息
    public static final String JK_API_EXAMINATIONCARD = JK_API
            + "GetChildInfo&KgId=%1$s";

    // 班级信息 ( 老接口 )
    public static final String JK_API_CLASSINFO_old = JK_API
            + "GetClassInfo&KgId=%1$s";

    // 班级信息 ( 新接口)
    public static final String JK_API_CLASSINFO = JK_API
            + "GetUserClassInfoByUserId&kgId=%1$s&userId=%2$s";




    // 孩子头像
    public static final String CHILD_LOGO = "http://image.youery.com/";


    /** 新的接口 */

    public static final String LOGIN = JK_API + "Login&userName=%1$s&userPwd=%2$s";  // 老师登录，获取园所id和园所名称

    public static final String CLASSTYPE = JK_API + "GetClassType" ;  // 获取班级类型



    //   ---------------------------------------------------------



    // 公共接口
//    public static final String URL = "http://192.168.1.121:8080/WebServices/AttendanceService.ashx?Option=";
    public static final String URL = "http://manage.youery.com/WebServices/AttendanceService.ashx?Option=";

    // 幼儿园信息
    public static final String GetKindergarten = URL
            + "GetKindergarten&Id=%1$s";



    // 考勤用户接口
    public static final String GetAttendanceUser = URL
            + "GetAttendanceUser&Id=%1$s&modifyTime=%2$s";

    // 考勤卡接口
    public static final String GetAttendanceCard = URL
            + "GetAttendanceCard&Id=%1$s&modifyTime=%2$s";


    // 考勤班级接口
    public static final String GetClassListInfo = URL
            + "GetClassListInfo&Id=%1$s";


    // 幼儿园节日信息
    public static final String GetHolidays = URL
            + "GetHolidays&Id=%1$s";


     // 监护人信息
    public static final String GetGuardianListInfo = URL
            + "GetGuardianListInfo&Id=%1$s";


    // 上传考勤数据  ( 如果没有照片附件，第三个参数不传 )
    public static final String UploadAttendance2 = URL
            + "UploadAttendance2";


    // 发送短信接口
    public static final String SendSMS = URL + "SendSMS";

    // 微信提醒
//    public static final String WeiXinURL = "http://192.168.1.122:8077/weixin/SendNoticePost";
    public static final String WeiXinURL = "http://wx.youery.com/weixin/SendNoticePost";
//    public static final String WeiXinURL = "http://192.168.1.120:8077//weixin/SendNoticePost";


    public static final String LogoURL = "http://image.youery.com";

}
