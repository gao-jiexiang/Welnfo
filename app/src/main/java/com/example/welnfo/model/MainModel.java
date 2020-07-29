package com.example.welnfo.model;


import android.util.Log;

import com.example.welnfo.base.BaseModel;
import com.example.welnfo.bean.Bean;
import com.example.welnfo.bean.Meizi;
import com.example.welnfo.net.BaseSubscriber;
import com.example.welnfo.net.HttpUtil;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.net.RxUtils;
import com.example.welnfo.util.ThreadManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainModel extends BaseModel {
    //Bean
    public void getData(final ResultCallBack<String> resultCallBack) {
        addDisposable(
                HttpUtil.getInstance()
                        .getApiService()
                        .getMeizi2(3)
                        .compose(RxUtils.<Meizi>rxSchedulerHelper())//切换观察者和被观察者的线程
                        .subscribeWith(new BaseSubscriber<Meizi>() {
                            @Override
                            public void onNext(Meizi meizi) {
                                resultCallBack.onSuccess(meizi.toString());
                            }
                        })
        );
    }


    //BannerBean
    public void  getBanner(ResultCallBack<Bean> resultCallBack){

    }

    //获取联系人+获取群组
    public void getContactList(final ResultCallBack<Map<String, EaseUser>> callBack) {
        ThreadManager.getInstance()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        //获取联系人
                        try {
                            List<String> usernames = EMClient.getInstance().contactManager()
                                    .getAllContactsFromServer();

                            //获取群组
                            //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
                            List<EMGroup> grouplist = EMClient.getInstance().
                                    groupManager().getJoinedGroupsFromServer();//需异步处理


                            Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
                            //添加联系人
                            for(int i = 0; i < usernames.size(); i++){
                                String username = usernames.get(i);
                                EaseUser user = new EaseUser(username);
                                user.setChatType(EaseConstant.CHATTYPE_SINGLE);
                                contacts.put(username,user);
                            }

                            //添加群组
                            for (int i = 0; i < grouplist.size(); i++) {
                                String  name= grouplist.get(i).getGroupName();
                                String  id= grouplist.get(i).getGroupId();
                                EaseUser easeUser = new EaseUser(id);
                                easeUser.setChatType(EaseConstant.CHATTYPE_GROUP);
                                easeUser.setGroupId(id);
                                contacts.put(id,easeUser);
                            }

                            callBack.onSuccess(contacts);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            callBack.onFail(e.toString());
                        }
                    }
                });
    }

}

