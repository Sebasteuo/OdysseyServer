package treeStructure;
/**
 * Clase encargada de crear la estructura del arbol binario de busqueda
 * @author Sebastian Alba
 * @author David Pereira
 * @author Randall Mendez  
 */
public class BinarySearchTree {
	protected BinaryTreeNode root;
	protected int size;
	private String[] array;
    private int count = 0;
	/**
	 * Constructor de la clase
	 */
	public BinarySearchTree() {
		this.root = null;
		this.size = 0;
	}
	/**
	 * Se encarga de llamar al metodo principal de la insercion de nodos
	 * @param value
	 * @param key
	 */
	public void insertNode(String value, String key) {
		this.root = insert(root, value, key);
		this.size += 1;
	}
	/**
	 * Funcion que llama al metodo de busqueda
	 * @param key
	 * @return
	 */
	public BinaryTreeNode searchNode(String key) {
		return search(root, key);
	}
	/**
	 * Se encarga de llamar al metodo de borrar nodos
	 * @param key
	 */
	public void deleteNode(String key) {
		delete(root, key);
	}
	/**
	 * Se encarga de settear el nodo raiz
	 * @param root
	 */
	public void setRoot(BinaryTreeNode root) {
		this.root = root;
	}
	/**
	 * Se encarga de obtener el nodo raiz
	 * @return
	 */
	public BinaryTreeNode getRoot() {
		return this.root;
	}
	/**
	 * Se encarga de verificar si el arbol esta vacio
	 * @return
	 */
	public boolean isEmpty() {
		return root == null;
	}
	/**
	 * Se encarga de vaciar el arbol
	 */
	public void makeTreeEmpty() {
		this.root = null;
	}
	/**
	 * Se encarga de consultar la altura del arbol
	 * @param node
	 * @return int
	 */
	public int consultHeight(BinaryTreeNode node) {
		return node == null? -1 : node.getHeight();
	}
	/**
	 * Se encarga de obtener el valor maximo entre dos elementos
	 * @param value1
	 * @param value2
	 * @return int
	 */
	public int getMax(int value1, int value2) {
		return value1 > value2? value1 : value2;
	}
	/**
	 * Se encarga de buscar el valor minimo que existe en el arbol
	 * @param node
	 * @return int
	 */
	public String findMin(BinaryTreeNode node) {
		return node.getLeft() == null? node.getValue() : findMin(node.getLeft());
	}
	
	
	/**
	 * Se encarga de llamar al metodo principal del inorder
	 * @return array
	 */
    public String[] inorder(){
    	array = new String[size];
    	inorder(root);
        return array;
    }
    /**
     * Se encarga de la rotacion izq-izq del arbol binario
     * @param k2
     * @return BinaryTreeNode
     */
    public BinaryTreeNode rotateLeftLeft(BinaryTreeNode k2) {
		BinaryTreeNode k1 = k2.getLeft();
		k2.setLeft(k1.getRight());
		k1.setRight(k2);
		k2.setHeight(getMax(consultHeight(k2.getLeft()), consultHeight(k2.getRight())) + 1);
		k1.setHeight(getMax(consultHeight(k1.getLeft()), consultHeight(k1.getRight())) + 1);
		return k1;
	}
    /**
     * Se encarga de rotar der-der del arbol binario
     * @param k1
     * @return BinaryTreeNode
     */
    public BinaryTreeNode rotateRightRight(BinaryTreeNode k1) {
		BinaryTreeNode k2 = k1.getRight();
		k1.setRight(k2.getLeft());
		k2.setLeft(k1);
		k1.setHeight(getMax(consultHeight(k1.getLeft()), consultHeight(k1.getRight())) + 1);
		k2.setHeight(getMax(consultHeight(k2.getLeft()), consultHeight(k2.getRight())) + 1);
		return k2;
	}
    /**
     * Se encarga de la rotacion der-izq del arbol binario
     * @param k3
     * @return BinaryTreeNode
     */
    public BinaryTreeNode rotateRightLeft(BinaryTreeNode k3) {
		k3.setLeft(rotateRightRight(k3.getLeft()));
		return rotateLeftLeft(k3);
	}
    /**
     * Se encarga de la rotacion izq-der
     * @param k3
     * @return BinaryTreeNode
     */
    public BinaryTreeNode rotateLeftRight(BinaryTreeNode k3) {
		k3.setRight(rotateLeftLeft(k3.getRight()));
		return rotateRightRight(k3);
	}
    /**
     * Se encarga de recorrer el arbol en Inorden
     * @param r
     */
    private void inorder(BinaryTreeNode r){
        if (r != null){
            inorder(r.getLeft());
            array[count++] = r.getValue();
            inorder(r.getRight());
        }else {    
        	return;
        }
    }
    /**
     * Se encarga de insertar los nodos en el arbol binario
     * @param root
     * @param value
     * @param key
     * @return BinaryTreeNode
     */
    private BinaryTreeNode insert(BinaryTreeNode root, String value, String key) {
    	if(root == null) {
    		root = new BinaryTreeNode(value, key);
    		return root;
    	}
    	
    	int comparisson = value.compareToIgnoreCase(root.getValue());
    	if(comparisson < 0) { //El valor a insertar es menor al de la raiz
    		root.setLeft(insert(root.getLeft(), value, key));    		
    	}
    	else if(comparisson > 0) { //El valor a insertar es mayor al de la raiz
    		root.setRight(insert(root.getRight(), value, key));
    	}
    	//else: ya existe entonces retorna la raiz sin cambiar nada
    	return root;
    }
    /**
     * Se encarga de buscar en el arbol binario
     * @param r
     * @param key
     * @return BinaryTreeNode
     */
	private BinaryTreeNode search(BinaryTreeNode r, String key) {
		while(r != null) {
			if(r.getKey().equalsIgnoreCase(key)) {
				return r;
			}else {
				int comparisson = key.compareToIgnoreCase(r.getKey());
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
	/**
	 * Se encarga de borrar los nodos del arbol binario	
	 * @param r
	 * @param key
	 * @return BinaryTreeNode
	 */
	private BinaryTreeNode delete(BinaryTreeNode r, String key) {
		if(r == null) {
			return null;
		}		

		if(r.getKey() == key) {
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
		if(key.compareToIgnoreCase(r.getKey()) < 0) {
			r.setLeft(delete(r.getLeft(), key));
			return r;
		}
		r.setRight(delete(r.getRight(), key));
		return r;
	}
}