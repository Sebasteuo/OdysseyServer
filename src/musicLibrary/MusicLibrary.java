package musicLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Scanner;

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
	private JsonObjectBuilder objBuilder;
	private JsonArrayBuilder arrBuilder;
	private JsonArray finalArray;
	
	private String home = System.getProperty("user.home"); //Obtiene la ruta principal del sistema (C://user//xxxx//)
	private String folderPath = home + "\\Documents\\MusicLibrary\\"; //Ruta donde se almacenaran las canciones
	
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
	
	@SuppressWarnings("resource")
	public String storeSong(Socket socket, String userName) throws Exception{	
		File song;
		ObjectOutputStream OOS = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream OIS = new ObjectInputStream(socket.getInputStream());
        FileOutputStream FOS = null;
        byte[] buffer = new byte[100];
        
        // Lee el nombre del archivo
        Object obj = OIS.readObject();
        if (obj instanceof String) {
        	song = new File(folderPath + "Principal\\" + obj.toString() + ".mp3");
            FOS = new FileOutputStream(song);
        } else {
            return "false";
        }
        
        // Lee el contenido del archivo.
        Integer bytesRead = 0;
        do {
            obj = OIS.readObject();
            if (!(obj instanceof Integer)) {
                return "false";
            }
            bytesRead = (Integer) obj;
            obj = OIS.readObject();
            if (!(obj instanceof byte[])) {
                return "false";
            }
            buffer = (byte[]) obj;
 
            //Escribe los datos en el archivo a guardar
            FOS.write(buffer, 0, bytesRead);
        } while (bytesRead == 100);
        
        FOS.close();
        OIS.close();
        OOS.close();
        
        File userSong = new File(folderPath + userName + "\\" + obj.toString() + ".mp3");
        Files.copy(song.toPath(), userSong.toPath());
        this.saveMetadata(song, userSong, userName); //Llama al metodo para guardar la metadata de la cancion
        return "true";
	}
	
	public void updateMetadata(File song, String userName, String[] metadata) throws Exception, IllegalArgumentException {
		Mp3File mp3File = new Mp3File(song);
		ID3v2 tag = mp3File.getId3v2Tag();
		String oldTitle = null;
		
		boolean change = false;
		if(!tag.getTitle().equalsIgnoreCase(metadata[0]) && metadata[0] != "") {
			oldTitle = tag.getTitle();
			tag.setTitle(metadata[0]);	
			change = true;
		}
		if(!tag.getArtist().equalsIgnoreCase(metadata[1]) && metadata[1] != "") {
			tag.setArtist(metadata[1]);
			change = true;
		}
		if(!tag.getGenreDescription().equalsIgnoreCase(metadata[2]) && metadata[2] != "") {
			tag.setGenreDescription(metadata[2]);
			change = true;
		}
		if((tag.getAlbum() == null || !tag.getAlbum().equalsIgnoreCase(metadata[3])) && metadata[3] != "") {
			tag.setAlbum(metadata[3]);
			change = true;
		}
		if(!tag.getYear().equalsIgnoreCase(metadata[4]) && metadata[4] != "") {
			tag.setYear(metadata[4]);
			change = true;
		}
		if(!tag.getLyrics().equalsIgnoreCase(metadata[5]) && metadata[5] != "") {
			tag.setLyrics(metadata[5]);
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
	
	@SuppressWarnings("resource")
	public void syncMetadata(File song, String userName) throws Exception {
		if(song.exists()) {	
			Mp3File mp3 = new Mp3File(song);
			ID3v2 tag = mp3.getId3v2Tag();
			
			//Realiza la conexion al proveedor
			String titleTag = tag.getTitle().replaceAll(" ", "%20");
			String artistTag = tag.getArtist().replaceAll(" ", "%20");
			String request = "http://api.musixmatch.com/ws/1.1/track.search?apikey=0a181cdc1ba6d0a216b779cf8ad585df&q_track=" + titleTag +"&q_artist=" + artistTag + "&page_size=1&s_track_rating=desc";
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			InputStream IS = connection.getInputStream();	//
			Scanner s = new Scanner(IS).useDelimiter("\\A");// Obtiene la respuesta del proveedor y la convierte a un String
			String result = s.hasNext() ? s.next() : "";	//
			
			JsonReader jsonReader = Json.createReader(new StringReader(result)); // 
			JsonObject jsonObj = jsonReader.readObject();						 // Parsea el string a JsonObject		
			jsonReader.close();													 //
			
			JsonObject track = (JsonObject) jsonObj.get("message").asJsonObject().get("body").asJsonObject().get("track_list").asJsonArray().get(0).asJsonObject().get("track");
			int trackID = track.getInt("track_id");
			String trackName, artist, genre, album, year, lyrics;
			try{
				trackName = track.getString("track_name");
			}catch(Exception ex){
				trackName = tag.getTitle();
			}
			try {
				artist = track.getString("artist_name");
			}catch(Exception ex) {
				artist = tag.getArtist();
			}
			try {
				JsonArray genreList = (JsonArray) track.get("primary_genres").asJsonObject().get("music_genre_list");
				genre = genreList.get(0).asJsonObject().get("music_genre").asJsonObject().getString("music_genre_name");
				tag.setGenreDescription(genre);
			}catch(Exception ex) {
				genre = tag.getGenreDescription();
			}
			try {
				album = track.getString("album_name");
			}catch(Exception ex) {
				if(tag.getAlbum() != null) {
					album = tag.getAlbum();
				}else {
					album = tag.getTitle();
				}
			}
			try {
				year = track.getString("first_release_date");
				year = year.substring(0, 4);
			}catch(Exception ex) {
				year = tag.getYear();
			}
			try {
				lyrics = getSongLyrics(trackID);
			}catch(Exception ex) {
				lyrics = "Unknown";
			}
			
			System.out.println("Title: " + trackName + "\nArtist: " + artist + "\nGenre: " + genre + "\nAlbum: " + album + "\nYear: " + year + "\nLyrics: " + lyrics);
			
			String[] arr = {trackName, artist, genre, album, year, lyrics};
			updateMetadata(song, userName, arr);
		}else {
			System.out.println("ERROR: La cancion no se encuentra en la biblioteca.");
		}
	}
	
	public void deleteSong(String songTitle, String userName) throws Exception{
		File file = new File(folderPath + userName + "\\" + songTitle + ".mp3");
		if(file.exists()) {
			Files.delete(file.toPath());
		}else {
			System.out.println("La cancion no se encuentra en la biblioteca.");
		}
	}
	
	public String getUserLibrary(String userName) throws Exception {
		String songs = "";
		FileReader fileReader = new FileReader(folderPath + userName + "\\" + "MusicLibrary.json");
		if(fileReader.ready()) {
			InputStream tempIS = new FileInputStream(new File(folderPath + userName + "\\" + "MusicLibrary.json"));			
			JsonReader reader = Json.createReader(tempIS);			
			JsonArray array = reader.readArray();			
			reader.close();
			for(int i = 0; i < array.size(); i++) {
				JsonObject obj = array.getJsonObject(i);
				songs += obj.getString("Title") + "/";
			}
		}
		fileReader.close();
		System.out.println(songs);
		return songs;
	}
	
	public void printMetadata(File song) throws Exception {		
		Mp3File file = new Mp3File(song);
		ID3v2 tag = file.getId3v2Tag();
    	System.out.println("Title: " + tag.getTitle());
    	System.out.println("Artist: " + tag.getArtist());
    	System.out.println("Genre: " + tag.getGenre() + " (" + tag.getGenreDescription() + ")");
    	System.out.println("Album: " + tag.getAlbum());
    	System.out.println("Year: " + tag.getYear());
    	System.out.println("Lyrics: " + tag.getLyrics());
	}
	
	@SuppressWarnings("resource")
	private String getSongLyrics (int trackID) throws Exception {
		String request = "http://api.musixmatch.com/ws/1.1/track.lyrics.get?track_id=" + trackID + "&apikey=0a181cdc1ba6d0a216b779cf8ad585df";
		URL url = new URL(request);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		InputStream IS = connection.getInputStream();	//
		Scanner s = new Scanner(IS).useDelimiter("\\A");// Obtiene la respuesta del proveedor y la convierte a un String
		String result = s.hasNext() ? s.next() : "";	//
		
		JsonReader jsonReader = Json.createReader(new StringReader(result)); // 
		JsonObject jsonObj = jsonReader.readObject();						 // Parsea el string a JsonObject		
		jsonReader.close();													 //
		
		String lyrics = jsonObj.get("message").asJsonObject().get("body").asJsonObject().get("lyrics").asJsonObject().getString("lyrics_body");
		return lyrics;
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
			objBuilder.add("Title", "Unavailable");
			tag.setTitle("Unavailable");
			change = true;
		}
		try{
			objBuilder.add("Artist", tag.getArtist()).toString();
		}catch(NullPointerException ex) {
			objBuilder.add("Artist", "Unavailable");
			tag.setArtist("Unavailable");
			change = true;
		}
		try{
			objBuilder.add("Genre", tag.getGenreDescription()).toString();
		}catch(NullPointerException ex) {
			objBuilder.add("Genre", "Other");
			tag.setGenreDescription("Other");
			change = true;		
		}
		try{
			objBuilder.add("Album", tag.getAlbum()).toString();
		}catch(NullPointerException ex) {
			objBuilder.add("Album", "Unavailable");
			tag.setAlbum("Unavailable");
			change = true;
		}
		try{
			objBuilder.add("Year", tag.getYear()).toString();
		}catch(NullPointerException ex) {
			objBuilder.add("Year", "Unavailable");
			tag.setYear("Unavailable");
			change = true;
		}
		try{
			objBuilder.add("Lyrics", tag.getLyrics()).toString();
		}catch(NullPointerException ex) {
			objBuilder.add("Lyrics", "Unavailable");
			tag.setLyrics("Unavailable");
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
		Files.deleteIfExists(newFile.toPath());
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
						objBuilder.add("Genre", tag.getGenreDescription());
						objBuilder.add("Album", tag.getAlbum());
						objBuilder.add("Year", tag.getYear());
						objBuilder.add("Lyrics", tag.getLyrics());
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
