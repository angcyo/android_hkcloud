package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork;

import java.lang.reflect.Type;

public class RdpRequest {

    public int method;
    public String mURL;
    public RdpRequestParams mRequestParams;
    public IRdpNetRequestCallBackListener mCallBackListener;
    
    public Type mTypeOfResult;
    public RdpRequest() {
        mRequestParams = new RdpRequestParams();
    }
    
    public RdpRequest(String URL, RdpRequestParams requestParams
            , IRdpNetRequestCallBackListener callBackListener, Type typeOfResult) {
        mURL = URL;
        mRequestParams = requestParams;
        mCallBackListener = callBackListener;
        mTypeOfResult = typeOfResult;
    }

    //Listener<String> listener, ErrorListener errorListener)


}
