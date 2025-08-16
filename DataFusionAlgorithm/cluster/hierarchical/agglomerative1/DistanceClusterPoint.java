package com.algorithm.cluster.hierarchical.agglomerative1;

import com.algorithm.DataStruct.HashMapDocKey;
import com.algorithm.DataStruct.HashMapDocValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DistanceClusterPoint {
    public static Double distanceClusterPoint(ClusterPoint runA,ClusterPoint runB){
        Double distance = 0.0;
        for (Map.Entry<HashMapDocKey, HashMapDocValue> AEntry : runA.getDimensionList().entrySet()) {
            HashMapDocKey AKey = AEntry.getKey();
            HashMapDocValue AValue = AEntry.getValue();
            if (runB.getDimensionList().containsKey(AKey)){
                HashMapDocValue BValue = runB.getDimensionList().get(AKey);
                distance += Math.pow(AValue.getScore() - BValue.getScore(),2);
            }else {
                distance += Math.pow(AValue.getScore(),2);
            }
        }

        for (Map.Entry<HashMapDocKey, HashMapDocValue> BEntry : runB.getDimensionList().entrySet()) {
            HashMapDocKey BKey = BEntry.getKey();
            HashMapDocValue BValue = BEntry.getValue();
            if (!runB.getDimensionList().containsKey(BKey)){
                distance += Math.pow(BValue.getScore(),2);
            }
        }
        distance = Math.sqrt(distance);
        return distance;
    }


}
