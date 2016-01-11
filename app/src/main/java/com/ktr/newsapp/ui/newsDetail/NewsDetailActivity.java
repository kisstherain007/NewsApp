package com.ktr.newsapp.ui.newsDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ktr.newsapp.R;
import com.ktr.newsapp.ui.abstractui.AbstractActivity;

/**
 * Created by kisstherain on 2016/1/11.
 */
public class NewsDetailActivity extends AbstractActivity {

    WebView webView;

    public static void startActivity(Context context, String linkUrl) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("linkUrl", linkUrl);
        context.startActivity(intent);
    }

    protected String getLinkUrl() {
        return getIntent().getStringExtra("linkUrl");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_layout);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(getLinkUrl());

    }


}
