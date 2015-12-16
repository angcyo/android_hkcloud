package com.huika.cloud.control.me.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.support.helps.AreaInfoDbHelper;
import com.huika.cloud.support.model.AreaInfo;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

/**
 * 
 * @description：区域选择
 * @author ht
 * @date 2015-12-4 上午10:26:52
 */
public class ChoiceProvienceActivity extends RdpBaseActivity implements OnItemClickListener, OnRefreshItemViewsListener {
	private View mMasterView;
	private ListView area_lv;
	private RdpDataListAdapter<AreaInfo> mAdapter;
	private int mLevel = 1;
	private AreaInfoDbHelper mAreaInfoDbHelper;
	private List<AreaInfo> provinceList = new ArrayList<AreaInfo>();
	private AreaInfo mProvienceInfo, mCityInfo, mAreaInfo;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("选择区域");
		mMasterView = addMasterView(R.layout.layout_choice_area);
		area_lv = (ListView) mMasterView.findViewById(R.id.eshop_select_lv);
		mAreaInfoDbHelper = new AreaInfoDbHelper();
		mAdapter = new RdpDataListAdapter<AreaInfo>(mApplication,R.layout.layout_area_info_item);
		mAdapter.setListener(this);
		area_lv.setAdapter(mAdapter);
		area_lv.setOnItemClickListener(this);
		initDatas();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent instanceof ListView) {
			int realPosition = position - ((ListView) parent).getHeaderViewsCount();
			if (realPosition >= 0 && realPosition < mAdapter.getCount()) {
				if (mLevel == 1) {
					mProvienceInfo = provinceList.get(realPosition);
				}
				else if (mLevel == 2) {
					mCityInfo = provinceList.get(realPosition);
				}
				mLevel++;
				if (!initDatas()) {// 如果没有下一级，则终止
					String address = mProvienceInfo.getAreaName() + " " + mCityInfo.getAreaName();
					int areaId = mCityInfo.getAreaId();
					if (provinceList != null && provinceList.size() > 0) {
						mAreaInfo = provinceList.get(realPosition);
						address = address + " " + mAreaInfo.getAreaName();
						areaId = mAreaInfo.getAreaId();
					}
					// 地区选择完毕， 返回调用界面
					Bundle bundle = new Bundle();
					bundle.putString(Constant.CHOICE_AREA_RESULT, address); // 中文地址
					bundle.putInt(Constant.CHOICE_AREA_RESULT_ID, areaId); // 地区ID
					Intent mIntent = getIntent();
					mIntent.putExtras(bundle);
					setResult(Activity.RESULT_OK, mIntent);
					finish();
				}
			}
		}
	}
	
	protected boolean initDatas() {
		switch (mLevel) {
			case 1:
				provinceList = mAreaInfoDbHelper.getProvinceList();
				break;
			case 2:
				provinceList = mAreaInfoDbHelper.getAreaListByParentID(mProvienceInfo.getAreaId());
				break;
			case 3:
				provinceList = mAreaInfoDbHelper.getAreaListByParentID(mCityInfo.getAreaId());
				break;
		}

		if (mLevel > 3 || provinceList == null || provinceList.size() == 0) { return false; }

		mAdapter.setData(provinceList);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		if (mLevel > 1) {
			mLevel--;
			initDatas();
		}
		else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position,
			View convertView, AdapterViewHolder holder) {
		AreaInfo item = mAdapter.getItem(position);
		holder.getTextView(R.id.eshop_address_item_tv).setText(item.getAreaName());
		return false;
	}
	
}
