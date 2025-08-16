package com.algorithm.fusion_Main;

import com.algorithm.LCfusion.Main.StardardFusionMain.FusionMain;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class fusion3_Main {

    private static String[] readXls(String xls_path,int Num) throws IOException {
        String[] fusionNames = new String[Num];

        FileInputStream fip = new FileInputStream(xls_path);
        HSSFWorkbook rb = new HSSFWorkbook(fip);
        Sheet sheet = rb.getSheetAt(0);
        Row row;

        for (int rowNum = 0; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
            row = sheet.getRow(rowNum);
            String fusionName = row.getCell(0).getStringCellValue().replaceAll("###","");
            fusionNames[rowNum] = fusionName;
        }
        fip.close();
        return fusionNames;
    }
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    private static void RunProgram(String runsPath,String classificationPath,String fusionPath,int[] topicsInt,int[] all_topic_ji,int[] all_topic_ou,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
            ,String Generatefile_output,String weights_output,String qrelsPath) throws Exception {
        FusionMain.setAll_topic(topicsInt);
        FusionMain.setAll_topic_ji(all_topic_ji);
        FusionMain.setAll_topic_ou(all_topic_ou);
        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum ; ExperimentNum++) {
                System.err.println();
                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\"+xmlPreName+Num+"_"+ExperimentNum+".xls";
                String[] fusionNames = readXls(xls_path,Num);
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    fusionNames[NameNum] = runsPath + fusionNames[NameNum];
                }

                String FusionFileName = "LCfusion_"+Num+"_"+ExperimentNum;
                FusionMain.setSystemName(FusionFileName);
                String outputFusionPath = outputPath + FusionFileName;
                //CombSUM.RunProgram(fusionNames,FusionFileName,outputFusionPath,topicsInt);
                FusionMain.RunProgram(fusionNames.length,2,fusionNames,runsPath,qrelsPath,ExperimentNum,Generatefile_output,
                        weights_output,outputFusionPath);
            }
        }

    }
    public static String xmlPreName= "sortBirch_k";
    public static void main(String[] args) throws Exception {
        String runsPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60\\";
        int[] topicsInt ={
                1,2,4,5,6,7,8,9,10,12,
                13,14,15,16,18,19,20,21,22,23,
                24,25,27,29,30,31,34,36,37,39,
                40,41,43,47,49,50,
        };
        int[] all_topic_ji = {
                1,4,6,8,10,
                13,15,18,20,22,
                24,27,30,34,37,
                40,43,49,
        };
        int[] all_topic_ou = {
                2,5,7,9,12,
                14,16,19,21,23,
                25,29,31,36,39,
                41,47,50,
        };
        int StartNum = 2;
        int EndNum = 12;
        int startExperimentNum = 1;
        int EndExperimentNum =1;
        String classificationPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\classification\\sortFusion\\TopNdcgUCCsort\\Birch\\";
        String fusion =             "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\20250108\\TopUCCndcg\\TopUCCndcg_Birch_fusion3_3_UCC";
        String fusionPath =         fusion+"\\fusion\\";
        String Generatefile_output= fusion+"\\LCfusion\\Generatefile\\";
        String weights_output=      fusion+"\\LCfusion\\Weightsfile\\";
        //String qrelsPath=           "D:\\myIdeaProject\\TREC\\2021health\\judgmentFile\\misinfo-resources-2021\\qrels\\2021-derived-qrels\\misinfo-qrels-binary.useful";
        //String qrelsPath=           "D:\\myIdeaProject\\TREC\\2021health\\judgmentFile\\misinfo-resources-2021\\qrels\\2021-derived-qrels\\misinfo-qrels-binary.useful-correct-credible";

        //String qrelsPath=           "D:\\myIdeaProject\\TREC\\2020Health\\judgment\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful";
        String qrelsPath=           "D:\\myIdeaProject\\TREC\\2020Health\\judgment\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-correct-credible";
        RunProgram(runsPath,classificationPath,fusionPath,topicsInt,all_topic_ji,all_topic_ou,StartNum,EndNum,startExperimentNum,EndExperimentNum,
                Generatefile_output,weights_output,qrelsPath);
    }


}
