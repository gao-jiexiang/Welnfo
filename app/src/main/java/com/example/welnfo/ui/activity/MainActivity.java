package com.example.welnfo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseActivity;
import com.example.welnfo.base.BaseApp;
import com.example.welnfo.presenter.MainPresenter;
import com.example.welnfo.ui.fragment.DiscoveryFragment;
import com.example.welnfo.view.MainView;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    @BindView(R.id.fl_container)
    FrameLayout mFl;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private ArrayList<String> mTitles;
    private ArrayList<Fragment> mFragments;
    private ArrayList<Integer> mTabImages;
    private FragmentManager mManager;
    private int mLastFragmentPosition = 0;

    /**
     * 启动activity全部通过这个方法去启动,在这个方法里面明确启动这个页面需要的参数
     *
     * @param context
     */
    public static void startAct(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initData() {
        mPresenter.getContactList();
    }

    @Override
    protected void initView() {
        mManager = getSupportFragmentManager();
        initTitle();
        initTabImages();
        initFragments();
        initTabs();
        //一上来显示会话fragment
        initConversionFragment();
        //tab选中监听
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tab选中
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tab取消选中
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //tab的重复选中
                switchFragment(tab.getPosition());

            }
        });
    }

    private void initConversionFragment() {
        mManager.beginTransaction()
                .add(R.id.fl_container,mFragments.get(0))
                .commit();
    }

    //切换fragment
    private void switchFragment(int position) {
        FragmentTransaction transaction = mManager.beginTransaction();
        //有显示的fragment,也有需要隐藏的fragment
        Fragment showFragment = mFragments.get(position);

        //隐藏的fragment
        Fragment hideFragment = mFragments.get(mLastFragmentPosition);

        if (!showFragment.isAdded()){
            transaction.add(R.id.fl_container,showFragment);
        }

        transaction.hide(hideFragment);
        transaction.show(showFragment);

        transaction.commit();

        //这次显示的fragment就是下次点击tab要隐藏的fragment
        mLastFragmentPosition = position;

    }

    private void initTabImages() {
        mTabImages = new ArrayList<>();
        mTabImages.add(R.drawable.em_tab_chat_bg);
        mTabImages.add(R.drawable.em_tab_contact_list_bg);
        mTabImages.add(R.drawable.em_tab_setting_bg);
    }

    private void initTabs() {
        //如果tab仅仅是只有文字,那么这么就行了
        //mTabLayout.addTab(mTabLayout.newTab().setText());
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTab(0)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTab(1)));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTab(2)));
    }

    private View getTab(int index) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.tab, null);
        ImageView iv = inflate.findViewById(R.id.iv);
        TextView tv = inflate.findViewById(R.id.tv);

        tv.setText(mTitles.get(index));
        iv.setImageResource(mTabImages.get(index));
        return inflate;
    }


    private void initFragments() {
        mFragments = new ArrayList<>();
        //会话的fragment
        EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
        mFragments.add(conversationListFragment);
        //联系人的fragmnet
        EaseContactListFragment contactListFragment = new EaseContactListFragment();
        mFragments.add(contactListFragment);
        DiscoveryFragment mDiscoveryFragment = new DiscoveryFragment();
        mFragments.add(mDiscoveryFragment);
        //设置联系人
        //contactListFragment.setContactsMap(getContacts());
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                EMConversation.EMConversationType type = conversation.getType();


                //判断是群聊的会话还是单聊的会话,然后去传递聊天类型
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                if (conversation.isGroup()) {
                    //群聊,需要设置聊天类型和群组id
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);

                } else {
                    //单聊,
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                }

                startActivity(intent);
            }
        });
        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {

            @Override
            public void onListItemClicked(EaseUser user) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                if (user.getChatType() == EaseConstant.CHATTYPE_SINGLE){
                    //默认单聊
                    //单聊,
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, user.getChatType());
                }else if (user.getChatType() ==  EaseConstant.CHATTYPE_GROUP){
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getGroupId());
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, user.getChatType());
                }
                startActivity(intent);
            }
        });
    }

    private void initTitle() {
        mTitles = new ArrayList<>();
        mTitles.add(BaseApp.getRes().getString(R.string.conversion));
        mTitles.add(BaseApp.getRes().getString(R.string.contacts));
        mTitles.add(BaseApp.getRes().getString(R.string.discovery));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void setContactList(final Map<String, EaseUser> contacts) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((EaseContactListFragment) mFragments.get(1)).setContactsMap(contacts);
            }
        });
    }
}

