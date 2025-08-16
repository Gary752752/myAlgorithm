package com.algorithm.LCfusion.hcl.fusion.datastruct;

import java.util.ArrayList;

public class QrelsList {
	public static class QrelsNote{
		int subtopic;
		String docid;
		int relevance;
		public QrelsNote(){}
		public QrelsNote(int subtopic, String docid, int relevance) {
			super();
			this.subtopic = subtopic;
			this.docid = docid;
			this.relevance = relevance;
		}
		public int getSubtopic() {
			return subtopic;
		}
		public void setSubtopic(int subtopic) {
			this.subtopic = subtopic;
		}
		public String getDocid() {
			return docid;
		}
		public void setDocid(String docid) {
			this.docid = docid;
		}
		public int getRelevance() {
			return relevance;
		}
		public void setRelevance(int relevance) {
			this.relevance = relevance;
		}
		
	}
	int topic;
	ArrayList<QrelsNote> list;
	public int getTopic() {
		return topic;
	}
	public void setTopic(int topic) {
		this.topic = topic;
	}
	public ArrayList<QrelsNote> getList() {
		return list;
	}
	public void setList(ArrayList<QrelsNote> list) {
		this.list = list;
	}
	public QrelsList() {
		super();
		this.list=new ArrayList<>();
	}
	public QrelsList(int topic) {
		super();
		this.topic = topic;
		this.list=new ArrayList<>();
	}
	
}
