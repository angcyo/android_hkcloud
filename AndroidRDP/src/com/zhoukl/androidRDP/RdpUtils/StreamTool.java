package com.zhoukl.androidRDP.RdpUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author
 *         time：2012-8-6 下午6:06:00
 *         description:
 */

/*
 * I/O流操作
 */
public class StreamTool {

	/*
	 * 数据流转成byte数组
	 * @prama isreturn byte[]
	 */

	public static byte[] convertStreamToByteArray(InputStream is) {
		if (is == null) {
			return null;
		}

		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.close();
			return baos == null ? null : baos.toByteArray();
		}
		catch (IOException e) {
			return null;
		}
		finally {
			closeOutputStream(baos);
		}
	}

	/*
	 * 数据流转成string
	 * @prama is return string
	 */
	public static String convertStreamToString(InputStream is) {
		byte[] byteArray = convertStreamToByteArray(is);
		if (byteArray != null && byteArray.length > 0) {
			try {
				return new String(byteArray, "UTF-8");
			}
			catch (UnsupportedEncodingException e) {
				// ignore.
			}
		}
		return "";
	}

	/**
	 * 关闭输入流, 释放资源.
	 * 
	 * @param is
	 *            输入流
	 */
	public static void closeInputStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
				is = null;
			}
			catch (IOException e) {
				// ignore.
			}
		}
	}

	/**
	 * 关闭输出流, 释放资源.
	 * 
	 * @param os
	 *            输出流
	 */
	public static void closeOutputStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
				os = null;
			}
			catch (IOException e) {
				// ignore.
			}
		}
	}

	/**
	 * 将文件输入流写到文件
	 * 
	 * @param is
	 * @param os
	 * @return
	 */
	public static boolean saveStreamToFile(InputStream is, FileOutputStream os) {
		if (is == null || os == null) {
			return false;
		}

		try {
			int len = -1;
			byte[] b = new byte[1024];

			while ((len = is.read(b)) != -1) {
				os.write(b, 0, len);
				os.flush();
			}
		}
		catch (IOException e) {
			return false;
		}

		return true;
	}

}
