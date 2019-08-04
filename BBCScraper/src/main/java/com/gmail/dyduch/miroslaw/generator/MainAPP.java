package com.gmail.dyduch.miroslaw.generator;

import com.gmail.dyduch.miroslaw.database.AppDB;
import com.gmail.dyduch.miroslaw.database.DB;
import com.gmail.dyduch.miroslaw.database.DBPodcasts;
import com.gmail.dyduch.miroslaw.scraper.Scraper;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class MainAPP {

	private MainAPP() throws IOException {

		// DBPodcasts dbps = null;

		//DB db = new DB("");
		DB db = new DB("postgresql"); //"", or "mysql"
		db.drop();

		db.create();
		// db.select(); // wyciaga select na db
		Scraper sc = new Scraper("http://www.bbc.co.uk/learningenglish/english/features/6-minute-english/");

		sc.getSubPage();
		db.insert(sc.getQuery()); // pobiera cale tablice scrapowana w formacie do wstawienia w DB (x,y,x) 
		// System.out.println(sc.getQuery());

		DBPodcasts dbps = db.getDBLinks(); //kopia tablicy w podcastami
		//dbps.reverseID(); //odwraca kolejnosc id najstarsza najmłodsza strona

		HtmlPage hp = new HtmlPage(3, dbps); // jj-kolumn
		//System.out.println(sc.getQuery());

		//zapis do pliku
		hp.writeToFile("WebContent/podcast.html");
		System.out.println("Hello World! database ;) ");
		
		XMLExport xmlData = new XMLExport(dbps, "WebContent/myfile.xml");
	}

	public static void main(String[] args) throws IOException {
		MainAPP ap = new MainAPP();
	}

}
