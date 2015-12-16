package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetRequest;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.IRdpNetRequestCallBackListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpRequest;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;

public class RdpNetRequest {
    HashMap<Object, RdpRequest> mRequestMap = new HashMap<Object, RdpRequest>();
    RdpRequest mRequest;
    IRdpNetRequestCallBackListener mCallBackListener;

    
    public void addRequest(Object keyTag, RdpRequest rdpRequest) {
        if (mRequestMap.containsKey(keyTag)) {
            cancelRequest(keyTag);
        }
        mRequestMap.put(keyTag, rdpRequest);
        
    }
    
    public void cancelRequest(Object keyTag) {
        
    }

    
    protected void setCallBackListener(IRdpNetRequestCallBackListener callbackListener) {
        mCallBackListener = callbackListener;
    }


    // 解析字符串成 RdpResponseResult
    protected RdpResponseResult stringToZklResponseResult(String response) {
        if (null == response || response.isEmpty()) { return null; }
        
        RdpResponseResult result = new RdpResponseResult();
        try {
            parseJson(response, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    protected void parseJson(String jsonData, RdpResponseResult taskResult) throws IOException {
        // 解析JSON数据，首先要创建一个JsonReader对象
//      JsonReader reader = new JsonReader(new StringReader(jsonData));
//      reader.beginObject();
        /*
         * 返回的json格式{"data":XXX,"errorcode":"XXX","msg":"XXX"} 通过循环取得data，errorcode,msg,变量的值，放到taskResult对象中
         */
        
        try {
            JSONObject allResult = new JSONObject(jsonData);
            taskResult.setCode(allResult.optInt("flag", -1));
            taskResult.setTotalCount(allResult.optInt("totalSize", 0));
            taskResult.setMsg(allResult.optString("msg"));
//          if (allResult.)
//          taskResult.setData(allResult.optJSONObject("rs").toString());  // rs-->data
            taskResult.setData(allResult.optString("rs"));  // rs-->data
        } catch (JSONException e) {
            e.printStackTrace();
        } 
    }

}
