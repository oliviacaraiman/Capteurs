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
    private static ArrayList<String> listeRFID = new ArrayList<String>();
    private static ArrayList<String> listeVR = new ArrayList<String>();
    private String port,port2;
    
    public static void main(String[] args) {
    	JavaArduino jaarRFID = new JavaArduino();
    	jaarRFID.addToList("55555");
    	jaarRFID.displayList(listeVR);
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
        port2 = "COM3";
        
        console.println("Connection au Port " + port+" et "+port2);
        try {
            final ArduinoUsbChannel vcpChannel = new ArduinoUsbChannel(port);
            final ArduinoUsbChannel vcpChannel2 = new ArduinoUsbChannel(port2);
            
            Thread readingThreadVR = new Thread(new Runnable() {
                public void run() {
                    BufferedReader vcpInput2 = new BufferedReader(new InputStreamReader(vcpChannel2.getReader()));
                    
                    String line;
                    try {

                        while ((line = vcpInput2.readLine()) != null) {
                        	console.log(line);
                        	listeVR.add(line); //ADAPTER POUR REC VOC
                        	displayList(listeVR);
                        	System.out.println("display list 1");
                            //console.println(line);
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace(System.err);
                    }
                    
                }
            }
            );
            
            Thread readingThread = new Thread(new Runnable() {
                public void run() {
                    BufferedReader vcpInput = new BufferedReader(new InputStreamReader(vcpChannel.getReader()));
                    
                    String line;
                    try {

                        while ((line = vcpInput.readLine()) != null) {
                        	console.log(line);
                        	listeRFID.add(line); //ADAPTER POUR REC VOC
                        	displayList(listeRFID);
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
            readingThreadVR.start();
            vcpChannel.open();
            vcpChannel2.open();

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
                vcpChannel2.getWriter().write(line.getBytes("UTF-8"));
                vcpChannel2.getWriter().write('\n');
            
            }
            
            vcpChannel.close();
            vcpChannel2.close();

            readingThread.interrupt();
            readingThreadVR.interrupt();

            try {
                readingThread.join(1000);
                readingThreadVR.join(1000);

            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } catch (SerialPortException ex) {
            ex.printStackTrace(System.err);
        }
        
    
    }
   

    public void displayList(ArrayList<String> liste){
       	for(String item : liste)
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