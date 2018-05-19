package users;

import java.io.StringReader;
import java.security.MessageDigest;

import javax.json.Json;
import javax.json.JsonObject;

import treeStructure.BinarySearchTree;
import treeStructure.BinaryTreeNode;

public class ExistingUser {
	private String userName;
	private String password;
	private BinarySearchTree usersTree;
	
	public ExistingUser(String userName, String password, BinarySearchTree usersTree) {
		this.userName = userName;
		this.password = password;
		this.usersTree = usersTree;
	}
	
	public boolean logIn() throws Exception{
		BinaryTreeNode userData = this.usersTree.searchNode(this.userName);
		if(userData != null) {																		   // Obtiene el valor del Nodo
			JsonObject object = Json.createReader(new StringReader(userData.getValue())).readObject(); // y lo parsea a un JsonObject
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

	public String getExistingUserNames() throws Exception {
		String[] array = this.usersTree.inorder();
		String existingUsers = "";
		for(int i = 0; i < array.length; i++) {
			JsonObject obj = Json.createReader(new StringReader(array[i])).readObject();
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
