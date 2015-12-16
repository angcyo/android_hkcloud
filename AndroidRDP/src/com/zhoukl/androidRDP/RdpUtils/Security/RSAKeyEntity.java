package com.zhoukl.androidRDP.RdpUtils.Security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @description：RSA的KeyPair实体,增加了KeyPair对应的字符串，便于发布公钥给对方。
 * @author zhoukl(67073753@qq.com)
 * @date 2015-4-15 下午3:53:09
 */
public class RSAKeyEntity {
    public RSAPublicKey mPublicKey;
    public String mPublicKeyStr;
    
    public RSAPrivateKey mPrivateKey;
    public String mPrivateKeyStr;

}
