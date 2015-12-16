package com.zhoukl.androidRDP.RdpFramework.RdpFragment;

import com.umeng.analytics.MobclickAgent;
import com.zhoukl.androidRDP.R;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandFailedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpCommand.OnCommandSuccessedListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.ISkipActivity;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpApplication;
import com.zhoukl.androidRDP.RdpFramework.RdpApp.RdpConstant;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

import fr.castorflex.android.loadingview.LoadingAndErrorOverlayLayout;
import fr.castorflex.android.loadingview.LoadingRainBowDialog;
import fr.castorflex.android.loadingview.overlay.OverlayLayout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RdpBaseFragment extends RdpFragment implements OnClickListener, OnCommandSuccessedListener,
   OnCommandFailedListener,ISkipActivity, OnCancelListener{
    protected final int TBAR_FUNC_BACK = 1;
    
    protected LayoutInflater mLayoutInflater;
    protected RdpApplication mApplication;
    protected RdpBaseActivity mActivity;
    protected View mRootView;
    
    protected TextView mTvTitle;
    protected RelativeLayout mTitleBar;
    protected LinearLayout mLltTitleBarLeftArea, mLltTitleBarRigthArea;

    protected LinearLayout mLltHeaderArea;
    protected LinearLayout mLltMasterArea;
    protected LinearLayout mLltFooterArea;
    
    protected LoadingRainBowDialog loadingDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (RdpBaseActivity) activity;
        mApplication = (RdpApplication) mActivity.getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //notifiyApplicationActivityCreating();
        onBeforeCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        //getTAApplication().getAppManager().addActivity(this);
        //onBeforeInitFragment();
        //initActivity();
        onAfterOnCreate(savedInstanceState);
        //notifiyApplicationActivityCreated();

        IntentFilter filter = new IntentFilter(RdpConstant.BROADCAST_LOGIN);
        mActivity.registerReceiver(mLoginReceiver1, filter);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mRootView = inflater.inflate(getBaseLayoutID(), container, false);
        onBeforeInitFragment();
        initFragment();
        onAfterInitFragment();
        return mRootView;
    }
    @Override
    public void onResume() {
    	super.onResume();
    	MobclickAgent.onPageEnd(this.getClass().getName()); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
    };
    
    
    @Override
    public void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	MobclickAgent.onPageEnd(this.getClass().getName());
    }
    
    protected void onBeforeCreate(Bundle savedInstanceState) {
        //getTAApplication().registerCommand(TAIDENTITYCOMMAND,TAIdentityCommand.class);
        
    }
    
    protected void onAfterOnCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
    }
    
    protected int getBaseLayoutID() {
        return R.layout.rdp_base_activity;
    }

    protected View findViewById(int id) {
        return mRootView.findViewById(id);
    }
    
    protected void onBeforeInitFragment() {
        mTitleBar = (RelativeLayout) findViewById(R.id.rltTitleBar);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mLltTitleBarLeftArea = (LinearLayout) findViewById(R.id.lltTitleBarLeftArea);
        mLltTitleBarRigthArea = (LinearLayout) findViewById(R.id.lltTitleBarRigthArea);
        addLeftFuncView(R.drawable.common_head_back_select, this, TBAR_FUNC_BACK);
        
        mLltHeaderArea = (LinearLayout) findViewById(R.id.lltHeaderArea);
        mLltMasterArea = (LinearLayout) findViewById(R.id.lltMasterArea);
        mLltFooterArea = (LinearLayout) findViewById(R.id.lltFooterArea);

    }
    
    protected void initFragment() {
        
    }
    
    protected void onAfterInitFragment() {
        
    }
    
    protected boolean isLogin() {
        return isLogin(true);
    }
    
    public boolean isLogin(boolean jumpToLoginActivity) {
        return mApplication.isLogin(jumpToLoginActivity);
    }
    
    protected void setFuncTitle(int titleResID) {
        setFuncTitle(getString(titleResID));
    }

    protected void setFuncTitle(String title) {
        mTvTitle.setText(title);
    }


    protected View addHeaderView(int resLayoutID) {
        return mLayoutInflater.inflate(resLayoutID, mLltHeaderArea);
    }
    
    protected View addMasterView(int resLayoutID) {
        return mLayoutInflater.inflate(resLayoutID, mLltMasterArea);
    }
    
    protected View addFooterView(int resLayoutID) {
        return mLayoutInflater.inflate(resLayoutID, mLltFooterArea);
    }
    
    protected ImageButton addLeftFuncView(int drawableResID, OnClickListener listener, int funcViewTag) {
        return addFuncView(drawableResID, listener, funcViewTag, mLltTitleBarLeftArea);
    }

    protected void removeLeftFuncView(int funcViewTag) {
        removeFuncView(funcViewTag, mLltTitleBarLeftArea);
    }

    protected ImageButton addRightFuncView(int drawableResID, OnClickListener listener, int funcViewTag) {
        return addFuncView(drawableResID, listener, funcViewTag, mLltTitleBarRigthArea);
    }
    
    protected TextView addRightFuncTextView(String text, OnClickListener listener, int funcViewTag) {
        return addFuncTextView(text, listener, funcViewTag, mLltTitleBarRigthArea);
    }

    protected void removeRightFuncView(int funcViewTag) {
        removeFuncView(funcViewTag, mLltTitleBarRigthArea);
    }

    private ImageButton addFuncView(int drawableResID, OnClickListener listener, int funcViewTag, LinearLayout parent) {
        ImageButton button = new ImageButton(mActivity.getApplicationContext());
        button.setTag(funcViewTag);
        if (drawableResID != 0)
            button.setImageResource(drawableResID);
        button.setBackgroundResource(R.drawable.common_head_btn_selector);
        parent.addView(button);
        if (listener != null)
            button.setOnClickListener(listener);
        return button;
    }

    private TextView addFuncTextView(String text, OnClickListener listener, int funcViewTag, LinearLayout parent) {
        TextView textView = new TextView(mActivity.getApplicationContext());
        textView.setTag(funcViewTag);
        textView.setText(text);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT); 
        //params.setMargins(20, 0, 40, 0);
        
        textView.setLayoutParams(params);
        textView.setPadding(20, 0, 40, 0);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        //if (drawableResID != 0)
        //    textView.setBackgroundResource(drawableResID);
        //textView.setBackgroundResource(R.drawable.common_head_btn_selector);
        parent.addView(textView);
        if (listener != null)
            textView.setOnClickListener(listener);
        return textView;
    }

    private void removeFuncView(int funcViewTag, LinearLayout parent) {
        View view = parent.findViewWithTag(funcViewTag);
        if (view != null)
            parent.removeView(view);
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null) {
            switch ((Integer) tag) {
            case TBAR_FUNC_BACK:
                mActivity.finish();
                return;

            default:
                break;
            }
        }
    }

    public void onLoginStatusChanged(boolean loginStatus) {
        
    }
    
    @Override
    public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onCommandFailed(Object reqKey, RdpResponseResult result) {
        mActivity.onCommandFailed(reqKey, result);        
    }

    @Override
    public void onDestroy() {
        mActivity.unregisterReceiver(mLoginReceiver1);
        super.onDestroy();
    }

    private BroadcastReceiver mLoginReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onLoginStatusChanged(isLogin(false));
        }
    };
    
    
    /**
	 * 调此方法可以显示错误页面 targetView 设置overlay替换的view reloadListener 不要用匿名内部类
	 * 点击事件监听的id=R.id.click_reload 点击重新加载
	 */
	private LoadingAndErrorOverlayLayout overLay;
	private OverlayLayout mEmtyOverlay;
	private View commonEmtyOverlay;

	// ------第一次加载覆盖页
	protected void showLoadingOverLay(View targetView) {
		if (overLay == null)
			overLay = new LoadingAndErrorOverlayLayout(getActivity(), targetView);
		overLay.showLoadingOverLay();
	}

	/** 显示的加载dialog页面，等在获取信息的提示dialog **/
	protected void showErrorLayView(View targetView, View.OnClickListener reloadListener) {
		if (overLay == null)
			overLay = new LoadingAndErrorOverlayLayout(getActivity(), targetView);
		overLay.setReloadClick(reloadListener);
		overLay.showErrorLayView();
	}

	/** 隐藏覆盖页 加载和错误的覆盖都是调同一个方法 */
	protected void hideOverLayView() {
		if (overLay != null)
			overLay.hideOverlay();
	}

	/** 显示数据空页面 : targetView为设置overlay依附的view,resid为提示信息资源id */
	public void showEmptyLayView(View targetView, int resid) {
		if (mEmtyOverlay == null) {
			mEmtyOverlay = new OverlayLayout(getActivity());
			mEmtyOverlay.attachTo(targetView);
			commonEmtyOverlay = mLayoutInflater.inflate(R.layout.layout_emty_overlay, null);
			TextView empt_tv = (TextView) commonEmtyOverlay.findViewById(R.id.empt_tv);
			empt_tv.setText(resid);
			mEmtyOverlay.setOverlayView(commonEmtyOverlay);
		}
		mEmtyOverlay.showOverlay();
	}

	/** 影藏数据空页面 */
	public void hideEmptyLayView() {
		if (mEmtyOverlay != null)
			mEmtyOverlay.hideOverlay();
	}

	@Override
	public void skipActivity(Activity aty, Class<?> cls) {
		showActivity(aty, cls);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Intent it) {
		showActivity(aty, it);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
		showActivity(aty, cls, extras);
		aty.finish();
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Intent it) {
		aty.startActivity(it);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
		Intent intent = new Intent();
		intent.putExtras(extras);
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}
	
	/****************************弹出框start*******************************/
	public void showToastMsg(String msg) {
		ToastMsg.showToastMsg(getActivity(), msg);
	}

	public void showToastMsg(int strId) {
		ToastMsg.showToastMsg(getActivity(), strId);
	}
	/****************************弹出框end*******************************/

	/**************************等待框*************************/
	protected void initLoadingDialog(boolean isTrans) {
		if (null == loadingDialog) {
			loadingDialog = new LoadingRainBowDialog(getActivity(), isTrans);
			// loadingDialog = new LoadingDialog(this, isTrans);
			loadingDialog.setOnCancelListener(this);
		}
	}

	public void showLoadingDialog(String parameter) {
		initLoadingDialog(false);// 透明
		loadingDialog.setTitle(parameter);
		if (!loadingDialog.isShowing())
			loadingDialog.show();
	}

	public void showLoadingDialog(boolean isTrans) {
		initLoadingDialog(isTrans);// 透明
		// loadingDialog.setTitle(parameter);
		if (!loadingDialog.isShowing())
			loadingDialog.show();
	}

	public void dismissLoadingDialog() {
		if (null != loadingDialog) {
			LoadingRainBowDialog.dismissDialog(loadingDialog);
			// LoadingDialog.dismissDialog(loadingDialog);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}
	/**************************等待框*************************/
}
