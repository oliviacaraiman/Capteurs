package fr.insalyon.p2i2.javaarduino.tdtp;

import java.util.ArrayList;

public class Main {
	public BDRecVoc bdRecVoc;
	public BDRFID bdRFID;
	public ArrayList<String> listeManquant;
	public Main mainClass;
	
	public static void main(String[] args) {
		BDRecVoc bdRecVoc = new BDRecVoc("sql11172522", "sql11172522", "Tclw7Ag8uh");
		ArrayList<String> listeManquant = new ArrayList<String>();
		Main mainClass = new Main();
		mainClass.compareLists();
	      bdRecVoc.selectObject("parc");
	      BDRFID bdRFID = new BDRFID();
	      
	}
	
	public void compareLists (){
		ArrayList<String> listeObjets = bdRecVoc.getListeObjets();
		ArrayList<String> listeRFID = bdRFID.getListeRFID();
		
		for (String itemObjets : listeObjets){
			boolean trouve = false;
			for (String itemRFID : listeRFID){
				if (itemObjets.equals(itemRFID)){
					trouve = true;
				}
			}
			if (!trouve) {
				listeManquant.add(itemObjets);
			}
		}
	}
	
	 public void displayList(ArrayList<String> list){
	     for(String item : list){   
	       		System.out.println(item);
	   	}
	 }
	
}
