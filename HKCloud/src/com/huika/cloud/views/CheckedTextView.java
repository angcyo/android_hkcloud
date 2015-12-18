package com.huika.cloud.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

/**
 * 实现类似checkbox的切换效果，需要完善背景的切换
 */
public class CheckedTextView extends TextView implements Checkable {
  private boolean mChecked;
  private OnCheckedChangeListener mOnCheckedChangeListener;
  private boolean mToggable;
  public CheckedTextView(Context context, AttributeSet attrs) {
    super(context, attrs, 0);
  }

  public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setChecked(mChecked);
  }

  @Override protected void drawableStateChanged() {
    super.drawableStateChanged();
    Drawable mButtonDrawable = getBackground();
    if (mButtonDrawable != null) {
      int[] myDrawableState =
          new int[] { mChecked ? android.R.attr.state_checked : android.R.attr.state_enabled };
      // Set the state of the Drawable
      mButtonDrawable.setState(myDrawableState);

      invalidate();
    }
  }

  @Override public boolean isChecked() {
    return mChecked;
  }

  @Override
  public void setChecked(boolean checked) {
    if (mChecked != checked) {
      mChecked = checked;
      refreshDrawableState();

      // Avoid infinite recursions if setChecked() is called from a listener
      if (mOnCheckedChangeListener != null) {
        mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
      }
    }
  }

  @Override public void toggle() {
    setChecked(!mChecked);
  }

  /**
   * 设置选中状态变化监听
   */
  public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
    mOnCheckedChangeListener = listener;
  }

  @Override public boolean performClick() {
    /*
     * XXX: These are tiny, need some surrounding 'expanded touch area',
		 * which will need to be implemented in Button if we only override
		 * performClick()
		 */

		/* When clicked, toggle the state */
    if (mToggable) toggle();
    return super.performClick();
  }

  public void setToggable(boolean toggable) {
    this.mToggable = toggable;
  }

  public interface OnCheckedChangeListener {
    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    void onCheckedChanged(Checkable checkableView, boolean isChecked);
  }
}
