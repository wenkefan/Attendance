package hylk.com.xiaochekaoqin.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 网络判断，判读当前网络是否可用
 * 
 * @author wzz
 * 
 */
public class DateGetAgeUtil {

	public static int getAge(String str) throws Exception
	{
		/** 将字符串转为Date格式 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date birthDay = sdf.parse(str);

		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay))
		{
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth)
		{
			if (monthNow == monthBirth)
			{
				if (dayOfMonthNow < dayOfMonthBirth)
					age--;
			}
			else
			{
				age--;
			}
		}
		return age;
	}
}
