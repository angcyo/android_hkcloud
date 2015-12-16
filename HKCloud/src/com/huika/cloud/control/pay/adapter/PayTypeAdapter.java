package com.huika.cloud.control.pay.adapter;

import com.huika.cloud.support.model.PayChannelBean;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PayTypeAdapter extends RdpDataListAdapter<PayChannelBean> {

	public PayTypeAdapter(Context context, int itemLayoutID) {
		super(context, itemLayoutID);
		// TODO Auto-generated constructor stub
	}

	public void setNoSelect(boolean isNoSelect) {
		if (isNoSelect) {
			for (int i = 0; i < getCount(); i++) {
				getItem(i).isSelect = false;			
			}
			notifyDataSetChanged();
		}
	}

	
}
