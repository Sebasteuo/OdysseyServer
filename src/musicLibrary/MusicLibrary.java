package musicLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

public class MusicLibrary {
	URLConnection connection;
	String songName;
	JsonObjectBuilder objBuilder;
	JsonArrayBuilder arrBuilder;
	JsonArray finalArray;
	
	
	public MusicLibrary(String songName, String url) throws Exception {		
		this.songName = songName;
		this.connection = new URL(url).openConnection();
		this.objBuilder = Json.createObjectBuilder();
		this.arrBuilder = Json.createArrayBuilder();
	}
	
	public void storeSong() throws Exception{
		String home = System.getProperty("user.home");
		File file = new File(home + "\\Documents\\MusicLibrary\\" + this.songName + ".mp3");
		if(!file.exists()) {
			InputStream IS = this.connection.getInputStream(); //Obtiene los datos recibidos por el url
			OutputStream OS = new FileOutputStream(file); //Crea el archivo para escribir la informacion
			
			byte[] buffer = new byte[4096]; 
			int length;
			while((length = IS.read(buffer)) > 0) {
				OS.write(buffer, 0, length); //Escribe los datos en el archivo mp3
			}
			System.out.println("Download Complete!");
			OS.close(); //Cierra el archivo 
			this.saveMetadata(file, home);
		}else {
			System.out.println("ERROR: La cancion ya se encuentra en la biblioteca.");
			return;
		}		
	}
	
	public void saveMetadata(File file, String home) throws Exception {
		InputStream IS = new FileInputStream(file);
		ContentHandler handler = new DefaultHandler();
		Metadata metadata = new Metadata();
		Parser parser = new Mp3Parser();
		ParseContext parseCtx = new ParseContext();
		parser.parse(IS, handler, metadata, parseCtx);
		IS.close();
		
		try {
			FileReader fileReader = new FileReader(home + "\\Documents\\MusicLibrary\\MusicLibrary.json");
			if(fileReader.ready()) {
				InputStream tempIS = new FileInputStream(new File(home + "\\Documents\\MusicLibrary\\MusicLibrary.json"));			
				JsonReader reader = Json.createReader(tempIS);			
				JsonArray oldArray = reader.readArray();			
				reader.close();			
				for(JsonValue i:oldArray) {
					arrBuilder.add(i);
				}
			}
			fileReader.close();
		}catch(FileNotFoundException ex) {}
				
		try{objBuilder.add("Title", metadata.get("title")).toString();}catch(NullPointerException ex) {objBuilder.add("Title", "Unknown");}
		try{objBuilder.add("Artist", metadata.get("xmpDM:artist")).toString();}catch(NullPointerException ex) {objBuilder.add("Artist", "Unknown");}
		try{objBuilder.add("Composer", metadata.get("xmpDM:composer")).toString();}catch(NullPointerException ex) {objBuilder.add("Composer", "Unknown");}
		try{objBuilder.add("Genre", metadata.get("xmpDM:genre")).toString();}catch(NullPointerException ex) {objBuilder.add("Genre", "Unknown");}
		try{objBuilder.add("Album", metadata.get("xmpDM:album")).toString();}catch(NullPointerException ex) {objBuilder.add("Album", "Unknown");}
		
		OutputStream OS = new FileOutputStream(home +"\\Documents\\MusicLibrary\\MusicLibrary.json");
		JsonObject obj = objBuilder.build();
		arrBuilder.add(obj);
		finalArray = arrBuilder.build();
		JsonWriter writer = Json.createWriter(OS);
		writer.writeArray(finalArray);
		writer.close();
	}
}
