import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import musicLibrary.MusicLibrary;
import treeStructure.AVLTree;
import treeStructure.BTree;
import treeStructure.BinarySearchTree;
import treeStructure.BinaryTreeNode;
import treeStructure.SplayTree;
import users.ExistingUser;
import users.NewUser;

public class Main {
	static BinarySearchTree users = new BinarySearchTree();
	
	public static void main(String args[]) throws Exception {		
		
		/*try {
			//Carga el archivo JSON en un arbol binario al iniciar el servidor
			users = loadFileInBST();
			
			//Obtiene el json correspondiente al usuario
			JsonObject object = Json.createReader(new StringReader(users.searchNode("davepj07").getValue())).readObject();	
		}catch(NullPointerException ex) {}	
		
		NewUser user = new NewUser();
		user.setUserName("davepj07");
		user.setName("David Pereira");
		user.setAge(21);
		String[] msclGrs = {"Pop","Rock","Electro"};
		user.setMusicalGenres(msclGrs);
		user.setPassword("p455w0rd");
		String[] fds = {"Andres", "Carla", "Jennifer"};
		user.setFriends(fds);
		JsonObject toInsert = user.signInUser();
		System.out.println(toInsert.toString());
		if(toInsert != null) {
			users.insertNode(toInsert.toString(), toInsert.getString("UserName"));			
		}
		
		NewUser user2 = new NewUser();
		user2.setUserName("sebas84");
		user2.setName("Sebastian Alba");
		user2.setAge(21);
		String[] msclGrs2 = {"Clasica","Pop","Electro"};
		user2.setMusicalGenres(msclGrs2);
		user2.setPassword("p455w0rd");
		String[] fds2 = {"Byron", "Cristian", "Karina"};
		user2.setFriends(fds2);
		JsonObject toInsert2 = user2.signInUser();
		if(toInsert2 != null) {
			users.insertNode(toInsert2.toString(), toInsert2.getString("UserName"));			
		}
		
		NewUser user3 = new NewUser();
		user3.setUserName("hack998");
		user3.setName("Samuel Mendez");
		user3.setAge(21);
		String[] msclGrs3 = {"Clasica","Pop","Instrumental"};
		user3.setMusicalGenres(msclGrs3);
		user3.setPassword("p455w0rd");
		String[] fds3 = {"Carlos", "Valeria", "Andrea"};
		user3.setFriends(fds3);
		JsonObject toInsert3 = user3.signInUser();
		if(toInsert3 != null) {
			users.insertNode(toInsert3.toString(), toInsert3.getString("UserName"));			
		}		
		
		ExistingUser exUser = new ExistingUser("davepj07", "p455w0rd");
		exUser.logIn(users);*/
		
		MusicLibrary mL = new MusicLibrary();
		mL.storeSong("Get Got", "https://dl.last.fm/static/1526169027/131211148/10d983676336984bb683b9f490bd919021c116314e355f6ccea2296c3c9bb2f3/Death+Grips+-+Get+Got.mp3");
		File file = new File("C:\\Users\\david\\Documents\\MusicLibrary\\Get Got.mp3");
		String[] arr = {"Get Got","Lifetheory", "DavePJ07", "Pop", "Let's Bring It All Together!"};
		mL.printMetadata(file);
		//mL.updateMetadata(file, arr);
		//mL.printMetadata(file);
	}
	
	public static BinarySearchTree loadFileInBST() {
		BinarySearchTree bst = new BinarySearchTree();	
		try {
			FileReader fileReader = new FileReader("usuarios.json");
			if(fileReader.ready()){	
				InputStream IS = new FileInputStream(new File("usuarios.json"));			
				JsonReader reader = Json.createReader(IS);			
				JsonArray oldArray = reader.readArray();			
				reader.close();			
				for(JsonValue i:oldArray) {
					bst.insertNode(i.toString(), i.asJsonObject().getString("UserName"));
				}
			}
			fileReader.close();	
			System.out.println("Arbol BB inicial: ");
			bst.inorder();
			return bst;
		}catch(Exception ex) {
			return bst;
		}		
	}
}
