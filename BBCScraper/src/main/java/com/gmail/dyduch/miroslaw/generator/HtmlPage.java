package com.gmail.dyduch.miroslaw.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.print.attribute.standard.PDLOverrideSupported;

import com.gmail.dyduch.miroslaw.database.DB;
import com.gmail.dyduch.miroslaw.database.DBPodcast;
import com.gmail.dyduch.miroslaw.database.DBPodcasts;
import com.gmail.dyduch.miroslaw.scraper.Scraper;
import com.webfirmframework.wffweb.tag.html.DocType;
import com.webfirmframework.wffweb.tag.html.tables.Td;
import com.webfirmframework.wffweb.tag.htmlwff.TagContent;

import j2html.TagCreator;
import j2html.attributes.Attr.ShortForm;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.DomContentJoiner;
import j2html.tags.EmptyTag;
import j2html.tags.Tag;
import static j2html.TagCreator.*;

public class HtmlPage {
	int jj = 0, n = 0;
	DBPodcasts dbps = null;

	public HtmlPage() {
		// TODO Auto-generated constructor stub
		//System.out.println("fff");
		this(1, null);
	}

	public HtmlPage(int jj, DBPodcasts dbps) {
		this.jj = jj;
		this.dbps = dbps;

		if (jj < 1) {
			System.out.println("Niepoprawna liczba kolumn < 0 !!! ");
			System.exit(0); //Mozna to ładniej obsłużyć
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); // tak musi byc tutaj bo inaczej jest czego 2x - n razy
		sb.append(document().render()).append("\r\n");
		sb.append(genPage().renderFormatted());

		return sb.toString();
	}

	private ContainerTag genPage() {
		return html(head(myHead(), body(myBody(), myTable(), myFooter())));
	}

	private ContainerTag myHead() {
		return head(
				title("Scraper- 6 Minute English"),
				meta().withName("generator").withContent("j2html"),
				meta().withCharset("utf-8"),
				meta().withLang("pl-PL"),
				meta().withName("author").withContent("Miroslaw Dyduch"),
				link().withRel("stylesheet").withHref("main.css")
						.withType("text/css"));
	}

	private ContainerTag myBody() {
		return TagCreator
				.main(attrs(".Naglowek"),
						h2("Wyniki scrapowanie strony BBC"),
						h1(a("6 Minute English")
								.withHref(
										"http://www.bbc.co.uk/learningenglish/english/features/6-minute-english/")
								.withTitle("Naciśnij link")));

	}

	private ContainerTag myTable() {
		//List<Integer> iColumn = Arrays.asList(1, 3, 4);
		int ii = 0;
		n = dbps.getDbPodcasts().size(); //elementow

		if (jj > n)
			jj = n;

		if ((n % jj) == 0)
			ii = (n / jj);
		else
			ii = (n / jj) + 1;

		return (table(attrs(".blueTable"), thead(myT1Thead(jj)),
				tfoot(myT1Foot(jj)), tbody(myT1RowCols(ii, jj))));
	}

	private ContainerTag[] myT1RowCols(int iRows, int jColumns) {
		ContainerTag[] myRowColsTags = new ContainerTag[iRows];
		ContainerTag[] myColumnsTags = new ContainerTag[jColumns];
		int nTmp = 0;
		DBPodcast dbp = null;

		for (int i = 0; i < iRows; i++) {
			for (int j = 0; j < jColumns; j++) {
				//System.out.println("i=" + i + ", j=" + j + ", nTmp=" + nTmp);
				if (nTmp < n) {
					//dbp = null; // test
					dbp = dbps.getDbPodcasts().get(nTmp);
					myColumnsTags[j] = td(
							a(
									img().withSrc(dbp.getImgURL())
											.withAlt("brak obrazka")
											.withTitle(
													"Przejdź do strony BBC 6 Minute English"))
									.withHref(dbp.getLessonURL()),
							h3(a(
									String.valueOf(dbps.getDbPodcasts()
											.get(nTmp).getId())
											+ ". " + dbp.getTitle()).withHref(
									dbp.getLessonURL()).withTitle(
									"Przejdź do strony BBC 6 Minute English")),
							h1(dbp.getKeynote()),
							h2(dbp.getEpisode()),
							table(attrs(".download"),
									h2(tr(td(
											a(
													img().withSrc("img/mp3.png")
															.withAlt(
																	"brak obrazka")
															.withTitle(
																	"Pobierz mp3"))
													.withHref(dbp.getMp3Url()),
											td(a(
													img().withSrc("img/pdf.png")
															.withAlt(
																	"brak obrazka")
															.withTitle(
																	"Pobierz pdf"))
													.withHref(dbp.getPdfUrl())))))));
					nTmp++;
				} else {
					myColumnsTags[j] = td("").withTitle("Pusta wartość, ");
					//System.out.println("Koniec");
				}
			}
			myRowColsTags[i] = tr(myColumnsTags);
		}
		return myRowColsTags;
	}

	/*	
	private ContainerTag[] myT1Rows(int iRows, int jColumns) {
		ContainerTag[] myRowsTags = new ContainerTag[iRows];

		for (int i = 0; i < iRows; i++) {
			myRowsTags[i] = tr(myT1Columns(jColumns));
		}
		return myRowsTags;
	}

	private ContainerTag[] myT1Columns(int jColumns) {
		ContainerTag[] myColumnsTags = new ContainerTag[jColumns];

		for (int j = 0; j < jColumns; j++) {
			myColumnsTags[j] = td(
					a(img().withSrc("img/bbc.jpg.jpg").withAlt("brak obrazka"))
							.withHref("https://www.wp.pl/"),
					h3(a(String.valueOf(" cosssss ")).withHref(
							"https://www.interia.pl/")), h1("FFFFF"), h2("fff"));
			//System.out.println(i);
		}
		return myColumnsTags;
	}
	*/

	private ContainerTag myFooter() {
		return footer(
				table(attrs(".xmlTable"),
				td(a("Plik XML").withHref("myfile.xml").withTitle("Plik XML do pobrania")),
				td(a("Plik DTD").withHref("myfile.dtd")
						.withTitle("Plik DTD do pobrania"))),
				p("Created by Mirosław Dyduch"),
				address(a("Contact information: ").withHref(
						"mailto:Miroslaw.Dyduch@gmail.com").withText(
						"Miroslaw.Dyduch@gmail.com")));

	}

	private ContainerTag[] myT1Thead(int jColumns) {
		ContainerTag[] myTheadTags = new ContainerTag[jColumns];

		for (int j = 0; j < jColumns; j++) {
			myTheadTags[j] = th(String.valueOf("Kolumna " + j));
		}
		return myTheadTags;
	}

	private ContainerTag[] myT1Foot(int jColumns) {
		ContainerTag[] myTFootTags = new ContainerTag[jColumns];

		for (int j = 0; j < jColumns; j++) {
			myTFootTags[j] = td(String.valueOf("Stopka " + j));
		}
		return myTFootTags;
	}

	public void writeToFile(String htmlFilePath) throws IOException {
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(htmlFilePath), "UTF-8"));
		try {
			out.write(this.toString());
		} finally {
			out.close();
		}

	}

	public static void main(String[] args) throws IOException {
		/*
				DBPodcasts dbps = null;
				//System.out.println(hp1.toString());

				//DB db = new DB("postgresql");
				DB db = new DB("postgresql");
				db.drop();

				db.create();
				// db.select(); // wyciaga select na db
				Scraper sc = new Scraper(); //("http://www.bbc.co.uk/learningenglish/english/features/6-minute-english/");

				sc.getSubPage();
				db.insert(sc.getQuery());
				//System.out.println(sc.getQuery());

				dbps = db.getDBLinks();
				dbps.reverseID();

				HtmlPage hp1 = new HtmlPage(3, dbps); // jj-kolumn
				//System.out.println(sc.getQuery());
				Writer out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("WebContent/podcast.html"), "UTF-8"));
				try {
					out.write(hp1.toString());
				} finally {
					out.close();
				}

				
					for (DBPodcast dbp : dbps.getDbPodcasts())
						System.out.println("Zaw.:\t" + dbp.getId() + "\t"
								+ dbp.getLessonURL() + "\t" + dbp.getTitle() + "\t"
								+ dbp.getKeynote() + "\t" + dbp.getImgURL() + "\t"
								+ dbp.getMp3Url() + "\t" + dbp.getPdfUrl() + "\t"
								+ dbp.getEpisode() + "\t" + dbp.getTs());
								*/

	}
}
