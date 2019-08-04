package com.gmail.dyduch.miroslaw.database;

import java.util.ArrayList;

import org.jsoup.select.Evaluator.IsEmpty;

public class DBPodcasts {
	ArrayList<DBPodcast> dbPodcasts = new ArrayList<>();

	public ArrayList<DBPodcast> getDbPodcasts() {
		return dbPodcasts;
	}

	public void setDbPodcasts(ArrayList<DBPodcast> dbPodcasts) {
		this.dbPodcasts = dbPodcasts;
	}

	public DBPodcasts() {
		// TODO Auto-generated constructor stub
	}

	public void reverseID() {
		/*
		int iSize = dbPodcasts.size();
		for (DBPodcast p : dbPodcasts)
			p.setId(String.valueOf(iSize--));
			*/
		
		int i = 0, iSize = dbPodcasts.size();
		String[] iTab = new String[iSize ];
		for (DBPodcast p : dbPodcasts) {
			iTab[i++] = p.getId();
		}
		iSize--;
		for (DBPodcast p : dbPodcasts) {
			p.setId(iTab[iSize--]);
			//System.out.println(p.getId());
		}
		
		//p.setId(String.valueOf(iTab[iSize--]));
		//System.out.println("\n\n test");
	}

}
