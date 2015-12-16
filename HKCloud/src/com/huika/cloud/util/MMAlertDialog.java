package com.huika.cloud.util;

import com.huika.cloud.R;
import com.huika.cloud.control.me.activity.WithdrawApplicationActivity;
import com.huika.cloud.views.passwordview.GridPasswordView;
import com.huika.cloud.views.passwordview.GridPasswordView.OnPasswordChangedListener;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MMAlertDialog {
	
	public interface DialogOnItemClickListener {
		void onItemClickListener(View v, int position);
		void onDialogDismiss(String psw);
	}
	// begin---底部弹出宽，类似苹果的//////////////////////////////////////////
	public static Dialog createShowAlert(final Context context, int layoutId) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(layoutId, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		// set a large value put it in bottom
		final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;// 改变显示位置
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(layout);
		return dlg;
	}
	
	/**
	 * 系统更新提示对话框
	 * @param context
	 * @param contentResId
	 * @param leftClick
	 * @param rightClick
	 * @return
	 */
	public static Dialog createSoftwareUpdate(final Context context,String content, final View.OnClickListener leftClick, final View.OnClickListener rightClick) {
		final Dialog dlg = new Dialog(context, R.style.IsDelDialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_exit, null); 
		TextView contentTv = (TextView) view.findViewById(R.id.tv_title_de);
		contentTv.setText(content);
		Button okBtn = (Button) view.findViewById(R.id.btn_affirm_de);
		Button cancel = (Button) view.findViewById(R.id.btn_cancel_de);
		// 确定实名认证
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				if (null != rightClick) {
					rightClick.onClick(v);
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				if (leftClick != null) leftClick.onClick(v);
			}
		});
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(view);
		return dlg;
	}
	/**
	 * @description：删除缓存
	 * @author shicm
	 * @date 2015-11-20 上午11:54:03
	 */
	public static Dialog ClearcacheDate(final Context context,String content, final View.OnClickListener leftClick, final View.OnClickListener rightClick) {
		final Dialog dlg = new Dialog(context, R.style.IsDelDialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_clearcache, null); 
		TextView contentTv = (TextView) view.findViewById(R.id.tv_title_de);
		contentTv.setText(content);
		Button okBtn = (Button) view.findViewById(R.id.btn_affirm_de);
		Button cancel = (Button) view.findViewById(R.id.btn_cancel_de);
		// 确定实名认证
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				if (null != rightClick) {
					rightClick.onClick(v);
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				if (leftClick != null) leftClick.onClick(v);
			}
		});
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(view);
		return dlg;
	}
	
	/**拨打电话*/
	public static Dialog createPrivateDialogExitDialog(final Context context, int fCStrId, int sCStrId, final DialogOnItemClickListener onItemClickListener) {
		final Dialog dlg = createShowAlert(context, R.layout.dialog_notitle_tel);
		dlg.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
		TextView item_first = (TextView) dlg.findViewById(R.id.item_first);
		item_first.setText(fCStrId);
		TextView item_second = (TextView) dlg.findViewById(R.id.item_second);
		item_second.setText(sCStrId);
		item_second.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
				if (onItemClickListener != null)
					onItemClickListener.onItemClickListener(v, 0);
			}
		});
		
		return dlg;
	}

	/**
	 * 
	 * @description：支付、提现输入密码弹框
	 * @param context
	 * @param onItemClickListener
	 * @param isPay 支付true 提现false
	 * @param withdraw_count 提现金额 
	 * @author ht
	 * @date 2015-11-30 下午12:10:13
	 */
	public static Dialog createPwdDialog(final Context context,final DialogOnItemClickListener onItemClickListener,boolean isPay, String withdraw_count) {
		final Dialog dlg = new Dialog(context, R.style.MMTheme);
		View view=View.inflate(context, R.layout.with_draw_warn, null);
		GridPasswordView gpwv = (GridPasswordView) view.findViewById(R.id.gpwv);
		TextView forget_pwd = (TextView) view.findViewById(R.id.forget_pwd);
		ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
		TextView with_draw_money = (TextView) view.findViewById(R.id.with_draw_money);
		TextView tv_withdraw= (TextView) view.findViewById(R.id.tv_withdraw);
		if(isPay){
			//支付
			with_draw_money.setVisibility(View.GONE);
			tv_withdraw.setVisibility(View.GONE);
		}else{
			with_draw_money.setText(withdraw_count);
		}
		//设置输入密码监听
		gpwv.setOnPasswordChangedListener(new OnPasswordChangedListener() {
			@Override
			public void onMaxLength(String psw) {
				//输入到最大密码框长度时，请求网络
				dlg.dismiss();
				//发送提现申请要求
				onItemClickListener.onDialogDismiss(psw);
			}
			@Override
			public void onChanged(String psw) {
				
			}
		});
		//关闭
		iv_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
		//忘记密码
		forget_pwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onItemClickListener.onItemClickListener(v,0);
			}
		});
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(view);
		return dlg;
	}
	/**
	 * @description：更改支付密码
	 * @author shicm
	 * @date 2015-12-7 下午4:18:30
	 */
	public static Dialog createUpPwdDialog(final Context context,final DialogOnItemClickListener onItemClickListener) {
		final Dialog dlg = new Dialog(context, R.style.MMTheme);
		View view=View.inflate(context, R.layout.account_update_pay_pwd, null);
		GridPasswordView gpwv = (GridPasswordView) view.findViewById(R.id.update_pwd_grv);
		//设置输入密码监听
		gpwv.setOnPasswordChangedListener(new OnPasswordChangedListener() {
			@Override
			public void onMaxLength(String psw) {
				dlg.dismiss();
				onItemClickListener.onDialogDismiss(psw);
			}
			@Override
			public void onChanged(String psw) {
				
			}
		});
		
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(view);
		return dlg;
	}
	
	
	public static Dialog createShowCenterDialog(final Context context, String titleStr, String contentStr, String leftStr, String rightStr, final View.OnClickListener leftClick, final View.OnClickListener rightClick) {
		final Dialog dlg = createCenterStructureDialog(context, titleStr, leftStr, rightStr, leftClick, rightClick);

		TextView content = (TextView) dlg.findViewById(R.id.tip_content);
		if (TextUtils.isEmpty(contentStr)) {
			content.setVisibility(View.GONE);
		}
		content.setText(contentStr);
		return dlg;
	}
		
	public static Dialog createCenterStructureDialog(final Context context, String titleStr, String leftStr, String rightStr, final View.OnClickListener leftClick, final View.OnClickListener rightClick) {
		final Dialog dlg = new Dialog(context, R.style.MMTheme);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_tips, null);
		TextView title = (TextView) layout.findViewById(R.id.tip_title);
		title.setText(titleStr);
		title.setVisibility(TextUtils.isEmpty(titleStr)?View.GONE:View.VISIBLE);
		Button leftBtn = (Button) layout.findViewById(R.id.tip_left_btn);
		leftBtn.setText(leftStr);
		leftBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.dismiss();
				if (leftClick != null)
					leftClick.onClick(v);
			}
		});

		Button rightBtn = (Button) layout.findViewById(R.id.tip_right_btn);
		rightBtn.setText(rightStr);
		rightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.dismiss();
				if (rightClick != null)
					rightClick.onClick(v);
			}
		});

		// set a large value put it in bottom
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(layout);
		return dlg;
	}
	
	/**中间有提示信息的弹框*/
	public static Dialog createCenterWarnDialog(final Context context, String titleStr, final View.OnClickListener closeClick) {
		final Dialog dlg = new Dialog(context, R.style.MMTheme);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.center_warn, null);
		TextView title = (TextView) layout.findViewById(R.id.tip_title);
		title.setText(titleStr);
		title.setVisibility(TextUtils.isEmpty(titleStr)?View.GONE:View.VISIBLE);
		
		ImageView closeBt = (ImageView)layout.findViewById(R.id.iv_close);
		closeBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.dismiss();
				if (closeClick != null)
					closeClick.onClick(v);
			}
		});
		dlg.setCanceledOnTouchOutside(false);
		dlg.setContentView(layout);
		return dlg;
	}
}
