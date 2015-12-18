package com.huika.cloud.control.safeaccount.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.config.UrlConstant;
import com.huika.cloud.control.base.HKCloudApplication;
import com.huika.cloud.support.model.UserModel;
import com.huika.cloud.util.ImageTools;
import com.huika.cloud.views.ActionPopupItem;
import com.huika.cloud.views.ButtomPopup;
import com.huika.cloud.views.ButtomPopup.OnPopupItemOnClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

import java.io.File;
import java.lang.reflect.Type;

/**
 * @description：实名认证（身份认证）
 * @author shicm
 * @date 2015-11-10 下午2:09:39
 */
public class AuthenticationActivity extends RdpBaseActivity implements OnPopupItemOnClickListener{
	public static final int RESULT_CODE = 99;
	private final static int CROPCAMARA_TAKEPHOTO = 1001;
	private final static int CROPCAMARA_SELECTFROMALBUM = 1002;
	@ViewInject(R.id.auth_name_edt)
    private EditText mEdtName;
	@ViewInject(R.id.auth_cardid_edt)
    private EditText mEdtCard;
	@ViewInject(R.id.img_card_face)
    private ImageView mImgFace;
	@ViewInject(R.id.img_card_oppositee)
    private ImageView mImgOppositee;
	@ViewInject(R.id.auth_card_sure_btn)
    private Button mBtnSureAuthl;
    private View mView;
	private ButtomPopup buttomPhotoPopup;
	
	private String[] buttomSpiners;
	
	private File mPicFile; // 选择图片路径
	private File tempFile;
	private int type = 0;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle(R.string.authentication_title);
		mView = addMasterView(R.layout.authentication_card);
		RdpAnnotationUtil.inject(this);
		buttomSpiners = getResources().getStringArray(R.array.auth_info_buttomSpiners_one);
	}
	
	@OnClick({R.id.img_card_face,R.id.img_card_oppositee,R.id.auth_card_sure_btn})
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.img_card_face:
				type = 0;
				showSelectPhoto();
				break;
			case R.id.img_card_oppositee:
				type = 1;
				showSelectPhoto();
				break;
			case R.id.auth_card_sure_btn:
				String  name = mEdtName.getText().toString();
				String  card  = mEdtCard.getText().toString();
				if (TextUtils.isEmpty(name)) {
					showToastMsg("名字不能为空");
					return;
				}
				if (TextUtils.isEmpty(card)) {
					showToastMsg("身份证不能为空");
					return;
				}
				executeAuth();
				break;

			default:
				break;
		}
	}
	
	/**
	 * @description：上传实名认证
	 * @author shicm
	 * @date 2015-11-17 下午2:20:15
	 */
	private void executeAuth() {
		showLoadingDialog("");
		UserModel mUser = HKCloudApplication.getInstance().getUserModel();
		Type authResult = new TypeToken<String>() {
		}.getType();
		RdpNetCommand authCommand = new RdpNetCommand(this, authResult);
		authCommand.setOnCommandSuccessedListener(this);
		authCommand.setOnCommandFailedListener(this);
		authCommand.setServerApiUrl(UrlConstant.USER_REALNAMEAUTHENTICATION); //网络链接地址接口
		authCommand.clearConditions(); 
		authCommand.setCondition("idNumber", mEdtCard.getText().toString());
		authCommand.setCondition("realName", mEdtName.getText().toString());
		authCommand.setCondition("merchantId", HKCloudApplication.MERCHANTID);
		authCommand.setCondition("memberId", mUser.memberId);
		authCommand.execute();
		
	}
	
	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		setResult(RESULT_CODE);//实名认证成功结果码
		dismissLoadingDialog();
		HKCloudApplication.getInstance().getUserModel().idNumber = mEdtCard.getText().toString();
		HKCloudApplication.getInstance().getUserModel().realName = mEdtName.getText().toString();
		HKCloudApplication.getInstance().getUserModel().realNameAuthentication = 1;
		showToastMsg(result.getMsg());
		finish();
	}
	
	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
		dismissLoadingDialog();
		showToastMsg(result.getMsg());
	}
	
	
	private void showSelectPhoto() {
		if (null == buttomPhotoPopup) {
			buttomPhotoPopup = new ButtomPopup(this, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					this);
		} else {
			buttomPhotoPopup.cleanAction();
		}
		buttomPhotoPopup.addAction(new ActionPopupItem(buttomSpiners[0], true, 0, false));
		buttomPhotoPopup.addAction(new ActionPopupItem(buttomSpiners[2], false,CROPCAMARA_SELECTFROMALBUM, false));
		buttomPhotoPopup.addAction(new ActionPopupItem(buttomSpiners[1], false,CROPCAMARA_TAKEPHOTO, false));
		buttomPhotoPopup.show(mView);
	}
	
	
	@Override
	public void onPopupItemClick(ActionPopupItem item, int position) {
		switch (item.mItemValue) {
			case CROPCAMARA_TAKEPHOTO:// 拍照
				takePhoto();
				break;
			case CROPCAMARA_SELECTFROMALBUM:// 选择照片
				selectPhoto();
				break;
			default:
				break;

		}
	}

	
	/** 拍照 */
	private void takePhoto() {
		if (!ImageTools.isSDCardExist()) {
			// showToastMsg(this.getString(R.string.chat_no_sdcard));
			return;
		}
		Intent cameraIntent = null;
		mPicFile = ImageTools.initTempFile();
		cameraIntent = ImageTools.getTakeCameraIntent(Uri.fromFile(mPicFile));
		startActivityForResult(cameraIntent, Constant.ACTIVITY_RESULT_CROPCAMARA_WITH_DATA);
	}
	
	/** 从相册选择 */
	private void selectPhoto() {
		Intent photoIntent = null;
		mPicFile = ImageTools.initTempFile();
		photoIntent = ImageTools.cropPhotoOfCompressFromGalleryIntent(Uri.fromFile(mPicFile));
		startActivityForResult(photoIntent, Constant.ACTIVITY_RESULT_CROPIMAGE_WITH_DATA);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) { return; }
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
				ImageLoader.getInstance().displayImage(Uri.fromFile(tempFile).toString(), type == 0 ? mImgFace : mImgOppositee);
				break;
			default:
				break;
		}
	}
}
