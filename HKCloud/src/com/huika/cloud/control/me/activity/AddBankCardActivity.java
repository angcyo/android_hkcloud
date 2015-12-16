package com.huika.cloud.control.me.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.support.model.BankBean;
import com.huika.cloud.support.model.CardBean;
import com.huika.cloud.util.CommonAlertDialog;
import com.huika.cloud.util.MMAlertDialog;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

/**
 * @description：添加银行卡界面
 * @author ht
 * @date 2015-12-4 上午11:24:52
 */
public class AddBankCardActivity extends RdpBaseActivity {
	private static final int ADD_CARD = 0;
	private static final int EDIT_CARD = 1;
	private static final int SAVE_CARD = 2;
	private static final int REQUEST_CODE = 5;//跳转到银行列表请求码
	private View mMasterView;
	@ViewInject(R.id.my_add_bankcard_bank_part_name_edt)
	private EditText bank_part_name;
	@ViewInject(R.id.my_add_bankcard_no_edt)
	private EditText card_num;
	@ViewInject(R.id.my_add_bank_name)
	private TextView bank_name;
	@ViewInject(R.id.my_add_bankcard_bank_name_edt)
	private TextView bank_name_select;
	@ViewInject(R.id.my_add_bankcard_person_edt)
	private TextView bankCardPerson;
	private ArrayList<String> bankList;
	private BankBean selectedBank;
	private RelativeLayout selecte_bank;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("添加银行卡");
		RdpAnnotationUtil.inject(this);
		bankList = new ArrayList<String>();
		if (null != getIntent().getSerializableExtra("cardNumList")) {
			bankList.addAll((ArrayList<String>) getIntent().getSerializableExtra("cardNumList"));
		}
		addRightFuncTextView("保存", this, SAVE_CARD);
		mMasterView = addMasterView(R.layout.add_card_layout);
		mMasterView.findViewById(R.id.my_bank_tips_iv).setOnClickListener(this);
		mMasterView.findViewById(R.id.delete_card).setOnClickListener(this);
		mMasterView.findViewById(R.id.rl_selected_bank).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getTag() != null && (Integer) v.getTag() == SAVE_CARD) {
			// 保存
			if(isExistNull()){
				if(isExistCardNum()){
					//调用保存银行卡接口
					Type addResult = new TypeToken<String>() {
					}.getType();
					RdpNetCommand addCardCommand = new RdpNetCommand(mApplication, addResult);
					addCardCommand.setServerApiUrl(UrlConstant.USER_ADD_CARD);
					addCardCommand.setOnCommandSuccessedListener(this);
					addCardCommand.setOnCommandFailedListener(this);
					addCardCommand.clearConditions();
					addCardCommand.setCondition("memberId","402894e1511f1b6d01511f1bf30d0000");
					addCardCommand.setCondition("cardNumber",card_num.getText().toString());
					addCardCommand.setCondition("bankId",selectedBank.id);
					addCardCommand.setCondition("realName","糖糖");
					addCardCommand.setCondition("subbranchBank",bank_part_name.getText().toString());
					addCardCommand.execute();
				}
			}
		}
		switch (v.getId()) {
			case R.id.my_bank_tips_iv:
				showBankTipsDialog();
				break;
			case R.id.delete_card:
				//删除银行卡
				break;
			case R.id.rl_selected_bank:
				//跳转到银行列表界面
				startActivityForResult(new Intent(mApplication, BankListActivity.class), REQUEST_CODE);
				break;
			default:
				super.onClick(v);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE&&resultCode==BankListActivity.RESULT_CODE){
			selectedBank = (BankBean) data.getSerializableExtra(BankListActivity.SELECTED_BANK);
			if(selectedBank!=null){
				bank_name_select.setText(selectedBank.bankName);
			}
		}
	}
	
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		ToastMsg.showToastMsg(mApplication, result.getMsg());
		finish();
	}
	
	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
		ToastMsg.showToastMsg(mApplication, result.getMsg());
	}
	
	/**
	 * 处理真实姓名，只显示名字最后一个字
	 * @description：
	 * @author ldm
	 * @date 2014年12月16日 下午2:12:33
	 */
	private String dealRealName(String realName) {
		StringBuffer sb = new StringBuffer();
		if (!TextUtils.isEmpty(realName)) {
			String[] nameArray = new String[realName.length()];
			if (nameArray.length > 0) {
				for (int i = 0; i < nameArray.length; i++) {
					if (i != nameArray.length - 1) {
						sb.append("*");
					}
					else {
						sb.append(realName.charAt(i));
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 判断卡号是否存在，已经存在的卡号不能添加
	 * @description：
	 * @author ldm
	 * @date 2014年12月1日 下午2:40:08
	 */
	private boolean isExistCardNum() {
		boolean isExist = true;
		for (int i = 0; i < bankList.size(); i++) {
			if (card_num.getText().toString().trim().equals(bankList.get(i))) {
				showToastMsg(R.string.my_card_exist);
				isExist = false;
			}
		}
		return isExist;
	}

	/**
	 * 提交保存前所填项目不能为空
	 * @description：
	 * @author ldm
	 * @date 2014年12月1日 下午2:40:16
	 */
	private boolean isExistNull() {
//		if (TextUtils.isEmpty(bank_name.getText().toString())) {
//			showToastMsg(R.string.my_bankcard_select_account_bank);
//			return false;
//		}
		if (TextUtils.isEmpty(card_num.getText().toString())) {
			showToastMsg(R.string.my_bankcard_input_numbers);
			return false;
		}
		else if (!isBankCard(card_num.getText().toString().trim())) {
			showToastMsg(R.string.my_bankcard_input_numbers);
			return false;
		}
		else if (TextUtils.isEmpty(bank_part_name.getText().toString().trim())) {
			showToastMsg(R.string.my_bankcard_input_part);
			return false;
		}
		else {
			return true;
		}
	}

	// tip对话框
	private void showBankTipsDialog() {
		final CommonAlertDialog tipDialog = CommonAlertDialog.getInstance(AddBankCardActivity.this);
		tipDialog.withTitle("持卡人说明").withMessage("为了保障用户资金安全，只能绑定实名认证本人的银行卡").withSingleButton().setRightButtonClick(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tipDialog.dismiss();
			}
		}).show();
	}
	
	/**
	 * 判断是否银行卡
	 */
	public static boolean isBankCard(String str) {
		if (TextUtils.isEmpty(str)) { return false; }
		String scard = str.trim();
		Pattern pattern = Pattern.compile("^\\d{12,1000}$");
		return pattern.matcher(scard).matches();
	}
	
//	private void initView() {
//	bankCardPerson = (TextView) mMasterView.findViewById(R.id.my_add_bankcard_person_edt);
//	bank_name_select = (TextView) mMasterView.findViewById(R.id.my_add_bankcard_bank_name_edt);
//	bank_name = (TextView) mMasterView.findViewById(R.id.my_add_bank_name);
//	card_num = (EditText) mMasterView.findViewById(R.id.my_add_bankcard_no_edt);
//	bank_part_name = (EditText) mMasterView.findViewById(R.id.my_add_bankcard_bank_part_name_edt);
//}

}
