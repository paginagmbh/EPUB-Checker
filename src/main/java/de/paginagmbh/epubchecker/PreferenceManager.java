package de.paginagmbh.epubchecker;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class PreferenceManager {

	private static String prefsNode = "de/paginagmbh/epubchecker";
	public enum Preference {
		WINDOWPOSITION;
	}

	public static String getPref(Preference pref) {
		Preferences prefs = Preferences.userRoot().node(prefsNode);
		return prefs.get(pref.toString().toLowerCase(), null);
	}

	public static void savePref(Preference pref, String value) {
		Preferences prefs = Preferences.userRoot().node(prefsNode);
		prefs.put(pref.toString().toLowerCase(), value);
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
