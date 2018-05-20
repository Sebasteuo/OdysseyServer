package treeStructure;
/**
 * Clase que define el nodo de un arbol binario
 * @author Sebastian Alba
 * @author David Pereira
 * @author Randall Mendez 
 */
public class BinaryTreeNode {
	private String key;
	private String value;
	private BinaryTreeNode left;
	private BinaryTreeNode right;
	private int height;
	
	/**
	 * Constructor de la clase
	 */
	public BinaryTreeNode() {
		this.key = "";
		this.value = "";
		this.left = null;
		this.right = null;
		this.height = 0;
	}
	/**
	 * Constructor de la clase
	 * @param value
	 * @param key
	 */
	public BinaryTreeNode(String value, String key) {
		this.key = key;
		this.value = value;
		this.left = null;
		this.right = null;
		this.height = 0;
	}
	/**
	 * Obtiene la llave del nodo
	 * @return string
	 */
	public String getKey() {
		return key;
	}
	/**
	 * Settea la llave del nodo
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * Setea el valor del nodo
	 * @param value
	 */
	public void setValue(String value) {		
		this.value = value;
	}
	/**
	 * Obtiene el valor del nodo
	 * @return string
	 */
	public String getValue() {
		return this.value;		
	}
	/**
	 * Setea el hijo izquierdo del nodo
	 * @param left
	 */
	public void setLeft(BinaryTreeNode left) {
		this.left = left;
	}
	/**
	 * Obtiene el hijo izquierdo del nodo
	 * @return BinaryTreeNode
	 */
	public BinaryTreeNode getLeft() {
		return this.left;
	}
	/**
	 * Settea el hijo derecho del nodo
	 * @param right
	 */
	public void setRight(BinaryTreeNode right) {
		this.right = right;
	}
	/**
	 * Obtiene el hijo derecho del nodo
	 * @return BinaryTreeNode
	 */
	public BinaryTreeNode getRight() {
		return this.right;
	}
	/**
	 * Setea la altura de nodo
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * Obtiene la altura del nodo
	 * @return int
	 */
	public int getHeight() {
		return this.height;
	}
}
