package android.support.v4.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhoukl.androidRDP.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @description：图片滚动类
 * @T
 * @date 2014年7月14日 下午5:44:17
 * @date 2014年7月15日 下午19, fanxing修改
 */
public class AutoScrollViewPager<T extends PagerData> extends ViewPager {
	private int mScrollTime = 0;
	private int oldIndex = 0;
	private int curIndex = 0;
	private List<T> mPagerData = new ArrayList<T>();
	private PagerAdapter pagerAdapter;
	private LinearLayout indicatorView;
	private int focusedDrawable, normalDrawable;
	private LayoutInflater inflater;
	private OnPageItemClickListener<T> pageItemClickListener;
	private boolean isFakeCycle = false;// 是否是假的循环
	private boolean isStartScroll;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			handler.removeCallbacksAndMessages(null);
			if (isFakeCycle) {
				setCurrentItem(getCurrentItem() + 1);
			}
			else {
				if (getCurrentItem() == mPagerData.size() - 1) {
					setCurrentItem(0, true);
				}
				else {
					setCurrentItem(getCurrentItem() + 1);
				}
			}
			handler.sendMessageDelayed(handler.obtainMessage(), mScrollTime);
		}
	};
	// private int errorImageResId=R.drawable.product_list_default;
	// private int defaultImageResId=R.drawable.product_list_default;
	private DisplayImageOptions options;

	public AutoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);

		inflater = LayoutInflater.from(context);

		focusedDrawable = R.drawable.common_dot_selected;
		normalDrawable = R.drawable.common_dot_normal;

		setInternalPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int i) {
				if (mPagerData.isEmpty() || indicatorView == null || indicatorView.getChildCount() == 0) return;

				curIndex = i % mPagerData.size();
				// 取消圆点选中
				indicatorView.getChildAt(oldIndex).setBackgroundResource(normalDrawable);
				// 圆点选中
				indicatorView.getChildAt(curIndex).setBackgroundResource(focusedDrawable);
				oldIndex = curIndex;
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});

		// 设置滑动动画时间 ,如果用默认动画时间可不用 ,反射技术实现
		new FixedSpeedScroller(getContext()).setDuration(this, 700);

		pagerAdapter = new MyPagerAdapter();
		setAdapter(pagerAdapter);

		setOffscreenPageLimit(2);

		requestDisallowInterceptTouchEvent(true);

		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.index_advert_default) // resource or drawable
				.showImageForEmptyUri(R.drawable.index_advert_default) // resource or drawable
				.showImageOnFail(R.drawable.index_advert_default) // resource or drawable
				.cacheInMemory(true)// 开启内存缓存
				.cacheOnDisk(true) // 开启硬盘缓存
				.resetViewBeforeLoading(false).build();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (isStartScroll) {
			start(mScrollTime);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (isStartScroll) handler.removeCallbacksAndMessages(null);
	}

	/** 构建内部索引父view */
	private void initInnerIndicator() {
		indicatorView = new IndicatorView(getContext());
		indicatorView.setGravity(Gravity.CENTER);
		indicatorView.setBackgroundColor(0x0fFF00FF);
		indicatorView.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams params = new LayoutParams();
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.height = 40;
		params.gravity = Gravity.BOTTOM;
		addView(indicatorView, params);
	}

	public void setDatas(List<T> imgUrls, LinearLayout outIndicator, boolean needInnerIndicator, boolean isFakeCycle) {
		this.isFakeCycle = isFakeCycle;
		setDatas(imgUrls, outIndicator, needInnerIndicator);
	}

	/**重复调用可能会anr*/
	public void setDatas(List<T> imgUrls, LinearLayout outIndicator, boolean needInnerIndicator) {
		if (outIndicator != null) {
			indicatorView = outIndicator;
		}
		else if (needInnerIndicator) {
			initInnerIndicator();
		}
		mPagerData.clear();
		mPagerData.addAll(imgUrls);
		pagerAdapter.notifyDataSetChanged();
	}

	/**重新加载图片数据，刷新vp内容*/
	public void refreshDatas(List<T> imgUrls, LinearLayout outIndicator, boolean needInnerIndicator, boolean isFakeCycle) {
		this.isFakeCycle = isFakeCycle;
		if (outIndicator != null) {
			indicatorView = outIndicator;
		}
		else if (needInnerIndicator) {
			initInnerIndicator();
		}
		mPagerData.clear();
		mPagerData.addAll(imgUrls);
		pagerAdapter = new MyPagerAdapter();
		setAdapter(pagerAdapter);
		pagerAdapter.notifyDataSetChanged();
	}

	@Override
	void dataSetChanged() {
		super.dataSetChanged();
		setUpIndicator();
	}

	// 设置圆点
	private void setUpIndicator() {
		if (indicatorView != null) {
			indicatorView.removeAllViews();
			if (mPagerData.isEmpty()) {
				indicatorView.setVisibility(View.GONE);
				return;
			}
			else {
				indicatorView.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < mPagerData.size(); i++) {
				View v = inflater.inflate(R.layout.auto_scrollpage_dot, indicatorView, false);
				v.setBackgroundResource(normalDrawable);
				indicatorView.addView(v);
			}
			indicatorView.getChildAt(0).setBackgroundResource(focusedDrawable);
		}
		curIndex = oldIndex = 0;
		if (mPagerData.size() > 1 && isFakeCycle) {
			setCurrentItem(15 - 15 % mPagerData.size());// 设置选中为中间/图片为和第0张一样
		}
		else {
			setCurrentItem(0);
		}
	}

	/** 获取真实的当前位置 */
	public int getCurrentPosition() {
		return curIndex;
	}

	/** 获取真实的当前位置的数据 */
	public T getCurrentData() throws IndexOutOfBoundsException {
		return mPagerData.get(curIndex);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
			start();
		}
		else {
			handler.removeCallbacksAndMessages(null);
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
			start();
		}
		else {
			handler.removeCallbacksAndMessages(null);
		}
		return super.onTouchEvent(ev);
	}

	/** 开始广告滚动 */
	private void start() {
		start(mScrollTime);
	}

	public void start(int scrollTime) {
		// handler.removeCallbacksAndMessages(null);
		mScrollTime = scrollTime;
		isStartScroll = false;
		if (mScrollTime > 0 && !mPagerData.isEmpty()) {
			isStartScroll = true;
			handler.sendMessageDelayed(handler.obtainMessage(), mScrollTime);
		}
	}

	// public void setErrorImageResId(int errorImageResId){
	// this.errorImageResId=errorImageResId;
	// }
	// public void setDefaultImageResId(int defaultImageResId){
	// this.defaultImageResId=defaultImageResId;
	// }
	public void setImageOptions(DisplayImageOptions options) {
		this.options = options;
	}

	/** 设置显示条目的点击事件 */
	public void setOnPageItemClickListener(OnPageItemClickListener<T> pageItemClickListener) {
		this.pageItemClickListener = pageItemClickListener;
	}

	public interface OnPageItemClickListener<T extends PagerData> {
		void onPageItemClickListener(T pd);
	}

	// 适配器 //循环设置
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (mPagerData.size() == 1) {
				return 1;
			}
			else if (mPagerData.size() > 1) { return isFakeCycle ? Integer.MAX_VALUE : mPagerData.size(); }
			return 0;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			final int realP = position % mPagerData.size();
			// NetworkImageView netImgView = (NetworkImageView) inflater.inflate(R.layout.auto_scrollpage_img, container, false);
			final ImageView netImgView = (ImageView) inflater.inflate(R.layout.auto_scrollpage_img, container, false);

			// netImgView.setErrorImageResId(errorImageResId);
			// netImgView.setDefaultImageResId(errorImageResId);
			// volley的imageloader缓存有问题，换成惠信的
			// ImageLoader imageLoader = VolleyRequestManager.getImageLoader();
			// netImgView.setImageUrl(mPagerData.get(realP).getImageUrl(), imageLoader);
			// TODO
			ImageLoader.getInstance().displayImage(mPagerData.get(realP).getImageUrl(), netImgView, options);

			netImgView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (pageItemClickListener != null) {
						pageItemClickListener.onPageItemClickListener(mPagerData.get(realP));
					}
				}
			});

			container.addView(netImgView);
			return netImgView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (object instanceof View) {
				container.removeView((View) object);
			}
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
}
