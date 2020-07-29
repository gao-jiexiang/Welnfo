package com.example.welnfo.view;


import com.example.welnfo.base.BaseView;
import com.example.welnfo.bean.ITbean;
import com.example.welnfo.bean.ItInfoArticle;

import java.util.List;

public interface ITInfoItemView extends BaseView {

    void setData(List<ITbean.ResultsBean> results);
}
