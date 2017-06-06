package fr.insalyon.p2i2.javaarduino.tdtp;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Pannel extends JPanel {
	private Interface myInterface; 
	private ArrayList<String> listeObjets = new ArrayList<String>();
	public JTextArea textArea = new JTextArea("Objets manquants: \n", 20, 20);
	
	public Pannel(Interface myInt){
		this.myInterface = myInt;
		this.affichage();
	}

	public void affichage(){
		//recuperation de la liste des objets manquants
				listeObjets = myInterface.mainClass.listeNomsObjetsManquants;
				System.out.println(listeObjets.size());
				// affichage des objets manquant
				System.out.println("Interf Pannel:");
				for(String item : listeObjets){   
					System.out.println(item);
		       		textArea.append(item + "\n");
				}
	}
		
		
	
}
