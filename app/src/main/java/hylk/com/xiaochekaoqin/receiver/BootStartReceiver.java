package hylk.com.xiaochekaoqin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hylk.com.xiaochekaoqin.activity.SplashActivity;
import hylk.com.xiaochekaoqin.utils.ToastUtil;
import hylk.com.xiaochekaoqin.utils.UIUtils;

/**
 *  开机自启广播
 * Created by _wzz on 2016/5/16 09:34.
 * 		注：
 * 		1.应用安装到了sd卡内，安装在sd卡内的应用是收不到BOOT_COMPLETED广播的
 * 		2.应用程序安装后重来没有启动过，这种情况下应用程序接收不到任何广播，包括BOOT_COMPLETED、ACTION_PACKAGE_ADDED、CONNECTIVITY_ACTION等等。
 *
 * 	我们可以通过

 adb shell am broadcast -a android.intent.action.BOOT_COMPLETED

 命令发送BOOT_COMPLETED广播，而不用重启测试机或模拟器来测试BOOT_COMPLETED广播

 */
public class BootStartReceiver extends BroadcastReceiver {

	public static final String action_boot="android.intent.action.BOOT_COMPLETED";


	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction().toString();

		if (action.equals(action_boot)){

//			ToastUtil.showLong(UIUtils.getContext(),"开机广播");
//			Intent ootStartIntent = new Intent(context,SplashActivity.class);
//			ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(ootStartIntent);
			
		}
	}
}
