package com.ktr.newsapp.bean.newsBean;

/**
 * Created by kisstherain on 2016/1/9.
 */
public class NewsChannelBean {

    private int showapi_res_code;

    private String showapi_res_error;

    private ChannelResBody showapi_res_body;

    private int ret_code;

    private int totalNum;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

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

    public ChannelResBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ChannelResBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }
}
