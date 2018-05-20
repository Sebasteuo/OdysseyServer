import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.json.JsonObject;

import social.Friends;
import social.Recommendations;
import users.ExistingUser;

import treeStructure.BinarySearchTree;
import users.NewUser;

public class Sockets {
	@SuppressWarnings("resource")
	public static void conectar(BinarySearchTree users) throws Exception {
		ServerSocket serversocket = new ServerSocket(6000);
		System.out.println("Listo");
		while (true) {
			Socket client = serversocket.accept();
			Scanner scanner = new Scanner(client.getInputStream());
			PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
			String name = scanner.nextLine();
			System.out.println(name);
			if (name.substring(0, 1).equals("0")) {
				ExistingUser a = new ExistingUser("", "", users);
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
								for (int c = 0; name.substring(fingenero, name.length()).length() > c; c++)
								{
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
				String xml = "<"+validador+"> Se ha enviado el mensaje </"+validador+">";
				pw.println(xml);
			}
			client.close();
		}
	}
}
//SOCKETS LOS QUE HAY Y LOS FALTANTES
//obtener amigo 0,
//crea usuario 10
//validarregistro12
//cargararchivosycarpetas13
//sincronizar bibliotecadeusuario14
//metadata15
//calificaciondecancion16
//mensages17
//recomendaciones18
//listadeamigos19
//ordenamientoBiblioteca20
//reproducirMusica21
//buscarCanciones22
//agregar amigo 23