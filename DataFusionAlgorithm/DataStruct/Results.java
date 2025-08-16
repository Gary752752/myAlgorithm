package com.algorithm.DataStruct;

import java.util.ArrayList;

public class Results {
    private int topic;
    private ArrayList<Result> list;
    private String systemName;

    public Results() {
    }

    public Results(int topic, ArrayList<Result> list, String systemName) {
        this.topic = topic;
        this.list = list;
        this.systemName = systemName;
    }

    public Results(int topic, ArrayList<Result> list) {
        this.topic = topic;
        this.list = list;
    }

    /**
     * 自动new一个Result集合
     * @param topic
     */
    public Results(int topic) {
        this.topic = topic;
        this.list = new ArrayList<Result>();
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public ArrayList<Result> getList() {
        return list;
    }

    public void setList(ArrayList<Result> list) {
        this.list = list;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
