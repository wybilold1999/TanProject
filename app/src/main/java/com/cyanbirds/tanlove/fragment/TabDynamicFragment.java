package com.cyanbirds.tanlove.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.adapter.TabDynamicAdapter;
import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.config.ValueKey;
import com.cyanbirds.tanlove.entity.DynamicContent;
import com.cyanbirds.tanlove.eventtype.PubDycEvent;
import com.cyanbirds.tanlove.listener.NestedScrollViewListener;
import com.cyanbirds.tanlove.net.request.GetDynamicListRequest;
import com.cyanbirds.tanlove.ui.widget.WrapperLinearLayoutManager;
import com.cyanbirds.tanlove.utils.RxBus;
import com.cyanbirds.tanlove.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * @author: wangyb
 * @datetime: 2016-01-02 16:32 GMT+8
 * @email: 395044952@qq.com
 * @description:
 */
public class TabDynamicFragment extends Fragment {
	@BindView(R.id.recyclerview)
	RecyclerView mRecyclerview;
	@BindView(R.id.tv_tab_content)
	TextView mTvTabContent;
	@BindView(R.id.scrollView)
	NestedScrollView mScrollView;
	private View rootView;

	private List<DynamicContent.DataBean> mAllData;
	private TabDynamicAdapter mAdapter;
	private Gson gson = new Gson();
	private LinearLayoutManager layoutManager;
	private String curUserId;
	private int pageNo = 1;
	private int pageSize = 50;

	private int mServerDynamicCount = 0;

	private Observable<PubDycEvent> observable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.tab_item_dynamic, null);
			ButterKnife.bind(this, rootView);
			setupViews();
			setupData();
			rxBusSub();
			setHasOptionsMenu(true);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void setupViews() {
		layoutManager = new WrapperLinearLayoutManager(
				getActivity(), LinearLayoutManager.VERTICAL, false);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerview.setLayoutManager(layoutManager);
		mRecyclerview.setItemAnimator(new DefaultItemAnimator());
		mRecyclerview.setNestedScrollingEnabled(false);
	}

	/**
	 * rx订阅
	 */
	private void rxBusSub() {
		observable = RxBus.getInstance().register(AppConstants.PUB_DYNAMIC);
		observable.subscribe(pubDycEvent -> updatePublishedDynamic(pubDycEvent));
	}

	private void setupData() {
		if (getArguments() == null) {
			mTvTabContent.setVisibility(View.VISIBLE);
			mRecyclerview.setVisibility(View.GONE);
		} else {
			curUserId = getArguments().getString(ValueKey.USER_ID);
		}
		mAllData = new ArrayList<>();
		mAdapter = new TabDynamicAdapter(getActivity(), mAllData);
		mRecyclerview.setAdapter(mAdapter);
		mScrollView.setOnScrollChangeListener(new NestedScrollViewListener(layoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (totalItemsCount < mServerDynamicCount) {
					new GetDynamicListTask().request(curUserId, ++pageNo, pageSize);
				}
			}
		});
		new GetDynamicListTask().request(curUserId, pageNo, pageSize);
	}

	/**
	 * 获取动态
	 */
	class GetDynamicListTask extends GetDynamicListRequest {
		@Override
		public void onPostExecute(String data) {
			DynamicContent content = gson.fromJson(data, DynamicContent.class);
			if (content != null && content.getData() != null && !content.getData().isEmpty()) {
				curUserId = String.valueOf(content.getData().get(0).getUsersId());
				mServerDynamicCount = content.getData().get(0).getCount();
				mAllData.addAll(content.getData());
				mAdapter.setData(mAllData);
				mAdapter.notifyDataSetChanged();
				mTvTabContent.setVisibility(View.GONE);
				mRecyclerview.setVisibility(View.VISIBLE);
			} else {
				mTvTabContent.setVisibility(View.VISIBLE);
				mRecyclerview.setVisibility(View.GONE);
			}
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void updatePublishedDynamic(PubDycEvent pubDycEvent) {
		DynamicContent.DataBean dataBean = new DynamicContent.DataBean();
		JsonObject obj = new JsonParser().parse(pubDycEvent.dynamicContent).getAsJsonObject();
		JsonObject data = obj.get("data").getAsJsonObject();
		dataBean.setContent(data.get("content").getAsString());
		dataBean.setFaceUrl(data.get("faceUrl").getAsString());
		dataBean.setNickname(data.get("nickname").getAsString());
		dataBean.setCreateTime(data.get("createTime").getAsString());
		dataBean.setUsersId(data.get("usersId").getAsInt());
		Object o = data.get("pictures");
		if (!(o instanceof JsonNull)) {
			Type listType = new TypeToken<ArrayList<DynamicContent.DataBean.PicturesBean>>() {
			}.getType();
			ArrayList<DynamicContent.DataBean.PicturesBean> picturesBeen = gson.fromJson(
					data.get("pictures").getAsJsonArray(), listType);
			dataBean.setPictures(picturesBeen);
		}
		mTvTabContent.setVisibility(View.GONE);
		mRecyclerview.setVisibility(View.VISIBLE);
		mAllData.add(0, dataBean);
		mAdapter.notifyItemInserted(0);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

}
