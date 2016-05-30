package com.ktr.newsapp.weight;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktr.ktrsupportlibrary.autoScrollViewPager.AutoScrollViewPager;
import com.ktr.ktrsupportlibrary.bitmaploader.BitmapLoader;
import com.ktr.ktrsupportlibrary.bitmaploader.config.ImageConfig;
import com.ktr.ktrsupportlibrary.pagerAdapter.ImagePagerAdapter;
import com.ktr.newsapp.R;
import com.ktr.newsapp.bean.newsBean.ContentlistBean;
import com.ktr.newsapp.ui.news.ARecylclerReleaseFragment;

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

//    List<ContentlistBean> contentlist;

    private SortedList<ContentlistBean> contentlistBeanSortedList;

    ARecylclerReleaseFragment aReleaseFragment;

    public KRecyclerAdapter(Context context, ARecylclerReleaseFragment aReleaseFragment){

        this.mContext = context;
        this.aReleaseFragment = aReleaseFragment;
        mLayoutInflater = LayoutInflater.from(mContext);

        contentlistBeanSortedList = new SortedList<ContentlistBean>(ContentlistBean.class, new SortedList.Callback<ContentlistBean>() {
            @Override
            public int compare(ContentlistBean t0, ContentlistBean t1) {

//                // 实现这个方法来定义Item的显示顺序
//                int txtComp = t0.getTitle().compareTo(t1.getTitle());
//                if (txtComp != 0) {
//                    return txtComp;
//                }
//                if (t0.id < t1.id) {
//                    return -1;
//                } else if (t0.id > t1.id) {
//                    return 1;
//                }
                return 0;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(ContentlistBean oldItem, ContentlistBean newItem) {
                return oldItem.getTitle().equals(newItem.getTitle());// 比较两个Item的内容是否一致，如不一致则会调用adapter的notifyItemChanged()
            }

            @Override
            public boolean areItemsTheSame(ContentlistBean item1, ContentlistBean item2) {
                return false;
            }
        });
    }

    public void addItems(List<ContentlistBean> items){

        contentlistBeanSortedList.beginBatchedUpdates();  // 开始批量更新
        contentlistBeanSortedList.addAll(items);          // 更新一批数据
        contentlistBeanSortedList.endBatchedUpdates();    // 结束更新
    }

//    void deleteItems(List<Item> items){
//        mData.beginBatchedUpdates();  // 开始批量更新
//        for(Item item : items){       // 删除一批数据
//            mData.remove(item);
//        }
//        mData.endBatchedUpdates();    // 结束更新
//    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return contentlistBeanSortedList == null ? 0 : contentlistBeanSortedList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        Log.d(TAG, "onCreateViewHolder" + i);

        View itemView = null;
        RecyclerView.ViewHolder viewHolder = null;

        switch (i){
//            case titleType:
//                itemView = mLayoutInflater.inflate(R.layout.news_remmond_title_item, viewGroup, false);
//                viewHolder = new RemmondTitleKViewHolder(itemView);
//                break;
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

        Log.d(TAG, "onBindViewHolder" + i);

        switch (i){
//            case titleType:
//                RemmondTitleKViewHolder remmondTitleKViewHolder = (RemmondTitleKViewHolder) holder;
//                initRecomnd(remmondTitleKViewHolder.autoScrollViewPager);
//                break;
            default:
                ContentlistBean contentlistBean = contentlistBeanSortedList.get(i);
                KViewHolder kViewHolder = (KViewHolder) holder;
                kViewHolder.itemView.setTag(contentlistBean);
                kViewHolder.titleTextView.setText(contentlistBean.getTitle());
                kViewHolder.contextTextView.setText(contentlistBean.getDesc());
                kViewHolder.date_textView.setText(contentlistBean.getPubDate());
                if (!contentlistBean.getImageurls().isEmpty()){

//                    kViewHolder.contentImage.setVisibility(View.VISIBLE);

                    Log.i("imageUrl", contentlistBean.getImageurls().get(0).getUrl());
//                    Uri uri = Uri.parse(contentlistBean.getImageurls().get(0).getUrl());
                    ImageConfig imageConfig = new ImageConfig();
                    imageConfig.setLoadingRes(R.mipmap.ic_launcher);
//                    BitmapLoader.getInstance().display(aReleaseFragment, kViewHolder.contentImage, contentlistBean.getImageurls().get(0).getUrl(), imageConfig);

                    kViewHolder.displayPicsView.setPics(aReleaseFragment, contentlistBean.getImageurls());
//                    DraweeController draweeController1 = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build();
//                    kViewHolder.contentImage.setController(draweeController1);
                }else{

//                    kViewHolder.contentImage.setVisibility(View.GONE);
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

    public class KViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView contextTextView;
        TextView date_textView;

         ImageView contentImage;

        DisplayPicsView displayPicsView;

        public KViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.title_textView);
            contextTextView = (TextView) itemView.findViewById(R.id.content_textView);
            date_textView = (TextView) itemView.findViewById(R.id.date_textView);
            contentImage = (ImageView) itemView.findViewById(R.id.content_img);
            displayPicsView = (DisplayPicsView) itemView.findViewById(R.id.display_view);
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
