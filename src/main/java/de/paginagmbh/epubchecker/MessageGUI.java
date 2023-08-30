package de.paginagmbh.epubchecker;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;



/**
 * @author      Tobias Fischer
 * @copyright   pagina GmbH, Tübingen
 * @date        2016-01-24
 */
public class MessageGUI extends JDialog {

	private static final long serialVersionUID = 1L;
	private JFrame f;
	private String windowTitle = __("EPUB Checker - Update");



	/* ***************************************************************************************************************** */

	public MessageGUI() {
		GuiManager guiManager = GuiManager.getInstance();
		if(guiManager.getCurrentGUI() == null) {
			// epub-checker gui isn't loaded yet
			newFrame();
		} else {
			f = guiManager.getCurrentGUI();
		}
	}



	/* ***************************************************************************************************************** */

	@Override
	public void setTitle(String s) {
		windowTitle = s;
	}


	/* ***************************************************************************************************************** */

	public void newFrame() {

		/* GUI-Aussehen definieren */
		setDefaultLookAndFeelDecorated(true);

		/* System-LookAndFeel laden */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}


		f = new JFrame();
		f.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		f.setAlwaysOnTop(true);

		// set window position
		f.setLocation(50, 50);

		/* Icon definieren */
		f.setIconImages(FileManager.logoIcons);
	}



	/* ***************************************************************************************************************** */

	public void showError(String message) {
		showError(message, windowTitle);
	}
	public void showError(String message, String title) {
		JOptionPane.showMessageDialog(f, "<html><b>"+ title +"</b><br/><br/>" + message.replace("\n", "<br/>") + "<br/><br/></html>", windowTitle, JOptionPane.ERROR_MESSAGE);
	}



	/* ***************************************************************************************************************** */

	public void showMessage(String message) {
		showMessage(message, windowTitle);
	}
	public void showMessage(String message, String title) {
		JOptionPane.showMessageDialog(f, "<html><b>"+ title +"</b><br/><br/>" + message.replace("\n", "<br/>") + "<br/><br/></html>", windowTitle, JOptionPane.INFORMATION_MESSAGE);
	}



	/* ***************************************************************************************************************** */

	public int showQuestion(String message) {
		return showQuestion(message, windowTitle);
	}
	public int showQuestion(String message, String title) {
		Object[] options = {__("Start update"), __("Cancel")};
		int n = JOptionPane.showOptionDialog(f, "<html><b>"+ title +"</b><br/><br/>" + message.replace("\n", "<br/>") + "<br/><br/></html>", windowTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		return n;
	}




	/* ********************************************************************************************************** */

	private String __(String s) {
		return GuiManager.getInstance().getCurrentLocalizationObject().getString(s);
	}

}