package com.algorithm.cluster.DensityBased1;

import java.util.ArrayList;

public class OneClass {
    private Integer id;
    private ArrayList<RunPoint> oneClassPoints;

    public OneClass(Integer id) {
        this.id = id;
        this.oneClassPoints = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<RunPoint> getOneClassPoints() {
        return oneClassPoints;
    }

    public void setOneClassPoints(ArrayList<RunPoint> oneClassPoints) {
        this.oneClassPoints = oneClassPoints;
    }
}
