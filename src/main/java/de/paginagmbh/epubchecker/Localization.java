package de.paginagmbh.epubchecker;

import java.io.IOException;
import org.json.JSONObject;

import de.paginagmbh.common.json.JSON;


/**
 * handles the localization and language file management for the pagina EPUB-Checker
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tübingen
 * @version		1.3.1
 * @date 		2016-12-08
 */
public class Localization {

	private JSONObject currentLanguageJSON = null;
	private String currentLanguage;
	private RegexSearchReplace regexEngine;

	private final String[] availableLanguages = {
			"German",
			"English",
			"French",
			"Spanish",
			"Russian",
			"Japanese"
	};

	public Localization(String initialLanguage) {

		// set system language
		if(initialLanguage == null || initialLanguage.equals("systemLanguage")) {

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

				// Japanese
			} else if(currentUserLang.equals("ja")) {
				currentLanguage = "japanese";
				currentLanguageJSON = loadLanguageFile("japanese");

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

		// save currentLanguage in GuiManager to avoid
		// NPE in RegexSearchReplace()
		GuiManager.getInstance().setCurrentLanguage(currentLanguage);
		GuiManager.getInstance().setCurrentLanguageJSONObject(currentLanguageJSON);
	}




	/* ********************************************************************************************************** */

	private JSONObject loadLanguageFile(String language) {
		try {
			return JSON.parseString(JSON.readResourceAsString(Localization.class, "/localization/" + language + ".json"));

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

	public String[] getAvailableLanguages() {
		return availableLanguages;
	}

	public void setRegexEngine(RegexSearchReplace regexEngine) {
		this.regexEngine = regexEngine;
	}

	public RegexSearchReplace getRegexEngine() {
		return regexEngine;
	}
}
