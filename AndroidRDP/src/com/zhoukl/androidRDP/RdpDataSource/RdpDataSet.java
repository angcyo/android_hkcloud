package com.zhoukl.androidRDP.RdpDataSource;

import android.content.Context;

import java.util.ArrayList;
/**
 * @description：多个网络obj处理
 * @date 2015-11-9 下午3:01:33
 */
public class RdpDataSet extends RdpCommand {
    protected Context mContext;

    /** 分页功能：每页显示的记录*/
    protected int mPageSise = 20;
    /** 分页功能：当前已经获取到的页数*/
//  protected int mCurrPageNo = 1;
    /** 分页功能：总页数*/
    protected int mPageCount = 0;
    /** 分页功能：总记录数*/
    protected int mTotalCount = 0;
    
    protected ArrayList<Object> mDataList = new ArrayList<Object>();
    protected RdpDataSetDealDataListener mOnDealDataListener;
//    protected RdpDataSetCallBackListener mOpenDataListener;

    public void open() {
        
    }
    
    public void getNextPageDatas() {
        
    }

    public ArrayList<Object> getData() {
        return mDataList;
    }

    public void setData(ArrayList<Object> data) {
        mDataList = data;
    }
    
    /** 分页功能：现有记录数*/
    public int getRecordCount() {
        return mDataList.size();
    }

    public int getPageCount() {
        return mPageCount;
    }

    public void setPageCount(int pageCount) {
        mPageCount = pageCount;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public Object getRecord(int index) {
        if (index >= mDataList.size()) {
            return null;
        } else {
            return mDataList.get(index);
        }
    }
    
//    public Object findFirstRecord(String fieldName, String fieldValue) {
//        for (int i = 0; i < mDataList.size(); i++) {
//            
//        }
//    }
    
//    public void setOpenDataListener(RdpDataSetCallBackListener openDataListener) {
//        mOpenDataListener = openDataListener;
//    }

    public void setDealDataListener(RdpDataSetDealDataListener onDealDataListener) {
        mOnDealDataListener = onDealDataListener;
    }

//    public interface RdpDataSetCallBackListener {
//        /** 数据获取成功.          */
//        public void onOpenComplete();
//
//        /** 数据获取失败       */
//        public void onOpenFailed();
//    }

    public interface RdpDataSetDealDataListener {
        /** 获取到数据后，可对数据进行处理          */
        boolean onDealData(ArrayList<Object> dataSet, ArrayList<Object> result);

    }

}
