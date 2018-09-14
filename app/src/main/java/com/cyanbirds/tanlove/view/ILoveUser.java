package com.cyanbirds.tanlove.view;

import com.cyanbirds.tanlove.activity.base.IBasePresenter;
import com.cyanbirds.tanlove.activity.base.IBaseView;

/**
 * Created by wangyb on 2018/9/14
 */
public interface ILoveUser<R> {

    interface LoveView<R> extends IBaseView<LovePresenter> {
        void loadDataNoMore(R r);
    }

    interface LovePresenter extends IBasePresenter {
        void loadData();
    }
}
