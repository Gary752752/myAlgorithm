package com.algorithm.fusionAlgorithm;

import com.algorithm.DataStruct.ArrayDocs;
import com.algorithm.DataStruct.Doc;
import com.algorithm.DataStruct.Docs;
import com.algorithm.myUtil.XLS_Util.XLS_Util;

import java.io.*;
import java.util.*;

public class CombMNZ {
    /**
     * 此方法用于实现CombMNZ算法
     * 同时使用hashMap方法进行开发
     */
    /**
     * 1.创建一个finalList作为最终结果列表，每个topic列表作为其中的一个元素
     * 2.获得每一个run的信息并保持到一个list中，每个list的元素是一个topic列表
     *
     */
    public static ArrayList<ArrayDocs> finalList = new ArrayList<>();
    public static HashMap<Doc,Integer> totalMNZ = new HashMap<>();


    public static Docs getDocs(String runPath, int getTopic) throws IOException {
        Docs docs = new Docs(getTopic);
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        String line = "";
        while ((line = br.readLine())!= null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            if (topic == getTopic){
                String docID = str[2];
                int rank = Integer.parseInt(str[3]);
                double score = Double.parseDouble(str[4]);
                String systemName = str[5];
                Doc d = new Doc(docID,topic);
                d.setRank(rank);
                d.setScore(score);
                d.setSystemName(systemName);
                docs.getList().add(d);
                if (totalMNZ.containsKey(d)){
                    Integer integer = totalMNZ.get(d);
                    totalMNZ.put(d,integer+1);
                }else {
                    totalMNZ.put(d,1);
                }
            }
            br.close();
        }
        return docs;
    }

    public static ArrayList<Docs> getOneRun(String runPath,int[] topics) throws IOException {
        ArrayList<Docs> oneRun = new ArrayList<>();
        for (int topic : topics) {
            Docs docs = getDocs(runPath, topic);
            oneRun.add(docs);
        }
        return oneRun;
    }

    public static void calculateCombMNZ(String[] runPaths, int[] topics) throws IOException {
        initFinalList(topics);
        for (String runPath : runPaths) {
            System.out.println(runPath);
            ArrayList<Docs> oneRun = getOneRun(runPath,topics);
            /*
            向finalList中添加数据先进行CombSUM计算，而后计算CombMNZ
             */
            joinFinalList(oneRun);
        }
        /*
        MNZ
         */
        for (ArrayDocs finalDocs : finalList) {
            for (Doc finalDoc : finalDocs.getList()) {
                Integer times = totalMNZ.get(finalDoc);
                finalDoc.setScore(finalDoc.getScore()*times);
            }
        }

        sortFinalList();


    }



    private static void sortFinalList() {
        /**
         * 排序Docs
         */
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

    private static void joinFinalList(ArrayList<Docs> oneRun) {
        for (Docs oneDocs : oneRun) {
            for (ArrayDocs finalDocs : finalList) {
                if (oneDocs.getTopic() == finalDocs.getTopic()){
                    for (Doc oneDoc : oneDocs.getList()) {
                        if (finalDocs.getList().contains(oneDoc)){
                            //存在当前doc就更新finalDocs中该doc的分数
                            Doc finalDoc = findDoc(finalDocs, oneDoc);
                            finalDoc.setScore(finalDoc.getScore()+oneDoc.getScore());
                        }else {
                            //不存在就进行添加
                            finalDocs.getList().add(oneDoc);
                        }
                    }
                }
            }
        }
    }

    private static Doc findDoc(ArrayDocs finalDocs, Doc oneDoc) {
        for (Doc finalDoc : finalDocs.getList()) {
            if (finalDoc.equals(oneDoc)){
                return finalDoc;
            }
        }
        return null;
    }


    private static void initFinalList(int[] topics) {
        for (int topic : topics) {
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
    public static void RunProgram(String runPaths,int[] topic,String outputPath,String SystemName,int NUM,String XLS_Path) throws IOException {
        String[] fusionNames = XLS_Util.getXLSFusionNames(XLS_Path,NUM);
        for (int i = 0; i < fusionNames.length; i++) {
            fusionNames[i] =runPaths+fusionNames[i];
        }
        calculateCombMNZ(fusionNames,topic);
        writeFinalList(outputPath,SystemName);
        clearGlobalVariable();
    }
    public static void clearGlobalVariable(){
        finalList.clear();
        totalMNZ.clear();
    }
    public static void main(String[] args) throws IOException {
        String runPaths ="E:\\TRECDataset\\test\\test_nor\\";
        int[] topic = {
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
