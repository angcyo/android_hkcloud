package com.huika.cloud.control.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.home.adapter.FilterAttrAdapter;
import com.huika.cloud.support.event.FinishEvent;
import com.huika.cloud.support.model.AttrBean;
import com.huika.cloud.support.model.AttrItemBean;
import com.huika.cloud.support.model.BrandBean;
import com.huika.cloud.support.model.ProductBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpInnerGridView;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpInnerListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @description：商品列表
 * @author ht
 * @date 2015-12-4 上午10:29:39
 */
public class ProductListActivity extends RdpBaseActivity implements OnRefreshItemViewsListener, OnItemClickListener {
	private static final int REQUEST_CODE = 50;
	private static final String POP_LV_TAG = "popLvTag";
	onGotoNextPopListener onGotoNextPopListener;
	private String coming;
	private View headView;
	private View mMasterView;
	private ImageView iv_search;
	private ImageView iv_grid;
	private ImageView iv_list;
	private ImageView back_iv;
	private RdpDataListAdapter<ProductBean> listAdapter;
	private RdpNetDataSet mDataSet;
	private RdpDataListAdapter<ProductBean> gridAdapter;
	private TextView index_tabRb1, index_tabRb2, index_tabRb4;
	private ArrayList<TextView> mTextViews = new ArrayList<TextView>();
	private RelativeLayout rl_tabRb1, rl_tabRb2, rl_tabRb4;
	/** 排序字段 0-综合 1-最终交易价从低到高 2最终交易价从高到底 3-销量 4-人气5 */
	private int sortType;
	private boolean isSortUp = true;
	private PopupWindow popupWindow;
	private LinearLayout ll_all;
	private LinearLayout ll_all_category;
	private int type;
	private View pop_contentView;
	private RdpDataListAdapter<String> popAdapter;
	private PopupWindow composite_popupwindow;
	private int isSelectedPosition = 0;
	private String[] items;
	private List<String> dataItems;
	private ExpandableListView extListView;
	private ListView attr_lv;
	private HashMap<String, List<Object>> hashMap;
	private RdpDataListAdapter<AttrBean> aAdapter;
	private boolean isClick = true;
	private boolean isShow = true;
	private Type typeOfResult;
	private String categoryId;
	private String categoryName;
	private String keyWord = "";
	private PullToRefreshScrollView ptrsv;
	private RdpInnerGridView product_gv;
	private RdpInnerListView product_lv;
	private FilterAttrAdapter<Object> attrGvAdapter;
	private TextView selected_attr;
	private AttrBean item;
	private String priceType = "0";// 价格类型
	private boolean isSelectedSale = false;
	private String saleCountType = "0";// 销售类型
	private String skuAttribute;// sku属性
	private String brandId;// 品牌id
	private String priceArea;// 价格区间
	private EditText start_price;
	private EditText end_price;
	private PopupWindow oneLevelPop;
	OnClickListener filterClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Drawable drawable;
			switch (v.getId()) {
				case R.id.iv_search:
					startActivity(new Intent(mApplication, SearchActivity.class));
					break;
				case R.id.rl_tabRb1:
					changeTextViewColor(R.id.index_tabRb1);
					composite_popupwindow.showAsDropDown(headView);
					sortType = -1;
					drawable = getResources().getDrawable(R.drawable.icon_up);
					// 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					index_tabRb1.setCompoundDrawables(null, null, drawable, null);
					break;
				case R.id.rl_tabRb2:
					if (!isSelectedSale) {
						isSelectedSale = true;
						sortType = 3;
						index_tabRb2.setText("销量优先");
						saleCountType = "2";
						changeTextViewColor(R.id.index_tabRb2);
						getProductListData(true, "402881e8461795c201461795c2e90000", categoryId, priceType, keyWord, saleCountType, priceArea, brandId, skuAttribute, true);
					}
					break;
				case R.id.rl_tabRb4:
					View parent = getWindow().getDecorView();
					Rect rectangle = new Rect();
					Window window = ProductListActivity.this.getWindow();
					// 获取到手机状态栏的高度
					window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
					int statusBarHeight = rectangle.top;
					// 动态设置状态栏的高度
					oneLevelPop.setHeight(parent.getMeasuredHeight() - statusBarHeight);
					// 并将popwindow的显示的初始位置的顶部也就是Y坐标开始显示
					oneLevelPop.showAtLocation(parent, Gravity.TOP, 0, statusBarHeight);
					drawable = getResources().getDrawable(R.drawable.icon_up);
					// 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					index_tabRb4.setCompoundDrawables(null, null, drawable, null);
					break;
				default:
					break;
			}
			// 初始化综合筛选栏显示
			if (v.getId() == R.id.rl_tabRb2) {
				initCompositeFilter();
			} else {
				isSelectedSale = false;
				index_tabRb2.setText("销量");
				saleCountType = "0";
			}
		}
	};
	private RdpDataListAdapter<AttrBean> oneLevelAdapter;
	private HashMap<String, List<Object>> selectedFilterAttrMap;
	private AttrBean attrBean;
	private FilterAttrAdapter<Object> secondLevelAdapter;
	private PopupWindow secondLevelPop;
	private AttrItemBean allAttrItemBean;
	private BrandBean allBrand;
	private EditText searchEt;

	@Override
	protected void initActivity() {
		super.initActivity();
		EventBus.getDefault().register(this);
		removeLeftFuncView(TBAR_FUNC_BACK);
		Intent intent = getIntent();
		headView = addHeaderView(R.layout.product_list_head);
		mMasterView = addMasterView(R.layout.product_list_master);
		if ("category".equals(coming)) {
			categoryName = intent.getStringExtra("categoryName");
			categoryId = intent.getStringExtra("categoryId");
			setFuncTitle(categoryName);
			iv_search = (ImageView) findViewById(R.id.iv_search);
			iv_search.setOnClickListener(this);
		}
		else if ("search".equals(coming)) {
			keyWord = intent.getStringExtra("keyword");
			searchEt = (EditText) findViewById(R.id.searchEt);
			searchEt.setText(keyWord);
		}
		ptrsv = (PullToRefreshScrollView) mMasterView.findViewById(R.id.master_sc);
		ptrsv.setMode(Mode.BOTH);

		listAdapter = new RdpDataListAdapter<ProductBean>(mApplication, R.layout.list_item_type_zero);
		gridAdapter = new RdpDataListAdapter<ProductBean>(mApplication, R.layout.product_grid_item);

		initView();
		initPubWindow();
		initListener();

		index_tabRb1.setText(dataItems.get(isSelectedPosition));
		listAdapter.setListener(this);
		product_lv.setAdapter(listAdapter);

		product_gv.setVisibility(View.GONE);
		product_gv.setAdapter(gridAdapter);
		// 获取商品列表
		mDataSet = new RdpNetDataSet(mApplication);
		mDataSet.setOnCommandSuccessedListener(this);
		mDataSet.setOnCommandFailedListener(this);
		typeOfResult = new TypeToken<ArrayList<ProductBean>>() {
		}.getType();

		getProductListData(true, "402881e8461795c201461795c2e90000", categoryId, priceType, keyWord, saleCountType, priceArea, brandId, skuAttribute, true);
	}

	public void onEventMainThread(FinishEvent event) {
		finish();
	}

	/**获取商品列表数据*/
	private void getProductListData(boolean isFirst, String merchantId, String categoryId, String priceType, String keyword, String saleCountType, String priceArea, String brandId, String skuAttribute, boolean flushOrMore) {
		if (isFirst) {
			showLoadingOverLay(ptrsv);
		}
		mDataSet.setServerApiUrl(UrlConstant.GET_PRODUCT_LIST);
		mDataSet.clearConditions();
		mDataSet.setCondition("merchantId", merchantId);
		mDataSet.setCondition("categoryId", categoryId);
		mDataSet.setCondition("priceType", priceType);
		mDataSet.setCondition("keyword", keyword);
		mDataSet.setCondition("saleCountType", saleCountType);
		mDataSet.setCondition("priceArea", priceArea);
		mDataSet.setCondition("brandId", brandId);
		mDataSet.setCondition("skuAttribute", skuAttribute);
		mDataSet.setTypeOfResult(typeOfResult);
		if (flushOrMore) {
			// 刷新
			mDataSet.open();
		}
		else {
			mDataSet.getNextPageDatas();
		}
	}

	private void initView() {
		index_tabRb1 = (TextView) headView.findViewById(R.id.index_tabRb1);
		index_tabRb2 = (TextView) headView.findViewById(R.id.index_tabRb2);
		index_tabRb4 = (TextView) headView.findViewById(R.id.index_tabRb4);
		mTextViews.add(index_tabRb1);
		mTextViews.add(index_tabRb2);

		ll_all = (LinearLayout) findViewById(R.id.ll_all);
		rl_tabRb1 = (RelativeLayout) headView.findViewById(R.id.rl_tabRb1);
		rl_tabRb2 = (RelativeLayout) headView.findViewById(R.id.rl_tabRb2);
		rl_tabRb4 = (RelativeLayout) headView.findViewById(R.id.rl_tabRb4);

		back_iv = (ImageView) findViewById(R.id.iv_back_icon);
		iv_list = (ImageView) headView.findViewById(R.id.iv_list);
		iv_grid = (ImageView) headView.findViewById(R.id.iv_grid);

		product_gv = (RdpInnerGridView) mMasterView.findViewById(R.id.product_gv);
		product_lv = (RdpInnerListView) mMasterView.findViewById(R.id.product_lv);

	}

	private void initListener() {
		back_iv.setOnClickListener(this);
		iv_list.setOnClickListener(this);
		iv_grid.setOnClickListener(this);

		rl_tabRb1.setOnClickListener(filterClick);
		rl_tabRb2.setOnClickListener(filterClick);
		rl_tabRb4.setOnClickListener(filterClick);

		if (searchEt != null) {
			searchEt.setOnClickListener(this);
		}
		product_lv.setOnItemClickListener(this);
		product_gv.setOnItemClickListener(this);

		ptrsv.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 下拉刷新
				getProductListData(false, "402881e8461795c201461795c2e90000", categoryId, priceType, keyWord, saleCountType, priceArea, brandId, skuAttribute, true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 上拉加载更多
				getProductListData(false, "402881e8461795c201461795c2e90000", categoryId, priceType, keyWord, saleCountType, priceArea, brandId, skuAttribute, false);
			}
		});
		gridAdapter.setListener(this);
	}

	/** 初始化化综合筛选栏 */
	private void initCompositeFilter() {
		Drawable drawable3 = getResources().getDrawable(R.drawable.icon_down);
		// 这一步必须要做,否则不会显示.
		drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
		index_tabRb1.setCompoundDrawables(null, null, drawable3, null);
	}

	/** 综合筛选栏选中关闭 */
	private void initCompositeFilterSelected() {
		Drawable drawable3 = getResources().getDrawable(R.drawable.icon_down);
		// 这一步必须要做,否则不会显示.
		drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
		index_tabRb1.setCompoundDrawables(null, null, drawable3, null);
		getProductListData(true, "402881e8461795c201461795c2e90000", categoryId, priceType, keyWord, saleCountType, priceArea, brandId, skuAttribute, true);
	}

	/**筛选栏选中关闭 */
	private void initFilterSelected() {
		Drawable drawable3 = getResources().getDrawable(R.drawable.icon_down);
		// 这一步必须要做,否则不会显示.
		drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
		index_tabRb4.setCompoundDrawables(null, null, drawable3, null);
		getProductListData(true, "402881e8461795c201461795c2e90000", categoryId, priceType, keyWord, saleCountType, priceArea, brandId, skuAttribute, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back_icon:
				if ("search".equals(coming)) {
					// 直接返回首页,防止死循环
					EventBus.getDefault().post(new FinishEvent());
				}
				finish();
				break;
			case R.id.searchEt:
				startActivity(new Intent(mApplication, SearchActivity.class));
				break;
			case R.id.iv_search:
				startActivity(new Intent(mApplication, SearchActivity.class));
				finish();
				break;
			case R.id.iv_list:
				iv_list.setVisibility(View.GONE);
				iv_grid.setVisibility(View.VISIBLE);
				product_lv.setVisibility(View.VISIBLE);
				product_gv.setVisibility(View.GONE);
				break;
			case R.id.iv_grid:
				iv_grid.setVisibility(View.GONE);
				iv_list.setVisibility(View.VISIBLE);
				product_lv.setVisibility(View.GONE);
				product_gv.setVisibility(View.VISIBLE);
				break;
			case R.id.reset_filter:
				// 清空筛选条件
				hashMap.clear();
				aAdapter.notifyDataSetChanged();
				break;
			default:
				super.onClick(v);
				break;
		}
	}

	/** 根据从不同的activity跳转到列表activity显示不同的布局 */
	@Override
	protected int getBaseLayoutID() {
		Intent intent = getIntent();
		coming = intent.getStringExtra("coming");
		if ("search".equals(coming)) {
			return R.layout.search_product_list;
		}
		else if ("category".equals(coming)) { return R.layout.category_product_list; }
		return super.getBaseLayoutID();
	}

	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
		if (adapter.equals(listAdapter)) {
			ProductBean productBean = listAdapter.getItem(position);
			holder.getTextView(R.id.tv_name).setText(productBean.getProductName());
			holder.getTextView(R.id.tv_price).setText(productBean.getShopPrice() + "");
			holder.getTextView(R.id.tv_discount_2).setText(productBean.getDiscount() + "折");
			if (productBean.getImageUrl() != null && !TextUtils.isEmpty(productBean.getImageUrl())) {
				RdpImageLoader.displayImage(productBean.getImageUrl(), holder.getImageView(R.id.iv_icon));
			}
		}
		if (adapter.equals(gridAdapter)) {
			ProductBean productBean = gridAdapter.getItem(position);
			holder.getTextView(R.id.tv_name).setText(productBean.getProductName());
			holder.getTextView(R.id.tv_price).setText(productBean.getProductPrice() + "");
			holder.getTextView(R.id.tv_discount).setText(productBean.getDiscount() + "折");
			if (productBean.getImageUrl() != null && !TextUtils.isEmpty(productBean.getImageUrl())) {
				RdpImageLoader.displayImage(productBean.getImageUrl(), holder.getImageView(R.id.iv_icon));
			}
		}
		if (adapter.equals(oneLevelAdapter)) {
			AttrBean attrBean = oneLevelAdapter.getItem(position);
			holder.getTextView(R.id.tv_attr_name).setText(attrBean.attributeName);
		}
		if (adapter.equals(secondLevelAdapter)) {
			Object obj = secondLevelAdapter.getItem(position);
			if (obj instanceof BrandBean) {
				holder.getTextView(R.id.tv_second_filter_item).setText(((BrandBean) obj).brandName);
			}
			if (obj instanceof AttrItemBean) {
				holder.getTextView(R.id.tv_second_filter_item).setText(((AttrItemBean) obj).valudStr);
			}
		}
		return false;
	}

	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		if (UrlConstant.GET_PRODUCT_LIST.equals(result.getUrl())) {
			hideOverLayView();
			ptrsv.onRefreshComplete();
			listAdapter.setData((List<ProductBean>) data);
			gridAdapter.setData((List<ProductBean>) data);
			if (mDataSet.getTotalCount() > mDataSet.getRecordCount()) {
				ptrsv.setMode(Mode.BOTH);
			}
			else {
				ptrsv.setMode(Mode.PULL_FROM_START);
			}
			// if (mDataSet.getRecordCount() == 0) {
			// ptrsv.setSpecifyView(mRdpListView.getEmptyView());
			// }
		}
		if (UrlConstant.GET_PRODUCT_FIRST_FILTER.equals(result.getUrl())) {
			// aAdapter.setData((List<AttrBean>) data);
			oneLevelAdapter.setData((List<AttrBean>) data);
		}
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 跳转到商品详情界面
		if (view.getId() == R.id.pop_lv_item) {
			// 综合筛选栏的条目点击事件
			isSelectedPosition = position;
			if (composite_popupwindow != null && composite_popupwindow.isShowing()) {
				composite_popupwindow.dismiss();
			}
			priceType = position + "";
			initCompositeFilterSelected();
			index_tabRb1.setText(popAdapter.getItem(position));
		}
		if (view.getId() == R.id.one_level_filter_item) {
			AttrBean attrBean = oneLevelAdapter.getItem(position);
			// 一级筛选的条目点击事件
			onGotoNextPopListener.gotoSecondPop(attrBean, position);
			secondLevelPop.showAsDropDown(headView);
		}
	}

	private void initPubWindow() {
		// 初始化综合弹框
		initCompositePop();
		// 初始化一级筛选弹框
		initOneLevelFilterPop();
		// 初始化二级筛选弹框
		initSecondLevelFilterPop();
	}

	/**初始化综合筛选弹框*/
	private void initCompositePop() {
		pop_contentView = View.inflate(mApplication, R.layout.composite_popupwindow, null);
		composite_popupwindow = new PopupWindow(pop_contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		pop_contentView.setFocusable(true); // 这个很重要
		pop_contentView.setFocusableInTouchMode(true);
		sortType = -1;
		ListView pop_lv = (ListView) pop_contentView.findViewById(R.id.lv_pop);
		pop_lv.setOnItemClickListener(this);
		popAdapter = new RdpDataListAdapter<String>(mApplication, R.layout.pop_lv_item);
		items = new String[] { "综合排序", "价格从低到高", "价格从高到低" };
		dataItems = Arrays.asList(items);
		popAdapter.setData(dataItems);
		popAdapter.setListener(new OnRefreshItemViewsListener() {
			@Override
			public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
				holder.getTextView(R.id.pop_item_name).setTextColor(Color.BLACK);
				if (position == isSelectedPosition) {
					holder.getTextView(R.id.pop_item_name).setTextColor(getResources().getColor(R.color.selected_product_lv_filter));
				}
				String item = (String) adapter.getItem(position);
				holder.getTextView(R.id.pop_item_name).setText(item);
				return false;
			}
		});
		pop_lv.setAdapter(popAdapter);
		// 重写onKeyListener,按返回键消失
		pop_contentView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					composite_popupwindow.dismiss();
					initCompositeFilterSelected();
					return true;
				}
				return false;
			}
		});
		// 点击其他地方消失
		pop_contentView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (composite_popupwindow != null && composite_popupwindow.isShowing()) {
					composite_popupwindow.dismiss();
				}
				initCompositeFilterSelected();
				return false;
			}
		});
	}

	private void changeTextViewColor(int id) {
		for (TextView tv_item : mTextViews) {
			if (tv_item.getId() == id) {
				tv_item.setTextColor(getResources().getColor(R.color.selected_product_lv_filter));
			}
			else {
				tv_item.setTextColor(Color.BLACK);
			}
		}
	}

	/**初始化一级筛选弹框*/
	public void initOneLevelFilterPop() {
		View one_level_filter = View.inflate(mApplication, R.layout.one_level_filter, null);
		one_level_filter.setFocusable(true);
		one_level_filter.setFocusableInTouchMode(true);
		oneLevelPop = new PopupWindow(one_level_filter, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
		oneLevelPop.setAnimationStyle(R.style.filter_sku_animation);
		one_level_filter.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					oneLevelPop.dismiss();
					initFilterSelected();
					return true;
				}
				return false;
			}
		});
		// // 点击其他地方消失
		// one_level_filter.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (oneLevelPop != null && oneLevelPop.isShowing()) {
		// oneLevelPop.dismiss();
		// initFilterSelected();
		// }
		// return false;
		// }
		// });
		start_price = (EditText) one_level_filter.findViewById(R.id.start_price);
		end_price = (EditText) one_level_filter.findViewById(R.id.end_price);
		ListView one_level_lv = (ListView) one_level_filter.findViewById(R.id.one_level_lv);
		one_level_filter.findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				oneLevelPop.dismiss();
				initFilterSelected();
			}
		});
		one_level_filter.findViewById(R.id.tv_confirm).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				oneLevelPop.dismiss();
				// 确认筛选条件
				if (hashMap != null) {
					Iterator iter = hashMap.entrySet().iterator();
					StringBuffer skuStr = new StringBuffer();
					StringBuffer brandStr = new StringBuffer();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						String key = (String) entry.getKey();
						if ("品牌".equals(key)) {
							List<BrandBean> val = (List<BrandBean>) entry.getValue();
							for (BrandBean brandBean : val) {
								brandStr.append(brandBean.brandId).append(",");
							}
						}
						else {
							List<AttrItemBean> val = (List<AttrItemBean>) entry.getValue();
							for (AttrItemBean attrItemBean : val) {
								brandStr.append(attrItemBean.valudStr).append(",");
							}
						}
					}
					if (brandStr.length() > 0) {
						brandId = brandStr.substring(0, brandStr.lastIndexOf(",") - 1);
					}
					else {
						brandId = "";
					}
					if (skuStr.length() > 0) {
						skuAttribute = skuStr.substring(0, skuStr.lastIndexOf(",") - 1);
					}
					else {
						skuAttribute = "";
					}
					String startPrice = start_price.getText().toString();
					String endPrice = end_price.getText().toString();
					priceArea = startPrice + "-" + endPrice;
				}
				oneLevelPop.dismiss();
				initFilterSelected();
			}
		});
		oneLevelAdapter = new RdpDataListAdapter<AttrBean>(mApplication, R.layout.one_level_filter_item);
		oneLevelAdapter.setListener(ProductListActivity.this);
		one_level_lv.setAdapter(oneLevelAdapter);
		one_level_lv.setOnItemClickListener(ProductListActivity.this);
	}

	/**初始化二级筛选弹框*/
	public void initSecondLevelFilterPop() {
		View second_level_filter = View.inflate(mApplication, R.layout.second_level_filter, null);
		second_level_filter.setFocusable(true);
		second_level_filter.setFocusableInTouchMode(true);
		secondLevelPop = new PopupWindow(second_level_filter, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
		second_level_filter.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					secondLevelPop.dismiss();
					return true;
				}
				return false;
			}
		});
		// 点击其他地方消失
		second_level_filter.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (secondLevelPop != null && secondLevelPop.isShowing()) {
					secondLevelPop.dismiss();
				}
				return false;
			}
		});
		ListView second_level_filter_lv = (ListView) second_level_filter.findViewById(R.id.second_level_filter_lv);
		selectedFilterAttrMap = new HashMap<String, List<Object>>();
		second_level_filter_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object obj = secondLevelAdapter.getItem(position);
				if (obj instanceof BrandBean) {
					List list = selectedFilterAttrMap.get(attrBean.attributeName);
					if (list == null) {
						list = new ArrayList<BrandBean>();
					}
					if ("全部".equals(((BrandBean) obj).brandName)) {
						list.clear();
					}
					else {
						list.remove(allBrand);
						if (list.contains(obj)) {
							list.remove(obj);
						}
						else {
							list.add(obj);
						}
					}
					selectedFilterAttrMap.put(attrBean.attributeName, list);
				}
				if (obj instanceof AttrItemBean) {
					List list = selectedFilterAttrMap.get(attrBean.attributeName);
					if (list == null) {
						list = new ArrayList<AttrItemBean>();
					}
					if ("全部".equals(((AttrItemBean) obj).valudStr)) {
						list.clear();
					}
					else {
						list.remove(allAttrItemBean);
						if (list.contains(obj)) {
							list.remove(obj);
						}
						else {
							list.add(obj);
						}
					}
					selectedFilterAttrMap.put(attrBean.attributeName, list);
				}
				secondLevelAdapter.notifyDataSetChanged();
			}
		});
		secondLevelAdapter = new FilterAttrAdapter<Object>(mApplication, R.layout.second_level_filter_lv_item);
		secondLevelAdapter.setListener(new OnRefreshItemViewsListener() {
			@Override
			public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
				List<Object> list = selectedFilterAttrMap.get(attrBean.attributeName);
				Object obj = secondLevelAdapter.getItem(position);
				holder.getTextView(R.id.tv_second_filter_item).setTextColor(getResources().getColor(R.color.common_text_color));
				holder.getImageView(R.id.iv_item_selected).setVisibility(View.GONE);
				if (obj instanceof BrandBean) {
					holder.getTextView(R.id.tv_second_filter_item).setText(((BrandBean) obj).brandName);
					if (list != null && (list.contains(obj))) {
						holder.getImageView(R.id.iv_item_selected).setVisibility(View.VISIBLE);
						holder.getTextView(R.id.tv_second_filter_item).setTextColor(getResources().getColor(R.color.selected_product_lv_filter));
					}
				}
				if (obj instanceof AttrItemBean) {
					holder.getTextView(R.id.tv_second_filter_item).setText(((AttrItemBean) obj).valudStr);
					if (list != null && (list.contains(obj))) {
						holder.getImageView(R.id.iv_item_selected).setVisibility(View.VISIBLE);
						holder.getTextView(R.id.tv_second_filter_item).setTextColor(getResources().getColor(R.color.selected_product_lv_filter));
					}
				}
				else if (list == null && position == 0 || (list != null && list.size() == 0 && position == 0)) {
					holder.getImageView(R.id.iv_item_selected).setVisibility(View.VISIBLE);
					holder.getTextView(R.id.tv_second_filter_item).setTextColor(getResources().getColor(R.color.selected_product_lv_filter));
				}
				return false;
			}
		});
		second_level_filter_lv.setAdapter(secondLevelAdapter);

		onGotoNextPopListener = new onGotoNextPopListener() {
			@Override
			public void gotoSecondPop(AttrBean object, int position) {
				attrBean = object;
				if (position == 0) {
					ArrayList<BrandBean> brandBeans = new ArrayList<BrandBean>();
					allBrand = new BrandBean("", "全部");
					brandBeans.add(allBrand);
					brandBeans.addAll(attrBean.brandChildren);
					secondLevelAdapter.setData(brandBeans);
				}
				else {
					ArrayList<AttrItemBean> attrItemBeans = new ArrayList<AttrItemBean>();
					allAttrItemBean = new AttrItemBean("全部", "");
					attrItemBeans.add(allAttrItemBean);
					attrItemBeans.addAll(attrBean.skuChildren);
					secondLevelAdapter.setData(attrItemBeans);
				}
			}
		};
	}

	@Override
	public void onBackPressed() {
		if ("search".equals(coming)) {
			// 直接返回首页,防止死循环
			EventBus.getDefault().post(new FinishEvent());
		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public interface onGotoNextPopListener {
		void gotoSecondPop(AttrBean attrBean, int position);
	}

	// /**初始化筛选弹框*/
	// private void initFliterPop() {
	// View filterView = View.inflate(mApplication, R.layout.first_filter, null);
	// filterView.setFocusable(true);
	// filterView.setFocusableInTouchMode(true);
	// View filterPopFoot = View.inflate(mApplication, R.layout.reset_filter, null);
	// filterPopWindow = new PopupWindow(filterView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
	// filterPopWindow.setAnimationStyle(R.style.item_sku_animation);
	// // 重写onKeyListener,按返回键消失
	// filterView.setOnKeyListener(new OnKeyListener() {
	// @Override
	// public boolean onKey(View v, int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// filterPopWindow.dismiss();
	// initFilterSelected();
	// return true;
	// }
	// return false;
	// }
	// });
	// // 点击其他地方消失
	// filterView.setOnTouchListener(new OnTouchListener() {
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// if (filterPopWindow != null && filterPopWindow.isShowing()) {
	// filterPopWindow.dismiss();
	// initFilterSelected();
	// }
	// return false;
	// }
	// });
	// // 将筛选属性存储起来
	// hashMap = new HashMap<String, List<Object>>();
	// attr_lv = (ListView) filterView.findViewById(R.id.attr_lv);
	// start_price = (EditText) filterView.findViewById(R.id.start_price);
	// end_price = (EditText) filterView.findViewById(R.id.end_price);
	// Button bt_reset = (Button) filterPopFoot.findViewById(R.id.reset_filter);
	// Button bt_confirm = (Button) filterView.findViewById(R.id.bt_confirm);
	// // 确认按钮点击监听
	// bt_confirm.setOnClickListener(this);
	// bt_reset.setOnClickListener(this);
	// attr_lv.setOnItemClickListener(null);
	// attr_lv.addFooterView(filterPopFoot);
	// aAdapter = new RdpDataListAdapter<AttrBean>(mApplication, R.layout.attr_item);
	// aAdapter.setListener(new OnRefreshItemViewsListener() {
	// @Override
	// public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, final AdapterViewHolder holder) {
	// onRefreshAttrLvItem(position, holder);
	// return false;
	// }
	// });
	// attr_lv.setAdapter(aAdapter);
	//
	// RdpNetDataSet filterData = new RdpNetDataSet(mApplication);
	// filterData.setServerApiUrl(UrlConstant.GET_PRODUCT_FIRST_FILTER);
	// filterData.clearConditions();
	// filterData.setOnCommandFailedListener(ProductListActivity.this);
	// filterData.setOnCommandSuccessedListener(ProductListActivity.this);
	// filterData.setCondition("categoryId", categoryId);
	// filterData.setCondition("merchantId", "402881e8461795c201461795c2e90000");
	// Type filterResult=new TypeToken<List<AttrBean>>(){}.getType();
	// filterData.setTypeOfResult(filterResult);
	// filterData.open();
	// }
	//
	// /**筛选列表条目显示*/
	// private void onRefreshAttrLvItem(int position, final AdapterViewHolder holder) {
	// final AttrBean item = (AttrBean) aAdapter.getItem(position);
	// final RdpInnerGridView rigv = (RdpInnerGridView) holder.getView(R.id.rigv);
	// holder.getTextView(R.id.attr_name).setText(item.attributeName);
	// final TextView selected_attr = holder.getTextView(R.id.select_attr);
	// // 如果没有保存的筛选数据，筛选属性默认设为全部
	// if (!(hashMap.size() > 0)) {
	// selected_attr.setText("全部");
	// holder.getImageView(R.id.iv_icon).setImageResource(R.drawable.icon_drop_down);
	// }
	// holder.getImageView(R.id.iv_icon).setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// if (isClick) {
	// isShow = false;
	// isClick = false;
	// holder.getImageView(R.id.iv_icon).setImageResource(R.drawable.icon_drop_down);
	// }
	// else {
	// isShow = true;
	// isClick = true;
	// holder.getImageView(R.id.iv_icon).setImageResource(R.drawable.icon_take_up);
	// }
	// attrGvAdapter.setShow(isShow);
	// }
	// });
	// attrGvAdapter = new FilterAttrAdapter<Object>(mApplication, R.layout.attr_two_item);
	// rigv.setAdapter(attrGvAdapter);
	// attrGvAdapter.setListener(new OnRefreshItemViewsListener() {
	// @Override
	// public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
	// Object attrItem = attrGvAdapter.getItem(position);
	// if(attrItem instanceof BrandBean){
	// final BrandBean attr_two =(BrandBean) attrGvAdapter.getItem(position);
	// holder.getButton(R.id.bt).setText(attr_two.brandName);
	// holder.getButton(R.id.bt).setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // 将选中的属性显示，并保存起来
	// Button bt = (Button) v;
	// List listSelected = hashMap.get(item.attributeName);
	// if (listSelected == null) {
	// listSelected = new ArrayList<BrandBean>();
	// }
	// if (listSelected.contains(attr_two)) {
	// listSelected.remove(attr_two);
	// }
	// else {
	// listSelected.add(attr_two);
	// }
	// hashMap.put(item.attributeName, listSelected);
	// StringBuffer sb=new StringBuffer();
	// for (int i = 0; i <listSelected.size(); i++) {
	// BrandBean brandBean = (BrandBean) listSelected.get(i);
	// sb.append(brandBean.brandName).append(" ");
	// }
	// selected_attr.setText(sb.toString());
	// }
	// });
	// }
	// if(attrItem instanceof AttrItemBean){
	// final AttrItemBean attr_two = (AttrItemBean) attrGvAdapter.getItem(position);
	// holder.getButton(R.id.bt).setText(attr_two.valudStr);
	// holder.getButton(R.id.bt).setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // 将选中的属性显示，并保存起来
	// Button bt = (Button) v;
	// List listSelected = hashMap.get(item.attributeName);
	// if (listSelected == null) {
	// listSelected = new ArrayList<AttrItemBean>();
	// }
	// if (listSelected.contains(attr_two)) {
	// listSelected.remove(attr_two);
	// }
	// else {
	// listSelected.add(attr_two);
	// }
	// hashMap.put(item.attributeName, listSelected);
	// StringBuffer sb=new StringBuffer();
	// for (int i = 0; i <listSelected.size(); i++) {
	// AttrItemBean attrItemBean =(AttrItemBean) listSelected.get(i);
	// sb.append(attrItemBean.valudStr).append(" ");
	// }
	// selected_attr.setText(listSelected.toString());
	// }
	// });
	// }
	// return false;
	// }
	// });
	// if(position==0){
	// attrGvAdapter.setData(item.brandChildren);
	// }else{
	// attrGvAdapter.setData(item.skuChildren);
	// }
	// }
	// /**筛选列表条目里面的条目显示
	// * @param attributeName */
	// private void onRefreshFilterAttr(final Object item, final TextView selected_attr, int position, AdapterViewHolder holder, final String attributeName) {
	// if(item instanceof BrandBean){
	// BrandBean attr_two =(BrandBean) attrGvAdapter.getItem(position);
	// holder.getButton(R.id.bt).setText(attr_two.brandName);
	// }
	// if(item instanceof AttrItemBean){
	// AttrItemBean attr_two = (AttrItemBean) attrGvAdapter.getItem(position);
	// holder.getButton(R.id.bt).setText(attr_two.valudStr);
	// }
	// holder.getButton(R.id.bt).setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // 将选中的属性显示，并保存起来
	// Button bt = (Button) v;
	// List listSelected = hashMap.get(attributeName);
	// if (listSelected == null) {
	// listSelected = new ArrayList<Object>();
	// }
	// if (listSelected.contains(bt.getText())) {
	// listSelected.remove(bt.getText());
	// }
	// else {
	// if (listSelected.size() == 5) {
	// ToastMsg.showToastMsg(mApplication, "最多设置5个筛选条件");
	// }
	// listSelected.add((String) bt.getText());
	// }
	// hashMap.put(attributeName, listSelected);
	//
	// selected_attr.setText(listSelected.toString());
	// }
	// });
	// }

}
