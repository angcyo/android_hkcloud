package com.huika.cloud.control.home.adapter;

import android.content.Context;

import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;

public class FilterAdapter<T> extends RdpDataListAdapter {
	
	public FilterAdapter(Context context) {
		super(context);
	}

	public FilterAdapter(Context context, int itemLayoutID) {
		super(context, itemLayoutID);
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	public void setShow(boolean isShow) {
		
	}
}
