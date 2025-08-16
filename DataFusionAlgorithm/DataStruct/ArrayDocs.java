package com.algorithm.DataStruct;

import java.util.ArrayList;

public class ArrayDocs {
    private int topic;
    private ArrayList<Doc> list;

    public ArrayDocs() {
    }

    public ArrayDocs(int topic) {
        this.topic = topic;
        this.list = new ArrayList<>();
    }

    public ArrayDocs(int topic, ArrayList<Doc> list) {
        this.topic = topic;
        this.list = list;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public ArrayList<Doc> getList() {
        return list;
    }

    public void setList(ArrayList<Doc> list) {
        this.list = list;
    }
}
