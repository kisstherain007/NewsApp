package com.ktr.newsapp.bean.newsBean;

/**
 * Created by kisstherain on 2016/1/10.
 */
public class NewsDetailBody {

    private NewsDetialPagebean pagebean;

    private int ret_code;

    public NewsDetialPagebean getPagebean() {
        return pagebean;
    }

    public void setPagebean(NewsDetialPagebean pagebean) {
        this.pagebean = pagebean;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }
}
