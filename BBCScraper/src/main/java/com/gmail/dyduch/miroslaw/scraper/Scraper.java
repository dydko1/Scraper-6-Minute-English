package com.gmail.dyduch.miroslaw.scraper;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Scraper {
	private String url;
	private List<String[]> podcasts = new ArrayList<String[]>();

	// Jsoup data
	private Document doc;
	private Elements links, media, keynote;

	public Scraper() {
		this(
				"http://127.0.0.1/learningenglish/english/features/6-minute-english/");
	}

	public Scraper(String url) {
		if (!url.isEmpty())
			this.url = url;
		else {
			System.out.println("Pusty link.: " + url);
			System.exit(0); //Mozna to ładniej obsłużyć
		}
	}

	/*	List<String[]> getPodcasts() {
			return podcasts;
		}*/

	public void print() {
		System.out.println("Main z Scraper" + url);
	}

	private void getMainPage() throws IOException {
		doc = Jsoup.connect(url).get();
		String[] query = { "div.text > h2 a[href]", "div.img [src$=.jpg]",
				"div.details" };
		links = doc.select(query[0]); // div with class=text
		media = doc.select(query[1]);
		keynote = doc.select(query[2]);

		for (int i = 0; i < links.size(); i++) {

			podcasts.add(new String[] { links.get(i).attr("abs:href"),
					links.get(i).text().replace("'", "''"),
					keynote.get(i).select("p").text().replace("'", "''"),
					media.get(i).attr("abs:src"), "", "", "" });
		}

		/*
		for (Element link : links) {
			System.out.println("link.:" + link.tagName() + " \t" + link.attr("abs:href") + "\t" + link.text());
		}
		
		for (Element src : media) {
			System.out.println("link.:" + src.tagName() + " \t" + src.attr("abs:src") + "\t" + src.text());
		}
		
		System.out.println("linkow.: " + links.size());
		System.out.println("obrazkow.: " + media.size());
		System.out.println("Main z Scraper" + url);
		System.out.println(query[1] + "\tdddddddddddddddddddddddddd\n");
		*/
	}

	public void getSubPage() throws IOException {
		String sTmp, sEpisode, sDate; // for example EPISODE 190509 / 09 MAY 2019
		getMainPage();

		for (int i = 0; i < podcasts.size(); i++) {
			//System.out.println(i + "\t" + podcasts.size() + "\t"					+ podcasts.get(i).length);

			doc = Jsoup.connect(podcasts.get(i)[0]).get();
			Elements pdfUrl = doc.getElementsByClass(
					"download bbcle-download-extension-pdf").select("a[href]");
			Elements mp3Url = doc.getElementsByClass(
					"download bbcle-download-extension-mp3").select("a[href]");
			Elements detail = doc.getElementsByClass("details").select("h3"); // > b
			sTmp = detail.first().text(); // 'EPISODE 190704 / 04 JUL 2019'
			sEpisode = sTmp.substring(8, 14); // extract episode '190704' 
			sDate = sTmp.substring(17); // extract data '04 JUL 2019'

			//podcasts.get(i)[3] = pdfUrl.attr("abs:href");
			podcasts.get(i)[4] = mp3Url.attr("abs:href");
			podcasts.get(i)[5] = pdfUrl.attr("abs:href");
			podcasts.get(i)[6] = sTmp;
			System.out.println("link nr.:\t" + i + "\t" + sTmp);
			// TimeUnit.MILLISECONDS.sleep(500);
			// System.out.println("Lekcja.:\t" + (i + 1) + "\t" + sDate);
			/*
			for (int j = 3; j < podcasts.get(i).length; j++) {
				System.out.println(i + "\t" + pdfUrl.attr("abs:href"));
				System.out.println(i + "\t" + mp3Url.attr("abs:href"));
			}
			*/
		}
	}

	public void getList() {
		int k = 250;
		if (podcasts.isEmpty())
			System.out.println("Lista adresów jest pusta");
		else {
			for (int j = 0; j < podcasts.get(k).length; j++)
				System.out.println(podcasts.get(k)[j] + "\t");
		}
		// podcasts.size();
		//		ArrayList<String> sentence = new ArrayList<String>();
		//		//sentence.add("fff","ggg");
		//		//sentence.add("fff1");
		//		//podcasts.add("ddd");
		//		podcasts.add(new String[] { "d", "e", "f", "" });
		//
		//		//podcasts.add(new String[] { "g", "h" });
		//		//podcasts.add(new String[] { "xxxxg", "hxxx", "xfdsfdsxxxg", "hx22323xx" });
		//		//podcasts.add(new String[] { "xxxxg", "hxxx", "xfdsfdsxxxg", "hx22323xx" });
	}

	public String getQuery() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < podcasts.size(); i++) {
			sb.append("(");
			for (int j = 0; j < podcasts.get(i).length; j++) {
				sb.append("'").append(podcasts.get(i)[j]).append("'");
				if (1 < podcasts.get(i).length - 1)
					sb.append(",");
			}
			sb.append("NOW())");
			if (1 < podcasts.size() - 1 & i < podcasts.size() - 1)
				sb.append(",\n");
		}
		//System.out.println("koniec" + "\t");

		return sb.toString(); //.replace("http://127.0.0.1/", "http://www.bbc.co.uk/"); //podmiana na bbc.co.uk
		/*		for (int i = 0; i < 10; i++)
					sb.append(i).append("  ");
				return sb.toString();*/
	}

	@Override
	public String toString() {
		return "Wywolanie Scraper toString";

	}

	public static void main(String[] args) throws IOException {
		//Scraper scraper = new Scraper("http://www.bbc.co.uk/learningenglish/english/features/6-minute-english/");
		Scraper scraper = new Scraper();
		scraper.toString();
		//scraper.print();
		//scraper.getList();
		//System.out.println(scraper.getpodcasts().size());
		//scraper.getpodcasts().get(3)[0]="fffff";
		scraper.getSubPage();
		System.out.println(scraper.getQuery());
		//scraper.getList();
		//System.out.println(scraper.getpodcasts().get(250)[3]);
		//System.out.println("Main z Scraper.: " + scraper.toString());
	}
}
