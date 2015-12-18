package com.zhoukl.androidRDP.RdpViews.RdpDBViews;

public interface IRdpDBView {

    RdpDBExtendView getDBExtendView();

    void refreshView(String value, RdpDBViewListener listener);


    interface RdpDBViewListener {
        boolean onRefreshDBView(IRdpDBView dataBindView, String value, StringBuffer displayValue);
    }


    
}
