package com.algorithm.cluster.hierarchical.agglomerative1;

import com.algorithm.DataStruct.HashMapDocKey;
import com.algorithm.DataStruct.HashMapDocValue;

import java.util.HashMap;
import java.util.Objects;

public class RunPoint {
    private String systemName;
    private HashMap<HashMapDocKey, HashMapDocValue> dimensionList;

    private double a_i;//Silhouette a(i)
    private double b_i;//Silhouette b(i)
    private double s_i;//Silhouette s(i)
    public RunPoint() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunPoint runPoint = (RunPoint) o;
        return Objects.equals(systemName, runPoint.systemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemName);
    }

    public RunPoint(String systemName, HashMap<HashMapDocKey, HashMapDocValue> dimensionList) {
        this.systemName = systemName;
        this.dimensionList = dimensionList;
    }

    public RunPoint(String systemName) {
        this.systemName = systemName;
        dimensionList = new HashMap<>();
    }

    public double getS_i() {
        return s_i;
    }

    public void setS_i(double s_i) {
        this.s_i = s_i;
    }

    public double getA_i() {
        return a_i;
    }

    public void setA_i(double a_i) {
        this.a_i = a_i;
    }

    public double getB_i() {
        return b_i;
    }

    public void setB_i(double b_i) {
        this.b_i = b_i;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public HashMap<HashMapDocKey, HashMapDocValue> getDimensionList() {
        return dimensionList;
    }

    public void setDimensionList(HashMap<HashMapDocKey, HashMapDocValue> dimensionList) {
        this.dimensionList = dimensionList;
    }
}
