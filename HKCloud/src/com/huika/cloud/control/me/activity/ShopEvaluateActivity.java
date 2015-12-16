package com.huika.cloud.control.me.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.control.me.view.MultiImageViewGroup;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by angcyo on 2015/12/3 15:14.
 */
public class ShopEvaluateActivity extends RdpBaseActivity
		implements MultiImageViewGroup.OnAddViewClickListener, MultiImageViewGroup.OnImageViewClickListener {
	private View mMasterView;
	
	@ViewInject(R.id.shop_evaluate_image_group)
	private MultiImageViewGroup multiImageViewGroup;
	private Uri mImageUri;
	
	@ViewInject(R.id.shop_image)
	private ImageView mShopImage;// 显示商品的图片
	@ViewInject(R.id.shop_title)
	private TextView mShopTitle;// 显示商品的标题
	@ViewInject(R.id.shop_summary1)
	private TextView mShopSummary1;// 描述1
	@ViewInject(R.id.shop_summary2)
	private TextView mShopSummary2;// 描述2
	@ViewInject(R.id.shop_price)
	private TextView mShopPrice;// 价格
	@ViewInject(R.id.shop_num)
	private TextView mShopNum;// 数量

	@ViewInject(R.id.shop_rating)
	private RatingBar mShopRating;// 商品评价等级 0-5
	@ViewInject(R.id.shop_evaluate_content)
	private EditText mShopEvaluateContentEdit;// 商品评价内容

	@ViewInject(R.id.shop_anonymity)
	private CheckBox mShopAnonymity;// 匿名评价
	@ViewInject(R.id.shop_ok)
	private Button mShopOk;// 确定

	@ViewInject(R.id.bottom_layout)
	private ViewGroup mShopBottomLayout;// 底部view

	private SelectorDialogFragment selectorDialogFragment;

	private static StateListDrawable generateRoundBgDrawable(int radii, int pressCol, int defCol,
			DisplayMetrics metrics) {
		radii = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radii, metrics);

		// 圆角矩形
		float[] outerRadii = new float[] { radii, radii, radii, radii, radii, radii, radii, radii };
		Shape roundShape = new RoundRectShape(outerRadii, null, null);

		// 圆角drawable
		ShapeDrawable pressDraw = new ShapeDrawable(roundShape);
		ShapeDrawable defDraw = new ShapeDrawable(roundShape);
		pressDraw.getPaint().setColor(pressCol);
		defDraw.getPaint().setColor(defCol);

		StateListDrawable bgStateDrawable = new StateListDrawable();
		bgStateDrawable.addState(new int[] { android.R.attr.state_pressed }, pressDraw);
		bgStateDrawable.addState(new int[] {}, defDraw);

		return bgStateDrawable;
	}

	@SuppressLint("NewApi")
	private static void setBgDrawable(View view, Drawable drawable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("商品评价");
		removeLeftFuncView(TBAR_FUNC_BACK);
		addLeftView();

		mMasterView = addMasterView(R.layout.shop_evaluate_activity);
		RdpAnnotationUtil.inject(this);
		initView();
		initViewListener();
		initDatas();
	}

	private void initDatas() {
		setmShopPrice(800);

		setBgDrawable(mShopOk, generateRoundBgDrawable(5, getResources().getColor(R.color.shop_evaluate_bt_p),
				getResources().getColor(R.color.shop_evaluate_bt_n), getResources().getDisplayMetrics()));
		setBgDrawable(mShopBottomLayout,
				generateRoundBgDrawable(5, Color.WHITE, Color.WHITE, getResources().getDisplayMetrics()));
	}

	private void initViewListener() {
		multiImageViewGroup.setAddListener(this);
		multiImageViewGroup.setImageViewListener(this);

		selectorDialogFragment.setListener(new SelectorDialogFragment.OnClickListener() {
			@Override
			public void onClick(View v, int position) {
				if (position == 0) {
					getPhotoFromCamera();
				} else if (position == 1) {
					getPhotoFromPhotos();
				}
			}
		});

		mShopOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showLoadingDialog("稍等...");
				loadingDialog.setCancelable(false);
			}
		});
	}

	private void initView() {
		ArrayList<String> options = new ArrayList<String>();
		options.add("拍照获取");
		options.add("相册获取");
		selectorDialogFragment = SelectorDialogFragment.getInstance(options);
	}

	public String getmShopImage() {
		return (String) this.mShopImage.getTag();
	}

	public void setmShopImage(String imagePath) {
		Bitmap bitmap = MultiImageViewGroup.getThumbBitmap(imagePath, mShopImage.getWidth(), mShopImage.getHeight());
		this.mShopImage.setImageBitmap(bitmap);
	}

	public String getmShopTitle() {
		return this.mShopTitle.getText().toString();
	}

	public void setmShopTitle(String mShopTitle) {
		this.mShopTitle.setText(mShopTitle);
	}

	public String getmShopSummary1() {
		return mShopSummary1.getText().toString();
	}

	public void setmShopSummary1(String mShopSummary1) {
		this.mShopSummary1.setText(mShopSummary1);
	}

	public String getmShopSummary2() {
		return mShopSummary2.getText().toString();
	}

	public void setmShopSummary2(String mShopSummary2) {
		this.mShopSummary2.setText(mShopSummary2);
	}

	public String getmShopPrice() {
		return mShopPrice.getText().toString();
	}

	public void setmShopPrice(float price) {
		String p = String.format("￥%01.2f", price);//
		this.mShopPrice.setText(p);
	}

	public void setmShopPrice(String mShopPrice) {
		this.mShopPrice.setText("￥" + mShopPrice);
	}

	public String getmShopNum() {
		return mShopNum.getText().toString();
	}

	public void setmShopNum(int mShopNum) {
		this.mShopNum.setText("x" + mShopNum);
	}

	public void setmShopNum(String mShopNum) {
		this.mShopNum.setText("x" + mShopNum);
	}

	/**
	 * 评价等级 0-5
	 */
	public float getmShopRating() {
		return mShopRating.getRating();
	}

	/**
	 * 评价等级 0-5
	 */
	public void setmShopRating(float mShopRating) {
		this.mShopRating.setRating(mShopRating);
	}

	public String getmShopEvaluateContentEdit() {
		return mShopEvaluateContentEdit.getText().toString();
	}

	public void setmShopEvaluateContentEdit(String mShopEvaluateContentEdit) {
		this.mShopEvaluateContentEdit.setText(mShopEvaluateContentEdit);
	}

	/**
	 * 是否是匿名评
	 */
	public boolean getmShopAnonymity() {
		return mShopAnonymity.isChecked();
	}

	public void setmShopAnonymity(boolean mShopAnonymity) {
		this.mShopAnonymity.setChecked(mShopAnonymity);
	}

	private void addLeftView() {
		Button backButton = new Button(this);
		backButton.setTag(TBAR_FUNC_BACK);
		backButton.setText("返回");
		backButton.setTextColor(Color.WHITE);
		backButton.setCompoundDrawablesWithIntrinsicBounds(com.zhoukl.androidRDP.R.drawable.common_head_back_select, 0,
				0, 0);
		backButton.setBackgroundResource(com.zhoukl.androidRDP.R.drawable.common_head_btn_selector);
		backButton.setCompoundDrawablePadding(-40);
		backButton.setOnClickListener(this);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		layoutParams.setMargins(-50, 0, 0, 0);
		mLltTitleBarLeftArea.addView(backButton, layoutParams);
	}

	@Override
	public void onAddViewClick(View v) {
		selectorMode();
	}

	private void selectorMode() {
		selectorDialogFragment.show(getSupportFragmentManager(), selectorDialogFragment.getClass().getSimpleName());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String imagePath = null;// = mImageUri.getPath();

		/** getPhotoFromPhotos */
		if (requestCode == 100 && resultCode == RESULT_OK) {
			String scheme = data.getScheme();
			if (scheme.equalsIgnoreCase(ContentResolver.SCHEME_CONTENT)) {
				imagePath = getPathFromUri(data.getData());
			} else {
				imagePath = data.getData().getPath();
			}
		}

		/** getPhotoFromCamera */
		if (requestCode == 110 && resultCode == RESULT_OK) {
			imagePath = mImageUri.getPath();
		}

		if (!TextUtils.isEmpty(imagePath)) {
			if (!multiImageViewGroup.addImageView(imagePath)) {
				ToastMsg.showToastMsg(this, "添加图片失败");
			}
		} else {
			ToastMsg.showToastMsg(this, "未选择图片");
		}
	}

	protected String getPathFromUri(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		String result;
		Cursor cursor;
		cursor = getContentResolver().query(uri, projection, null, null, null);

		int column_index = cursor.getColumnIndexOrThrow(projection[0]);
		cursor.moveToFirst();

		result = cursor.getString(column_index);
		cursor.close();
		return result;
	}

	public void getPhotoFromCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		mImageUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				UUID.randomUUID() + ".jpg"));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri); // set the image
																// file name
		startActivityForResult(intent, 110);
	}

	public void getPhotoFromPhotos() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, 100);
		// startActivityForResult(Intent.createChooser(intent, "选择图片"), 100);
	}

	@Override
	public void onImageViewClick(View v) {
		multiImageViewGroup.removeImageView(v);
	}

	public static class SelectorDialogFragment extends DialogFragment implements View.OnClickListener {
		private static final String KEY_OPTIONS = "options";
		ArrayList<String> options;
		private OnClickListener listener;

		public static SelectorDialogFragment getInstance(ArrayList<String> options) {
			Bundle bundle = new Bundle();
			bundle.putStringArrayList(KEY_OPTIONS, options);
			SelectorDialogFragment dialogFragment = new SelectorDialogFragment();
			dialogFragment.setArguments(bundle);
			return dialogFragment;
		}

		private static StateListDrawable generateBgDrawable(int pressCol, int defCol) {
			ColorDrawable pressDraw = new ColorDrawable(pressCol);
			ColorDrawable defDraw = new ColorDrawable(defCol);

			StateListDrawable bgStateDrawable = new StateListDrawable();
			bgStateDrawable.addState(new int[] { android.R.attr.state_pressed }, pressDraw);
			bgStateDrawable.addState(new int[] {}, defDraw);

			return bgStateDrawable;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setStyle(STYLE_NO_TITLE, getTheme());

			Bundle args = getArguments();
			options = args.getStringArrayList(KEY_OPTIONS);
			if (options == null) {
				dismiss();
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
				@Nullable Bundle savedInstanceState) {
			View rootView = generateRootView();
			return rootView;
		}

		private View generateRootView() {
			// 根布局
			LinearLayout linearLayout = new LinearLayout(getActivity());
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));

			// options 添加选项
			View button;
			for (int i = 0; i < options.size(); i++) {
				String option = options.get(i);
				button = generateOptionView(option, i);
				linearLayout.addView(button);
			}

			// 添加分割线
			View divideView = new View(getActivity());
			divideView.setBackgroundColor(Color.parseColor("#a3a3a3"));
			divideView
					.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue
							.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, getResources().getDisplayMetrics())));
			linearLayout.addView(divideView);

			// 添加取消按钮
			Button cancelButton = (Button) generateOptionView("取消", -1);
			linearLayout.addView(cancelButton);
			cancelButton.setTextColor(new ColorStateList(new int[][] { { android.R.attr.state_pressed }, {} },
					new int[] { Color.RED, Color.BLACK }));

			return linearLayout;
		}

		private View generateOptionView(String text, int pos) {
			Button option = new Button(getActivity());
			option.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			option.setText(text);
			StateListDrawable bgDraw = generateBgDrawable(Color.parseColor("#40000000"), Color.TRANSPARENT);
			setBgDrawable(option, bgDraw);
			option.setTag(pos);
			option.setOnClickListener(this);
			return option;
		}

		public void setListener(OnClickListener listener) {
			this.listener = listener;
		}

		@Override
		public void onClick(View v) {
			int pos = (Integer) v.getTag();
			// 非取消按钮
			if (pos != -1 && listener != null) {
				listener.onClick(v, pos);
			}
			dismiss();
		}

		public interface OnClickListener {
			void onClick(View v, int position);
		}
	}
}
