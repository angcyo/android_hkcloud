package com.huika.cloud.control.home.fragment;

/**
 * @description：有图的评价
 * @author shicm
 * @date 2015-11-20 下午3:13:42
 */
public class HasImgsProductEnvaluteFragment extends BaseProductEnvaluteFragment {
       @Override
    protected void openDataSet() {
    	// TODO Auto-generated method stub
    	super.openDataSet();
    	 refreshData(1, productId);
    }
}
