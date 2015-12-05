package de.paginagmbh.epubchecker;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A static Manager class to maintain all relevant file paths
 * and image icons - including initialising them on different OS'es
 * 
 * @author Tobias Fischer
 * @date   2015-12-05
 */
public class FileManager {

	private static GuiManager guiManager = GuiManager.getInstance();

	public static String os_name;

	// paths and files
	public static String path_ConfigDir;
	public static String path_LastUpdateCheckFile;
	private static String cfgFile_LastUpdateCheck = "UpdateCheck.cfg";
	public static String path_AutoSaveFile;
	private static String cfgFile_AutoSave = "AutoSave.cfg";
	public static String path_LanguageFile;
	private static String cfgFile_Language = "Language.cfg";
	public static String path_TranslateFile;
	private static String cfgFile_Translate = "Translate.cfg";
	public static String path_LogViewFile;
	private static String cfgFile_LogView = "LogView.cfg";
	public static String path_WindowFile;
	private static String cfgFile_Window = "WindowPosition.cfg";
	public static String path_FirstRunFile;
	private static String cfgFile_FirstRun = "FirstRun_" + paginaEPUBChecker.PROGRAMVERSION + ".cfg";

	// logo icons
	public static final Image logoImg16 = Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/paginaEPUBChecker_16.png"));
	public static final Image logoImg32 = Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/paginaEPUBChecker_32.png"));
	public static final Image logoImg64 = Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/paginaEPUBChecker_64.png"));
	public static final Image logoImg128 = Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/paginaEPUBChecker_128.png"));
	public static final Image logoImg256 = Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/paginaEPUBChecker_256.png"));
	public static final Image logoImg512 = Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/paginaEPUBChecker_512.png"));
	public static final Image logoImg1024 = Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/paginaEPUBChecker_1024.png"));

	// icons
	public static final Icon iconLoading = new ImageIcon(Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/infinity-loader_small.gif")));
	public static final Icon iconError = new ImageIcon(Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/icon_error.png")));
	public static final Icon iconWarning = new ImageIcon(Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/icon_warning.png")));
	public static final Icon iconInfo = new ImageIcon(Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/icon_info.png")));
	public static final Icon iconDebug = new ImageIcon(Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/icon_debug.png")));
	public static final Icon iconConfig = new ImageIcon(Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/resources/icons/icon_config.png")));


	public static void init() {

		// windows
		if(System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
			os_name = "windows";
			path_ConfigDir = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "paginaEpubChecker";

		// mac
		} else if(System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
			os_name = "mac";
			path_ConfigDir = System.getProperty("user.home") + File.separator + "Library" + File.separator + "paginaEpubChecker";

		// linux
		} else if(System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
			os_name = "linux";
			path_ConfigDir = System.getProperty("user.home") + File.separator + ".paginaEpubChecker";

		// any other OS isn't supported yet
		} else {
			messageGUI msg = new messageGUI();
			msg.setTitle("pagina EPUB-Checker");
			msg.showError("This operating system isn't supported yet!");
			System.exit(1);
		}

		// set paths to config files after having set the OS dependant config base path above
		path_LastUpdateCheckFile = path_ConfigDir + File.separator + cfgFile_LastUpdateCheck;
		path_AutoSaveFile = path_ConfigDir + File.separator + cfgFile_AutoSave;
		path_LanguageFile = path_ConfigDir + File.separator + cfgFile_Language;
		path_TranslateFile = path_ConfigDir + File.separator + cfgFile_Translate;
		path_LogViewFile = path_ConfigDir + File.separator + cfgFile_LogView;
		path_WindowFile = path_ConfigDir + File.separator + cfgFile_Window;
		path_FirstRunFile = path_ConfigDir + File.separator + cfgFile_FirstRun;

		// create directories to config base path if not existing
		if(!new File(path_ConfigDir).exists()) {
			(new File(path_ConfigDir)).mkdirs();
		}





		// load language string from language config file
		if(new File(FileManager.path_LanguageFile).exists()) {
			try {
				String lang = StringHelper.readFileAsString(FileManager.path_LanguageFile);
				if(lang.length() != 0) {
					guiManager.setCurrentLanguage(lang);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// "else" isn't needed since the default is the "systemLanguage" if no language is specified



		// load window position and dimensions from window config file (content example: 775x650@50,50 - WxH@X,Y)
		if(new File(FileManager.path_WindowFile).exists()) {
			try {
				String windowConfig = StringHelper.readFileAsString(FileManager.path_WindowFile);
				String[] windowConfigSplit = windowConfig.split("@");

				// check if windowConfig contains valid information
				if(windowConfig.length() > 0 && windowConfigSplit.length == 2) {

					// handle window dimensions (WxH)
					if(windowConfigSplit[0].length() != 0) {
						String[] windowDimensions = windowConfigSplit[0].split("x");

						// reload window dimensions only if they are integers
						if(windowDimensions.length == 2 && isInteger(windowDimensions[0]) && isInteger(windowDimensions[1])) {
							guiManager.setMainGuiDimension(new Dimension(
									new Integer(windowDimensions[0]),
									new Integer(windowDimensions[1])
									));
						}
					}
					// handle window position (X,Y)
					if(windowConfigSplit[1].length() != 0) {
						String[] windowPosition = windowConfigSplit[1].split(",");

						// reload window position only if they are integers, and...
						if(windowPosition.length == 2 && isInteger(windowPosition[0]) && isInteger(windowPosition[1])) {
							int posX = new Integer(windowPosition[0]);
							int posY = new Integer(windowPosition[1]);
							// ... and only if the posX and posY are within the current screen size dimensions (second screen fallback)
							if(posX < Toolkit.getDefaultToolkit().getScreenSize().getWidth() && posY < Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
								guiManager.setMainGuiPosition(new Point(posX, posY));
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// "else" isn't needed since there's a default in mainGui.java
	}




	/* ********************************************************************************************************** */

	/**
	 * Parse string as integer and
	 * return true if possible
	 * 
	 * @author http://stackoverflow.com/a/5439547/1128689
	 * @param String string
	 * @return boolean True if string can pe parsed as Integer
	 */
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}

}
