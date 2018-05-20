package users;

import java.io.StringReader;
import java.security.MessageDigest;

import javax.json.Json;
import javax.json.JsonObject;

import treeStructure.BinarySearchTree;
import treeStructure.BinaryTreeNode;
/**
 * Clase encargada de definir un usuario existente en el registro
 * @author Sebastian Alba
 * @author David Pereira
 * @author Randall Mendez 
 */
public class ExistingUser {
	private String userName;
	private String password;
	private BinarySearchTree usersTree;
	/**
	 * Constructor de la clase
	 * @param userName
	 * @param password
	 * @param usersTree
	 */
	public ExistingUser(String userName, String password, BinarySearchTree usersTree) {
		this.userName = userName;
		this.password = password;
		this.usersTree = usersTree;
	}
	/**
	 * Se encarga de validar el inicio de sesion del usuario
	 * @return
	 * @throws Exception
	 */
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
	/**
	 * Se encarga de obtener el nickname de los usuario existentes en el registro
	 * @return string
	 * @throws Exception
	 */
	public String getExistingUserNames() throws Exception {
		String[] array = this.usersTree.inorder();
		String existingUsers = "";
		for(int i = 0; i < array.length; i++) {
			JsonObject obj = Json.createReader(new StringReader(array[i])).readObject();
			existingUsers += obj.getString("UserName") + "/";
		}
		return existingUsers;
	}
	/**
	 * Se encarga de codificar la contraseña  con protocolo Hash MD5
	 * Almacenamiento de contraseña
	Se guardará la contraseña en un documento JSON, el cual se accede
	mediante el árbol binario de búsqueda, se almacenará encriptada mediante el
	HASH MD5. Esto se hace en NewUser, recibe como parámetro el usuario dl nuevo 
	usuarios la contraseña, y toma la contraseña y la codifica con el método, 
	la retorna encriptada para almacenarla en el JSON, con editJsonDoc en el ExistingUser
	 * @param password
	 * @return string
	 * @throws Exception
	 */
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
