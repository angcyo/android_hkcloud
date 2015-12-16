package cn.sharesdk.onekeyshare.view;

import com.hkcloud.share.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class MySharePopWindows extends PopupWindow {
	private Context mContext;
	private NoScrollGridView mGridView;
	private String[] titles;
	private String[] platform;
	private Drawable[] res;
	private Button cancalBtn;
	private GriViewOnImtermClick click;
	private boolean flag=false;

	/** 获取默认的分享pop */
	public static MySharePopWindows getDefaultSharePop(Context context, GriViewOnImtermClick click) {
		String[] title = new String[] { context.getString(R.string.qq), context.getString(R.string.qzone),
				context.getString(R.string.wechatmoments), context.getString(R.string.sinaweibo),
				context.getString(R.string.wechat) };
		String[] platform = new String[] { QQ.NAME, QZone.NAME, WechatMoments.NAME, SinaWeibo.NAME, Wechat.NAME };
		Drawable[] resImg = new Drawable[] { context.getResources().getDrawable(R.drawable.pm_logo_qq),
				context.getResources().getDrawable(R.drawable.pm_logo_qzone),
				context.getResources().getDrawable(R.drawable.pm_logo_wechatmoments),
				context.getResources().getDrawable(R.drawable.pm_logo_sinaweibo),
				context.getResources().getDrawable(R.drawable.pm_logo_wechat) };
		return new MySharePopWindows(context, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, title, platform,
				resImg, click);
	}
   /**
    * 底部分享GridView
    * @param context
    * @param width
    * @param height
    * @param titles
    * @param platforms
    * @param res
    * @param click
    */
	public MySharePopWindows(Context context, int width, int height, String[] titles, String[] platforms,
			Drawable[] res, GriViewOnImtermClick click) {
		super();
		this.mContext = context;
		this.titles = titles;
		this.platform = platforms;
		this.res = res;
		this.click = click;
		setFocusable(true);
		// 设置弹窗内可点击
		setTouchable(true);
		// 设置弹窗外可点击
		setOutsideTouchable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.mystyle);
		// 加上这个只是为了让返回键点击能消失popWindow
		setBackgroundDrawable(new BitmapDrawable());
		// 设置弹窗的宽度和高度
		setWidth(width);
		setHeight(height);
		// 设置弹窗的布局界面
		setContentView(LayoutInflater.from(mContext).inflate(R.layout.popwindow_list, null));
		initUi();
	}


	private void initUi() {
		mGridView = (NoScrollGridView) getContentView().findViewById(R.id.pop_grideview);
		cancalBtn = (Button) getContentView().findViewById(R.id.btn_cancal);
		cancalBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
		mGridView.setAdapter(new BaseAdapter() {

			@SuppressLint("NewApi")
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				CHolder holder;
				if (convertView == null) {
					holder = new CHolder();
					if(!flag){
						convertView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_item, null);
					}else{
						convertView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_item_two, null);
					}
					holder.tvTitle = (TextView) convertView.findViewById(R.id.title_tv);
					convertView.setTag(holder);
				} else {
					holder = (CHolder) convertView.getTag();
				}
				holder.tvTitle.setText(titles[position]);
				holder.tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, res[position], null, null);
				holder.tvTitle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						click.onGvOnItem(platform[position]);
						dismiss();
					}
				});
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return titles[position];
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return titles.length;
			}

			class CHolder {
				TextView tvTitle;
			}
		});
	}

	public void showPop(View v) {
		showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	public interface GriViewOnImtermClick {
		void onGvOnItem(String title);
	}
}
