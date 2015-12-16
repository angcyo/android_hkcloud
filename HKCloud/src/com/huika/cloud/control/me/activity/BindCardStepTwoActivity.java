package com.huika.cloud.control.me.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.util.GetCardTypeUtils;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

public class BindCardStepTwoActivity extends RdpBaseActivity {
	private static final String BIND_CARD_TWO = "bind_card_two";
	private static final int FRAGMENT_BACK = 1;
	private View mMasterView;
	private Button goto_step_three;
	private EditText et_card_num;
	private TextView input_card_type;
	private BroadcastReceiver receiver;
	private ImageView goto_card_list;

	@Override
	protected void onBeforeCreate(Bundle savedInstanceState) {
		super.onBeforeCreate(savedInstanceState);
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				BindCardStepTwoActivity.this.finish();
			}
		};
		IntentFilter filter=new IntentFilter(Constant.FINISH_ACTIVITY);
		registerReceiver(receiver, filter);
	}
	
	@Override
	protected void initActivity() {
		super.initActivity();
		// mTitleBar.setVisibility(View.GONE);
		// removeLeftFuncView(TBAR_FUNC_BACK);
		// addLeftFuncView(R.drawable.common_head_back_select, this,
		// FRAGMENT_BACK);
		mMasterView = addMasterView(R.layout.bind_card_two);
		et_card_num = (EditText) mMasterView.findViewById(R.id.et_card_num);
		goto_card_list = (ImageView) mMasterView.findViewById(R.id.goto_card_list);
		input_card_type = (TextView) mMasterView
				.findViewById(R.id.input_card_type);
		goto_step_three = (Button) mMasterView
				.findViewById(R.id.goto_step_three);
		
//		et_card_num.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (!hasFocus) {
//					ToastMsg.showToastMsg(mApplication, "et失去焦点");
//					// 失去焦点
//					if (et_card_num.getText().length() >= 16) {
//						String nameOfBank = queryCardType();
//						if (nameOfBank != null && !nameOfBank.equals("磁条卡卡号")) {
//							input_card_type.setText(nameOfBank);
//							goto_step_three.setVisibility(View.VISIBLE);
//						} else {
//							ToastMsg.showToastMsg(mApplication, "请检查输入的银行卡号");
//						}
//					} else {
//						ToastMsg.showToastMsg(mApplication, "请检查输入的银行卡号");
//					}
//				}
//			}
//		});
		goto_step_three.setOnClickListener(this);
		goto_card_list.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (et_card_num.getText() != null
				&& !TextUtils.isEmpty(et_card_num.getText())) {
			queryCardType();
			goto_step_three.setVisibility(View.VISIBLE);
		}
	}

	private String queryCardType() {
		String input_card_num = et_card_num.getText().toString();
		String substring = input_card_num.substring(0, 6);
		char[] charBin = substring.toCharArray();
		String nameOfBank = GetCardTypeUtils.getNameOfBank(charBin, 0);
		nameOfBank = nameOfBank.substring(0, nameOfBank.length() - 2);
		input_card_type.setText(nameOfBank);
		return nameOfBank;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_step_three:
			ToastMsg.showToastMsg(mApplication, "跳转到绑定银行第三步");
			// 跳转到绑定银行卡第三步
			Intent intent = new Intent(mApplication, BindCardStepThreeActivity.class);
			getCardTypeAndNum(intent);
			startActivity(intent);
			break;
		case R.id.goto_card_list:
			if (et_card_num.getText().length() >= 16) {
				String nameOfBank = queryCardType();
				if (nameOfBank != null && !nameOfBank.equals("磁条卡卡号")) {
					input_card_type.setText(nameOfBank);
					goto_step_three.setVisibility(View.VISIBLE);
				} else {
					ToastMsg.showToastMsg(mApplication, "请检查输入的银行卡号");
				}
			} else {
				ToastMsg.showToastMsg(mApplication, "请检查输入的银行卡号");
			}
			break;
		default:
			super.onClick(v);
			break;
		}

	}
	
	/** 获取输入的银行卡号后四位 和银行类型 */
	private void getCardTypeAndNum(Intent intent) {
		String card_bank_type = input_card_type.getText().toString();
		int index = card_bank_type.indexOf(".");
		String card_type = card_bank_type.substring(0, index);
		String card_num = et_card_num.getText().toString();
		String card_num_last_four = card_num.substring(
				card_num.length() - 5, card_num.length() - 1);
		intent.putExtra("card_type", card_type);
		intent.putExtra("card_num", card_num_last_four);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
