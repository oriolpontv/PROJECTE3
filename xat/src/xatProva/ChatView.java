package xatProva;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ChatView {
	String userName;  // Establecer cuando el cliente inicia sesión
	JTextField text;
	JTextArea textArea;
	
	ClientReadAndPrint.ChatViewListen listener;
	
	// Constructor
	public ChatView(String userName) {
		this.userName = userName ;
		init();
	}
	// Función de inicialización
	void init() {
		JFrame jf = new JFrame("Cliente");
		jf.setBounds(500,200,400,330);  // Establecer las coordenadas y el tamaño
		jf.setResizable(false);  // Zoom para no hacer zoom
		
		JPanel jp = new JPanel();
		JLabel lable = new JLabel("usuario:" + userName);
		textArea = new JTextArea("*************** El inicio de sesión se realizó correctamente, ¡bienvenido a la sala de chat de varias personas! **************** \n",12, 35);
		textArea.setEditable(false);  // establecer como no modificable
		JScrollPane scroll = new JScrollPane(textArea);  // Establecer el panel de desplazamiento (cargar textArea)
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // mostrar barra vertical
		jp.add(lable);
		jp.add(scroll);
		
		text = new JTextField(20);
		JButton button = new JButton("enviar");
		JButton openFileBtn = new JButton("Enviar archivo");
		jp.add(text);
		jp.add(button);
		jp.add(openFileBtn);
		
		// Configurar la supervisión de "archivo abierto"
		openFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showFileOpenDialog(jf);
			}
		});
		
		// Configurar el monitor "enviar"
		listener = new ClientReadAndPrint().new ChatViewListen();
		listener.setJTextField(text);  // Llamar al método de la clase PoliceListen
		listener.setJTextArea(textArea);
		listener.setChatViewJf(jf);
		text.addActionListener(listener);  // Agregar monitor al cuadro de texto
		button.addActionListener(listener);  // botón para agregar monitor
		
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Establece la función del icono de cierre en la esquina superior derecha
		jf.setVisible(true);  // establecer visible
	}
	// función de llamada "Abrir archivo"
	void showFileOpenDialog(JFrame parent) {
		// Crea un selector de archivos predeterminado
		JFileChooser fileChooser = new JFileChooser();
		// Establecer la carpeta que se muestra por defecto
		fileChooser.setCurrentDirectory(new File("C:/Users/Samven/Desktop"));
		// Agregar filtros de archivo disponibles (el primer parámetro de FileNameExtensionFilter es la descripción, seguido de la extensión del archivo a filtrar)
//        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("(txt)", "txt"));
        // Establecer el filtro de archivo utilizado por defecto (el primer parámetro de FileNameExtensionFilter es la descripción, seguido del parámetro variable de la extensión del archivo a filtrar)
        fileChooser.setFileFilter(new FileNameExtensionFilter("(txt)", "txt"));
		// Abra el cuadro de selección de archivo (el hilo se bloqueará hasta que se cierre el cuadro de selección)
		int result = fileChooser.showOpenDialog(parent);  // El diálogo se mostrará lo más cerca posible del centro del padre
		// Haga clic en Aceptar
		if(result == JFileChooser.APPROVE_OPTION) {
			// Obtener ruta
			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			ClientFileThread.outFileToServer(path);
		}
	}
}