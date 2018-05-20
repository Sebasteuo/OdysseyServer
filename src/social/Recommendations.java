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
 * Clase encargada de  gestionar recomendaciones que se le hacen al usuario
 * @author Sebastian Alba
 * @author David Pereira
 * @author Randall Mendez 
 */
public class Recommendations {
	private BinarySearchTree usersTree;
	/**
	 * Constructor de la clase
	 * @param usersTree
	 */
	public Recommendations(BinarySearchTree usersTree) {
		this.usersTree = usersTree;
	}
	/**
	 * Settea el arbol 
	 * @param usersTree
	 */
	public void setUsersTrees(BinarySearchTree usersTree) {
		this.usersTree = usersTree;
	}
	/**
	 * Se encarga de añadir nuevos mensajes al registro del usuario
	 * @param message
	 * @throws Exception
	 */
	public void addMessages(String[] message) throws Exception {
		//["emisor", "receptor", "mensaje"]
		//[{"emisor":"", "mensaje", ""}, {"emisor":"", "mensaje":""}]
		String[] array = usersTree.inorder();
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for(int i = 0; i < array.length; i++) {
			JsonObjectBuilder objBuilder = Json.createObjectBuilder();
			JsonObject obj = Json.createReader(new StringReader(array[i])).readObject();
			if(obj.getString("UserName").equalsIgnoreCase(message[1])) {
				JsonArray messages = obj.getJsonArray("Messages");
				JsonArrayBuilder arrBuilder2 = Json.createArrayBuilder();
				for(int j = 0; j < messages.size(); j++) {
					arrBuilder2.add(messages.get(j));
				}
				arrBuilder2.add(Json.createObjectBuilder().add("Emisor", message[0]).add("Message", message[2]).build());
				objBuilder.add("UserName", obj.get("UserName"));
				objBuilder.add("Name", obj.get("Name"));
				objBuilder.add("Age", obj.get("Age"));
				objBuilder.add("MusicalGenres", obj.get("MusicalGenres"));
				objBuilder.add("Password", obj.get("Password"));				
				objBuilder.add("Friends", obj.get("Friends"));
				objBuilder.add("Messages", arrBuilder2.build());
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
	}
	/**
	 * Se encarga de obtener los mensajes que tiene el usuario en sesion
	 * @param userName
	 * @return string
	 * @throws Exception
	 */
	public String getMessagesList(String userName) throws Exception {
		BinaryTreeNode userData = usersTree.searchNode(userName);								// Obtiene el nodo que contiene el usuario
		JsonObject obj = Json.createReader(new StringReader(userData.getValue())).readObject(); // llama al valor que contiene, y lo parsea a JsonObject
		String messageList = "";
		System.out.println(obj);
		JsonArray messages = obj.getJsonArray("Messages");
		for(int j = 0; j < messages.size(); j++) {
			JsonObject msg = messages.getJsonObject(j);
			messageList += msg.getString("Emisor") + "/" + msg.getString("Message") + "/";
		}
		System.out.println(messageList);
		return messageList;
	}
}
