package de.paginagmbh.common.internet;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;


/**
 * reads the website sourcecode of a given URI (as string) and returns it as a string
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tuebingen
 * @version		1.0
 * @date 		2012-02-02
 */
public class WebSiteReader {




	/* ********************************************************************************************************** */

	public static String read(String location) {

		String nextLine;
		URL url = null;
		URLConnection urlConn = null;
		InputStreamReader inStream = null;
		BufferedReader buff = null;
		String result = "";

		try {
			// URL aus Parameter definieren
			url = new URL(location);

			// Connection zur URL herstellen
			urlConn = url.openConnection();

			// Content einlesen
			inStream = new InputStreamReader(urlConn.getInputStream());
			buff = new BufferedReader(inStream);

			// Content Zeile für Zeile ausgeben bzw. in Variable speichern (jede neue Zeile wird hinten angehängt)
			while (true){
				nextLine = buff.readLine();
				if (nextLine != null){
					//System.out.println(nextLine);
					result = result + nextLine;
				}
				else{
					break;
				}
			}

		// Error-Handling
		} catch(MalformedURLException e) {
			System.out.println("Please check the URL: " + e.toString() );
		} catch(IOException e1) {
			System.out.println("Can't read from the Internet: "+ e1.toString() );
		}

		// Kompletten Inhalt zurückgeben
		return result;
	}




	/* ********************************************************************************************************** */

	public Boolean saveAsFile(String location, String filename) {

		String content = read(location);

		try{
			// Datei erzeugen und öffnen
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);

			// Content in Datei schreiben
			out.write(content);

			// Datei schließen
			out.close();

			return true;

		}catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return false;
		}
	}
}