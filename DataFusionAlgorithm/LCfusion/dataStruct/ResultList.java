package com.algorithm.LCfusion.dataStruct;
import java.util.ArrayList;
public class ResultList{
    public int topic;
    public String system;
    public ArrayList<result> list;
    public ResultList(){
        list=new ArrayList<>();
    }
    public ResultList(int topic,String system){
        this.topic=topic;
        this.system=system;
        list=new ArrayList<>();
    }
    public boolean contains(String docid){
        for(result r:list){
            if(r.docid.equals(docid))
                return true;
        }
        return false;
    }
    public int findRank(String docid){
        for(result r:list){
            if(r.docid.equals(docid))
                return r.rank;
        }
        return -1;
    }
    public static void main(String[] args) throws Exception{
        System.out.println("dd");
    }
}
