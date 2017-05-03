package hylk.com.xiaochekaoqin.bean;

/**
 * Created by Administrator on 2016/11/3.
 */
public class Parent{

    public int parentLevel;
    public int parentSex;
    public String parentName;
    public String parentPhone;
    public String parentLogo;
    public String relationshipName;  // 关系名称 爸爸/妈妈/爷爷/奶奶


    public int parentId ; // 对应监护人里的GuardianInfoId字段，是孩子对应的家长userId


    public int receiverUserId;  // 对应监护人表里的UserId字段，只用于发微信时的receiverUserId的参数



}