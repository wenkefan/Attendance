package hylk.com.xiaochekaoqin.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import hylk.com.xiaochekaoqin.bean.AttendanceRecord;
import hylk.com.xiaochekaoqin.bean.Child;
import hylk.com.xiaochekaoqin.bean.Parent;
import hylk.com.xiaochekaoqin.utils.LogUtil;

/**
 * 数据库操作类   在方法中添加同步锁，多线程并发的时候的执行顺序为，依次执行，避免数据库锁定的异常。
 *
 */
public class DBManagerCorrect {

	private static MyOpenHelper helper;
	private static SQLiteDatabase db;
	/**
	 * 记数器 应该设置静态的类变量
	 * @param context
	 */
	private static  int mCount;
	//同一个数据库连接
	private static DBManagerCorrect mMnanagerInstance;
	private DBManagerCorrect(Context context) {
		helper = MyOpenHelper.getInstance(context);
	}
	//单例
	public static synchronized DBManagerCorrect getIntance(Context context){
		if(mMnanagerInstance==null){
			return new DBManagerCorrect(context);
		}
		return mMnanagerInstance;
	}

	public synchronized SQLiteDatabase openDb(){
		if(mCount==0){
			db=helper.getWritableDatabase();
		}
		mCount++;
		return db;
	}
	public synchronized void CloseDb(SQLiteDatabase database){
		mCount--;
		if(mCount==0){
			database.close();
		}
	}
/*

	// 插入
	private void insert(Student student) {
		try {
			db.execSQL(
					"INSERT INTO " + DBHelper.TABLE + " VALUES(?,?,?)",
					new Object[] {null,student.mName,student.mAge});
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	*/
/**
	 * @param students
	 *//*

	public void  insertAll(List<Student> students,Thread thread) {
		try {
			Log.i("lthj.exchangestock2", "helper对象为：="+helper.toString()+"线程名为："+thread.getName());
			Log.i("lthj.exchangestock2", "db对象为="+db.toString()+"线程名为："+thread.getName());
			if (students == null) {
				return;
			}
			for (Student student : students) {
				insert(student);
//							Log.i("lthj.exchangestock2", "正在插入。。数据=+"+student.getmID()+student.getmName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	*/
/**
	 * @param student
	 *//*

	private void update(Student student) {
		try {
			db.execSQL("UPDATE " + DBHelper.TABLE
							+ " SET mName = ? WHERE _id = ?",
					new Object[] { student.mName,student.mID });
			Log.e("lthj.exchangestock2", "正在更新。。数据=+"+student.getmID()+student.getmName());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	*/
/**
	 * 更新所有
	 * @param students
	 *//*

	public void  updateAll(List<Student> students) {
		try {
			if (students == null) {
				return;
			}
			for (Student student : students) {
				update(student);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 删除指定数据
	public void delete(String id) {
		try {
			db.execSQL("DELETE FROM " + DBHelper.TABLE + " WHERE _id = ? ",
					new String[] { id });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除所有数据
	public void delete() {
		try {
			db.execSQL("DELETE * FROM " + DBHelper.TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查找所有的Students
	public List<Student> query() {
		List<Student> students = new ArrayList<Student>();
		Cursor c = queryTheCursor();
		Student student = null;
		try {
			while (c.moveToNext()) {
				student=new Student();
				student.mID=c.getString(c.getColumnIndex("_id"));
				student.mName=c.getString(c.getColumnIndex("mName"));
				student.mAge=c.getInt(c.getColumnIndex("mAge"));
				students.add(student);
				Log.i("lthj.exchangestock2", "正在查询。。数据=+"+student.getmID()+student.getmName());
				student=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return students;
	}

	// 查找指定ID的Student
	public Student query(String id) {
		Student student = null;
		Cursor c = queryTheCursor(id);
		if(c==null)return null;
		try {
			while (c.moveToNext()) {
				student=new Student();
				student.mID=c.getString(c.getColumnIndex("_id"));
				student.mName=c.getString(c.getColumnIndex("mName"));
				student.mAge=c.getInt(c.getColumnIndex("mAge"));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return student;
	}

	// 获取游标
	public Cursor queryTheCursor(String id) {
		try {
			Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE
					+ " WHERE _id = ?", new String[] { id });
			return c;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	// 获取游标
	public Cursor queryTheCursor() {
		try {
			if (!db.isOpen()) {
				db = helper.getWritableDatabase();
			}
			Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE, null);
			return c;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();return null;
		}
	}
*/



	/**
	 * query查  根据孩子卡号查询班级和姓名
	 */
	public Child queryByCardNo(String CardNo) {

		boolean hasThisCard = false ;  // 是否已经存过此卡号

		Child bean = new Child();
		bean.list = new ArrayList<Parent>();



		// 先查询有无之前
		Cursor cursor5 = db.rawQuery("select * from CardNoRecord where CardNo = ?", new String[]{CardNo});

		if (cursor5.moveToNext()){

			hasThisCard = true ;

			long lastTime = cursor5.getLong(cursor5.getColumnIndex("LastTime"));

			if (lastTime + 0*60*1000 > System.currentTimeMillis()){  // 上次刷卡时间小于五分钟，提示已经刷过卡
				bean.lastTime = true ;  // 证明五分钟内刷过卡

				cursor5.close();
				db.close();

				return  bean;
			}

		}


		Cursor cursor = db.rawQuery(
				"select * from Card where State = ? and CardNo = ?", //CardType 幼儿1 老师2 家长3
				new String[]{String.valueOf(1), CardNo});

		if (cursor.moveToNext()) {

			int cardType = cursor.getInt(cursor.getColumnIndex("CardType"));
			int RelateId = cursor.getInt(cursor.getColumnIndex("RelateId"));


//			cardType1时，userId为幼儿id
//			cardType3时，userId为家长id（对应监护人表里的GuardianInfoId），relateId为幼儿id（对应监护人表里的ChildId）

			int userId ;
			if (cardType == 3){
				userId = cursor.getInt(cursor.getColumnIndex("RelateId"));
				bean.parentId = cursor.getInt(cursor.getColumnIndex("UserId"));
			}else {
				userId = cursor.getInt(cursor.getColumnIndex("UserId"));
			}

			bean.userid = userId ;  // 幼儿Id
			bean.cardType = cardType ;
			bean.relateId = RelateId ;


			Cursor cursor1 = db.rawQuery(
					"select * from AttendanceUser where UserId = ? and State = ? ",  // and UserType = ? usertype 幼儿1  老师2
					new String[]{String.valueOf(userId), String.valueOf(1) } );

			if (cursor1.moveToNext()) {
				// 幼儿名称
				bean.name = cursor1.getString(cursor1.getColumnIndex("UserName"));

				LogUtil.d(bean.name);


				bean.note = cursor1.getString(cursor1.getColumnIndex("Note"));  // 用于当刷老师卡时，classInfoId为0，显示Note { 教学部 }

				bean.sex = cursor1.getInt(cursor1.getColumnIndex("Sex"));

				// 获得班级编号 用来查询班级表 得到班级
				int ClassInfoID = cursor1.getInt(cursor1.getColumnIndex("ClassInfoID"));

				String headImage = cursor1.getString(cursor1.getColumnIndex("HeadImage"));
				String birthDay = cursor1.getString(cursor1.getColumnIndex("BirthDay"));
				int userType = cursor1.getInt(cursor1.getColumnIndex("UserType"));

				bean.HeadImage = headImage ;

				bean.birthDay = birthDay ;

				bean.classInfoID = ClassInfoID ;

				bean.userType = userType ;  // usertype

				if (cursor1 != null) {
					cursor1.close();
				}

				// 通过班级编号得到班级名称
				Cursor cursor2 = db.rawQuery(
						"select ClassName from ClassInfo where ClassInfoID = ?",
						new String[]{String.valueOf(ClassInfoID)});
				if (cursor2.moveToNext()) {
					// 班级姓名
					bean.className = cursor2.getString(cursor2.getColumnIndex("ClassName"));
				}
				if (cursor2 != null) {
					cursor2.close();
				}




				/// ---------------------------------------
				// 通过userId得到监护人信息  集合


				if (cardType == 2 ){  // 老师没有监护人
					return  bean;
				}

				Cursor cursor3 = null ;


				if (cardType == 3){

					cursor3 = db.rawQuery(
							"select * from Parents where ChildId = ? and GuardianInfoId = ? ",
							new String[]{String.valueOf(userId),String.valueOf(bean.parentId)});

				}else {

					cursor3 = db.rawQuery(
							"select * from Parents where ChildId = ? and Guardianlevel = ?",
							new String[]{String.valueOf(userId),String.valueOf(1)});

				}

				if (cursor3.moveToNext()){ // 监护人

					Parent parent = new Parent();

					LogUtil.d("查询监护人进来了3-----");

					parent.parentLevel = cursor3.getInt(cursor3.getColumnIndex("Guardianlevel"));
					parent.parentSex = cursor3.getInt(cursor3.getColumnIndex("Sex"));
					parent.parentName = cursor3.getString(cursor3.getColumnIndex("Name"));
					parent.parentPhone = cursor3.getString(cursor3.getColumnIndex("Phone"));
					parent.parentId = cursor3.getInt(cursor3.getColumnIndex("GuardianInfoId")); // 家长id
					parent.parentLogo = cursor3.getString(cursor3.getColumnIndex("GuardianImg"));

					parent.relationshipName = cursor3.getString(cursor3.getColumnIndex("RelationshipName"));  // 关系名称

					parent.receiverUserId = cursor3.getInt(cursor3.getColumnIndex("UserId"));  // 用于发微信时的receiverUserId参数

					bean.list.add(parent);

				}

				if (cursor3 != null) {
					cursor3.close();
				}

				// -------------------------------------------------------------------------
				Cursor cursor4 = null ;

				if (cardType == 3){

					cursor4 = db.rawQuery(
							"select * from Parents where ChildId = ? and GuardianInfoId != ? ",
							new String[]{String.valueOf(userId),String.valueOf(bean.parentId)});


				}else {

					cursor4 = db.rawQuery(
							"select * from Parents where ChildId = ? and Guardianlevel != ?",
							new String[]{String.valueOf(userId),String.valueOf(1)});

				}

				while (cursor4.moveToNext()){ // 监护人

					Parent parent = new Parent();

					parent.parentLevel = cursor4.getInt(cursor4.getColumnIndex("Guardianlevel"));
					parent.parentSex = cursor4.getInt(cursor4.getColumnIndex("Sex"));
					parent.parentName = cursor4.getString(cursor4.getColumnIndex("Name"));
					parent.parentPhone = cursor4.getString(cursor4.getColumnIndex("Phone"));
					parent.parentId = cursor4.getInt(cursor4.getColumnIndex("GuardianInfoId")); // 家长id
					parent.parentLogo = cursor4.getString(cursor4.getColumnIndex("GuardianImg"));

					parent.relationshipName = cursor4.getString(cursor4.getColumnIndex("RelationshipName"));  // 关系名称

					parent.receiverUserId = cursor4.getInt(cursor4.getColumnIndex("UserId"));  // 用于发微信时的receiverUserId参数

					bean.list.add(parent);

				}

				if (cursor4 != null) {
					cursor4.close();
				}


				//  ------------------------------------------------------------------
				// 将卡号插入到sql中

				if (hasThisCard){
					db.execSQL( "update CardNoRecord set LastTime=? where CardNo = ? ",new Object[]{System.currentTimeMillis(),CardNo});
				}else {
					db.execSQL("insert into CardNoRecord (CardNo,LastTime) values (?,?) ",new Object[]{CardNo,System.currentTimeMillis()});
				}


			}



		}

		if (cursor != null) {
			cursor.close();
		}

		db.close();



		return bean;
	}





	/**
	 * insert增
	 * @param bean
	 */
	public  void insert(AttendanceRecord bean) {

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

		db.close();
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("插入一条考勤记录的时间：----------" + time);

	}





}
