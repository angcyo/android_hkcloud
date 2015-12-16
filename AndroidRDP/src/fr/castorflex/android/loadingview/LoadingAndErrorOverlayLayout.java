package fr.castorflex.android.loadingview;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhoukl.androidRDP.R;

import fr.castorflex.android.loadingview.overlay.OverlayLayout;


public class LoadingAndErrorOverlayLayout extends OverlayLayout {
	private Activity context;
	private boolean isSetOnclick;
	private View loadErrorContainer;//错误页
	private LoadingView loadingContainer;//加载页
	
	public LoadingAndErrorOverlayLayout(Activity context,View targetView) {
		super(context);
		this.context=context;
		initOverlayLayout(targetView);
	}

	public LoadingAndErrorOverlayLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoadingAndErrorOverlayLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	protected View netErrorAndLoadingView;
	//---------错误覆盖页------
	/** 调此方法可以显示错误页面 
	 * targetView 设置overlay替换的view 
	 * reloadListener 不要用匿名内部类
	 * 点击事件监听的id=R.id.click_reload  点击重新加载*/
	public void showErrorLayView() {
		loadingContainer.dismiss();
		loadErrorContainer.setVisibility(View.VISIBLE);
		showOverlay();
	}
	
	
	/**隐藏覆盖页 加载和错误的覆盖都是调同一个方法*/
	public void hideOverLayView() {
		hideOverlay();
	}


	public void initOverlayLayout(View targetView) {
		netErrorAndLoadingView = setOverlayView(R.layout.common_load_error_and_loading_layout);
		attachTo(targetView);
		RelativeLayout rootRl = (RelativeLayout) netErrorAndLoadingView.findViewById(R.id.rootRl);
		loadErrorContainer = netErrorAndLoadingView.findViewById(R.id.load_error_parent);
		loadingContainer = new LoadingView(getContext());
		rootRl.addView(loadingContainer);
	}
	
	public void setReloadClick(View.OnClickListener reloadListener) {
		if(!isSetOnclick){
			isSetOnclick=true;
			netErrorAndLoadingView.findViewById(R.id.click_reload).setOnClickListener(reloadListener);
			netErrorAndLoadingView.findViewById(R.id.click_checknetwork).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = null;
					// 判断手机系统的版本 即API大于10 就是3.0或以上版本及魅族手机 
					if (android.os.Build.VERSION.SDK_INT > 10 && !android.os.Build.MANUFACTURER.equals("Meizu")) {
						intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					}
					else if(android.os.Build.VERSION.SDK_INT > 17 && android.os.Build.MANUFACTURER.equals("Meizu")){
						//魅族更高版本调转的方式与其它手机型号一致  可能之前的版本有些一样  所以另加条件(tsp)
						intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					}
					else {
						intent = new Intent();
						ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
						intent.setComponent(component);
						intent.setAction("android.intent.action.VIEW");
					}
					context.startActivity(intent);
				}
			});
		}
	}

	//------第一次加载覆盖页
	public void showLoadingOverLay() {
		loadErrorContainer.setVisibility(View.GONE);
		loadingContainer.show();
		showOverlay();
	}
    //重载可以添加文字
    public void showLoadingOverLay(String loadingTips) {
        loadErrorContainer.setVisibility(View.GONE);
        loadingContainer.setLoadingTips(loadingTips);
        loadingContainer.show();
        showOverlay();
    }
}
