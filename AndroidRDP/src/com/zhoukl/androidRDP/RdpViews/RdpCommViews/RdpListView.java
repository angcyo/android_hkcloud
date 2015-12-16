package com.zhoukl.androidRDP.RdpViews.RdpCommViews;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;

//ListView 
public class RdpListView extends PullToRefreshListView {
    public static final int BOTH = 1;
    public static final int PULL_FROM_START = 2;
    
    protected View mEmptyView;
    protected View mErrorView;
    protected View mSpecifyView;
    
    public RdpListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    // Constructor that is called when inflating a view from XML
    public RdpListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Perform inflation from XML and apply a class-specific base style
    public RdpListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public void onRefreshComplete1() {
        // TODO Auto-generated method stub
        
    }

    public void setMode(int both2) {
        // TODO Auto-generated method stub
        
    }
    
    public void setSpecifyView(View specifyView) {
        if (mSpecifyView != null) {
            ViewParent newSpecifyViewParent = mSpecifyView.getParent();
            if (null != newSpecifyViewParent && newSpecifyViewParent instanceof ViewGroup) {
                ((ViewGroup) newSpecifyViewParent).removeView(mSpecifyView);
            }
        }
        mSpecifyView = specifyView;
        setEmptyView(mSpecifyView);
    }
    
    public View getEmptyView() {
        return mEmptyView;
    }
    
    public void setRdpEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public View getErrorView() {
        return mErrorView;
    }

    public void setRdpErroView(View errorView) {
        mErrorView = errorView;
    }
}
