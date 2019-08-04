package com.gmail.dyduch.miroslaw.scraper;

import java.io.IOException;

import com.gmail.dyduch.miroslaw.database.DB;

public class App {
	private App() throws IOException {
		DB db1 = new DB("postgresql");
		//DB db1=new DB("mysql");
		//DB db1=new DB();
		//db1.drop();
		Scraper scraper = new Scraper();

		scraper.getSubPage();
		db1.create();
		db1.insert(scraper.getQuery());
		db1.select();
		//db1.drop();
		System.out.println("Test z KONSTRUKTOREM z DB");
	}

	public static void main(String[] args) throws IOException {
		App ap = new App();
		System.out.println("Hello World! Scraper");
	}
}

/*
Scraper scraper=new Scraper("http://127.0.0.1/learningenglish/english/features/6-minute-english/");
Scraper scraper = new Scraper("http://www.bbc.co.uk/learningenglish/english/features/6-minute-english/");
*/