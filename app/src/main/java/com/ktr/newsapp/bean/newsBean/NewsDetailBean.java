package com.ktr.newsapp.bean.newsBean;

/**
 * Created by kisstherain on 2016/1/10.
 */
public class NewsDetailBean {

    private int showapi_res_code;

    private String showapi_res_error;

    private NewsDetailBody showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public NewsDetailBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(NewsDetailBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }
}
