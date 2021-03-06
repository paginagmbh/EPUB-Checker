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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.messages.Severity;

import de.paginagmbh.common.gui.DashedLineBorder;
import de.paginagmbh.common.gui.StatusBar;
import de.paginagmbh.common.internet.OpenURIinBrowser;
import de.paginagmbh.epubchecker.GuiManager.ExpandedSaveMode;
import de.paginagmbh.epubchecker.GuiManager.LogViewMode;



/**
 * loads the main window of the EPUB-Checker
 *
 * @author      Tobias Fischer
 * @copyright   pagina GmbH, Tübingen
 * @date        2019-01-22
 */
public class MainGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = -9097011004038447484L;
	private static GuiManager guiManager;
	private JTextField input_filePath;
	private JTextArea txtarea_results;
	private DefaultTableModel tableModel;
	private JScrollPane scroll_results;
	private JTable table_results;
	private JLabel lbl_test;
	private JButton btn_validateEpub;
	private JButton btn_chooseEpubFile;
	private JMenuItem mnItem_Open;
	private JMenuItem mnItem_Save;
	private JMenuItem mnItem_Exit;
	private JMenuItem mnItem_About;
	private JMenuItem mnItem_Translations;
	private JMenuItem mnItem_licenceInformation;
	private JMenuItem mnItem_WebsiteEpubcheck;
	private JMenuItem mnItem_WebsitePagina;
	private JMenuItem mnItem_Updates;
	private JMenu mn_File, mn_Expanded, mn_Log, mn_Language, mn_Help;
	private JRadioButtonMenuItem opt_ViewMode_Text, opt_ViewMode_Table;
	private JRadioButtonMenuItem opt_ExpandedSave_Never, opt_ExpandedSave_Valid, opt_ExpandedSave_Always;
	private ButtonGroup AutoSaveFromExpandedGroup;
	private JCheckBoxMenuItem opt_AutoSaveLogfile;
	private StatusBar statusBar;
	private String currentLogMessages = "";



	/* ********************************************************************************************************** */

	public MainGUI() {

		// new JFrame
		super("pagina EPUB-Checker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		guiManager = GuiManager.getInstance();

		// set IconList with different sizes
		setIconImages(FileManager.logoIcons);

		// set Apple specific system properties
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "pagina EPUB-Checker");
		System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
		System.setProperty("com.apple.mrj.application.live-resize", "true");

		// set window size
		if(guiManager.getMainGuiDimension() == null) {
			setSize(775,650);
		} else {
			setSize(guiManager.getMainGuiDimension());
		}


		// set minimum size
		setMinimumSize(new Dimension(650,500));


		// set window position
		if(guiManager.getMainGuiPosition() == null) {
			setLocation(50, 50);
		} else {
			setLocation(guiManager.getMainGuiPosition());
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

		// Fill path from GuiManager if possible
		// e.g. in case of a language switch
		if(guiManager.getCurrentFile() != null && guiManager.getCurrentFile().exists()) {
			input_filePath.setText(guiManager.getCurrentFile().getAbsolutePath());
		}

		// Key listener listening to the filepath input field and changing the validate buttons status
		KeyListener keyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) { }
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
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

		JLabel lbl_epubcheckVersion = new JLabel("(" + String.format("EPUBCheck %1$1s", EpubCheck.version()) + ")");
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

		GridBagConstraints gbc_txtarea_results = new GridBagConstraints();
		gbc_txtarea_results.insets = new Insets(0, 0, 5, 5);
		gbc_txtarea_results.fill = GridBagConstraints.BOTH;
		gbc_txtarea_results.gridwidth = 3;
		gbc_txtarea_results.gridx = 1;
		gbc_txtarea_results.gridy = 6;




		tableModel = new DefaultTableModel();
		tableModel.addColumn(__("Severity"));
		tableModel.addColumn(__("Code"));
		tableModel.addColumn(__("File (line,col)"));
		tableModel.addColumn(__("Message"));

		table_results = new JTable(tableModel){
			private static final long serialVersionUID = -4430174981226468686L;

			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
		};

		table_results.setAutoCreateRowSorter(true);
		table_results.getTableHeader().setReorderingAllowed(false);
		table_results.setFillsViewportHeight(true);
		table_results.setOpaque(true);
		table_results.setRowHeight(25);
		table_results.setShowGrid(false);
		table_results.setIntercellSpacing(new Dimension(0, 0));
		table_results.setRowMargin(0);
		table_results.setDropMode(DropMode.INSERT);
		//table_results.getTableHeader().setPreferredSize(new Dimension(-1,25));

		table_results.getColumnModel().getColumn(0).setResizable(false);
		table_results.getColumnModel().getColumn(1).setResizable(false);

		table_results.getColumnModel().getColumn(0).setMaxWidth(100);
		table_results.getColumnModel().getColumn(0).setMinWidth(100);
		table_results.getColumnModel().getColumn(1).setMaxWidth(100);
		table_results.getColumnModel().getColumn(1).setMinWidth(100);
		table_results.getColumnModel().getColumn(2).setMinWidth(130);
		table_results.getColumnModel().getColumn(2).setPreferredWidth(130);
		table_results.getColumnModel().getColumn(3).setMinWidth(270);
		table_results.getColumnModel().getColumn(3).setPreferredWidth(270);

		table_results.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		table_results.getColumnModel().getColumn(0).setCellRenderer(new IconTableCellRenderer());
		table_results.getColumnModel().getColumn(1).setCellRenderer(new BoardTableCellRenderer());
		table_results.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());
		table_results.getColumnModel().getColumn(3).setCellRenderer(new MultiLineCellRenderer());



		scroll_results = new JScrollPane();
		scroll_results.setViewportView(table_results);
		setBorderStateNormal();
		GridBagConstraints gbc_scroll_results = new GridBagConstraints();
		gbc_scroll_results.insets = new Insets(0, 0, 5, 5);
		gbc_scroll_results.fill = GridBagConstraints.BOTH;
		gbc_scroll_results.gridwidth = 3;
		gbc_scroll_results.gridx = 1;
		gbc_scroll_results.gridy = 6;
		main.add(scroll_results, gbc_scroll_results);



		// Create the drag and drop listener
		DragDropListener dragDropListener = new DragDropListener();
		// Connect the label with a drag and drop listener
		new DropTarget(table_results, dragDropListener);
		new DropTarget(txtarea_results, dragDropListener);



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
		if(FileManager.os_name.equals("mac")) {
			mnItem_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_MASK));
		} else {
			mnItem_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		}
		mnItem_Open.addActionListener(this);
		mn_File.add(mnItem_Open);

		// not a mac system
		if(!FileManager.os_name.equals("mac")) {

			mn_File.addSeparator();

			mnItem_Exit = new JMenuItem(__("Exit"));
			if(FileManager.os_name.equals("windows")) {
				mnItem_Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
			} else {
				mnItem_Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
			}
			mnItem_Exit.addActionListener(this);
			mn_File.add(mnItem_Exit);
		}


		mn_Expanded = new JMenu(__("Expanded"));
		menuBar.add(mn_Expanded);

		AutoSaveFromExpandedGroup = new ButtonGroup();

		opt_ExpandedSave_Never = new JRadioButtonMenuItem(__("ExpandedSave_Never"));
		opt_ExpandedSave_Never.addActionListener(this);
		mn_Expanded.add(opt_ExpandedSave_Never);
		AutoSaveFromExpandedGroup.add(opt_ExpandedSave_Never);

		opt_ExpandedSave_Valid = new JRadioButtonMenuItem(__("ExpandedSave_Valid"));
		opt_ExpandedSave_Valid.addActionListener(this);
		opt_ExpandedSave_Valid.setSelected(true);
		mn_Expanded.add(opt_ExpandedSave_Valid);
		AutoSaveFromExpandedGroup.add(opt_ExpandedSave_Valid);

		opt_ExpandedSave_Always = new JRadioButtonMenuItem(__("ExpandedSave_Always"));
		opt_ExpandedSave_Always.addActionListener(this);
		mn_Expanded.add(opt_ExpandedSave_Always);
		AutoSaveFromExpandedGroup.add(opt_ExpandedSave_Always);


		mn_Log = new JMenu(__("Logfile"));
		menuBar.add(mn_Log);

		mnItem_Save = new JMenuItem(__("Save logfile"));
		if(FileManager.os_name.equals("mac")) {
			mnItem_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.META_MASK));
		} else {
			mnItem_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		}
		mnItem_Save.setEnabled(false);
		mnItem_Save.addActionListener(this);
		mn_Log.add(mnItem_Save);

		opt_AutoSaveLogfile = new JCheckBoxMenuItem(__("Auto Save"));
		opt_AutoSaveLogfile.addActionListener(this);
		mn_Log.add(opt_AutoSaveLogfile);

		mn_Log.addSeparator();

		opt_ViewMode_Text = new JRadioButtonMenuItem(__("Text View"));
		opt_ViewMode_Text.addActionListener(this);
		opt_ViewMode_Text.setSelected(false);
		mn_Log.add(opt_ViewMode_Text);

		opt_ViewMode_Table = new JRadioButtonMenuItem(__("Table View"));
		opt_ViewMode_Table.addActionListener(this);
		opt_ViewMode_Table.setSelected(true);
		mn_Log.add(opt_ViewMode_Table);



		mn_Language = new JMenu(__("Language"));
		menuBar.add(mn_Language);


		// init the AvailableLanguage/-button arrays
		final Map<Locale, String> availableLanguages = guiManager.getCurrentLocalizationObject().getAvailableLanguages();
		final Map<String, Locale> availableLanguagesTranslated = new HashMap<>();

		// iterate over the available langauges
		for (Map.Entry<Locale, String> entry : availableLanguages.entrySet()) {
			Locale locale = entry.getKey();
		    String localeName = entry.getValue();

			// add the translated language string to the "original" array
			// later on we need the translated string to determine which language was clicked
		    availableLanguagesTranslated.put(__(localeName), locale);

			// make AWT RadioButton
		    JRadioButtonMenuItem item = new JRadioButtonMenuItem(__(localeName));
			// select RadioButton if language equals
			if(guiManager.getCurrentLocale().equals(locale)) { item.setSelected(true); }
			// add RadioButton to "language" menu item
			mn_Language.add(item);
			// add actionListener
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// get index of Menu Label in Array "availableLanguagesOriginal"
					Locale selectedLocale = availableLanguagesTranslated.get(e.paramString().split(",")[1].replaceAll("cmd=", ""));
					// get english language string of given index in array "availableLanguages"
					restartWithNewLocale(selectedLocale);
				}
			});
		}


		mn_Help = new JMenu(__("Help"));
		menuBar.add(mn_Help);

		mnItem_About = new JMenuItem(__("About"));
		mnItem_About.addActionListener(this);
		mn_Help.add(mnItem_About);

		mnItem_Translations = new JMenuItem(__("Translations"));
		mnItem_Translations.addActionListener(this);
		mn_Help.add(mnItem_Translations);

		mnItem_licenceInformation = new JMenuItem(__("License information"));
		mnItem_licenceInformation.addActionListener(this);
		mn_Help.add(mnItem_licenceInformation);

		mn_Help.addSeparator();

		mnItem_WebsiteEpubcheck = new JMenuItem(__("Visit W3C EPUBCheck website"));
		mnItem_WebsiteEpubcheck.addActionListener(this);
		mn_Help.add(mnItem_WebsiteEpubcheck);

		mnItem_WebsitePagina = new JMenuItem(__("Visit pagina EPUB-Checker website"));
		mnItem_WebsitePagina.addActionListener(this);
		mn_Help.add(mnItem_WebsitePagina);

		mn_Help.addSeparator();

		mnItem_Updates = new JMenuItem(__("Check for updates..."));
		mnItem_Updates.addActionListener(this);
		mn_Help.add(mnItem_Updates);




		// Swing Worker which reads and sets the "autoSave" and "translate" options in a background task
		SwingWorker<Void, Void> setOptionsWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {

				// ExpandedSaveMode
				if(new File(FileManager.path_ExpandedSaveFile).exists()) {
					try {
						String expandedSaveState = StringHelper.readFileAsString(FileManager.path_ExpandedSaveFile);
						if(expandedSaveState.equals("never")) {
							guiManager.setExpandedSave(ExpandedSaveMode.NEVER);
						} else if(expandedSaveState.equals("all")) {
							guiManager.setExpandedSave(ExpandedSaveMode.ALWAYS);
						} else {
							guiManager.setExpandedSave(ExpandedSaveMode.VALID);
						}
					} catch (IOException e) {
						guiManager.setExpandedSave(ExpandedSaveMode.VALID);
						e.printStackTrace();
					}

					switch (guiManager.getExpandedSave()) {
					case ALWAYS:
						opt_ExpandedSave_Always.setSelected(true);
						break;
					case NEVER:
						opt_ExpandedSave_Never.setSelected(true);
						break;
					default:
						opt_ExpandedSave_Valid.setSelected(true);
						break;
					}
				}

				// AutoSave
				if(new File(FileManager.path_AutoSaveFile).exists()) {

					try {
						guiManager.setMenuOptionAutoSaveLogfile(Boolean.valueOf(StringHelper.readFileAsString(FileManager.path_AutoSaveFile)));
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(guiManager.getMenuOptionAutoSaveLogfile()) {
						opt_AutoSaveLogfile.setSelected(true);
					}
				}


				// LogViewMode
				if(new File(FileManager.path_LogViewFile).exists()) {

					try {
						if(StringHelper.readFileAsString(FileManager.path_LogViewFile).equals("text")) {
							guiManager.setLogView(LogViewMode.TEXT);
							scroll_results.setViewportView(txtarea_results);
						} else {
							guiManager.setLogView(LogViewMode.TABLE);
						}
					} catch (IOException e) {
						guiManager.setLogView(LogViewMode.TABLE);
						e.printStackTrace();
					}

					if(guiManager.getLogView() == LogViewMode.TEXT) {
						opt_ViewMode_Table.setSelected(false);
						opt_ViewMode_Text.setSelected(true);
					}
				}


				// add initial rag & Drop message
				clearLog();
				addLogMessage(__("Drag & Drop here to validate! Either an EPUB file or an expanded EPUB folder..."));


				return null;
			}
		};
		setOptionsWorker.execute();



		// show GUI
		setVisible(true);
	}




	/* ********************************************************************************************************** */

	@Override
	public void actionPerformed(ActionEvent e) {

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "exit" menuItem
		if(e.getSource() == mnItem_Exit) {

			// close pagina EPUB-Checker
			System.exit(0);



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "File Choose" menuItem and Button
		} else if(e.getSource() == mnItem_Open || e.getSource() == btn_chooseEpubFile) {

			// better File-Chooser for Mac OS X and Linux
			if(FileManager.os_name.matches("mac|linux")) {

				FileDialog fd = new FileDialog(MainGUI.this, __("Please choose an EPUB file for validation"), FileDialog.LOAD);
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
					File file = new File(fd.getDirectory() + File.separator + fd.getFile());
					// validate EPUB file
					new EpubValidator().validate(file);
				}


			} else {

				final JFileChooser fc = new JFileChooser();
				fc.setName(__("Please choose an EPUB file for validation"));
				fc.setAcceptAllFileFilterUsed(false);
				fc.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.getName().toLowerCase().endsWith(".epub") || f.isDirectory();
					}
					@Override
					public String getDescription() {
						return __("EPUB files (*.epub)");
					}
				});

				int returnVal = fc.showOpenDialog(getComponent(0));
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// validate EPUB file
					new EpubValidator().validate(file);
				}

			}



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "validate" button
		} else if(e.getSource() == btn_validateEpub) {
			File file = new File(input_filePath.getText());

			// Check whether file exists
			if(file.exists()) {
				// validate EPUB file
				new EpubValidator().validate(file);
			} else {
				addLogMessage(Severity.FATAL, __("EPUB file couldn't be found"));
			}



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "Save result" menuItem
		} else if(e.getSource() == mnItem_Save) {

			// better File-Chooser for Mac OS X and Linux
			if(FileManager.os_name.matches("mac|linux")) {

				FileDialog fd = new FileDialog(MainGUI.this, __("Save logfile"), FileDialog.SAVE);
				System.setProperty("apple.awt.use-file-dialog-packages", "true");
				fd.setFilenameFilter(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith( ".txt" );
					}
				});
				fd.setLocation(0, 0);
				fd.setDirectory(guiManager.getCurrentFile().getAbsolutePath());
				fd.setFile(guiManager.getCurrentFile().getName().replaceAll("\\.epub", "_log.txt"));
				fd.setVisible(true);

				if(fd.getFile() != null) {
					File file = new File(fd.getDirectory() + File.separator + fd.getFile());

					// add .txt extension if mising
					if(!file.getName().toLowerCase().endsWith(".txt")) {
						file = new File(file.getAbsoluteFile() + ".txt");
					}

					saveLogfile(file);
				}

			} else {

				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(guiManager.getCurrentFile());
				fc.setSelectedFile(new File(guiManager.getCurrentFile().getAbsolutePath().replaceAll("\\.epub", "_log.txt")));
				fc.setAcceptAllFileFilterUsed(false);
				fc.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
					}
					@Override
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
			}



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "ExpandedSave" menuItem
		} else if(e.getSource() == opt_ExpandedSave_Never || e.getSource() == opt_ExpandedSave_Valid || e.getSource() == opt_ExpandedSave_Always) {

			if(e.getSource() == opt_ExpandedSave_Never) {
				guiManager.setExpandedSave(ExpandedSaveMode.NEVER);
			} else if(e.getSource() == opt_ExpandedSave_Always) {
				guiManager.setExpandedSave(ExpandedSaveMode.ALWAYS);
			} else {
				guiManager.setExpandedSave(ExpandedSaveMode.VALID);
			}

			StringHelper.writeStringToFile(FileManager.path_ExpandedSaveFile, guiManager.getExpandedSave().toString().toLowerCase());



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "AutoSave" menuItem
		} else if(e.getSource() == opt_AutoSaveLogfile) {

			StringHelper.writeStringToFile(FileManager.path_AutoSaveFile, String.valueOf(opt_AutoSaveLogfile.isSelected()));
			guiManager.setMenuOptionAutoSaveLogfile(opt_AutoSaveLogfile.isSelected());



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "ViewMode" menuItem
		} else if(e.getSource() == opt_ViewMode_Table || e.getSource() == opt_ViewMode_Text) {

			if((e.getSource() == opt_ViewMode_Table && guiManager.getLogView() == LogViewMode.TABLE)
					|| (e.getSource() == opt_ViewMode_Text && guiManager.getLogView() == LogViewMode.TEXT) ) {
				// re-select the item because the change-listener has already de-selected on-click
				((JRadioButtonMenuItem)e.getSource()).setSelected(true);
				// do not reload the view
				return;
			}

			if(e.getSource() == opt_ViewMode_Text) {
				guiManager.setLogView(LogViewMode.TEXT);
			} else {
				guiManager.setLogView(LogViewMode.TABLE);
			}

			StringHelper.writeStringToFile(FileManager.path_LogViewFile, guiManager.getLogView().toString().toLowerCase());

			// start re-validating immediately if a file has been set yet
			saveGuiSettingsAndReloadGui();



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "open website pagina"
		} else if(e.getSource() == mnItem_WebsitePagina) {

			new OpenURIinBrowser("https://bit.ly/1h7g4rn");



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "open website epubcheck"
		} else if(e.getSource() == mnItem_WebsiteEpubcheck) {

			new OpenURIinBrowser("https://github.com/w3c/epubcheck");



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "about" dialog
		} else if(e.getSource() == mnItem_About) {

			SubGUI s = new SubGUI(MainGUI.this);
			s.displayAboutBox();



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "translation" dialog
		} else if(e.getSource() == mnItem_Translations) {

			SubGUI s = new SubGUI(MainGUI.this);
			s.displayTranslationBox();



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "licence" dialog
		} else if(e.getSource() == mnItem_licenceInformation) {

			SubGUI s = new SubGUI(MainGUI.this);
			s.displayLicenceBox();



		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

		// handle "update check" dialog
		} else if(e.getSource() == mnItem_Updates) {

			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {

					new UpdateCheck(false);

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

	private void setLogComponentsBackgroundColor(Color color) {
		txtarea_results.setBackground(color);
		table_results.setBackground(color);
		scroll_results.setBackground(color);
	}

	public void setBorderStateActive() {
		setLogComponentsBackgroundColor(new Color(255,255,215));
		scroll_results.setBorder(new DashedLineBorder(new Color(255,153,0), 7));
	}

	public void setBorderStateNormal() {
		setLogComponentsBackgroundColor(new Color(255,255,245));
		scroll_results.setBorder(new DashedLineBorder(Color.ORANGE, 7));
	}

	public void setBorderStateError() {
		setLogComponentsBackgroundColor(new Color(255,230,230));
		scroll_results.setBorder(new DashedLineBorder(Color.RED, 7));
	}

	public void setBorderStateWarning() {
		setLogComponentsBackgroundColor(new Color(255,240,230));
		scroll_results.setBorder(new DashedLineBorder(new Color(255,102,0), 7));
	}

	public void setBorderStateValid() {
		setLogComponentsBackgroundColor(new Color(235,247,235));
		scroll_results.setBorder(new DashedLineBorder(new Color(51,173,51), 7));
	}




	/* ********************************************************************************************************** */

	private void restartWithNewLocale(Locale newLocale) {
		// write new language string to file
		StringHelper.writeStringToFile(FileManager.path_LanguageFile, newLocale.toString());

		// set new language in mainClass so that the new Constructor can read this information
		guiManager.setCurrentLocale(newLocale);

		saveGuiSettingsAndReloadGui();
	}




	/* ********************************************************************************************************** */

	private void saveGuiSettingsAndReloadGui() {

		// read and save dimensions of the current gui window
		guiManager.setMainGuiDimension(getSize());

		// read and save position of the current gui window
		guiManager.setMainGuiPosition(getLocation());

		// new GUI in given language
		new PaginaEPUBChecker(null);
	}




	/* ********************************************************************************************************** */

	public void saveLogfile(File logfile) {

		// save epubcheck results
		try{

			// Write currentLogMessages to log file
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile), "UTF-8"));
			out.write(currentLogMessages);
			out.close();

			// add separator in text log
			addLogMessageToTextLog("\n\n---------------------------------------------------");

			if(logfile.exists()) {
				addLogMessage("\n\n" + __("Test results were saved in a logfile") + ":\n" + logfile + "\n\n");
			} else {
				addLogMessage(Severity.WARNING, "\n\n" + __("An error occured! Logfile couldn't be saved!") + "\n" + logfile + "\n\n");
			}

		} catch (Exception e1) {
			addLogMessageToTextLog("\n\n---------------------------------------------------");
			addLogMessage(Severity.WARNING, "\n\n" + __("An error occured! Logfile couldn't be saved!") + "\n" + logfile + "\n\n");
		}

		// scroll to the end
		scrollToBottom();
	}




	/* ********************************************************************************************************** */

	public void scrollToBottom() {
		if(guiManager.getLogView() == LogViewMode.TEXT) {
			txtarea_results.setCaretPosition(txtarea_results.getText().length());
		} else {
			table_results.scrollRectToVisible(table_results.getCellRect(table_results.getRowCount()-1, 0, true));
		}
	}




	/* ********************************************************************************************************** */

	public void clearLog() {
		if(guiManager.getLogView() == LogViewMode.TEXT) {
			txtarea_results.setText("");
		} else {
			while(tableModel.getRowCount() > 0) {
				tableModel.removeRow(0);
			}
		}
		currentLogMessages = "";
	}




	/* ********************************************************************************************************** */

	public void addLogMessage(String message) {
		addLogMessage(Severity.INFO, message);
	}

	public void addLogMessage(Severity severity, String message) {
		addLogMessage(message, new Object[]{severity, "", "", message.trim()});
	}

	public void addLogMessage(String message, Object[] tableLogObject) {
		// for LogViewMode.TEXT and for log file in TABLE mode
		addLogMessageToTextLog(message);

		if(guiManager.getLogView() == LogViewMode.TABLE) {
			// remove leading and trailing line breaks in table log message
			tableModel.addRow(tableLogObject);
		}
	}

	public void addLogMessageToTextLog(String message) {
		if(guiManager.getLogView() == LogViewMode.TEXT) {
			txtarea_results.append(message);
		}
		currentLogMessages += message;
	}

	public void insertLogMessageAtFirstPosition(Severity severity, String message) {
		if(guiManager.getLogView() == LogViewMode.TEXT) {
			txtarea_results.insert(message, 0);
		} else {
			tableModel.insertRow(0, new Object[]{severity, "", "", message.trim()});
		}
		currentLogMessages = message + currentLogMessages;
	}




	/* ********************************************************************************************************** */

	public StatusBar getStatusBar() {
		return statusBar;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JTextArea getTextArea() {
		return txtarea_results;
	}

	public JTextField getPathInputField() {
		return input_filePath;
	}

	public void disableButtonsDuringValidation() {
		btn_validateEpub.setEnabled(false);
		mnItem_Save.setEnabled(false);
		btn_chooseEpubFile.setEnabled(false);
	}
	public void enableButtonsAfterValidation() {
		btn_validateEpub.setEnabled(true);
		mnItem_Save.setEnabled(true);
		btn_chooseEpubFile.setEnabled(true);
	}




	/* ********************************************************************************************************** */

	private String __(String s) {
		return GuiManager.getInstance().getCurrentLocalizationObject().getString(s);
	}

}
