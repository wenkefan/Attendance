package hylk.com.xiaochekaoqin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * sharedpreference工具类
 */
public class PrefUtils {
	public static SharedPreferences sp;

	public static SharedPreferences getSharedPre(Context ctx) {
		if (sp == null) {
			sp = ctx.getSharedPreferences("config_attendance",
					Context.MODE_PRIVATE);
		}
		return sp;
	}

	public static void putBoolean(Context ctx, String key,
								  boolean value) {
		SharedPreferences sp = getSharedPre(ctx);
		sp.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context ctx, String key,
									 boolean defValue) {
		SharedPreferences sp = getSharedPre(ctx);
		return sp.getBoolean(key, defValue);
	}

	public static void putString(Context ctx, String key, String value) {
		SharedPreferences sp = getSharedPre(ctx);
		sp.edit().putString(key, value).commit();
	}

	public static String getString(Context ctx, String key,
								   String defValue) {
		SharedPreferences sp = getSharedPre(ctx);
		return sp.getString(key, defValue);
	}

	public static void putInt(Context ctx, String key, int value) {
		SharedPreferences sp = getSharedPre(ctx);
		sp.edit().putInt(key, value).commit();
	}

	public static int getInt(Context ctx, String key, int defValue) {
		SharedPreferences sp = getSharedPre(ctx);
		return sp.getInt(key, defValue);
	}

	public static void putLong(Context ctx, String key, long value) {
		SharedPreferences sp = getSharedPre(ctx);
		sp.edit().putLong(key, value).commit();
	}

	public static long getLong(Context ctx, String key, long defValue) {
		SharedPreferences sp = getSharedPre(ctx);
		return sp.getLong(key, defValue);
	}
	
	public static void removeString(Context ctx, String key) {
	    getSharedPre(ctx).edit().remove(key).commit();
	}


	public static void putFloat(Context ctx, String key, float value) {
		SharedPreferences sp = getSharedPre(ctx);
		sp.edit().putFloat(key, value).commit();

	}

	public static float getFloat(Context ctx, String key, float defValue) {
		SharedPreferences sp = getSharedPre(ctx);
		return sp.getFloat(key, defValue);

	}

	public static void saveToShared(Context ctx,String key, Object obj) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SharedPreferences sp = getSharedPre(ctx);
		SharedPreferences.Editor editor;
		try {
			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(obj);
			String value = new String(Base64.encode(out.toByteArray()));
			editor = sp.edit();
			editor.putString(key, value);
			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object queryForSharedToObject(Context ctx,String key) {
		SharedPreferences sp = getSharedPre(ctx);
		String value = sp.getString(key, null);
		if (value != null) {
			byte[] valueBytes = Base64.decode(value);
			ByteArrayInputStream bin = new ByteArrayInputStream(valueBytes);
			try {
				ObjectInputStream oin = new ObjectInputStream(bin);

				return oin.readObject();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
}
