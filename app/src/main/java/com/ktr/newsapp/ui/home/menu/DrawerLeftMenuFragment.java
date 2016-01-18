package com.ktr.newsapp.ui.home.menu;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.ktr.ktrsupportlibrary.adapter.BaseAdapterHelper;
import com.ktr.ktrsupportlibrary.adapter.QuickAdapter;
import com.ktr.ktrsupportlibrary.utils.ViewFinder;
import com.ktr.newsapp.R;
import com.ktr.newsapp.ui.abstractui.AbstractFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kisstherain on 2015/7/12.
 * 左侧菜单
 */
public class DrawerLeftMenuFragment extends AbstractFragment {

    ViewFinder viewFinder;
    ListView drawer_listView;
    List<String> datas = new ArrayList<>();

    public static DrawerLeftMenuFragment newInstance(String param1, String param2) {
        DrawerLeftMenuFragment fragment = new DrawerLeftMenuFragment();
        return fragment;
    }

    @Override
    protected int inflateContentView() {
        return R.layout.drawer_menu_layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewFinder = new ViewFinder(view);

        initView();
    }

    private void initView() {

        Resources res =getResources();
        String[] homeTitleArry = res.getStringArray(R.array.home_title_arry);

        for (String title : homeTitleArry){

            datas.add(title);
        }

        drawer_listView = viewFinder.find(R.id.drawer_listView);

        drawer_listView.setAdapter(new QuickAdapter<String>(getActivity(), R.layout.ktr_listview_item_layout, datas) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {

                switch (item){
                    case "首页":
                        helper.setImageResource(R.id.left_item_icon_imageView, R.mipmap.ic_launcher);
                        break;
                    case "店铺":
                        helper.setImageResource(R.id.left_item_icon_imageView, R.mipmap.ic_launcher);
                        break;
                    case "发现":
                        helper.setImageResource(R.id.left_item_icon_imageView, R.mipmap.ic_launcher);
                        break;
                    case "我的":
                        helper.setImageResource(R.id.left_item_icon_imageView, R.mipmap.ic_launcher);
                        break;
                    case "FUN":
                        helper.setImageResource(R.id.left_item_icon_imageView, R.mipmap.ic_launcher);
                        break;
                    case "微信Hot":
                        helper.setImageResource(R.id.left_item_icon_imageView, R.mipmap.ic_launcher);
                        break;
                }
                helper.setText(R.id.title_textView, item);

            }
        });

        drawer_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(onLeftMenuItemClickListener != null) onLeftMenuItemClickListener.onMenuItemClick((String) parent.getAdapter().getItem(position));
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onLeftMenuItemClickListener = (OnLeftMenuItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLeftMenuItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onLeftMenuItemClickListener = null;
    }

    OnLeftMenuItemClickListener onLeftMenuItemClickListener;

    public interface OnLeftMenuItemClickListener{

        void onMenuItemClick(String title);
    }
}
