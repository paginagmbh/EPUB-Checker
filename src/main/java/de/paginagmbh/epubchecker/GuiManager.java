package de.paginagmbh.epubchecker;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;

import org.json.JSONObject;

import com.apple.eawt.Application;

/**
 * GUI Manager singelton
 * 
 * is used to store all relevant GUI settings
 * like position, language, etc...
 * 
 * 
 * @author  Tobias Fischer
 * @date    2015-12-04
 *
 */
public class GuiManager {

	private static volatile GuiManager instance = null;
	private String currentLanguage = null;
	private JSONObject currentLanguageJSONObject = null;
	private Dimension MainGuiDimension = null;
	private Point MainGuiPosition = null;
	private mainGUI currentGUI = null;
	private Application macApp = null;
	private Boolean menuOptionAutoSave = false;
	private File currentFile = null;
	private Localization l10n = null;
	private LogViewMode LogView = LogViewMode.TABLE;
	public enum LogViewMode {
		TEXT, TABLE;
	}

	protected GuiManager() {
		// Exists only to defeat instantiation.
	}

	public static GuiManager getInstance() {
		if (instance == null) {
			synchronized (GuiManager.class) {
				// Double check
				if (instance == null) {
					instance = new GuiManager();
				}
			}
		}
		return instance;
	}

	public String getCurrentLanguage() {
		return currentLanguage;
	}

	public void setCurrentLanguage(String currentLanguage) {
		this.currentLanguage = currentLanguage;
	}

	public JSONObject getCurrentLanguageJSONObject() {
		return currentLanguageJSONObject;
	}

	public void setCurrentLanguageJSONObject(JSONObject currentLanguageJSONObject) {
		this.currentLanguageJSONObject = currentLanguageJSONObject;
	}

	public Dimension getMainGuiDimension() {
		return MainGuiDimension;
	}

	public void setMainGuiDimension(Dimension mainGuiDimension) {
		MainGuiDimension = mainGuiDimension;
	}

	public Point getMainGuiPosition() {
		return MainGuiPosition;
	}

	public void setMainGuiPosition(Point mainGuiPosition) {
		MainGuiPosition = mainGuiPosition;
	}

	public mainGUI getCurrentGUI() {
		return currentGUI;
	}

	public void setCurrentGUI(mainGUI currentGUI) {
		this.currentGUI = currentGUI;
	}

	public Application getMacApp() {
		return macApp;
	}

	public void setMacApp(Application macApp) {
		this.macApp = macApp;
	}

	public Boolean getMenuOptionAutoSave() {
		return menuOptionAutoSave;
	}

	public void setMenuOptionAutoSave(Boolean menuOptionAutoSave) {
		this.menuOptionAutoSave = menuOptionAutoSave;
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
		this.getCurrentGUI().getPathInputField().setText(currentFile.getAbsolutePath());
	}

	public void createNewLocalizationObject() {
		// create new Localization object
		this.l10n = new Localization(getCurrentLanguage());
		// init regex-patterns
		this.l10n.setRegexEngine(new RegexSearchReplace());
	}

	public Localization getCurrentLocalizationObject() {
		return l10n;
	}

	public LogViewMode getLogView() {
		return LogView;
	}

	public void setLogView(LogViewMode logView) {
		LogView = logView;
	}

}