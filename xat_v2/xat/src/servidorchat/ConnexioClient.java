package servidorchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;


public class ConnexioClient extends Thread implements Observer{
    
    private Socket socket; 
    private MissatgeXat missatge;
    private DataInputStream entradaDades;
    private DataOutputStream sortidaDades;
    
    public ConnexioClient (Socket socket, MissatgeXat mensajes){
        this.socket = socket;
        this.missatge = mensajes;
        
        try {
        	entradaDades = new DataInputStream(socket.getInputStream());
        	sortidaDades = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
        	System.out.println("Error al crear els stream d'entrada i sortida : " + ex.getMessage());
        }
    }
    
    @Override
    public void run(){
        String missatgeRebut;
        boolean connectat = true;

        missatge.addObserver(this);
        
        while (connectat) {
            try {
                // Llegeix un missatge enviat pel client
            	missatgeRebut = entradaDades.readUTF();
            	
                missatge.setMissatge(missatgeRebut);
            } catch (IOException ex) {
            	System.out.println("Client amb IP " + socket.getInetAddress().getHostName() + " desconnectat.");
            	connectat = false; 
                // Si hi ha problemes tanquem la connexio del client
                try {
                	entradaDades.close();
                	sortidaDades.close();
                } catch (IOException ex2) {
                	System.out.println("Error al tancar els stream d'entrada i sortida : " + ex2.getMessage());
                }
            }
        }   
    }
    
    @Override
    public void update(Observable o, Object arg) {
        try {
            // Enviar el missatge al client
        	sortidaDades.writeUTF(arg.toString());
        } catch (IOException ex) {
            System.out.println("Error al enviar el missatge al cliente (" + ex.getMessage() + ").");
        }
    }
} 