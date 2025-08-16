package com.algorithm.LCfusion.org.hcl.fusion.fuse;

import com.algorithm.LCfusion.hcl.fusion.datastruct.*;
import com.algorithm.LCfusion.hcl.fusion.datastruct.QrelsList.QrelsNote;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author hcl
 *
 */
public class LC {
	public static int numofsystem;//number of component systems
	public static int numofquery;//number of query
	public int startquery;//start num of query
	public int de_gid;//groupid for DE train-test
	public static Hashtable<String,Integer> systems=new Hashtable<>();
	public static int standardlength=10000;
	public static String[] bufferline;
	public String bufferqrel;
	public double[][] weights;//5 groups of weights
	public double[] weight;//1 group of weight
	public boolean qrel_adhoc_2009=false;
	
	//-------------------------------------���캯��&��Աget/set-------------------------------------------
	public LC(){
		LC.bufferline=new String[numofsystem];
	}
	public LC(int numofsystem,int numofquery,int startquery,Hashtable<String, Integer> sys){
		LC.numofsystem=numofsystem;
		LC.numofquery=numofquery;
		this.startquery=startquery;
		LC.systems=sys;
		this.weights=new double[5][numofsystem];
		LC.bufferline=new String[numofsystem];
		this.bufferqrel=new String();
	}
	//for DE
	public LC(int numofsystem,int numofquery,int startquery,int groupid,Hashtable<String, Integer> sys){
		LC.numofsystem=numofsystem;
		LC.numofquery=numofquery;
		this.startquery=startquery;
		this.de_gid=groupid;
		LC.systems=sys;
		this.weight=new double[numofsystem];
		LC.bufferline=new String[numofsystem];
		this.bufferqrel=new String();
	}
	public int getStandardlength() {
		return standardlength;
	}
	public void setStandardlength(int standardlength) {
		LC.standardlength = standardlength;
	}
	public void setQrelFlag(boolean isSpecial){
		this.qrel_adhoc_2009=isSpecial;
	}
	/**
	 * set system:��rawinputĿ¼�»�ȡ����input�����ϵͳ�������ϣֵ��Ϊ��������ں�����
	 * <p>ÿ��ϵͳһ��������ţ�1-numofsystem����ϣ���ʽ��system,sid��(������ڸ����ݼ���Ӧ������������ļ�,system=��qrels��,sid=-1)</p>
	 * @param rawinput
	 * @return
	 * @throws IOException
	 */
	public static Hashtable<String, Integer> setSystem(String rawinput) throws IOException{
		File fpath=new File(rawinput);
		File[] files=fpath.listFiles();
		Hashtable<String, Integer> systems=new Hashtable<>();
		systems.put("qrels", -1);
		BufferedReader reader=null;
		for(int i=0;i<files.length;i++){
			reader=new BufferedReader(new FileReader(files[i]));
			//StringTokenizer tokenizer=new StringTokenizer(reader.readLine());
			//for(int item=1;item<=5;item++) tokenizer.nextToken();
			//String sysname=tokenizer.nextToken();
			String sysname=files[i].getName();
			systems.put(sysname, i);
			System.err.print(sysname+" ");
			reader.close();
		}
		System.out.println(" ");
		return systems;
	}
    /**
     * set system:��rawinputĿ¼�»�ȡ<b>����</b>input�����ϵͳ�������ϣֵ��Ϊ��������ں�����
     * <p>ÿ��ϵͳһ��������ţ�1-numofsystem����ϣ���ʽ��system,sid��(������ڸ����ݼ���Ӧ������������ļ�,system=��qrels��,sid=-1)</p>
     * @param files ����input��Ӧ��File��������
     * @return
     * @throws IOException
     */
	public static Hashtable<String, Integer> setSystem(File[] files) throws IOException{
		Hashtable<String, Integer> systems=new Hashtable<>();
		systems.put("qrels", -1);
		BufferedReader reader=null;
		for(int i=0;i<files.length;i++){
			reader=new BufferedReader(new FileReader(files[i]));
			StringTokenizer tokenizer=new StringTokenizer(reader.readLine());
			for(int item=1;item<=5;item++) tokenizer.nextToken();
			String sysname=tokenizer.nextToken();
			systems.put(sysname, i);
			System.err.print(sysname+" ");
			reader.close();
		}
		System.out.println(" ");
		
		return systems;
	}
	
	
	//-------------------------------------��ȡ/���������ļ���һ��query���֣�input,qrelsinput��--------------------------------------
	/**
	 * ��TREC input�ļ��л�ȡһ��query�µ��ĵ�����������һ��ResultList���󣨰���topic��system���ĵ������б�docid,rank,score����
	 * <p>*ʹ��ǰ������ļ��ĵ�һ�л��棨���浽bufferline[sid]��</p>
	 * @param reader ָ��input�ļ���BufferedReader����
	 * @param sid ��ǰinput�ļ���Ӧϵͳ��sid,�����������������ţ���ͬsid�Ļ����з���һԪ��������Ĳ�ͬλ�ã�
	 * @return
	 * @throws IOException
	 */
	/**
	 * ��TREC input�ļ��л�ȡһ��query�µ��ĵ�����������һ��ResultList���󣨰���topic��system���ĵ������б�docid,rank,score����
	 * <p>*ʹ��ǰ������ļ��ĵ�һ�л��棨���浽bufferline[sid]��</p>
	 * @param reader ָ��input�ļ���BufferedReader����
	 * @param sid ��ǰinput�ļ���Ӧϵͳ��sid,�����������������ţ���ͬsid�Ļ����з���һԪ��������Ĳ�ͬλ�ã�
	 * @param isrecord ָ���Ƿ���Ҫ����ResultList����true���ǣ�����readerָ��ָ����һ��query���ĵ��������һ���������洢
	 * @return
	 * @throws IOException
	 */
	public static ResultList getResultList(BufferedReader reader,int sid,boolean isrecord) throws IOException{
		StringTokenizer tokenizer=new StringTokenizer(bufferline[sid]);
		//����topic
		int topic=Integer.parseInt(tokenizer.nextToken());
		int currenttopic=topic;
		
		if(isrecord){			
			tokenizer.nextToken();
			String docid=tokenizer.nextToken();
			int rank=Integer.parseInt(tokenizer.nextToken());
			double score=Double.parseDouble(tokenizer.nextToken());
			String system=tokenizer.nextToken();
						
			//construct the result list first item
			ResultList rList=new ResultList(topic,system);
			rList.list.add(new result(docid, rank,score));
			//read more line of the topic until it is different from current topic,and the readed line which is not with current topic have to buffer 
			String templine=null;
			while((templine=reader.readLine())!=null){
				tokenizer=new StringTokenizer(templine);
				topic=Integer.parseInt(tokenizer.nextToken());
				if(topic!=currenttopic){
					bufferline[sid]=new String(templine);
					break;
				}else{
					tokenizer.nextToken();
					docid=tokenizer.nextToken();
					try {
						rank=Integer.parseInt(tokenizer.nextToken());
					} catch (Exception e) {
						System.out.println(sid+" "+topic+" "+rank);
					}
					
					score=Double.parseDouble(tokenizer.nextToken());
					rList.list.add(new result(docid,rank,score));
				}
			}
			return rList;
		}else{
			String templine=null;
			while((templine=reader.readLine())!=null){
				tokenizer=new StringTokenizer(templine);
				topic=Integer.parseInt(tokenizer.nextToken());
				if(topic!=currenttopic){
					bufferline[sid]=new String(templine);
					return null;
				}
			}
		}
		return null;
	}
	public static ResultList getSingleResultList(File input,int topic) throws IOException{
		ResultList rlist=new ResultList();
		BufferedReader reader=new BufferedReader(new FileReader(input));
		String templine=null;
		String sysname = null;
		while((templine=reader.readLine())!=null){
			String[] items=templine.split("[ \t]");
			if(Integer.parseInt(items[0])==topic){
				sysname=items[5];
				rlist.list.add(new result(items[2], Integer.parseInt(items[3]), Double.parseDouble(items[4])));
			}else if(Integer.parseInt(items[0])>topic){
				break;
			}
		}
		rlist.system=sysname;
		rlist.topic=topic;
		reader.close();
		return rlist;
	}
	
	public static ResultList getSingleResultList_health2020(String input,int topic) throws IOException{
		ResultList rlist=new ResultList();
		BufferedReader reader=new BufferedReader(new FileReader(input));
		String templine=null;
		String sysname = null;
		while((templine=reader.readLine())!=null){
			String[] items=templine.split("[ \t]");
			if(Integer.parseInt(items[0])==topic){
				sysname=items[5];
				rlist.list.add(new result(items[2], Integer.parseInt(items[3]), Double.parseDouble(items[4])));
			}else if(Integer.parseInt(items[0])>topic){
				break;
			}
		}
		rlist.system=sysname;
		rlist.topic=topic;
		reader.close();
		return rlist;
	}
	/**
	 * ��TREC ����������ļ�qrels�л�ȡһ��query�µ�����ĵ�����Ϣ������һ��QrelsList����
	 * <p>*ʹ��ǰ������ļ��ĵ�һ�л���(���浽bufferqrel)</p>
	 * <p>*����صĲ����棨rel<=0����أ�</p>
	 * @param reader
	 * @param isrecord Ϊfalseʱ��ʾreaderָ��ָ����һ��query������ǰquery�Ľ��������
	 * @return
	 * @throws IOException
	 */
	public QrelsList getQrelsList(BufferedReader reader,boolean isrecord) throws IOException{
		StringTokenizer tokenizer=new StringTokenizer(bufferqrel);
		int topic=Integer.parseInt(tokenizer.nextToken());//qrel
		int currenttopic=topic;
		int subtopic=0;
		if(isrecord){
			QrelsList qList=new QrelsList(topic);
			if(!qrel_adhoc_2009){
				subtopic=Integer.parseInt(tokenizer.nextToken());//subtopic
			}
			String docid=tokenizer.nextToken();//docid
			int relevance=Integer.parseInt(tokenizer.nextToken());//relevance
			if(relevance>0){
				qList.getList().add(new QrelsNote(subtopic, docid, relevance));
			}	
			String templine=null;
			while((templine=reader.readLine())!=null){
				tokenizer=new StringTokenizer(templine);
				topic=Integer.parseInt(tokenizer.nextToken());
				if(topic!=currenttopic){
					bufferqrel=new String(templine);
					break;
				}else{
					if(!qrel_adhoc_2009){
						subtopic=Integer.parseInt(tokenizer.nextToken());//subtopic
					}
					docid=tokenizer.nextToken();
					relevance=Integer.parseInt(tokenizer.nextToken());
					if(relevance>0){
						qList.getList().add(new QrelsNote(subtopic, docid, relevance));
					}	
				}
			}
			return qList;
		}else{
			String templine=null;
			while((templine=reader.readLine())!=null){
				tokenizer=new StringTokenizer(templine);
				topic=Integer.parseInt(tokenizer.nextToken());
				if(topic!=currenttopic){
					bufferqrel=new String(templine);
					return null;
				}
			}
		}
		return null;
		
	}	
	//dealing with
	/**
	 * ����Եȼ�����binary/graded
	 * @param require ����Ҫ�󣺡�binary�����ߡ�graded��
	 * @param qrels ����Եȼ�
	 * @return
	 */
	public double dealQrels(String require,double qrels){
		if(require.equals("binary"))
			return (qrels>0)? 1.0:0.0;//binary
		else if(require.equals("graded"))
			return (qrels>0)? qrels:0.0;//graded	
		return qrels;
	}
	/**
	 * ��ResultList�е������ĵ���Ϣ���������������ĸ�ʽstyle
	 * <li>style=0:[sid,score,sid,score,...]</li>
	 * @param list
	 * @param bt
	 * @param sid
	 * @param style
	 */
	public void addResultlistInTree(ArrayList<result> list, BinaryTree bt,int sid,int style) {
		// TODO Auto-generated method stub
		// ������
		for(int i=0;i<list.size();i++){
			String name=list.get(i).docid;
			if(style==0){
				String partscore=sid+","+list.get(i).score+",";
				bt.search_add(name, partscore);
			}
		}
	}
	/**
	 * ��QrelList���ĵ���Ϣ���������(�����ĵ����в����ڣ�������������)������ĸ�ʽstyle
	 * <li>style=0:adhoc [qrel,rel]</li>
	 * <li>style=1:diversity [qrel,subtopic,rel,qrel,subtopic,rel,...]</li>
	 * @param list
	 * @param bt
	 * @param style
	 */
	public void addQrellistInTree(ArrayList<QrelsNote> list,BinaryTree bt,int style){
		for(int i=0;i<list.size();i++){
			String name=list.get(i).getDocid();
			if(style==0){
				String partscore=numofsystem+","+list.get(i).getRelevance()+",";
				bt.search_add_qrels(name, partscore);
			}
			if(style==1){
				String partscore=numofsystem+","+list.get(i).getSubtopic()+","+list.get(i).getRelevance()+",";
				bt.search_add_qrels(name, partscore);
			}
		}
	}
	//-------------------------------------ͳ�������ļ��������Ϣ��input,qrelsinput��----------------------------------
	/**
	 * check the a result: the query Length of a result
	 * @param input
	 * @throws IOException
	 */
	public void checkQueryLength(String input) throws IOException{
		File path=new File(input);
		File[] files=path.listFiles();
		BufferedReader[] readers=new BufferedReader[numofsystem];
		for(int j=0;j<numofsystem;j++){
			readers[j]=new BufferedReader(new FileReader(files[j]));
			bufferline[j]=new String(readers[j].readLine());
			System.out.print(","+files[j].getName());
		}
		System.out.print("\n");
		int[] total=new int[numofsystem];
		for(int i=0;i<numofquery;i++){
			System.out.print((i+startquery));
			for(int j=0;j<numofsystem;j++){
				ResultList list=getResultList(readers[j], j,true);
				total[j]+=list.list.size();
				System.out.print(","+list.list.size());
			}
			System.out.print("\n");
		}
		System.out.print("total");
		for(int j=0;j<numofsystem;j++){
			readers[j].close();
			System.out.print(","+total[j]);
		}
		
	}
	public void checkRealQueryNum(File input) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(input));
		int tempqueryid=this.startquery;
		String templine;
		while((templine=reader.readLine())!=null){
			StringTokenizer tokenizer=new StringTokenizer(templine);
			int queryid=Integer.parseInt(tokenizer.nextToken());
			if(queryid!=tempqueryid){
				tempqueryid++;
				if(queryid!=tempqueryid){
					System.err.println("the file: "+input.getName()+"miss the query "+tempqueryid);
				}
			}
		}
		if(tempqueryid<this.startquery+numofquery-1)
			for(;tempqueryid<this.startquery+numofquery-1;){
				tempqueryid++;
				System.err.println("the file: "+input.getName()+"miss the query "+tempqueryid);
			}
		reader.close();
	}
	
	
	//-------------------------------------�淶��--------------------------------------------------------
	/**
	 * �淶��score�Ĺ�ʽ������rank��</br>*���Ի���ʽ
	 * @param rank �ĵ���rank��Ϣ
	 * @param type 
	 * @param score 
	 * @param max 
	 * @param min 
	 * @return
	 */
	public static double normscore(int rank, double score, double min, double max, int type,double sum,double miu,double biaozhuncha,double zscore,double zmax,double zmin){
		if(type==0){//reciprocal rank   0-1  [a,b]  log rank  0.5*(lnrank_1_0.2+25.92*reciprank_60)  reciprocal rank80 sum
			return 1.0/(rank+61);
//			return (rank<100)?1.0/(rank+60):0;
		}
		if(type==1){//0-1
			if(min==max){
				min=0;
				max=(double)standardlength;
				score=max-rank;
			}
			return (score-min)/(max-min);
		}
		if(type==2){//[a,b]
			double a=0.06;
			double b=0.6;
			if(min==max){
				min=0;
				max=(double)standardlength;
				score=max-rank;
			}
			return (b-a)*(score-min)/(max-min)+a;
		}
		if(type==3){//log rank
//			return Math.log(rank+2);
			double k=1-0.2*Math.log(rank+1);
			return (k>0)?k:0;
//			return k;
		}
		if(type==4)//0.5*(lnrank_1_0.2+25.92*reciprank_60)

		{
			double log=1-0.2*Math.log(rank+1);
			double k=0.0;
			if(log>0)
			{
				k=log;
			}else
			{
				k=0.0;
			}
			
			double rec=1.0/(rank+60);
			score=0.5*(k+25.92*rec);	
			return score;
		}
		
		if(type==5){//reciprocal rank80
			return 1.0/(rank+80);
//			return (rank<100)?1.0/(rank+60):0;
		}
		if(type==6){//sum
			return (score-min)/sum;
//			return (rank<100)?1.0/(rank+60):0;
		}
		if(type==7){//Z-score
			/*double k=(score-miu)/biaozhuncha;
			return k>0?k:0.0;*/
			//return (zscore-zmin)/(zmax-zmin);
			return (score-miu)/biaozhuncha;
		}
		return 0;

	}
	/**
	 * �淶��һ��ResultList�Ĵ����裺</br>
	 * 1.���query����С����</br>
	 * 2.ÿ��query�ڵ�ResultList������rank�����ĵ����ֵĴ����1��ʼ��ţ��󲿷ֲ��䣬������������rankʼ��Ϊ0��</br>
	 * 3.score���ݹ�ʽ�淶������ʽ����rank��</br>
	 * @param input  ԭʼinput��File����
	 * @param output  �淶������ļ������·��
	 * @throws IOException
	 */
	public static void normAinput(File input,String output,int type) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(input));
		BufferedWriter writer=new BufferedWriter(new FileWriter(output));
		//����input�ļ���һ��
		bufferline[0]=new String(reader.readLine());
		//��ȡinput���ļ�����query�µ�ResultList
		ArrayList<ResultList> arraylists=new ArrayList<>();
		for(int i=0;i<numofquery;i++){
			ResultList list=getResultList(reader, 0,true);//��ȡһ��query��ResultList
			arraylists.add(list);
		}
		//���ResultList�Ȱ�query��С����
		Comparator<ResultList> comp=new Comparator<ResultList>() {
				
				@Override
				public int compare(ResultList o1, ResultList o2) {
					return o1.topic-o2.topic;
				}
		};			
		Collections.sort(arraylists, comp);	
		//ÿ��query�µ�ResultList�ڲ���list��ԭʼ����˳�����±��rank(�󲿷�rank�ͳ��ִ���һ�£�������һ��)���淶score,��
		//ÿһ����װ��ԭ����ʽ������淶����input·��
		for(int i=0;i<numofquery;i++){
			ArrayList<result> list=arraylists.get(i).list;
			double min=10000,max=-100000,sum=0,miu=0.0,biaozhuncha=0.0,sumfmiu=0.0,zscore=0.0;
			for(int j=0;j<list.size();j++){
				if(list.get(j).score>max){
					max=list.get(j).score;
				}
				if(list.get(j).score<min){
					min=list.get(j).score;
				}
			}
			
			//sum������sum
			for(int j=0;j<list.size();j++){
				sum=sum+(list.get(j).score-min);
			}
			for(int j=0;j<list.size();j++){
				sumfmiu=sumfmiu+list.get(j).score;
			}
			//System.out.println("sum"+sum+" "+i);
			miu=sumfmiu/list.size();
			//System.out.println("miu"+miu+"size"+list.size());
			for(int j=0;j<list.size();j++){
				biaozhuncha=biaozhuncha+(list.get(j).score-miu)*(list.get(j).score-miu);
			}
			biaozhuncha=Math.sqrt(biaozhuncha/list.size());
			
			//��Zscore ���·ֲ���0-1����
			double zmax=(max-miu)/biaozhuncha;
			double zmin=(min-miu)/biaozhuncha;
			
			
			System.out.println("biaozhuncha"+biaozhuncha+"miu"+miu);
			System.out.println("max"+max+"min"+min);
			System.out.println("zmax"+zmax+"zmin"+zmin);
			
			for(int j=1;j<=list.size();j++){
				zscore=(list.get(j-1).score-miu)/biaozhuncha;
				double normscore=normscore(j,list.get(j-1).score,min,max,type,sum,miu,biaozhuncha,zscore,zmax,zmin);
				if(normscore>0){//normscore>0
					writer.write(arraylists.get(i).topic+"\tQ0\t"+list.get(j-1).docid+
							"\t"+(j)+"\t"+normscore+
							"\t"+arraylists.get(i).system+"\n");
				}			
			}
		}
		reader.close();
		writer.close();
	}	
	/**
	 * �淶����ָ��ԭʼinput�ļ�����Ŀ¼���淶��Ŀ¼������ϵͳ��input��
	 * <p>��ֵ�Ĺ淶����ʽ����normscore</p>
	 * @param rawinput ԭʼ�ļ�Ŀ¼
	 * @param norminput �淶���ļ�Ŀ¼
	 * @throws IOException
	 */
	@Deprecated
	public static void normalization(String rawinput,String norminput) throws IOException{
		File fpath=new File(rawinput);
		File[] files=fpath.listFiles();
		for(int i=0;i<files.length;i++){
			normAinput(files[i], norminput+"/"+files[i].getName(),0);
		}	
	}
	/**
	 * �淶����ָ��ԭʼinput�ļ�����Ŀ¼���淶��Ŀ¼������ϵͳ��input��
	 * <p>��ֵ�Ĺ淶����ʽ����normscore</p>
	 * @param rawinput ԭʼ�ļ�Ŀ¼
	 * @param norminput �淶���ļ�Ŀ¼
	 * @param type 0:1/(r+60) 1:min-max
	 * @throws IOException
	 */
	public static void normalization(String rawinput,String norminput,int type) throws IOException{
		File fpath=new File(rawinput);
		File[] files=fpath.listFiles();
		for(int i=0;i<files.length;i++){
			normAinput(files[i], norminput+"/"+files[i].getName(),type);
		}	
	}
	public BinaryTree[] normAccordtoCover(String qrelinput) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(qrelinput));
		bufferqrel=reader.readLine();
		String docid,partscore;
		BinaryTree[] bts=new BinaryTree[numofquery];
		for(int qid=0;qid<numofquery;qid++){
			//get a query's qrellist
			QrelsList qrelslist=getQrelsList(reader,true);
			//if the query in qrelsfile is missing...
			while(qrelslist.getTopic()>this.startquery+qid){
				qid++;
			}
			if(qrelslist.getTopic()==this.startquery+qid){
				//create document pool/tree
				BinaryTree bt = new BinaryTree();
				ArrayList<QrelsNote> list=qrelslist.getList();
				for(int i=0;i<list.size();i++){		
					if(list.get(i).getRelevance()>0){
						docid=list.get(i).getDocid();
						partscore=list.get(i).getSubtopic()+","+list.get(i).getRelevance()+",";
						bt.search_add(docid, partscore);
					}
				}
				bts[qid]=bt;
			}	
		}
		return bts;
	}
	public int getSimofSubtopics(String[] a,String[] b){
		int num=10;
		int[] asubt=new int[num];
		int[] bsubt=new int[num];
		for(int i=0;i<a.length;i++){
			int p=Integer.parseInt(a[i]);
			asubt[p]=Integer.parseInt(a[++i]);
		}
		for(int i=0;i<b.length;i++){
			int p=Integer.parseInt(b[i]);
			bsubt[p]=Integer.parseInt(b[++i]);
		}
		int sim=0;
		for(int i=0;i<num;i++){
			if(asubt[i]==bsubt[i]&&asubt[i]>0)
				sim++;
		}
		if(sim==a.length/2){
			return -1;//have to discount
		}
		if(sim<a.length/2){
			return (a.length/2-sim)/(a.length/2+b.length/2-sim);
		}
		return 0;
	}
	public double getNormedScore(result cur,result after,int r,BinaryTree bt){
		double afs=1.0/(r+61);
		BinaryTreeNode curnode=bt.search(cur.docid);
		BinaryTreeNode afnode=bt.search(after.docid);
		String[] cursubts = null, afsubts = null;
		if(curnode!=null && afnode!=null){
			cursubts=bt.search(cur.docid).getPartscore().split(",");
			afsubts=bt.search(after.docid).getPartscore().split(",");
			if(getSimofSubtopics(cursubts, afsubts)<0){
				afs-=(afs-1.0/(r+62))*(1-1.0/(r+60));
			}else{
				
			}
		}else if(curnode!=null && afnode==null){//have to award
			//need to be changed with the method "getSimofSubtopics"	
		}else if(curnode==null && afnode!=null){//have to discount
			afs-=(afs-1.0/(r+62))*(1-1.0/(r+60));
		}
		//ֻ�ͷ���ǰ����ĵ��÷�
		
		
		return afs;
	}
	public void normalization(String rawinput,String norminput,String qrelinput) throws IOException{
		BinaryTree[] bts=normAccordtoCover(qrelinput);
		File fpath=new File(rawinput);
		File[] files=fpath.listFiles();
		for(int i=0;i<files.length;i++){
			BufferedReader reader=new BufferedReader(new FileReader(files[i]));
			BufferedWriter writer=new BufferedWriter(new FileWriter(norminput+"/"+files[i].getName()));
			bufferline[0]=new String(reader.readLine());
			ArrayList<ResultList> arraylists=new ArrayList<>();
			for(int qi=0;qi<numofquery;qi++){
				ResultList list=getResultList(reader, 0,true);
				arraylists.add(list);
			}
			Comparator<ResultList> comp=new Comparator<ResultList>() {
					@Override
					public int compare(ResultList o1, ResultList o2) {
						return o1.topic-o2.topic;
					}
			};			
			Collections.sort(arraylists, comp);	
			//compute norm score
			for(int qi=0;qi<numofquery;qi++){
				ArrayList<result> list=arraylists.get(qi).list;
				BinaryTree bt=bts[qi];
				writer.write(arraylists.get(qi).topic+"\tQ0\t"+list.get(0).docid+
						"\t0\t"+Double.toString(1.0/(60))+
						"\t"+arraylists.get(qi).system+"\n");
				for(int j=1;j<(list.size()<10000?list.size():10000);j++){
					double afs=1.0/(j+60);
					if(bt!=null){
						afs=getNormedScore(list.get(j-1), list.get(j), j-1, bt);
					}
					writer.write(arraylists.get(qi).topic+"\tQ0\t"+list.get(j).docid+
							"\t"+j+"\t"+Double.toString(afs)+
							"\t"+arraylists.get(qi).system+"\n");
				}
			}
			reader.close();
			writer.close();
		}
		
	}
	
	
	//-------------------------------------Ȩ��-------------------------------------------------
	/**
	 * �趨Ȩ�أ����ļ���ȡ���ļ���ʽ��һ��Ϊһ��Ȩ�أ���,�ָ���
	 * @param weightfile
	 * @throws IOException 
	 */
	public void setWeights(String weightfile) throws IOException{
		int group=0;
		String templine=null;

		BufferedReader reader=new BufferedReader(new FileReader(weightfile));		
		while((templine=reader.readLine())!=null){		
			group++;
		}
		reader.close();
		
		this.weights=new double[group][numofsystem];
		reader=new BufferedReader(new FileReader(weightfile));	
		int i=0;
		while((templine=reader.readLine())!=null){
			for(int j=0;j<numofsystem;j++){
				weights[i][j]=new Double(templine.split(",")[j]);
			}
			i++;
		}
		reader.close();
		
	}
	public void dissInWeights(){
		int n=this.weights.length;
		String out="";
		double[][] diss=new double[n][n];
		for(int i=0;i<n;i++){
			for(int j=i+1;j<n;j++){
				double dis=0,va=0,vb=0;
				for(int s=0;s<numofsystem;s++){
					dis+=weights[i][s]*this.weights[j][s];
					va+=Math.pow(weights[i][s], 2);
					vb+=Math.pow(weights[j][s], 2);
				}
				diss[i][j]=dis/(Math.sqrt(va)*Math.sqrt(vb));
			}
		}
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				out+=diss[i][j]+"\t";
			}
			System.out.println(out);
			out="";
		}
	}
	
	public void setWeight(double[] weight){
		this.weight=weight;
	}
	public void setWeights(double[][] weights) throws IOException{
		this.weights=weights;
	}
	/**
	 * ����̨���Ȩ��
	 */
	public void printWeights(){
		System.out.println("");
		for(int i=0;i<5;i++){
			String s="";
			for(int j=0;j<numofsystem;j++){
				s+=weights[i][j]+",";
			}
			System.out.println(s);
		}
	}
	
	
	//-------------------------------------�ں�-------------------------------------------------
	/**
	 * ����һ��query�µ��ĵ�������ת���ɰ���ֵ����,������ںϽ���ļ�
	 * @param topic
	 * @param bt �����ĵ���Ϣ�Ķ���������docid����
	 * @param combsysname �ںϺ�����ϵͳ����
	 * @param writer
	 * @throws IOException
	 */
	public void combineGroupSystem(int topic,BinaryTree bt,String combsysname,FileWriter writer) throws IOException{
		String temp ="";
		BinaryTreeNode btn = null;
		TreeByAttribute tba = new TreeByAttribute();
		btn=bt.getFirstNodeInLexicalOrder();
		while (btn!=null)
		{
			tba.search_add(btn.getName(),btn.getScore());
			btn=btn.getSuccesor();
		}
		//System.out.println(topic);
		btn=tba.getFirstNodeInOrder();
		int i=0;
		while (btn!=null&&i<standardlength)
		{
			temp=""; temp=new Integer(topic).toString();
			temp=temp.concat("\tQ0\t");
			temp=temp.concat(btn.getName());
			temp=temp.concat("\t"+new Integer(i+1).toString()+"\t");
			temp=temp.concat(new Double (btn.getScore()).toString());
			temp=temp.concat("\t"+combsysname+"\n");
			writer.write(temp);
			btn=btn.getSuccesor(); 
			i++;
		}
	}
	
	/**
	 * linear combination(5-fold ������֤��֧��rawinputĿ¼�в����ļ����ں�)
	 * <p>�������query�µ�����input</p>
	 * <p>For validation, the query is divided into 5 groups</p>
	 * <p>����һ�Ű�docid����Ķ��������һ��query�µ������ĵ�������Ϣ����ֵ�����淶������������ϵͳȨ�أ�
	 * ��Ȼ�������ת����һ����score�����������������������������ָ��·���ļ�</p>
	 * @param files  ����ϵͳ��Ӧ��input�ļ���������
	 * @param output
	 * @param combsysname
	 * @throws Exception 
	 */
	public void LCFusion(File[] files,String output,String combsysname) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		for(int qid=0;qid<numofquery;qid++){
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
					int group=qid/(numofquery/weights.length);
					System.out.print(group+" ");
					String docid=grouplists[sid].list.get(i).docid;
					double iscore=grouplists[sid].list.get(i).score;
					if(iscore==0){
						break;
					}else{
						double score=grouplists[sid].list.get(i).score*weights[group][sid];
						bt.search_add(docid, score);
					}		
				}
			}
			combineGroupSystem(qid+startquery, bt, combsysname,writer);
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
	}
	
	
	
	
	public void LCFusion(File[] files,String output,String combsysname,int[][]group) throws Exception{
		FileWriter writer=new FileWriter(output);
		
		for(int i=0;i<group.length;i++){
			for(int j=0;j<group[i].length;j++){
				ResultList[] grouplists=new ResultList[numofsystem];
				BinaryTree bt = new BinaryTree();//store ranklist
				for(int k=0;k<numofsystem;k++){						
					grouplists[k]=LC.getSingleResultList(files[k],group[i][j]);
					for (int l=0; l<grouplists[k].list.size(); l++){
						String docid=grouplists[k].list.get(l).docid;
						double iscore=grouplists[k].list.get(l).score;
						if(iscore==0){
							break;
						}else{
							double score=grouplists[k].list.get(l).score*weights[i][k];
							bt.search_add(docid, score);
						}		
					}
					
				}
				/*if(grouplists[0].topic==16){
					System.out.println("");
				}*/
				combineGroupSystem(grouplists[0].topic, bt, combsysname,writer);
				
			}
		}
		writer.close();
	}
	public void LCFusion_health2020(String[] files,String output,String combsysname,int[][]group) throws Exception{
		FileWriter writer=new FileWriter(output);
		String date1String1 = null;
		String date1String2 = null;
		
		double time = 0.0;
		
		for(int i=0;i<group.length;i++){
			
			for(int j=0;j<group[i].length;j++){
				ResultList[] grouplists=new ResultList[numofsystem];
				BinaryTree bt = new BinaryTree();//store ranklist
				for(int k=0;k<numofsystem;k++){	
					date1String1 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
					grouplists[k]=LC.getSingleResultList_health2020(files[k],group[i][j]);
					date1String2 = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
					time += Double.valueOf(date1String2)- Double.valueOf(date1String1);
					
					//System.out.println(time);
					
					for (int l=0; l<grouplists[k].list.size(); l++){
						String docid=grouplists[k].list.get(l).docid;
						double iscore=grouplists[k].list.get(l).score;
						if(iscore==0){
							break;
						}else{
							double score=grouplists[k].list.get(l).score*weights[i][k];
							bt.search_add(docid, score);
						}		
					}
					
				}
				/*if(grouplists[0].topic==16){
					System.out.println("");
				}*/
				combineGroupSystem(grouplists[0].topic, bt, combsysname,writer);
				
			}
			
		}
		
		
		writer.close();
	}

	/**
	 * linear combination(5-fold cross validation):�������query�µ�����input	
	 * <p>�����ڸ���inputĿ¼����Ŀ¼������ϵͳ��input�ļ������ں�</p>
	 * @param norminput �����淶�����inputĿ¼
	 * @param output
	 * @param combsysname
	 * @throws Exception 
	 */
	public void LCFusion(String norminput,String output,String combsysname) throws Exception{
		File fpath=new File(norminput);
		File[] files=fpath.listFiles();
		LCFusion(files, output, combsysname);
	}
	/**
	 * linear combination(�ǽ�����֤��ֻ��һ��Ȩ��)
	 * @param norminput
	 * @param output
	 * @param combsysname
	 * @throws Exception 
	 */
	public void LCFusion(String norminput,String output,String combsysname,double[] givenweight) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];
		File fpath=new File(norminput);
		File[] files=fpath.listFiles();
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		for(int qid=0;qid<numofquery;qid++){
			//:get the lists
			for(int i=0;i<numofsystem;i++){				
				grouplists[i]=getResultList(readers[i],i,true);
				if(i>0 && grouplists[i].topic!=grouplists[i-1].topic)
					throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");	
			}
			//combine systems in a query
			BinaryTree bt = new BinaryTree();//LN
			for (int sid=0; sid<numofsystem; sid++){
				for (int i=0; i<grouplists[sid].list.size(); i++){
					String docid=grouplists[sid].list.get(i).docid;
					double score=grouplists[sid].list.get(i).score*givenweight[sid];
					bt.search_add(docid, score);
					}
				}
			combineGroupSystem(qid+startquery, bt, combsysname,writer);
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	/**
	 * ΪDEѧϰ���ã�startquery,endquery֮��Ĳ�ѯ���ڲ��ԣ������ѵ��
	 * @param runs
	 * @param output
	 * @throws IOException 
	 */
	public void LCFusion_DeTrain(ArrayList<ResultList[]> runs, String output,int top) throws IOException {
		FileWriter writer=new FileWriter(output);
		for(int qid=0;qid<runs.size();qid++){		
			if(runs.get(qid)!=null){//ѵ��
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){
					int l=runs.get(qid)[sid].list.size();
					for (int i=0; i<(l>top?top:l); i++){//�����ں�
						String docid=runs.get(qid)[sid].list.get(i).docid;
						double score=runs.get(qid)[sid].list.get(i).score*weight[sid];
						bt.search_add(docid, score);
						}
					}
				
				combineGroupSystem(runs.get(qid)[0].topic, bt, "tempedrun",writer);
			}
		}
		writer.close();
	}
	public void LCFusion(File[] files, String output, String combsysname,
			double[] givenweight) throws Exception {
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		for(int qid=0;qid<numofquery;qid++){
			//:get the lists
			for(int i=0;i<numofsystem;i++){				
				grouplists[i]=getResultList(readers[i],i,true);
				if(i>0 && grouplists[i].topic!=grouplists[i-1].topic)
					throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");	
			}
			//combine systems in a query
			BinaryTree bt = new BinaryTree();//LN
			for (int sid=0; sid<numofsystem; sid++){
				for (int i=0; i<grouplists[sid].list.size(); i++){
					String docid=grouplists[sid].list.get(i).docid;
					double score=grouplists[sid].list.get(i).score*givenweight[sid];
					bt.search_add(docid, score);
					}
				}
			combineGroupSystem(qid+startquery, bt, combsysname,writer);
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
		
	}
	
}
