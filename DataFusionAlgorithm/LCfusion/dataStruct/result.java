package com.algorithm.LCfusion.dataStruct;

public class result{
    public String docid;
    public int rank;
    public double score;
    public result(){}
    public result(String docid, int rank, double score) {
        super();
        this.docid = docid;
        this.rank = rank;
        this.score = score;
    }

}
