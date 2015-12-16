package com.huika.cloud.control.home.fragment;

import java.lang.reflect.Type;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.home.activity.ProductListActivity;
import com.huika.cloud.support.model.AttrBean;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpFramework.RdpFragment.RdpBaseFragment;

@SuppressLint("NewApi")
public class FilterFragment extends RdpBaseFragment {
	@Override
	protected void initFragment() {
		super.initFragment();
		mTitleBar.setVisibility(View.GONE);
		addMasterView(R.layout.first_filter);
		Bundle arguments = getArguments();
		String categoryId = arguments.getString("category", "");
		
		RdpNetDataSet oneLevelData = new RdpNetDataSet(mApplication);
		oneLevelData.setServerApiUrl(UrlConstant.GET_PRODUCT_FIRST_FILTER);
		oneLevelData.clearConditions();
		oneLevelData.setOnCommandFailedListener(this);
		oneLevelData.setOnCommandSuccessedListener(this);
		oneLevelData.setCondition("categoryId", categoryId);
		oneLevelData.setCondition("merchantId", "402881e8461795c201461795c2e90000");
		Type filterResult = new TypeToken<List<AttrBean>>() {
		}.getType();
		oneLevelData.setTypeOfResult(filterResult);
		oneLevelData.open();
	}
}
