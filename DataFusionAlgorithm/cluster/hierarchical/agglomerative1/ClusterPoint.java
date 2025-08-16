package com.algorithm.cluster.hierarchical.agglomerative1;

import com.algorithm.DataStruct.HashMapDocKey;
import com.algorithm.DataStruct.HashMapDocValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ClusterPoint {
    private Integer clusterID;
    private ArrayList<RunPoint> clusterList;
    private HashMap<HashMapDocKey, HashMapDocValue> dimensionList;
    private Double meanDistanceForC_P = 0.0;
    public ClusterPoint() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClusterPoint that = (ClusterPoint) o;
        return Objects.equals(clusterID, that.clusterID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clusterID);
    }

    public ClusterPoint(Integer clusterID) {
        this.clusterID = clusterID;
        this.dimensionList = new HashMap<>();
        this.clusterList = new ArrayList<>();
    }

    public ClusterPoint(Integer clusterID, ArrayList<RunPoint> clusterList, HashMap<HashMapDocKey, HashMapDocValue> clusterList1) {
        clusterID = clusterID;
        this.clusterList = clusterList;
        dimensionList = clusterList1;
    }

    public Integer getClusterID() {
        return clusterID;
    }

    public void setClusterID(Integer clusterID) {
        this.clusterID = clusterID;
    }

    public ArrayList<RunPoint> getClusterList() {
        return clusterList;
    }

    public void setClusterList(ArrayList<RunPoint> clusterList) {
        this.clusterList = clusterList;
    }

    public HashMap<HashMapDocKey, HashMapDocValue> getDimensionList() {
        return dimensionList;
    }

    public void setDimensionList(HashMap<HashMapDocKey, HashMapDocValue> dimensionList) {
        this.dimensionList = dimensionList;
    }

    public Double getMeanDistanceForC_P() {
        return meanDistanceForC_P;
    }

    public void setMeanDistanceForC_P(Double meanDistanceForC_P) {
        this.meanDistanceForC_P = meanDistanceForC_P;
    }
}
