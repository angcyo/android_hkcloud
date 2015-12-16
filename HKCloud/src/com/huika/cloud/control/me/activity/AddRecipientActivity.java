package com.huika.cloud.control.me.activity;

import java.lang.reflect.Type;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.support.model.AddressBean;
import com.huika.cloud.util.PhoneUtil;
import com.huika.cloud.views.ChangeAddressDialog;
import com.huika.cloud.views.ChangeAddressDialog.OnAddressCListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

/**
 * @description：编辑、新增收货地址
 * @author ht
 * @date 2015-12-4 上午10:28:33
 */
public class AddRecipientActivity extends RdpBaseActivity {
	private View mMasterview;
	private View footView;
	private Button bt_save_recipient;
	private boolean isadd;
	private EditText et_address_people;
	private EditText et_address_phone;
	private EditText et_address_detail;
	private TextView et_address_area;
	private AddressBean addressBean;

	/** 选择区域请求 */
	private static final int REQUEST_AREA = 0;
	static final String ADDRESS = "ADDRESS";
	private RdpNetCommand addressCommand;
	private ToggleButton tglbt;
	
	private String addressId;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		Intent intent = getIntent();
		isadd = intent.getBooleanExtra("isadd", false);
		if (isadd) {
			// 添加新地址界面
			setFuncTitle("新建收货地址");
			addressBean = new AddressBean();
			addressBean.addressId="0";
		}
		else {
			addressId = intent.getStringExtra(Constant.IPN_SELECTED_ITEM);
			// 编辑地址界面
			setFuncTitle("编辑收获地址");
			// 查询单个地址
			Type simpleAddressResult = new TypeToken<AddressBean>() {
			}.getType();
			RdpNetCommand simpleAddressCommand = new RdpNetCommand(mApplication, simpleAddressResult);
			simpleAddressCommand.setOnCommandFailedListener(this);
			simpleAddressCommand.setOnCommandSuccessedListener(this);
			
			getSimpleAddress(simpleAddressCommand);
		}
		mMasterview = addMasterView(R.layout.add_new_recipient);
		initView();
		initListener();
	}
	/**获取单个地址信息*/
	private void getSimpleAddress(RdpNetCommand simpleAddressCommand) {
		simpleAddressCommand.setServerApiUrl(UrlConstant.USER_QUERY_SIMPLE_ADDRESS);
		simpleAddressCommand.clearConditions();
		simpleAddressCommand.setCondition("memberId", "402894e1511f1b6d01511f1bf30d0000");
		simpleAddressCommand.setCondition("addressId", addressId);
		simpleAddressCommand.execute();
	}

	private void initListener() {
		bt_save_recipient.setOnClickListener(this);
		et_address_area.setOnClickListener(this);
		tglbt.setOnCheckedChangeListener(occl);
	}

	private void initView() {
		bt_save_recipient = (Button) mMasterview.findViewById(R.id.bt_save_recipient);
		et_address_people = (EditText) mMasterview.findViewById(R.id.et_address_people);
		et_address_phone = (EditText) mMasterview.findViewById(R.id.et_address_phone);
		et_address_detail = (EditText) mMasterview.findViewById(R.id.et_address_detail);
		et_address_area = (TextView) mMasterview.findViewById(R.id.et_address_area);
		tglbt = (ToggleButton) mMasterview.findViewById(R.id.tglbt);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_save_recipient:
				setResult(RESULT_OK);
				finish();
				// TODO 新增或修改收货地址请求
				 newOrAddAddressRequest();
				break;
			case R.id.et_address_area:
				// 保存现有数据
				ChangeAddressDialog mChangeAddressDialog = new ChangeAddressDialog(AddRecipientActivity.this);
				mChangeAddressDialog.show();
				mChangeAddressDialog.setAddresskListener(new OnAddressCListener() {

					@Override
					public void onClick(String province, String city, String area, String selectedAreaId) {
						addressBean.receiverAreaName = province + city + area;
						et_address_area.setText(addressBean.receiverAreaName);
						addressBean.areaId = selectedAreaId;
					}
				});

				break;
			default:
				super.onClick(v);
				break;
		}
	}

	/**新增或者修改收货地址请求*/
	private void newOrAddAddressRequest() {
		String input_detail = et_address_detail.getText().toString();
		String input_people = et_address_people.getText().toString();
		String input_phone = et_address_phone.getText().toString();
		if (checkEmpty(input_people, R.string.my_address_people_input_hint) && PhoneUtil.IsPhoneStr(input_phone) && checkArea() && checkEmpty(input_detail, R.string.my_address_detail_input_hint)) {
			showLoadingDialog(getString(R.string.common_loading));
			addressBean.receiverName = input_people;
			addressBean.receiverPhone = input_phone;
			addressBean.receiverAddress = input_detail;
		}
		// 保存收获地址
		Type typeOfResult = new TypeToken<String>() {
		}.getType();
		addressCommand = new RdpNetCommand(mApplication, typeOfResult);
		addressCommand.setOnCommandSuccessedListener(this);
		addressCommand.setOnCommandFailedListener(this);
		addressCommand.setServerApiUrl(UrlConstant.USER_ADD_OR_UPDATE_ADDRESS); // 下订单的接口
		addressCommand.clearConditions();
		addressCommand.setCondition("addressId", addressBean.addressId);
		addressCommand.setCondition("memberId", "402894e1511f1b6d01511f1bf30d0000");
		addressCommand.setCondition("merchantId","402881e8461795c201461795c2e90000");
		addressCommand.setCondition("areaId",addressBean.areaId);
		addressCommand.setCondition("receiverName", addressBean.receiverName);
		addressCommand.setCondition("receiverAddress",addressBean.receiverAddress);
		addressCommand.setCondition("receiverPhone", addressBean.receiverPhone);
		addressCommand.setCondition("isDefault", isDefault + "");
		addressCommand.execute();
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
	}

	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		if (UrlConstant.USER_ADD_OR_UPDATE_ADDRESS.equals(result.getUrl())) {
			Intent i = new Intent();
			setResult(RESULT_OK, i);
			finish();
		}
		else if (UrlConstant.USER_QUERY_SIMPLE_ADDRESS.equals(result.getUrl())) {
			addressBean = (AddressBean) data;
			if (addressBean != null) {
				et_address_people.setText(addressBean.receiverName);
				et_address_phone.setText(addressBean.receiverPhone);
				et_address_area.setText(addressBean.province+addressBean.city+addressBean.area);
				et_address_detail.setText(addressBean.receiverAddress);
			}
		}
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == RESULT_OK && data != null) {
//			if (requestCode == REQUEST_AREA) {
//				int areaId = data.getIntExtra(Constant.CHOICE_AREA_RESULT_ID, -1);
//				addressBean.receiverAreaID = areaId == -1 ? "" : (areaId + "");
//				addressBean.receiverAreaName = data.getStringExtra(Constant.CHOICE_AREA_RESULT);
//			}
//			showData();
//			setResult(RESULT_OK);
//		}
//	}

	private void showData() {
		if (addressBean != null) {
			et_address_people.setText(addressBean.receiverName);
			et_address_phone.setText(addressBean.receiverPhone);
			et_address_detail.setText(addressBean.receiverAddress);
			if (!TextUtils.isEmpty(addressBean.receiverAreaName)) {
				et_address_area.setText(addressBean.receiverAreaName);
			}
			else {
				et_address_area.setText(R.string.my_address_area);
			}
		}
	}

	/** 保存 end */

	private boolean checkArea() {
		if (TextUtils.isEmpty(addressBean.areaId) || TextUtils.isEmpty(addressBean.receiverAreaName)) {
			showToastMsg(R.string.my_address_area_hint);
			return false;
		}
		return true;
	}

	/**
	 * 邮编可以为空
	 */
	private boolean checkPostage(String input) {
		if (!TextUtils.isEmpty(input) && !input.matches("^\\d{6}$")) {
			showToastMsg(R.string.my_address_postage_error);
			return false;
		}
		return true;
	}

	private boolean checkEmpty(String input, int strId) {
		if (TextUtils.isEmpty(input)) {
			showToastMsg(strId);
			return false;
		}
		return true;
	}

	int isDefault = 0;
	OnCheckedChangeListener occl = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				isDefault = 1;
				ToastMsg.showToastMsg(mApplication, "设为默认");
			}
			else {
				isDefault = 0;
				ToastMsg.showToastMsg(mApplication, "取消默认");
			}
		}
	};

}
