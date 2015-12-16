package com.huika.cloud.control.home.adapter;

import android.content.Context;

import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;

public class GoodTypeAdapter<T> extends RdpDataListAdapter {
public int selectedItem=0;
	
	public GoodTypeAdapter(Context context) {
		super(context);
	}

	public GoodTypeAdapter(Context context, int itemLayoutID) {
		super(context, itemLayoutID);
	}

	public void setSelectedItem(int selectedItem) {
		this.selectedItem = selectedItem;
	}

	public int getSelectedItem() {
		return selectedItem;
	}
}
