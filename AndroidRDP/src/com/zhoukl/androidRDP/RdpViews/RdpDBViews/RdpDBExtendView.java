package com.zhoukl.androidRDP.RdpViews.RdpDBViews;

import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @description： 数据感知控件的共性基类
 * @author zhoukl
 * @date 2014年11月12日 下午5:42:50
 */
public class RdpDBExtendView {

    //private ZklDBListView i
    private Context mContext; 
    public String mDBFieldName = "";

    public RdpDBExtendView(Context context) {
        mContext = context;
    }
    
    public String getFieldName() {
        return mDBFieldName;
    }
}

