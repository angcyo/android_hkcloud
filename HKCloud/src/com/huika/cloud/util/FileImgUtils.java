package com.huika.cloud.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.huika.cloud.config.Constant;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class FileImgUtils {  

	public static String SDPATH = Constant.PUBLISH_FAVOLABLE_PIC_CACHE_DIR;

	public static File saveBitmap(Bitmap zoomBitmap, String picName) {
		Log.e("", "保存图片");
		File f = null;
		try {
			if (!isFileExist("")) {  
				createSDDir("");
			}
			f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			if (zoomBitmap.compress(CompressFormat.JPEG, 80, out)) {
				out.flush();
				out.close();
			}
			zoomBitmap.recycle();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	/**
	 * 
	 * 方法概述：不做回收处理，发表优惠；
	 * 
	 * @author samy
	 * @date 2014-5-22 下午4:04:37
	 */
	public static File saveBitmap2(Bitmap zoomBitmap, String picName) {
		Log.e("", "保存图片");
		File f = null;
		try {
			if (!isFileExist("")) {
				createSDDir("");
			}
			f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			if (zoomBitmap.compress(CompressFormat.JPEG, 80, out)) {
				out.flush();
				out.close();
			}
			// zoomBitmap.recycle();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}
	/**
	 * 判断文件是否存在（参数绝对路径）
	 * @description：
	 * @author wangxuhao(379394493@qq.com)
	 * @date 2014年11月24日 上午10:56:18
	 */
	public static boolean isChatFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		}
		catch (Exception e) {

			return false;
		}
		return true;
	}

	/**
	 * 
	 * @description：文件转化成字节数组
	 * @author lzt
	 * @date 2014年6月26日 下午6:05:07
	 */
	public static byte[] getBytesFromFile(File file) {
		byte[] ret = null;
		try {
			if (file == null) {
//				IMDebug.print("helper:the file is null!");
				return null;
			}
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			byte[] b = new byte[4096];
			int n;
			while ((n = in.read(b)) != -1) {
				out.write(b, 0, n);
			}
			in.close();  
			out.close();
			ret = out.toByteArray();
		}
		catch (IOException e) {
//			IMDebug.print("helper:get bytes from file process error!");
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * 获取文件的扩展名
	 * @description：
	 * @author wangxuhao(379394493@qq.com)
	 * @date 2015年2月10日 下午3:22:47
	 */
	public static String getExtensionName(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length() - 1))) {   
                return filename.substring(dot + 1);   
            }   
        }   
        return "jpg";   
    }   

}
