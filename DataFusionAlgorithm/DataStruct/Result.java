package com.algorithm.DataStruct;

import java.util.Objects;

public class Result {
    private int topic;
    private String docID;
    private double score;
    private int rank;
    private String systemName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return topic == result.topic && Objects.equals(docID, result.docID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, docID);
    }

    @Override
    public String toString() {
        return "Result{" +
                "topic=" + topic +
                ", docID='" + docID + '\'' +
                ", score=" + score +
                ", rank=" + rank +
                '}';
    }

    public Result() {
    }

    public Result(int topic, String docID, double score) {
        this.topic = topic;
        this.docID = docID;
        this.score = score;
    }

    public Result(int topic, String docID, int rank, double score, String systemName) {
        this.topic = topic;
        this.docID = docID;
        this.score = score;
        this.rank = rank;
        this.systemName = systemName;
    }
    public Result(int topic, String docID,double score ,int rank , String systemName) {
        this.topic = topic;
        this.docID = docID;
        this.score = score;
        this.rank = rank;
        this.systemName = systemName;
    }
    public Result(int topic, String docID, int rank) {
        this.topic = topic;
        this.docID = docID;
        this.rank = rank;
    }

    public Result(int topic, String docID) {
        this.topic = topic;
        this.docID = docID;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
