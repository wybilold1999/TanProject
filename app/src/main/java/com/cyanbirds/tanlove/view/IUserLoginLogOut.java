package com.cyanbirds.tanlove.view;

import com.cyanbirds.tanlove.activity.base.IBasePresenter;
import com.cyanbirds.tanlove.activity.base.IBaseView;
import com.cyanbirds.tanlove.entity.ClientUser;

public interface IUserLoginLogOut {

    interface View extends IBaseView<Presenter> {
        void loginLogOutSuccess(ClientUser clientUser);
    }

    interface Presenter extends IBasePresenter {
        void onUserLogin(String account, String pwd, String city);
        void onWXLogin(String code, String channelId, String city);
        void onQQLogin(String token, String openId, String channelId, String city);
        void onRegist(ClientUser clientUser, String channel);
        void onLogOut();
    }

    interface CheckSmsCodeView extends IBaseView<CheckSmsCodePresenter> {
        void checkSmsCode(int checkCode);
    }

    interface CheckSmsCodePresenter extends IBasePresenter {
        void checkSmsCode(String code, String phoneNum, int mPhoneType);
    }
}
