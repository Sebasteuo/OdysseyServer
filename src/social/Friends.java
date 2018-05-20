package social;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import treeStructure.BinarySearchTree;
import treeStructure.BinaryTreeNode;
/**
 * dministración y presentación de listas de amigos, obteniendo los amigos del
árbol binario de búsqueda que contiene el registro de usuarios,según el usuario
que se encuentra en sesión. En clase friends, revisa y toma como parámetro el nombre 
de usuario en sesión para ir a revisar el documento asociado al registro, toma la lista 
de amigos de ese usuario del árbol
 * Clase encargada de gestionar los amigos que tenga el usuario en su registro
 * @author Sebastian Alba
 * @author David Pereira
 * @author Randall Mendez
 *
 */
public class Friends {
	private BinarySearchTree usersTree;
	/**
	 * Constructor de la clase
	 * @param usersTree
	 */
	public Friends(BinarySearchTree usersTree) {
		this.usersTree = usersTree;
	}
	/**
	 * Se encarga de setear el arbol de usuarios
	 * @param usersTree
	 */
	public void setUsersTrees(BinarySearchTree usersTree) {
		this.usersTree = usersTree;
	}
	/**
	 * Se encarga de agregar amigos al registro del usuario
	 * @param friend
	 * @return string
	 * @throws Exception
	 */
	public String addFriends(String[] friend) throws Exception {
		//["usuario", "amigo"]
		//["amigo1", "amigo2", "amigo3"]
		if(usersTree.searchNode(friend[1]) == null) {
			System.out.println("El amigo que desea agregar no existe");
			return "false";
		}
		if(!friendAlreadyExists(friend[0], friend[1])) {
			String[] array = usersTree.inorder();
			JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
			for(int i = 0; i < array.length; i++) {
				JsonObjectBuilder objBuilder = Json.createObjectBuilder();
				JsonObject obj = Json.createReader(new StringReader(array[i])).readObject();
				if(obj.getString("UserName").equalsIgnoreCase(friend[0])) {
					JsonArray friends = obj.getJsonArray("Friends");
					JsonArrayBuilder arrBuilder2 = Json.createArrayBuilder();
					for(int j = 0; j < friends.size(); j++) {
						arrBuilder2.add(friends.get(j));
					}
					arrBuilder2.add(friend[1]);
					objBuilder.add("UserName", obj.get("UserName"));
					objBuilder.add("Name", obj.get("Name"));
					objBuilder.add("Age", obj.get("Age"));
					objBuilder.add("MusicalGenres", obj.get("MusicalGenres"));
					objBuilder.add("Password", obj.get("Password"));				
					objBuilder.add("Friends", arrBuilder2.build());
					objBuilder.add("Messages", obj.get("Messages"));
					arrBuilder.add(objBuilder.build());
				}else {
					arrBuilder.add(obj);
				}
			}
			
			JsonArray finalArray = arrBuilder.build();
			OutputStream tempOS = new FileOutputStream(new File("usuarios.json"));
			JsonWriter writer = Json.createWriter(tempOS);
			writer.writeArray(finalArray);
			writer.close();
			
			System.out.println("Amigo agregado");
			return "true";
		}else {
			System.out.println("El amigo ya se encuentra agregado");
			return "ya";
		}
	}
	/**
	 * Se encarga de obtener la lista de amigos del usuario en sesion
	 * @param userName
	 * @return string
	 * @throws Exception
	 */
	public String getFriendsList(String userName) throws Exception {
		BinaryTreeNode userData = usersTree.searchNode(userName);								// Obtiene el nodo que contiene el usuario
		JsonObject obj = Json.createReader(new StringReader(userData.getValue())).readObject(); // llama al valor que contiene, y lo parsea a JsonObject
		String friendsList = "";
		
		JsonArray friends = obj.getJsonArray("Friends");
		for(int j = 0; j < friends.size(); j++) {
			friendsList += friends.getString(j) + "/";
		}
		return friendsList;
	}
	/**
	 * Se encarga de verificar si el amigo ya esta agregado
	 * @param userName
	 * @param friend
	 * @return boolean
	 * @throws Exception
	 */
	private boolean friendAlreadyExists(String userName, String friend) throws Exception {
		BinaryTreeNode userData = usersTree.searchNode(userName);								// Obtiene el nodo que contiene el usuario
		JsonObject obj = Json.createReader(new StringReader(userData.getValue())).readObject(); // llama al valor que contiene, y lo parsea a JsonObject
		JsonArray friends = obj.getJsonArray("Friends");
		for(int j = 0; j < friends.size(); j++) {
			if(friends.getString(j).equalsIgnoreCase(friend)) {
				return true;
			}
		}			
		return false;
	}
}

