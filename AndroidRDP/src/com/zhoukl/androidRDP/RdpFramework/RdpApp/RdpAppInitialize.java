package com.zhoukl.androidRDP.RdpFramework.RdpApp;

import java.util.HashMap;
import java.util.Locale;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zhoukl.androidRDP.RdpUtils.RdpLanguageUtil;

/**
 * @description：Application全局的初始化
 * @author zhoukl(67073753@qq.com)
 * @date 2015-8-31 下午2:13:36
 */
public class RdpAppInitialize {
    protected static RdpApplication mApplication;
    protected static Context mContext;
    public static RdpAppProperties mRdpAppProperties;
    
    public void initialize(RdpApplication application) {
        mApplication = application;
        mContext = mApplication.getApplicationContext();
        mRdpAppProperties = mApplication.mRdpAppProperties;
        
        initAppProperties();
        
        initSupportLanguages();
        
        initNetwork();
        
        
        initImageLoader();
    }

    
    public void initAppProperties() {
        
    }
    
    
    public void initNetwork() {
        
    }
    
    
    /**
     * @description：初始化当前APP所支持的所有语言
     * @author zhoukl(67073753@qq.com)
     * @date 2015-8-31 下午2:16:17
     */
    public void initSupportLanguages() {
        HashMap<String, Locale> localeMap = new HashMap<String, Locale>();
        localeMap.put("en", Locale.ENGLISH);
        localeMap.put("th", new Locale("th"));
        RdpLanguageUtil.initialize(localeMap);
    }
    
    public static void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
       ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(mContext);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

}
