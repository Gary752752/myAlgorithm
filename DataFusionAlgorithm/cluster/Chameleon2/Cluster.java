package com.algorithm.cluster.Chameleon2;

import java.util.ArrayList;

public class Cluster {
    private int id;
    private ArrayList<Point> points ;

    public Cluster(int id) {
        this.id = id;
        this.points = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }
}
