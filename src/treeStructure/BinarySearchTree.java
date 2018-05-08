package treeStructure;

public class BinarySearchTree {
	protected BinaryTreeNode root;
	
	public BinarySearchTree() {
		this.root = null;
	}
	
	public void insertNode(String value, String key) {
		this.root = insert(root, value, key);
	}
	
	public BinaryTreeNode searchNode(String key) {
		return search(root, key);
	}
	
	public void deleteNode(String key) {
		delete(root, key);
	}
	
	public void setRoot(BinaryTreeNode root) {
		this.root = root;
	}
	
	public BinaryTreeNode getRoot() {
		return this.root;
	}
	
	public boolean isEmpty() {
		return root == null;
	}
	
	public void makeTreeEmpty() {
		this.root = null;
	}
	
	public int consultHeight(BinaryTreeNode node) {
		return node == null? -1 : node.getHeight();
	}
	
	public int getMax(int value1, int value2) {
		return value1 > value2? value1 : value2;
	}
	
	public String findMin(BinaryTreeNode node) {
		return node.getLeft() == null? node.getValue() : findMin(node.getLeft());
	}
	
		
    public void inorder(){
        inorder(root);
    }
	
    public BinaryTreeNode rotateLeftLeft(BinaryTreeNode k2) {
		BinaryTreeNode k1 = k2.getLeft();
		k2.setLeft(k1.getRight());
		k1.setRight(k2);
		k2.setHeight(getMax(consultHeight(k2.getLeft()), consultHeight(k2.getRight())) + 1);
		k1.setHeight(getMax(consultHeight(k1.getLeft()), consultHeight(k1.getRight())) + 1);
		return k1;
	}
	
    public BinaryTreeNode rotateRightRight(BinaryTreeNode k1) {
		BinaryTreeNode k2 = k1.getRight();
		k1.setRight(k2.getLeft());
		k2.setLeft(k1);
		k1.setHeight(getMax(consultHeight(k1.getLeft()), consultHeight(k1.getRight())) + 1);
		k2.setHeight(getMax(consultHeight(k2.getLeft()), consultHeight(k2.getRight())) + 1);
		return k2;
	}
	
    public BinaryTreeNode rotateRightLeft(BinaryTreeNode k3) {
		k3.setLeft(rotateRightRight(k3.getLeft()));
		return rotateLeftLeft(k3);
	}
	
    public BinaryTreeNode rotateLeftRight(BinaryTreeNode k3) {
		k3.setRight(rotateLeftLeft(k3.getRight()));
		return rotateRightRight(k3);
	}

    private void inorder(BinaryTreeNode r){
        if (r != null)
        {
            inorder(r.getLeft());
            System.out.print(r.getValue() +"\n");
            inorder(r.getRight());
        }
    }
    
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
