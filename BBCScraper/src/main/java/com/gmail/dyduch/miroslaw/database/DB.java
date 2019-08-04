package com.gmail.dyduch.miroslaw.database;

import java.sql.*;

/*
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 import java.sql.Statement;
 */

public class DB {
	// H2 or Mysql or PSQL
	private String dbmsType;
	private boolean isParameters = false;
	// JDBC driver name and database URL
	private String JDBC_DRIVER;
	private String DB_URL;
	// Database credentials
	private String USER;
	private String PASS;
	Connection conn = null;
	Statement stmt = null;
	private DBPodcasts dbps = new DBPodcasts();

	public DB() {
		// dbmsType = "H2"; // Domyślnie H2 SQL
		// https://4programmers.net/In%C5%BCynieria_oprogramowania/Wzorce_projektowe/%C5%81a%C5%84cuch_konstruktor%C3%B3w
		this("", "", "", "", "");
		System.out.println("Domyślny konstruktor został uruchomiony.\n");
	}

	public DB(String dbmsType) {
		this(dbmsType, "", "", "", "");
		System.out.println("Konstruktor jednoparametrowy wywołany.: "
				+ this.dbmsType);
	}

	public DB(String dbmsType, String jdbc_driver, String db_url, String user,
			String pass) {

		// ******** DB select
		if (!dbmsType.equalsIgnoreCase("MySQL")
				&& !dbmsType.equalsIgnoreCase("PostgreSQL")) {
			this.dbmsType = "H2";
			System.out.println(this.dbmsType);
		} else if (dbmsType.equalsIgnoreCase("MySQL")) {
			this.dbmsType = "MySQL";
			System.out.println(this.dbmsType);
		} else if (dbmsType.equalsIgnoreCase("PostgreSQL")) {
			this.dbmsType = "PostgreSQL";
			System.out.println(this.dbmsType);
		} else
			System.out.println("Nieobsługiwana baza danych. Błąd ?!");

		// ******** if db ok then user parameters, without this in condition,
		// must be equalsIgnoreCase, check inouts value
		if (dbmsType.equalsIgnoreCase("H2")
				|| dbmsType.equalsIgnoreCase("MySQL")
				|| dbmsType.equalsIgnoreCase("PostgreSQL")) {
			// można zostawić tylko usera lub usunac warunek z hasla ponizej,
			// przypadek puste hasło "". Tu musza być dane.
			if (!jdbc_driver.isEmpty() && !db_url.isEmpty() && !user.isEmpty()
					&& !pass.isEmpty()) {
				this.JDBC_DRIVER = jdbc_driver;
				this.DB_URL = db_url;
				this.USER = user;
				this.PASS = pass;
				isParameters = true;
				System.out
						.println("Podano parametry użytkownika do logowania.");
				// System.out.println("ee.: " + JDBC_DRIVER + " " + DB_URL +" "+
				// USER + " " + " " + PASS );
			}
		} else
			System.out
					.println("Poprawna nazwa bazy nie została podana. Wybrana zostanie baza H2 z domyślnymi parametrami.");

		// ************************************************
		if (this.dbmsType.equals("H2") && !isParameters) {
			this.JDBC_DRIVER = "org.h2.Driver";
			this.DB_URL = "jdbc:h2:~/test";
			this.USER = "SA";
			this.PASS = "";
		} else if (this.dbmsType.equals("MySQL") && !isParameters) {
			// DB_URL =
			// "jdbc:mysql://localhost/PODCAST?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
			// dla Windows na Linux ok
			// "jdbc:mysql://127.0.0.1:3306/blog";
			this.JDBC_DRIVER = "com.mysql.jdbc.Driver";
			this.DB_URL = "jdbc:mysql://mysql.agh.edu.pl/midyduch"; // ?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
			this.USER = "user";
			this.PASS = "password";
		} else if (this.dbmsType.equals("PostgreSQL") && !isParameters) {
			this.JDBC_DRIVER = "org.postgresql.Driver";
			this.DB_URL = "jdbc:postgresql://localhost:5432/miro";
			this.USER = "user";
			this.PASS = "**password**";
		} else
			System.out.println(this.dbmsType
					+ " wcześniej ustawiona danymi użytkownika. -:]" + "\n");

		// ************************************************
		System.out.println("Ustawiono parametry dla.: " + this.dbmsType
				+ "\nJDBC_DRIVER = " + this.JDBC_DRIVER + "\nthis.DB_URL = "
				+ this.DB_URL + "\nthis.USER = " + this.USER + "\nPASS = "
				+ this.PASS);
		System.out
				.println("Konstruktor wieloparametrowy, ustawiony typ bazy.: "
						+ this.dbmsType);
	}

	public void create() {
		try {
			String sql = " lessonUrl VARCHAR(255), title VARCHAR(255), keynote VARCHAR(255),"
					+ " imgUrl VARCHAR(255), mp3Url VARCHAR(255),"
					+ " pdfUrl VARCHAR(255), episode VARCHAR(255),"
					+ " ts TIMESTAMP)";
			// // STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// // STEP 2: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// // STEP 3: Execute a query
			System.out.println("Creating table in given database...");
			stmt = conn.createStatement();
			if (dbmsType.equals("H2") || dbmsType.equals("MySQL")) {
				sql = "CREATE TABLE IF NOT EXISTS podcast "
						+ "(id INT PRIMARY KEY AUTO_INCREMENT, " + sql;
				System.out.println("Utworzono nową tablice.: " + dbmsType);
			} else if (dbmsType.equals("PostgreSQL")) {
				sql = "CREATE TABLE IF NOT EXISTS podcast "
						+ "(id SERIAL PRIMARY KEY, " + sql;
				System.out.println("Utworzono nową tablice.: " + dbmsType);
			} else {
				System.out.println("Problem z utworzeniem nowej tablicy !");
			}
			stmt.executeUpdate(sql);

			//
			// // STEP 4: Clean-up environment
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// // finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}

	public void insert(String links) {
		try {
			String sql;
			// // STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// // STEP 2: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// // STEP 3: Execute a query
			System.out.println("Inserting data in database...");
			stmt = conn.createStatement();

			sql = "INSERT INTO podcast (lessonUrl, title, keynote, imgUrl, mp3Url, pdfUrl, episode, ts) "
					+ "VALUES " + links;
			stmt.executeUpdate(sql);

			System.out.println("Created table in given database...");
			//
			// // STEP 4: Clean-up environment
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// // finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}

	public void drop() {
		try {
			String sql;
			// // STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// // STEP 2: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// // STEP 3: Execute a query
			System.out.println("Droping data in database...");
			stmt = conn.createStatement();

			sql = "DROP TABLE IF EXISTS podcast ";
			stmt.executeUpdate(sql);
			System.out.println("Dropped table in given database...");
			//
			// // STEP 4: Clean-up environment
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// // finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}

	public void select() {
		try {
			String sql;
			// // STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// // STEP 2: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// // STEP 3: Execute a query
			System.out.println("Inserting data in database...");
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("select * from podcast");
			int size = 0;
			//rs.getString("id") + "\t" +
			while (rs.next()) {
				System.out.println((rs.getString("id") + "\t"
						+ "lessonURL")
						+ "\t"
						+ rs.getString("title")
						+ "\t"
						+ rs.getString("keynote")
						+ "\t"
						+ rs.getString("imgURL")
						+ "\t"
						+ rs.getString("mp3Url")
						+ "\t"
						+ rs.getString("pdfUrl")
						+ "\t"
						+ rs.getString("episode") + "\t" + rs.getString("ts"));
				size++;
			}

			System.out.println("Selected * table in given database...");
			// System.out.println("\n " + size);
			// STEP 4: Clean-up environment
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// // finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}

	public DBPodcasts getDBLinks() {
		DBPodcast dbp = null;
		try {
			String sql;
			// // STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// // STEP 2: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// // STEP 3: Execute a query
			System.out.println("Inserting data in database...");
			stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("select * from podcast");
			int size = 0;
			//rs.getString("id") + "\t" +
			while (rs.next()) {
				dbp = new DBPodcast(rs.getString("id"),
						rs.getString("lessonURL"), rs.getString("title"),
						rs.getString("keynote"), rs.getString("imgURL"),
						rs.getString("mp3Url"), rs.getString("pdfUrl"),
						rs.getString("episode"), rs.getString("ts"));
				dbps.getDbPodcasts().add(dbp);
				size++;
			}
			//System.out.println("Selected * table in given database...");
			// System.out.println("\n " + size);
			// STEP 4: Clean-up environment
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// // finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
		//System.out.println(dbps.getDbPodcasts().size());
		dbps.reverseID(); // jzu dziaala opjonalnie //uwaga to zly pomysl tutaj
		return dbps; //return DBPodcasts
	}

	@Override
	public String toString() {
		return "Wywolanie DB toString";
	}

	public static void main(String[] args) {

		// H2-default, MysQL, PostgreSQL
		DB db = new DB();

		//DB db = new DB("postgresql");
		//DB db =new DB("mysql");
		//db.drop();
		db.getDBLinks();
		//db.create();
		//db.insert();
		//db.select();
		//db.drop();
		System.out.println("Main z DB");
	}
}

// MysQL, PostgreSQL
// DB db = new DB("postgresql");
// DB db = new DB("mysql");
// DB db = new DB("??");
// db.create();
// db.insert();
// db.drop();
// DB db = new DB();
// DB db = new DB("mYsql");
// DB db = new DB("PostgreSQL");
// DB db = new DB("PostgreSQL", "kk", "dburl", "user12", "passelko");
// DB db = new DB("mysql", "dbdriver", "dburl", "user12","passelko");

// DB("mysql","com.mysql.jdbc.Driver","jdbc:mysql://mysql.agh.edu.pl/midyduch","midyduch","8eCkauXZNdPGGfvE");
// DB db = new
// DB("postgresql","org.postgresql.Driver","jdbc:postgresql://localhost:5432/miro","postgres","mirdyd12345");
// Zablokowane na borgu
// DB db = new DB("postgresql", "org.postgresql.Driver",
// "jdbc:postgresql://borg.kis.agh.edu.pl:5432/dydumiro", "dydumiro",
// "mirdyd12345");
