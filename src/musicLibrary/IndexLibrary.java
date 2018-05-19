package musicLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import treeStructure.AVLTree;
import treeStructure.BTree;
import treeStructure.BinarySearchTree;
import treeStructure.SplayTree;

public class IndexLibrary {
	private BTree titleIndex;
	private AVLTree artistIndex;
	private SplayTree albumIndex;
	private BinarySearchTree lyricsIndex;
	
	private String home = System.getProperty("user.home"); //Obtiene la ruta principal del sistema (C://user//xxxx//)
	private String folderPath = home + "\\Documents\\MusicLibrary\\"; //Ruta donde se almacenaran las canciones
	
	public IndexLibrary() {
		this.titleIndex = new BTree(16);
		this.artistIndex = new AVLTree();
		this.albumIndex = new SplayTree();
		this.lyricsIndex = new BinarySearchTree();
	}
	
	public BTree createTitleIndex() {
		try {	
			FileReader fileReader = new FileReader(folderPath + "Principal\\MusicLibrary.json");
			if(fileReader.ready()) {
				InputStream IS = new FileInputStream(new File(folderPath + "Principal\\MusicLibrary.json"));
				JsonReader reader = Json.createReader(IS);
				JsonArray array = reader.readArray();
				reader.close();
				for (int i = 0; i < array.size(); i++) {
					JsonObject obj = array.getJsonObject(i);
					this.titleIndex.insertNode(obj.toString());
				}
			}
			fileReader.close();
		}catch(Exception ex) {}

		return this.titleIndex;
	}
	
	public AVLTree createArtistIndex() {
		try {	
			FileReader fileReader = new FileReader(folderPath + "Principal\\MusicLibrary.json");
			if(fileReader.ready()) {
				InputStream IS = new FileInputStream(new File(folderPath + "Principal\\MusicLibrary.json"));
				JsonReader reader = Json.createReader(IS);
				JsonArray array = reader.readArray();
				reader.close();
				for (int i = 0; i < array.size(); i++) {
					JsonObject obj = array.getJsonObject(i);
					String artist = obj.getString("Artist");
					System.out.println(artist);
					this.artistIndex.insertNode(obj.toString(), artist);
				}
			}
			fileReader.close();
		}catch(Exception ex) {}

		return this.artistIndex;
	}
	
	public SplayTree createAlbumIndex() {
		try {	
			FileReader fileReader = new FileReader(folderPath + "Principal\\MusicLibrary.json");
			if(fileReader.ready()) {
				InputStream IS = new FileInputStream(new File(folderPath + "Principal\\MusicLibrary.json"));
				JsonReader reader = Json.createReader(IS);
				JsonArray array = reader.readArray();
				reader.close();
				for (int i = 0; i < array.size(); i++) {
					JsonObject obj = array.getJsonObject(i);
					String album = obj.getString("Album");
					System.out.println(album);
					this.albumIndex.insertNode(obj.toString(), album);
				}
			}
			fileReader.close();
		}catch(Exception ex) {}

		return this.albumIndex;
	}
	
	public BinarySearchTree createLyricsIndex() {
				
		
		return lyricsIndex;
	}
}

