package com.algorithm.LCfusion.hcl.fusion.datastruct;

public class TreeByAttribute{
	private int nodeCount;
	private BinaryTreeNode firstNode;

	public TreeByAttribute (){
		super();
		nodeCount = 0; 
		firstNode = null;
	}

	public TreeByAttribute (BinaryTreeNode btn){
		super();
		nodeCount = 1;
		firstNode = btn;
	}

	public int getNodeCount() {return nodeCount;}

	public BinaryTreeNode getFirstNode () {return firstNode;}

	public void setNodeCount (int i) {nodeCount = i;}

	public void setFirstNode (BinaryTreeNode btn) {firstNode = btn;}

	public void addNodeCount () {nodeCount++;}

	/**
	 * ��ȡ����ڵ㣨ӵ������score��
	 * @return
	 */
	public BinaryTreeNode getFirstNodeInOrder(){
		BinaryTreeNode btn = getFirstNode();
		
		while (btn.getLeftLink()!=null) {
			btn = btn.getLeftLink();
			
		}
			
		
		return btn;
	}

	public BinaryTreeNode search(String s){
		BinaryTreeNode working = firstNode;
		int temp;
		while (working!=null){
			temp = working.getName().compareTo(s);
			if (temp==0) return working;
			else if (temp<0) working = working.getRightLink();
			else working = working.getLeftLink();
		}
		return null;
	}

	public void search_add(String s, double score){
		BinaryTreeNode working = firstNode;
		BinaryTreeNode pre = null; //this node points to possible predecasor of the insered node, if it happened finally
		int temp;
		double get;
		if (working == null){
			BinaryTreeNode btn = new BinaryTreeNode(s, score);
			setFirstNode(btn);
			setNodeCount(1);
			return;
		}

		while (true){
			get=working.getScore();
			if (get>=score) 
				temp=-1; 
			else 
				temp=1;

			if ((working.getRightLink()!=null)&&(temp<0)){
				pre = working; 
				working = working.getRightLink();
			}else if ((working.getLeftLink()!=null)&&(temp>0)) 
				working = working.getLeftLink();
			else if ((working.getRightLink()==null)&&(temp<0)){
				BinaryTreeNode btn = new BinaryTreeNode(s, score);
				working.setRightLink(btn);
				btn.setSuccesor(working.getSuccesor());
				working.setSuccesor(btn);
				nodeCount++;
				return;
			}else if ((working.getLeftLink()==null)&&(temp>0)){
				BinaryTreeNode btn = new BinaryTreeNode(s, score);
				working.setLeftLink(btn);
				if (pre!=null) pre.setSuccesor(btn);
				btn.setSuccesor(working);
				nodeCount++;
				return;
			}
		}
	}

}