package cn.sharesdk.onekeyshare.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author shicm
 * @description：设置不能滑动的的GridView
 * @date 2014年12月17日 下午8:26:01
 */
public class NoScrollGridView extends GridView {

  public NoScrollGridView(Context context) {
    super(context);
    // TODO Auto-generated constructor stub
  }

  public NoScrollGridView(Context context, AttributeSet attrs) {
    super(context, attrs);
    // TODO Auto-generated constructor stub
  }

  public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    // TODO Auto-generated constructor stub
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }
}
