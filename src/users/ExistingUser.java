package users;

import java.io.StringReader;

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
	
	public boolean logIn(BinarySearchTree users) {
		if(users.searchNode(this.userName) != null) {
			JsonObject object = Json.createReader(new StringReader(users.searchNode(this.userName).getValue())).readObject();
			if(object.getString("Password").equalsIgnoreCase(this.password)) {
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
}
