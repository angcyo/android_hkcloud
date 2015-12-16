package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.zhoukl.androidRDP.R;

/**
 * @author lihy
 * @description：自定义的一个刷新头部
 * @date 2014年10月14日 上午10:01:25
 */
@SuppressLint("ViewConstructor")
public class CustomLoadingLayout extends LoadingLayout {

	static final int FLIP_ANIMATION_DURATION = 150;

	private final Animation mRotateAnimation, mResetRotateAnimation;

	public CustomLoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

		final int rotateAngle = mode == Mode.PULL_FROM_START ? -180 : 180;

		mRotateAnimation = new RotateAnimation(0, rotateAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
		mRotateAnimation.setFillAfter(true);

		mResetRotateAnimation = new RotateAnimation(rotateAngle, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mResetRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
		mResetRotateAnimation.setFillAfter(true);

		View v = findViewById(R.id.fl_img_parent);
		ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
		layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		v.setLayoutParams(layoutParams);

		mHeaderProgress.setIndeterminate(true);
		ViewGroup.LayoutParams playoutParams = mHeaderProgress.getLayoutParams();
		playoutParams.width = dip2px(getContext(), 30);
		playoutParams.height = dip2px(getContext(), 30);
		mHeaderProgress.setLayoutParams(playoutParams);
		mHeaderProgress.setIndeterminateDrawable(getResources().getDrawable(R.drawable.head_loading_backgroud));
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {
		if (null != imageDrawable) {
			final int dHeight = imageDrawable.getIntrinsicHeight();
			final int dWidth = imageDrawable.getIntrinsicWidth();

			/**
			 * We need to set the width/height of the ImageView so that it is
			 * square with each side the size of the largest drawable dimension.
			 * This is so that it doesn't clip when rotated.
			 */
			ViewGroup.LayoutParams lp = mHeaderImage.getLayoutParams();
			lp.width = lp.height = Math.max(dHeight, dWidth);
			mHeaderImage.requestLayout();

			/**
			 * We now rotate the Drawable so that is at the correct rotation,
			 * and is centered.
			 */
			mHeaderImage.setScaleType(ScaleType.MATRIX);
			Matrix matrix = new Matrix();
			matrix.postTranslate((lp.width - dWidth) / 2f, (lp.height - dHeight) / 2f);
			matrix.postRotate(getDrawableRotationAngle(), lp.width / 2f, lp.height / 2f);
			mHeaderImage.setImageMatrix(matrix);
		}
	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
		// NO-OP
	}

	@Override
	protected void pullToRefreshImpl() {
		hideAllViews();
		mHeaderImage.setVisibility(View.VISIBLE);// lihy 只显示图片
		// Only start reset Animation, we've previously show the rotate anim
		if (mRotateAnimation == mHeaderImage.getAnimation()) {
			mHeaderImage.startAnimation(mResetRotateAnimation);
		}
	}

	@Override
	protected void refreshingImpl() {
		hideAllViews();
		mHeaderImage.clearAnimation();
		mHeaderImage.setVisibility(View.INVISIBLE);
		mHeaderProgress.setVisibility(View.VISIBLE);
	}

	@Override
	protected void releaseToRefreshImpl() {
		hideAllViews();
		mHeaderImage.setVisibility(View.VISIBLE);// lihy 只显示图片
		mHeaderImage.startAnimation(mRotateAnimation);
	}

	@Override
	protected void resetImpl() {
		hideAllViews();
		mHeaderImage.clearAnimation();
		mHeaderProgress.setVisibility(View.GONE);
		mHeaderImage.setVisibility(View.VISIBLE);
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.default_ptr_flip;
	}

	private float getDrawableRotationAngle() {
		float angle = 0f;
		switch (mMode) {
			case PULL_FROM_END:
				if (mScrollDirection == Orientation.HORIZONTAL) {
					angle = 90f;
				} else {
					angle = 180f;
				}
				break;

			case PULL_FROM_START:
				if (mScrollDirection == Orientation.HORIZONTAL) {
					angle = 270f;
				}
				break;

			default:
				break;
		}

		return angle;
	}

	/********************************设置子标题*/
	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		super.setLastUpdatedLabel(null);
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
