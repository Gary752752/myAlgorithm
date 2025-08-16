package com.algorithm.fusion_Main;

import com.algorithm.fusionAlgorithm.FastCombMNZ;
import com.algorithm.fusionAlgorithm.FastCombSUM;
import com.algorithm.myUtil.XLS_Util.XLS_Util;

import java.io.File;
import java.io.IOException;
import java.sql.Time;

public class fusion1_2_Main {
    /**
     * 1.读取xls文件，得到要融合的run的路径##共有两种nor文件类型
     * 2.CombSUM融合
     */
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    private static void RunProgram(String runsPath,String classificationPath,String fusionPath,Integer[] topicsInt,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
    ,String fuseName) throws IOException {
        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum ; ExperimentNum++) {
                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\"+xmlPreName+Num+"_"+ExperimentNum+".xls";//组合为xls路径

                //String[] fusionNames = readXls(xls_path,Num);
                String[] fusionNames = XLS_Util.getXLSFusionNames(xls_path,Num);
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    fusionNames[NameNum] = runsPath + fusionNames[NameNum];
                }

                String FusionFileName = fuseName+"_"+Num+"_"+ExperimentNum;

                String outputFusionPath = outputPath + FusionFileName;
                //CombSUM.RunProgram(fusionNames,FusionFileName,outputFusionPath,topicsInt);
                if (fuseName.equals("CombSUM")){
                    FastCombSUM.RunProgram(runsPath,topicsInt,outputFusionPath,FusionFileName,Num,xls_path);
                }else if (fuseName.equals("CombMNZ")) {
                    FastCombMNZ.RunProgram(runsPath,topicsInt,outputFusionPath,FusionFileName,Num,xls_path);
                }else{
                    System.err.println("fuseName error");
                }

            }

        }

    }
    public static String xmlPreName= "sortBirch_k";
    public static void main(String[] args) throws IOException {
        String runsPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60\\";
        Integer[] topicsInt ={
                1,2,4,5,6,7,8,9,10,12,
                13,14,15,16,18,19,20,21,22,23,
                24,25,27,29,30,31,34,36,37,39,
                40,41,43,47,49,50,
        };
        int StartNum = 2;
        int EndNum = 12;
        int startExperimentNum = 1;
        int EndExperimentNum =1;
        String fuseName = "CombMNZ";//CombSUM CombMNZ
        String classificationPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\classification\\sortFusion\\TopNdcgUCCsort\\Birch\\";
        String fusionPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\20250108\\TopUCCndcg\\TopUCCndcg_Birch_fusion3_2\\fusion\\";

        RunProgram(runsPath,classificationPath,fusionPath,topicsInt,StartNum,EndNum,startExperimentNum,EndExperimentNum,fuseName);
    }


}
