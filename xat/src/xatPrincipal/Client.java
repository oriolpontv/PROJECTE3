package xatPrincipal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread {
	
	public static void main (String[] args) {
		Client client = new Client();
		System.out.println("Client...");
		client.connect("127.0.0.1", 9090);
	}
	
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
			
			Scanner entrada = new Scanner (System.in);

			//El client atén el port fins que decideix finalitzar
			
			while(continueConnected) {
				serverData = in.readLine();
				//Processament de les dades rebudes i obtenció d’una nova petició
				if(serverData == "FI") {
					request = getRequest(serverData);
				} else {
					request = entrada.nextLine();
				}
				//Enviament de la petició
				out.println(request);//assegurem que acaba amb un final de línia
				out.flush(); //assegurem que s’envia
				//Eomprovem si la petició és un petició de finalització i encas que ho sigui ens preparem per sortir del bucle
				continueConnected = mustFinish(request);

			}
			entrada.close();
			close(socket);
			
		}catch(UnknownHostException ex) {
			
			System.out.println("Error de connexió. Noexisteix el host" + ex);
			
		}catch(IOException ex) {
			
			System.out.println("Error de connexió indefinit" + ex);
			
		}
		
	}
	
	private void close(Socket socket) {
		
		// Si falla el tancament no podem fer gaire cosa, només enregistrar el problema
		
		try {
			
			// Tancament de tots els recursos
			
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
			
			// Enregistrem l’error amb un objecte Logger
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	public String getRequest(String ServerData) {
		return ServerData;
	}
	
	public boolean mustFinish(String request) {
		if(request.equals("FI")) {
			return false;
		}else {
			return true;
		}
	}

}
