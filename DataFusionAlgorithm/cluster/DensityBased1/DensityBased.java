package com.algorithm.cluster.DensityBased1;

import com.algorithm.Distance.StandardDistance;
import com.algorithm.cluster.CalculateDistanceMatrix;
import com.algorithm.cluster.hierarchical.agglomerative1.ClusterPoint;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DensityBased {
    /**
     * 实现基于密度聚类的DBSCAN算法
     * 1.计算得到距离矩阵
     * 2 声明run对象 ，对象属性包括run名和id号，E临域中的点个数，E临域中各个点ArrayList列表
     * 3 声明簇对象，对象属性包括该簇中所有的run对象和簇ID号
     * 4.创建一个结果聚类对象classResultArrayList，使用ArrayList对象，每个元素都是簇对象
     * 5.设置E临域
     * 6.设置密度阈值MinPts
     * 7 创建一个arraylist对象rawALLPointArrayList，包含所有的run对象，
     * 8 实现算法
     * 8.1 将rawALLPointArrayList中的一个元素加载到classResultArrayList.get(i)中，
     * 8.2 读取该元素的临域列表，进行遍历，并把遍历结果加入到classResultArrayList.get(i)中，
     * 8.3 删去所有在rawALLPointArrayList中的与classResultArrayList重复出现的点
     * 8.4 重新获取rawALLPointArrayList的第一个元素，重复8.1-8.3步骤，直至rawALLPointArrayList大小为0
     * 8.5 输出classResultArrayList
     */
    public static ArrayList<OneClass> classResultArrayList = new ArrayList<>();
    public static double EDistance =15;
    public static Integer minPts = 1;
    public static double[][] distanceMatrix ;
    public static ArrayList<RunPoint> rawALLPointArrayList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String runPath = "E:\\TRECDateset\\classificationEXP\\test\\standard_input_nor30-60\\";
        String XLSPath = "E:\\TRECDateset\\classificationEXP\\test\\classification\\";
        runProgram(runPath,XLSPath);
    }
    public static void runProgram(String runPath,String XLSPath) throws IOException {
        getDistanceMatrix(runPath);
        getRawALLPointArrayList(runPath);
        getALLEDistance();
        densityCalculate();
        showValue();
        writeClassXLS(XLSPath);

    }
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    private static void writeClassXLS(String outputXLS) throws IOException {
        outputXLS = outputXLS +"\\"+classResultArrayList.size()+"\\";
        createDirectory(outputXLS);
        String XLSPath = outputXLS + "Density_K"+classResultArrayList.size()+"_1.xls";
        FileOutputStream fos = new FileOutputStream(XLSPath);
        HSSFWorkbook hw = new HSSFWorkbook();
        Sheet sheet = hw.createSheet();
        for (int i = 0; i < classResultArrayList.size(); i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < classResultArrayList.get(i).getOneClassPoints().size(); j++) {
                row.createCell(j).setCellValue(classResultArrayList.get(i).getOneClassPoints().get(j).getName());
            }
        }
        hw.write(fos);
        fos.close();
    }

    public static void getDistanceMatrix(String runPath) throws IOException {
        File file = new File(runPath);
        File[] files = file.listFiles();
        distanceMatrix = new double[files.length][files.length];
        distanceMatrix = CalculateDistanceMatrix.runProgram(runPath);
        /*for (int i = 0; i < files.length; i++) {
            for (int j = 0; j < files.length; j++) {
                distanceMatrix[i][j] = StandardDistance.getRunARunBDistance(files[i].getPath(),files[j].getPath());
            }
        }*/
    }
    public static void getRawALLPointArrayList(String runPath){
        File file = new File(runPath);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            RunPoint runPoint = new RunPoint(files[i].getName(),i);
            rawALLPointArrayList.add(runPoint);
        }
    }
    private static void getALLEDistance() {
        for (int i = 0; i < rawALLPointArrayList.size(); i++) {
            int countEDistance = 0;
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                if (i==j){
                    continue;
                }
                if (distanceMatrix[i][j]<=EDistance){
                    countEDistance++;
                }
            }
            if (countEDistance>=minPts){
                for (int j = 0; j < distanceMatrix[i].length; j++) {
                    if (i==j){
                        continue;
                    }
                    if (distanceMatrix[i][j]<=EDistance){
                        rawALLPointArrayList.get(i).getERunPoints().add(rawALLPointArrayList.get(j));
                    }
                }
            }
        }
    }

    private  static void ergodicOneClass(RunPoint runPoint,int classNUM){
        if (!classResultArrayList.get(classNUM).getOneClassPoints().contains(runPoint)){
            classResultArrayList.get(classNUM).getOneClassPoints().add(runPoint);
            runPoint.setFlag(false);
        }
        if (runPoint.getERunPoints().size()!=0){
            for (int i = 0; i < runPoint.getERunPoints().size(); i++) {
                if (!classResultArrayList.get(classNUM).getOneClassPoints().contains(runPoint.getERunPoints().get(i))){
                    classResultArrayList.get(classNUM).getOneClassPoints().add(runPoint.getERunPoints().get(i));
                    runPoint.getERunPoints().get(i).setFlag(false);
                    ergodicOneClass(runPoint.getERunPoints().get(i),classNUM);
                }

            }
        }

    }

    private static boolean judgmentFlag(){
        for (RunPoint runPoint : rawALLPointArrayList) {
            if (runPoint.isFlag()==true){
                return true;
            }
        }
        return false;
    }

    private static void densityCalculate() {
        int classNUM = 0;
        for (int i = 0; i < rawALLPointArrayList.size(); i++) {
            if (rawALLPointArrayList.get(i).isFlag()==true){
                classResultArrayList.add(new OneClass(classNUM));
                ergodicOneClass(rawALLPointArrayList.get(i),classNUM);
                classNUM++;
            }
        }


    }

    private static void showValue() {
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                System.out.print(distanceMatrix[i][j]+"\t");
            }
            System.out.println();
        }
        for (int i = 0; i < rawALLPointArrayList.size(); i++) {
            System.out.println(rawALLPointArrayList.get(i)+"\t"+rawALLPointArrayList.get(i).getERunPoints().size());
            for (int j = 0; j < rawALLPointArrayList.get(i).getERunPoints().size(); j++) {
                System.out.print(rawALLPointArrayList.get(i).getERunPoints().get(j)+"\t");
            }
            System.out.println();
        }


        for (OneClass oneClass : classResultArrayList) {
            System.out.println(oneClass.getId()+"\t"+oneClass.getOneClassPoints().size());
            for (RunPoint oneClassPoint : oneClass.getOneClassPoints()) {
                System.out.print(oneClassPoint.getName()+"\t");
            }
            System.out.println();
        }

    }




}
