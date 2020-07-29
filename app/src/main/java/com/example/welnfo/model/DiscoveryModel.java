package com.example.welnfo.model;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseApp;
import com.example.welnfo.base.BaseModel;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.util.ThreadManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class DiscoveryModel extends BaseModel {
    public void addFriend(final String friendName, final ResultCallBack<String> callBack) {
        ThreadManager.getInstance()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        //参数为要添加的好友的username和添加理由
                        try {
                            EMClient.getInstance()
                                    .contactManager()
                                    .addContact(friendName, "");

                            callBack.onSuccess(
                                    BaseApp.getRes().
                                            getString(R.string.add_succees));
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            callBack.onFail(e.getMessage());
                        }
                    }
                });
    }
}
