package de.paginagmbh.epubchecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * A bunch of helper functions to read and write strings
 * from/to files
 *
 * @author  Tobias Fischer
 * @date    2015-11-07
 */
public class StringHelper {

	public static String readFileAsString(String filePath) throws java.io.IOException {

		// Variablen instanziieren
		StringBuffer fileData = new StringBuffer(1000);
		char[] buf = new char[1024];
		int numRead=0;

		// Datei einlesen
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		while((numRead=reader.read(buf)) != -1){
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		// Datei schließen
		reader.close();

		// Dateiinhalt als String zurückgeben
		return fileData.toString();
	}


	public static void writeStringToFile(String file, String content) {
		try {
			// Create file
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			out.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
