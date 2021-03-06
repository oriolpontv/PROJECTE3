package clientXat;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class FinestraConfiguracio extends JDialog{
    
    private JTextField tfUsuari;
    private JTextField tfHost;
    private JTextField tfPort;
    private ArrayList<String> list0 = new ArrayList<String>();

    public FinestraConfiguracio(JFrame pare) throws IOException {
    	
        super(pare, "Configuració inicial", true);
        
        //Comprovem el nom d'usuari amb els existents de la base de dades
        String url0 = "jdbc:postgresql://192.168.1.22/Deliverass";
	    String user0 = "postgres";
	    String password0 = "Fat/3232";
	    String QUERY0 = "SELECT dni_client FROM usuaris_login_xat";
	    
	    try (Connection connection0 = DriverManager.getConnection(url0, user0, password0);

		PreparedStatement preparedStatement0 = connection0.prepareStatement(QUERY0);) {
		ResultSet rs0 = preparedStatement0.executeQuery();

			while (rs0.next()) {
					
				System.out.println(rs0.getString("dni_client")); 
				list0.add(rs0.getString("dni_client"));
				System.out.println(list0.toString());
					
			}
				
		} catch (SQLException ex) {
				
			System.out.println("Error: " + ex);
				
		}
        
        Font myFont = new Font("Maven Pro Bold", Font.PLAIN, 16);
        
        //Elements de la finestra
        JLabel lbUsuari = new JLabel("Usuari:");
        lbUsuari.setFont(myFont);
        JLabel lbHost = new JLabel("Host:");
        lbHost.setFont(myFont);
        JLabel lbPort = new JLabel("Port:");
        lbPort.setFont(myFont);
        
        tfUsuari = new JTextField();
        tfHost = new JTextField("192.168.1.134");
        tfHost.setEditable(false);
        tfPort = new JTextField("9090");
        tfPort.setEditable(false);
        
        JButton btAceptar = new JButton("Aceptar");
        btAceptar.setBackground(Color.decode("#C1271F"));
        btAceptar.setBorderPainted(false);
        btAceptar.setForeground(Color.WHITE);
        btAceptar.setFont(myFont);
        
        Image icon = Toolkit.getDefaultToolkit().getImage("D:\\2n DAM\\M9\\xat\\icon.png");    
        this.setIconImage(icon);   
        
        BufferedImage myPicture = ImageIO.read(new File("D:\\2n DAM\\M9\\xat\\Logotip.png"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        
        //Definim l'accio del boto per establir connexio
        btAceptar.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
            	//Comprovem el resultat de les credencials
            	if(list0.contains(tfUsuari.getText())) {
            		System.out.println("Usuari correcte");
            		setVisible(false);
            	}else {
            		System.out.println("Usuari incorrecte");
            	}
            }
        });
        
        //Posicionament dels elements
        Container c = this.getContentPane();
        c.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(20, 20, 0, 20);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        c.add(picLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        c.add(lbUsuari, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        c.add(lbHost, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        c.add(lbPort, gbc);
        
        gbc.ipadx = 100;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        c.add(tfUsuari, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        c.add(tfHost, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        c.add(tfPort, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        c.add(btAceptar, gbc);
        
        this.pack(); // Minim tamany per a la pantalla
        this.setLocation(450, 200); // Posició de la finestra
        this.setResizable(false); // Evitar que es pugui modificar el tamany de la pantalla
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Bloquejar el boto de tancar la pantalla
        this.setVisible(true);
        
    }
    
    
    public String getUsuari(){
    	return this.tfUsuari.getText();
    }
    
    public String getHost(){
        return this.tfHost.getText();
    }
    
    public int getPort(){
        return Integer.parseInt(this.tfPort.getText());
    }

}