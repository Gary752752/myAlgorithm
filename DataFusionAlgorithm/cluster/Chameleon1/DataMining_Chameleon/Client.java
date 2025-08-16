package com.algorithm.cluster.Chameleon1.DataMining_Chameleon;
import com.algorithm.cluster.CalculateDistanceMatrix;

import java.io.File;
import java.io.IOException;

/**
 * Chameleon(变色龙)两阶段聚类算法
 * @author lyq
 *
 */
public interface Client {
    public static void main(String[] args) throws IOException, Exception{
        //String filePath = "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\adhoc\\standard_input_nor1_60\\";
        //String filePath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60\\";
        String filePath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60\\";
        //k-近邻的k设置
        double k =1;
        //度量函数阈值
        double minMetric =1;

        ChameleonTool tool = new ChameleonTool(filePath, k, minMetric);
        ChameleonTool.setDistanceMatrix(CalculateDistanceMatrix.runProgram(filePath));

        tool.buildCluster();
    }
}
