package com.zhoukl.androidRDP.RdpAdapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;

/**
 * @description：适配器 正常按数据装在适配器
 * @date 2015-11-5 上午9:28:00
 */
public class RdpDataListAdapter<T> extends RdpAdapter {

    protected List<T> mDataList;
    private ImageLoader mImageLoader;

    public RdpDataListAdapter(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public RdpDataListAdapter(Context context, int itemLayoutID) {
        this(context);
        mItemLayoutID = itemLayoutID;
    }
    
    @Override
    public int getCount() {
        if (mDataList == null)
            return 0;
        if (mMaxCount == NO_LIMIT_COUNT)
            return mDataList.size();

        return mDataList.size() > mMaxCount ? mMaxCount : mDataList.size();
    }

    @Override
    public T getItem(int position) {
        if (mDataList == null)
            return null;
        else
            return mDataList.get(position);
    }
    
    public void setData(List<T> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }
}
