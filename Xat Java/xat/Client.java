package xat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
	
	public void connect(String address, int port) {
		
		String serverData;
		String request;
		boolean continueConnected = true;
		Socket socket;
		BufferedReader in;
		PrintStream out;
		
		try {
			
			socket = new Socket(InetAddress.getByName(address), port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream());
			
			//el client at�n el port fins que decideix finalitzar
			
			while(continueConnected) {
				
				serverData = in.readLine();
				//processament de les dades rebudes i obtenci� d�una nova petici�
				request = getRequest(serverData);
				//enviament de la petici�
				out.println(request);//assegurem que acaba amb un final de l�nia
				out.flush(); //assegurem que s�envia
				//comprovem si la petici� �s un petici� de finalitzaci� i encas
				//que ho sigui ens preparem per sortir del bucle
				continueConnected = mustFinish(request);

			}
			
			close(socket);
			
		}catch(UnknownHostException ex) {
			
			System.out.println("Error de connexi�. Noexisteix el host" + ex);
			
		}catch(IOException ex) {
			
			System.out.println("Error de connexi� indefinit" + ex);
			
		}
		
	}
	
	private void close(Socket socket) {
		
		// si falla el tancament no podem fer gaire cosa, nom�s enregistrar el problema
		
		try {
			
			// tancament de tots els recursos
			
			if (socket != null && !socket.isClosed()) {
				
				if (!socket.isInputShutdown()) {
					
					socket.shutdownInput();
					
				}
				
				if (!socket.isOutputShutdown()) {
					
					socket.shutdownOutput();
					
				}
				
				socket.close();
				
			}
			
		} catch (IOException ex) {
			
			// enregistrem l�error amb un objecte Logger
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		
	}

}
