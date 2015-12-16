package com.huika.cloud.control.home.adapter;

import android.content.Context;

import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;

public class FilterAttrAdapter<T> extends RdpDataListAdapter {
	
//	boolean isShow=false;
	
	public FilterAttrAdapter(Context context) {
		super(context);
	}
	
	public FilterAttrAdapter(Context context, int itemLayoutID) {
		super(context, itemLayoutID);
	}
	
//	@Override
//	public int getCount() {
//		if(isShow){
//			return super.getCount();
//		}else{
//			return 4;
//		}
//	}
//
//	public void setShow(boolean isShow) {
//		this.isShow = isShow;
//		notifyDataSetChanged();
//	}
}
