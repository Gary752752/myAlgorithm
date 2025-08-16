package com.algorithm.cluster.classificationTopMapXLS;

import com.algorithm.DataStruct.RunPerformanceValues;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * 输入文件为MAP P10等性能指标XLS文件，使用聚类算法的分类XLS文件
 * 输出为将要融合的XLS fusionNames文件，排序方式为先挑选出簇内性能最好的run，然后把这些run再按照性能排序（先簇内排序，然后簇间排序）
 */
public class ClassificationTopMapXLSMain {
    public static ArrayList<ArrayList<RunPerformanceValues>> classificationRuns = new ArrayList<>();
    public static ArrayList<RunPerformanceValues> rpvList = new ArrayList<>();

    public static void readPerformanceXLS(String performanceXLSPath) throws IOException {
        FileInputStream fis = new FileInputStream(performanceXLSPath);
        HSSFWorkbook hw = new HSSFWorkbook(fis);
        Sheet sheet = hw.getSheetAt(0);
        Row row ;
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            RunPerformanceValues tempRPV = new RunPerformanceValues();
            String name = row.getCell(0).getStringCellValue();
            double performanceValue = row.getCell(1).getNumericCellValue();
            tempRPV.setName(name);
            tempRPV.setPerformanceValues(performanceValue);
            rpvList.add(tempRPV);
        }
    }

    public static void readClassificationXLS(String classificationXLSPath) throws IOException {
        FileInputStream fis = new FileInputStream(classificationXLSPath);
        HSSFWorkbook hw = new HSSFWorkbook(fis);
        Sheet sheet = hw.getSheetAt(0);
        Row row;
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            ArrayList<RunPerformanceValues> oneClassification = new ArrayList<>();
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                String name = row.getCell(j).getStringCellValue().split(",")[row.getCell(j).getStringCellValue().split(",").length-1];
                Double performanceValue = findRunPerformance(name);
                RunPerformanceValues tempRPV = new RunPerformanceValues(name,performanceValue);
                oneClassification.add(tempRPV);
            }
            System.out.println(i);
            /**
             * 簇内排序
             */
            Collections.sort(oneClassification, new Comparator<RunPerformanceValues>() {
                @Override
                public int compare(RunPerformanceValues o1, RunPerformanceValues o2) {
                    if (o1.getPerformanceValues()> o2.getPerformanceValues()){
                        return -1;
                    } else if (o1.getPerformanceValues() < o2.getPerformanceValues()) {
                        return 1;
                    }else {
                        return 0;
                    }
                }
            });
            classificationRuns.add(oneClassification);
        }
    }

    private static Double findRunPerformance(String name) {
        for (RunPerformanceValues runPerformanceValues : rpvList) {
            if (runPerformanceValues.getName().equals(name)){
                return runPerformanceValues.getPerformanceValues();
            }
        }
        return null;
    }
    private static void writeTopPerformanceXLS(String outputClassificationTopPerformancePath) throws IOException {

        sortClassificationRuns();

        FileOutputStream fos = new FileOutputStream(outputClassificationTopPerformancePath);
        HSSFWorkbook hw = new HSSFWorkbook();
        Sheet sheet = hw.createSheet();
        Row row;
        for (int i = 0; i < classificationRuns.size(); i++) {
            row = sheet.createRow(i);
            String fusionName = classificationRuns.get(i).get(0).getName();
            row.createCell(0).setCellValue(fusionName);

        }
        hw.write(fos);
        fos.close();
    }

    private static void sortClassificationRuns() {
        /**
         * 簇间排序
         */
        for (int i = 0; i < classificationRuns.size(); i++) {
            for (int j = 0; j < classificationRuns.size(); j++) {
                if (classificationRuns.get(i).get(0).getPerformanceValues() > classificationRuns.get(j).get(0).getPerformanceValues()){
                    Collections.swap(classificationRuns,i,j);
                }
            }
        }

    }

    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    public static void writeSortPerformanceXLS(String outputClassificationSortPerformanceFilePath) throws IOException {
        sortClassificationRuns();

        FileOutputStream fos = new FileOutputStream(outputClassificationSortPerformanceFilePath);
        HSSFWorkbook hw = new HSSFWorkbook();
        Sheet sheet = hw.createSheet();
        Row row;
        for (int i = 0; i < classificationRuns.size(); i++) {
            row = sheet.createRow(i);
            for (int j = 0; j < classificationRuns.get(i).size(); j++) {
                String fusionName = classificationRuns.get(i).get(j).getName();
                row.createCell(j).setCellValue(fusionName);
            }
        }
        hw.write(fos);
        fos.close();
    }
    public static void runProgram(String performanceXLSPath,String classificationXLSPath, String outputClassificationTopPerformancePath,String outputClassificationSortPerformancePath
    ,int startNum,int endNum,int startExperimentNum,int endExperimentNum , String classificationPreName) throws IOException {
        readPerformanceXLS(performanceXLSPath);
        for (int NUM = startNum; NUM <= endNum ; NUM++) {
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= endExperimentNum; ExperimentNum++) {
                String classificationXLSFilePath = classificationXLSPath+"\\"+NUM+"\\"+classificationPreName+NUM+"_"+ExperimentNum+".xls";
                System.out.println(classificationXLSFilePath);
                readClassificationXLS(classificationXLSFilePath);
                //若outputClassificationTopPerformancePath路径存在则输出的xls只保留第一个run
                if (!outputClassificationTopPerformancePath.equals("")){
                    String outputClassificationTopPerformanceFilePath = outputClassificationTopPerformancePath+"\\"+NUM+"\\";
                    createDirectory(outputClassificationTopPerformanceFilePath);
                    outputClassificationTopPerformanceFilePath = outputClassificationTopPerformanceFilePath +"sort"+classificationPreName+NUM+"_"+ExperimentNum+".xls";
                    System.out.println(outputClassificationTopPerformanceFilePath);
                    writeTopPerformanceXLS( outputClassificationTopPerformanceFilePath);
                }
                //若outputClassificationSortPerformanceFilePath路径存在则输出的xls保留所有的run
                if (!outputClassificationSortPerformancePath.equals("")){
                    String outputClassificationSortPerformanceFilePath = outputClassificationSortPerformancePath+"\\"+NUM+"\\";
                    createDirectory(outputClassificationSortPerformanceFilePath);
                    outputClassificationSortPerformanceFilePath = outputClassificationSortPerformanceFilePath +"sort"+classificationPreName+NUM+"_"+ExperimentNum+".xls";
                    System.out.println(outputClassificationSortPerformanceFilePath);
                    writeSortPerformanceXLS(outputClassificationSortPerformanceFilePath);
                }
                clearGlobalVariable();
            }
        }
    }

    private static void clearGlobalVariable() {
        classificationRuns.clear();
    }

    public static void main(String[] args) throws IOException {
        String performanceXLSPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\2020Health_evalUCCndcg.xls";
        String classificationXLSPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\classification\\Birch\\";
//        String outputClassificationTopPerformancePath = "D:\\myIdeaProject\\TREC\\2021health\\input\\classification\\sortFusion\\TopNdcgUCCsort\\CHC\\";
        String outputClassificationTopPerformancePath = "";//每个类保留单个run
        String outputClassificationSortPerformancePath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\classification\\sortClassificationCluster\\Birch\\TopNdcgUCC";//每个类保留全部run
        String classificationPreName = "Birch_k";// CHC_k  KMeans_k  HC_k Birch_k sortBirch_k
        int startNum = 12;
        int endNum = 12;
        int startExperimentNum = 1;
        int endExperimentNum = 1;
        runProgram(performanceXLSPath,classificationXLSPath,outputClassificationTopPerformancePath,outputClassificationSortPerformancePath,startNum,endNum,startExperimentNum,endExperimentNum,classificationPreName);
    }

}
