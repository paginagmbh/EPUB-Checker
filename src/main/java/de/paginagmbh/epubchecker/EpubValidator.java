package de.paginagmbh.epubchecker;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.SwingWorker;

import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.api.Report;

/**
 * Validates EPUB files with EpubCheck
 * in a SwingWorker instance
 * 
 * @author   Tobias Fischer
 * @date     2016-12-12
 */
public class EpubValidator {

	private static GuiManager guiManager = GuiManager.getInstance();
	private final mainGUI gui = guiManager.getCurrentGUI();
	private final boolean autoSaveLog = guiManager.getMenuOptionAutoSave();
	private long timestamp_begin;
	private long timestamp_end;
	private boolean epubcheckResult;
	protected File epubFile = null;
	private String resultMessage = "";

	// Setters available
	private boolean expanded = false;
	private File expandedBasedir = null;
	private boolean keepArchive = false;
	private Report report = null;




	/* ********************************************************************************************************** */

	public EpubValidator(Report report) {
		this.report = report;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public void setExpandedBasedir(File expandedBasedir) {
		this.expandedBasedir = expandedBasedir;
	}

	public void setKeepArchive(boolean keepArchive) {
		this.keepArchive = keepArchive;
	}




	/* ********************************************************************************************************** */

	public void validate(File file) {
		this.epubFile = file;

		// set "begin" timestamp
		timestamp_begin = System.currentTimeMillis();

		// clear and reset TextArea and Table
		gui.clearLog();

		// Print timestamp of current epubcheck
		Calendar cal = Calendar.getInstance();
		cal.setTime( new Date() );
		DateFormat formater = DateFormat.getDateTimeInstance( DateFormat.LONG, DateFormat.LONG );
		gui.addLogMessageToTextLog(formater.format(cal.getTime()) + "\n\n\n");
		gui.addLogMessageToTextLog("---------------------------------------------------\n\n");

		// disable validation button && "save" menuItem
		gui.disableButtonsDuringValidation();

		// set the loading icon and update the statusbar
		gui.getStatusBar().update(FileManager.iconLoading, __("Checking file"));

		// reset border color to normal
		gui.setBorderStateNormal();


		// init SwingWorker
		SwingWorker<Void, Void> validationWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {

				// run original epubcheck
				EpubCheck epubcheck = new EpubCheck(epubFile, report);
				epubcheckResult = epubcheck.validate();

				return null;
			}


			@Override
			protected void done() {

				// validation finished with warnings or errors
				if(epubcheckResult == false) {

					// set border color to red
					gui.setBorderStateError();

					// add separator in text mode
					gui.addLogMessageToTextLog("\n" + "---------------------------------------------------");


					// warnings AND errors
					if(report.getErrorCount() > 0 && report.getWarningCount() > 0) {
						resultMessage = String.format(__("Check finished with %1$1s warnings and %2$1s errors!"), report.getWarningCount(), report.getErrorCount());

					// only errors
					} else if(report.getErrorCount() > 0) {
						resultMessage = String.format(__("Check finished with %d errors!"), report.getErrorCount());

					// only warnings
					} else if(report.getWarningCount() > 0) {
						// set border color to orange
						gui.setBorderStateWarning();
						resultMessage = String.format(__("Check finished with %d warnings!"), report.getWarningCount());

					// something went wrong
					} else {
						resultMessage = __("Check finished with warnings or errors!");
					}

					// add result message to log
					gui.addLogMessage("\n\n" + resultMessage + "\n");


					// set error counter in mac dock badge
					if(guiManager.getMacApp() != null) {
						if(report.getWarningCount() + report.getErrorCount() > 0) {
							guiManager.getMacApp().setDockIconBadge(new Integer(report.getWarningCount() + report.getErrorCount()).toString());
						} else {
							guiManager.getMacApp().setDockIconBadge("error");
						}
					}


					// delete the temporarily created EPUB file cause it's invalid
					if(expanded && epubFile.exists()) {
						epubFile.delete();
						gui.addLogMessage("\n\n" + __("EPUB from source folder wasn't saved because it contains errors or warnings!") + "\n");
					}


				// validation finished without warnings or errors
				} else {

					// set border color to green
					gui.setBorderStateValid();

					// translateLog the output
					resultMessage = __("No errors or warnings detected");
					gui.addLogMessage("\n\n" + resultMessage + "\n");


					// set error counter in mac dock badge
					if(guiManager.getMacApp() != null) {
						guiManager.getMacApp().setDockIconBadge("✓");
					}


					// mode "expanded" : show a message "epub" saved successfully
					if(expanded && epubFile.exists() && keepArchive) {
						// #11: move temp epub to basedir
						if(expandedBasedir != null && expandedBasedir.exists()) {
							File destEpubFile = new File(expandedBasedir, epubFile.getName().replaceAll("\\.epub$", "") + "__created-with-epubcheck.epub");
							if(destEpubFile.exists()) {
								destEpubFile.delete();
							}
							epubFile.renameTo(destEpubFile);
							if(destEpubFile.exists()) {
								gui.addLogMessage("\n\n" + __("EPUB from source folder was successfully saved!") + "\n" + destEpubFile.getAbsolutePath() + "\n");
							} else {
								gui.addLogMessage("\n\n" + __("EPUB from source folder couldn't be saved next to the source folder!") + "\n");
							}
						} else {
							gui.addLogMessage("\n\n" + __("EPUB from source folder couldn't be saved next to the source folder!") + "\n" + "Error 2" + "\n");
						}
					}
				}


				// scroll to the end
				gui.scrollToBottom();


				// set "end" timestamp
				timestamp_end = System.currentTimeMillis();

				// calculate the processing duration
				double timestamp_diff = timestamp_end-timestamp_begin;
				DecimalFormat df = new DecimalFormat("0.0#");
				String timestamp_result = df.format(timestamp_diff/1000);

				// remove the loading icon and update the status bar
				gui.getStatusBar().update(null,
						__("Done") + ". "
						+ String.format(__("Validated in %s seconds"), timestamp_result) + ". "
						+ resultMessage);

				// re-enable validation button && "save" menuItem
				gui.enableButtonsAfterValidation();

				// Auto Save logfile if desired
				if(autoSaveLog) {
					gui.saveLogfile(new File(epubFile.getAbsolutePath().replaceAll("(?i)\\.epub", "_log.txt")));
				}
			}
		};

		// execute SwingWorker
		validationWorker.execute();
	}




	/* ********************************************************************************************************** */

	private String __(String s) {
		return GuiManager.getInstance().getCurrentLocalizationObject().getString(s);
	}
}
