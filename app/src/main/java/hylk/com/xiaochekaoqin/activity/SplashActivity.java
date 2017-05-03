package hylk.com.xiaochekaoqin.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import hylk.com.xiaochekaoqin.R;
import hylk.com.xiaochekaoqin.bean.AttendanceRecord;
import hylk.com.xiaochekaoqin.bean.AutoUpdaterDTO;
import hylk.com.xiaochekaoqin.bean.Child;
import hylk.com.xiaochekaoqin.bean.WeiXinRecord;
import hylk.com.xiaochekaoqin.dao.AttendanceDao_0;
import hylk.com.xiaochekaoqin.dao.CardNoRecordDao;
import hylk.com.xiaochekaoqin.global.Constants;
import hylk.com.xiaochekaoqin.global.UrlConstants;
import hylk.com.xiaochekaoqin.upload.JKHttp;
import hylk.com.xiaochekaoqin.utils.AutoUpdaterUtils;
import hylk.com.xiaochekaoqin.utils.LogUtil;
import hylk.com.xiaochekaoqin.utils.OkHttpUtil;
import hylk.com.xiaochekaoqin.utils.PrefUtils;
import hylk.com.xiaochekaoqin.utils.TimeUtil;
import hylk.com.xiaochekaoqin.utils.ToastUtil;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static hylk.com.xiaochekaoqin.upload.JKHttp.TAG_POST_ATTENDANCE;


/**
 *	Created by wzz on 2016/5/11.
 */
public class SplashActivity extends Activity {

	private boolean if_first = true ;

	private String mUrl_WEIXIN = UrlConstants.WeiXinURL; // 微信接口
	private AttendanceDao_0 mDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//高亮显示...

		setContentView(R.layout.activity_splash);

		Log.d("有无SD卡：-------", hasSDcard() + "");

		check();

//		login();


	}

	// 版本更新检测
	private ProgressDialog mMypDialog;

	private void showProgress1() {
		//实例化
		mMypDialog = new ProgressDialog(SplashActivity.this);
		//设置进度条风格，风格为圆形，旋转的
		mMypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//设置ProgressDialog 标题
		mMypDialog.setTitle("请稍候");
		//设置ProgressDialog 提示信息
		mMypDialog.setMessage("正在检测版本更新...");
		//设置ProgressDialog 标题图标
//        mMypDialog.setIcon(R.drawable.logo);

		//设置ProgressDialog 的进度条是否不明确
		mMypDialog.setIndeterminate(false);
		//设置ProgressDialog 是否可以按退回按键取消
//		mMypDialog.setCancelable(false);

		//让ProgressDialog显示
		mMypDialog.show();
	}

	private void login() {

		if_first = PrefUtils.getBoolean(getApplication(), Constants.FIRST_LOGIN, true);

		if (if_first){   // 第一次登陆

			// 延缓2s，进入登陆界面
			new Thread() {
				@Override
				public void run() {
					super.run();

					SystemClock.sleep(2000);
					handler.sendEmptyMessage(0);

				}
			}.start();

		}else {

			/** 初始化数据 */
			UpdateUtil updateUtil = UpdateUtil.getInstance();
			updateUtil.init(SplashActivity.this,false);
			updateUtil.upDateInfo();
			updateUtil.setOnCompleteListener(new UpdateUtil.OnCompleteListener() {
				@Override
				public void complete() {

					LogUtil.d( "splash界面初始化数据成功");
					deleteDataAndUpload();

				}

				@Override
				public void fail() {

					LogUtil.d( "splash界面初始化数据时网络出现问题");
					deleteDataAndUpload() ;

				}
			});

		}
	}

	private void deleteDataAndUpload() {

		showProgress();

		// 判断当前时间是不是本天，删除上一天拍的图片和 五分钟之内刷的卡表
		String lastDay = PrefUtils.getString(SplashActivity.this, Constants.LAST_DAY, "");
		String todayTime = TimeUtil.getTodayTime();

		if (!lastDay.equals(todayTime)){

			/** 删照片 */
			String picturePath = getPicturePath();
			File file = new File(picturePath);
			deleteFile(file);

			/** 删刷卡记录 */
			CardNoRecordDao dao = new CardNoRecordDao(SplashActivity.this);

			dao.delete();

			dao.close();

		}

		//  -----------------------------
		method();

	}

	private void method() {

		setOkhttpListener();

		queryAndUpload();
	}

	private void setOkhttpListener() {
		/** post请求*/
		OkHttpUtil.getInstance().setPostListener(new OkHttpUtil.OnPostListener() {
			@Override
			public void onFail(int tag, AttendanceRecord attendanceRecord, Child child1) {
				LogUtil.d("网络出现问题了，上传失败");

				if (tag == JKHttp.TAG_POST_ATTENDANCE && attendanceRecord == null ){

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ToastUtil.show(SplashActivity.this,"网络出现问题，续传考勤记录失败");
							goMain();
						}
					});

				}

			}

			@Override
			public void onSuccess(int tag, final Child bean, final String json) {

				switch (tag) {

					case TAG_POST_ATTENDANCE: // 上传考勤记录

						LogUtil.d("上传考勤记录返回-：" + json);

						if (TextUtils.equals(json, "true")) {  // 考勤成功

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(SplashActivity.this,"续传考勤记录成功");
									mDao.delete();
									mDao.close();
									goMain();
                                }
                            });

						}else {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									ToastUtil.show(SplashActivity.this,"续传考勤记录失败");
									goMain();
								}
							});
						}
						// 成功之后再删除数据
						break;

				}
			}

		});


	}

	private void goMain() {

		mypDialog.cancel();

		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//						Intent intent = new Intent(SplashActivity.this, MainActivity_0.class);
		startActivity(intent);

		finish();
	}


	//递归删除文件夹
	private void deleteFile(File file) {
		if (file.exists()) {//判断文件是否存在
			if (file.isFile()) {//判断是否是文件
				file.delete();//删除文件
			} else if (file.isDirectory()) {//否则如果它是一个目录
				File[] files = file.listFiles();//声明目录下所有的文件 files[];
				for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件
					this.deleteFile(files[i]);//把每个文件用这个方法进行迭代
				}
				file.delete();//删除文件夹
			}
		} else {
			System.out.println("所删除的文件夹不存在-----");
		}
	}


	/** 获取图片存储路径 */
	public String getPicturePath(){

		//下面获取sd卡的根目录，设置我们需要保存的路径...
//        String filename = Environment.getExternalStorageState().toString() + File.separator + "CameraPhoto" + File.separator + "picture" + ".jpg";

		String filename = null ;
		// 图片路径初始化
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {  // 有内存卡
			filename = Environment.getExternalStorageDirectory().getPath() + "/hylk/attendance/img_USBCamera/";
			Log.d("wzz------", "有内存卡");
		} else {  // 无内存卡

			filename = getFilesDir().getPath() + "/hylk/attendance/img_USBCamera/";
			Log.d("wzz------", "无内存卡");
		}


		File file = new File(filename);
		if (!file.exists()) {//如果父文件夹不存在则进行新建...
			file.mkdirs();
		}

		return filename;
	}


	/**
	 * 查询未上传体检记录，并上传
	 */
	private void queryAndUpload() {

		mDao = new AttendanceDao_0(this);

		ArrayList<AttendanceRecord> list = mDao.queryNoUpload();  // 查询 、 上传、 并删除


//		ToastUtil.show(this,"查询出未上传记录个数：list.size---"+list.size());

		LogUtil.d("考勤未上传记录个数： "+list.size());

		if (list.size()>0){
			JKHttp.getInstance().upload(list,null,PrefUtils.getInt(this, Constants.YEYID,33), null,null);
		}else {
			ToastUtil.show(SplashActivity.this,"无未上传考勤记录");
			mDao.close();
			goMain();
		}


	}







	/** 微信提醒
	 * @param bean
	 * */
	private void WeiXinNotify(WeiXinRecord bean) {

//        您好,{0}{1}已到校,请勿挂念！【{2}】	{0}姓名 {1}时间 {2}幼儿园名称
//        您好,{0}{1}已离校,感谢关注！【{2}】	{0}姓名 {1}时间 {2}幼儿园名称
		String content1 = "您好，" + bean.ChildName + TimeUtil.getCurrentTime() +"已到校，请勿挂念! " + PrefUtils.getString(this,Constants.YEYNAME,"幼儿园");
		String content2 = "您好，" + bean.ChildName + TimeUtil.getCurrentTime() +"已离校，感谢关注! " + PrefUtils.getString(this,Constants.YEYNAME,"幼儿园") ;

		String content = null;
		if (bean.AttendanceDirection == 1){
			content = content1;
		}else {
			content = content2;
		}

		int receiverUserId = bean.ReceiverUserId ;

		RequestBody requestBodyPost = new FormBody.Builder()
				.add("receiverUserId",  receiverUserId+"") // 363  监护人表中的UserId字段  [  ]
				.add("noticecontent",content)
				.add("msgType", "2") // 消息类型  普通消息 1  考勤消息 2
				.add("childId", bean.ChildId+"") // 410  幼儿id  王翰潇
				.add("date", bean.SendDate )
				.build();

		LogUtil.d("微信key-value： "+ "[receiverUserId="+ receiverUserId +"&noticecontent="+content+"&msgType=2"+"&childId="+bean.ChildId+"&date" +
				"="+ bean.SendDate +"]");


		OkHttpUtil.getInstance().PostRequest2(mUrl_WEIXIN,0,null,requestBodyPost,null);

	}




	private ProgressDialog mypDialog;  // 进度提示，请稍候

	private void showProgress() {
		//实例化
		mypDialog = new ProgressDialog(SplashActivity.this);
		//设置进度条风格，风格为圆形，旋转的
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//设置ProgressDialog 标题
		mypDialog.setTitle("请稍候");
		//设置ProgressDialog 提示信息
		mypDialog.setMessage("查询未上传记录中...");
		//设置ProgressDialog 标题图标
//		mypDialog.setIcon(R.drawable.logo);

		//设置ProgressDialog 的进度条是否不明确
		mypDialog.setIndeterminate(false);
		//设置ProgressDialog 是否可以按退回按键取消
//		mypDialog.setCancelable(false);

		//让ProgressDialog显示
		mypDialog.show();
	}




	public static boolean hasSDcard(){
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 先判断是否首次进入程序
			switch (msg.what) {

				case 0:   // 进入登陆界面

					Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(intent);

					finish();

					break;
			}

		}
	};


	private AutoUpdaterDTO auto;

	//  版本更新 --------------------------

	public void check() {


		showProgress1();


		TextView tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText(getVersionName(this));  // 版本号

		new Thread(new Runnable() {

			public void run() {

				try {
					auto = AutoUpdaterUtils.getInstance().http();
				} catch (IOException | XmlPullParserException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mMypDialog.dismiss();
					}
				});

				if (auto != null) {

					LogUtil.d( auto.getAllowMinVersion() + "----" + auto.getCurrentVersion());

					if (maxVersions(auto.getCurrentVersion())) {
						dialog();
					} else {

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								loginMain();
							}
						});

					}
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(SplashActivity.this, "网络连接出现问题", Toast.LENGTH_SHORT)
									.show();
							loginMain();
						}
					});
				}

			}
		}).start();

	}


	/**
	 * 版本更新提示
	 */
	protected void dialog() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// 判断强制更新和费强制更新
				if (!minVersions(auto.getAllowMinVersion())) {
					AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
					b.setTitle("版本更新提示");
					b.setMessage("版本过低请更新");
					b.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									if (fileIsExists(auto
											.getCurrentVersionFileName())) {
									} else
										downloadApp(
												auto.getCurrentVersionFileUrl(),
												auto.getCurrentVersionFileName());
								}
							});
					b.setCancelable(false);// 设置点击屏幕Dialog不消失
					b.create().show();


				} else {
					/*
					AlertDialog.Builder b = new AlertDialog.Builder(SplashActivity.this);
					b.setTitle("版本更新提示");
					b.setMessage("您有新版本，是否更新 ?");
					b.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
													int which) {
//                                    if (fileIsExists(auto
//                                            .getCurrentVersionFileName())) {
//                                    } else
									downloadApp(
											auto.getCurrentVersionFileUrl(),
											auto.getCurrentVersionFileName());
								}
							});
					*//** 下方是为了让他只能选择更新，去掉否选项 *//*
					*//*if (minVersions(auto.getAllowMinVersion())) {
						b.setNegativeButton("否",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
														int which) {
										dialog.dismiss();
										loginMain();

									}
								});
					}*//*
					b.setCancelable(false);// 设置点击屏幕Dialog不消失
					b.create().show();

					*/


					//  ======================================
					/** 以下为让它自动下载 ，不提示用户 */  // _wzz  2017/01/06
					downloadApp(
							auto.getCurrentVersionFileUrl(),
							auto.getCurrentVersionFileName());

					//  ------------------------------------------------
				}
			}
		});

	}


	// 进入main
	public void loginMain() {

		login();

	}


	ProgressDialog tDialog;
	HttpHandler<File> handler1;

	/** 获取图片存储路径 */
	public String getApkDownloadPath(){

		String filename = null ;
		// 图片路径初始化
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {  // 有内存卡
			filename = Environment.getExternalStorageDirectory().getPath() + "/hylk/attendance/app/";
			Log.d("wzz------", "有内存卡");
		} else {  // 无内存卡

			filename = getFilesDir().getPath() + "/hylk/attendance/app/";
			Log.d("wzz------", "无内存卡");
		}


		File file = new File(filename);
		if (!file.exists()) {//如果父文件夹不存在则进行新建...
			file.mkdirs();
		}

		return filename;
	}

	/**
	 * 下载最新安装包
	 *
	 * @param uri     地址
	 * @param apkname 名字
	 */
	@SuppressWarnings("deprecation")
	public void downloadApp(String uri, String apkname) {

		// 下载路径
		String apkDownloadPath = "";

		tDialog = new ProgressDialog(this);
		tDialog.setCanceledOnTouchOutside(false);
		tDialog.setProgressNumberFormat("%1d kb/%2d kb");
		tDialog.setCancelable(false);
		tDialog.setTitle("下载进度");
		tDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		/** 不让它取消下载 */  // _wzz  2017/01/06
		/*tDialog.setButton("取消下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler1.cancel();
				loginMain();
			}
		});*/

		System.out.println(apkname);

		apkDownloadPath = getApkDownloadPath() + apkname;    // 自定义apk下载路径   /hylk/attendance/app/LK_Attendance.apk


		HttpUtils utils = new HttpUtils();
		handler1 = utils.download(uri, apkDownloadPath, new RequestCallBack<File>() {

			@Override
			public void onStart() {
				// 开始下载 super.onStart();
				System.out.println("开始下载");

			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// 下载中 super.onLoading(total, current, isUploading);

				System.out.println("下载中。。。。");

				tDialog.setMax((int) (total / 1024));
				tDialog.show();
				tDialog.setProgress((int) (current / 1024));

			}

			@Override
			public void onCancelled() {
				// 取消下载 super.onCancelled();

				System.out.println("取消下载");
				Toast.makeText(SplashActivity.this, "取消下载", Toast.LENGTH_SHORT).show();
				tDialog.dismiss();

			}

			public void onSuccess(ResponseInfo<File> arg0) {
				// 下载成功
				System.out.println("下载成功:" + arg0.result.getPath());
				Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
				tDialog.dismiss();
				openFile(arg0.result);

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// 下载失败
				Toast.makeText(SplashActivity.this,
						"下载失败原因：" + arg0.getExceptionCode(), Toast.LENGTH_SHORT).show();
				tDialog.dismiss();

			}

		});

	}




	/**
	 * 打开安装包
	 *
	 * @param file
	 */
	private void openFile(File file) {
		// TODO Auto-generated method stub
		Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	// --------------------------------------

	/**
	 * 版本名称比较
	 */
	public boolean maxVersions(String max) {
		boolean compare = false;
		// 当前版本号1
		long dqone = 0;
		// 当前版本号2
		long dqtwo = 0;

		// 最高版本号1   jj
		long zgone = 0;
		// 最高版本号2
		long zgtwo = 0;

		String dqString = getVersionName(this);

		dqone = Long.parseLong(dqString.subSequence(9, 17).toString());
		dqtwo = Long.parseLong(dqString.subSequence(dqString.length() - 1,
				dqString.length()).toString());

		zgone = Long.parseLong(max.subSequence(4, 12).toString());
		zgtwo = Long.parseLong(max.subSequence(max.length() - 1, max.length())
				.toString());

		Log.d("ANXU", dqone + "-" + zgone + "-" + dqtwo + "-" + zgtwo);

		if (dqone < zgone || (dqone == zgone && dqtwo < zgtwo) ) {
			compare = true;
		}

		return compare;

	}

	/**
	 * 最低版本号
	 *
	 * @param
	 * @return
	 */
	public boolean minVersions(String min) {
		boolean compare = false;
		// 当前版本号1
		long dqone = 0;
		// 当前版本号2
		long dqtwo = 0;
		// 最低版本号1
		long zdone = 0;
		// 最低版本号2
		long zdtwo = 0;

		String dqString = getVersionName(this);
		dqone = Long.parseLong(dqString.subSequence(9, 17).toString());

		dqtwo = Long.parseLong(dqString.subSequence(dqString.length() - 1,
				dqString.length()).toString());

		zdone = Long.parseLong(min.subSequence(4, 12).toString());
		zdtwo = Long.parseLong(min.subSequence(min.length() - 1, min.length())
				.toString());

		Log.d("ANXU", dqone + "-" + zdone + "-" + dqtwo + "-" + zdtwo);

		// 当前大于或者等于最低就是 有 否
		// [ 注：最低和当前一致的时候，需不需要强制更新 (暂存的小问题) ]

		if (dqone < zdone) {
			compare = false;
		} else if (dqone == zdone) {
			if (dqtwo <= zdtwo) {
				compare = false;
			} else {
				compare = true;
			}
		} else {
			compare = true;
		}

		return compare;

	}

	// -----------------------------------------------

	// 版本名
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(),0);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}


	/**
	 * 获取当前应用程序的版本号名称
	 */
	private String getVersion() {
		String st = getResources().getString(R.string.Version_number_is_wrong);
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packinfo.versionName;
			return version;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return st;
		}
	}

	/**
	 * 文件是否已下载
	 *
	 * @param apkname
	 * @return
	 */
	public boolean fileIsExists(String apkname) {

		String path = getApkDownloadPath() + apkname;

		boolean exists = true;
		File file;
		try {
			file = new File(path);
			if (!file.exists()) {
				exists = false;
				return exists;
			}

		} catch (Exception e) {
			// TODO: handle exception
			exists = false;
			return exists;
		}
		if (exists) {
			if (file != null) {
				openFile(file);
			}
		}
		return exists;
	}



/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
*/


}
