package com.zhoukl.androidRDP.RdpViews.RdpDBViews;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class RdpDBEditText extends TextView implements IRdpDBView {
	private RdpDBExtendView mDBExtendView;
	

	public RdpDBEditText(Context context) {
		super(context);
		mDBExtendView = new RdpDBExtendView(context);
	}

	public RdpDBEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		//TODO: ???
//		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZklDBView);
//		mDBExtendView.mDBFieldName = typedArray.getString(R.styleable.ZklDBView_dbFieldName);
		
	}

	public RdpDBEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZklDBView);
//		mDBExtendView.mDBFieldName = typedArray.getString(R.styleable.ZklDBView_dbFieldName);
		
		// TODO Auto-generated constructor stub
	}

    @Override
    public RdpDBExtendView getDBExtendView() {
        return mDBExtendView;
    }

    @Override
    public void refreshView(String value, RdpDBViewListener listener) {
        StringBuffer displayValue = new StringBuffer(value);
        if (listener != null) {
            if (listener.onRefreshDBView(this, displayValue.toString(), displayValue)) {
                return;
            }
        }
        setText(displayValue);
    }

}
