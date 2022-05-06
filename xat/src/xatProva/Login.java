package xatProva;

import java.awt.*;
import javax.swing.*;

public class Login {
	JTextField textField = null;
	JPasswordField pwdField = null;
	ClientReadAndPrint.LoginListen listener=null;
	
	// Constructor
	public Login() {
		init();
	}
	
	void init() {
		JFrame jf = new JFrame("iniciar sesión");
		jf.setBounds(500, 250, 310, 210);
		jf.setResizable(false);  // Establecer si hacer zoom
		
		JPanel jp1 = new JPanel();
		JLabel headJLabel = new JLabel("interfaz de inicio de sesión");
		headJLabel.setFont(new Font(null, 0, 35));  // Establece el tipo de fuente, el estilo y el tamaño del texto
		jp1.add(headJLabel);
		
		JPanel jp2 = new JPanel();
		JLabel nameJLabel = new JLabel("nombre de usuario:");
		textField = new JTextField(20);
		JLabel pwdJLabel = new JLabel("Contraseña: ");
		pwdField = new JPasswordField(20);
		JButton loginButton = new JButton("iniciar sesión");
		JButton registerButton = new JButton("registrado");  // Sin función establecida
		jp2.add(nameJLabel);
		jp2.add(textField);
		jp2.add(pwdJLabel);
		jp2.add(pwdField);
		jp2.add(loginButton);
		jp2.add(registerButton);
		
		JPanel jp = new JPanel(new BorderLayout());  // Diseño de BorderLayout
		jp.add(jp1, BorderLayout.NORTH);
		jp.add(jp2, BorderLayout.CENTER);
		
		// Configurar monitoreo
		listener = new ClientReadAndPrint().new LoginListen();  // Nueva clase de monitor
		listener.setJTextField(textField);  // Llamar al método de la clase PoliceListen
		listener.setJPasswordField(pwdField);
		listener.setJFrame(jf);
		pwdField.addActionListener(listener);  // agregar monitor en el cuadro de contraseña
		loginButton.addActionListener(listener);  // botón para agregar monitor
		
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Establecer función de icono de cierre
		jf.setVisible(true);  // establecer visible
	}
}