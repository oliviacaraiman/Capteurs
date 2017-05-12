package fr.insalyon.p2i2.javaarduino.tdtp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
	
	private Connection conn;
    private PreparedStatement selectObjectStatement;
	public BDRecVoc bdRecVoc;
	public BDRFID bdRFID;
	public ArrayList<String> listeNomsObjetsManquants;
	public ArrayList<String> listeRFID;
	public ArrayList<String> listeObjets;
	public Main mainClass;
	
	public Main (String bd, String compte, String motDePasse){
		 try {
	            //Enregistrement de la classe du driver par le driverManager
	            Class.forName("com.mysql.jdbc.Driver");
	            System.out.println("Driver trouvé...");

	            //Création d'une connexion sur la base de donnée
	            this.conn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/" + bd, compte, motDePasse);
	            //this.conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/" + bd, compte, motDePasse);
	            System.out.println("Connexion établie...");
	            this.selectObjectStatement= this.conn.prepareStatement("SELECT * FROM Objets where idObjets=?;");
		 }catch (Exception ex) {
	            ex.printStackTrace(System.err);
	            System.exit(-1);
	        }
	    }
	
	public static void main(String[] args) {
		BDRecVoc bdRecVoc = new BDRecVoc("sql11172522", "sql11172522", "Tclw7Ag8uh");
		Main mainClass = new Main("sql11172522", "sql11172522", "Tclw7Ag8uh");
		
	      bdRecVoc.selectObject("parc");
	      BDRFID bdRFID = new BDRFID();
	      bdRFID.addToList("22");
	      bdRFID.addToList("25");
	      ArrayList<String> listeRFID = bdRFID.getListeRFID();
	      ArrayList<String> listeObjets = bdRecVoc.getListeObjets();
	      mainClass.compareLists(listeObjets,listeRFID);
	}
	
	public void compareLists(ArrayList<String> listeObjets, ArrayList<String> listeRFID){
		//displayList(listeObjets);
		//displayList(listeRFID);
		for (String itemObjets : listeObjets){
			boolean trouve = false;
			for (String itemRFID : listeRFID){
				if (itemObjets.equals(itemRFID)){
					trouve = true;
				}
			}
			if (!trouve) {
				objetManquant(itemObjets);
			}
		}
	}
	
	 public void objetManquant(String idObjets){
	    	try{
	    		//for (String itemObjets : listeManquant){
	    			listeNomsObjetsManquants = new ArrayList<String>();
	    			this.selectObjectStatement.setString(1,idObjets); 
	    			ResultSet result = this.selectObjectStatement.executeQuery();
	    		
	    			while (result.next()) {
	    				String s =result.getString("nomObjets");
	    				listeNomsObjetsManquants.add(s);
	    			}
	    			System.out.println("vkjend");
	    			displayList(listeNomsObjetsManquants);
	    		//}
	    	} catch (SQLException ex) {
	    		ex.printStackTrace(System.err);
	    		System.exit(-1);
	    	}
	    }
	
	 public void displayList(ArrayList<String> list){
	     for(String item : list){   
	       		System.out.println(item);
	   	}
	 }
	
}
