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
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;
import javax.imageio.ImageIO;
import javax.net.ssl.SSLSocket;
import javax.swing.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.text.SimpleDateFormat;  
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.xml.parsers.DocumentBuilder;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

//import org.bson.Document;
//import org.w3c.dom.Element;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import clientXat.ConnexioServidor;


public class ClientXat extends JFrame {
    
    private JTextArea missatgeXat;
    private Socket socket;
    private int port;
    private String host;
    static String usuari;
    static String llistaParaules;
    static String llistaOrdenada = ConnexioServidor.listParaules.toString();
    static ArrayList<String> listP = ConnexioServidor.listParaules;

    
    static String pattern = "yyyy-MM-dd";
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	static String date = simpleDateFormat.format(new Date());
    
    public ClientXat() throws IOException{
        super("Desliverass Chat");
        
        
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
        scrollMissatgeXat.setForeground(Color.BLACK);
        missatgeXat.setFont(myFont);
        missatgeXat.setForeground(Color.BLACK);
        
        FinestraConfiguracio vc = new FinestraConfiguracio(this);
        host = vc.getHost();
        port = vc.getPort();
        usuari = vc.getUsuari();
        
        Image icon = Toolkit.getDefaultToolkit().getImage("D:\\2n DAM\\M9\\xat\\icon.png");    
        this.setIconImage(icon);    
        
        BufferedImage myPicture = ImageIO.read(new File("D:\\2n DAM\\M9\\xat\\Logotip.png"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        
        //Icon icon = new ImageIcon("D:\\2n DAM\\M9\\xat\\active.png");
        
        JLabel label1 =new JLabel("USUARI: " + usuari);
        
        JTextField tfMissatge = new JTextField("");
        JButton btEnviar = new JButton("Enviar");
        btEnviar.setBackground(Color.decode("#C1271F"));
        btEnviar.setBorderPainted(false);
        btEnviar.setForeground(Color.WHITE);
        btEnviar.setFont(myFont);
        
        JButton btBackup = new JButton("Exportar Xat");
        btBackup.setBackground(Color.decode("#FF9933"));
        btBackup.setBorderPainted(false);
        btBackup.setForeground(Color.WHITE);
        btBackup.setFont(myFont);
        
        JButton btParaules = new JButton("Llistar paraules");
        btParaules.setBackground(Color.decode("#FF9933"));
        btParaules.setBorderPainted(false);
        btParaules.setForeground(Color.WHITE);
        btParaules.setFont(myFont);
        
        label1.setFont(myFont);
        
        
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

        gbc.gridwidth = 1;        
        gbc.weighty = 0;
        
        gbc.fill = GridBagConstraints.HORIZONTAL;        
        gbc.insets = new Insets(0, 20, 20, 20);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        tfMissatge.setMargin(new Insets(5,10,5,10));
        c.add(tfMissatge, gbc);

        gbc.weightx = 0;
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        c.add(btEnviar, gbc);
        
        gbc.weightx = 0;
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        c.add(label1, gbc);

        gbc.weightx = 0;
        gbc.gridx = 1;
        gbc.gridy = 3;
        c.add(btBackup, gbc);
        
        gbc.weightx = 0;
        gbc.gridx = 1;
        gbc.gridy = 4;
        c.add(btParaules, gbc);
        
        this.setBounds(400, 100, 700, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        
        System.out.println("Et vols connectar a " + host + " amb el port " + port + " amb el nom d'usuari: " + usuari + ".");
        
        // Creem el socket per connectar amb el servidor
        try {
        	
            socket = (SSLSocket) clientFactory.createSocket(host,port);
            
        } catch (UnknownHostException ex) {
        	
        	System.out.println("No s'ha pogut connectar amb el servidor (" + ex.getMessage() + ").");
        	
        } catch (IOException ex) {
        	
        	System.out.println("No s'ha pogut connectar amb el servidor (" + ex.getMessage() + ").");
        	
        }
        
        // Acció del boto enviar
        btEnviar.addActionListener(new ConnexioServidor(socket, tfMissatge, usuari));
        
        btBackup.addActionListener(new ActionListener() {

            //@Override
            public void actionPerformed(ActionEvent e) {
                
            	exportarXat();
            	
            }
            
        });
        
        btParaules.addActionListener(new ActionListener() {

            //@Override
            public void actionPerformed(ActionEvent e) {
                
            	exportarParaules();
            	
            }
            
        });
        
        //ENCRIPTACIO DEL FITXER XML
        System.out.println(usuari);
        File tempFile = new File("D:\\2n DAM\\M9\\xat\\paraules_" + usuari + ".xml");
        System.out.println(usuari);
        
        SecretKey key;
        //Generar una clau de mida de 256
		key = keygenKeyGeneration(256);
		File arxiuEntrada = new File("D:\\2n DAM\\M9\\xat\\paraules_" + usuari + ".xml");
		File arxiuEncrypted= new File("D:\\2n DAM\\M9\\xat\\paraules_" + usuari + "_encriptat.xml");
		File arxiuDecrypted= new File("D:\\2n DAM\\M9\\xat\\paraules_" + usuari + "_desencriptat.xml");

		try {
			//Encriptem i desencriptem el fitxer
			encriptacioIdecriptacio(Cipher.ENCRYPT_MODE, key, arxiuEntrada, arxiuEncrypted);
			encriptacioIdecriptacio(Cipher.DECRYPT_MODE, key, arxiuEncrypted, arxiuDecrypted);
			System.out.println("S'ha pogut encriptar i decriptar correctament!");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
        
    }
    
    public static void encriptacioIdecriptacio(int cipherMode, SecretKey key, File arxiuEntrada, File arxiuSortida) {
		try {
			// Xifrar en mode AES
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(cipherMode, key);

			// Obtenció de dades de l'arxiu
			FileInputStream is = new FileInputStream(arxiuEntrada);
			byte[] bytes = new byte[(int) arxiuEntrada.length()];
			is.read(bytes);

			byte[] os = cipher.doFinal(bytes);

			FileOutputStream outputStream = new FileOutputStream(arxiuSortida);
			outputStream.write(os);

			is.close();
			outputStream.close();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
		}
	}

    //METODE PER GENERAR LES CLAUS
	public static SecretKey keygenKeyGeneration(int keySize) {
		SecretKey sKey = null;
		if ((keySize == 128) || (keySize == 192) || (keySize == 256)) {
			try {
				KeyGenerator kgen = KeyGenerator.getInstance("AES");
				kgen.init(keySize);
				sKey = kgen.generateKey();

			} catch (NoSuchAlgorithmException ex) {
				System.err.println("Generador no disponible.");
			}
		}
		return sKey;
	}
    
    static void exportarParaules() {
    	
    	System.out.println("Array bo: " + ConnexioServidor.listParaules);
    	System.out.println(llistaOrdenada);
    	System.out.println(listP);
    	
    	try {
	    	System.out.println("Generant XML...");
	    	//System.out.println(llistaParaules);
	    	DocumentBuilderFactory df0 = DocumentBuilderFactory.newInstance();
		    DocumentBuilder db0 = df0.newDocumentBuilder();
			    Document document0 = db0.newDocument();
		
		        // Element Arrel
			    Element arrel0 = document0.createElement("xat");
			    document0.appendChild(arrel0);
			    
			    Element usuariXat = document0.createElement("usuari");
			    usuariXat.appendChild(document0.createTextNode(usuari));
				arrel0.appendChild(usuariXat);
			    
				for (String s : listP) { 
					Element frasesXat = document0.createElement("paraula");
					frasesXat.appendChild(document0.createTextNode(s));
					arrel0.appendChild(frasesXat);
					System.out.println(s);
				}
			    
			    TransformerFactory tf0 = TransformerFactory.newInstance();
		        Transformer t0 = tf0.newTransformer();
		        DOMSource ds0 = new DOMSource(document0);
		        StreamResult sr0 = new StreamResult(new File("D:\\2n DAM\\M9\\xat\\paraules_" + usuari + ".xml"));
		        t0.transform(ds0, sr0);
	    	
	        
	        System.out.println("XML Generat");
    	}catch (Exception exm) {
    		
    		System.out.println("Error de connexio: " + exm);
    		
    	}
    }
    
    static void exportarXat(){
    	
    	ArrayList<String> llistaFrases = new ArrayList<String>();
    	
    	try {
    		
	    	MongoClient mongo = new MongoClient("192.168.1.22", 27017);
			DB database = mongo.getDB("deliverass");
			
			System.out.println("MongoDB Connectat!");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("usuari", usuari);
		    whereQuery.put("data", date);
		    System.out.println(date);
			
			DBCollection collection = database.getCollection("frases_usuaris");
			DBCursor cursor = collection.find(whereQuery);
			
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		    DocumentBuilder db = df.newDocumentBuilder();
		    Document document = db.newDocument();

	        // Element Arrel
		    Element arrel = document.createElement("xat");
		    document.appendChild(arrel);
			
	        int i = 1;
	        int fr = 1;
	        
			while(cursor.hasNext()) {
				
				llistaFrases.add(cursor.next().toString());  
				
				i++;
				fr++;
				
			}
			
			for(int ii = 0; ii < llistaFrases.size(); ii++) {
				System.out.println(llistaFrases);

				Element frasesXat = document.createElement("frases");
				frasesXat.appendChild(document.createTextNode(llistaFrases.toString()));
				arrel.appendChild(frasesXat);
			}
			
			TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer t = tf.newTransformer();
	        DOMSource ds = new DOMSource(document);
	        StreamResult sr = new StreamResult(new File("D:\\2n DAM\\M9\\xat\\xat_" + usuari + "_" + date + ".xml"));

	        t.transform(ds, sr);
			
			System.out.println("Fi de consulta.");
			
			
    	}catch (Exception exm) {
    		
    		System.out.println("Error de connexio: " + exm);
    		
    	}
    	
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
        	
        	System.out.println("El socket no s'ho creat correctament.");
        	
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
            	
                System.out.println("El socket no s'ha creat correctament.");
                connectat = false;
                
            }
        }
        
    }

    public static void main(String[] args) throws IOException {      
        
    	ClientXat c = new ClientXat();
        c.rebreMissatgesServidor();
        
    }

}