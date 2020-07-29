package com.example.welnfo.view;


import com.example.welnfo.base.BaseView;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.Map;

public interface MainView extends BaseView {
    void setContactList(Map<String, EaseUser> contacts);
}

