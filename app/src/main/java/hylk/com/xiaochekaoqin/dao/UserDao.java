package hylk.com.xiaochekaoqin.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import hylk.com.xiaochekaoqin.bean.UserMessage;

/**
 * 操作sql用户表
 *
 * @author lenovo
 */
public class UserDao {

	public Context context;
	public MyOpenHelper openHelper;

	public UserDao(Context context) {
		this.context = context;
		openHelper = MyOpenHelper.getInstance(context);


	}

	// 插入用户表
	public  void insert(List<UserMessage> list, boolean isFirst) {

		if (list.size() == 0 ){
			System.out.println("考勤用户表无更新数据：----------");
			return;
		}

		SQLiteDatabase db = openHelper.getWritableDatabase();
		long start = System.currentTimeMillis();
		db.beginTransaction();

		try {

			if (isFirst){

				db.execSQL("delete from AttendanceUser");

				for (int i = 0; i < list.size(); i++) {

					UserMessage user = list.get(i);

					int kgId = user.KgId;
					int workerExtensionId = user.WorkerExtensionId ;
					int userType = user.UserType;
					int userId = user.UserId;
					int classInfoID = user.ClassInfoID;
					String note = user.Note;
					String userName = user.UserName;
					int sex = user.Sex;
					String headImage = user.HeadImage;
					String birthDay = user.BirthDay;
					int autoSendMsg = user.AutoSendMsg;
					int state = user.State;
					String sACardNo = user.SACardNo;
					String campusNo = user.CampusNo;
					String monitorInfo = user.MonitorInfo;

					db.execSQL(
							"insert into AttendanceUser(AutoSendMsg,BirthDay,CampusNo,ClassInfoID,HeadImage,KgId,MonitorInfo,Note,SACardNo,Sex,State,UserName,UserType,WorkerExtensionId,UserId)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							new Object[]{autoSendMsg,birthDay,campusNo,classInfoID,headImage,kgId,monitorInfo,note,sACardNo,sex,state,userName,userType,workerExtensionId,userId});

				}

			}else {

				for (int i = 0; i < list.size(); i++) {

					UserMessage user = list.get(i);

					int kgId = user.KgId;
					int workerExtensionId = user.WorkerExtensionId ;
					int userType = user.UserType;
					int userId = user.UserId;
					int classInfoID = user.ClassInfoID;
					String note = user.Note;
					String userName = user.UserName;
					int sex = user.Sex;
					String headImage = user.HeadImage;
					String birthDay = user.BirthDay;
					int autoSendMsg = user.AutoSendMsg;
					int state = user.State;
					String sACardNo = user.SACardNo;
					String campusNo = user.CampusNo;
					String monitorInfo = user.MonitorInfo;


					Cursor cursor = db.rawQuery(
							"select UserId  from  AttendanceUser where UserId = ? ",
							new String[]{ userId + "" });

					if (cursor.moveToNext()) {
						db.execSQL(
								"update AttendanceUser set AutoSendMsg=?,BirthDay=?,CampusNo=?,ClassInfoID=?,HeadImage=?,KgId=?,MonitorInfo=?,Note=?," +
										"SACardNo=?,Sex = ? ,State = ?  ,UserName = ? ,UserType = ? ,WorkerExtensionId = ? where UserId = ?",
								new Object[]{autoSendMsg,birthDay,campusNo,classInfoID,headImage,kgId,monitorInfo,note,sACardNo,sex,state,userName,userType,workerExtensionId,userId});
					}else {
						db.execSQL(
								"insert into AttendanceUser(AutoSendMsg,BirthDay,CampusNo,ClassInfoID,HeadImage,KgId,MonitorInfo,Note,SACardNo,Sex,State,UserName,UserType,WorkerExtensionId,UserId)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
								new Object[]{autoSendMsg,birthDay,campusNo,classInfoID,headImage,kgId,monitorInfo,note,sACardNo,sex,state,userName,userType,workerExtensionId,userId});
					}

					cursor.close();


				}

			}

			//设置事务标志为成功，当结束事务时就会提交事务
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//结束事务
			db.endTransaction();
		}

		db.close();
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("插入考勤用户表的时间：----------" + time); // 插入完成的时间大约在9s左右
	}




	// 关闭
	public void close() {
		if (openHelper != null) {
			openHelper.close();
		}
	}


}
