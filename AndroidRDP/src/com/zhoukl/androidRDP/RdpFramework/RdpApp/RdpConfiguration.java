package com.zhoukl.androidRDP.RdpFramework.RdpApp;

import java.util.HashSet;

public class RdpConfiguration {
    
    public static final String APP_NAME = "androidRDP";
    /** 网络请求中需要加密的参数列表 */
    public static HashSet<String> NET_REQ_PARAM_ENCRYPT_LIST; // = getNetReqParamEncryptList(); 

    static {
        NET_REQ_PARAM_ENCRYPT_LIST = new HashSet<String>();
        //TODO: 最终要改成从配置文件上进行配置，config.ini
      /*  NET_REQ_PARAM_ENCRYPT_LIST.add("password");
        NET_REQ_PARAM_ENCRYPT_LIST.add("oldPassword");
        NET_REQ_PARAM_ENCRYPT_LIST.add("newPassword");
        NET_REQ_PARAM_ENCRYPT_LIST.add("account");
        NET_REQ_PARAM_ENCRYPT_LIST.add("accountId");
        NET_REQ_PARAM_ENCRYPT_LIST.add("MemberId");
        NET_REQ_PARAM_ENCRYPT_LIST.add("userName");
        NET_REQ_PARAM_ENCRYPT_LIST.add("merchantName");
        NET_REQ_PARAM_ENCRYPT_LIST.add("merchantAccount");
        NET_REQ_PARAM_ENCRYPT_LIST.add("merchantId");
        NET_REQ_PARAM_ENCRYPT_LIST.add("amount");
        NET_REQ_PARAM_ENCRYPT_LIST.add("referrer");
        NET_REQ_PARAM_ENCRYPT_LIST.add("friendId");
        NET_REQ_PARAM_ENCRYPT_LIST.add("friendName");
        NET_REQ_PARAM_ENCRYPT_LIST.add("friendName");
        NET_REQ_PARAM_ENCRYPT_LIST.add("phoneMsg");
        NET_REQ_PARAM_ENCRYPT_LIST.add("psaKey");
        
        NET_REQ_PARAM_ENCRYPT_LIST.add("productId");*/
        
    }

}
