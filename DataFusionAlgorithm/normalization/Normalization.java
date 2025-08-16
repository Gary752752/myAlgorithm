package com.algorithm.normalization;

import com.algorithm.DataStruct.Result;
import com.algorithm.DataStruct.Results;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Normalization {
    /**
     * 对run进行分数归一化
     * type1:30/60+rank;
     * type2:1/N,1/2*N,1/3*N...1/10*N;
     */
    public static int DEATH = 1000;

    /**
     *
     * @param runPath
     * @param getTopic
     * @return 返回runPath中以getTopic开头的result对象列表
     * @throws IOException
     */
    public static Results getResults(String runPath, int getTopic) throws IOException {
        Results Rs = new Results(getTopic);
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        String line = "";
        int rank = 1;
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            // System.out.println(line);
            int topic = Integer.parseInt(str[0].replaceAll("MB",""));
            if(topic==getTopic){
                String docID = str[2];
                double score = Double.parseDouble(str[4].replaceAll("-Infinity","-100").replaceAll("-nan","-100").replaceAll("-inf","0.0"));
                String systemName = str[5];
                Result r = new Result(topic,docID,rank,score,systemName);
                Rs.getList().add(r);
                rank++;
            }
        }
        br.close();

        Collections.sort(Rs.getList(), new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o1.getRank()- o2.getRank();
            }
        });

        return Rs;
    }

    /**
     * 将输入的run中的docID和topic存储进results对象中;
     * @param runPath
     * @param topicsInt
     * @throws IOException
     */
    public static ArrayList<Results> getRun(String runPath, int[] topicsInt) throws IOException {
        ArrayList<Results> run = new ArrayList<>();
        ArrayList<Integer> topics = changeInt(topicsInt);
        for (Integer topic : topics) {
            Results results = getResults(runPath, topic);
            //results.setSystemName(runPath.split("\\\\")[runPath.split("\\\\").length-1].replaceAll("input.",""));
            results.setSystemName(runPath.split("\\\\")[runPath.split("\\\\").length-1]);
            run.add(results);
        }
        Collections.sort(run, new Comparator<Results>() {
            @Override
            public int compare(Results o1, Results o2) {
                return o1.getTopic()-o2.getTopic();
            }
        });


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
     *
     * @param type
     * type==0 score == 30/60+rank
     * type==1 score == idea3
     * @param run 要进行赋值的run
     * @param runNum run的总个数
     */
    public static void setScore(int type,ArrayList<Results> run,int runNum){

        for (Results results : run) {

            double max = -1000000;
            double min = 1000000;
            for (Result result : results.getList()) {//获取当前results的最大最小值
                if (result.getScore()>=max){
                    max = result.getScore();
                }
                if (result.getScore()<=min){
                    min = result.getScore();
                }
            }
            for (Result result : results.getList()) {
                if (type == 0){
                    double score = 30.0/(60+result.getRank());
                    result.setScore(score);
                }
                if (type == 1){
                    double score = 0.0;
                    switch ((int) ((result.getRank()-1)/(runNum))){
                        case 0:
                            score = 1.0/runNum;break;
                        case 1:
                            score = 1.0/(runNum*2);break;
                        case 2:
                            score = 1.0/(runNum*3);break;
                        case 3:
                            score = 1.0/(runNum*4);break;
                        case 4:
                            score = 1.0/(runNum*5);break;
                        case 5:
                            score = 1.0/(runNum*6);break;
                        case 6:
                            score = 1.0/(runNum*7);break;
                        case 7:
                            score = 1.0/(runNum*8);break;
                        case 8:
                            score = 1.0/(runNum*9);break;
                        case 9:
                            score = 1.0/(runNum*10);break;
                    }
                    result.setScore(score);
                }
                if (type == 2){
                    double score = 10*30.0/(60+result.getRank());
                    result.setScore(score);
                }
                if (type == 3){
                    double score = result.getScore();
                    result.setScore(score);
                }
                if (type == 4){
                    double score = 1.0/(60+result.getRank());
                    result.setScore(score);
                }
                if (type == 5){
                    double score = 0.0;

                    // System.out.println(max+"\t"+min);
                    if (max-min!=0){
                        score = (result.getScore()-min)/(max-min);
                    }else{
                        score = (results.getList().size()-result.getRank()*1.0+1)/results.getList().size();
                    }

                    result.setScore(score);
                }
            }
        }
    }

    /**
     * 将run中的信息写入到文件中
     * @param writePath
     * @param run
     * @throws IOException
     */
    public static void writeRun(String writePath,ArrayList<Results> run) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(writePath));

        for (Results results : run) {
            System.out.println("write: "+results.getTopic()+"\t"+results.getList().size());
            for (Result result : results.getList()) {
                if (result.getRank() > DEATH){
                    break;
                }
                String line = "";
                line += result.getTopic()+"\t";
                line += "Q0"+"\t";
                line += result.getDocID()+"\t";
                line += result.getRank()+"\t";
                line += result.getScore()+"\t";
                line += results.getSystemName()+"\n";
                bw.write(line);
            }
        }
        bw.close();
    }

    /**
     *
     * @param runsPath 归一化文件夹
     * @param topicsInt 归一化挑选的topic
     * @param writePath 归一化输出文件夹
     * @param type 归一化分数类型
     * @throws IOException
     */
    public static void executeProgram(String runsPath,int[] topicsInt,String writePath,int type) throws IOException {
        File[] runsFile = new File(runsPath).listFiles();
        int runNum = runsFile.length; // 修改N参数
        for (File runPath : runsFile) {
            //String runName = runPath.getName().replaceAll("input.","");
            String runName = runPath.getName();
            System.out.println(runName);
            ArrayList<Results> run = getRun(runPath.toString(), topicsInt);
            if (type == 3){
                /*
                按照分数进行重排序
                */
                for (Results FResults : run) {

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
            }
            setScore(type,run,runNum);
            writeRun(writePath+runName,run);
        }
    }
    public static void main(String[] args) throws IOException {
        //String runsPath = "D:\\TREC数据集文件\\new_idea3\\health2020\\fusion3\\idea3_raw_input";
        //String writePath = "D:\\TREC数据集文件\\new_idea3\\health2020\\fusion3\\idea3_qrel_nor_input"+"\\";
        //for (int date=20150720;date<=20150729;date++){
        String runsPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input\\";
        String writePath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60\\";

        int[] topicsInt = {
                1,2,4,5,6,7,8,9,10,12,
                13,14,15,16,18,19,20,21,22,23,
                24,25,27,29,30,31,34,36,37,39,
                40,41,43,47,49,50,
        };
            /*
            type == 0  30.0/rank+60
            type == 1  按照前N个得1/N分 前2*N个得 1/2*N分 一直到 前10*N 得1/10*N分为止
            type == 2  10*30.0/rank+60
            type == 3  为原文档分数，此方法为了将原文档的规格进行再次排列
            type == 4  1.0/rank+60
            type == 5  0-1
             */
        int type =0;
        executeProgram(runsPath,topicsInt,writePath,type);
        //}


    }
}
