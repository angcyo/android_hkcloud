package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.httpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetRequest.RdpNetRequest;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpRequest;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * 异步任务实例类
 * 
 * @author time：2012-8-13 上午10:29:42
 */
public class RdpHttpClientRequest extends RdpNetRequest implements Observer {
	/** 任务key */
	//protected String mRequestKey = "mRequestKey";
	/** 任务提交监听器 */
	//protected IRdpNetRequestCallBackListener taskListener = null;
    public static HttpClient mHttpClient;
    protected HashMap<String, HttpClientReqAsyncTask> mAsyncTaskMap = new HashMap<String, HttpClientReqAsyncTask>();

//	protected AjaxParams mAjaxParams;// = new AjaxParams(); // 有可能不是AjaxParams，而是HttpEntity
	//protected HttpEntity mHttpEntity;
	//protected String mUrl = "";
    /**
     * 是否有网络连接
     */
    private boolean isNetworkAvailable = true;
    private boolean isCompleteOK = false;

    public static void initialize(HttpClient httpClient) {
		mHttpClient = httpClient;
	}
	
	/** 构建异步任务实例.
	 * @param activity 任务依附的Activity实例
	 * @param taskPreListener 任务提交监听器实例
	 * @param taskPostListener 任务返回结果监听器实例
	 * @param taskKey 任务ID
	 */
//	public RdpHttpClientRequest(IRdpNetRequestCallBackListener taskListener, String requestKey) {
//		this.taskListener = taskListener;
//		mRequestKey = requestKey;
//		isCompleteOK = false;
//	}

//	public IRdpNetRequestCallBackListener getAsynTaskListener() {
//		return taskListener;
//	}

//	public void addRequest(String URL, HttpEntity httpEntity) {
//		mUrl = URL;
//		mHttpEntity = httpEntity;
////		asynTask = new HttpClientReqAsyncTask();
////		mAsyncTaskMap.put(key, value)
////		asynTask.execute((Void) null);
//	}
	public void addRequest(Object keyTag, RdpRequest rdpRequest) {
//        if (!NetUtil.isNetworkAvailable()) {
//            rdpRequest.mCallBackListener.onErrorResult(keyTag, RdpResponseResult.createNoNetworkResponse());
//            return;
//        }
        
        //        mUrl = rdpRequest.mURL;
//        mHttpEntity = rdpRequest.mRequestParams.getEntity(); 
//        taskListener = rdpRequest.mCallBackListener;
        HttpClientReqAsyncTask asynTask = new HttpClientReqAsyncTask(keyTag, rdpRequest);
        //mAsyncTaskMap.put(rdpRequest.toString(), asynTask);
        asynTask.execute((Void) null);
	}
	
	public void addRequest(String requestKey, RdpRequest rdpRequest) {
//        mUrl = rdpRequest.mURL;
//        mHttpEntity = rdpRequest.mRequestParams.getEntity(); 
//        taskListener = rdpRequest.mCallBackListener;
        HttpClientReqAsyncTask asynTask = new HttpClientReqAsyncTask(requestKey, rdpRequest);
        //mAsyncTaskMap.put(rdpRequest.toString(), asynTask);
        asynTask.execute((Void) null);
	}
	
//    public void executeRequest(String requestKey, RdpRequest rdpRequest) {
//        mRequestKey = requestKey;
//        mUrl = rdpRequest.mURL;
//        mHttpEntity = rdpRequest.mRequestParams.getEntity();
//        taskListener = rdpRequest.mCallBackListener;
//        mAsynTask.execute((Void) null);
//    }
	
	
    // TODO: 是否有需要，待考虑
    @Override
    public void update(Observable observable, Object data) {
//        if (mRequestKey.equals(data)) {
//            if (mAsynTask.getStatus() == AsyncTask.Status.RUNNING) {
//                mAsynTask.cancel(true);
//            }
//        }
    }
	
	public Response executeHttpPost(RdpRequest rdpRequest) throws IOException {
		HttpPost post = new HttpPost(rdpRequest.mURL);
		Response result = null;
		HttpResponse httpResponse = null;
		post.setEntity(rdpRequest.mRequestParams.getEntity());
		
		Log.e("zhoukl", post.getEntity().toString());
		HttpParams params = post.getParams();
		
//		Log.e("zhoukl", params.toString());
		InputStream is = post.getEntity().getContent();
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		while((line=br.readLine())!=null){
			sb.append(line);
		}		
		Log.e("zhoukl---", sb.toString());
		
		StringEntity stringEntity = (StringEntity) post.getEntity();
		Header header = stringEntity.getContentEncoding();
		if (header == null) {
			Log.e("zhoukl---getContentEncoding", "getContentEncoding()=null");
		} else {
			Log.e("zhoukl---header", "Name:" + header.getName() + "   Value:" + header.getValue());
		}
		header = stringEntity.getContentType();
		if (header == null) {
			Log.e("zhoukl---getContentType", "getContentType()=null");
		} else {
			Log.e("zhoukl---getContentType", "Name:" + header.getName() + "   Value:" + header.getValue());
		}
		
		httpResponse = mHttpClient.getHttpClient().execute(post);
		if (httpResponse.getStatusLine().getStatusCode() != 200) {// 说明请求网络失败了，比如网络不可用
			Log.i("zhoukl", "请求网络失败");
			throw new HttpException(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase());
		}
		result = new Response(httpResponse);
		return result;
	}

	public boolean canRemove() {
		return isCompleteOK;
	}

//  AsyncTask<Void, Object, String> mAsynTask = new AsyncTask<Void, Object, String> () {
    class HttpClientReqAsyncTask extends AsyncTask<Void, Object, String> {
        Object mKeyTag;
        RdpRequest mRdpRequest;
        Exception mException;
        public HttpClientReqAsyncTask(Object keyTag, RdpRequest rdpRequest) {
            mKeyTag = keyTag;
            mRdpRequest = rdpRequest;
        }
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * 判断是否有网络，没有网络则不执行具体的异步任务
             */
            //if (!NetUtil.isNetworkAvailable()) {
                //isNetworkAvailable = false;
            //}

            // 任务提交前准备
//            if (!taskListener.onBeforeExecute(mRequestKey)) {
//                // 如果返回false,则取消此任务
//                this.cancel(false);
//            }
        }

        @Override
        protected String doInBackground(Void... params) {
            if (!isNetworkAvailable) {
                String result = new String();
//            result.setCode(String.FAILED);
//            result.setMsg("网络不可用,请设置网络！");
                return result;
            }
            
            
            Response response;
            String strResponse="";
            try {
                response = executeHttpPost(mRdpRequest);
                strResponse = response.asString();
            } catch (Exception e) {
                mException = e;
                //e.printStackTrace();
            }
            return strResponse;
//        return taskListener.doTaskInBackground(mRequestKey);
        }

        @Override
        protected void onPostExecute(String response) {
            RdpResponseResult result;
            if (mException != null) {
                result = new RdpResponseResult();
                if (mException instanceof HttpException) {
                    result.setCode(((HttpException) mException).mErrorCode);
                } else {
                    result.setCode(mException.hashCode());
                }
                result.setMsg(mException.getMessage());
                mRdpRequest.mCallBackListener.onErrorResult(mKeyTag, result);
                return;
            }
            //super.onPostExecute(result);
            // 处理返回结果
            //taskListener.onResult(mRequestKey, result);
            
            /* 临时处理 */
            if ("{\"flag\":1,\"msg\":\"关注成功\"}".equals(response)) {
                response = "{\"flag\":1,\"msg\":\"关注成功\",\"rs\":[1]}";
            }
            if ("{\"flag\":2,\"msg\":\"取消成功\"}".equals(response)) {
                response = "{\"flag\":2,\"msg\":\"取消成功\",\"rs\":[2]}";
            }
            
            
            
            
            result =  stringToZklResponseResult(response);
            if (result == null) {
                result = new RdpResponseResult();
                result.setCode(RdpResponseResult.ERROR);
                result.setMsg("网络请求失败！");
            } 
            if (result.getCode() == RdpResponseResult.OK) {
                mRdpRequest.mCallBackListener.onResult(mKeyTag, result);
            } else {
                mRdpRequest.mCallBackListener.onErrorResult(mKeyTag, result);
            }

            // TODO 需要提供给主线程处理的机会，比如：处理结束后，需要关闭数据加载界面等善后工作。
            
        }

        @Override
        protected void onCancelled(String response) {
            //解析 response 的错误码
            // TODO 需要提供给主线程处理的机会，比如：处理结束后，需要关闭数据加载界面等善后工作。
            RdpResponseResult result = new RdpResponseResult();
            result.setCode(RdpResponseResult.CANCELLED);
            result.setMsg("Users to cancel");
            result.setUrl(mRdpRequest.mURL);
            mRdpRequest.mCallBackListener.onErrorResult(mKeyTag, result);
        }

    }

//	public interface ZklHttpClientAsyncTaskListener {
//		/** 提交任务前先添加到任务观察器管理中.
//		 * @param task 实现了Observer接口的任务实例
//		 * @param taskKey 任务标识
//		 * @return 正常情况下返回true, 如果返回false, 应该终止任务继续执行.
//		 */
//		public boolean onBeforeExecute(long taskKey, RdpHttpClientRequest asyncTask);
//
//		/** 后台执行任务		 */
////		public String doTaskInBackground(long taskKey);
//
//		/** 任务返回后将要执行的操作. 		 */
//		public void onResult(long taskKey, String result);
//
//		/** 任务取消后将要执行的操作. 		 */
//		public void onCancelled(long taskKey, String result);
//	}
    
    public class HttpException extends java.io.IOException {
        int mErrorCode;
        
        public HttpException(int errorCode, String message) {
            
        }
    }

}
