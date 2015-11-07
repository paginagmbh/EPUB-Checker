package de.paginagmbh.epubchecker;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.paginagmbh.common.gui.StatusBar;
import de.paginagmbh.common.internet.FileDownloader;
import de.paginagmbh.common.internet.NetTest;


/**
 * checks for updates
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tübingen
 * @date 		2015-11-07
 */
public class updateCheck {

	private final String updateCheckURL = "http://download.pagina-online.de/epubchecker/updatecheck.php?from="+ paginaEPUBChecker.PROGRAMVERSION;
	private Boolean backgroundTask;
	private DocumentBuilder builder;
	private XPath xpath;
	public FileDownloader dlgui;
	private static GuiManager guiManager;
	private StatusBar statusBar;

	public FileDownloader getFileDownloaderGui() {
		return dlgui;
	}


	/* ***************************************************************************************************************** */

	public updateCheck(Boolean performInBackground) {

		guiManager = GuiManager.getInstance();
		statusBar = guiManager.getCurrentGUI().getStatusBar();

		if(performInBackground) {
			backgroundTask = true;
		} else {
			backgroundTask = false;
		}

		// date object
		Calendar cal = Calendar.getInstance();

		// today's date
		SimpleDateFormat sdfCheck = new SimpleDateFormat("yyyyMMdd");
		String UpdateCheckToday = sdfCheck.format(cal.getTime()).toString();


		// check for last updatecheck if check runs in background at startup
		if(backgroundTask && new File(FileManager.path_LastUpdateCheckFile).exists()) {

			String UpdateCheckLast = null;

			try {
				UpdateCheckLast = StringHelper.readFileAsString(FileManager.path_LastUpdateCheckFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// return if the updater checked once today
			if(Integer.parseInt(UpdateCheckLast) == Integer.parseInt(UpdateCheckToday) && UpdateCheckLast != null ) {
				return;
			}
		}


		statusBar.update(FileManager.loadingIcon, __("Checking for updates..."));


		// InternetConnection Test
		statusBar.update(FileManager.loadingIcon, __("Checking internet connection..."));

		try {
			NetTest internetTest = new NetTest("http://www.google.com");
			boolean hasInternetConnection = internetTest.testInternetConnection();

			// cancel updateCheck with message if failing
			if(hasInternetConnection == false) {
				errorInternetConnectionNotAvailable();
				return;
			}

		} catch (MalformedURLException e1) {
			errorUpdateCheck(e1);
			return;
		}



		// UpdateServer Test
		statusBar.update(FileManager.loadingIcon, __("Checking update server..."));

		try {
			NetTest updateserverTest = new NetTest(updateCheckURL);
			boolean updateserverReady = updateserverTest.testWebsiteConnection(NetTest.HTTP_OK);

			// cancel updateCheck with message if failing
			if(updateserverReady == false) {
				errorUpdateServerNotAvailable();
				return;
			}

		} catch (MalformedURLException e2) {
			errorUpdateCheck(e2);
			return;
		}





		statusBar.update(FileManager.loadingIcon, __("Gathering update information..."));



		// Dokument instanzieren
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		try {
			builder = domFactory.newDocumentBuilder();

			// XPath instanzieren
			XPathFactory factory = XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI,
					"net.sf.saxon.xpath.XPathFactoryImpl",
					ClassLoader.getSystemClassLoader());
			xpath = factory.newXPath();


			// write today's date in updatecheckFile
			StringHelper.writeStringToFile(FileManager.path_LastUpdateCheckFile, UpdateCheckToday);


			// read update info from server
			//  [0] BuildVersion
			//  [1] BuildDate
			//  [2] DownloadURL
			//  [3] ReleaseNotes
			String[] UpdateInfo = retrieve_UpdateInfo(FileManager.os_name);


			// lokale Version ist niedriger als Server-Version
			// Ein Update steht bereit!
			if(Integer.parseInt(paginaEPUBChecker.PROGRAMVERSION.replace(".", "")) < Integer.parseInt(UpdateInfo[0].replace(".", ""))) {

				statusBar.reset();

				messageGUI msg = new messageGUI();
				int answer = msg.showQuestion(
						__("Version %NEW_VERSION% is now available for download!")
						.replaceAll("%NEW_VERSION%", UpdateInfo[0])
						+ "<br/>"
						+ __("You are currently using %CURRENT_VERSION%")
						.replaceAll("%CURRENT_VERSION%", paginaEPUBChecker.PROGRAMVERSION)
						+ "<br/><br/>"
						+ __("New version %NEW_VERSION% includes these features:")
						.replaceAll("%NEW_VERSION%", UpdateInfo[0])
						+ "<br/><br/>"
						+ UpdateInfo[3]
								+ "<br/><br/><br/>"
								+ __("Do you want to download the update?"));


				if(answer == JOptionPane.YES_OPTION) {

					// download the update
					dlgui = new FileDownloader(
							UpdateInfo[2],
							System.getProperty("user.home") + File.separator + "Desktop",
							String.format(
									__("An update (v%1$s, %2$s) for your current installation (v%3$s, %4$s) is beeing downloaded right now..."),
									UpdateInfo[0],
									UpdateInfo[1],
									paginaEPUBChecker.PROGRAMVERSION,
									paginaEPUBChecker.VERSIONDATE));

				} else {
					return;
				}



			// lokale Version ist höher als oder gleich wie Server-Version
			// Es gibt kein Update!
			} else {

				if(backgroundTask) {
					statusBar.update(null, __("There are no new updates available."));
				} else {
					messageGUI msg = new messageGUI();
					statusBar.reset();
					msg.showMessage(__("There are no new updates available."), __("You're up-to-date!"));
				}
				return;
			}



		} catch (Exception e) {
			errorUpdateCheck(e);
			return;
		}
	}



	/* ***************************************************************************************************************** */

	public void errorUpdateCheck(Exception e) {
		if(backgroundTask) {
			statusBar.update(null, __("Update check failed!") + " " + __("Please check manually for updates").replace("<br/>", " "));
		} else {
			messageGUI msg = new messageGUI();
			statusBar.reset();
			msg.showMessage(__("Please check manually for updates") + "<br/><br/>["+ e.getClass().getName() +"]<br/>"+ e.getMessage().replace(System.getProperty("line.separator"), "<br/>"), __("Update check failed!"));
		}
	}



	/* ***************************************************************************************************************** */

	public void errorInternetConnectionNotAvailable() {
		if(backgroundTask) {
			statusBar.update(null, __("Update check failed!") + " " + __("Can't establish internet connection."));
		} else {
			messageGUI msg = new messageGUI();
			statusBar.reset();
			msg.showError(__("Update check failed!") + "<br/>" + __("Can't establish internet connection."));
		}
	}



	/* ***************************************************************************************************************** */

	public void errorUpdateServerNotAvailable() {
		if(backgroundTask) {
			statusBar.update(null, __("Update check failed!") + " " + __("Update server not available."));
		} else {
			messageGUI msg = new messageGUI();
			statusBar.reset();
			msg.showError(__("Update check failed!") + "<br/>" + __("Update server not available."));
		}
	}



	/* ***************************************************************************************************************** */

	public String[] retrieve_UpdateInfo(String OSname) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		String[] UpdateInfo = {null, null, null, null};

		// read update information
		Document docUpdate = builder.parse(updateCheckURL);

		// BuildVersion
		UpdateInfo[0] = xpath.compile("//package[@os='" + OSname + "']/entry[@key='buildversion']/value").evaluate(docUpdate);

		// BuildDate
		UpdateInfo[1] = xpath.compile("//package[@os='" + OSname + "']/entry[@key='builddate']/value").evaluate(docUpdate);

		// DownloadURL
		UpdateInfo[2] = xpath.compile("//package[@os='" + OSname + "']/entry[@key='downloadURL']/value").evaluate(docUpdate);

		// ReleaseNotes
		UpdateInfo[3] = xpath.compile("//package[@os='" + OSname + "']/entry[@key='releaseNotes']/value").evaluate(docUpdate);

		return UpdateInfo;
	}




	/* ********************************************************************************************************** */

	private String __(String s) {
		return LocalizationManager.getInstance().getString(s);
	}
}