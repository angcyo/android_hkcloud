package com.huika.cloud.control.home.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huika.cloud.R;
import com.huika.cloud.support.event.FinishEvent;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @description：搜索
 * @author ht
 * @date 2015-12-4 上午10:29:52
 */
public class SearchActivity extends RdpBaseActivity implements OnRefreshItemViewsListener, OnItemClickListener {
	private static final String NAME = "search_history";
	private static final String HISTORY_SEARCH_STRING = "history_search_string";
	public String realSearchKey;
	List<String> historySearchList = new ArrayList<String>();//搜索历史
	@ViewInject(R.id.searchEt)
	private EditText searchEt;
	private View mMasterView;
	@ViewInject(R.id.shop_history)
	private LinearLayout search_history;
	@ViewInject(R.id.shop_list_history)
	private ListView shop_list_history;
	private RdpDataListAdapter<String> historSearchAdapter;
	private SharedPreferences sp;
	private View foot;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		EventBus.getDefault().register(this);
		removeLeftFuncView(TBAR_FUNC_BACK);
		mTvTitle.setVisibility(View.GONE);
		mMasterView = addMasterView(R.layout.search_master);
		RdpAnnotationUtil.inject(this);
		foot = View.inflate(mApplication, R.layout.activity_serch_mechent_foot, null);
		shop_list_history.addFooterView(foot);
		sp=mApplication.getSharedPreferences(NAME, 0);
		historSearchAdapter = new RdpDataListAdapter<String>(mApplication, R.layout.search_history_item);
		historSearchAdapter.setListener(this);
		shop_list_history.setAdapter(historSearchAdapter);
		shop_list_history.setOnItemClickListener(this);
	}

	public void onEventMainThread(FinishEvent event) {
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initSearchHistorySearch();
	}
	
	@Override
	protected int getBaseLayoutID() {
		return R.layout.search_base_layout;
	}

	@OnClick({R.id.gotoSearch, R.id.iv_back_icon, R.id.clear_history_shop_bt})
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gotoSearch:
			//跳转到搜索列表
			String keyword = null;
			if (!TextUtils.isEmpty(searchEt.getText().toString())) {
				keyword = searchEt.getText().toString();
			} else {
				if (!TextUtils.isEmpty(searchEt.getHint())) {
					if(realSearchKey==null){
						return ;
					}else{
						keyword= realSearchKey;
					}
				} else {
					return;
				}
			}
			gotoSearchList(keyword);
			break;
			case R.id.iv_back_icon:
			finish();
			break;
		case R.id.clear_history_shop_bt:
			deleteHistorySearch();
			break;
		default:
			super.onClick(v);
			break;
		}
	}

	private void gotoSearchList(String keyword) {
		Intent intent=new Intent(mApplication, ProductListActivity.class);
		intent.putExtra("keyword", keyword);
		intent.putExtra("coming", "search");
		startActivity(intent);
		// 判断搜索记录中是否已有，有则将其调到最前面，而不是添加
		if (historySearchList.contains(keyword)) {
			historySearchList.remove(keyword);
		}
		historySearchList.add(0, keyword);
		// 保存搜索记录到本地
		saveHistorySearch();
	}
	/*初始化搜索历史*/
	private void saveHistorySearch() {
		StringBuilder tempSave = new StringBuilder();
		// 保存搜索历史
		for (int i = 0; i < historySearchList.size(); i++) {
			if (i == 0) {
				tempSave.append(historySearchList.get(i));
			} else {
				tempSave.append("￡" + historySearchList.get(i));
			}
		}
		sp.edit().putString(HISTORY_SEARCH_STRING, tempSave.toString()).commit();
	}

	// 清空搜索历史
	private void deleteHistorySearch() {
		sp.edit().putString(HISTORY_SEARCH_STRING, "").commit();
		historySearchList.clear();
		historSearchAdapter.notifyDataSetChanged();
		search_history.setVisibility(View.GONE);
	}

	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position,
									  View convertView, AdapterViewHolder holder) {
		String item = (String) adapter.getItem(position);
		holder.getTextView(R.id.shop_item_name).setText(item);
		return false;
	}
	
	private void initSearchHistorySearch() {
		String content = sp.getString(HISTORY_SEARCH_STRING, "");
		if (!TextUtils.isEmpty(content)) {
			historySearchList.clear();
			if (content.contains("￡")) {
				String[] split = content.split("￡");
				if (split.length > 0) {
					historySearchList.addAll(Arrays.asList(split));
				}
			} else {
				historySearchList.add(content);
			}
			if (!historySearchList.isEmpty()) {
				historSearchAdapter.setData(historySearchList);
//				change(0);
			}
		}else{
			search_history.setVisibility(View.GONE);
		}
		if(historySearchList.size()>0){
			search_history.setVisibility(View.VISIBLE);
//			if(foot!=null){
//				shop_list_history.addFooterView(foot);
//			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String item = historSearchAdapter.getItem(position);
		gotoSearchList(item);
	}

}
