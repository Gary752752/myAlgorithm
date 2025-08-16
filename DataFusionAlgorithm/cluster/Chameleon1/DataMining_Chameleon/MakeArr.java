package com.algorithm.cluster.Chameleon1.DataMining_Chameleon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class MakeArr {



    public static void main(String[] args) {

        String filePath = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\graphData.txt";
        File file = new File(filePath);
        ArrayList<String[]> dataArray = new ArrayList<String[]>();
        // 原始坐标点数据
        ArrayList<Point> totalPoints;
        // 总的坐标点的个数
        int pointNum;
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            String[] tempArray;
            while ((str = in.readLine()) != null) {
                tempArray = str.split(" ");
                dataArray.add(tempArray);
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

        Point p;
        totalPoints = new ArrayList<>();
        for (String[] array : dataArray) {
            p = new Point(array[0], array[1], array[2]);
            totalPoints.add(p);
        }
        pointNum = totalPoints.size();


        double[][] DisMatrix=new double[pointNum ][pointNum ];

        for(int i=0,len=pointNum;i<len;i++){
            for(int j=i+1,jlen=pointNum;j<jlen;j++){
                DisMatrix[i][j]=DisMatrix[j][i]=Distance(totalPoints.get(i),totalPoints.get(j));
            }
        }

        for(int i=0,len=pointNum;i<len;i++){
            for(int j=0,jlen=pointNum;j<jlen;j++){
                System.out.print(DisMatrix[i][j]+"\t");
            }
            System.out.println();

        }

    }


    /**
     * 计算当前点与制定点之间的欧式距离
     *
     * @param p1
     *            待计算聚类的p点
     * @return
     */
    public static double Distance(Point p1,Point p2) {
        double distance = 0;

        distance = Math.pow((p1.x - p2.x),2) +Math.pow((p1.y - p2.y),2) ;

        distance = Math.sqrt(distance);

        return distance;
    }

}
