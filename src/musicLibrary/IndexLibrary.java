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
/**
 * Se encarga de iniciar los indices en los arboles, los indices de la biblioteca.
 * @author Sebastian Alba
 * @author David Pereira
 * @author Randall Mendez
 *LOS INDEX SON PARA LA BIBLIOTECA MUSICAL, COOPERAN EN LA BUSQUEDA DE CANCIONES
 */
public class IndexLibrary {
	private BTree titleIndex;
	private AVLTree artistIndex;
	private SplayTree albumIndex;
	private BinarySearchTree lyricsIndex;
	
	private String home = System.getProperty("user.home"); //Obtiene la ruta principal del sistema (C://user//xxxx//)
	private String folderPath = home + "\\Documents\\MusicLibrary\\"; //Ruta donde se almacenaran las canciones
	/**
	 * Constructor de la clase
	 */
	public IndexLibrary() {
		this.titleIndex = new BTree(16);
		this.artistIndex = new AVLTree();
		this.albumIndex = new SplayTree();
		this.lyricsIndex = new BinarySearchTree();
	}
	/**
	 * Crea el arbol B que contiene el indice de los titulos,
	 * @return
	 */
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
					String title = obj.getString("Title");
					this.titleIndex.insertNode(title);
				}
			}
			fileReader.close();
		}catch(Exception ex) {}

		return this.titleIndex;
	}
	/**
	 * Crea el arbol AVL que contiene el indice de los titulos
	 * @return
	 */
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
					this.artistIndex.insertNode( obj.toString(), artist+i);
				}
			}
			fileReader.close();
		}catch(Exception ex) {}

		return this.artistIndex;
	}
	/**
	 * Crea el arbol Splay que contiene el indice de los albunes
	 * @return
	 */
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
					this.albumIndex.insertNode(obj.toString(), album+i);
				}
			}
			fileReader.close();
		}catch(Exception ex) {}

		return this.albumIndex;
	}
	/**
	 * Crea el arbol binario que contiene el indice de las letras
	 * @return
	 */
	public BinarySearchTree createLyricsIndex() {
		try {	
			FileReader fileReader = new FileReader(folderPath + "davepj07\\MusicLibrary.json");
			if(fileReader.ready()) {
				InputStream IS = new FileInputStream(new File(folderPath + "davepj07\\MusicLibrary.json"));
				JsonReader reader = Json.createReader(IS);
				JsonArray array = reader.readArray();
				reader.close();
				for (int i = 0; i < array.size(); i++) {
					JsonObject obj = array.getJsonObject(i);
					String lyric = obj.getString("Lyrics");
					this.lyricsIndex.insertNode(obj.toString(), lyric);
				}
			}
			fileReader.close();
		}catch(Exception ex) {}

		return lyricsIndex;
	}
}

