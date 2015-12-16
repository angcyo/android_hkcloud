package com.huika.cloud.control.home.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.AutoScrollViewPager;
import android.support.v4.view.AutoScrollViewPager.OnPageItemClickListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huika.cloud.R;
import com.huika.cloud.control.home.activity.GoodTypesActivity;
import com.huika.cloud.control.home.activity.SearchActivity;
import com.huika.cloud.support.model.ProductBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpApplication;
import com.zhoukl.androidRDP.RdpFramework.RdpFragment.RdpBaseFragment;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpInnerGridView;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpInnerListView;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;

/**
 * @description：首页
 * @date 2015-11-9 上午10:16:14
 */
@SuppressLint({ "NewApi", "ResourceAsColor" })
public class HomeFragment extends RdpBaseFragment implements OnRefreshItemViewsListener {

	private static final int left_iv_id = 0;
	private static final int right_iv_id = 1;
	private static final int rl_id = 2;
	private View mMasterView;
	private LinearLayout m_ll;
	private PullToRefreshScrollView ptrsv;
	private LinearLayout titleLeft;
	private RelativeLayout search;

	class TitleBarSetting {
		public String name;
		public int type_align;// 0 左对齐 1居中 2右对齐
		public int show_right;// 0不显示 1显示
		public int text_color;
		public int backgroud;
		public int left_icon;
		public int right_icon;
		public String link;
	}

	class BlankStrip {
		public int height;
	}

	class ListType {
		public int type;
		public String url;
	}

	class AdVpSetting {
		public int show_type;// 0轮播显示 1列表显示
		public List<ProductBean> productBeans;
	}

	class SearchBarBean {

	}

	class NavigationBean {

	}

	@Override
	protected void initFragment() {
		super.initFragment();
		mTvTitle.setVisibility(View.GONE);
		removeLeftFuncView(TBAR_FUNC_BACK);
		mMasterView = addMasterView(R.layout.master_layout);
		initView();
		initListener();
		ptrsv.setMode(Mode.BOTH);
		m_ll = (LinearLayout) mMasterView.findViewById(R.id.m_ll);
		ArrayList<Object> arrayList = fillDemoData();
		addViewBySetting(arrayList);
	}

	private void initListener() {

	}

	private void initView() {
		ptrsv = (PullToRefreshScrollView) mMasterView.findViewById(R.id.ptrsv);
	}

	private ArrayList<Object> fillDemoData() {

		ArrayList<Object> arrayList = new ArrayList<Object>();

		SearchBarBean searchBarBean = new SearchBarBean();
		arrayList.add(searchBarBean);

		AdVpSetting adVpSetting1 = new AdVpSetting();
		adVpSetting1.show_type = 0;
		adVpSetting1.productBeans = ProductBean.getDemoData4();
		arrayList.add(adVpSetting1);

		TitleBarSetting titleBarSetting1 = new TitleBarSetting();
		titleBarSetting1.backgroud = R.color.bgColor;
		titleBarSetting1.name = "品牌推荐";
		titleBarSetting1.show_right = 0;
		titleBarSetting1.text_color = Color.RED;
		titleBarSetting1.type_align = 1;
		titleBarSetting1.left_icon = R.drawable.ic_launcher;
		titleBarSetting1.right_icon = R.drawable.ic_launcher;
		titleBarSetting1.link = "跳转到品牌推荐";

		arrayList.add(titleBarSetting1);

		NavigationBean navigationBean = new NavigationBean();
		arrayList.add(navigationBean);

		SearchBarBean searchBarBean2 = new SearchBarBean();
		arrayList.add(searchBarBean2);

		BlankStrip blankStrip3 = new BlankStrip();
		blankStrip3.height = 10;
		arrayList.add(blankStrip3);

		ListType listType1 = new ListType();
		listType1.type = 1;
		listType1.url = "1";
		arrayList.add(listType1);

		BlankStrip blankStrip = new BlankStrip();
		blankStrip.height = 20;
		arrayList.add(blankStrip);

		AdVpSetting adVpSetting2 = new AdVpSetting();
		adVpSetting2.show_type = 1;
		adVpSetting2.productBeans = ProductBean.getDemoData3();
		arrayList.add(adVpSetting2);

		BlankStrip blankStrip2 = new BlankStrip();
		blankStrip2.height = 50;

		arrayList.add(blankStrip2);

		ListType listType4 = new ListType();
		listType4.type = 3;
		listType4.url = "1";
		arrayList.add(listType4);
		
		TitleBarSetting titleBarSetting2 = new TitleBarSetting();
		titleBarSetting2.backgroud = Color.YELLOW;
		titleBarSetting2.name = "更多惊喜";
		titleBarSetting2.show_right = 1;
		titleBarSetting2.text_color = Color.BLUE;
		titleBarSetting2.type_align = 2;
		titleBarSetting2.left_icon = R.drawable.ic_launcher;
		titleBarSetting2.link = "跳转到更多惊喜界面";

		arrayList.add(titleBarSetting2);

		ListType listType2 = new ListType();
		listType2.type = 0;
		listType2.url = "2";

		arrayList.add(listType2);

		return arrayList;
	}

	// 根据配置添加view
	private void addViewBySetting(ArrayList<Object> arrayList) {
		for (int i = 0; i < arrayList.size(); i++) {
			Object object = arrayList.get(i);
			if (object instanceof TitleBarSetting) {
				addTitleBar((TitleBarSetting) object);
			}
			if (object instanceof BlankStrip) {
				addBlankStrip((BlankStrip) object);
			}
			if (object instanceof ListType) {
				addListView((ListType) object);
			}
			if (object instanceof AdVpSetting) {
				addVpView((AdVpSetting) object);
			}
			if (object instanceof SearchBarBean) {
				addSearchBar((SearchBarBean) object);
			}
			if (object instanceof NavigationBean) {
				addNavigationView((NavigationBean) object);
			}
		}
	}

	/**添加搜索栏*/
	private void addSearchBar(SearchBarBean searchBarBean) {
		RelativeLayout sRl=new RelativeLayout(mApplication);
		RelativeLayout rl = new RelativeLayout(mApplication);
		rl.setBackground(getResources().getDrawable(R.drawable.search_et_bg));
		ImageView searchIcon = new ImageView(mApplication);
		searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.search_icon));
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams.setMargins(0, 0, 10, 0);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		rl.addView(searchIcon, layoutParams);
		
		TextView textView=new TextView(mApplication);
		android.widget.RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParams4.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams4.setMargins(0, 0, 0, 0);
		textView.setHintTextColor(R.color.common_edit_hint_color);
		textView.setHint("商品搜索：请输入商品关键字");
		textView.setBackground(null);
		textView.setTextSize(12);
		rl.addView(textView,layoutParams4);

		android.widget.RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams2.setMargins(12, 5, 12, 5);
		
		rl.setFocusable(true);
		rl.setFocusableInTouchMode(true);
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mApplication, SearchActivity.class));
			}
		});
		sRl.addView(rl,layoutParams2);
		android.widget.RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		m_ll.addView(sRl,layoutParams3);
	}

	/**添加广告轮播图*/
	private void addVpView(AdVpSetting adVpSetting) {
		// 轮播显示
		if (adVpSetting.show_type == 0) {
			RelativeLayout rl = new RelativeLayout(mApplication);
			AutoScrollViewPager<ProductBean> asvp = new AutoScrollViewPager<ProductBean>(mApplication, null);
			android.widget.RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			rl.addView(asvp, layoutParams);

			LinearLayout ll = new LinearLayout(mApplication);
			ll.setPadding(0, 4, 0, 4);
			RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
			rl.addView(ll, layoutParams2);

			android.widget.RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 142);
			m_ll.addView(rl, layoutParams3);

			asvp.refreshDatas(adVpSetting.productBeans, ll, false, true);
			asvp.start(5000);
			asvp.setOnPageItemClickListener(new OnPageItemClickListener<ProductBean>() {

				@Override
				public void onPageItemClickListener(ProductBean pd) {
					Toast.makeText(mApplication, "点击了轮播图" + pd.getProductName(), Toast.LENGTH_SHORT).show();
				}
			});
		}
		else if (adVpSetting.show_type == 1) {
			RdpInnerListView rilv = new RdpInnerListView(mApplication);
			rilv.setSelector(new ColorDrawable(Color.TRANSPARENT));
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			m_ll.addView(rilv, layoutParams);

			final RdpDataListAdapter<ProductBean> adapter = new RdpDataListAdapter<ProductBean>(mApplication, R.layout.item_ad);
			adapter.setListener(new OnRefreshItemViewsListener() {
				@Override
				public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
					ProductBean productBean = (ProductBean) adapter.getItem(position);
					RdpImageLoader.displayImage(productBean.getImageUrl(), holder.getImageView(R.id.iv_ad_item));
					return false;
				}
			});
			adapter.setData(adVpSetting.productBeans);
			rilv.setAdapter(adapter);
			rilv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ProductBean productBean = adapter.getItem(position);
					ToastMsg.showToastMsg(mApplication, "点击的广告" + productBean.getProductName());
				}
			});
		}
		else {
			return;
		}

	}

	/**添加列表*/
	private void addListView(ListType listType) {
		if (listType.type == 3) {
			RdpInnerListView rilv = new RdpInnerListView(mApplication);
			rilv.setSelector(new ColorDrawable(Color.TRANSPARENT));
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			m_ll.addView(rilv, layoutParams);

//			MultiTypeListAdapter multiTypeListAdapter = new MultiTypeListAdapter(mApplication);
//			rilv.setAdapter(multiTypeListAdapter);
//			// 获取商品列表数据
//			List<ProductBean> demoData = null;
//			if ("1".equals(listType.url)) {
//				demoData = ProductBean.getDemoData();
//			}
//			else if ("2".equals(listType.url)) {
//				demoData = ProductBean.getDemoData2();
//			}
//			multiTypeListAdapter.setmDataList(demoData);
			
			final MultiTypeAdapter<ProductBean> multiTypeAdapter = new MultiTypeAdapter<ProductBean>(mApplication, R.layout.list_type_mul_item);
			multiTypeAdapter.setListener(new OnRefreshItemViewsListener() {
				
				@Override
				public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
					ProductBean firstItem =null;
					ProductBean secondItem = null;
					ProductBean zeroItem=null;
					holder.getView(R.id.rl_zero_item).setVisibility(View.VISIBLE);
					holder.getView(R.id.rl_second_item).setVisibility(View.VISIBLE);
					holder.getView(R.id.rl_first_item).setVisibility(View.VISIBLE);
					if(position/2*3<multiTypeAdapter.mDataList.size()){
						zeroItem= multiTypeAdapter.mDataList.get(position/2*3);
					}
					if(position/2*3+1<multiTypeAdapter.mDataList.size()){
						firstItem = multiTypeAdapter.mDataList.get(position/2*3+1);
					}
					if(position/2*3+2<multiTypeAdapter.mDataList.size()){
						secondItem = multiTypeAdapter.mDataList.get(position/2*3+2);
					}
					if(firstItem!=null){
						setValueByFirstItem(holder, firstItem);
					}else{
						holder.getView(R.id.rl_first_item).setVisibility(View.GONE);
					}
					if(secondItem!=null){
						setValueBySecondItem(holder, secondItem);
					}else{
						holder.getView(R.id.rl_second_item).setVisibility(View.GONE);
					}
					if(zeroItem!=null){
						setValueByZeroItem(holder, zeroItem);
					}else{
						holder.getView(R.id.rl_zero_item).setVisibility(View.GONE);
					}
					return false;
				}

			});
			rilv.setAdapter(multiTypeAdapter);
			// 获取商品列表数据
			List<ProductBean> demoData = null;
			if ("1".equals(listType.url)) {
					demoData = ProductBean.getDemoData();
			}
			else if ("2".equals(listType.url)) {
					demoData = ProductBean.getDemoData2();
			}
			multiTypeAdapter.setmDataList(demoData);
		}
		else if (listType.type == 1) {
			RdpInnerListView rilv = new RdpInnerListView(mApplication);
			rilv.setSelector(new ColorDrawable(Color.TRANSPARENT));
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			m_ll.addView(rilv, layoutParams);
			final RdpDataListAdapter<ProductBean> mAdapter = new RdpDataListAdapter<ProductBean>(mApplication, R.layout.list_item_type_one);
			mAdapter.setListener(new OnRefreshItemViewsListener() {
				
				@Override
				public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
					ProductBean firstItem =null;
					ProductBean secondItem = null;
					holder.getView(R.id.rl_first_item).setVisibility(View.VISIBLE);
					holder.getView(R.id.rl_second_item).setVisibility(View.VISIBLE);
					if(position*2<mAdapter.getCount()){
						firstItem = mAdapter.getItem(position*2);
					}
					if(position*2+1<mAdapter.getCount()){
						secondItem =mAdapter.getItem(position*2+1);
					}
					if(firstItem!=null){
						setValueByFirstItem(holder, firstItem);
					}else{
						holder.getView(R.id.rl_first_item).setVisibility(View.GONE);
					}
					if(secondItem!=null){
						setValueBySecondItem(holder, secondItem);
					}else{
						holder.getView(R.id.rl_second_item).setVisibility(View.GONE);
					}
					return false;
				}
			});
			rilv.setAdapter(mAdapter);
			// 获取商品列表数据
			List<ProductBean> demoData = null;
			if ("1".equals(listType.url)) {
					demoData = ProductBean.getDemoData();
			}
			else if ("2".equals(listType.url)) {
					demoData = ProductBean.getDemoData2();
			}
			mAdapter.setData(demoData);
		}
		else {
			RdpInnerGridView gv = new RdpInnerGridView(mApplication);
			gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			m_ll.addView(gv, layoutParams);
			final RdpDataListAdapter<ProductBean> mAdapter;
			if (listType.type == 0) {
				mAdapter = new RdpDataListAdapter<ProductBean>(mApplication, R.layout.list_item_type_zero);
			}
			else if (listType.type == 2) {
				mAdapter = new RdpDataListAdapter<ProductBean>(mApplication, R.layout.list_item_type_two);
			}
			else {
				// hou..
				return;
			}
			mAdapter.setListener(this);
			gv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ProductBean item = mAdapter.getItem(position);
					Toast.makeText(mApplication, "点击了商品" + item.getProductName(), Toast.LENGTH_SHORT).show();
				}
			});
			gv.setAdapter(mAdapter);

			// 获取商品列表数据
			List<ProductBean> demoData = null;
			if ("1".equals(listType.url)) {
				demoData = ProductBean.getDemoData();
			}
			else if ("2".equals(listType.url)) {
				demoData = ProductBean.getDemoData2();
			}
			mAdapter.setData(demoData);
		}
	}

	private void setValueByFirstItem(AdapterViewHolder holder, final ProductBean firstItem) {
		holder.getView(R.id.rl_first_item).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastMsg.showToastMsg(mApplication, "点击了商品"+firstItem.getProductName());
			}
		});
		RdpImageLoader.displayImage(firstItem.getImageUrl(), holder.getImageView(R.id.iv_icon_1));
		holder.getTextView(R.id.tv_name_1).setText(firstItem.getProductName());
		holder.getTextView(R.id.tv_price_1).setText(firstItem.getShopPrice()+"");
	}
	
	private void setValueByZeroItem(AdapterViewHolder holder, final ProductBean zeroItem) {
		holder.getView(R.id.rl_zero_item).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastMsg.showToastMsg(mApplication, "点击了"+zeroItem.getProductName());
			}
		});
		RdpImageLoader.displayImage(zeroItem.getImageUrl(), holder.getImageView(R.id.iv_icon));
		holder.getTextView(R.id.tv_name).setText(zeroItem.getProductName());
		holder.getTextView(R.id.tv_price).setText(zeroItem.getShopPrice()+"");
	}

	private void setValueBySecondItem(AdapterViewHolder holder, final ProductBean secondItem) {
		holder.getView(R.id.rl_second_item).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastMsg.showToastMsg(mApplication, "点击了"+secondItem.getProductName());
			}
		});
		RdpImageLoader.displayImage(secondItem.getImageUrl(), holder.getImageView(R.id.iv_icon_2));
		holder.getTextView(R.id.tv_name_2).setText(secondItem.getProductName());
		holder.getTextView(R.id.tv_price_2).setText(secondItem.getShopPrice()+"");
	}
	
	/**添加导航栏*/
	private void addNavigationView(NavigationBean navigationBean) {
		RdpInnerGridView gv = new RdpInnerGridView(mApplication);
		gv.setNumColumns(4);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		m_ll.addView(gv, layoutParams);
		final MyAdapter<ProductBean> mAdapter = new MyAdapter<ProductBean>(mApplication, R.layout.navigation_item);
		mAdapter.setListener(new OnRefreshItemViewsListener() {
			@Override
			public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
				ProductBean productBean = mAdapter.getItem(position);
				RdpImageLoader.displayImage(productBean.getImageUrl(), holder.getImageView(R.id.iv_nav_icon));
				holder.getTextView(R.id.tv_nav).setText(productBean.getProductName());
				return false;
			}
		});
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProductBean productBean = mAdapter.getItem(position);
				ToastMsg.showToastMsg(mApplication, "点击的导航栏" + productBean.getProductName());
				if (position==7) {
					startActivity(new Intent(mApplication, GoodTypesActivity.class));
				}
			}
		});
		gv.setAdapter(mAdapter);
		// 获取商品列表数据
		List<ProductBean> demoData = null;
		demoData = ProductBean.getDemoData();
		mAdapter.setmDataList(demoData);
	}

	/**添加空白条*/
	private void addBlankStrip(BlankStrip blankStrip) {
		RelativeLayout margin_blank = new RelativeLayout(mApplication);
		margin_blank.setBackgroundColor(Color.RED);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, blankStrip.height);
		m_ll.addView(margin_blank, layoutParams);
	}

	/**添加标题栏*/
	private void addTitleBar(final TitleBarSetting titleBarSetting1) {
		RelativeLayout first_rl = new RelativeLayout(mApplication);
		// 添加左边的图标
		first_rl.setBackgroundColor(titleBarSetting1.backgroud);
		first_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mApplication, "点击的标题栏" + titleBarSetting1.link, Toast.LENGTH_SHORT).show();
			}
		});

		ImageView left_iv = new ImageView(mApplication);
		left_iv.setImageDrawable(getResources().getDrawable(titleBarSetting1.left_icon));
		left_iv.setId(left_iv_id);
		RelativeLayout.LayoutParams left_lp = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		left_lp.addRule(RelativeLayout.CENTER_VERTICAL);
		first_rl.addView(left_iv, left_lp);
		// 添加右边的图标
		ImageView right_iv = null;
		if (titleBarSetting1.show_right == 1) { 
			right_iv = new ImageView(mApplication);
			right_iv.setImageResource(titleBarSetting1.right_icon);
			right_iv.setId(right_iv_id);
			RelativeLayout.LayoutParams right_lp = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			right_lp.addRule(RelativeLayout.CENTER_VERTICAL);
			right_lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			first_rl.addView(right_iv, right_lp);
		}

		// 设置标题
		TextView titleText = new TextView(mApplication);
		titleText.setText(titleBarSetting1.name);
		titleText.setTextColor(titleBarSetting1.text_color);
		RelativeLayout.LayoutParams title_lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		if (titleBarSetting1.type_align == 1) {
			// 居中
			title_lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		}
		else if (titleBarSetting1.type_align == 0) {
			// 左对齐
			if (left_iv != null) {
				title_lp.addRule(RelativeLayout.RIGHT_OF, left_iv.getId());
				title_lp.addRule(RelativeLayout.CENTER_VERTICAL);
			}
		}
		else {
			// 右对齐
			if (right_iv != null) {
				title_lp.addRule(RelativeLayout.LEFT_OF, right_iv.getId());
			}
			else {
				title_lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			}
			title_lp.addRule(RelativeLayout.CENTER_VERTICAL);
		}
		first_rl.addView(titleText, title_lp);

		LayoutParams params = new LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, 35);
		m_ll.addView(first_rl, params);
	}

	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
		ProductBean item = (ProductBean) adapter.getItem(position);
		holder.getTextView(R.id.tv_name).setText(item.getProductName());
		holder.getTextView(R.id.tv_price).setText("$ " + item.getShopPrice());
		RdpImageLoader.displayImage(item.getImageUrl(), holder.getImageView(R.id.iv_icon));
		return false;
	}

	class MyAdapter<T> extends RdpDataListAdapter {
		protected List<T> mDataList;

		public MyAdapter(Context context) {
			super(context);
		}

		public MyAdapter(Context context, int itemLayoutID) {
			this(context);
			mItemLayoutID = itemLayoutID;
		}

		@Override
		public int getCount() {
			if (mDataList == null) return 0;
			if (mDataList.size() <= 8) {
				return mDataList.size();
			}
			else {
				return 8;
			}
		}

		@Override
		public T getItem(int position) {
			if (mDataList == null) return null;
			else return mDataList.get(position);
		}

		public void setmDataList(List<T> mDataList) {
			this.mDataList = mDataList;
			notifyDataSetChanged();
		}
	}
	
	class TwoItemAdapter extends RdpDataListAdapter{

		public TwoItemAdapter(Context context, int itemLayoutID) {
			super(context, itemLayoutID);
		}

		public TwoItemAdapter(Context context) {
			super(context);
		}
		
		@Override
		public int getCount() {
			if (mDataList == null) return 0;
			if (mDataList.size()%2!=0) {
				return mDataList.size()/2+1;
			}
			else{
				return mDataList.size()/2;
			}
		}
		
	}
	
	
	/**一个条目三个布局*/
	class MultiTypeAdapter<T> extends RdpDataListAdapter{
		
		protected List<T> mDataList;
		
		public MultiTypeAdapter(Context context, int itemLayoutID) {
			super(context, itemLayoutID);
		}

		public MultiTypeAdapter(Context context) {
			super(context);
		}
		
		@Override
		public int getCount() {
			if (mDataList == null) return 0;
			if (mDataList.size()%3!=0) {
				return mDataList.size()/3*2+1;
			}
			else{
				return mDataList.size()/3*2;
			}
		}

		public void setmDataList(List<T> mDataList) {
			this.mDataList = mDataList;
			notifyDataSetChanged();
		}
		
	}
	
	/**按type分类的adapter*/
//	class MultiTypeListAdapter extends RdpDataListAdapter {
//
//		private static final int TYPE_1 = R.layout.list_item_type_one;
//		private static final int TYPE_2 = R.layout.list_mul_type_item;
//		List<ProductBean> mDataList;
//
//		public MultiTypeListAdapter(Context context) {
//			super(context);
//		}
//
//		public MultiTypeListAdapter(Context context, int itemLayoutID) {
//			super(context, itemLayoutID);
//		}
//
//		public void setmDataList(List<ProductBean> mDataList) {
//			this.mDataList = mDataList;
//			notifyDataSetChanged();
//		}
//
//		@Override
//		public int getViewTypeCount() {
//			return 2;
//		}
//
//		@Override
//		public int getItemViewType(int position) {
//			if (position % 2 == 0) {
//				return 0;
//			}
//			else {
//				return 1;
//			}
//		}
//
//		@Override
//		public int getCount() {
//			if (mDataList == null) {
//				return 0;
//			}
//			else if (mDataList.size() % 3 == 0) {
//				return (mDataList.size() / 3)*2;
//			}
//			else {
//				return (mDataList.size() / 3)*2 + 1;
//			}
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			int itemViewType = getItemViewType(position);
//			ViewHolder holder = null;
//			ViewHolderTwo holderTwo=null;
//			if (convertView == null) {
//				 switch (itemViewType) {
//				 case 1:
//    				 convertView=View.inflate(mApplication,TYPE_1, null);
//    				 holder = initHolder(convertView);
//    				 convertView.setTag(holder);
//				 break;
//				 case 0:
//    				 convertView=View.inflate(mApplication, TYPE_2, null);
//    				 holderTwo = initHolderTwo(convertView);
//    				 convertView.setTag(holderTwo);
//				 break;
//				 default:
//				 break;
//				 }
//			}
//			else {
//				switch (itemViewType) {
//					case 1:
//						holder = (ViewHolder) convertView.getTag();
//						break;
//					case 0:
//						holderTwo = (ViewHolderTwo) convertView.getTag();
//						break;
//					default:
//						break;
//				}
//			}
//			if(position%2==0){
//				//两小
//				final ProductBean firstItem =mDataList.get(position*3/2);
//				final ProductBean secondItem ;
//				if(position*3/2+1<mDataList.size()){
//					secondItem = mDataList.get(position*3/2+1);
//				}else{
//					secondItem=null;
//				}
//				if(firstItem!=null){
//					SetValueByHoldTwoFirstItem(holderTwo, firstItem);
//				}
//				if(secondItem!=null){
//					setValueByHolderTwoSecondItem(holderTwo, secondItem);
//				}else{
//					setSecondItemGone(holderTwo);
//				}
//			}else{
//				//一大
//				final ProductBean item = mDataList.get((position/2+1)*3-1);
//				setValueByHolderView(holder, item);
//			}
//			return convertView;
//		}
//
//
//	}
//	
//	/**大条目设置值并设置监听*/
//	private void setValueByHolderView(ViewHolder holder, final ProductBean item) {
//		holder.rl_item.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				ToastMsg.showToastMsg(mApplication, item.getProductName());
//			}
//		});
//		RdpImageLoader.displayImage(item.getImageUrl(), holder.iv_icon);
//		holder.tv_name.setText(item.getProductName());
//		holder.tv_price.setText(item.getMarketPrice() + "");
//	}
//	/**设置第二个小条目不可见*/
//	private void setSecondItemGone(ViewHolderTwo holderTwo) {
//		holderTwo.iv_icon_2.setVisibility(View.GONE);
//		holderTwo.tv_name_2.setVisibility(View.GONE);
//		holderTwo.tv_price_2.setVisibility(View.GONE);
//	}
//	/**第二个小条目设置值并设置监听*/
//	private void setValueByHolderTwoSecondItem(ViewHolderTwo holderTwo, final ProductBean secondItem) {
//		holderTwo.rl_second_item.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ToastMsg.showToastMsg(mApplication, "点击了"+secondItem.getProductName());
//			}
//		});
//		holderTwo.iv_icon_2.setVisibility(View.VISIBLE);
//		holderTwo.tv_name_2.setVisibility(View.VISIBLE);
//		holderTwo.tv_price_2.setVisibility(View.VISIBLE);
//		RdpImageLoader.displayImage(secondItem.getImageUrl(), holderTwo.iv_icon_2);
//		holderTwo.tv_name_2.setText(secondItem.getProductName());
//		holderTwo.tv_price_2.setText(secondItem.getMarketPrice() + "");
//	}
//	/**给第一个小条目设置值并设置监听*/
//	private void SetValueByHoldTwoFirstItem(ViewHolderTwo holderTwo, final ProductBean firstItem) {
//		RdpImageLoader.displayImage(firstItem.getImageUrl(), holderTwo.iv_icon);
//		holderTwo.tv_name.setText(firstItem.getProductName());
//		holderTwo.tv_price.setText(firstItem.getMarketPrice() + "");
//		holderTwo.rl_first_item.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ToastMsg.showToastMsg(mApplication, "点击了"+firstItem.getProductName());
//			}
//		});
//	}
//	
//	/**初始化viewholderTwo*/
//	private ViewHolderTwo initHolderTwo(View convertView) {
//		ViewHolderTwo holderTwo;
//		holderTwo = new ViewHolderTwo();
//		holderTwo.rl_first_item=(RelativeLayout) convertView.findViewById(R.id.rl_first_item);
//		holderTwo.rl_second_item=(RelativeLayout) convertView.findViewById(R.id.rl_second_item);
//		holderTwo.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
//		holderTwo.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
//		holderTwo.tv_price=(TextView) convertView.findViewById(R.id.tv_price);
//		holderTwo.iv_icon_2=(ImageView) convertView.findViewById(R.id.iv_icon_2);
//		holderTwo.tv_name_2=(TextView) convertView.findViewById(R.id.tv_name_2);
//		holderTwo.tv_price_2=(TextView) convertView.findViewById(R.id.tv_price_2);
//		return holderTwo;
//	}
//	/**初始化viewholder*/
//	private ViewHolder initHolder(View convertView) {
//		ViewHolder holder;
//		holder=new ViewHolder();
//		holder.rl_item=(RelativeLayout) convertView.findViewById(R.id.rl_item);
//		holder.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
//		holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
//		holder.tv_price=(TextView) convertView.findViewById(R.id.tv_price);
//		return holder;
//	}
//
//	class ViewHolder {
//		ImageView iv_icon;
//		TextView tv_name;
//		TextView tv_price;
//		RelativeLayout rl_item;
//	}
//	
//	class ViewHolderTwo{
//		RelativeLayout rl_first_item;
//		RelativeLayout rl_second_item;
//		ImageView iv_icon;
//		TextView tv_name;
//		TextView tv_price;
//		ImageView iv_icon_2;
//		TextView tv_name_2;
//		TextView tv_price_2;
//	}
}
