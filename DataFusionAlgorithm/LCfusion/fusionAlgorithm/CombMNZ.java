package com.algorithm.LCfusion.fusionAlgorithm;

import com.algorithm.LCfusion.LC.*;
import com.algorithm.LCfusion.dataStruct.*;

import java.io.*;
import java.util.Hashtable;


public class CombMNZ extends LC{

	public CombMNZ(int numofsystem, int numofquery, int startquery,
			Hashtable<String, Integer> sys) {
		super(numofsystem, numofquery, startquery, sys);
		// TODO Auto-generated constructor stub
	}

	public void CombMNZFusion2020health_classification(String[] classification_run,String output,String combsysname) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];//��������ϵͳ��Ӧ��resultlist����
		//File fpath=new File(norminput);//��ȡ���ݼ��ļ�
		//File[] files=fpath.listFiles();//��ȡ�������ݼ��ļ���������Ӧ��file����
		BufferedReader[] readers=new BufferedReader[numofsystem];//������ȡ�������ݼ��ļ�������������
		FileWriter writer=new FileWriter(output);//�������������������ļ��������
		int qqID=1;
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(classification_run[i]));
			//readers[i]=new BufferedReader(new FileReader(files[i]));//��ÿ����ȡ�����������ÿ�����ݼ��ļ���Ӧ����
			bufferline[i]=new String(readers[i].readLine());	//������ȡ�������ļ��Ķ��󲢽����е�һ�н��ж�ȡ�������ַ�����
		}
		//for all query
	    if(startquery==51){
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
				combineSystem(qid+startquery, bt, combsysname,writer);
			}
	    	
	    }else
	    {
	    	for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50��
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
				combineSystem(qqID, bt, combsysname,writer);
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
	public void CombMNZFusion2017_Cha_classification(String[] classification_run,String output,String combsysname) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];//��������ϵͳ��Ӧ��resultlist����
		//File fpath=new File(norminput);//��ȡ���ݼ��ļ�
		//File[] files=fpath.listFiles();//��ȡ�������ݼ��ļ���������Ӧ��file����
		BufferedReader[] readers=new BufferedReader[numofsystem];//������ȡ�������ݼ��ļ�������������
		FileWriter writer=new FileWriter(output);//�������������������ļ��������
		int qqID=0;
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(classification_run[i]));
			//readers[i]=new BufferedReader(new FileReader(files[i]));//��ÿ����ȡ�����������ÿ�����ݼ��ļ���Ӧ����
			bufferline[i]=new String(readers[i].readLine());	//������ȡ�������ļ��Ķ��󲢽����е�һ�н��ж�ȡ�������ַ�����
		}
		//for all query
	    if(startquery==51){
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
				combineSystem(qid+startquery, bt, combsysname,writer);
			}
	    	
	    }else
	    {
	    	for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50��
				//:get the lists
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");	
					qqID = grouplists[i].topic;
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
				combineSystem(qqID, bt, combsysname,writer);
			}
	    }
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	public void CombMNZFusion2018_Cha_classification(String[] classification_run,String output,String combsysname) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];//��������ϵͳ��Ӧ��resultlist����
		//File fpath=new File(norminput);//��ȡ���ݼ��ļ�
		//File[] files=fpath.listFiles();//��ȡ�������ݼ��ļ���������Ӧ��file����
		BufferedReader[] readers=new BufferedReader[numofsystem];//������ȡ�������ݼ��ļ�������������
		FileWriter writer=new FileWriter(output);//�������������������ļ��������
		int qqID=0;
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(classification_run[i]));
			//readers[i]=new BufferedReader(new FileReader(files[i]));//��ÿ����ȡ�����������ÿ�����ݼ��ļ���Ӧ����
			bufferline[i]=new String(readers[i].readLine());	//������ȡ�������ļ��Ķ��󲢽����е�һ�н��ж�ȡ�������ַ�����
		}
		//for all query
	    if(startquery==51){
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
				combineSystem(qid+startquery, bt, combsysname,writer);
			}
	    	
	    }else
	    {
	    	for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50��
				//:get the lists
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");	
					qqID = grouplists[i].topic;
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
				combineSystem(qqID, bt, combsysname,writer);
			}
	    }
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	public void CombMNZFusion_classification(String[] classification_run,String output,String combsysname) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];//��������ϵͳ��Ӧ��resultlist����
		//File fpath=new File(norminput);//��ȡ���ݼ��ļ�
		//File[] files=fpath.listFiles();//��ȡ�������ݼ��ļ���������Ӧ��file����
		BufferedReader[] readers=new BufferedReader[numofsystem];//������ȡ�������ݼ��ļ�������������
		FileWriter writer=new FileWriter(output);//�������������������ļ��������
		int qqID=0;
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(classification_run[i]));
			//readers[i]=new BufferedReader(new FileReader(files[i]));//��ÿ����ȡ�����������ÿ�����ݼ��ļ���Ӧ����
			bufferline[i]=new String(readers[i].readLine());	//������ȡ�������ļ��Ķ��󲢽����е�һ�н��ж�ȡ�������ַ�����
		}
		//for all query
	    if(startquery==-1){
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
				combineSystem(qid+startquery, bt, combsysname,writer);
			}
	    	
	    }else
	    {
	    	for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50��
				//:get the lists
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					//��ִ��2010�������������100topic���������Ա�ע��
					/*
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");	
					*/
					qqID = grouplists[i].topic;
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
				//System.out.println(qqID);
				combineSystem(qqID, bt, combsysname,writer);
			}
	    }
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	public void CombMNZFusion2018_C_FS_classification(String[] classification_run,String output,String combsysname) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];//��������ϵͳ��Ӧ��resultlist����
		//File fpath=new File(norminput);//��ȡ���ݼ��ļ�
		//File[] files=fpath.listFiles();//��ȡ�������ݼ��ļ���������Ӧ��file����
		BufferedReader[] readers=new BufferedReader[numofsystem];//������ȡ�������ݼ��ļ�������������
		FileWriter writer=new FileWriter(output);//�������������������ļ��������
		int qqID=0;
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(classification_run[i]));
			//readers[i]=new BufferedReader(new FileReader(files[i]));//��ÿ����ȡ�����������ÿ�����ݼ��ļ���Ӧ����
			bufferline[i]=new String(readers[i].readLine());	//������ȡ�������ļ��Ķ��󲢽����е�һ�н��ж�ȡ�������ַ�����
		}
		//for all query
	    if(startquery==51){
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
				combineSystem(qid+startquery, bt, combsysname,writer);
			}
	    	
	    }else
	    {
	    	for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50��
				//:get the lists
				for(int i=0;i<numofsystem;i++){		
					grouplists[i]=getResultList(readers[i],i,true);
					if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
						throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");	
					qqID = grouplists[i].topic;
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
				combineSystem(qqID, bt, combsysname,writer);
			}
	    }
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	public void CombMNZFusion2020health(String norminput,String output,String combsysname) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];//��������ϵͳ��Ӧ��resultlist����
		File fpath=new File(norminput);//��ȡ���ݼ��ļ�
		File[] files=fpath.listFiles();//��ȡ�������ݼ��ļ���������Ӧ��file����
		BufferedReader[] readers=new BufferedReader[numofsystem];//������ȡ�������ݼ��ļ�������������
		FileWriter writer=new FileWriter(output);//�������������������ļ��������
		int qqID=1;
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(files[i]));//��ÿ����ȡ�����������ÿ�����ݼ��ļ���Ӧ����
			bufferline[i]=new String(readers[i].readLine());	//������ȡ�������ļ��Ķ��󲢽����е�һ�н��ж�ȡ�������ַ�����
		}
		//for all query
	    if(startquery==51){
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
				combineSystem(qid+startquery, bt, combsysname,writer);
			}
	    	
	    }else
	    {
	    	for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50��
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
				combineSystem(qqID, bt, combsysname,writer);
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
	public void CombMNZFusion(String norminput,String output,String combsysname) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];//��������ϵͳ��Ӧ��resultlist����
		File fpath=new File(norminput);//��ȡ���ݼ��ļ�
		File[] files=fpath.listFiles();//��ȡ�������ݼ��ļ���������Ӧ��file����
		BufferedReader[] readers=new BufferedReader[numofsystem];//������ȡ�������ݼ��ļ�������������
		FileWriter writer=new FileWriter(output);//�������������������ļ��������
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(files[i]));//��ÿ����ȡ�����������ÿ�����ݼ��ļ���Ӧ����
			bufferline[i]=new String(readers[i].readLine());	//������ȡ�������ļ��Ķ��󲢽����е�һ�н��ж�ȡ�������ַ�����
		}
		//for all query
	    if(startquery==51){
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
				combineSystem(qid+startquery, bt, combsysname,writer);
			}
	    	
	    }else
	    {
	    	for(int qid=0;qid<numofquery;qid++){//������ѯidС�ڵ���50�������һ��numofquery�ڳ�ʼ��ʱΪ50��
				//:get the lists
					
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
				combineSystem(qid+startquery, bt, combsysname,writer);
			}
	    }
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}

	public void combineSystem(int topic,BinaryTree bt,String combsysname,FileWriter writer) throws IOException{
		String temp ="";
		BinaryTreeNode btn = null;
		TreeByAttribute tba = new TreeByAttribute();
		btn=bt.getFirstNodeInLexicalOrder();
		while (btn!=null){
			tba.search_add(btn.getName(),btn.getScore()*btn.getCount());//score���ĵ�������ϵͳ�ķ�ֵ�ͣ�count���ĵ����ֵĴ���
			btn=btn.getSuccesor();
		}
		btn=tba.getFirstNodeInOrder();
		int i=1;
		while (btn!=null&&i<=standardlength)
		{
			temp=""; temp=new Integer(topic).toString();
			temp=temp.concat("\tQ0\t");
			temp=temp.concat(btn.getName());
			temp=temp.concat("\t"+new Integer(i).toString()+"\t");
			temp=temp.concat(new Double (btn.getScore()).toString());
			temp=temp.concat("\t"+combsysname+"\n");
			writer.write(temp);
			btn=btn.getSuccesor(); 
			i++;
		}
	}
	
}
