package fr.insalyon.p2i2.javaarduino.tdtp;

import java.io.BufferedReader;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ArrayList;
import fr.insalyon.p2i2.javaarduino.usb.ArduinoUsbChannel;
import fr.insalyon.p2i2.javaarduino.util.Console;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import jssc.SerialPortException;

public class JavaArduino {

    private Connection conn;
    private PreparedStatement insertObjectStatement;
    private PreparedStatement selectMesuresStatement;
    private ArrayList<String> listeRFID = new ArrayList<String>();
    private String port;
    
    public static void main(String[] args) {
    	JavaArduino jaarRFID = new JavaArduino();
    	jaarRFID.addToList("55555");
    	jaarRFID.displayList();
    }
    

    public JavaArduino() {
    	final Console console = new Console();
        
        console.log( "DEBUT du programme TestArduino !.." );
        
        
        
        do {
        
            console.log( "RECHERCHE d'un port disponible..." );
            port = ArduinoUsbChannel.getOneComPort();
            
            if (port == null) {
                console.log( "Aucun port disponible!" );
                console.log( "Nouvel essai dans 5s" );
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    // Ignorer l'Exception
                }
            }

        } while (port == null);
        
        port = "COM7";
        
        console.println("Connection au Port " + port);
        try {
            final ArduinoUsbChannel vcpChannel = new ArduinoUsbChannel(port);

            Thread readingThread = new Thread(new Runnable() {
                public void run() {
                    BufferedReader vcpInput = new BufferedReader(new InputStreamReader(vcpChannel.getReader()));
                    
                    String line;
                    try {

                        while ((line = vcpInput.readLine()) != null) {
                        	console.log(line);
                        	listeRFID.add(line); //ADAPTER POUR REC VOC
                        	displayList();
                        	System.out.println("display list 1");
                            //console.println(line);
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace(System.err);
                    }
                    
                }
            }
            );
            
            readingThread.start();
            
            vcpChannel.open();
            
            boolean exit = false;
            
            while (!exit) {
            
                String line = console.readLine("Envoyer une ligne (ou 'fin') > ");
                
                if (line.length() == 0) {
                    continue;
                }
                
                if ("fin".equals(line)) {
                    exit = true;
                    continue;
                }
                
                vcpChannel.getWriter().write(line.getBytes("UTF-8"));
                vcpChannel.getWriter().write('\n');
            
            }
            
            vcpChannel.close();
            
            readingThread.interrupt();
            try {
                readingThread.join(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } catch (SerialPortException ex) {
            ex.printStackTrace(System.err);
        }
        
    
    }
   

    public void displayList(){
       	for(String item : listeRFID)
       	{   
       		System.out.println(item);
    	}
    }
    
    public void addToList (String idObjets){
    	listeRFID.add(idObjets);
    }


	public ArrayList<String> getListeRFID() {
		return listeRFID;
	}


	public void setListeRFID(ArrayList<String> listeRFID) {
		this.listeRFID = listeRFID;
	}
	
	//public String traiteSring(String line){
		//String line2;
		//for(int i =0;)
	//}
	  
}