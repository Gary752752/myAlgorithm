package com.algorithm.DataStruct;

import java.util.Objects;

public class HashMapDocKey {
    private Integer topic;
    private String docID;

    public HashMapDocKey() {
    }

    public HashMapDocKey(Integer topic, String docID) {
        this.topic = topic;
        this.docID = docID;
    }

    public Integer getTopic() {
        return topic;
    }

    public void setTopic(Integer topic) {
        this.topic = topic;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashMapDocKey that = (HashMapDocKey) o;
        return Objects.equals(topic, that.topic) && Objects.equals(docID, that.docID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, docID);
    }
}
