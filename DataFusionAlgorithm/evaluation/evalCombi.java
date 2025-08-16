package com.algorithm.evaluation;

import com.algorithm.DataStruct.RunPerformanceValues;
import com.algorithm.Distance.StandardDistance;
import com.algorithm.myUtil.XLS_Util.XLS_Util;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 用来进行Combi值的评价，
 * Combi = 0.5 * (MAP/Max_MAP)+0.5 * (Dist / Max_Dist)
 * 输入：MAP为当前参与融合的成员系统的平均MAP，Dist同理为平均DIST，Max_MAP为最大MAP，Max_Dist为最大Dist。
 */
public class evalCombi {
    public static ArrayList<RunPerformanceValues> rpvList = new ArrayList<>();
    /**
     * 1.得到XLS的每个成员系统的MAP值，求平均
     * 2.得到XLS的距离矩阵，求平均
     */
    //1.
    public static void runProgram(String classificationPath,String performanceXLSPath,String runPaths,int startNUM,int endNUM,int startEXP,int endEXP) throws IOException {
        readPerformanceXLS(performanceXLSPath);
        for (int NUM = startNUM; NUM <= endNUM ; NUM++) {
            for (int EXP = startEXP; EXP <= endEXP ; EXP++) {
                System.out.print(NUM+"\t");
                String XLSPath = classificationPath +"\\"+NUM+"\\division_K"+NUM+"_"+EXP+".xls";
                String[] xlsFusionNames = XLS_Util.getXLSFusionNames(XLSPath, NUM);
                ArrayList<RunPerformanceValues> fusionSystemList = new ArrayList<>();
                for (String xlsFusionName : xlsFusionNames) {
                    Double runPerformance = findRunPerformance(xlsFusionName);
                    fusionSystemList.add(new RunPerformanceValues(xlsFusionName,runPerformance));

                    //System.out.println(xlsFusionName+"\t"+runPerformance);

                }
                calculatorIndex(fusionSystemList,runPaths);
            }
        }
    }

    private static void calculatorIndex(ArrayList<RunPerformanceValues> fusionSystemList,String runPaths) throws IOException {
        double meanMAP = 0.0;
        for (RunPerformanceValues runPerformanceValues : fusionSystemList) {
            meanMAP += runPerformanceValues.getPerformanceValues();
        }
        meanMAP = meanMAP / fusionSystemList.size();
        System.out.print("meanMAP:\t"+meanMAP);
        double meanDist = getMatrix(fusionSystemList,runPaths);
        System.out.println("\tmeanDist\t"+meanDist);
    }

    private static double getMatrix(ArrayList<RunPerformanceValues> fusionSystemList, String runPaths) throws IOException {
        double meanDist = 0.0;
        int count = 0;
        for (int i = 0; i < fusionSystemList.size(); i++) {
            for (int j = i+1; j < fusionSystemList.size(); j++) {
                String runAName = fusionSystemList.get(i).getName();
                String runBName = fusionSystemList.get(j).getName();
                String runAPath = runPaths + "\\" +runAName;
                String runBPath = runPaths + "\\" +runBName;
                double tempDist = StandardDistance.getRunARunBDistance(runAPath,runBPath);
                meanDist += tempDist;
                count++;
            }
        }
        //System.out.println("count\t"+count+"\t"+meanDist);
        return meanDist/count;
    }

    private static void readPerformanceXLS(String performanceXLSPath) throws IOException {
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
    private static Double findRunPerformance(String name) {
        for (RunPerformanceValues runPerformanceValues : rpvList) {
            if (runPerformanceValues.getName().equals(name)){
                return runPerformanceValues.getPerformanceValues();
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        String classificationPath="E:\\TRECDateset\\paperExperiment\\ideaOne\\2021Health\\adhoc\\fusion3_3\\jSortClassification\\";
        String performanceXLSPath="E:\\TRECDateset\\paperExperiment\\ideaOne\\2021Health\\adhoc\\eval\\evalMAP.xls";
        String runPaths="E:\\TRECDateset\\paperExperiment\\ideaOne\\2021Health\\adhoc\\standard_input_nor30-60\\";
        int startNUM = 2;
        int endNUM= 40;
        int startEXP= 1;
        int endEXP= 1;
        runProgram(classificationPath,performanceXLSPath,runPaths,startNUM,endNUM,startEXP,endEXP);
    }
}
