package treeStructure;

public class BinaryTreeNode {
	private String key;
	private String value;
	private BinaryTreeNode left;
	private BinaryTreeNode right;
	private int height;
	
	public BinaryTreeNode() {
		this.setKey("");
		this.value = "";
		this.left = null;
		this.right = null;
		this.height = 0;
	}
	
	public BinaryTreeNode(String value, String key) {
		this.setKey(key);
		this.value = value;
		this.left = null;
		this.right = null;
		this.height = 0;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
