package treeStructure;

public abstract class BinaryTree {
	protected BinaryTreeNode root;
	
	public abstract void insertNode(String value);
	
	public abstract BinaryTreeNode searchNode(String value);
	
	public abstract void deleteNode(String value);
	
	public void setRoot(BinaryTreeNode root) {
		this.root = root;
	}
	
	public BinaryTreeNode getRoot() {
		return this.root;
	}
	
	public boolean isEmpty() {
		return root == null;
	}
	
	public void makeTreeEmpty() {
		this.root = null;
	}
	
	public int consultHeight(BinaryTreeNode node) {
		return node == null? -1 : node.getHeight();
	}
	
	public int getMax(int value1, int value2) {
		return value1 > value2? value1 : value2;
	}
	
	public String findMin(BinaryTreeNode node) {
		return node.getLeft() == null? node.getValue() : findMin(node.getLeft());
	}
	
		
    public void inorder(){
        inorder(root);
    }

    private void inorder(BinaryTreeNode r){
        if (r != null)
        {
            inorder(r.getLeft());
            System.out.print(r.getValue() +" ");
            inorder(r.getRight());
        }
    }
	
    public BinaryTreeNode rotateLeftLeft(BinaryTreeNode k2) {
		BinaryTreeNode k1 = k2.getLeft();
		k2.setLeft(k1.getRight());
		k1.setRight(k2);
		k2.setHeight(getMax(consultHeight(k2.getLeft()), consultHeight(k2.getRight())) + 1);
		k1.setHeight(getMax(consultHeight(k1.getLeft()), consultHeight(k1.getRight())) + 1);
		return k1;
	}
	
    public BinaryTreeNode rotateRightRight(BinaryTreeNode k1) {
		BinaryTreeNode k2 = k1.getRight();
		k1.setRight(k2.getLeft());
		k2.setLeft(k1);
		k1.setHeight(getMax(consultHeight(k1.getLeft()), consultHeight(k1.getRight())) + 1);
		k2.setHeight(getMax(consultHeight(k2.getLeft()), consultHeight(k2.getRight())) + 1);
		return k2;
	}
	
    public BinaryTreeNode rotateRightLeft(BinaryTreeNode k3) {
		k3.setLeft(rotateRightRight(k3.getLeft()));
		return rotateLeftLeft(k3);
	}
	
    public BinaryTreeNode rotateLeftRight(BinaryTreeNode k3) {
		k3.setRight(rotateLeftLeft(k3.getRight()));
		return rotateRightRight(k3);
	}
}
