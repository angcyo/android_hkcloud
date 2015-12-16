package com.huika.cloud.views;

import java.util.Hashtable;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 超过控件宽度以后，添加view不显示
 */
public class MyLinelayout extends LinearLayout {
	int mLeft, mRight, mTop, mBottom;
	Hashtable<View, Position> map = new Hashtable<View, Position>();
	private final static int DEFAULT_DIVIDORCOL = 8;

	/**
	 * 每个view上下的间距
	 */
	private final int dividerLine = 10;
	/**
	 * 每个view左右的间距
	 */
	private int dividerCol = DEFAULT_DIVIDORCOL;

	public MyLinelayout(Context context) {
		super(context);
	}

	public MyLinelayout(Context context, int horizontalSpacing,
			int verticalSpacing) {
		super(context);
	}

	public MyLinelayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int mWidth = MeasureSpec.getSize(widthMeasureSpec);
		int mCount = getChildCount();
		// int mX = 0;
		// int mY = 0;
		mLeft = 0;
		mRight = 0;
		mTop = 5;
		mBottom = 0;
		int j = 0;
		boolean isFirstRow = true;
		//遍历每一个view，判断view宽度是否超出
		for (int i = 0; i < mCount; i++) {
			final View child = getChildAt(i);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int childw = child.getMeasuredWidth();
			int childh = child.getMeasuredHeight();
			
			mRight += childw; 

			Position position = new Position();
			mLeft = getPosition(i - j, i);
			mRight = mLeft + child.getMeasuredWidth();
			if (mRight >= mWidth) {
				setMeasuredDimension(mWidth, mBottom + getPaddingBottom());
				return ;
			}
			mBottom = mTop + child.getMeasuredHeight();
			position.left = mLeft;
			position.top = mTop;
			position.right = mRight;
			position.bottom = mBottom;
			map.put(child, position);
		}
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(1, 1); // default of 1px spacing
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			Position pos = map.get(child);
			if (pos != null) {
				child.layout(pos.left, pos.top, pos.right, pos.bottom);
			} else {
				Log.i("MyLayout", "error");
			}
		}
	}

	private class Position {
		int left, top, right, bottom;
	}

	public int getPosition(int IndexInRow, int childIndex) {
		if (IndexInRow > 0) {
			return getPosition(IndexInRow - 1, childIndex - 1)
					+ getChildAt(childIndex - 1).getMeasuredWidth()
					+ dividerCol;
		}
		return getPaddingLeft();
	}
}
