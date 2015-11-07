package de.paginagmbh.epubchecker;

import java.io.IOException;
import org.json.JSONObject;

import de.paginagmbh.common.json.JSON;


/**
 * handles the localization and language file management for the pagina EPUB-Checker
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tübingen
 * @version		1.3.0
 * @date 		2015-11-07
 */
public class LocalizationManager {

	private static volatile LocalizationManager instance = null;
	private JSONObject currentLanguageJSON = null;
	private String currentLanguage;
	private RegexSearchReplace regexEngine;

	private final String[] availableLanguages = {
			"German",
			"English",
			"French",
			"Spanish",
			"Russian"
	};

	protected LocalizationManager() {
		// Exists only to defeat instantiation.
	}

	public static LocalizationManager getInstance() {
		if (instance == null) {
			synchronized (LocalizationManager.class) {
				// Double check
				if (instance == null) {
					instance = new LocalizationManager();
				}
			}
		}
		return instance;
	}


	/* ********************************************************************************************************** */

	public void init() {
		init("systemLanguage");
	}

	public void init(String initialLanguage) {

		// set system language
		if(initialLanguage.equals("systemLanguage")) {

			// retrieve the "system language"
			String currentUserLang = System.getProperty("user.language").toLowerCase();


			// load language file depending on system language
			// possible values are: http://mindprod.com/jgloss/countrycodes.html

			// German
			if(currentUserLang.equals("de")) {
				currentLanguage = "german";
				currentLanguageJSON = loadLanguageFile("german");

				// French
			} else if(currentUserLang.equals("fr")) {
				currentLanguage = "french";
				currentLanguageJSON = loadLanguageFile("french");

				// Spanish
			} else if(currentUserLang.equals("es")) {
				currentLanguage = "spanish";
				currentLanguageJSON = loadLanguageFile("spanish");

				// Russian
			} else if(currentUserLang.equals("ru")) {
				currentLanguage = "russian";
				currentLanguageJSON = loadLanguageFile("russian");

				// English; Fallback
			} else {
				currentLanguage = "english";
				currentLanguageJSON = loadLanguageFile("english");
			}


			// set pre-defined language (when user switched language in menu)
		} else {
			currentLanguage = initialLanguage;
			currentLanguageJSON = loadLanguageFile(initialLanguage);
		}

		// init regex-patterns
		regexEngine = new RegexSearchReplace();
	}




	/* ********************************************************************************************************** */

	private JSONObject loadLanguageFile(String language) {
		try {
			return JSON.parseString(JSON.readResourceAsString(LocalizationManager.class, "/resources/localization/" + language + ".json"));

		} catch (IOException e) {
			// Fallback, if language file couldn't be found
			currentLanguage = "english";
			return loadLanguageFile("english");
		}
	}




	/* ********************************************************************************************************** */

	public String getString(String s) {

		// try to return the value of the key "s" in the JSONObject
		try {

			// value of key "s"
			String keyValue = currentLanguageJSON.getString(s);

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




	/* ********************************************************************************************************** */

	public String getCurrentLanguage() {
		return currentLanguage;
	}

	public String[] getAvailableLanguages() {
		return availableLanguages;
	}

	public RegexSearchReplace getRegexEngine() {
		return regexEngine;
	}
}
