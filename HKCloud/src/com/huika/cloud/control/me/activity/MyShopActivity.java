package com.huika.cloud.control.me.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.control.me.fragment.MyDisOrderFragment;
import com.huika.cloud.control.me.fragment.MyTeamFragment;
import com.huika.cloud.views.RoundImageView;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpViews.RdpCommViews.RdpInnerListView;

/**
 * @description：我的店铺
 * @author ht
 * @date 2015-12-4 上午10:23:17
 */
public class MyShopActivity extends RdpBaseActivity {
	private static final int BACK_IV = 0;
	private View mMasterView;
	private ImageView iv_user, iv_level;
	private TextView tv_user_name, tv_level_name, commission_account, today_commission_account, tv_my_team_num, dis_order_num, consume_count, expand_product_num;
	private ImageView goto_commission_detail;
	private RelativeLayout rl_team;
	private RelativeLayout rl_dis_order;
	private RelativeLayout rl_consume_count;
	private RelativeLayout rl_expand_product;
	private RelativeLayout rl_bind_wx;
	private RelativeLayout rl_expand_quickmark;
	private RelativeLayout rl_apply_v;
	private ImageView iv_user_bg;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("个人中心");
//		removeLeftFuncView(TBAR_FUNC_BACK);
		mMasterView = addMasterView(R.layout.layout_my_shop);
		RoundImageView riv=null;
		RdpInnerListView rilv=null;
		initView();
		initListener();
	}

	private void initListener() {
		rl_team.setOnClickListener(this);
		rl_dis_order.setOnClickListener(this);
		rl_consume_count.setOnClickListener(this);
		rl_expand_product.setOnClickListener(this);
		rl_bind_wx.setOnClickListener(this);
		rl_expand_quickmark.setOnClickListener(this);
		rl_apply_v.setOnClickListener(this);
		goto_commission_detail.setOnClickListener(this);
	}

	private void initView() {
		iv_user_bg = (ImageView) mMasterView.findViewById(R.id.iv_user_bg);
		iv_user = (ImageView) mMasterView.findViewById(R.id.iv_user);
		iv_level = (ImageView) mMasterView.findViewById(R.id.iv_level);
		tv_user_name = (TextView) mMasterView.findViewById(R.id.tv_user_name);
		tv_level_name = (TextView) mMasterView.findViewById(R.id.tv_level_name);
		tv_my_team_num = (TextView) mMasterView.findViewById(R.id.tv_my_team_num);
		consume_count = (TextView) mMasterView.findViewById(R.id.consume_count);
		dis_order_num = (TextView) mMasterView.findViewById(R.id.dis_order_num);
		expand_product_num = (TextView) mMasterView.findViewById(R.id.expand_product_num);
		commission_account = (TextView) mMasterView.findViewById(R.id.commission_account);
		today_commission_account = (TextView) mMasterView.findViewById(R.id.today_commission_account);
		goto_commission_detail = (ImageView) mMasterView.findViewById(R.id.goto_commission_detail);

		rl_team = (RelativeLayout) mMasterView.findViewById(R.id.rl_team);
		rl_dis_order = (RelativeLayout) mMasterView.findViewById(R.id.rl_dis_order);
		rl_consume_count = (RelativeLayout) mMasterView.findViewById(R.id.rl_consume_count);
		rl_expand_product = (RelativeLayout) mMasterView.findViewById(R.id.rl_expand_product);
		rl_bind_wx = (RelativeLayout) mMasterView.findViewById(R.id.rl_bind_wx);
		rl_expand_quickmark = (RelativeLayout) mMasterView.findViewById(R.id.rl_expand_quickmark);
		rl_apply_v = (RelativeLayout) mMasterView.findViewById(R.id.rl_apply_v);
	}

	@Override
	public void onClick(View v) {
		// super.onClick(v);
		switch (v.getId()) {
			case R.id.goto_commission_detail:
				// 佣金明细
				startActivity(new Intent(mApplication,CommissionDetailActivity.class));
				break;
			case R.id.rl_team:
				// 我的团队
				// startActivity(new Intent(mApplication, MyTeamActivity.class));
				MyTeamFragment myTeamFragment = new MyTeamFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, myTeamFragment).addToBackStack(null).commit();
				break;
			case R.id.rl_dis_order:
				// 我的分销订单
				// startActivity(new Intent(mApplication, MyDistributionOrderActivity.class));
				MyDisOrderFragment disOrderFragment = new MyDisOrderFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, disOrderFragment).addToBackStack(null).commit();
				break;
			case R.id.rl_consume_count:
				// 我的消费统计
				break;
			case R.id.rl_expand_product:
				// 推广产品
				break;
			case R.id.rl_bind_wx:
				// 绑定微信公众号
				startActivity(new Intent(mApplication, BindWXPublicNum.class));
				break;
			case R.id.rl_expand_quickmark:
				// 推广二维码
				break;
			case R.id.rl_apply_v:
				// 申请成为v
				break;
			default:
				break;
		}

		if (v.getTag() != null) {
			switch ((Integer) v.getTag()) {
				case TBAR_FUNC_BACK:
					if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
						super.onBackPressed();
					}
					else {
						getSupportFragmentManager().popBackStack();
					}
					break;
				default:
					break;
			}
		}
	}
}
