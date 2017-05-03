package hylk.com.xiaochekaoqin.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hylk.com.xiaochekaoqin.bean.Child;
import hylk.com.xiaochekaoqin.bean.ClassMessage;
import hylk.com.xiaochekaoqin.bean.Parent;
import hylk.com.xiaochekaoqin.utils.LogUtil;

/**
 * 操作sql班级表
 * @author lenovo
 */
public class ClassDao {

	public Context context;
	public MyOpenHelper openHelper;

	public ClassDao(Context context) {
		this.context = context;
		openHelper = MyOpenHelper.getInstance(context);

	}

	public ArrayList<ClassMessage> queryAllClassList(){

		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("select ClassInfoID,ClassName  from  ClassInfo where State = 1 ", null);

		ArrayList<ClassMessage> list = new ArrayList<>();
		/**
		 * AuditState : 1
		 * Capacity : 30
		 * ClassCardNo : 001
		 * ClassDes : 大1班
		 * ClassFullName : null
		 * ClassImg : /GardenImage/33/ClassAlbum/55e4aa5b-3e6e-48c8-a5a4-a5679e438267.jpg
		 * ClassInfoID : 53
		 * ClassName : 大1班
		 * ClassStage : 1
		 * ClassType : 2
		 * CreateDate : 2014-02-10 00:00:00
		 * Creator : htyey
		 * DispayOrder : null
		 * KgId : 33
		 * State : 1
		 * UpDate : 2015-06-25 00:00:00
		 * Updator : htyey
		 */

		while (cursor.moveToNext()) {

			ClassMessage bean = new ClassMessage();

			// 获得用户编号
			int ClassInfoID = cursor.getInt(cursor.getColumnIndex("ClassInfoID"));
			String ClassName = cursor.getString(cursor.getColumnIndex("ClassName"));

			bean.ClassInfoID = ClassInfoID ;
			bean.ClassName = ClassName ;


			list.add(bean);

		}

		cursor.close();
		db.close();

		return list;

	}

	/**
	 * insert增 班级表
	 */
	public void insert(List<ClassMessage> list) {

		if (list.size() == 0){
			System.out.println("班级表无更新数据：----------");
			return;
		}

		SQLiteDatabase db = openHelper.getWritableDatabase();
		long start = System.currentTimeMillis();
		db.beginTransaction();
		try {
			db.execSQL("delete from ClassInfo");

				for (int i = 0; i < list.size(); i++) {

					ClassMessage bean = list.get(i);

					int auditState = bean.AuditState;
					int capacity = bean.Capacity;
					String classCardNo = bean.ClassCardNo;
					String classDes = bean.ClassDes;
					String classFullName = bean.ClassFullName;
					String classImg = bean.ClassImg;
					int classInfoID = bean.ClassInfoID;
					String className = bean.ClassName;
					int classStage = bean.ClassStage;
					int classType = bean.ClassType;
					String createDate = bean.CreateDate;
					String creator = bean.Creator;
					String dispayOrder = bean.DispayOrder;
					int kgId = bean.KgId;
					int state = bean.State;
					String upDate = bean.UpDate;
					String updator = bean.Updator;

					db.execSQL(
							"insert into ClassInfo (ClassDes,ClassImg," +
									"ClassInfoID,ClassName,ClassType,KgId,State)values(?,?,?,?,?,?,?)",
							new Object[]{classDes, classImg,classInfoID,className, classType, kgId, state});

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
		System.out.println("插入班级表的时间：----------" + time);

	}




	/**
	 * query查  根据孩子卡号查询班级和姓名
	 */
	public Child queryByCardNo(String CardNo) {

		boolean hasThisCard = false ;  // 是否已经存过此卡号

		Child bean = new Child();
		bean.list = new ArrayList<Parent>();

		SQLiteDatabase db = openHelper.getReadableDatabase();

		// 先查询有无之前
		Cursor cursor5 = db.rawQuery("select * from CardNoRecord where CardNo = ?", new String[]{CardNo});

		if (cursor5.moveToNext()){

			hasThisCard = true ;

			long lastTime = cursor5.getLong(cursor5.getColumnIndex("LastTime"));

			if (lastTime + 10*1000 > System.currentTimeMillis()){  // 上次刷卡时间小于10s，提示已经刷过卡
				bean.lastTime = true ;  // 证明10s内刷过卡

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

//		db.close();



		return bean;
	}



	// 关闭
	public void close() {
		if (openHelper != null) {
			openHelper.close();
		}
	}

}
