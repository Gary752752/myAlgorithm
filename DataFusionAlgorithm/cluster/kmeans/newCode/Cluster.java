package com.algorithm.cluster.kmeans.newCode;

import com.algorithm.DataStruct.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Cluster {

    public int clusterID;
    public ArrayList<Point> Pointlist;//该类的点
    public ArrayList<Result> coordinateList;//坐标
    public Cluster(int clusterID) throws IOException {
        Pointlist = new ArrayList<>();
        this.clusterID=clusterID;
    }
    public void read_old_CoordinateList(String old_clusterPath) throws IOException {
        this.coordinateList = new ArrayList<>();
        File oldCluster = new File(old_clusterPath);
        BufferedReader reader=new BufferedReader(new FileReader(oldCluster));
        String bufferline = new String(reader.readLine());
        StringTokenizer tokenizer=new StringTokenizer(bufferline);
        //System.out.println(bufferline);
        //topic
        int topic=Integer.parseInt(tokenizer.nextToken());
        //zoey 读取文档id
        String docid=tokenizer.nextToken();
        //获取分数
        double score =Double.parseDouble(tokenizer.nextToken()) ;
        this.coordinateList.add(new Result(topic, docid, score));
        while((bufferline=reader.readLine())!=null) {
            tokenizer=new StringTokenizer(bufferline);
            topic=Integer.parseInt(tokenizer.nextToken());
            docid=tokenizer.nextToken();
            score =Double.parseDouble(tokenizer.nextToken()) ;
            this.coordinateList.add(new Result(topic, docid, score));
        }
        reader.close();
    }
    public void write_new_CoordinateList(String new_clusterPath)throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(new_clusterPath));
        String bwLine ;
        int coordinateList_size = this.coordinateList.size();
        for (int i = 0; i < coordinateList_size; i++) {
            Result oneLine = this.coordinateList.get(i);
            bwLine = oneLine.getTopic()+"\t"+oneLine.getDocID()+"\t"+ oneLine.getScore()+"\n";
            //System.out.println(oneLine.getTopic()+oneLine.getDocID()+oneLine.getRank()+oneLine.getScore());
            bw.write(bwLine);
        }
        bw.close();
    };
    public void createCoordinateList() throws IOException {
        this.coordinateList = new ArrayList<>();
        File nullpoint = new File("D:\\myIdeaProject\\TREC\\2021health\\nullPoint");
        //File nullpoint = new File("health2020\\test_center\\testnullcenter");//测试
        BufferedReader reader=new BufferedReader(new FileReader(nullpoint));
        String bufferline = new String(reader.readLine());
        StringTokenizer tokenizer=new StringTokenizer(bufferline);
        //System.out.println(bufferline);
        //topic
        int topic=Integer.parseInt(tokenizer.nextToken());
        //zoey 读取文档id
        String docid=tokenizer.nextToken();

        this.coordinateList.add(new Result(topic, docid, 0));
        while((bufferline=reader.readLine())!=null) {
            tokenizer=new StringTokenizer(bufferline);
            topic=Integer.parseInt(tokenizer.nextToken());
            docid=tokenizer.nextToken();
            this.coordinateList.add(new Result(topic, docid, 0));
        }
        reader.close();
    }
    public void clearPoint() {//将该组的所有点清空
        Pointlist.clear();
    }
    public void ZeroCoordinateList() {//将坐标位置的分数清零
        for(int i=0;i<coordinateList.size();i++) {
            coordinateList.get(i).setScore(0);
        }
    }
    public int getClusterID() {
        return clusterID;
    }
    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public void setCoordinateList(ArrayList<Result> coordinateList) {
        this.coordinateList = coordinateList;
    }
}
