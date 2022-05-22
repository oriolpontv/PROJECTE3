package servidorXat;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocketFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServidorXat extends JFrame {
	
	private String log = null;
	
	private JTextArea missatgesXat;
        
	public ServidorXat() throws IOException{
		super("DeliverAss Server");
		//System.setProperty("javax.net.ssl.keyStore", "serverKey.jks");
		//System.setProperty("javax.net.ssl.keyStorePassword","servpass");
		
		// Elements de la finestra
        Font myFont = new Font("Maven Pro Bold", Font.PLAIN, 16);
        missatgesXat = new JTextArea();
        missatgesXat.setEnabled(false); 
        missatgesXat.setLineWrap(true); // Permet partir la línea del text en cas de que sigui molt llarga
        missatgesXat.setWrapStyleWord(true); // Les línies es parteixen entre les paraules (espais en blanc)
        missatgesXat.setForeground(Color.BLACK);
        JScrollPane scrollMissatgeXat = new JScrollPane(missatgesXat);
        scrollMissatgeXat.getViewport().getView().setForeground(Color.black);
        missatgesXat.setFont(myFont);
        missatgesXat.setForeground(Color.BLACK);
        
        Image icon = Toolkit.getDefaultToolkit().getImage("D:\\2n DAM\\M9\\xat\\icon.png");    
        this.setIconImage(icon);  
        
        // Posicio dels components de la finestra
        Container c = this.getContentPane();
        c.setLayout(new GridBagLayout());
       
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(20, 20, 20, 20);
		
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        c.add(scrollMissatgeXat, gbc);
        
     // Restaura valores por defecto
        gbc.gridwidth = 1;        
        gbc.weighty = 0;
        
        gbc.fill = GridBagConstraints.HORIZONTAL;        
        gbc.insets = new Insets(0, 20, 20, 20);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        
        this.setBounds(400, 100, 400, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void missatgesClients() throws IOException {
		DataInputStream entradaDades = null;
		String missatgesCliets;
		Socket socket2 = null;
		
		try {
        	entradaDades = new DataInputStream(socket2.getInputStream());
        } catch (IOException ex) {
        	log = "Error al crear stream d'entrada: " + ex.getMessage() + "\n";
        	missatgesXat.append(log);
        } catch (NullPointerException ex) {
        	log = "El socket no s'ha creat correctament. \n";
        	missatgesXat.append(log);
        }
		
		// Bucle infinit que reb els missatges
        boolean connectat = true;
        while (connectat) {
            try {
            	missatgesCliets = entradaDades.readUTF();
            	missatgesXat.append(missatgesCliets + System.lineSeparator());
            } catch (IOException ex) {
            	log = "Error al llegir de stream d'entrada: " + ex.getMessage() + "\n";
            	missatgesXat.append(log);
            	connectat = false;
            } catch (NullPointerException ex) {
            	log = "El socket no s'ha creat correctament. \n";
            	missatgesXat.append(log);
                connectat = false;
            }
        }
		
		final int PORT = 9090;
		// Màxim nº de connexións
		int connMax = 10;
       
		System.setProperty("javax.net.ssl.keyStore", "serverKey.jks");
		System.setProperty("javax.net.ssl.keyStorePassword","servpass");

		SSLServerSocketFactory serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

		MissatgeXat missatges = new MissatgeXat();
   
        ServerSocket servidor = null;
        
        try {
            // S'ha crear el socket del servidor
            servidor = serverFactory.createServerSocket(PORT);
            
            // Bucle infinit per esperar connexions
            while (true) {
            	//missatgesXat = "Servidor a l'espera de connexions...";
            	log = "Servidor a l'espera de connexions...\n";
            	missatgesXat.append(log);
                socket2 = servidor.accept();
                log = "Client amb IP " + socket2.getInetAddress().getHostName() + " conectat.\n";
                missatgesXat.append(log);
                ConnexioClient cc = new ConnexioClient(socket2, missatges);
                cc.start();
                
            }
        } catch (IOException ex) {
        	log = "Error: " + ex.getMessage() + "\n";
        	missatgesXat.append(log);
        } finally{
            try {
                socket2.close();
                servidor.close();
            } catch (IOException ex) {
            	log = "Error al tancar el servidor: " + ex.getMessage() + "\n";
            	missatgesXat.append(log);
            }
        }
   }
   
   public static void main(String[] args) throws IOException {
       
	   ServidorXat s = new ServidorXat();
	   s.missatgesClients();
   }
   
}