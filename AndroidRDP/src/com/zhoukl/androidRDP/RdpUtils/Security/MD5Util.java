package com.zhoukl.androidRDP.RdpUtils.Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description：
 * @author zhoukl(67073753@qq.com)
 * @date 2015-6-26 下午2:41:08
 */

public class MD5Util {
    private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		int t;
		for (int i = 0; i < 16; i++) {
			t = bytes[i];
			if (t < 0)
				t += 256;
			sb.append(hexDigits[(t >>> 4)]);
			sb.append(hexDigits[(t % 16)]);
		}
		return sb.toString();
	}

	public static String getMd5_16(String input, int bit) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
			if (bit == 16)
				return bytesToHex(md.digest(input.getBytes("utf-8"))).substring(8, 24);
			return bytesToHex(md.digest(input.getBytes("utf-8")));
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("Could not found MD5 algorithm.", e);
		}
	}

	public static String getMd5_32(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			re_md5 = buf.toString();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

}
