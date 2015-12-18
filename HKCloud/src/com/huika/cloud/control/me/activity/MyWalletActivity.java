package com.huika.cloud.control.me.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.safeaccount.activity.AuthenticationActivity;
import com.huika.cloud.support.model.CardBean;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.MMAlertDialog;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

import java.util.List;

/**
 * @description：我的钱包
 * @author ht
 * @date 2015-12-4 上午10:24:05
 */
public class MyWalletActivity extends RdpBaseActivity {
	private static final int REQUEST_CODE = 0;
	private static final int ACCOUNT_DETAIL = 101;
	RdpDataListAdapter<CardBean> cardListAdapter;
	private View mMasterView;
	@ViewInject(R.id.wallet_count)
	private TextView wallet_count;
	@ViewInject(R.id.isBind)
	private TextView isBind;
	@ViewInject(R.id.bt_withdraw)
	private Button bt_withdraw;
	@ViewInject(R.id.ll_master)
	private LinearLayout ll_master;
	@ViewInject(R.id.rl_bind_card)
	private RelativeLayout rl_bind_card;
	private int realNameAuthentication;
	private int isBindBank;
	private Dialog setPayPwdWarn;
	private List<CardBean> cardList;
	private int isSelectedPosition;
	private PopupWindow cardListPop;
	private CardBean selectedCard;
	private UserModel mUser;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("我的钱包");
		addRightFuncTextView("账单", this, ACCOUNT_DETAIL);
		mMasterView = addMasterView(R.layout.my_wallet_activity);
		RdpAnnotationUtil.inject(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mUser = HKCloudApplication.getInstance().getUserModel();
		wallet_count.setText("¥" + mUser.balance);
		if (mUser.isBindBank == 0) {
			isBind.setText("未绑定");
		} else if (mUser.isBindBank == 1) {
			isBind.setText(mUser.bankNum);
		}
	}

	@OnClick({R.id.rl_bind_card, R.id.bt_withdraw})
	@Override
	public void onClick(View v) {
		if (v.getTag() != null && v.getTag() == (Integer) ACCOUNT_DETAIL) {
			startActivity(new Intent(mApplication, WithDrawRecordActivity.class));
			return;
		}
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
			default:
				super.onClick(v);
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE){
			selectedCard = data.getParcelableExtra("select_item");
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
