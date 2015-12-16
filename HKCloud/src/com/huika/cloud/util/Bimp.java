package com.huika.cloud.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bimp {
	public static int max = 0;
	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();

	// 图片sd地址 上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public static List<String> drr = new ArrayList<String>();

	/**
	 * 清理静态值
	 */
	public static void clearBimp() {
		max = 0;
		act_bool = true;
		for (Bitmap bitmap : bmp) {

			if (null != bitmap) {
				if (!bitmap.isRecycled())
					bitmap.recycle();
			}
		}
		if (null != bmp) {
			bmp.clear();
		}
		drr.clear();
	}

	public static Bitmap zoomBitmap(String path) {
		Bitmap bitmap = null;
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			int i = 0;
			while (true) {
				if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
					in = new BufferedInputStream(new FileInputStream(new File(path)));
					options.inSampleSize = (int) Math.pow(2.0D, i);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(in, null, options);
					break;
				}
				i += 1;
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
