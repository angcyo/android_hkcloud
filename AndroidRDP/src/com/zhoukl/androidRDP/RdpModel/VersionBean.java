package com.zhoukl.androidRDP.RdpModel;

import java.io.Serializable;

/**
 * @description：获取软件版本信息
 * @author shicm
 * @date 2015-11-17 下午2:32:28
 */
public class VersionBean implements Serializable {
	private static final long serialVersionUID = -5415506163755359079L;
	/**
	 * @description：
	 * @author samy
	 * @date 2014年10月17日 下午5:33:06
	 */
	/** 应用名称 */
	public String appName;
	/** 包名 */
	public String packageName;
	/** 版本号 */
	public int versionNo;
	/** 版本名称 */
	public String versionName;
	/** 文件大小 */
	public String fileSize;
	/** 下载地址 */
	public String downloadURL;
	/** 更新内容 */
	public String updateInfo;
	/** 是否强制升级（0：否，1：是）*/
	public int isForceUpdate;

	// 给数据库看的；
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(int versionNo) {
		this.versionNo = versionNo;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

	public int getIsForceUpdate() {
		return isForceUpdate;
	}

	public void setIsForceUpdate(int isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
	}

	@Override
	public String toString() {
		return "VersionBean [appName=" + appName + ", packageName=" + packageName + ", versionNo=" + versionNo + ", versionName=" + versionName + ", fileSize=" + fileSize + ", downloadURL=" + downloadURL + ", updateInfo=" + updateInfo + ", isForceUpdate=" + isForceUpdate + "]";
	}

}
