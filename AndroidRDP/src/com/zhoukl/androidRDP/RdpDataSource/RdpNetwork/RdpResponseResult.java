package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork;

import android.text.TextUtils;

/**
 * @description: 
 * @author zhoukl
 *返回的数据格式声明：
 *{"flag":1,"msg":"查找成功!","totalSize":100,"data":[{...},...,{...}]}
 *{"flag":1,"msg":"查找成功!","totalSize":100,"data":{...}}
 */
public class RdpResponseResult {
	/** 网络请求成功(true) */
	public static final int OK = 1;


    /** 没有网络 */
    public static final int NO_NETWORK     = -1;
    /** 网络请求失败(false) */
    public static final int FAILED     = -2;
    /** 用户主动取消 */
    public static final int CANCELLED  = -3;

    /** 任务执行成功, 但返回结果为空 */
	public static final int EMPTY      = -4;
	
	/** 任务执行抛出错误 */
	public static final int ERROR = 0;
	/** 交易密码错误 */
	public static final int PWD_ERROR = 2;
	/** 验证码错误 */
	public static final int OVER_FIAL = 3;
	/** 验证码过期 */
	public static final int OVER_DUE = 4;
    /** 已到达末页 */
    public static final int ENDPAGE = 11;
	
	private String url = "";
	
	private int mTotalSize;
	public int getTotalSize() {
		return mTotalSize;
	}
	
	public void setTotalCount(int totalCount) {
		this.mTotalSize = totalCount;
	}

	/** 任务执行完成后的结果码 */
	private int code = FAILED;

	/** 返回的数据 */
	private String data = null;

	/** 服务器返回的提示信息(当返回值为 'OK' or 'FAILED', 从该字段取信息) */
	private String msg = "";

	/** 当任务执行抛出异常时, 此属性不为空 */
	private Exception exception = null;

	/**
	 * @return 网络请求的结果码
	 */
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return 当任务执行抛出异常时, 该方法返回结果不为空.
	 */
	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	/** 服务器返回的提示信息(当返回值为 'OK' or 'FAILED', 从该字段取信息) */
	public String getMsg() {
		if (TextUtils.isEmpty(msg)) {
			return "网络请求失败";
		}
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public static RdpResponseResult createNoNetworkResponse() {
	    RdpResponseResult result = new RdpResponseResult();
	    result.setCode(NO_NETWORK);
	    result.setMsg("当前设备没有连入网络，请确认！");
	    return result;
	}

}
