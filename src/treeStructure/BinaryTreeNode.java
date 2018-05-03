package treeStructure;

public class BinaryTreeNode {
	private String value;
	private BinaryTreeNode left;
	private BinaryTreeNode right;
	private int height;
	
	public BinaryTreeNode() {
		this.value = "";
		this.left = null;
		this.right = null;
		this.height = 0;
	}
	
	public BinaryTreeNode(String value) {
		this.value = value;
		this.left = null;
		this.right = null;
		this.height = 0;
	}
	
	public void setValue(String value) {		
		this.value = value;
	}
	
	public String getValue() {
		return this.value;		
	}
	
	public void setLeft(BinaryTreeNode left) {
		this.left = left;
	}
	
	public BinaryTreeNode getLeft() {
		return this.left;
	}
	
	public void setRight(BinaryTreeNode right) {
		this.right = right;
	}
	
	public BinaryTreeNode getRight() {
		return this.right;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return this.height;
	}
}
