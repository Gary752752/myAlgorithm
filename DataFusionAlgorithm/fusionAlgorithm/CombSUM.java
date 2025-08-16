package com.algorithm.fusionAlgorithm;

import com.algorithm.DataStruct.Result;
import com.algorithm.DataStruct.Results;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * 对输入的系统进行combSUM融合
 */
public class CombSUM {
    /**
     * 1.获得每一个run，并以topic分割
     * 2.创建结果对象，以topic分割
     *  2.1 遍历每一个run 若是第一个run则把其全部添加至结果集中
     *  2.2 若是第2个或以上的run 将相同的docid的分数相加，将结果集没有的docID直接添加到结果集中
     * 3.输出结果对象
     */
    public static ArrayList<Results> finalResult = new ArrayList<>();
    public static final int DEEP = 1000;//单个topic输出结果最大排名个数
    /**
     *
     * @param runPath
     * @param getTopic
     * @return 返回runPath中以getTopic开头的result对象列表
     * @throws IOException
     */
    public static Results getResults(String runPath,int getTopic) throws IOException {
        Results Rs = new Results(getTopic);
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        String line = "";
        int rank = 1;
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            if(topic==getTopic){
                String docID = str[2];
                double score = Double.parseDouble(str[4]);
                // System.out.println(score);
                String systemName = str[5];
                Result r = new Result(topic,docID,rank,score,systemName);
                Rs.getList().add(r);
                rank++;
            }
        }
        br.close();
        return Rs;
    }

    /**
     * 将输入的run中的docID和topic存储进results对象中;
     * @param runPath
     * @param topicsInt
     * @throws IOException
     */
    public static ArrayList<Results> getRun(String runPath,int[] topicsInt) throws IOException {
        ArrayList<Results> run = new ArrayList<>();
        ArrayList<Integer> topics = changeInt(topicsInt);
        for (Integer topic : topics) {
            run.add(getResults(runPath,topic));
        }
        return run;
    }

    /**
     * 将int[]数组转换为集合对象
     * @param topicsInt
     * @return
     */
    public static ArrayList<Integer> changeInt(int[] topicsInt){
        ArrayList<Integer> topics = new ArrayList<>();
        for (int topic : topicsInt) {
            topics.add(topic);
        }
        return topics;
    }

    /**
     * 进行CombSUM融合
     * @param runsPath
     * @param topicsInt
     * @throws IOException
     */
    public static void CombSUM_fusion(String runsPath,int[] topicsInt,String outputPath,String FusionName) throws IOException {
        File path = new File(runsPath);
        File[] runs = path.listFiles();
        initFinalResult(topicsInt);
        for (File runPath : runs) {
            System.out.println(runPath);
            ArrayList<Results> run = getRun(runPath.toString(),topicsInt);
            Calculator(run,FusionName);
            System.out.println(finalResult.get(0).getList().size());
        }
        /**
         * 将finalResults按照topic排序
         */
        Collections.sort(finalResult, new Comparator<Results>() {
            @Override
            public int compare(Results o1, Results o2) {
                return o1.getTopic()- o2.getTopic();
            }
        });
        for (Results FResults : finalResult) {

            for (int i = 0; i < FResults.getList().size(); i++) {
                for (int j = 0; j < FResults.getList().size()-1; j++) {
                    if (FResults.getList().get(j).getScore()<FResults.getList().get(j+1).getScore()){
                        Collections.swap(FResults.getList(),j,j+1);
                    }
                }
            }


            int rank = 1;
            for (Result result : FResults.getList()) {
                result.setRank(rank++);
            }
        }
        System.out.println(finalResult.get(0).getList().size());
        output_fusion(outputPath);
    }
    /**
     * 进行CombSUM融合
     * @param fusionNames
     * @param topicsInt
     * @throws IOException
     */
    public static void CombSUM_fusion_fusionNames(String[] fusionNames,int[] topicsInt,String outputPath,String FusionName) throws IOException {

        initFinalResult(topicsInt);
        for (String runPath : fusionNames) {
            System.out.println(runPath);
            ArrayList<Results> run = getRun(runPath.toString(),topicsInt);
            Calculator(run,FusionName);
            System.out.println(finalResult.get(0).getList().size());
        }
        /**
         * 将finalResults按照topic排序
         */
        Collections.sort(finalResult, new Comparator<Results>() {
            @Override
            public int compare(Results o1, Results o2) {
                return o1.getTopic()- o2.getTopic();
            }
        });
        for (Results FResults : finalResult) {

            for (int i = 0; i < FResults.getList().size(); i++) {
                for (int j = 0; j < FResults.getList().size()-1; j++) {
                    if (FResults.getList().get(j).getScore()<FResults.getList().get(j+1).getScore()){
                        Collections.swap(FResults.getList(),j,j+1);
                    }
                }
            }


            int rank = 1;
            for (Result result : FResults.getList()) {
                result.setRank(rank++);
            }
        }
        System.out.println(finalResult.get(0).getList().size());
        output_fusion(outputPath);
    }

    private static void output_fusion(String outputPath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
        for (Results FResults : finalResult) {
            for (Result FResult : FResults.getList()) {
                if (FResult.getRank()>DEEP){
                    break;
                }
                bw.write(FResult.getTopic()+"\t"+
                        "Q0"+"\t"+
                        FResult.getDocID()+"\t"+
                        FResult.getRank()+"\t"+
                        FResult.getScore()+"\t"+
                        FResult.getSystemName()+"\n"
                );
                bw.flush();
            }
        }
        bw.close();
    }

    public static void Calculator(ArrayList<Results> run,String FusionName) {
        for (Results results : run) {
            for (Results fResults : finalResult) {
                if (results.getTopic() == fResults.getTopic()){//topic相等时进行docID的比较
                    for (Result result : results.getList()) {
                        if (fResults.getList().contains(result)){
                            for (Result FResult : fResults.getList()) {
                                if (result.equals(FResult)){
                                    FResult.setScore(FResult.getScore()+result.getScore());
                                }
                            }
                        }else{
                            fResults.getList().add(new Result(result.getTopic()
                                    ,result.getDocID()
                                    , result.getRank()
                                    , result.getScore()
                                    , "CombSUM_fusion"));
                        }
                    }
                }
            }
        }
    }

    public static void initFinalResult(int[] topicsInt){
        for (int topic : topicsInt) {
            finalResult.add(new Results(topic));
        }
    }
    public static void clearGlobalVariable(){
        finalResult.clear();
    }
    public static void RunProgram(String[] fusionNames,String FusionFileName,String outputPath,int[] topicsInt) throws IOException {
        CombSUM_fusion_fusionNames(fusionNames,topicsInt,outputPath,FusionFileName);
        clearGlobalVariable();
    }


    public static void main(String[] args) throws IOException {
        //String date = "20150729";
        //for (int date = 20150720; date <= 20150729 ; date++) {
        String runsPath = "D:\\TREC数据集文件\\2013Microblog\\adhoc\\fusion19\\standard_input_norSlide_ou\\";
        String FusionFileName = "CombSUM_fusion";
        String outputPath ="D:\\TREC数据集文件\\2013Microblog\\adhoc\\fusion19\\fusion_ou\\"+FusionFileName;
        int[] topicsInt ={
                112,114,116,118,120,
                122,124,126,128,130,
                132,134,136,138,140,
                142,144,146,148,150,
                152,154,156,158,160,
                162,164,166,168,170};

        //int[] topicsInt ={1,2,4,5,6,7,8,9,10,12,13,14,15,16,18,19,20,21,22,23,24,25,27,28,29,30,31,34,36,37,39,40,41,43,47,49,50};
//        String FusionFileName = date+"_CombSUM_fusion";
        /*int[] topicsInt ={171,172,173,174,175,176,177,178,179,180
                ,181,182,183,184,185,186,187,188,189,190
                ,191,192,193,194,195,196,197,198,199,200
                ,201,202,203,204,205,206,207,208,209,210
                ,211,212,213,214,215,216,217,218,219,220
                ,221,222,223,224,225};*/

        CombSUM_fusion(runsPath,topicsInt,outputPath,FusionFileName);
        finalResult.clear();
        //}
    }

}
