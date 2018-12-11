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



/**
 * handles Drag'n'Drop events for the EPUB-Checker GUI
 *
 * idea: http://blog.christoffer.me/post/2011-01-09-drag-and-dropping-files-to-java-desktop-application/
 *
 * @author      Tobias Fischer
 * @copyright   pagina GmbH, Tübingen
 * @version     1.3.0
 * @date        2016-12-14
 */
public class DragDropListener implements DropTargetListener {


	@Override
	public void drop(DropTargetDropEvent event) {
		// System.out.println("Drop");


		// Accept copy drops
		event.acceptDrop(DnDConstants.ACTION_COPY);

		// Get the transfer which can provide the dropped item data
		Transferable transferable = event.getTransferable();


		// Drag&Drop for mac and windows
		if(!FileManager.os_name.equals("linux")) {

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

						new EpubValidator().validate(files);

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


				List<File> files = new ArrayList<>();

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

				new EpubValidator().validate(files);

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
		MainGUI gui = GuiManager.getInstance().getCurrentGUI();
		// System.out.println("Enter");
		gui.setBorderStateActive();
		gui.clearLog();
		gui.addLogMessage(__("Yeah! Drop your EPUB right here!"));

	}

	@Override
	public void dragExit(DropTargetEvent event) {
		MainGUI gui = GuiManager.getInstance().getCurrentGUI();
		// System.out.println("Exit");
		gui.setBorderStateNormal();
		gui.clearLog();
		gui.addLogMessage(__("Drag & Drop here to validate! Either an EPUB file or an expanded EPUB folder..."));
	}

	@Override
	public void dragOver(DropTargetDragEvent event) {
		// System.out.println("Over");
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent event) {
	}




	/* ********************************************************************************************************** */

	private String __(String s) {
		return GuiManager.getInstance().getCurrentLocalizationObject().getString(s);
	}

}
