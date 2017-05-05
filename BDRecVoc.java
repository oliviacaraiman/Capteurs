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

public class BDRecVoc {

    private Connection conn;
    private PreparedStatement insertObjectStatement;
    private PreparedStatement selectMesuresStatement;
    public ArrayList<String> listeObjets = new ArrayList<String>();
    
   // public static void main(String[] args) {

  //      BDRecVoc bdRecVoc = new BDRecVoc("sql11172522", "sql11172522", "Tclw7Ag8uh");
        
        //bdFlux.ajouterObjet(26, "ObjetTag4", "parc,chien", new GregorianCalendar(2017, Calendar.MAY, 5).getTime() );
      //bdFlux.ajouterMesure(60,"blbla","[vwenfw]");
   //   bdRecVoc.selectObject("parc");
        
        //bdFlux.lireMesuresDepuisFichier("L:\\TDTP3\\mesures-input.txt");

//        bdFlux.ecrireMesures(
//                new PrintWriter(System.out, true),
//                1,
//                new GregorianCalendar(2016, Calendar.MAY, 8).getTime(),
//                new GregorianCalendar(2016, Calendar.MAY, 9).getTime()
//            );
//        
//        bdFlux.ecrireMesuresDansFichier(
//                "L:\\TDTP3\\output",
//                1,
//                new GregorianCalendar(2016, Calendar.MAY, 8).getTime(),
//                new GregorianCalendar(2016, Calendar.MAY, 9).getTime()
//            );

//    }

    public BDRecVoc(String bd, String compte, String motDePasse) {
        try {

            //Enregistrement de la classe du driver par le driverManager
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver trouvé...");

            //Création d'une connexion sur la base de donnée
            this.conn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/" + bd, compte, motDePasse);
            //this.conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/" + bd, compte, motDePasse);
            System.out.println("Connexion établie...");

            // Prepared Statement
            this.insertObjectStatement = this.conn.prepareStatement("INSERT INTO Objets (idObjets,nomObjets,tags, dateUtilisationObjets) VALUES (?,?,?,?) ;");
            this.selectMesuresStatement = this.conn.prepareStatement("SELECT * FROM Objets where tags like ? ;");
            	
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    public void displayList(ArrayList<String> list){
       	for(String item : list)
       	{   
       		System.out.println(item);
    	}
    	}
    	    
    public void selectObject(String tag){
    	try{
    			listeObjets.clear();
    			this.selectMesuresStatement.setString(1,"%" + tag + "%"); // DATETIME
    			ResultSet result = this.selectMesuresStatement.executeQuery();
    
    			while (result.next()) {
    				String s =result.getString("idObjets");
    				listeObjets.add(s);
    			}
    			displayList(listeObjets);
     
    	} catch (SQLException ex) {
    		ex.printStackTrace(System.err);
    		System.exit(-1);
    	}
    }

   

    public int ajouterObjet(String idObjet, String nomObjet, String tags, Date dateUtil) {
        try {
            this.insertObjectStatement.setString(1, idObjet);
            this.insertObjectStatement.setString(2, nomObjet);
            this.insertObjectStatement.setString(3,tags);
            this.insertObjectStatement.setTimestamp(4,new Timestamp(dateUtil.getTime()));
            System.out.println("Id " + idObjet + " added");
            return this.insertObjectStatement.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            return -1;
        }
    }

	public ArrayList<String> getListeObjets() {
		return listeObjets;
	}

	public void setListeObjets(ArrayList<String> listeObjets) {
		this.listeObjets = listeObjets;
	}
    
}
  
