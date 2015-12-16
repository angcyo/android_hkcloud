package com.zhoukl.androidRDP.RdpUtils.Security;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

import com.zhoukl.androidRDP.RdpUtils.Security.huixinRSA.Base64Encoder;
import com.zhoukl.androidRDP.RdpUtils.Security.huixinRSA.Base64Helper;


import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * @description：RSA工具类
 * @author zhoukl(67073753@qq.com)
 * @date 2015-4-15 下午3:53:09
 */
public class RSAUtil {
    private static final String ALGORITHM = "RSA";
    private static final int RSA_KEY_SIZE = 1024;
    private static Cipher cipher;

    static {
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            // java
            // Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            // android
            // Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description： 生成公钥和私钥
     */
    public static RSAKeyEntity generateRSAKey() {
        return generateRSAKey(RSA_KEY_SIZE);
    }

    public static RSAKeyEntity generateRSAKey(int keySize) {
        RSAKeyEntity result = new RSAKeyEntity();
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
            // 密钥位数
            keyPairGen.initialize(keySize);
            // 生成公钥、秘钥
            KeyPair keyPair = keyPairGen.generateKeyPair();

            result.mPublicKey = (RSAPublicKey) keyPair.getPublic();
            result.mPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            result.mPublicKeyStr = getKeyString(result.mPublicKey);
            result.mPrivateKeyStr = getKeyString(result.mPrivateKey);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 使用公钥对明文进行加密，返回BASE64编码的字符串
     */
    public static String encrypt(String data, RSAPublicKey publicKey) {
        String result = "";
        if (publicKey == null)
            return result;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 模长
            int key_len = publicKey.getModulus().bitLength() / 8;
            // 加密数据长度 <= 模长-11, 如果明文长度大于模长-11则要分组加密
            BASE64Encoder encoder = new BASE64Encoder();
            byte[] decodeByteArray = data.getBytes("UTF-8");
            byte[][] arrays = splitArray(decodeByteArray, key_len - 11);
            for (byte[] arr : arrays) {
                result += encoder.encode(cipher.doFinal(arr));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @description: 使用私钥对密文进行解密
     */
    public static String decrypt(String data, RSAPrivateKey privateKey) {
        String result = "";
        if (privateKey == null)
            return result;

        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 模长
            int key_len = privateKey.getModulus().bitLength() / 8;
            byte[] bcd = (new BASE64Decoder()).decodeBuffer(data);
            // 如果密文长度大于模长则要分组解密
            byte[][] arrays = splitArray(bcd, key_len);
            byte[] bytes = new byte[bcd.length];
            int dstPos = 0;
            for (byte[] arr : arrays) {
                byte[] a = cipher.doFinal(arr);
                System.arraycopy(a, 0, bytes, dstPos, a.length);
                dstPos += a.length;
            }
            result = new String(bytes).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * @description: 拆分数组
     */
    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    /**
     * @description: 得到密钥字符串（经过base64编码）
     */
    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
//        String s = (new BASE64Encoder()).encode(keyBytes);
        String s = ( new Base64Encoder()).encode(keyBytes);
        return s;
    }

    /**
     * @description: 得到公钥
     */
    public static RSAPublicKey getPublicKey(String key) {
        try {
            byte[] keyBytes;
            keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @description: 使用模和指数生成RSA公钥
     *               注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，
     *               如Android默认是RSA/None/NoPadding】
     * @param modulus 模
     * @param exponent 指数
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到私钥
     */
    public static RSAPrivateKey getPrivateKey(String key) {
        RSAPrivateKey privateKey = null;

        try {
            byte[] keyBytes;
            keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * @description: 使用模和指数生成RSA私钥
     *               注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，
     *               如Android默认是RSA/None/NoPadding】
     * @param modulus 模
     * @param exponent 指数
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    /*
     * java端公钥转换成C#公钥
     */
    public static String encodePublicKeyToXml(PublicKey key) {
        if (!RSAPublicKey.class.isInstance(key)) { return null; }
        RSAPublicKey pubKey = (RSAPublicKey) key;
        StringBuilder sb = new StringBuilder();

        sb.append("<RSAKeyValue>");
        sb.append("<Modulus>").append(Base64Helper.encode(pubKey.getModulus().toByteArray())).append("</Modulus>");
        sb.append("<Exponent>").append(Base64Helper.encode(pubKey.getPublicExponent().toByteArray())).append("</Exponent>");
        sb.append("</RSAKeyValue>");
        return sb.toString();
    }

    /*
     * C#端公钥转换成java公钥
     */
    public static PublicKey decodePublicKeyFromXml(String xml) {
        xml = xml.replaceAll("\r", "").replaceAll("\n", "");
        BigInteger modulus = new BigInteger(1, Base64Helper.decode(getMiddleString(xml, "<Modulus>", "</Modulus>")));
        BigInteger publicExponent = new BigInteger(1, Base64Helper.decode(getMiddleString(xml, "<Exponent>", "</Exponent>")));

        RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);

        KeyFactory keyf;
        try {
            keyf = KeyFactory.getInstance("RSA");
            return keyf.generatePublic(rsaPubKey);
        }
        catch (Exception e) {
            return null;
        }
    }

    /*
     * C#端私钥转换成java私钥
     */
    public static PrivateKey decodePrivateKeyFromXml(String xml) {
        xml = xml.replaceAll("\r", "").replaceAll("\n", "");
        BigInteger modulus = new BigInteger(1, Base64Helper.decode(getMiddleString(xml, "<Modulus>", "</Modulus>")));
        BigInteger publicExponent = new BigInteger(1, Base64Helper.decode(getMiddleString(xml, "<Exponent>", "</Exponent>")));
        BigInteger privateExponent = new BigInteger(1, Base64Helper.decode(getMiddleString(xml, "<D>", "</D>")));
        BigInteger primeP = new BigInteger(1, Base64Helper.decode(getMiddleString(xml, "<P>", "</P>")));
        BigInteger primeQ = new BigInteger(1, Base64Helper.decode(getMiddleString(xml, "<Q>", "</Q>")));
        BigInteger primeExponentP = new BigInteger(1, Base64Helper.decode(getMiddleString(xml, "<DP>", "</DP>")));
        BigInteger primeExponentQ = new BigInteger(1, Base64Helper.decode(getMiddleString(xml, "<DQ>", "</DQ>")));
        BigInteger crtCoefficient = new BigInteger(1, Base64Helper.decode(getMiddleString(xml, "<InverseQ>", "</InverseQ>")));

        RSAPrivateCrtKeySpec rsaPriKey = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);

        KeyFactory keyf;
        try {
            keyf = KeyFactory.getInstance("RSA");
            return keyf.generatePrivate(rsaPriKey);
        }
        catch (Exception e) {
            return null;
        }
    }

    /*
     * java端私钥转换成C#私钥
     */
    public static String encodePrivateKeyToXml(PrivateKey key) {
        if (!RSAPrivateCrtKey.class.isInstance(key)) { return null; }
        RSAPrivateCrtKey priKey = (RSAPrivateCrtKey) key;
        StringBuilder sb = new StringBuilder();

        sb.append("<RSAKeyValue>");
        sb.append("<Modulus>").append(Base64Helper.encode(priKey.getModulus().toByteArray())).append("</Modulus>");
        sb.append("<Exponent>").append(Base64Helper.encode(priKey.getPublicExponent().toByteArray())).append("</Exponent>");
        sb.append("<P>").append(Base64Helper.encode(priKey.getPrimeP().toByteArray())).append("</P>");
        sb.append("<Q>").append(Base64Helper.encode(priKey.getPrimeQ().toByteArray())).append("</Q>");
        sb.append("<DP>").append(Base64Helper.encode(priKey.getPrimeExponentP().toByteArray())).append("</DP>");
        sb.append("<DQ>").append(Base64Helper.encode(priKey.getPrimeExponentQ().toByteArray())).append("</DQ>");
        sb.append("<InverseQ>").append(Base64Helper.encode(priKey.getCrtCoefficient().toByteArray())).append("</InverseQ>");
        sb.append("<D>").append(Base64Helper.encode(priKey.getPrivateExponent().toByteArray())).append("</D>");
        sb.append("</RSAKeyValue>");
        return sb.toString();
    }
 
    /**
     * 返回两个字符串中间的内容
     * 
     * @param all
     * @param start
     * @param end
     * @return
     */
    public static String getMiddleString(String all, String start, String end) {
        int beginIdx = all.indexOf(start) + start.length();
        int endIdx = all.indexOf(end);
        return all.substring(beginIdx, endIdx);
    }
}


/**
 * @description: ASCII码转BCD码
 * 暂无用，现使用Base64码
 */
/*   public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
    byte[] bcd = new byte[asc_len / 2];
    int j = 0;
    for (int i = 0; i < (asc_len + 1) / 2; i++) {
        bcd[i] = asc_to_bcd(ascii[j++]);
        bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
    }
    return bcd;
}

public static byte asc_to_bcd(byte asc) {
    byte bcd;

    if ((asc >= '0') && (asc <= '9'))
        bcd = (byte) (asc - '0');
    else if ((asc >= 'A') && (asc <= 'F'))
        bcd = (byte) (asc - 'A' + 10);
    else if ((asc >= 'a') && (asc <= 'f'))
        bcd = (byte) (asc - 'a' + 10);
    else
        bcd = (byte) (asc - 48);
    return bcd;
}
*/
/**
 * @description: BCD转字符串
 */
/*    public static String bcd2Str(byte[] bytes) {
    char temp[] = new char[bytes.length * 2], val;

    for (int i = 0; i < bytes.length; i++) {
        val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
        temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

        val = (char) (bytes[i] & 0x0f);
        temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
    }
    return new String(temp);
}
*/
