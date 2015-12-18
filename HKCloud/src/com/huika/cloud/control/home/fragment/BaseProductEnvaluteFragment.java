package com.huika.cloud.control.home.fragment;

import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.home.activity.ProductCommentsActivity;
import com.huika.cloud.control.home.adapter.EvaluteListImgsAdapter;
import com.huika.cloud.support.model.ProductComment;
import com.huika.cloud.support.model.ProductImageArray;
import com.huika.cloud.views.GridViewForScrollView;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpFragment.RdpNetListBaseFragment;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;

import fr.castorflex.android.loadingview.overlay.OverlayLayout;

/**
 * @author chenyuan
 * @date：2015年7月8日,上午11:02:15
 * @describe:
 */
public class BaseProductEnvaluteFragment extends RdpNetListBaseFragment {

	public String productId;
	public int type;
	public int size;
	private OverlayLayout overlayLayout;
	private EvaluteListImgsAdapter evaluteListImgsAdapter;

	@Override
	protected void initFragment() {
		super.initFragment();
		initdata();
		initView();
	}

	private void initdata() {
		productId = getArguments().getString("productId");
	}

	private void initView() {
		showLoadingDialog(getString(R.string.common_loading));
		mLvMaster.setScrollingWhileRefreshingEnabled(true);
		mMasterAdapter.setItemLayoutID(R.layout.item_dish_evaluate_lv);
		mMasterAdapter.setListener(this);
	}

	protected void refreshData(int type, String productId) {
		Type productType = new TypeToken<ArrayList<ProductComment>>() {
		}.getType();
		mDataSet.setTypeOfResult(productType);
		mDataSet.setServerApiUrl(UrlConstant.PRODUCT_GETPRODUCTCOMMENTLIST);
		mDataSet.clearConditions();
		mDataSet.setCondition("productId", productId); // 0001
		mDataSet.setCondition("getMemberId", HKCloudApplication.getInstance().getUserModel().memberId); // 402880e447d7243d0147d72ea3eb0002
		mDataSet.setCondition("type", type);
		mDataSet.open();
	}

@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		hideOverLayView();
		dismissLoadingDialog();
		// mTotalNumber.setVisibility(View.VISIBLE);
		int totalSize = result.getTotalSize();
		size = totalSize;
		if (type == 0) {
			ProductCommentsActivity acy = (ProductCommentsActivity) getActivity();
			acy.all_envalute.setText("全部(" + size + ")");
		} else if (type == 1) {
			ProductCommentsActivity acy = (ProductCommentsActivity) getActivity();
			acy.has_imgs_envalute.setText("有图(" + size + ")");
		}
		ArrayList<ProductComment> commentList = (ArrayList<ProductComment>) data;
		if (commentList != null && commentList.size() > 0) {
			//
		} else if (totalSize == 0) {
			showOverLayTip(R.string.evaluate_empty);
		}
	}

    @Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		// TODO Auto-generated method stub
		super.onCommandFailed(reqKey, result);
		showToastMsg(R.string.common_loading_net_error);

	}

	private void showOverLayTip(int strId) {
		if (overlayLayout == null) {
			overlayLayout = createEmptyOverLay();
		}
		TextView tv_tip = (TextView) overlayLayout.getTag();
		tv_tip.setText(strId);
		overlayLayout.showOverlay();
	}

	protected OverlayLayout createEmptyOverLay() {
		overlayLayout = new OverlayLayout(getActivity());
		overlayLayout.attachTo(mLltMasterArea);
		overlayLayout.setOverlayView(R.layout.rdp_listview_empty);
		TextView tv = (TextView) overlayLayout.getOverlayView().findViewById(R.id.tv_empty_tip);
		overlayLayout.setTag(tv);
		return overlayLayout;
	}
	
	@Override
	public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
		holder.getTextView(R.id.comment_user_tv);
		holder.getRatingBar(R.id.comment_level_rb);
		holder.getTextView(R.id.comment_dt_tv).setVisibility(View.GONE);
		holder.getTextView(R.id.comment_content_tv);
		holder.getTextView(R.id.machent_res_tv).setVisibility(View.GONE);
		GridViewForScrollView grideView = (GridViewForScrollView) holder.getView(R.id.shop_evaluate_gridview);
		evaluteListImgsAdapter = new EvaluteListImgsAdapter(getActivity());
		grideView.setAdapter(evaluteListImgsAdapter);
		evaluteListImgsAdapter.setItemLayoutID(R.layout.comment_image_item);
		evaluteListImgsAdapter.setListener(new OnRefreshItemViewsListener() {
			@Override
			public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
				ProductImageArray mproductImage = (ProductImageArray) adapter.getItem(position);
				RdpImageLoader.displayImage(mproductImage.getImageUrl(), holder.getImageView(R.id.seller_main_item_iv));
				return false;
			}
		});
		return false;
	}
}
