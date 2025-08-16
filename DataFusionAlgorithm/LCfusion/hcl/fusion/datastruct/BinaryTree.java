package com.algorithm.LCfusion.hcl.fusion.datastruct;

public class BinaryTree{
	private int nodeCount;
	private BinaryTreeNode firstNode;

	public BinaryTree(){
		super(); 
		nodeCount = 0; 
		firstNode = null;
	}

	BinaryTree (BinaryTreeNode btn){
		super();
		nodeCount = 1;
		firstNode = btn;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}

	public BinaryTreeNode getFirstNode() {
		return firstNode;
	}

	public void setFirstNode(BinaryTreeNode firstNode) {
		this.firstNode = firstNode;
	}

	public void addNodeCount () {
		nodeCount++;
	}
	/**
	 * ��ȡ����ߵĽڵ㣬�ýڵ�ӵ������name
	 * @return
	 */
	public BinaryTreeNode getFirstNodeInLexicalOrder(){//get the most left node
		if (nodeCount==0) return null;
		BinaryTreeNode btn = getFirstNode();
		while (btn.getLeftLink()!=null)
			btn = btn.getLeftLink();
		return btn;
	}

//	public void traverseByThreadWithScore(){
//		BinaryTreeNode btn = getFirstNodeInLexicalOrder();
//		if (btn==null) 
//			return; 
//		else
//			System.out.println(btn.getName());
//
//		while (btn!=null)
//		{
//			System.out.println(btn.getName()+ " " + btn.getScore());
//			int temp = btn.getPosition();
//			double temp1 = (double)temp/10000.0;
//			System.out.println(new Double(temp1).toString());
//			btn = btn.getSuccesor();
//		}
//	}
	public void search_addScore(String name,double score){
		BinaryTreeNode working = firstNode;
		int temp;
		while (working!=null){
			temp = working.getName().compareTo(name);
			if (temp==0){ 
				working.setScore(score);
				break;
			}
			else if (temp<0) working = working.getRightLink();
			else working = working.getLeftLink();
		}
	}
	/**
	 * ����name���ҽڵ�
	 * @param s
	 * @return
	 */
	public BinaryTreeNode search(String s){//search according to the docid
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
	
	/**
	 * ����name���Ҳ�����ĵ���Ϣ��name��<p>
	 * ������ĵ��Ѵ��ڣ����ҵ��ýڵ㣬�޸������Ϣ����������ڣ������½ڵ㣬����name��С��������λ�ã�
	 * @param s
	 */
	public void search_add (String s){
		BinaryTreeNode working = firstNode;
		BinaryTreeNode pre = null; //this node points to possible predecasor of the insered node, if it happened finally
		int temp;
		if (working == null){
			BinaryTreeNode btn = new BinaryTreeNode(s);
			setFirstNode(btn);
			setNodeCount(1);
			return;
		}
		while (true){
			temp = working.getName().compareTo(s);
			if (temp==0) {
				working.addCount(); 
				return;
			}
			else if((working.getRightLink()!=null)&&(temp<0)){
				pre = working; 
				working = working.getRightLink();
			}else if((working.getLeftLink()!=null)&&(temp>0)){ 
				working = working.getLeftLink();
			}else if((working.getRightLink()==null)&&(temp<0)){
				BinaryTreeNode btn = new BinaryTreeNode(s);
				working.setRightLink(btn);
				btn.setSuccesor(working.getSuccesor());
				working.setSuccesor(btn);
				nodeCount++;
				return;
			}else if((working.getLeftLink()==null)&&(temp>0)){
				BinaryTreeNode btn = new BinaryTreeNode(s);
				working.setLeftLink(btn);
				if (pre!=null) pre.setSuccesor(btn);
				btn.setSuccesor(working);
				nodeCount++;
				return;
			}
		}
	}

	public void search_add (String name, double score){
		BinaryTreeNode working = firstNode;
		BinaryTreeNode pre = null; //this node points to possible predecasor of the insered node, if it happened finally
		int temp;
		if (working == null){
			BinaryTreeNode btn = new BinaryTreeNode(name, score);
			setFirstNode(btn);
			setNodeCount(1);
			return;
		}

		while (true){
			temp = working.getName().compareTo(name);
			if (temp==0) {
				working.addCount(); 
				working.addScore(score);
				return;
			}else if((working.getRightLink()!=null)&&(temp<0)){
				pre = working; 
				working = working.getRightLink();
			}else if((working.getLeftLink()!=null)&&(temp>0)){
				working = working.getLeftLink();
			}else if((working.getRightLink()==null)&&(temp<0)){
				BinaryTreeNode btn = new BinaryTreeNode(name, score);
				working.setRightLink(btn);
				btn.setSuccesor(working.getSuccesor());
				working.setSuccesor(btn);
				nodeCount++;
				return;
			}else if((working.getLeftLink()==null)&&(temp>0)){
				BinaryTreeNode btn = new BinaryTreeNode(name, score);
				working.setLeftLink(btn);
				if (pre!=null) pre.setSuccesor(btn);
				btn.setSuccesor(working);
				nodeCount++;
				return;
			}
		}
	}
	
	public void search_add(String name, String partscore){
		BinaryTreeNode working = firstNode;
		BinaryTreeNode pre = null; //this node points to possible predecasor of the insered node, if it happened finally
		int temp;
		if (working == null){
			BinaryTreeNode btn = new BinaryTreeNode(name, partscore);
			setFirstNode(btn);
			setNodeCount(1);
			return;
		}

		while (true){
			temp = working.getName().compareTo(name);
			if (temp==0) {
				working.addCount(); 
				working.addPartScore(partscore);
				return;
			}else if((working.getRightLink()!=null)&&(temp<0)){
				pre = working;
				working = working.getRightLink();
			}else if((working.getLeftLink()!=null)&&(temp>0)){
				working = working.getLeftLink();
			}else if((working.getRightLink()==null)&&(temp<0)){
				BinaryTreeNode btn = new BinaryTreeNode(name, partscore);
				working.setRightLink(btn);
				btn.setSuccesor(working.getSuccesor());
				working.setSuccesor(btn);
				nodeCount++;
				return;
			}else if((working.getLeftLink()==null)&&(temp>0)){			
				BinaryTreeNode btn = new BinaryTreeNode(name, partscore);
				working.setLeftLink(btn);
				if (pre!=null) pre.setSuccesor(btn);
				btn.setSuccesor(working);
				nodeCount++;
				return;
			}
		}
	}
	/**
	 * �����콣�ĵ�������ԣ������ڵĲ���ӣ�
	 * @param name
	 * @param partscore
	 */
	public void search_add_qrels (String name, String partscore){
		BinaryTreeNode working = firstNode;
		int temp;
		if (working == null){
			return;
		}
		while (true){
			temp = working.getName().compareTo(name);
			if (temp==0) {
				working.addPartScore(partscore);
				return;
			}
			else if ((working.getRightLink()!=null)&&(temp<0)){
				 working = working.getRightLink();
			}
			else if ((working.getLeftLink()!=null)&&(temp>0)){
				working = working.getLeftLink();
			}
			else
				return;
		}

	}
	
}