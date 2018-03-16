package hylk.com.xiaochekaoqin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by wenke on 2018/3/16.
 */

public class ClearTime {

    public static ClearTime getInstance() {
        return new ClearTime();
    }

    public boolean getTime(String tiem1, String time2) {
        try {
            DateFormat date = new SimpleDateFormat("HH:mm:ss");
            Date one = date.parse(tiem1);
            Date two = date.parse(time2);
            long one1 = one.getTime();
            long two1 = two.getTime();
            long diff;
            long H;
            if (one1 >= two1) {
                return true;
            } else {
                diff = two1 - one1;
                H = diff / (1000 * 60 * 60);
                if (H >= 2)
                    return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
