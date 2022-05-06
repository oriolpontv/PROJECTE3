package xatPrincipal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
	
	static final int PORT = 9090;
	private boolean end = false;
	
	public static void main (String[] args) {
		Server server= new Server();
		System.out.println("Servidor escoltant...");
		server.listen();
	}

	public void listen() {
		
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		
		try {
			
			serverSocket = new ServerSocket(PORT);
			
			while (!end) {
				
				clientSocket = serverSocket.accept();
				//Processem la petició del client
				proccesClientRequest(clientSocket);
				//Tanquem el sòcol temporal per atendre el client
				closeClient(clientSocket);
				
			}
			//Tanquem el sòcol principal
			if (serverSocket != null && !serverSocket.isClosed()) {
				
				serverSocket.close();
				
			}
			
		} catch (IOException ex) {
			
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		
	}
	
	public void proccesClientRequest(Socket clientSocket) {
		
		boolean farewellMessage = false;
		String clientMessage = "";
		BufferedReader in = null;
		PrintStream out = null;
		
		try {
			
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintStream(clientSocket.getOutputStream());
			
			do {
				
				// Processem el missatge del client i generem la resposta. Si clientMessage és buida generarem el missatge de benvinguda
				String dataToSend = processData(clientMessage);
				out.println(dataToSend);
				out.flush();
				clientMessage = in.readLine();
				farewellMessage = isFarewellMessage(clientMessage);
				
			} while ((clientMessage) != null && !farewellMessage);
			
		} catch (IOException ex) {
			
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			
		}
		
	}
	
	private void closeClient(Socket clientSocket) {
		
		//Si falla en tancament no podem fer gaire cosa, només enregistrar el problema
		
		try {
			
			//Tancament de tots els recursos
			if(clientSocket != null && !clientSocket.isClosed()) {
				
				if(!clientSocket.isInputShutdown()) {
					
					clientSocket.shutdownInput();
					
				}
				
				if(!clientSocket.isOutputShutdown()) {
					
					clientSocket.shutdownOutput();
					
				}
				
				clientSocket.close();
				
			}
			
		}catch (IOException ex) {
			
			//Imprimim per pantalla l'error
			System.out.println(ex);
			
		}
		
	}
	
	public String processData(String clientMessage){
		System.out.println(clientMessage);
		return clientMessage;
	}
	
	public boolean isFarewellMessage(String clientMessage) {
		if(clientMessage.equals("FI")) {
			return true;
		} else {
			return false;
		}
	}
	
}