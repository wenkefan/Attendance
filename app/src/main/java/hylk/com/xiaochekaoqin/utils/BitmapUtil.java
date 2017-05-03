package hylk.com.xiaochekaoqin.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 时间工具类，获取当前时间
 * Created by _wzz on 2016/5/12.
 */
public class BitmapUtil {
	/**
	 * bitmap转为base64
	 * @param bitmap
	 * @return
	 */
	public  static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP); // No_WRAP转后的字符串无空格，后台接受的图片会小一些
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

}
