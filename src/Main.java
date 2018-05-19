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

import musicLibrary.IndexLibrary;
import musicLibrary.MusicLibrary;
import social.Friends;
import social.Recommendations;
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
		
		try {
			//Carga el archivo JSON en un arbol binario al iniciar el servidor
			users = loadFileInBST();
		}catch(NullPointerException ex) {}	
		
		NewUser user = new NewUser(users);
		user.setUserName("davepj07");
		user.setName("David Pereira");
		user.setAge(21);
		String[] msclGrs = {"Pop","Rock","Electro"};
		user.setMusicalGenres(msclGrs);
		user.setPassword("p455w0rd");
		String[] fds = {"Andres", "Carla", "Jennifer"};
		user.setFriends(fds);
		JsonObject toInsert = user.signInUser();

		if(toInsert != null) {
			users.insertNode(toInsert.toString(), toInsert.getString("UserName"));			
		}
		
		NewUser user2 = new NewUser(users);
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
		
		NewUser user3 = new NewUser(users);
		user3.setUserName("hack998");
		user3.setName("Samuel Mendez");
		user3.setAge(21);
		String[] msclGrs3 = {"Clasica","Pop","Instrumental"};
		user3.setMusicalGenres(msclGrs3);
		user3.setPassword("p455w0rd");
		String[] fds3 = {"Carlos", "sebas84", "Andrea"};
		user3.setFriends(fds3);
		JsonObject toInsert3 = user3.signInUser();
		if(toInsert3 != null) {
			users.insertNode(toInsert3.toString(), toInsert3.getString("UserName"));			
		}		
		
		ExistingUser exUser = new ExistingUser("davepj07", "p455w0rd", users);
		exUser.logIn();
		
		Sockets.conectar(users);
	  /*System.out.println(exUser.getExistingUserNames());
		
		Recommendations messages = new Recommendations(users);			  
		String[] message = {"davepj07","hack998","Esto es solo una prueba"}; 
		messages.addMessages(message);									  // *** CADA VEZ QUE SE AGREGUE UN MENSAJE ***	
		messages.setUsersTrees(users = loadFileInBST());				  // *** SE DEBE VOLVER A CARGAR EL ARCHIVO EN EL BST ***
		messages.getMessagesList("hack998");
		
		String[] friend = {"sebas84", "davepj07"};
		Friends friends = new Friends(users);
		String wasAdded = friends.addFriends(friend);				   						  // *** CADA VEZ QUE SE AGREGUE UN AMIGO ***
		if(wasAdded.equalsIgnoreCase("true")) friends.setUsersTrees(users = loadFileInBST()); // *** SE DEBE VOLVER A CARGAR EL ARCHIVO EN EL BST ***
		friends.getFriendsList("sebas84");*/
		
		MusicLibrary mL = new MusicLibrary();
		mL.storeSong("The Only Place", "https://dl.last.fm/static/1526631790/131564291/5b375529319897e6224a051fa6f8068fb34190272b1ac1331c348cce5935156f/Best+Coast+-+The+Only+Place.mp3", "davepj07");
		mL.deleteSong("Daisy", "davepj07");
		mL.getUserLibrary("davepj07");
		File file = new File("C:\\Users\\david\\Documents\\MusicLibrary\\Principal\\Get Got.mp3");
		mL.syncMetadata(file, "davepj07");
		String[] arr = {"Get Got","Lifetheory", "Other", "Let's Bring it All Together!", "2016", ""};
		mL.printMetadata(file);
		mL.updateMetadata(file, "davepj07", arr);
		mL.printMetadata(file);
		
		/*IndexLibrary index = new IndexLibrary();
		BTree bTree = index.createTitleIndex();
		bTree.print(bTree.getRoot());*/
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
			return bst;
		}catch(Exception ex) {
			return bst;
		}		
	}
}
