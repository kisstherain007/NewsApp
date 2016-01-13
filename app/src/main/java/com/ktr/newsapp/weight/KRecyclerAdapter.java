package com.ktr.newsapp.weight;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ktr.ktrsupportlibrary.autoScrollViewPager.AutoScrollViewPager;
import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;
import com.ktr.ktrsupportlibrary.pagerAdapter.ImagePagerAdapter;
import com.ktr.newsapp.R;
import com.ktr.newsapp.bean.newsBean.ContentlistBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kisstherain on 2015/10/11.
 */
public class KRecyclerAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    public static final String TAG = KRecyclerAdapter.class.getSimpleName();

    public static final int titleType = 0;

    Context mContext;

    LayoutInflater mLayoutInflater;

    List<ContentlistBean> contentlist;

    public KRecyclerAdapter(Context context){

        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void refreshAdapter(List<ContentlistBean> datas){

        this.contentlist = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return contentlist == null ? 0 : contentlist.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = null;
        RecyclerView.ViewHolder viewHolder = null;

        switch (i){
            case titleType:
                itemView = mLayoutInflater.inflate(R.layout.news_remmond_title_item, viewGroup, false);
                viewHolder = new RemmondTitleKViewHolder(itemView);
                break;
            default:
                itemView = mLayoutInflater.inflate(R.layout.news_item_layout, viewGroup, false);
                viewHolder = new KViewHolder(itemView);
                viewHolder.itemView.setOnClickListener(this);
                break;
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        switch (i){
            case titleType:
                RemmondTitleKViewHolder remmondTitleKViewHolder = (RemmondTitleKViewHolder) holder;
                initRecomnd(remmondTitleKViewHolder.autoScrollViewPager);
                break;
            default:
                ContentlistBean contentlistBean = contentlist.get(i);
                KViewHolder kViewHolder = (KViewHolder) holder;
                kViewHolder.itemView.setTag(contentlistBean);
                kViewHolder.titleTextView.setText(contentlistBean.getTitle());
                kViewHolder.contextTextView.setText(contentlistBean.getDesc());
                kViewHolder.date_textView.setText(contentlistBean.getPubDate());
                if (!contentlistBean.getImageurls().isEmpty()){

                    kViewHolder.contentImage.setVisibility(View.VISIBLE);

                    Log.i("imageUrl", contentlistBean.getImageurls().get(0).getUrl());
//                    Uri uri = Uri.parse(contentlistBean.getImageurls().get(0).getUrl());
                    BitmapLoader.getInstance().display(kViewHolder.contentImage, contentlistBean.getImageurls().get(0).getUrl());
//                    DraweeController draweeController1 = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build();
//                    kViewHolder.contentImage.setController(draweeController1);
                }else{

                    kViewHolder.contentImage.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {

        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,v.getTag());
        }
    }

     class KViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView contextTextView;
        TextView date_textView;

         ImageView contentImage;

        public KViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.title_textView);
            contextTextView = (TextView) itemView.findViewById(R.id.content_textView);
            date_textView = (TextView) itemView.findViewById(R.id.date_textView);
            contentImage = (ImageView) itemView.findViewById(R.id.content_img);
        }
    }

    class RemmondTitleKViewHolder extends RecyclerView.ViewHolder{

        AutoScrollViewPager autoScrollViewPager;

        public RemmondTitleKViewHolder(View itemView) {
            super(itemView);

            autoScrollViewPager = (AutoScrollViewPager) itemView.findViewById(R.id.view_pager);
        }
    }

    void initRecomnd(AutoScrollViewPager titleViewPager) {
        List<Integer> imageIdList = new ArrayList<Integer>();
        imageIdList.add(R.mipmap.banner3);
        imageIdList.add(R.mipmap.banner3);
        imageIdList.add(R.mipmap.banner3);
        imageIdList.add(R.mipmap.banner3);
        titleViewPager.setAdapter(new ImagePagerAdapter(mContext, imageIdList).setInfiniteLoop(true));
        titleViewPager.setInterval(2000);
        titleViewPager.startAutoScroll();
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {

        void onItemClick(View view , Object t);
    }
}
