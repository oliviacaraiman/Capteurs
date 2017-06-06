package fr.insalyon.p2i2.javaarduino.tdtp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

	public class Interface extends JFrame {


	   
	    public JTextArea textArea = new JTextArea("Objets manquants: \n", 20, 20);
	    public Main mainClass = new Main();

		/**
	     * Constructeur de la classe UneFenetre
	     */
	    public Interface(int lg, int larg, Main mainClass) {

	        //on definit le nom de la fenetre
	        super("Interface pour la Reconnaissance Vocale");

	        //Dimensions de la fenetre graphique et fermeture
	        this.setSize(new Dimension(lg,larg));
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.mainClass = mainClass;

	        //Création des éléments visibles sur la fenetre
	        //Ici on n'a que le bouton
	        
	        JButton bouttonACliquer = new JButton("Afficher les objets");
	        bouttonACliquer.addActionListener(new EcouteurMotASaisir(this));
	   

	        //Création d'un conteneur principal
	        JPanel conteneurPrincipal = new JPanel(new FlowLayout());
	        this.setContentPane(conteneurPrincipal);
	        
	        conteneurPrincipal.add(bouttonACliquer);
	        conteneurPrincipal.add(textArea);
	        conteneurPrincipal.add(new Pannel(this));
	        
	        
	        //Rendre la fenêtre visible
	        this.setVisible(true);
	    }

	   
}
