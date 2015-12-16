package android.support.v4.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * 
 * @author fanxing
 * 
 */
public class CustomViewPager extends ViewPager {
	private OnRightEndScrollListener rightEndScrollListener;
	private float pStart;
	private VelocityTracker mVelocityTracker;
	private int touchSlop;
	private float scrollFriction;
	private boolean mCanDrag = true;

	public interface OnRightEndScrollListener {
		void onRightEndScrollListener();
	}

	public CustomViewPager(Context context) {
		super(context);
		init();
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		touchSlop = 200;
		scrollFriction = 2000;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (!mCanDrag) return false;

		boolean flag = false;
		try {
			flag = super.onInterceptTouchEvent(arg0);
		}
		catch (Exception e) {

		}
		return flag;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			if (getAdapter() != null && (getCurrentItem() + 1) == getAdapter().getCount())// 最后一项
			{
				if (mVelocityTracker == null) {
					mVelocityTracker = VelocityTracker.obtain();
				}
				mVelocityTracker.addMovement(event);

				int action = event.getAction() & MotionEvent.ACTION_MASK;
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						pStart = event.getX();
						break;
					case MotionEvent.ACTION_MOVE:
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						float pEnd = event.getX();
						mVelocityTracker.computeCurrentVelocity(1000);
						float velocityX = mVelocityTracker.getXVelocity();

						if ((pStart > pEnd && (pStart - pEnd) > touchSlop) || (velocityX < -scrollFriction)) {// 触发左滑事件
							if (rightEndScrollListener != null) {
								rightEndScrollListener.onRightEndScrollListener();
							}
						}

						Log.v("fan", touchSlop + "velocityX=" + velocityX);
						Log.v("fan", scrollFriction + "(x1-x2)=" + (pStart - pEnd) + "," + pStart + "=" + "x2=" + pEnd);

						mVelocityTracker.clear();
						break;
					default:
						break;
				}
			}
		}
		catch (Exception e) {

		}
		return super.onTouchEvent(event);
	}

	public void setRightEndScrollListener(OnRightEndScrollListener rightEndScrollListener) {
		this.rightEndScrollListener = rightEndScrollListener;
	}

	/**
	 * 是否可以滑动
	 * 
	 * @param canDrag
	 */
	public void setCanDragState(boolean canDrag) {
		mCanDrag = canDrag;
	}
}
