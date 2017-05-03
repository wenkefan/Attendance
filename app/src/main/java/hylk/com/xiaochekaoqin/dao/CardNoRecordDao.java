package hylk.com.xiaochekaoqin.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * 操作sql考勤记录表
 *
 * @author _wzz
 */
public class CardNoRecordDao {

	public Context context;
	public MyOpenHelper openHelper;

	public CardNoRecordDao(Context context) {

		this.context = context;
		openHelper = MyOpenHelper.getInstance(context);

	}



	// 关闭
	public void close(){
		if (openHelper != null){
			openHelper.close();
		}
	}

	public void delete() {

		SQLiteDatabase db = openHelper.getReadableDatabase();

		db.execSQL("delete from CardNoRecord");

		db.close();

	}
}
