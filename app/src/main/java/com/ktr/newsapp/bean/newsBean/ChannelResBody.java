package com.ktr.newsapp.bean.newsBean;

import java.util.List;

/**
 * Created by kisstherain on 2016/1/9.
 */
public class ChannelResBody {

    private List<ChannelList> channelList;

    public List<ChannelList> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<ChannelList> channelList) {
        this.channelList = channelList;
    }

    @Override
    public String toString() {
        return "ChannelResBody{" +
                "channelList=" + channelList +
                '}';
    }
}
