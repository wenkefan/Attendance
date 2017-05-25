package hylk.com.xiaochekaoqin.bean;

import java.io.Serializable;

/**
 * Created by fanwenke on 2017/5/22.
 */

public class JiLuBean implements Serializable {
    private String name;
    private int kgid;
    private String kgName;
    private int classid;
    private String className;
    private String time;
    private String leixing;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKgid() {
        return kgid;
    }

    public void setKgid(int kgid) {
        this.kgid = kgid;
    }

    public String getKgName() {
        return kgName;
    }

    public void setKgName(String kgName) {
        this.kgName = kgName;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) {
        this.leixing = leixing;
    }
}
