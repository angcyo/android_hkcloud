package com.zhoukl.androidRDP.RdpViews.RdpCommViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @description：一次性全部生成所有项目，用于解决ListView/ScrollView等的嵌套子ListView时，显示不完整的问题
 * @author zhoukl
 * @date 2015-5-29 下午5:51:04
 */
public class RdpInnerGridView extends GridView {

    public RdpInnerGridView(Context context) {
        super(context);
    }

    public RdpInnerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RdpInnerGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
