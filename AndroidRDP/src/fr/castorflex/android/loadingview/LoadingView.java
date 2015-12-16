package fr.castorflex.android.loadingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhoukl.androidRDP.R;
import com.zhoukl.androidRDP.RdpUtils.UiHelper;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

public class LoadingView extends RelativeLayout {
	private Context mContext;
	private CircularProgressBar mCircularProgressBar;
	private LinearInterpolator mCurrentInterpolator;
    private TextView tv_loadingtips;
	private View mParentView;

	public LoadingView(Context context) {
		super(context);
		this.mContext = context;
		mParentView = LayoutInflater.from(mContext).inflate(R.layout.loading_view, this, true);
		initView();
	}
	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		mParentView = LayoutInflater.from(mContext).inflate(R.layout.loading_view, this, true);
		initView();
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.loading_view, this, true);
		initView();
	}
//	public LoadingView(Context context,RelativeLayout parentView) {
//		super(context);
//		this.mContext = context;
//		LayoutInflater inflater =  LayoutInflater.from(mContext);
//		mParentView = inflater.inflate(R.layout.loading_view, null);
//		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		params.addRule(RelativeLayout.CENTER_VERTICAL);
//		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		parentView.addView(mParentView, params);
//		initView();
//	}

	private void initView() {
		mCircularProgressBar = (CircularProgressBar) mParentView.findViewById(R.id.progressbar_circular);
        tv_loadingtips = (TextView) mParentView.findViewById(R.id.tv_loadingtips);
		updateValues();
	}
	

	private void updateValues() {
		mCurrentInterpolator = new LinearInterpolator();
		CircularProgressDrawable circularProgressDrawable;
		CircularProgressDrawable.Builder builder = new CircularProgressDrawable.Builder(mContext).colors(mContext.getResources().getIntArray(R.array.gplus_colors)).sweepSpeed(1.4f).rotationSpeed(1.2f).strokeWidth(UiHelper.dpToPx(4, mContext)).style(CircularProgressDrawable.Style.ROUNDED);
		mCircularProgressBar.setIndeterminateDrawable(circularProgressDrawable = builder.build());
		if (mCurrentInterpolator != null) {
			builder.sweepInterpolator(mCurrentInterpolator);
		}
		// /!\ Terrible hack, do not do this at home!
		circularProgressDrawable.setBounds(0, 0, mCircularProgressBar.getWidth(), mCircularProgressBar.getHeight());
//		mCircularProgressBar.setVisibility(View.INVISIBLE);
		mCircularProgressBar.setVisibility(View.VISIBLE);
		show();
	}
	public void show() {
		setVisibility(View.VISIBLE);
		((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).start();
	}

	public void dismiss() {
		((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).progressiveStop();
		setVisibility(View.GONE);
	}

	//设置文字提示
    public void setLoadingTips(String loadingTips){
        tv_loadingtips.setText(loadingTips);
    }
}
