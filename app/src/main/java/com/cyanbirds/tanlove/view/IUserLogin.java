package com.cyanbirds.tanlove.view;

import com.cyanbirds.tanlove.activity.base.IBasePresenter;
import com.cyanbirds.tanlove.activity.base.IBaseView;
import com.cyanbirds.tanlove.entity.ClientUser;

public interface IUserLogin {

    interface View extends IBaseView<Presenter> {
        void loginSuccess(ClientUser clientUser);
    }

    interface Presenter extends IBasePresenter {
        void onUserLogin(String account, String pwd, String city);
        void onWXLogin(String code, String channelId, String city);
        void onQQLogin(String token, String openId, String channelId, String city);
    }
}
