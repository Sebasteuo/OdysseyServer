package treeStructure;

public class AVLTree extends BinarySearchTree{
	
	public AVLTree() {
		this.root = null;
	}
	
	@Override
	public void insertNode(String value, String key) {
		this.root = insert(value, root, key);
	}
	
	private BinaryTreeNode insert(String value, BinaryTreeNode root, String key) {
		if(root == null) {
			root = new BinaryTreeNode(value, key);
		}else{
			int comparisson = value.compareToIgnoreCase(root.getKey()); //Negativo value es menor alfab. Positivo value es mayor alfab.
			if(comparisson < 0) {
				root.setLeft(insert(value, root.getLeft(), key));
				if(consultHeight(root.getLeft()) - consultHeight(root.getRight()) == 2) {					
					if(value.compareToIgnoreCase(root.getRight().getKey()) < 0) {
						root = rotateLeftLeft(root);
					}else {
						root = rotateRightLeft(root);
					}
				}
			}else if(comparisson > 0) {
				root.setRight(insert(value, root.getRight(), key));
				if(consultHeight(root.getRight()) - consultHeight(root.getLeft()) == 2) {
					if(value.compareToIgnoreCase(root.getRight().getKey()) > 0) {
						root = rotateRightRight(root);
					}else {
						root = rotateLeftRight(root);
					}
				}
			}						
		}
		root.setHeight(getMax(consultHeight(root.getLeft()), consultHeight(root.getRight())) + 1);
		return root;
	}
}
