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
	public ArrayList<String> listeNomsObjetsManquants =new ArrayList<String>();
	public static ArrayList<String> listeRFID=new ArrayList<String>();
	public ArrayList<String> listeObjets=new ArrayList<String>();
	//public Main this;
	public ArrayList<String> listeLieu = new ArrayList<String>();;
	
	public Main (String bd, String compte, String motDePasse){
		
		 try {
	            //Enregistrement de la classe du driver par le driverManager
	            Class.forName("com.mysql.jdbc.Driver");
	            System.out.println("Driver trouv�...");
	           // this = new Main("sql11172522", "sql11172522", "Tclw7Ag8uh");
	            //Cr�ation d'une connexion sur la base de donn�e
	            this.conn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/" + bd, compte, motDePasse);
	            System.out.println("Connexion �tablie...");
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
	/*
	public static void main(String[] args) {
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//creation des objets
		BDRecVoc bdRecVoc = new BDRecVoc("sql11172522", "sql11172522", "Tclw7Ag8uh");
		Main main = new Main("sql11172522", "sql11172522", "Tclw7Ag8uh");
		
		//Interface myInterface = new Interface(800,800,main);  
		
		//recuperer le mot de l'interface
		//this.nomLieu = myInterface.tfMotASaisir.getText();
		
		bdRecVoc.selectObject("obligatoire");
		
		
		
		//ajout des objets qui ne sont pas oubliee (RFID)
  	    
	      //bdRFID.addToList("25");
	     // System.out.println("Arduino list:");
	      
	      //creation des listes
	      
	      ArrayList<String> listeObjets = bdRecVoc.getListeObjets();
	      main.displayList(listeObjets);
	}
	*/
	//methode qui detecte les objets qu'on a oublie et qu'on a sur soi
	public void compareLists(ArrayList<String> listeObjets, ArrayList<String> listeRFID){
		//displayList(listeObjets);
		//displayList(listeRFID);
		if(listeObjets.get(0)==null){
			listeObjets.add("asfg");
			
		}
		for (String itemObjets : listeObjets){
			boolean trouve = false;
			for (String itemRFID : listeRFID){
				if (itemObjets.equals(itemRFID)){
					trouve = true;
					for(int i =0;i<this.listeLieu.size();i++){
						ajouterObjetUtilise(itemObjets,listeLieu.get(i));
					}
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
	    			this.selectObjectStatement.setString(1,idObjets); 
	    			ResultSet result = this.selectObjectStatement.executeQuery();
	    		
	    			while (result.next()) {
	    				String s =result.getString("nomObjets");
	    				listeNomsObjetsManquants.add(s);
	    				
	    				
	    				//ajouterObjetUtilise(idObjets,"PARC");
	    				
	    				if(this.listeLieu.get(0)!=null){
		    				for(int i =0;i<this.listeLieu.size();i++){
								ajouterObjetUtilise(idObjets,this.listeLieu.get(i));
							}
	    				}
	    			}
	    			displayList(listeNomsObjetsManquants);
	    			
	    	} catch (SQLException ex) {
	    		ex.printStackTrace(System.err);
	    		System.exit(-1);
	    	}
	    }
	
	 //methode qui affiche les element d'une liste
	 public void displayList(ArrayList<String> list){
		 System.out.println("Display list:");
	     for(String item : list){   
	       		System.out.println(item + " manque");
	   	}
	 }
	
}
