package com.algorithm.fusionAlgorithm;

import com.algorithm.DataStruct.*;
import com.algorithm.myUtil.XLS_Util.XLS_Util;

import java.io.*;
import java.util.*;

/**
 * 使用Hashset集合实现CombMNZ算法
 */
public class FastCombMNZ {

    private static ArrayList<ArrayDocs> finalList = new ArrayList<>();

    private static ArrayList<ArrayTreeMapOneTopicDocList> arrayTreeMapList = new ArrayList<>();

    private static final Integer DEEP = 1000;
    private static void getTotalMNZ(String runPath,Integer[] topics) throws IOException {
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
                DValue.setRank(rank);
                DValue.setScore(score);
                DValue.setSystemName(systemName);
                for (ArrayTreeMapOneTopicDocList arrayTreeMapOneTopicDocList : arrayTreeMapList) {
                    if (arrayTreeMapOneTopicDocList.getTopic() == topic){
                        if (arrayTreeMapOneTopicDocList.getOneTopicDocList().containsKey(DKey)){
                            //若存在,修改值
                            HashMapDocValue treeMapDocValue = arrayTreeMapOneTopicDocList.getOneTopicDocList().get(DKey);
                            treeMapDocValue.setScore(treeMapDocValue.getScore()+score);
                            treeMapDocValue.setMNZ_Num(treeMapDocValue.getMNZ_Num()+1);
                        }else {
                            //若不存在直接添加
                            arrayTreeMapOneTopicDocList.getOneTopicDocList().put(DKey,DValue);
                        }
                        break;
                    }
                }
            }
        }
        br.close();
    }



    public static void calculateCombMNZ(String[] runPaths, Integer[] topics) throws IOException {
        initFinalList(topics);
        initFusionMNZList(topics);
        for (String runPath : runPaths) {
            System.out.println(runPath);
            getTotalMNZ(runPath,topics);
        }
        getFinalList();
        calculate();
        sortFinalList();
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

        for (ArrayDocs finalDocs : finalList) {
            for (int i = 0; i < finalDocs.getList().size(); i++) {
                for (int j = 0; j < finalDocs.getList().size(); j++) {
                    if (finalDocs.getList().get(i).getScore() > finalDocs.getList().get(j).getScore()){
                        Collections.swap(finalDocs.getList(),i,j);
                    }
                }
            }
            int rank = 1;
            for (Doc finalDoc : finalDocs.getList()) {
                finalDoc.setRank(rank++);
            }
        }
    }

    private static void calculate() {
        for (ArrayDocs arrayDocs : finalList) {
            for (Doc doc : arrayDocs.getList()) {
                doc.setScore(doc.getScore()* doc.getMNZ_NUM());
            }
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

    private static void initFusionMNZList(Integer[] topics) {
        for (Integer topic : topics) {
            ArrayTreeMapOneTopicDocList arrayTreeMapOneTopicDocList = new ArrayTreeMapOneTopicDocList(topic);
            arrayTreeMapList.add(arrayTreeMapOneTopicDocList);
        }
    }




    private static void initFinalList(Integer[] topics) {
        for (Integer topic : topics) {
            ArrayDocs docs = new ArrayDocs(topic);
            finalList.add(docs);
        }
    }
    private static void writeFinalList(String outputPath, String systemName) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));

        for (ArrayDocs finalDocs : finalList) {
            for (Doc finalDoc : finalDocs.getList()) {
                String line = "";
                line += finalDoc.getTopic()+"\t";
                line += "Q0"+"\t";
                line += finalDoc.getDocID()+"\t";
                line += finalDoc.getRank()+"\t";
                line += finalDoc.getScore()+"\t";
                line += systemName + "\n";
                bw.write(line);
            }
        }
        bw.close();
    }

    public static void RunProgram(String runPaths,Integer[] topic,String outputPath,String SystemName,int NUM,String XLS_Path) throws IOException {
        String[] fusionNames = XLS_Util.getXLSFusionNames(XLS_Path,NUM);
        for (int i = 0; i < fusionNames.length; i++) {
            fusionNames[i] =runPaths+fusionNames[i];
        }
        calculateCombMNZ(fusionNames,topic);
        writeFinalList(outputPath,SystemName);
        clearGlobalVariable();
    }
    private static void clearGlobalVariable() {
        finalList.clear();
        arrayTreeMapList.clear();
    }
    public static void main(String[] args) throws IOException {
        String runPaths ="E:\\TRECDataset\\test\\test_nor\\";
        Integer[] topic = {
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,
        };
        String SystemName = "CombMNZtest";
        String outputPath = "E:\\TRECDataset\\test\\fusion\\"+SystemName;
        int NUM = 14;
        String XLS_Path = "E:\\TRECDataset\\test\\classification\\14\\Semi_K14_1.xls";
        RunProgram(runPaths,topic,outputPath,SystemName,NUM,XLS_Path);
    }
}
