package treeStructure;

public class SplayTree extends BinaryTree{
	
	public SplayTree() {
		this.root = null;
	}
	
	@Override
	public void insertNode(String value) {
		if(root == null) {
			root = new BinaryTreeNode(value);
			return;
		}
		
		root = splay(root, value);
		
		int comparisson = value.compareToIgnoreCase(root.getValue());
		if(comparisson < 0) { //Value es menor que la raiz
			BinaryTreeNode node = new BinaryTreeNode(value);
			node.setLeft(root.getLeft());
			node.setRight(root);
			root.setLeft(null);
			root = node;
		}
		else if(comparisson > 0) {//Value es mayor que la raiz
			BinaryTreeNode node = new BinaryTreeNode(value);
			node.setRight(root.getRight());
			node.setLeft(root);
			root.setRight(null);
			root = node;
		}
		else {//Value es igual que la raiz solo actualiza el valor
			root.setValue(value);
		}
	}
	
	@Override
	public BinaryTreeNode searchNode(String value) {
		root = splay(root, value);
		int comparisson = value.compareToIgnoreCase(root.getValue());
		if(comparisson == 0) {
			return root;
		}else {
			return null;
		}
	}
	
	@Override 
	public void deleteNode(String value) {
		if(root == null) {
			return;
		}
		
		root = splay(root, value);
		
		int comparisson = value.compareToIgnoreCase(root.getValue());
		
		if(comparisson == 0) { //El nodo se encontro
			if(root.getLeft() == null) {
				root = root.getRight();
			}else {
				BinaryTreeNode node = root.getRight();
				root = root.getLeft();
				splay(root, value);
				root.setRight(node);
			}
		}
		//else: el nodo no se encontro en el arbol, no hay nada que eliminar
	}
	
	private BinaryTreeNode splay(BinaryTreeNode node, String value) {
		if(node == null) {
			return null;
		}
		
		int comparisson = value.compareToIgnoreCase(node.getValue());
		
		if(comparisson < 0) {
			if(node.getLeft() == null) { //El valor no esta en el arbol
				return node;
			}
			int comparisson2 = value.compareToIgnoreCase(node.getLeft().getValue());
			if(comparisson2 < 0) {
				node.getLeft().setLeft(splay(node.getLeft().getLeft(), value));
				node = rotateLeftLeft(node);
			}
			else if(comparisson2 > 0) {
				node.getLeft().setRight(splay(node.getLeft().getRight(), value));
				if(node.getLeft().getRight() != null) {
					node.setLeft(rotateRightRight(node.getLeft()));
				}
			}
			if(node.getLeft() == null) {
				return node;
			}else {
				return rotateLeftLeft(node);
			}
		}
		else if(comparisson > 0) {
			if(node.getRight() == null) {
				return node;
			}
			int comparisson2 = value.compareToIgnoreCase(node.getRight().getValue());
			if(comparisson2 < 0) {
				node.getRight().setLeft(splay(node.getRight().getLeft(), value));
				if(node.getRight().getLeft() != null) {
					node.setRight(rotateLeftLeft(node.getRight()));
				}
			}
			else if(comparisson2 > 0) {
				node.getRight().setRight(splay(node.getRight().getRight(), value));
				node = rotateRightRight(node);
			}
			if(node.getRight() == null) {
				return node;
			}else {
				return rotateRightRight(node);
			}
		}
		else {
			return node;
		}
	}
}
