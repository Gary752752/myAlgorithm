package com.algorithm.DataStruct;

import java.util.Objects;

public class Qrel {
    private int topic;
    private String docID;
    private double rel;
    private double U_C_rel;
    private double _UC_rel;
    private int rank;

    public Qrel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qrel qrel = (Qrel) o;
        return topic == qrel.topic && Objects.equals(docID, qrel.docID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, docID);
    }

    @Override
    public String toString() {
        return "Qrel{" +
                "topic=" + topic +
                ", docID='" + docID + '\'' +
                ", rel=" + rel +
                '}';
    }

    public Qrel(int topic, String docID, double rel, double u_C_rel, double _UC_rel, int rank) {
        this.topic = topic;
        this.docID = docID;
        this.rel = rel;
        U_C_rel = u_C_rel;
        this._UC_rel = _UC_rel;
        this.rank = rank;
    }

    public Qrel(int topic, String docID, double rel) {
        this.topic = topic;
        this.docID = docID;
        this.rel = rel;
    }

    public Qrel(int topic, String docID) {
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

    public double getRel() {
        return rel;
    }

    public void setRel(double rel) {
        this.rel = rel;
    }

    public double getU_C_rel() {
        return U_C_rel;
    }

    public void setU_C_rel(double u_C_rel) {
        U_C_rel = u_C_rel;
    }

    public double get_UC_rel() {
        return _UC_rel;
    }

    public void set_UC_rel(double _UC_rel) {
        this._UC_rel = _UC_rel;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
