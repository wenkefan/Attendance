package hylk.com.xiaochekaoqin.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import hylk.com.xiaochekaoqin.bean.AttendanceRecord;
import hylk.com.xiaochekaoqin.bean.Child;
import hylk.com.xiaochekaoqin.bean.WeiXinRecord;
import hylk.com.xiaochekaoqin.utils.TimeUtil;


/**
 * 操作sql考勤记录表
 *
 * @author _wzz
 */
public class AttendanceDao {

	public Context context;
	public MyOpenHelper openHelper;

	public AttendanceDao(Context context) {

		this.context = context;
		openHelper = MyOpenHelper.getInstance(context);

	}

	/**
	 * insert增
	 * @param bean
	 */
	public  void insert(AttendanceRecord bean) {

		SQLiteDatabase db = openHelper.getWritableDatabase();

		long start = System.currentTimeMillis();
		db.beginTransaction();

			try {

				db.execSQL(
						"insert into AttendanceRecord(UserName,SADate,SACardNo,RelateId,CheckType,RelateId,AttendanceTaget,UserId,AttendanceDirection)values(?,?,?,?,?,?,?,?,?)",
						new Object[]{bean.UserName,bean.SADate,bean.SACardNo,bean.RelateId,bean.CheckType,bean.RelateId,bean.AttendanceTaget,bean.UserId,bean.AttendanceDirection});

				//设置事务标志为成功，当结束事务时就会提交事务
				db.setTransactionSuccessful();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				//结束事务
				db.endTransaction();
			}

//			db.close();
			long end = System.currentTimeMillis();
			long time = end - start;
			System.out.println("插入一条考勤记录的时间：----------" + time);

	}

	/**
	 * insert增
	 * @param bean
	 */
	public  void insert(AttendanceRecord bean, Child child, int attendanceDirection) {

		SQLiteDatabase db = openHelper.getWritableDatabase();

		long start = System.currentTimeMillis();
		db.beginTransaction();

		try {

			db.execSQL(
					"insert into AttendanceRecord(UserName,SADate,SACardNo,RelateId,CheckType,RelateId,AttendanceTaget,UserId,AttendanceDirection) values (?,?,?,?,?,?,?,?,?)",
					new Object[]{bean.UserName,bean.SADate,bean.SACardNo,bean.RelateId,bean.CheckType,bean.RelateId,bean.AttendanceTaget,bean.UserId,bean.AttendanceDirection});

			db.execSQL(
					"insert into WeiXinRecord(AttendanceDirection,ChildName,ReceiverUserId,ChildId,SendDate) values (?,?,?,?,?)",
					new Object[]{ attendanceDirection , child.name , child.list.get(0).receiverUserId , child.userid , TimeUtil.getCurrentTime() });
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
		System.out.println("插入一条考勤记录的时间：----------" + time);

	}





	/**
	 * 查询未上传记录
	 */
	public ArrayList<AttendanceRecord> queryNoUpload() {

		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("select *  from  AttendanceRecord  ", null);

		ArrayList<AttendanceRecord> attendanceRecords = new ArrayList<>();

		while (cursor.moveToNext()) {

			AttendanceRecord attendanceRecord = new AttendanceRecord();

			// 获得用户编号
			int UserId = cursor.getInt(cursor.getColumnIndex("UserId"));
			String UserName = cursor.getString(cursor.getColumnIndex("UserName"));
			int CheckType = cursor.getInt(cursor.getColumnIndex("CheckType"));
			int RelateId = cursor.getInt(cursor.getColumnIndex("RelateId"));
			String SACardNo = cursor.getString(cursor.getColumnIndex("SACardNo"));
			int AttendanceDirection = cursor.getInt(cursor.getColumnIndex("AttendanceDirection"));
			String SADate = cursor.getString(cursor.getColumnIndex("SADate"));
			int AttendanceTaget = cursor.getInt(cursor.getColumnIndex("AttendanceTaget"));

			attendanceRecord.UserId= UserId ;
			attendanceRecord.UserName = UserName ;
			attendanceRecord.CheckType = 1; // 考勤方式   0、手工  1、一体机读卡器 2、网络读卡器 3、通道 4、门禁  5、手机
			attendanceRecord.RelateId = RelateId ;  // 刷家长卡时，relateId不为0  家长刷卡时候保存家长Id，幼儿或员工刷卡时默认为0
			attendanceRecord.SACardNo = SACardNo ;
			attendanceRecord.AttendanceDirection = AttendanceDirection ; // 考勤进出方向   全部All -1  未知Unknown 0  入园Enter 1 出园Leave 2
			attendanceRecord.SADate = SADate; // 考勤时间
			attendanceRecord.AttendanceTaget = AttendanceTaget ;  // 考勤对象  幼儿1  员工2  家长3

			attendanceRecords.add(attendanceRecord);

		}

		db.execSQL("delete from AttendanceRecord");

		cursor.close();
		db.close();

		return attendanceRecords;

	}

	public ArrayList<WeiXinRecord> queryWXRecord() {

		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery(" select *  from  WeiXinRecord  ", null);

		ArrayList<WeiXinRecord> weiXinList = new ArrayList<>();

		while (cursor.moveToNext()) {

			WeiXinRecord  bean = new WeiXinRecord();

			// 获得用户编号
			bean.AttendanceDirection = cursor.getInt(cursor.getColumnIndex("AttendanceDirection"));
			bean.ChildName = cursor.getString(cursor.getColumnIndex("ChildName"));
			bean.ReceiverUserId = cursor.getInt(cursor.getColumnIndex("ReceiverUserId"));
			bean.MsgType = cursor.getInt(cursor.getColumnIndex("MsgType"));
			bean.ChildId = cursor.getInt(cursor.getColumnIndex("ChildId"));
			bean.SendDate = cursor.getString(cursor.getColumnIndex("SendDate"));

			weiXinList.add( bean );

		}

		db.execSQL("delete from WeiXinRecord");

		cursor.close();
		db.close();

		return weiXinList;
	}


	// 关闭
	public void close(){
		if (openHelper != null){
			openHelper.close();
		}
	}

	public void delete() {

		SQLiteDatabase db = openHelper.getReadableDatabase();

		db.execSQL("delete from AttendanceRecord");

		db.close();

	}
}
