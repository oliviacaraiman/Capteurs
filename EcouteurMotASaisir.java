package fr.insalyon.p2i2.javaarduino.tdtp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EcouteurMotASaisir implements ActionListener {

	private Interface myInterface; 
	private ArrayList<String> listeObjets = new ArrayList<String>();
	
	
	public EcouteurMotASaisir(Interface myInt){
		this.myInterface = myInt;
	}
	
	public void actionPerformed(ActionEvent e) {
		//recuperation de la liste des objets manquants
		listeObjets = myInterface.mainClass.listeNomsObjetsManquants;
		System.out.println(listeObjets.size());
		// affichage des objets manquant
		System.out.println("Interf:");
		for(String item : listeObjets){   
			System.out.println(item);
       		myInterface.textArea.append(item + "\n");
		}
		
	}

}
