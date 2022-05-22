package xatProva;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class MutiChat {
	JTextArea textArea;
	
	// Se usa para agregar información al área de texto
	void setTextArea(String str) {
		textArea.append(str+'\n');
		textArea.setCaretPosition(textArea.getDocument().getLength());  // Establecer la barra de desplazamiento en la parte inferior
	}
	
	// Constructor
	public MutiChat() {
		init();
	}
	
	void init() {
		JFrame jf = new JFrame("Terminal de servicio");
		jf.setBounds(500,100,450,500);  // Establecer las coordenadas y el tamaño de la ventana
		jf.setResizable(false);  // Establecer para no hacer zoom
		
		JPanel jp = new JPanel();  // Nuevo contenedor
		JLabel lable = new JLabel("== Bienvenido al sistema de chat de varias personas (lado del servidor) ==");
		textArea = new JTextArea(23, 38);  // Crea una nueva área de texto y establece la longitud y el ancho
		textArea.setEditable(false);  // establecer como no modificable
		JScrollPane scroll = new JScrollPane(textArea);  // Establecer el panel de desplazamiento (cargar textArea)
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);  // mostrar barra vertical
		jp.add(lable);
		jp.add(scroll);
		
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Establecer función de icono de cierre
		jf.setVisible(true);  // establecer visible
	}
}