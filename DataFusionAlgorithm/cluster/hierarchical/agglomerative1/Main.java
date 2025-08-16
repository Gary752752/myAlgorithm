package com.algorithm.cluster.hierarchical.agglomerative1;

import com.algorithm.DataStruct.HashMapDocKey;
import com.algorithm.DataStruct.HashMapDocValue;
import com.algorithm.Distance.StandardDistance;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.algorithm.cluster.hierarchical.agglomerative1.DistanceClusterPoint.distanceClusterPoint;

public class Main {
    /**
     * 实现分层聚类方法
     * 1.将每一个点作为一个类
     * 2.计算每个类之间的距离
     * 3.得到最小距离的两个类
     * 4.将两个类合并，并计算两个类的中心点
     * 重复1-4过程直到获得K类，或者融合为1个类
     */
    public static ArrayList<ClusterPoint> clusterPoints = new ArrayList<>();

    /**
     * 计算时间间隔
     */
    //首先默认要融合的两个类是第一个和第二个
    public static Integer fusionClusterA = 0;
    public static Integer fusionClusterB = 1;
    public static void initClusterPoints(String pointPath) throws IOException {
        File[] Points = new File(pointPath).listFiles();
        System.out.println("Points:"+Points.length);
        for (int i = 0; i < Points.length; i++) {
            System.out.println(Points[i].getName());
            ClusterPoint tempC = new ClusterPoint(i+1);
            RunPoint runPoint = new RunPoint(Points[i].getName());
            setRunPointDimension(runPoint,Points[i]);
            tempC.getClusterList().add(runPoint);
            initClusterPointDimensionList(tempC);
            clusterPoints.add(tempC);
        }
    }

    private static void setRunPointDimension(RunPoint runPoint,File Point) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(Point));
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            Integer topic = Integer.parseInt(str[0]);
            String docID = str[2];
            Double score = Double.parseDouble(str[4]);
            HashMapDocKey key = new HashMapDocKey(topic,docID);
            HashMapDocValue value = new HashMapDocValue();
            value.setScore(score);
            runPoint.getDimensionList().put(key,value);
        }
    }

    public static void initClusterPointDimensionList(ClusterPoint clusterPoint){
        clearDimension(clusterPoint);
        RunPoint runPoint = clusterPoint.getClusterList().get(0);
        for (Map.Entry<HashMapDocKey, HashMapDocValue> pointEntry : runPoint.getDimensionList().entrySet()) {
            HashMapDocKey runPointKey = pointEntry.getKey();
            HashMapDocValue runPointValue = pointEntry.getValue();
            HashMapDocKey key = new HashMapDocKey(runPointKey.getTopic(),runPointKey.getDocID());
            HashMapDocValue value = new HashMapDocValue();
            value.setScore(runPointValue.getScore());
            clusterPoint.getDimensionList().put(key,value);
        }
    }

    private static void clearDimension(ClusterPoint clusterPoint) {
        /*for (Map.Entry<HashMapDocKey, HashMapDocValue> hashMapDocKeyHashMapDocValueEntry : clusterPoint.getDimensionList().entrySet()) {
            hashMapDocKeyHashMapDocValueEntry.getValue().setScore(0.0);
        }*/
        clusterPoint.getDimensionList().clear();

    }

    public static  double[][] calculateDistanceMatrix(){
        double[][] distanceMatrix = new double[clusterPoints.size()][clusterPoints.size()];
        for (int i = 0; i < clusterPoints.size(); i++) {
            for (int j = 0; j < clusterPoints.size(); j++) {
                distanceMatrix[i][j] = StandardDistance.getRunARunBDistance(clusterPoints.get(i),clusterPoints.get(j));
            }
        }
        //首先默认要融合的两个类是第一个和第二个
        fusionClusterA = 0;
        fusionClusterB = 1;
        Double minDistance = distanceMatrix[fusionClusterA][fusionClusterB];

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = i+1; j < distanceMatrix.length; j++) {
                if (i != j && minDistance > distanceMatrix[i][j]){

                    minDistance = distanceMatrix[i][j];
                    fusionClusterA = i;
                    fusionClusterB = j;
                }
            }
        }

        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                System.out.print(distanceMatrix[i][j]+"\t");
            }
            System.out.println();
        }
        System.out.println(minDistance);
        System.out.println(fusionClusterA);
        System.out.println(fusionClusterB);
        return distanceMatrix;
    }



    private static void alterClusterPoints() {
        ClusterPoint clusterPointA = clusterPoints.get(fusionClusterA);
        ClusterPoint clusterPointB = clusterPoints.get(fusionClusterB);
        for (RunPoint runPointB : clusterPointB.getClusterList()) {
            clusterPointA.getClusterList().add(runPointB);
        }

        clusterPoints.remove(clusterPointB);
        calculateClusterPintDimension(clusterPointA);

        refreshClusterPointsID();
    }

    private static void refreshClusterPointsID() {
        Integer ID = 1;
        for (ClusterPoint clusterPoint : clusterPoints) {
            clusterPoint.setClusterID(ID);
            ID++;
        }
    }

    private static void calculateClusterPintDimension(ClusterPoint clusterPointA) {
        clearDimension(clusterPointA);

        for (RunPoint runPoint : clusterPointA.getClusterList()) {
            //System.out.println(runPoint.getSystemName());
            for (Map.Entry<HashMapDocKey, HashMapDocValue> runPointEntry : runPoint.getDimensionList().entrySet()) {
                HashMapDocKey pointKey = runPointEntry.getKey();
                HashMapDocValue pointValue = runPointEntry.getValue();
                HashMapDocValue clusterValue = clusterPointA.getDimensionList().get(pointKey);
                if (clusterValue != null){
                    clusterValue.setScore(clusterValue.getScore()+pointValue.getScore());
                }else {
                    Integer topic = pointKey.getTopic();
                    String docID = pointKey.getDocID();
                    Integer rank = pointValue.getRank();
                    Double score = pointValue.getScore();
                    String systemName = pointValue.getSystemName();

                    clusterPointA.getDimensionList().put(new HashMapDocKey(topic,docID),new HashMapDocValue(rank,score,systemName));
                }

            }
        }

        for (Map.Entry<HashMapDocKey, HashMapDocValue> clusterEntry : clusterPointA.getDimensionList().entrySet()) {
            clusterEntry.getValue().setScore(clusterEntry.getValue().getScore()/clusterPointA.getClusterList().size());
        }
    }
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    public static void writeXLS(String outputXLS) throws IOException {
        outputXLS = outputXLS +"\\"+clusterPoints.size()+"\\";
        createDirectory(outputXLS);
        String XLSPath = outputXLS + "AGG_K"+clusterPoints.size()+"_1.xls";
        FileOutputStream fos = new FileOutputStream(XLSPath);
        HSSFWorkbook hw = new HSSFWorkbook();
        Sheet sheet = hw.createSheet();
        for (int i = 0; i < clusterPoints.size(); i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < clusterPoints.get(i).getClusterList().size(); j++) {
                row.createCell(j).setCellValue(clusterPoints.get(i).getClusterList().get(j).getSystemName());
            }
        }
        hw.write(fos);
        fos.close();
    }
    public static void runProgram(String pointPath,Integer k,String outputXLS) throws IOException {


        initClusterPoints(pointPath);
        writeXLS(outputXLS);
        long intervalTime = 0;
        while ( clusterPoints.size() >= k && clusterPoints.size() > 1){
            long interval1 = System.currentTimeMillis();
            calculateDistanceMatrix();
            alterClusterPoints();
            long interval2 = System.currentTimeMillis();
            intervalTime += interval2-interval1;
            //showErrPoint();
            //showCluster();
            System.out.println("------------------------");
            System.out.println(clusterPoints.size());
            int runNum = 0;
            for (ClusterPoint clusterPoint : clusterPoints) {
                runNum += clusterPoint.getClusterList().size();
            }
            System.out.println(runNum);
            resortPointXLS();
            writeXLS(outputXLS);
        }
        System.out.println("间隔时间为："+(intervalTime));
    }

    /**
     * 将每个簇内的run按照距离中心点的距离进行从小到大排序
     */
    private static void resortPointXLS() {
        for (int i = 0; i < clusterPoints.size(); i++) {
            double[] distanceTemp = new double[clusterPoints.get(i).getClusterList().size()];
            for (int j = 0; j < clusterPoints.get(i).getClusterList().size(); j++) {
                distanceTemp[j] = StandardDistance.getRunARunBDistance(clusterPoints.get(i),clusterPoints.get(i).getClusterList().get(j));
            }
            for (int k = 0; k < clusterPoints.get(i).getClusterList().size(); k++) {
                for (int z = k+1; z < clusterPoints.get(i).getClusterList().size(); z++) {
                    if (distanceTemp[k] > distanceTemp[z]){
                        Collections.swap(clusterPoints.get(i).getClusterList(),k,z);
                        double temp = distanceTemp[k];
                        distanceTemp[k] = distanceTemp[z];
                        distanceTemp[z] = temp;
                    }
                }
            }
            for (int p = 0; p < distanceTemp.length; p++) {
                System.out.print(distanceTemp[p]+"\t");
            }
            System.out.println();
        }


    }

    private static void showCluster(){
        for (ClusterPoint clusterPoint : clusterPoints) {
            System.out.println("clusterPoint");
            for (Map.Entry<HashMapDocKey, HashMapDocValue> tempEntry : clusterPoint.getDimensionList().entrySet()) {
                System.out.println("#########"+tempEntry.getValue().getScore());
            }
        }
    }
    private static void showErr() {
        System.out.println(clusterPoints.size());
        for (ClusterPoint clusterPoint : clusterPoints) {
            System.out.println(clusterPoint.getClusterID()+clusterPoint.getClusterList().get(0).getSystemName());
            for (Map.Entry<HashMapDocKey, HashMapDocValue> testEntry : clusterPoint.getDimensionList().entrySet()) {
                HashMapDocKey key = testEntry.getKey();
                HashMapDocValue value = testEntry.getValue();
                System.out.println("-----"+key.getTopic()+"\t"+key.getDocID()+"\t"+value.getScore());
            }
        }
    }
    private static void showErrPoint(){
        for (ClusterPoint clusterPoint : clusterPoints) {
            for (RunPoint runPoint : clusterPoint.getClusterList()) {
                System.out.println("********"+runPoint.getSystemName());
                for (Map.Entry<HashMapDocKey, HashMapDocValue> tempEntry : runPoint.getDimensionList().entrySet()) {
                    HashMapDocKey key = tempEntry.getKey();
                    HashMapDocValue value = tempEntry.getValue();
                    System.out.println("*********"+key.getTopic()+"\t"+key.getDocID()+"\t"+ value.getScore());
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String pointPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60"+"\\";
        String outputXLS = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\classification\\intervalClassification\\HC"+"\\";
        Integer k = 12;
        runProgram(pointPath,k,outputXLS);
    }
}
