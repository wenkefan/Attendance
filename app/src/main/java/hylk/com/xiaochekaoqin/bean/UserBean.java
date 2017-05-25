package hylk.com.xiaochekaoqin.bean;

import java.io.Serializable;

/**
 * Created by fanwenke on 2017/5/23.
 */

public class UserBean implements Serializable {
    private int kgid;
    private int userId;
    private String userName;
    private int classId;
    private String className;
    private String SACardNo;

    public int getKgid() {
        return kgid;
    }

    public void setKgid(int kgid) {
        this.kgid = kgid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSACardNo() {
        return SACardNo;
    }

    public void setSACardNo(String SACardNo) {
        this.SACardNo = SACardNo;
    }
}
