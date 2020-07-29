package com.example.welnfo.ui.fragment;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.welnfo.R;
import com.example.welnfo.base.BaseFragment;
import com.example.welnfo.presenter.DiscoveryPresenter;
import com.example.welnfo.ui.activity.ItInfoActivity;
import com.example.welnfo.ui.activity.LoginActivity;
import com.example.welnfo.ui.activity.ZhihuActivity;
import com.example.welnfo.view.DiscoveryView;
import com.hyphenate.easeui.ui.MapActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class DiscoveryFragment extends BaseFragment<DiscoveryPresenter>
        implements DiscoveryView {
    @BindView(R.id.et_friend)
    EditText etFriend;
    @BindView(R.id.iv_add_friend)
    ImageView ivAddFriend;
    @BindView(R.id.cl_add)
    ConstraintLayout clAdd;
    @BindView(R.id.et_create_group)
    EditText etCreateGroup;
    @BindView(R.id.iv_create_group)
    ImageView ivCreateGroup;
    @BindView(R.id.cl_create_group)
    ConstraintLayout clCreateGroup;
    @BindView(R.id.et_group)
    EditText etGroup;
    @BindView(R.id.iv_add_group)
    ImageView ivAddGroup;
    @BindView(R.id.cl_add_group)
    ConstraintLayout clAddGroup;
    @BindView(R.id.iv_zhihu)
    ImageView ivZhihu;
    @BindView(R.id.tv_zhihu)
    TextView tvZhihu;
    @BindView(R.id.cl_zhihu)
    ConstraintLayout clZhihu;
    @BindView(R.id.iv_it)
    ImageView ivIt;
    @BindView(R.id.tv_it)
    TextView tvIt;
    @BindView(R.id.cl_it_info)
    ConstraintLayout clItInfo;
    @BindView(R.id.iv_tencent)
    ImageView ivTencent;
    @BindView(R.id.tv_tencent)
    TextView tvTencent;
    @BindView(R.id.cl_tencent)
    ConstraintLayout clTencent;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    @Override
    protected void initView(View inflate) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected DiscoveryPresenter initPresenter() {
        return new DiscoveryPresenter();
    }

    @OnClick({R.id.iv_add_friend, R.id.iv_create_group, R.id.iv_add_group, R.id.cl_zhihu, R.id.cl_it_info, R.id.cl_tencent, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_friend:
                addFrinds();
                break;
            case R.id.iv_create_group:
                createGroup();
                break;
            case R.id.iv_add_group:
                joinGroup();
                break;
            case R.id.cl_zhihu:
                ZhihuActivity.startAct(getContext());
                break;
            case R.id.cl_it_info:
                itinfo();
                break;
            case R.id.cl_tencent:
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void itinfo() {
        ItInfoActivity.startAct(getContext());
    }

    private void logout() {
        mPresenter.logout();
    }

    private void joinGroup() {
        String id = etGroup.getText().toString().trim();
        mPresenter.joinGroup(id);
    }

    //创建群
    private void createGroup() {
        String name = etCreateGroup.getText().toString().trim();
        mPresenter.createGroup(name);
    }

    private void addFrinds() {
        String friendName = etFriend.getText().toString().trim();
        if (TextUtils.isEmpty(friendName)){
            showToast("请输入好友id");
            return;
        }
        mPresenter.addFriend(friendName);
    }

    @Override
    public void logoutSuccess() {
        showToast("退出登录成功");
        getActivity().finish();
        LoginActivity.starAct(getContext());
    }
}
