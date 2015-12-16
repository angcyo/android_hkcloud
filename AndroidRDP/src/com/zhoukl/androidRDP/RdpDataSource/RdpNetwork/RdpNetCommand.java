package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import android.content.Context;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetRequest.RdpNetwork;
import com.zhoukl.androidRDP.RdpUtils.DataParser.DataParseUtil;

/**
 * @description：单个网络obj网络处理
 * @date 2015-11-9 下午3:00:57
 */
public class RdpNetCommand extends RdpCommand implements IRdpNetRequestCallBackListener {

    protected Type mTypeOfResult;
    protected RdpRequest mRequest;
    /** 返回数据集的关键字，默认为空  */
    protected String mRecordKey = "";
    protected boolean mResultIsArray = false;
    
    public RdpNetCommand(Context context, Type typeOfResult) {
        mTypeOfResult = typeOfResult;
        mRequest = new RdpRequest();
        mRequest.mCallBackListener = this;
    }
    
    public void clearConditions() {
        mRequest.mRequestParams.clear();
    }
    
    public void setCondition(String key, String value) {
        mRequest.mRequestParams.put(key, value);
    }
    public void setCondition(String key, long value) {
    	mRequest.mRequestParams.put(key, value);
    }
    public void setCondition(String key, double value) {
    	mRequest.mRequestParams.put(key, value);
    }
    
    public void setCondition(String key, int value) {
        mRequest.mRequestParams.put(key, value);
    }
    
    public void setCondition(String key, File value) {
        try {
            mRequest.mRequestParams.put(key, value);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void execute() {
        RdpNetwork.addRequest(mRequest.mURL, mRequest);
    }

    public void setServerApiUrl(String ServerApiUrl) {
        mRequest.mURL = ServerApiUrl;
    }
    
    public void setRecordKey(String recordKey) {
        mRecordKey = recordKey;
    }

    public void setResultIsArray(boolean value) {
        mResultIsArray = value;
    }
    
    @Override
    public String doTaskInBackground(String requestKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onResult(Object reqKey, RdpResponseResult result) {
        String jsonData;
        Object object;
        if (mRecordKey.isEmpty()) {
            jsonData = result.getData();
        } else {
            jsonData = DataParseUtil.getJsonArrayString(result.getData(), mRecordKey);
        }
        if (mResultIsArray) {
            object = DataParseUtil.fromJson(jsonData, mTypeOfResult);
        } else {
            object = DataParseUtil.fromJson1(jsonData, mTypeOfResult);
        }
        
        // TODO 需要提供给主线程处理的机会，比如：处理结束后，需要关闭数据加载界面等善后工作。
        if (mOnSuccessedListener != null) {
        	result.setUrl(mRequest.mURL);
            mOnSuccessedListener.onCommandSuccessed(reqKey, result, object);
        }
    }

    @Override
    public void onErrorResult(Object reqKey, RdpResponseResult result) {
        if (mOnFailedListener != null) {
        	result.setUrl(mRequest.mURL);
            mOnFailedListener.onCommandFailed(reqKey, result);
        }
    }
}
