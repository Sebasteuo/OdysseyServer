package users;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.security.MessageDigest;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import treeStructure.BinarySearchTree;

public class ExistingUser {
	private String userName;
	private String password;
	
	public ExistingUser(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public boolean logIn(BinarySearchTree users) throws Exception{
		if(users.searchNode(this.userName) != null) {
			JsonObject object = Json.createReader(new StringReader(users.searchNode(this.userName).getValue())).readObject();
			String passwordEncode = encodePassword(this.password);
			if(object.getString("Password").equalsIgnoreCase(passwordEncode)) {
				System.out.println("Inicio exitoso");
				return true;
			}else {
				System.out.println("La contrasena es incorrecta.");
				return false;
			}
		}else {
			System.out.println("El usuario no existe.");
			return false;
		}		
	}
	
	public void addMessages(String[] message) throws Exception {
		//["emisor", "receptor", "mensaje"]
		//[{"emisor":"", "mensaje", ""}, {"emisor":"", "mensaje":""}]
		JsonArray array = readJsonFile();
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for(int i = 0; i < array.size(); i++) {
			JsonObjectBuilder objBuilder = Json.createObjectBuilder();
			JsonObject obj = array.getJsonObject(i);
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
	
	public String addFriends(String[] friend) throws Exception {
		//["usuario", "amigo"]
		//["amigo1", "amigo2", "amigo3"]
		if(!userNameExists(friend[1])) {
			return "false";
		}
		if(!friendAlreadyExists(friend[0], friend[1])) {
			JsonArray array = readJsonFile();
			JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
			for(int i = 0; i < array.size(); i++) {
				JsonObjectBuilder objBuilder = Json.createObjectBuilder();
				JsonObject obj = array.getJsonObject(i);
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
			return "true";
		}else {
			return "ya";
		}
	}
	
	public String getExistingUserNames() throws Exception {
		JsonArray array = readJsonFile();
		String existingUsers = "";
		for(int i = 0; i < array.size(); i++) {
			JsonObject obj = array.getJsonObject(i);
			existingUsers += obj.getString("UserName") + "/";
		}
		return existingUsers;
	}
	
	public String getFriendsList(String userName) throws Exception {
		JsonArray array = readJsonFile();
		String friendsList = "";
		for(int i = 0; i < array.size(); i++) {
			JsonObject obj = array.getJsonObject(i);
			if(obj.getString("UserName").equalsIgnoreCase(userName)) {
				JsonArray friends = obj.getJsonArray("Friends");
				for(int j = 0; j < friends.size(); j++) {
					friendsList += friends.getString(j) + "/";
				}
			}			
		}
		return friendsList;
	}
	
	public String getMessagesList(String userName) throws Exception {
		JsonArray array = readJsonFile();
		String messageList = "";
		for(int i = 0; i < array.size(); i++) {
			JsonObject obj = array.getJsonObject(i);
			if(obj.getString("UserName").equalsIgnoreCase(userName)) {
				JsonArray messages = obj.getJsonArray("Messages");
				for(int j = 0; j < messages.size(); j++) {
					JsonObject msg = messages.getJsonObject(j);
					messageList += msg.getString("Emisor") + "/" + msg.getString("Message") + "/";
				}
			}			
		}
		return messageList;
	}
	
	private String encodePassword(String password) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		
		byte[] byteData = md.digest();
		
		StringBuffer hexaString = new StringBuffer();
		for(int i = 0; i < byteData.length; i++) {
			String hexa = Integer.toHexString(0xff & byteData[i]);
			if(hexa.length() == 1) {
				hexaString.append("0");
			}
			hexaString.append(hexa);
		}
		
		return hexaString.toString();
	}
	
	private JsonArray readJsonFile() throws Exception {
		File jsonFile = new File("usuarios.json");
		InputStream IS = new FileInputStream(jsonFile);
		String friendsList = "";
		JsonReader reader = Json.createReader(IS);
		JsonArray array = reader.readArray();
		reader.close();
		return array;
	}
	
	private boolean friendAlreadyExists(String userName, String friend) throws Exception {		
		JsonArray array = readJsonFile();
		for(int i = 0; i < array.size(); i++) {
			JsonObject obj = array.getJsonObject(i);
			if(obj.getString("UserName").equalsIgnoreCase(userName)) {
				JsonArray friends = obj.getJsonArray("Friends");
				for(int j = 0; j < friends.size(); j++) {
					if(friends.getString(j).equalsIgnoreCase(friend)) {
						return true;
					}
				}
			}			
		}
		return false;
	}
	
	private boolean userNameExists(String userName) throws Exception {
		JsonArray array = readJsonFile();
		for(int i = 0; i < array.size(); i++) {
			JsonObject obj = array.getJsonObject(i);
			if(obj.getString("UserName").equalsIgnoreCase(userName)) {
				return true;
			}
		}
		return false;
	}
}
