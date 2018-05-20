import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;
import javax.json.JsonObject;

import musicLibrary.MusicLibrary;
import social.Friends;
import social.Recommendations;
import users.ExistingUser;

import treeStructure.BinarySearchTree;
import users.NewUser;
/**
 * Se encarga de la conexion con el cliente.
 * @author Sebastian Alba
 * @author David Pereira
 * @author Randall Mendez
 *
 */
public class Sockets {
	@SuppressWarnings("resource")
	/**
	 * Se deben crear los sockets correspondientes para cada requerimiento.(Con
	Randall) con el metodo .conectar, recibe un numero y varios strings, en este caso son máximo 6 parámetros, por nombre dude usuario,
 	contraseña (Al crear usuarios).Inicia el TCP Client con el local host y el work que es el entero que recibe, va a decir que va 
 	enviar y que va hacer con lo que recibe. Hay 23 Sockets, uno para cada funcionalidad, el 13 es el que tiene los treats, 
 	este enviado la canción, el archivo al servidor.Obtiene los bytes de un archivo y los convierte en storing de base 64, 
 	luego ese storing lo codifica en UTF8. Y lo envía por treats para que no se congele la app
	 * Inicia la conexion y cierra la conexion, y llama las operaciones del servidor.
	 * @param users , es el arbol binario que contiene el registro de los usuarios
	 * @throws Exception, para evitar que se caigan lo sockets
	 */
	public static void conectar(BinarySearchTree users) throws Exception {
		ServerSocket serversocket = new ServerSocket(3000);
		System.out.println("Listo");
		while (true) {
			Socket client = serversocket.accept();
			Scanner scanner = new Scanner(client.getInputStream());
			PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
			String name = scanner.nextLine();
			if (name.substring(0, 1).equals("0")) {
				ExistingUser a = new ExistingUser("", "", Main.loadFileInBST());
				String b = a.getExistingUserNames();
				String xml = "<b>" + b + "</b>";
				pw.println(xml);
			}
			if (name.substring(0, 2).equals("10")) {
				if ("hola".equals("hola")) {
					String xml = "<true> Apodo libre </true>";
					pw.println(xml);
					int finick = 0;
					int finame = 0;
					int finedad = 0;
					int fingenero = 0;
					int fincontra = 0;
					int j = 4;
					int x = 0;
					String[] genero = new String[10];
					String[] amigo = new String[10];
					for (int i = 3; name.length() > i; i++) {
						if (name.substring(i, j).equals("/")) {
							if (x == 0) {
								finick = j;
								x++;
							} else if (x == 1) {
								finame = j;
								x++;
							} else if (x == 2) {
								finedad = j;
								x++;
							} else if (x == 3) {
								fingenero = j;
								x++;
								int v = finedad + 1;
								int z = 0;
								int m = finedad;
								for (int c = 0; name.substring(finedad, fingenero - 1).length() >= c; c++) {
									if (name.substring(v - 1, v).equals(",")) {
										genero[z] = name.substring(m, v - 1);
										m = c + finedad + 1;
										z++;
									}
									v++;
								}
							} else {
								fincontra = j;
								x++;
								int v = fingenero + 1;
								int z = 0;
								int m = fincontra;
								for (int c = 0; name.substring(fingenero, name.length()).length() > c; c++) {
									if (name.substring(v - 1, v).equals(",")) {
										amigo[z] = name.substring(m, v - 1);
										m = c + fingenero + 1;
										z++;
									}
									v++;
								}
							}
						}
						j++;
					}
					NewUser usuario = new NewUser(users);
					usuario.setUserName(name.substring(2, finick - 1));
					usuario.setName(name.substring(finick, finame - 1));
					usuario.setAge(Integer.parseInt(name.substring(finame, finedad - 1)));
					usuario.setMusicalGenres(genero);
					usuario.setPassword(name.substring(fingenero, fincontra - 1));
					usuario.setFriends(amigo);
					JsonObject toInsert = usuario.signInUser();
					System.out.println(toInsert.toString());
				} else {
					pw.println("<false> Apodo utilizado </false>");
				}
			}
			if (name.substring(0, 2).equals("12")) {
				String nick = "";
				String pass = "";
				for (int i = 3; name.length() > i; i++) {
					if (name.substring(i, i + 1).equals("/")) {
						nick = name.substring(3, i);
						pass = name.substring(i + 1, name.length());
					}
				}
				ExistingUser user = new ExistingUser(nick, pass, users);
				boolean validacion = user.logIn();
				if (validacion) {
					String xml = "<true> Apodo libre </true>";
					pw.println(xml);
				} else {
					String xml = "<false> Apodo libre </false>";
					pw.println(xml);
				}
			}
			if (name.substring(0, 2).equals("17")) {
				String xml = "<true> Se ha enviado el mensaje </true>";
				pw.println(xml);
				Recommendations a = new Recommendations(users);
				String[] message = new String[3];
				int z = 0;
				int x = 3;
				for (int i = 3; name.length() > i; i++) {
					if (name.substring(i, i + 1).equals("/")) {
						message[z] = name.substring(x, i);
						x = i + 1;
						z++;
					}

				}
				message[z] = name.substring(x, name.length());
				a.addMessages(message);
			}
			if (name.substring(0, 2).equals("18")) {
				Recommendations a = new Recommendations(users);
				String xml = "<true>" + a.getMessagesList(name.substring(3, name.length())) + "</true>";
				pw.println(xml);
			}
			if (name.substring(0, 2).equals("19")) {
				Friends a = new Friends(users);
				String xml = "<true>" + a.getFriendsList(name.substring(3, name.length())) + "</true>";
				pw.println(xml);
			}
			if (name.substring(0, 2).equals("20")) {
				MusicLibrary ml = new MusicLibrary();
				String xml = "";
				if (name.substring(2, 3).equals("1")) {
					xml = "<true>" + ml.sortLibraryByTitle(name.substring(4)) + "</true>";
				}
				if (name.substring(2, 3).equals("2")) {
					xml = "<true>" + ml.sortLibraryByArtist(name.substring(4)) + "</true>";
				}
				if (name.substring(2, 3).equals("3")) {
					xml = "<true>" + ml.sortLibraryByAlbum(name.substring(4)) + "</true>";
				}
				System.out.println(xml);
				pw.println(xml);
			}
			if (name.substring(0, 2).equals("22")) {
				MusicLibrary ml = new MusicLibrary();
				String xml = "";
				if (name.substring(2, 3).equals("1")) {
					System.out.println("nombre");
					xml = "<true>" + ml.searchByTitle(name.substring(4, name.length()-2)) + "</true>";
				}
				if (name.substring(2, 3).equals("2")) {
					System.out.println("artista");
					xml = "<true>" + ml.searchByArtist(name.substring(4, name.length()-2)) + "</true>";
				}
				if (name.substring(2, 3).equals("3")) {
					System.out.println("album");
					xml = "<true>" + ml.searchByAlbum(name.substring(4, name.length()-2)) + "</true>";
				}
				if (name.substring(2, 3).equals("4")) {
					System.out.println("letra");
					xml = "<true>" + ml.searchByLyrics(name.substring(4, name.length()-2)) + "</true>";
				}
				System.out.println(xml);
				pw.println(xml);
			}
			if ((name.substring(0, 2)).equals("23")) {
				String[] friend = new String[3];
				int z = 0;
				int x = 3;
				for (int i = 3; name.length() > i; i++) {
					if (name.substring(i, i + 1).equals("/")) {
						friend[z] = name.substring(x, i);
						x = i + 1;
						z++;
					}
				}
				friend[z] = name.substring(x, name.length());
				Friends a = new Friends(users);
				String validador = a.addFriends(friend);
				String xml = "<" + validador + "> Se ha enviado el mensaje </" + validador + ">";
				pw.println(xml);
			}
			if ((name.substring(0, 2)).equals("13")) {
				int u = 0;
				int p = 0;
				int g = 0;
				for (int q = 3; q < name.length(); q++) {
					if (name.substring(q, q + 1).equals("/")) {
						if (g == 0) {
							u = q;
							g++;
						} else {
							p = q;
							break;
						}
					}
				}
				String nick = name.substring(2, u);
				String cancion = name.substring(u + 1, p);
				byte[] buf = Base64.getDecoder().decode(name.substring(p + 1, name.length() - 2));
				MusicLibrary ml = new MusicLibrary();
				ml.storeSong(cancion, buf, nick);
			}
			if((name.substring(0, 2)).equals("33")) {
				File file = new File("C:\\Users\\mende_000\\Documents\\MusicLibrary\\Principal\\No Love.mp3");
				FileInputStream files = new FileInputStream(file);
				byte [] buffer = new byte[files.available()];
				files.read(buffer);
				String enco = Base64.getEncoder().encode(buffer).toString();
				String xml = enco;
				pw.println(xml);
				
			}
			client.close();
		}
	}
}
// SOCKETS LOS QUE HAY Y LOS FALTANTES
// obtener amigo 0,
// crea usuario 10
// validarregistro12
// cargararchivosycarpetas13
// sincronizar bibliotecadeusuario14
// metadata15
// calificaciondecancion16
// mensages17
// recomendaciones18
// listadeamigos19
// ordenamientoBiblioteca20
// reproducirMusica21
// buscarCanciones22
// agregar amigo 23