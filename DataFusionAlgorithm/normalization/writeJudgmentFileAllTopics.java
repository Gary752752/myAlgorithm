package com.algorithm.normalization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

public class writeJudgmentFileAllTopics {
    /*
    2021health U__
    101,102,103,104,105,106,107,108,109,110,
    111,112,114,115,117,118,120,121,122,127,
    128,129,131,132,133,134,136,137,139,140,
    143,144,145,146,149,
    2021health UC_
    102,103,104,105,106,107,108,109,110,111,
    112,114,115,117,118,120,121,122,127,128,
    129,131,132,133,134,136,137,139,140,143,
    144,145,146,149,
    2021health U_C
    102,103,104,105,106,107,108,109,110,111,
    114,115,117,118,120,121,122,127,128,129,
    131,132,133,134,136,137,139,140,143,144,
    145,146,149,
    2021health UCC
    102,103,104,105,106,107,108,109,110,111,
    114,115,117,118,120,121,122,127,128,129,
    131,132,133,134,136,137,139,140,143,144,
    145,146,149,
    * */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:\\myIdeaProject\\TREC\\2020Health\\judgment\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-credible"));
        TreeSet<Integer> topics = new TreeSet<>();
        String oneLine = "";
        while ((oneLine = br.readLine())!=null){
            String[] str = oneLine.split("\\s+");
            topics.add(Integer.parseInt(str[0]));
        }
        Iterator<Integer> iteratorTopics = topics.iterator();
        while (iteratorTopics.hasNext()){
            System.out.print(iteratorTopics.next()+",");
        }
        br.close();
    }
}
/*
useful
1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,34,36,37,38,39,40,41,42,43,44,45,47,49,50,
useful-correct
1,2,4,5,6,7,8,9,10,12,13,14,15,16,18,19,20,21,22,23,24,25,27,28,29,30,31,34,36,37,39,40,41,43,47,49,50,
useful-correct-credible
1,2,4,5,6,7,8,9,10,12,13,14,15,16,18,19,20,21,22,23,24,25,27,29,30,31,34,36,37,39,40,41,43,47,49,50,
useful-credible
1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,29,30,31,32,34,36,37,38,39,40,41,42,43,44,45,47,49,50,
 */

/*
useful
101,102,103,104,105,106,107,108,109,110,111,112,114,115,117,118,120,121,122,127,128,129,131,132,133,134,136,137,139,140,143,144,145,146,149,
//useful-correct
102,103,104,105,106,107,108,109,110,111,112,114,115,117,118,120,121,122,127,128,129,131,132,133,134,136,137,139,140,143,144,145,146,149,
useful-credible
101,102,103,104,105,106,107,108,109,110,111,112,114,115,117,118,120,121,122,127,128,129,131,132,133,134,136,137,139,140,143,144,145,146,149,
useful-correct-credible
102,103,104,105,106,107,108,109,110,111,114,115,117,118,120,121,122,127,128,129,131,132,133,134,136,137,139,140,143,144,145,146,149,
 */