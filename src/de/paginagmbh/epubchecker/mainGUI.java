package de.paginagmbh.epubchecker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import com.adobe.epubcheck.api.EpubCheck;
import de.paginagmbh.common.gui.DashedLineBorder;
import de.paginagmbh.common.gui.StatusBar;
import de.paginagmbh.common.internet.OpenURIinBrowser;
import javax.swing.UIManager;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;


/**
  * loads the main window of the EPUB-Checker
  * 
  * @author		Tobias Fischer
  * @copyright	pagina GmbH, Tübingen
  * @date 		2013-12-13
  * @lastEdit	Tobias Fischer
  */
public class mainGUI extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
    public static JTextField input_filePath;
    public static JTextArea txtarea_results;
    public static JScrollPane scroll_results;
    public static JLabel lbl_test;
    public static JButton btn_validateEpub, btn_chooseEpubFile;
    public static JMenuItem mnItem_Open, mnItem_Save, mnItem_Exit, mnItem_About, mnItem_Translations, mnItem_licenceInformation, mnItem_WebsiteEpubcheck, mnItem_WebsitePagina, mnItem_Updates;
    JMenu mn_File, mn_Language, mn_Help;
    public static JRadioButtonMenuItem opt_AutoSave, opt_Translate;
    public static StatusBar statusBar;
    private JMenu mn_Log;
    
    
    // language stuff
    private static JRadioButtonMenuItem[] opt_Lang;
    private static String[] availableLanguagesOriginal;
    
    
    
    
    /* ********************************************************************************************************** */
	
    public mainGUI() {

    	// new JFrame
        super("pagina EPUB-Checker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // set Icon (Windows only)
        setIconImage(paginaEPUBChecker.logoImg32);
    	
        // set Apple specific system properties
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "pagina EPUB-Checker");
		System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
		System.setProperty("com.apple.mrj.application.live-resize", "true");
        
        // set window size
		if(paginaEPUBChecker.MainGuiDimension == null) {
			setSize(775,650);
		} else {
			setSize(paginaEPUBChecker.MainGuiDimension);
		}
		
		
		// set minimum size
        setMinimumSize(new Dimension(525,450));
        
        
        // set window position
        if(paginaEPUBChecker.MainGuiPosition == null) {
        	 setLocation(50, 50);
		} else {
			 setLocation(paginaEPUBChecker.MainGuiPosition);
		}
       
        
        // set gui to be always on top
        setAlwaysOnTop(false);
        
        

        Container parent = getContentPane();
        BorderLayout borderLayout = new BorderLayout();
        parent.setLayout(borderLayout);
        
        
        Container main = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{25, 50, 103, 50, 25, 0};
        gridBagLayout.rowHeights = new int[]{25, 45, 15, 25, 14, 15, 250, 25};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0};
        
        main.setLayout(gridBagLayout);
        parent.add(main,BorderLayout.CENTER);
        
        btn_chooseEpubFile = new JButton(__("choose EPUB file"));
        btn_chooseEpubFile.setFont(btn_chooseEpubFile.getFont().deriveFont(12f));
        btn_chooseEpubFile.addActionListener(this);
        GridBagConstraints gbc_btn_chooseEpubFile = new GridBagConstraints();
        gbc_btn_chooseEpubFile.anchor = GridBagConstraints.WEST;
        gbc_btn_chooseEpubFile.insets = new Insets(0, 0, 5, 5);
        gbc_btn_chooseEpubFile.gridx = 1;
        gbc_btn_chooseEpubFile.gridy = 1;
        main.add(btn_chooseEpubFile, gbc_btn_chooseEpubFile);
        
        input_filePath = new JTextField();
        input_filePath.addActionListener(this);
        input_filePath.setFont(input_filePath.getFont().deriveFont(12f));
        GridBagConstraints gbc_input_filePath = new GridBagConstraints();
        gbc_input_filePath.ipady = 5;
        gbc_input_filePath.ipadx = 5;
        gbc_input_filePath.fill = GridBagConstraints.HORIZONTAL;
        gbc_input_filePath.insets = new Insets(0, 0, 5, 5);
        gbc_input_filePath.gridwidth = 2;
        gbc_input_filePath.gridx = 2;
        gbc_input_filePath.gridy = 1;
        main.add(input_filePath, gbc_input_filePath);
        
        // Key listener listening to the filepath input field and changing the validate buttons status
        KeyListener keyListener = new KeyListener() {
        	public void keyPressed(KeyEvent keyEvent) { }
        	public void keyTyped(KeyEvent e) { }
        	public void keyReleased(KeyEvent keyEvent) {
        		if(input_filePath.getText().length() > 0) {
        			if(btn_validateEpub.isEnabled() == false) {
        				btn_validateEpub.setEnabled(true);
        			}
        		} else {
        			if(btn_validateEpub.isEnabled() == true) {
        				btn_validateEpub.setEnabled(false);
        			}
        		}
        	}
        };
        input_filePath.addKeyListener(keyListener);
        
        btn_validateEpub = new JButton(__("validate EPUB"));
        btn_validateEpub.setEnabled(false);
        btn_validateEpub.addActionListener(this);
        btn_validateEpub.setFont(btn_validateEpub.getFont().deriveFont(btn_validateEpub.getFont().getStyle() | Font.BOLD, btn_validateEpub.getFont().getSize() + 3f));
        GridBagConstraints gbc_btn_validateEpub = new GridBagConstraints();
        gbc_btn_validateEpub.ipady = 15;
        gbc_btn_validateEpub.ipadx = 10;
        gbc_btn_validateEpub.gridwidth = 3;
        gbc_btn_validateEpub.insets = new Insets(0, 0, 5, 5);
        gbc_btn_validateEpub.gridx = 1;
        gbc_btn_validateEpub.gridy = 3;
        main.add(btn_validateEpub, gbc_btn_validateEpub);
        
        JLabel lbl_epubcheckVersion = new JLabel("(" + __(String.format("epubcheck %1$1s", EpubCheck.version())) + ")");
        lbl_epubcheckVersion.setForeground(Color.DARK_GRAY);
        lbl_epubcheckVersion.setFont(lbl_epubcheckVersion.getFont().deriveFont(10f));
        lbl_epubcheckVersion.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lbl_epubcheckVersion = new GridBagConstraints();
        gbc_lbl_epubcheckVersion.gridwidth = 3;
        gbc_lbl_epubcheckVersion.fill = GridBagConstraints.BOTH;
        gbc_lbl_epubcheckVersion.insets = new Insets(0, 0, 5, 5);
        gbc_lbl_epubcheckVersion.gridx = 1;
        gbc_lbl_epubcheckVersion.gridy = 4;
        main.add(lbl_epubcheckVersion, gbc_lbl_epubcheckVersion);
        
        txtarea_results = new JTextArea();
        txtarea_results.setFont(UIManager.getFont("OptionPane.messageFont"));
        txtarea_results.setDropMode(DropMode.INSERT);
        txtarea_results.setEditable(false);
        txtarea_results.setWrapStyleWord(true);
        txtarea_results.setLineWrap(true);
        txtarea_results.setBackground(new Color(255,255,240));
        txtarea_results.setMargin(new Insets(10,10,10,15));
        
        // try catch is needed for running this app on openjdk on ubuntu
        // don't know exactly why...
        try {
        	txtarea_results.setText(__("Drag & Drop here to validate! Either an EPUB file or an expanded EPUB folder..."));
	    } catch (Exception e) {
//	        e.printStackTrace();
	    }
        
        GridBagConstraints gbc_txtarea_results = new GridBagConstraints();
        gbc_txtarea_results.insets = new Insets(0, 0, 5, 5);
        gbc_txtarea_results.fill = GridBagConstraints.BOTH;
        gbc_txtarea_results.gridwidth = 3;
        gbc_txtarea_results.gridx = 1;
        gbc_txtarea_results.gridy = 6;
        
        scroll_results = new JScrollPane(txtarea_results);
        setBorderStateNormal();
        GridBagConstraints gbc_scroll_results = new GridBagConstraints();
        gbc_scroll_results.insets = new Insets(0, 0, 5, 5);
        gbc_scroll_results.fill = GridBagConstraints.BOTH;
        gbc_scroll_results.gridwidth = 3;
        gbc_scroll_results.gridx = 1;
        gbc_scroll_results.gridy = 6;
        main.add(scroll_results, gbc_scroll_results);
        
        // Create the drag and drop listener
        DragDropListener txtareaDNDListener = new DragDropListener();
        // Connect the label with a drag and drop listener
        new DropTarget(txtarea_results, txtareaDNDListener);
        
        
		// Windows
		if(System.getProperty("os.name").indexOf("Windows") > -1) {
			statusBar = new StatusBar(null, "", true);
		} else {
			statusBar = new StatusBar(null, "", false);
		}
		parent.add(statusBar,BorderLayout.PAGE_END);
        
		
        lbl_test = new JLabel();
        GridBagConstraints gbc_lbl_test = new GridBagConstraints();
        gbc_lbl_test.insets = new Insets(0, 0, 5, 5);
        gbc_lbl_test.gridx = 3;
        gbc_lbl_test.gridy = 2;
        main.add(lbl_test, gbc_lbl_test);
        
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        mn_File = new JMenu(__("File"));
        menuBar.add(mn_File);
        
        mnItem_Open = new JMenuItem(__("Open"));
        if(paginaEPUBChecker.os_name.equals("mac")) {
        	mnItem_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_MASK));
        } else {
        	mnItem_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        }
        mnItem_Open.addActionListener(this);
        mn_File.add(mnItem_Open);
        
        
        // not a mac system
        if(!paginaEPUBChecker.os_name.equals("mac")) {
        	
	        mn_File.addSeparator();
	        
	        mnItem_Exit = new JMenuItem(__("Exit"));
	        if(paginaEPUBChecker.os_name.equals("windows")) {
	        	mnItem_Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
	        } else {
	        	mnItem_Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
	        }
	        mnItem_Exit.addActionListener(this);
	        mn_File.add(mnItem_Exit);
        }
        
        
        mn_Log = new JMenu(__("Logfile"));
        menuBar.add(mn_Log);
        
        mnItem_Save = new JMenuItem(__("Save logfile"));
        if(paginaEPUBChecker.os_name.equals("mac")) {
        	mnItem_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.META_MASK));
        } else {
        	mnItem_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        }
        mnItem_Save.setEnabled(false);
        mnItem_Save.addActionListener(this);
        mn_Log.add(mnItem_Save);
        
        mn_Log.addSeparator();
        
        opt_AutoSave = new JRadioButtonMenuItem(__("Auto Save"));
        opt_AutoSave.addActionListener(this);
        mn_Log.add(opt_AutoSave);
        
        
        
        mn_Language = new JMenu(__("Language"));
        menuBar.add(mn_Language);
        
        
        // init the AvailableLanguage/-button arrays
        opt_Lang = new JRadioButtonMenuItem[paginaEPUBChecker.availableLanguages.length];
        availableLanguagesOriginal = new String[paginaEPUBChecker.availableLanguages.length];
        
        // iterate over the available langauges
        for(int i=0; i<paginaEPUBChecker.availableLanguages.length; i++) {
        	
        	// add the translated language string to the "original" array
        	// later on we need the translated string to determine which language was clicked
        	availableLanguagesOriginal[i] = __(paginaEPUBChecker.availableLanguages[i]);
        	
        	
        	// make AWT RadioButton
			opt_Lang[i] = new JRadioButtonMenuItem(__(paginaEPUBChecker.availableLanguages[i]));
			// select RadioButton if language equals
            if(paginaEPUBChecker.programLanguage.equals(paginaEPUBChecker.availableLanguages[i].toLowerCase())) { opt_Lang[i].setSelected(true); }
            // add RadioButton to "language" menu item
            mn_Language.add(opt_Lang[i]);
            // add actionListener
            opt_Lang[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	// get index of Menu Label in Array "availableLanguagesOriginal"
                	int index = getIndex(availableLanguagesOriginal, e.paramString().split(",")[1].replaceAll("cmd=", ""));
                	// get english language string of given index in array "availableLanguages"
                	restartWithNewLanguage(paginaEPUBChecker.availableLanguages[index].toLowerCase());
                }
            });
        }
        
        
        
        mn_Language.addSeparator();

        opt_Translate = new JRadioButtonMenuItem(__("Translate epubcheck log messages"));
        opt_Translate.addActionListener(this);
        mn_Language.add(opt_Translate);
        
        
        mn_Help = new JMenu(__("Help"));
        menuBar.add(mn_Help);
        
        mnItem_About = new JMenuItem(__("About"));
        mnItem_About.addActionListener(this);
        mn_Help.add(mnItem_About);
        
        mnItem_Translations = new JMenuItem(__("Translations"));
        mnItem_Translations.addActionListener(this);
        mn_Help.add(mnItem_Translations);
        
        mnItem_licenceInformation = new JMenuItem(__("Licence information"));
        mnItem_licenceInformation.addActionListener(this);
        mn_Help.add(mnItem_licenceInformation);
        
        mn_Help.addSeparator();
        
        mnItem_WebsiteEpubcheck = new JMenuItem(__("Visit epubcheck-Website"));
        mnItem_WebsiteEpubcheck.addActionListener(this);
        mn_Help.add(mnItem_WebsiteEpubcheck);
        
        mnItem_WebsitePagina = new JMenuItem(__("Visit pagina-online.de"));
        mnItem_WebsitePagina.addActionListener(this);
        mn_Help.add(mnItem_WebsitePagina);
        
        mn_Help.addSeparator();
        
        mnItem_Updates = new JMenuItem(__("Check for updates..."));
        mnItem_Updates.addActionListener(this);
        mn_Help.add(mnItem_Updates);
        
        
        
        
        

        // show GUI
        setVisible(true);
        paginaEPUBChecker.guiReady = true;
        
        
        
        // start validating immediately if a file has been set yet (e.g. when changing the language)
        validateImmediatelyIfFileIsSet();
        
        
        
        
        // Swing Worker which reads and sets the "autoSave" and "translate" options in a background task
        SwingWorker<Void, Void> setOptionsWorker = new SwingWorker<Void, Void>() {

        	@Override
        	protected Void doInBackground() throws Exception {
        		
        		// AutoSave
        		if(new File(paginaEPUBChecker.path_AutoSaveFile).exists()) {
        			
        			try {
        				paginaEPUBChecker.AutoSave = Boolean.valueOf(updateCheck.readFileAsString(paginaEPUBChecker.path_AutoSaveFile));
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			if(paginaEPUBChecker.AutoSave) {
        				opt_AutoSave.setSelected(true);
        			}
        		}
        		
        		
        		// Translate
        		if(new File(paginaEPUBChecker.path_TranslateFile).exists()) {
        			
        			try {
        				paginaEPUBChecker.epubcheck_translate = Boolean.valueOf(updateCheck.readFileAsString(paginaEPUBChecker.path_TranslateFile));
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			
        		} else if(paginaEPUBChecker.programLanguage.equals("german")) {
        			// Always auto-translate for "german"
        			paginaEPUBChecker.epubcheck_translate = true;
        			
        		} else {
        			// Never auto-translate for other languages
        			paginaEPUBChecker.epubcheck_translate = false;
        		}
        		
                opt_Translate.setSelected(paginaEPUBChecker.epubcheck_translate);
        		
				return null;
        	}
        };
        setOptionsWorker.execute();
        
    }
	
	
    
	
	/* ********************************************************************************************************** */
	
	public void actionPerformed(ActionEvent e) {
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "exit" menuItem
        if(e.getSource() == mnItem_Exit) {
        	
        	// close pagina EPUB-Checker
        	System.exit(0);
			
			
			
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
		
		// handle "File Choose" menuItem and Button
        } else if(e.getSource() == mnItem_Open || e.getSource() == btn_chooseEpubFile) {
        	
        	// better File-Chooser for Mac OS X
        	if(paginaEPUBChecker.os_name.equals("mac")) {
        		
        		FileDialog fd = new FileDialog(mainGUI.this, __("Please choose an EPUB file for validation"), FileDialog.LOAD);
        		System.setProperty("apple.awt.use-file-dialog-packages", "true");
				fd.setFilenameFilter(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith( ".epub" );
					}
				});
			    fd.setLocation(0, 0);
			    fd.setVisible(true);
				
			    if(fd.getFile() != null) {
				    File file = new File(fd.getDirectory() + System.getProperty("file.separator") + fd.getFile());
				    input_filePath.setText(file.getPath());
    	            
					paginaEPUBChecker.modeExp = false;
            		paginaEPUBChecker.epubcheck_File = file;
            		paginaEPUBChecker.epubcheck_Report = new paginaReport(file.getName());
            		
    	            paginaEPUBChecker.validate();
			    }
			    
			    
        	} else {

    			final JFileChooser fc = new JFileChooser();
    			fc.setName(__("Please choose an EPUB file for validation"));
    			fc.setAcceptAllFileFilterUsed(false);
    			fc.setFileFilter(new FileFilter() {
                    public boolean accept(File f) {
                        return f.getName().toLowerCase().endsWith(".epub") || f.isDirectory();
                    }
                    public String getDescription() {
                        return __("EPUB files (*.epub)");
                    }
                });
    			
    			int returnVal = fc.showOpenDialog(getComponent(0));
    			if (returnVal == JFileChooser.APPROVE_OPTION) {
    	            
    				File file = fc.getSelectedFile();
    	            input_filePath.setText(file.getPath());

					paginaEPUBChecker.modeExp = false;
            		paginaEPUBChecker.epubcheck_File = file;
            		paginaEPUBChecker.epubcheck_Report = new paginaReport(file.getName());
            		
    	            paginaEPUBChecker.validate();
    	        }
    			
        	}
        	
        	
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
		
		// handle "validate" button
        } else if(e.getSource() == btn_validateEpub) {
        	
            File file = new File(input_filePath.getText());
            
            // file doesn't exist
            if(!file.exists()) {
            	
            	txtarea_results.setText(__("EPUB file couldn't be found"));
            
            // file exists
            } else {

				paginaEPUBChecker.modeExp = false;
	    		paginaEPUBChecker.epubcheck_File = file;
	    		paginaEPUBChecker.epubcheck_Report = new paginaReport(file.getName());
	    		
	        	// validate the given file
	        	paginaEPUBChecker.validate();
            }
        	
        	
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "Save result" menuItem
        } else if(e.getSource() == mnItem_Save) {
        	
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(paginaEPUBChecker.epubcheck_File);
			fc.setSelectedFile(new File(paginaEPUBChecker.epubcheck_File.getAbsolutePath().replaceAll("\\.epub", "_log.txt")));
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
                }
                public String getDescription() {
                    return __("Text files (*.txt)");
                }
            });

			int returnVal = fc.showSaveDialog(getComponent(0));
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            
				File file = fc.getSelectedFile();
				
				// add .txt extension if mising
				if(!file.getName().toLowerCase().endsWith(".txt")) {
					file = new File(file.getAbsoluteFile() + ".txt");
				}
				
				saveLogfile(file);
	        }
        	
        	
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
		
		// handle "AutoSave" menuItem
        } else if(e.getSource() == opt_AutoSave) {
        	
        	updateCheck.writeStringToFile(paginaEPUBChecker.path_AutoSaveFile, String.valueOf(opt_AutoSave.isSelected()));
        	paginaEPUBChecker.AutoSave = opt_AutoSave.isSelected();
        	
        	
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
		
		// handle "Translate" menuItem
        } else if(e.getSource() == opt_Translate) {
        	
        	updateCheck.writeStringToFile(paginaEPUBChecker.path_TranslateFile, String.valueOf(opt_Translate.isSelected()));
        	paginaEPUBChecker.epubcheck_translate = opt_Translate.isSelected();
        	
            // start re-validating immediately if a file has been set yet
            validateImmediatelyIfFileIsSet();
			
			
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
		
		// handle "open website pagina"
        } else if(e.getSource() == mnItem_WebsitePagina) {
        	
        	new OpenURIinBrowser("http://bit.ly/1h7g4rn");
			
			
    		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
		
		// handle "open website epubcheck"
        } else if(e.getSource() == mnItem_WebsiteEpubcheck) {
        	
        	new OpenURIinBrowser("http://github.com/IDPF/epubcheck");
			
			
    		
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

        // handle "about" dialog
        } else if(e.getSource() == mnItem_About) {
        	
        	subGUI s = new subGUI(mainGUI.this);
        	s.displayAboutBox();
			
			
    		
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

        // handle "translation" dialog
        } else if(e.getSource() == mnItem_Translations) {
        	
        	subGUI s = new subGUI(mainGUI.this);
        	s.displayTranslationBox();



        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

        // handle "licence" dialog
        } else if(e.getSource() == mnItem_licenceInformation) {
        	
        	subGUI s = new subGUI(mainGUI.this);
        	s.displayLicenceBox();



        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

        // handle "update check" dialog
        } else if(e.getSource() == mnItem_Updates) {
        	
    		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
    			
    			@Override
    			protected Void doInBackground() throws Exception {

    		        new updateCheck(false);
    		        
    				return null;
    			}
    			
    			@Override
    			protected void done() {
    			}
    		};
    		worker.execute();
        }
    }
	
	
    
	
	/* ********************************************************************************************************** */
	
	public static void setBorderStateActive() {
    	txtarea_results.setBackground(new Color(255,255,215));
    	scroll_results.setBackground(new Color(255,255,215));
    	scroll_results.setBorder(new DashedLineBorder(new Color(255,153,0), 7));
    }	
	
	public static void setBorderStateNormal() {
    	txtarea_results.setBackground(new Color(255,255,245));
    	scroll_results.setBackground(new Color(255,255,245));
    	scroll_results.setBorder(new DashedLineBorder(Color.ORANGE, 7));
    }
	
	public static void setBorderStateError() {
    	txtarea_results.setBackground(new Color(255,230,230));
    	scroll_results.setBackground(new Color(255,230,230));
    	scroll_results.setBorder(new DashedLineBorder(Color.RED, 7));
    }
	
	
	
	
	/* ********************************************************************************************************** */
	
	private static void restartWithNewLanguage(String language) {
		
		// write new language string to file
    	updateCheck.writeStringToFile(paginaEPUBChecker.path_LanguageFile, String.valueOf(language));
    	
    	// set new language in mainClass so that the new Constructor can read this information
    	paginaEPUBChecker.programLanguage = language;
    	
    	// read and save dimensions of the current gui window
    	paginaEPUBChecker.MainGuiDimension = paginaEPUBChecker.gui.getSize();
    	
    	// read and save position of the current gui window
    	paginaEPUBChecker.MainGuiPosition = paginaEPUBChecker.gui.getLocation();
    	
    	// new GUI in given language
    	paginaEPUBChecker.loadAndInitGuiAndDependencies();
	}
	
	
	
	
	/* ********************************************************************************************************** */
	
	private static void validateImmediatelyIfFileIsSet() {
	    if(paginaEPUBChecker.epubcheck_File != null && paginaEPUBChecker.epubcheck_File.exists()) {

			paginaEPUBChecker.modeExp = false;
			paginaEPUBChecker.epubcheck_Report = new paginaReport(paginaEPUBChecker.epubcheck_File.getName());
			mainGUI.input_filePath.setText(paginaEPUBChecker.epubcheck_File.getPath());
			
	    	paginaEPUBChecker.validate();
	    }
	}
	
	
	
	
	/* ********************************************************************************************************** */
	
	public static void saveLogfile(File logfile) {
		
		mainGUI.txtarea_results.append("\n\n---------------------------------------------------");
		
        // save epubcheck results
        try{
        	
			// Create file
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile), "UTF-8"));
			
			// write text from textarea
			out.write(txtarea_results.getText());
			
			// close stream
			out.close();
			
			if(logfile.exists()) {
				txtarea_results.append("\n\n" + __("Test results were saved in a logfile") + ":\n" + logfile + "\n\n");
			} else {
				txtarea_results.append("\n\n" + __("An error occured! Logfile couldn't be saved!") + "\n" + logfile + "\n\n");
			}
			
        } catch (Exception e1) {
			txtarea_results.append("\n\n" + __("An error occured! Logfile couldn't be saved!") + "\n" + logfile + "\n\n");
        }
        
		// scroll to the end
		mainGUI.txtarea_results.setCaretPosition(mainGUI.txtarea_results.getText().length());
	}
	
	
	
	
	/* ********************************************************************************************************** */
	
	public int getIndex(String[] array, String specificValue){
		for(int i=0; i<array.length; i++){
			if(array[i].equals(specificValue)){
				return i;
			}
		}
		return -1;
	}
	
	
	
	
	/* ********************************************************************************************************** */
	
	private static String __(String s) {
		return paginaEPUBChecker.l10n.getString(s);
	}

}
