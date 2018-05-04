package treeStructure;

public class AVLTree extends BinaryTree{
	
	public AVLTree() {
		this.root = null;
	}
	
	@Override
	public void insertNode(String value) {
		insert(value, root);
	}
	
	@Override
	public BinaryTreeNode searchNode(String value) {
		return search(root, value);
	}
	
	@Override
	public void deleteNode(String value) {
		delete(root, value);
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
	
	private BinaryTreeNode search(BinaryTreeNode r, String value) {
		while(r != null) {
			if(r.getValue() == value) {
				break;
			}else {
				int comparisson = value.compareToIgnoreCase(r.getValue());
				if(comparisson < 0) {
					r = r.getLeft();
				}
				else if(comparisson > 0) {
					r = r.getRight();
				}
			}
		}
		return r;
	}
	
	private BinaryTreeNode delete(BinaryTreeNode r, String value) {
		if(r == null) {
			return null;
		}		
		if(r.getValue() == value) {
			if(r.getLeft() == null && r.getRight() == null) { //Nodo es hoja
				return null;
			}
			else if(r.getLeft() == null && r.getRight() != null) {//Nodo solo tiene hijo derecho
				return r.getRight();
			}
			else if(r.getRight() == null && r.getLeft() != null) {//Nodo solo tiene hijo izquierdo
				return r.getLeft();
			}
			else if(r.getLeft() != null && r.getRight() != null) {//Nodo tiene dos hijos
				String min = findMin(r.getRight());
				r.setValue(min);
				r.setRight(delete(r.getRight(), min));
				return r;
			}
		}
		if(value.compareToIgnoreCase(r.getValue()) < 0) {
			r.setLeft(delete(r.getLeft(), value));
			return r;
		}
		r.setRight(delete(r.getRight(), value));
		return r;
	}
}
