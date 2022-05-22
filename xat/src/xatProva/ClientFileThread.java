package xatProva;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClientFileThread extends Thread{
	private Socket socket = null;
	private JFrame chatViewJFrame = null;
	static String userName = null;
	static PrintWriter out = null;  // Envío de mensajes ordinarios (valor de Server.java)
	static DataInputStream fileIn = null;
	static DataOutputStream fileOut = null;
	static DataInputStream fileReader = null;
	static DataOutputStream fileWriter = null;
	
	public ClientFileThread(String userName, JFrame chatViewJFrame, PrintWriter out) {
		ClientFileThread.userName = userName;
		this.chatViewJFrame = chatViewJFrame;
		ClientFileThread.out = out;
	}
	
	// El cliente recibe el archivo
	public void run() {
		try {
			InetAddress addr = InetAddress.getByName(null);  // Obtener la dirección del host
			socket = new Socket(addr, 8090);  // conector de cliente
			fileIn = new DataInputStream(socket.getInputStream());  // flujo de entrada
			fileOut = new DataOutputStream(socket.getOutputStream());  // salida de corriente
			// Recibir archivos
			while(true) {
				String textName = fileIn.readUTF();
				long totleLength = fileIn.readLong();
				int result = JOptionPane.showConfirmDialog(chatViewJFrame, "¿Si aceptar?", "inmediato",
														   JOptionPane.YES_NO_OPTION);
				int length = -1;
				byte[] buff = new byte[1024];
				long curLength = 0;
				// Resultado de la selección del cuadro de aviso, 0 es correcto, 1 se cancela
				if(result == 0){
//out.println ("[" + userName + "¡Seleccionado para recibir archivos!]");
//					out.flush();
					File file = new File("C: \\ Usuarios \\ Samven \\ Escritorio \\ aceptar archivo \\ (" +
							 userName + ")" + textName);
					fileWriter = new DataOutputStream(new FileOutputStream(file));
					while((length = fileIn.read(buff)) > 0) {  // escribe el archivo localmente
						fileWriter.write(buff, 0, length);
						fileWriter.flush();
						curLength += length;
//out.println ("[Progreso de recepción:" + curLength / totleLength * 100 + "%]");
//						out.flush();
						if(curLength == totleLength) {  // obligado a terminar
							break;
						}
					}
					out.println("【" + userName + "¡Archivo recibido!]");
					out.flush();
					// dirección de almacenamiento de archivos de solicitud
					JOptionPane.showMessageDialog(chatViewJFrame, "Ubicación de almacenamiento de archivos: \n" +
							"C: \\ Usuarios \\ Samven \\ Escritorio \\ aceptar archivo \\ (" +
							userName + ")" + textName, "inmediato", JOptionPane.INFORMATION_MESSAGE);
				}
				else {  // no acepta archivos
					while((length = fileIn.read(buff)) > 0) {
						curLength += length;
						if(curLength == totleLength) {  // obligado a terminar
							break;
						}
					}
				}
				fileWriter.close();
			}
		} catch (Exception e) {}
	}
	
	// El cliente envía archivos
	static void outFileToServer(String path) {
		try {
			File file = new File(path);
			fileReader = new DataInputStream(new FileInputStream(file));
			fileOut.writeUTF(file.getName());  // enviar nombre de archivo
			fileOut.flush();
			fileOut.writeLong(file.length());  // enviar la longitud del archivo
			fileOut.flush();
			int length = -1;
			byte[] buff = new byte[1024];
			while ((length = fileReader.read(buff)) > 0) {  // Enviar contenido
				
				fileOut.write(buff, 0, length);
				fileOut.flush();
			}
			
			out.println("【" + userName + "¡El archivo se ha enviado correctamente!]");
			out.flush();
		} catch (Exception e) {}
	}
}