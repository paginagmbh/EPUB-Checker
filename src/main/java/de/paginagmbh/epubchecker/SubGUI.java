package de.paginagmbh.epubchecker;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * keeps the different sub GUI's called from the main GUI menu
 *
 * @author      Tobias Fischer
 * @copyright   pagina GmbH, Tübingen
 * @date        2019-03-26
 */
public class SubGUI {

	private JFrame f;
	private final String about_header = "<html><h2>" + "pagina EPUB-Checker" + "</h2><br/>© 2010-"
			+ new SimpleDateFormat("yyyy").format(new Date())
			+ " pagina GmbH, Tübingen (Germany)<br/>https://www.pagina.gmbh<br/><br/>";



	/* ********************************************************************************************************** */

	public SubGUI(JFrame parentFrame) {
		// new JFrame
		f = parentFrame;
	}




	/* ********************************************************************************************************** */

	public void displayAboutBox() {

		JOptionPane.showMessageDialog(f,

				about_header
				+ __("about_content-1") + "\n\n"
				+ __("about_content-2")
				.replaceAll("%PROGRAM_VERSION%", PaginaEPUBChecker.PROGRAMVERSION + " " + PaginaEPUBChecker.PROGRAMRELEASE.toLowerCase())
				.replaceAll("%VERSION_DATE%", PaginaEPUBChecker.VERSIONDATE)
				+ "\n\n"
				+ __("about_content-3") + "\n\n"
				+ __("about_content-4") + "\n\n"
				+ "Java Version: " + System.getProperty("java.version") + "\n\n",

				__("About"),
				JOptionPane.INFORMATION_MESSAGE,
				FileManager.logoIcon64
				);
	}




	/* ********************************************************************************************************** */

	public void displayTranslationBox() {

		JOptionPane.showMessageDialog(f,

				about_header + "\n\n"
						+ __("translation_content-1") + "\n\n"
						+ __("translation_content-2") + "\n\n"

				+ "<html>"
				+ "<b>" + __("French") + "</b>: Quentin Valmori" + "<br/>"
				+ "<b>" + __("German") + "</b>: Tobias Fischer (pagina GmbH)" + "<br/>"
				+ "<b>" + __("Russian") + "</b>: Pavel Zuev" + "<br/>"
				+ "<b>" + __("Spanish") + "</b>: Pedro Alamo" + "<br/>"
				+ "<b>" + __("Japanese") + "</b>: Masayoshi Takahashi" + "<br/>"
				+ "<b>" + __("Portuguese [BR]") + "</b>: Thiago de Oliveira Pereira" + "<br/>"
				+ "<b>" + __("Czech") + "</b>: Petr Burkot" + "<br/>"
				+ "<b>" + __("Chinese [TW]") + "</b>: Bobby Tung" + "<br/>"
				+ "<b>" + __("Turkish") + "</b>: N. Erhan Üzümcü" + "<br/>"
				+ "<br/></html>",

				__("Translations"),
				JOptionPane.INFORMATION_MESSAGE,
				FileManager.logoIcon64
				);
	}




	/* ********************************************************************************************************** */

	public void displayLicenceBox() {

		JOptionPane.showMessageDialog(f,

				about_header + "\n\n"
						+ __("licence content") + "\n\n"

				+ "-------------------------------------------------------------\n\n"

				+ __("new bsd license") + "\n\n\n",

				__("License information"),
				JOptionPane.INFORMATION_MESSAGE,
				FileManager.logoIcon64
				);

	}




	/* ********************************************************************************************************** */

	private String __(String s) {
		return GuiManager.getInstance().getCurrentLocalizationObject().getString(s);
	}

}
