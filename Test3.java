package fr.insalyon.p2i2.javaarduino.tdtp;


import java.util.ArrayList;

import fr.insalyon.p2i2.javaarduino.tdtp.BDRecVoc;

public class Test3 {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		BDRecVoc bdRecVoc = new BDRecVoc("sql11172522", "sql11172522", "Tclw7Ag8uh");
		bdRecVoc.selectObject("parc");
		ArrayList<String> listeObjets = bdRecVoc.getListeObjets();
       	for(String item : listeObjets){   
       		System.out.println(item);
    	}
	}
}
