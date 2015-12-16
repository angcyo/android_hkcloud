package com.huika.cloud.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.BroadcastConstants;
import com.huika.cloud.config.Constant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandFailedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandSuccessedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpModel.VersionBean;
import com.zhoukl.androidRDP.RdpUtils.NetUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author shicm
 * @description：版本管理类；
 * @date 2014年10月10日 下午12:19:15
 */
public class RdpVersionManager implements OnCommandSuccessedListener, OnCommandFailedListener {
	/** 防止多次点击检查更新 */
	private boolean updating;
	private Context context;
	// private long updateTime;
	private Object lock = new Object();
	/** 是否用户自行更新 */
	private boolean isSilent = true;
	/** 对话框 */
	private Dialog updateDialog;
	/** 是否取消更新 */
	private volatile boolean isCancel = false;

	/** 本地当前版本信息 */
	private PackageInfo currentVerInfo;
	/** 服务器最新版本信息 */
	private VersionBean latestVerInfo;

	/** 本地保存目录 */
	private static String localDir = Environment.getExternalStorageDirectory().getPath() + "/HKMall/update/";
	/** 本地保存文件名 */
	private static String localFileName = "HKMall.apk";

	/** 进度条 */
	private ProgressBar downloadBar;
	/** 进度值 */
	private TextView progressTv;
	private long fileSize;
	private AlertDialog mBuilder;
	private String mVersionUrl;

	// 获取新版本信息
	private static final int HANDLER_GET_LATEST_VERSION = 11;
	private static final int HANDLER_DOWNLOAD_PROGRESS = 12;
	private static final int HANDLER_DOWNLOAD_FINISH = 13;
	private static final int HANDLER_DOWNLOAD_ERROR = 14;
	private static final int HANDLER_DOWNLOAD_SIZE = 15;
	private static final int HANDLER_DOWNLOAD_STOP = 16;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			updating = false;
			switch (msg.what) {
				case HANDLER_GET_LATEST_VERSION:
					if (updateDialog != null && updateDialog.isShowing()) {
						updateDialog.dismiss();
					}
					// 获取最新版本
					if (isCancel) {
						break;
					}
					// 比较新版本与当前版本
					compareVersion();
					break;
				case HANDLER_DOWNLOAD_SIZE:
					downloadBar.setMax((int) fileSize);
					break;
				case HANDLER_DOWNLOAD_PROGRESS:
					updating = true;
					// 更新进度
					long downLen = msg.arg1;
					progressTv.setText(downLen * 100 / fileSize + "%");
					downloadBar.setProgress((int) downLen);
					break;
				case HANDLER_DOWNLOAD_FINISH:
					// 下载完成,转到安装界面
					installApkByPath(context, new File(localDir, localFileName).getAbsolutePath());
					if (null != mBuilder && mBuilder.isShowing()) {
						mBuilder.dismiss();
					}
					if (null != updateDialog && updateDialog.isShowing()) {
						updateDialog.dismiss();
					}
					break;
				case HANDLER_DOWNLOAD_STOP:
					if (null != updateDialog) {
						if (updateDialog.isShowing()) {
							updateDialog.dismiss();
						}
					}
					break;
				case HANDLER_DOWNLOAD_ERROR:
					// 下载发生错误
					String errMsg = (String) msg.obj;
					if (errMsg == null && errMsg.length() <= 0) {
						errMsg = "更新失败！";
					}
					Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
					if (updateDialog != null && updateDialog.isShowing()) {
						updateDialog.dismiss();
					}
					break;
				default:
					break;
			}
		}
	};

	public RdpVersionManager(Context context, String url) {
		super();
		this.context = context;
		this.mVersionUrl = url;
	}

	/**
	 * @description：检查版本更新;isSilent 是否自动检查更新
	 * @author samy
	 * @date 2014年10月9日 下午6:12:18
	 */
	public void checkVersion(boolean isSilent) {
		setSilent(isSilent);
		if (!NetUtil.isNetworkAvailable(context)) {
			return;
		}
		if (!isSilent()) {
			updateDialog = new Dialog(context, R.style.MMTheme);
			RotateAnimation rotate = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setRepeatCount(-1);
			rotate.setDuration(1500);
			updateDialog.setContentView(R.layout.dialog_updating);
			updateDialog.findViewById(R.id.update_loading_iv).startAnimation(rotate);
			updateDialog.show();
		}
		if (!updating) getLatestVersion();
		// handler.sendEmptyMessage(HANDLER_GET_LATEST_VERSION);
	}

	/**
	 * @description：从服务器获取新版本信息
	 * @author samy
	 * @date 2014年10月9日 下午6:08:46
	 */
	private void getLatestVersion() {
		currentVerInfo = getCurrentVersionInfo();
		// 泛型的描述
		Type typeOfT = new TypeToken<VersionBean>() {
		}.getType();

		RdpNetCommand versionCommand = new RdpNetCommand(context, typeOfT);
		versionCommand.setOnCommandSuccessedListener(this);
		versionCommand.setOnCommandFailedListener(this);
		versionCommand.setServerApiUrl(mVersionUrl);
		versionCommand.clearConditions();
		versionCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		versionCommand.setCondition("packageName", currentVerInfo.packageName);
		versionCommand.setCondition("versionNo", currentVerInfo.versionCode + "");
		versionCommand.setCondition("platform", 1);
		versionCommand.execute();
	}


	/**
	 * @description：获取本地当前版本信息
	 * @author samy
	 * @date 2014年10月9日 下午6:09:08
	 */
	private PackageInfo getCurrentVersionInfo() {
		PackageManager pm = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		try {
			PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packInfo;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @description：外部调用
	 * @author tsp
	 * @date 2015年11月5日 下午8:18:52
	 */
	public static PackageInfo getLoginCurrentVersionInfo(Context context) {
		PackageManager pm = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		try {
			PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packInfo;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @description：由应用包名得到应用信息
	 * @author samy
	 * @date 2014年10月17日 下午3:02:58
	 */
	public static PackageInfo getPkgInfoByName(Context context, String pkgName) {
		PackageInfo pkgInfo = null;
		PackageManager pm = context.getPackageManager();
		try {
			// 0代表是获取版本信息
			pkgInfo = pm.getPackageInfo(pkgName, 0);
		} catch (Exception e) {
		}
		return pkgInfo;
	}

	/**
	 * @description：比较本地版本和服务器版本
	 * @author samy
	 * @date 2014年10月9日 下午6:09:17
	 */
	private void compareVersion() {
		if (updateDialog != null && updateDialog.isShowing()) {
			updateDialog.dismiss();
		}
		if (latestVerInfo == null || currentVerInfo == null) {// 获取版本信息失败
			if (!isSilent()) {
				Toast.makeText(context, "版本不存在！", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		if (latestVerInfo.versionNo <= currentVerInfo.versionCode) {// 无需更新
			if (!isSilent()) {
				Toast.makeText(context, "已经是最新版本", Toast.LENGTH_SHORT).show();
			}
			return;
		} else {// 有新版本
			try {
				String negativeText = "稍后更新";
				if (latestVerInfo.isForceUpdate == 1) {
					negativeText = "退出";
				}
				// 有新版本
				final AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("发现新版本");
				builder.setCancelable(false);
				builder.setMessage(latestVerInfo.getUpdateInfo());
				builder.setPositiveButton("立刻更新", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						PreferHelper.getInstance().setString(PreferHelper.LAST_CHECK_VERSION, latestVerInfo.versionName);
						dialog.dismiss();
						updateVersion();
					}
				});

				builder.setNegativeButton(negativeText, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						builder.create().dismiss();
						if (latestVerInfo.getIsForceUpdate() == 1) {
							PreferHelper.getInstance().setLong(PreferHelper.LAST_VERSION_CHECK_TIME, System.currentTimeMillis());
							if (latestVerInfo.isForceUpdate == 1) {
								// 退出系统
								// KJActivityManager.create().AppExit(context);
								Intent intent = new Intent(BroadcastConstants.ACTION_EXIT_SYSTEM);
								context.sendBroadcast(intent);
							}
						}
					}
				});
				builder.create().show();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @description：更新版本
	 * @author samy
	 * @date 2014年10月17日 下午3:03:39
	 */
	protected void updateVersion() {
		File apkFile = new File(localDir, localFileName);
		/**
		 * 如果之前已经下载过就直接安装
		 */
		if (apkFile.exists()) {
			// ApplicationInfo appInfo = getApkInfoByPath(context, apkFile.getAbsolutePath());
			PackageInfo pkgInfo = getPackageInfoByPath(context, apkFile.getPath());
			if (pkgInfo != null && pkgInfo.packageName.equals(latestVerInfo.packageName)) {
				// 如果包名相同
				if (pkgInfo.versionCode == latestVerInfo.versionNo) {
					// 如果版本号等于最新版本号
					try {
						// 直接跳到安装界面
						Toast.makeText(context, "安装包已经存在，将直接安装！", Toast.LENGTH_SHORT).show();
						installApkByPath(context, apkFile.getCanonicalPath());
					} catch (Exception e) {
						Toast.makeText(context, "安装出错！", Toast.LENGTH_SHORT).show();
						apkFile.delete();
					}
					return;
				}
			}
		}
		File dir = new File(localDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		/**
		 * 去下载安装；
		 */
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("正在升级").setView(addLayout()).setCancelable(false);
			// 判断强制更新
			builder.setNegativeButton(latestVerInfo.isForceUpdate == 1 ? "退出" : "取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					isCancel = true;// 取消下载
					if (latestVerInfo.isForceUpdate == 1) {
						Intent intent = new Intent(BroadcastConstants.ACTION_EXIT_SYSTEM);
						context.sendBroadcast(intent);
					}
				}
			});
			mBuilder = builder.show();
			if (updating) {
				Toast.makeText(context, "正在下载中", 1).show();
			} else {
				downloadFile2(apkFile);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * @description：直接文件操作更新进度条；
	 * @author samy
	 * @date 2014年10月27日 上午9:58:02
	 */
	private void downloadFile2(final File saveFile) {
		new Thread(new Runnable() {
			@SuppressWarnings("resource")
			@Override
			public void run() {
				InputStream inStream = null;
				FileOutputStream fos = null;
				try {
					String dd = new java.text.DecimalFormat("0").format(Double.parseDouble(latestVerInfo.getFileSize().replace("byte", "")));
					fileSize = Long.parseLong(dd);
					if (fileSize > 0) {
						Log.d("md", "fileSize:" + fileSize);
						handler.sendEmptyMessage(HANDLER_DOWNLOAD_SIZE);
					} else {
						handler.sendEmptyMessage(HANDLER_DOWNLOAD_ERROR);
						return;
					}
					// TODO 测试用 暂时写死
					String fileName = latestVerInfo.getDownloadURL().substring(latestVerInfo.getDownloadURL().lastIndexOf("/") + 1);
					String downloadUrl = latestVerInfo.getDownloadURL();
					URL url = new URL(downloadUrl);
					HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
					inStream = urlConn.getInputStream();

					fos = new FileOutputStream(saveFile);
					byte[] buffer = new byte[1024];
					int offset = 0;
					int sum = 0;

					while ((offset = inStream.read(buffer, 0, 1024)) != -1) {
						if (isCancel) {
							// 暂停退出
							handler.obtainMessage(HANDLER_DOWNLOAD_STOP).sendToTarget();
							return;
						}
						fos.write(buffer, 0, offset);
						sum += offset;
						handler.obtainMessage(HANDLER_DOWNLOAD_PROGRESS, sum, 0).sendToTarget();
					}
					if (sum == fileSize) {
						handler.sendEmptyMessage(HANDLER_DOWNLOAD_FINISH);
					} else {
						handler.obtainMessage(HANDLER_DOWNLOAD_ERROR, "下载文件不匹配！").sendToTarget();
					}
				} catch (Exception e) {
					handler.obtainMessage(HANDLER_DOWNLOAD_ERROR, "文件下载失败！").sendToTarget();
				} finally {
					try {
						if (fos != null) {
							fos.close();
						}
						if (inStream != null) {
							inStream.close();
						}
					} catch (Exception e) {
					}
				}
			}
		}).start();
	}

	/**
	 * @description： 由路径得到app信息
	 * @author samy
	 * @date 2014年10月17日 下午3:00:41
	 */
	public static ApplicationInfo getApkInfoByPath(Context context, String absPath) {
		ApplicationInfo appInfo = null;
		PackageManager pm = context.getPackageManager();
		PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
		if (pkgInfo != null) {
			appInfo = pkgInfo.applicationInfo;
			/* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
			appInfo.sourceDir = absPath;
			appInfo.publicSourceDir = absPath;
		}

		return appInfo;
	}

	public static PackageInfo getPackageInfoByPath(Context context, String absPath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pkgInfo = null;
		try {
			pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
		} catch (Exception e) {
			// should be something wrong with parse
			e.printStackTrace();
		}
		return pkgInfo;
	}

	/**
	 * @description：由apk路径直接跳到安装界面
	 * @author samy
	 * @date 2014年10月17日 下午3:04:27
	 */
	public static void installApkByPath(Context context, String absPath) {
		String cmd = "chmod 777 " + absPath;
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + absPath), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public boolean isSilent() {
		synchronized (lock) {
			return isSilent;
		}
	}

	public void setSilent(boolean isSilent) {
		synchronized (lock) {
			this.isSilent = isSilent;
		}
	}

	public RelativeLayout addLayout() {
		RelativeLayout relativeLayout = new RelativeLayout(context);
		TextView textView = new TextView(context);
		textView.setId(1);
		textView.setTextSize(15);
		textView.setTextColor(Color.BLACK);
		textView.setText("下载进度:");
		RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		layoutParams1.setMargins(10, 10, 10, 10);
		relativeLayout.addView(textView, layoutParams1);

		progressTv = new TextView(context);
		progressTv.setId(2);
		progressTv.setTextSize(15);
		progressTv.setTextColor(Color.BLACK);
		RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams2.addRule(RelativeLayout.RIGHT_OF, 1);
		layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		layoutParams2.setMargins(0, 10, 10, 10);
		relativeLayout.addView(progressTv, layoutParams2);

		downloadBar = new ProgressBar(context);
		downloadBar.setId(3);
		downloadBar.setMax(100);
		downloadBar.setProgress(25);
		downloadBar.setMinimumHeight(20);
		ProgressBeanUtil.setFieldValue(downloadBar, "mOnlyIndeterminate", new Boolean(false));
		downloadBar.setIndeterminate(false);
		downloadBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_bar_states));
		// downloadBar.setProgressDrawable(context.getResources().getDrawable(android.R.drawable.progress_horizontal));
		downloadBar.setIndeterminateDrawable(context.getResources().getDrawable(android.R.drawable.progress_indeterminate_horizontal));
		RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20);

		layoutParams3.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		layoutParams3.addRule(RelativeLayout.BELOW, 1);
		layoutParams3.setMargins(10, 10, 10, 10);
		relativeLayout.addView(downloadBar, layoutParams3);
		return relativeLayout;
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		updating = false;
		if (!isSilent()) {
			Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
		}
		if (updateDialog != null && updateDialog.isShowing()) {
			updateDialog.dismiss();
		}
		if (isCancel) {
			return;
		}

	}

	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		updating = false;
		if (updateDialog != null && updateDialog.isShowing()) {
			updateDialog.dismiss();
		}
		if (isCancel) {
			return;
		}
		if (1 != result.getCode()) {
		} else {
			latestVerInfo = (VersionBean) data;
			compareVersion();
		}

	}
}