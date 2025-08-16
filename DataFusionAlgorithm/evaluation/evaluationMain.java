package com.algorithm.evaluation;

import java.io.File;
import java.sql.SQLOutput;

public class evaluationMain {
    /*
    public static void main(String[] args) throws Exception {
        args= new String[]{"-RR"};
        //String qrel = "qrels\\2004WEBqrels.txt";
        //String fusionResult = "2004LCOutputFile";
        //String fusionMap= "2004_fusion_WEB_MAP";
        //String qrel = "2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful";//useful qrel file
        //String qrel = "2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-correct";//useful and correct  qrel file
        //String qrel = "2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-credible";//useful and credible  qrel file
        String qrel = "D:\\TREC鏁版嵁闆嗘枃浠禱\zhangzhen\\new_PMSA2018A\\2018qrel\\qrels.txt";

        //String qrel = "2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-correct-credible";//useful and correct and credible  qrel file
        //String fusionResult = "D:\\eclipse\\eclipse-workspace\\fusion2\\health2020\\kmeans_LC_fusion";
        for(int classification_num = 2;classification_num<=24;classification_num++) {
            String fusionResult = "D:\\TREC鏁版嵁闆嗘枃浠禱\zhangzhen\\new_PMSA2018A\\2018C_SF\\2018C_SFfinalououtput_fusion\\2018C_SFfinalou_LCfusion\\C_SFfinal_LCfusion_LCLRTest\\fusion_"+classification_num;
            String fusionTrecEvaloutput= "D:\\TREC鏁版嵁闆嗘枃浠禱\zhangzhen\\new_PMSA2018A\\2018C_SF\\2018C_SFfinalououtput_fusion\\2018C_SFfinalou_LCfusion\\C_SFfinal_LCfusion_RR";
            TrecEval eval = new TrecEval();
            File file = new File(fusionResult);
            File[] fusionRuns = file.listFiles();
            for(File run:fusionRuns)
            {
                eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\RR_2018C_SFLCou_"+run.getName().toString().replaceAll("input.", ""), args);
            }
            System.out.println("END");
        }


    }*/
	/*
	public static void main(String[] args) throws Exception {
		args= new String[]{"-RR"};
		//-MAP
		//-P
		//-Rprec
		//-RR

		String qrel = "D:\\TREC鏁版嵁闆嗘枃浠禱\zhangzhen\\new_PMSA2017\\2017qrel\\qrels.txt";
		for(int system_num=2;system_num<=20;system_num++) {
			String fusionResult = "D:\\TREC鏁版嵁闆嗘枃浠禱\zhangzhen\\new_PMSA2017\\2017random\\CombSUMfusion\\CombSUM_k"+system_num;
			String fusionTrecEvaloutput= "D:\\TREC鏁版嵁闆嗘枃浠禱\zhangzhen\\new_PMSA2017\\2017random\\CombSUMfusion\\CombSUM_RR_k"+system_num;
			TrecEval eval = new TrecEval();
			File file = new File(fusionResult);
			File[] fusionRuns = file.listFiles();
			for(File run:fusionRuns)
			{
				eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\RR_2017_Random_"+run.getName().toString().replaceAll("input.", ""), args);
			}
			System.out.println("END"+system_num);
		}
	}*/
    public static void output_MAP(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-MAP"};
        //-P
        //-Rprec
        //-RR
        //-NDCG


        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        //System.out.println("MAP");
        double mapValue = 0.0;
        for(File run:fusionRuns)
        {
            TrecEval eval = new TrecEval();
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\MAP_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
            double oneMapValue = eval.getMapValue();
            mapValue += oneMapValue;
        }
        mapValue = mapValue/fusionRuns.length;
        System.out.println(file.getName()+",meanMAP\tmeanMAP\t"+mapValue+"\t---------------------");

    }
    public static void output_ALL(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-ALL"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        //System.out.println("ALL");
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\ALL_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }

    }
    public static void output_AP(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-AP"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        System.out.println("AP");
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\AP_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }

    }
    public static void output_ERR(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-ERR"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\ERR_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }

    }
    /*
     * p10 p10
     */
    public static void output_PX(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {

        String[] args= new String[]{"-P"};
        //-P
        //-Rprec
        //-RR
        //-NDCG
        double P10Value = 0.0;
        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\Px_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
            double oneP10Value = eval.getP10Value();
            P10Value +=oneP10Value;
        }
        P10Value = P10Value/fusionRuns.length;
        System.out.println(file.getName()+",meanP10\tmeanP10\t"+P10Value+"\t---------------------");
    }

    public static void output_RP(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-Rprec"};
        //-P
        //-Rprec
        //-RR
        //-NDCG
        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        System.out.println("RP");
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\RP_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }


    }
    public static void output_RR(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {

        String[] args= new String[]{"-RR"};
        //-P
        //-Rprec
        //-RR
        //-NDCG
        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\RR_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }
    }
    public static void output_U_NDCG(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-NDCG"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        //System.out.println("NDCG");
        double NDCG1000Value = 0.0;
        for(File run:fusionRuns)
        {
            TrecEval eval = new TrecEval();
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\NDCG_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
            double oneNDCG1000Value = eval.getNDCG1000Value();
            NDCG1000Value += oneNDCG1000Value;
        }
        NDCG1000Value = NDCG1000Value/fusionRuns.length;

        System.out.println(file.getName()+",meanNDCG1000\tmeanNDCG1000\t"+NDCG1000Value+"\t---------------------");
    }
    public static void output_UC_NDCG(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-NDCG"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        //System.out.println("NDCG");
        double NDCG1000Value = 0.0;
        for(File run:fusionRuns)
        {
            TrecEval eval = new TrecEval();
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\NDCG_UC_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
            double oneNDCG1000Value = eval.getNDCG1000Value();
            NDCG1000Value += oneNDCG1000Value;
        }
        NDCG1000Value = NDCG1000Value/fusionRuns.length;

        System.out.println(file.getName()+",meanNDCG1000\tmeanNDCG1000\t"+NDCG1000Value+"\t---------------------");
    }
    public static void output_U_C_NDCG(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-NDCG"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        //System.out.println("NDCG");
        double NDCG1000Value = 0.0;
        for(File run:fusionRuns)
        {
            TrecEval eval = new TrecEval();
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\NDCG_U_C_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
            double oneNDCG1000Value = eval.getNDCG1000Value();
            NDCG1000Value += oneNDCG1000Value;
        }
        NDCG1000Value = NDCG1000Value/fusionRuns.length;

        System.out.println(file.getName()+",meanNDCG1000\tmeanNDCG1000\t"+NDCG1000Value+"\t---------------------");
    }
    public static void output_UCC_NDCG(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-NDCG"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        //System.out.println("NDCG");
        double NDCG1000Value = 0.0;
        for(File run:fusionRuns)
        {
            TrecEval eval = new TrecEval();
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\NDCG_UCC_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
            double oneNDCG1000Value = eval.getNDCG1000Value();
            NDCG1000Value += oneNDCG1000Value;
        }
        NDCG1000Value = NDCG1000Value/fusionRuns.length;

        System.out.println(file.getName()+",meanNDCG1000\tmeanNDCG1000\t"+NDCG1000Value+"\t---------------------");
    }
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    public static void main(String[] args) throws Exception {

        /*
         * 2019decision
         */
        //String U_qrel = "D:\\TREC鏁版嵁闆嗘枃浠禱\2019decision\\qrel\\file-containing-raw-U__-judgments";//useful qrel file is U	"CAM" use U UC U_C
        //String UC_qrel = "D:\\TREC鏁版嵁闆嗘枃浠禱\2019decision\\qrel\\file-containing-raw-_C_-judgments";//useful and correct  qrel file is UC 	"CAM" use U UC U_C
        //String U_C_qrel = "D:\\TREC鏁版嵁闆嗘枃浠禱\2019decision\\qrel\\file-containing-raw-__C-judgments";//useful and credible  qrel file is U_C	"CAM" use U UC U_C

		/*
		 2014MB
		 */
        //String U_qrel = "D:\\TREC鏁版嵁闆嗘枃浠禱\methode_qrel012\\BigExperiment\\2014MB\\Judgmentfile\\relevance judgments";
		/*
		 2015MB
		 */
        //String U_qrel = "D:\\TREC鏁版嵁闆嗘枃浠禱\2015Microblog\\Judgment_file\\Judgment_file012";
		/*
		 2013MB
		 */
        //String U_qrel = "E:\\TRECDataset\\methode_qrel012\\WISA\\2013Microblog\\Judgmentfile\\Judgment_file012";
		/*
		 2012MB
		 */
        //String U_qrel = "D:\\TREC鏁版嵁闆嗘枃浠禱\methode_qrel012\\BigExperiment\\2012MB\\Judgmentfile\\Judgment_file012";
		/*
		 2011MB
		 */
        //String U_qrel = "D:\\TREC鏁版嵁闆嗘枃浠禱\methode_qrel012\\BigExperiment\\2011MB\\Judgmentfile\\Judgment_file012";




        /*
         * 2020DeepLearning_Passage
         * */
        //String U_qrel = "E:\\TRECDataset\\Semi-SupervisedMethodExperiment\\2020DeepLearning_Passage\\judgmentfile\\judgmentfile_01";
        //String eval_filename = "2020DeepLearning_Passage";
        /*
         * 2021DeepLearning_Document
         * */
        //String U_qrel = "E:\\TRECDataset\\Semi-SupervisedMethodExperiment\\2021DeepLearning_Document\\judgmentfile\\judgmentfile_01";
        //String eval_filename = "2021DeepLearning_Document";
        //String U_qrel = "D:\\TREC鏁版嵁闆嗘枃浠禱\diversification_1_TREC\\TREC2009\\qrel\\qrels-Multi_rel";
        /*
         * 2014session
         * */
        // String U_qrel = "E:\\TRECDataset\\methode_qrelNegative\\2014Session\\Judgmentfile\\SessionJudgmentFile_201234";
        // String eval_filename = "2014Session";
        /*
         * 2011Session
         * */
        //String U_qrel = "E:\\TRECDataset\\methode_qrelNegative\\2011Session\\Judgmentfile\\judgmentfile.txt";
        //String eval_filename = "2011Session";
        /*
         * 2012Session
         * */
        //String U_qrel = "E:\\TRECDataset\\methode_qrelNegative\\2012Session\\Judgmentfile\\evalJudgmentMappingFile201234";
        //String eval_filename = "2012Session";

        /*
         * 2020health
         */
        String U_qrel = "D:\\myIdeaProject\\TREC\\2020Health\\judgment\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful";//useful qrel file is U	"CAM" use U UC U_C
        String UC_qrel = "D:\\myIdeaProject\\TREC\\2020Health\\judgment\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-correct";//useful and correct  qrel file is UC 	"CAM" use U UC U_C
        String U_C_qrel = "D:\\myIdeaProject\\TREC\\2020Health\\judgment\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-credible";//useful and credible  qrel file is U_C	"CAM" use U UC U_C
        String UCC_qrel = "D:\\myIdeaProject\\TREC\\2020Health\\judgment\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-correct-credible";
        String eval_filename = "2020health";
        /*
         * 2021health
         */
//        String U_qrel = "D:\\myIdeaProject\\TREC\\2021health\\judgmentFile\\misinfo-resources-2021\\qrels\\2021-derived-qrels\\misinfo-qrels-binary.useful";//useful qrel file is U	"CAM" use U UC U_C
//        String UC_qrel = "D:\\myIdeaProject\\TREC\\2021health\\judgmentFile\\misinfo-resources-2021\\qrels\\2021-derived-qrels\\misinfo-qrels-binary.useful-correct";//useful and correct  qrel file is UC 	"CAM" use U UC U_C
//        String U_C_qrel = "D:\\myIdeaProject\\TREC\\2021health\\judgmentFile\\misinfo-resources-2021\\qrels\\2021-derived-qrels\\misinfo-qrels-binary.useful-credible";//useful and credible  qrel file is U_C	"CAM" use U UC U_C
//        String UCC_qrel = "D:\\myIdeaProject\\TREC\\2021health\\judgmentFile\\misinfo-resources-2021\\qrels\\2021-derived-qrels\\misinfo-qrels-binary.useful-correct-credible";//useful and credible  qrel file is U_C	"CAM" use U UC U_C
//        String eval_filename = "2021health";
        /*
         * 2020health
         */
        //String U_qrel = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2020Health\\judgment\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful";//useful qrel file is U	"CAM" use U UC U_C
        //String UC_qrel = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2020Health\\judgment\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-correct";//useful and correct  qrel file is UC 	"CAM" use U UC U_C
        //String U_C_qrel = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2020Health\\judgment\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-credible";//useful and credible  qrel file is U_C	"CAM" use U UC U_C
        //String eval_filename = "2020health";
        /*
         * 2019PrecisionLiterature
         * */
        //String U_qrel = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2019PrecisionLiterature\\judgmentFile\\judgmentfile_01";
        //String eval_filename = "2019PrecisionLiterature";
        /*
         * 2018PrecisionLiterature
         * */
//        String U_qrel = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2018PrecisionLiterature\\judgmentFile\\judgmentfile_01";
//        String eval_filename = "2018PrecisionLiterature";

        int StartNum = 2;
        int EndNum = 2;
            /*
            2021health
            * */
        String fusionPath= "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\20250108\\TopUCCndcg\\TopUCCndcg_Birch_fusion3_3_useful\\";//输出路径
        System.out.println(fusionPath);
        for(int select_num = StartNum ;select_num <= EndNum;select_num++) {

            //String fusionResult = fusionPath+"\\fusion\\"+select_num+"\\";
            String fusionResult = "D:\\myIdeaProject\\TREC\\2020Health\\Adhoc\\standard_input_nor30-60\\";//评价路径

            String MAP_outputpath= fusionPath+"\\eval\\MAP\\";
            createDirectory(MAP_outputpath);
            output_MAP(U_qrel, fusionResult, MAP_outputpath,eval_filename);

            String Px_outputpath= fusionPath+"\\eval\\P10\\";
            createDirectory(Px_outputpath);
            output_PX(U_qrel, fusionResult, Px_outputpath,eval_filename);

            String UCC_NDCG_outputpath= fusionPath+"\\eval\\UCCndcg\\";
            createDirectory(UCC_NDCG_outputpath);
            output_UCC_NDCG(UCC_qrel, fusionResult, UCC_NDCG_outputpath,eval_filename);

            String U_NDCG_outputpath= fusionPath+"\\eval\\U__\\";
            createDirectory(U_NDCG_outputpath);
            output_U_NDCG(U_qrel, fusionResult, U_NDCG_outputpath,eval_filename);

            String UC_NDCG_outputpath= fusionPath+"\\eval\\UC_\\";
            createDirectory(UC_NDCG_outputpath);
            output_UC_NDCG(UC_qrel, fusionResult, UC_NDCG_outputpath,eval_filename);

            String U_C_NDCG_outputpath= fusionPath+"\\eval\\U_C\\";
            createDirectory(U_C_NDCG_outputpath);
            output_U_C_NDCG(U_C_qrel, fusionResult, U_C_NDCG_outputpath,eval_filename);



            //System.out.print(select_num+"\t");
            //for(int dateEnd = 0 ; dateEnd <=9;dateEnd++) {
            //String date = "2015072"+dateEnd;
            //String fusionName = "fusion"+22;```
            //String jiou = "ou";
            //String fusionResult = "D:\\TREC鏁版嵁闆嗘枃浠禱\2015Microblog\\Microblog_TaskB\\split_standard_input\\"+date+"\\";
            //System.err.println(select_num);
            //String fusionResult = "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\adhoc\\fusion3_7\\fusion\\"+select_num+"\\";

            //String fusionResult = "E:\\TRECDateset\\classificationEXP\\2021Health\\adhoc\\standard_input_nor30-60\\";

            //String ALL_outputpath= "E:\\TRECDateset\\paperExperiment\\ideaTwo\\2018PrecisionLiterature\\adhoc\\fusion11_1\\eval\\all\\";
            //createDirectory(ALL_outputpath);
            //output_ALL(U_qrel, fusionResult, ALL_outputpath,eval_filename);

//            String MAP_outputpath= "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\adhoc\\fusion3_7\\eval\\MAP\\";
//            createDirectory(MAP_outputpath);
//            output_MAP(U_qrel, fusionResult, MAP_outputpath,eval_filename);

//            String U_NDCG_outputpath= "E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\fusion3_7\\eval\\U__\\";
//            createDirectory(U_NDCG_outputpath);
//            output_U_NDCG(U_qrel, fusionResult, U_NDCG_outputpath,eval_filename);
//
//            String UC_NDCG_outputpath= "E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\fusion3_7\\eval\\UC_\\";
//            createDirectory(UC_NDCG_outputpath);
//            output_UC_NDCG(UC_qrel, fusionResult, UC_NDCG_outputpath,eval_filename);
//
//            String U_C_NDCG_outputpath= "E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\fusion3_7\\eval\\U_C\\";
//            createDirectory(U_C_NDCG_outputpath);
//            output_U_C_NDCG(U_C_qrel, fusionResult, U_C_NDCG_outputpath,eval_filename);

            //String AP_outputpath= "D:\\TREC鏁版嵁闆嗘枃浠禱\2013Microblog\\adhoc\\"+fusionName+"\\eval_"+ iou+"\\AP";
            //output_AP(U_qrel, fusionResult, AP_outputpath,eval_filename);

            //String ERR_outputpath = "D:\\TREC鏁版嵁闆嗘枃浠禱\diversification_1_TREC\\TREC2009\\eval\\raw_inputeval\\ERR";
            //output_ERR(U_qrel, fusionResult, ERR_outputpath, eval_filename);

            //String Px_outputpath= "D:\\TREC鏁版嵁闆嗘枃浠禱\2013Microblog\\adhoc\\"+fusionName+"\\eval_"+jiou+"\\P10";
            //output_PX(U_qrel, fusionResult, Px_outputpath,eval_filename);

            //String RP_outputpath= "D:\\TREC鏁版嵁闆嗘枃浠禱\2013Microblog\\adhoc\\"+fusionName+"\\eval_"+jiou+"\\RP";
            //output_RP(U_qrel, fusionResult, RP_outputpath,eval_filename);

            //String RR_outputpath= "D:\\TREC鏁版嵁闆嗘枃浠禱\new_idea\\2020Health\\classification_fusion\\"+select_num+"\\eval\\RR";
            //output_RR(U_qrel, fusionResult, RR_outputpath,eval_filename);



            //System.out.println("END");
            //}

        }

    }
}
