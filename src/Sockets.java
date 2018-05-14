import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.json.JsonObject;
import users.ExistingUser;

import treeStructure.BinarySearchTree;
import users.NewUser;

public class Sockets {
	public static void conectar(BinarySearchTree users) throws Exception {
		ServerSocket serversocket = new ServerSocket(8000);
		System.out.println("Listo");
		while (true) {
			Socket client = serversocket.accept();
			Scanner scanner = new Scanner(client.getInputStream());
			PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
			String name = scanner.nextLine();
			System.out.println(name);
			if (name.substring(0,1).equals("0")) {
				ExistingUser a = new ExistingUser("","");
				String b = a.getExistingUserNames();
				String xml = "<b> Usuarios existentes: </b>";
				pw.println(xml);
			}
			if (name.substring(0, 1).equals("1")) {
				if ("hola".equals("hola")) {
					String xml = "<true> Apodo libre </true>";
					pw.println(xml);
					int finick = 0;
					int finame = 0;
					int finedad = 0;
					int fingenero = 0;
					int fincontra = 0;
					int j = 3;
					int x = 0;
					String[] genero = new String[10];
					String[] amigo = new String [10];
					for (int i = 2; name.length() > i; i++) {
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
								//int v = fingenero + 1;
								//int z = 0;
								//int m = fingenero;
								//for (int c = 0; name.substring(fingenero, name.length()).length() >= c; c++) {
									//if (name.substring(v - 1, v).equals(",")) {
										//amigo[z] = name.substring(m, v - 1);
										//m = c + fingenero + 1;
										//z++;
									//}
									//v++;
								//}
							}
						}
						j++;
					}
					NewUser usuario = new NewUser();
					usuario.setUserName(name.substring(2, finick-1));
					usuario.setName(name.substring(finick, finame-1));
					usuario.setAge(Integer.parseInt(name.substring(finame, finedad-1)));
					usuario.setMusicalGenres(genero);
					usuario.setPassword(name.substring(fingenero, fincontra-1));
					usuario.setFriends(amigo);
					JsonObject toInsert = usuario.signInUser();
					System.out.println(toInsert.toString());
				} else {
					pw.println("<false> Apodo utilizado </false>");
				}
			}
			client.close();
		}
	}
}
