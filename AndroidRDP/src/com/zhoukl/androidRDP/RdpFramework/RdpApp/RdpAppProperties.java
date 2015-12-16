package com.zhoukl.androidRDP.RdpFramework.RdpApp;

import java.util.HashMap;

public class RdpAppProperties {

    // ro.开头，一旦设置了，就不能修改
    // persist.开头设置成功后，需要缓存到本地文件中，下次启动会从中获取数据
    // 其他，则不保存
    HashMap<String, Object> mAppProperties = new HashMap<String, Object>();
    
    public void setProperty(String keyName, Object value) {
        mAppProperties.put(keyName, value);
    }
    
    public Object getProperty(String keyName) {
        return mAppProperties.get(keyName);
    }
    
    public boolean containsProperty(String keyName) {
        return mAppProperties.containsKey(keyName);        
    }
    
    public boolean containsValue(Object value) {
        return mAppProperties.containsValue(value);        
    }
    
    public void loadProperties(String fileName) {
        
    }
}
