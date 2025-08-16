package com.algorithm.cluster.eval;

import com.algorithm.DataStruct.HashMapDocKey;
import com.algorithm.DataStruct.HashMapDocValue;
import com.algorithm.Distance.StandardDistance;
import com.algorithm.cluster.hierarchical.agglomerative1.ClusterPoint;
import com.algorithm.cluster.hierarchical.agglomerative1.RunPoint;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * 实现聚类结果的内部评价指标，DBI(Davies-Bouldin index),CH(Calinski-Harbasz Score) DI(Dunn index),
 */
public class evaluationInternal {
    public static void main(String[] args) throws IOException {
        int startNum = 2;
        int endNum = 12;
        int startEXP = 1;
        int endEXP = 1;
        String classificationResultXLSPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\classification\\sortClassificationCluster\\Birch\\TopNdcgUCC\\";
        String inputPaths = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60\\";
        String classificationPreName="sortBirch_k";// sortCHC_k  sortKMeans_k  sortHC_k sortBirch_k
        runProgram(classificationResultXLSPath,inputPaths,startNum,endNum,startEXP,endEXP,classificationPreName);
    }
    public static void runProgram(String classificationResultXLSPath,String inputPaths,int startNum,int endNum,int startEXP,int endEXP,String classificationPreName) throws IOException {
        System.out.println(classificationResultXLSPath);
        for (int NUM = startNum; NUM <= endNum; NUM++) {
            double mean_DBI =0.0;
            double mean_CH = 0.0;
            double mean_DI = 0.0;
            double mean_Silhouette = 0.0;
            double mean_Silhouette_SC = 0.0;
            for (int EXP = startEXP; EXP <= endEXP; EXP++) {
                String xlsName = classificationPreName+NUM+"_"+EXP+".xls";
                String classificationXLSNamePath = classificationResultXLSPath+"\\"+NUM+"\\"+xlsName;
                ArrayList<ClusterPoint> clusterPoints = readClassificationResultXLSPath(classificationXLSNamePath, inputPaths);

                clusterPoints = calculateClusterDimensionList(clusterPoints);
                ArrayList<RunPoint> points = readAllPoints(clusterPoints);
                Double DBI = calculateDBI(clusterPoints);//评价DBI指标
                mean_DBI += DBI;
                Double CH = calculateCH(clusterPoints);//评价CH指标
                mean_CH += CH;
                Double DI = calculateDI(clusterPoints);//评价指标DI
                mean_DI += DI;
                Double Silhouette = calculateSilhouette(clusterPoints);//评价Silhouette指标
                mean_Silhouette += Silhouette;
                Double Silhouette_SC = calculateSilhouette_SC(clusterPoints);//评价Silhouette SC指标
                mean_Silhouette_SC += Silhouette_SC;
                String XLSName = classificationXLSNamePath.split("\\\\")[classificationXLSNamePath.split("\\\\").length - 1];
                //System.out.println(XLSName+"\tDBI\t"+DBI+"\tCH\t"+CH+"\tDI\t"+DI+"\tSilhouette\t"+Silhouette+"\tSilhouette_SC\t"+Silhouette_SC+"\t");
                System.out.println(XLSName+"\tDBI\t"+DBI+"\tCH\t"+CH+"\tSilhouette\t"+Silhouette+"\tSilhouette_SC\t"+Silhouette_SC+"\t");
//                for (RunPoint point : points) {
//                    System.out.println(point.getSystemName()+"\t"+point.getS_i()+"\t"+point.getA_i()+"\t"+point.getB_i());
//                }
            }
            int expNum = endEXP - startEXP + 1 ;
            mean_DBI = mean_DBI/expNum;
            mean_CH = mean_CH/expNum;
            mean_DI = mean_DI/expNum;
            mean_Silhouette = mean_Silhouette/expNum;
            mean_Silhouette_SC = mean_Silhouette_SC/expNum;
            System.out.println("expNum_mean"+NUM+"\tmean_DBI\t"+mean_DBI+"\tmean_CH\t"+mean_CH+"\tmean_Silhouette\t"+mean_Silhouette+"\tmean_Silhouette_SC\t"+mean_Silhouette_SC+"\t");
        }
    }

    private static Double calculateSilhouette_SC(ArrayList<ClusterPoint> clusterPoints) {
        ArrayList<Double> silhouetteClusterList = new ArrayList<>();
        for (ClusterPoint clusterPoint : clusterPoints) {
            double clusterSilhouette = 0.0;
            for (RunPoint runPoint : clusterPoint.getClusterList()) {
                clusterSilhouette += runPoint.getS_i();
            }
            clusterSilhouette = clusterSilhouette / clusterPoint.getClusterList().size();
            //System.out.println(clusterPoint.getClusterID()+"\t"+clusterSilhouette);
            silhouetteClusterList.add(clusterSilhouette);
        }
        Double Silhouette_SC = Collections.max(silhouetteClusterList);
        return Silhouette_SC;
    }

    /**
     * read all points
     * @param clusterPoints  //points path
     * @return //points
     */
    private static ArrayList<RunPoint> readAllPoints( ArrayList<ClusterPoint> clusterPoints) {
        ArrayList<RunPoint> points = new ArrayList<>();
        for (ClusterPoint clusterPoint : clusterPoints) {
            points.addAll(clusterPoint.getClusterList());
        }
        return points;
    }




    private static Double calculateSilhouette(ArrayList<ClusterPoint> clusterPoints) {
        double mean_s_i = 0.0;
        int count =0;
        for (ClusterPoint clusterPoint : clusterPoints) {
            //System.out.println("当前类号："+clusterPoint.getClusterID());
            for (RunPoint point_i : clusterPoint.getClusterList()) {
                //System.out.println("计算点"+point_i.getSystemName());
                count++;
                if (clusterPoint.getClusterList().size()==1){
                    point_i.setS_i(0.0);
                    continue;
                }
                //Calculate the a(i) value for each point i
                double a_i = 0.0;
                //System.out.println("当前point名："+point_i.getSystemName());
                for (RunPoint point_j : clusterPoint.getClusterList()) {

                    if (!point_j.equals(point_i)){
                        //System.out.print(point_j.getSystemName()+"\t");
                        double iToj = StandardDistance.getRunARunBDistance(point_i, point_j);
                        a_i += iToj;
                    }
                }
                //System.out.println();
                a_i = a_i / (clusterPoint.getClusterList().size()-1);
                point_i.setA_i(a_i);
                //Calculate the b(i) value for each point i
                double b_i = Double.MAX_VALUE;
                for (int i = 0; i < clusterPoints.size(); i++) {
                    if (clusterPoints.get(i).getClusterID()!=clusterPoint.getClusterID()){
                        //System.out.println("其余类号：      "+clusterPoints.get(i).getClusterID());
                        double one_b_i = 0.0;
                        for (RunPoint otherClusterPoint_j : clusterPoints.get(i).getClusterList()) {
                            double other_iToj = StandardDistance.getRunARunBDistance(point_i, otherClusterPoint_j);
                            one_b_i += other_iToj;
                        }
                        one_b_i = one_b_i / clusterPoints.get(i).getClusterList().size();
                        if (one_b_i<=b_i){
                            //System.out.println("最小的b_i值所在的类号："+clusterPoints.get(i).getClusterID());
                            b_i=one_b_i;
                        }
                    }
                }
                point_i.setB_i(b_i);
                //Calculate the s(i) value for each point i
                double s_i = (b_i-a_i)/(Math.max(a_i, b_i));
                //System.out.println(point_i.getSystemName()+"\t"+a_i+"\t"+b_i+"\t"+s_i+"\t"+(b_i-a_i)+"\t"+(Math.max(a_i, b_i)));
                mean_s_i += s_i;
                point_i.setS_i(s_i);
            }
        }

        mean_s_i = mean_s_i /count;
        //System.out.println(count);
        return mean_s_i;
    }

    /**
     * DI = min[d(i,j)]/max[d'(k)];1<=i<j<=n,1<=k<=n But this function implements the reciprocal, since the denominator max[d'(k)] may be zero
     * d(i,j) represents the distance between clusters i and j, and d '(k) measures the intra-cluster distance of cluster k.
     * @param clusterPoints
     * @return
     */
    private static Double calculateDI(ArrayList<ClusterPoint> clusterPoints) {
        //get all d(i,j)  The inter-cluster distance d(i,j) between two clusters may be any number of distance measures, such as the distance between the centroids of the clusters.
        //get min_d(i,j)
        double min_centroids_AToB = Double.MAX_VALUE;
        for (ClusterPoint clusterPointA : clusterPoints) {
            for (ClusterPoint clusterPointB : clusterPoints) {
                if (clusterPointA.equals(clusterPointB)){
                    continue;
                }
                double centroids_AToB = StandardDistance.getRunARunBDistance(clusterPointA,clusterPointB);
                if (centroids_AToB<=min_centroids_AToB){
                    min_centroids_AToB = centroids_AToB;
                }
            }
        }
        //get all d'(k) Similarly, the intra-cluster distance d '(k) may be measured in a variety of ways, such as the maximal distance between any pair of elements in cluster k.
        //get max_d'(k)
        double max_PointAtoPointB = Double.MIN_VALUE;
        for (ClusterPoint clusterPoint : clusterPoints) {
            for (RunPoint runPointA : clusterPoint.getClusterList()) {
                for (RunPoint runPointB : clusterPoint.getClusterList()) {
                    if (runPointA.equals(runPointB)){
                        continue;//ignore a == b
                    }
                    double pointAtoPointB = StandardDistance.getRunARunBDistance(runPointA,runPointB);

                    if (pointAtoPointB>=max_PointAtoPointB){
                        max_PointAtoPointB=pointAtoPointB;
                    }
                }
            }
        }
        return min_centroids_AToB/max_PointAtoPointB;
    }

    private static ArrayList<ClusterPoint> calculateClusterDimensionList(ArrayList<ClusterPoint> clusterPoints) {
        for (ClusterPoint clusterPoint : clusterPoints) {

            for (RunPoint runPoint : clusterPoint.getClusterList()) {
                //System.out.println(runPoint.getSystemName());
                for (Map.Entry<HashMapDocKey, HashMapDocValue> runPointEntry : runPoint.getDimensionList().entrySet()) {
                    HashMapDocKey pointKey = runPointEntry.getKey();
                    HashMapDocValue pointValue = runPointEntry.getValue();
                    HashMapDocValue clusterValue = clusterPoint.getDimensionList().get(pointKey);
                    if (clusterValue != null){
                        clusterValue.setScore(clusterValue.getScore()+pointValue.getScore());
                    }else {
                        Integer topic = pointKey.getTopic();
                        String docID = pointKey.getDocID();
                        Integer rank = pointValue.getRank();
                        Double score = pointValue.getScore();
                        String systemName = pointValue.getSystemName();

                        clusterPoint.getDimensionList().put(new HashMapDocKey(topic,docID),new HashMapDocValue(rank,score,systemName));
                    }

                }
            }

            for (Map.Entry<HashMapDocKey, HashMapDocValue> clusterEntry : clusterPoint.getDimensionList().entrySet()) {
                clusterEntry.getValue().setScore(clusterEntry.getValue().getScore()/clusterPoint.getClusterList().size());
            }
        }
        return clusterPoints;
    }

    private static ArrayList<ClusterPoint>  readClassificationResultXLSPath(String classificationResultXLSPath,String inputPaths) throws IOException {
        ArrayList<ClusterPoint> clusterPoints = new ArrayList<>();
        FileInputStream fis = new FileInputStream(classificationResultXLSPath);
        HSSFWorkbook hw = new HSSFWorkbook(fis);
        Sheet sheet = hw.getSheetAt(0);
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            ClusterPoint clusterPoint = new ClusterPoint(i);
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                String runName = row.getCell(j).getStringCellValue().split(",")[row.getCell(j).getStringCellValue().split(",").length-1];
                RunPoint runPoint = getRunPoint(runName, inputPaths);
                clusterPoint.getClusterList().add(runPoint);
            }
            clusterPoints.add(clusterPoint);
        }
        hw.close();
        fis.close();
        return clusterPoints;
    }

    private static RunPoint getRunPoint(String runName, String inputPaths) throws IOException {
        RunPoint runPoint = new RunPoint(runName);
        String runPointPath = inputPaths +"\\"+runName;
        BufferedReader br = new BufferedReader(new FileReader(runPointPath));
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            Integer topic = Integer.parseInt(str[0]);
            String docID = str[2];
            Double score = Double.parseDouble(str[4]);
            HashMapDocKey key = new HashMapDocKey(topic,docID);
            HashMapDocValue value = new HashMapDocValue();
            value.setScore(score);
            runPoint.getDimensionList().put(key,value);
        }
        br.close();
        return runPoint;
    }

    private static Double calculateCH(ArrayList<ClusterPoint> clusterPoints) {
        Double CH = 0.0;
        //CH=(BGSS/WGSS)*(点总个数-聚类数)/(聚类数-1),WGSS=1/2[(n1-1)diss1+(n2-1)diss2+...+(nk-1)dissk],BGSS=1/2[(K-1)diss+(N-k)Ak],https://blog.csdn.net/qqwowo99/article/details/107799714
        Double AllPointNum = 0.0;//点个数
        for (ClusterPoint clusterPoint : clusterPoints) {
            AllPointNum += clusterPoint.getClusterList().size();
        }
        if (AllPointNum == clusterPoints.size() || clusterPoints.size() == 1){

        }else {
            Double WGSS = calculateWGSS(clusterPoints);
            Double BGSS = calculateBGSS(clusterPoints);
            CH = (BGSS/WGSS)*((AllPointNum - clusterPoints.size())/(clusterPoints.size() -1));
        }
        return CH;
    }

    private static Double calculateBGSS(ArrayList<ClusterPoint> clusterPoints) {
        Double BGSS = 0.0;
        Double meanALLDistance = 0.0;
        Double AllPointNum = 0.0;
        for (ClusterPoint clusterPoint : clusterPoints) {
            AllPointNum += clusterPoint.getClusterList().size();
        }
        for (ClusterPoint clusterPoint : clusterPoints) {
            for (RunPoint runPointA : clusterPoint.getClusterList()) {
                for (RunPoint runPointB : clusterPoint.getClusterList()) {
                    if (!runPointA.getSystemName().equals(runPointB.getSystemName())){
                        meanALLDistance += StandardDistance.getRunARunBDistance(runPointA,runPointB);
                    }
                }
            }
        }
        meanALLDistance = meanALLDistance/2;
        meanALLDistance = meanALLDistance/getMeanCount((int) Math.round(AllPointNum));
        Double Ak = 0.0;

        if (clusterPoints.size() != AllPointNum){
            Double tempSUM = 0.0;
            for (ClusterPoint clusterPoint : clusterPoints) {
                Double meanDistance = 0.0;
                if (clusterPoint.getClusterList().size()!=1){
                    for (RunPoint runPointA : clusterPoint.getClusterList()) {
                        for (RunPoint runPointB : clusterPoint.getClusterList()) {
                            if (!runPointA.getSystemName().equals(runPointB.getSystemName())){
                                meanDistance += StandardDistance.getRunARunBDistance(runPointA,runPointB);
                            }
                        }
                    }
                }
                meanDistance = meanDistance/2;
                meanDistance = meanDistance/getMeanCount(clusterPoint.getClusterList().size());
                tempSUM += (clusterPoint.getClusterList().size()-1)*(meanALLDistance-meanDistance);
            }

            Ak = tempSUM/(AllPointNum-clusterPoints.size());
        }
        BGSS = (clusterPoints.size()-1)*meanALLDistance + (AllPointNum - clusterPoints.size()*Ak);
        BGSS = BGSS/2;
        return BGSS;
    }

    private static Double calculateWGSS(ArrayList<ClusterPoint> clusterPoints) {
        Double WGSS = 0.0;
        for (ClusterPoint clusterPoint : clusterPoints) {
            Double meanDistance = 0.0;
            if (clusterPoint.getClusterList().size()!=1){
                for (RunPoint runPointA : clusterPoint.getClusterList()) {
                    for (RunPoint runPointB : clusterPoint.getClusterList()) {
                        if (!runPointA.getSystemName().equals(runPointB.getSystemName())){
                            meanDistance += StandardDistance.getRunARunBDistance(runPointA,runPointB);
                        }
                    }
                }
            }
            meanDistance = meanDistance/2;
            meanDistance = meanDistance/getMeanCount(clusterPoint.getClusterList().size());
            WGSS += (clusterPoint.getClusterList().size()-1)*meanDistance;
        }
        WGSS = WGSS/2;
        return WGSS;

    }

    private static int getMeanCount(int count) {
        int MeanCount = 1;
        if (count != 1){
            for (int i = 1; i <count; i++) {
                MeanCount += i;
            }
            MeanCount--;
        }
        return MeanCount;
    }

    private static Double calculateDBI(ArrayList<ClusterPoint> clusterPoints) {
        Double DBI = 0.0;
        //计算每个簇中心点到簇内各个点的平均距离
        calculateMeanDistanceForC_P(clusterPoints);
        for (ClusterPoint clusterPointA : clusterPoints) {
            //计算当前簇和其他簇的所有R_AB值，R_AB=(ClusterPointA.meanDistanceForC_P+ClusterPointB.meanDistanceForC_P)/(Distance(ClusterPointA,ClusterPointB))
            ArrayList<Double> R_AB = new ArrayList<>();
            for (ClusterPoint clusterPointB : clusterPoints) {
                if (clusterPointA.getClusterID()!=clusterPointB.getClusterID()){
                    R_AB.add((clusterPointA.getMeanDistanceForC_P()+clusterPointB.getMeanDistanceForC_P())/StandardDistance.getRunARunBDistance(clusterPointA,clusterPointB));
                }
            }
            //选择R_AB中最大的
            Double D_A = Collections.max(R_AB);
            //System.out.println(D_A);
            DBI += D_A;
        }
        DBI = DBI/clusterPoints.size();
        return DBI;
    }

    private static void calculateMeanDistanceForC_P(ArrayList<ClusterPoint> clusterPoints) {
        for (ClusterPoint clusterPoint : clusterPoints) {
            Double meanDistanceForC_P = 0.0;
            for (RunPoint runPoint : clusterPoint.getClusterList()) {
                meanDistanceForC_P += StandardDistance.getRunARunBDistance(clusterPoint,runPoint);
            }
            meanDistanceForC_P = meanDistanceForC_P/clusterPoint.getClusterList().size();
            clusterPoint.setMeanDistanceForC_P(meanDistanceForC_P);
        }
    }


}
