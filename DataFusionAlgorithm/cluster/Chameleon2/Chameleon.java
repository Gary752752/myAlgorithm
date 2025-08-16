package com.algorithm.cluster.Chameleon2;

import com.algorithm.DataStruct.HashMapDocKey;
import com.algorithm.DataStruct.HashMapDocValue;
import com.algorithm.DataStruct.Result;
import com.algorithm.cluster.CalculateDistanceMatrix;
import com.algorithm.cluster.hierarchical.agglomerative1.ClusterPoint;
import com.algorithm.cluster.hierarchical.agglomerative1.RunPoint;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 实现变色龙聚类算法
 */
public class Chameleon {
    /*
    1.采用K-最近邻图的方法构造一个稀疏图（其中，图的每个顶点代表一个数据对象，如果一个对象是另一个对象的k个最相似的对象之一，那么这两个对象之间就存在一条边，这些边加权后反应对象间的相似度）
    2.使用图划分算法，把k-个邻图划分成大量相对较小的子簇，使得边割最小（划分代价，切断边上的权重之和最小）
    3使用凝聚层次聚类算法，基于子簇的相似度（考察每个簇的互连性和邻近性），合并子簇
    簇的互联性（RI）考察两个簇之间的互联度和簇内部各元组之间的互联度。如果两个簇的边界距离和各簇内部元组之间的距离大致相同，那么就合并它们
    公式介绍：Chameleon使用相对互连性（Relative Interconnectivity）和相对近似性（Relative Closeness）来决定子簇之间的相似性，会选择RI和RC都高的子簇对进行合并。
        相对互连性： RI(Ci,Cj)={EC(Ci,Cj)}/{[|EC(Ci)|+EC(Cj)]/2}
            其中，EC(Ci,Cj)是连接簇Ci和Cj的边的权重之和，EC(Ci)是把簇Ci划分成两个大小大致相等部分的最少边的权重之和。相对互连性解决的是簇之间形状不同和互连度不同的问题。
        相对近似性：RC(Ci,Cj)={^SEC(Ci,Cj)/[|Ci|/(|Ci|+|Cj|)*^SEC(Ci)+|Cj|/(|Ci|+|Cj|)*^SEC(Cj)]}
            其中^SEC(Ci,Cj)是连接簇Ci和Cj的边的平均权重，^SEC(Ci)是把簇Ci划分成两个大小大致相等部分的最少边的平均权重
    具体步骤：
        一、子图划分：Chameleon使用hMetis[2]算法根据最小化截断的边的权重之和来分隔k近邻图。
        二、子簇合并Chameleon使用度量RI(Ci,Cj)*RC(Ci,Cj)^α来选择合并的子簇，用这个度量是同时考虑到了两个子簇之间的互连性和近似性。
     */
    /*
    步骤1. 获得k邻近图
        1.1 获取所有的point，计算point两两之间的距离，得到距离矩阵，并将距离矩阵转换为权重矩阵，权重为距离的倒数
        1.2 构建邻接矩阵N*N，N为point的数量，找到每个point的k个邻点，矩阵中Aij表示点i和点j的边的权重，若不是邻点则Aij为0
        1.3 构造邻接表：对于每个节点，存储一个列表，列表中包括所有与其相连的节点以及对应的边权重
     */
    public double[][] createKAdjacentMatrix(String pointPath,int k) throws IOException {
        /*
        计算距离矩阵
         */
        File[] Points = new File(pointPath).listFiles();
        assert Points != null;
        int PointsNum = Points.length;
        System.out.println("参与聚类的point数量："+PointsNum);
        double[][] distanceMatrix = CalculateDistanceMatrix.runProgram(pointPath);
        System.out.println("distanceMatrix大小："+distanceMatrix.length);
        System.out.println("distanceMatrix大小："+distanceMatrix[0].length);
        /*
        将距离矩阵转换为权重矩阵
         */
        double[][] powerMatrix = changeDistanceToPower(distanceMatrix);
        /*
            将权重矩阵转换为邻接矩阵
         */
        double[][] adjacentMatrix = changePowerToAdjacent(powerMatrix,k);

        return adjacentMatrix;
    }

    public ArrayList<Cluster> DFSPoints(ArrayList<Point> points, ArrayList<Cluster> clusters) {
        /*
        使用深度优先算法遍历每一个point
         */
        int count = 0;
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.isFlag()==false){
                Cluster cluster = new Cluster(++count);
                cluster.getPoints().add(point);
                point.setFlag(true);

                dfs(point,cluster);
                clusters.add(cluster);
            }
        }

        return clusters;
    }

    private void dfs(Point point, Cluster cluster) {

        for (Point p : point.getPointArray()) {
            if (p.isFlag()==false){
                p.setFlag(true);
                cluster.getPoints().add(p);
                dfs(p,cluster);
            }
        }
    }


    public ArrayList<Point> getOneStep(String pointPath, double[][] adjacentMatrix) {
        ArrayList<Point> points = new ArrayList<>();
        File[] files = new File(pointPath).listFiles();
        for (int i = 0; i < files.length; i++) {
            Point point = new Point(files[i].getName(),i);
            points.add(point);
        }
        for (int i = 0; i < points.size(); i++) {
            System.out.println(points.get(i).toString());
        }
        /*
            遍历adjacentMatrix矩阵，将相连的point加载到对应point的pointArray列表中
         */
        for (int i = 0; i < adjacentMatrix.length; i++) {
            for (int j = 0; j < adjacentMatrix[i].length; j++) {
                if (adjacentMatrix[i][j]!=0){
                    points.get(i).getPointArray().add(points.get(j));
                }
            }
        }
        for (int i = 0; i < points.size(); i++) {
            System.out.print(points.get(i).getPointName()+"\t 连接了：\t");
            if (points.get(i).getPointArray().size()!=0){
                for (int z = 0; z < points.get(i).getPointArray().size(); z++) {
                    System.out.print(points.get(i).getPointArray().get(z).getPointName()+"\t");
                }
            }
            System.out.println();
        }
        return points;
    }

    private boolean AlinkB(Point a, Point b) {
        for (Point point : b.getPointArray()) {
            if (point.getPointName().equals(a.getPointName())){
                return true;
            }
        }
        return false;
    }

    public double[][] changePowerToAdjacent(double[][] powerMatrix, int k) {
        double[][] adjacencyMatrix = new double[powerMatrix.length][powerMatrix.length];

        int n = powerMatrix.length;
        System.out.println("输出权重矩阵");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%8.5f \t",powerMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        //无向图邻接矩阵
        int[][] minCols = new int[n][n];
        for (int i = 0; i < n; i++) {
            //遍历每一行找到该行的k个邻点，极限为k等于列数-1

            double[] sortRow = powerMatrix[i].clone();;
            Arrays.sort(sortRow);
            reverseArray(sortRow);
            for (int j = 0; j < sortRow.length; j++) {
                System.out.printf("%8.5f \t",sortRow[j]);
            }
            System.out.println();
            for (int j = 0; j < sortRow.length; j++) {
                for (int l = 0; l < powerMatrix[i].length; l++) {
                    if (sortRow[j]==powerMatrix[i][l]){
                        minCols[i][l] = j+1;
                    }
                }
            }
        }
//        for (int i = 0; i < minCols.length; i++) {
//            for (int j = i+1; j < minCols[i].length; j++) {
//                if (minCols[i][j]>=minCols[j][i]){
//                    minCols[i][j] = minCols[j][i];
//                }else {
//                    minCols[j][i] = minCols[i][j];
//                }
//            }
//        }
        System.out.println("输出前N项的权重矩阵");
        for (int i = 0; i < minCols.length; i++) {
            for (int j = 0; j < minCols[i].length; j++) {
                System.out.printf("%8d \t",minCols[i][j]);
                if (minCols[i][j]<=k){
                    //System.out.printf("%6d \t %d %d \n",minCols[i][j],i,j);
                    adjacencyMatrix[i][j] = powerMatrix[i][j];

                }
            }
            System.out.println();
        }
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                if (adjacencyMatrix[i][j]!=0&&adjacencyMatrix[j][i]!=0){

                }else {
                    adjacencyMatrix[i][j]=0;
                    adjacencyMatrix[j][i]=0;
                }
            }
        }
        //输出k邻接矩阵
        System.out.println("输出k邻接矩阵");
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            System.out.print("run:"+i+"\t");
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                System.out.printf("%8.5f \t",adjacencyMatrix[i][j]);
            }
            System.out.println();
        }
        return adjacencyMatrix;
    }
    // 反转数组的方法
    public static void reverseArray(double[] array) {
        int n = array.length;
        for (int i = 0; i < n / 2; i++) {
            double temp = array[i];
            array[i] = array[n - 1 - i];
            array[n - 1 - i] = temp;
        }
    }
    public double[][] changeDistanceToPower(double[][] distanceMatrix) {
        double[][] powerMatrix = new double[distanceMatrix.length][distanceMatrix.length];
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                powerMatrix[i][j] = distanceMatrix[i][j]==0?0:1/distanceMatrix[i][j];
            }
        }
        return powerMatrix;
    }

    private void readALLPoints(String pointPath) throws IOException {
        File input = new File(pointPath);

        File[] runs = input.listFiles();
        for(File run:runs) {
            com.algorithm.cluster.kmeans.newCode.Point point = new com.algorithm.cluster.kmeans.newCode.Point();
            System.out.println(run.getName());
            BufferedReader readerPoint = new BufferedReader(new FileReader(run));
            String runPointname = run.toString().split("\\\\")[run.toString().split("\\\\").length-1];//获得到点名
            point.setPointname(runPointname);
            String bufferline = new String(readerPoint.readLine());
            StringTokenizer tokenizer=new StringTokenizer(bufferline);
            //读取topic
            int topic=Integer.parseInt(tokenizer.nextToken());
            tokenizer.nextToken();
            //读取docID
            String docid=tokenizer.nextToken();

            //读取分数
            double score=Double.parseDouble(tokenizer.nextToken());
            point.coordinatelist.add(new Result(topic, docid, score));
            while((bufferline=readerPoint.readLine())!=null) {
                tokenizer=new StringTokenizer(bufferline);

                //读取topic
                topic=Integer.parseInt(tokenizer.nextToken());
                tokenizer.nextToken();
                //读取docID
                docid=tokenizer.nextToken();
                //读取分数
                score=Double.parseDouble(tokenizer.nextToken());
                point.coordinatelist.add(new Result(topic, docid, score));
            }
            Points.add(point);
            readerPoint.close();
        }
        System.err.println("-------------读取所有点   end-------------");
    }
    public void runProgram(String pointPath,int k,double minMetric,String outputXLS) throws IOException {
        /*
        执行步骤1：获得k邻近图
         */
        double[][] adjacentMatrix = createKAdjacentMatrix(pointPath, k);

        //得到points的连接情况
        ArrayList<Point> KNNPoints = getOneStep(pointPath,adjacentMatrix);
        /*
        利用邻接矩阵获得初始聚簇
         */
        ArrayList<Cluster> clusters =new ArrayList<>();
        clusters =  DFSPoints(KNNPoints,clusters);
        //输出k邻近算法后的无向图，每一个cluster都是一个无向图
        for (int i = 0; i < clusters.size(); i++) {
            System.out.print("cluster id : "+clusters.get(i).getId()+"\t###\t");
            for (int j = 0; j < clusters.get(i).getPoints().size(); j++) {
                System.out.print("\t"+clusters.get(i).getPoints().get(j).getId());
            }
            System.out.println();
        }
        /*
        执行步骤2：进行图划分算法，使用最小截断边来进行划分
         */

        /*
        执行步骤2：进行层次聚类
         */
        //按照clusters和adjacentMatrix进行进行聚类
        //计算相似度矩阵
        readALLPoints(pointPath);
        //输出所有点及其长度
        for (int i = 0; i < Points.size(); i++) {
            System.out.println(Points.get(i).getPointname()+"\t"+Points.get(i).coordinatelist.size());
        }
        double[][] RIMatrix = calcRI(clusters);
        double[][] RCMatrix = calcRC(clusters);



    }

    private double[][] calcRC(ArrayList<Cluster> clusters) {
        int size = clusters.size();
        double[][] RCMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = i+1; j < size-1 ; j++) {
                RCMatrix[i][j] = calcRC_A_B(clusters.get(i),clusters.get(j));
            }
        }

        return RCMatrix;
    }

    private double calcRC_A_B(Cluster A, Cluster B) {
        double tempRC =  0.0;

        return tempRC;
    }

    private double[][] calcRI(ArrayList<Cluster> clusters) {
        int size = clusters.size();
        double[][] RIMatrix = new double[size][size];
        return RIMatrix;
    }

    public static ArrayList<com.algorithm.cluster.kmeans.newCode.Point> Points = new ArrayList<>();//点列表



    public static void main(String[] args) throws IOException {
        String pointPath ="D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60";
        int k=31;//邻接矩阵k值
        double minMetric = 0.01;
        String outputXLS = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\classification\\CHC"+"\\";
        Chameleon chameleon = new Chameleon();
        chameleon.runProgram(pointPath,k,minMetric,outputXLS);
    }
}
