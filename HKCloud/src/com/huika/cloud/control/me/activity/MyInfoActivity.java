package com.huika.cloud.control.me.activity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.control.safeaccount.activity.AccountSafeActivity;
import com.huika.cloud.support.model.UpLoadImageResult;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.ImageTools;
import com.huika.cloud.util.MultipartEntity;
import com.huika.cloud.util.PreferHelper;
import com.huika.cloud.views.IMActionPopupItem;
import com.huika.cloud.views.IMButtomPopup;
import com.huika.cloud.views.IMButtomPopup.OnPopupItemOnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.httpClient.HttpClient;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpMultimedia.Image.RdpImageLoader;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

/**
 * @description：我的个人资料
 * @author ht
 * @date 2015-12-4 上午10:22:52
 */
public class MyInfoActivity extends RdpBaseActivity {
	private final static int CROPCAMARA_TAKEPHOTO = 1003;
	private final static int CROPCAMARA_SELECTFROMALBUM = 1004;
	private View mMasterView;
	@ViewInject(R.id.edit_picture)
	private LinearLayout edit_picture;
	@ViewInject(R.id.edit_nickname)
	private LinearLayout edit_nickname;
	@ViewInject(R.id.member_Level)
	private LinearLayout member_Level;
	@ViewInject(R.id.my_superior)
	private LinearLayout my_superior;
	@ViewInject(R.id.distributor_level)
	private LinearLayout distributor_level;
	@ViewInject(R.id.edit_recipient)
	private LinearLayout edit_recipient;
	@ViewInject(R.id.ll_safe)
	private LinearLayout ll_safe;
	@ViewInject(R.id.shop_my_data_picture)
	private ImageView shop_my_data_picture;
	private IMButtomPopup buttomPhotoPopup;
	private String[] buttomSpiners = { "上传头像", "拍照", "从手机相册中选择" };
	
	private File mPicFile;
	private View footView;
	private File tempFile;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("个人资料");
		mMasterView = addMasterView(R.layout.my_info_activity);
		footView = addFooterView(R.layout.my_info_foot);
		RdpAnnotationUtil.inject(this);
//		initView();
//		initListener();
		footView.findViewById(R.id.tv_log_out).setOnClickListener(this);
	}
	
	@OnClick({R.id.edit_picture,R.id.edit_nickname,R.id.member_Level,R.id.distributor_level,
		R.id.my_superior,R.id.edit_recipient,R.id.ll_safe,R.id.tv_log_out})
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.edit_picture:
			//编辑图像
			showSelectPhoto();
			break;
		case R.id.edit_nickname:
			//编辑昵称
			ToastMsg.showToastMsg(mApplication, "跳转到修改昵称界面");
			break;
		case R.id.member_Level:
			//会员等级
			ToastMsg.showToastMsg(mApplication, "跳转到会员等级说明界面");
			intent=new Intent(mApplication, MemberLevelActivity.class);
			startActivity(intent);
			break;
		case R.id.distributor_level:
			//分销商等级
			ToastMsg.showToastMsg(mApplication, "跳转到分销商等级说明界面");
			intent=new Intent(mApplication, DistributorlevelActivity.class);
			startActivity(intent);
			break;
		case R.id.my_superior:
			//我的上级
			ToastMsg.showToastMsg(mApplication, "跳转到上级介绍界面");
			intent=new Intent(mApplication, MySuperiorActivity.class);
			startActivity(intent);
			break;
		case R.id.edit_recipient:
			//我的收货地址
			ToastMsg.showToastMsg(mApplication, "跳转到地址界面");
			intent = new Intent(mApplication, MyRecipientActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_safe:
			//账号安全
			intent=new Intent(mApplication, AccountSafeActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_log_out:
			//退出登录
			HKCloudApplication.getInstance().setCurrUser(null);
			PreferHelper.getInstance().setString(PreferHelper.KEY_LOGIN_PHONE, null);
			PreferHelper.getInstance().setString(PreferHelper.KEY_LOGIN_PWD, null);
			MyInfoActivity.this.finish();
			break;
		default:
			super.onClick(v);
			break;
		}
		
	}
	
	private void showSelectPhoto() {
		if (null == buttomPhotoPopup) {
			buttomPhotoPopup = new IMButtomPopup(mApplication, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					mSexPopupClickLis);
		}
		else {
			buttomPhotoPopup.cleanAction();
		}
//		buttomPhotoPopup.addAction(new IMActionPopupItem(buttomSpiners[0], true, 0, false));
		buttomPhotoPopup.addAction(new IMActionPopupItem(buttomSpiners[1], false, CROPCAMARA_TAKEPHOTO, false));
		buttomPhotoPopup.addAction(new IMActionPopupItem(buttomSpiners[2], false, CROPCAMARA_SELECTFROMALBUM, false));
//		buttomPhotoPopup.setAnimationStyle(R.style.popupAnimation);
		buttomPhotoPopup.show(mMasterView);
	}
	
	OnPopupItemOnClickListener mSexPopupClickLis = new OnPopupItemOnClickListener() {

		@Override
		public void onPopupItemClick(IMActionPopupItem item, int position) {
			// TODO Auto-generated method stub
			switch (item.mItemValue) {
				case CROPCAMARA_TAKEPHOTO:// 拍照
					ToastMsg.showToastMsg(mApplication, "拍照");
					takePhoto();
					break;
				case CROPCAMARA_SELECTFROMALBUM:// 选择照片
					ToastMsg.showToastMsg(mApplication, "选择照片");
					selectPhoto();
					break;
				default:
					break;
			}
		}
	};
	
	/** 拍照 */
	private void takePhoto() {
		if (!ImageTools.isSDCardExist()) {
			ToastMsg.showToastMsg(mApplication,"sd卡不可用");
			return;
		}
		Intent cameraIntent = null;
		mPicFile = ImageTools.initTempFile();
		cameraIntent = ImageTools.getTakeCameraIntent(Uri.fromFile(mPicFile));
		startActivityForResult(cameraIntent, Constant.ACTIVITY_RESULT_CROPCAMARA_WITH_DATA);
	}

	/** 从相册选择 */
	private void selectPhoto() {
		mPicFile = ImageTools.initTempFile();
		Intent photoIntent = ImageTools.cropPhotoOfCompressFromGalleryIntent(Uri.fromFile(mPicFile)); 
		startActivityForResult(photoIntent, Constant.ACTIVITY_RESULT_CROPIMAGE_WITH_DATA);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) { return; }
		// TODO:返回处理
		switch (requestCode) {
			case Constant.ACTIVITY_RESULT_CROPCAMARA_WITH_DATA: // 拍照
				Intent intent = ImageTools.cropPhotoOfCompressIntent(Uri.fromFile(mPicFile)); 
				startActivityForResult(intent, Constant.ACTIVITY_RESULT_CROPIMAGE_WITH_DATA); 
				break;
			case Constant.ACTIVITY_RESULT_CROPIMAGE_WITH_DATA:
				if (null == data) { return; }
				if (TextUtils.isEmpty(mPicFile.toString()) || !mPicFile.exists()) {
					showToastMsg("没有选择到图片");
					return;
				}
				tempFile = mPicFile;
//				ImageLoader.getInstance().displayImage(Uri.fromFile(tempFile).toString(), );
				RdpImageLoader.displayImage(Uri.fromFile(tempFile).toString(), shop_my_data_picture);
				//上传图片
				uploadImage();
				break;
			default:
				break;
		}
	}

	private void uploadImage() {
		Type uploadResult=new TypeToken<UpLoadImageResult>(){}.getType();
		RdpNetCommand uploadCommand = new RdpNetCommand(mApplication, uploadResult);
//		uploadCommand.setServerApiUrl("http://192.168.49.198:8080/hxlmpro-api/User/uploadFile");
		uploadCommand.setServerApiUrl("http://192.168.49.198:8080/hxlmpro-api/User/testUploadFile");
		uploadCommand.setOnCommandSuccessedListener(this);
		uploadCommand.setOnCommandFailedListener(this);
		uploadCommand.clearConditions();
//		uploadCommand.setCondition("memberId", "123");
		uploadCommand.setCondition("fileName",tempFile);
		showLoadingOverLay(mMasterView);
		uploadCommand.execute();
			
	}
	
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result,
			Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		//更新用户图像
		UpLoadImageResult upLoadImageResult=(UpLoadImageResult) data;
		RdpImageLoader.displayImage(upLoadImageResult.imageUrl, shop_my_data_picture);
	}
	
	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
	}
	
	private void initListener() {
		edit_picture.setOnClickListener(this);
		edit_nickname.setOnClickListener(this);
		member_Level.setOnClickListener(this);
		distributor_level.setOnClickListener(this);
		my_superior.setOnClickListener(this);
		edit_recipient.setOnClickListener(this);
		ll_safe.setOnClickListener(this);
	}

	private void initView() {
		edit_picture = (LinearLayout) mMasterView.findViewById(R.id.edit_picture);
		edit_nickname = (LinearLayout) mMasterView.findViewById(R.id.edit_nickname);
		member_Level = (LinearLayout) mMasterView.findViewById(R.id.member_Level);
		distributor_level = (LinearLayout) mMasterView.findViewById(R.id.distributor_level);
		my_superior = (LinearLayout) mMasterView.findViewById(R.id.my_superior);
		edit_recipient = (LinearLayout) mMasterView.findViewById(R.id.edit_recipient);
		ll_safe = (LinearLayout) mMasterView.findViewById(R.id.ll_safe);
		shop_my_data_picture = (ImageView) mMasterView.findViewById(R.id.shop_my_data_picture);
		
	}
}

