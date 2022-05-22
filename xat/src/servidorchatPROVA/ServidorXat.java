package servidorchatPROVA;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocketFactory;

public class ServidorXat {

    public static void main(String[] args) throws IOException {
        
    	final int PORT = 9090;
        // Màxim nº de connexións
        int connMax = 10;
        MissatgeXat missatges = new MissatgeXat();
        
        System.setProperty("javax.net.ssl.keyStore", "serverKey.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","servpass");

        SSLServerSocketFactory serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        
        Socket socket = null;
        ServerSocket servidor = null;
        
        try {
            // S'ha crear el socket del servidor
            servidor = serverFactory.createServerSocket(PORT);
            
            // Bucle infinit per esperar connexions
            while (true) {
            	System.out.println("Servidor a l'espera de connexions...");
                socket = servidor.accept();
                System.out.println("Client amb IP " + socket.getInetAddress().getHostName() + " conectat.");
                
                ConnexioClient cc = new ConnexioClient(socket, missatges);
                cc.start();
                
            }
        } catch (IOException ex) {
        	System.out.println("Error: " + ex.getMessage());
        } finally{
            try {
                socket.close();
                servidor.close();
            } catch (IOException ex) {
            	System.out.println("Error al tancar el servidor: " + ex.getMessage());
            }
        }
    }
}