package android.support.v4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CustomIndicatorLinearLayout extends LinearLayout implements ViewPager.Decor {

	public CustomIndicatorLinearLayout(Context context) {
		super(context);
	}

	public CustomIndicatorLinearLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomIndicatorLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
