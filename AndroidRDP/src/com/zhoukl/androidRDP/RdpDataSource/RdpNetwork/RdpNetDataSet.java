package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import android.content.Context;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.zhoukl.androidRDP.RdpDataSource.RdpDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpRecord;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetRequest.RdpNetwork;
import com.zhoukl.androidRDP.RdpUtils.DataParser.DataParseUtil;

/**
 * @description：网络请求的记录集
 * @author zhoukl(67073753@qq.com)
 * @date 2015年6月19日 下午4:37:16
 * 返回的数据格式约定：  {"flag":n, "msg":"", "rs":[{}{}{}], "encoder":n}
 *      其中：encoder：是指rs值的编码类型，0为明文；1为使用RSA加密；2为使用AES加密；
 */
public class RdpNetDataSet extends RdpDataSet implements IRdpNetRequestCallBackListener {
	
	/** */
	protected long mTaskKey = 0;
	/** 在返回数据集的关键字，默认为空*/
	protected String mRecordKey = "";
	protected boolean mObjectToList = false;
	protected Type mTypeOfResult;
	
	protected boolean mAppendData = false;
	protected RdpRequest mRequest;
	
    public RdpNetDataSet(Context context) {
        this(context, new TypeToken<ArrayList<RdpRecord>>() {}.getType());
        //this(context, new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType());
    }

	public RdpNetDataSet(Context context, Type typeOfResult) {
		mContext = context;
		mTypeOfResult = typeOfResult;
		mRequest = new RdpRequest();
        mRequest.mCallBackListener = this;
		mTaskKey = System.currentTimeMillis();
	}
	
	public void setTypeOfResult(Type typeOfResult) {
	    mTypeOfResult = typeOfResult;
	}
	
	public void clearConditions() {
	    mRequest.mRequestParams.clear();
	}
	
	public void setCondition(String key, String value) {
	    mRequest.mRequestParams.put(key, value == null ? "" : value);
	}
	
	public void setCondition(String key, int value) {
	    mRequest.mRequestParams.put(key, value);
	}
	public void setCondition(String key, long value) {
		mRequest.mRequestParams.put(key, value);
	}
	public void setCondition(String key, double value) {
		mRequest.mRequestParams.put(key, value);
	}
	
	public void setCondition(String key, File value) {
		try {
		    mRequest.mRequestParams.put(key, value);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * @description： 在异步任务中进行服务器交互
     * @author zhoukl
     * @date 2014年11月12日 上午11:09:55
     */
	@Override
    public void open() {
        mAppendData = false;
        mRequest.mRequestParams.put("pageNo", 1);
        mRequest.mRequestParams.put("pageSize", mPageSise);
        RdpNetwork.addRequest(mRequest.mURL, mRequest);
    }

    public void open(int netFraType) {
        mAppendData = false;
        mRequest.mRequestParams.put("pageNo", 1);
        mRequest.mRequestParams.put("pageSize", mPageSise);
        
        RdpNetwork.addRequest("RequestKey", mRequest);
    }
	
	@Override
	public void getNextPageDatas() {
		if (getRecordCount() == getTotalCount()) return;

		mAppendData = true;
		int pageNo = getCurrPageNo();
		if (getRecordCount() > 0) {
			pageNo++;
		}
		mRequest.mRequestParams.put("pageNo", pageNo);
		mRequest.mRequestParams.put("pageSize", mPageSise);
		RdpNetwork.addRequest(mRequest.mURL, mRequest);
	}
	
//	@Override
//	public boolean onBeforeExecute(String requestKey) {
//		// TODO 需要提供给主线程处理的机会，比如：显示加载界面/进度条滚动，让用户知悉系统在后头运行
//		return true;
//	}

    @Override
    public String doTaskInBackground(String requestKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public void onResult(Object requestKey, RdpResponseResult result) {
		// TODO 需要提供给主线程处理的机会，比如：数据获取失败等
		if (result == null ) {
			Toast.makeText(mContext, "网络请求失败，请检查下网络！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (result.getCode() != RdpResponseResult.OK) {
			Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		String jsonData;
		if (mRecordKey.isEmpty()) {
			jsonData = result.getData();
		} else {
			jsonData = DataParseUtil.getJsonArrayString(result.getData(), mRecordKey);
		}
		if(mObjectToList) {
		    jsonData = "[" + jsonData + "]";
		}
		mTotalCount = result.getTotalSize();
//		parseJsonData(jsonData, true);
        ArrayList<Object> object = DataParseUtil.fromJson(jsonData, mTypeOfResult);
        boolean dealComplete = false;
        if (mOnDealDataListener != null) {
            dealComplete = mOnDealDataListener.onDealData(mDataList, object);
        }
        if (!dealComplete) {
    		if (!mAppendData) {
    		    mDataList.clear();
    			//mDataList = object;
    		} else {
    			//mDataList.addAll(object);
    		}
    		if (object != null)
    		    mDataList.addAll(object);
        }
        
		// TODO 需要提供给主线程处理的机会，比如：处理结束后，需要关闭数据加载界面等善后工作。
        if (mOnSuccessedListener != null) {
        	result.setUrl(mRequest.mURL);
            mOnSuccessedListener.onCommandSuccessed(requestKey, result, mDataList);
            // 在此事件中进行处理
            //mDataSetAdapter.notifyDataSetChanged();

        }
	}

	@Override
	public void onErrorResult(Object requestKey, RdpResponseResult result) {
		// TODO 需要提供给主线程处理的机会，比如：处理结束后，需要关闭数据加载界面、显示错误图片等善后工作。
	    
	    //mDataList.clear();
	    
	    if (mOnFailedListener != null) {
	    	result.setUrl(mRequest.mURL);
	        mOnFailedListener.onCommandFailed(requestKey, result);
		}
	}
	
	public String getRecordKey() {
		return mRecordKey;
	}

	public void setRecordKey(String recordKey) {
		mRecordKey = recordKey;
	}

	public void setObjectToList(boolean value) {
	    mObjectToList = value;    
	}
	
	public int getPageSise() {
		return mPageSise;
	}

	public void setPageSise(int pageSise) {
		mPageSise = pageSise;
	}

	public int getCurrPageNo() {
		int count = getRecordCount() / getPageSise();
		int round = getRecordCount() % getPageSise();
		if (round > 0) {
			return count + 1;
		} else {
			if (count == 0) return 1;
			else return count;
		}
	}

	public String getServerApiUrl() {
		return mRequest.mURL;
	}

	public void setServerApiUrl(String ServerApiUrl) {
	    mRequest.mURL = ServerApiUrl;
	}

  
}
