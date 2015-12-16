package com.zhoukl.androidRDP.RdpViews.RdpDBViews;

import android.view.View;


public interface IRdpDBView {

    public RdpDBExtendView getDBExtendView();
    
    public void refreshView(String value, RdpDBViewListener listener);
    
    
    public interface RdpDBViewListener {
        public boolean onRefreshDBView(IRdpDBView dataBindView, String value, StringBuffer displayValue); 
    }


    
}
