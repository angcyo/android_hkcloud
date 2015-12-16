package com.huika.cloud.control.home.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

/**
 * @description：搜索
 * @author ht
 * @date 2015-12-4 上午10:29:52
 */
public class SearchActivity extends RdpBaseActivity implements OnRefreshItemViewsListener {
	private static final String NAME = "search_history";
	private static final String HISTORY_SEARCH_STRING = "history_search_string";
	private ImageView back_iv;
	private EditText searchEt;
	private TextView gotoSearch;
	private View mMasterView;
	private LinearLayout search_history;
	private ListView shop_list_history;
	List<String> historySearchList=new ArrayList<String>();//搜索历史
	public String realSearchKey;
	private RdpDataListAdapter<String> historSearchAdapter;
	private SharedPreferences sp;
	private View foot;
	private Button clear_history_button;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		removeLeftFuncView(TBAR_FUNC_BACK);
		mTvTitle.setVisibility(View.GONE);
		mMasterView = addMasterView(R.layout.search_master);
		foot = View.inflate(mApplication, R.layout.activity_serch_mechent_foot, null);
		initView();
		initListener();
		shop_list_history.addFooterView(foot);
		sp=mApplication.getSharedPreferences(NAME, 0);
		historSearchAdapter = new RdpDataListAdapter<String>(mApplication, R.layout.search_history_item);
		historSearchAdapter.setListener(this);
//		initSearchHistorySearch();
		shop_list_history.setAdapter(historSearchAdapter);
		shop_list_history.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String item = historSearchAdapter.getItem(position);
				ToastMsg.showToastMsg(mApplication, "点击的搜索条目为"+item);
				gotoSearchList(item);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initSearchHistorySearch();
	}
	private void initListener() {
		back_iv.setOnClickListener(this);
		gotoSearch.setOnClickListener(this);
		clear_history_button.setOnClickListener(this);
	}

	private void initView() {
		search_history = (LinearLayout) mMasterView.findViewById(R.id.shop_history);
		shop_list_history = (ListView) mMasterView.findViewById(R.id.shop_list_history);
		back_iv = (ImageView) findViewById(R.id.back_iv);
		searchEt = (EditText) findViewById(R.id.searchEt);
		gotoSearch = (TextView) findViewById(R.id.gotoSearch);
		clear_history_button = (Button) foot.findViewById(R.id.clear_history_shop_bt);
	}
	
	@Override
	protected int getBaseLayoutID() {
		return R.layout.search_base_layout;
	}
	
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
		case R.id.back_iv:
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
//		initSearchHistorySearch();
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
}
