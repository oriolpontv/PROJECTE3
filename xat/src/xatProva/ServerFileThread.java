package xatProva;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerFileThread extends Thread{
	ServerSocket server = null;
	Socket socket = null;
	static List<Socket> list = new ArrayList<Socket>();  // cliente de la tienda
	
	public void run() {
		try {
			server = new ServerSocket(8090);
			while(true) {
				socket = server.accept();
				list.add(socket);
				// Abre el hilo de transferencia de archivos
				FileReadAndWrite fileReadAndWrite = new FileReadAndWrite(socket);
				fileReadAndWrite.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class FileReadAndWrite extends Thread {
	private Socket nowSocket = null;
	private DataInputStream input = null;
	private DataOutputStream output = null;
	
	public FileReadAndWrite(Socket socket) {
		this.nowSocket = socket;
	}
	public void run() {
		try {
			input = new DataInputStream(nowSocket.getInputStream());  // flujo de entrada
			while (true) {
				// Obtener el nombre y la longitud del archivo
				String textName = input.readUTF();
				long textLength = input.readLong();
				// Envíe el nombre y la longitud del archivo a todos los clientes
				for(Socket socket: ServerFileThread.list) {
					output = new DataOutputStream(socket.getOutputStream());  // salida de corriente
					if(socket != nowSocket) {  // enviar a otros clientes
						output.writeUTF(textName);
						output.flush();
						output.writeLong(textLength);
						output.flush();
					}
				}
				// enviar contenido de archivo
				int length = -1;
				long curLength = 0;
				byte[] buff = new byte[1024];
				while ((length = input.read(buff)) > 0) {
					curLength += length;
					for(Socket socket: ServerFileThread.list) {
						output = new DataOutputStream(socket.getOutputStream());  // salida de corriente
						if(socket != nowSocket) {  // enviar a otros clientes
							output.write(buff, 0, length);
							output.flush();
						}
					}
					if(curLength == textLength) {  // forzar salida
						break;
					}
				}
			}
		} catch (Exception e) {
			ServerFileThread.list.remove(nowSocket);  // Se cierra el hilo y se quita el conector correspondiente
		}
	}
}