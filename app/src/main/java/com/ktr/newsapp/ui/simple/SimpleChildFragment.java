package com.ktr.newsapp.ui.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ktr.ktrsupportlibrary.adapter.BaseAdapterHelper;
import com.ktr.ktrsupportlibrary.adapter.QuickAdapter;
import com.ktr.newsapp.R;
import com.ktr.newsapp.ui.abstractui.AbstractFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kisstherain on 2016/1/26.
 */
public class SimpleChildFragment extends Fragment {

    ListView simple_listView;

    int index;

    public static SimpleChildFragment newInstance(int index) {
        SimpleChildFragment fragment = new SimpleChildFragment();
        fragment.index = index;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_child_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.test_layout);
//        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
//        layoutParams.height = index * 200;
//        linearLayout.setLayoutParams(layoutParams);
//        linearLayout.setBackgroundColor(getResources().getColor(R.color.red));

        List<String> datas = new ArrayList<>();

        for (int i = 0; i < index; i ++){

            datas.add(i+"");
        }

        simple_listView = (ListView) view.findViewById(R.id.simple_listView);
        simple_listView.setAdapter(new QuickAdapter<String>(getActivity(), R.layout.ktr_listview_item_layout, datas){
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setImageResource(R.id.left_item_icon_imageView, R.mipmap.ic_launcher);
            }
        });
    }
}
