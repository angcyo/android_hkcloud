package com.huika.cloud.control.me.activity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.safeaccount.LoginHelper;
import com.huika.cloud.control.safeaccount.activity.AuthenticationActivity;
import com.huika.cloud.support.model.CardBean;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.CommonAlertDialog;
import com.huika.cloud.util.MMAlertDialog;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;
import com.zhoukl.androidRDP.RdpUtils.RdpUtils;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

/**
 * @description：我的钱包
 * @author ht
 * @date 2015-12-4 上午10:24:05
 */
public class MyWalletActivity extends RdpBaseActivity {
	private static final int REQUEST_CODE = 0;
	private View mMasterView;
	@ViewInject(R.id.wallet_count)
	private TextView wallet_count;
	@ViewInject(R.id.isBind)
	private TextView isBind;
	@ViewInject(R.id.bt_withdraw)
	private Button bt_withdraw;
	@ViewInject(R.id.withdraw_record)
	private Button withdraw_record;
	@ViewInject(R.id.ll_master)
	private LinearLayout ll_master;
	@ViewInject(R.id.rl_bind_card)
	private RelativeLayout rl_bind_card;
	
	private UserModel mUser;
	private int realNameAuthentication;
	private int isBindBank;
	private Dialog setPayPwdWarn;
	private List<CardBean> cardList;
	RdpDataListAdapter<CardBean> cardListAdapter;
	private int isSelectedPosition;
	private PopupWindow cardListPop;
	private CardBean selectedCard;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("我的钱包");
		mMasterView = addMasterView(R.layout.my_wallet_activity);
		RdpAnnotationUtil.inject(this);
		// 获取用户信息
	}

	@Override
	protected void onResume() {
		super.onResume();
		//更新账户余额
		UserModel mUser = HKCloudApplication.getInstance().getUserModel();
		wallet_count.setText("¥"+mUser.getBalance());
		if(mUser.getIsBindBank()==0){
			isBind.setText("未绑定");
		}else if(mUser.getIsBindBank()==1){
			isBind.setText("已绑定");
		}
	}

	@OnClick({R.id.rl_bind_card,R.id.bt_withdraw,R.id.withdraw_record})
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_bind_card:
				//点击跳转到银行卡列表
				startActivityForResult(new Intent(mApplication, BankCardListActivity.class),REQUEST_CODE);
				break;
			case R.id.bt_withdraw:
				// 提现
				Intent withDrawIntent = new Intent(mApplication, WithdrawApplicationActivity.class);
				withDrawIntent.putExtra("selected_card", selectedCard);
				startActivity(withDrawIntent);
				break;
			case R.id.withdraw_record:
				// 提现记录
				startActivity(new Intent(mApplication, WithDrawRecordActivity.class));
				break;
			default:
				super.onClick(v);
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE){
			selectedCard = (CardBean) data.getParcelableExtra("select_item");
			if(selectedCard!=null){
				isBind.setText(setBankNameAndCardNum(selectedCard));
			}
		}
	}
	
	private void warning() {
		Dialog dialog = MMAlertDialog.createShowCenterDialog(MyWalletActivity.this, "温馨提示", "您还没有实名认证，请先进行实名认证", "忽略", "立即前往", null, new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastMsg.showToastMsg(mApplication, "跳转到实名注册界面");
				startActivity(new Intent(mApplication, AuthenticationActivity.class));
			}
		});
		dialog.show();
	}
	
	private String setBankNameAndCardNum(CardBean cardBean) {
		String bankName = cardBean.bankName;
		String cardNumber = cardBean.cardNumber;
		cardNumber = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
		return bankName + "(" + cardNumber + ")";
	}
	
}
