package com.gmail.dyduch.miroslaw.database;

public class DBPodcast {
	private String id, lessonURL, title, keynote, imgURL, mp3Url, pdfUrl,
			episode, ts;

	public DBPodcast() {
		this(null, null, null, null, null, null, null, null, null);
	}

	public DBPodcast(String id, String lessonURL, String title, String keynote,
			String imgURL, String mp3Url, String pdfUrl, String episode,
			String ts) {
		//super();
		this.id = id;
		this.lessonURL = lessonURL;
		this.title = title;
		this.keynote = keynote;
		this.imgURL = imgURL;
		this.mp3Url = mp3Url;
		this.pdfUrl = pdfUrl;
		this.episode = episode;
		this.ts = ts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLessonURL() {
		return lessonURL;
	}

	public void setLessonURL(String lessonURL) {
		this.lessonURL = lessonURL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeynote() {
		return keynote;
	}

	public void setKeynote(String keynote) {
		this.keynote = keynote;
	}

	public String getMp3Url() {
		return mp3Url;
	}

	public void setMp3Url(String mp3Url) {
		this.mp3Url = mp3Url;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public String getEpisode() {
		return episode;
	}

	public void setEpisode(String episode) {
		this.episode = episode;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

}
