package com.algorithm.DataStruct;

import java.util.ArrayList;

public class ArrayQrels {
    private int topic;
    private ArrayList<Qrel> list;

    public ArrayQrels() {
    }

    public ArrayQrels(int topic, ArrayList<Qrel> list) {
        this.topic = topic;
        this.list = list;
    }

    public ArrayQrels(int topic) {
        this.topic = topic;
        this.list = new ArrayList<>();
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public ArrayList<Qrel> getList() {
        return list;
    }

    public void setList(ArrayList<Qrel> list) {
        this.list = list;
    }
}
