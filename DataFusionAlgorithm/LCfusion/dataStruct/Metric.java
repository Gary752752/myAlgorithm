package com.algorithm.LCfusion.dataStruct;

public class Metric {
    public static enum BinaryMetric{
        map(0),recip_rank(1),P_5(2),P_10(3),P_15(4),P_20(5),P_30(6),P_100(7);

        private int value;
        private BinaryMetric(int value){
            this.value=value;
        }
        public int getValue(){
            return value;
        }
    }
    public static enum GradedMetric{
        ERR_20(0),NDCG_20(1);//need to be changed

        private int value;
        private GradedMetric(int value){
            this.value=value;
        }
        public int getValue(){
            return value;
        }
    }
    public static enum DivMetric{
        ERR_IA_20(3);
        private int value;
        private DivMetric(int value){
            this.value=value;
        }
        public int getValue(){
            return value;
        }
    }
}

