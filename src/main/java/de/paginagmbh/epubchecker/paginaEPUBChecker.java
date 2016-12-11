package de.paginagmbh.epubchecker;

import java.awt.Dimension;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent.AboutEvent;
import com.apple.eawt.AppEvent.QuitEvent;
import com.apple.eawt.Application;
import com.apple.eawt.OpenFilesHandler;
import com.apple.eawt.AppEvent.OpenFilesEvent;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;


/**
 * checks and validates EPUB eBooks in a nice graphical user interface
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tübingen
 * @version		1.6.1
 * @date		2016-12-11
 */
public class paginaEPUBChecker {

	// +++++++++++++++++++++++++ DON'T FORGET TO UPDATE EVERYTIME ++++++++++++++++++ //

	public static final String PROGRAMVERSION = "1.6.1";
	public static final String VERSIONDATE = "02.08.2016";
	public static final String PROGRAMRELEASE = "";	// "" or "beta"
	public static final String RELEASENOTES = "- Support for Apple Retina Displays<br/>- Fixed crash on Windows when running with Java 1.8.0_101 or higher";

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ //

	private static GuiManager guiManager;




	/* ********************************************************************************************************** */

	public static void main(String[] args) {
		new paginaEPUBChecker(args);
	}
	
	public paginaEPUBChecker(String[] args) {

		// use system proxy
		System.setProperty("java.net.useSystemProxies", "true");

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
		new updateCheck(true);


		// init mac specific event listeners; after GUI is loaded
		if(FileManager.os_name.equals("mac")) {
			initMacOSEventListeners();
		}


		// init commandLine start for windows drag'n'drop on exe icon (issue #6)
		List<File> argFiles = new ArrayList<File>();
		if(args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				argFiles.add(new File(args[i]));
			}
			DragDropListener dragDropListener = new DragDropListener();
			dragDropListener.handleDroppedFiles(argFiles);
		}


		// show release notes only on first run
		if( ! new File(FileManager.path_FirstRunFile).exists() ) {
			messageGUI msg = new messageGUI();
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

		String currentLanguage = guiManager.getCurrentLanguage();

		// set the defaultLocale for epubcheck resource bundles
		// TODO: seems as this has no effect when switching the language and the user already validated an epub
		if(currentLanguage.equals("german")) {
			Locale.setDefault(new Locale("de", "DE"));
		} else if(currentLanguage.equals("french")) {
			Locale.setDefault(new Locale("fr", "FR"));
		} else if(currentLanguage.equals("spanish")) {
			Locale.setDefault(new Locale("es", "ES"));
		} else if(currentLanguage.equals("russian")) {
			Locale.setDefault(new Locale("ru", "RU"));
		} else if(currentLanguage.equals("english")) {
			Locale.setDefault(new Locale("en", "US"));
		} else {
			// don't fall back to en_US but use standard default locale instead
			// this is to support official epubcheck localizations for which
			// pagina EPUB-Checker doesn't offer translations

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
		mainGUI gui =  new mainGUI();
		guiManager.setCurrentGUI(gui);

		// start validating immediately if a file has been set yet
		// (e.g. when changing the language)
		if(guiManager.getCurrentFile() != null && guiManager.getCurrentFile().exists()) {
			File file = guiManager.getCurrentFile();
			EpubValidator epubValidator = new EpubValidator(new paginaReport(file.getName()));
			epubValidator.validate(file);
		}
	}




	/* ********************************************************************************************************** */

	public void initMacOSEventListeners() {
		// mac specific event listeners
		// have to be set after the GUI was loaded

		/*
		 * Help and tutorial:
		 * https://developer.apple.com/library/mac/documentation/Java/Reference/JavaSE6_AppleExtensionsRef/api/com/apple/eawt/Application.html#addApplicationListener%28com.apple.eawt.ApplicationListener%29
		 *
		 */


		// create an instance of the mac osx Application class
		Application macApp = Application.getApplication();

		// Exit handler
		macApp.setQuitHandler(new QuitHandler() {
			@Override
			public void handleQuitRequestWith(QuitEvent arg0, QuitResponse arg1) {
				System.exit(0);
			}
		});

		// AboutMenu handler
		macApp.setAboutHandler(new AboutHandler() {
			@Override
			public void handleAbout(AboutEvent arg0) {
				subGUI s = new subGUI(guiManager.getCurrentGUI());
				s.displayAboutBox();
			}
		});

		// Drop handler (for dropping files on the program or dock)
		macApp.setOpenFileHandler(new OpenFilesHandler() {

			@Override
			public void openFiles(OpenFilesEvent arg0) {
				List<File> files = arg0.getFiles();

				DragDropListener dragDropListener = new DragDropListener();
				dragDropListener.handleDroppedFiles(files);

			}
		});

		// store current Application object in GuiManager
		guiManager.setMacApp(macApp);
	}




	/* ********************************************************************************************************** */

	private String __(String s) {
		return GuiManager.getInstance().getCurrentLocalizationObject().getString(s);
	}
}