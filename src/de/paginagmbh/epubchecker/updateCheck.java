package de.paginagmbh.epubchecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

import de.paginagmbh.common.internet.FileDownloader;
import de.paginagmbh.common.internet.NetTest;


/**
  * checks for updates
  * 
  * @author		Tobias Fischer
  * @copyright	pagina GmbH, Tübingen
  * @date 		2014-01-12
  * @lastEdit	Tobias Fischer
  */
public class updateCheck {
	
	private static final String updateCheckURL = "http://download.pagina-online.de/epubchecker/updatecheck.php?from="+ paginaEPUBChecker.PROGRAMVERSION;
    private static Boolean backgroundTask;
    private static DocumentBuilder builder;
    private static XPath xpath;
    public static FileDownloader dlgui;
    
    
    /* ***************************************************************************************************************** */
    
	public updateCheck(Boolean performInBackground) {
		
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
		if(backgroundTask && new File(paginaEPUBChecker.path_LastUpdateCheckFile).exists()) {
			
			String UpdateCheckLast = null;

			try {
				UpdateCheckLast = readFileAsString(paginaEPUBChecker.path_LastUpdateCheckFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// return if the updater checked once today
			if(Integer.parseInt(UpdateCheckLast) == Integer.parseInt(UpdateCheckToday) && UpdateCheckLast != null ) {
//				System.out.println("");
//				System.out.println("Updateüberprüfung: Wurde heute bereits ausgeführt");
//				System.out.println("");
				return;
			}
		}
		
		
		mainGUI.statusBar.update(paginaEPUBChecker.loadingIcon, __("Checking for updates..."));
		
		
		// InternetConnection Test
		mainGUI.statusBar.update(paginaEPUBChecker.loadingIcon, __("Checking internet connection..."));
		
		try {
			NetTest internetTest = new NetTest("http://www.google.com");
			boolean hasInternetConnection = internetTest.testInternetConnection();
			
			// cancel updateCheck with message if failing
			if(hasInternetConnection == false) {
				if(backgroundTask) {
					mainGUI.statusBar.update(null, __("Update check failed!<br/>Can't establish internet connection.").replaceAll("<br/>", " "));
					return;
				} else {
					messageGUI msg = new messageGUI();
					mainGUI.statusBar.reset();
					msg.showError(__("Update check failed!<br/>Can't establish internet connection."));
					return;
				}
			}
			
		} catch (MalformedURLException e1) {
			messageGUI msg = new messageGUI();
			mainGUI.statusBar.reset();
			msg.showError(__("Update check failed!<br/>Can't establish internet connection."));
			return;
		}

		
		
		// UpdateServer Test
		mainGUI.statusBar.update(paginaEPUBChecker.loadingIcon, __("Checking update server..."));
		
		try {
			NetTest updateserverTest = new NetTest(updateCheckURL);
			boolean updateserverReady = updateserverTest.testWebsiteConnection(NetTest.HTTP_OK);
			
			// cancel updateCheck with message if failing
			if(updateserverReady == false) {
				if(backgroundTask) {
					mainGUI.statusBar.update(null, __("Update check failed!<br/>Update server not available.").replaceAll("<br/>", " "));
					return;
				} else {
					messageGUI msg = new messageGUI();
					mainGUI.statusBar.reset();
					msg.showError(__("Update check failed!<br/>Update server not available."));
					return;
				}
			}
			
		} catch (MalformedURLException e2) {
			messageGUI msg = new messageGUI();
			mainGUI.statusBar.reset();
			msg.showError(__("Update check failed!<br/>Update server not available."));
			return;
		}
		
		
		
        // Dokument instanzieren
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        try {
			builder = domFactory.newDocumentBuilder();
	        
	        // XPath instanzieren
	        XPathFactory factory = XPathFactory.newInstance();
	        xpath = factory.newXPath();
	        
	        
			// write today's date in updatecheckFile
			writeStringToFile(paginaEPUBChecker.path_LastUpdateCheckFile, UpdateCheckToday);

			
			
			
			
			mainGUI.statusBar.update(paginaEPUBChecker.loadingIcon, __("Gathering update information..."));
			

	        // read update info from server
	        //  [0] BuildVersion
	        //  [1] BuildDate
	        //  [2] DownloadURL
	        //  [3] ReleaseNotes
	        String[] UpdateInfo = retrieve_UpdateInfo(paginaEPUBChecker.os_name);
			
//	        System.out.println(paginaEPUBChecker.os_name);
//	        System.out.println(UpdateInfo[0]);
//	        System.out.println(UpdateInfo[1]);
//	        System.out.println(UpdateInfo[2]);
//	        System.out.println(UpdateInfo[3]);
	        
	        
	        // lokale Version ist niedriger als Server-Version
			// Ein Update steht bereit!
			if(Integer.parseInt(paginaEPUBChecker.PROGRAMVERSION.replace(".", "")) < Integer.parseInt(UpdateInfo[0].replace(".", ""))) {
				
				mainGUI.statusBar.reset();
				
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
					mainGUI.statusBar.update(null, __("There are no new updates available."));
					return;
				} else {
					messageGUI msg = new messageGUI();
					mainGUI.statusBar.reset();
					msg.showMessage(__("There are no new updates available."));
					return;
				}
			}
		
			
		
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    
    
    /* ***************************************************************************************************************** */
    
    public static String readFileAsString(String filePath) throws java.io.IOException {
    	
    	// Variablen instanziieren
    	StringBuffer fileData = new StringBuffer(1000);
    	char[] buf = new char[1024];
    	int numRead=0;
    	
    	// Datei einlesen
    	BufferedReader reader = new BufferedReader(new FileReader(filePath));
    	while((numRead=reader.read(buf)) != -1){
    		String readData = String.valueOf(buf, 0, numRead);
    		fileData.append(readData);
    		buf = new char[1024];
    	}
    	
    	// Datei schließen
    	reader.close();
    	
    	// Dateiinhalt als String zurückgeben
    	return fileData.toString();
    }
    
    
    
    /* ***************************************************************************************************************** */
    
    public static void writeStringToFile(String file, String content) {
    	try {
			// Create file 
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			out.close();
        } catch (Exception e){
			e.printStackTrace();
        }
    }
    
    
    
    /* ***************************************************************************************************************** */
    
	public static String[] retrieve_UpdateInfo(String OSname) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		
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
	
	private static String __(String s) {
		return paginaEPUBChecker.l10n.getString(s);
	}
}
