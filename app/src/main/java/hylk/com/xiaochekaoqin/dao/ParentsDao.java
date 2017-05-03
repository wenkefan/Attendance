package hylk.com.xiaochekaoqin.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import hylk.com.xiaochekaoqin.bean.ParentsMessage;


/**
 * 操作sql考勤卡表
 *
 * @author _wzz
 */
public class ParentsDao {

	public Context context;
	public MyOpenHelper openHelper;

	public ParentsDao(Context context) {

		this.context = context;
		openHelper = MyOpenHelper.getInstance(context);

	}

	/**
	 * insert增
	 */
	public  void insert(List<ParentsMessage> list) {

		if (list.size() == 0 ){
			System.out.println("监护人表无数据：----------");
			return;
		}

		SQLiteDatabase db = openHelper.getWritableDatabase();

		long start = System.currentTimeMillis();
		db.beginTransaction();

			try {

				db.execSQL("delete from Parents");

					// 不是第一次登陆，根据修改数据进行更新或者增添
//
					for (int i = 0; i < list.size(); i++) {

						ParentsMessage bean = list.get(i);

						int childId = bean.ChildId;
						String guardianImg = bean.GuardianImg;
						int guardianlevel = bean.Guardianlevel;
						String homeAddress = bean.HomeAddress;
						String name = bean.Name;
						int kgId = bean.KgId;
						String phone = bean.Phone;
						int sex = bean.Sex;

						int userId = bean.UserId;   // 用于发微信时使用  对应receiverUserId

						int guardianInfoId = bean.GuardianInfoId; // 家长id
						String relationshipName = bean.RelationshipName; // 关系

						db.execSQL(
								"insert into Parents(ChildId,GuardianImg,Guardianlevel,HomeAddress,Name,KgId,Phone,Sex,GuardianInfoId,RelationshipName,UserId)values(?,?,?,?,?,?,?,?,?,?,?)",
								new Object[]{childId,guardianImg,guardianlevel,homeAddress,name,kgId,phone,sex,guardianInfoId,relationshipName,userId});
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
			System.out.println("插入监护人表的时间：----------" + time);

	}




	// 关闭
	public void close(){
		if (openHelper != null){
			openHelper.close();
		}
	}


}
