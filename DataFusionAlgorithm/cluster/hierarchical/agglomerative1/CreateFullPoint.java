package com.algorithm.cluster.hierarchical.agglomerative1;

import com.algorithm.DataStruct.*;

import java.io.*;
import java.util.*;

public class CreateFullPoint {

    private static ArrayList<ArrayDocs> finalList = new ArrayList<>();
    private static ArrayList<ArrayTreeMapOneTopicDocList> arrayTreeMapList = new ArrayList<>();

    private static void getTotalRun(String runPath,Integer[] topics) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        List<Integer> topicsList = Arrays.asList(topics);
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            if (topicsList.contains(topic)){
                String docID = str[2];
                int rank = Integer.parseInt(str[3]);
                double score = Double.parseDouble(str[4]);
                String systemName = str[5];
                HashMapDocKey DKey = new HashMapDocKey(topic,docID);
                HashMapDocValue DValue = new HashMapDocValue();
                for (ArrayTreeMapOneTopicDocList arrayTreeMapOneTopicDocList : arrayTreeMapList) {
                    if (arrayTreeMapOneTopicDocList.getTopic() == topic){
                        arrayTreeMapOneTopicDocList.getOneTopicDocList().put(DKey,DValue);
                        break;
                    }
                }
            }
        }
        br.close();
    }
    private static void nor_FullPoint(String runPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            String docID = str[2];
            int rank = Integer.parseInt(str[3]);
            double score = Double.parseDouble(str[4]);
            String systemName = str[5];
            HashMapDocKey hashMapDocKey = new HashMapDocKey(topic,docID);
            for (ArrayTreeMapOneTopicDocList arrayTreeMapOneTopicDocList : arrayTreeMapList) {
                if (arrayTreeMapOneTopicDocList.getTopic() == topic){
                    HashMapDocValue nor_value = arrayTreeMapOneTopicDocList.getOneTopicDocList().get(hashMapDocKey);
                    nor_value.setRank(rank);
                    nor_value.setScore(score);
                    nor_value.setSystemName(systemName);
                }
            }
        }

    }
    private static void writeFinalList(String writePath,String systemName) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(writePath));

        for (ArrayDocs finalDocs : finalList) {

            for (Doc finalDoc : finalDocs.getList()) {
                String line = "";
                line += finalDoc.getTopic()+"\t";
                line += "Q0"+"\t";
                line += finalDoc.getDocID()+"\t";
                line += 0+"\t";
                line += finalDoc.getScore()+"\t";
                line += systemName + "\n";
                bw.write(line);
            }
        }
        bw.close();
    }
    private static void sortFinalList() {
        // 排序Docs
        for (int i = 0; i < finalList.size(); i++) {
            for (int j = 0; j < finalList.size(); j++) {
                if (finalList.get(i).getTopic() < finalList.get(j).getTopic()){
                    Collections.swap(finalList,i,j);
                }
            }
        }

    }
    private static void initFinalList(Integer[] topics) {
        for (Integer topic : topics) {
            ArrayDocs docs = new ArrayDocs(topic);
            finalList.add(docs);
        }
    }

    private static void initFusionMNZList(Integer[] topics) {
        for (Integer topic : topics) {
            ArrayTreeMapOneTopicDocList arrayTreeMapOneTopicDocList = new ArrayTreeMapOneTopicDocList(topic);
            arrayTreeMapList.add(arrayTreeMapOneTopicDocList);
        }
    }
    private static void getFinalList() {
        for (ArrayDocs arrayDocs : finalList) {
            for (ArrayTreeMapOneTopicDocList arrayTreeMapOneTopicDocList : arrayTreeMapList) {
                if (arrayDocs.getTopic() == arrayTreeMapOneTopicDocList.getTopic()){
                    for (Map.Entry<HashMapDocKey, HashMapDocValue> treeMapDocKeyTreeMapDocValueEntry : arrayTreeMapOneTopicDocList.getOneTopicDocList().entrySet()) {
                        HashMapDocKey key = treeMapDocKeyTreeMapDocValueEntry.getKey();
                        HashMapDocValue value = treeMapDocKeyTreeMapDocValueEntry.getValue();
                        Integer topic = key.getTopic();
                        String docID = key.getDocID();
                        Integer rank = value.getRank();
                        Double score = value.getScore();
                        String systemName = value.getSystemName();
                        Integer mnz_num = value.getMNZ_Num();
                        arrayDocs.getList().add(new Doc(topic,docID,rank,score,systemName,mnz_num));
                    }
                    break;
                }
            }
        }
    }
    public static void runProgram(String runPaths,Integer[] topics,String outputPath,String systemName) throws IOException {
        initFusionMNZList(topics);
        File[] files = new File(runPaths).listFiles();
        for (File file : files) {
            System.out.println(file.getPath());
            getTotalRun(file.getPath(),topics);
        }

        for (File file : files) {
            System.out.println("nor--------\t"+file.getName());
            nor_FullPoint(file.getPath());
            initFinalList(topics);
            getFinalList();
            sortFinalList();
            writeFinalList(outputPath+"\\"+file.getName(),file.getName());
            frasharrayTreeMapList();
            finalList.clear();
        }
        //getFinalList();
        //sortFinalList();
        //writeFinalList(outputPath,systemName);
    }

    private static void frasharrayTreeMapList() {
        for (ArrayTreeMapOneTopicDocList arrayTreeMapOneTopicDocList : arrayTreeMapList) {
            for (Map.Entry<HashMapDocKey, HashMapDocValue> hashMapDocKeyHashMapDocValueEntry : arrayTreeMapOneTopicDocList.getOneTopicDocList().entrySet()) {
                HashMapDocValue value = hashMapDocKeyHashMapDocValueEntry.getValue();
                value.setRank(0);
                value.setScore(0.0);
                value.setSystemName("");

            }
        }
    }

    public static void main(String[] args) throws IOException {
        String runPaths = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2020Health\\Adhoc\\standard_input_nor30-60";
        Integer[] topics = {
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,34,36,37,38,39,40,
                41,42,43,44,45,47,49,50,
        };
        String outputPath = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2020Health\\Adhoc\\standard_input_nor30-60_Point";
        String systemName = "fullPoint";
        runProgram(runPaths,topics,outputPath,systemName);
    }
}
