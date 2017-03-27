package com.cyanbirds.tanlove.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.adapter.MyGoldAdapter;
import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.entity.MemberBuy;
import com.cyanbirds.tanlove.entity.PayResult;
import com.cyanbirds.tanlove.entity.UserVipModel;
import com.cyanbirds.tanlove.entity.WeChatPay;
import com.cyanbirds.tanlove.eventtype.PayEvent;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.request.CreateOrderRequest;
import com.cyanbirds.tanlove.net.request.GetAliPayOrderInfoRequest;
import com.cyanbirds.tanlove.net.request.GetMemberBuyListRequest;
import com.cyanbirds.tanlove.net.request.GetPayResultRequest;
import com.cyanbirds.tanlove.ui.widget.DividerItemDecoration;
import com.cyanbirds.tanlove.utils.DensityUtil;
import com.cyanbirds.tanlove.utils.ToastUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2016-06-22 14:44 GMT+8
 * @description
 */
public class MyGoldActivity extends BaseActivity {

	@BindView(R.id.recyclerview)
	RecyclerView mRecyclerView;
	@BindView(R.id.my_gold_num)
	TextView mMyGoldNum;
	@BindView(R.id.gold_info_3)
	TextView mGoldInfo3;


	private static final int SDK_PAY_FLAG = 1;

	private int BUY_GOLD = 2;
	private MyGoldAdapter mAdapter;

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
						ToastUtil.showMessage("支付成功");
						new GetPayResultTask().request();
					} else {
						// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
						ToastUtil.showMessage("支付失败");
					}
					break;
				}
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_gold);
		ButterKnife.bind(this);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.my_gold);
		}
		setupView();
		setupEvent();
		setupData();
	}

	private void setupView() {
		LinearLayoutManager manager = new LinearLayoutManager(this);
		manager.setOrientation(LinearLayout.VERTICAL);
		mRecyclerView.setLayoutManager(manager);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.addItemDecoration(new DividerItemDecoration(
				this, LinearLayoutManager.VERTICAL, DensityUtil
				.dip2px(this, 12), DensityUtil.dip2px(this, 12)));
	}

	private void setupEvent() {
		EventBus.getDefault().register(this);
	}

	private void setupData() {
		if (AppManager.getClientUser().isShowVip) {
			mGoldInfo3.setVisibility(View.VISIBLE);
		} else {
			mGoldInfo3.setVisibility(View.GONE);
		}
		mMyGoldNum.setText(String.format(getResources().getString(R.string.my_gold_num), AppManager.getClientUser().gold_num));
		new GetGoldListTask().request(BUY_GOLD);
	}

	/**
	 * 请求金币商品列表
	 */
	class GetGoldListTask extends GetMemberBuyListRequest {
		@Override
		public void onPostExecute(List<MemberBuy> memberBuys) {
			mAdapter = new MyGoldAdapter(memberBuys, MyGoldActivity.this);
			mAdapter.setOnItemClickListener(mOnItemClickListener);
			mRecyclerView.setAdapter(mAdapter);
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}


	private MyGoldAdapter.OnItemClickListener mOnItemClickListener = new MyGoldAdapter.OnItemClickListener() {
		@Override
		public void onItemClick(View view, int position) {
			MemberBuy memberBuy = mAdapter.getItem(position);
			showPayDialog(memberBuy);
		}
	};

	private void showPayDialog(final MemberBuy memberBuy) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.pay_type));
		builder.setNegativeButton(getResources().getString(R.string.cancel),
				null);
		builder.setItems(
				new String[]{getResources().getString(R.string.ali_pay),
						getResources().getString(R.string.weixin_pay)},
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

	class CreateOrderTask extends CreateOrderRequest {
		@Override
		public void onPostExecute(WeChatPay weChatPay) {
			PayReq payReq = new PayReq();
			payReq.appId = AppConstants.WEIXIN_ID;
			payReq.partnerId = weChatPay.mch_id;
			payReq.prepayId = weChatPay.prepay_id;
			payReq.packageValue = "Sign=WXPay";
			payReq.nonceStr = weChatPay.nonce_str;
			payReq.timeStamp = weChatPay.timeStamp;
			payReq.sign = weChatPay.appSign;
			CSApplication.api.sendReq(payReq);
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
				PayTask alipay = new PayTask(MyGoldActivity.this);
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

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void paySuccess(PayEvent event) {
		new GetPayResultTask().request();
	}

	class GetPayResultTask extends GetPayResultRequest {
		@Override
		public void onPostExecute(UserVipModel userVipModel) {
			AppManager.getClientUser().is_vip = userVipModel.isVip;
			AppManager.getClientUser().is_download_vip = userVipModel.isDownloadVip;
			AppManager.getClientUser().gold_num = userVipModel.goldNum;
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

    /*class UpdateGoldTask extends UpdateGoldRequest {
		@Override
        public void onPostExecute(Integer s) {
            mMyGoldNum.setText(String.format(getResources().getString(R.string.my_gold_num), s));
            AppManager.getClientUser().gold_num = s;
        }

        @Override
        public void onErrorExecute(String error) {
            ToastUtil.showMessage(error);
        }
    }*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0 && requestCode == SDK_PAY_FLAG) {
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
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
