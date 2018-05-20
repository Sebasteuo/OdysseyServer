package treeStructure;
/**
 * Clase encargada de crear el arbol AVL, HEREDA DE LA CLASE BINARYSEARCHTREE
 * @author Sebastian Alba
 * @author David Pereira
 * @author Randall Mendez 
 *
 */
public class AVLTree extends BinarySearchTree{
	/**
	 * Constructor de la clase
	 */
	public AVLTree() {
		this.root = null;
		this.size = 0;
	}
	/**
	 * Funcion encargada de llamar la funcion principal de la insercion de nodos
	 */
	@Override
	public void insertNode(String value, String key) {
		this.root = insert(value, root, key);
		this.size += 1;
	}
	/**
	 * Metodo encargado de insertar nodos
	 * @param value
	 * @param root
	 * @param key
	 * @return nodo
	 */
	private BinaryTreeNode insert(String value, BinaryTreeNode root, String key) {
		if(root == null) {
			root = new BinaryTreeNode(value, key);
		}else{
			int comparisson = key.compareToIgnoreCase(root.getKey()); //Negativo value es menor alfab. Positivo value es mayor alfab.
			if(comparisson < 0) {
				root.setLeft(insert(value, root.getLeft(), key));
				if(consultHeight(root.getLeft()) - consultHeight(root.getRight()) == 2) {					
					if(key.compareToIgnoreCase(root.getRight().getKey()) < 0) {
						root = rotateLeftLeft(root);
					}else {
						root = rotateRightLeft(root);
					}
				}
			}else if(comparisson > 0) {
				root.setRight(insert(value, root.getRight(), key));
				if(consultHeight(root.getRight()) - consultHeight(root.getLeft()) == 2) {
					if(key.compareToIgnoreCase(root.getRight().getKey()) > 0) {
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
