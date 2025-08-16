package com.algorithm.DataStruct;

public class HashMapDocValue {
    private Integer rank = 0;
    private Double score = 0.0;
    private String systemName = "null";
    private Integer MNZ_Num = 1;
    private Double rel = 0.0;

    public HashMapDocValue() {
    }

    public HashMapDocValue(Integer rank,Double score,String systemName){
        this.rank = rank;
        this.score = score;
        this.systemName = systemName;
    }

    public HashMapDocValue(Integer rank, Double score, String systemName, Integer MNZ_Num, Double rel) {
        this.rank = rank;
        this.score = score;
        this.systemName = systemName;
        this.MNZ_Num = MNZ_Num;
        this.rel = rel;
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

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getMNZ_Num() {
        return MNZ_Num;
    }

    public void setMNZ_Num(Integer MNZ_Num) {
        this.MNZ_Num = MNZ_Num;
    }

    public Double getRel() {
        return rel;
    }

    public void setRel(Double rel) {
        this.rel = rel;
    }
}
