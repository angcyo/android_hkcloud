package com.handmark.pulltorefresh.library.pinnedheader;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;


public class PinnedHeaderPullRefreshListView extends PullToRefreshListView {

	public PinnedHeaderPullRefreshListView(Context context) {
		super(context);
	}

	public PinnedHeaderPullRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PinnedHeaderPullRefreshListView(Context context, Mode mode) {
		super(context, mode);
	}

	public PinnedHeaderPullRefreshListView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	protected ListView createListView(Context context, AttributeSet attrs) {
		return new PinnedHeaderListView(context, attrs);
	}

	public void setPinnedHeaderView(View view) {
		if (view != null) ((PinnedHeaderListView) getRefreshableView()).setPinnedHeaderView(view);
	}

	public class PinnedHeaderListView extends InternalListViewSDK9 implements PinnedHeaderListViewConfig {
		private static final int MAX_ALPHA = 255;

		private PinnedHeaderStateListener mAdapter;
		private View mHeaderView;
		private boolean mHeaderViewVisible;

		private int mHeaderViewWidth;

		private int mHeaderViewHeight;

		public PinnedHeaderListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public void setPinnedHeaderView(View view) {
			mHeaderView = view;

			// Disable vertical fading when the pinned header is present
			// TODO change ListView to allow separate measures for top and bottom fading edge;
			// in this particular case we would like to disable the top, but not the bottom edge.
			if (mHeaderView != null) {
				setFadingEdgeLength(0);
			}
			requestLayout();
		}

		@Override
		public void setAdapter(ListAdapter adapter) {
			super.setAdapter(adapter);
			if (adapter instanceof PinnedHeaderStateListener) {
				mAdapter = (PinnedHeaderStateListener) adapter;
				setOnScrollListener(mAdapter);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			if (mHeaderView != null) {
				measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
				mHeaderViewWidth = mHeaderView.getMeasuredWidth();
				mHeaderViewHeight = mHeaderView.getMeasuredHeight();
			}
		}

		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
			super.onLayout(changed, left, top, right, bottom);
			if (mHeaderView != null) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
				configureHeaderView(getFirstVisiblePosition() - getHeaderViewsCount());
			}
		}

		@Override
		public void configureHeaderView(int position) {
			if (mHeaderView == null) { return; }

			int state = mAdapter.getPinnedHeaderState(position);
			switch (state) {
				case PinnedHeaderStateListener.PINNED_HEADER_GONE: {
					mHeaderViewVisible = false;
					break;
				}

				case PinnedHeaderStateListener.PINNED_HEADER_VISIBLE: {
					mAdapter.configurePinnedHeader(mHeaderView, position, MAX_ALPHA);
					if (mHeaderView.getTop() != 0) {
						mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
					}
					mHeaderViewVisible = true;
					break;
				}

				case PinnedHeaderStateListener.PINNED_HEADER_PUSHED_UP: {
					View firstView = getChildAt(0);
					int bottom = firstView.getBottom();
					int itemHeight = firstView.getHeight();
					int headerHeight = mHeaderView.getHeight();
					int y;
					int alpha;
					if (bottom < headerHeight) {
						y = (bottom - headerHeight);
						alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
					} else {
						y = 0;
						alpha = MAX_ALPHA;
					}
					mAdapter.configurePinnedHeader(mHeaderView, position, alpha);
					if (mHeaderView.getTop() != y) {
						mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
					}
					mHeaderViewVisible = true;
					break;
				}
			}
		}

		@Override
		protected void dispatchDraw(Canvas canvas) {
			super.dispatchDraw(canvas);
			if (mHeaderViewVisible) {
				drawChild(canvas, mHeaderView, getDrawingTime());
			}
		}
	}
}
