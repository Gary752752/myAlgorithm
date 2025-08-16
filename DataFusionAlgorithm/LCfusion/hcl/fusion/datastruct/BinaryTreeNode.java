package com.algorithm.LCfusion.hcl.fusion.datastruct;

public class BinaryTreeNode{
	private String name;//docid
	private int count;//num of systems the doc ranked
	private double score;//score
	private String partscore;//diversity task:subtopic
//	int position;
	private BinaryTreeNode leftLink;
	private BinaryTreeNode rightLink;
	private BinaryTreeNode succesor;

	
	public BinaryTreeNode(String name) {
		super();
		this.name = name;
		this.count=1;
		this.score=0;
		this.partscore="";
		this.leftLink=null;
		this.rightLink=null;
		this.succesor=null;
	}
	
	public BinaryTreeNode(String name, double score) {
		super();
		this.name = name;
		this.count=1;
		this.score = score;
		this.partscore="";
		this.leftLink=null;
		this.rightLink=null;
		this.succesor=null;
	}
	
	public BinaryTreeNode(String name, String partscore) {
		super();
		this.name = name;
		this.count=1;
		this.score=0;
		this.partscore = partscore;
		this.leftLink=null;
		this.rightLink=null;
		this.succesor=null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getPartscore() {
		return partscore;
	}

	public void setPartscore(String partscore) {
		this.partscore = partscore;
	}

	public BinaryTreeNode getLeftLink() {return leftLink;}
	public void setLeftLink(BinaryTreeNode b) {leftLink = b;}
	public BinaryTreeNode getRightLink() {return rightLink;}
	public void setRightLink(BinaryTreeNode b) {rightLink = b;}
	public BinaryTreeNode getSuccesor() {return succesor;}
	public void setSuccesor(BinaryTreeNode s) {succesor = s;}
	
	public void addScore(double s) {
		score=score+s;
	}
	public void addCount() { 
		count++;
	}
	public void addPartScore(String ps){
		partscore+=ps;
	}

}