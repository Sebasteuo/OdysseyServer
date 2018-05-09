package users;

import java.io.StringReader;
import java.security.MessageDigest;

import javax.json.Json;
import javax.json.JsonObject;

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
	
	public String encodePassword(String password) throws Exception {
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
