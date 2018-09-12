package com.cyanbirds.tanlove.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.adapter.MemberBuyAdapter;
import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.entity.MemberBuy;
import com.cyanbirds.tanlove.entity.PayResult;
import com.cyanbirds.tanlove.entity.UserVipModel;
import com.cyanbirds.tanlove.entity.WeChatPay;
import com.cyanbirds.tanlove.helper.SDKCoreHelper;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.request.CreateOrderRequest;
import com.cyanbirds.tanlove.net.request.GetAliPayOrderInfoRequest;
import com.cyanbirds.tanlove.net.request.GetMemberBuyListRequest;
import com.cyanbirds.tanlove.net.request.GetPayResultRequest;
import com.cyanbirds.tanlove.net.request.GetUserNameRequest;
import com.cyanbirds.tanlove.ui.widget.DividerItemDecoration;
import com.cyanbirds.tanlove.ui.widget.WrapperLinearLayoutManager;
import com.cyanbirds.tanlove.utils.CheckUtil;
import com.cyanbirds.tanlove.utils.DensityUtil;
import com.cyanbirds.tanlove.utils.PreferencesUtils;
import com.cyanbirds.tanlove.utils.RxBus;
import com.cyanbirds.tanlove.utils.ToastUtil;
import com.sunfusheng.marqueeview.MarqueeView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.umeng.analytics.MobclickAgent;
import com.yuntongxun.ecsdk.ECInitParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2016-01-13 21:34 GMT+8
 * @email 395044952@qq.com
 */
public class VipCenterActivity extends BaseActivity {

	@BindView(R.id.toolbar)
	Toolbar mToolbar;
	@BindView(R.id.marqueeView)
	MarqueeView mMarqueeView;
	@BindView(R.id.recyclerview)
	RecyclerView mRecyclerView;
	@BindView(R.id.preferential)
	TextView mPreferential;//优惠的说明文字，可以控制什么时候显示
	@BindView(R.id.vip_7_lay)
	RelativeLayout mVip7Lay;
	@BindView(R.id.vip_8_lay)
	RelativeLayout mVip8Lay;
	@BindView(R.id.scrollView)
	NestedScrollView mScrollView;
	@BindView(R.id.pref_tel_fare_lay)
	LinearLayout mPrefTelFareLay;

	private MemberBuyAdapter mAdapter;

	/**
	 * 没有网络时显示开通会员的名单
	 */
	private List<String> turnOnVipNameList;

	private static final int SDK_PAY_FLAG = 1;

	/**
	 * 普通会员商品
	 */
	private final int NORMAL_VIP = 0;

	private List<Integer> array;

	private String mPref;//优惠信息
	private MemberBuy mMemberBuy;
	private String channel = "";

	private Observable<?> observable;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					@SuppressWarnings("unchecked")
					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
					/**
					 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为9000则代表支付成功
					if (TextUtils.equals(resultStatus, "9000")) {
						// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
						ToastUtil.showMessage(R.string.pay_success);
						new GetPayResultTask().request();
					} else {
						// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
						ToastUtil.showMessage(R.string.pay_failure);
					}
					break;
				}
				default:
					break;
			}
		}

		;
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vipcenter);
		ButterKnife.bind(this);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.vip_center);
		}
		setupView();
		rxBusSub();
		setupData();
	}

	private void setupView() {
		LinearLayoutManager layoutManager = new WrapperLinearLayoutManager(
				this, LinearLayoutManager.VERTICAL, false);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.addItemDecoration(new DividerItemDecoration(
				this, LinearLayoutManager.VERTICAL, DensityUtil
				.dip2px(this, 12), DensityUtil.dip2px(
				this, 12)));
		mRecyclerView.setNestedScrollingEnabled(false);
	}

	/**
	 * rx订阅
	 */
	private void rxBusSub() {
		observable = RxBus.getInstance().register(AppConstants.CITY_WE_CHAT_RESP_CODE);
		observable.subscribe(o -> new GetPayResultTask().request());
	}

	private void setupData() {
		channel = CheckUtil.getAppMetaData(this, "UMENG_CHANNEL");
		if (!AppManager.getClientUser().is_vip && !"oppo".equals(channel)) {//oppo渠道，不显示这两个权限
			mVip7Lay.setVisibility(View.VISIBLE);
			mVip8Lay.setVisibility(View.VISIBLE);
		} else {
			mVip7Lay.setVisibility(View.GONE);
			mVip8Lay.setVisibility(View.GONE);
		}
		new GetMemberBuyListTask().request(NORMAL_VIP);
	}

	/**
	 * 获取用户名
	 */
	class GetUserNameTask extends GetUserNameRequest {
		@Override
		public void onPostExecute(final List<String> strings) {
			if (strings != null && strings.size() > 0) {
				turnOnVipNameList = new ArrayList<>();
				for (String name : strings) {
					turnOnVipNameList.add(name + " 开通了会员，赶快去和TA聊天吧！");
				}
				mMarqueeView.startWithList(turnOnVipNameList);
				mPreferential.setText(mPref);
			} else {
				setTurnOnVipUserName();
			}
		}

		@Override
		public void onErrorExecute(String error) {
			setTurnOnVipUserName();
		}
	}

	class GetMemberBuyListTask extends GetMemberBuyListRequest {
		@Override
		public void onPostExecute(List<MemberBuy> memberBuys) {
			mAdapter = new MemberBuyAdapter(VipCenterActivity.this, memberBuys);
			mAdapter.setOnItemClickListener(mOnItemClickListener);
			mRecyclerView.setAdapter(mAdapter);

			if (null != memberBuys && memberBuys.size() > 0) {
				array = new ArrayList<>(memberBuys.size());
				for (int i = 0; i < memberBuys.size(); i++) {
					if (!TextUtils.isEmpty(memberBuys.get(i).preferential) &&
							memberBuys.get(i).preferential.length() > 10) {
						mPreferential.setVisibility(View.VISIBLE);
						mPref = memberBuys.get(i).preferential;
						continue;
					}
					if (!TextUtils.isEmpty(memberBuys.get(i).preferential)) {
						array.add(Integer.parseInt(memberBuys.get(i).preferential));
					}
				}
				if (array.size() == 0 || "oppo".equals(channel)) {//oppo渠道，不显示有什么人赠送话费
					mPrefTelFareLay.setVisibility(View.VISIBLE);
					PreferencesUtils.setIsHasGetFareActivity(VipCenterActivity.this, false);
				} else {
					mPrefTelFareLay.setVisibility(View.GONE);
					PreferencesUtils.setIsHasGetFareActivity(VipCenterActivity.this, true);
				}
			}
			new GetUserNameTask().request(1, 100);
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	private void setTurnOnVipUserName() {
		turnOnVipNameList = new ArrayList<>();
		turnOnVipNameList.add("雨天 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("秋叶 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("撕裂时光 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("真爱 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("许愿树 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("木瓜 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("山楂 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("夕阳 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("花依旧开 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("心在这里 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("无花果 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("萌兔 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("残缺布偶 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("潮汐 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("寂寞的心 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("丹樱。。。 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("如影随形 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("葛葛 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("薛金玲 开通了会员，赶快去和TA聊天吧！");
		turnOnVipNameList.add("花物语 开通了会员，赶快去和TA聊天吧！");
		if (null != mMarqueeView) {
			mMarqueeView.startWithList(turnOnVipNameList);
		}
	}

	private MemberBuyAdapter.OnItemClickListener mOnItemClickListener = new MemberBuyAdapter.OnItemClickListener() {
		@Override
		public void onItemClick(View view, int position) {
			MemberBuy memberBuy = mAdapter.getItem(position);
			choicePayWay(memberBuy);
		}
	};

	private void choicePayWay(MemberBuy memberBuy) {
		if (memberBuy.isShowAliPay && memberBuy.isShowWePay) {
			showPayDialog(memberBuy);
		} else if (memberBuy.isShowAliPay) {
			new GetAliPayOrderInfoTask().request(memberBuy.id, AppConstants.ALI_PAY_PLATFORM);
		} else if (memberBuy.isShowWePay) {
			new CreateOrderTask().request(memberBuy.id, AppConstants.WX_PAY_PLATFORM);
		}
	}

	private void showPayDialog(final MemberBuy memberBuy) {
		mMemberBuy = memberBuy;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.pay_type));
		builder.setNegativeButton(getResources().getString(R.string.cancel),
				null);
		String aliPay = getResources().getString(R.string.ali_pay);
		String weChatPay = getResources().getString(R.string.weixin_pay);
		if (memberBuy.isShowAli) {
			aliPay = aliPay + "(推荐)";
		} else if (memberBuy.isShowWe) {
			weChatPay = weChatPay + "(推荐)";
		}
		builder.setItems(
				new String[]{aliPay, weChatPay},
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case 0:
								new GetAliPayOrderInfoTask().request(memberBuy.id, AppConstants.ALI_PAY_PLATFORM);
								break;
							case 1:
								new CreateOrderTask().request(memberBuy.id, AppConstants.WX_PAY_PLATFORM);
								break;
						}
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * 获取支付成功之后用户开通了哪项服务
	 */
	class GetPayResultTask extends GetPayResultRequest {
		@Override
		public void onPostExecute(UserVipModel userVipModel) {
			SDKCoreHelper.init(CSApplication.getInstance(), ECInitParams.LoginMode.FORCE_LOGIN);
			AppManager.getClientUser().is_vip = userVipModel.isVip;
			AppManager.getClientUser().is_download_vip = userVipModel.isDownloadVip;
			AppManager.getClientUser().gold_num = userVipModel.goldNum;
			Snackbar.make(findViewById(R.id.vip_layout),
					"您已经是会员了，赶快去聊天吧", Snackbar.LENGTH_SHORT)
					.show();
			if (mMemberBuy != null) {
				if (mMemberBuy.price > 200) {
					PreferencesUtils.setRewardCount(VipCenterActivity.this, 3);
				}
				if (mMemberBuy.price > 150) {
					PreferencesUtils.setWhichVip(VipCenterActivity.this, 0);
				} else {
					PreferencesUtils.setWhichVip(VipCenterActivity.this, 1);
				}
			}
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	/*********************************************************************************************************************/

	/**
	 * 调用微信支付
	 */
	class CreateOrderTask extends CreateOrderRequest {
		@Override
		public void onPostExecute(WeChatPay weChatPay) {
			PayReq payReq = new PayReq();
			payReq.appId = AppConstants.WEIXIN_PAY_ID;
			payReq.partnerId = weChatPay.mch_id;
			payReq.prepayId = weChatPay.prepay_id;
			payReq.packageValue = "Sign=WXPay";
			payReq.nonceStr = weChatPay.nonce_str;
			payReq.timeStamp = weChatPay.timeStamp;
			payReq.sign = weChatPay.appSign;
			AppManager.getIWX_PAY_API().sendReq(payReq);
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	/**
	 * 调用支付宝支付
	 */
	class GetAliPayOrderInfoTask extends GetAliPayOrderInfoRequest {
		@Override
		public void onPostExecute(String s) {
			payV2(s);
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	/**
	 * 支付宝支付业务
	 *
	 * @param orderInfo
	 */
	public void payV2(final String orderInfo) {
		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
		 * orderInfo的获取必须来自服务端；
		 */
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(VipCenterActivity.this);
				Map<String, String> result = alipay.payV2(orderInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}


	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
		MobclickAgent.onPause(this);
	}
}
