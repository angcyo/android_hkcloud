package com.zhoukl.androidRDP.RdpUtils.help;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhoukl.androidRDP.R;


/**
 * @description：自定义ToastMsg，选择系统或自定义的；
 * @author samy
 * @date 2014年9月30日 下午3:54:52
 */
public class ToastMsg {
	private static Toast toast;// 只有一个toast
	private static Boolean isDefine = false;

	/**
	 * @description：默认模式用系统的显示
	 * @author samy
	 * @date 2014年9月30日 下午4:08:22
	 */
	public static void showToastMsg(Context ctx, String msg) {
		showToastMsg(ctx, msg, isDefine);
	}

	public static void showToastMsg(Context ctx, int strId) {
		showToastMsg(ctx, ctx.getResources().getString(strId), isDefine);
	}

	public static void showToastMsg(Context ctx, int strId, Boolean isDefine) {
		showToastMsg(ctx, ctx.getResources().getString(strId), isDefine);
	}

	public static void showToastMsg(Context ctx, String msg, Boolean isDefine) {
		if (null != toast) {
			closeToastMsg();
		}
		if (isDefine) {
			toast = new Toast(ctx);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			View toastRoot = LayoutInflater.from(ctx).inflate(R.layout.toast_text_item, null);
			((TextView) toastRoot.findViewById(R.id.toast_text)).setText(msg);
			toast.setView(toastRoot);
			toast.show();
		}
		else {
			Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
		}
	}

	public static void closeToastMsg() {
		toast.cancel();
		toast = null;
	}
}
