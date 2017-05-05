package fr.insalyon.p2i2.javaarduino.tdtp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestFlux {
    
    public static void main(String[] args) {
        
        TestFlux testFlux = new TestFlux();
        
        testFlux.sommeDepuisClavier();
        //testFlux.lireFichier("L:\\TDTP3\\mesures-input.txt");
        //testFlux.ecrireFichier("L:\\TDTP3\\output");
        
        
    }
    
    public void sommeDepuisClavier() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            
            int somme = 0;
            
            String ligne;
            System.out.print("Entrer un nombre: ");
            while ((ligne = input.readLine()) != null) {
                
                System.out.println("Ligne lue: >>>" + ligne + "<<<");
                
                if (ligne.equals("fin")) {
                    break;
                }
                
                // À compléter
                int nombre = Integer.parseInt(ligne);
                somme = somme + nombre;
                
                System.out.print("Entrer un autre nombre (ou 'fin'): ");
            }
            
            System.out.println("Somme: " + somme);
            
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
        
    }
    
    public void lireFichier(String cheminVersFichier) {
        try {
            // À compléter
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    new FileInputStream(cheminVersFichier)
                ));
            
            int nbLignes = 0;
            double somme = 0.0;
            
            String line;

            while ((line = input.readLine()) != null) {
                String[] valeurs = line.split(";");
                if (valeurs.length > 1) {
                    
                    // À compléter
                    Integer numInventaire = Integer.parseInt(valeurs[0]);
                    Double valeur = Double.parseDouble(valeurs[1]);
                    System.out.println("Le Capteur n°" + numInventaire + " a mesuré: " + valeur);
                    
                    nbLignes++;
                    // À compléter
                    somme = somme + valeur;
                }
            }
            
            input.close();
            
            System.out.println("Nombre de Lignes: " + nbLignes);
            System.out.println("Moyennes des Mesures: " + (somme/nbLignes));
            
            
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
        
    }
    
    public void ecrireFichier(String cheminVersDossier) {
        try {
            
            DecimalFormat formatNombreDecimal = new DecimalFormat("0.00");
            formatNombreDecimal.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ROOT));

            SimpleDateFormat formatDatePourNomFichier = new SimpleDateFormat("yyyyMMdd-HHmmss");
            
            String datePourNomFichier = formatDatePourNomFichier.format(new Date());
            String nomFichier = "nombres-output_" + datePourNomFichier + ".csv";
            
            // À compléter
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(cheminVersDossier + "/" + nomFichier)
                ));
            
            for (int i=0; i<250; i++) {
                
                double nombre = Math.random() * 100;
                // À compléter
                writer.println(formatNombreDecimal.format(nombre));
            }
            
            writer.close();
            
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
        
    }

}
