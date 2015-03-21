package de.paginagmbh.epubchecker;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * keeps the different sub GUI's called from the main GUI menu
 * 
 * @author		Tobias Fischer
 * @copyright	pagina GmbH, Tübingen
 * @date 		2013-07-23
 */
public class subGUI {

	private static JFrame f;
	private static final String about_header = "<html><h2>" + "pagina EPUB-Checker" + "</h2><br/>© 2010-"
			+ new SimpleDateFormat("yyyy").format(new Date())
			+ " pagina GmbH, Tübingen (Germany)<br/>http://www.pagina-online.de<br/><br/>";



	/* ********************************************************************************************************** */

	public subGUI(JFrame parentFrame) {
		// new JFrame
		f = parentFrame;
	}




	/* ********************************************************************************************************** */

	public void displayAboutBox() {

		JOptionPane.showMessageDialog(f,

				about_header
				+ __("about_content-1") + "\n\n"
				+ __("about_content-2")
				.replaceAll("%PROGRAM_VERSION%", paginaEPUBChecker.PROGRAMVERSION + " " + paginaEPUBChecker.PROGRAMRELEASE)
				.replaceAll("%VERSION_DATE%", paginaEPUBChecker.VERSIONDATE)
				+ "\n\n"
				+ __("about_content-3") + "\n\n"
				+ __("about_content-4") + "\n\n",

				__("About"),
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(paginaEPUBChecker.logoImg64)
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
				+ "<b>" + __("German") + "</b>: pagina GmbH" + "<br/>"
				+ "<b>" + __("Russian") + "</b>: Pavel Zuev" + "<br/>"
				+ "<b>" + __("Spanish") + "</b>: Pedro Alamo" + "<br/>"
				+ "<br/></html>",

				__("Translations"),
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(paginaEPUBChecker.logoImg64)
				);
	}




	/* ********************************************************************************************************** */

	public void displayLicenceBox() {

		JOptionPane.showMessageDialog(f,

				about_header + "\n\n"
						+ __("licence content") + "\n\n"

				+ "-------------------------------------------------------------\n\n"

				+ __("adobe bsd licence") + "\n\n\n",

				__("Licence information"),
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(paginaEPUBChecker.logoImg64)
				);

	}




	/* ********************************************************************************************************** */

	private static String __(String s) {
		return paginaEPUBChecker.l10n.getString(s);
	}

}