package com.algorithm.DataStruct;

import org.apache.poi.ss.formula.functions.T;

import java.util.Objects;

public class Doc{
    private int topic;
    private String docID;


    private Integer rank;

    private Double score;


    private  Double rel = 0.0;

    private  String systemName;
    private Integer MNZ_NUM = 0;

    public Doc() {
    }

    public Doc(int topic, String docID, Integer rank, Double score, Double rel, String systemName) {
        this.topic = topic;
        this.docID = docID;
        this.rank = rank;
        this.score = score;
        this.rel = rel;
        this.systemName = systemName;
    }

    public Doc(int topic, String docID, Integer rank, Double score, String systemName) {
        this.topic = topic;
        this.docID = docID;
        this.rank = rank;
        this.score = score;
        this.systemName = systemName;
    }

    public Doc(String docID, int topic) {
        this.docID = docID;
        this.topic = topic;
    }

    public Doc(int topic, String docID, Integer rank, Double score, String systemName, Integer MNZ_NUM) {
        this.topic = topic;
        this.docID = docID;
        this.rank = rank;
        this.score = score;
        this.systemName = systemName;
        this.MNZ_NUM = MNZ_NUM;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getMNZ_NUM() {
        return MNZ_NUM;
    }

    public void setMNZ_NUM(Integer MNZ_NUM) {
        this.MNZ_NUM = MNZ_NUM;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getRel() {
        return rel;
    }

    public void setRel(Double rel) {
        this.rel = rel;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Doc{" +
                "docID='" + docID + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doc doc = (Doc) o;
        return Objects.equals(docID, doc.docID) && Objects.equals(topic, doc.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docID, topic);
    }
}
