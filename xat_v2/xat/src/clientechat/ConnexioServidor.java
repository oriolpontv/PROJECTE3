package clientechat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import java.sql.*;
import com.mongodb.MongoClient; 
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class ConnexioServidor implements ActionListener {
    
    private Socket socket; 
    private JTextField tfMissatge;
    private String usuari;
    private String missatgeFiltrat;
    
    static String frase = null;
	static String paraula = null;
	static String pattern = "yyyy-MM-dd HH:mm:ss";
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	static String date = simpleDateFormat.format(new Date());
	static Calendar cal = new GregorianCalendar();
	static String db_url = "jdbc:postgresql://192.168.1.22/Deliverass";
	static String db_user = "postgres";
	static String db_password = "Fat/3232";
    
    private DataOutputStream sortidaDades;
    
    public ConnexioServidor(Socket socket, JTextField tfMissatge, String usuari) {
    	
        this.socket = socket;
        this.tfMissatge = tfMissatge;
        this.usuari = usuari;
        
        try {
        	
            this.sortidaDades = new DataOutputStream(socket.getOutputStream());
            
        } catch (IOException ex) {
        	
        	System.out.println("Error al crear el stream de sortida : " + ex.getMessage());
        	
        } catch (NullPointerException ex) {
        	
        	System.out.println("El socket no s'ha creat correctament. ");
        	
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	
        try {
        	
        	String pattern = "yyyy-MM-dd";
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        	String date = simpleDateFormat.format(new Date());
        	
        	missatgeFiltrat = tfMissatge.getText();
        
        	System.out.println(missatgeFiltrat);
        	
        	String url = "jdbc:postgresql://192.168.1.22/Deliverass";
    	    String user = "postgres";
    	    String password = "Fat/3232";
    	    String QUERY = "SELECT paraula FROM black_list_paraules";
    	    ArrayList<String> list = new ArrayList<String>();
    	    
    	    try (Connection connection = DriverManager.getConnection(url, user, password);

    				PreparedStatement preparedStatement = connection.prepareStatement(QUERY);) {
    				ResultSet rs = preparedStatement.executeQuery();

    				while (rs.next()) {
    					
    					list.add(rs.getString("paraula"));  
    					
    				}
    				
    			} catch (SQLException ex) {
    				
    				System.out.println("Error: " + ex);
    				
    			}
    			
    			frase = missatgeFiltrat;
    			String[] split = frase.split(" ");
    			
    			for (int i=0; i<split.length; i++) {
    				
    				if(list.contains(split[i])) {
    					
    					paraula = split[i];
    					frase = frase.replaceAll(censorWords(split[i]),"*");
    					
    				}
    				
    			}
    			
    			//INSERTEM LA PARAULA AL MONGODB
    			MongoClient mongo2 = new MongoClient("localhost", 27017);
    			MongoDatabase database2 = mongo2.getDatabase("deliverass");
    			Document document2 = new Document();
    			document2.append("paraula", frase);
    			document2.append("usuari", usuari);
    			document2.append("data", date);
    			database2.getCollection("frases_usuaris").insertOne(document2);
    			System.out.println("Frase registrada correctament.");
    			
    			//COMPROVEM SI HI HA PARAULA MALSONANT
    			if(paraula != null) {
    				
    				String query = "INSERT INTO usuaris_paraules (paraula, usuari, datahora) VALUES ('" + paraula + "','" + usuari + "','" + date + "')";
    				
    				//INSERTEM LA PARAULA AL MONGODB
    				MongoClient mongo = new MongoClient("localhost", 27017);
    				MongoDatabase database = mongo.getDatabase("deliverass");
    				Document document = new Document();
    				document.append("paraula", paraula);
    				document.append("usuari", usuari);
    				document.append("data", date);
    				document.append("tipus", "Ofensiva");
    				database.getCollection("paraules_usuaris").insertOne(document);
    				System.out.println("Paraula registrada correctament.");
    				
    				try (Connection connection = DriverManager.getConnection(db_url, db_user, db_password);

    					PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    					preparedStatement.executeUpdate();
    					
    				} catch (SQLException ex) {

    					System.out.println(ex);
    					
    				}
    				
    			}
        	
        	sortidaDades.writeUTF("[" + date + "] - " + usuari + ": " + frase);
            tfMissatge.setText("");
            
        } catch (IOException ex) {
        	
            System.out.println("Error al intentar enviar un missatge: " + ex.getMessage());
            
        }
    }
    
    //METODE PER CENSURAR LLETRE A PARTIR DEL PRIMER CARACTER
    public static String censorWords(String... words) {
		
	    StringBuilder sb = new StringBuilder();
	    
	    for (String w : words) {
	    	
	        if (sb.length() > 0) sb.append("|");
	        sb.append(
	           String.format("(?<=(?=%s).{1,%d}).",
	              Pattern.quote(w),
	              w.length()-1
	           )
	        );
	        
	    }
	    
	    return sb.toString();
	    
	}
    
}