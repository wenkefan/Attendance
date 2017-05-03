package hylk.com.xiaochekaoqin.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "attendance.db";
	private static final int DATABASE_VERSION = 1;

	public MyOpenHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}



	private static MyOpenHelper helper = null ;

	public static MyOpenHelper getInstance(Context context){

//		if (helper == null){
//			helper = new MyOpenHelper(context);
//		}

		if (helper == null){
			synchronized (MyOpenHelper.class){
				if (helper == null){
					helper = new MyOpenHelper(context) ;
				}

			}
		}
		return helper ;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  // ID KgId  UserType  UserId

		// 创建考勤用户表
		db.execSQL("create table AttendanceUser(ID integer primary key autoincrement,AutoSendMsg integer,BirthDay varchar(50),CampusNo varchar(20),ClassInfoID integer,HeadImage varchar(50) ,KgId integer,MonitorInfo varchar(20),Note nvarchar(20),SACardNo varchar(50),Sex integer ,State integer,UserId integer,UserName nvarchar(20), UserType integer,WorkerExtensionId integer )");

		// 创建考勤卡表
		db.execSQL("CREATE TABLE Card(Id integer primary key UNIQUE,AccessControlUserId varchar(50),CardNo varchar(50) ,CardType integer,CreateTime varchar(50),KgId integer ,Reason varchar(50),RelateId integer,State integer ,UpdateTime varchar(50),UserId integer)");


				//		public int AuditState;
				//		public int Capacity;
				//		public String ClassCardNo;
				//		public String ClassDes;
				//		public String ClassFullName;
				//		public String ClassImg;
				//		public int ClassInfoID;
				//		public String ClassName;
				//		public int ClassStage;
				//		public int ClassType;
				//		public String CreateDate;
				//		public String Creator;
				//		public String DispayOrder;
				//		public int KgId;
				//		public int State;
				//		public String UpDate;
				//		public String Updator;
		// 创建班级表 //已修改
		db.execSQL("create table ClassInfo(ID integer primary key autoincrement,AuditState integer,Capacity integer,ClassCardNo varchar(20),ClassDes varchar(20),ClassFullName varchar(20),ClassImg varchar(200)," +
					"ClassInfoID integer,ClassName varchar(20),ClassStage integer,ClassType integer,CreateDate varchar(50)," +
				"Creator varchar(50),DispayOrder varchar(20),KgId integer,State integer,Updator varchar(20) )");

//		// 创建班级类型表
//		db.execSQL("create table ClassType(ID integer primary key autoincrement,ClassType integer,ClassTypeName varchar(20) )");

		// 创建节假日表
		db.execSQL("create table HoliDays(ID integer primary key autoincrement,HolidayDate varchar(50),HolidayName varchar(20),HolidaysId integer,KgId integer,Memo varchar(20),State integer,TitleImage varchar(20) )");

		// 创建监护人表
		db.execSQL("create table Parents(ID integer primary key autoincrement,ChildId integer,GuardianImg varchar(20),Guardianlevel integer,HomeAddress varchar(20),Name varchar(20),KgId integer,Phone varchar(50),Sex integer,GuardianInfoId integer ,RelationshipName varchar(20),UserId integer)"); // GuardianInfoId为幼儿对应的父母id


		// 创建考勤记录表
		db.execSQL("CREATE TABLE AttendanceRecord(Id integer primary key UNIQUE,UserName varchar(50),SADate varchar(50) ,SACardNo varchar(50),RelateId integer ,CheckType integer,AttendanceTaget integer,UserId integer,AttendanceDirection integer)");

		// 创建刷卡记录表   // LastTime为long类型
		db.execSQL("CREATE TABLE CardNoRecord(Id integer primary key UNIQUE,CardNo varchar(50),LastTime integer )");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("CREATE TABLE WeiXinRecord(Id integer primary key UNIQUE,AttendanceDirection integer ,  ChildName varchar(50), ReceiverUserId integer , MsgType integer ,ChildId integer , SendDate varchar(50) )");

	}

}
