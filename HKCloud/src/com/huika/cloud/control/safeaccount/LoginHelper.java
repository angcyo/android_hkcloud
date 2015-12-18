package com.huika.cloud.control.safeaccount;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.support.event.CartChangeEvent;
import com.huika.cloud.support.event.LoginEvent;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.PreferHelper;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandFailedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandSuccessedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpUtils.MD5Security;

import java.lang.reflect.Type;

import de.greenrobot.event.EventBus;

/**
 * @description：登录帮助类
 * @author shicm
 * @date 2015-11-9 下午2:44:19
 */
public class LoginHelper implements  OnCommandSuccessedListener,OnCommandFailedListener{
	private static LoginHelper loginHelper;
	//	private String mVersion;
	RdpNetCommand rdpNetCommand;
	OnLoginSuccessListener isLoginSuccess;
	private Context context;
	private String mAccount;
	private String mPassword;

	private LoginHelper(Context context) {
		super();
		this.context = context;

	}

	public static synchronized LoginHelper getInstance(Context context) {
		if (loginHelper == null) {
			loginHelper = new LoginHelper(context);
		}
		return loginHelper;
	}

	// 失败
	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
	    EventBus.getDefault().post(new LoginEvent(false, result.getMsg()));
		if (isLoginSuccess != null) {
			isLoginSuccess.getLoginUserInfoUpdateUi();
		}
	}

	// 成功
	@SuppressWarnings("static-access")
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		UserModel mUser = (UserModel) data;
		HKCloudApplication.getInstance().setCurrUser(mUser);
		PreferHelper.getInstance().saveLoginInfo(mAccount, mPassword);
		//保存登录成功状态到首选项里
		PreferHelper.getInstance().setBoolean(PreferHelper.HAD_LOGIN, true);
		EventBus.getDefault().post(new LoginEvent(true, result.getMsg()));
		EventBus.getDefault().post(new CartChangeEvent());
		if(isLoginSuccess!=null){
			isLoginSuccess.getLoginUserInfoUpdateUi();
		}
	}

	public void executeLoginRequest(String account, String md5Pwd){
		this.mAccount = account;
		this.mPassword = md5Pwd;
//		this.mVersion = version;
		Type typeOfResult = new TypeToken<UserModel>() {
		}.getType();
		rdpNetCommand = new RdpNetCommand(context, typeOfResult);
		rdpNetCommand.setOnCommandSuccessedListener(this);
		rdpNetCommand.setOnCommandFailedListener(this);
		rdpNetCommand.setServerApiUrl(UrlConstant.USER_LOGINVALIDATE); //网络链接地址接口
		rdpNetCommand.clearConditions();
		rdpNetCommand.setCondition("account", account);
		rdpNetCommand.setCondition("password", MD5Security.getMd5_32_UP(md5Pwd));
		rdpNetCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
//		rdpNetCommand.setCondition("version", version);
		rdpNetCommand.execute();
	}

	/**
	 * @Description:判断是否自动登陆
	 */
	public boolean isAutoLogin() {
		// 判断本地是否存有用户名和密码。有的话就自动登陆
		String phone = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_PHONE);
		String pwd = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_PWD);
		return !(TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd));
	}
	
	public void setOnLoginSuccess(OnLoginSuccessListener isLoginSuccess) {
		this.isLoginSuccess = isLoginSuccess;
	}
	//成功登录的接口回调
	public interface OnLoginSuccessListener{
		void getLoginUserInfoUpdateUi();

		public abstract void getUnLoginUserInfoUpdateUi();
	}
}
