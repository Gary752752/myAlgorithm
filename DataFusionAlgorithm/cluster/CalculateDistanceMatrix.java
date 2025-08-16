package com.algorithm.cluster;

import com.algorithm.cluster.hierarchical.agglomerative1.Main;

import java.io.IOException;

public class CalculateDistanceMatrix {
    //计算距离矩阵
    public static double[][] runProgram(String pointPath) throws IOException {
        Main.initClusterPoints(pointPath);
        double[][] Matrix = Main.calculateDistanceMatrix();
        return Matrix;
    }
    public static void main(String[] args) throws IOException {
        String pointPath = "D:\\myIdeaProject\\TREC\\2021health\\input\\standard_input_nor30_60";
        runProgram(pointPath);
    }
}
