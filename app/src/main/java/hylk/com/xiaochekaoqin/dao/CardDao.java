package hylk.com.xiaochekaoqin.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import hylk.com.xiaochekaoqin.bean.CardMessage;


/**
 * 操作sql考勤卡表
 *
 * @author _wzz
 */
public class CardDao {

	public Context context;
	public MyOpenHelper openHelper;

	public CardDao(Context context) {

		this.context = context;
		openHelper = MyOpenHelper.getInstance(context);

	}

	/**
	 * insert增
	 */
	public  void insert(List<CardMessage> list, boolean isFirst) {

		if (list.size() == 0 ){
			System.out.println("考勤卡表无更新数据：----------");
			return;
		}

		SQLiteDatabase db = openHelper.getWritableDatabase();

		long start = System.currentTimeMillis();
		db.beginTransaction();

			try {

				if (isFirst){
					// 第一次登陆

					db.execSQL("delete from Card");

					for (int i = 0; i < list.size(); i++) {

						CardMessage bean = list.get(i);

						String accessControlUserId = bean.AccessControlUserId;
						String cardNo = bean.CardNo;
						int cardType = bean.CardType;
						String createTime = bean.CreateTime;
						int id = bean.Id;  // 唯一标识 不变的
						int kgId = bean.KgId;
						String reason = bean.Reason;
						int relateId = bean.RelateId;
						int state = bean.State;
						String updateTime = bean.UpdateTime;
						int userId = bean.UserId;

						db.execSQL(
								"insert into Card(Id,AccessControlUserId,CardNo,CardType,KgId,Reason,RelateId,State,UpdateTime,UserId)values(?,?,?,?,?,?,?,?,?,?)",
								new Object[]{id,accessControlUserId,cardNo,cardType,kgId,reason,relateId,state,updateTime,userId});
					}

				}else {

					// 不是第一次登陆，根据修改数据进行更新或者增添


					for (int i = 0; i < list.size(); i++) {

						CardMessage bean = list.get(i);

						String accessControlUserId = bean.AccessControlUserId;
						String cardNo = bean.CardNo;
						int cardType = bean.CardType;
						String createTime = bean.CreateTime;
						int id = bean.Id;  // 唯一标识 不变的
						int kgId = bean.KgId;
						String reason = bean.Reason;
						int relateId = bean.RelateId;
						int state = bean.State;
						String updateTime = bean.UpdateTime;
						int userId = bean.UserId;

						Cursor cursor = db.rawQuery(
								"select Id  from  Card where UserId = ? ",
								new String[]{ userId + "" });

						if (cursor.moveToNext()) {
							db.execSQL("update Card set AccessControlUserId=?,CardNo=?,CardType=?,KgId=?,Reason=?,RelateId=?,State=?,UpdateTime=? where UserId = ?",
									new Object[]{accessControlUserId,cardNo,cardType,kgId,reason,relateId,state,updateTime,userId});
						}else {
							db.execSQL(
									"insert into Card(Id,AccessControlUserId,CardNo,CardType,KgId,Reason,RelateId,State,UpdateTime,UserId)values(?,?,?,?,?,?,?,?,?,?)",
									new Object[]{id,accessControlUserId,cardNo,cardType,kgId,reason,relateId,state,updateTime,userId});
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
			System.out.println("插入考勤卡表的时间：----------" + time);

	}


	// delete删
	// update改


	/**
	 * 根据卡号获得(用户编号UserId)
	 */
	public int queryChildId( String CardNo) {
		int ChildId = 0;
		SQLiteDatabase db = openHelper.getReadableDatabase();
		System.out.println("aaaa--------");

		Cursor cursor = db.rawQuery(
				"select * from Card where State = ? and CardNo = ?",
				new String[]{String.valueOf(1), CardNo});
		if (cursor.moveToNext()) {
			System.out.println("bbbb--------");
			// System.out.println(cursor.getInt(5) + "幼儿编号");
			// 获得用户编号
			ChildId = cursor.getInt(cursor.getColumnIndex("UserId"));

		}
		System.out.println("cccc--------");
		cursor.close();

		db.close();
		return ChildId;

	}


	/**
	 * 根据姓名获得CardNo(卡号)
	 */
	public String queryCardNo(String name) {

		String CardNo = "";

		SQLiteDatabase db = openHelper.getReadableDatabase();
		System.out.println("aaaa--------");

		Cursor cursor = db.rawQuery(
				"select UserId  from  AttendanceUser where UserName = ? ",
				new String[]{ name });


		if (cursor.moveToNext()) {

			// System.out.println(cursor.getInt(5) + "幼儿编号");
			// 获得用户编号
			int ChildId = cursor.getInt(cursor.getColumnIndex("UserId"));
			Cursor cursor1 = db.rawQuery(
					"select CardNo  from  Card where UserId = ? ",
					new String[]{ String.valueOf(ChildId) });
			if (cursor1.moveToNext()){
				CardNo = cursor1.getString(cursor1.getColumnIndex("CardNo"));
			}
			cursor1.close();

		}

		cursor.close();
		db.close();

		return CardNo;

	}

	// 关闭
	public void close(){
		if (openHelper != null){
			openHelper.close();
		}
	}


}
