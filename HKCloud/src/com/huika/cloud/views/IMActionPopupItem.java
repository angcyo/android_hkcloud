package com.huika.cloud.views;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * @Description:弹窗内部子类项（绘制标题和图标）
 * @author lihy
 * @date 2015-7-17 下午1:55:51
 */
public class IMActionPopupItem {
	/** 定义图片对象 */
	public Drawable mDrawable;
	/** 定义文本对象 */
	public CharSequence mTitle;
	/** 最上面头部是否做特殊处理显示 */
	public Boolean isTopSpecial;
	/** 当前的选项是否被选中*/
	public Boolean mItemSelected = false;
	/** 当前的选项的值*/
	public int mItemValue;

	public IMActionPopupItem(Drawable drawable, CharSequence title, Boolean isTopSpecial) {
		this.mDrawable = drawable;
		this.mTitle = title;
		this.isTopSpecial = isTopSpecial;
	}

	public IMActionPopupItem(CharSequence title, Boolean isTopSpecial, int mItemValue, boolean mItemSelected) {
		this.mTitle = title;
		this.isTopSpecial = isTopSpecial;
		this.mItemValue = mItemValue;
		this.mItemSelected = mItemSelected;
	}

	public IMActionPopupItem(Context context, int titleId, int drawableId, Boolean isTopSpecial) {
		this(context, context.getResources().getText(titleId), drawableId, isTopSpecial);
	}

	public IMActionPopupItem(Context context, CharSequence title, int drawableId, Boolean isTopSpecial) {
		this(context.getResources().getDrawable(drawableId), title, isTopSpecial);
	}

	/**
	 * 重构构造函数，可以选择不传图片
	 * 
	 * @param context
	 * @param title
	 * @param isTopSpecial
	 */
	public IMActionPopupItem(Context context, CharSequence title, Boolean isTopSpecial) {
		this((Drawable) null, title, isTopSpecial);
		// this.mTitle = title;
		// this.isTopSpecial = isTopSpecial;
	}

	public IMActionPopupItem(CharSequence title, Boolean itemSelected) {
		this((Drawable) null, title, false);
		mItemSelected = itemSelected;
		// this.mTitle = title;
		// this.isTopSpecial = isTopSpecial;
	}
}
