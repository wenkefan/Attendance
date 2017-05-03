package hylk.com.xiaochekaoqin.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类，获取当前时间
 * Created by _wzz on 2016/5/12.
 */
public class TimeUtil {

	// 获得当前时间，用于获取平台数据时使用
	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;

	}

	public static String getCurrentTime_data() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str.substring(0,10);

	}

	// 获得当前小时，用于判断考勤方式为自动时，根据当前时间判断入园还是离园
	public static int getCurrentHour() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate).substring(11,13);

		return Integer.valueOf(str);

	}


	/**
	 * 获得当前时间(HTTP转换后使用的)
	 *
	 * @return 当前时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String uploadTime() {
		String str;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		str = formatter.format(curDate);
		str = str.replace(" ", "%20");  // 注：这里%20不能修改，涉及到后面选择症状提交时的接口问题。 2016-05-26%2010:44:51
		return str;
	}
	public static String saveTime() {
		String str;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		str = formatter.format(curDate);
		str = str.replace(" ", " ").substring(0,16);  // 注：这里%20不能修改，涉及到后面选择症状提交时的接口问题。 2016-05-26%2010:44:51
		return str;
	}

	public static String saveTime1() {  // 保存本地人成功或失败的文件时间
		String str;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		str = formatter.format(curDate);
		str = str.replace(" ", " ").substring(0,19);  // 注：这里%20不能修改，涉及到后面选择症状提交时的接口问题。 2016-05-26%2010:44:51
		return str;
	}


	// 获得当前时间
//	public static String getTodayTime() {  // 获取今天的时间  06-08
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
//
//		String str = formatter.format(curDate);
//		String todayTime = str.substring(5, 11);
//		return todayTime;   // 06-08
//
//	}

	// 获得当前时间
	public static String getTodayTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		String str = formatter.format(curDate);
		String todayTime = str.substring(0, 10); // 2016-06-08
		System.out.println("todayTime-------"+todayTime);
		return todayTime;

	}

	//
	// 获得当前年月
	public static String getYearMon() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		String str = formatter.format(curDate);
		String todayTime = str.substring(0, 10); // 2016-06-08
		String year = str.substring(0, 4);  // 2016
		String month = str.substring(5, 7);
		int mon = Integer.parseInt(month); // 6
		String day = str.substring(8, 10);
		int da = Integer.parseInt(day); // 8
		System.out.println("todayTime-------"+todayTime);
		return year+"年"+mon+"月";

	}
	// 获得当前日期
	public static String getDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间

		String str = formatter.format(curDate);
		String todayTime = str.substring(0, 10); // 2016-06-08
		String year = str.substring(0, 4);  // 2016
		String month = str.substring(5, 7);
		int mon = Integer.parseInt(month); // 6
		String day = str.substring(8, 10);
		int da = Integer.parseInt(day); // 8
		System.out.println("todayTime-------"+todayTime);
		return da+"日";

	}

	public static String getWeek(){
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

		String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码

		String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if("1".equals(mWay)){
			mWay ="日";
		}else if("2".equals(mWay)){
			mWay ="一";
		}else if("3".equals(mWay)){
			mWay ="二";
		}else if("4".equals(mWay)){
			mWay ="三";
		}else if("5".equals(mWay)){
			mWay ="四";
		}else if("6".equals(mWay)){
			mWay ="五";
		}else if("7".equals(mWay)){
			mWay ="六";
		}
		return "星期"+mWay;
	}
}
