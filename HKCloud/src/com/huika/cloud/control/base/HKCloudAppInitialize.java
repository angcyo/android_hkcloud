package com.huika.cloud.control.base;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Locale;

import com.huika.cloud.R;
import com.huika.cloud.util.PreferHelper;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpRequestParams;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetRequest.RdpNetwork;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.httpClient.HttpClient;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.httpClient.RdpHttpClientRequest;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpAppInitialize;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpApplication;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpConfiguration;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpConstant;
import com.zhoukl.androidRDP.RdpUtils.RdpLanguageUtil;
import com.zhoukl.androidRDP.RdpUtils.Security.huixinRSA.RsaHelper;
/**
 * @description：联盟实例的处理
 * @author shicm
 * @date 2015-11-5 下午3:22:41
 */
public class HKCloudAppInitialize extends RdpAppInitialize {

    @Override
    public void initialize(RdpApplication application) {
        super.initialize(application);
        PreferHelper.initialize(mApplication);
    }
        
    @Override
    public void initAppProperties() {
        mRdpAppProperties.setProperty(RdpConstant.PROPERTY_LISTVIEW_ERROR_RES_ID, R.layout.rdp_listview_no_network);
        mRdpAppProperties.setProperty(RdpConstant.PROPERTY_LISTVIEW_EMPTY_RES_ID, R.layout.rdp_listview_empty);
    }
    
    @Override
    public void initNetwork() {
        String PUBLIC_KEY_VALUE = "<RSAKeyValue><Modulus>wVwBKuePO3ZZbZ//gqaNuUNyaPHbS3e2v5iDHMFRfYHS/bFw+79GwNUiJ+wXgpA7SSBRhKdLhTuxMvCn1aZNlXaMXIOPG1AouUMMfr6kEpFf/V0wLv6NCHGvBUK0l7O+2fxn3bR1SkHM1jWvLPMzSMBZLCOBPRRZ5FjHAy8d378=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
        RSAPublicKey publicKey = (RSAPublicKey) RsaHelper.decodePublicKeyFromXml(PUBLIC_KEY_VALUE);

        
        RdpRequestParams.initEncryptkeys(RdpConfiguration.NET_REQ_PARAM_ENCRYPT_LIST, publicKey); //mRSAKeyEntity.mPublicKey);
        RdpHttpClientRequest netRequest = new RdpHttpClientRequest();
        RdpHttpClientRequest.initialize(new HttpClient(mContext));
        RdpNetwork.initNetworkRequest("HTTPCLIENT", netRequest, true);
    }

    @Override
    public void initSupportLanguages() {
        HashMap<String, Locale> localeMap = new HashMap<String, Locale>();
//        localeMap.put("en", Locale.ENGLISH);
//        localeMap.put("th", new Locale("th"));
        RdpLanguageUtil.initialize(localeMap);
    }
    
}
