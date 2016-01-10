package com.ktr.newsapp.bean.newsBean;

import java.io.Serializable;

/**
 * Created by kisstherain on 2016/1/9.
 */
public class ChannelList{

    private String channelId;

    private String name;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ChannelList{" +
                "channelId='" + channelId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
