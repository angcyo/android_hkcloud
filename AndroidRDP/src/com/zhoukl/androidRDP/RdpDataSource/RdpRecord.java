package com.zhoukl.androidRDP.RdpDataSource;

import java.util.HashMap;

/**
 * @description：记录集的一条记录
 * @author zhoukl
 * @date 2014年11月25日 下午1:32:18
 */
public class RdpRecord {
	protected HashMap<String, Object> mRecord = new HashMap<String, Object>();
	
	public void setFieldValue(String fieldName, Object value) {
		mRecord.put(fieldName, value);
	}
	
	public Object getValue(String fieldName) {
		return mRecord.get(fieldName);
	}
	
	public String getStringValue(String fieldName) {
		return (String) mRecord.get(fieldName);
	}
	
	public int getIntValue(String fieldName) {
		return (Integer) mRecord.get(fieldName);
	}
	
   public int getFloatValue(String fieldName) {
        return (Integer) mRecord.get(fieldName);
    }

}
