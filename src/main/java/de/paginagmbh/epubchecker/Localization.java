package de.paginagmbh.epubchecker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import de.paginagmbh.common.json.JSON;


/**
 * handles the localization and language file management for the pagina EPUB-Checker
 *
 * @author      Tobias Fischer
 * @copyright   pagina GmbH, Tübingen
 * @version     2.0.4
 * @date        2019-05-13
 */
public class Localization {

	private JSONObject currentLanguageJSON = null;
	private RegexSearchReplace regexEngine;

	private static final Map<Locale, String> availableLanguages;
	static
    {
		availableLanguages = new HashMap<>();
		availableLanguages.put(new Locale("en","US"), "English");
		availableLanguages.put(new Locale("de","DE"), "German");
		availableLanguages.put(new Locale("fr","FR"), "French");
		availableLanguages.put(new Locale("es","ES"), "Spanish");
		availableLanguages.put(new Locale("ru","RU"), "Russian");
		availableLanguages.put(new Locale("ja","JP"), "Japanese");
		availableLanguages.put(new Locale("pt","BR"), "Portuguese [BR]");
		availableLanguages.put(new Locale("cs","CZ"), "Czech");
		availableLanguages.put(new Locale("zh","TW"), "Chinese [TW]");
		availableLanguages.put(new Locale("tr","TR"), "Turkish");
		availableLanguages.put(new Locale("dk","DK"), "Danish");
    }



	public Localization(Locale locale) {

		// load language file depending on system language
		if(availableLanguages.containsKey(locale)) {
			currentLanguageJSON = loadLanguageFile(locale);
		} else {
			currentLanguageJSON = loadLanguageFile(new Locale("en","US"));
		}

		// save currentLanguage in GuiManager to avoid
		// NPE in RegexSearchReplace()
		GuiManager.getInstance().setCurrentLocale(locale);
		GuiManager.getInstance().setCurrentLanguageJSONObject(currentLanguageJSON);
	}




	/* ********************************************************************************************************** */

	private JSONObject loadLanguageFile(Locale locale) {
		try {
			return JSON.parseString(JSON.readResourceAsString(Localization.class, "/localization/" + locale.toString() + ".json"));
		} catch (IOException e) {
			// Fallback, if language file couldn't be found
			return loadLanguageFile(new Locale("en","US"));
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

	public Map<Locale, String> getAvailableLanguages() {
		return availableLanguages;
	}

	public void setRegexEngine(RegexSearchReplace regexEngine) {
		this.regexEngine = regexEngine;
	}

	public RegexSearchReplace getRegexEngine() {
		return regexEngine;
	}
}
