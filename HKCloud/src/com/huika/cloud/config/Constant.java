package com.huika.cloud.config;

import android.os.Environment;


/**一些常量***/
public class Constant {
	
	/** 接口环境配置 */
	public static final String IS_RELEASE_ENVIRONMENT = "IS_RELEASE_ENVIRONMENT";
	public static final String IS_RELEASE_TEST_ENVIRONMENT = "IS_RELEASE_TEST_ENVIRONMENT";
	public static final String IS_TEST_ENVIRONMENT = "IS_TEST_ENVIRONMENT";
	public static final String IS_WEB_TEST_ENVIRONMENT = "WEB_TEST_ENVIRONMENT";
	
	public static final String HOMEDIR = getHomeDIR();
	private static final String getHomeDIR() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/rdp";
	}
	
	public static final String IMAGE_DIR = "/images/";
	// 临时图片缓存目录
	public static final String TEMP_CACHE_DIR = HOMEDIR + "/temp/";
	
	public static final String PUBLISH_FAVOLABLE_PIC_CACHE_DIR = HOMEDIR + IMAGE_DIR + "rdp/";
	
	/** 从 拍照中选择 */
	public static final int ACTIVITY_RESULT_CROPCAMARA_WITH_DATA = 1;
	/** 从 相册中选择 */
	public static final int ACTIVITY_RESULT_CROPIMAGE_WITH_DATA = 2;
	/** 从 拍照中选择不剪裁 */
	public static final int ACTIVITY_RESULT_NO_CROPCAMARA_WITH_DATA = 3;
	/** 从 相册中选择不剪裁 */
	public static final int ACTIVITY_RESULT_NO_CROPIMAGE_WITH_DATA = 4;
	/** 相册中选择了相片进去裁剪**/
	public static final int ACTIVITY_RESULT_CROPIMAGE_WITH_DATA_PHOTO = 5;
	/** 自定义图库选取**/
	public static final int ACTIVITY_RESULT_SELECT_PEC = 6;
	/** 保存购物车数量文件 */
	public static final String SAVE_SHOP_CART_FILE = "shopCartFile";
	/** 保存购物车数量字段 */
	public static final String SHOP_CART_NUMBER = "shopCartNumber";
	/**关闭绑定银行卡的界面*/
	public static final String FINISH_ACTIVITY = "finish_activity";
	/**选择收获地址区域*/
	public static final String CHOICE_AREA_RESULT_ID = "choiceAreaResultID";
	public static final String CHOICE_AREA_RESULT = "choiceAreaResult";

	/** 是否是第一次安装 应用 */
	public static final String IS_FIRST_INSTALL = "isFirstInstall";

	 // 微信支付API资料
	  // 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
	  public static final String APP_ID = "wxf87f6adee7e3c02f";
	  //"wx50268e3aff4bd560"; // wxf2f565574a968187
	/**获取用户信息*/
	public static final String GET_USER_INFO = "getUserInfo";
	/**选中的地址条目*/
	public static final String IPN_SELECTED_ITEM = "selectedItem";
	
}
