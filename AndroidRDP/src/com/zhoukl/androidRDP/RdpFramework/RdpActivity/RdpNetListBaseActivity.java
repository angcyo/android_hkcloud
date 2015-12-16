package com.zhoukl.androidRDP.RdpFramework.RdpActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.zhoukl.androidRDP.R;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataSetAdapter;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpConstant;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;

/**
 * @description：所有单个、简单列表信息显示风格的基类；
 * @author zhoukl
 * @date 2014年10月24日 下午6:11:10
 */
public class RdpNetListBaseActivity extends RdpBaseActivity implements OnRefreshItemViewsListener {
	//protected ListView mLvMaster;   // ZklListView mLvMaster;
	//protected PullToRefreshListView mLvMaster;
	protected RdpListView mLvMaster;
	protected RdpDataSetAdapter mMasterAdapter;
	protected RdpNetDataSet mDataSet;
	protected int mErrorLayoutID, mEmptyLayoutID;

	@Override
	protected void onBeforeInitActivity() {
		super.onBeforeInitActivity();
		
		View view = addMasterView(getMasterBaseLayoutID());
//		mLvMaster = (ListView) view.findViewById(R.id.lvMaster);
		mLvMaster = (RdpListView) view.findViewById(R.id.lvMaster);
		mDataSet = new RdpNetDataSet(getApplicationContext());
        setSpecViewForRdpListView(mLvMaster, mDataSet);
		mMasterAdapter = getMasterAdapter();
		mMasterAdapter.bindDataView(mLvMaster);
		mMasterAdapter.setData(mDataSet);
		
		mDataSet.setOnCommandSuccessedListener(this);
		mDataSet.setOnCommandFailedListener(this);
		
	}
	
//	/**
//	 * @description：在本函数将会调用openDataSet()函数来获取数据，
//	 *              故子类应在调用super.initActivity()之前，
//	 *              要把获取所需的参数、数据获取后的处理事项都准备好！
//	 */
//	@Override
//    protected void initActivity() {
//        super.initActivity();
//    }
    
	@Override
	protected void onAfterInitActivity(Bundle savedInstanceState) {
	    super.onAfterInitActivity(savedInstanceState);
        openDataSet();
        mLvMaster.setAdapter(mMasterAdapter);
	}
	
	protected int getMasterBaseLayoutID() {
		return R.layout.rdp_list_base_activity;
	}
	
	protected RdpDataSetAdapter getMasterAdapter() {
	    mMasterAdapter = new RdpDataSetAdapter(this);
	    mMasterAdapter.setListener(this);
	    return mMasterAdapter;
	}
	
	protected void openDataSet() {
		
	}
	
    public void setSpecViewForRdpListView(RdpListView rdpListView, final RdpNetDataSet dataSet) {
        if (mRdpAppProperties.containsProperty(RdpConstant.PROPERTY_LISTVIEW_ERROR_RES_ID)) {
            View errorView = getLayoutInflater().inflate((Integer) mRdpAppProperties.getProperty(RdpConstant.PROPERTY_LISTVIEW_ERROR_RES_ID), null);
            rdpListView.setRdpErroView(errorView);
            Button btn_reload = (Button) errorView.findViewById(R.id.btn_reload);
            btn_reload.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    dataSet.open();
                }
            });
        }
        if (mRdpAppProperties.containsProperty(RdpConstant.PROPERTY_LISTVIEW_EMPTY_RES_ID)) {
            View emptyView = getLayoutInflater().inflate((Integer) mRdpAppProperties.getProperty(RdpConstant.PROPERTY_LISTVIEW_EMPTY_RES_ID), null);
            rdpListView.setRdpEmptyView(emptyView);
        }
    }
    
    protected String getCurrentMemberId() {
        return "";
    }

    @Override
    public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
        return false;
    }

    @Override
    public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
        dismissLoadingDialog();
        mMasterAdapter.notifyDataSetChanged();
    }

    /**
     * 请求执行失败的基类处理规则
     */
    @Override
    public void onCommandFailed(Object reqKey, RdpResponseResult result) {
        dismissLoadingDialog();
		showToastMsg(result.getMsg());
        mMasterAdapter.onCommandFailed(reqKey, result);
    }

}
