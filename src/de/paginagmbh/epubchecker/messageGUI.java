package de.paginagmbh.epubchecker;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;






/**
  * @author		Tobias Fischer
  * @copyright	pagina GmbH, Tübingen
  * @date 		2013-01-04
  * @lastEdit	Tobias Fischer
  */
public class messageGUI extends JDialog {
	
	private static final long serialVersionUID = 1L;
    private static JFrame f;
    private String windowTitle = __("EPUB Checker - Update");
    
    
    
    /* ***************************************************************************************************************** */
    
	public messageGUI() {
		
		// epub-checker gui isn't loaded yet
		if(paginaEPUBChecker.gui == null) {
			newFrame();
		} else {
			f = paginaEPUBChecker.gui;
		}
        
	}

    
    
    /* ***************************************************************************************************************** */
    
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
        
        /* Icon definieren (Windows only) */
        f.setIconImage(paginaEPUBChecker.logoImg32);
        
	}
    
    
    
    /* ***************************************************************************************************************** */
    
	public void showError(String message) {
        
		JOptionPane.showMessageDialog(f, "<html><b>"+ windowTitle +"</b><br/><br/>" + message + "<br/><br/><html>", windowTitle, JOptionPane.ERROR_MESSAGE);
	}
    
    
    
    /* ***************************************************************************************************************** */
    
	public void showMessage(String message) {
        
		JOptionPane.showMessageDialog(f, "<html><b>"+ windowTitle +"</b><br/><br/>" + message + "<br/><br/><html>", windowTitle, JOptionPane.INFORMATION_MESSAGE);
	}
    
    
    
    /* ***************************************************************************************************************** */
    
	public int showQuestion(String message) {
        
		Object[] options = {__("Start update"), __("Cancel")};
		int n = JOptionPane.showOptionDialog(f, "<html><b>"+ windowTitle +"</b><br/><br/>" + message + "<br/><br/><html>", windowTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		return n;
	}
	
	
	
	
	/* ********************************************************************************************************** */
	
	private static String __(String s) {
		if(paginaEPUBChecker.l10n != null) { 
			return paginaEPUBChecker.l10n.getString(s);
		} else {
			return s;
		}
	}

}
