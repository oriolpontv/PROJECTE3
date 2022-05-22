package clientechat;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import clientechat.ConnexioServidor;
import javax.net.ssl.SSLSocketFactory;
import javax.imageio.ImageIO;
import javax.net.ssl.SSLSocket;
import javax.swing.*;


public class ClientXat extends JFrame {
    
    private JTextArea missatgeXat;
    private Socket socket;
    private int port;
    private String host;
    private String usuari;
    
    public ClientXat() throws IOException{
        super("Client Xat");
        
        System.out.println(usuari);
        
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
        host = vc.getHost();
        port = vc.getPort();
        usuari = vc.getUsuari();
        
        BufferedImage myPicture = ImageIO.read(new File("D:\\2n DAM\\M9\\xat\\Logotip.png"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        
        JLabel label1 =new JLabel("USUARI: " + usuari);
        
        JTextField tfMissatge = new JTextField("");
        JButton btEnviar = new JButton("Enviar");
        btEnviar.setBackground(Color.decode("#C1271F"));
        btEnviar.setBorderPainted(false);
        btEnviar.setForeground(Color.WHITE);
        btEnviar.setFont(myFont);
        
        label1.setFont(myFont);
        
        
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
        c.add(label1, gbc);
        
        //gbc.weightx = 0;
        
        //gbc.gridx = 1;
        //gbc.gridy = 3;
        //c.add(picLabel, gbc);
        
        this.setBounds(400, 100, 400, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        
        // Ventana de configuracion inicial
        
        System.out.println("Et vols connectar a " + host + " amb el port " + port + " amb el nom d'usuari: " + usuari + ".");
        
        // Creem el socket per connectar amb el servidor
        try {
            socket = (SSLSocket) clientFactory.createSocket(host,port);//new Socket(host, port);
        } catch (UnknownHostException ex) {
        	System.out.println("No s'ha pogut connectar amb el servidor (" + ex.getMessage() + ").");
        } catch (IOException ex) {
        	System.out.println("No s'ha pogut connectar amb el servidor (" + ex.getMessage() + ").");
        }
        
        // Acció del boto enviar
        btEnviar.addActionListener(new ConnexioServidor(socket, tfMissatge, usuari));
        
        System.out.println(usuari);
        
    }

    public void rebreMissatgesServidor(){
        // Obtenim el flux d'entrada
        DataInputStream entradaDades = null;
        String missatge;
        try {
        	entradaDades = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
        	System.out.println("Error al crear stream d'entrada: " + ex.getMessage());
        } catch (NullPointerException ex) {
        	System.out.println("El socket no s'ho creat correctament. ");
        }
        
        // Bucle infinit que reb els missatges
        boolean connectat = true;
        while (connectat) {
            try {
            	missatge = entradaDades.readUTF();
                missatgeXat.append(missatge + System.lineSeparator());
            } catch (IOException ex) {
            	System.out.println("Error al llegir de stream d'entrada: " + ex.getMessage());
            	connectat = false;
            } catch (NullPointerException ex) {
                System.out.println("El socket no s'ha creat correctament. ");
                connectat = false;
            }
        }
    }

    public static void main(String[] args) throws IOException {      
        
    	ClientXat c = new ClientXat();
        c.rebreMissatgesServidor();
    }

}