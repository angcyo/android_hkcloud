package com.huika.cloud.control.me.view;

import android.view.ViewGroup;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;

/**
 * Created by angcyo on 15-12-18 018 15:16 下午.
 */
public class RsenTestActivity extends RdpBaseActivity {
    private ViewGroup mRootLayout;

    @Override
    protected void initActivity() {
        super.initActivity();
        mRootLayout = (ViewGroup) addMasterView(R.layout.rsen_test_layout);
    }
}
