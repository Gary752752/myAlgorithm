package com.algorithm.LCfusion.Main.StardardFusionMain;

import com.algorithm.LCfusion.hcl.math.openforecast.DataPoint;
import com.algorithm.LCfusion.hcl.math.openforecast.DataSet;
import com.algorithm.LCfusion.hcl.math.openforecast.MultipleLinearRegressionModel;
import com.algorithm.LCfusion.hcl.math.openforecast.Observation;

import java.io.*;
import java.util.Hashtable;

public class LCLRweights_C_SF {
    static double [][] weights=new double[6][200];
    public double dealQrels(String require,double qrels){
        if(require.equals("binary"))
            return (qrels>0)? 1.0:0.0;//binary
        else if(require.equals("graded"))
            return (qrels>0)? qrels:0.0;//graded
        return qrels;
    }
    public void generateAGroupWeight_ji(BufferedReader reader,String dealqrelway,int group,int numofsystem,String jiou)throws Exception
    {



        DataSet observedData=new DataSet();
        DataPoint dp;
        double[] feature;
        String[] items;
        String[] valuename=new String[numofsystem];
        for(int i=0;i<numofsystem;i++){
            valuename[i]=new String("x"+i);
        }
        String templine="";
        while((templine=reader.readLine())!=null)
        {
            items=templine.split("\t");
            feature=new double[numofsystem+1];
            for(int i=2;i<items.length;i++)
            {
                if(i==(items.length-1))
                {
                    //feature[i-2]=dealQrels(dealqrelway,Double.parseDouble(items[i]));//the value is relevance !!!!!!!!!!!!!!!!!!!
                    feature[i-2]=Double.parseDouble(items[i]);//the value is relevance !!!!!!!!!!!!!!!!!!!
                    //System.out.print("relevance "+feature[i-2]);
                }else
                {
                    if(items[i].equals("null"))
                    {
                        feature[i-2]=0.0;
                        //System.out.print("null "+feature[i-2]);
                    }
                    else
                    {
                        feature[i-2]=Double.parseDouble(items[i]);
                        //System.out.print(feature[i-2]);
                    }
                }

            }

			/*
			for(int i=0;i<=numofsystem;i++)
			{
				bw.write(feature[i]+"\t");
			}
			bw.write("\n");
			*/

            //give to mlr
            dp = new Observation(feature[numofsystem]);//y
            for(int i=0;i<numofsystem;i++){
                dp.setIndependentValue(valuename[i], feature[i]);
                //System.out.print(arg0);
            }
            observedData.add(dp);

        }

		/*bw.flush();
		bw.close();
		reader.close();*/

        MultipleLinearRegressionModel mlr=new MultipleLinearRegressionModel(valuename);
        mlr.init(observedData);
        Hashtable<String,Double> co=mlr.getCoefficients();
        System.out.println("ji weight");
        for (int i=0; i<numofsystem; i++){
            weights[group][i] = co.get(valuename[i]);
            System.out.print(weights[group][i]+",");
        }
        //System.out.println("bbbbbbbbbbbbbbbbbbbbbbbb");
        //observedData=null;
    }

    public void generateAGroupWeight_ou(BufferedReader reader,String dealqrelway,int group,int numofsystem,String jiou)throws Exception
    {

        DataSet observedData=new DataSet();
        DataPoint dp;
        double[] feature;
        String[] items;
        String[] valuename=new String[numofsystem];
        for(int i=0;i<numofsystem;i++){
            valuename[i]=new String("x"+i);
        }
        String templine="";
        while((templine=reader.readLine())!=null)
        {
            items=templine.split("\t");
            feature=new double[numofsystem+1];
            for(int i=2;i<items.length;i++)
            {
                if(i==(items.length-1))
                {
                    //feature[i-2]=dealQrels(dealqrelway,Double.parseDouble(items[i]));//the value is relevance !!!!!!!!!!!!!!!!!!!
                    feature[i-2]=Double.parseDouble(items[i]);//the value is relevance !!!!!!!!!!!!!!!!!!!
                    //System.out.print("relevance "+feature[i-2]);
                }else
                {
                    if(items[i].equals("null"))
                    {
                        feature[i-2]=0.0;
                        //System.out.print("null "+feature[i-2]);
                    }
                    else
                    {
                        feature[i-2]=Double.parseDouble(items[i]);
                        //System.out.print(feature[i-2]);
                    }
                }

            }

			/*
			for(int i=0;i<=numofsystem;i++)
			{
				bw.write(feature[i]+"\t");
			}
			bw.write("\n");
			*/

            //give to mlr
            dp = new Observation(feature[numofsystem]);//y
            for(int i=0;i<numofsystem;i++){
                dp.setIndependentValue(valuename[i], feature[i]);
                //System.out.print(arg0);
            }
            observedData.add(dp);

        }

		/*bw.flush();
		bw.close();
		reader.close();*/

        MultipleLinearRegressionModel mlr=new MultipleLinearRegressionModel(valuename);
        mlr.init(observedData);
        Hashtable<String,Double> co=mlr.getCoefficients();
        System.out.println("ou weight");
        for (int i=0; i<numofsystem; i++){
            weights[group][i] = co.get(valuename[i]);
            System.out.print(weights[group][i]+",");
        }
        //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        //observedData=null;
    }

    public void generateAGroupWeight_type(BufferedReader reader,String dealqrelway,int group,int numofsystem,String jiou)throws Exception
    {

        DataSet observedData=new DataSet();
        DataPoint dp;
        double[] feature;
        String[] items;
        String[] valuename=new String[numofsystem];
        for(int i=0;i<numofsystem;i++){
            valuename[i]=new String("x"+i);
        }
        String templine="";
        while((templine=reader.readLine())!=null)
        {
            items=templine.split("\t");
            feature=new double[numofsystem+1];
            for(int i=2;i<items.length;i++)
            {
                if(i==(items.length-1))
                {
                    //feature[i-2]=dealQrels(dealqrelway,Double.parseDouble(items[i]));//the value is relevance !!!!!!!!!!!!!!!!!!!
                    feature[i-2]=Double.parseDouble(items[i]);//the value is relevance !!!!!!!!!!!!!!!!!!!
                    //System.out.print("relevance "+feature[i-2]);
                }else
                {
                    if(items[i].equals("null"))
                    {
                        feature[i-2]=0.0;
                        //System.out.print("null "+feature[i-2]);
                    }
                    else
                    {
                        feature[i-2]=Double.parseDouble(items[i]);
                        //System.out.print(feature[i-2]);
                    }
                }

            }

			/*
			for(int i=0;i<=numofsystem;i++)
			{
				bw.write(feature[i]+"\t");
			}
			bw.write("\n");
			*/

            //give to mlr
            dp = new Observation(feature[numofsystem]);//y
            for(int i=0;i<numofsystem;i++){
                dp.setIndependentValue(valuename[i], feature[i]);
                //System.out.print(arg0);
            }
            observedData.add(dp);

        }

		/*bw.flush();
		bw.close();
		reader.close();*/

        MultipleLinearRegressionModel mlr=new MultipleLinearRegressionModel(valuename);
        mlr.init(observedData);
        Hashtable<String,Double> co=mlr.getCoefficients();
        System.out.println();
        for (int i=0; i<numofsystem; i++){
            weights[group][i] = co.get(valuename[i]);
            System.out.print(weights[group][i]+",");
        }

        //observedData=null;
    }
    /*
     * 写权重文件
     * 偶权重在第一行
     * 奇权重在第二行
     */
    public void write_weight_file(String weightfilepath,int numofsystem) throws IOException {
        BufferedWriter bw=new BufferedWriter(new FileWriter(weightfilepath));
        for(int i=1;i<weights.length;i++) {
            for(int j=0;j<numofsystem;j++) {
                bw.write(weights[i][j]+",");
                //System.out.print(weights[i][j]+",");
            }
            //System.out.println();
            bw.write("\n");
        }
        bw.close();
    }
    public static void main(String[] args)throws Exception
    {
        int num_classification ;
        for(num_classification=2;num_classification<=24;num_classification++) {
            LCLRweights_C_SF lw=new LCLRweights_C_SF();
            String jiou="ji";
            String output="D:\\TRECDateset\\zhangzhen\\new_PMSA2018A\\sort2018LCchaou_jiou_weights\\sort2018chaou_LC_"+num_classification+"_weights";

            String path="D:\\TRECDateset\\zhangzhen\\new_PMSA2018A\\sort2018LCchaou_jiou\\sort2018chaou_LCjiou_"+num_classification+jiou;
            BufferedReader br=new BufferedReader(new FileReader(path));
            lw.generateAGroupWeight_ji(br, "binary", 2, num_classification,jiou);


            jiou="ou";
            path="D:\\TRECDateset\\zhangzhen\\new_PMSA2018A\\sort2018LCchaou_jiou\\sort2018chaou_LCjiou_"+num_classification+jiou;
            br=new BufferedReader(new FileReader(path));
            lw.generateAGroupWeight_ou(br, "binary", 1, num_classification,jiou);

            lw.write_weight_file(output, num_classification);

            br.close();
            //清空权重数组
            weights = new double[3][30];
        }
        System.out.println();
        System.out.println("end");
    }

}
