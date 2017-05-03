package hylk.com.xiaochekaoqin.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import hylk.com.xiaochekaoqin.bean.HoliDayMessage;


/**
 * 操作sql考勤卡表
 *
 * @author _wzz
 */
public class HoliDayDao {

	public Context context;
	public MyOpenHelper openHelper;

	public HoliDayDao(Context context) {

		this.context = context;
		openHelper = MyOpenHelper.getInstance(context);

	}

	/**
	 * insert增
	 */
	public  void insert(List<HoliDayMessage> list) {

		if (list.size() == 0 ){
			System.out.println("假期表无数据：----------");
			return;
		}

		SQLiteDatabase db = openHelper.getWritableDatabase();

		long start = System.currentTimeMillis();
		db.beginTransaction();

			try {

				db.execSQL("delete from HoliDays");

					// 不是第一次登陆，根据修改数据进行更新或者增添

					for (int i = 0; i < list.size(); i++) {

						HoliDayMessage bean = list.get(i);

						String holidayDate = bean.HolidayDate;
						String holidayName = bean.HolidayName;
						int holidaysId = bean.HolidaysId;
						int kgId = bean.KgId;
						String memo = bean.Memo;
						int state = bean.State;
						String titleImage = bean.TitleImage;

						db.execSQL(
								"insert into HoliDays(HolidayDate,HolidayName,HolidaysId,KgId,Memo,State,TitleImage)values(?,?,?,?,?,?,?)",
								new Object[]{holidayDate,holidayName,holidaysId,kgId,memo,state,titleImage});
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
			System.out.println("插入节假日表的时间：----------" + time);

	}




	// 关闭
	public void close(){
		if (openHelper != null){
			openHelper.close();
		}
	}


}
