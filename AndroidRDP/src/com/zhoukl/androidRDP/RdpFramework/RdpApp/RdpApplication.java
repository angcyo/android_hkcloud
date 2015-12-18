package com.zhoukl.androidRDP.RdpFramework.RdpApp;

import android.app.Application;

import com.zhoukl.androidRDP.RdpModel.BaseUserBean;
import com.zhoukl.androidRDP.RdpUtils.Security.RSAKeyEntity;
import com.zhoukl.androidRDP.RdpUtils.Security.RSAUtil;

/**
 * @description：应用全局
 * @author zhoukl
 * @date 2014年11月12日 上午10:19:22
 */
public class RdpApplication extends Application {

    public static RdpAppProperties mRdpAppProperties;
    public BaseUserBean mCurrUser;
    public RSAKeyEntity mRSAKeyEntity;
    
    private RdpAppInitialize mRdpAppInitialize;

    @Override
    public void onCreate() {
        super.onCreate();
        mRdpAppProperties = new RdpAppProperties();
        mRSAKeyEntity = RSAUtil.generateRSAKey();
        if (mRSAKeyEntity == null) {
            onTerminate();
            return;
        }
        mRdpAppInitialize = getAppInitialize();
        if (mRdpAppInitialize == null) {
            onTerminate();
            return;
        }
        initAppEnvironment();
    }

    protected void initAppEnvironment() {
        mRdpAppInitialize.initialize(this);
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate(); 
    }

    protected RdpAppInitialize getAppInitialize() {
        return new RdpAppInitialize();
    }
    
    public boolean isLogin(boolean jumpToLoginActivity) {
        return false;
    }

    public String getMemberId(boolean jumpToLoginActivity) {
        if (mCurrUser == null) {
            return "";
        } else {
            return mCurrUser.memberId;
        }
    }
    
    public void setCurrUser(BaseUserBean user) {
        mCurrUser = user;
    }
}
