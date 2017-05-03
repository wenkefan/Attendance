package hylk.com.xiaochekaoqin.bean;

/**
 * Created by Administrator on 2016/10/19.
 */
public class AttendanceRecord {


    /**
     * AttendanceDirection : 1
     * AttendanceTaget : 1
     * CheckInfo :
     * CheckType : 4
     * DiseaseType : 0
     * IfSendMsg : 0
     * MsgPrompt :
     * RelateId : 0
     * SACardNo : 2B71D20C
     * SADate : 2016-10-17 07:40:12
     * SAId : 8696
     * TemperatureField : 0
     * UploadState : 0
     * UserId : 22155
     * UserName : null
     */




    /** 用到的 */
    public int UserId;  // 幼儿、职工编号
    public String UserName;
    public int CheckType; // 考勤方式   0、手工  1、一体机读卡器 2、网络读卡器 3、通道 4、门禁  5、手机
    public int RelateId; // 家长刷卡时候保存家长Id，幼儿或员工刷卡时默认为0

    public String SACardNo; // 考勤卡号
    public int AttendanceDirection; // 考勤进出方向   全部All -1  未知Unknown 0  入园Enter 1 出园Leave 2
    public String SADate; // 考勤时间
    public int AttendanceTaget; // 考勤对象  幼儿1  员工2  家长3

//    [{"UserName":"test","SADate":"2016-10-19 15:19:13","SACardNo":"E00401005C284360","IfSendMsg":0,"DiseaseType":0,"RelateId":2757,"CheckType":1,"AttendanceTaget":3,"SAId":0,"TemperatureField":0,"UploadState":0,"UserId":2825,"AttendanceDirection":1}]
//    [{"UserName":"test","SADate":"2016-10-19 15:23:05","SACardNo":"E00401005C284360","RelateId":2757,"CheckType":1,"AttendanceTaget":3,"UserId":2825,"AttendanceDirection":1}]

    /** 暂时未用到的 */
//    public String CheckInfo;
//
//    public int DiseaseType;
//    public int IfSendMsg;
//    public String MsgPrompt;
//
//    public int SAId;  //  0
//    public int TemperatureField;
//    public int UploadState;




}
