package com.algorithm.cluster.DensityBased1;

import java.util.ArrayList;
import java.util.Objects;

public class RunPoint {
    private String name;
    private Integer id;
    private ArrayList<RunPoint> ERunPoints;

    private boolean flag = true;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "RunPoint{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunPoint runPoint = (RunPoint) o;
        return name.equals(runPoint.name) && id.equals(runPoint.id) && Objects.equals(ERunPoints, runPoint.ERunPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    public RunPoint(String name, Integer id) {
        this.name = name;
        this.id = id;
        this.ERunPoints = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<RunPoint> getERunPoints() {
        return ERunPoints;
    }

    public void setERunPoints(ArrayList<RunPoint> ERunPoints) {
        this.ERunPoints = ERunPoints;
    }
}
