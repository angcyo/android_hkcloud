package com.huika.cloud.control.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.home.adapter.GoodTypeAdapter;
import com.huika.cloud.support.model.OneTypeBean;
import com.huika.cloud.support.model.ThreeTypeBean;
import com.huika.cloud.support.model.TwoTypeBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandSuccessedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpInnerGridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @description：商品分类
 * @author ht
 * @date 2015-12-4 上午10:29:16
 */
public class GoodTypesActivity extends RdpBaseActivity implements
		OnRefreshItemViewsListener, OnItemClickListener {
	private View mMasterView;
	private ListView first_classify;
	private ListView second_classify;
	private GoodTypeAdapter<OneTypeBean> firstAdapter;
	private RdpNetDataSet firstDataSet;
	private RdpNetDataSet secondDataSet;
	private GoodTypeAdapter<TwoTypeBean> secondAdapter;
	private RdpDataListAdapter<ThreeTypeBean> threeAdapter;
	private RelativeLayout rl_search_bar;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("分类");
		removeLeftFuncView(TBAR_FUNC_BACK);
		rl_search_bar = (RelativeLayout) findViewById(R.id.rl_search_bar);
		mMasterView = addMasterView(R.layout.classify_layout);
		first_classify = (ListView) mMasterView
				.findViewById(R.id.first_classify);
		second_classify = (ListView) mMasterView
				.findViewById(R.id.second_classify);
		
		firstAdapter = new GoodTypeAdapter<OneTypeBean>(mApplication,
				R.layout.first_classify_item);
		secondAdapter = new GoodTypeAdapter<TwoTypeBean>(mApplication,
				R.layout.second_type_item);

		firstDataSet = new RdpNetDataSet(mApplication);
		secondDataSet = new RdpNetDataSet(mApplication);

		first_classify.setAdapter(firstAdapter);
		second_classify.setAdapter(secondAdapter);

		initListener();
		// 获取一级分类列表数据
		getFirstCategory();
	}

	private void getFirstCategory() {
		firstDataSet
		.setServerApiUrl(UrlConstant.GET_PRODUCT_CATEGORY_LIST);
		firstDataSet.clearConditions();
		firstDataSet.setCondition("categoryId", "");
		firstDataSet.setCondition("merchantId", "402881e8461795c201461795c2e90000");

		Type typeOfResult = new TypeToken<ArrayList<OneTypeBean>>() {
		}.getType();
		firstDataSet.setTypeOfResult(typeOfResult);
		firstDataSet.open();
	}

	private void initListener() {
		rl_search_bar.setOnClickListener(this);
		first_classify.setOnItemClickListener(this);
		firstAdapter.setListener(this);
		firstDataSet.setOnCommandSuccessedListener(this);
		firstDataSet.setOnCommandFailedListener(this);
		secondDataSet
				.setOnCommandSuccessedListener(new OnCommandSuccessedListener() {
					@Override
					public void onCommandSuccessed(Object reqKey,
							RdpResponseResult result, Object data) {
						hideOverLayView();
						secondAdapter.setData((List<TwoTypeBean>) data);
					}
				});
		first_classify.setOnItemClickListener(this);
		secondAdapter.setListener(new OnRefreshItemViewsListener() {

			@Override
			public boolean onRefreshItemViews(RdpAdapter adapter, int position,
					View convertView, AdapterViewHolder holder) {
				final TwoTypeBean twoTypeBean = (TwoTypeBean) secondDataSet
						.getRecord(position);
				holder.getTextView(R.id.tv_title_icm).setText(
						twoTypeBean.categoryName);
				RdpInnerGridView rigv = (RdpInnerGridView) holder
						.getView(R.id.gv_content_icm);
				threeAdapter = new RdpDataListAdapter<ThreeTypeBean>(mApplication,
						R.layout.three_type_item);
				threeAdapter.setListener(new OnRefreshItemViewsListener() {
					@Override
					public boolean onRefreshItemViews(RdpAdapter adapter,
							int position, View convertView,
							AdapterViewHolder holder) {
						ThreeTypeBean threeTypeBean = threeAdapter
								.getItem(position);
						RdpImageLoader.displayImage(threeTypeBean.categoryImage,
								holder.getImageView(R.id.iv_classify_iccg), R.drawable.ic_launcher);
						holder.getTextView(R.id.tv_classify_iccg).setText(
								threeTypeBean.categoryName);
						return false;
					}
				});
				threeAdapter.setData(twoTypeBean.children);
				rigv.setAdapter(threeAdapter);
				rigv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ThreeTypeBean threeTypeBean = twoTypeBean.children.get(position);
						Intent intent=new Intent(mApplication, ProductListActivity.class);
						intent.putExtra("categoryId", threeTypeBean.categoryId);
						intent.putExtra("categoryName", threeTypeBean.categoryName);
						intent.putExtra("coming", "category");
						startActivity(intent);
					}
				});
				return false;
			}});
	}

	@Override
	protected int getBaseLayoutID() {
		return R.layout.type_title_bar;
	}

	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position,
			View convertView, AdapterViewHolder holder) {
		holder.getTextView(R.id.tv_menu_icm).setTextColor(Color.BLACK);
		OneTypeBean oneTypeBean = (OneTypeBean) firstDataSet
				.getRecord(position);
		if (firstAdapter.getSelectedItem() == position) {
			holder.getTextView(R.id.tv_menu_icm).setTextColor(Color.RED);
		}
		holder.getTextView(R.id.tv_menu_icm).setText(oneTypeBean.categoryName);
		return false;
	}

	private void getSecondTypeData(String classifyId) {
		showLoadingOverLay(second_classify);
		secondDataSet.setServerApiUrl(UrlConstant.GET_PRODUCT_CATEGORY_LIST);
		secondDataSet.clearConditions();
		secondDataSet.setCondition("categoryId", classifyId);
		secondDataSet.setCondition("merchantId", "402881e8461795c201461795c2e90000");

		Type typeOfResult = new TypeToken<ArrayList<TwoTypeBean>>() {
		}.getType();
		secondDataSet.setTypeOfResult(typeOfResult);
		secondDataSet.open();
	}

	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result,
			Object data) {
		firstAdapter.setData((List<OneTypeBean>) data);
		firstAdapter.setSelectedItem(0);
		OneTypeBean bean = (OneTypeBean) firstDataSet.getRecord(0);
		if (bean != null) {
			getSecondTypeData(bean.categoryId);
		}
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		OneTypeBean oneTypeBean = (OneTypeBean) firstAdapter.getItem(position);
		if (firstAdapter.getSelectedItem() != position) {
			getSecondTypeData(oneTypeBean.categoryId);
		}
		firstAdapter.setSelectedItem(position);
		firstAdapter.notifyDataSetChanged();
		first_classify.setSelectionFromTop(position, 0);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_search_bar:
			Toast.makeText(mApplication, "条状到搜索界面", Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(mApplication, SearchActivity.class);
			startActivity(intent);
			break;
		default:
			super.onClick(v);
			break;
		}
	}
}
