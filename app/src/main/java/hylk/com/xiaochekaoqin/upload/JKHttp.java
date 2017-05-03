package hylk.com.xiaochekaoqin.upload;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.util.ArrayList;

import hylk.com.xiaochekaoqin.bean.AttendanceRecord;
import hylk.com.xiaochekaoqin.bean.Child;
import hylk.com.xiaochekaoqin.global.UrlConstants;
import hylk.com.xiaochekaoqin.utils.BitmapUtil;
import hylk.com.xiaochekaoqin.utils.LogUtil;
import hylk.com.xiaochekaoqin.utils.OkHttpUtil;
import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * 连接接口 返回所需数据
 *
 * @author Anxu
 */

public class JKHttp {



	public static Gson gson = new Gson();
	private static JKHttp instance;


	public static final int TAG_POST_ATTENDANCE = 0;

	public static JKHttp getInstance() {

		if (instance == null) {
			instance = new JKHttp();
		}

		return instance;
	}


	public static String mUrl_Attendance = UrlConstants.UploadAttendance2; // 上传考勤数据;


	public void upload(ArrayList<AttendanceRecord> mAttendanceRecordList, Bitmap bitmap, int kgId, Child bean, AttendanceRecord attendanceRecord) {

//		ArrayList<AttendanceRecord> mAttendanceRecordList = new ArrayList<>();
//
//		mAttendanceRecordList.add(attendanceRecord);

		String jsonStr = gson.toJson(mAttendanceRecordList);

		LogUtil.d("listToJson---"+jsonStr);

		RequestBody requestBodyPost = null ;

		if (bitmap != null){

			String bitmapStr = BitmapUtil.bitmapToBase64(bitmap);


			LogUtil.d("bitmapStr---- "+bitmapStr);

			requestBodyPost = new FormBody.Builder()
					.add("Id", kgId+"")
					.add("jsonStr", jsonStr)
					.add("bitmapStr", bitmapStr)
					.build();

		}else {

			requestBodyPost = new FormBody.Builder()
					.add("Id", kgId+"")
					.add("jsonStr", jsonStr)
//					.add("bitmapStr", bitmapStr)
					.build();

		}


		// 上传图片
//		Option=UploadAttendance2&Id=33&jsonStr=
//		String jsonStr = "[{"AttendanceDirection":1,"AttendanceTaget":1,"CheckInfo":"","CheckType":4,"DiseaseType":0,"IfSendMsg":0,"MsgPrompt":"","RelateId":0,"SACardNo":"2B71D20C","SADate":"2016-10-17 07:40:12","SAId":8696,"TemperatureField":0,"UploadState":0,"UserId":22155,"UserName":null}]";


//		OkHttpUtil.getInstance().PostRequest(mUrl_Attendance,TAG_POST_ATTENDANCE,requestBodyPost);
		OkHttpUtil.getInstance().PostRequest2(mUrl_Attendance,TAG_POST_ATTENDANCE,bean,requestBodyPost,attendanceRecord);

	}




}
