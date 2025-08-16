package com.algorithm.DataStruct;

import java.util.HashSet;

public class Docs {
    private int topic;
    private HashSet<Doc> list;

    public Docs() {
    }

    public Docs(int topic) {
        this.topic = topic;
        this.list = new HashSet<>();
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public HashSet<Doc> getList() {
        return list;
    }

    public void setList(HashSet<Doc> list) {
        this.list = list;
    }
}
