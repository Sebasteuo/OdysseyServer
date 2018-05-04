import treeStructure.AVLTree;

public class Main {
	
	public static void main(String args[]) {
		AVLTree avl = new AVLTree();
		avl.insertNode("Ardilla");
		avl.insertNode("Buho");
		avl.insertNode("Caballo");
		avl.insertNode("Danta");
		avl.insertNode("Elefante");
		avl.insertNode("Foca");
		
		System.out.println("Arbol inicial: ");
		avl.inorder();
		
		avl.deleteNode("Buho");
		
		System.out.println("Arbol despues de eliminar: ");
		avl.inorder();
	}
}
