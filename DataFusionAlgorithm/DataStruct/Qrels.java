package com.algorithm.DataStruct;

import java.util.LinkedHashSet;

public class Qrels {
    private int topic;
    private LinkedHashSet<Qrel> list;

    public Qrels() {
    }

    public Qrels(int topic, LinkedHashSet<Qrel> list) {
        this.topic = topic;
        this.list = list;
    }

    /**
     * 以topic创建一个Qrels并且将Qrel列表创建完成
     * @param topic
     */
    public Qrels(int topic) {
        this.topic = topic;
        this.list = new LinkedHashSet<>();
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public LinkedHashSet<Qrel> getList() {
        return list;
    }

    public void setList(LinkedHashSet<Qrel> list) {
        this.list = list;
    }
}
