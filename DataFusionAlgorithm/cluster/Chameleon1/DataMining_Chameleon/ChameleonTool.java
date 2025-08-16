package com.algorithm.cluster.Chameleon1.DataMining_Chameleon;

import com.algorithm.cluster.CalculateDistanceMatrix;
import com.algorithm.cluster.Chameleon2.Chameleon;
import com.algorithm.cluster.hierarchical.agglomerative1.ClusterPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class ChameleonTool {

    // 测试数据点文件地址
    private String filePath;
    // 第一阶段的k近邻的k大小
    private double k;
    // 簇度量函数阈值
    private double minMetric;
    // 总的坐标点的个数
    //private int pointNum=86;//2018A
    //public static  int   Num=86;//2018A
    private int pointNum= 37;//RUNS个数
    public static  int   Num= 37;//RUNS个数
    public static double[][] DistanceMatrix= new double[Num][Num];
    // 总的连接矩阵的情况,括号表示的是坐标点的id号
    public static int[][] edges;
    // 点与点之间的边的权重
    public static double[][] weights;
    // 原始坐标点数据
    private ArrayList<Point> totalPoints;
    // 第一阶段产生的所有的连通子图作为最初始的聚类
    private ArrayList<Cluster> initClusters;
    // 结果簇结合
    private ArrayList<Cluster> resultClusters;

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }

    public static int getNum() {
        return Num;
    }

    public static void setNum(int num) {
        Num = num;
    }

    public static double[][] getDistanceMatrix() {
        return DistanceMatrix;
    }

    public static void setDistanceMatrix(double[][] distanceMatrix) {
        DistanceMatrix = distanceMatrix;
    }

    public ChameleonTool(String filePath, double k, double minMetric) {
        this.filePath = filePath;
        this.k = k;
        this.minMetric = minMetric;

        readDataFile();
    }

    /**
     * 从文件中读取数据
     */
    private void readDataFile() {

        ArrayList<String[]> dataArray = new ArrayList<String[]>();
        Point p;
        totalPoints = new ArrayList<>();
        for (int i=0;i<Num;i++) {
            p = new Point();
            p.id=i;
            totalPoints.add(p);
        }
        pointNum = totalPoints.size();
    }

    /**
     * 递归的合并小聚簇
     * @throws Exception
     * @throws IOException
     */
    private void combineSubClusters() throws IOException, Exception {
        Cluster cluster = null;

        resultClusters = new ArrayList<>();

        // 当最后的聚簇只剩下一个的时候，则退出循环
        while (initClusters.size() >= 1) {
            cluster = initClusters.get(0);
            combineAndRemove(cluster, initClusters);
        }
    }
    /**
     * 递归的合并聚簇和移除聚簇
     *
     * @param clusterList
     * @throws Exception
     * @throws IOException
     */
    private ArrayList<Cluster> combineAndRemove(Cluster cluster,ArrayList<Cluster> clusterList) throws IOException, Exception {
        ArrayList<Cluster> remainClusters;
        double metric = 0;
        double maxMetric = -Integer.MAX_VALUE;
        Cluster cluster1 = null;
        Cluster cluster2 = null;

        for (Cluster c2 : clusterList) {
            if (cluster.id == c2.id) {
                continue;
            }

            metric = calMetricfunction(cluster, c2, 1.0);

            if (metric > maxMetric) {
                maxMetric = metric;
                cluster1 = cluster;
                cluster2 = c2;
            }
        }

        // 如果度量函数值超过阈值，则进行合并,继续搜寻可以合并的簇
        if (maxMetric > minMetric) {
            clusterList.remove(cluster2);
            // 将边进行连接
            connectClusterToCluster(cluster1, cluster2);
            // 将簇1和簇2合并
            cluster1.points.addAll(cluster2.points);
            remainClusters = combineAndRemove(cluster1, clusterList);
        } else {
            clusterList.remove(cluster);
            remainClusters = clusterList;
            resultClusters.add(cluster);
        }

        return remainClusters;
    }

    /**
     * 将2个簇进行边的连接
     *
     * @param c1
     *            聚簇1
     * @param c2
     *            聚簇2
     * @throws Exception
     * @throws IOException
     */
    private void connectClusterToCluster(Cluster c1, Cluster c2) throws IOException, Exception {
        ArrayList<int[]> connectedEdges;

        connectedEdges = c1.calNearestEdge(c2, 2);

        for (int[] array : connectedEdges) {
            edges[array[0]][array[1]] = 1;
            edges[array[1]][array[0]] = 1;
        }
    }

    /**
     * 算法第一阶段形成局部的连通图
     * @throws Exception
     * @throws IOException
     */
    private void connectedGraph() throws IOException, Exception {
        double distance = 0;
        Point p1;
        Point p2;

        // 初始化权重矩阵和连接矩阵
        weights = new double[pointNum][pointNum];
        edges = new int[pointNum][pointNum];


        double[][] dis=new double[pointNum][pointNum];
        dis = readDisMatrix(filePath);
        //dis=readDisMatrix();

        for (int i = 0; i < pointNum; i++) {
            for (int j = 0; j < pointNum; j++) {
                p1 = totalPoints.get(i);
                p2 = totalPoints.get(j);

                //distance = p1.ouDistance(p2);//可以根据制作的距离矩阵表查询
                distance = dis[i][j];//可以根据制作的距离矩阵表查询
                if (distance == 0) {
                    // 如果点为自身的话，则权重设置为0
                    weights[i][j] = 0;
                } else {
                    // 边的权重采用的值为距离的倒数,距离越近，权重越大
                    weights[i][j] = 1.0 / distance;
                }
            }
        }

        //for (int i = 0; i < pointNum; i++) {
        //	for (int j = 0; j < pointNum; j++) {
        // 边的权重采用的值为距离的倒数,距离越近，权重越大
        //		System.out.print(weights[i][j]+"\t");
        //	}
        //	System.out.println();
        //	}

        double[] tempWeight;
        int[] ids;
        int id1 = 0;
        int id2 = 0;
        // 对每个id坐标点，取其权重前k个最大的点进行相连
        for (int i = 0; i < pointNum; i++) {
            tempWeight = weights[i];
            // 进行排序
            ids = sortWeightArray(tempWeight);

            // 取出前k个权重最大的边进行连接
            for (int j = 0; j < ids.length; j++) {
                if (j < k) {//K=1,意味着每组只取前两个点进行赋予边权重，为1
                    id1 = i;
                    id2 = ids[j];

                    edges[id1][id2] = 1;
                    edges[id2][id1] = 1;
                }
            }
        }
        System.out.println("bbbbbbbbbbbbbbbb");
        for (int i = 0; i < pointNum; i++) {
            for (int j = 0; j < pointNum; j++) {
                // 边的权重采用的值为距离的倒数,距离越近，权重越大
                System.out.print(edges[i][j]+"\t");
            }
            System.out.println();
        }
        System.out.println("cccccccccccccccccccc");


    }

    public double[][] readDisMatrix() throws Exception, IOException{
        double[][] distance=new double[Num][Num];
        //String adr="D:\\ZZ_PMSA2018A\\Distance matrix.txt";
        //String adr= "E:\\ZZ_PMSA2014_24G\\Distance matrix.txt";
        //String adr="D:\\TREC数据集文件\\zhangzhen\\ZZ_PMSA2018A\\Distance matrix.txt";
        String adr= "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\DistanceMatrix.txt";

        BufferedReader br = new BufferedReader(new FileReader(new File(adr)));
        String line=null, tempcol[];
        int rowc=0;
        while((line = br.readLine())!=null){
            tempcol= line.split("\t");
            for(int colc=0,len=tempcol.length;colc<len;colc++){
                distance[rowc][colc] = Double.parseDouble(tempcol[colc]);
            }
            rowc++;
        }
        br.close();
        for(int i=0;i<Num;i++){
            for(int j=0;j<Num;j++){
                System.out.print(distance[i][j]+"\t");
            }
            System.out.println();
        }
        return distance;
    }
    public static double[][] readDisMatrix(String runsPath) throws IOException {
        Num = new File(runsPath).listFiles().length;

        double[][] distance = getDistanceMatrix();
        return distance;
    }

    /**
     * 权重的冒泡算法排序
     *
     * @param array
     *            待排序数组
     */
    private int[] sortWeightArray(double[] array) {
        double[] copyArray = array.clone();
        int[] ids = null;
        int k = 0;
        double maxWeight = -1;

        ids = new int[pointNum];
        for (int i = 0; i < pointNum; i++) {
            maxWeight = -1;

            for (int j = 0; j < copyArray.length; j++) {
                if (copyArray[j] > maxWeight) {
                    maxWeight = copyArray[j];
                    k = j;
                }
            }

            ids[i] = k;
            // 将当前找到的最大的值重置为-1代表已经找到过了
            copyArray[k] = -1;
        }

        return ids;
    }

    /**
     * 根据边的连通性去深度优先搜索所有的小聚簇
     */
    private void searchSmallCluster() {
        int currentId = 0;
        Point p;
        Cluster cluster;
        initClusters = new ArrayList<>();
        ArrayList<Point> pointList = null;

        // 以id的方式逐个去dfs搜索
        for (int i = 0; i < pointNum; i++) {
            p = totalPoints.get(i);

            if (p.isVisited) {
                //System.out.println("p0");
                continue;
            }

            pointList = new ArrayList<>();
            pointList.add(p);
            recusiveDfsSearch(p, -1, pointList);

            cluster = new Cluster(currentId, pointList);
            initClusters.add(cluster);

            currentId++;
        }
        System.out.println("searchSmallCluster");
        System.out.println(currentId);
    }

    /**
     * 深度优先的方式找到边所连接着的所有坐标点
     *
     * @param p
     *            当前搜索的起点
     * @param parentId
     *            此点的父坐标点
     * @param pList
     *            坐标点列表
     */
    private void recusiveDfsSearch(Point p, int parentId, ArrayList<Point> pList) {
        int id1 = 0;
        int id2 = 0;
        Point newPoint;

        if (p.isVisited) {
            return;
        }

        p.isVisited = true;
        for (int j = 0; j < pointNum; j++) {
            id1 = p.id;
            id2 = j;

            if (edges[id1][id2] == 1 && id2 != parentId) {
                newPoint = totalPoints.get(j);
                pList.add(newPoint);
                // 以此点为起点，继续递归搜索
                recusiveDfsSearch(newPoint, id1, pList);
            }
        }
    }

    /**
     * 计算连接2个簇的边的权重
     *
     * @param c1
     *            聚簇1
     * @param c2
     *            聚簇2
     * @return
     * @throws Exception
     * @throws IOException
     */
    private double calEC(Cluster c1, Cluster c2) throws IOException, Exception {
        double resultEC = 0;
        ArrayList<int[]> connectedEdges = null;

        connectedEdges = c1.calNearestEdge(c2, 2);

        // 计算连接2部分的边的权重和
        for (int[] array : connectedEdges) {
            resultEC += weights[array[0]][array[1]];
        }

        return resultEC;
    }

    /**
     * 计算2个簇的相对互连性
     *
     * @param c1
     * @param c2
     * @return
     * @throws Exception
     * @throws IOException
     */
    private double calRI(Cluster c1, Cluster c2) throws IOException, Exception {
        double RI = 0;
        double EC1 = 0;
        double EC2 = 0;
        double EC1To2 = 0;

        EC1 = c1.calEC();
        EC2 = c2.calEC();
        EC1To2 = calEC(c1, c2);

        RI = 2 * EC1To2 / (EC1 + EC2);

        return RI;
    }

    /**
     * 计算簇的相对近似度
     *
     * @param c1
     *            簇1
     * @param c2
     *            簇2
     * @return
     * @throws Exception
     * @throws IOException
     */
    private double calRC(Cluster c1, Cluster c2) throws IOException, Exception {
        double RC = 0;
        double EC1 = 0;
        double EC2 = 0;
        double EC1To2 = 0;
        int pNum1 = c1.points.size();
        int pNum2 = c2.points.size();

        EC1 = c1.calEC();
        EC2 = c2.calEC();
        EC1To2 = calEC(c1, c2);

        RC = EC1To2 * (pNum1 + pNum2) / (pNum2 * EC1 + pNum1 * EC2);

        return RC;
    }

    /**
     * 计算度量函数的值
     *
     * @param c1
     *            簇1
     * @param c2
     *            簇2
     * @param alpha
     *            幂的参数值
     * @return
     * @throws Exception
     * @throws IOException
     */
    private double calMetricfunction(Cluster c1, Cluster c2, double alpha) throws IOException, Exception {
        // 度量函数值
        double metricValue = 0;
        double RI = 0;
        double RC = 0;

        RI = calRI(c1, c2);
        RC = calRC(c1, c2);
        // 如果alpha大于1，则更重视相对近似性，如果alpha逍遥于1，注重相对互连性
        metricValue = RI * Math.pow(RC, alpha);

        return metricValue;
    }

    /**
     * 输出聚簇列
     *
     * @param clusterList
     *            输出聚簇列
     */
    private void printClusters(ArrayList<Cluster> clusterList) {
        int i = 1;

        for (Cluster cluster : clusterList) {
            System.out.print("聚簇" + i + ":");
            for (Point p : cluster.points) {
                //System.out.print(MessageFormat.format("({0}, {1}) ", p.x, p.y));
                System.out.print("\t"+p.id+"\t");
            }
            System.out.println();
            i++;
        }

    }

    /**
     * 创建聚簇
     * @throws Exception
     * @throws IOException
     */
    public void buildCluster() throws IOException, Exception {
        System.out.println("aaaaaaaaaaaaaaaaa");
        // 第一阶段形成小聚簇
        try {
            connectedGraph();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        searchSmallCluster();
        String pointPath ="D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60";
        int KNN_K = 2;
        Chameleon chameleon = new Chameleon();
        System.out.println("ccccccccccccccccccccc");

        double[][] adjacentMatrix = chameleon.changePowerToAdjacent(chameleon.changeDistanceToPower(getDistanceMatrix()),KNN_K);
        System.out.println("ddddddddddddddddddddddd");
        ArrayList<com.algorithm.cluster.Chameleon2.Point> KNNPoints = chameleon.getOneStep(pointPath, adjacentMatrix);
        System.out.println("eeeeeeeeeeeeeeeeeee");
        ArrayList<com.algorithm.cluster.Chameleon2.Cluster> clusters = new ArrayList<>();
        System.out.println("ffffffffffffffffff");
        clusters = chameleon.DFSPoints(KNNPoints,clusters);
        System.out.println("ggggggggggggggggggggggggg");
        for (int i = 0; i < clusters.size(); i++) {
            System.out.print("cluster id : "+clusters.get(i).getId()+"\t###\t");
            for (int j = 0; j < clusters.get(i).getPoints().size(); j++) {
                System.out.print("\t"+clusters.get(i).getPoints().get(j).getId());
            }
            System.out.println();
        }
        initClusters.clear();
        //替换需要的列表

        for (int i = 0; i < clusters.size(); i++) {
            ArrayList<Point> pointList = new ArrayList<>();

            for (int j = 0; j < clusters.get(i).getPoints().size(); j++) {
                for (int l = 0; l < totalPoints.size(); l++) {
                    if (totalPoints.get(l).id == clusters.get(i).getPoints().get(j).getId()){
                        pointList.add(totalPoints.get(l));
                    }
                }
            }
            Cluster cluster = new Cluster(i,pointList);
            initClusters.add(cluster);
        }
        System.out.println("第一阶段形成的小簇集合：");
        printClusters(initClusters);

        // 第二阶段根据RI和RC的值合并小聚簇形成最终结果聚簇
        combineSubClusters();
        System.out.println("最终的聚簇集合：");
        printClusters(resultClusters);
        for (int i = 0; i < resultClusters.size(); i++) {
            Cluster cluster = resultClusters.get(i);

            System.out.print("聚簇:"+i+"\t");
            for (int j = 0; j < cluster.points.size(); j++) {
                for (int z = 0; z < KNNPoints.size(); z++) {
                    if (cluster.points.get(j).id==KNNPoints.get(z).getId()){
                        System.out.print(KNNPoints.get(z).getPointName()+"\t");
                    }
                }
            }
            System.out.println();
        }
        /**
         * 进行聚簇结果的性能评价
         */
    }



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
