package com.algorithm.cluster.Chameleon2;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Point {
    private String pointName;
    private int id;
    private ArrayList<Point> pointArray;

    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Point{" +
                "pointName='" + pointName + '\'' +
                ", id=" + id +
                ", flag=" + flag +
                '}';
    }

    public Point(String pointName, int id) {
        this.pointName = pointName;
        this.id = id;
        this.pointArray = new ArrayList<>();
    }

    public void initPoint(int id){
        pointArray = new ArrayList<>();
    }
    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Point> getPointArray() {
        return pointArray;
    }

    public void setPointArray(ArrayList<Point> pointArray) {
        this.pointArray = pointArray;
    }
}
