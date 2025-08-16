package com.algorithm.LCfusion.Main.StardardFusionMain;

import com.algorithm.LCfusion.org.hcl.fusion.fuse.LC;
import com.algorithm.LCfusion.org.hcl.fusion.fuse.LC;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class FusionMain {
	/*

	 鎵ц鏃跺彲鑳介渶瑕佹敼鍙樼殑鏂规硶鎴栧彉閲忥細
	 鍙橀噺锛�
	  	1.system_num	铻嶅悎鐨勭郴缁熶釜鏁�
	  	2.weight_num	LC铻嶅悎鏃跺嚑缁勪氦鍙�
	  	3.TREC_name		TREC鏁版嵁闆嗙殑鍚嶅瓧
	  	4.inputpath		瑕佽瀺鍚堢殑鏁版嵁闆嗗瓨鏀炬枃浠跺す
	  	5.qrelsPath		LC璁＄畻鏉冮噸鏃秖rel鏂囦欢鐨勮矾寰�
	  	6.select_system_result_path		瑕佽瀺鍚堢郴缁熸椂fusionrun.xls鏂囦欢璺緞
	  	7.startquery	鏁版嵁闆嗚瀺鍚堟椂topic鐨勮捣濮嬫暟瀛�
	  	8.experiments_num	閲嶅瀹為獙鏃剁殑閲嶅娆℃暟
	  	9.fusion_qrel_num	鏁版嵁闆嗚瀺鍚堟椂鍙備笌铻嶅悎鐨則opic鎬绘暟
	 	10.CombSUMoutput		CombSUM铻嶅悎缁撴潫鍚庤瀺鍚堢粨鏋滄枃浠剁殑杈撳嚭璺緞
	 	11.CombMNZoutput	CombMNZ铻嶅悎缁撴潫鍚庤瀺鍚堢粨鏋滄枃浠剁殑杈撳嚭璺緞
	 	12.all_topic	鍏ㄩ儴鐨則opic
	 	13.NotFusion_topic 琚拷鐣ョ殑topic
	 	14system_NUM_2018/system_NUM_2017/system_NUM_2020Health	鏁版嵁闆唕un鐨勪釜鏁�
	 	15.Standardlength	绾挎�х粍鍚堟椂缁撴灉淇濈暀鐨勬瘡涓猼opic鐨刣oc鐨勪釜鏁�

	 	16.Generatefile_output_path	generatefile鏂囦欢璺緞
	 	17.weights_output_path	weights鏂囦欢璺緞
	 	18.LCLRTest_output_path	LC铻嶅悎缁撴灉璺緞

	 鏂规硶锛�
	 	1.Random_select_system_NUM / all_select_system_NUM / health2020_kmeans_select_system ;

	 */
	/*
	public static int[] all_topic = {
			1,2,3,4,5,6,7,8,9,10,
			11,12,13,14,15,16,17,18,19,20,
			21,22,23,24,25,26,27,28,29,30,
			31,32,33,34,35,36,37,38,39,40,
			41,42,43,44,45,46,47,48,49,50};
	public static int[] NotFusion_topic = {33,35,46,48};
	*/
	/*public static int[] all_topic = {
			  171,172,173,174,175,176,177,178,179,180
             ,181,182,183,184,185,186,187,188,189,190
             ,191,192,193,194,195,196,197,198,199,200
             ,201,202,203,204,205,206,207,208,209,210
             ,211,212,213,214,215,216,217,218,219,220
             ,221,222,223,224,225};
	public static int[] NotFusion_topic = {};*/

	/*public static int[] all_topic = {
			226,227,228,236,242,243,246,248,249,253,
            254,255,260,262,265,267,278,284,287,298,
            305,324,326,331,339,344,348,353,354,357,
            359,362,366,371,377,379,383,384,389,391,
            392,401,400,405,409,416,419,432,434,439,448};
	public static int[] NotFusion_topic = {};*/

    public static int system_NUM_2018 = 86;//闅忔満鏃舵墠鐢�
    public static int system_NUM_2017 = 108;
    public static int system_NUM_2020Health = 51;

    public static int Standardlength = 1000;

    /*
     LC绗竴姝ユ椂璋冪敤涓変釜鍏ㄥ眬鍙橀噺
     */
    public static HashMap<String,String> Qrels=null;
    public static ArrayList<String> runIdList=null;
    public static HashMap<String,HashMap<String,String>> Runs=null;


    /*
     * 闅忔満閫夋嫨绨囦腑鐨勪竴涓郴缁熷苟杈撳嚭
     */
    public static String[] Random_select_system_NUM(int system_num,String inputpath,String select_system_result_path,int experiments_num) throws IOException {
        FileWriter writer = new FileWriter(select_system_result_path,true);
        String writerline = "";
        String[] fusionnames = new String[system_num];
        int[] fusionIDs = new int[system_num];
        Random Randomone = new Random();
        int flag = 0;

        for(int system_count=0;system_count<system_num;system_count++) {
            int systemID = Randomone.nextInt(system_NUM_2017)+1;//淇敼
            for(int i=0;i<system_num;i++) {
                if(systemID==fusionIDs[i]) {
                    flag = 1;
                    break;
                }
            }
            if(flag==1) {
                flag=0;
                system_count--;
                continue;
            }
            fusionIDs[system_count]=systemID;
        }
        File file = new File(inputpath);
        File[] files = file.listFiles();
        System.out.println(system_num);
        for(int system_count=0;system_count<system_num;system_count++) {

            fusionnames[system_count]=files[fusionIDs[system_count]-1].toString();
            writerline=writerline+fusionnames[system_count]+"\n";
        }

        for(int i=0;i<system_num;i++) {
            System.out.println(fusionnames[i]);
        }
        writerline =writerline+""+experiments_num+" one select \n";
        System.out.println("涓�娆￠�夋嫨缁撴潫");
        writer.write(writerline);
        writer.close();
        return fusionnames;
    }

    /*
     LC绗竴姝�,灏嗗叏閮ㄧ殑topic浜ゅ弶鍒嗕负N缁勶紝骞舵坊鍔犱竴鍒楀睘鎬�(鐩稿叧鎬�)
     */
    public static void Generatefile_Main(String qrelsPath,String[] fusion_runpath,String output,int weight_num) throws IOException {

        Qrels=processQrels(qrelsPath);
        System.out.println("READ Qrels FILE ...");

        generateRunIdList(fusion_runpath);
        System.out.println("GET runID..");

        Runs=processRuns(fusion_runpath);
        System.out.println("GET runs..");

        storeInFile(output,weight_num);
        System.out.println("weight file ...");
        runIdList.clear();
        Runs.clear();
        Qrels.clear();
        System.out.println(runIdList.size()+"\t"+Runs.size()+"\t"+Qrels.size());
    }
    /**
     * 鎶妐rels鏂囦欢瀛樺叆HashMap<String,String> Qrels涓��
     * @throws IOException
     *
     */
    public static HashMap<String,String> processQrels(String input) throws IOException{
        FileReader fileReader=null;
        BufferedReader bufferedReader=null;
        HashMap<String,String> hashMapQrels=new LinkedHashMap<String,String>();
        String tempLine=null;
        String[] terms=null;
        String hashMapKey=null;

        fileReader=new FileReader(input);
        bufferedReader=new BufferedReader(fileReader);
        while((tempLine=bufferedReader.readLine())!=null){
            terms=tempLine.split("  | \t| |\t");
            hashMapKey="queryId="+terms[0]+"\tdocId="+terms[2];
            hashMapQrels.put(hashMapKey, terms[3]);
        }
        bufferedReader.close();
        return hashMapQrels;
    }

    /**
     * 閬嶅巻鐩綍涓嬬殑鏂囦欢,鑾峰彇鏂囦欢涓殑runId,瀛樺叆runIdList涓�
     * @throws IOException
     * data:20210602
     */
    public static void generateRunIdList(String[] path) throws IOException{

        FileReader fileReader=null;
        BufferedReader bufferedReader=null;
        String tempLine=null;
        String[] terms=null;

        //file1=new File(path);
        //files=file1.listFiles();
        runIdList=new ArrayList<String>();
        //寰幆璇诲彇鐩綍涓嬬殑鎵�鏈夋枃浠�,鑾峰彇鏂囦欢涓殑runId,瀛樺叆runIdList涓�
        for(int i=0;i<path.length;i++){
            fileReader=new FileReader(path[i]);
            bufferedReader=new BufferedReader(fileReader);
            tempLine=bufferedReader.readLine();
            terms=tempLine.split("  | \t| |\t");
            runIdList.add(terms[5]);
            bufferedReader.close();
        }
    }

    /**
     * 閬嶅巻path鐩綍涓嬬殑鏂囦欢,鎶婃枃浠朵腑鐨勪俊鎭瓨鍏ashMapRuns涓��
     * @throws IOException
     *
     */
    public static HashMap<String,HashMap<String,String>> processRuns(String[] classification_run) throws IOException{
        HashMap<String,HashMap<String,String>> hashMapRuns=new LinkedHashMap<String,HashMap<String,String>>();
        HashMap<String,String> hashMap_runIdScore=null;

        FileReader fileReader=null;
        BufferedReader bufferedReader=null;
        String tempLine=null;
        String[] terms=null;
        String hashMapKey=null;//渚沨ashMapRuns浣跨敤

        //file1=new File(path);
        //files=file1.listFiles();

        //寰幆璇诲彇鐩綍涓嬬殑鎵�鏈夋枃浠�,骞舵妸淇℃伅瀛樺埌hashMapRuns涓�
        for(int i=0;i<classification_run.length;i++){
            fileReader=new FileReader(classification_run[i]);
            bufferedReader=new BufferedReader(fileReader);

            while((tempLine=bufferedReader.readLine())!=null){
                terms=tempLine.split("  | \t| |\t");
                for(String item :terms)
                {
                    //System.out.println(item);
                }
                //terms=tempLine.split("\t");
                hashMapKey="queryId="+terms[0]+"\tdocId="+terms[2];
                //hashMapRuns涓病鏈塰ashMapKey閿�煎鏃�,瀛樺叆hashMapKey hashMap_runIdScore閿�煎
                if(!hashMapRuns.containsKey(hashMapKey)){
                    //System.out.println(hashMapKey);
                    hashMap_runIdScore=new LinkedHashMap<String,String>();
                    hashMap_runIdScore.put(terms[5], terms[4]);
                    hashMapRuns.put(hashMapKey, hashMap_runIdScore);
                }
                //hashMapRuns涓凡鏈塰ashMapKey閿�煎鏃�,鍦╤ashMap_runIdScore涓姞鍏ユ柊鐨剅unId Score閿�煎
                if(hashMapRuns.containsKey(hashMapKey)){
                    hashMap_runIdScore=hashMapRuns.get(hashMapKey);
                    hashMap_runIdScore.put(terms[5], terms[4]);
                }
            }
            bufferedReader.close();
        }
        return hashMapRuns;
    }

    /**
     * 鎶奟uns鏁版嵁鍐欏叆output鏂囦欢
     * @throws IOException
     *
     */
    public static void storeInFile(String output,int weight_num) throws IOException{
        if(weight_num == 1) {

            FileWriter fileWriter_type1 = null;
            fileWriter_type1 = new FileWriter(output+"type1");

            String tempLine="";
            String[] terms=null;//涓存椂瀛樻斁hashMapKey淇℃伅

            Set<Entry<String,HashMap<String,String>>> set=null;
            Iterator<Entry<String,HashMap<String,String>>> it=null;
            Entry<String,HashMap<String,String>> runEntry=null;
            String hashMapKey=null;//涓存椂瀛樻斁runEntry淇℃伅
            HashMap<String,String> hashMap_runIdScore=null;//涓存椂瀛樻斁runEntry涓殑hashMap_runIdScore淇℃伅

            String queryId=null;//涓存椂瀛樺偍Runs key涓殑queryId淇℃伅
            String docId=null;//涓存椂瀛樺偍Runs key涓殑docId淇℃伅
            String strScores="";//涓存椂瀛樺偍hashMap_runIdScore鐨剆core淇℃伅
            String relevance=null;//涓存椂瀛樺偍Qrels涓殑relevance淇℃伅

            for(int i=0;i<runIdList.size();i++){
                tempLine=tempLine+runIdList.get(i)+"\t";
            }
            tempLine="queryId\tdocId\t"+tempLine+"relevance\n";
            //fileWriter.write(tempLine);
            //閬嶅巻Runs
            set=Runs.entrySet();
            it=set.iterator();
            while(it.hasNext()){
                runEntry=it.next();
                hashMapKey=runEntry.getKey();
                hashMap_runIdScore=runEntry.getValue();
                terms=hashMapKey.split("\t");
                queryId=terms[0].split("=")[1];
                docId=terms[1].split("=")[1];
                relevance=Qrels.get(hashMapKey);
                //褰搑elevance涓簄ull,relevance缃负"0"
                if(relevance==null) relevance="0";
                strScores="";//strScore缃负绌哄瓧绗︿覆
                for(int i=0;i<runIdList.size();i++){
                    strScores=strScores+hashMap_runIdScore.get(runIdList.get(i))+"\t";
                }
                tempLine=queryId+"\t"+docId+"\t"+strScores+relevance+"\n";

                fileWriter_type1.write(tempLine);
            }
            fileWriter_type1.close();
        }
        if(weight_num == 2) {
            FileWriter fileWriterji=null;
            fileWriterji=new FileWriter(output+"type1");

            FileWriter fileWriterou=null;
            fileWriterou=new FileWriter(output+"type2");

            String tempLine="";
            String[] terms=null;//涓存椂瀛樻斁hashMapKey淇℃伅

            Set<Entry<String,HashMap<String,String>>> set=null;
            Iterator<Entry<String,HashMap<String,String>>> it=null;
            Entry<String,HashMap<String,String>> runEntry=null;
            String hashMapKey=null;//涓存椂瀛樻斁runEntry淇℃伅
            HashMap<String,String> hashMap_runIdScore=null;//涓存椂瀛樻斁runEntry涓殑hashMap_runIdScore淇℃伅

            String queryId=null;//涓存椂瀛樺偍Runs key涓殑queryId淇℃伅
            String docId=null;//涓存椂瀛樺偍Runs key涓殑docId淇℃伅
            String strScores="";//涓存椂瀛樺偍hashMap_runIdScore鐨剆core淇℃伅
            String relevance=null;//涓存椂瀛樺偍Qrels涓殑relevance淇℃伅

            for(int i=0;i<runIdList.size();i++){
                tempLine=tempLine+runIdList.get(i)+"\t";
            }
            tempLine="queryId\tdocId\t"+tempLine+"relevance\n";
            //fileWriter.write(tempLine);
            //閬嶅巻Runs
            set=Runs.entrySet();
            it=set.iterator();
            while(it.hasNext()){
                runEntry=it.next();
                hashMapKey=runEntry.getKey();
                hashMap_runIdScore=runEntry.getValue();
                terms=hashMapKey.split("\t");
                queryId=terms[0].split("=")[1];
                docId=terms[1].split("=")[1];
                relevance=Qrels.get(hashMapKey);
                //褰搑elevance涓簄ull,relevance缃负"0"
                if(relevance==null) relevance="0";
                strScores="";//strScore缃负绌哄瓧绗︿覆
                for(int i=0;i<runIdList.size();i++){
                    strScores=strScores+hashMap_runIdScore.get(runIdList.get(i))+"\t";
                }
                tempLine=queryId+"\t"+docId+"\t"+strScores+relevance+"\n";


                if(judgeTopic(Integer.parseInt(queryId))) {
                    continue;
                }
                if(isJi(Integer.parseInt(queryId))) {//濂囩被1,3,5,7,9
                    //System.out.println("ji:"+all_topic[i]);

                    fileWriterji.write(tempLine);
                    //System.out.println(tempLine);
                }
                if(isOu(Integer.parseInt(queryId))) {
                    //System.out.println("ou:"+all_topic[i]);
                    fileWriterou.write(tempLine);
                    //System.out.println(tempLine);
                }

            }
            fileWriterji.close();
            fileWriterou.close();
        }
        if(weight_num==5) {



            FileWriter fileWriter_type1 = null;
            fileWriter_type1 = new FileWriter(output+"type1");

            FileWriter fileWriter_type2 = null;
            fileWriter_type2 = new FileWriter(output+"type2");

            FileWriter fileWriter_type3 = null;
            fileWriter_type3 = new FileWriter(output+"type3");

            FileWriter fileWriter_type4 = null;
            fileWriter_type4 = new FileWriter(output+"type4");

            FileWriter fileWriter_type5 = null;
            fileWriter_type5 = new FileWriter(output+"type5");

            String tempLine="";
            String[] terms=null;//涓存椂瀛樻斁hashMapKey淇℃伅

            Set<Entry<String,HashMap<String,String>>> set=null;
            Iterator<Entry<String,HashMap<String,String>>> it=null;
            Entry<String,HashMap<String,String>> runEntry=null;
            String hashMapKey=null;//涓存椂瀛樻斁runEntry淇℃伅
            HashMap<String,String> hashMap_runIdScore=null;//涓存椂瀛樻斁runEntry涓殑hashMap_runIdScore淇℃伅

            String queryId=null;//涓存椂瀛樺偍Runs key涓殑queryId淇℃伅
            String docId=null;//涓存椂瀛樺偍Runs key涓殑docId淇℃伅
            String strScores="";//涓存椂瀛樺偍hashMap_runIdScore鐨剆core淇℃伅
            String relevance=null;//涓存椂瀛樺偍Qrels涓殑relevance淇℃伅

            for(int i=0;i<runIdList.size();i++){
                tempLine=tempLine+runIdList.get(i)+"\t";
            }
            tempLine="queryId\tdocId\t"+tempLine+"relevance\n";
            //fileWriter.write(tempLine);
            //閬嶅巻Runs
            set=Runs.entrySet();
            it=set.iterator();
            while(it.hasNext()){
                runEntry=it.next();
                hashMapKey=runEntry.getKey();
                hashMap_runIdScore=runEntry.getValue();
                terms=hashMapKey.split("\t");
                queryId=terms[0].split("=")[1];
                docId=terms[1].split("=")[1];
                relevance=Qrels.get(hashMapKey);
                //褰搑elevance涓簄ull,relevance缃负"0"
                if(relevance==null) relevance="0";
                strScores="";//strScore缃负绌哄瓧绗︿覆
                for(int i=0;i<runIdList.size();i++){
                    strScores=strScores+hashMap_runIdScore.get(runIdList.get(i))+"\t";
                }
                tempLine=queryId+"\t"+docId+"\t"+strScores+relevance+"\n";

                //鍒ゆ柇queryID鐨勫�硷紝鐩殑鏄负寰楀埌浜屾姌浜ゅ弶鐨勫垎缁勭敱浜�
                int count = 1;
                for(int i = 0;i<all_topic.length ;i++) {
                    if(judgeTopic(all_topic[i])) {
                        continue;
                    }
                    if(count%5==1) {//濂囩被1,3,5,7,9
                        fileWriter_type1.write(tempLine);
                    }else if(count%5==2) {
                        fileWriter_type2.write(tempLine);
                    }else if(count%5==3) {
                        fileWriter_type3.write(tempLine);
                    }else if(count%5==4) {
                        fileWriter_type4.write(tempLine);
                    }else if(count%5==0) {
                        fileWriter_type5.write(tempLine);
                    }
                    count++;
                }

                if(( Integer.valueOf(queryId)%50)>=1&&( Integer.valueOf(queryId)%50)<=10) {
                    fileWriter_type1.write(tempLine);
                }else if(( Integer.valueOf(queryId)%50)>=11&&( Integer.valueOf(queryId)%50)<=20) {
                    fileWriter_type2.write(tempLine);
                }else if(( Integer.valueOf(queryId)%50)>=21&&( Integer.valueOf(queryId)%50)<=30) {
                    fileWriter_type3.write(tempLine);
                }else if(( Integer.valueOf(queryId)%50)>=31&&( Integer.valueOf(queryId)%50)<=40) {
                    fileWriter_type4.write(tempLine);
                }else if((( Integer.valueOf(queryId)%50)>=41&&( Integer.valueOf(queryId)%50)<=50)||( Integer.valueOf(queryId)%50)==0) {
                    fileWriter_type5.write(tempLine);
                }

            }
            fileWriter_type1.close();
            fileWriter_type2.close();
            fileWriter_type3.close();
            fileWriter_type4.close();
            fileWriter_type5.close();
        }
    }

    /*
     褰撳墠topic鏄惁鍦∟otfusion_topic闆嗗悎涓�
     */
    public static boolean judgeTopic(int topic) {
        for(int i = 0;i<NotFusion_topic.length;i++) {
            if(topic == NotFusion_topic[i]) {
                return true;
            }
        }
        return false;
    }


    public static void LCLRweights_Main(int num_classification,String output,String Generatefile_output,int system_num,int experiments_num,int weight_num) throws Exception {
        LCLRweights_C_SF lw=new LCLRweights_C_SF();
        if(weight_num == 1) {

            String type1 = "type1";
            String pathtype1 = Generatefile_output +"\\jiou_k"+system_num+"_"+experiments_num+type1;
            BufferedReader br=new BufferedReader(new FileReader(pathtype1));
            System.out.println("cccccccccccccccc");
            lw.generateAGroupWeight_type(br, "binary", 1, num_classification,type1);
            System.out.println("ddddddddddddddddddd");
            lw.write_weight_file(output, num_classification);

            br.close();

        }
        if(weight_num==2) {//绗竴琛屽啓ou鏉冮噸锛岀浜岃鍐欏鏉冮噸
            String jiou="type1";
            String  pathou = Generatefile_output + "\\jiou_k"+system_num+"_"+experiments_num+jiou;
            BufferedReader br=new BufferedReader(new FileReader(pathou));
            //System.out.println(pathou);
            lw.generateAGroupWeight_ou(br, "binary", 1, num_classification,jiou);

            jiou="type2";
            String pathji = Generatefile_output + "\\jiou_k"+system_num+"_"+experiments_num+jiou;
            //System.out.println(pathji);
            br=new BufferedReader(new FileReader(pathji));
            lw.generateAGroupWeight_ji(br, "binary", 2, num_classification,jiou);

            lw.write_weight_file(output, num_classification);

            br.close();
        }
        if(weight_num==5) {
            String type1 = "type1";
            String pathtype1 = Generatefile_output +"\\jiou_k"+system_num+"_"+experiments_num+type1;
            BufferedReader br=new BufferedReader(new FileReader(pathtype1));
            lw.generateAGroupWeight_type(br, "binary", 1, num_classification,type1);

            String type2 = "type2";
            String pathtype2 = Generatefile_output +"\\jiou_k"+system_num+"_"+experiments_num+type2;
            br=new BufferedReader(new FileReader(pathtype2));
            lw.generateAGroupWeight_type(br, "binary", 2, num_classification,type2);

            String type3 = "type3";
            String pathtype3 = Generatefile_output +"\\jiou_k"+system_num+"_"+experiments_num+type3;
            br=new BufferedReader(new FileReader(pathtype3));
            lw.generateAGroupWeight_type(br, "binary", 3, num_classification,type3);

            String type4 = "type4";
            String pathtype4 = Generatefile_output +"\\jiou_k"+system_num+"_"+experiments_num+type4;
            br=new BufferedReader(new FileReader(pathtype4));
            lw.generateAGroupWeight_type(br, "binary", 4, num_classification,type4);

            String type5 = "type5";
            String pathtype5 = Generatefile_output +"\\jiou_k"+system_num+"_"+experiments_num+type5;
            br=new BufferedReader(new FileReader(pathtype5));
            lw.generateAGroupWeight_type(br, "binary", 5, num_classification,type5);


            lw.write_weight_file(output, num_classification);

            br.close();
        }



        //娓呯┖鏉冮噸鏁扮粍



        System.out.println("end");
    }

    public static void LCLRTest_Main(int num_classification,String[] classification_run,String resultpath,String output,String weightspath,String TREC_name,int weight_num,int fusion_qrel_num) throws Exception {

        if(weight_num==1) {
            ArrayList<Integer> groupList1 = new ArrayList<Integer>();
            for(int i = 0; i<all_topic.length;i++) {
                if(judgeTopic(all_topic[i])) {
                    continue;
                }
                groupList1.add(all_topic[i]);
            }
            int[] one =  groupList1.stream().mapToInt(Integer::valueOf).toArray();

            int [][] group = {one};
            int startquery = group[0][0];
            Hashtable<String, Integer> systems=LC.setSystem(resultpath);
            LC lc=new LC(num_classification, fusion_qrel_num, startquery, systems);
            lc.setStandardlength(1000);
            lc.setWeights(weightspath);
            lc.LCFusion_health2020(classification_run, output, TREC_name+"_LC",group);

        }

        if(weight_num == 2) {
//            ArrayList<Integer> groupList1 = new ArrayList<Integer>();
//            ArrayList<Integer> groupList2 = new ArrayList<Integer>();

//            for(int i = 0; i<all_topic.length;i++) {
//                if(judgeTopic(all_topic[i])) {
//                    continue;
//                }
//                if(isJi(all_topic[i])) {//濂囩被1,3,5,7,9
//                    groupList1.add(all_topic[i]);
//                }
//                if(isOu(all_topic[i])) {
//                    groupList2.add(all_topic[i]);
//                }
//            }

//            int[] one =  groupList1.stream().mapToInt(Integer::valueOf).toArray();
//            int[] two =  groupList2.stream().mapToInt(Integer::valueOf).toArray();

            int [][] group = {all_topic_ji,all_topic_ou};
            int startquery = group[0][0];

            Hashtable<String, Integer> systems=LC.setSystem(resultpath);
            LC lc=new LC(num_classification, fusion_qrel_num, startquery, systems);
            lc.setStandardlength(Standardlength);
            lc.setWeights(weightspath);
            lc.LCFusion_health2020(classification_run, output, TREC_name+"_LC",group);
        }


        if(weight_num == 5) {
            ArrayList<Integer> groupList1 = new ArrayList<Integer>();
            ArrayList<Integer> groupList2 = new ArrayList<Integer>();
            ArrayList<Integer> groupList3 = new ArrayList<Integer>();
            ArrayList<Integer> groupList4 = new ArrayList<Integer>();
            ArrayList<Integer> groupList5 = new ArrayList<Integer>();
            int count = 1;
            for(int i = 0; i<all_topic.length;i++) {
                if(judgeTopic(all_topic[i])) {
                    continue;
                }
                if(count%5==1) {//濂囩被1,3,5,7,9
                    groupList1.add(all_topic[i]);
                }else if(count%5==2) {
                    groupList2.add(all_topic[i]);
                }else if(count%5==3) {
                    groupList3.add(all_topic[i]);
                }else if(count%5==4) {
                    groupList4.add(all_topic[i]);
                }else if(count%5==0) {
                    groupList5.add(all_topic[i]);
                }
                count++;
            }
            int[] one =  groupList1.stream().mapToInt(Integer::valueOf).toArray();
            int[] two =  groupList2.stream().mapToInt(Integer::valueOf).toArray();
            int[] three =  groupList3.stream().mapToInt(Integer::valueOf).toArray();
            int[] four =  groupList4.stream().mapToInt(Integer::valueOf).toArray();
            int[] five =  groupList5.stream().mapToInt(Integer::valueOf).toArray();

            int [][] group = {one,two,three,four,five};
            int startquery = group[0][0];

            Hashtable<String, Integer> systems=LC.setSystem(resultpath);
            LC lc=new LC(num_classification, fusion_qrel_num, startquery, systems);
            lc.setStandardlength(1000);
            lc.setWeights(weightspath);
            lc.LCFusion_health2020(classification_run, output, TREC_name+"_LC",group);
        }

        //鏉冮噸鏂囦欢绗竴琛屼负鍋讹紝绗簩琛屼负濂�,鎵�浠ラ渶瑕佷氦鍙夌潃鏉�
        System.out.println("end----------------------------------------end");

    }
    private static boolean isJi(int topic){
        for (int ji : all_topic_ji) {
            if (topic == ji){
                return true;
            }
        }
        return false;
    }
    private static boolean isOu(int topic){
        for (int ou : all_topic_ou) {
            if (topic == ou){
                return true;
            }
        }
        return false;
    }
//    private static int[] all_topic = {
//            111,112,113,114,115,116,117,118,119,120,
//            121,122,123,124,125,126,127,128,129,130,
//            131,132,133,134,135,136,137,138,139,140,
//            141,142,143,144,145,146,147,148,149,150,
//            151,152,153,154,155,156,157,158,159,160,
//            161,162,163,164,165,166,167,168,169,170};
//    private static int[] all_topic_ji = {
//            112,114,116,118,120,
//            122,124,126,128,130,
//            132,134,136,138,140,
//            142,144,146,148,150,
//            152,154,156,158,160,
//            162,164,166,168,170
//    };
//    private static int[] all_topic_ou = {
//            112,114,116,118,120,
//            122,124,126,128,130,
//            132,134,136,138,140,
//            142,144,146,148,150,
//            152,154,156,158,160,
//            162,164,166,168,170
//    };
    private static int[] all_topic = {
    };
    private static int[] all_topic_ji = {

    };
    private static int[] all_topic_ou = {
    };
    private static int[] NotFusion_topic = {};

    public static void setAll_topic(int[] topicInts){
        all_topic = topicInts;
    }
    public static void setAll_topic_ji(int[] topicInts){
        all_topic_ji = topicInts;
    }
    public static void setAll_topic_ou(int[] topicInts){
        all_topic_ou = topicInts;
    }
    private static String SystemName = "";
    public static void setSystemName(String SystemName){
        FusionMain.SystemName = SystemName;
    }
    public static String getSystemName(){
        return FusionMain.SystemName;
    }
    public static void RunProgram(int system_num,int weight_num,String[] fusionNames,String inputPath,String qrelsPath,int experiments_num,
                                  String Generatefile_output,String weights_output,String LCLRTest_output_path) throws Exception {
        int fusion_qrel_num = all_topic.length-NotFusion_topic.length;
        String TREC_name = FusionMain.SystemName;
        //String inputpath ="D:\\TREC鏁版嵁闆嗘枃浠禱\methode_qrel012\\BigExperiment\\2013MB\\Adhoc\\standard_input_nor1-60\\";
        //String qrelsPath="D:\\TREC鏁版嵁闆嗘枃浠禱\2015Microblog\\Judgment_file\\Judgment_file012";
        //String qrelsPath="D:\\TREC鏁版嵁闆嗘枃浠禱\methode_qrel012\\2013Microblog\\Judgmentfile\\Judgment_file012";
        //String qrelsPath="D:\\TREC鏁版嵁闆嗘枃浠禱\2014Microblog\\relevance_judgmentFile\\Judgment_file012";
        int startquery = all_topic[0];
        for(int i=0;i<system_num;i++) {
            System.out.println(fusionNames[i]);
        }
        //lc
        //String Generatefile_output = "D:\\TREC鏁版嵁闆嗘枃浠禱\methode_qrel012\\2013Microblog\\adhoc\\fusion17\\LCfusion_ou\\Generatefile"+"\\";
        String Generatefile_output_path = Generatefile_output + "\\jiou_k"+system_num+"_"+experiments_num;
        if (Generatefile_output != ""){
            Generatefile_Main(qrelsPath, fusionNames, Generatefile_output_path,weight_num);
        }

        //String weights_output = "D:\\TREC鏁版嵁闆嗘枃浠禱\methode_qrel012\\2013Microblog\\adhoc\\fusion22\\LCfusion_ji\\Weightsfile";
        String weights_output_path =  weights_output + "\\weights_"+system_num+"_"+experiments_num;
        File weightFile = new File(weights_output_path);
        if (!weightFile.exists()){
            LCLRweights_Main(system_num, weights_output_path,Generatefile_output,system_num,experiments_num,weight_num);
        }

        //String LCLRTest_output = "D:\\TREC鏁版嵁闆嗘枃浠禱\methode_qrel012\\2013Microblog\\adhoc\\fusion22\\fusion_ou";
        //String LCLRTest_output_path = LCLRTest_output+"\\LCfusion_k"+system_num+"_"+experiments_num;
        LCLRTest_Main(system_num, fusionNames, inputPath, LCLRTest_output_path, weights_output_path,TREC_name,weight_num,fusion_qrel_num);
        //System.out.println("end-----"+experiments_num);

        //System.out.println(MAP_mean);
        System.out.println("------------------------all end-------------------------");

    }
    public static void main(String[] args) throws Exception {
        int system_num=70;
        int fusion_qrel_num = all_topic.length-NotFusion_topic.length;
        int weight_num = 2;
        String TREC_name = "2013MB";

        for(;system_num<=70;system_num++) {
            //String date = "2015072"+9;
            String inputpath ="D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2013MB\\Adhoc\\standard_input_nor1-60\\";
            //String qrelsPath="D:\\TREC鏁版嵁闆嗘枃浠禱\diversification_1_TREC\\adhoc_dataset\\TREC2009\\qrel\\diversity-qrel";
            //String qrelsPath="D:\\TREC鏁版嵁闆嗘枃浠禱\2015Microblog\\Judgment_file\\Judgment_file012";
            String qrelsPath="D:\\TRECDateset\\methode_qrel012\\2013Microblog\\Judgmentfile\\Judgment_file012";
            //String qrelsPath="D:\\TREC鏁版嵁闆嗘枃浠禱\2014Microblog\\relevance_judgmentFile\\Judgment_file012";
            String[] fusionnames = new String[system_num];

            //闅忔満
            //String select_system_result_path = "D:\\TREC鏁版嵁闆嗘枃浠禱\SIGIR-table-fig\\zhangzhen\\new_PMSA2017\\norm160_\\random_50\\2017random_system_result_k"+system_num;
            //String[] fusionnames = new String[system_num];
            int startquery = all_topic[0];



            for(int experiments_num=1;experiments_num<=1;experiments_num++) {
                //闅忔満鍦ㄨ繖閲�
                //fusionnames = Random_select_system_NUM(system_num, inputpath,select_system_result_path,experiments_num);//闅忔満

                //fusionnames = all_select_system_NUM(system_num, inputpath, select_system_result_path, experiments_num);
                //String  system_xls = "D:\\TREC鏁版嵁闆嗘枃浠禱\new_health2020\\kmeans_classification_xls\\"+system_num+"\\kmeans_k"+system_num+"_"+experiments_num+".xls";
                //fusionnames = health2020_kmeans_select_system(system_num, inputpath, system_xls);

                //浠巟ls鏂囦欢涓幏鍙栬瀺鍚堢殑run

                //String system_xls = "E:\\2018_4suanfa\\index2_Y_50.xls";
                //String system_xls = "E:\\2017_4suanfa\\maprank.xls";
                //String  system_xls = "E:\\2018_4suanfa\\index1_1\\paixu\\"+system_num+".xls";
                //String  system_xls = "D:\\2020cvx\\TREC2020\\绠楁硶2\\combsum.xls";
                //fusionnames = health2020_kmeans_select_system(system_num, inputpath, system_xls);

//				int classification_num = 17;
//				String system_xls = "D:\\2020cvx\\index\\"+classification_num+".xls";
//				health2020_newidea_select_system(system_num, classification_num, inputpath, system_xls);
//				fusionnames = health2020_newidea_select_system(system_num, classification_num, inputpath, system_xls);

                //fusionnames = get_filesname(inputpath, fusionnames);
                for(int i=0;i<system_num;i++) {
                    System.out.println(fusionnames[i]);
                }


                //lc
                String Generatefile_output =	"D:\\TRECDateset\\methode_qrel012\\2013Microblog\\adhoc\\fusion17\\LCfusion_ou\\Generatefile"+"\\";
                String Generatefile_output_path = Generatefile_output + "jiou_k"+system_num+"_"+experiments_num;
                Generatefile_Main(qrelsPath, fusionnames, Generatefile_output_path,weight_num);
                String weights_output_path =  "D:\\TRECDateset\\methode_qrel012\\2013Microblog\\adhoc\\fusion22\\LCfusion_ji\\Weightsfile"+"\\weights_"+system_num+"_"+experiments_num;
                LCLRweights_Main(system_num, weights_output_path,Generatefile_output,system_num,experiments_num,weight_num);
                String LCLRTest_output_path = "D:\\TRECDateset\\methode_qrel012\\2013Microblog\\adhoc\\fusion22\\fusion_ou"+"\\LCfusion_k"+system_num+"_"+experiments_num;
                LCLRTest_Main(system_num, fusionnames, inputpath, LCLRTest_output_path, weights_output_path,TREC_name,weight_num,fusion_qrel_num);
                System.out.println("end-----"+experiments_num);
            }
        }
        //System.out.println(MAP_mean);
        System.out.println("------------------------all end-------------------------");

    }





}
