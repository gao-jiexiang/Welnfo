package com.example.welnfo.presenter;

import com.example.welnfo.base.BasePresenter;
import com.example.welnfo.model.DiscoveryModel;
import com.example.welnfo.net.ResultCallBack;
import com.example.welnfo.util.LogUtil;
import com.example.welnfo.util.ThreadManager;
import com.example.welnfo.view.DiscoveryView;
import com.example.welnfo.view.LoginView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

public class DiscoveryPresenter extends BasePresenter<DiscoveryView> {

    private DiscoveryModel discoveryModel;

    @Override
    protected void initModel() {
        discoveryModel = new DiscoveryModel();
        addModel(discoveryModel);
    }

    public void addFriend(String friendName) {
        discoveryModel.addFriend(friendName, new ResultCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                mView.showToast(s);
            }

            @Override
            public void onFail(String msg) {
                mView.showToast(msg);
            }
        });
    }

    public void createGroup(final String name) {
        ThreadManager.getInstance()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 创建群组
                         * @param groupName 群组名称
                         * @param desc 群组简介
                         * @param allMembers 群组初始成员，如果只有自己传空数组即可
                         * @param reason 邀请成员加入的reason
                         * @param option 群组类型选项，可以设置群组最大用户数(默认200)及群组类型@see {@link EMGroupStyle}
                         *               option.inviteNeedConfirm表示邀请对方进群是否需要对方同意，默认是需要用户同意才能加群的。
                         *               option.extField创建群时可以为群组设定扩展字段，方便个性化订制。
                         * @return 创建好的group
                         * @throws HyphenateException
                         */
                        /**
                         * EMGroupStylePrivateOnlyOwnerInvite——私有群，只有群主可以邀请人；
                         * EMGroupStylePrivateMemberCanInvite——私有群，群成员也能邀请人进群；
                         * EMGroupStylePublicJoinNeedApproval——公开群，加入此群除了群主邀请，只能通过申请加入此群；
                         * EMGroupStylePublicOpenJoin ——公开群，任何人都能加入此群。
                         */
                        EMGroupOptions option = new EMGroupOptions();
                        option.maxUsers = 200;
                        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;

                        try {
                            EMGroup group = EMClient.getInstance().groupManager().createGroup(name,
                                    "随便群", new String[]{}, "", option);

                            LogUtil.print("id:"+group.getGroupId());
                            mView.showToast("群组创建成功");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            mView.showToast("群组创建失败");
                            LogUtil.print(e.getMessage());
                        }
                    }
                });
    }

    public void joinGroup(final String id) {
        ThreadManager.getInstance()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        //如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
                        try {
                            EMClient.getInstance().groupManager().joinGroup(id);//需异步处理
                            mView.showToast("加入成功");
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            mView.showToast("加入失败");
                            LogUtil.print(e.getMessage());
                        }
                    }
                });
    }

    //退出登录
    public void logout() {
        ThreadManager.getInstance()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        //退出登录
                        EMClient.getInstance().logout(true);
                        //清除用户资料
                        deleteUserData();
                        mView.logoutSuccess();
                    }
                });
    }

    private void deleteUserData() {

    }
}
