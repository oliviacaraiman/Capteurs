package fr.insalyon.p2i2.javaarduino.tdtp;

import java.util.List;
import java.io.BufferedReader;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class BDFlux {

    private Connection conn;
    private PreparedStatement insertObjectStatement;
    private PreparedStatement selectMesuresStatement;
    private ArrayList<String> listeObjets = new ArrayList<String>();

    public static void main(String[] args) {

        BDFlux bdFlux = new BDFlux("sql11172522", "sql11172522", "Tclw7Ag8uh");
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
        //bdFlux.ajouterMesure(60,"blbla","[vwenfw]");
        bdFlux.selectObject("parc");

    }
    
   

    public BDFlux(String bd, String compte, String motDePasse) {
        try {

            //Enregistrement de la classe du driver par le driverManager
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver trouvé...");

            //Création d'une connexion sur la base de donnée
            this.conn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/" + bd, compte, motDePasse);
            //this.conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/" + bd, compte, motDePasse);
            System.out.println("Connexion établie...");

            // Prepared Statement
            this.insertObjectStatement = this.conn.prepareStatement("INSERT INTO Objets (idObjets,nomObjets,tags) VALUES (?,?,?) ;");
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
        String s =result.getString("nomObjets") +"; "+ result.getString("tags");
        listeObjets.add(s);
        }
        displayList(listeObjets);
        
    	} catch (SQLException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
    }
    
    public void lireMesuresDepuisFichier(String cheminVersFichier) {
        try {
            // À compléter
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    new FileInputStream(cheminVersFichier)
            ));

            lireMesures(input);

            input.close();

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }

    }

    public void lireMesures(BufferedReader input) {
      //  try {

//            String line;
//
//            while ((line = input.readLine()) != null) {
//                String[] valeurs = line.split(";");
//                if (valeurs.length > 1) {
//
//                    // À compléter
//                    Integer numInventaire = Integer.parseInt(valeurs[0]);
//                    Double valeur = Double.parseDouble(valeurs[1]);
//                    System.out.println("Le Capteur n°" + numInventaire + " a mesuré: " + valeur);

                    ajouterMesure(20, "fejwkn", "fewknfw");
   //             }
    //        }

//        } catch (IOException ex) {
//            ex.printStackTrace(System.err);
//            System.exit(-1);
//        }

    }

    public int ajouterMesure(int idObjet, String valeur, String tag) {
        try {
            this.insertObjectStatement.setInt(1, idObjet);
            this.insertObjectStatement.setString(2, valeur);
            this.insertObjectStatement.setString(3,tag); // DATETIME
            return this.insertObjectStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            return -1;
        }
    }


}
