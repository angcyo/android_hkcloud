package com.zhoukl.androidRDP.RdpDataSource;

import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;

/**
 * @description：
 * @author zhoukl(67073753@qq.com)
 * @date 2015年6月19日 下午3:59:29
 */
public class RdpCommand {

    protected OnCommandSuccessedListener mOnSuccessedListener;
    protected OnCommandFailedListener mOnFailedListener;
    protected OnDealWithDataListener OnDealWithDataListener;
    
    public OnCommandSuccessedListener getOnCommandSuccessedListener() {
        return mOnSuccessedListener;
    }

    public void setOnCommandSuccessedListener(OnCommandSuccessedListener listener) {
        mOnSuccessedListener = listener;
    }
    
    public OnCommandFailedListener getOnCommandFailedListener() {
        return mOnFailedListener;
    }

    public void setOnCommandFailedListener(OnCommandFailedListener listener) {
        mOnFailedListener = listener;
    }
    
    public OnDealWithDataListener getOnDealWithDataListener() {
        return OnDealWithDataListener;
    }

    public void setOnDealWithDataListener(OnDealWithDataListener listener) {
        OnDealWithDataListener = listener;
    }
    
    public interface OnCommandSuccessedListener {
        /** 获取到数据，并进行相应的数据处理后，会调用本回调函数        */
        void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data);
    }
    
    public interface OnCommandFailedListener {
        /** 执行失败    : 包括业务逻辑执行失败，也会走这个分支  */
        void onCommandFailed(Object reqKey, RdpResponseResult result);
    }

    public interface OnDealWithDataListener {
        /** 数据处理: 一般情况下会实现本接口     */
        void onDealWithData(Object reqKey, RdpResponseResult result);
    }

}
