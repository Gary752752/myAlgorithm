package com.algorithm.LCfusion.fusionAlgorithm;

import com.algorithm.LCfusion.LC.LC;
import com.algorithm.LCfusion.dataStruct.BinaryTree;
import com.algorithm.LCfusion.dataStruct.ResultList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Hashtable;
 
public class CombSUM extends LC{
	
	public CombSUM(int numofsystem, int numofquery, int startquery,
			Hashtable<String, Integer> sys) {
		super(numofsystem, numofquery, startquery, sys);
		// TODO Auto-generated constructor stub
	}
	/*
	 * norminput���ݼ��ļ���
	 * output������·��+�ļ���
	 * combsysname������ϵͳ��
	 * */
	public void CombSumFusion2020health_classification(String[] classification_run,String output,String combsysname) throws Exception{
		//File fpath=new File(norminput);
		//File[] files=fpath.listFiles();
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		int qqID=1;
		//open file reader Ϊÿһ������������ֵ������ȡ��ÿ���������ĵ�һ������
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(classification_run[i]));
			//readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		int count_test1=0;
		if(startquery==51){//
			for(int qid=0;qid<numofquery;qid++){
				//:get the lists
				if (qid == 44 || qid == 49) {
					continue;
				}
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
				}
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){
					for (int i=0; i<grouplists[sid].list.size(); i++){
						String docid=grouplists[sid].list.get(i).docid;
						double score=grouplists[sid].list.get(i).score;
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qid+startquery, bt, combsysname,writer);
			}
		}else {
			for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50������������ݼ��л�topic������Ҫ����
				//:get the lists
				if(qqID==33) {
					qqID++;
				}
				if(qqID==35) {
					qqID++;
				}
				if(qqID==46) {
					qqID++;
				}
				if(qqID==48) {
					qqID++;
				}
				for(int i=0;i<numofsystem;i++){//����ÿһ������ϵͳ�ĵ�ǰ��ѯid�����м�¼
					/*��ȡÿһ��ϵͳ��ͬһ��queryID������ÿ��ϵͳ��ͬһ����ѯ���µ��ĵ����뵽��ͬ��Resultlist������
					 * ������Щ����ֱ𷵻���gruplists������
					 */
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
				}
				
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){//�������еķ���ϵͳ
					for (int i=0; i<grouplists[sid].list.size(); i++){//����ÿһ������ϵͳ�е�ͬһ��queryID��resultlist�б�
						String docid=grouplists[sid].list.get(i).docid;//��ȡ���е�һ���ļ����ļ�ID
						double score=grouplists[sid].list.get(i).score;//��ȡ����һ���ļ����ļ�����Է���
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qqID, bt, combsysname,writer);
				qqID++;
			}
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	/*
	 * norminput���ݼ��ļ���
	 * output������·��+�ļ���
	 * combsysname������ϵͳ��
	 * */
	public void CombSumFusion2017_Cha_classification(String[] classification_run,String output,String combsysname) throws Exception{
		//File fpath=new File(norminput);
		//File[] files=fpath.listFiles();
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		
		//open file reader Ϊÿһ������������ֵ������ȡ��ÿ���������ĵ�һ������
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(classification_run[i]));
			//readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		int count_test1=0;
		if(startquery==51){//
			for(int qid=0;qid<numofquery;qid++){
				//:get the lists
				if (qid == 44 || qid == 49) {
					continue;
				}
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
				}
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){
					for (int i=0; i<grouplists[sid].list.size(); i++){
						String docid=grouplists[sid].list.get(i).docid;
						double score=grouplists[sid].list.get(i).score;
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qid+startquery, bt, combsysname,writer);
			}
		}else {
			for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50������������ݼ��л�topic������Ҫ����
				//:get the lists
				int qqID=0;
				for(int i=0;i<numofsystem;i++){//����ÿһ������ϵͳ�ĵ�ǰ��ѯid�����м�¼
					/*��ȡÿһ��ϵͳ��ͬһ��queryID������ÿ��ϵͳ��ͬһ����ѯ���µ��ĵ����뵽��ͬ��Resultlist������
					 * ������Щ����ֱ𷵻���gruplists������
					 */
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");
					qqID=grouplists[i].topic;
				}
				
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){//�������еķ���ϵͳ
					for (int i=0; i<grouplists[sid].list.size(); i++){//����ÿһ������ϵͳ�е�ͬһ��queryID��resultlist�б�
						String docid=grouplists[sid].list.get(i).docid;//��ȡ���е�һ���ļ����ļ�ID
						double score=grouplists[sid].list.get(i).score;//��ȡ����һ���ļ����ļ�����Է���
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qqID, bt, combsysname,writer);
			}
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	/*
	 * norminput���ݼ��ļ���
	 * output������·��+�ļ���
	 * combsysname������ϵͳ��
	 * */
	public void CombSumFusion2018_Cha_classification(String[] classification_run,String output,String combsysname) throws Exception{
		//File fpath=new File(norminput);
		//File[] files=fpath.listFiles();
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		
		//open file reader Ϊÿһ������������ֵ������ȡ��ÿ���������ĵ�һ������
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(classification_run[i]));
			//readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		int count_test1=0;
		if(startquery==51){//
			for(int qid=0;qid<numofquery;qid++){
				//:get the lists
				if (qid == 44 || qid == 49) {
					continue;
				}
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
				}
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){
					for (int i=0; i<grouplists[sid].list.size(); i++){
						String docid=grouplists[sid].list.get(i).docid;
						double score=grouplists[sid].list.get(i).score;
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qid+startquery, bt, combsysname,writer);
			}
		}else {
			for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50������������ݼ��л�topic������Ҫ����
				//:get the lists
				int qqID=0;
				for(int i=0;i<numofsystem;i++){//����ÿһ������ϵͳ�ĵ�ǰ��ѯid�����м�¼
					/*��ȡÿһ��ϵͳ��ͬһ��queryID������ÿ��ϵͳ��ͬһ����ѯ���µ��ĵ����뵽��ͬ��Resultlist������
					 * ������Щ����ֱ𷵻���gruplists������
					 */
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");
					qqID=grouplists[i].topic;
				}
				
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){//�������еķ���ϵͳ
					for (int i=0; i<grouplists[sid].list.size(); i++){//����ÿһ������ϵͳ�е�ͬһ��queryID��resultlist�б�
						String docid=grouplists[sid].list.get(i).docid;//��ȡ���е�һ���ļ����ļ�ID
						double score=grouplists[sid].list.get(i).score;//��ȡ����һ���ļ����ļ�����Է���
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qqID, bt, combsysname,writer);
			}
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	/*
	 * norminput���ݼ��ļ���
	 * output������·��+�ļ���
	 * combsysname������ϵͳ��
	 * */
	public void CombSumFusion_classification(String[] classification_run,String output,String combsysname) throws Exception{
		//File fpath=new File(norminput);
		//File[] files=fpath.listFiles();
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		
		//open file reader Ϊÿһ������������ֵ������ȡ��ÿ���������ĵ�һ������
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(classification_run[i]));
			//readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		int count_test1=0;
		if(startquery==-1){//
			for(int qid=0;qid<numofquery;qid++){
				//:get the lists
				if (qid == 44 || qid == 49) {
					continue;
				}
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
				}
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){
					for (int i=0; i<grouplists[sid].list.size(); i++){
						String docid=grouplists[sid].list.get(i).docid;
						double score=grouplists[sid].list.get(i).score;
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qid+startquery, bt, combsysname,writer);
			}
		}else {
			for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50������������ݼ��л�topic������Ҫ����
				//:get the lists
				int qqID=startquery;
				for(int i=0;i<numofsystem;i++){//����ÿһ������ϵͳ�ĵ�ǰ��ѯid�����м�¼
					/*��ȡÿһ��ϵͳ��ͬһ��queryID������ÿ��ϵͳ��ͬһ����ѯ���µ��ĵ����뵽��ͬ��Resultlist������
					 * ������Щ����ֱ𷵻���gruplists������
					 */
					grouplists[i]=getResultList(readers[i],i,true);
					//��ִ��2010�������������100topic���������Ա�ע��
					/*
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic) {
						System.out.println(grouplists[i].topic+"\t"+grouplists[i-1].topic);
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");
					}
					*/
					qqID=grouplists[i].topic;
				}
				
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){//�������еķ���ϵͳ
					for (int i=0; i<grouplists[sid].list.size(); i++){//����ÿһ������ϵͳ�е�ͬһ��queryID��resultlist�б�
						String docid=grouplists[sid].list.get(i).docid;//��ȡ���е�һ���ļ����ļ�ID
						double score=grouplists[sid].list.get(i).score;//��ȡ����һ���ļ����ļ�����Է���
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qqID, bt, combsysname,writer);
			}
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	/*
	 * norminput���ݼ��ļ���
	 * output������·��+�ļ���
	 * combsysname������ϵͳ��
	 * */
	public void CombSumFusion2018_C_SF_classification(String[] classification_run,String output,String combsysname) throws Exception{
		//File fpath=new File(norminput);
		//File[] files=fpath.listFiles();
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		
		//open file reader Ϊÿһ������������ֵ������ȡ��ÿ���������ĵ�һ������
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(classification_run[i]));
			//readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		int count_test1=0;
		if(startquery==51){//
			for(int qid=0;qid<numofquery;qid++){
				//:get the lists
				if (qid == 44 || qid == 49) {
					continue;
				}
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
				}
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){
					for (int i=0; i<grouplists[sid].list.size(); i++){
						String docid=grouplists[sid].list.get(i).docid;
						double score=grouplists[sid].list.get(i).score;
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qid+startquery, bt, combsysname,writer);
			}
		}else {
			for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50������������ݼ��л�topic������Ҫ����
				//:get the lists
				int qqID=0;
				for(int i=0;i<numofsystem;i++){//����ÿһ������ϵͳ�ĵ�ǰ��ѯid�����м�¼
					/*��ȡÿһ��ϵͳ��ͬһ��queryID������ÿ��ϵͳ��ͬһ����ѯ���µ��ĵ����뵽��ͬ��Resultlist������
					 * ������Щ����ֱ𷵻���gruplists������
					 */
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");
					qqID=grouplists[i].topic;
				}
				
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){//�������еķ���ϵͳ
					for (int i=0; i<grouplists[sid].list.size(); i++){//����ÿһ������ϵͳ�е�ͬһ��queryID��resultlist�б�
						String docid=grouplists[sid].list.get(i).docid;//��ȡ���е�һ���ļ����ļ�ID
						double score=grouplists[sid].list.get(i).score;//��ȡ����һ���ļ����ļ�����Է���
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qqID, bt, combsysname,writer);
			}
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	/*
	 * norminput���ݼ��ļ���
	 * output������·��+�ļ���
	 * combsysname������ϵͳ��
	 * */
	public void CombSumFusion2020health(String norminput,String output,String combsysname) throws Exception{
		File fpath=new File(norminput);
		File[] files=fpath.listFiles();
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		int qqID=1;
		//open file reader Ϊÿһ������������ֵ������ȡ��ÿ���������ĵ�һ������
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		int count_test1=0;
		if(startquery==51){//
			for(int qid=0;qid<numofquery;qid++){
				//:get the lists
				if (qid == 44 || qid == 49) {
					continue;
				}
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
				}
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){
					for (int i=0; i<grouplists[sid].list.size(); i++){
						String docid=grouplists[sid].list.get(i).docid;
						double score=grouplists[sid].list.get(i).score;
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qid+startquery, bt, combsysname,writer);
			}
		}else {
			for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50������������ݼ��л�topic������Ҫ����
				//:get the lists
				if(qqID==33) {
					qqID++;
				}
				if(qqID==35) {
					qqID++;
				}
				if(qqID==46) {
					qqID++;
				}
				if(qqID==48) {
					qqID++;
				}
				for(int i=0;i<numofsystem;i++){//����ÿһ������ϵͳ�ĵ�ǰ��ѯid�����м�¼
					/*��ȡÿһ��ϵͳ��ͬһ��queryID������ÿ��ϵͳ��ͬһ����ѯ���µ��ĵ����뵽��ͬ��Resultlist������
					 * ������Щ����ֱ𷵻���gruplists������
					 */
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
				}
				
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){//�������еķ���ϵͳ
					for (int i=0; i<grouplists[sid].list.size(); i++){//����ÿһ������ϵͳ�е�ͬһ��queryID��resultlist�б�
						String docid=grouplists[sid].list.get(i).docid;//��ȡ���е�һ���ļ����ļ�ID
						double score=grouplists[sid].list.get(i).score;//��ȡ����һ���ļ����ļ�����Է���
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qqID, bt, combsysname,writer);
				qqID++;
			}
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	/*
	 * norminput���ݼ��ļ���
	 * output������·��+�ļ���
	 * combsysname������ϵͳ��
	 * */
	public void CombSumFusion(String norminput,String output,String combsysname) throws Exception{
		File fpath=new File(norminput);
		File[] files=fpath.listFiles();
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		//open file reader Ϊÿһ������������ֵ������ȡ��ÿ���������ĵ�һ������
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		int count_test1=0;
		if(startquery==51){//
			for(int qid=0;qid<numofquery;qid++){
				//:get the lists
				if (qid == 44 || qid == 49) {
					continue;
				}
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
				}
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){
					for (int i=0; i<grouplists[sid].list.size(); i++){
						String docid=grouplists[sid].list.get(i).docid;
						double score=grouplists[sid].list.get(i).score;
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qid+startquery, bt, combsysname,writer);
			}
		}else{
			for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50��
				//:get the lists
				for(int i=0;i<numofsystem;i++){//����ÿһ������ϵͳ�ĵ�ǰ��ѯid�����м�¼
					/*��ȡÿһ��ϵͳ��ͬһ��queryID������ÿ��ϵͳ��ͬһ����ѯ���µ��ĵ����뵽��ͬ��Resultlist������
					 * ������Щ����ֱ𷵻���gruplists������
					 */
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
				}
				
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){//�������еķ���ϵͳ
					for (int i=0; i<grouplists[sid].list.size(); i++){//����ÿһ������ϵͳ�е�ͬһ��queryID��resultlist�б�
						String docid=grouplists[sid].list.get(i).docid;//��ȡ���е�һ���ļ����ļ�ID
						double score=grouplists[sid].list.get(i).score;//��ȡ����һ���ļ����ļ�����Է���
						bt.search_add(docid, score);
					}
				}
				combineGroupSystem(qid+startquery, bt, combsysname,writer);
			}
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
}
