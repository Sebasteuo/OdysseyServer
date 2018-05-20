package treeStructure;
/**
 * Clase encargada de definir el arbol Splay
 * @author Sebastian Alba
 * @author David Pereira
 * @author Randall Mendez 
 */
public class SplayTree extends BinarySearchTree{
	/**
	 * Constructor de la clase
	 */
	public SplayTree() {
		this.root = null;
		this.size = 0;
	}
	/**
	 * Se encarga de insertar el nodo en el arbol splay
	 */
	@Override
	public void insertNode(String value, String key) {
		if(root == null) {
			root = new BinaryTreeNode(value, key);
			this.size += 1;
			return;
		}
		
		root = splay(root, key);
		
		int comparisson = key.compareToIgnoreCase(root.getKey());
		if(comparisson < 0) { //Value es menor que la raiz
			BinaryTreeNode node = new BinaryTreeNode(value, key);
			node.setLeft(root.getLeft());
			node.setRight(root);
			root.setLeft(null);
			root = node;
			this.size += 1;
		}
		else if(comparisson > 0) {//Value es mayor que la raiz
			BinaryTreeNode node = new BinaryTreeNode(value, key);
			node.setRight(root.getRight());
			node.setLeft(root);
			root.setRight(null);
			root = node;
			this.size += 1;
		}
		else {//Value es igual que la raiz solo actualiza el valor
			root.setKey(key);
		}
		
	}
	/**
	 * Se encarga de buscar el nodo en el arbol
	 */
	@Override
	public BinaryTreeNode searchNode(String key) {
		root = splay(root, key);
		int comparisson = key.compareToIgnoreCase(root.getKey());
		if(comparisson == 0) {
			return root;
		}else {
			return null;
		}
	}
	/**
	 * Se encarga de borrar el nodo del arbol
	 */
	@Override 
	public void deleteNode(String key) {
		if(root == null) {
			return;
		}
		
		root = splay(root, key);
		
		int comparisson = key.compareToIgnoreCase(root.getKey());
		
		if(comparisson == 0) { //El nodo se encontro
			if(root.getLeft() == null) {
				root = root.getRight();
			}else {
				BinaryTreeNode node = root.getRight();
				root = root.getLeft();
				splay(root, key);
				root.setRight(node);
			}
		}
		//else: el nodo no se encontro en el arbol, no hay nada que eliminar
	}
	/**
	 * Se encarga de realizar la operacion splay(LLeva los nodos hasta la raiz)
	 * @param node
	 * @param key
	 * @return BinaryTreeNode
	 */
	private BinaryTreeNode splay(BinaryTreeNode node, String key) {
		if(node == null) {
			return null;
		}
		
		int comparisson = key.compareToIgnoreCase(node.getKey());
		
		if(comparisson < 0) {
			if(node.getLeft() == null) { //El valor no esta en el arbol
				return node;
			}
			int comparisson2 = key.compareToIgnoreCase(node.getLeft().getKey());
			if(comparisson2 < 0) {
				node.getLeft().setLeft(splay(node.getLeft().getLeft(), key));
				node = rotateLeftLeft(node);
			}
			else if(comparisson2 > 0) {
				node.getLeft().setRight(splay(node.getLeft().getRight(), key));
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
			int comparisson2 = key.compareToIgnoreCase(node.getRight().getKey());
			if(comparisson2 < 0) {
				node.getRight().setLeft(splay(node.getRight().getLeft(), key));
				if(node.getRight().getLeft() != null) {
					node.setRight(rotateLeftLeft(node.getRight()));
				}
			}
			else if(comparisson2 > 0) {
				node.getRight().setRight(splay(node.getRight().getRight(), key));
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
