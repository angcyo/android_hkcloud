package android.support.v4.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**自定义指示view*/
public class IndicatorView extends LinearLayout implements ViewPager.Decor {

	@SuppressLint("NewApi")
	public IndicatorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public IndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IndicatorView(Context context) {
		super(context);
	}

}
