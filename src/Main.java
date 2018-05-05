import treeStructure.AVLTree;
import treeStructure.BTree;
import treeStructure.BinarySearchTree;
import treeStructure.SplayTree;

public class Main {
	
	public static void main(String args[]) {
		
		BinarySearchTree bst = new BinarySearchTree();
		bst.insertNode("T");
		bst.insertNode("U");
		bst.insertNode("V");
		bst.insertNode("W");
		bst.insertNode("X");
		bst.insertNode("Y");
		bst.insertNode("Z");
		
		System.out.println("Arbol BB inicial: ");
		bst.inorder();
		
		bst.deleteNode("W");
		
		System.out.println("\nArbol BB despues de eliminar: ");
		bst.inorder();
		
		AVLTree avl = new AVLTree();
		avl.insertNode("A");
		avl.insertNode("D");
		avl.insertNode("E");
		avl.insertNode("B");
		avl.insertNode("C");
		avl.insertNode("F");		
		
		System.out.println("\nArbol AVL inicial: ");
		avl.inorder();
		
		avl.deleteNode("B");
		
		System.out.println("\nArbol AVL despues de eliminar: ");
		avl.inorder();
		
		SplayTree splay = new SplayTree();
		splay.insertNode("G");
		splay.insertNode("J");
		splay.insertNode("K");
		splay.insertNode("H");
		splay.insertNode("I");
		splay.insertNode("L");		
		
		System.out.println("\nArbol Splay inicial: ");
		splay.inorder();
		
		splay.deleteNode("I");
		
		System.out.println("\nArbol Splay despues de eliminar: ");
		splay.inorder();
		
		BTree bTree = new BTree(4);
		bTree.insertNode("Q");
		bTree.insertNode("R");
		bTree.insertNode("M");
		bTree.insertNode("N");
		bTree.insertNode("S");
		bTree.insertNode("O");
		bTree.insertNode("P");
		
		System.out.println("\nArbol B inicial: ");
		bTree.print(bTree.getRoot());
		
		bTree.deleteKey("P");
		
		System.out.println("\nArbol B despues de eliminar: ");
		bTree.print(bTree.getRoot());
	}
}
