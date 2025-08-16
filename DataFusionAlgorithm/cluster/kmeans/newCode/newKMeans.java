package com.algorithm.cluster.kmeans.newCode;

import com.algorithm.DataStruct.Result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


public class newKMeans {
    /*
    1.读取所有的点坐标
    2.将点坐标分为k份
    3.计算每个簇的中心点
    4.计算每个点坐标和各个中心点的距离
    5.将每个点分配到距离它最近的中心点
    6.重新计算每个簇的中心点
    7.再次计算每个点到每个中心点的距离
    8.判断是否有点修改了簇，若有则重复5-7、若没有则输出聚类结果
    * */
    /*
    * 读取所有点
    * */
    public static void readAllPoint(String inputpath) throws IOException {
        File input = new File(inputpath);

        File[] runs = input.listFiles();
        for(File run:runs) {
            Point point = new Point();
            System.out.println(run.getName());
            BufferedReader readerPoint = new BufferedReader(new FileReader(run));
            String runPointname = run.toString().split("\\\\")[run.toString().split("\\\\").length-1];//获得到点名
            point.setPointname(runPointname);
            String bufferline = new String(readerPoint.readLine());
            StringTokenizer tokenizer=new StringTokenizer(bufferline);
            //读取topic
            int topic=Integer.parseInt(tokenizer.nextToken());
            //读取docID
            String docid=tokenizer.nextToken();

            //读取分数
            double score=Double.parseDouble(tokenizer.nextToken());
            point.coordinatelist.add(new Result(topic, docid, score));
            while((bufferline=readerPoint.readLine())!=null) {
                tokenizer=new StringTokenizer(bufferline);
                //读取topic
                topic=Integer.parseInt(tokenizer.nextToken());
                //读取docID
                docid=tokenizer.nextToken();
                //读取分数
                score=Double.parseDouble(tokenizer.nextToken());
                point.coordinatelist.add(new Result(topic, docid, score));
            }
            Points.add(point);
            readerPoint.close();
        }
        read_MAP();
        System.err.println("-------------读取所有点   end-------------");
    }
    /*
     * 对每个point点赋MAP值
     */
    public static void read_MAP() throws IOException {
        FileInputStream fip = new FileInputStream(evalSort_XML_Path);
        HSSFWorkbook wb = new HSSFWorkbook(fip);
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(0);
        String runName;
        double MAP;
        //i为run的个数
        for(int i= 1;i<sheet.getPhysicalNumberOfRows();i++) {
            row = sheet.getRow(i);
            runName = row.getCell(0).getStringCellValue();
            System.out.println("read_MAP     "+runName);
            MAP = row.getCell(1).getNumericCellValue();
            Point point = findPoint(runName);
            point.setMAP(MAP);
        }
        wb.close();
        fip.close();
    }
    /*
     *  创建本次聚类需要的Clusters C1
     * */
    private static void C1_initClusters() throws IOException {

        Collections.shuffle(Points);//Points列表打乱,取前k个点作为中心点

        //创建需要的簇的中心点文件
        for (int i= 0 ; i< NUM_CLUSTERS;i++){
            Path random_point = Paths.get(inputfilepath+"\\"+Points.get(i).getPointname());
            String Cluster_file_path = Clusters_PATH+"\\"+NUM_CLUSTERS+"\\";
            createDirectory(Cluster_file_path);
            Path Cluster_file = Paths.get(Cluster_file_path+"cluster_"+(i+1));
            Files.copy(random_point,Cluster_file, StandardCopyOption.REPLACE_EXISTING);
        }

        for(int j=1;j<=NUM_CLUSTERS;j++) {//创建中心点列表
            Cluster cluster = new Cluster(j);
            cluster.Pointlist.add(Points.get(j-1));
            Clusters.add(cluster);
        }

    }
    /*
    *  创建本次聚类需要的Clusters C2
    * */
    private static void C2_initClusters() throws IOException {
        Path nullpoint = Paths.get(nullPointPath);
        //创建需要的簇的中心点文件
        for (int i = 1;i <= NUM_CLUSTERS;i++){
            String Cluster_file_path = Clusters_PATH+"\\"+NUM_CLUSTERS+"\\";
            createDirectory(Cluster_file_path);
            Path Cluster_file = Paths.get(Cluster_file_path+"cluster_"+i);
            Files.copy(nullpoint,Cluster_file, StandardCopyOption.REPLACE_EXISTING);
        }

    }
    /*
    * 计算每个中心点的坐标并覆盖保存到指定位置
    * */
    private static void calculate_Centers() throws IOException {
        for (int i = 0 ; i < NUM_CLUSTERS ; i++){
            Cluster cluster = Clusters.get(i);
            cluster.read_old_CoordinateList(Clusters_PATH+NUM_CLUSTERS+"\\"+"cluster_"+cluster.clusterID);//读取每个cluster的上一次的坐标
            int cluster_size = cluster.Pointlist.size();//获取每个cluster有几个点
            for(int coordinate=0;coordinate<coordinate_size;coordinate++) {//维度坐标计算
                //System.out.println("计算第: "+(coordinate+1)+" 中心点维度坐标值");
                double cluster_coordinaate_score = 0.0;
                for(int clustercount=0;clustercount<cluster_size;clustercount++) {//计算中心点每个维度的值
                    cluster_coordinaate_score += Clusters.get(i).Pointlist.get(clustercount).coordinatelist.get(coordinate).getScore();
                }
                cluster.coordinateList.get(coordinate).setScore(cluster_coordinaate_score/cluster_size);
				/*System.out.println("topic\t"+Clusters.get(i).coordinateList.get(coordinate).getTopic()
						+"\tdocid\t"+Clusters.get(i).coordinateList.get(coordinate).getDocID()
						+"\tscore\t"+Clusters.get(i).coordinateList.get(coordinate).getScore());*/
            }
            cluster.write_new_CoordinateList(Clusters_PATH+NUM_CLUSTERS+"\\"+"cluster_"+cluster.clusterID);
        }
    }
    public static double new_distance_P_C(Point point,Cluster cluster) throws IOException {
        Cluster cluster1 = new Cluster(cluster.getClusterID());
        cluster1.read_old_CoordinateList(Clusters_PATH+NUM_CLUSTERS+"\\"+"cluster_"+cluster.clusterID);
        double distance=0.0;
        for(int i = 0;i<coordinate_size;i++) {
            distance += Math.pow(point.coordinatelist.get(i).getScore()-cluster1.coordinateList.get(i).getScore(), 2) ;
        }
        return Math.sqrt(distance);
    }
    public static void update_Cluster() throws IOException {
        /*
         * 清理掉中心点中的point点，为之后替换新的坐准备
         */
        for(int i=0;i<Clusters.size();i++) {
            Clusters.get(i).Pointlist.clear();
        }

        for (int i = 0; i < Points.size(); i++) {
            double[] distance_set = new double[Clusters.size()];
            System.out.println(i);
            for (int j = 0; j < Clusters.size(); j++) {
                distance_set[j] = new_distance_P_C(Points.get(i),Clusters.get(j));
            }
            int position_min = 0;//中心点在distance_set中的位置坐标
            double distance_min = 1000.0;
            for(int j=0;j<Clusters.size();j++) {//找到最小的距离
                if(distance_min>=distance_set[j]) {
                    distance_min=distance_set[j];
                    position_min=j;
                }
            }
            Point point = findPoint(Points.get(i).getPointname());
            Clusters.get(position_min).Pointlist.add(point);
        }
    }


    public static void distribute_Centers() throws IOException {
        //保存旧的中心点的points信息
        save_old_Cluster();
        System.out.println("save_old_Cluster....down");
        //更新points信息
        update_Cluster();
        System.out.println("update_Cluster....down");
    }
    public static int save_old_Cluster() throws IOException {
        for (int i = 0; i < Clusters.size(); i++) {
            old_Clusters.add(new Cluster(i+1));
        }
        for (int i = 0; i < Clusters.size(); i++) {
            Cluster cluster = Clusters.get(i);
            if (cluster.Pointlist.size()==0){
                System.err.println(cluster.getClusterID()+"组内无点   save_old_Cluster");
                return 0;
            }
            for (int j = 0; j < cluster.Pointlist.size(); j++) {
                Point point = cluster.Pointlist.get(j);
                old_Clusters.get(i).Pointlist.add(point);
            }
        }
        return 1;
    }
    /*
     * 找到对应的point
     */
    public static Point findPoint(String name) {
        for(int i=0;i<Points.size();i++) {
            if(name.equals(Points.get(i).getPointname())) {
                return Points.get(i);
            }
        }
        return null;
    }
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    public  static boolean if_consistent(){
        for (int i = 0; i < old_Clusters.size(); i++) {
            if (old_Clusters.get(i).Pointlist.size()==Clusters.get(i).Pointlist.size()){
                for (int j = 0; j < old_Clusters.get(i).Pointlist.size(); j++) {
                    if(!old_Clusters.get(i).Pointlist.get(j).getPointname().equals(Clusters.get(i).Pointlist.get(j).getPointname()) ){
                        return false;
                    }
                }
            }else{
                return false;
            }
        }
        return true;
    }
    public static int showclusters( ArrayList<Cluster> clusters) {
        System.out.println("共有： "+clusters.size()+" 组");
        for(int j=0;j<clusters.size();j++) {
            System.out.print("cluster ID :"+clusters.get(j).getClusterID()+"  cluster size: "+clusters.get(j).Pointlist.size());
            System.out.println();
            if(clusters.get(j).Pointlist.size()==0) {
                System.err.println("组内无点："+clusters.get(j).Pointlist.size());
                return 0;
            }
            for(int i=0;i<clusters.get(j).Pointlist.size();i++) {
                System.out.print(clusters.get(j).Pointlist.get(i).getPointname()+" , MAP: "+clusters.get(j).Pointlist.get(i).getMAP()+" , Distance: "+clusters.get(j).Pointlist.get(i).P_C_distance+" ");
            }
            System.out.println();
        }
        return 1;
    }
    /*
     * 比较器将point按照MAP进行排序
     */
    public static class pointCompareByMAP implements Comparator<Point>{
        @Override
        public int compare(Point o1, Point o2) {
            if(o1.getMAP() > o2.getMAP()) {
                return -1;
            }
            if(o1.getMAP() < o2.getMAP()) {
                return 1;
            }
            return (int) (o2.getMAP()-o1.getMAP());
        }
    }
    /*
     * 排序将pointlist按照MAP进行排序
     */
    public static void sortPoint(ArrayList<Cluster> Clusters) {
        for(int i=0;i<Clusters.size();i++) {
            Collections.sort(Clusters.get(i).Pointlist,new newKMeans.pointCompareByMAP());
        }
    }

    /*
     * 根据名字填获得系统序号
     */
    public static int getID(String PointName) {
        int ID = 0;
        //原始文件路径
        //File file = new File("D:\\eclipse\\eclipse-workspace\\my_k_means2\\health2020\\input_raw");
        File file = new File(Point_NUM);
        File[] files = file.listFiles();
        for(File run:files) {
            //String path_runName = run.toString().split("\\\\")[run.toString().split("\\\\").length-1].replaceAll("input.", "");
            String path_runName = run.toString().split("\\\\")[run.toString().split("\\\\").length-1].replaceAll("input.", "");
            if(PointName.equals(path_runName)||PointName.contains(path_runName)||path_runName.contains(PointName)) {
                return ID;
            }else {
                ID++;
            }
        }
        return ID;
    }
    /*
     * 将结果输入到一个表格中
     */
    public static int writeTo_XLS(String xlspath,ArrayList<Cluster> Clusters) throws IOException {
        int flag = 1;//为1则不许需要重新运行;为0则需要重新运行;
        int count_row = 0;
        FileOutputStream fop = new FileOutputStream(xlspath);
        HSSFWorkbook wb= new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row;

        for(int i=0;i<Clusters.size();i++) {
            row = sheet.createRow(count_row);
            if(Clusters.get(i).Pointlist.size()==0) {
                row.createCell(0).setCellValue("null");
                return 0;
            }else {

                for(int j=0;j<Clusters.get(i).Pointlist.size();j++) {
                    row.createCell(j).setCellValue(getID(Clusters.get(i).Pointlist.get(j).getPointname())+","+Clusters.get(i).Pointlist.get(j).getPointname());
                }

            }
            count_row++;
        }
        wb.write(fop);
        wb.close();
        fop.close();
        return 1;
    }
    private static void randomSort() throws IOException {
        Collections.shuffle(Points);//Points列表打乱
        int start = 0;////points列表开始读取位置
        int Points_size = Points.size();//point总数
        for(int j=1;j<=NUM_CLUSTERS;j++) {//创建中心点列表
            Cluster cluster = new Cluster(j);
            Clusters.add(cluster);
        }
        for(start=1;start<=Points_size;start++) {
            Point point = findPoint(Points.get(start-1).getPointname());
            int j = (int)(start % NUM_CLUSTERS);
            if(j==0) {
                j=NUM_CLUSTERS;
            }
            System.out.print(start+" ");
            System.out.println(j-1);
            Clusters.get(j-1).Pointlist.add(point);
        }
        showclusters(Clusters);//显示每个中心点都有哪些point，判断是否有中心点的大小为0
        System.out.println("-------------所有点分类完毕   end-------------");
    }
    //c1方法是先选k个point作为中心点，然后进行kmeans
    //c2方法是把整个points分为k个簇，然后分别计算每个簇的中心位置
    public static String kmeans_method="C2";
    //2021health 264234
    public static int coordinate_size = 195010;//每个run的维度大小
    public static int NUM_CLUSTERS = 12;//分组数
    public static ArrayList<Point> Points = new ArrayList<>();//点列表
    public static ArrayList<Cluster> Clusters = new ArrayList<>();//组列表
    public static ArrayList<Cluster> old_Clusters = new ArrayList<>();//组列表
    public static int num_experiments_start = 1;//实验次数
    public static int num_experiments_end = 50;//实验次数
    public static String evalSort_XML_Path ="D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\2020Health_evalMAP.xls";
    public static String Clusters_PATH = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\classification\\kmeans_Clusters_C2\\";
    public static String nullPointPath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\nullPoint";//获得nullPoint的路径，
    public static String Point_NUM = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60";//按照文件名排序为每个point编号
    public static String inputfilepath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\input_nor30_60_classification";
    public static int run_program(int num_experiments) throws IOException{
        //导入需要进行分类的点的文件夹

        //创建分好类的文件夹路径
        String output_XLS_path = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\classification\\kmeans_C2\\"+NUM_CLUSTERS+"\\";
        createDirectory(output_XLS_path);//创建分组路径c
        output_XLS_path = output_XLS_path + "\\KMeans_k"+NUM_CLUSTERS+"_"+num_experiments+".xls";//填入分类后xls文件的路径和文件名
        readAllPoint(inputfilepath);//将所有的点加载到内存中
        if (kmeans_method == "C1"){
            C1_initClusters();
            System.err.println("C1");
            showclusters(Clusters);
            System.out.println("判断当前每个中心点有多少point.....");
            //根据目前的中心点重新对points进行分配
            distribute_Centers();//重新依据中心点位置进行分配
            System.out.println("distribute_Centers....down");
            showclusters(Clusters);
            System.out.println("判断当前每个中心点有多少point.....");
        }
        if (kmeans_method == "C2"){
            C2_initClusters();//创建初始的中心点
            System.err.println("C2");
            randomSort();//随机分配point到Clusters中
            System.out.println("randomSort....down");
        }

        calculate_Centers();//计算每个中心点的坐标并覆盖保存到指定位置
        System.out.println("calculate_Centers....down");
        distribute_Centers();//重新依据中心点位置进行分配
        System.out.println("distribute_Centers....down");

        //证明新的cluster的簇内都有point分布就继续进行执行判断
        //判断old_cluster和cluster中的每个cluster的pointlist是否一致，若一致则证明分簇完毕，若没有则继续循环
        while(showclusters(Clusters)==1 && if_consistent()){
            calculate_Centers();//计算每个中心点的坐标并覆盖保存到指定位置
            distribute_Centers();//重新依据中心点位置进行分配
        }
        if (showclusters(Clusters)==0){
            System.err.println("有空点重新运行，返回0");
            return 0;
        }
        sortPoint(Clusters);//使用MAP对其进行排序
        showclusters(Clusters);//显示分好簇的聚类效果
        writeTo_XLS(output_XLS_path, Clusters);
        System.out.println("run_program....down");
        return 1;
    }

    public static void main(String[] args) throws IOException {
        //c1方法是先选k个point作为中心点，然后进行kmeans
        //c2方法是把整个points分为k个簇，然后分别计算每个簇的中心位置
        int num_experiments;//实验次数
        for(num_experiments=num_experiments_start;num_experiments<=num_experiments_end;num_experiments++) {
            System.err.println("第： "+num_experiments+" 次实验开始");
            Points.clear();
            Clusters.clear();
            old_Clusters.clear();
            while(run_program(num_experiments)==0) {
                System.err.println("第： "+num_experiments+" 次实验  再次  开始");
                Points.clear();
                Clusters.clear();
                old_Clusters.clear();
            }
        }
    }
}
