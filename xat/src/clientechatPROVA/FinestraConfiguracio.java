package clientechatPROVA;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class FinestraConfiguracio extends JDialog{
    
    private JTextField tfUsuari;
    private JTextField tfHost;
    private JTextField tfPort;

    public FinestraConfiguracio(JFrame pare) {
        super(pare, "Configuració inicial", true);
        
        JLabel lbUsuari = new JLabel("Usuari:");
        JLabel lbHost = new JLabel("Host:");
        JLabel lbPort = new JLabel("Port:");
        
        tfUsuari = new JTextField();
        tfHost = new JTextField("localhost");
        tfPort = new JTextField("9090");
        
        JButton btAceptar = new JButton("Aceptar");
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
        c.add(lbUsuari, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        c.add(lbHost, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        c.add(lbPort, gbc);
        
        gbc.ipadx = 100;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        c.add(tfUsuari, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        c.add(tfHost, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        c.add(tfPort, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
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