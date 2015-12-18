package com.huika.cloud.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huika.cloud.R;
import com.zhoukl.androidRDP.RdpUtils.UiHelper;

import java.util.ArrayList;

/**
 * @Description:自定义PopupWindows
 * @author lihy
 * @date 2015-7-17 下午1:56:04
 */
public class IMButtomPopup extends PopupWindow {

	// 列表弹窗的间隔
	protected final int LIST_PADDING = 10;
	// 坐标的位置（x、y）
	private final int[] mLocation = new int[2];
	private Context mContext;
	// 实例化一个矩形
	private Rect mRect = new Rect();
	// 屏幕的宽度和高度
	private int mScreenWidth, mScreenHeight;
	// 判断是否需要添加或更新列表子类项
	private boolean mIsDirty;
	// 位置不在中心
	// private int popupGravity = Gravity.NO_GRAVITY;
	private int popupGravity = Gravity.BOTTOM;
	// 弹窗子类项选中时的监听
	private OnPopupItemOnClickListener mItemOnClickListener;
	// 定义列表对象
	private ListView mListView;
	private TextView cancel;
	// 定义弹窗子类项列表
	private ArrayList<IMActionPopupItem> mActionItems = new ArrayList<IMActionPopupItem>();
	private OnClickListener dismissClickListener;

	public IMButtomPopup(Context mContext, int width, int height, OnPopupItemOnClickListener listener) {
		super(mContext);
		this.mContext = mContext;
		this.mItemOnClickListener = listener;
		setContentView(LayoutInflater.from(mContext).inflate(R.layout.im_actionsheet_popup, null));
		// 设置弹窗的宽度和高度
		setWidth(width);
		setHeight(height);
		setFocusable(true);
		// 设置弹窗内可点击
		setTouchable(true);
		// 设置弹窗外可点击
		setOutsideTouchable(true);
		// setCanceledOnTouchOutside(false);
		setBackgroundDrawable(new BitmapDrawable());
		update();
		
		initScreenWidth();
//		appBaseApplication = GlobalApp.getInstance();
//		// 获得屏幕的宽度和高度
//		// 获得屏幕的宽度和高度
//		mScreenWidth = appBaseApplication.SCREEN_WIDTH;
//		mScreenHeight = appBaseApplication.SCREEN_HEIGHT;
		// 设置弹窗的布局界面
		initUI();
	}

	private void initScreenWidth() {
		DisplayMetrics dm = UiHelper.getDisplayMetrics(mContext);
		mScreenWidth = dm.heightPixels;
		mScreenHeight = dm.widthPixels;
	}
	/**
	 * 初始化弹窗列表
	 */
	private void initUI() {
		// getContentView这种设置不错
		mListView = (ListView) getContentView().findViewById(R.id.listview);
		mListView.setVerticalScrollBarEnabled(true);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
				if (mItemOnClickListener != null) {
					controlPopSelect(index);
					mItemOnClickListener.onPopupItemClick(mActionItems.get(index), index);
				}
				dismiss();
			}
		});
		cancel = (TextView) getContentView().findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dismissClickListener != null) {
					dismissClickListener.onClick(v);
				}
				dismiss();
			}
		});
	}

	public OnClickListener getDismissClickListener() {
		return dismissClickListener;
	}

	public void setDismissClickListener(OnClickListener dismissClickListener) {
		this.dismissClickListener = dismissClickListener;
	}

	private void controlPopSelect(int index) {
		for (IMActionPopupItem item : mActionItems) {
			item.mItemSelected = false;
		}
		mActionItems.get(index).mItemSelected = true;
		populateActions();
	}

	/**
	 * 
	 * @description：清除  打钩
	 * @author zhangjianlin (990996641)
	 * @date 2015年3月25日 下午1:33:00
	 */
	public void resetPopSelect() {
		for (IMActionPopupItem item : mActionItems) {
			item.mItemSelected = false;
		}
	}

	/**
	 * 设置弹窗列表子项
	 */
	private void populateActions() {
		mIsDirty = false;
		// 设置列表的适配器
		mListView.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = null;
				PopHolder popHolder = null;
				if (convertView == null) {
					popHolder = new PopHolder();
					convertView = View.inflate(mContext, R.layout.im_item_pop_action, null);
					popHolder.actionInfo = (TextView) convertView.findViewById(R.id.action_info);
					popHolder.actionImage = (ImageView) convertView.findViewById(R.id.action_image);
					convertView.setTag(popHolder);
				} else {
					popHolder = (PopHolder) convertView.getTag();
				}

				if (1 == getCount()) {
					popHolder.actionInfo.setBackgroundResource(R.drawable.im_btn_dialog_special);
				} else if (2 == getCount()) {
					if (0 == position) {
						popHolder.actionInfo.setBackgroundResource(R.drawable.im_btn_dialog_special);
					} else if (1 == position) {
						popHolder.actionInfo.setBackgroundResource(R.drawable.im_btn_dialog_special);
					}
				} else {
					if (0 == position) {
						popHolder.actionInfo.setBackgroundResource(R.drawable.im_btn_dialog_special);
					} else if (getCount() - 1 == position) {
						popHolder.actionInfo.setBackgroundResource(R.drawable.im_btn_dialog_special);
					} else {
						popHolder.actionInfo.setBackgroundResource(R.drawable.im_btn_dialog_special);
					}
				}

				IMActionPopupItem item = mActionItems.get(position);
				// 设置文本文字
				popHolder.actionInfo.setText(item.mTitle);
				if (item.isTopSpecial) {
					popHolder.actionInfo.setEnabled(false);
					popHolder.actionInfo.setTextColor(Color.parseColor("#a0a0a0"));
				}
				if (item.mItemSelected) {
					popHolder.actionImage.setVisibility(View.VISIBLE);
				} else {
					popHolder.actionImage.setVisibility(View.GONE);
				}
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return mActionItems.get(position);
			}

			@Override
			public int getCount() {
				return mActionItems.size();
			}

			// adapter中的所有item是否可以点击
			@Override
			public boolean areAllItemsEnabled() {
				return true;
			}

			// 表明下标为position 的item不可选中，不可点击
			@Override
			public boolean isEnabled(int position) {
				return !mActionItems.get(position).isTopSpecial;
			}
		});
	}

	/**
	 * 添加子类项
	 */
	public void addAction(IMActionPopupItem action) {
		if (action != null) {
			mActionItems.add(action);
			mIsDirty = true;
		}
	}

	/**
	 * 清除子类项
	 */
	public void cleanAction() {
		if (!mActionItems.isEmpty()) {
			mActionItems.clear();
			mIsDirty = true;
		}
	}

	/**
	 * 根据位置得到子类项
	 */
	public IMActionPopupItem getAction(int position) {
		if (position < 0 || position > mActionItems.size()) return null;
		return mActionItems.get(position);
	}

	/**
	 * 显示弹窗列表界面
	 */
	public void show(View view) {
		// 获得点击屏幕的位置坐标
		// view.getLocationOnScreen(mLocation);
		// 设置矩形的大小
		// mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(), mLocation[1] + view.getHeight());
		// 判断是否需要添加或更新列表子类项
		if (mIsDirty) {
			populateActions();
		}
		// 显示弹窗的位置
		// showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING - (getWidth() / 2), mRect.bottom);
		showAtLocation(view, popupGravity, 0, 0);

	}

	/**
	 * @description：弹窗子类项按钮监听事件
	 * @author samy
	 * @date 2014年8月25日 上午12:04:14
	 */
	public interface OnPopupItemOnClickListener {
		void onPopupItemClick(IMActionPopupItem item, int position);
	}

	class PopHolder {
		TextView actionInfo;
		ImageView actionImage;
	}

}
