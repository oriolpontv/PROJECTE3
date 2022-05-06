package clientechat;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class FinestraConfiguracio extends JDialog{
    
    private JTextField tfUsuari;
    private JTextField tfHost;
    private JTextField tfPort;

    public FinestraConfiguracio(JFrame pare) throws IOException {
    	
        super(pare, "Configuració inicial", true);
        
        Font myFont = new Font("Maven Pro Bold", Font.PLAIN, 16);
        
        JLabel lbUsuari = new JLabel("Usuari:");
        lbUsuari.setFont(myFont);
        JLabel lbHost = new JLabel("Host:");
        lbHost.setFont(myFont);
        JLabel lbPort = new JLabel("Port:");
        lbPort.setFont(myFont);
        
        tfUsuari = new JTextField();
        tfHost = new JTextField("localhost");
        tfHost.setEditable(false);
        tfPort = new JTextField("9090");
        tfPort.setEditable(false);
        
        JButton btAceptar = new JButton("Aceptar");
        btAceptar.setBackground(Color.decode("#C1271F"));
        btAceptar.setBorderPainted(false);
        btAceptar.setForeground(Color.WHITE);
        btAceptar.setFont(myFont);
        
        BufferedImage myPicture = ImageIO.read(new File("D:\\2n DAM\\M9\\xat\\Logotip.png"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        
        btAceptar.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
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