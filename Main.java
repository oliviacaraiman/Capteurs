package fr.insalyon.p2i2.javaarduino.tdtp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;

public class Main {
	
	private Connection conn;
    private PreparedStatement selectObjectStatement;
    private PreparedStatement insertObjetOublieStatement;
    private PreparedStatement insertObjetUtiliseStatement;
	public BDRecVoc bdRecVoc;
	public BDRFID bdRFID;
	public ArrayList<String> listeNomsObjetsManquants;
	public ArrayList<String> listeRFID;
	public ArrayList<String> listeObjets;
	public Main mainClass;
	public String nomLieu = "cours";
	
	public Main (String bd, String compte, String motDePasse){
		
		 try {
	            //Enregistrement de la classe du driver par le driverManager
	            Class.forName("com.mysql.jdbc.Driver");
	            System.out.println("Driver trouvé...");

	            //Création d'une connexion sur la base de donnée
	            this.conn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/" + bd, compte, motDePasse);
	            System.out.println("Connexion établie...");
	            this.selectObjectStatement= this.conn.prepareStatement("SELECT * FROM Objets where idObjets=?;");
	            this.insertObjetOublieStatement = this.conn.prepareStatement("INSERT INTO ObjetsOublies (idObjets,lieu,dateOubli) VALUES (?,?,?) ;");
	            this.insertObjetUtiliseStatement = this.conn.prepareStatement("INSERT INTO ObjetsUtilises (idObjets,lieu,dateUtilisation) VALUES (?,?,?) ;");

		 }catch (Exception ex) {
	            ex.printStackTrace(System.err);
	            System.exit(-1);
	        }
	    }
	
	public Main() {
		
	}
	
	public static void main(String[] args) {
		//creation des objets
		BDRecVoc bdRecVoc = new BDRecVoc("sql11172522", "sql11172522", "Tclw7Ag8uh");
		Main mainClass = new Main("sql11172522", "sql11172522", "Tclw7Ag8uh");
		BDRFID bdRFID = new BDRFID();
		Interface myInterface = new Interface(800,800,mainClass);  
		
		//recuperer le mot de l'interface
		//mainClass.nomLieu = myInterface.tfMotASaisir.getText();
		System.out.println("Mot:" + mainClass.nomLieu);
		
		bdRecVoc.selectObject(mainClass.nomLieu);
		
		//ajout des objets qui ne sont pas oubliee (RFID)
  	    bdRFID.addToList("12345");
	      //bdRFID.addToList("25");
	      System.out.println("Arduino list:");
	      bdRFID.displayList();
	      
	      //creation des listes
	      ArrayList<String> listeRFID = bdRFID.getListeRFID();
	      ArrayList<String> listeObjets = bdRecVoc.getListeObjets();
	      mainClass.compareLists(listeObjets,listeRFID);
	}
	
	//methode qui detecte les objets qu'on a oublie et qu'on a sur soi
	public void compareLists(ArrayList<String> listeObjets, ArrayList<String> listeRFID){
		//displayList(listeObjets);
		//displayList(listeRFID);
		for (String itemObjets : listeObjets){
			boolean trouve = false;
			for (String itemRFID : listeRFID){
				if (itemObjets.equals(itemRFID)){
					trouve = true;
					ajouterObjetUtilise(itemObjets,nomLieu);
				}
			}
			if (!trouve) {
				objetManquant(itemObjets);
			}
		}
	}
	
	//methode qui ajoute les objets oublies dans le table "ObjetsOublies"
	public int ajouterObjetOublie(String idObjet, String lieu) {
        try {
        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            this.insertObjetOublieStatement.setString(1, idObjet);
            this.insertObjetOublieStatement.setString(2, lieu);
            this.insertObjetOublieStatement.setTimestamp(3,timestamp);
           // System.out.println("Id " + idObjet + " manque");
            return this.insertObjetOublieStatement.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            return -1;
        }
    }
	
	//methode qui ajoute les objets qu'on n'a pas oublie dans le table "ObjetsUtilises"
	public int ajouterObjetUtilise(String idObjet, String lieu) {
        try {
        	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            this.insertObjetUtiliseStatement.setString(1, idObjet);
            this.insertObjetUtiliseStatement.setString(2, lieu);
            this.insertObjetUtiliseStatement.setTimestamp(3,timestamp);
            //System.out.println("Id " + idObjet + " added");
            return this.insertObjetUtiliseStatement.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            return -1;
        }
    }
	
	 public void objetManquant(String idObjets){
	    	try{
	    			listeNomsObjetsManquants = new ArrayList<String>();
	    			this.selectObjectStatement.setString(1,idObjets); 
	    			ResultSet result = this.selectObjectStatement.executeQuery();
	    		
	    			while (result.next()) {
	    				String s =result.getString("nomObjets");
	    				listeNomsObjetsManquants.add(s);
	    				ajouterObjetOublie(idObjets,nomLieu);
	    			}
	    			displayList(listeNomsObjetsManquants);
	    			
	    	} catch (SQLException ex) {
	    		ex.printStackTrace(System.err);
	    		System.exit(-1);
	    	}
	    }
	
	 //methode qui affiche les element d'une liste
	 public void displayList(ArrayList<String> list){
	     for(String item : list){   
	       		System.out.println(item + " manque");
	   	}
	 }
	
}
