package com.huika.cloud.control.me.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;

import com.google.gson.reflect.TypeToken;
import com.huika.cloud.R;
import com.huika.cloud.config.Constant;
import com.huika.cloud.control.safeaccount.activity.UpPayPwdActivity;
import com.huika.cloud.support.event.GetUserInfoEvent;
import com.huika.cloud.support.model.CardBean;
import com.huika.cloud.support.model.WithDrawInfoBean;
import com.huika.cloud.util.MMAlertDialog;
import com.huika.cloud.util.MMAlertDialog.DialogOnItemClickListener;
import com.huika.cloud.views.passwordview.GridPasswordView;
import com.huika.cloud.views.passwordview.GridPasswordView.OnPasswordChangedListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpDataListAdapter;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.AdapterViewHolder;
import com.zhoukl.androidRDP.RdpAdapter.RdpAdapter.OnRefreshItemViewsListener;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetCommand;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpNetDataSet;
import com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.RdpResponseResult;
import com.zhoukl.androidRDP.RdpFramework.RdpActivity.RdpBaseActivity;
import com.zhoukl.androidRDP.RdpUtils.RdpAnnotationUtil;
import com.zhoukl.androidRDP.RdpUtils.help.ToastMsg;

import de.greenrobot.event.EventBus;

/**
 * @description：提现申请
 * @author ht
 * @date 2015-12-4 上午10:24:21
 */
public class WithdrawApplicationActivity extends RdpBaseActivity {
	private static final int CARD_LIST_REQUEST_CODE = 0;
	protected static final int REQUESTCODE = 0;
	private View mMasterView;
	private View card_selected;
	private View weixin_selected;
	private LinearLayout ll_card;
	private LinearLayout ll_weixin;
	@ViewInject(R.id.confirm_apply_card)
	private Button confirm_apply_card;
	@ViewInject(R.id.confirm_apply_weixin)
	private Button confirm_apply_weixin;
	@ViewInject(R.id.weixin_num)
	private TextView weixin_num;
	@ViewInject(R.id.wx_money)
	private EditText wx_money;
	@ViewInject(R.id.card_count)
	private EditText card_count;
	@ViewInject(R.id.card_name)
	private TextView card_name;
	@ViewInject(R.id.gpwv)
	private GridPasswordView gpwv;
	@ViewInject(R.id.forget_pwd)
	private TextView forget_pwd;
	@ViewInject(R.id.with_draw_money)
	private TextView with_draw_money;
	
	private List<CardBean> cardList;
	private int isSelectedPosition;
	private CardBean selectedCard;


	private RdpDataListAdapter<CardBean> cardListAdapter;
	private PopupWindow cardListPop;
	
	@Override
	protected void initActivity() {
		super.initActivity();
		setFuncTitle("提现申请");
		selectedCard = getIntent().getParcelableExtra("selected_card");
		mMasterView = addMasterView(R.layout.apply_for_with_draw);
		RdpAnnotationUtil.inject(this);
		initListener();
		if(selectedCard!=null){
			card_name.setText(setBankNameAndCardNum(selectedCard));
		}
	}
	private void initListener() {
//		roll_out_card.setOnClickListener(this);
//		roll_out_weixin.setOnClickListener(this);
//		confirm_apply_card.setOnClickListener(this);
//		confirm_apply_weixin.setOnClickListener(this);
//		rl_card_list.setOnClickListener(this);
		TextWatcher textWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					confirm_apply_card.setEnabled(true);
				}
				else {
					confirm_apply_card.setEnabled(false);
					confirm_apply_weixin.setEnabled(false);
				}
			}
		};
		TextWatcher textWatcher2 = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					confirm_apply_weixin.setEnabled(true);
				}
				else {
					confirm_apply_weixin.setEnabled(false);
				}
			}
		};
		// 设置文字长度改变监听
		card_count.addTextChangedListener(textWatcher);
		wx_money.addTextChangedListener(textWatcher2);
	}
	
	private void initView() {
		card_selected = mMasterView.findViewById(R.id.card_selected);
		weixin_selected = mMasterView.findViewById(R.id.weixin_selected);
		ll_card = (LinearLayout) mMasterView.findViewById(R.id.ll_card);
		ll_weixin = (LinearLayout) mMasterView.findViewById(R.id.ll_weixin);
		card_name = (TextView) mMasterView.findViewById(R.id.card_name);
		card_count = (EditText) mMasterView.findViewById(R.id.card_count);
		wx_money = (EditText) mMasterView.findViewById(R.id.wx_money);
		weixin_num = (TextView) mMasterView.findViewById(R.id.weixin_num);
		confirm_apply_card = (Button) mMasterView.findViewById(R.id.confirm_apply_card);
		confirm_apply_weixin = (Button) mMasterView.findViewById(R.id.confirm_apply_weixin);
	}

	@OnClick({R.id.roll_out_card,R.id.roll_out_weixin,R.id.rl_card_list,R.id.confirm_apply_card,R.id.confirm_apply_weixin})
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.roll_out_card:
				// 提现到银行卡
				ll_weixin.setVisibility(View.GONE);
				ll_card.setVisibility(View.VISIBLE);
				card_selected.setVisibility(View.VISIBLE);
				weixin_selected.setVisibility(View.GONE);
				confirm_apply_card.setVisibility(View.VISIBLE);
				confirm_apply_weixin.setVisibility(View.GONE);
				break;
			case R.id.roll_out_weixin:
				// 提现到微信钱包
				ll_weixin.setVisibility(View.VISIBLE);
				ll_card.setVisibility(View.GONE);
				card_selected.setVisibility(View.GONE);
				weixin_selected.setVisibility(View.VISIBLE);
				confirm_apply_card.setVisibility(View.GONE);
				confirm_apply_weixin.setVisibility(View.VISIBLE);
				break;
			case R.id.rl_card_list:
				// 银行卡列表
				startActivityForResult(new Intent(mApplication, BankCardListActivity.class),CARD_LIST_REQUEST_CODE);
				break;
			case R.id.confirm_apply_card:
				String withdraw_count ="¥"+card_count.getText().toString();
				// 提现到银行卡
				DialogOnItemClickListener onItemClickListener = new DialogOnItemClickListener() {
					@Override
					public void onItemClickListener(View v, int position) {
						if(v.getId()==R.id.forget_pwd){
							ToastMsg.showToastMsg(mApplication, "忘记密码");
							Intent updatePwd = new Intent(mApplication,UpPayPwdActivity.class);
							updatePwd.putExtra(BindBankActivity.INP_TYPE, 2);
							startActivityForResult(updatePwd, REQUESTCODE);
						}
					}
					
					@Override
					public void onDialogDismiss(String psw) {
//						ToastMsg.showToastMsg(mApplication, "输入的密码" + psw);
						String withDrawMoney = card_count.getText().toString();
						// TODO 发送提现申请请求
						// sendWithDrawRequest();
						String titleStr="申请已提交，将在1-2个工作日内完成转账，请注意查收";
						Dialog warnDlg = MMAlertDialog.createCenterWarnDialog(WithdrawApplicationActivity.this, titleStr, closeClick);
						warnDlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
							@Override
							public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
								 if (keyCode == KeyEvent.KEYCODE_BACK  
					                        && event.getRepeatCount() == 0) {  
					                   WithdrawApplicationActivity.this.finish();
					                }  
								return false;
							}
						});
						warnDlg.show();
					}
				};
				Dialog dialog = new MMAlertDialog().createPwdDialog(WithdrawApplicationActivity.this, onItemClickListener, false,withdraw_count);
				dialog.show();
				break;
			case R.id.confirm_apply_weixin:
				// 提现到微信
				break;
			default:
				break;
		}
		super.onClick(v);
	}

	OnClickListener closeClick=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.iv_close){
				EventBus.getDefault().post(
						new GetUserInfoEvent());
				WithdrawApplicationActivity.this.finish();
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode== CARD_LIST_REQUEST_CODE) {
			CardBean select_card = data.getParcelableExtra("select_item");
			String bankNameAndCardNum = setBankNameAndCardNum(select_card);
			card_name.setText(bankNameAndCardNum);
		}
	}

	private void sendWithDrawRequest() {
		Type typeOfResult = new TypeToken<WithDrawInfoBean>() {
		}.getType();
		RdpNetCommand withdrawRequest = new RdpNetCommand(mApplication, typeOfResult);
		withdrawRequest.setServerApiUrl("提现申请url");
		withdrawRequest.clearConditions();
		withdrawRequest.setOnCommandSuccessedListener(WithdrawApplicationActivity.this);
		withdrawRequest.setOnCommandFailedListener(WithdrawApplicationActivity.this);
		withdrawRequest.setCondition("cardId", "cardId");
		withdrawRequest.setCondition("memberId", "memberId");
		withdrawRequest.setCondition("payPwd", "payPwd");
		withdrawRequest.setCondition("amount", "amount");
		withdrawRequest.setCondition("validateCode", "validateCode");
		withdrawRequest.setCondition("type", "type");
		withdrawRequest.execute();
	}

	@Override
	public void onCommandSuccessed(Object reqKey, RdpResponseResult result, Object data) {
		super.onCommandSuccessed(reqKey, result, data);
		if ("提现请求的url".equals(result.getUrl())) {
			// 提现请求
			result.getMsg();
		}
	}

	@Override
	public void onCommandFailed(Object reqKey, RdpResponseResult result) {
		super.onCommandFailed(reqKey, result);
	}



	private String setBankNameAndCardNum(CardBean cardBean) {
		String bankName = cardBean.bankName;
		String cardNumber = cardBean.cardNumber;
		cardNumber = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
		return bankName + "(" + cardNumber + ")";
	}
}