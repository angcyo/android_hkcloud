package com.huika.cloud.control.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.eshop.actvity.MyOrderActivity;
import com.huika.cloud.control.me.activity.ApplyForShopActivity;
import com.huika.cloud.control.me.activity.InviteMembersActivity;
import com.huika.cloud.control.me.activity.MyCouponsCountActivity;
import com.huika.cloud.control.me.activity.MyInfoActivity;
import com.huika.cloud.control.me.activity.MyShopActivity;
import com.huika.cloud.control.me.activity.MyWalletActivity;
import com.huika.cloud.control.me.activity.ShopEvaluateActivity;
import com.huika.cloud.control.safeaccount.LoginHelper;
import com.huika.cloud.control.safeaccount.LoginHelper.OnLoginSuccessListener;
import com.huika.cloud.control.safeaccount.activity.LoginActivity;
import com.huika.cloud.support.event.GetUserInfoEvent;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.PreferHelper;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpFramework.RdpFragment.RdpBaseFragment;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

import de.greenrobot.event.EventBus;

/**
 * @description： 我的页面
 * @date 2015-11-9 上午10:15:51
 */
public class MeFragment extends RdpBaseFragment {
	private View mMasterView;
	@ViewInject(R.id.iv_user)
	private ImageView iv_user;
	@ViewInject(R.id.reg_log)
	private Button reg_log;
	@ViewInject(R.id.rl_user_data)
	private RelativeLayout rl_user_data;
	@ViewInject(R.id.ptrsv)
	private ScrollView ptrsv;
	@ViewInject(R.id.ll_has_shop)
	private LinearLayout ll_has_shop;
	@ViewInject(R.id.no_shop)
	private LinearLayout ll_no_shop;
	@ViewInject(R.id.tv_user_name)
	private TextView tv_user_name;
	@ViewInject(R.id.tv_level_name)
	private TextView tv_level_name;
	//	@ViewInject(R.id.my_all_order_num)
//	private TextView my_all_order_num;
	@ViewInject(R.id.my_wait_pay_order_num)
	private TextView my_wait_pay_order_num;
	@ViewInject(R.id.my_wait_send_num)
	private TextView my_wait_send_num;
	@ViewInject(R.id.my_exchange_order_num)
	private TextView my_exchange_order_num;
	@ViewInject(R.id.iv_level)
	private ImageView iv_level;
	@ViewInject(R.id.iv_user_bg)
	private ImageView iv_user_bg;
	
	private LoginHelper loginHelper;
	private boolean isAutoLogin;
	private boolean isLoginSuccess;

	@Override
	protected void initFragment() {
		super.initFragment();
		setFuncTitle("个人中心");
		mMasterView = addMasterView(R.layout.me_master);
		EventBus.getDefault().register(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		RdpAnnotationUtil.inject(this, mRootView);
		return mRootView;
	}

	public void onEventMainThread(GetUserInfoEvent event) {
		getUserInfo();
	}

	/**获取用户信息*/
	private void getUserInfo() {
		String phone = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_PHONE);
		String pwd = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_PWD);
		loginHelper = LoginHelper.getInstance(mApplication);
		// 设置请求成功的回调监听
		loginHelper.setOnLoginSuccess(new OnLoginSuccessListener() {
			@Override
			public void getLoginUserInfoUpdateUi() {
				//登录成功，更新UI
				UserModel user = HKCloudApplication.getInstance().getUserModel();
				isLoginSuccess = true;
				// 判断用户是否已经开店
//				initLoginUI(user);
			}

			@Override
			public void getUnLoginUserInfoUpdateUi() {
				//登录失败，更新UI
				isLoginSuccess = false;
//				UserModel user = HKCloudApplication.getInstance().getUserModel();
//				if(user==null){
//					initUnloginUI();
//				}else{
//					initLoginUI(user);
//				}
			}
		});
		loginHelper.executeLoginRequest(phone, pwd);
	}

	/**初始化登录状态*/
	private void initLoginUI(UserModel user) {
		reg_log.setVisibility(View.GONE);
		rl_user_data.setVisibility(View.VISIBLE);
		tv_user_name.setText(user.nickName);
		tv_level_name.setText(user.levelName);
		RdpImageLoader.displayImage(user.levelIcon, iv_level);
		RdpImageLoader.displayImage(user.imageUrl, iv_user_bg);
		RdpImageLoader.displayImage(user.imageUrl, iv_user);
//		my_wait_pay_order_num.setText(user.paymentNum);
//		my_wait_send_num.setText(user.ge)
//		my_exchange_order_num.setText(user.returnNum);
	}
	
	/** 初始化未登录状态ui*/
	private void initUnloginUI() {
		reg_log.setVisibility(View.VISIBLE);
		rl_user_data.setVisibility(View.GONE);
		iv_user.setImageResource(R.drawable.default_icon_photo);
	}

	@Override
	public void onResume() {
		super.onResume();
		isAutoLogin = LoginHelper.getInstance(mApplication).isAutoLogin();
		if (isAutoLogin) {
			reg_log.setVisibility(View.GONE);
			rl_user_data.setVisibility(View.VISIBLE);
			// 如果是自动登录了 刷新登录以后的ui
			getUserInfo();
		}
		else {
			reg_log.setVisibility(View.VISIBLE);
			rl_user_data.setVisibility(View.GONE);
			// 如果不是自动登录 刷新没有登录的ui
			initUnloginUI();
		}
	}

	@OnClick({R.id.iv_user,R.id.reg_log,R.id.rl_all_orders,R.id.rl_wait_payment,R.id.rl_wait_send,
		R.id.rl_change_return,R.id.invite_members,R.id.apply_for_shop,R.id.rl_my_shop,R.id.coupons_count,R.id.my_wallet})
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_user:
				// 点击图像
				loginOrJump(isLoginSuccess, MyInfoActivity.class);
				break;
			case R.id.reg_log:
				// 登录注册
				loginOrJump(isLoginSuccess, LoginActivity.class);
				break;
			case R.id.my_wallet:
				// 我的钱包
				loginOrJump(isLoginSuccess, MyWalletActivity.class);
				break;
			case R.id.coupons_count:
				// 我的优惠券
				loginOrJump(isLoginSuccess, MyCouponsCountActivity.class);
				break;
			case R.id.rl_all_orders:
				// 全部订单
				loginOrJump(isLoginSuccess, MyOrderActivity.class);
				showActivity(mActivity, ShopEvaluateActivity.class);
				break;
			case R.id.rl_wait_payment:
				// 待付款订单
				ToastMsg.showToastMsg(mApplication, "点击了待付款订单");
				break;
			case R.id.rl_wait_send:
				// 待发货订单
				ToastMsg.showToastMsg(mApplication, "点击了待发货订单");
				break;
			case R.id.rl_change_return:
				// 退换货订单
				ToastMsg.showToastMsg(mApplication, "点击了退换货订单");
				break;
			case R.id.apply_for_shop:
				// 申请开店
				loginOrJump(isLoginSuccess, ApplyForShopActivity.class);
				break;
			case R.id.invite_members:
				// 邀请会员
				loginOrJump(isLoginSuccess, InviteMembersActivity.class);
				break;
			case R.id.rl_my_shop:
				//我的店铺
				startActivity(new Intent(mApplication, MyShopActivity.class));
				break;
			default:
				super.onClick(v);
				break;
		}
	}

	private void loginOrJump(boolean isLogin, Class<?> cls) {
		if (isLogin) {
			// 登录了才能跳转到相应的界面
			startActivity(new Intent(mApplication, cls));
		}
		else {
			Intent intent = new Intent(mApplication, LoginActivity.class);
			intent.putExtra("INP_WHERE","MeFragment");
			startActivity(intent);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
