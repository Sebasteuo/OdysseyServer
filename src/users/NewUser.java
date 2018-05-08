package users;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;

public class NewUser {
	OutputStream ots;
	private JsonObjectBuilder userBuilder;
	private JsonArrayBuilder musicalGenresBuilder;
	private JsonArrayBuilder friendsBuilder;	
	private JsonArrayBuilder arrayBuilder;
	private JsonArray users;
	private String userName;
	
	public NewUser() throws FileNotFoundException {
		userBuilder = Json.createObjectBuilder();
		musicalGenresBuilder = Json.createArrayBuilder();
		friendsBuilder = Json.createArrayBuilder();
		arrayBuilder = Json.createArrayBuilder();
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
		for(String i:musicalGenres) {
			musicalGenresBuilder.add(i);
		}
		userBuilder.add("MusicalGenres", musicalGenresBuilder);
	}
	
	public void setPassword(String password) {
		userBuilder.add("Password", password);
	}
	
	public void setFriends(String[] friends) {
		for(String i:friends) {
			friendsBuilder.add(i);			
		}
		userBuilder.add("Friends", friendsBuilder);
	}
	
	public JsonObject signInUser() throws IOException {
		try {
			FileReader fileReader = new FileReader("usuarios.json");
		
			if(fileReader.ready()){	
				InputStream IS = new FileInputStream(new File("usuarios.json"));			
				JsonReader reader = Json.createReader(IS);			
				JsonArray oldArray = reader.readArray();			
				reader.close();			
				for(JsonValue i:oldArray) {
					if(!i.asJsonObject().getString("UserName").equals(this.userName)) { //Autentica que el usuario no exista registrado
						arrayBuilder.add(i);
					}else {
						System.out.println("\nERROR: El usuario ya existe!");				
						return null;
					}
				}
			}
			fileReader.close();		
		}catch(Exception ex) {}
		
		ots =  new FileOutputStream("usuarios.json");
		arrayBuilder.add(userBuilder.build());
		users = arrayBuilder.build();
		JsonWriter jsonWriter = Json.createWriter(ots);
		jsonWriter.writeArray(users);
		jsonWriter.close();
		
		return userBuilder.build();
	}
}
