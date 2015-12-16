package com.huika.cloud.control.home.fragment;

/**
 * @description：全部评价
 * @author shicm
 * @date 2015-11-20 下午3:13:18
 */
public class AllProductEnvaluteFragment extends BaseProductEnvaluteFragment {
	
	@Override
	protected void openDataSet() {
		// TODO Auto-generated method stub
		super.openDataSet();
		 refreshData(0, productId);
	}
}
