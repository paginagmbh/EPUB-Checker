package de.paginagmbh.common.internet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import javax.swing.*;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;

import de.paginagmbh.epubchecker.paginaEPUBChecker;
import de.paginagmbh.epubchecker.updateCheck;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;



/**
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tübingen
 * @date 		2015-10-08
 */
public class FileDownloader {

	private static JLabel downloadInfo;
	private static JButton btn_Button;
	private static String downloadPath;
	private static JDialog f;

	// html tags for label wrap
	private static final String html1 = "<html><body style='width:400px'>";
	private static final String html2 = "</body></html>";


	/* ***************************************************************************************************************** */

	// Constructor
	public FileDownloader(String url, String downloadP, String message) {

		downloadPath = downloadP;

		/* neuen Frame instanzieren und Titel setzen */
		f = new JDialog(paginaEPUBChecker.gui);
		f.setTitle(__("EPUB Checker - Update"));

		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		/* System-LookAndFeel laden */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Icon definieren (Windows only) */
		f.setIconImage(paginaEPUBChecker.logoImg32);

		/* Fenstergröße setzen */
		f.setSize(570,340);
		f.setResizable(false);

		// set window position
		f.setLocation(100, 100);

		Container c = f.getContentPane();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{25, 0, 425, 25, 0};
		gridBagLayout.rowHeights = new int[]{25, 49, -23, 14, 34, 14, 32, 29, 10, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		f.getContentPane().setLayout(gridBagLayout);

		JLabel lbl_Icon = new JLabel();
		Icon paginaIcon = new ImageIcon(paginaEPUBChecker.logoImg64);
		lbl_Icon.setIcon(paginaIcon);
		GridBagConstraints gbc_lbl_Icon = new GridBagConstraints();
		gbc_lbl_Icon.anchor = GridBagConstraints.NORTH;
		gbc_lbl_Icon.ipadx = 20;
		gbc_lbl_Icon.gridheight = 2;
		gbc_lbl_Icon.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_Icon.gridx = 1;
		gbc_lbl_Icon.gridy = 1;
		f.getContentPane().add(lbl_Icon, gbc_lbl_Icon);

		// Header
		JLabel lbl_header = new JLabel(__("EPUB Checker - Update"));
		lbl_header.setFont(lbl_header.getFont().deriveFont(lbl_header.getFont().getStyle() | Font.BOLD, lbl_header.getFont().getSize() + 5f));
		GridBagConstraints gbc_lbl_header = new GridBagConstraints();
		gbc_lbl_header.anchor = GridBagConstraints.NORTHWEST;
		gbc_lbl_header.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_header.gridx = 2;
		gbc_lbl_header.gridy = 1;
		c.add(lbl_header, gbc_lbl_header);

		// TextPane "message"
		JTextPane tp_message = new JTextPane();
		tp_message.setFont(tp_message.getFont().deriveFont(12f));
		tp_message.setBackground(c.getBackground());
		tp_message.setEditable(false);
		tp_message.setText(message);
		GridBagConstraints gbc_tp_message = new GridBagConstraints();
		gbc_tp_message.fill = GridBagConstraints.BOTH;
		gbc_tp_message.insets = new Insets(0, 0, 5, 5);
		gbc_tp_message.gridx = 2;
		gbc_tp_message.gridy = 2;
		c.add(tp_message, gbc_tp_message);


		// Label "Download: File.Ext"
		downloadInfo = new JLabel(html1 + __("Download") + ": " + url + html2);
		downloadInfo.setVerticalAlignment(SwingConstants.BOTTOM);
		downloadInfo.setFont(downloadInfo.getFont().deriveFont(11f));
		GridBagConstraints gbc_downloadInfo = new GridBagConstraints();
		gbc_downloadInfo.gridwidth = 2;
		gbc_downloadInfo.ipady = 40;
		gbc_downloadInfo.fill = GridBagConstraints.BOTH;
		gbc_downloadInfo.insets = new Insets(0, 0, 5, 5);
		gbc_downloadInfo.gridx = 1;
		gbc_downloadInfo.gridy = 3;
		c.add(downloadInfo, gbc_downloadInfo);
		downloadInfo.setHorizontalAlignment(SwingConstants.CENTER);

		// ProgressBar
		JProgressBar progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.gridwidth = 2;
		gbc_progressBar.fill = GridBagConstraints.BOTH;
		gbc_progressBar.insets = new Insets(0, 0, 5, 5);
		gbc_progressBar.gridx = 1;
		gbc_progressBar.gridy = 4;
		c.add(progressBar, gbc_progressBar);

		// Label "Downloadstatus"
		JLabel progressInfo = new JLabel(__("Estimating download size..."));
		progressInfo.setFont(progressInfo.getFont().deriveFont(11f));
		GridBagConstraints gbc_progressInfo = new GridBagConstraints();
		gbc_progressInfo.gridwidth = 2;
		gbc_progressInfo.fill = GridBagConstraints.BOTH;
		gbc_progressInfo.insets = new Insets(0, 0, 5, 5);
		gbc_progressInfo.gridx = 1;
		gbc_progressInfo.gridy = 5;
		c.add(progressInfo, gbc_progressInfo);
		progressInfo.setHorizontalAlignment(SwingConstants.CENTER);


		btn_Button = new JButton(__("Cancel"));
		btn_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				f.dispose();
				try {
					updateCheck.dlgui.finalize();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btn_Button = new GridBagConstraints();
		gbc_btn_Button.gridwidth = 2;
		gbc_btn_Button.insets = new Insets(0, 0, 5, 5);
		gbc_btn_Button.anchor = GridBagConstraints.NORTH;
		gbc_btn_Button.gridx = 1;
		gbc_btn_Button.gridy = 7;
		f.getContentPane().add(btn_Button, gbc_btn_Button);


		// Build String "Download Location"
		String downloadLocation = downloadPath + File.separator + url.substring(url.lastIndexOf("/") + 1);


		f.setVisible(true);





		Boolean dl_status = false;

		try {

			dl_status = downloadFile(url, downloadLocation, progressBar, progressInfo);

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		if(dl_status==true && new File(downloadLocation).exists()) {

			// Update Download-URI label with thank you note
			downloadInfo.setText(html1 + __("Thanks for updating!") + " " + __("The new version was saved on your desktop.") + html2);
			// Update file size label with UNZIP hint (refs issue #5) in bold and slightly bigger font size
			progressInfo.setText(html1 + __("Please unzip the downloaded update and replace your current version manually!") + html2);
			progressInfo.setFont(progressInfo.getFont().deriveFont(Font.BOLD, progressInfo.getFont().getSize() + 1f));

			btn_Button.setText(__("Finish update"));
			for(int i=0; i< btn_Button.getActionListeners().length; i++) {
				btn_Button.removeActionListener(btn_Button.getActionListeners()[i]);
			}
			btn_Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					Desktop desktop = null;

					if (Desktop.isDesktopSupported()) {
						desktop = Desktop.getDesktop();

						try {
							desktop.open(new File(downloadPath));
						} catch (IOException e) { ; }
					}

					System.exit(0);
				}
			});

		} else {

			downloadInfo.setText(__("Failed to download the update!"));
			btn_Button.setText(__("Finish update"));

			return;
		}
	}



	/* ***************************************************************************************************************** */

	public static String readableFileSize(long size) {
		if(size <= 0) return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		return new DecimalFormat("#,##0.0").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}



	/* ***************************************************************************************************************** */

	public static boolean downloadFile(String url_str, String downloadLocation, JProgressBar progressBar, JLabel progressInfo) throws MalformedURLException, ProtocolException, IOException {

		// OutputStream zum Speicherort öffnen
		FileOutputStream fos = new FileOutputStream(downloadLocation);

		// Download-URL aus Parameter instanzieren
		URL url = new URL(url_str.replace(" ", "%20"));

		// HTTPConnection öffnen
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();

		// Größe des Downloads ermitteln
		int size = conn.getContentLength();
		//System.out.println("Download-Größe: " + size + "Byte");

		// HTTP ResponseCode abfragen
		int responseCode = conn.getResponseCode();


		if (responseCode == HttpURLConnection.HTTP_OK) {

			// Buffergröße festlegen
			byte tmp_buffer[] = new byte[4096];

			// InputStream instanzieren
			InputStream is = conn.getInputStream();

			// lokale Variablen instanzieren
			int n;	// aktueller Buffer
			long i = 0;	// kumulierte Buffergröße
			int p = 0;	// Prozentuale Angabe des bisherigen Downloads

			// ProgressBar instanzieren
			progressBar.setIndeterminate(false); 
			progressBar.setString(null);
			progressBar.setValue(p);

			// Buffer des InputStreams abarbeiten
			while ((n = is.read(tmp_buffer)) > 0) {

				// aktuellen Buffer in den OutputStream schreiben
				fos.write(tmp_buffer, 0, n);
				fos.flush();

				// kumulierte Buffergröße schreiben ("bereits heruntergeladene Bytes")
				i = i + n;

				// prozentualen Wert ermitteln ("bereits heruntergeladene Bytes in prozent der Gesamtdatei")
				p = Math.round( (i*100)/size );

				// ProgressBar aktualisieren
				progressBar.setValue(p);

				// ProgressInfo aktualisieren
				progressInfo.setText(readableFileSize(i) + " " + __("of") + " " + readableFileSize(size));
			}

			// close output stream
			fos.flush();
			fos.close();

			// wenn Download zu 100% erfolgt ist, dann nochmals ProgressBar aktualisieren und Methode mit Rückgabewert "true" beenden
			if(p==100) {
				progressBar.setValue(p);
				return true;
			} else {
				return false;
			}

		} else {

			// close output stream
			fos.flush();
			fos.close();

			downloadInfo.setText(__("Failed to download the update!"));
			btn_Button.setText(__("Finish update"));

			return false;
		}
	}




	/* ********************************************************************************************************** */

	private static String __(String s) {
		return paginaEPUBChecker.l10n.getString(s);
	}
}