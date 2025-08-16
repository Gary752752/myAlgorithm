package com.algorithm.Distance;


import com.algorithm.DataStruct.HashMapDocKey;
import com.algorithm.DataStruct.HashMapDocValue;
import com.algorithm.DataStruct.Result;
import com.algorithm.DataStruct.Results;
import com.algorithm.cluster.hierarchical.agglomerative1.ClusterPoint;
import com.algorithm.cluster.hierarchical.agglomerative1.RunPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class StandardDistance {
    /**
     * 1.计算两个run之间的距离
     *  1.1 先将一个run加载到一个arraylist对象中，其中每个对象都是一个以topic进行分割的arraylist对象
     */


    public static boolean existTopic(int topic){
        for (int t : topics) {
            if(t==topic){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Results> getRun(String runPath) throws IOException {
        ArrayList<Results> Run = new ArrayList<>();
        for (int topic : topics) {
            Run.add(getTopic(runPath,topic));
        }
        return Run;
    }

    private static Results getTopic(String runPath, int this_topic) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        Results topic_Results = new Results(this_topic);
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            //String date = str[0];
            int topic = Integer.parseInt(str[0].replaceAll("MB",""));

            if (topic == this_topic){
                String docID = str[2];
                int rank = Integer.parseInt(str[3]);
                double score = Double.parseDouble(str[4]);
                score = 30.0/(rank+60.0);//使用30/60+rank对run进行分数归一化
                String systemName = str[5];
                /*
                    当有date属性时将date数据连同docID属性链接到一起
                 */
                topic_Results.getList().add(new Result(topic,docID,score,rank,systemName));

            }
        }
        return topic_Results;
    }

    public static void runA_runB_Distance(String RunsAPath,String RunsBPath) throws IOException {
        File[] filesA = new File(RunsAPath).listFiles();
        File[] filesB = new File(RunsBPath).listFiles();
        double[][] distanceMatrix = new double[filesA.length][filesB.length];
        System.out.println(filesA.length);
        System.out.println(filesB.length);
        int lengthA = 0;
        int lengthB = 0;
        for (File RunA : filesA) {
            ArrayList<Results> runA = getRun(RunA.toString());
            System.out.println(RunA.getName()+"\t");
            lengthB=0;
            for (File RunB : filesB) {
                System.out.print(RunB.getName()+"\t");
                ArrayList<Results> runB = getRun(RunB.toString());
                double distance = 0.0;
                distance = distanceAB(runA,runB);
                distanceMatrix[lengthA][lengthB]=distance;
                System.out.println(distance);
                lengthB++;
            }
            lengthA++;
        }

        showMatrix(filesA,filesB,distanceMatrix);
    }

    private static double distanceAB(ArrayList<Results> runA, ArrayList<Results> runB) {
        double distance = 0.0;
        for (Results resultsA : runA) {

            for (Results resultsB : runB) {
                if (resultsA.getTopic()==resultsB.getTopic()){

                    for (Result resultA : resultsA.getList()) {
                        for (Result resultB : resultsB.getList()) {
                            if (resultA.equals(resultB)){
                                distance += Math.pow(resultA.getScore()-resultB.getScore(),2) ;
                            }
                        }
                    }
                    for (Result resultA : resultsA.getList()) {
                        if (!resultsB.getList().contains(resultA)){
                            distance +=Math.pow(resultA.getScore(),2) ;
                        }
                    }
                    for (Result resultB : resultsB.getList()) {
                        if (!resultsA.getList().contains(resultB)){
                            distance +=Math.pow(resultB.getScore(),2) ;
                        }
                    }
                }
            }
        }
        return Math.sqrt(distance);
    }

    public static void showMatrix(File[] filesA,File[] filesB,double[][] distanceMatrix){
        System.out.print("\t");
        for (File runB : filesB) {
            System.out.print(runB.getName()+"\t");
        }
        System.out.println();
        for (int i = 0; i < distanceMatrix.length; i++) {
            System.out.print(filesA[i].getName()+"\t");
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                System.out.print(distanceMatrix[i][j]+"\t");
            }
            System.out.println();
        }

    }
    private static HashMap<HashMapDocKey, HashMapDocValue> readRunBToHashMapList(String runBPath) throws IOException {
        HashMap<HashMapDocKey, HashMapDocValue> runB = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(runBPath));
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            String docID = str[2];
            int rank = Integer.parseInt(str[3]);
            double score = Double.parseDouble(str[4]);
            String systemName = str[5];
            HashMapDocKey hashMapDocKey = new HashMapDocKey(topic,docID);
            HashMapDocValue hashMapDocValue = new HashMapDocValue(rank,score,systemName);
            runB.put(hashMapDocKey,hashMapDocValue);
        }
        br.close();
        return runB;
    }

    public static void setTopics(int[] topics) {
        StandardDistance.topics = topics;
    }
    public static void initTopics(int[] topics){
        setTopics(topics);
    }

    /**
     * 输入需要比较的两个run的路径进行比较，返回他们的距离
     * @param runA runA的路径
     * @param runB runB的路径
     * @return 距离
     * @throws IOException 异常抛出
     */
    /*public static double getRunARunBDistance(String runA,String runB) throws IOException {

        double diss = 0;
        ArrayList<Results> OBJrunA = getRun(runA);
        ArrayList<Results> OBJrunB = getRun(runB);
        diss = distanceAB(OBJrunA,OBJrunB);
        return diss;
    }*/

    /**
     * 输入需要比较的两个run，一个是hashMap格式一个是路径，返回他们的距离
     * @param runA hashMap对象
     * @param runBPath 路径
     * @return 距离
     * @throws IOException 异常抛出
     */
    public static double getRunARunBDistance(HashMap<HashMapDocKey, HashMapDocValue> runA,String runBPath) throws IOException {
        double distance = 0.0;
        HashMap<HashMapDocKey, HashMapDocValue> runB = readRunBToHashMapList(runBPath);
        for (Map.Entry<HashMapDocKey, HashMapDocValue> runAEntry : runA.entrySet()) {
            HashMapDocKey runAKey = runAEntry.getKey();
            HashMapDocValue runAValue = runAEntry.getValue();
            if (runB.containsKey(runAKey)){
                distance += Math.pow(runAValue.getScore() - runB.get(runAKey).getScore(),2);
            }else {
                distance += Math.pow(runAValue.getScore(),2);
            }
        }
        for (Map.Entry<HashMapDocKey, HashMapDocValue> runBEntry : runB.entrySet()) {
            HashMapDocKey runBKey = runBEntry.getKey();
            HashMapDocValue runBValue = runBEntry.getValue();
            if (runA.containsKey(runBKey)){
                //略过
            }else {
                distance += Math.pow(runBValue.getScore(),2);
            }
        }
        return Math.sqrt(distance);
    }
    public static double getRunARunBDistance(String runAPath,String runBPath) throws IOException {
        double distance = 0.0;
        HashMap<HashMapDocKey, HashMapDocValue> runA = readRunBToHashMapList(runAPath);
        HashMap<HashMapDocKey, HashMapDocValue> runB = readRunBToHashMapList(runBPath);

        for (Map.Entry<HashMapDocKey, HashMapDocValue> runAEntry : runA.entrySet()) {
            HashMapDocKey runAKey = runAEntry.getKey();
            HashMapDocValue runAValue = runAEntry.getValue();
            if (runB.containsKey(runAKey)){
                distance += Math.pow(runAValue.getScore() - runB.get(runAKey).getScore(),2);
            }else {
                distance += Math.pow(runAValue.getScore(),2);
            }
        }
        for (Map.Entry<HashMapDocKey, HashMapDocValue> runBEntry : runB.entrySet()) {
            HashMapDocKey runBKey = runBEntry.getKey();
            HashMapDocValue runBValue = runBEntry.getValue();
            if (runA.containsKey(runBKey)){
                //略过
            }else {
                distance += Math.pow(runBValue.getScore(),2);
            }
        }
        return Math.sqrt(distance);
    }
    public static double getRunARunBDistance(ClusterPoint runA,ClusterPoint runB){
        double distance = 0.0;
        //System.out.println(runA.getClusterID()+"\t"+runB.getClusterID());
        for (Map.Entry<HashMapDocKey, HashMapDocValue> AEntry : runA.getDimensionList().entrySet()) {
            HashMapDocKey AKey = AEntry.getKey();
            HashMapDocValue AValue = AEntry.getValue();
            HashMapDocValue BValue = runB.getDimensionList().get(AKey);
            if (BValue != null){
                distance += Math.pow((AValue.getScore()-BValue.getScore()),2);
            }else {
                distance += Math.pow(AValue.getScore(),2);
            }
        }
        for (Map.Entry<HashMapDocKey, HashMapDocValue> BEntry : runB.getDimensionList().entrySet()) {
            HashMapDocKey BKey = BEntry.getKey();
            HashMapDocValue BValue = BEntry.getValue();
            HashMapDocValue AValue = runA.getDimensionList().get(BKey);
            if (AValue == null){
                distance += Math.pow(BValue.getScore(),2);
            }
        };
        return Math.sqrt(distance);
    }

    public static double getRunARunBDistance(HashMap<HashMapDocKey, HashMapDocValue> runA,HashMap<HashMapDocKey, HashMapDocValue> runB) throws IOException {
        double distance = 0.0;
        for (Map.Entry<HashMapDocKey, HashMapDocValue> runAEntry : runA.entrySet()) {
            HashMapDocKey runAKey = runAEntry.getKey();
            HashMapDocValue runAValue = runAEntry.getValue();
            if (runB.containsKey(runAKey)){
                distance += Math.pow(runAValue.getScore() - runB.get(runAKey).getScore(),2);
            }else {
                distance +=Math.pow(runAValue.getScore(),2) ;
            }
        }
        for (Map.Entry<HashMapDocKey, HashMapDocValue> runBEntry : runB.entrySet()) {
            HashMapDocKey runBKey = runBEntry.getKey();
            HashMapDocValue runBValue = runBEntry.getValue();
            if (runA.containsKey(runBKey)){
                //略过
            }else {
                distance +=Math.pow(runBValue.getScore(),2) ;
            }
        }
        return Math.sqrt(distance);
    }
    public static double getRunARunBDistance(RunPoint runPointA, RunPoint runPointB){
        double distance = 0.0;

        for (Map.Entry<HashMapDocKey, HashMapDocValue> runAEntry : runPointA.getDimensionList().entrySet()) {
            HashMapDocKey runAKey = runAEntry.getKey();
            HashMapDocValue runAValue = runAEntry.getValue();
            if (runPointB.getDimensionList().containsKey(runAKey)){
                distance += Math.pow(runAValue.getScore()-runPointB.getDimensionList().get(runAKey).getScore(),2);
            }else {
                distance += Math.pow(runAValue.getScore(),2);
            }
        }
        for (Map.Entry<HashMapDocKey, HashMapDocValue> runBEntry : runPointB.getDimensionList().entrySet()) {
            HashMapDocKey runBKey = runBEntry.getKey();
            HashMapDocValue runBValue = runBEntry.getValue();
            if (!runPointA.getDimensionList().containsKey(runBKey)){
                distance += Math.pow(runBValue.getScore(),2);
            }
        }
        return Math.sqrt(distance);
    }
    public static double getRunARunBDistance(ClusterPoint clusterPoint, RunPoint runPoint){
        double distance = 0.0;

        for (Map.Entry<HashMapDocKey, HashMapDocValue> runAEntry : clusterPoint.getDimensionList().entrySet()) {
            HashMapDocKey runAKey = runAEntry.getKey();
            HashMapDocValue runAValue = runAEntry.getValue();
            if (runPoint.getDimensionList().containsKey(runAKey)){
                distance += Math.pow(runAValue.getScore()-runPoint.getDimensionList().get(runAKey).getScore(),2);
            }else {
                distance += Math.pow(runAValue.getScore(),2);
            }
        }
        for (Map.Entry<HashMapDocKey, HashMapDocValue> runBEntry : runPoint.getDimensionList().entrySet()) {
            HashMapDocKey runBKey = runBEntry.getKey();
            HashMapDocValue runBValue = runBEntry.getValue();
            if (!clusterPoint.getDimensionList().containsKey(runBKey)){
                distance += Math.pow(runBValue.getScore(),2);
            }
        }

        return Math.sqrt(distance);
    }


    public static void main(String[] args) throws IOException {
        String RunsAPath = "D:\\myIdeaProject\\TRECdataset\\2021health\\standard_input_nor30_60";
        String RunsBPath = "D:\\myIdeaProject\\TRECdataset\\2021health\\standard_input_nor30_60";
        Calendar c1 = Calendar.getInstance();
        Date time1 = c1.getTime();

        runA_runB_Distance(RunsAPath,RunsBPath);
        Date time2 = c1.getTime();
        System.out.println(time2.getTime()-time1.getTime());

    }
    public static int[] topics ={
            102,103,104,105,106,107,108,109,110,111,
            114,115,117,118,120,121,122,127,128,129,
            131,132,133,134,136,137,139,140,143,144,
            145,146,149,};//需留下的topic
}
