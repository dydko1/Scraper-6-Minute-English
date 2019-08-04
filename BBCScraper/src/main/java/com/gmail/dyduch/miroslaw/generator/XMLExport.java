package com.gmail.dyduch.miroslaw.generator;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.nodes.DocumentType;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.gmail.dyduch.miroslaw.database.DB;
import com.gmail.dyduch.miroslaw.database.DBPodcast;
import com.gmail.dyduch.miroslaw.database.DBPodcasts;

public class XMLExport {

	/*	
	private String xmlFilePath = null;
	private DBPodcasts dbps = null;
	*/

	public XMLExport() {
		this(null, null);
		// https://examples.javacodegeeks.com/core-java/xml/parsers/documentbuilderfactory/create-xml-file-in-java-using-dom-parser-example/
		// https://stackoverflow.com/questions/7373567/how-to-read-and-write-xml-files		
	}

	public XMLExport(DBPodcasts dbps, String xmlFilePath) {
		//		this.dbps=dbps;
		//		this.xmlFilePath=xmlFilePath;

		if (dbps != null) {

			try {
				DocumentBuilderFactory documentFactory = DocumentBuilderFactory
						.newInstance();

				DocumentBuilder documentBuilder = documentFactory
						.newDocumentBuilder();

				Document document = documentBuilder.newDocument();

				// root element
				Element root = document.createElement("BBC-6-minute-english");
				document.appendChild(root);

				//dbps.reverseID();
				for (DBPodcast dbp : dbps.getDbPodcasts()) {
					// episodeId element
					Element episodeId = document.createElement("EpisodeId");
					root.appendChild(episodeId);
					// set an attribute to staff element
					Attr attr = document.createAttribute("id");
					//	System.out.println(dbp.getId());
					attr.setValue(dbp.getId());
					episodeId.setAttributeNode(attr);
					
					//you can also use staff.setAttribute("id", "1") for this
					// ** Elements
					// firstname element
					Element title = document.createElement("Title");
					title.appendChild(document.createTextNode(dbp.getTitle()));
					episodeId.appendChild(title);
					
					Element keynote = document.createElement("Keynote");
					keynote.appendChild(document.createTextNode(dbp.getKeynote()));
					episodeId.appendChild(keynote);
					
					Element episode = document.createElement("Episode");
					episode.appendChild(document.createTextNode(dbp.getEpisode()));
					episodeId.appendChild(episode);
					
					Element lessonUrl = document.createElement("LessonUrl");
					lessonUrl.appendChild(document.createTextNode(dbp.getLessonURL()));
					episodeId.appendChild(lessonUrl);
					
					Element imgUrl = document.createElement("ImgUrl");
					imgUrl.appendChild(document.createTextNode(dbp.getImgURL()));
					episodeId.appendChild(imgUrl);
					
					Element mp3Url = document.createElement("Mp3Url");
					mp3Url.appendChild(document.createTextNode(dbp.getMp3Url()));
					episodeId.appendChild(mp3Url);
					
					Element pdfUrl = document.createElement("PdfUrl");
					pdfUrl.appendChild(document.createTextNode(dbp.getPdfUrl()));
					episodeId.appendChild(pdfUrl);
					/*
					Element ts = document.createElement("Ts");
					ts.appendChild(document.createTextNode(dbp.getTs()));
					episodeId.appendChild(ts);
					*/
				}
				
				
				// create the xml file
				//transform the DOM Object to an XML File
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				
				//nowe
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
				transformer.setOutputProperty(OutputKeys.METHOD, "xml");
				
				DOMImplementation domImpl = document.getImplementation();
				
				org.w3c.dom.DocumentType doctype = domImpl.createDocumentType("doctype",  "BBC SYSTEM", "myfile.dtd");
				//
				transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
				transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
				DOMSource source = new DOMSource(document);
				//
				
				DOMSource domSource = new DOMSource(document);
				StreamResult streamResult = new StreamResult(new File(
						xmlFilePath));

				// If you use
				// StreamResult result = new StreamResult(System.out);
				// the output will be pushed to the standard output ...
				// You can use that for debugging 

				transformer.transform(domSource, streamResult);

				System.out.println("Done creating XML File");

			} catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			} catch (TransformerException tfe) {
				tfe.printStackTrace();
			}
		} else
			System.out.println("\n\nPodaj DBPodcasts jako argument!\n\n");

	}

	public static void main(String[] args) {
		DB db = new DB("postgresql"); //"", or "mysql"
		DBPodcasts dbps = db.getDBLinks(); //kopia tablicy w podcastami
		//dbps.reverseID(); //odwraca kolejnosc id najstarsza namłodsza stron
		XMLExport xmlData = new XMLExport(dbps, "WebContent/myfile.xml");
		/*
		for (DBPodcast dbp : dbps.getDbPodcasts()) {
			System.out.println(dbp.getId());
		}
		*/
	}
}
