package fr.insalyon.p2i2.javaarduino.tdtp;

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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class BDFlux {

    private Connection conn;
    private PreparedStatement insertObjectStatement;
    private PreparedStatement selectMesuresStatement;

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
            this.insertObjectStatement = this.conn.prepareStatement("INSERT INTO Objets (idObjet,nomObjet,tag) VALUES (?,?,?) ;");

        } catch (Exception ex) {
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
        try {

            String line;

            while ((line = input.readLine()) != null) {
                String[] valeurs = line.split(";");
                if (valeurs.length > 1) {

                    // À compléter
                    Integer numInventaire = Integer.parseInt(valeurs[0]);
                    Double valeur = Double.parseDouble(valeurs[1]);
                    System.out.println("Le Capteur n°" + numInventaire + " a mesuré: " + valeur);

                    ajouterMesure(numInventaire, valeur, new Date());
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }

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

    public void ecrireMesures(PrintWriter output, int numInventaire, Date dateDebut, Date dateFin) {

        try {

            this.selectMesuresStatement.setInt(1, numInventaire);
            this.selectMesuresStatement.setTimestamp(2, new Timestamp(dateDebut.getTime())); // DATETIME
            this.selectMesuresStatement.setTimestamp(3, new Timestamp(dateFin.getTime())); // DATETIME
            ResultSet result = this.selectMesuresStatement.executeQuery();

            SimpleDateFormat formatDatePourCSV = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DecimalFormat formatNombreDecimal = new DecimalFormat("0.00");
            formatNombreDecimal.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ROOT));

            while (result.next()) {

                output.println(
                        result.getInt("numInventaire") + ";"
                        + formatDatePourCSV.format(new Date(result.getTimestamp("dateMesure").getTime())) + ";"
                        + formatNombreDecimal.format(result.getDouble("valeur")) + ";"
                );

            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    public void ecrireMesuresDansFichier(String cheminVersDossier, int numInventaire, Date dateDebut, Date dateFin) {

        try {
            SimpleDateFormat formatDatePourNomFichier = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String datePourNomFichier = formatDatePourNomFichier.format(new Date());
            String nomFichier = "mesures-output_" + datePourNomFichier + ".csv";

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(cheminVersDossier + "\\" + nomFichier)
            ));

            ecrireMesures(writer, numInventaire, dateDebut, dateFin);

            writer.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}
