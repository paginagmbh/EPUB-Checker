package de.paginagmbh.epubchecker;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.UIManager;



/**
 * checks and validates EPUB eBooks in a nice graphical user interface
 *
 * @author      Tobias Fischer
 * @copyright   pagina GmbH, Tübingen
 * @version     1.8.0
 * @date        2019-02-07
 */
public class PaginaEPUBChecker {

	// +++++++++++++++++++++++++ DON'T FORGET TO UPDATE EVERYTIME ++++++++++++++++++ //

	public static final String PROGRAMVERSION = "1.8.0";
	public static final String VERSIONDATE = "07.02.2019";
	public static final String PROGRAMRELEASE = "";	// "" or "Beta"
	public static final String RELEASENOTES = "- Update the official W3C EPUBCheck library to the latest release v4.1.1<br/>- Added Brasilian Portuguese translation (Thanks to Thiago de Oliveira Pereira!)";

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ //

	private static GuiManager guiManager;




	/* ********************************************************************************************************** */

	public static void main(String[] args) {
		new PaginaEPUBChecker(args);
	}

	public PaginaEPUBChecker(String[] args) {

		// use system proxy
		System.setProperty("java.net.useSystemProxies", "true");

		// Workaround for SSL connection errors ( https://stackoverflow.com/questions/7615645/ )
		System.setProperty("jsse.enableSNIExtension", "false");

		// create a GuiManager instance
		guiManager = GuiManager.getInstance();



		// load and set system LookAndFeel
		try {
			JFrame.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}



		/* init FileManager && check operating system */
		FileManager.init();


		// load and init GUI and its dependencies (e.g. language object, regex object, etc.)
		loadAndInitGuiAndDependencies();


		// perform update check
		new UpdateCheck(true);


		// init mac specific event listeners; after GUI is loaded
		if(FileManager.os_name.equals("mac")) {
			MacOsIntegration macOsIntegration = new MacOsIntegration();
			try {
				macOsIntegration.addEventHandlers();
				// store current Application object in GuiManager
				guiManager.setMacOsIntegration(macOsIntegration);

			} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
				System.out.println("ERROR: Failed to load Mac OS integration: 'About' menu or Drag&Drop features may not work as expected! Please report to the developer!");
				e.printStackTrace();
			}
		}


		// init commandLine start for windows drag'n'drop on exe icon (issue #6)
		List<File> argFiles = new ArrayList<>();
		if(args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				argFiles.add(new File(args[i]));
			}
			// validate EPUB files
			new EpubValidator().validate(argFiles);
		}


		// show release notes only on first run
		if( ! new File(FileManager.path_FirstRunFile).exists() ) {
			MessageGUI msg = new MessageGUI();
			//msg.setTitle("");
			msg.showMessage(__("Thanks for updating!")
					+ "<br/><br/><br/>"
					+ __("New version %NEW_VERSION% includes these features:").replaceAll("%NEW_VERSION%", PROGRAMVERSION)
					+ "<br/><br/>"
					+ RELEASENOTES
					+ "<br/>");

			// write current version to FirstRun File
			StringHelper.writeStringToFile(FileManager.path_FirstRunFile, String.valueOf(PROGRAMVERSION));
		}


		// ShutdownHook to save window size and position for next startup
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override public void run() {

				Dimension MainGuiDimension = guiManager.getCurrentGUI().getSize();
				Point MainGuiPosition = guiManager.getCurrentGUI().getLocation();

				// write window dimension and position to config file
				StringHelper.writeStringToFile(FileManager.path_WindowFile, (int)MainGuiDimension.getWidth() + "x" + (int)MainGuiDimension.getHeight() + "@" + (int)MainGuiPosition.getX() + "," + (int)MainGuiPosition.getY());
			}
		});
	}




	/* ********************************************************************************************************** */

	public void loadAndInitGuiAndDependencies() {

		// init language object
		guiManager.createNewLocalizationObject();
		Locale currentLocale = guiManager.getCurrentLocale();

		// set the defaultLocale for epubcheck resource bundles
		if(guiManager.getCurrentLocalizationObject().getAvailableLanguages().containsKey(currentLocale)) {
			Locale.setDefault(currentLocale);
		} else {
			/* don't fall back to en_US but use standard default locale instead
			 * this is to support official epubcheck localizations for which
			 * pagina EPUB-Checker doesn't offer translations
			 */
			//Locale.setDefault(new Locale("en", "US"));
		}

		ResourceBundle.clearCache();
		System.out.println(Locale.getDefault());

		// invalidate and dispose the old GUI (needed after switching the program's language in the gui menu)
		if(guiManager.getCurrentGUI() != null) {
			guiManager.getCurrentGUI().invalidate();
			guiManager.getCurrentGUI().dispose();
		}

		// show main GUI
		MainGUI gui =  new MainGUI();
		guiManager.setCurrentGUI(gui);

		// start validating immediately if a file has been set yet
		// (e.g. when changing the language)
		File file = guiManager.getCurrentFile();
		if(file != null && file.exists()) {
			// validate EPUB file
			new EpubValidator().validate(file);
		}
	}




	/* ********************************************************************************************************** */

	private String __(String s) {
		return GuiManager.getInstance().getCurrentLocalizationObject().getString(s);
	}
}
