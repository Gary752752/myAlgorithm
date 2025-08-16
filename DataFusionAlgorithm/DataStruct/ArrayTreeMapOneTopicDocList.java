package com.algorithm.DataStruct;


import java.util.HashMap;

public class ArrayTreeMapOneTopicDocList {
    private Integer topic;
    private HashMap<HashMapDocKey, HashMapDocValue> oneTopicDocList ;

    public ArrayTreeMapOneTopicDocList() {
    }

    public ArrayTreeMapOneTopicDocList(Integer topic, HashMap<HashMapDocKey, HashMapDocValue> oneTopicDocList) {
        this.topic = topic;
        this.oneTopicDocList = oneTopicDocList;
    }

    public ArrayTreeMapOneTopicDocList(Integer topic) {
        this.topic = topic;
        this.oneTopicDocList = new HashMap<>();
    }

    public Integer getTopic() {
        return topic;
    }

    public void setTopic(Integer topic) {
        this.topic = topic;
    }

    public HashMap<HashMapDocKey, HashMapDocValue> getOneTopicDocList() {
        return oneTopicDocList;
    }

    public void setOneTopicDocList(HashMap<HashMapDocKey, HashMapDocValue> oneTopicDocList) {
        this.oneTopicDocList = oneTopicDocList;
    }
}
