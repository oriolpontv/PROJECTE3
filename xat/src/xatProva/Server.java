package xatProva;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
	static ServerSocket server = null;
	static Socket socket = null;
	static List<Socket> list = new ArrayList<Socket>();  // cliente de la tienda
	
	public static void main(String[] args) {
		MutiChat mutiChat = new MutiChat();  // Nueva interfaz del sistema de chat
		try {
			// Inicie el hilo de transferencia de archivos en el servidor al cliente
			ServerFileThread serverFileThread = new ServerFileThread();
			serverFileThread.start();
			server = new ServerSocket(8081);  // Conector del lado del servidor (solo se puede crear una vez)
			// Espera la conexi�n e inicia el hilo correspondiente
			while (true) {
				socket = server.accept();  // espera la conexi�n
				list.add(socket);  // Agrega el cliente actual a la lista
				// Abra el hilo correspondiente en el lado del servidor para el cliente
				ServerReadAndPrint readAndPrint = new ServerReadAndPrint(socket, mutiChat);
				readAndPrint.start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();  // Si ocurre una excepci�n, imprima la ubicaci�n de la excepci�n
		}
	}
}

/**
* Hilos de lectura y escritura del lado del servidor
* Utilizado por el servidor para leer la informaci�n del cliente y enviar la informaci�n a todos los clientes
*/
class ServerReadAndPrint extends Thread{
	Socket nowSocket = null;
	MutiChat mutiChat = null;
	BufferedReader in =null;
	PrintWriter out = null;
	// Constructor
	public ServerReadAndPrint(Socket s, MutiChat mutiChat) {
		this.mutiChat = mutiChat;  // Obt�n la interfaz del sistema de chat para varias personas
		this.nowSocket = s;  // Obtener el cliente actual
	}
	
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream()));  // flujo de entrada
			// Obtener informaci�n del cliente y enviar la informaci�n a todos los clientes
			while (true) {
				String str = in.readLine();
				// enviar a todos los clientes
				for(Socket socket: Server.list) {
					out = new PrintWriter(socket.getOutputStream());  // Crea un socket correspondiente para cada cliente
					if(socket == nowSocket) {  // enviar al cliente actual
						out.println("(usted)" + str);
					}
					else {  // enviar a otros clientes
						out.println(str);
					}
					out.flush();  // Limpiar el cach�
				}
				// Llamar a una funci�n personalizada para enviarla a la interfaz gr�fica
				mutiChat.setTextArea(str);
			}
		} catch (Exception e) {
			Server.list.remove(nowSocket);  // Se cierra el hilo y se quita el conector correspondiente
		}
	}
}