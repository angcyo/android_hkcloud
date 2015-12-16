package com.zhoukl.androidRDP.RdpFramework.RdpFragment;


import android.view.View;

import com.zhoukl.androidRDP.R;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataSetAdapter;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;


public class RdpNetListBaseFragment extends RdpBaseFragment implements OnRefreshItemViewsListener {

    protected RdpListView mLvMaster;
    protected RdpDataSetAdapter mMasterAdapter;
    protected RdpNetDataSet mDataSet;
    protected int mErrorLayoutID, mEmptyLayoutID;
    
    @Override
    protected void onBeforeInitFragment() {
        super.onBeforeInitFragment();
        
        View view = addMasterView(getMasterBaseLayoutID());
//      mLvMaster = (ListView) view.findViewById(R.id.lvMaster);
        mLvMaster = (RdpListView) view.findViewById(R.id.lvMaster);
        mDataSet = new RdpNetDataSet(mActivity);
        //setSpecViewForRdpListView(mLvMaster, mDataSet);
		mMasterAdapter = getMasterAdapter();
        mMasterAdapter.bindDataView(mLvMaster);
        mMasterAdapter.setData(mDataSet);
        
        mDataSet.setOnCommandSuccessedListener(this);
        mDataSet.setOnCommandFailedListener(this);
    }
    
	@Override
	protected void onAfterInitFragment() {
	    super.onAfterInitFragment();
        openDataSet();
        mLvMaster.setAdapter(mMasterAdapter);
	}
	
    protected int getMasterBaseLayoutID() {
        return R.layout.rdp_list_base_activity;
    }

    
	protected RdpDataSetAdapter getMasterAdapter() {
        mMasterAdapter = new RdpDataSetAdapter(mActivity);
        mMasterAdapter.setListener(this);
        return mMasterAdapter;
	}
	
	protected void openDataSet() {
    }

    @Override
    public boolean onRefreshItemViews(RdpAdapter adapter, int position, View convertView, AdapterViewHolder holder) {
        // TODO Auto-generated method stub
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
