package treeStructure;

public abstract class BinaryTree {
	protected BinaryTreeNode root;
	
	public abstract void insertNode();
	
	public abstract BinaryTreeNode searchNode(String value);
	
	public abstract void deleteNode(String value);
	
	public void setRoot(BinaryTreeNode root) {
		this.root = root;
	}
	
	public BinaryTreeNode getRoot() {
		return this.root;
	}
}
