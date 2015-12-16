package com.huika.cloud.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
  public interface OnScrollListener {
    void onScrollChanged(int x, int y);
  }

  private OnScrollListener listener = null;

  public MyScrollView(Context context) {
    super(context);
  }

  public MyScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setOnScrollListener(OnScrollListener listener) {
    this.listener = listener;
  }

  @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);
    if (listener != null) {
      listener.onScrollChanged(getScrollX(), getScrollY());
    }
  }
}
