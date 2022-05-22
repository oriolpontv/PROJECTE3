package xatProva;

import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class Client {
	// Función principal, nueva ventana de inicio de sesión
	public static void main(String[] args) {
		new Login();
	}
}

class ClientReadAndPrint extends Thread{
	static Socket mySocket = null;  // Asegúrese de agregar estática; de lo contrario, se borrará cuando se cree un nuevo hilo
	static JTextField textInput;
	static JTextArea textShow;
	static JFrame chatViewJFrame;
	static BufferedReader in = null;
	static PrintWriter out = null;
	static String userName;
	
	// Se usa para recibir mensajes enviados desde el servidor
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));  // flujo de entrada
			while (true) {
				String str = in.readLine();  // Obtener la información enviada por el servidor
				textShow.append(str + '\n');  // Agregar al área de texto del cliente de chat
				textShow.setCaretPosition(textShow.getDocument().getLength());  // Establecer la barra de desplazamiento en la parte inferior
			}
		} catch (Exception e) {}
	}
	
	/// ********************** Monitoreo de inicio de sesión (clase interna) ******************** * /
	class LoginListen implements ActionListener{
		JTextField textField;
		JPasswordField pwdField;
		JFrame loginJFrame;  // ventana de inicio de sesión en sí
		
		ChatView chatView = null;
		
		public void setJTextField(JTextField textField) {
			this.textField = textField;
		}
		public void setJPasswordField(JPasswordField pwdField) {
			this.pwdField = pwdField;
		}
		public void setJFrame(JFrame jFrame) {
			this.loginJFrame = jFrame;
		}
		public void actionPerformed(ActionEvent event) {
			userName = textField.getText();
			String userPwd = String.valueOf(pwdField.getPassword());  // método getPassword para obtener la matriz de caracteres
			if(userName.length() >= 1 && userPwd.equals("123")) {  // La contraseña es 123 y la longitud del nombre de usuario es mayor o igual a 1
				chatView = new ChatView(userName);  // Cree una nueva ventana de chat, configure el nombre de usuario de la ventana de chat (estático)
				// Establece una conexión con el servidor
				try {
					InetAddress addr = InetAddress.getByName(null);  // Obtener la dirección del host
					mySocket = new Socket(addr,8081);  // conector de cliente
					loginJFrame.setVisible(false);  // ocultar la ventana de inicio de sesión
					out = new PrintWriter(mySocket.getOutputStream());  // salida de corriente
					out.println("usuario【" + userName + "] ¡Entra en la sala de chat!");  // Enviar nombre de usuario al servidor
					out.flush();  // Limpia los datos del búfer
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Cree un nuevo hilo de lectura y escritura ordinario y comience
				ClientReadAndPrint readAndPrint = new ClientReadAndPrint();
				readAndPrint.start();
				// Crea un nuevo archivo de lectura y escritura y comienza
				ClientFileThread fileThread = new ClientFileThread(userName, chatViewJFrame, out);
				fileThread.start();
			}
			else {
				JOptionPane.showMessageDialog(loginJFrame, "La cuenta o la contraseña son incorrectas, ¡vuelva a ingresar!", "inmediato", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	/// ********************** Supervisión de la interfaz de chat (clase interna) ******************** ** /
	class ChatViewListen implements ActionListener{
		public void setJTextField(JTextField text) {
			textInput = text;  // Ponlo en una clase externa, porque se usará en otro lugar
		}
		public void setJTextArea(JTextArea textArea) {
			textShow = textArea;  // Ponlo en una clase externa, porque se usará en otro lugar
		}
		public void setChatViewJf(JFrame jFrame) {
			chatViewJFrame = jFrame;  // Ponlo en una clase externa, porque se usará en otro lugar
			// Establecer para cerrar la supervisión de la interfaz de chat
			chatViewJFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					out.println("usuario【" + userName + "] ¡Sal de la sala de chat!");
					out.flush();
					System.exit(0);
				}
			});
		}
		// supervisar la función de ejecución
		public void actionPerformed(ActionEvent event) {
			try {
				String str = textInput.getText();
				// El contenido del cuadro de texto está vacío
				if("".equals(str)) {
					textInput.grabFocus();  // Establecer foco (factible)
					// Diálogo de mensaje emergente (mensaje de advertencia)
					JOptionPane.showMessageDialog(chatViewJFrame, "¡La entrada está vacía, vuelva a ingresar!", "inmediato", JOptionPane.WARNING_MESSAGE);
					return;
				}
				out.println(userName + "Decir:" + str);  // salida al servidor
				out.flush();  // Limpia los datos del búfer
				
				textInput.setText("");  // Limpia el cuadro de texto
				textInput.grabFocus();  // Establecer foco (factible)
// textInput.requestFocus (true); // establecer foco (factible)
			} catch (Exception e) {}
		}
	}
}
