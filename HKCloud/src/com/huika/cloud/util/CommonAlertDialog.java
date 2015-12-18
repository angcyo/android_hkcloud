package com.huika.cloud.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.util.help.BaseEffects;
import com.huika.cloud.util.help.Effectstype;

/**
 * 通用弹出对话框
 * 改动说明 ：6月1日同产品经理确认：统一右边为确定（操作）按钮，左边为取消按钮
 *
 * @author ldm
 * @description：
 * @date 2015-5-21 下午1:56:50
 */
public class CommonAlertDialog extends Dialog {
	/** 默认标题文字颜色 */
	private final String defTextColor = "#333333";
	/**默认分隔线颜色*/
	// private final String defDividerColor = "#d0d0d0";
	/** 默认内容文字颜色 */
	private final String defMsgColor = "#3333333";
	/** 默认对话框背景色 */
	private final String defDialogColor = "#e3e3e3";
	/** 动画类型 */
	private Effectstype type = Effectstype.SlideBottom;
	private LinearLayout mLinearLayoutView;
	private LinearLayout mRelativeLayoutView;
	private LinearLayout mContentLayout;
	private LinearLayout mTitleLayout;
	private LinearLayout mBottomLayoutView;
	private FrameLayout mFrameLayoutCustomView;
	private View mDialogView;
	// private View mDivider;
	/** 对话框标题TextView */
	private TextView mTitleTv;
	/** 对话框提示内容TextView */
	private TextView mContentTv;
	/** 输入对话框输入框EditText */
	private EditText mContentEdt;
	/**标题左边带图片*/
	// private ImageView mIcon;
	/** 对话框左边按钮（默认取消） */
	private Button mCancelBtn;
	/** 对话框右边按钮（默认是操作功能） */
	private Button mSureBtn;
	private View lineView;

	/** 动画时间 */
	private int mDuration = 350;

	public CommonAlertDialog(Context context) {
		super(context);
		init(context);
	}

	public CommonAlertDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	/**
	 * 这里不能使用单例，已经改过来，待改方法名称
	 *
	 * @description：
	 * @author ldm
	 * @date 2015-6-10 下午5:36:13
	 */
	public static CommonAlertDialog getInstance(Context context) {
		return new CommonAlertDialog(context, R.style.common_dialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(params);
	}

	private void init(Context context) {
		mDialogView = View.inflate(context, R.layout.common_dialog_layout, null);
		mLinearLayoutView = (LinearLayout) mDialogView.findViewById(R.id.parentPanel);
		mRelativeLayoutView = (LinearLayout) mDialogView.findViewById(R.id.dialog_main_rela);
		mTitleLayout = (LinearLayout) mDialogView.findViewById(R.id.title_layout);
		mContentLayout = (LinearLayout) mDialogView.findViewById(R.id.content_layout);
		mBottomLayoutView = (LinearLayout) mDialogView.findViewById(R.id.dialog_bottom_lin);
		mFrameLayoutCustomView = (FrameLayout) mDialogView.findViewById(R.id.dialog_custom_fl);
		mTitleTv = (TextView) mDialogView.findViewById(R.id.dialog_title_tv);
		mContentTv = (TextView) mDialogView.findViewById(R.id.dialog_content_tv);
		mContentEdt = (EditText) mDialogView.findViewById(R.id.dialog_content_edt);
		lineView = mDialogView.findViewById(R.id.common_btn_line);
		// mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
		// mDivider = mDialogView.findViewById(R.id.titleDivider);
		mCancelBtn = (Button) mDialogView.findViewById(R.id.cancel_btn);
		mSureBtn = (Button) mDialogView.findViewById(R.id.submit_btn);
		setContentView(mDialogView);

		this.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {

				mLinearLayoutView.setVisibility(View.VISIBLE);
				if (type == null) {
					type = Effectstype.Slidetop;
				}
				start(type);
			}
		});
		mCancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	/** 设置对话框对应文字 */
	public void toDefault() {
		mTitleTv.setTextColor(Color.parseColor(defTextColor));
		// mDivider.setBackgroundColor(Color.parseColor(defDividerColor));
		mContentTv.setTextColor(Color.parseColor(defMsgColor));
		mLinearLayoutView.setBackgroundColor(Color.parseColor(defDialogColor));
	}

	/**设置分隔线颜色*/
	// public CommonAlertDialog withDividerColor(String colorString) {
	// mDivider.setBackgroundColor(Color.parseColor(colorString));
	// return this;
	// }

	/** 设置对话框标题 */
	public CommonAlertDialog withTitle(CharSequence title) {
		mCancelBtn.setVisibility(View.VISIBLE);
		lineView.setVisibility(View.VISIBLE);
		if (TextUtils.isEmpty(title)) {
			mTitleLayout.setVisibility(View.GONE);
		} else {
			mTitleLayout.setVisibility(View.VISIBLE);
			// toggleView(mTitleLayout, title);
			mTitleTv.setText(title);
		}
		return this;
	}

	/** 设置对话框标题文字大小 */
	public CommonAlertDialog withTitleTextSize(int size) {
		mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		return this;
	}

	/** 设置对话框标题颜色 */
	public CommonAlertDialog withTitleColor(String colorString) {
		mTitleTv.setTextColor(Color.parseColor(colorString));
		return this;
	}

	/** 设置对话框标题颜色 */
	public CommonAlertDialog withTitleColor(int color) {
		mTitleTv.setTextColor(color);
		return this;
	}

	/** 设置对话框标题上下间距 */
	public CommonAlertDialog withTitleMargin(int left, int top, int right, int bottom) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(left, top, right, bottom);// 设置边距
		mTitleTv.setLayoutParams(params);
		return this;
	}

	/** 设置对话框内容上下间距 */
	public CommonAlertDialog withContentMargin(int left, int top, int right, int bottom) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(left, top, right, bottom);// 设置边距
		mContentTv.setLayoutParams(params);
		return this;
	}

	/** 设置对话框中输入框下间距 */
	public CommonAlertDialog withInputMargin(int left, int top, int right, int bottom) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(left, top, right, bottom);// 设置边距
		mContentTv.setLayoutParams(params);
		return this;
	}

	/** 设置对话框显示内容 */
	public CommonAlertDialog withMessage(CharSequence msg) {
		return withMessage(msg, false);
	}

	/** 设置对话框显示内容 */
	public CommonAlertDialog withMessage(CharSequence msg, boolean alignCenter) {
		mCancelBtn.setVisibility(View.VISIBLE);
		lineView.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(msg)) {
			mContentTv.setVisibility(View.VISIBLE);
			mContentEdt.setVisibility(View.GONE);
			if (alignCenter) {
				mContentTv.setGravity(Gravity.CENTER);
			}
			mContentTv.setText(msg);
		} else {
			mContentTv.setVisibility(View.GONE);
		}
		return this;
	}

	/** 设置对话框内容文字大小 */
	public CommonAlertDialog withMessageTextSize(int size) {
		mContentTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		return this;
	}

	/**
	 * 设置对话框为可以输入的对话框
	 * 分别传入：提示文字 ，限制的长度，以及输入文字类型，如果没有限制，则字符传""，数字传0或负数
	 *
	 * @description：
	 * @author ldm
	 * @date 2015-5-21 下午1:40:49
	 */
	public CommonAlertDialog withInputContent(CharSequence hintMsg, int maxLength, int inputType) {
		mContentTv.setVisibility(View.GONE);
		mContentEdt.setVisibility(View.VISIBLE);
		if (maxLength > 0) {
			mContentEdt.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
		}
		if (inputType > 0) {
			mContentEdt.setInputType(inputType);
		}
		mContentEdt.setHint(hintMsg);
		return this;
	}

	/**
	 * 设置输入文字颜色
	 *
	 * @description：
	 * @author ldm
	 * @date 2015-5-21 下午1:40:49
	 */
	public CommonAlertDialog withInputContentColor(String colorStr) {
		mContentEdt.setTextColor(Color.parseColor(colorStr));
		return this;
	}

	/** 如果是输入提示框，获取输入的内容 */
	public String getInputStr() {
		return mContentEdt.getText().toString().trim();
	}

	/** 设置对话框内容颜色 */
	public CommonAlertDialog withMessageColor(String colorString) {
		mContentTv.setTextColor(Color.parseColor(colorString));
		return this;
	}

	/** 设置对话框内容颜色 */
	public CommonAlertDialog withMessageColor(int color) {
		mContentTv.setTextColor(color);
		return this;
	}

	/** 设置动画时间 */
	public CommonAlertDialog withDuration(int duration) {
		this.mDuration = duration;
		return this;
	}

	/** 设置动画类型 */
	public CommonAlertDialog withEffect(Effectstype type) {
		this.type = type;
		return this;
	}

	/** 设置按钮背景 */
	public CommonAlertDialog withButtonDrawable(int resid) {
		mCancelBtn.setBackgroundResource(resid);
		mSureBtn.setBackgroundResource(resid);
		return this;
	}

	/** 设置左侧按钮文字 */
	public CommonAlertDialog withLeftButtonText(CharSequence text) {
		if (TextUtils.isEmpty(text)) {
			mBottomLayoutView.setVisibility(View.GONE);
		} else {
			mBottomLayoutView.setVisibility(View.VISIBLE);
			mCancelBtn.setText(text);
		}
		return this;
	}

	/** 设置左侧按钮文字颜色 */
	public CommonAlertDialog withLeftButtonTextColor(String color) {
		mCancelBtn.setTextColor(Color.parseColor(color));
		return this;
	}

	/** 设置右侧按钮文字 */
	public CommonAlertDialog withRightButtonTextColor(String color) {
		mSureBtn.setTextColor(Color.parseColor(color));
		return this;
	}

	/** 设置右侧按钮文字颜色 */
	public CommonAlertDialog withRightButtonText(CharSequence text) {
		if (TextUtils.isEmpty(text)) {
			mBottomLayoutView.setVisibility(View.GONE);
		} else {
			mBottomLayoutView.setVisibility(View.VISIBLE);
			mSureBtn.setText(text);
		}
		return this;
	}

	/** 设置只显示单个按钮的Dialog */
	public CommonAlertDialog withSingleButton() {
		mCancelBtn.setVisibility(View.GONE);
		lineView.setVisibility(View.GONE);
		return this;
	}

	/** 设置左侧按钮点击操作 */
	public CommonAlertDialog setRightButtonClick(View.OnClickListener click) {
		mBottomLayoutView.setVisibility(View.VISIBLE);
		mSureBtn.setOnClickListener(click);
		return this;
	}

	/** 设置右侧按钮点击操作 */
	public CommonAlertDialog setLeftButtonClick(View.OnClickListener click) {
		mBottomLayoutView.setVisibility(View.VISIBLE);
		mCancelBtn.setOnClickListener(click);
		return this;
	}

	public void removeCustomView() {
		mFrameLayoutCustomView.setVisibility(View.GONE);
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
	}

	/** 设置对话框自内容View */
	public CommonAlertDialog setCustomView(int resId, Context context) {
		View customView = View.inflate(context, resId, null);
		mFrameLayoutCustomView.setVisibility(View.VISIBLE);
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mFrameLayoutCustomView.addView(customView);
		return this;
	}

	/** 设置对话框自内容View */
	public CommonAlertDialog setCustomView(View view, Context context) {
		mFrameLayoutCustomView.setVisibility(View.VISIBLE);
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mFrameLayoutCustomView.addView(view);

		return this;
	}

	/** 设置对话框setCanceledOnTouchOutside */
	public CommonAlertDialog isCancelableOnTouchOutside(boolean cancelable) {
		this.setCanceledOnTouchOutside(cancelable);
		return this;
	}

	/** 设置对话框setCancelable */
	public CommonAlertDialog isCancelable(boolean cancelable) {
		this.setCancelable(cancelable);
		return this;
	}

	/** 执行动画 */
	private void start(Effectstype type) {
		BaseEffects animator = type.getAnimator();
		if (mDuration != -1) {
			animator.setDuration(Math.abs(mDuration));
		}
		animator.start(mRelativeLayoutView);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}
}
