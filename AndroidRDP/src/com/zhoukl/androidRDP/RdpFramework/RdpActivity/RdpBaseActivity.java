package com.zhoukl.androidRDP.RdpFramework.RdpActivity;

import com.umeng.analytics.MobclickAgent;
import com.zhoukl.androidRDP.R;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandFailedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandSuccessedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivityManager;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpApplication;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpConstant;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

import fr.castorflex.android.loadingview.LoadingAndErrorOverlayLayout;
import fr.castorflex.android.loadingview.LoadingRainBowDialog;
import fr.castorflex.android.loadingview.overlay.OverlayLayout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @description：项目开发中所有Activity的基类
 * @author zhoukl
 * @date 2014年11月12日 上午10:25:32
 */
public class RdpBaseActivity extends RdpActivity implements OnClickListener, OnCommandSuccessedListener, OnCommandFailedListener, ISkipActivity, OnCancelListener {
	protected final int TBAR_FUNC_BACK = 1;
	protected RdpApplication mApplication;

	protected LayoutInflater mLayoutInflater;
	public LayoutInflater inflater;
	protected TextView mTvTitle;
	protected LinearLayout mLltTitleBarLeftArea, mLltTitleBarRigthArea;

	protected LinearLayout mLltHeaderArea;
	protected LinearLayout mLltMasterArea;
	protected LinearLayout mLltFooterArea;

	public LoadingRainBowDialog loadingDialog;

	// 是否允许全屏
	private boolean mAllowFullScreen = false;

	public RdpBaseActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// notifiyApplicationActivityCreating();
		if (mAllowFullScreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		mApplication = (RdpApplication) getApplication();
		onBeforeCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		// getTAApplication().getAppManager().addActivity(this);
		// 把activity加入栈中
		RdpActivityManager.create().addActivity(this);

		onBeforeInitActivity();
		initActivity();
		onAfterInitActivity(savedInstanceState);
		// notifiyApplicationActivityCreated();

		IntentFilter filter = new IntentFilter(RdpConstant.BROADCAST_LOGIN);
		registerReceiver(mLoginReceiver, filter);
	}

	protected void onBeforeCreate(Bundle savedInstanceState) {
		// getTAApplication().registerCommand(TAIDENTITYCOMMAND,TAIdentityCommand.class);

	}

	/**
	 * @description：实话标题的控件
	 * @date 2015-10-12 上午9:56:02
	 */
	protected void onBeforeInitActivity() {
		inflater = LayoutInflater.from(this);
		mLayoutInflater = getLayoutInflater();
		setContentView(getBaseLayoutID());
		mTvTitle = (TextView) findViewById(R.id.tvTitle);
		mLltTitleBarLeftArea = (LinearLayout) findViewById(R.id.lltTitleBarLeftArea); // 左边
		mLltTitleBarRigthArea = (LinearLayout) findViewById(R.id.lltTitleBarRigthArea); // 右边
		addLeftFuncView(R.drawable.common_head_back_select, this, TBAR_FUNC_BACK);

		mLltHeaderArea = (LinearLayout) findViewById(R.id.lltHeaderArea); // 标题
		mLltMasterArea = (LinearLayout) findViewById(R.id.lltMasterArea); // 中间内容
		mLltFooterArea = (LinearLayout) findViewById(R.id.lltFooterArea); // 底部

	}

	protected void initActivity() {
	}

	protected void onAfterInitActivity(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	}

	protected int getBaseLayoutID() {
		return R.layout.rdp_base_activity;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName()); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName()); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mLoginReceiver);
		super.onDestroy();
		RdpActivityManager.create().finishActivity(this);
	}

	protected void setFuncTitle(int titleResID) {
		setFuncTitle(getString(titleResID));
	}

	protected void setFuncTitle(String title) {
		mTvTitle.setText(title);
	}

	protected TextView addRightFuncTextView(String text, OnClickListener listener, int funcViewTag) {
		return addFuncTextView(text, listener, funcViewTag, mLltTitleBarRigthArea);
	}

	private TextView addFuncTextView(String text, OnClickListener listener, int funcViewTag, LinearLayout parent) {
		TextView textView = new TextView(this.getApplicationContext());
		textView.setTag(funcViewTag);
		textView.setText(text);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		// params.setMargins(20, 0, 40, 0);

		textView.setLayoutParams(params);
		textView.setPadding(20, 0, 40, 0);
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(getResources().getColor(android.R.color.white));
		// if (drawableResID != 0)
		// textView.setBackgroundResource(drawableResID);
		// textView.setBackgroundResource(R.drawable.common_head_btn_selector);
		parent.addView(textView);
		if (listener != null) textView.setOnClickListener(listener);
		return textView;
	}

	protected void addLeftFuncView(int drawableResID, OnClickListener listener, int funcViewTag) {
		addLeftFunc(drawableResID, listener, funcViewTag, mLltTitleBarLeftArea);
	}

	protected void removeLeftFuncView(int funcViewTag) {
		removeFuncView(funcViewTag, mLltTitleBarLeftArea);
	}

	protected void addRightFuncView(int drawableResID, OnClickListener listener, int funcViewTag) {
		addLeftFunc(drawableResID, listener, funcViewTag, mLltTitleBarRigthArea);
	}

	protected void removeRightFuncView(int funcViewTag) {
		removeFuncView(funcViewTag, mLltTitleBarRigthArea);
	}

	/**
	 * @description：添加左边图片
	 * @author shicm
	 * @date 2015-11-9 上午9:21:42
	 */
	private void addLeftFunc(int drawableResID, OnClickListener listener, int funcViewTag, LinearLayout parent) {
		ImageButton textView = new ImageButton(this.getApplicationContext());
		textView.setTag(funcViewTag);
		if (drawableResID != 0) textView.setImageResource(drawableResID);
		textView.setBackgroundResource(R.drawable.common_head_btn_selector);
		parent.addView(textView);
		if (listener != null) textView.setOnClickListener(listener);
	}

	private void removeFuncView(int funcViewTag, LinearLayout parent) {
		View view = parent.findViewWithTag(funcViewTag);
		if (view != null) parent.removeView(view);
	}

	protected View addHeaderView(int resLayoutID) {
		return mLayoutInflater.inflate(resLayoutID, mLltHeaderArea);
	}

	protected View addMasterView(int resLayoutID) {
		return mLayoutInflater.inflate(resLayoutID, mLltMasterArea);
	}

	protected View addFooterView(int resLayoutID) {
		return mLayoutInflater.inflate(resLayoutID, mLltFooterArea);
	}

	public void onLoginStatusChanged(boolean loginStatus) {

	}

	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		// TODO Auto-generated method stub

	}

	/**
	 * 请求执行失败的基类处理规则
	 */
	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		// TODO Auto-generated method stub

	}

	public void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	@Override
	public void onClick(View v) {
		Object tag = v.getTag();
		if (tag != null) {
			switch ((Integer) tag) {
				case TBAR_FUNC_BACK:
					finish();
					return;

				default:
					break;
			}
		}
	}

	protected boolean isLogin() {
		return isLogin(true);
	}

	public boolean isLogin(boolean jumpToLoginActivity) {
		return mApplication.isLogin(jumpToLoginActivity);
	}

	public String getMemberId() {
		return getMemberId(true);
	}

	public String getMemberId(boolean jumpToLoginActivity) {
		return mApplication.getMemberId(jumpToLoginActivity);
	}

	public void setViewVisible(View view, boolean value) {
		if (value) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}

	public void startActivity(Class<?> cls, boolean finishSelf) {
		startActivity(cls, null, finishSelf);
	}

	public void startActivity(Class<?> cls, Bundle bundle) {
		startActivity(cls, bundle, false);
	}

	public void startActivity(Class<?> cls, Bundle bundle, boolean finishSelf) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) intent.putExtras(bundle);
		startActivity(intent);
		if (finishSelf) finish();
	}

	private BroadcastReceiver mLoginReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			onLoginStatusChanged(isLogin(false));
		}
	};

	/**
	 * 调此方法可以显示错误页面 targetView 设置overlay替换的view reloadListener 不要用匿名内部类
	 * 点击事件监听的id=R.id.click_reload 点击重新加载
	 */
	private LoadingAndErrorOverlayLayout overLay;
	private OverlayLayout mEmtyOverlay;
	private View commonEmtyOverlay;

	// ------第一次加载覆盖页
	protected void showLoadingOverLay(View targetView) {
		if (overLay == null) overLay = new LoadingAndErrorOverlayLayout(this, targetView);
		overLay.showLoadingOverLay();
	}

	/** 显示的加载dialog页面，等在获取信息的提示dialog **/
	protected void showErrorLayView(View targetView, View.OnClickListener reloadListener) {
		if (overLay == null) overLay = new LoadingAndErrorOverlayLayout(this, targetView);
		overLay.setReloadClick(reloadListener);
		overLay.showErrorLayView();
	}

	/** 隐藏覆盖页 加载和错误的覆盖都是调同一个方法 */
	protected void hideOverLayView() {
		if (overLay != null) overLay.hideOverlay();
	}

	/** 显示数据空页面 : targetView为设置overlay依附的view,resid为提示信息资源id */
	public void showEmptyLayView(View targetView, int resid) {
		if (mEmtyOverlay == null) {
			mEmtyOverlay = new OverlayLayout(this);
			mEmtyOverlay.attachTo(targetView);
			commonEmtyOverlay = mLayoutInflater.inflate(R.layout.layout_emty_overlay, null);
			TextView empt_tv = (TextView) commonEmtyOverlay.findViewById(R.id.empt_tv);
			empt_tv.setText(resid);
			mEmtyOverlay.setOverlayView(commonEmtyOverlay);
		}
		mEmtyOverlay.showOverlay();
	}

	/** 影藏数据空页面 */
	public void hideEmptyLayView() {
		if (mEmtyOverlay != null) mEmtyOverlay.hideOverlay();
	}

	/*****************************跳转start*********************************/
	@Override
	public void skipActivity(Activity aty, Class<?> cls) {
		showActivity(aty, cls);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Intent it) {
		showActivity(aty, it);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
		showActivity(aty, cls, extras);
		aty.finish();
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Intent it) {
		aty.startActivity(it);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
		Intent intent = new Intent();
		intent.putExtras(extras);
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	/*****************************跳转end*********************************/

	/****************************弹出框start*******************************/

	public void showToastMsg(String msg) {
		ToastMsg.showToastMsg(this, msg);
	}

	public void showToastMsg(int strId) {
		ToastMsg.showToastMsg(this, strId);
	}

	/****************************弹出框end*******************************/

	/*********************dialog start*****************/
	protected void initLoadingDialog(boolean isTrans) {
		if (null == loadingDialog) {
			loadingDialog = new LoadingRainBowDialog(this, isTrans);
			// loadingDialog = new LoadingDialog(this, isTrans);
			loadingDialog.setOnCancelListener(this);
		}
	}

	public void showLoadingDialog(String parameter) {
		initLoadingDialog(false);// 透明
		loadingDialog.setTitle(parameter);
		if (!loadingDialog.isShowing()) loadingDialog.show();
	}

	public void showLoadingDialog(boolean isTrans) {
		initLoadingDialog(isTrans);// 透明
		// loadingDialog.setTitle(parameter);
		if (!loadingDialog.isShowing()) loadingDialog.show();
	}

	public void dismissLoadingDialog() {
		if (null != loadingDialog) {
			LoadingRainBowDialog.dismissDialog(loadingDialog);
			// LoadingDialog.dismissDialog(loadingDialog);
		}
	}

	/*********************dialog end*****************/
	/**
	 * loadingdialog取消监听
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
	}

	public void setAllowFullScreen(boolean allowFullScreen) {
		this.mAllowFullScreen = allowFullScreen;
	}
}
