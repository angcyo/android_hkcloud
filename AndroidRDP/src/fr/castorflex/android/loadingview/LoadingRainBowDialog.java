package fr.castorflex.android.loadingview;

import com.zhoukl.androidRDP.R;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

public class LoadingRainBowDialog extends Dialog {
	private CircularProgressBar mCircularProgressBar;
	private Context mContext;
	private LinearInterpolator mCurrentInterpolator;


	public LoadingRainBowDialog(Context context) {
		super(context, R.style.Dialog_TransRainbow);
		this.mContext = context;
		init(true);
	}
	/**
	 * @param context
	 * @param isTrans  true 不带边框, false 带边框
	 */
	public LoadingRainBowDialog(Context context, boolean isTrans) {
		super(context, R.style.Dialog_TransRainbow);
		this.mContext = context;
		init(isTrans);
	}

	/**
	 * 
	 * @description： 初始化Dialog
	 * @author jxh
	 * @date 2015-6-3 下午4:11:39
	 */
	private void init(boolean isTrans) {
		if (isTrans) {
			setContentView(R.layout.rainbow_trans_dialog);
		}else{
			setContentView(R.layout.rainbow_dialog);
		}
		mCircularProgressBar = (CircularProgressBar)findViewById(R.id.progressbar_circular);
		updateValues();
	}

	@Override
	public void show() {
		((CircularProgressDrawable)mCircularProgressBar.getIndeterminateDrawable()).start();
		super.show();
	}

	@Override
	public void dismiss() {
		((CircularProgressDrawable)mCircularProgressBar.getIndeterminateDrawable()).progressiveStop();
		super.dismiss();
	}

	@Override
	public void setTitle(CharSequence title) {
	}

	@Override
	public void setTitle(int titleId) {
		setTitle(getContext().getString(titleId));
	}

	public static void dismissDialog(LoadingRainBowDialog loadingDialog) {
		if (null == loadingDialog) { return; }
		loadingDialog.dismiss();
	}
	
	private void updateValues() {
		mCurrentInterpolator = new LinearInterpolator();
		CircularProgressDrawable circularProgressDrawable;
		CircularProgressDrawable.Builder builder = new CircularProgressDrawable.Builder(mContext)
				.colors(mContext.getResources().getIntArray(R.array.gplus_colors)).sweepSpeed(1.4f)
				.rotationSpeed(1.2f)
				.strokeWidth(dpToPx(4))
				.style(CircularProgressDrawable.Style.ROUNDED);
		mCircularProgressBar.setIndeterminateDrawable(circularProgressDrawable = builder.build());
        if (mCurrentInterpolator != null) {
            builder.sweepInterpolator(mCurrentInterpolator);
        }
		// /!\ Terrible hack, do not do this at home!
		circularProgressDrawable.setBounds(0, 0,
				mCircularProgressBar.getWidth(),
				mCircularProgressBar.getHeight());
		mCircularProgressBar.setVisibility(View.INVISIBLE);
		mCircularProgressBar.setVisibility(View.VISIBLE);
		show();
	}

    public int dpToPx(int dp) {
        Resources r = mContext.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
        return px;
    }
}
