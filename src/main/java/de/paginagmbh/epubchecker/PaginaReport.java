package de.paginagmbh.epubchecker;

import java.io.File;

import com.adobe.epubcheck.api.EPUBLocation;
import com.adobe.epubcheck.messages.Message;
import com.adobe.epubcheck.messages.Severity;
import com.adobe.epubcheck.util.DefaultReportImpl;
import com.adobe.epubcheck.util.FeatureEnum;
import com.adobe.epubcheck.util.Messages;
import com.adobe.epubcheck.util.PathUtil;



/**
 * overrides the Adobe epubcheck "Report" class to handle all the epubcheck warnings and errors
 * instead of writing them to stderr, we are appending them to the rsult textarea on our GUI
 *
 * @author      Tobias Fischer
 * @copyright   pagina GmbH, Tübingen
 * @version     2.0.3
 * @date        2018-12-04
 */
public class PaginaReport extends DefaultReportImpl {

	private boolean DEBUG = false;
	boolean quiet, saveQuiet;
	private String currentEpubVersion;




	/* ********************************************************************************************************** */

	public PaginaReport(String ePubName) {
	    super(ePubName);
	}




	/* ********************************************************************************************************** */

	// duplicate method fixMessage() since original method is private
	private String fixMessage(String message) {
		if (message == null) {
			return "";
		}
		return message.replaceAll("[\\s]+", " ");
	}




	/* ********************************************************************************************************** */

	@Override
	public void info(String resource, FeatureEnum feature, String value) {

		MainGUI gui = GuiManager.getInstance().getCurrentGUI();

		switch (feature) {
		case FORMAT_VERSION:
			// System.out.println(String.format(Messages.VALIDATING_VERSION_MESSAGE, value));

			// "insert at 0" instead of "append" to catch warnings and errors from above
			gui.insertLogMessageAtFirstPosition(Severity.INFO,
					String.format(Messages.getInstance().get("validating_version_message"), value )
						+ "\n" + "(https://github.com/w3c/epubcheck)"
						+ "\n\n");

			currentEpubVersion = value;
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

	String formatMessage(Message message, EPUBLocation location, Object... args) {
		String fileName = (location.getPath() == null ? "" : "/" + location.getPath());
		fileName = PathUtil.removeWorkingDirectory(fileName);
		return String.format("%1$s (%2$s) %3$s \"%4$s%5$s\"%6$s:\n   %7$s\n\n",
				__(message.getSeverity().toString()),
				message.getID(),
				__(message.getSeverity().toString()+"_preposition"),
				PathUtil.removeWorkingDirectory(this.getEpubFileName()),
				fileName,
				location.getLine() > 0 ? (" (" + __("line") + " " + location.getLine() + (location.getColumn() > 0 ? ", " + __("col") + " " + location.getColumn() : ""))  + ")" : "",
				analyzeString(fixMessage(args != null && args.length > 0 ? message.getMessage(args) : message.getMessage())));
	}




	/* ********************************************************************************************************** */

	@Override
	public void message(Message message, EPUBLocation location, Object... args) {

		MainGUI gui = GuiManager.getInstance().getCurrentGUI();

		Severity severity = message.getSeverity();
		String fileName = (location.getPath() == null ? "" : "/" + location.getPath());

		// clean up fileName (1/2)
		fileName = PathUtil.removeWorkingDirectory(fileName.replaceAll("^//", "/"));
		// if fileName is still an sbolute file path, extract only the epub name
		if(new File(fileName).exists()) {
			fileName = new File(fileName).getName();
		}
		// clean up fileName (2/2)
		fileName = (fileName.endsWith(".epub") ? fileName.replaceAll("^/", "") : fileName);

		String text = formatMessage(message, location, args);
		Object[] tableLogObject = new Object[]{
						severity,
						message.getID(),
						fileName + (location.getLine() > 0 ? ("\n(" + __("line") + " " + location.getLine() + (location.getColumn() > 0 ? ", " + __("col") + " " + location.getColumn() : ""))  + ")" : ""),
						analyzeString(fixMessage(args != null && args.length > 0 ? message.getMessage(args) : message.getMessage()))
					};

		gui.addLogMessage(text, tableLogObject);

		// scroll to the end
		gui.scrollToBottom();
	}




	/* ********************************************************************************************************** */

	public String analyzeString(String s) {

		// try to replace all strings without any regex-patterns with the localized version
		try {

			//retreive language key
			String s_loc = __(s);

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
			return GuiManager.getInstance().getCurrentLocalizationObject().getRegexEngine().doReplace(s);
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

	public String getCurrentEpubVersion() {
		return currentEpubVersion;
	}




	/* ********************************************************************************************************** */

	private String __(String s) {
		return GuiManager.getInstance().getCurrentLocalizationObject().getString(s);
	}

}
