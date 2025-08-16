package com.algorithm.CreateQrel;

import com.algorithm.DataStruct.Result;
import com.algorithm.DataStruct.Results;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreateNullQrel {
    /**
     * 读取每一个run文件创建一个null的集合列表，列表中覆盖每个topic的所有文档;
     */
    /*
    1.创建NullQrel对象
    2.创建results对象集合，每一个results对象集合都是一个run
    3.将输入的run中的docID和topic存储进results对象中;
    4.不重复的输入到nullQrel对象中
     */
    public static ArrayList<Results> NullQrel = new ArrayList<>();

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
                //double score = Double.parseDouble(str[4]);
                Result r = new Result(topic,docID,rank);
                Rs.getList().add(r);
                rank++;
            }
        }
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
     * 把runsPath中所有的run添加至Nullqrel对象中
     * @param runsPath
     * @param topicsInt
     * @throws IOException
     */
    public static void addNullQrel(String runsPath,int[] topicsInt) throws IOException {
        File path = new File(runsPath);
        File[] runs = path.listFiles();
        for (File runPath : runs) {
            System.out.println(runPath);
            ArrayList<Results> run = getRun(runPath.toString(),topicsInt);

            compareNullQrel(run);
        }
    }

    /**
     * 将run中所有不重复的result添加到对应topic的Nullqrel中
     * @param run
     */
    public static void compareNullQrel(ArrayList<Results> run){
        for (Results results : run) {
            int topic = results.getTopic();
            if (existTopic(topic)){//如果存在topic在nullqrel中
                //System.out.println("旧topic开始输入");
                for (Results NullQrelResults : NullQrel) {
                    if (topic == NullQrelResults.getTopic()){
                        addDifferent(results,NullQrelResults);
                    }
                }
            }else {
                Results Rs = new Results(topic);
                //System.out.println("新topic开始输入");
                addDifferent(results,Rs);
                NullQrel.add(Rs);
            }
        }
    }

    /**
     * 将results和NullQrelResults中不同的对象添加到NullQrelResults中
     * @param results
     * @param NullQrelResults
     */
    public static void addDifferent(Results results,Results NullQrelResults){
        for (Result result : results.getList()) {
            NullQrelResults.getList().add(result);
        }
    }

    /**
     * 判断NUllQrel对象中是否包含该topic的Results对象
     * @param topic
     * @return
     */
    public static boolean existTopic(int topic){
        for (Results NullQrelResults : NullQrel) {
            if (topic == NullQrelResults.getTopic()){
                return true;
            }
        }
        return false;
    }
    public static void showNullQrel(){
        int count = 0;
        for (Results results : NullQrel) {
            System.out.println(results.getTopic()+"\t"+results.getList().size());
            count += results.getList().size();
        }
        System.out.println(count);
    }
    public static void main(String[] args) throws IOException {
        String runsPath = "D:\\TRECDataset\\new_idea3\\health2020\\nor_input";
        int[] topicsInt = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50};
        addNullQrel(runsPath,topicsInt);
        showNullQrel();
    }
}
