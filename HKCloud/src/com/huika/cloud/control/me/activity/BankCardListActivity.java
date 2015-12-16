package com.huika.cloud.control.me.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.safeaccount.activity.AuthenticationActivity;
import com.huika.cloud.support.model.AccountDetailBean;
import com.huika.cloud.support.model.CardBean;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.CommonAlertDialog;
import com.huika.cloud.util.MMAlertDialog;
import com.umeng.common.util.DeltaUpdate;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

/**
 * 
 * @description：银行卡列表
 * @author ht
 * @date 2015-12-4 上午10:27:55
 */
public class BankCardListActivity extends RdpBaseActivity implements OnRefreshItemViewsListener, OnItemClickListener, OnItemLongClickListener {
	private static final int AUTHENTICATION_REQUEST_CODE = 0;//实名认证请求码
	private static final int SET_PAY_WORD_REQUESTCODE = 1;//设置支付密码请求码
	private View mMasterView;
	private ListView lv_card;
	private RdpDataListAdapter<CardBean> mAdapter;
	private View mFootView;
	private LinearLayout ll_add_new_card;
	private UserModel mUser;
	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("银行卡列表");
		
		mMasterView = addMasterView(R.layout.bank_card_list);
		mFootView = addFooterView(R.layout.bank_card_lv_foot);
		initView();
		
		ll_add_new_card.setOnClickListener(this);
		
		mAdapter = new RdpDataListAdapter<CardBean>(mApplication,R.layout.card_list_item);
		mAdapter.setListener(this);
		lv_card.setAdapter(mAdapter);
		lv_card.setOnItemClickListener(this);
		lv_card.setOnItemLongClickListener(this);
		
		cardListData = new RdpNetDataSet(mApplication);
		cardListData.setOnCommandSuccessedListener(this);
		cardListData.setOnCommandFailedListener(this);
		
	}
	/**获取银行卡列表数据*/
	private void getCardListData(RdpNetDataSet cardListData) {
		cardListData.clearConditions();
		cardListData.setServerApiUrl(UrlConstant.USER_GET_MY_CARD_LIST);
		cardListData.setCondition("memberId","402894e1511f1b6d01511f1bf30d0000");
		Type typeOfResult = new TypeToken<List<CardBean>>() {
		}.getType();
		cardListData.setTypeOfResult(typeOfResult);
		showLoadingOverLay(lv_card);
		cardListData.open();
	}

	private void initView() {
		lv_card = (ListView) mMasterView.findViewById(R.id.lv_card);
		ll_add_new_card = (LinearLayout) mFootView.findViewById(R.id.ll_add_new_card);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		mUser = new UserModel();
//		mUser.setRealNameAuthentication(1);
//		mUser.setTransPassword(1);
		mUser = HKCloudApplication.getInstance().getUserModel();
		getCardListData(cardListData);
	}
	
	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
		CardBean item =mAdapter.getItem(position);
		holder.getTextView(R.id.tv_bank_name).setText(item.bankName);
		String cardNumber = item.cardNumber;
		cardNumber=cardNumber.substring(cardNumber.length()-4, cardNumber.length());
		holder.getTextView(R.id.tv_card_num).setText(cardNumber);
		RdpImageLoader.displayImage(item.logoImg, holder.getImageView(R.id.iv_bank_icon));
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		selectedPosition=position;
		CardBean item = mAdapter.getItem(position);
		ToastMsg.showToastMsg(mApplication, item.cardNumber+":"+item.bankName);
		Intent data=new Intent();
		data.putExtra("select_item", item);
		setResult(RESULT_OK, data);
		BankCardListActivity.this.finish();
	}
	
	int selectedPosition=0;
	
	@Override
	public void onBackPressed() {
		CardBean item=null;
		if (cardListData.getRecordCount()>0) {
			item= mAdapter.getItem(selectedPosition);
		}
		Intent data=new Intent();
		data.putExtra("select_item", item);
		setResult(RESULT_OK, data);
		BankCardListActivity.this.finish();
	}
	
	
	@Override
	public void onClick(View v) {
		if(v.getTag()!=null&&(Integer)v.getTag()==TBAR_FUNC_BACK){
			onBackPressed();
		}
		switch (v.getId()) {
			case R.id.ll_add_new_card:
				//判断是否实名认证
				if(mUser.getRealNameAuthentication()==0){
					//未实名认证
					startActivityForResult(new Intent(mApplication,AuthenticationActivity.class), AUTHENTICATION_REQUEST_CODE);
					return;
				}
				if(mUser.getTransPassword()==0){
					//没有设置支付密码
					Intent intent = new Intent(mApplication, BindBankActivity.class);
					intent.putExtra(BindBankActivity.INP_TYPE, 1);
					startActivityForResult(intent, SET_PAY_WORD_REQUESTCODE);
					return;
				}
				//绑定银行卡
				List<String> cardNumList = new ArrayList<String>();
				for (int i = 0; i < cardListData.getRecordCount(); i++) {
					CardBean record = (CardBean) cardListData.getRecord(i);
					cardNumList.add(record.cardNumber);
				}
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("cardNumList", (ArrayList<String>) cardNumList);
				startActivity(new Intent(mApplication, AddBankCardActivity.class));
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==AUTHENTICATION_REQUEST_CODE&&resultCode==AuthenticationActivity.RESULT_CODE){
			if(mUser.getTransPassword()==0){
				//没有设置支付密码
				Intent intent = new Intent(mApplication, BindBankActivity.class);
				intent.putExtra(BindBankActivity.INP_TYPE, 1);
				startActivityForResult(intent, SET_PAY_WORD_REQUESTCODE);
			}
		};
		if(requestCode==SET_PAY_WORD_REQUESTCODE&&resultCode==BindBankActivity.RESULT_CODE){
			//绑定银行卡
			startActivity(new Intent(mApplication, AddBankCardActivity.class));
		}
	}

	private Dialog dlg;
	private int deletePosition;
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		deletePosition = position;
		dlg = MMAlertDialog.createSoftwareUpdate(BankCardListActivity.this, "是否删除该银行卡", leftClick, rightClick);
		dlg.show();
		return false;
	}
	
	OnClickListener leftClick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			dlg.dismiss();
		}
	};
	
	OnClickListener rightClick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			dlg.dismiss();
			CardBean deleteCard=(CardBean) cardListData.getRecord(deletePosition);
			Type deleteResult = new TypeToken<String>() {
			}.getType();
			RdpNetCommand deleteCardCommand = new RdpNetCommand(mApplication, deleteResult);
			deleteCardCommand.setOnCommandSuccessedListener(BankCardListActivity.this);
			deleteCardCommand.setOnCommandFailedListener(BankCardListActivity.this);
			deleteCardCommand.setServerApiUrl(UrlConstant.USER_DELETE_CARD);
			deleteCardCommand.clearConditions();
			deleteCardCommand.setCondition("memberId","402894e1511f1b6d01511f1bf30d0000");
			deleteCardCommand.setCondition("cardId",deleteCard.cardId);
			showLoadingOverLay(lv_card);
			deleteCardCommand.execute();
		}
	};
	private RdpNetDataSet cardListData;
	
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		hideOverLayView();
		if(UrlConstant.USER_GET_MY_CARD_LIST.equals(result.getUrl())){
			//获取银行卡列表
			mAdapter.setData((List<CardBean>) data);
		}
		if(UrlConstant.USER_DELETE_CARD.equals(result.getUrl())){
			//删除银行卡
			getCardListData(cardListData);
		}
	}
	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {

	}
}
