package com.huika.cloud.control.main.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.CustomViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huika.cloud.R;
import com.huika.cloud.support.model.ProductImageArray;
import com.huika.cloud.util.FileTool;
import com.huika.cloud.views.ActionPopupItem;
import com.huika.cloud.views.ButtomPopup;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhoukl.androidRDP.RdpViews.photoview.PhotoView;
import com.zhoukl.androidRDP.RdpViews.photoview.PhotoViewAttacher.OnPhotoTapListener;

/**
 * 查看图片大图公共类 从相关页面跳转过来时要传递key为imageDetails的List<ImageInfo> mPhotoLists数据
 * @description：ldm
 * @date 2014年12月12日 下午3:19:44
 */
@SuppressLint("CutPasteId")
public class ImageDetailAct extends Activity implements OnPageChangeListener, OnClickListener, ButtomPopup.OnPopupItemOnClickListener {
	public static final String INP_IMAGELIST = "IMAGELIST";  // 图片的列表
	public static final String INP_IMAGEDETAILCHECK = "IMAGEDETAILCHECK"; // 第一张展示的
	
	
	private ImageButton mButton;
	private ImageButton mBtnMoreSettings;
	private TextView mTv;// 显示图片当前数量及总数量
	// private TextView mDesc;// 对图片的描述
	// private LinearLayout mBottomLinLayout;// 显示图片描述及数量的布局
	private CustomViewPager mImageDetailViewPager;
	private ImagePageViewAdapter mShopPhotoAdapter;
	// private List<ImageInfo> mPhotoLists = new ArrayList<ImageInfo>();
	private ArrayList<CommonBigImgBean> mPhotoLists = new ArrayList<CommonBigImgBean>();
	private List<PhotoView> mListImage = new ArrayList<PhotoView>();
	private String mPhotoCheck;
	private Integer mPageNo;
	private ButtomPopup buttomPopup;
	private String[] buttomSpiners = { "保存到手机" };
	/** 用于判断对应页面图片是否加载完成，以便没加载完成不允许发送 */
	private List<Boolean> isLoadCompleted;
	private int checkIndex = -1;

	private LinearLayout layoutDot;

	/** 要使用查看大图功能的话，相应的bean要实现该接口 */
	public interface CommonBigImgBean {
		String getCommonBigImg();
		void setCommonBigImg();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		init();
	}

	public void init() {
		initDatas();
		mTv = (TextView) findViewById(R.id.image_detail_bottom_tv);
		mButton = (ImageButton) findViewById(R.id.image_detail_back_btn);
		mBtnMoreSettings = (ImageButton) findViewById(R.id.more_settings_ibt);
		// mDesc = (TextView) findViewById(R.id.image_detail_bottom_tv);
		// mBottomLinLayout = (LinearLayout)
		// findViewById(R.id.image_detail_ablume_lin);
		mImageDetailViewPager = (CustomViewPager) findViewById(R.id.image_deteil_vp);
		layoutDot = (LinearLayout) findViewById(R.id.layout_dot);

		mShopPhotoAdapter = new ImagePageViewAdapter(mListImage);
		mImageDetailViewPager.setAdapter(mShopPhotoAdapter);
		mButton.setVisibility(View.GONE);
		for (int i = 0; i < mPhotoLists.size(); i++) {
			CommonBigImgBean temp = mPhotoLists.get(i);
			if (!TextUtils.isEmpty(mPhotoCheck) && temp.getCommonBigImg().equals(mPhotoCheck)) {
				checkIndex = i;
				break;
			}
		}
		if (checkIndex != -1) {
			mImageDetailViewPager.setCurrentItem(checkIndex);
			refreshCurrentPageInfo(checkIndex);
		} else {
			mImageDetailViewPager.setCurrentItem(0);
			refreshCurrentPageInfo(0);
		}

		buttomPopup = new ButtomPopup(this, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, this);
		buttomPopup.addAction(new ActionPopupItem(buttomSpiners[0], false));
		setDot();
		initListeners();
	}

	private void setDot() {
		for (int i = 0; i < mPhotoLists.size(); i++) {
			ImageView iv = new ImageView(this);
			LayoutParams params = new LayoutParams(30, 30);
			params.rightMargin = 12;
			iv.setLayoutParams(params);
			if (i == checkIndex) {
				iv.setImageResource(R.drawable.dot_checked);
			} else {
				iv.setImageResource(R.drawable.dot_uncheck);
			}
			layoutDot.addView(iv);
		}
	}

	protected void initDatas() {
		ArrayList<ProductImageArray> parcelableArrayListExtra = getIntent().getParcelableArrayListExtra(INP_IMAGELIST);
		while (!parcelableArrayListExtra.isEmpty()) {
			mPhotoLists.add(parcelableArrayListExtra.remove(0));
		}
		for (int i = 0; i < mPhotoLists.size(); i++) {
			mListImage.add(null);
		}
		mPhotoCheck = getIntent().getStringExtra(INP_IMAGEDETAILCHECK);
	}

	protected void initListeners() {
		mButton.setOnClickListener(this);
		mBtnMoreSettings.setOnClickListener(this);
		mImageDetailViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int newPage) {
				refreshCurrentPageInfo(newPage);
				for (int i = 0; i < layoutDot.getChildCount(); i++) {
					ImageView iv = (ImageView) layoutDot.getChildAt(i);
					if (i != newPage) {
						iv.setImageResource(R.drawable.dot_uncheck);
					} else {
						iv.setImageResource(R.drawable.dot_checked);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	protected void refreshCurrentPageInfo(int newPage) {
		mPageNo = newPage;
		mTv.setText(newPage + 1 + "/" + mPhotoLists.size());
	}

	/**
	 * 
	 * @description 详细描述： ViewPager的图片适配器
	 */
	class ImagePageViewAdapter extends PagerAdapter {
		public List<PhotoView> mViewArray;

		private LayoutInflater inflater;

		public ImagePageViewAdapter(List<PhotoView> listImage) {
			inflater = getLayoutInflater();
			mViewArray = listImage;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return mPhotoLists.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			if (isLoadCompleted == null) {
				int size = mPhotoLists.size();
				isLoadCompleted = new ArrayList<Boolean>(size);
				for (int i = 0; i < size; i++)
					isLoadCompleted.add(false);
			}
			View imageLayout = inflater.inflate(R.layout.shop_gallery_image, view, false);
			assert imageLayout != null;

			PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.gallery_image);
			mViewArray.set(position, imageView);
			imageView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					buttomPopup.show(v);
					return false;
				}

			});
			imageView.setOnPhotoTapListener(new OnPhotoTapListener() {
				@Override
				public void onPhotoTap(View view, float x, float y) {
					finish();
				}
			});

			CommonBigImgBean photo = mPhotoLists.get(position);

			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
			if (photo != null)
				ImageLoader.getInstance().displayImage(photo.getCommonBigImg(), imageView, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						spinner.setVisibility(View.VISIBLE);
					}

					@SuppressWarnings("unused")
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						String message = "图片加载异常";
						Toast.makeText(ImageDetailAct.this, message, Toast.LENGTH_SHORT).show();
						spinner.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						isLoadCompleted.set(position, true);
						spinner.setVisibility(View.GONE);
					}
				});
			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	// @Override
	// public void onItemClick(ActionPopupItem item, int position) {
	//
	// }

	/**
	 * 方法概述：保存图片文件
	 * 
	 * @description 方法详细描述：
	 */
	private String saveImage(boolean tempSave) {
		String tarFileName = "pic_" + System.currentTimeMillis() + ".jpg";
		String forderName = "/picture";
		ImageLoader imageLoader = ImageLoader.getInstance();
		String imgUrl = mPhotoLists.get(mPageNo).getCommonBigImg();
		Bitmap bitmap = imageLoader.loadImageSync(imgUrl);
		if (null == bitmap) {
			File oldFile = imageLoader.getDiscCache().get(imgUrl);
			copyFile(tarFileName, forderName, oldFile, tempSave);
		} else {
			try {
				// BitmapUtils.saveImageTo(photo, spath)
				FileTool.saveFile(bitmap, tarFileName, forderName);
				if (!tempSave) {
					Toast.makeText(ImageDetailAct.this, "图片成功保存到/hkcloud/savePic文件夹下。", Toast.LENGTH_SHORT).show();
					refreshGallery(this, new File(FileTool.getDefaultfoldername() + forderName + "/" + tarFileName));
				}
			} catch (IOException e) {
				if (!tempSave) {
					Toast.makeText(ImageDetailAct.this, "保存图片失败！", Toast.LENGTH_SHORT).show();
				}
			}
		}
		return FileTool.getDefaultfoldername() + forderName + "/" + tarFileName;
	}

	private void copyFile(String tarFileName, String forderName, File file, Boolean tempSave) {
		if (file != null && file.exists()) {
			String subForder = FileTool.getDefaultfoldername() + forderName;
			File foder = new File(subForder);
			if (!foder.exists()) {
				foder.mkdirs();
			}
			try {
				FileTool.copyFile(file.getAbsolutePath(), subForder + "/" + tarFileName);
				if (!tempSave) {
					Toast.makeText(ImageDetailAct.this, "图片成功保存到/hkcloud/savePic文件夹下。", Toast.LENGTH_SHORT).show();
					refreshGallery(this, new File(FileTool.getDefaultfoldername() + forderName + "/" + tarFileName));
				}
			} catch (Exception e) {
				Toast.makeText(ImageDetailAct.this, "保存图片失败！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 通知刷新这块的图库
	public void refreshGallery(Context context, File file) {

		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		context.sendBroadcast(intent);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		refreshCurrentPageInfo(arg0);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_settings_ibt:
			buttomPopup.show(v);
			break;
		case R.id.image_detail_back_btn:
			finish();
			break;
		}
	}

	@Override
	public void onPopupItemClick(ActionPopupItem item, int position) {
		if (item.mTitle.equals(buttomSpiners[0])) {
			saveImage(false);
		}
	}
}
