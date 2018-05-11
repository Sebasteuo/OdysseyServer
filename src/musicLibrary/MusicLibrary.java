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
	JsonObjectBuilder objBuilder;
	JsonArrayBuilder arrBuilder;
	JsonArray finalArray;
	
	File folder; //Carpeta donde se almacenaran las canciones
	String home = System.getProperty("user.home"); //Obtiene la ruta principal del sistema (C://user//xxxx//)
	String jsonDocPath = home + "\\Documents\\MusicLibrary\\MusicLibrary.json"; //Ruta de acceso al JsonDoc
	
	public MusicLibrary() throws Exception {
		this.objBuilder = Json.createObjectBuilder();
		this.arrBuilder = Json.createArrayBuilder();
		
		this.folder = new File(home + "\\Documents\\MusicLibrary");
		if(!this.folder.exists()) { //Crea la carpeta en caso de que no exista
			this.folder.mkdirs();
		}
	}
	
	public void storeSong(String songName, String url) throws Exception{	
		URLConnection connection = new URL(url).openConnection();
		File song = new File(home + "\\Documents\\MusicLibrary\\" + songName + ".mp3"); //Crea el archivo donde se guardara la cancion
		if(!song.exists()) { //Comprueba si la cancion ya existe en el directorio
			InputStream IS = connection.getInputStream(); //Obtiene los datos recibidos por el url
			OutputStream OS = new FileOutputStream(song); //Convierte el archivo en editable para escribir la informacion
			
			byte[] buffer = new byte[4096]; 
			int length;
			while((length = IS.read(buffer)) > 0) {
				OS.write(buffer, 0, length); //Escribe los datos en el archivo mp3
			}
			System.out.println("Download Complete!");
			OS.close(); //Cierra el archivo 
			this.saveMetadata(song); //Llama al metodo para guardar la metadata de la cancion
		}else {
			System.out.println("ERROR: La cancion ya se encuentra en la biblioteca.");
			return;
		}		
	}
	
	public void updateMetadata() {
		
	}
	
	private void saveMetadata(File song) throws Exception {
		InputStream IS = new FileInputStream(song);
		ContentHandler handler = new DefaultHandler();
		Metadata metadata = new Metadata();
		Parser parser = new Mp3Parser();
		ParseContext parseCtx = new ParseContext();
		parser.parse(IS, handler, metadata, parseCtx);
		IS.close();
		try {
			FileReader fileReader = new FileReader(jsonDocPath);
			if(fileReader.ready()) {
				InputStream tempIS = new FileInputStream(new File(jsonDocPath));			
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
		
		OutputStream OS = new FileOutputStream(jsonDocPath);
		JsonObject obj = objBuilder.build();
		arrBuilder.add(obj);
		finalArray = arrBuilder.build();
		JsonWriter writer = Json.createWriter(OS);
		writer.writeArray(finalArray);
		writer.close();
	}
}
