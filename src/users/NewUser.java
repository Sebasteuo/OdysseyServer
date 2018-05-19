package users;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import treeStructure.BinarySearchTree;

public class NewUser {
	private BinarySearchTree usersTree;
	private OutputStream ots;
	private JsonObjectBuilder userBuilder;
	private JsonArrayBuilder musicalGenresBuilder;
	private JsonArrayBuilder friendsBuilder;
	private JsonArrayBuilder arrayBuilder;
	private JsonArray users;
	private String userName;

	public NewUser(BinarySearchTree usersTree) throws FileNotFoundException {
		this.usersTree = usersTree;
		this.userBuilder = Json.createObjectBuilder();
		this.musicalGenresBuilder = Json.createArrayBuilder();
		this.friendsBuilder = Json.createArrayBuilder();
		this.arrayBuilder = Json.createArrayBuilder();
	}

	public void setUserName(String userName) {
		this.userName = userName;
		userBuilder.add("UserName", userName);
	}

	public void setName(String name) {
		userBuilder.add("Name", name);
	}

	public void setAge(int age) {
		userBuilder.add("Age", age);
	}

	public void setMusicalGenres(String[] musicalGenres) {
		for (String i : musicalGenres) {
			if (i != null) {
				musicalGenresBuilder.add(i);
			}
		}
		userBuilder.add("MusicalGenres", musicalGenresBuilder);
	}

	public void setPassword(String password) throws Exception {
		password = encodePassword(password);
		userBuilder.add("Password", password);
	}

	public void setFriends(String[] friends) {
		for (String i : friends) {
			if (i != null) {
				friendsBuilder.add(i);
			}
		}
		userBuilder.add("Friends", friendsBuilder);
	}

	public JsonObject signInUser() throws IOException {
		if(this.usersTree.searchNode(this.userName) == null){ //Valida que el usuario no se encuentre dentro del arbol de registro
			userBuilder.add("Messages", Json.createArrayBuilder().build());
			
			try {
				FileReader fileReader = new FileReader("usuarios.json");
				if (fileReader.ready()) {
					InputStream IS = new FileInputStream(new File("usuarios.json"));
					JsonReader reader = Json.createReader(IS);
					JsonArray oldArray = reader.readArray();
					reader.close();
					for (JsonValue i : oldArray) {
						arrayBuilder.add(i);
					}
				}
				fileReader.close();
			} catch (Exception ex) {}
	
			ots = new FileOutputStream("usuarios.json");
			JsonObject user = userBuilder.build();
			arrayBuilder.add(user);
			users = arrayBuilder.build();
			JsonWriter jsonWriter = Json.createWriter(ots);
			jsonWriter.writeArray(users);
			jsonWriter.close();
	
			return user;
		}else {
			System.out.println("ERROR: El usuario ya existe!");
			return null;
		}
		
	}

	private String encodePassword(String password) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());

		byte[] byteData = md.digest();

		StringBuffer hexaString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			String hexa = Integer.toHexString(0xff & byteData[i]);
			if (hexa.length() == 1) {
				hexaString.append("0");
			}
			hexaString.append(hexa);
		}

		return hexaString.toString();
	}
}
