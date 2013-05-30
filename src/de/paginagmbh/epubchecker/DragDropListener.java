package de.paginagmbh.epubchecker;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.util.Archive;
import com.adobe.epubcheck.util.FeatureEnum;


/**
  * handles Drag'n'Drop events for the EPUB-Checker GUI
  * 
  * idea: http://blog.christoffer.me/2011/01/drag-and-dropping-files-to-java-desktop.html
  * 
  * @author		Tobias Fischer
  * @copyright	pagina GmbH, Tübingen
  * @version	1.2
  * @date 		2013-05-30
  * @lastEdit	Tobias Fischer
  */
// 
public class DragDropListener implements DropTargetListener {
	
	
	@Override
    public void drop(DropTargetDropEvent event) {
    	// System.out.println("Drop");
		
		
        // Accept copy drops
        event.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        
        // Drag&Drop for mac and windows
        if(!paginaEPUBChecker.os_name.equals("linux")) {
	        
        	// Get the data formats of the dropped item
	        DataFlavor[] flavors = transferable.getTransferDataFlavors();
	
	        // Loop through the flavors
	        for (DataFlavor flavor : flavors) {
	
	            try {
	
	                // If the drop items are files
	                if (flavor.isFlavorJavaFileListType()) {
	
	                    // Get all of the dropped files
	                	@SuppressWarnings("unchecked")
						List<File> files = (java.util.List<File>) transferable.getTransferData(flavor);
	                    
	                	handleDropedFiles(files);
	                	
	                }
	
	            } catch (Exception e) {
	
	                // Print out the error stack
	                e.printStackTrace();
	
	            }
	        }
	    
	        
	    // Drag&Drop for Linux
	    // http://stackoverflow.com/questions/811248/how-can-i-use-drag-and-drop-in-swing-to-get-file-path
        } else {

        	try {
        		DataFlavor nixFileDataFlavor = new DataFlavor("text/uri-list;class=java.lang.String");
        		String data = (String)transferable.getTransferData(nixFileDataFlavor);

        		
        		List<File> files = new ArrayList<File>();

        		for(StringTokenizer st = new StringTokenizer(data, "\r\n"); st.hasMoreTokens();) {

        			String token = st.nextToken().trim();
        			
        			if(token.startsWith("#") || token.isEmpty()) {
        				// comment line, by RFC 2483
        				continue;
        			}

        			try {
        				
        				File file = new File(new URI(token));
        				files.add(file);

        			} catch(Exception e) {
        				e.printStackTrace();
        			}
        		}
                
            	handleDropedFiles(files);

        	} catch (UnsupportedFlavorException e1) {
        		e1.printStackTrace();
        	} catch (IOException e1) {
        		e1.printStackTrace();
        	} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

        // Inform that the drop is complete
        event.dropComplete(true);

    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {
    	// System.out.println("Enter");
    	mainGUI.setBorderStateActive();
    	mainGUI.txtarea_results.setText(__("Yeah! Drop your EPUB right here!"));
    	
    }

    @Override
    public void dragExit(DropTargetEvent event) {
    	// System.out.println("Exit");
    	mainGUI.setBorderStateNormal();
    	mainGUI.txtarea_results.setText(__("Drag & Drop here to validate! Either an EPUB file or an expanded EPUB folder..."));
    }

    @Override
    public void dragOver(DropTargetDragEvent event) {
    	// System.out.println("Over");
    } 

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
    }
	
    
	
	
	/* ********************************************************************************************************** */
	
	public static void handleDropedFiles(List<File> files) {
		
		// If exactly 1 file was dropped
		if(files.size() == 1) { 
			for(int i=0; i<files.size(); i++) {
				
				File file = files.get(i);
				
				if(file.isFile() && file.getName().toLowerCase().endsWith(".epub")) {
					
					mainGUI.setBorderStateNormal();

					paginaEPUBChecker.modeExp = false;
					paginaEPUBChecker.epubcheck_File = file;
					paginaEPUBChecker.epubcheck_Report = new paginaReport(file.getName());
					
					// set file path in the file-path-input field
					mainGUI.input_filePath.setText(file.getPath());
					
					paginaEPUBChecker.validate();
					
					
				} else if(file.isDirectory()) {
					
					File expectedMimetype = new File(file.getPath() + File.separator + "mimetype");
					File expectedMetaInf = new File(file.getPath() + File.separator + "META-INF");
					
					if(expectedMimetype.exists() && expectedMimetype.isFile()
						&& expectedMetaInf.exists() && expectedMetaInf.isDirectory()) {
						
						paginaEPUBChecker.modeExp = true;
						paginaEPUBChecker.modeExp_keepArchive = true;
						
						Archive epub = new Archive(file.getPath(), paginaEPUBChecker.modeExp_keepArchive);
						
						paginaReport report = new paginaReport(epub.getEpubName());
						
		                report.info(null, FeatureEnum.TOOL_NAME, "epubcheck");
						report.info(null, FeatureEnum.TOOL_VERSION, EpubCheck.version());
						
						epub.createArchive();
						
						paginaEPUBChecker.modeExp_epub = epub;
						
						paginaEPUBChecker.epubcheck_File = epub.getEpubFile();
						paginaEPUBChecker.epubcheck_Report = report;
						
						// set file path in the file-path-input field
						mainGUI.input_filePath.setText(epub.getEpubFile().toString());
						
						paginaEPUBChecker.validate();
						
						
					} else {

						mainGUI.setBorderStateError();
						mainGUI.txtarea_results.setText(__("This folder doesn't seem to contain any valid EPUB structure") + ": " + file.getName() + "/");
						mainGUI.txtarea_results.append("\n\n" + __("There should be at least a folder named 'META-INF' and the 'mimetype' file..."));
					}
					
					
				} else {
					
					mainGUI.setBorderStateError();
					mainGUI.txtarea_results.setText(__("This isn't an EPUB file") + ": " + file.getName());
				}
				
			}
			
			// if multiple files were dropped
		} else {
			mainGUI.txtarea_results.setText(__("Sorry, but more than one file can't be validated at the same time!"));
		}
    }
	
    
	
	
	/* ********************************************************************************************************** */
	
	private static String __(String s) {
		return paginaEPUBChecker.l10n.getString(s);
	}

}
