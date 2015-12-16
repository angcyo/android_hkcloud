package com.huika.cloud.control.cart.adapter;

import java.util.List;

import android.content.Context;

import com.huika.cloud.support.model.CartProduct;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;

public class CartAdapter extends RdpDataListAdapter<CartProduct>{
	public CartAdapter(Context context, int itemLayoutID) {
		super(context, itemLayoutID);
		// TODO Auto-generated constructor stub
	}
	
	public void setAllSelect(boolean isAllSelect) {
		for (int i = 0; i < mDataList.size(); i++) {
			mDataList.get(i).setSelect(isAllSelect);
		}
		notifyDataSetChanged();
	}
		
}
