package hylk.com.xiaochekaoqin.utils;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import hylk.com.xiaochekaoqin.bean.AttendanceRecord;
import hylk.com.xiaochekaoqin.bean.Child;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by _wzz on 2016/8/2 10:00.
 */
public class OkHttpUtil {

	public static OkHttpUtil instance;


	private static OkHttpClient client;
	private Activity mActivity;

	private OnPostListener postListener;
	private OnGETListener getListener;


	public static OkHttpUtil getInstance(){

		if (instance == null){
			synchronized (OkHttpUtil.class){
				if (instance == null){
					instance = new OkHttpUtil() ;
				}

			}
		}
		return instance ;
	}


	public OkHttpUtil(){

		/** OKHttp 2.x的超时设置 */
//		if (client == null) {
//			client = new OkHttpClient();
//			client.newBuilder().connectTimeout(60, TimeUnit.SECONDS);  // 全局的连接超时时间
//			client.newBuilder().readTimeout(60, TimeUnit.SECONDS);  // 全局的读取超时时间
//			client.newBuilder().writeTimeout(60, TimeUnit.SECONDS); // 全局的写入超时时间
//		}

//		和OkHttp2.x有区别的是不能通过OkHttpClient直接设置超时时间和缓存了，而是通过OkHttpClient.Builder来设置，通过builder配置好OkHttpClient后用builder.build()来返回OkHttpClient，所以我们通常不会调用new OkHttpClient()来得到OkHttpClient，而是通过builder.build()：

		/** OKHttp 3.x的超时设置 */
//		File sdcache = mActivity.getApplicationContext().getExternalCacheDir();
//		int cacheSize = 10 * 1024 * 1024;

		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS);
//				.cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));

		client = builder.build();

	}


	public interface OnGETListener {

		 void onFail();

		 <T> void onSuccess(String json, T cla, int tag);

		void onSuccess(String json, int tag);
	}


	public void setGETListener(OnGETListener getListener) {
		this.getListener = getListener;
	}


	public interface OnPostListener {

		void onFail(int tag, AttendanceRecord attendanceRecord, Child child1);

		void onSuccess(int tag, Child bean, String json);
	}


	public void setPostListener(OnPostListener postListener) {
		this.postListener = postListener;
	}

	/**
	 * GET请求
	 */
	public void getRequestJson(String url , final int tag) {


		Request request = new Request.Builder().get().url(url).build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {

				String json = response.body().string();

				getListener.onSuccess(json,tag);

			}

			@Override
			public void onFailure(Call call, IOException e) {

				getListener.onFail();

			}

		});

	}



	/**
	 * POST请求  根据tag来判断是哪个post请求
	 */
	public void PostRequest2(String POST_URL, final int tag, final Child child1, RequestBody requestBodyPost, final AttendanceRecord attendanceRecord) {

//		RequestBody requestBodyPost = new FormBody.Builder()
//				.add("page", "1")
//				.add("code", "news")
//				.add("pageSize", "20")
//				.add("parentid", "0")
//				.add("type", "1")
//				.build();

		Request requestPost = new Request.Builder()
				.url(POST_URL)
				.post(requestBodyPost)
				.build();


		Call call = client.newCall(requestPost);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

				if (postListener != null){
					postListener.onFail(tag , attendanceRecord,child1);
				}
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				/**
				 * 通过拿到response这个响应请求，然后通过body().string(),拿到请求到的数据
				 * 这里最好用string()  而不要用toString（）
				 * toString（）每个类都有的，是把对象转换为字符串
				 * string（）是把流转为字符串
				 */
				final String string = response.body().string();
				Log.d("--------",string);
				if (postListener != null){
					postListener.onSuccess(tag,child1,string);
				}

			}
		});

	}

	public static final String TYPE = "application/octet-stream";



}


/**
 * MultipartBody body = new MultipartBody.Builder("AaB03x")
 * .setType(MultipartBody.FORM)
 * .addFormDataPart("files", null, new MultipartBody.Builder("BbC04y")
 * .addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
 * RequestBody.create(MediaType.parse("image/png"), new File("/storage/emulated/0/lkcj/imgCamera/IMG_20160809_095612.png")))
 * .build())
 * .build();
 * <p/>
 * Request requestPostFile = new Request.Builder()
 * .url("http://10.11.64.50/upload/UploadServlet")
 * .post(body)
 * .build();
 */

