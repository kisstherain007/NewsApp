package com.ktr.newsapp.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by kisstherain on 2016/1/5.
 */
public class WxHot {

    private int code;

    private String msg;

    private List<Map<String, WxHotChild>> hotChilds;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Map<String, WxHotChild>> getHotChilds() {
        return hotChilds;
    }

    public void setHotChilds(List<Map<String, WxHotChild>> hotChilds) {
        this.hotChilds = hotChilds;
    }
}
