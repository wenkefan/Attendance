package hylk.com.xiaochekaoqin.utils;

import hylk.com.xiaochekaoqin.global.Constants;
import hylk.com.xiaochekaoqin.global.MyApplication;

/**
 * Created by wenke on 2018/3/16.
 */

public class ClearUpData {

    private static final String Key_JiLu = "Key_JiLu";
    public static ClearUpData getInstance(){
        return new ClearUpData();
    }

    public ClearUpData clearData(){
        PrefUtils.removeString(MyApplication.getContext(),Key_JiLu);
        return this;
    }

    public void clearTime(){
        PrefUtils.removeString(MyApplication.getContext(), Constants.CLEARUPDATA);
    }
}
