package com.huika.cloud.control.me.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.support.model.AddressBean;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.MMAlertDialog;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @description：收货地址
 * @date 2015-12-3 下午5:40:42
 */
public class MyRecipientActivity extends RdpBaseActivity implements OnRefreshItemViewsListener, OnItemClickListener {
	public static final String IPN_ITEM_CLICK_TYPE ="itemClickType";//1 选中收货地址  0跳转到编辑地址
	public static final int RESULTCODE = 55;
	private static final int REQUEST = 0;
	private static final int ADDRESS_DELETE = 99;
	private View mMasterView;
	private View footView;
	private RdpListView rlv;
	private TextView tv_address;
	private TextView tv_phone;
	private TextView tv_name;
	private RdpDataListAdapter<AddressBean> mAdapter;

	//选中删除的地址
	private int selectedPosition = -1;
	OnClickListener rightClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			AddressBean addressBean = mAdapter.getItem(selectedPosition);
			//TODO 删除收货地址
			Type deleteAddressResult = new TypeToken<String>() {
			}.getType();
			showLoadingOverLay(rlv);
			RdpNetCommand deleteAddressCommand = new RdpNetCommand(mApplication, deleteAddressResult);
			deleteAddressCommand.setServerApiUrl(UrlConstant.USER_DELETE_ADDRESS);
			deleteAddressCommand.clearConditions();
			deleteAddressCommand.setOnCommandSuccessedListener(MyRecipientActivity.this);
			deleteAddressCommand.setOnCommandFailedListener(MyRecipientActivity.this);
			deleteAddressCommand.setCondition("memberId", "402894e1511f1b6d01511f1bf30d0000");
			deleteAddressCommand.setCondition("addressId", addressBean.addressId);
			deleteAddressCommand.execute();
		}
	};
	private Dialog deleteWarnDlg;
	OnClickListener leftClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			deleteWarnDlg.dismiss();
		}
	};
	private int itemClickType;
	private RelativeLayout rl_add_address;
	private View no_address;
	private UserModel mUser;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		mUser = HKCloudApplication.getInstance().getUserModel();
		setFuncTitle("我的收货地址");
		itemClickType = getIntent().getIntExtra(IPN_ITEM_CLICK_TYPE,0);
		addRightFuncTextView("删除", this, ADDRESS_DELETE);
		mMasterView = addMasterView(R.layout.my_recipient);
		footView = addFooterView(R.layout.my_recipient_foot);
		rl_add_address = (RelativeLayout) footView.findViewById(R.id.rl_add_address);
		rl_add_address.setOnClickListener(this);
		rlv = (RdpListView) mMasterView.findViewById(R.id.rlv);
		no_address = mMasterView.findViewById(R.id.no_address);

		mAdapter = new RdpDataListAdapter<AddressBean>(mApplication, R.layout.item_address_list);
		mAdapter.setListener(this);
		rlv.setAdapter(mAdapter);
		rlv.setOnItemClickListener(this);

		//获取地址列表数据
		getAddressListData(mUser.memberId, "402881e8461795c201461795c2e90000");

	}

	private void getAddressListData(String memberId, String merchantId) {
		RdpNetDataSet addressList = new RdpNetDataSet(mApplication);
		showLoadingOverLay(rlv);
		addressList.setServerApiUrl(UrlConstant.USER_ADDRESS_LIST);
		addressList.clearConditions();
		addressList.setOnCommandSuccessedListener(this);
		addressList.setOnCommandFailedListener(this);
		addressList.setCondition("memberId", memberId);
		addressList.setCondition("merchantId", merchantId);
		Type addressResult = new TypeToken<List<AddressBean>>() {
			}.getType();
		addressList.setTypeOfResult(addressResult);
		addressList.open();
	}

	@Override
	public void onClick(View v) {

		if(v.getTag()!=null){
			switch ((Integer)v.getTag()) {
				case ADDRESS_DELETE:
					if(selectedPosition<0){
						ToastMsg.showToastMsg(mApplication, "请先选择要删除的地址");
					}else{
						deleteWarnDlg = MMAlertDialog.createSoftwareUpdate(MyRecipientActivity.this, "是否删除该地址", leftClick, rightClick);
						deleteWarnDlg.show();
					}
					return;
				default:
					break;
			}
		}
		switch (v.getId()) {
		case R.id.rl_add_address:
			//添加新的收货地址
			Intent intent=new Intent(mApplication,AddRecipientActivity.class);
			intent.putExtra("isadd", true);
			startActivityForResult(intent, REQUEST);
			break;
		default:
			super.onClick(v);
			break;
		}

	}
	
	@Override
	public boolean onRefreshItemViews(final RdpAdapter adapter, final int position,
			View convertView, AdapterViewHolder holder) {
		final ImageView iv_select = holder.getImageView(R.id.iv_select);
		holder.getTextView(R.id.tv_is_default).setVisibility(View.GONE);
		if(selectedPosition==position){
			iv_select.setImageResource(R.drawable.icon_radio_selected);
		}else{
			iv_select.setImageResource(R.drawable.icon_radio_normal);
		}
		AddressBean item = (AddressBean) adapter.getItem(position);
		holder.getTextView(R.id.tv_name).setText(item.receiverName);
		holder.getTextView(R.id.tv_phone).setText(item.receiverPhone);
		((EditText)holder.getView(R.id.tv_address)).setText(item.receiverAddress);
		iv_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedPosition = position;
				adapter.notifyDataSetChanged();
			}
		});
		if(item.isDefault==1){
			//默认地址
			holder.getTextView(R.id.tv_is_default).setVisibility(View.VISIBLE);
			holder.getTextView(R.id.tv_is_default).setText(getShowDefaultName(""));
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AddressBean addressBean = mAdapter.getItem(position-rlv.getRefreshableView().getHeaderViewsCount());
		if (itemClickType==0) {
			Intent intent=new Intent(mApplication, AddRecipientActivity.class);
			intent.putExtra(Constant.IPN_SELECTED_ITEM,addressBean.addressId);
			startActivityForResult(intent, REQUEST);
		}else if(itemClickType==1){
			Intent data=new Intent();
			data.putExtra(Constant.IPN_SELECTED_ITEM, addressBean);
			setResult(RESULTCODE, data);
			MyRecipientActivity.this.finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			 //获取收货地址列表，刷新
			getAddressListData(mUser.memberId, "402881e8461795c201461795c2e90000");
		}
	}
	
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result,
			Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		hideOverLayView();
		if(UrlConstant.USER_ADDRESS_LIST.equals(result.getUrl())){
			//获取地址列表
			if (((List<AddressBean>) data).size() > 0) {
				no_address.setVisibility(View.GONE);
				mAdapter.setData((List<AddressBean>) data);
			} else {
				no_address.setVisibility(View.VISIBLE);
			}
		}else if(UrlConstant.USER_DELETE_ADDRESS.equals(result.getUrl())){
			ToastMsg.showToastMsg(mApplication, "删除地址成功");
			selectedPosition=-1;
			getAddressListData(mUser.memberId, "402881e8461795c201461795c2e90000");
		}
	}
	
	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
	}
	
	private Spanned getShowDefaultName(String address) {
		return Html.fromHtml("<font color=#ff8502>[默认]</font><font color=#969696>" + address + "</font>");
	}
}
