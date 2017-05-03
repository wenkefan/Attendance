package hylk.com.xiaochekaoqin.activity;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

//import com.iflytek.cloud.SpeechConstant;
//import com.iflytek.cloud.SpeechError;
//import com.iflytek.cloud.SpeechSynthesizer;
//import com.iflytek.cloud.SynthesizerListener;
//import com.softwinner.Gpio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import hylk.com.xiaochekaoqin.global.Constants;
import hylk.com.xiaochekaoqin.utils.PrefUtils;


/**
 * 
 * @author: _wzz
 * @time:   2016-5-6 上午9:56:20
 * @version 1.0
 * @Description: 让此类继承SerialPortChat，为了获取返回的体温，并设为抽象类，具体实现由MainActivity来实现
 */
public class SerialPortChat {
//	public Context context;
//
//	/**  串口  */
//	protected static final String TAG = "wzz----";
//	protected OutputStream mOutputStream;
//	private InputStream mInputStream;
//
//	public SpeechSynthesizer mTts;
//
//	public SerialPortChat(Context context){
//		this.context = context;
//		mTts = SpeechSynthesizer.createSynthesizer(context, null);  // 语音播报
//	}
//
//	// --------------------------分班播报-----------------------------------------------------
//
//	private String ttyPath = "/dev/ttyS3";
//	public static OutputStream mOutputStream1;
//	public static InputStream mInputStream1;
//
//
////	public boolean mIfFenBanBoBao ;  // 是否启用分班播报功能
//
//
//	/** 播放内容，班级id，考勤方向 */
//	public void FenBanBoBao(final String name, final int classInfoID , int AttendanceDirection){
//
//		// 盒子
//		final int box = PrefUtils.getInt(context, classInfoID + "", -1);  // 没配置的话盒子为-1
//		if (box == -1){
//			return;
//		}
//
//		String speakText = "";
//		if (AttendanceDirection == 1){  // 入园
//			speakText = PrefUtils.getString(context,Constants.RuYuanOne,"欢迎") + name + PrefUtils.getString(context,Constants.RuYuanTwo,"入园");
//		}else { // 离园
//			speakText = PrefUtils.getString(context,Constants.LiYuanOne,"") + name + PrefUtils.getString(context,Constants.LiYuanTwo,"再见");
//		}
//
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				// 进入配置模式(配置信道前必须点击)
//				goSetting();
//			}
//		},0);
//
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				// 设置信道
//				setXinDao(box);
//			}
//		},100);
//
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				// 从配置模式转到发射模式
//				SetToFaShe();
//			}
//		},300);
//
//		final String finalSpeakText = speakText;
//
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				// 发送
//				sendMessage(finalSpeakText);
//			}
//		},500);
//
//
//
//	}
//
//
//	final Handler handler = new Handler(){
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//		}
//	};
//
//
//	protected  void goSetting(){
//
//		Gpio.writeGpio('L', 11, 1); //power pin
//		//Gpio.writeGpio('L', 8, 1); //PA power pin
//		Gpio.writeGpio('L', 9, 0);  //enable pin
//		Gpio.writeGpio('H', 9, 0);  //CFG pin
//
//	}
//
//	protected  void setXinDao(int box){
//
//		String text = "AT+CS="+Integer.toHexString(box).toUpperCase()+"\r\n";
//
//		Log.e("nick", "--------text="+text);
//
//		byte[] bytes;
//		try {
//			bytes = text.getBytes("GB2312");
//			try {
//				mOutputStream1.write(bytes);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//	}
//
//	protected  void SetToFaShe(){
//
//		Gpio.writeGpio('L', 11, 1); //power pin
//		//Gpio.writeGpio('L', 8, 1); //PA power pin
//		Gpio.writeGpio('L', 9, 0);  //enable pin
//		Gpio.writeGpio('H', 9, 1);  //CFG pin
//
//	}
//
//	public void sendMessage(String text)
//	{
//		byte[] bytes;
//		try {
//			bytes = text.getBytes("GB2312");
//			int size = bytes.length+2;
//			byte h = (byte)((size & 0xff00)>>8);
//			byte l = (byte)(size&0xff);
//			byte[] head = {(byte)0xfd,h,l,0x01,0x00};
//			byte[] send = arraycat(head,bytes);
//
//			try {
//				mOutputStream1.write(send);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}
//
//
//	byte[] arraycat(byte[] buf1,byte[] buf2)
//	{
//		byte[] bufret=null;
//		int len1=0;
//		int len2=0;
//		if(buf1!=null)
//			len1=buf1.length;
//		if(buf2!=null)
//			len2=buf2.length;
//		if(len1+len2>0)
//			bufret=new byte[len1+len2];
//		if(len1>0)
//			System.arraycopy(buf1,0,bufret,0,len1);
//		if(len2>0)
//			System.arraycopy(buf2,0,bufret,len1,len2);
//		return bufret;
//	}
//
//
////
////	class myThread extends Thread{/**/
////
////		@Override
////		public void run() {
////			// TODO Auto-generated method stub
////			super.run();
////
////			while(mIfFenBanBoBao){
////				try {
////
////					readFormMcu();  // 开启读取数据，如果有响应，就setText
////
////				} catch (Exception e) {
////					e.printStackTrace();
////				}
////			}
////		}
////
////	}
//
////	private byte[] readFormMcu(){
////
////		byte[] buffer = new byte[128];
////
////		if(mInputStream1 != null){
////			try {
////
////				int size = mInputStream1.read(buffer);
////
////				//Log.d(TAG, "readFormNfc.....");
////				for(int i=0; i<buffer.length; i++){
////					Log.d("nick-----", "buffer[" + i + "] = " + buffer[i]);
////				}
////
////				if (size > 0 ){
//////					Message message = new Message();
//////					message.what = 1;
//////					message.obj = new String(buffer,"GB2312");
////////					mHandler.sendMessage(message);
//////
//////					ToastUtil.show(SerialPortChat.this,new String(buffer,"GB2312") + "----");
////				}
////
////				return buffer;
////				//mInputStream.close();
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		}else {
////			//Log.e("nick", "mInputStream:--------null");
////		}
////		return null;
////	}
//
//
//	// -------------------------------------------------------------------------------
//
//
//
//	/////////////////////////////////////////////////////////////
//
//	/**
//	 * 语音播报设置 第一个字段Speechsynthesizer对象;第二个字段为是否保存音频,默认为false;第三个字段为音频保存的位置;
//	 * 第四个字段为要播报的内容
//	 *
//	 *       isSave     是否保存音频 默认为false
//	 *       src
//	 *            音频保存的位置
//	 * @param text
//	 *            播报的内容
//	 */
////	TextToSpeech(mTts, false, null,"进入主页面");
//	// by _wzz
//	public void speech(String text) {
//		// 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
//		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");// 设置发音人xiaoyan  vixq   vixyun    vinn
//		mTts.setParameter(SpeechConstant.SPEED, "60");// 设置语速
//		mTts.setParameter(SpeechConstant.VOLUME, "100");// 设置音量，范围0~100
//		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
//		// 设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
//		// 保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
//		// 如果不需要保存合成音频，注释该行代码
//		if (false) {
//			mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, null);
//		}
//		// 3.开始合成
//		mTts.startSpeaking(text, mSynListener);
//	}
//
//
//
//	// 合成监听器
//	private SynthesizerListener mSynListener = new SynthesizerListener() {
//		// 会话结束回调接口，没有错误时，error为null
//		public void onCompleted(SpeechError error) {
//			if (error == null) {
//				System.out.println("播放完成error---");
//			} else if (error != null) {
//				System.out.println("ddd---"+error.getPlainDescription(true));
//			}
//		}
//
//		// 缓冲进度回调
//		// percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
//		public void onBufferProgress(int percent, int beginPos, int endPos,
//									 String info) {
//		}
//
//		// 开始播放
//		public void onSpeakBegin() {
//		}
//
//		// 暂停播放
//		public void onSpeakPaused() {
//		}
//
//		// 播放进度回调
//		// percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
//		public void onSpeakProgress(int percent, int beginPos, int endPos) {
//		}
//
//		// 恢复播放回调接口
//		public void onSpeakResumed() {
//		}
//
//		// 会话事件回调接口
//		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
//		}
//	};
//


}
