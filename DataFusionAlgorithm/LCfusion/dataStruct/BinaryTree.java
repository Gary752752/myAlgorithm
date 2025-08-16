package com.algorithm.LCfusion.dataStruct;

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
     * 获取最左边的节点，该节点拥有最大的name
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
     * 根据name查找节点
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
     * 根据name查找并添加文档信息（name）<p>
     * （如果文档已存在，则找到该节点，修改相关信息，如果不存在，插入新节点，根据name大小决定插入位置）
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
    /*
     * 将文档id为查找文档的基准 若文档id相同则分数累加
     * 若分数不同则创建新的二叉树节点
     */
    public void search_add (String name, double score){//name为文档id score为该文档的相关性分数
        BinaryTreeNode working = firstNode;//第一次执行firstNode为NULL
        BinaryTreeNode pre = null; //this node points to possible predecasor of the insered node, if it happened finally
        int temp;
        if (working == null){//父节点设置
            BinaryTreeNode btn = new BinaryTreeNode(name, score);//创建一个二叉树节点，传入其文档id和分数
            setFirstNode(btn);//设置其FirstNode值父节点的firstnode为自己
            setNodeCount(1);//设置Nodecount值
            return;
        }

        while (true){
            /*
             * compareto（）函数 若str1小于str2则返回负值
             * 					若str1大于str1则返回正值
             * 					若相等则返回零
             */
            temp = working.getName().compareTo(name);//获取节点，比较字符串和对象，若相等则返回0
            if (temp==0) {
                working.addCount(); //节点数加1
                working.addScore(score);//节点分数累加
                return;
            }else if((working.getRightLink()!=null)&&(temp<0)){//若当前节点的右节点不为空且temp小于零
                pre = working; //将当前节点赋值给他的前驱节点
                working = working.getRightLink();//将当前节点变为右节点
            }else if((working.getLeftLink()!=null)&&(temp>0)){
                working = working.getLeftLink();
            }else if((working.getRightLink()==null)&&(temp<0)){//若当前节点为空且当前节点文档id小于他的父节点名
                BinaryTreeNode btn = new BinaryTreeNode(name, score);//创建新的节点
                working.setRightLink(btn);//设置当前节点为父节点的右节点
                btn.setSuccesor(working.getSuccesor());//将当前节点的父节点的succesor值复制给当前节点
                working.setSuccesor(btn);//父节点的succesor变为当前节点
                nodeCount++;//二叉树节点数量加一
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
     * 查找天剑文档的相关性（不存在的不添加）
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
