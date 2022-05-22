package servidorchat;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import clientechat.ClientXat;
import clientechat.ConnexioServidor;
import clientechat.FinestraConfiguracio;

public class ServidorXat extends JFrame {
	
	static JTextArea missatgeXat;
	
	public ServidorXat() throws IOException{
        super("Client Xat");
        
        //System.out.println(usuari);
        
        System.setProperty("javax.net.ssl.trustStore", "clientTrustedCerts.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","clientpass");

        SSLSocketFactory clientFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        
        // Elements de la finestra
        Font myFont = new Font("Maven Pro Bold", Font.PLAIN, 16);
        missatgeXat = new JTextArea();
        missatgeXat.setEnabled(false); 
        missatgeXat.setLineWrap(true); // Permet partir la línea del text en cas de que sigui molt llarga
        missatgeXat.setWrapStyleWord(true); // Les línies es parteixen entre les paraules (espais en blanc)
        JScrollPane scrollMissatgeXat = new JScrollPane(missatgeXat);
        missatgeXat.setFont(myFont);
        missatgeXat.setForeground(Color.BLACK);
        
        FinestraConfiguracio vc = new FinestraConfiguracio(this);
        //host = vc.getHost();
        //port = vc.getPort();
        //usuari = vc.getUsuari();
        
        BufferedImage myPicture = ImageIO.read(new File("D:\\2n DAM\\M9\\xat\\Logotip.png"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        
        //JLabel label1 =new JLabel("USUARI: " + usuari);
        
        JTextField tfMissatge = new JTextField("");
        JButton btEnviar = new JButton("Enviar");
        btEnviar.setBackground(Color.decode("#C1271F"));
        btEnviar.setBorderPainted(false);
        btEnviar.setForeground(Color.WHITE);
        btEnviar.setFont(myFont);
        
        //label1.setFont(myFont);
        
        
        // Posicio dels components de la finestra
        Container c = this.getContentPane();
        c.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(20, 20, 20, 20);
        
        //gbc.gridx = 0;
        //gbc.gridy = 0;
        //c.add(picLabel, gbc);
        
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
        tfMissatge.setMargin(new Insets(5,10,5,10));
        c.add(tfMissatge, gbc);
        
        // Restaura valores por defecto
        gbc.weightx = 0;
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        c.add(btEnviar, gbc);
        
        gbc.weightx = 0;
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        //c.add(label1, gbc);
        
        //gbc.weightx = 0;
        
        //gbc.gridx = 1;
        //gbc.gridy = 3;
        //c.add(picLabel, gbc);
        
        this.setBounds(400, 100, 400, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        
        // Ventana de configuracion inicial
        
        //System.out.println("Et vols connectar a " + host + " amb el port " + port + " amb el nom d'usuari: " + usuari + ".");
        
        // Creem el socket per connectar amb el servidor
        // Acció del boto enviar
        //btEnviar.addActionListener(new ConnexioServidor(socket, tfMissatge, usuari));
        
        //System.out.println(usuari);
        
    }

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