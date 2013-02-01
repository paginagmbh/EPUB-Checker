package de.paginagmbh.epubchecker;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;


/**
  * creates the small "about" guis
  * 
  * @author		Tobias Fischer
  * @copyright	pagina GmbH, Tübingen
  * @date 		2012-05-20
  * @lastEdit	Tobias Fischer
  */
public class boxGUI {
	
    private static JFrame f;
    private static final String about_header = "<html><h2>" + "pagina EPUB-Checker" + "</h2>© 2010-"
    											+ new SimpleDateFormat("yyyy").format(new Date())
    											+ " pagina GmbH, Tübingen (Germany)<br/>http://www.pagina-online.de<br/><br/>";
    
    
    
    /* ********************************************************************************************************** */
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {

		// load and set system LookAndFeel
		try {
			JFrame.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
    	// new JFrame
        f = new JFrame();
        f.setResizable(true);

        f.setSize(530,400);
        
        f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                f.setSize(new Dimension(500, f.getHeight()));
                super.componentResized(e);
            }
        });

        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{25, 84, 366, 25, 0};
        gridBagLayout.rowHeights = new int[]{25, 0, 20, 0, 25, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        f.getContentPane().setLayout(gridBagLayout);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(boxGUI.class.getResource("/resources/icons/paginaEPUBChecker_64.png")));
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblNewLabel.ipadx = 20;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 1;
        gbc_lblNewLabel.gridy = 1;
        f.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel(about_header);
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 2;
        gbc_lblNewLabel_1.gridy = 1;
        f.getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
        
        JTextArea txtrcopyrightc = new JTextArea();
        txtrcopyrightc.setWrapStyleWord(true);
        txtrcopyrightc.setLineWrap(true);
        txtrcopyrightc.setText("<html><small>Copyright (c) 2007 Adobe Systems Incorporated  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.  THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.</small></html>");
        GridBagConstraints gbc_txtrcopyrightc = new GridBagConstraints();
        gbc_txtrcopyrightc.insets = new Insets(0, 0, 5, 5);
        gbc_txtrcopyrightc.fill = GridBagConstraints.BOTH;
        gbc_txtrcopyrightc.gridx = 2;
        gbc_txtrcopyrightc.gridy = 3;
        f.getContentPane().add(txtrcopyrightc, gbc_txtrcopyrightc);
        
        f.setVisible(true);
        
	}
    
    
    
    /* ********************************************************************************************************** */
	
	public boxGUI(JFrame parentFrame) {
		
    	// new JFrame
        f = parentFrame;
	}

}
