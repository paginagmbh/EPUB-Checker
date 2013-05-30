package de.paginagmbh.epubchecker;

import com.adobe.epubcheck.api.Report;
import com.adobe.epubcheck.util.FeatureEnum;
import com.adobe.epubcheck.util.Messages;



/**
 * overrides the Adobe epubcheck "Report" class to handle all the epubcheck warnings and errors
 * instead of writing them to stderr, we are appending them to the rsult textarea on our GUI
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tübingen
 * @version		1.2
 * @date 		2013-05-30
 * @lastEdit	Tobias Fischer
 */
public class paginaReport implements Report {

	private String ePubName;
	private static boolean DEBUG = false;
	public static int errorCount, warningCount, exceptionCount;




	/* ********************************************************************************************************** */

	public paginaReport(String ePubName) {
		this.ePubName = ePubName;
		errorCount = 0;
		warningCount = 0;
		exceptionCount = 0;
	}




	/* ********************************************************************************************************** */

	public paginaReport(String ePubName, String info) {
		this.ePubName = ePubName;
		warning("", 0, 0, info);
		errorCount = 0;
		warningCount = 0;
		exceptionCount = 0;
	}




	/* ********************************************************************************************************** */

	private String fixMessage(String message) {
		if (message == null)
			return "";
		return message.replaceAll("[\\s]+", " ");
	}




	/* ********************************************************************************************************** */

	@Override
	public void hint(String resource, int line, int column, String message) {
		message = fixMessage(message);

		// translate epubcheck results
		if(paginaEPUBChecker.epubcheck_translate) {

			// pagina implementation
			mainGUI.txtarea_results.append(
					__("HINT:") + " "
							+ "\"" + ePubName
							+ (resource == null ? "" : "/" + resource) + "\""
							+ (line <= 0 ? "" : " (" + __("line") + " " + line
									+ (column <= 0 ? "" : ", " + __("col") + " " + column) + ")") + ":\n"
									+ "   " + analyzeString(message)
									+ "\n\n"
					);

			// do not translate epubcheck results
		} else {
			// changed standard implementation from "DefaultReportImpl.java"
			mainGUI.txtarea_results.append(
					"HINT: "
							+ ePubName
							+ (resource == null ? "" : "/" + resource)
							+ (line <= 0 ? "" : "(" + line
									+ (column <= 0 ? "" : "," + column) + ")") + ": "
									+ message
									+ "\n\n"
					);
		}

		// scroll to the end
		mainGUI.txtarea_results.setCaretPosition(mainGUI.txtarea_results.getText().length());
	}




	/* ********************************************************************************************************** */

	@Override
	public void info(String resource, FeatureEnum feature, String value) {

		switch (feature) {
		case FORMAT_VERSION:
			// System.out.println(String.format(Messages.VALIDATING_VERSION_MESSAGE, value));
			
			// "insert at 0" instead of "append" to catch warnings and errors from above
			mainGUI.txtarea_results.insert((String.format(__(Messages.VALIDATING_VERSION_MESSAGE), value )
					+ "\n" + "(http://code.google.com/p/epubcheck/)"
					+ "\n\n"), 0);
			break;
		default:
			if (DEBUG) {
				if (resource == null) {
					System.out.println("INFO: [" + feature + "]=" + value);
				} else {
					System.out.println("INFO: [" + feature + " (" + 
							resource + ")]=" + value);
				}
			}
			break;      
		}
	}




	/* ********************************************************************************************************** */

	public void error(String resource, int line, int column, String message) {
		errorCount++;
		message = fixMessage(message);

		// translate epubcheck results
		if(paginaEPUBChecker.epubcheck_translate) {

			// pagina implementation
			mainGUI.txtarea_results.append(
					__("ERROR:") + " "
							+ "\"" + ePubName
							+ (resource == null ? "" : "/" + resource) + "\""
							+ (line <= 0 ? "" : " (" + __("line") + " " + line
									+ (column <= 0 ? "" : ", " + __("col") + " " + column) + ")") + ":\n"
									+ "   " + analyzeString(message)
									+ "\n\n"
					);

			// do not translate epubcheck results
		} else {
			// standard implementation of "DefaultReportImpl.java"
			mainGUI.txtarea_results.append(
					"ERROR: "
							+ ePubName
							+ (resource == null ? "" : "/" + resource)
							+ (line <= 0 ? "" : "(" + line
									+ (column <= 0 ? "" : "," + column) + ")") + ": "
									+ message
									+ "\n\n"
					);
		}

		// scroll to the end
		mainGUI.txtarea_results.setCaretPosition(mainGUI.txtarea_results.getText().length());
	}




	/* ********************************************************************************************************** */

	public void warning(String resource, int line, int column, String message) {
		warningCount++;
		message = fixMessage(message);

		// translate epubcheck results
		if(paginaEPUBChecker.epubcheck_translate) {

			// pagina implementation
			mainGUI.txtarea_results.append(
					__("WARNING:") + " "
							+ "\"" + ePubName
							+ (resource == null ? "" : "/" + resource) + "\""
							+ (line <= 0 ? "" : " (" + __("line") + " " + line
									+ (column <= 0 ? "" : ", " + __("col") + " " + column) + ")") + ":\n"
									+ "   " + analyzeString(message)
									+ "\n\n"
					);

			// do not translate epubcheck results
		} else {
			// standard implementation of "DefaultReportImpl.java"
			mainGUI.txtarea_results.append(
					"WARNING: "
							+ ePubName
							+ (resource == null ? "" : "/" + resource)
							+ (line <= 0 ? "" : "(" + line
									+ (column <= 0 ? "" : "," + column) + ")") + ": "
									+ message
									+ "\n\n"
					);
		}

		// scroll to the end
		mainGUI.txtarea_results.setCaretPosition(mainGUI.txtarea_results.getText().length());
	}




	/* ********************************************************************************************************** */

	public void exception(String resource, Exception e) {
		exceptionCount++;

		// translate epubcheck results
		if(paginaEPUBChecker.epubcheck_translate) {

			// pagina implementation
			mainGUI.txtarea_results.append(
					__("EXCEPTION:") + " "
							+ "\"" + ePubName
							+ (resource == null ? "" : "/" + resource) + "\":\n"
							+ "   " + e.getMessage()
							+ "\n\n"
					);

			// do not translate epubcheck results
		} else {
			// standard implementation of "DefaultReportImpl.java"
			mainGUI.txtarea_results.append(
					"EXCEPTION: " + ePubName
					+ (resource == null ? "" : "/" + resource) + e.getMessage()
					+ "\n\n"
					);
		}

		// scroll to the end
		mainGUI.txtarea_results.setCaretPosition(mainGUI.txtarea_results.getText().length());
	}




	/* ********************************************************************************************************** */

	public int getErrorCount() {
		return errorCount;
	}

	public int getWarningCount() {
		return warningCount;
	}

	public int getExceptionCount() {
		return exceptionCount;
	}




	/* ********************************************************************************************************** */

	public static String analyzeString(String s) {

		// try to replace all strings without any regex-patterns with the localized version
		try {
			
			//retreive language key
			String s_loc = paginaEPUBChecker.l10n.getString(s);
			
			if(s.equals(s_loc)) {
				// there seems to be no corresponding language key => Try Regex patterns!
				throw new Exception();
				
			} else {
				// there's a corresponding language key => return it!
				return s_loc;
			}
			
			
		// there's no corresponding language key
		// we're assuming the string includes a regex pattern
		} catch (Exception e) {
			
			// do regex stuff
			return paginaEPUBChecker.regex.doReplace(s);

		}
	}




	/* ********************************************************************************************************** */
	// might probably be useful some day if we switch to html formatted results in the gui

	//	public void appendToJTextPane(JTextPane t, String s) {
	//		try {
	//	  		Document doc = t.getDocument();
	//			doc.insertString(doc.getLength(), s, null);
	//		} catch (BadLocationException e) {
	//			e.printStackTrace();
	//		}
	//	}




	/* ********************************************************************************************************** */

	private static String __(String s) {
		return paginaEPUBChecker.l10n.getString(s);
	}

}
