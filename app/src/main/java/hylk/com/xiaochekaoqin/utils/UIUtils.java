package hylk.com.xiaochekaoqin.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.view.View;

import hylk.com.xiaochekaoqin.global.MyApplication;


/**
 * 用来获取初始化的一些可能经常用到的对象
 * 
 * @author Administrator
 * 
 */
public class UIUtils {
	
	// 获取context
	public static Context getContext() {
		return MyApplication.getContext();
	}

	// 获取handler
	public static Handler getHandler() {
		return MyApplication.getHandler();
	}
//
//	// 获取线程id
//	public static int getMainThreadId() {
//		return MyApplication.getMainThreadId();
//	}
//
//	// ////////////////////////////////////////////////////////
//
//	// 获取字符串
//	public static String getString(int id) {
//		return getContext().getResources().getString(id);
//	}
//
//	// 颜色
//	public static int getColor(int id) {
//		return getContext().getResources().getColor(id);
//	}
//
//	// 图片
//	public static Drawable getDrawable(int id) {
//		return getContext().getResources().getDrawable(id);
//	}

	// 尺寸 单位是像素
	public static int getDimen(int id) {
		return getContext().getResources().getDimensionPixelSize(id);
	}

	// 获取字符串数组
	public static String[] getStringArray(int id) {
		return getContext().getResources().getStringArray(id);
	}
	
	// dip转为px   dip = px / density ;
	public static int dip2px(float dip){
		float density = getContext().getResources().getDisplayMetrics().density;
		int px = (int) (dip * density + 0.5f);
		return px ;
	}
	//px转为dip
	public static float px2dip(int px){
		float density = getContext().getResources().getDisplayMetrics().density;
		float dip = px / density ;
		return dip  ;
	}
	
	////////////////////////////////////////////
	//获取布局文件
	public static View inflate(int id){
		View view = View.inflate(getContext(), id, null);
		return view;
	}
	////////////////////////////////////////
	
//	//判断当前线程是否为主线程 ui线程
//	public static boolean isRunOnUiThread(){
//		int myTid = android.os.Process.myTid(); //在哪里调用，返回哪个线程的id
//		//拿到调用此方法的方法中获取线程id和主线程的id比较，相同说明就是主线程，否则为不是
//		return myTid == getMainThreadId()? true : false ;
//	}
//
	/**
	 * 运行在主线程
	 * @param r
	 */
//	public static void runOnUiThread(Runnable r){
//		if (isRunOnUiThread()) {
//			// 已经运行在主线程
//			r.run();
//		}else {
//			//没有在主线程中，用handler来让它运行在主线程中
//			//和发消息一样，发送一个可执行的对象，此对象运行在主线程
//			getHandler().post(r);
//		}
//	}

	/**
	 * //获取颜色选择器
	 * @param mTabTextColorResId
	 * @return
	 */
	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		
		return getContext().getResources().getColorStateList(mTabTextColorResId);
	}

}
