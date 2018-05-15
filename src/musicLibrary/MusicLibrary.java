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
import java.nio.file.Files;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;

public class MusicLibrary {
	JsonObjectBuilder objBuilder;
	JsonArrayBuilder arrBuilder;
	JsonArray finalArray;
	
	String home = System.getProperty("user.home"); //Obtiene la ruta principal del sistema (C://user//xxxx//)
	String folderPath = home + "\\Documents\\MusicLibrary\\"; //Ruta donde se almacenaran las canciones
	
	public MusicLibrary() throws Exception {
		this.objBuilder = Json.createObjectBuilder();
		this.arrBuilder = Json.createArrayBuilder();
		
		File folder = new File(folderPath + "Principal");
		if(!folder.exists()) { //Crea la carpeta en caso de que no exista
			folder.mkdirs();
		}
	}
	
	public void storeSong(String songName, String url, String userName) throws Exception{	
		URLConnection connection = new URL(url).openConnection();
		File song = new File(folderPath + "Principal\\" + songName + ".mp3"); //Crea el archivo donde se guardara la cancion
		File userSong = new File(folderPath + userName + "\\" + songName + ".mp3");
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
			Files.copy(song.toPath(), userSong.toPath()); //Copia la cancion en la biblioteca especifica del usuario
			this.saveMetadata(song, userSong, userName); //Llama al metodo para guardar la metadata de la cancion
		}else {
			System.out.println("ERROR: La cancion ya se encuentra en la biblioteca.");
			return;
		}		
	}
	
	public void updateMetadata(File song, String userName, String[] metadata) throws Exception {
		Mp3File mp3File = new Mp3File(song);
		ID3v2 tag = mp3File.getId3v2Tag();
		String oldTitle = null;
		boolean change = false;
		
		if(!tag.getTitle().equalsIgnoreCase(metadata[0])) {
			oldTitle = tag.getTitle();
			tag.setTitle(metadata[0]);	
			change = true;
		}
		if(!tag.getArtist().equalsIgnoreCase(metadata[1])) {
			tag.setArtist(metadata[1]);
			change = true;
		}
		if(!tag.getComposer().equalsIgnoreCase(metadata[2])) {
			tag.setComposer(metadata[2]);
			change = true;
		}
		if(!tag.getGenreDescription().equalsIgnoreCase(metadata[3])) {
			tag.setGenreDescription(metadata[3]);
			change = true;
		}
		if(!tag.getAlbum().equalsIgnoreCase(metadata[4])) {
			tag.setAlbum(metadata[4]);
			change = true;
		}
		
		if(change == true) {
			tag.setPadding(true);
			mp3File.save(mp3File.getFilename() + ".retag");
			renameFiles(mp3File);
			if(oldTitle != null) {
				editJsonDoc(oldTitle, tag, userName);
				mp3File.save(folderPath + userName + "\\" + metadata[0] + ".mp3");
				Files.deleteIfExists(song.toPath());
			}else {
				editJsonDoc(tag.getTitle(), tag, userName);
			}
		}
	}
	
	public void printMetadata(File song) throws Exception {		
		Mp3File file = new Mp3File(song);
		ID3v2 tag = file.getId3v2Tag();
    	System.out.println("Title: " + tag.getTitle());
    	System.out.println("Artist: " + tag.getArtist());
    	System.out.println("Composer: " + tag.getComposer());
    	System.out.println("Album: " + tag.getAlbum());
    	System.out.println("Genre: " + tag.getGenre() + " (" + tag.getGenreDescription() + ")");
	}
	
	private void saveMetadata(File song, File userSong, String userName) throws Exception {
		Mp3File mp3File = new Mp3File(song);
		
		ID3v2 tag;
		if(mp3File.hasId3v2Tag()) {
			tag = mp3File.getId3v2Tag();
		}else {
			tag = new ID3v24Tag();
			mp3File.setId3v2Tag(tag);
		}
		
		try {
			FileReader fileReader = new FileReader(folderPath + "Principal\\" + "MusicLibrary.json");
			if(fileReader.ready()) {
				InputStream tempIS = new FileInputStream(new File(folderPath + "Principal\\" + "MusicLibrary.json"));			
				JsonReader reader = Json.createReader(tempIS);			
				JsonArray oldArray = reader.readArray();			
				reader.close();			
				for(JsonValue i:oldArray) {
					arrBuilder.add(i);
				}
			}
			fileReader.close();
		}catch(FileNotFoundException ex) {}
		
		boolean change = false;
		try{
			objBuilder.add("Title", tag.getTitle()).toString();
		}catch(NullPointerException ex) {
			objBuilder.add("Title", "Unknown");
			tag.setTitle("Unknown");
			change = true;
		}
		try{
			objBuilder.add("Artist", tag.getArtist()).toString();
		}catch(NullPointerException ex) {
			objBuilder.add("Artist", "Unknown");
			tag.setArtist("Unknown");
			change = true;
		}
		try{
			objBuilder.add("Composer", tag.getComposer()).toString();
		}catch(NullPointerException ex) {
			objBuilder.add("Composer", "Unknown");
			tag.setComposer("Unknown");
			change = true;
		}
		try{
			objBuilder.add("Genre", tag.getGenreDescription()).toString();
		}catch(NullPointerException ex) {
			objBuilder.add("Genre", "Unknown");
			tag.setGenreDescription("Rock");
			change = true;		
		}
		try{
			objBuilder.add("Album", tag.getAlbum()).toString();
		}catch(NullPointerException ex) {
			objBuilder.add("Album", "Unknown");
			tag.setAlbum("Unknown");
			change = true;
		}
		
		if(change) {
			tag.setPadding(true);
			mp3File.save(mp3File.getFilename() + ".retag");
			renameFiles(mp3File);
			Files.deleteIfExists(userSong.toPath());
			Files.copy(song.toPath(), userSong.toPath());
		}
		
		OutputStream OS = new FileOutputStream(folderPath + "Principal\\" + "MusicLibrary.json");
		JsonObject obj = objBuilder.build();
		arrBuilder.add(obj);
		finalArray = arrBuilder.build();
		JsonWriter writer = Json.createWriter(OS);
		writer.writeArray(finalArray);
		writer.close();
		
		//Copia el archivo json al directorio del usuario 
		File newFile = new File(folderPath + userName + "\\" + "MusicLibrary.json");
		File file = new File(folderPath + "Principal\\" + "MusicLibrary.json");
		Files.copy(file.toPath(), newFile.toPath());
	}
	
	private void editJsonDoc(String title, ID3v2 tag, String userName) throws Exception {
		try {
			FileReader fileReader = new FileReader(folderPath + userName + "\\" + "MusicLibrary.json");
			if(fileReader.ready()) {
				InputStream tempIS = new FileInputStream(new File(folderPath + userName + "\\" + "MusicLibrary.json"));			
				JsonReader reader = Json.createReader(tempIS);			
				JsonArray array = reader.readArray();			
				reader.close();
				for(int i = 0; i < array.size(); i++) {
					JsonObject obj = array.getJsonObject(i);
					if(obj.getString("Title").equalsIgnoreCase(title)) {
						objBuilder.add("Title", tag.getTitle());
						objBuilder.add("Artist", tag.getArtist());
						objBuilder.add("Composer", tag.getComposer());
						objBuilder.add("Genre", tag.getGenre());
						objBuilder.add("Album", tag.getAlbum());
						JsonObject newObj = objBuilder.build();
						
						for(int j = 0; i < array.size(); i++) {
							if(j == i) {
								arrBuilder.add(newObj);
							}else {
								arrBuilder.add(array.get(j));
							}
						}
						finalArray = arrBuilder.build();
						OutputStream tempOS = new FileOutputStream(new File(folderPath + userName + "\\" + "MusicLibrary.json"));
						JsonWriter writer = Json.createWriter(tempOS);
						writer.writeArray(finalArray);
						writer.close();
						break;
					}	
				}
				
			}
			fileReader.close();
		}catch(FileNotFoundException ex) {}
	}
	
	private void renameFiles(Mp3File mp3File) {
		File originalFile = new File(mp3File.getFilename());
		File backupFile = new File(mp3File.getFilename() + ".bak");
		File retaggedFile = new File(mp3File.getFilename() + ".retag");
		
		originalFile.renameTo(backupFile);
		retaggedFile.renameTo(originalFile);
		backupFile.delete();
	}
}
