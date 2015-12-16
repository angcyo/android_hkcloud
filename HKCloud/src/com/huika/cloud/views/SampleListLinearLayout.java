package com.huika.cloud.views;

import java.util.Stack;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpUtils.RdpUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class SampleListLinearLayout extends LinearLayout {
  private static final Object DIVIDER_TAG = new Object();
  private BaseAdapter mBaseAdapter;
  private DataSetObserver mDataSetObserver;
  private int dividerSize = 0;
  private int dividerColor = 0xFFFFFFFF;
  private boolean showLastDivide;
  private boolean needDivider;
  private Stack<View> mCacheViewStack;

  public SampleListLinearLayout(Context context) {
    super(context);
    init();
  }

  public SampleListLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SampleListLinearLayout);

    dividerSize = a.getDimensionPixelSize(R.styleable.SampleListLinearLayout_dividerSize, 1);
    dividerColor = a.getColor(R.styleable.SampleListLinearLayout_dividerColor, 0xFFd1d1d1);
    needDivider = a.getBoolean(R.styleable.SampleListLinearLayout_needDivider, true);

    init();

    a.recycle();
  }

  public void setNeedDivider(boolean needDivider) {
    this.needDivider = needDivider;
  }

  private void init() {
    mCacheViewStack = new Stack<View>();
    mDataSetObserver = new DataSetObserver() {
      @Override public void onChanged() {
        super.onChanged();
        inflateView();
      }

      @Override public void onInvalidated() {
        super.onInvalidated();
        inflateView();
      }
    };
  }

  public void setAdapter(BaseAdapter baseAdapter) {
    if (mBaseAdapter != null) {
      mBaseAdapter.unregisterDataSetObserver(mDataSetObserver);
    }

    mBaseAdapter = baseAdapter;
    mBaseAdapter.registerDataSetObserver(mDataSetObserver);

    inflateView();
  }

  public void inflateView() {
    View cacheView;
    for (int i = 0; i < getChildCount(); i++) {
      cacheView = getChildAt(i);
      if (cacheView.getTag() != DIVIDER_TAG) {
        mCacheViewStack.push(cacheView);
      }
    }
    removeAllViews();

    int count = mBaseAdapter.getCount();
    for (int i = 0; i < mBaseAdapter.getCount(); i++) {
      View listItem =
          mBaseAdapter.getView(i, mCacheViewStack.isEmpty() ? null : mCacheViewStack.pop(), this);
      LayoutParams itemParams =
          new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      addView(listItem, itemParams);

      if (needDivider && dividerSize > 0 && dividerColor != 0) {
        if (!showLastDivide && (i == count - 1)) {// 最后一条分割线是否显示
          break;
        }

        LayoutParams params = null;
        if (getOrientation() == HORIZONTAL) {// 水平的
          params = new LayoutParams(dividerSize, LayoutParams.MATCH_PARENT);
        } else {
          params = new LayoutParams(LayoutParams.MATCH_PARENT, dividerSize);
          params.topMargin = RdpUtils.dip2px(getContext(), 10);
          params.bottomMargin = RdpUtils.dip2px(getContext(), 10);
        }
        if (i < mBaseAdapter.getCount() - 1) {
          View view = new View(getContext());
          view.setTag(DIVIDER_TAG);
          view.setBackgroundColor(dividerColor);
          addView(view, params);
        }
      }
    }

    postInvalidate();
  }

  public BaseAdapter getBaseAdapter() {
    return mBaseAdapter;
  }
}
