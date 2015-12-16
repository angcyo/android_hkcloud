package com.huika.cloud.control.me.activity;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.support.model.BankBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpNetListBaseActivity;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;

public class BankListActivity extends RdpNetListBaseActivity implements OnItemClickListener {
	public static final String SELECTED_BANK ="selected_bank";
	public static final int RESULT_CODE = 1;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("选择开户银行");
		mMasterAdapter.setItemLayoutID(R.layout.bank_lv_item);
		mLvMaster.setOnItemClickListener(this);
	}

	@Override
	protected void openDataSet() {
		super.openDataSet();
		// 设置请求参数
		mDataSet.setServerApiUrl(UrlConstant.USER_ALL_BANK);
		mDataSet.clearConditions();
		mDataSet.setTypeOfResult(new TypeToken<List<BankBean>>(){}.getType());
		mDataSet.open();
	}
	
	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
		BankBean bankBean= (BankBean) mDataSet.getRecord(position);
		RdpImageLoader.displayImage(bankBean.bankLogoUrl, holder.getImageView(R.id.iv_bank_logo));
		holder.getTextView(R.id.tv_bank_name).setText(bankBean.bankName);
		return super.onRefreshItemViews(adapter, position, convertView, holder);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		BankBean bankBean= (BankBean)mDataSet.getRecord(position-mLvMaster.getRefreshableView().getHeaderViewsCount());
		Intent intent = new Intent();
		intent.putExtra(SELECTED_BANK, bankBean);
		setResult(RESULT_CODE, intent);
		finish();
	}
}
