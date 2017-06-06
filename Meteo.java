package fr.insalyon.p2i2.javaarduino.tdtp;

import java.io.*;
import java.net.*;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Meteo {
	public static String tagMeteo(){
		String tagPluie="";
		try{
			String chaine="";
			
			//RECUPERATION DONNEES METEO
			URL url = new URL("https://api.darksky.net/forecast/ea4c8dfa83dc8a5d148ce6ea2237a019/51.4800000,-3.1800000?exclude=daily,minutely,hourly");
			URLConnection con=url.openConnection();
			System.out.println(con.getContent());
			InputStream input = con.getInputStream();
			while(input.available()>0){
				chaine+=(char)input.read();  // �crire une ligne dans le fichier resultat.txt
			} 
    
			//RECUPERATION DE LA CHAINE
			Pattern p = Pattern.compile(".*\"summary\"(.*)\"icon\".*");
			Matcher m = p.matcher(chaine);
			
				//System.out.println(m.group(1));
			
			//DETECTION DU MOT RAIN DANS LA CHAINE
			String [] mots = {"Rain","rain","Drizzle","drizzle"};
			
			for (int i=0; i<mots.length; i++){
				int pos = chaine.indexOf(mots[i]);
				if (pos >= 0) 
					tagPluie="pluie";
			}
		}
		catch(MalformedURLException e){System.out.println(e);}
		catch(IOException e){System.out.println(e);}
		catch(PatternSyntaxException pse){System.err.println("Le pattern n'a pas un format correct.");}
		return tagPluie;
	}
	
	
	
	
	
	


}