package com.atguigu.mobileplay2.domain;

/**
 * Created by My on 2017/5/19.
 */

public class MediaItem {
    private String name;
    private long duration;
    private long size;
    private String data;

    @Override
    public String toString() {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", data='" + data + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public MediaItem(String name, long duration, long size, String data) {

        this.name = name;
        this.duration = duration;
        this.size = size;
        this.data = data;
    }
}
