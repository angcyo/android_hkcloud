package com.huika.cloud.control.me.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huika.cloud.R;
import com.huika.cloud.control.me.fragment.MyDisOrderFragment;
import com.huika.cloud.control.me.fragment.MyTeamFragment;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;

/**
 * @description：我的店铺
 * @author ht
 * @date 2015-12-4 上午10:23:17
 */
public class MyShopActivity extends RdpBaseActivity {
	private static final int BACK_IV = 0;
	private View mMasterView;
	@ViewInject(R.id.iv_user)
	private ImageView iv_user;
	@ViewInject(R.id.iv_level)
	private ImageView iv_level;
	@ViewInject(R.id.tv_user_name)
	private TextView tv_user_name;
	@ViewInject(R.id.tv_level_name)
	private TextView tv_level_name;
	@ViewInject(R.id.commission_account)
	private TextView commission_account;
	@ViewInject(R.id.today_commission_account)
	private TextView today_commission_account;
	@ViewInject(R.id.tv_my_team_num)
	private TextView tv_my_team_num;
	@ViewInject(R.id.dis_order_num)
	private TextView dis_order_num;
	@ViewInject(R.id.consume_count)
	private TextView consume_count;
	@ViewInject(R.id.expand_product_num)
	private TextView expand_product_num;
	@ViewInject(R.id.iv_user_bg)
	private ImageView iv_user_bg;

	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("个人中心");
		mMasterView = addMasterView(R.layout.layout_my_shop);
		RdpAnnotationUtil.inject(this);
	}

	@OnClick({R.id.goto_commission_detail, R.id.rl_team, R.id.rl_dis_order, R.id.rl_consume_count, R.id.rl_expand_product,
			R.id.rl_bind_wx, R.id.rl_expand_quickmark, R.id.rl_apply_v})
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.goto_commission_detail:
				// 佣金明细
//				startActivity(new Intent(mApplication,CommissionDetailActivity.class));
				break;
			case R.id.rl_team:
				// 我的团队
				MyTeamFragment myTeamFragment = new MyTeamFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, myTeamFragment).addToBackStack(null).commit();
				break;
			case R.id.rl_dis_order:
				// 我的分销订单
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
