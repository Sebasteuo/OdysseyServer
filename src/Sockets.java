import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Sockets {
	public static void conectar() throws Exception {
		ServerSocket serversocket = new ServerSocket(113, 10);
		System.out.println("Listo");
		while (true) {
			Socket client = serversocket.accept();
			Scanner scanner = new Scanner(client.getInputStream());
			PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
			String name = scanner.nextLine();
			;
			if ("hola".equals(name)) {
				String xml = "<good>Found</good>";
				pw.println(xml);
			} else {
				pw.println("<error>Not found</error>");
			}
			client.close();
		}
	}
}
