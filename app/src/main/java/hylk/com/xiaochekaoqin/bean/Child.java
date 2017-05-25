package hylk.com.xiaochekaoqin.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by _wzz on 2016/7/19 16:13.
 */
public class Child implements Serializable{
	public int userid; // userid

	public int relateId; // relateId

	public int classInfoID;  // 班级id
	public int sex;
	public String name; // 孩子姓名
	public String className; // 班级姓名

	public String HeadImage ; // 孩子头像

	public String birthDay ; // 孩子生日

	public int cardType;  // 卡类型
	public int userType;  // 用户类型

	public int parentId ; // 对应监护人里的GuardianInfoId字段，是孩子对应的家长userId

	public String sacrd;

	/** 监护人*/


	public ArrayList<Parent> list ;


	public String note;  // 用于老师刷卡时显示班级{如 教学部}，老师的ClassInfoID为0
	public boolean lastTime;  // 是否5分钟之内刷过卡

	@Override
	public String toString() {
		return "Child{" +
				"userid=" + userid +
				", relateId=" + relateId +
				", classInfoID=" + classInfoID +
				", sex=" + sex +
				", name='" + name + '\'' +
				", className='" + className + '\'' +
				", HeadImage='" + HeadImage + '\'' +
				", birthDay='" + birthDay + '\'' +
				", cardType=" + cardType +
				", userType=" + userType +
				", parentId=" + parentId +
				", sacrd='" + sacrd + '\'' +
				", list=" + list +
				", note='" + note + '\'' +
				", lastTime=" + lastTime +
				'}';
	}
}
