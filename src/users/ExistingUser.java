package users;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.security.MessageDigest;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

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
	
	public String getExistingNickNames() throws Exception {
		File jsonFile = new File("usuarios.json");
		InputStream IS = new FileInputStream(jsonFile);
		String existingUsers = "";
		JsonReader reader = Json.createReader(IS);
		JsonArray array = reader.readArray();
		reader.close();
		for(int i = 0; i < array.size(); i++) {
			JsonObject obj = array.getJsonObject(i);
			existingUsers += obj.getString("UserName") + "/";
		}
		return existingUsers;
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
}
