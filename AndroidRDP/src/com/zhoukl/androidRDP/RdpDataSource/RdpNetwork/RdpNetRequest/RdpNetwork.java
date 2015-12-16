package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetRequest;

import java.io.IOException;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.IRdpNetRequestCallBackListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpRequest;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpRequestParams;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;

import android.content.Context;



/**
 * 网络请求框架的接口层，对应用开发屏蔽了具体网络请求框架的实现细节
 * @author zhoukl
 *
 */
public class RdpNetwork   {
	public static final int NFT_HTTPCLIENT 	= 1;
	public static final int NFT_VOLLEY 		= 2;
	public static final String NRT_DEFAULT ="NRT_DEFAULT";
	private int mNetFraType = NFT_HTTPCLIENT;
	
	protected RdpRequestParams mRequestParams = new RdpRequestParams(); // 很有可能不是AjaxParams，而是HttpEntity
	protected String mUrl = "";
	private Context mContext;
	private IRdpNetRequestCallBackListener mCallBackListener;
	long mTaskKey = 0;
	
	
	protected static RdpNetRequest mDefaultNetRequest;
	protected static HashMap<String, RdpNetRequest> mNetRequestMap = new HashMap<String, RdpNetRequest>();
	
    public static void initNetworkRequest(String netReqType, RdpNetRequest netRequest) {
        mNetRequestMap.put(netReqType, netRequest);
        if (mDefaultNetRequest == null) {
            mNetRequestMap.put(NRT_DEFAULT, netRequest);
            mDefaultNetRequest = netRequest;
        }
    }
    public static void initNetworkRequest(String netReqType, RdpNetRequest netRequest, boolean defaultReq) {
        mNetRequestMap.put(netReqType, netRequest);
        mNetRequestMap.put(NRT_DEFAULT, netRequest);
        mDefaultNetRequest = netRequest;
    }

    
    public RdpNetwork(Context context, IRdpNetRequestCallBackListener listener) {
		mContext = context;
		mCallBackListener = listener;
	}
	
    
    public static void addRequest(Object keyTag, RdpRequest rdpRequest) {
        getNetRequest().addRequest(keyTag, rdpRequest);
    }
    
    public static void cancelRequest(String requestKey) {
        getNetRequest().cancelRequest(requestKey);
    }
    
    
    
    public static RdpNetRequest getNetRequest() {
        if (mDefaultNetRequest == null) {
            Exception err = new Exception("not init Network Request instance!");
            System.err.println(err.getMessage());
            return null;
        }
        
        return mDefaultNetRequest;
    }
    
    public static RdpNetRequest getNetRequest(String netReqType) {
        if (!mNetRequestMap.containsKey(netReqType)) {
            Exception err = new Exception("not init Network Request [" + netReqType + "] instance!");
            System.err.println(err.getMessage());
            return null;
        }
        
        return mNetRequestMap.get(netReqType);
    }


    
    
    
//	public void executeRequest(String url, RdpRequestParams ajaxParams) {
//		executeRequest(url, ajaxParams, mNetFraType == NFT_HTTPCLIENT ? NFT_HTTPCLIENT : NFT_HTTPCLIENT);
//	}
//	
//    public void executeRequest(String url, RdpRequestParams ajaxParams, int netFraType) {
//		mUrl = url;
//		mRequestParams = ajaxParams;
//		mTaskKey = System.currentTimeMillis();
//		
//		getNetRequest().addRequest("", mUrl, mRequestParams.getEntity());
//		
//		
//	}
	
	
/*

	private void execute_volley() {
		StringRequest stringRequest = new StringRequest(Method.POST, mUrl, mListener, mErrorListener) {
			@Override
			public HttpEntity getHttpEntity() {
				return mRequestParams.getEntity();
			}
		};
		
		mQueue.add(stringRequest);
	}

	Response.Listener<String> mListener = new Response.Listener<String>() {
		@Override
		public void onResponse(String response) {
			
			RdpResponseResult result = stringToZklResponseResult(response);
			mZklResponseListener.onReponse(mTaskKey, result);
		}
	};
		
	Response.ErrorListener mErrorListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			// 暂不对外抛出此处理
			Log.e("TAG", error.getMessage(), error);
			RdpResponseResult result = new RdpResponseResult();
			result.setCode(error.networkResponse.statusCode);
			result.setMsg(error.networkResponse.toString());
			result.setUrl(error.url);
			mZklResponseListener.onCancelled(mTaskKey, result);
		}
	};
	*/
	
	
	
	// 解析字符串成 ZklResponseResult
	public RdpResponseResult stringToZklResponseResult(String response) {
		if (null == response || response.isEmpty()) { return null; }
		
		RdpResponseResult result = new RdpResponseResult();
		try {
			parseJson(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}

	public void parseJson(String jsonData, RdpResponseResult taskResult) throws IOException {
		// 解析JSON数据，首先要创建一个JsonReader对象
//		JsonReader reader = new JsonReader(new StringReader(jsonData));
//		reader.beginObject();
		/*
		 * 返回的json格式{"data":XXX,"errorcode":"XXX","msg":"XXX"} 通过循环取得data，errorcode,msg,变量的值，放到taskResult对象中
		 */
		
		try {
			JSONObject allResult = new JSONObject(jsonData);
			taskResult.setCode(allResult.optInt("flag", -1));
			taskResult.setTotalCount(allResult.optInt("totalSize", 0));
			taskResult.setMsg(allResult.optString("msg"));
//			if (allResult.)
//			taskResult.setData(allResult.optJSONObject("rs").toString());  // rs-->data
			taskResult.setData(allResult.opt("rs").toString());  // rs-->data
			
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}




}
