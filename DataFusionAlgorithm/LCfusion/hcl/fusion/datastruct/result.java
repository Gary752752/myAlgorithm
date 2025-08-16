package com.algorithm.LCfusion.hcl.fusion.datastruct;

public class result{
	public String docid;
	public int rank;
	public double score;
	public result(){}
	public result(String docid, int rank, double score) {
		super();
		this.docid = docid;
		this.rank = rank;
		this.score = score;
	}
	
}