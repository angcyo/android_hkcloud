package com.zhoukl.androidRDP.RdpAdapter;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandFailedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandSuccessedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpDataSource.RdpRecord;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpListView;
import com.zhoukl.androidRDP.RdpViews.RdpDBViews.IRdpDBView;
import com.zhoukl.androidRDP.RdpViews.RdpDBViews.IRdpDBView.RdpDBViewListener;

/**
 * @description：绑定listView的适配器，绑定网络加载
 * @date 2015-11-5 上午9:28:25
 */
public class RdpDataSetAdapter extends RdpAdapter implements OnCommandSuccessedListener, OnCommandFailedListener {
    protected RdpDataSet mDataSet;
    protected RdpListView mRdpListView;
    private RdpDBViewListener mRdpDBViewListener;
    
    private OnRefreshDBViewListener mOnRefreshDBViewListener;
    
	public RdpDataSetAdapter(Context context) {
		this(context, -1);
	}
	
	public RdpDataSetAdapter (Context context, int itemLayoutID) {
	    super(context, itemLayoutID);
    }

    public void setRdpDatasetAdapterListener(RdpDBViewListener listener) {
        mRdpDBViewListener = listener;
    }
    
    @Override
    public int getCount() {
        if (mDataSet == null) return 0;
        
        int count = mDataSet.getRecordCount();

        if (mMaxCount == NO_LIMIT_COUNT)
            return count;
        
        return count > mMaxCount ? mMaxCount : count;
    }

    @Override
    public Object getItem(int position) {
        if (mDataSet == null) return null;
        
        return mDataSet.getRecord(position);
    }


    public void setData(RdpDataSet dataSet) {
        mDataSet = dataSet;
    }

    public void bindDataView(RdpListView view) {
        mRdpListView = view;
        view.setAdapter(this);
        setOnRefreshListener();
    }
    
    private void setOnRefreshListener() {
        if (mRdpListView == null) return;
        
        mRdpListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mDataSet.open();
                if (mOnRefreshDBViewListener != null) {
                	mOnRefreshDBViewListener.onExtendedBs();  // 扩展方法
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mDataSet.getNextPageDatas();
            }
            
        });
    }
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        
        if (mRdpListView == null) return;
        
        mRdpListView.onRefreshComplete();
        if (mDataSet.getTotalCount() > mDataSet.getRecordCount()) {
            mRdpListView.setMode(Mode.BOTH);
        } else {
            mRdpListView.setMode(Mode.PULL_FROM_START);
        }
        if (mDataSet.getRecordCount() == 0) {
            mRdpListView.setSpecifyView(mRdpListView.getEmptyView());
        }
    }
    
    @Override
    protected void refreshItemViews(int position, AdapterViewHolder viewHolder) {
        Object object = getItem(position);
        if (!(object instanceof RdpRecord)) return;
        
        RdpRecord record = (RdpRecord) object;

        for (int i = 0; i < viewHolder.mViewArray.size(); i++) {
            View view = (View) viewHolder.mViewArray.valueAt(i);
            if (view instanceof IRdpDBView) {
                IRdpDBView rdpDBView = (IRdpDBView) view;
                String fieldName = rdpDBView.getDBExtendView().getFieldName();
                if (!(fieldName.isEmpty())) {
                    boolean dealComplete = false;
                    StringBuffer displayValue = new StringBuffer(record.getStringValue(fieldName));
                    if (mOnRefreshDBViewListener != null) {
                        dealComplete = mOnRefreshDBViewListener.onRefreshDBView(rdpDBView, displayValue.toString(), displayValue);
                    }
                    if (!dealComplete) {
                        rdpDBView.refreshView(displayValue.toString(), mRdpDBViewListener);
                    }
                }
            }
        }
    }
    
    @Override
    public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
        notifyDataSetChanged();
    }

    @Override
    public void onCommandFailed(Object reqKey, RdpResponseResult result) {
        if (mRdpListView == null) return;
        
        mRdpListView.onRefreshComplete();
        if (mDataSet.getRecordCount() == 0) {
            mRdpListView.setSpecifyView(mRdpListView.getErrorView());
        }
    }

    public interface OnRefreshDBViewListener {
        boolean onRefreshDBView(IRdpDBView dbView, String value, StringBuffer displayValue);
        void onExtendedBs();
    }
}
