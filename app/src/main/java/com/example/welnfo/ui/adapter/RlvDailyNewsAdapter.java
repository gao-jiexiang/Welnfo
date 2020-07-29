package com.example.welnfo.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.welnfo.R;
import com.example.welnfo.bean.DailyNewsBean;
import com.example.welnfo.util.TimeUtil;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RlvDailyNewsAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final ArrayList<DailyNewsBean.TopStoriesBean> mBannerList;
    public final ArrayList<DailyNewsBean.StoriesBean> mNewsList;
    public static final int TYPE_BANNER = 0;
    public static final int TYPE_DATE = 1;
    public static final int TYPE_NEWS = 2;
    private String mDate = "今日新闻";
    private onItemClickListener mOnItemClickListener;


    public RlvDailyNewsAdapter(Context context,
                               ArrayList<DailyNewsBean.TopStoriesBean> bannerList,
                               ArrayList<DailyNewsBean.StoriesBean> newsList) {
        mContext = context;
        mBannerList = bannerList;
        mNewsList = newsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        //viewType:这个是系统帮我们调用getItemViewType之后的返回值
        if (viewType == TYPE_BANNER){
            //parent要给,不要给null,如果给了null,条目水平方向撑不满
            return new BannerVH(inflater.inflate(R.layout.item_banner,parent,false));
        }else if (viewType == TYPE_DATE){
            return new DateVH(inflater.inflate(R.layout.item_date,parent,false));
        }else {
            return new NewsVH(inflater.inflate(R.layout.item_news,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_BANNER){
            BannerVH bannerVH = (BannerVH) holder;
            bannerVH.mBanner.setImages(mBannerList)
                    .setImageLoader(new ImageLoader() {
                        @Override
                        public void displayImage(Context context,
                                                 Object path, ImageView imageView) {
                            //path 类型是mBannerList 的泛型
                            DailyNewsBean.TopStoriesBean bean =
                                    (DailyNewsBean.TopStoriesBean) path;
                            Glide.with(mContext).load(bean.getImage()).into(imageView);
                        }
                    })
                    .start();
        }else if (viewType == TYPE_DATE){
            DateVH dateVH = (DateVH) holder;
            dateVH.mTvDate.setText(mDate);
        }else {
            NewsVH newsVH = (NewsVH) holder;

            //需要求新的索引
            int newPosition = position-1;
            if (mBannerList.size()>0){
                newPosition -= 1;
            }

            DailyNewsBean.StoriesBean newsBean = mNewsList.get(newPosition);

            List<String> images = newsBean.getImages();
            if (images != null && images.size()>0){
                Glide.with(mContext).load(images.get(0)).into(newsVH.mIv);
            }

            newsVH.mTvTitle.setText(newsBean.getTitle());

            final int finalNewPosition = newPosition;
            newsVH.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(v, finalNewPosition);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mBannerList.size()>0){
            //有banner
            if (position == 0){
                return TYPE_BANNER;
            }else if (position == 1){
                return TYPE_DATE;
            }else {
                return TYPE_NEWS;
            }
        }else {
            if (position == 0){
                return TYPE_DATE;
            }else {
                return TYPE_NEWS;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mBannerList.size()>0){
            return mNewsList.size()+1+1;
        }else {
            return mNewsList.size()+1;
        }
    }

    public void setData(DailyNewsBean dailyNewsBean) {
        mDate = dailyNewsBean.getDate();
        if (TimeUtil.checkTimeIsTotay(dailyNewsBean.getDate())){
            mDate = "今日新闻";
        }

        mBannerList.clear();
        mNewsList.clear();
        if (dailyNewsBean.getTop_stories() != null
                && dailyNewsBean.getTop_stories().size()>0){
            mBannerList.addAll(dailyNewsBean.getTop_stories());
        }

        if (dailyNewsBean.getStories() != null
                && dailyNewsBean.getStories().size() >0){
            mNewsList.addAll(dailyNewsBean.getStories());
        }
        notifyDataSetChanged();
    }

    class BannerVH extends RecyclerView.ViewHolder{
        @BindView(R.id.banner)
        Banner mBanner;
        public BannerVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class DateVH extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_date)
        TextView mTvDate;
        public DateVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class NewsVH extends RecyclerView.ViewHolder{
        @BindView(R.id.iv)
        ImageView mIv;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        public NewsVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface onItemClickListener{
        void onItemClick(View v,int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
}
