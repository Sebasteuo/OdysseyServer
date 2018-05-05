package treeStructure;

public class AVLTree extends BinarySearchTree{
	
	public AVLTree() {
		this.root = null;
	}
	
	@Override
	public void insertNode(String value) {
		this.root = insert(value, root);
	}
	
	private BinaryTreeNode insert(String value, BinaryTreeNode root) {
		if(root == null) {
			root = new BinaryTreeNode(value);
		}else{
			int comparisson = value.compareToIgnoreCase(root.getValue()); //Negativo value es menor alfab. Positivo value es mayor alfab.
			if(comparisson < 0) {
				root.setLeft(insert(value, root.getLeft()));
				if(consultHeight(root.getLeft()) - consultHeight(root.getRight()) == 2) {					
					if(value.compareToIgnoreCase(root.getRight().getValue()) < 0) {
						root = rotateLeftLeft(root);
					}else {
						root = rotateRightLeft(root);
					}
				}
			}else if(comparisson > 0) {
				root.setRight(insert(value, root.getRight()));
				if(consultHeight(root.getRight()) - consultHeight(root.getLeft()) == 2) {
					if(value.compareToIgnoreCase(root.getRight().getValue()) > 0) {
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
