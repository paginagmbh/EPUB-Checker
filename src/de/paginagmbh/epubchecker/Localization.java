package de.paginagmbh.epubchecker;

import java.io.IOException;
import org.json.JSONObject;

import de.paginagmbh.common.json.JSON;


/**
  * handles the localization and language file management for the pagina EPUB-Checker
  * 
  * @author		Tobias Fischer
  * @copyright	pagina GmbH, Tübingen
  * @version	1.2.1
  * @date 		2013-01-03
  * @lastEdit	Tobias Fischer
  */
public class Localization {
	
    private JSONObject lang;
	
	
	/* ********************************************************************************************************** */
	
	public Localization() {
		
		lang = null;

		try {
			
			// set system language
			if(paginaEPUBChecker.programLanguage.equals("systemLanguage")) {
				
				// retrieve the "system language"
				String locale = System.getProperty("user.language");
				
				
				// load language file depending on system language
				// possible values are: http://ftp.ics.uci.edu/pub/ietf/http/related/iso639.txt
				
				// German
				if(locale.equals("de")) {
					paginaEPUBChecker.programLanguage = "german";
					lang = loadLanguageFile("german");

				// French
				} else if(locale.equals("fr")) {
					paginaEPUBChecker.programLanguage = "french";
					lang = loadLanguageFile("french");
					
				// English; Fallback
				} else {
					paginaEPUBChecker.programLanguage = "english";
					lang = loadLanguageFile("english");
				}
			
			
			// set pre-defined language (when user switched language in menu)
			} else {
				lang = loadLanguageFile(paginaEPUBChecker.programLanguage);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	
	
	/* ********************************************************************************************************** */
	
	private static JSONObject loadLanguageFile(String language) throws IOException {
		return JSON.parseString(JSON.readResourceAsString(Localization.class, "/resources/localization/" + language + ".json"));
	}
	
	
	
	
	/* ********************************************************************************************************** */
	
	public String getString(String s) {
		
		// try to return the value of the key "s" in the JSONObject
		try {
			
			// value of key "s"
			String keyValue = lang.getString(s);
			
			// return the value if it isn't an empty string
			if(keyValue.length() > 0) {
				return keyValue;
			
			// return the key itself if the keys value IS an empty string
			} else {
				return s;
			}
			
		// if this fails (e.g. key doesn't exist), return the key itself as a string
		} catch (Exception e) {
			return s;
		}
	}
}
